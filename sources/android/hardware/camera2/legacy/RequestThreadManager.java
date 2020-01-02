package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException;
import android.hardware.camera2.utils.SizeAreaComparator;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestThreadManager {
    private static final float ASPECT_RATIO_TOLERANCE = 0.01f;
    private static final boolean DEBUG = false;
    private static final int JPEG_FRAME_TIMEOUT = 4000;
    private static final int MAX_IN_FLIGHT_REQUESTS = 2;
    private static final int MSG_CLEANUP = 3;
    private static final int MSG_CONFIGURE_OUTPUTS = 1;
    private static final int MSG_SUBMIT_CAPTURE_REQUEST = 2;
    private static final int PREVIEW_FRAME_TIMEOUT = 1000;
    private static final int REQUEST_COMPLETE_TIMEOUT = 4000;
    private static final boolean USE_BLOB_FORMAT_OVERRIDE = true;
    private static final boolean VERBOSE = false;
    private final String TAG;
    private final List<Surface> mCallbackOutputs = new ArrayList();
    private Camera mCamera;
    private final int mCameraId;
    private final CaptureCollector mCaptureCollector;
    private final CameraCharacteristics mCharacteristics;
    private final CameraDeviceState mDeviceState;
    private Surface mDummySurface;
    private SurfaceTexture mDummyTexture;
    private final ErrorCallback mErrorCallback = new ErrorCallback() {
        public void onError(int i, Camera camera) {
            if (i == 2) {
                RequestThreadManager.this.flush();
                RequestThreadManager.this.mDeviceState.setError(0);
            } else if (i != 3) {
                String access$100 = RequestThreadManager.this.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Received error ");
                stringBuilder.append(i);
                stringBuilder.append(" from the Camera1 ErrorCallback");
                Log.e(access$100, stringBuilder.toString());
                RequestThreadManager.this.mDeviceState.setError(1);
            } else {
                RequestThreadManager.this.flush();
                RequestThreadManager.this.mDeviceState.setError(6);
            }
        }
    };
    private final LegacyFaceDetectMapper mFaceDetectMapper;
    private final LegacyFocusStateMapper mFocusStateMapper;
    private GLThreadManager mGLThreadManager;
    private final Object mIdleLock = new Object();
    private Size mIntermediateBufferSize;
    private final PictureCallback mJpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(RequestThreadManager.this.TAG, "Received jpeg.");
            Pair<RequestHolder, Long> captureInfo = RequestThreadManager.this.mCaptureCollector.jpegProduced();
            if (captureInfo == null || captureInfo.first == null) {
                Log.e(RequestThreadManager.this.TAG, "Dropping jpeg frame.");
                return;
            }
            RequestHolder holder = captureInfo.first;
            long timestamp = ((Long) captureInfo.second).longValue();
            for (Surface s : holder.getHolderTargets()) {
                try {
                    if (LegacyCameraDevice.containsSurfaceId(s, RequestThreadManager.this.mJpegSurfaceIds)) {
                        Log.i(RequestThreadManager.this.TAG, "Producing jpeg buffer...");
                        int totalSize = ((data.length + LegacyCameraDevice.nativeGetJpegFooterSize()) + 3) & -4;
                        LegacyCameraDevice.setNextTimestamp(s, timestamp);
                        LegacyCameraDevice.setSurfaceFormat(s, 1);
                        int dimen = (((int) Math.ceil(Math.sqrt((double) totalSize))) + 15) & -16;
                        LegacyCameraDevice.setSurfaceDimens(s, dimen, dimen);
                        LegacyCameraDevice.produceFrame(s, data, dimen, dimen, 33);
                    }
                } catch (BufferQueueAbandonedException e) {
                    Log.w(RequestThreadManager.this.TAG, "Surface abandoned, dropping frame. ", e);
                }
            }
            RequestThreadManager.this.mReceivedJpeg.open();
        }
    };
    private final ShutterCallback mJpegShutterCallback = new ShutterCallback() {
        public void onShutter() {
            RequestThreadManager.this.mCaptureCollector.jpegCaptured(SystemClock.elapsedRealtimeNanos());
        }
    };
    private final List<Long> mJpegSurfaceIds = new ArrayList();
    private LegacyRequest mLastRequest = null;
    private Parameters mParams;
    private final FpsCounter mPrevCounter = new FpsCounter("Incoming Preview");
    private final OnFrameAvailableListener mPreviewCallback = new OnFrameAvailableListener() {
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            RequestThreadManager.this.mGLThreadManager.queueNewFrame();
        }
    };
    private final List<Surface> mPreviewOutputs = new ArrayList();
    private boolean mPreviewRunning = false;
    private SurfaceTexture mPreviewTexture;
    private final AtomicBoolean mQuit = new AtomicBoolean(false);
    private final ConditionVariable mReceivedJpeg = new ConditionVariable(false);
    private final FpsCounter mRequestCounter = new FpsCounter("Incoming Requests");
    private final Callback mRequestHandlerCb = new Callback() {
        private boolean mCleanup = false;
        private final LegacyResultMapper mMapper = new LegacyResultMapper();

        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        /* JADX WARNING: Removed duplicated region for block: B:159:0x0441  */
        public boolean handleMessage(android.os.Message r24) {
            /*
            r23 = this;
            r1 = r23;
            r2 = r24;
            r0 = r1.mCleanup;
            r3 = 1;
            if (r0 == 0) goto L_0x000a;
        L_0x0009:
            return r3;
        L_0x000a:
            r4 = 0;
            r0 = r2.what;
            r6 = -1;
            if (r0 == r6) goto L_0x04e1;
        L_0x0011:
            r6 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            if (r0 == r3) goto L_0x0464;
        L_0x0015:
            r8 = 2;
            r9 = 3;
            if (r0 == r8) goto L_0x00af;
        L_0x0019:
            if (r0 != r9) goto L_0x0091;
        L_0x001b:
            r1.mCleanup = r3;
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x0040 }
            r0 = r0.mCaptureCollector;	 Catch:{ InterruptedException -> 0x0040 }
            r8 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0040 }
            r0 = r0.waitForEmpty(r6, r8);	 Catch:{ InterruptedException -> 0x0040 }
            if (r0 != 0) goto L_0x003f;
        L_0x002b:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x0040 }
            r6 = r6.TAG;	 Catch:{ InterruptedException -> 0x0040 }
            r7 = "Timed out while queueing cleanup request.";
            android.util.Log.e(r6, r7);	 Catch:{ InterruptedException -> 0x0040 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x0040 }
            r6 = r6.mCaptureCollector;	 Catch:{ InterruptedException -> 0x0040 }
            r6.failAll();	 Catch:{ InterruptedException -> 0x0040 }
        L_0x003f:
            goto L_0x0055;
        L_0x0040:
            r0 = move-exception;
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.TAG;
            r7 = "Interrupted while waiting for requests to complete: ";
            android.util.Log.e(r6, r7, r0);
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.mDeviceState;
            r6.setError(r3);
        L_0x0055:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mGLThreadManager;
            r6 = 0;
            if (r0 == 0) goto L_0x006c;
        L_0x005e:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mGLThreadManager;
            r0.quit();
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0.mGLThreadManager = r6;
        L_0x006c:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0.disconnectCallbackSurfaces();
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mCamera;
            if (r0 == 0) goto L_0x008c;
        L_0x0079:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mCamera;
            r0.release();
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0.mCamera = r6;
            r6 = r3;
            r21 = r4;
            goto L_0x04e4;
        L_0x008c:
            r6 = r3;
            r21 = r4;
            goto L_0x04e4;
        L_0x0091:
            r0 = new java.lang.AssertionError;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r6 = "Unhandled message ";
            r3.append(r6);
            r6 = r2.what;
            r3.append(r6);
            r6 = " on RequestThread.";
            r3.append(r6);
            r3 = r3.toString();
            r0.<init>(r3);
            throw r0;
        L_0x00af:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mRequestThread;
            r10 = r0.getHandler();
            r11 = 0;
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mRequestQueue;
            r12 = r0.getNext();
            if (r12 != 0) goto L_0x012b;
        L_0x00c6:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x0111 }
            r0 = r0.mCaptureCollector;	 Catch:{ InterruptedException -> 0x0111 }
            r13 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0111 }
            r0 = r0.waitForEmpty(r6, r13);	 Catch:{ InterruptedException -> 0x0111 }
            if (r0 != 0) goto L_0x00e8;
        L_0x00d4:
            r13 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x0111 }
            r13 = r13.TAG;	 Catch:{ InterruptedException -> 0x0111 }
            r14 = "Timed out while waiting for prior requests to complete.";
            android.util.Log.e(r13, r14);	 Catch:{ InterruptedException -> 0x0111 }
            r13 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x0111 }
            r13 = r13.mCaptureCollector;	 Catch:{ InterruptedException -> 0x0111 }
            r13.failAll();	 Catch:{ InterruptedException -> 0x0111 }
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r13 = r0.mIdleLock;
            monitor-enter(r13);
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ all -> 0x010e }
            r0 = r0.mRequestQueue;	 Catch:{ all -> 0x010e }
            r0 = r0.getNext();	 Catch:{ all -> 0x010e }
            r12 = r0;
            if (r12 != 0) goto L_0x010c;
        L_0x00fd:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ all -> 0x010e }
            r0 = r0.mDeviceState;	 Catch:{ all -> 0x010e }
            r0.setIdle();	 Catch:{ all -> 0x010e }
            monitor-exit(r13);	 Catch:{ all -> 0x010e }
            r6 = r3;
            r21 = r4;
            goto L_0x04e4;
        L_0x010c:
            monitor-exit(r13);	 Catch:{ all -> 0x010e }
            goto L_0x012b;
        L_0x010e:
            r0 = move-exception;
            monitor-exit(r13);	 Catch:{ all -> 0x010e }
            throw r0;
        L_0x0111:
            r0 = move-exception;
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.TAG;
            r7 = "Interrupted while waiting for requests to complete: ";
            android.util.Log.e(r6, r7, r0);
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.mDeviceState;
            r6.setError(r3);
            r6 = r3;
            r21 = r4;
            goto L_0x04e4;
            r10.sendEmptyMessage(r8);
            r0 = r12.isQueueEmpty();
            if (r0 == 0) goto L_0x013e;
        L_0x0135:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mDeviceState;
            r0.setRequestQueueEmpty();
        L_0x013e:
            r8 = r12.getBurstHolder();
            r0 = r12.getFrameNumber();
            r13 = r0.longValue();
            r13 = r8.produceRequestHolders(r13);
            r14 = r13.iterator();
        L_0x0153:
            r0 = r14.hasNext();
            if (r0 == 0) goto L_0x043c;
        L_0x0159:
            r0 = r14.next();
            r15 = r0;
            r15 = (android.hardware.camera2.legacy.RequestHolder) r15;
            r3 = r15.getRequest();
            r16 = 0;
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mLastRequest;
            if (r0 == 0) goto L_0x017f;
        L_0x016e:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mLastRequest;
            r0 = r0.captureRequest;
            if (r0 == r3) goto L_0x0179;
        L_0x0178:
            goto L_0x017f;
        L_0x0179:
            r21 = r4;
            r4 = r16;
            goto L_0x01f7;
        L_0x017f:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mParams;
            r0 = r0.getPreviewSize();
            r6 = android.hardware.camera2.legacy.ParameterUtils.convertSize(r0);
            r0 = new android.hardware.camera2.legacy.LegacyRequest;
            r7 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r7 = r7.mCharacteristics;
            r9 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r9 = r9.mParams;
            r0.<init>(r7, r3, r6, r9);
            r7 = r0;
            android.hardware.camera2.legacy.LegacyMetadataMapper.convertRequestMetadata(r7);
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mParams;
            r9 = r7.parameters;
            r0 = r0.same(r9);
            if (r0 != 0) goto L_0x01ec;
        L_0x01b0:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ RuntimeException -> 0x01ca }
            r0 = r0.mCamera;	 Catch:{ RuntimeException -> 0x01ca }
            r9 = r7.parameters;	 Catch:{ RuntimeException -> 0x01ca }
            r0.setParameters(r9);	 Catch:{ RuntimeException -> 0x01ca }
            r16 = 1;
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r9 = r7.parameters;
            r0.mParams = r9;
            r21 = r4;
            r17 = r6;
            goto L_0x01f0;
        L_0x01ca:
            r0 = move-exception;
            r9 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r9 = r9.TAG;
            r21 = r4;
            r4 = "Exception while setting camera parameters: ";
            android.util.Log.e(r9, r4, r0);
            r15.failRequest();
            r4 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r4 = r4.mDeviceState;
            r17 = r6;
            r5 = 0;
            r9 = 3;
            r4.setCaptureStart(r15, r5, r9);
            r7 = r10;
            r15 = 3;
            goto L_0x022d;
        L_0x01ec:
            r21 = r4;
            r17 = r6;
        L_0x01f0:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0.mLastRequest = r7;
            r4 = r16;
        L_0x01f7:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0420, InterruptedException -> 0x0404, RuntimeException -> 0x03e8 }
            r0 = r0.mCaptureCollector;	 Catch:{ IOException -> 0x0420, InterruptedException -> 0x0404, RuntimeException -> 0x03e8 }
            r5 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0420, InterruptedException -> 0x0404, RuntimeException -> 0x03e8 }
            r17 = r5.mLastRequest;	 Catch:{ IOException -> 0x0420, InterruptedException -> 0x0404, RuntimeException -> 0x03e8 }
            r18 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r20 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ IOException -> 0x0420, InterruptedException -> 0x0404, RuntimeException -> 0x03e8 }
            r5 = r15;
            r15 = r0;
            r16 = r5;
            r0 = r15.queueRequest(r16, r17, r18, r20);	 Catch:{ IOException -> 0x03e2, InterruptedException -> 0x03dc, RuntimeException -> 0x03d6 }
            if (r0 != 0) goto L_0x024b;
        L_0x0211:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0244, InterruptedException -> 0x023d, RuntimeException -> 0x0236 }
            r6 = r6.TAG;	 Catch:{ IOException -> 0x0244, InterruptedException -> 0x023d, RuntimeException -> 0x0236 }
            r7 = "Timed out while queueing capture request.";
            android.util.Log.e(r6, r7);	 Catch:{ IOException -> 0x0244, InterruptedException -> 0x023d, RuntimeException -> 0x0236 }
            r5.failRequest();	 Catch:{ IOException -> 0x0244, InterruptedException -> 0x023d, RuntimeException -> 0x0236 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0244, InterruptedException -> 0x023d, RuntimeException -> 0x0236 }
            r6 = r6.mDeviceState;	 Catch:{ IOException -> 0x0244, InterruptedException -> 0x023d, RuntimeException -> 0x0236 }
            r7 = r10;
            r9 = 0;
            r15 = 3;
            r6.setCaptureStart(r5, r9, r15);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
        L_0x022d:
            r10 = r7;
            r9 = r15;
            r4 = r21;
            r3 = 1;
            r6 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            goto L_0x0153;
        L_0x0236:
            r0 = move-exception;
            r7 = r10;
            r15 = r3;
            r16 = r4;
            goto L_0x03ee;
        L_0x023d:
            r0 = move-exception;
            r7 = r10;
            r15 = r3;
            r16 = r4;
            goto L_0x040a;
        L_0x0244:
            r0 = move-exception;
            r7 = r10;
            r15 = r3;
            r16 = r4;
            goto L_0x0426;
        L_0x024b:
            r7 = r10;
            r15 = 3;
            r6 = r5.hasPreviewTargets();	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            if (r6 == 0) goto L_0x026b;
        L_0x0253:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6.doPreviewCapture(r5);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            goto L_0x026b;
        L_0x0259:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x03ee;
        L_0x025f:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x040a;
        L_0x0265:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x0426;
        L_0x026b:
            r6 = r5.hasJpegTargets();	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            if (r6 == 0) goto L_0x02a5;
        L_0x0271:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.mCaptureCollector;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r9 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r15 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.waitForPreviewsEmpty(r9, r15);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            if (r6 != 0) goto L_0x0297;
        L_0x0281:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.TAG;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r9 = "Timed out while waiting for preview requests to complete.";
            android.util.Log.e(r6, r9);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.mCaptureCollector;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6.failNextPreview();	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r15 = 3;
            goto L_0x0271;
        L_0x0297:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.mReceivedJpeg;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6.close();	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6.doJpegCapturePrepare(r5);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
        L_0x02a5:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r6 = r6.mFaceDetectMapper;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r9 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r9 = r9.mParams;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r6.processFaceDetectMode(r3, r9);	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r6 = r6.mFocusStateMapper;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r9 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r9 = r9.mParams;	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r6.processRequestTriggers(r3, r9);	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            r6 = r5.hasJpegTargets();	 Catch:{ IOException -> 0x03d0, InterruptedException -> 0x03cb, RuntimeException -> 0x03c6 }
            if (r6 == 0) goto L_0x02f0;
        L_0x02c9:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6.doJpegCapture(r5);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.mReceivedJpeg;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r9 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r6 = r6.block(r9);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            if (r6 != 0) goto L_0x02f0;
        L_0x02dc:
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.TAG;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r9 = "Hit timeout for jpeg callback!";
            android.util.Log.e(r6, r9);	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6 = r6.mCaptureCollector;	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            r6.failNextJpeg();	 Catch:{ IOException -> 0x0265, InterruptedException -> 0x025f, RuntimeException -> 0x0259 }
            if (r4 == 0) goto L_0x032b;
        L_0x02f3:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ RuntimeException -> 0x0313 }
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ RuntimeException -> 0x0313 }
            r6 = r6.mCamera;	 Catch:{ RuntimeException -> 0x0313 }
            r6 = r6.getParameters();	 Catch:{ RuntimeException -> 0x0313 }
            r0.mParams = r6;	 Catch:{ RuntimeException -> 0x0313 }
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mLastRequest;
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.mParams;
            r0.setParameters(r6);
            goto L_0x032b;
        L_0x0313:
            r0 = move-exception;
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.TAG;
            r9 = "Received device exception: ";
            android.util.Log.e(r6, r9, r0);
            r6 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r6 = r6.mDeviceState;
            r9 = 1;
            r6.setError(r9);
            goto L_0x043f;
        L_0x032b:
            r0 = new android.util.MutableLong;
            r9 = 0;
            r0.<init>(r9);
            r6 = r0;
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x03ab }
            r15 = r0.mCaptureCollector;	 Catch:{ InterruptedException -> 0x03ab }
            r17 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r19 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x03ab }
            r9 = 3;
            r16 = r5;
            r20 = r6;
            r0 = r15.waitForRequestCompleted(r16, r17, r19, r20);	 Catch:{ InterruptedException -> 0x03ab }
            if (r0 != 0) goto L_0x0362;
        L_0x0348:
            r10 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x035d }
            r10 = r10.TAG;	 Catch:{ InterruptedException -> 0x035d }
            r15 = "Timed out while waiting for request to complete.";
            android.util.Log.e(r10, r15);	 Catch:{ InterruptedException -> 0x035d }
            r10 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x035d }
            r10 = r10.mCaptureCollector;	 Catch:{ InterruptedException -> 0x035d }
            r10.failAll();	 Catch:{ InterruptedException -> 0x035d }
            goto L_0x0362;
        L_0x035d:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x03af;
            r0 = r1.mMapper;
            r10 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r10 = r10.mLastRequest;
            r15 = r3;
            r16 = r4;
            r3 = r6.value;
            r0 = r0.cachedConvertResultMetadata(r10, r3);
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mFocusStateMapper;
            r3.mapResultTriggers(r0);
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mFaceDetectMapper;
            r4 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r4 = r4.mLastRequest;
            r3.mapResultFaces(r0, r4);
            r3 = r5.requestFailed();
            if (r3 != 0) goto L_0x039b;
        L_0x0392:
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mDeviceState;
            r3.setCaptureResult(r5, r0);
        L_0x039b:
            r3 = r5.isOutputAbandoned();
            if (r3 == 0) goto L_0x03a3;
        L_0x03a1:
            r3 = 1;
            r11 = r3;
        L_0x03a3:
            r10 = r7;
            r4 = r21;
            r3 = 1;
            r6 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            goto L_0x0153;
        L_0x03ab:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
        L_0x03af:
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.TAG;
            r4 = "Interrupted waiting for request completion: ";
            android.util.Log.e(r3, r4, r0);
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mDeviceState;
            r4 = 1;
            r3.setError(r4);
            goto L_0x043f;
        L_0x03c6:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x03ee;
        L_0x03cb:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x040a;
        L_0x03d0:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            goto L_0x0426;
        L_0x03d6:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            r7 = r10;
            goto L_0x03ee;
        L_0x03dc:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            r7 = r10;
            goto L_0x040a;
        L_0x03e2:
            r0 = move-exception;
            r15 = r3;
            r16 = r4;
            r7 = r10;
            goto L_0x0426;
        L_0x03e8:
            r0 = move-exception;
            r16 = r4;
            r7 = r10;
            r5 = r15;
            r15 = r3;
        L_0x03ee:
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.TAG;
            r4 = "Received device exception during capture call: ";
            android.util.Log.e(r3, r4, r0);
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mDeviceState;
            r4 = 1;
            r3.setError(r4);
            goto L_0x043f;
        L_0x0404:
            r0 = move-exception;
            r16 = r4;
            r7 = r10;
            r5 = r15;
            r15 = r3;
        L_0x040a:
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.TAG;
            r4 = "Interrupted during capture: ";
            android.util.Log.e(r3, r4, r0);
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mDeviceState;
            r4 = 1;
            r3.setError(r4);
            goto L_0x043f;
        L_0x0420:
            r0 = move-exception;
            r16 = r4;
            r7 = r10;
            r5 = r15;
            r15 = r3;
        L_0x0426:
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.TAG;
            r4 = "Received device exception during capture call: ";
            android.util.Log.e(r3, r4, r0);
            r3 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r3.mDeviceState;
            r4 = 1;
            r3.setError(r4);
            goto L_0x043f;
        L_0x043c:
            r21 = r4;
            r7 = r10;
        L_0x043f:
            if (r11 == 0) goto L_0x0461;
        L_0x0441:
            r0 = r8.isRepeating();
            if (r0 == 0) goto L_0x0461;
        L_0x0447:
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r3 = r8.getRequestId();
            r3 = r0.cancelRepeating(r3);
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.mDeviceState;
            r5 = r8.getRequestId();
            r0.setRepeatingRequestError(r3, r5);
            r6 = 1;
            goto L_0x04e4;
        L_0x0461:
            r6 = 1;
            goto L_0x04e4;
        L_0x0464:
            r21 = r4;
            r0 = r2.obj;
            r3 = r0;
            r3 = (android.hardware.camera2.legacy.RequestThreadManager.ConfigureHolder) r3;
            r0 = r3.surfaces;
            if (r0 == 0) goto L_0x0476;
        L_0x046f:
            r0 = r3.surfaces;
            r0 = r0.size();
            goto L_0x0477;
        L_0x0476:
            r0 = 0;
        L_0x0477:
            r4 = r0;
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r0 = r0.TAG;
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r6 = "Configure outputs: ";
            r5.append(r6);
            r5.append(r4);
            r6 = " surfaces configured.";
            r5.append(r6);
            r5 = r5.toString();
            android.util.Log.i(r0, r5);
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x04ca }
            r0 = r0.mCaptureCollector;	 Catch:{ InterruptedException -> 0x04ca }
            r5 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x04ca }
            r6 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r0 = r0.waitForEmpty(r6, r5);	 Catch:{ InterruptedException -> 0x04ca }
            if (r0 != 0) goto L_0x04bb;
        L_0x04a7:
            r5 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x04ca }
            r5 = r5.TAG;	 Catch:{ InterruptedException -> 0x04ca }
            r6 = "Timed out while queueing configure request.";
            android.util.Log.e(r5, r6);	 Catch:{ InterruptedException -> 0x04ca }
            r5 = android.hardware.camera2.legacy.RequestThreadManager.this;	 Catch:{ InterruptedException -> 0x04ca }
            r5 = r5.mCaptureCollector;	 Catch:{ InterruptedException -> 0x04ca }
            r5.failAll();	 Catch:{ InterruptedException -> 0x04ca }
            r0 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r5 = r3.surfaces;
            r0.configureOutputs(r5);
            r0 = r3.condition;
            r0.open();
            r6 = 1;
            goto L_0x04e4;
        L_0x04ca:
            r0 = move-exception;
            r5 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r5 = r5.TAG;
            r6 = "Interrupted while waiting for requests to complete.";
            android.util.Log.e(r5, r6);
            r5 = android.hardware.camera2.legacy.RequestThreadManager.this;
            r5 = r5.mDeviceState;
            r6 = 1;
            r5.setError(r6);
            goto L_0x04e4;
        L_0x04e1:
            r6 = r3;
            r21 = r4;
        L_0x04e4:
            return r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.RequestThreadManager$AnonymousClass5.handleMessage(android.os.Message):boolean");
        }
    };
    private final RequestQueue mRequestQueue = new RequestQueue(this.mJpegSurfaceIds);
    private final RequestHandlerThread mRequestThread;

    private static class ConfigureHolder {
        public final ConditionVariable condition;
        public final Collection<Pair<Surface, Size>> surfaces;

        public ConfigureHolder(ConditionVariable condition, Collection<Pair<Surface, Size>> surfaces) {
            this.condition = condition;
            this.surfaces = surfaces;
        }
    }

    public static class FpsCounter {
        private static final long NANO_PER_SECOND = 1000000000;
        private static final String TAG = "FpsCounter";
        private int mFrameCount = 0;
        private double mLastFps = 0.0d;
        private long mLastPrintTime = 0;
        private long mLastTime = 0;
        private final String mStreamType;

        public FpsCounter(String streamType) {
            this.mStreamType = streamType;
        }

        public synchronized void countFrame() {
            this.mFrameCount++;
            long nextTime = SystemClock.elapsedRealtimeNanos();
            if (this.mLastTime == 0) {
                this.mLastTime = nextTime;
            }
            if (nextTime > this.mLastTime + 1000000000) {
                this.mLastFps = ((double) this.mFrameCount) * (1.0E9d / ((double) (nextTime - this.mLastTime)));
                this.mFrameCount = 0;
                this.mLastTime = nextTime;
            }
        }

        public synchronized double checkFps() {
            return this.mLastFps;
        }

        public synchronized void staggeredLog() {
            if (this.mLastTime > this.mLastPrintTime + 5000000000L) {
                this.mLastPrintTime = this.mLastTime;
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("FPS for ");
                stringBuilder.append(this.mStreamType);
                stringBuilder.append(" stream: ");
                stringBuilder.append(this.mLastFps);
                Log.d(str, stringBuilder.toString());
            }
        }

        public synchronized void countAndLog() {
            countFrame();
            staggeredLog();
        }
    }

    private void createDummySurface() {
        if (this.mDummyTexture == null || this.mDummySurface == null) {
            this.mDummyTexture = new SurfaceTexture(0);
            this.mDummyTexture.setDefaultBufferSize(640, 480);
            this.mDummySurface = new Surface(this.mDummyTexture);
        }
    }

    private void stopPreview() {
        if (this.mPreviewRunning) {
            this.mCamera.stopPreview();
            this.mPreviewRunning = false;
        }
    }

    private void startPreview() {
        if (!this.mPreviewRunning) {
            this.mCamera.startPreview();
            this.mPreviewRunning = true;
        }
    }

    private void doJpegCapturePrepare(RequestHolder request) throws IOException {
        if (!this.mPreviewRunning) {
            createDummySurface();
            this.mCamera.setPreviewTexture(this.mDummyTexture);
            startPreview();
        }
    }

    private void doJpegCapture(RequestHolder request) {
        this.mCamera.takePicture(this.mJpegShutterCallback, null, this.mJpegCallback);
        this.mPreviewRunning = false;
    }

    private void doPreviewCapture(RequestHolder request) throws IOException {
        if (!this.mPreviewRunning) {
            SurfaceTexture surfaceTexture = this.mPreviewTexture;
            if (surfaceTexture != null) {
                surfaceTexture.setDefaultBufferSize(this.mIntermediateBufferSize.getWidth(), this.mIntermediateBufferSize.getHeight());
                this.mCamera.setPreviewTexture(this.mPreviewTexture);
                startPreview();
                return;
            }
            throw new IllegalStateException("Preview capture called with no preview surfaces configured.");
        }
    }

    private void disconnectCallbackSurfaces() {
        for (Surface s : this.mCallbackOutputs) {
            try {
                LegacyCameraDevice.disconnectSurface(s);
            } catch (BufferQueueAbandonedException e) {
                Log.d(this.TAG, "Surface abandoned, skipping...", e);
            }
        }
    }

    private void configureOutputs(Collection<Pair<Surface, Size>> outputs) {
        String str = "Received device exception in configure call: ";
        try {
            Size outSize;
            stopPreview();
            try {
                this.mCamera.setPreviewTexture(null);
            } catch (IOException e) {
                Log.w(this.TAG, "Failed to clear prior SurfaceTexture, may cause GL deadlock: ", e);
            } catch (RuntimeException e2) {
                Log.e(this.TAG, str, e2);
                this.mDeviceState.setError(1);
                return;
            }
            GLThreadManager gLThreadManager = this.mGLThreadManager;
            if (gLThreadManager != null) {
                gLThreadManager.waitUntilStarted();
                this.mGLThreadManager.ignoreNewFrames();
                this.mGLThreadManager.waitUntilIdle();
            }
            resetJpegSurfaceFormats(this.mCallbackOutputs);
            disconnectCallbackSurfaces();
            this.mPreviewOutputs.clear();
            this.mCallbackOutputs.clear();
            this.mJpegSurfaceIds.clear();
            this.mPreviewTexture = null;
            ArrayList previewOutputSizes = new ArrayList();
            List<Size> callbackOutputSizes = new ArrayList();
            int facing = ((Integer) this.mCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
            int orientation = ((Integer) this.mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
            if (outputs != null) {
                for (Pair<Surface, Size> outPair : outputs) {
                    Surface s = outPair.first;
                    outSize = outPair.second;
                    try {
                        int format = LegacyCameraDevice.detectSurfaceType(s);
                        LegacyCameraDevice.setSurfaceOrientation(s, facing, orientation);
                        if (format != 33) {
                            LegacyCameraDevice.setScalingMode(s, 1);
                            this.mPreviewOutputs.add(s);
                            previewOutputSizes.add(outSize);
                        } else {
                            LegacyCameraDevice.setSurfaceFormat(s, 1);
                            this.mJpegSurfaceIds.add(Long.valueOf(LegacyCameraDevice.getSurfaceId(s)));
                            this.mCallbackOutputs.add(s);
                            callbackOutputSizes.add(outSize);
                            LegacyCameraDevice.connectSurface(s);
                        }
                    } catch (BufferQueueAbandonedException e3) {
                        Log.w(this.TAG, "Surface abandoned, skipping...", e3);
                    }
                }
            }
            List<Size> callbackOutputSizes2;
            try {
                this.mParams = this.mCamera.getParameters();
                List<int[]> supportedFpsRanges = this.mParams.getSupportedPreviewFpsRange();
                int[] bestRange = getPhotoPreviewFpsRange(supportedFpsRanges);
                this.mParams.setPreviewFpsRange(bestRange[0], bestRange[1]);
                Size smallestSupportedJpegSize = calculatePictureSize(this.mCallbackOutputs, callbackOutputSizes, this.mParams);
                List<int[]> supportedFpsRanges2;
                int[] bestRange2;
                if (previewOutputSizes.size() > 0) {
                    Size largestOutput = SizeAreaComparator.findLargestByArea(previewOutputSizes);
                    Size largestJpegDimen = ParameterUtils.getLargestSupportedJpegSizeByArea(this.mParams);
                    if (smallestSupportedJpegSize != null) {
                        outSize = smallestSupportedJpegSize;
                    } else {
                        outSize = largestJpegDimen;
                    }
                    List<Size> supportedPreviewSizes = ParameterUtils.convertSizeList(this.mParams.getSupportedPreviewSizes());
                    long largestOutputArea = ((long) largestOutput.getHeight()) * ((long) largestOutput.getWidth());
                    Size bestPreviewDimen = SizeAreaComparator.findLargestByArea(supportedPreviewSizes);
                    Iterator it = supportedPreviewSizes.iterator();
                    while (it.hasNext()) {
                        Size s2 = (Size) it.next();
                        Size largestOutput2 = largestOutput;
                        Iterator it2 = it;
                        callbackOutputSizes2 = callbackOutputSizes;
                        long currArea = (long) (s2.getWidth() * s2.getHeight());
                        supportedFpsRanges2 = supportedFpsRanges;
                        bestRange2 = bestRange;
                        long bestArea = (long) (bestPreviewDimen.getWidth() * bestPreviewDimen.getHeight());
                        if (checkAspectRatiosMatch(outSize, s2) && currArea < bestArea && currArea >= largestOutputArea) {
                            bestPreviewDimen = s2;
                        }
                        callbackOutputSizes = callbackOutputSizes2;
                        it = it2;
                        largestOutput = largestOutput2;
                        supportedFpsRanges = supportedFpsRanges2;
                        bestRange = bestRange2;
                    }
                    callbackOutputSizes2 = callbackOutputSizes;
                    supportedFpsRanges2 = supportedFpsRanges;
                    bestRange2 = bestRange;
                    this.mIntermediateBufferSize = bestPreviewDimen;
                    this.mParams.setPreviewSize(this.mIntermediateBufferSize.getWidth(), this.mIntermediateBufferSize.getHeight());
                } else {
                    supportedFpsRanges2 = supportedFpsRanges;
                    bestRange2 = bestRange;
                    this.mIntermediateBufferSize = null;
                }
                if (smallestSupportedJpegSize != null) {
                    String str2 = this.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("configureOutputs - set take picture size to ");
                    stringBuilder.append(smallestSupportedJpegSize);
                    Log.i(str2, stringBuilder.toString());
                    this.mParams.setPictureSize(smallestSupportedJpegSize.getWidth(), smallestSupportedJpegSize.getHeight());
                }
                if (this.mGLThreadManager == null) {
                    this.mGLThreadManager = new GLThreadManager(this.mCameraId, facing, this.mDeviceState);
                    this.mGLThreadManager.start();
                }
                this.mGLThreadManager.waitUntilStarted();
                ArrayList previews = new ArrayList();
                Iterator<Size> previewSizeIter = previewOutputSizes.iterator();
                for (Surface p : this.mPreviewOutputs) {
                    previews.add(new Pair(p, (Size) previewSizeIter.next()));
                }
                this.mGLThreadManager.setConfigurationAndWait(previews, this.mCaptureCollector);
                for (Surface p2 : this.mPreviewOutputs) {
                    try {
                        LegacyCameraDevice.setSurfaceOrientation(p2, facing, orientation);
                    } catch (BufferQueueAbandonedException e32) {
                        Log.e(this.TAG, "Surface abandoned, skipping setSurfaceOrientation()", e32);
                    }
                }
                this.mGLThreadManager.allowNewFrames();
                this.mPreviewTexture = this.mGLThreadManager.getCurrentSurfaceTexture();
                SurfaceTexture surfaceTexture = this.mPreviewTexture;
                if (surfaceTexture != null) {
                    surfaceTexture.setOnFrameAvailableListener(this.mPreviewCallback);
                }
                try {
                    this.mCamera.setParameters(this.mParams);
                } catch (RuntimeException e22) {
                    Log.e(this.TAG, "Received device exception while configuring: ", e22);
                    this.mDeviceState.setError(1);
                }
            } catch (RuntimeException e222) {
                callbackOutputSizes2 = callbackOutputSizes;
                Log.e(this.TAG, "Received device exception: ", e222);
                this.mDeviceState.setError(1);
            }
        } catch (RuntimeException e2222) {
            Log.e(this.TAG, str, e2222);
            this.mDeviceState.setError(1);
        }
    }

    private void resetJpegSurfaceFormats(Collection<Surface> surfaces) {
        if (surfaces != null) {
            for (Surface s : surfaces) {
                if (s == null || !s.isValid()) {
                    Log.w(this.TAG, "Jpeg surface is invalid, skipping...");
                } else {
                    try {
                        LegacyCameraDevice.setSurfaceFormat(s, 33);
                    } catch (BufferQueueAbandonedException e) {
                        Log.w(this.TAG, "Surface abandoned, skipping...", e);
                    }
                }
            }
        }
    }

    private Size calculatePictureSize(List<Surface> callbackOutputs, List<Size> callbackSizes, Parameters params) {
        if (callbackOutputs.size() == callbackSizes.size()) {
            Size jpegSize;
            List<Size> configuredJpegSizes = new ArrayList();
            Iterator<Size> sizeIterator = callbackSizes.iterator();
            for (Surface callbackSurface : callbackOutputs) {
                jpegSize = (Size) sizeIterator.next();
                if (LegacyCameraDevice.containsSurfaceId(callbackSurface, this.mJpegSurfaceIds)) {
                    configuredJpegSizes.add(jpegSize);
                }
            }
            if (configuredJpegSizes.isEmpty()) {
                return null;
            }
            int maxConfiguredJpegWidth = -1;
            int maxConfiguredJpegHeight = -1;
            for (Size jpegSize2 : configuredJpegSizes) {
                maxConfiguredJpegWidth = jpegSize2.getWidth() > maxConfiguredJpegWidth ? jpegSize2.getWidth() : maxConfiguredJpegWidth;
                maxConfiguredJpegHeight = jpegSize2.getHeight() > maxConfiguredJpegHeight ? jpegSize2.getHeight() : maxConfiguredJpegHeight;
            }
            jpegSize = new Size(maxConfiguredJpegWidth, maxConfiguredJpegHeight);
            List<Size> supportedJpegSizes = ParameterUtils.convertSizeList(params.getSupportedPictureSizes());
            List<Size> candidateSupportedJpegSizes = new ArrayList();
            for (Size supportedJpegSize : supportedJpegSizes) {
                if (supportedJpegSize.getWidth() >= maxConfiguredJpegWidth && supportedJpegSize.getHeight() >= maxConfiguredJpegHeight) {
                    candidateSupportedJpegSizes.add(supportedJpegSize);
                }
            }
            if (candidateSupportedJpegSizes.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not find any supported JPEG sizes large enough to fit ");
                stringBuilder.append(jpegSize);
                throw new AssertionError(stringBuilder.toString());
            }
            Size smallestSupportedJpegSize = (Size) Collections.min(candidateSupportedJpegSizes, new SizeAreaComparator());
            if (!smallestSupportedJpegSize.equals(jpegSize)) {
                Log.w(this.TAG, String.format("configureOutputs - Will need to crop picture %s into smallest bound size %s", new Object[]{smallestSupportedJpegSize, jpegSize}));
            }
            return smallestSupportedJpegSize;
        }
        throw new IllegalStateException("Input collections must be same length");
    }

    private static boolean checkAspectRatiosMatch(Size a, Size b) {
        return Math.abs((((float) a.getWidth()) / ((float) a.getHeight())) - (((float) b.getWidth()) / ((float) b.getHeight()))) < ASPECT_RATIO_TOLERANCE;
    }

    private int[] getPhotoPreviewFpsRange(List<int[]> frameRates) {
        if (frameRates.size() == 0) {
            Log.e(this.TAG, "No supported frame rates returned!");
            return null;
        }
        int bestMin = 0;
        int bestMax = 0;
        int bestIndex = 0;
        int index = 0;
        for (int[] rate : frameRates) {
            int minFps = rate[0];
            int maxFps = rate[1];
            if (maxFps > bestMax || (maxFps == bestMax && minFps > bestMin)) {
                bestMin = minFps;
                bestMax = maxFps;
                bestIndex = index;
            }
            index++;
        }
        return (int[]) frameRates.get(bestIndex);
    }

    public RequestThreadManager(int cameraId, Camera camera, CameraCharacteristics characteristics, CameraDeviceState deviceState) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
        this.mCameraId = cameraId;
        this.mCharacteristics = (CameraCharacteristics) Preconditions.checkNotNull(characteristics, "characteristics must not be null");
        String name = String.format("RequestThread-%d", new Object[]{Integer.valueOf(cameraId)});
        this.TAG = name;
        this.mDeviceState = (CameraDeviceState) Preconditions.checkNotNull(deviceState, "deviceState must not be null");
        this.mFocusStateMapper = new LegacyFocusStateMapper(this.mCamera);
        this.mFaceDetectMapper = new LegacyFaceDetectMapper(this.mCamera, this.mCharacteristics);
        this.mCaptureCollector = new CaptureCollector(2, this.mDeviceState);
        this.mRequestThread = new RequestHandlerThread(name, this.mRequestHandlerCb);
        this.mCamera.setDetailedErrorCallback(this.mErrorCallback);
    }

    public void start() {
        this.mRequestThread.start();
    }

    public long flush() {
        Log.i(this.TAG, "Flushing all pending requests.");
        long lastFrame = this.mRequestQueue.stopRepeating();
        this.mCaptureCollector.failAll();
        return lastFrame;
    }

    public void quit() {
        if (!this.mQuit.getAndSet(true)) {
            Handler handler = this.mRequestThread.waitAndGetHandler();
            handler.sendMessageAtFrontOfQueue(handler.obtainMessage(3));
            this.mRequestThread.quitSafely();
            try {
                this.mRequestThread.join();
            } catch (InterruptedException e) {
                Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[]{this.mRequestThread.getName(), Long.valueOf(this.mRequestThread.getId())}));
            }
        }
    }

    public SubmitInfo submitCaptureRequests(CaptureRequest[] requests, boolean repeating) {
        SubmitInfo info;
        Handler handler = this.mRequestThread.waitAndGetHandler();
        synchronized (this.mIdleLock) {
            info = this.mRequestQueue.submit(requests, repeating);
            handler.sendEmptyMessage(2);
        }
        return info;
    }

    public long cancelRepeating(int requestId) {
        return this.mRequestQueue.stopRepeating(requestId);
    }

    public void configure(Collection<Pair<Surface, Size>> outputs) {
        Handler handler = this.mRequestThread.waitAndGetHandler();
        ConditionVariable condition = new ConditionVariable(false);
        handler.sendMessage(handler.obtainMessage(1, 0, 0, new ConfigureHolder(condition, outputs)));
        condition.block();
    }
}
