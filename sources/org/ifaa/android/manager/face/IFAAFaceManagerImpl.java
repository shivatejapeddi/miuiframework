package org.ifaa.android.manager.face;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Slog;
import com.android.qualcomm.qti.seccamapi.SecCamServiceClient;
import com.android.qualcomm.qti.seccamapi.SecCamServiceClient.ClientCallback;
import com.android.qualcomm.qti.seccamapi.SecureSurface.FrameCallback;
import com.android.qualcomm.qti.seccamapi.SecureSurface.FrameInfo;
import java.util.concurrent.Semaphore;
import miui.util.FeatureParser;
import org.ifaa.android.manager.IIFAAService;
import org.ifaa.android.manager.IIFAAService.Stub;
import org.ifaa.android.manager.face.IFAAFaceManager.AuthenticatorCallback;

public class IFAAFaceManagerImpl extends IFAAFaceManagerV2 implements ClientCallback, FrameCallback {
    private static final boolean DEBUG = false;
    public static final Integer DESTINATION_MLVM = Integer.valueOf(2);
    public static final Integer DESTINATION_QTEE;
    private static volatile IFAAFaceManagerImpl INSTANCE = null;
    private static final boolean IS_SUPPORT_2DFA = isSupport2dfa();
    private static final Integer PREVIEW_CAMERAID;
    private static final Integer PREVIEW_FORMAT = Integer.valueOf(35);
    private static final Integer PREVIEW_HEIGHT = Integer.valueOf(480);
    private static final Integer PREVIEW_NUMOFBUFFERS = Integer.valueOf(4);
    private static final Integer PREVIEW_SURFACE_NUMOFBUFFERS = Integer.valueOf(3);
    private static final Integer PREVIEW_WIDTH = Integer.valueOf(640);
    private static final Integer SECCAM_STOP_TIMEOUT = Integer.valueOf(600);
    private static final String TAG = "IFAAFaceManagerImplV2";
    private static final int TZ_APP_BUFFER_SIZE = 8192;
    private static final String TZ_APP_NAME = "seccam2d";
    public static final int UPGRADE_ACTION_RESET = 0;
    public static final int UPGRADE_ACTION_UPDATE = 2;
    public static final int UPGRADE_ACTION_WRITE = 1;
    private static final String mClassName = "org.ifaa.android.manager.IFAAService";
    private static ServiceConnection mConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            StringBuilder stringBuilder;
            String str = IFAAFaceManagerImpl.TAG;
            try {
                service.linkToDeath(IFAAFaceManagerImpl.mDeathRecipient, 0);
            } catch (RemoteException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("linkToDeath fail: ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            }
            IFAAFaceManagerImpl.mService = Stub.asInterface(service);
            try {
                IFAAFaceManagerImpl.mService.faceGetCellinfo();
            } catch (RemoteException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("call ci info fail: ");
                stringBuilder.append(e2);
                Slog.e(str, stringBuilder.toString());
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            if (IFAAFaceManagerImpl.mContext != null) {
                Slog.i(IFAAFaceManagerImpl.TAG, "re-bind to IFAA service");
                IFAAFaceManagerImpl.mContext.getApplicationContext().unbindService(IFAAFaceManagerImpl.mConn);
                IFAAFaceManagerImpl.initService();
            }
        }
    };
    private static Context mContext = null;
    private static DeathRecipient mDeathRecipient = new DeathRecipient() {
        public void binderDied() {
            if (IFAAFaceManagerImpl.mService != null) {
                Slog.d(IFAAFaceManagerImpl.TAG, "binderDied, unlink service");
                IFAAFaceManagerImpl.mService.asBinder().unlinkToDeath(IFAAFaceManagerImpl.mDeathRecipient, 0);
            }
        }
    };
    private static final String mPackageName = "com.tencent.soter.soterserver";
    private static IIFAAService mService = null;
    protected int frameCallbackReturnParamsSize_ = 16;
    protected byte[] frameCallbackReturnParams_ = new byte[this.frameCallbackReturnParamsSize_];
    private Semaphore mCameraOperationLock = new Semaphore(1);
    private final ConditionVariable mSeccamStopping = new ConditionVariable(true);
    private IFAASecureCamera2API previewCamera_ = null;

    private class SeccamStopTask extends AsyncTask<String, Void, Boolean> {
        private SeccamStopTask() {
        }

        /* synthetic */ SeccamStopTask(IFAAFaceManagerImpl x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            Slog.d(IFAAFaceManagerImpl.TAG, "onPreExecute");
        }

        /* Access modifiers changed, original: protected|varargs */
        public Boolean doInBackground(String... tmpStr) {
            String str = IFAAFaceManagerImpl.TAG;
            Slog.d(str, "doInBackground");
            try {
                IFAAFaceManagerImpl.this.mCameraOperationLock.acquire();
                Slog.i(str, "seccam closeCamera+++");
                if (IFAAFaceManagerImpl.this.previewCamera_ != null) {
                    IFAAFaceManagerImpl.this.previewCamera_.stop();
                    IFAAFaceManagerImpl.this.previewCamera_ = null;
                } else {
                    Slog.e(str, "Secure camera already stopped.. ignoring stoptask");
                }
                Slog.i(str, "seccam closeCamera---");
                IFAAFaceManagerImpl.this.mSeccamStopping.open();
                IFAAFaceManagerImpl.this.mCameraOperationLock.release();
                return Boolean.TRUE;
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while trying to lock closeCamera.", e);
            } catch (Throwable th) {
                IFAAFaceManagerImpl.this.mSeccamStopping.open();
                IFAAFaceManagerImpl.this.mCameraOperationLock.release();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Boolean result) {
            Slog.d(IFAAFaceManagerImpl.TAG, "onPostExecute");
        }
    }

    static {
        Integer valueOf = Integer.valueOf(1);
        DESTINATION_QTEE = valueOf;
        PREVIEW_CAMERAID = valueOf;
    }

    public static IFAAFaceManagerV2 getInstance(Context context) {
        Context appContext = context.getApplicationContext();
        Slog.d(TAG, "getInstance+++");
        if ((INSTANCE == null || appContext != mContext) && IS_SUPPORT_2DFA) {
            synchronized (IFAAFaceManagerImpl.class) {
                Slog.d(TAG, "A new instance is required");
                if (mContext != null) {
                    mContext.getApplicationContext().unbindService(mConn);
                    SecCamServiceClient.getInstance().release();
                }
                INSTANCE = new IFAAFaceManagerImpl();
                mContext = appContext;
                initService();
                SecCamServiceClient.getInstance().start(mContext, INSTANCE, TZ_APP_NAME, 8192, DESTINATION_MLVM.intValue());
            }
        }
        Slog.d(TAG, "getInstance---");
        return INSTANCE;
    }

    private static void initService() {
        Intent intent = new Intent();
        intent.setClassName(mPackageName, mClassName);
        if (!mContext.getApplicationContext().bindService(intent, mConn, 1)) {
            Slog.e(TAG, "cannot bind service: org.ifaa.android.manager.IFAAService");
        }
    }

    private static boolean isSupport2dfa() {
        boolean is_feature_enabled = FeatureParser.getInteger("ifaa_2dfa_support", 0) == 1;
        boolean is_hyp_enabled = "enable".equals(SystemProperties.get("ro.boot.hypvm", ""));
        if (is_feature_enabled && is_hyp_enabled) {
            return true;
        }
        return false;
    }

    private boolean openCamera() {
        boolean result = false;
        String str = TAG;
        Slog.i(str, "seccam openCamera+++");
        if (this.mSeccamStopping.block((long) SECCAM_STOP_TIMEOUT.intValue())) {
            Slog.i(str, "SeccamStopping pass");
        } else {
            Slog.i(str, "Wait for seccam stop timeout");
        }
        try {
            this.mCameraOperationLock.acquire();
            String str2 = "seccam openCamera---";
            if (this.previewCamera_ != null) {
                Slog.e(str, "Secure camera already running.. ignoring StartTask");
                Slog.i(str, str2);
                this.mCameraOperationLock.release();
                return true;
            }
            this.previewCamera_ = new IFAASecureCamera2API(mContext, this);
            if (this.previewCamera_.start(PREVIEW_CAMERAID.intValue(), null, PREVIEW_WIDTH, PREVIEW_HEIGHT, PREVIEW_FORMAT, PREVIEW_NUMOFBUFFERS, PREVIEW_SURFACE_NUMOFBUFFERS).booleanValue()) {
                result = true;
            }
            Slog.i(str, str2);
            this.mCameraOperationLock.release();
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock openCamera.", e);
        } catch (Throwable th) {
            this.mCameraOperationLock.release();
        }
    }

    private boolean closeCamera() {
        this.mSeccamStopping.close();
        new SeccamStopTask(this, null).execute((Object[]) new String[0]);
        return true;
    }

    public void serviceConnected() {
        Slog.i(TAG, "seccamservice serviceConnected");
    }

    public void serviceDisconnected() {
        Slog.i(TAG, "seccamservice serviceDisconnected");
        SecCamServiceClient.getInstance().release();
        SecCamServiceClient.getInstance().start(mContext, INSTANCE, TZ_APP_NAME, 8192, DESTINATION_MLVM.intValue());
    }

    private int byte4ToInt(byte[] bytes, int off) {
        return ((((bytes[off + 3] & 255) << 24) | ((bytes[off + 2] & 255) << 16)) | ((bytes[off + 1] & 255) << 8)) | (bytes[off] & 255);
    }

    public void onSecureFrameAvalable(FrameInfo frameInfo, byte[] returnParams) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CameraActivity::onSecureFrameAvalable");
        stringBuilder.append(returnParams.length);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Slog.d(str, stringBuilder2);
        if (returnParams.length == 16) {
            StringBuffer sb = new StringBuffer(returnParams.length * 3);
            int length = returnParams.length;
            for (int i = 0; i < length; i++) {
                sb.append(String.format("%02x ", new Object[]{Byte.valueOf(returnParams[i])}));
            }
            Slog.i(str, sb.toString());
            try {
                if (this.mCameraOperationLock.tryAcquire()) {
                    if (this.previewCamera_ != null) {
                        this.previewCamera_.rectFromTEECallback(new Point(byte4ToInt(returnParams, 0), byte4ToInt(returnParams, 4)), new Point(byte4ToInt(returnParams, 8), byte4ToInt(returnParams, 12)));
                    } else {
                        Slog.e(str, "Secure camera already stopped.. ignoring rectFromTEECallback");
                    }
                    this.mCameraOperationLock.release();
                } else {
                    Slog.d(str, "onSecureFrameAvalable Semaphore not Acquired ");
                }
            } catch (Exception e) {
                Slog.e(str, "rectFromTEECallback Exception caught", e);
            }
        }
    }

    private void invokeCallback(AuthenticatorCallback callback, int status) {
        if (status == 1) {
            callback.onAuthenticationSucceeded();
        } else {
            callback.onAuthenticationFailed(status);
        }
    }

    public void enroll(String sessionId, int flags, AuthenticatorCallback callback) {
        int status = -1;
        try {
            openCamera();
            status = mService.faceEnroll(sessionId, flags);
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("enroll fail: ");
            stringBuilder.append(e);
            Slog.e(str, stringBuilder.toString());
        } catch (Throwable th) {
            invokeCallback(callback, -1);
            closeCamera();
        }
        invokeCallback(callback, status);
        closeCamera();
    }

    public void authenticate(int reqId, int flags, AuthenticatorCallback callback) {
        authenticate(String.valueOf(reqId), flags, callback);
    }

    public int cancel(int reqId) {
        return cancel(String.valueOf(reqId));
    }

    public void authenticate(String sessionId, int flags, AuthenticatorCallback callback) {
        int status = -1;
        try {
            openCamera();
            status = mService.faceAuthenticate_v2(sessionId, flags);
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("authenticat_v2 fail: ");
            stringBuilder.append(e);
            Slog.e(str, stringBuilder.toString());
        } catch (Throwable th) {
            invokeCallback(callback, -1);
            closeCamera();
        }
        invokeCallback(callback, status);
        closeCamera();
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:109:0x01c6=Splitter:B:109:0x01c6, B:98:0x01ad=Splitter:B:98:0x01ad, B:85:0x017e=Splitter:B:85:0x017e, B:120:0x01df=Splitter:B:120:0x01df} */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x01e7 A:{SYNTHETIC, Splitter:B:123:0x01e7} */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x01f3 A:{SYNTHETIC, Splitter:B:128:0x01f3} */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x01ce A:{SYNTHETIC, Splitter:B:112:0x01ce} */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01da A:{SYNTHETIC, Splitter:B:117:0x01da} */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01b5 A:{SYNTHETIC, Splitter:B:101:0x01b5} */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01c1 A:{SYNTHETIC, Splitter:B:106:0x01c1} */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0195 A:{SYNTHETIC, Splitter:B:88:0x0195} */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01a1 A:{SYNTHETIC, Splitter:B:93:0x01a1} */
    public void upgrade(java.lang.String r25) {
        /*
        r24 = this;
        r1 = r25;
        r2 = "close file fail";
        r3 = "close zipfile fail";
        r4 = 3;
        r0 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r11 = new byte[r0];
        r12 = 0;
        r13 = 0;
        r14 = "IFAAFaceManagerImplV2";
        if (r1 == 0) goto L_0x022d;
    L_0x0011:
        r0 = r25.length();
        if (r0 != 0) goto L_0x0019;
    L_0x0017:
        goto L_0x022d;
    L_0x0019:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r5 = "upgrade ha from path: ";
        r0.append(r5);
        r0.append(r1);
        r0 = r0.toString();
        android.util.Slog.d(r14, r0);
        r5 = mService;	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r6 = 0;
        r7 = "";
        r8 = 0;
        r10 = 0;
        r9 = r11;
        r0 = r5.faceUpgrade(r6, r7, r8, r9, r10);	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r5 = r0;
        if (r5 >= 0) goto L_0x005a;
    L_0x003c:
        r0 = "reset upgrade state fail";
        android.util.Slog.e(r14, r0);	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        if (r12 == 0) goto L_0x004d;
    L_0x0043:
        r12.close();	 Catch:{ IOException -> 0x0047 }
        goto L_0x004d;
    L_0x0047:
        r0 = move-exception;
        r6 = r0;
        r0 = r6;
        android.util.Slog.e(r14, r3);
    L_0x004d:
        if (r13 == 0) goto L_0x0059;
    L_0x004f:
        r13.close();	 Catch:{ IOException -> 0x0053 }
        goto L_0x0059;
    L_0x0053:
        r0 = move-exception;
        r3 = r0;
        r0 = r3;
        android.util.Slog.e(r14, r2);
    L_0x0059:
        return;
    L_0x005a:
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r0.<init>(r1);	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r13 = r0;
        r0 = new java.util.zip.ZipInputStream;	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r0.<init>(r13);	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r12 = r0;
        r0 = r12.getNextEntry();	 Catch:{ FileNotFoundException -> 0x01de, IOException -> 0x01c5, SecurityException -> 0x01ac, RemoteException -> 0x017d }
        r15 = r4;
        r16 = r5;
        r4 = r0;
    L_0x006e:
        if (r4 == 0) goto L_0x0100;
    L_0x0070:
        r0 = r4.getName();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r5 = r4.getSize();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r17 = r5;
        r5 = 0;
        r7 = new java.lang.StringBuilder;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r7.<init>();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r8 = "filename: ";
        r7.append(r8);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r7.append(r0);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r7 = r7.toString();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        android.util.Slog.d(r14, r7);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r7 = 0;
        r8 = r11.length;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r19 = 0;
        r9 = r5;
    L_0x0095:
        r5 = (r9 > r17 ? 1 : (r9 == r17 ? 0 : -1));
        if (r5 >= 0) goto L_0x00ef;
    L_0x0099:
        r5 = r11.length;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r5 = r5 - r8;
        r5 = r12.read(r11, r5, r8);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r16 = r5;
        if (r16 >= 0) goto L_0x00ab;
    L_0x00a3:
        r5 = "file read fail";
        android.util.Slog.e(r14, r5);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r22 = r9;
        goto L_0x00f1;
    L_0x00ab:
        r7 = r7 + r16;
        r8 = r8 - r16;
        if (r16 == 0) goto L_0x00c3;
    L_0x00b1:
        r5 = (long) r7;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r5 = r5 + r9;
        r5 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1));
        if (r5 == 0) goto L_0x00c3;
    L_0x00b7:
        r5 = r11.length;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r5 = (double) r5;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r20 = 4584304132692975288; // 0x3f9eb851eb851eb8 float:-3.218644E26 double:0.03;
        r5 = r5 * r20;
        r5 = (int) r5;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        if (r8 >= r5) goto L_0x0095;
    L_0x00c3:
        r5 = mService;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r6 = 1;
        r1 = (int) r9;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r20 = r7;
        r7 = r0;
        r21 = r8;
        r8 = r1;
        r22 = r9;
        r9 = r11;
        r10 = r20;
        r1 = r5.faceUpgrade(r6, r7, r8, r9, r10);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r16 = r1;
        if (r16 >= 0) goto L_0x00e4;
    L_0x00da:
        r1 = "file transfer error";
        android.util.Slog.e(r14, r1);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r7 = r20;
        r8 = r21;
        goto L_0x00f1;
    L_0x00e4:
        r7 = r20;
        r5 = (long) r7;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r9 = r22 + r5;
        r7 = 0;
        r1 = r11.length;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r8 = r1;
        r1 = r25;
        goto L_0x0095;
    L_0x00ef:
        r22 = r9;
    L_0x00f1:
        r12.closeEntry();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r1 = r12.getNextEntry();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r4 = r1;
        r15 = r15 + -1;
        r1 = r25;
        goto L_0x006e;
    L_0x0100:
        r12.close();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r12 = 0;
        r13.close();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r13 = 0;
        if (r15 <= 0) goto L_0x0137;
    L_0x010a:
        r0 = new java.lang.StringBuilder;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r0.<init>();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r1 = "not all files found, remain: ";
        r0.append(r1);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r0.append(r15);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r0 = r0.toString();	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        android.util.Slog.e(r14, r0);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        if (r12 == 0) goto L_0x012a;
    L_0x0120:
        r12.close();	 Catch:{ IOException -> 0x0124 }
        goto L_0x012a;
    L_0x0124:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r3);
    L_0x012a:
        if (r13 == 0) goto L_0x0136;
    L_0x012c:
        r13.close();	 Catch:{ IOException -> 0x0130 }
        goto L_0x0136;
    L_0x0130:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r2);
    L_0x0136:
        return;
    L_0x0137:
        r5 = mService;	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r6 = 2;
        r7 = "";
        r8 = 0;
        r10 = 0;
        r9 = r11;
        r0 = r5.faceUpgrade(r6, r7, r8, r9, r10);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
        r1 = r0;
        if (r1 >= 0) goto L_0x014b;
    L_0x0146:
        r0 = "update fail";
        android.util.Slog.d(r14, r0);	 Catch:{ FileNotFoundException -> 0x0174, IOException -> 0x0171, SecurityException -> 0x016e, RemoteException -> 0x016b, all -> 0x0167 }
    L_0x014b:
        if (r12 == 0) goto L_0x0157;
    L_0x014d:
        r12.close();	 Catch:{ IOException -> 0x0151 }
        goto L_0x0157;
    L_0x0151:
        r0 = move-exception;
        r4 = r0;
        r0 = r4;
        android.util.Slog.e(r14, r3);
    L_0x0157:
        if (r13 == 0) goto L_0x0164;
    L_0x0159:
        r13.close();	 Catch:{ IOException -> 0x015d }
    L_0x015c:
        goto L_0x0164;
    L_0x015d:
        r0 = move-exception;
        r3 = r0;
        r0 = r3;
        android.util.Slog.e(r14, r2);
        goto L_0x015c;
    L_0x0164:
        r4 = r15;
        goto L_0x01f7;
    L_0x0167:
        r0 = move-exception;
        r1 = r0;
        goto L_0x0214;
    L_0x016b:
        r0 = move-exception;
        r4 = r15;
        goto L_0x017e;
    L_0x016e:
        r0 = move-exception;
        r4 = r15;
        goto L_0x01ad;
    L_0x0171:
        r0 = move-exception;
        r4 = r15;
        goto L_0x01c6;
    L_0x0174:
        r0 = move-exception;
        r4 = r15;
        goto L_0x01df;
    L_0x0178:
        r0 = move-exception;
        r1 = r0;
        r15 = r4;
        goto L_0x0214;
    L_0x017d:
        r0 = move-exception;
    L_0x017e:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0178 }
        r1.<init>();	 Catch:{ all -> 0x0178 }
        r5 = "upgrade fail: ";
        r1.append(r5);	 Catch:{ all -> 0x0178 }
        r1.append(r0);	 Catch:{ all -> 0x0178 }
        r1 = r1.toString();	 Catch:{ all -> 0x0178 }
        android.util.Slog.e(r14, r1);	 Catch:{ all -> 0x0178 }
        if (r12 == 0) goto L_0x019f;
    L_0x0195:
        r12.close();	 Catch:{ IOException -> 0x0199 }
        goto L_0x019f;
    L_0x0199:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r3);
    L_0x019f:
        if (r13 == 0) goto L_0x01f7;
    L_0x01a1:
        r13.close();	 Catch:{ IOException -> 0x01a5 }
    L_0x01a4:
        goto L_0x01f7;
    L_0x01a5:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r2);
        goto L_0x01a4;
    L_0x01ac:
        r0 = move-exception;
    L_0x01ad:
        r1 = "open file fail, due to no permission";
        android.util.Slog.e(r14, r1);	 Catch:{ all -> 0x0178 }
        if (r12 == 0) goto L_0x01bf;
    L_0x01b5:
        r12.close();	 Catch:{ IOException -> 0x01b9 }
        goto L_0x01bf;
    L_0x01b9:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r3);
    L_0x01bf:
        if (r13 == 0) goto L_0x01f7;
    L_0x01c1:
        r13.close();	 Catch:{ IOException -> 0x01a5 }
        goto L_0x01a4;
    L_0x01c5:
        r0 = move-exception;
    L_0x01c6:
        r1 = "open file fail, due to io error";
        android.util.Slog.e(r14, r1);	 Catch:{ all -> 0x0178 }
        if (r12 == 0) goto L_0x01d8;
    L_0x01ce:
        r12.close();	 Catch:{ IOException -> 0x01d2 }
        goto L_0x01d8;
    L_0x01d2:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r3);
    L_0x01d8:
        if (r13 == 0) goto L_0x01f7;
    L_0x01da:
        r13.close();	 Catch:{ IOException -> 0x01a5 }
        goto L_0x01a4;
    L_0x01de:
        r0 = move-exception;
    L_0x01df:
        r1 = "open file fail, due to no such file";
        android.util.Slog.e(r14, r1);	 Catch:{ all -> 0x0178 }
        if (r12 == 0) goto L_0x01f1;
    L_0x01e7:
        r12.close();	 Catch:{ IOException -> 0x01eb }
        goto L_0x01f1;
    L_0x01eb:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        android.util.Slog.e(r14, r3);
    L_0x01f1:
        if (r13 == 0) goto L_0x01f7;
    L_0x01f3:
        r13.close();	 Catch:{ IOException -> 0x01a5 }
        goto L_0x01a4;
    L_0x01f7:
        r0 = com.android.qualcomm.qti.seccamapi.SecCamServiceClient.getInstance();
        r0.release();
        r5 = com.android.qualcomm.qti.seccamapi.SecCamServiceClient.getInstance();
        r6 = mContext;
        r7 = INSTANCE;
        r9 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r0 = DESTINATION_MLVM;
        r10 = r0.intValue();
        r8 = "seccam2d";
        r5.start(r6, r7, r8, r9, r10);
        return;
    L_0x0214:
        if (r12 == 0) goto L_0x0220;
    L_0x0216:
        r12.close();	 Catch:{ IOException -> 0x021a }
        goto L_0x0220;
    L_0x021a:
        r0 = move-exception;
        r4 = r0;
        r0 = r4;
        android.util.Slog.e(r14, r3);
    L_0x0220:
        if (r13 == 0) goto L_0x022c;
    L_0x0222:
        r13.close();	 Catch:{ IOException -> 0x0226 }
        goto L_0x022c;
    L_0x0226:
        r0 = move-exception;
        r3 = r0;
        r0 = r3;
        android.util.Slog.e(r14, r2);
    L_0x022c:
        throw r1;
    L_0x022d:
        r0 = "upgrade path is null or empty";
        android.util.Slog.e(r14, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ifaa.android.manager.face.IFAAFaceManagerImpl.upgrade(java.lang.String):void");
    }

    public int cancel(String sessionId) {
        try {
            closeCamera();
            return mService.faceCancel_v2(sessionId);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cancel_v2 fail: ");
            stringBuilder.append(e);
            Slog.e(TAG, stringBuilder.toString());
            return 0;
        }
    }

    public byte[] invokeCommand(Context context, byte[] param) {
        try {
            return mService.faceInvokeCommand(param);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invokeCommand fail: ");
            stringBuilder.append(e);
            Slog.e(TAG, stringBuilder.toString());
            return null;
        }
    }
}
