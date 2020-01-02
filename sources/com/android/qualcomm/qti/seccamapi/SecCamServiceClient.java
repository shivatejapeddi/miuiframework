package com.android.qualcomm.qti.seccamapi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.miui.BiometricConnect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;
import com.android.qualcomm.qti.seccamapi.SecureSurface.FrameCallback;
import com.android.qualcomm.qti.seccamapi.SecureSurface.FrameInfo;
import com.android.qualcomm.qti.seccamapi.SecureSurface.SurfaceInfo;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SecCamServiceClient extends Handler {
    public static final Integer DESTINATION_MLVM = Integer.valueOf(2);
    public static final Integer DESTINATION_QTEE;
    public static final Integer HAL1;
    public static final Integer HAL3 = Integer.valueOf(3);
    private static final String LOG_TAG = "SECCAM-SERVICE-CLIENT";
    private static final String MSG_DATA_CAMERAID = "cameraId";
    private static final String MSG_DATA_CSURFACEID = "cSurfaceId";
    private static final String MSG_DATA_FORMAT = "format";
    private static final String MSG_DATA_FRAMENUMBER = "frameNumber";
    private static final String MSG_DATA_HEIGHT = "height";
    private static final String MSG_DATA_NUMOFBUFFERS = "numOfBuffers";
    private static final String MSG_DATA_PSURFACE = "PSURFACE";
    private static final String MSG_DATA_RESULT = "result";
    private static final String MSG_DATA_RETURNPARAMS = "returnParams";
    private static final String MSG_DATA_RETURNPARAMSSIZE = "returnParamsSize";
    private static final String MSG_DATA_ROTATION = "rotation";
    private static final String MSG_DATA_STRIDE = "stride";
    private static final String MSG_DATA_SURFACEID = "surfaceId";
    private static final String MSG_DATA_TIMEOUT = "timeout";
    private static final String MSG_DATA_TIMESTAMP = "timeStamp";
    private static final String MSG_DATA_WIDTH = "width";
    private static final int MSG_ENABLE_FRAME_CALLBACK = 1005;
    private static final int MSG_FRAME_CALLBACK = 1006;
    private static final int MSG_GET_CAPTURE_SURFACE = 1001;
    private static final int MSG_RELEASE_CAPTURE_SURFACE = 1003;
    private static final int MSG_RELEASE_PREVIEW_SURFACE = 1004;
    private static final int MSG_REPLAY_TIMEOUT = 2;
    private static final int MSG_SET_PREVIEW_SURFACE = 1002;
    private static final int MSG_SEVICE_VERSION = 1000;
    private static final String SERVICE_NAME = "SecCamService";
    private static final String SERVICE_PACKAGE_NAME = "com.qualcomm.qti.seccamservice";
    private static final int SERVICE_READY_TIMEOUT = 2;
    private static HandlerThread handlerThread_ = null;
    private static SecCamServiceClient instance_ = null;
    private final ReentrantLock accessLock_ = new ReentrantLock();
    private Messenger activityMessenger_ = null;
    private Messenger bundServiceMessenger_;
    private ClientCallback callback_ = null;
    private Surface captureSurface_ = null;
    private WeakReference<Context> context_;
    private HashMap<Long, FrameCallback> frameCallbacks_ = null;
    private CountDownLatch replayReadyLatch_ = null;
    private boolean result_ = false;
    private boolean serviceConnected_ = false;
    private boolean serviceConnecting_ = false;
    private ServiceConnection serviceConnection_ = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            Log.d(SecCamServiceClient.LOG_TAG, "onServiceDisconnected");
            SecCamServiceClient.this.bundServiceMessenger_ = null;
            SecCamServiceClient.this.serviceDisonnecting_ = false;
            SecCamServiceClient.this.serviceConnected_ = false;
            SecCamServiceClient.this.serviceReadyLatch_.countDown();
            if (SecCamServiceClient.this.callback_ != null) {
                SecCamServiceClient.this.callback_.serviceDisconnected();
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(SecCamServiceClient.LOG_TAG, "onServiceConnected");
            SecCamServiceClient.this.bundServiceMessenger_ = new Messenger(service);
            SecCamServiceClient.this.serviceConnecting_ = false;
            SecCamServiceClient.this.serviceConnected_ = true;
            SecCamServiceClient.this.serviceReadyLatch_.countDown();
            if (SecCamServiceClient.this.callback_ != null) {
                SecCamServiceClient.this.callback_.serviceConnected();
            }
        }
    };
    private boolean serviceDisonnecting_ = false;
    private CountDownLatch serviceReadyLatch_ = null;
    private ServiceVersion serviceVersion_ = new ServiceVersion();
    private Long surfaceId_;

    public interface ClientCallback {
        void serviceConnected();

        void serviceDisconnected();
    }

    public static class ServiceVersion {
        public int jniVerMaj_;
        public int jniVerMin_;
        public int serviceVerMaj_;
        public int serviceVerMin_;
        public int taVerMaj_;
        public int taVerMin_;
    }

    static {
        Integer valueOf = Integer.valueOf(1);
        HAL1 = valueOf;
        DESTINATION_QTEE = valueOf;
    }

    private SecCamServiceClient() {
        super(handlerThread_.getLooper());
    }

    public static SecCamServiceClient getInstance() {
        if (instance_ == null) {
            handlerThread_ = new HandlerThread("SecCamServiceClientThread", -2);
            handlerThread_.start();
            instance_ = new SecCamServiceClient();
        }
        return instance_;
    }

    public static Integer getMinSupportedHAL() {
        return getMinSupportedHAL(Integer.valueOf(0));
    }

    public static Integer getMinSupportedHAL(Integer cameraId) {
        Integer minHAL = HAL3;
        try {
            Camera camera = Camera.openLegacy(cameraId.intValue(), 256);
            if (camera.getParameters().get("secure-mode") != null) {
                minHAL = HAL1;
            }
            camera.release();
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getMinSupportedHAL - HAL1 probe failed (Camera:");
            stringBuilder.append(cameraId);
            stringBuilder.append("),");
            stringBuilder.append(e);
            Log.d(LOG_TAG, stringBuilder.toString());
        }
        return minHAL;
    }

    public boolean start(Context context, ClientCallback callback) {
        return start(context, callback, null, 0, DESTINATION_QTEE.intValue());
    }

    public boolean start(Context context, ClientCallback callback, String taName, int taBufferSize) {
        return start(context, callback, taName, taBufferSize, DESTINATION_QTEE.intValue());
    }

    public boolean start(Context context, ClientCallback callback, String taName, int taBufferSize, int destination) {
        this.accessLock_.lock();
        if (!this.serviceConnected_) {
            this.serviceConnecting_ = true;
            this.serviceReadyLatch_ = new CountDownLatch(1);
            this.frameCallbacks_ = new HashMap();
            this.context_ = new WeakReference(context);
            this.callback_ = callback;
            this.activityMessenger_ = new Messenger((Handler) this);
            Intent intent = new Intent("com.qualcomm.qti.seccamservice.SecCamService");
            intent.setPackage(SERVICE_PACKAGE_NAME);
            String str = LOG_TAG;
            if (taName != null) {
                intent.putExtra("TANAME", taName);
                intent.putExtra("TABUFFERSIZE", taBufferSize);
                intent.putExtra("DESTINATION", destination);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Start service, TA: ");
                stringBuilder.append(taName);
                stringBuilder.append(", DESTINATION: ");
                stringBuilder.append(destination);
                Log.d(str, stringBuilder.toString());
            } else {
                Log.d(str, "Start service");
            }
            ((Context) this.context_.get()).getApplicationContext().bindService(intent, this.serviceConnection_, 1);
        }
        this.accessLock_.unlock();
        return true;
    }

    public void release() {
        this.accessLock_.lock();
        if (this.serviceConnected_) {
            new Intent("com.qualcomm.qti.seccamservice.SecCamService").setPackage(SERVICE_PACKAGE_NAME);
            this.serviceDisonnecting_ = true;
            this.serviceReadyLatch_ = new CountDownLatch(1);
            Log.d(LOG_TAG, "unbindService");
            ((Context) this.context_.get()).getApplicationContext().unbindService(this.serviceConnection_);
            this.serviceConnected_ = false;
            this.context_ = null;
            this.callback_ = null;
        }
        this.accessLock_.unlock();
    }

    public boolean isSeviceConnected() {
        String str = LOG_TAG;
        if (this.serviceConnecting_ || this.serviceDisonnecting_) {
            try {
                if (!this.serviceReadyLatch_.await(2, TimeUnit.SECONDS)) {
                    Log.d(str, "isSeviceConnected - ERROR: tmeout!");
                    return false;
                }
            } catch (InterruptedException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("isSeviceConnected - ERROR: ");
                stringBuilder.append(ex);
                Log.d(str, stringBuilder.toString());
                ex.printStackTrace();
                return false;
            }
        }
        return this.serviceConnected_;
    }

    public SurfaceInfo getSecureCameraSurface(int cameraId, int width, int height, int format, int numOfBuffers) {
        RemoteException ex;
        int i;
        int i2;
        int i3;
        StringBuilder stringBuilder;
        int i4;
        int i5;
        String str = "getSecureCameraSurface - ERROR: ";
        String str2 = LOG_TAG;
        this.accessLock_.lock();
        if (isSeviceConnected()) {
            try {
                Log.d(str2, "Send MSG: getSecureCameraSurface");
                Bundle out_bundle = new Bundle();
                try {
                    out_bundle.putInt(MSG_DATA_CAMERAID, cameraId);
                    try {
                        out_bundle.putInt("width", width);
                    } catch (RemoteException e) {
                        ex = e;
                        i = height;
                        i2 = format;
                        i3 = numOfBuffers;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(ex);
                        Log.d(str2, stringBuilder.toString());
                        ex.printStackTrace();
                        this.accessLock_.unlock();
                        return null;
                    }
                } catch (RemoteException e2) {
                    ex = e2;
                    i4 = width;
                    i = height;
                    i2 = format;
                    i3 = numOfBuffers;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(ex);
                    Log.d(str2, stringBuilder.toString());
                    ex.printStackTrace();
                    this.accessLock_.unlock();
                    return null;
                }
                try {
                    out_bundle.putInt("height", height);
                    try {
                        out_bundle.putInt("format", format);
                        try {
                            out_bundle.putInt(MSG_DATA_NUMOFBUFFERS, numOfBuffers);
                            Message msg = Message.obtain();
                            msg.what = 1001;
                            msg.setData(out_bundle);
                            msg.replyTo = this.activityMessenger_;
                            this.replayReadyLatch_ = new CountDownLatch(1);
                            this.captureSurface_ = null;
                            this.surfaceId_ = Long.valueOf(0);
                            this.bundServiceMessenger_.send(msg);
                            try {
                                if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                                    SurfaceInfo surfaceInfo = new SurfaceInfo(this.captureSurface_, this.surfaceId_);
                                    this.captureSurface_ = null;
                                    this.surfaceId_ = Long.valueOf(0);
                                    this.accessLock_.unlock();
                                    return surfaceInfo;
                                }
                                Log.d(str2, "getSecureCameraSurface - ERROR: tmeout!");
                            } catch (InterruptedException ex2) {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str);
                                stringBuilder2.append(ex2);
                                Log.d(str2, stringBuilder2.toString());
                                ex2.printStackTrace();
                            }
                        } catch (RemoteException e3) {
                            ex = e3;
                        }
                    } catch (RemoteException e4) {
                        ex = e4;
                        i3 = numOfBuffers;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(ex);
                        Log.d(str2, stringBuilder.toString());
                        ex.printStackTrace();
                        this.accessLock_.unlock();
                        return null;
                    }
                } catch (RemoteException e5) {
                    ex = e5;
                    i2 = format;
                    i3 = numOfBuffers;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(ex);
                    Log.d(str2, stringBuilder.toString());
                    ex.printStackTrace();
                    this.accessLock_.unlock();
                    return null;
                }
            } catch (RemoteException e6) {
                ex = e6;
                i5 = cameraId;
                i4 = width;
                i = height;
                i2 = format;
                i3 = numOfBuffers;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(ex);
                Log.d(str2, stringBuilder.toString());
                ex.printStackTrace();
                this.accessLock_.unlock();
                return null;
            }
        }
        i5 = cameraId;
        i4 = width;
        i = height;
        i2 = format;
        i3 = numOfBuffers;
        this.accessLock_.unlock();
        return null;
    }

    public boolean setSecurePreviewSurface(Surface previewSurface, SecureSurface captureSurface, int width, int height, int format, int rotation, int numOfBuffers) {
        String str = "setSecurePreviewSurface - ERROR: ";
        String str2 = LOG_TAG;
        this.accessLock_.lock();
        if (!(!isSeviceConnected() || previewSurface == null || captureSurface == null)) {
            try {
                Log.d(str2, "Send MSG: setSecurePreviewSurface");
                Bundle out_bundle = new Bundle();
                out_bundle.putParcelable(MSG_DATA_PSURFACE, previewSurface);
                out_bundle.putLong(MSG_DATA_CSURFACEID, captureSurface.getCaptureSurfaceId().longValue());
                out_bundle.putInt("width", width);
                out_bundle.putInt("height", height);
                out_bundle.putInt("format", format);
                out_bundle.putInt(MSG_DATA_ROTATION, rotation);
                out_bundle.putInt(MSG_DATA_NUMOFBUFFERS, numOfBuffers);
                Message msg = Message.obtain();
                msg.what = 1002;
                msg.setData(out_bundle);
                msg.replyTo = this.activityMessenger_;
                this.replayReadyLatch_ = new CountDownLatch(1);
                this.result_ = true;
                this.bundServiceMessenger_.send(msg);
                try {
                    if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                        this.accessLock_.unlock();
                        return this.result_;
                    }
                    Log.d(str2, "setSecurePreviewSurface - ERROR: timeout!");
                } catch (InterruptedException ex) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(ex);
                    Log.d(str2, stringBuilder.toString());
                    ex.printStackTrace();
                }
            } catch (RemoteException ex2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(ex2);
                Log.d(str2, stringBuilder2.toString());
                ex2.printStackTrace();
            }
        }
        this.accessLock_.unlock();
        return false;
    }

    public boolean releaseCaptureSurface(SecureSurface secureSurface) {
        String str = "releaseCaptureSurface - ERROR: ";
        String str2 = LOG_TAG;
        this.accessLock_.lock();
        if (!(!isSeviceConnected() || secureSurface == null || secureSurface.getCaptureSurface() == null)) {
            try {
                Log.d(str2, "Send MSG: releaseCaptureSurface");
                Bundle out_bundle = new Bundle();
                out_bundle.putLong(MSG_DATA_CSURFACEID, secureSurface.getCaptureSurfaceId().longValue());
                Message msg = Message.obtain();
                msg.what = 1003;
                msg.setData(out_bundle);
                msg.replyTo = this.activityMessenger_;
                this.replayReadyLatch_ = new CountDownLatch(1);
                this.result_ = true;
                this.bundServiceMessenger_.send(msg);
                try {
                    if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                        this.accessLock_.unlock();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("releaseCaptureSurface - return ");
                        stringBuilder.append(this.result_);
                        Log.d(str2, stringBuilder.toString());
                        return this.result_;
                    }
                    Log.d(str2, "releaseCaptureSurface - ERROR: timeout!");
                } catch (InterruptedException ex) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(ex);
                    Log.d(str2, stringBuilder2.toString());
                    ex.printStackTrace();
                }
            } catch (RemoteException ex2) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(ex2);
                Log.d(str2, stringBuilder3.toString());
                ex2.printStackTrace();
            }
        }
        this.accessLock_.unlock();
        return false;
    }

    public boolean releasePreviewSurface(Surface previewSurface, SurfaceInfo captureSurfaceInfo) {
        String str = "releasePreviewSurface - ERROR: ";
        String str2 = LOG_TAG;
        this.accessLock_.lock();
        if (!(!isSeviceConnected() || captureSurfaceInfo == null || previewSurface == null)) {
            try {
                Log.d(str2, "Send MSG: releasePreviewSurface");
                Bundle out_bundle = new Bundle();
                out_bundle.putParcelable(MSG_DATA_PSURFACE, previewSurface);
                out_bundle.putLong(MSG_DATA_CSURFACEID, captureSurfaceInfo.getSurfaceId().longValue());
                Message msg = Message.obtain();
                msg.what = 1004;
                msg.setData(out_bundle);
                msg.replyTo = this.activityMessenger_;
                this.replayReadyLatch_ = new CountDownLatch(1);
                this.result_ = true;
                this.bundServiceMessenger_.send(msg);
                try {
                    if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                        this.accessLock_.unlock();
                        return this.result_;
                    }
                    Log.d(str2, "releasePreviewSurface - ERROR: timeout!");
                } catch (InterruptedException ex) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(ex);
                    Log.d(str2, stringBuilder.toString());
                    ex.printStackTrace();
                }
            } catch (RemoteException ex2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(ex2);
                Log.d(str2, stringBuilder2.toString());
                ex2.printStackTrace();
            }
        }
        this.accessLock_.unlock();
        return false;
    }

    public ServiceVersion getServiceVersion() {
        String str = LOG_TAG;
        this.accessLock_.lock();
        if (isSeviceConnected()) {
            try {
                Log.d(str, "Send MSG: getServiceVersion");
                Message msg = Message.obtain(null, 1000, 0, 0);
                msg.replyTo = this.activityMessenger_;
                this.replayReadyLatch_ = new CountDownLatch(1);
                this.bundServiceMessenger_.send(msg);
                try {
                    if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                        this.accessLock_.unlock();
                        return this.serviceVersion_;
                    }
                    Log.d(str, "getServiceVersion - ERROR: timeout!");
                } catch (InterruptedException ex) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getServiceVersion - ERROR: ");
                    stringBuilder.append(ex);
                    Log.d(str, stringBuilder.toString());
                    ex.printStackTrace();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        this.accessLock_.unlock();
        return null;
    }

    public boolean enableFrameCallback(SecureSurface captureSurface, FrameCallback callback, int returnParamsSize) {
        String str = "enableFrameCallback - ERROR: ";
        String str2 = LOG_TAG;
        this.accessLock_.lock();
        if (!(!isSeviceConnected() || captureSurface == null || captureSurface.getCaptureSurface() == null || callback == null)) {
            try {
                Log.d(str2, "Send MSG: enableFrameCallback");
                Bundle out_bundle = new Bundle();
                out_bundle.putInt("timeout", 100);
                out_bundle.putLong(MSG_DATA_CSURFACEID, captureSurface.getCaptureSurfaceId().longValue());
                out_bundle.putInt(MSG_DATA_RETURNPARAMSSIZE, returnParamsSize);
                Message msg = Message.obtain();
                msg.what = 1005;
                msg.setData(out_bundle);
                msg.replyTo = this.activityMessenger_;
                this.replayReadyLatch_ = new CountDownLatch(1);
                this.result_ = true;
                this.bundServiceMessenger_.send(msg);
                try {
                    if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                        StringBuilder stringBuilder;
                        if (this.result_) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("enableFrameCallback - Register callback, surfaceId: ");
                            stringBuilder.append(captureSurface.getCaptureSurfaceId());
                            Log.d(str2, stringBuilder.toString());
                            this.frameCallbacks_.put(captureSurface.getCaptureSurfaceId(), callback);
                        }
                        this.accessLock_.unlock();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("enableFrameCallback return ");
                        stringBuilder.append(this.result_);
                        Log.d(str2, stringBuilder.toString());
                        return this.result_;
                    }
                    Log.d(str2, "enableFrameCallback - ERROR: timeout!");
                } catch (InterruptedException ex) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(ex);
                    Log.d(str2, stringBuilder2.toString());
                    ex.printStackTrace();
                }
            } catch (RemoteException ex2) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(ex2);
                Log.d(str2, stringBuilder3.toString());
                ex2.printStackTrace();
            }
        }
        this.accessLock_.unlock();
        return false;
    }

    public void dispatchVendorCommand(int commandId, Bundle bundle) {
        String str = "dispatchVendorCommand - ERROR: ";
        String str2 = LOG_TAG;
        this.accessLock_.lock();
        if (isSeviceConnected()) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Send MSG: dispatchVendorCommand ");
                stringBuilder.append(commandId);
                Log.d(str2, stringBuilder.toString());
                Message msg = Message.obtain();
                msg.what = commandId;
                if (bundle != null) {
                    msg.setData(bundle);
                }
                msg.replyTo = this.activityMessenger_;
                this.replayReadyLatch_ = new CountDownLatch(1);
                this.bundServiceMessenger_.send(msg);
                try {
                    if (this.replayReadyLatch_.await(2, TimeUnit.SECONDS)) {
                        this.accessLock_.unlock();
                        return;
                    }
                    Log.d(str2, "dispatchVendorCommand - ERROR: timeout!");
                } catch (InterruptedException ex) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(ex);
                    Log.d(str2, stringBuilder2.toString());
                    ex.printStackTrace();
                }
            } catch (RemoteException ex2) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(ex2);
                Log.d(str2, stringBuilder3.toString());
                ex2.printStackTrace();
            }
        }
        this.accessLock_.unlock();
    }

    public void handleMessage(Message msg) {
        String str = LOG_TAG;
        Log.d(str, "handleMessage");
        int i = msg.what;
        String str2 = MSG_DATA_SURFACEID;
        String str3 = "result";
        Bundle in_bundle;
        StringBuilder stringBuilder;
        switch (i) {
            case 1000:
                in_bundle = msg.getData();
                this.serviceVersion_.serviceVerMaj_ = in_bundle.getInt(BiometricConnect.MSG_VER_SER_MAJ);
                this.serviceVersion_.serviceVerMin_ = in_bundle.getInt(BiometricConnect.MSG_VER_SER_MIN);
                this.serviceVersion_.jniVerMaj_ = in_bundle.getInt("jni_ver_maj");
                this.serviceVersion_.jniVerMin_ = in_bundle.getInt("jni_ver_min");
                this.serviceVersion_.taVerMaj_ = in_bundle.getInt("ta_ver_maj");
                this.serviceVersion_.taVerMin_ = in_bundle.getInt("ta_ver_min");
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleMessage - MSG_SEVICE_VERSION:SVC: v");
                stringBuilder.append(this.serviceVersion_.serviceVerMaj_);
                str3 = ".";
                stringBuilder.append(str3);
                stringBuilder.append(this.serviceVersion_.serviceVerMin_);
                stringBuilder.append(", JNI: v");
                stringBuilder.append(this.serviceVersion_.jniVerMaj_);
                stringBuilder.append(str3);
                stringBuilder.append(this.serviceVersion_.jniVerMin_);
                stringBuilder.append(", TA: v");
                stringBuilder.append(this.serviceVersion_.taVerMaj_);
                stringBuilder.append(str3);
                stringBuilder.append(this.serviceVersion_.taVerMin_);
                Log.d(str, stringBuilder.toString());
                this.replayReadyLatch_.countDown();
                return;
            case 1001:
                in_bundle = msg.getData();
                in_bundle.setClassLoader(getClass().getClassLoader());
                Parcelable parcelable = in_bundle.getParcelable("SURFACE");
                if (parcelable instanceof Surface) {
                    this.captureSurface_ = (Surface) parcelable;
                    this.surfaceId_ = Long.valueOf(in_bundle.getLong(str2));
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("handleMessage - MSG_GET_CAPTURE_SURFACE:");
                    stringBuilder.append(this.captureSurface_.toString());
                    stringBuilder.append(", surfaceId: ");
                    stringBuilder.append(this.surfaceId_);
                    Log.d(str, stringBuilder.toString());
                }
                this.replayReadyLatch_.countDown();
                return;
            case 1002:
            case 1003:
            case 1004:
                this.result_ = msg.getData().getBoolean(str3);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleMessage - ");
                stringBuilder.append(msg.toString());
                stringBuilder.append(":");
                stringBuilder.append(this.result_);
                Log.d(str, stringBuilder.toString());
                this.replayReadyLatch_.countDown();
                return;
            case 1005:
                this.result_ = msg.getData().getBoolean(str3);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleMessage - 1005:");
                stringBuilder.append(this.result_);
                Log.d(str, stringBuilder.toString());
                this.replayReadyLatch_.countDown();
                return;
            case 1006:
                in_bundle = msg.getData();
                if (in_bundle.getBoolean(str3)) {
                    FrameInfo frameInfo = new FrameInfo();
                    long surfaceId = in_bundle.getLong(str2);
                    frameInfo.frameNumber_ = in_bundle.getLong(MSG_DATA_FRAMENUMBER);
                    frameInfo.timeStamp_ = in_bundle.getLong(MSG_DATA_TIMESTAMP);
                    frameInfo.width_ = in_bundle.getInt("width");
                    frameInfo.height_ = in_bundle.getInt("height");
                    frameInfo.stride_ = in_bundle.getInt("stride");
                    frameInfo.format_ = in_bundle.getInt("format");
                    byte[] returnParams = in_bundle.getByteArray(MSG_DATA_RETURNPARAMS);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("handleMessage - MSG_FRAME_CALLBACK: SurfaceId:");
                    stringBuilder2.append(surfaceId);
                    stringBuilder2.append(" FrameId: ");
                    stringBuilder2.append(frameInfo.frameNumber_);
                    Log.d(str, stringBuilder2.toString());
                    FrameCallback callback = (FrameCallback) this.frameCallbacks_.get(Long.valueOf(surfaceId));
                    if (callback != null) {
                        callback.onSecureFrameAvalable(frameInfo, returnParams);
                        return;
                    }
                    return;
                }
                return;
            default:
                SecCamServiceVendorClient vendorClient = new SecCamServiceVendorClient();
                if (SecCamServiceVendorClient.handleVendorMessage(msg)) {
                    this.replayReadyLatch_.countDown();
                    return;
                }
                return;
        }
    }
}
