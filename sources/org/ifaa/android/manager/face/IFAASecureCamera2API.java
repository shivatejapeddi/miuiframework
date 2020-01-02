package org.ifaa.android.manager.face;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.OutputConfiguration;
import android.os.AnrMonitor;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.TimedRemoteCaller;
import android.view.Surface;
import android.view.SurfaceView;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.qualcomm.qti.seccamapi.SecCamServiceClient;
import com.android.qualcomm.qti.seccamapi.SecureCamera2Surface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class IFAASecureCamera2API {
    private static final int SESSION_REGULAR = 0;
    private static final int SESSION_SECCAM = 33014;
    private static final String TAG = "IFAASecureCamera2API";
    private static boolean is_2dfa = true;
    private static Context mContext = null;
    private final int DEFAULT_REGION_WEIGHT = 30;
    private Handler backgroundHandler_;
    private HandlerThread backgroundThread_;
    protected CameraDevice cameraDevice_ = null;
    private Integer cameraId_;
    protected Semaphore cameraOpenCloseSemaphore_ = new Semaphore(1);
    protected CountDownLatch cameraStartedLatch_ = new CountDownLatch(1);
    protected boolean cameraStarted_ = false;
    protected CountDownLatch cameraStopedLatch_ = new CountDownLatch(1);
    protected Builder captureBuilder_ = null;
    protected CaptureCallback captureCallback_ = null;
    protected CountDownLatch captureSessionClosedLatch_ = new CountDownLatch(1);
    protected CameraCaptureSession captureSession_ = null;
    private Integer format_ = Integer.valueOf(35);
    private Integer height_ = Integer.valueOf(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH);
    private IFAAFaceManagerImpl ifaafacemgrimpl_ = null;
    protected final StateCallback mStateCallback = new StateCallback() {
        public void onOpened(CameraDevice cameraDevice) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::CameraDevice.StateCallback::onOpened - Camera #");
            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
            IFAASecureCamera2API iFAASecureCamera2API = IFAASecureCamera2API.this;
            iFAASecureCamera2API.cameraDevice_ = cameraDevice;
            iFAASecureCamera2API.createCameraSessionCommon();
        }

        public void onDisconnected(CameraDevice cameraDevice) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::CameraDevice.StateCallback::onDisconnected - Camera #");
            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
            cameraDevice.close();
        }

        public void onError(CameraDevice cameraDevice, int error) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::CameraDevice.StateCallback::onError - Camera #");
            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
            stringBuilder.append(" - Error: ");
            stringBuilder.append(error);
            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
            cameraDevice.close();
        }

        public void onClosed(CameraDevice cameraDevice) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::CameraDevice.StateCallback::onClosed - Camera #");
            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
            IFAASecureCamera2API iFAASecureCamera2API = IFAASecureCamera2API.this;
            iFAASecureCamera2API.cameraDevice_ = null;
            iFAASecureCamera2API.cameraStartedLatch_.countDown();
            IFAASecureCamera2API.this.cameraStopedLatch_.countDown();
        }
    };
    private Integer numOfBuffers_ = Integer.valueOf(2);
    private Integer previewRotation_ = Integer.valueOf(0);
    private Integer previewSurfaceNumOfBuffers_ = Integer.valueOf(3);
    private SurfaceView previewSurfaceView_ = null;
    protected SecureCamera2Surface secureCamera2Surface_ = null;
    protected CountDownLatch surfaceReadyLatch_ = new CountDownLatch(1);
    private Integer width_ = Integer.valueOf(1280);

    public IFAASecureCamera2API(Context context, IFAAFaceManagerImpl ifaafacemgrimpl) {
        mContext = context;
        this.ifaafacemgrimpl_ = ifaafacemgrimpl;
    }

    /* Access modifiers changed, original: protected */
    public void startBackgroundThread() {
        this.backgroundThread_ = new HandlerThread("CameraBackground");
        this.backgroundThread_.start();
        this.backgroundHandler_ = new Handler(this.backgroundThread_.getLooper());
    }

    /* Access modifiers changed, original: protected */
    public void stopBackgroundThread() {
        HandlerThread handlerThread = this.backgroundThread_;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            try {
                this.backgroundThread_.join(AnrMonitor.MESSAGE_EXECUTION_TIMEOUT);
                this.backgroundThread_ = null;
                this.backgroundHandler_ = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Handler getBackgroundHandler() {
        return this.backgroundHandler_;
    }

    /* Access modifiers changed, original: protected */
    public Boolean isSupportAERegion() {
        String str = TAG;
        try {
            Integer regionCount = (Integer) ((CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE)).getCameraCharacteristics(Integer.toString(this.cameraId_.intValue())).get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE);
            if ((regionCount != null ? regionCount.intValue() : 0) > 0) {
                return Boolean.TRUE;
            }
            Log.d(str, "Face AE: don't have AE region capability.");
            return Boolean.FALSE;
        } catch (CameraAccessException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isSupportAERegion - Camera - ERROR: ");
            stringBuilder.append(e);
            Log.d(str, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public Rect getActivityArray() {
        String str = TAG;
        try {
            Rect activeArray = (Rect) ((CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE)).getCameraCharacteristics(Integer.toString(this.cameraId_.intValue())).get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            Rect activeArraySize = activeArray != null ? activeArray : new Rect(0, 0, 0, 0);
            if (activeArraySize.width() != 0) {
                if (activeArraySize.height() != 0) {
                    return activeArraySize;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Face AE: error active array size:");
            stringBuilder.append(activeArraySize.width());
            stringBuilder.append("x");
            stringBuilder.append(activeArraySize.height());
            Log.d(str, stringBuilder.toString());
            return null;
        } catch (CameraAccessException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getActivityArray - Camera - ERROR: ");
            stringBuilder2.append(e);
            Log.d(str, stringBuilder2.toString());
            return null;
        }
    }

    public void rectFromTEECallback(Point topLeft, Point rightBottom) {
        Point point = topLeft;
        Point point2 = rightBottom;
        if (this.captureSession_ != null && isSupportAERegion() != Boolean.FALSE && (point.x != 0 || point.y != 0 || point2.x != 0 || point2.y != 0)) {
            Rect activeArray = getActivityArray();
            if (activeArray != null) {
                int width;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Face AE: getting rect topLeft:");
                stringBuilder.append(point.x);
                String str = ",";
                stringBuilder.append(str);
                stringBuilder.append(point.y);
                stringBuilder.append(", rightBottom:");
                stringBuilder.append(point2.x);
                stringBuilder.append(str);
                stringBuilder.append(point2.y);
                String stringBuilder2 = stringBuilder.toString();
                str = TAG;
                Log.d(str, stringBuilder2);
                stringBuilder = new StringBuilder();
                stringBuilder.append("Face AE: arraySize:");
                stringBuilder.append(activeArray.width());
                String str2 = "x";
                stringBuilder.append(str2);
                stringBuilder.append(activeArray.height());
                Log.d(str, stringBuilder.toString());
                float scaleRatioX = (float) (activeArray.width() / 640);
                float scaleRatioY = (float) (activeArray.height() / 480);
                int width2 = (int) (((float) Math.abs(point2.y - point.y)) * scaleRatioY);
                int height = (int) (((float) Math.abs(point2.x - point.x)) * scaleRatioX);
                point.y = point.x;
                point.x = 640 - point2.y;
                point.x = (int) (((float) point.x) * scaleRatioY);
                point.y = (int) (((float) point.y) * scaleRatioX);
                if (point.x < 0) {
                    point.x = 0;
                }
                if (point.y < 0) {
                    point.y = 0;
                }
                if (point.x + width2 > activeArray.width() - 1) {
                    width = (activeArray.width() - point.x) - 1;
                } else {
                    width = width2;
                }
                if (point.y + height > activeArray.height() - 1) {
                    height = (activeArray.height() - point.y) - 1;
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Face AE: metering region topLeft=");
                stringBuilder3.append(point.x);
                stringBuilder3.append(str2);
                stringBuilder3.append(point.y);
                stringBuilder3.append(" widthxheight=");
                stringBuilder3.append(width);
                stringBuilder3.append(str2);
                stringBuilder3.append(height);
                Log.d(str, stringBuilder3.toString());
                this.captureBuilder_.set(CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[]{new MeteringRectangle(point.x, point.y, width, height, 30)});
                try {
                    this.captureSession_.setRepeatingRequest(this.captureBuilder_.build(), this.captureCallback_, getBackgroundHandler());
                } catch (CameraAccessException e) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("SecureCamera2API::CameraCaptureSession.StateCallback:onConfigured - Camera #");
                    stringBuilder4.append(this.cameraId_);
                    stringBuilder4.append(" - setRepeatingRequest for face AE failed: ");
                    stringBuilder4.append(e);
                    Log.d(str, stringBuilder4.toString());
                }
            }
        }
    }

    public int getCameraRotation(int cameraId) {
        try {
            int defaultRotation = ((Integer) ((CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE)).getCameraCharacteristics(Integer.toString(cameraId)).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue() % 360;
            if (defaultRotation == 0) {
                return 1;
            }
            if (defaultRotation == 90) {
                return 6;
            }
            if (defaultRotation == 180) {
                return 2;
            }
            if (defaultRotation != 270) {
                return 0;
            }
            return 5;
        } catch (CameraAccessException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::getCameraRotation - ERROR: ");
            stringBuilder.append(e);
            Log.d(TAG, stringBuilder.toString());
            return 0;
        }
    }

    /* Access modifiers changed, original: protected */
    public void createCameraSessionCommon() {
        String str = TAG;
        try {
            this.secureCamera2Surface_ = null;
            this.secureCamera2Surface_ = new SecureCamera2Surface(this.cameraId_.intValue(), this.width_.intValue(), this.height_.intValue(), this.format_.intValue(), this.numOfBuffers_.intValue());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createCameraSessionCommon - numOfBuffers_ = ");
            stringBuilder.append(this.numOfBuffers_);
            Log.d(str, stringBuilder.toString());
            this.secureCamera2Surface_.enableFrameCallback(this.ifaafacemgrimpl_, this.ifaafacemgrimpl_.frameCallbackReturnParamsSize_);
            Surface surface = this.secureCamera2Surface_.getCaptureSurface();
            this.captureBuilder_ = this.cameraDevice_.createCaptureRequest(1);
            this.captureBuilder_.addTarget(surface);
            List<OutputConfiguration> configurations = new ArrayList();
            configurations.add(new OutputConfiguration(surface));
            this.cameraDevice_.createCustomCaptureSession(null, configurations, SESSION_SECCAM, new CameraCaptureSession.StateCallback() {
                public void onActive(CameraCaptureSession session) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SecureCamera2API::CameraCaptureSession.StateCallback::onActive - Camera #");
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                }

                public void onClosed(CameraCaptureSession session) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SecureCamera2API::CameraCaptureSession.StateCallback::onClosed - Camera #");
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                    IFAASecureCamera2API.this.captureSessionClosedLatch_.countDown();
                }

                public void onConfigureFailed(CameraCaptureSession session) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SecureCamera2API::CameraCaptureSession.StateCallback::onConfigureFailed - Camera #");
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                    IFAASecureCamera2API.this.cameraStartedLatch_.countDown();
                }

                public void onConfigured(CameraCaptureSession session) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String str = "SecureCamera2API::CameraCaptureSession.StateCallback::onConfigured - Camera #";
                    stringBuilder.append(str);
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    String stringBuilder2 = stringBuilder.toString();
                    String str2 = IFAASecureCamera2API.TAG;
                    Log.d(str2, stringBuilder2);
                    if (IFAASecureCamera2API.this.cameraDevice_ == null) {
                        IFAASecureCamera2API.this.cameraStartedLatch_.countDown();
                        return;
                    }
                    IFAASecureCamera2API iFAASecureCamera2API = IFAASecureCamera2API.this;
                    iFAASecureCamera2API.captureSession_ = session;
                    iFAASecureCamera2API.captureCallback_ = new CaptureCallback() {
                        public void onCaptureBufferLost(CameraCaptureSession session, CaptureRequest request, Surface target, long frameNumber) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureBufferLost - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": (");
                            stringBuilder.append(frameNumber);
                            stringBuilder.append(")");
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }

                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureCompleted - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": Done (");
                            stringBuilder.append(result.getFrameNumber());
                            stringBuilder.append(")");
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }

                        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureFailed - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": ");
                            stringBuilder.append(failure.getReason());
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }

                        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureProgressed - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": ");
                            stringBuilder.append(partialResult.toString());
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }

                        public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureSequenceAborted - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": ");
                            stringBuilder.append(sequenceId);
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }

                        public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frameNumber) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureSequenceCompleted - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": (");
                            stringBuilder.append(frameNumber);
                            stringBuilder.append(")");
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }

                        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SecureCamera2API::CameraCaptureSession.CaptureCallback::onCaptureStarted - Camera #");
                            stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                            stringBuilder.append(": (");
                            stringBuilder.append(frameNumber);
                            stringBuilder.append(")");
                            Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                        }
                    };
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    stringBuilder.append(" capture session is ready!");
                    Log.d(str2, stringBuilder.toString());
                    try {
                        IFAASecureCamera2API.this.captureSession_.setRepeatingRequest(IFAASecureCamera2API.this.captureBuilder_.build(), IFAASecureCamera2API.this.captureCallback_, IFAASecureCamera2API.this.getBackgroundHandler());
                    } catch (CameraAccessException e) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("SecureCamera2API::CameraCaptureSession.StateCallback:onConfigured - Camera #");
                        stringBuilder3.append(IFAASecureCamera2API.this.cameraId_);
                        stringBuilder3.append(" - setRepeatingRequest failed: ");
                        stringBuilder3.append(e);
                        Log.d(str2, stringBuilder3.toString());
                    }
                    IFAASecureCamera2API.this.cameraStartedLatch_.countDown();
                }

                public void onReady(CameraCaptureSession session) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SecureCamera2API::CameraCaptureSession.StateCallback::onReady - Camera #");
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                }

                public void onSurfacePrepared(CameraCaptureSession session, Surface surface) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SecureCamera2API::CameraCaptureSession.StateCallback::onSurfacePrepared - Camera #");
                    stringBuilder.append(IFAASecureCamera2API.this.cameraId_);
                    Log.d(IFAASecureCamera2API.TAG, stringBuilder.toString());
                }
            }, null);
        } catch (CameraAccessException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("SecureCamera2API::createCameraSession ERROR: ");
            stringBuilder2.append(e);
            Log.d(str, stringBuilder2.toString());
        }
    }

    public Boolean start(int cameraId, SurfaceView previewSurfaceView, Integer width, Integer height, Integer format, Integer numOfBuffers, Integer previewSurfaceNumOfBuffers) {
        this.previewSurfaceView_ = previewSurfaceView;
        this.previewSurfaceNumOfBuffers_ = previewSurfaceNumOfBuffers;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SecureCamera2API::start - previewSurfaceView_ = ");
        stringBuilder.append(this.previewSurfaceView_);
        Log.d(TAG, stringBuilder.toString());
        return start(Integer.valueOf(cameraId), width, height, format, numOfBuffers);
    }

    public Boolean start(Integer cameraId, Integer width, Integer height, Integer format, Integer numOfBuffers) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("SecureCamera2API::start+++ - Camera #");
        stringBuilder2.append(this.cameraId_);
        String stringBuilder3 = stringBuilder2.toString();
        String str = TAG;
        Log.d(str, stringBuilder3);
        Boolean result = Boolean.FALSE;
        this.cameraId_ = cameraId;
        this.width_ = width;
        this.height_ = height;
        this.format_ = format;
        this.numOfBuffers_ = numOfBuffers;
        startBackgroundThread();
        try {
            CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            if (this.cameraOpenCloseSemaphore_.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                this.cameraStartedLatch_ = new CountDownLatch(1);
                manager.openCamera(Integer.toString(this.cameraId_.intValue()), this.mStateCallback, getBackgroundHandler());
                if (!this.cameraStartedLatch_.await(TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                    Log.d(str, "SecureCamera2API::start - ERROR: Camera Open TimeOut");
                } else if (this.cameraDevice_ == null) {
                    Log.d(str, "SecureCamera2API::start FAILED");
                } else {
                    Log.d(str, "SecureCamera2API::start OK");
                    SecCamServiceClient.getInstance().dispatchVendorCommand(2003, null);
                    this.cameraStarted_ = true;
                    result = Boolean.TRUE;
                }
            } else {
                Log.d(str, "SecureCamera2API::start ERROR: Camera Lock TimeOut");
            }
            this.cameraOpenCloseSemaphore_.release();
        } catch (CameraAccessException e) {
            Log.d(str, "SecureCamera2API::start ERROR: Not accesible");
            throw new RuntimeException();
        } catch (InterruptedException e2) {
            Log.d(str, "SecureCamera2API::start ERROR: Interrupted while trying to lock camera for opening - ", e2);
            throw new RuntimeException();
        } catch (RuntimeException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::start ERROR: ");
            stringBuilder.append(e3);
            Log.d(str, stringBuilder.toString());
        } catch (Exception e4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("SecureCamera2API::start Exception ");
            stringBuilder.append(e4);
            Log.d(str, stringBuilder.toString());
        } catch (Throwable th) {
            this.cameraOpenCloseSemaphore_.release();
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("SecureCamera2API::start--- - Camera #");
        stringBuilder4.append(this.cameraId_);
        stringBuilder4.append(" - Done");
        Log.d(str, stringBuilder4.toString());
        return result;
    }

    public Boolean stop() {
        Boolean result = Boolean.FALSE;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SecureCamera2API::stop+++ - Camera #");
        stringBuilder.append(this.cameraId_);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        SecCamServiceClient.getInstance().dispatchVendorCommand(2004, null);
        try {
            if (!this.cameraOpenCloseSemaphore_.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                Log.d(str, "stop Camera - ERROR: Camera Lock TimeOut");
            }
            if (this.captureSession_ != null) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("SecureCamera2APIPreview::stop - Camera #");
                stringBuilder3.append(this.cameraId_);
                stringBuilder3.append(": Close Capture Session");
                Log.d(str, stringBuilder3.toString());
                this.captureSessionClosedLatch_ = new CountDownLatch(1);
                this.captureSession_.close();
                if (!this.captureSessionClosedLatch_.await(AnrMonitor.MESSAGE_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    Log.d(str, "stop Camera - ERROR: Close Capture Session TimeOut");
                }
                this.captureSession_ = null;
            }
            if (this.cameraDevice_ != null) {
                Log.d(str, "SecureCamera2API::stop - Camera : Close Camera");
                this.cameraStopedLatch_ = new CountDownLatch(1);
                this.cameraDevice_.close();
                if (!this.cameraStopedLatch_.await(AnrMonitor.MESSAGE_EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    Log.d(str, "SecureCamera2API::stop - Camera - ERROR: Close Camera TimeOut");
                }
                this.cameraDevice_ = null;
            }
            result = Boolean.TRUE;
        } catch (InterruptedException e) {
            Log.d(str, "SecureCamera2API::stop - ERROR: Interrupted while trying to lock camera closing.", e);
        } catch (Throwable th) {
            this.cameraStarted_ = false;
            stopBackgroundThread();
            this.cameraOpenCloseSemaphore_.release();
        }
        this.cameraStarted_ = false;
        stopBackgroundThread();
        this.cameraOpenCloseSemaphore_.release();
        if (this.secureCamera2Surface_ != null) {
            Log.d(str, "SecureCamera2API::stop - Camera Release Secure Camera Surface");
            this.secureCamera2Surface_.release();
            this.secureCamera2Surface_ = null;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("SecureCamera2API::stop--- - Camera #");
        stringBuilder.append(this.cameraId_);
        stringBuilder.append(": Done");
        Log.d(str, stringBuilder.toString());
        return result;
    }
}
