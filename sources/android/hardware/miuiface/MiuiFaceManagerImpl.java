package android.hardware.miuiface;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.miuiface.IMiuiFaceManager.AuthenticationCallback;
import android.hardware.miuiface.IMiuiFaceManager.EnrollmentCallback;
import android.hardware.miuiface.IMiuiFaceManager.LockoutResetCallback;
import android.hardware.miuiface.IMiuiFaceManager.RemovalCallback;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;
import android.util.Slog;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import miui.util.FeatureParser;

public class MiuiFaceManagerImpl implements IMiuiFaceManager {
    private static final int CODE_ADD_LOCKOUT_RESET_CALLBACK = 16;
    private static final int CODE_AUTHENTICATE = 3;
    private static final int CODE_CANCEL_AUTHENTICATE = 4;
    private static final int CODE_CANCEL_ENROLL = 6;
    private static final int CODE_ENROLL = 5;
    private static final int CODE_EXT_CMD = 101;
    private static final int CODE_GET_AUTHENTICATOR_ID = 14;
    private static final int CODE_GET_ENROLLED_FACE_LIST = 9;
    private static final int CODE_GET_VENDOR_INFO = 17;
    private static final int CODE_HAS_ENROLLED_FACES = 12;
    private static final int CODE_POST_ENROLL = 11;
    private static final int CODE_PRE_ENROLL = 10;
    private static final int CODE_PRE_INIT_AUTHEN = 2;
    private static final int CODE_REMOVE = 7;
    private static final int CODE_RENAME = 8;
    private static final int CODE_RESET_TIMEOUT = 15;
    private static boolean DEBUG = true;
    public static final int ERROR_BINDER_CALL = 2100;
    public static final int ERROR_CANCELED = 2000;
    public static final int ERROR_SERVICE_IS_BUSY = 2001;
    public static final int ERROR_SERVICE_IS_IDLE = 2002;
    public static final int ERROR_TIME_OUT = 2003;
    private static final int FACEUNLOCK_CURRENT_USE_INVALID_MODEL = 2;
    private static final int FACEUNLOCK_CURRENT_USE_RGB_MODEL = 1;
    private static final int FACEUNLOCK_CURRENT_USE_STRUCTURE_MODEL = 0;
    private static final String FACEUNLOCK_SUPPORT_SUPERPOWER = "faceunlock_support_superpower";
    private static final String FACE_UNLOCK_3D_HAS_FEATURE = "face_unlock_has_feature_sl";
    private static final String FACE_UNLOCK_HAS_FEATURE = "face_unlock_has_feature";
    private static final String FACE_UNLOCK_HAS_FEATURE_URI = "content://settings/secure/face_unlock_has_feature";
    private static final String FACE_UNLOCK_MODEL = "face_unlock_model";
    private static final String FACE_UNLOCK_VALID_FEATURE = "face_unlock_valid_feature";
    private static final String FACE_UNLOCK_VALID_FEATURE_URI = "content://settings/secure/face_unlock_valid_feature";
    private static volatile IMiuiFaceManager INSTANCE = null;
    public static final int MG_ATTR_BLUR = 20;
    public static final int MG_ATTR_EYE_CLOSE = 22;
    public static final int MG_ATTR_EYE_OCCLUSION = 21;
    public static final int MG_ATTR_MOUTH_OCCLUSION = 23;
    public static final int MG_OPEN_CAMERA_FAIL = 1000;
    public static final int MG_OPEN_CAMERA_SUCCESS = 1001;
    public static final int MG_UNLOCK_BAD_LIGHT = 26;
    public static final int MG_UNLOCK_COMPARE_FAILURE = 12;
    public static final int MG_UNLOCK_DARKLIGHT = 30;
    public static final int MG_UNLOCK_FACE_BAD_QUALITY = 4;
    public static final int MG_UNLOCK_FACE_BLUR = 28;
    public static final int MG_UNLOCK_FACE_DOWN = 18;
    public static final int MG_UNLOCK_FACE_MULTI = 27;
    public static final int MG_UNLOCK_FACE_NOT_COMPLETE = 29;
    public static final int MG_UNLOCK_FACE_NOT_FOUND = 5;
    public static final int MG_UNLOCK_FACE_NOT_ROI = 33;
    public static final int MG_UNLOCK_FACE_OFFSET_BOTTOM = 11;
    public static final int MG_UNLOCK_FACE_OFFSET_LEFT = 8;
    public static final int MG_UNLOCK_FACE_OFFSET_RIGHT = 10;
    public static final int MG_UNLOCK_FACE_OFFSET_TOP = 9;
    public static final int MG_UNLOCK_FACE_RISE = 16;
    public static final int MG_UNLOCK_FACE_ROTATED_LEFT = 15;
    public static final int MG_UNLOCK_FACE_ROTATED_RIGHT = 17;
    public static final int MG_UNLOCK_FACE_SCALE_TOO_LARGE = 7;
    public static final int MG_UNLOCK_FACE_SCALE_TOO_SMALL = 6;
    public static final int MG_UNLOCK_FAILURE = 3;
    public static final int MG_UNLOCK_FEATURE_MISS = 24;
    public static final int MG_UNLOCK_FEATURE_VERSION_ERROR = 25;
    public static final int MG_UNLOCK_HALF_SHADOW = 32;
    public static final int MG_UNLOCK_HIGHLIGHT = 31;
    public static final int MG_UNLOCK_INVALID_ARGUMENT = 1;
    public static final int MG_UNLOCK_INVALID_HANDLE = 2;
    public static final int MG_UNLOCK_KEEP = 19;
    public static final int MG_UNLOCK_LIVENESS_FAILURE = 14;
    public static final int MG_UNLOCK_LIVENESS_WARNING = 13;
    public static final int MG_UNLOCK_OK = 0;
    private static final String POWERMODE_SUPERSAVE_OPEN = "power_supersave_mode_open";
    private static final String POWERMODE_SUPERSAVE_OPEN_URI = "content://settings/secure/power_supersave_mode_open";
    private static String RECEIVER_DESCRIPTOR = "receiver.FaceService";
    private static final int RECEIVER_ON_AUTHENTICATION_FAILED = 204;
    private static final int RECEIVER_ON_AUTHENTICATION_SUCCEEDED = 203;
    private static final int RECEIVER_ON_ENROLL_RESULT = 201;
    private static final int RECEIVER_ON_ERROR = 205;
    private static final int RECEIVER_ON_EXT_CMD = 301;
    private static final int RECEIVER_ON_LOCKOUT_RESET = 261;
    private static final int RECEIVER_ON_ON_ACQUIRED = 202;
    private static final int RECEIVER_ON_PRE_INIT = 207;
    private static final int RECEIVER_ON_REMOVED = 206;
    private static String SERVICE_DESCRIPTOR = null;
    private static String SERVICE_NAME = null;
    private static String TAG = "FaceManagerImpl_client";
    private static final int VERSION_1 = 1;
    private AuthenticationCallback mAuthenticationCallback;
    private DeathRecipient mBinderDied = new DeathRecipient() {
        public void binderDied() {
            synchronized (MiuiFaceManagerImpl.this.mBinderLock) {
                Log.e(MiuiFaceManagerImpl.TAG, "mMiuiFaceService Service Died.");
                MiuiFaceManagerImpl.this.mMiuiFaceService = null;
            }
        }
    };
    private Object mBinderLock = new Object();
    private Context mContext;
    private EnrollmentCallback mEnrollmentCallback;
    private int mFaceUnlockModel;
    private Handler mHandler;
    private boolean mHasFaceData;
    private boolean mHasInit;
    private boolean mIsSuperPower;
    private boolean mIsValid;
    private LockoutResetCallback mLockoutResetCallback;
    private IBinder mMiuiFaceService;
    private RemovalCallback mRemovalCallback;
    private Miuiface mRemovalMiuiface;
    private IBinder mServiceReceiver = new Binder() {
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String access$200 = MiuiFaceManagerImpl.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mServiceReceiver callback: ");
            stringBuilder.append(i);
            Log.d(access$200, stringBuilder.toString());
            if (i == 261) {
                parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                MiuiFaceManagerImpl.this.mHandler.obtainMessage(261, Integer.valueOf(data.readInt())).sendToTarget();
                reply.writeNoException();
                return true;
            } else if (i != 301) {
                long readLong;
                switch (i) {
                    case 201:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        long devId = data.readLong();
                        i = data.readInt();
                        MiuiFaceManagerImpl.this.mHandler.obtainMessage(201, data.readInt(), 0, new Miuiface(null, data.readInt(), i, devId)).sendToTarget();
                        reply.writeNoException();
                        return true;
                    case 202:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        MiuiFaceManagerImpl.this.mHandler.obtainMessage(202, data.readInt(), data.readInt(), Long.valueOf(data.readLong())).sendToTarget();
                        reply.writeNoException();
                        return true;
                    case 203:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        readLong = data.readLong();
                        Miuiface face = null;
                        if (data.readInt() != 0) {
                            face = (Miuiface) Miuiface.CREATOR.createFromParcel(parcel);
                        }
                        MiuiFaceManagerImpl.this.mHandler.obtainMessage(203, data.readInt(), 0, face).sendToTarget();
                        reply.writeNoException();
                        return true;
                    case 204:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        readLong = data.readLong();
                        MiuiFaceManagerImpl.this.mHandler.obtainMessage(204).sendToTarget();
                        reply.writeNoException();
                        return true;
                    case 205:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        MiuiFaceManagerImpl.this.mHandler.obtainMessage(205, data.readInt(), data.readInt(), Long.valueOf(data.readLong())).sendToTarget();
                        reply.writeNoException();
                        return true;
                    case 206:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        long devId2 = data.readLong();
                        int faceId = data.readInt();
                        int groupId = data.readInt();
                        int remaining = data.readInt();
                        Miuiface miuiface = r6;
                        Handler access$1300 = MiuiFaceManagerImpl.this.mHandler;
                        i = 206;
                        Miuiface miuiface2 = new Miuiface(null, groupId, faceId, devId2);
                        access$1300.obtainMessage(i, remaining, 0, miuiface).sendToTarget();
                        reply.writeNoException();
                        return true;
                    case 207:
                        parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                        MiuiFaceManagerImpl.this.mHasInit = data.readInt() == 1;
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                parcel.enforceInterface(MiuiFaceManagerImpl.RECEIVER_DESCRIPTOR);
                MiuiFaceManagerImpl.this.mHandler.obtainMessage(301, Integer.valueOf(data.readInt())).sendToTarget();
                reply.writeNoException();
                return true;
            }
        }
    };
    private IBinder mToken = new Binder();

    private class ClientHandler extends Handler {
        private ClientHandler(Context context) {
            super(context.getMainLooper());
        }

        private ClientHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (MiuiFaceManagerImpl.DEBUG) {
                String access$200 = MiuiFaceManagerImpl.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" handleMessage  callback what:");
                stringBuilder.append(msg.what);
                Log.d(access$200, stringBuilder.toString());
            }
            int i = msg.what;
            if (i == 261) {
                MiuiFaceManagerImpl.this.sendLockoutReset();
            } else if (i != 301) {
                switch (i) {
                    case 201:
                        MiuiFaceManagerImpl.this.sendEnrollResult((Miuiface) msg.obj, msg.arg1);
                        return;
                    case 202:
                        MiuiFaceManagerImpl.this.sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                        return;
                    case 203:
                        MiuiFaceManagerImpl.this.sendAuthenticatedSucceeded((Miuiface) msg.obj, msg.arg1);
                        return;
                    case 204:
                        MiuiFaceManagerImpl.this.sendAuthenticatedFailed();
                        return;
                    case 205:
                        MiuiFaceManagerImpl.this.sendErrorResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                        return;
                    case 206:
                        MiuiFaceManagerImpl.this.sendRemovedResult((Miuiface) msg.obj, msg.arg1);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private class FaceObserver extends ContentObserver {
        public FaceObserver(Handler handler) {
            super(handler);
        }

        /* JADX WARNING: Removed duplicated region for block: B:27:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0040  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0040  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0040  */
        public void onChange(boolean r10, android.net.Uri r11) {
            /*
            r9 = this;
            super.onChange(r10, r11);
            r0 = r11.getLastPathSegment();
            r1 = r0.hashCode();
            r2 = -1709079816; // 0xffffffff9a2182f8 float:-3.3399815E-23 double:NaN;
            r3 = "face_unlock_valid_feature";
            r4 = "power_supersave_mode_open";
            r5 = "face_unlock_has_feature";
            r6 = 2;
            r7 = 1;
            r8 = 0;
            if (r1 == r2) goto L_0x0035;
        L_0x001a:
            r2 = 280401189; // 0x10b69525 float:7.2016136E-29 double:1.385365945E-315;
            if (r1 == r2) goto L_0x002d;
        L_0x001f:
            r2 = 1905422490; // 0x7192709a float:1.45027E30 double:9.41403793E-315;
            if (r1 == r2) goto L_0x0025;
        L_0x0024:
            goto L_0x003d;
        L_0x0025:
            r1 = r0.equals(r3);
            if (r1 == 0) goto L_0x0024;
        L_0x002b:
            r1 = r7;
            goto L_0x003e;
        L_0x002d:
            r1 = r0.equals(r4);
            if (r1 == 0) goto L_0x0024;
        L_0x0033:
            r1 = r6;
            goto L_0x003e;
        L_0x0035:
            r1 = r0.equals(r5);
            if (r1 == 0) goto L_0x0024;
        L_0x003b:
            r1 = r8;
            goto L_0x003e;
        L_0x003d:
            r1 = -1;
        L_0x003e:
            if (r1 == 0) goto L_0x0071;
        L_0x0040:
            if (r1 == r7) goto L_0x005b;
        L_0x0042:
            if (r1 == r6) goto L_0x0045;
        L_0x0044:
            goto L_0x0087;
        L_0x0045:
            r1 = android.hardware.miuiface.MiuiFaceManagerImpl.this;
            r2 = r1.mContext;
            r2 = r2.getContentResolver();
            r2 = android.provider.Settings.System.getIntForUser(r2, r4, r8, r8);
            if (r2 == 0) goto L_0x0056;
        L_0x0055:
            goto L_0x0057;
        L_0x0056:
            r7 = r8;
        L_0x0057:
            r1.mIsSuperPower = r7;
            goto L_0x0087;
        L_0x005b:
            r1 = android.hardware.miuiface.MiuiFaceManagerImpl.this;
            r2 = r1.mContext;
            r2 = r2.getContentResolver();
            r2 = android.provider.Settings.Secure.getIntForUser(r2, r3, r7, r8);
            if (r2 == 0) goto L_0x006c;
        L_0x006b:
            goto L_0x006d;
        L_0x006c:
            r7 = r8;
        L_0x006d:
            r1.mIsValid = r7;
            goto L_0x0087;
        L_0x0071:
            r1 = android.hardware.miuiface.MiuiFaceManagerImpl.this;
            r2 = r1.mContext;
            r2 = r2.getContentResolver();
            r2 = android.provider.Settings.Secure.getIntForUser(r2, r5, r8, r8);
            if (r2 == 0) goto L_0x0082;
        L_0x0081:
            goto L_0x0083;
        L_0x0082:
            r7 = r8;
        L_0x0083:
            r1.mHasFaceData = r7;
        L_0x0087:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.miuiface.MiuiFaceManagerImpl$FaceObserver.onChange(boolean, android.net.Uri):void");
        }
    }

    private class OnAuthenticationCancelListener implements OnCancelListener {
        private OnAuthenticationCancelListener() {
        }

        /* synthetic */ OnAuthenticationCancelListener(MiuiFaceManagerImpl x0, AnonymousClass1 x1) {
            this();
        }

        public void onCancel() {
            MiuiFaceManagerImpl.this.cancelAuthentication();
        }
    }

    private class OnEnrollCancelListener implements OnCancelListener {
        private OnEnrollCancelListener() {
        }

        /* synthetic */ OnEnrollCancelListener(MiuiFaceManagerImpl x0, AnonymousClass1 x1) {
            this();
        }

        public void onCancel() {
            MiuiFaceManagerImpl.this.cancelEnrollment();
        }
    }

    static {
        String str = "miui.face.FaceService";
        SERVICE_NAME = str;
        SERVICE_DESCRIPTOR = str;
    }

    private MiuiFaceManagerImpl(Context con) {
        this.mContext = con.getApplicationContext();
        this.mHandler = new ClientHandler(this, this.mContext, null);
        boolean equals = "ursa".equals(Build.DEVICE);
        String str = FACE_UNLOCK_VALID_FEATURE;
        String str2 = FACE_UNLOCK_HAS_FEATURE;
        if (equals) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            String str3 = FACE_UNLOCK_MODEL;
            this.mFaceUnlockModel = Secure.getIntForUser(contentResolver, str3, 1, -2);
            if (this.mFaceUnlockModel != 2) {
                Secure.putIntForUser(this.mContext.getContentResolver(), str3, 2, -2);
                if (this.mFaceUnlockModel == 0) {
                    Secure.putIntForUser(this.mContext.getContentResolver(), FACE_UNLOCK_3D_HAS_FEATURE, Secure.getIntForUser(this.mContext.getContentResolver(), str2, 0, -2), -2);
                    Secure.putIntForUser(this.mContext.getContentResolver(), str2, 0, -2);
                    Secure.putIntForUser(this.mContext.getContentResolver(), str, 1, -2);
                }
            }
        }
        Secure.putIntForUser(this.mContext.getContentResolver(), FACEUNLOCK_SUPPORT_SUPERPOWER, 1, -2);
        FaceObserver faceObserver = new FaceObserver(this.mHandler);
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(str2), false, faceObserver, 0);
        faceObserver.onChange(false, Uri.parse(FACE_UNLOCK_HAS_FEATURE_URI));
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(str), false, faceObserver, 0);
        faceObserver.onChange(false, Uri.parse(FACE_UNLOCK_VALID_FEATURE_URI));
        this.mContext.getContentResolver().registerContentObserver(System.getUriFor("power_supersave_mode_open"), false, faceObserver, 0);
        faceObserver.onChange(false, Uri.parse(POWERMODE_SUPERSAVE_OPEN_URI));
    }

    public static IMiuiFaceManager getInstance(Context con) {
        if (INSTANCE == null) {
            synchronized (MiuiFaceManagerImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MiuiFaceManagerImpl(con);
                }
            }
        }
        return INSTANCE;
    }

    private void initService() throws RemoteException {
        synchronized (this.mBinderLock) {
            if (this.mMiuiFaceService == null) {
                this.mMiuiFaceService = ServiceManager.getService(SERVICE_NAME);
                if (this.mMiuiFaceService != null) {
                    this.mMiuiFaceService.linkToDeath(this.mBinderDied, 0);
                }
            }
        }
    }

    public boolean isFaceFeatureSupport() {
        if (this.mIsSuperPower) {
            Slog.d(TAG, "enter super power mode, isFaceFeatureSupport:false");
            return false;
        }
        String[] supportRegion;
        boolean res = false;
        if (miui.os.Build.IS_INTERNATIONAL_BUILD) {
            supportRegion = FeatureParser.getStringArray("support_face_unlock_region_global");
        } else {
            supportRegion = FeatureParser.getStringArray("support_face_unlock_region_dom");
        }
        if (supportRegion != null && (Arrays.asList(supportRegion).contains(miui.os.Build.getRegion()) || Arrays.asList(supportRegion).contains("ALL"))) {
            res = true;
        }
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("inernational:");
            stringBuilder.append(miui.os.Build.IS_INTERNATIONAL_BUILD);
            stringBuilder.append(" supportR:");
            stringBuilder.append(Arrays.toString(supportRegion));
            stringBuilder.append(" nowR:");
            stringBuilder.append(miui.os.Build.getRegion());
            Log.d(str, stringBuilder.toString());
        }
        return res;
    }

    public boolean isSupportScreenOnDelayed() {
        boolean res = FeatureParser.getBoolean("support_screen_on_delayed", false);
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isSupportScreenOnDelayed:");
            stringBuilder.append(res);
            Log.d(str, stringBuilder.toString());
        }
        return res;
    }

    public boolean isFaceUnlockInited() {
        return this.mHasInit;
    }

    private void cancelAuthentication() {
        if (DEBUG) {
            Slog.d(TAG, "cancelAuthentication ");
        }
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                binderCallCancelAuthention(this.mMiuiFaceService, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void cancelEnrollment() {
        if (DEBUG) {
            Slog.d(TAG, "cancelEnrollment ");
        }
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                binderCallCancelEnrollment(this.mMiuiFaceService, this.mToken);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getManagerVersion() {
        return 1;
    }

    public String getVendorInfo() {
        String res = "";
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                res = binderCallGetVendorInfo(this.mMiuiFaceService, this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getVendorInfo, res:");
            stringBuilder2.append(res);
            Slog.d(str2, stringBuilder2.toString());
        }
        return res;
    }

    public void authenticate(CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int timeout) {
        CancellationSignal cancellationSignal = cancel;
        AuthenticationCallback authenticationCallback = callback;
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("authenticate mServiceReceiver:");
            stringBuilder.append(this.mServiceReceiver);
            Slog.d(str, stringBuilder.toString());
        }
        if (authenticationCallback != null) {
            if (cancellationSignal != null) {
                if (cancel.isCanceled()) {
                    Slog.d(TAG, "authentication already canceled");
                    return;
                }
                cancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(this, null));
            }
            useHandler(handler);
            this.mAuthenticationCallback = authenticationCallback;
            this.mEnrollmentCallback = null;
            try {
                initService();
                if (this.mMiuiFaceService != null) {
                    binderCallAuthenticate(this.mMiuiFaceService, this.mToken, -1, -1, this.mServiceReceiver, flags, this.mContext.getPackageName(), timeout);
                } else {
                    Slog.d(TAG, "mMiuiFaceService is null");
                    authenticationCallback.onAuthenticationError(2100, getMessageInfo(2100));
                }
            } catch (Exception e) {
                Log.e(TAG, "Remote exception while authenticating: ", e);
                authenticationCallback.onAuthenticationError(2100, getMessageInfo(2100));
            }
            return;
        }
        Handler handler2 = handler;
        throw new IllegalArgumentException("Must supply an authentication callback");
    }

    public void enroll(byte[] cryptoToken, CancellationSignal cancel, int flags, EnrollmentCallback enrollCallback, Surface surface, Rect detectArea, int timeout) {
        enroll(cryptoToken, cancel, flags, enrollCallback, surface, null, new RectF(detectArea), timeout);
    }

    public void enroll(byte[] cryptoToken, CancellationSignal cancel, int flags, EnrollmentCallback enrollCallback, Surface surface, RectF detectArea, RectF enrollArea, int timeout) {
        int i;
        RemoteException e;
        CancellationSignal cancellationSignal = cancel;
        EnrollmentCallback enrollmentCallback = enrollCallback;
        if (enrollmentCallback != null) {
            if (cancellationSignal != null) {
                if (cancel.isCanceled()) {
                    Slog.d(TAG, "enrollment already canceled");
                    return;
                }
                cancellationSignal.setOnCancelListener(new OnEnrollCancelListener(this, null));
            }
            try {
                initService();
                if (this.mMiuiFaceService != null) {
                    this.mEnrollmentCallback = enrollmentCallback;
                    i = 2100;
                    try {
                        binderCallEnroll(this.mMiuiFaceService, this.mToken, cryptoToken, 0, this.mServiceReceiver, flags, this.mContext.getPackageName(), surface, enrollArea, timeout);
                    } catch (RemoteException e2) {
                        e = e2;
                        Log.e(TAG, "exception in enroll: ", e);
                        enrollmentCallback.onEnrollmentError(i, getMessageInfo(i));
                        return;
                    }
                }
                i = 2100;
                Slog.d(TAG, "mMiuiFaceService is null");
                enrollmentCallback.onEnrollmentError(i, getMessageInfo(i));
            } catch (RemoteException e3) {
                e = e3;
                i = 2100;
                Log.e(TAG, "exception in enroll: ", e);
                enrollmentCallback.onEnrollmentError(i, getMessageInfo(i));
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an enrollment callback");
    }

    public int extCmd(int cmd, int param) {
        int res = -1;
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                res = binderCallExtCmd(this.mMiuiFaceService, this.mToken, this.mServiceReceiver, cmd, param, this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("extCmd  cmd:");
            stringBuilder2.append(cmd);
            stringBuilder2.append(" param:");
            stringBuilder2.append(param);
            stringBuilder2.append(" res:");
            stringBuilder2.append(res);
            Slog.d(str2, stringBuilder2.toString());
        }
        return res;
    }

    public void remove(Miuiface face, RemovalCallback callback) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("remove  faceId:");
            stringBuilder.append(face.getMiuifaceId());
            stringBuilder.append("  callback:");
            stringBuilder.append(callback);
            Slog.d(str, stringBuilder.toString());
        }
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                this.mRemovalMiuiface = face;
                this.mRemovalCallback = callback;
                this.mEnrollmentCallback = null;
                this.mAuthenticationCallback = null;
                binderCallRemove(this.mMiuiFaceService, this.mToken, face.getMiuifaceId(), face.getGroupId(), 0, this.mServiceReceiver);
                return;
            }
            Slog.d(TAG, "mMiuiFaceService is null");
            callback.onRemovalError(face, 2100, getMessageInfo(2100));
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact fail. ");
            stringBuilder2.append(e);
            Log.e(str2, stringBuilder2.toString());
        }
    }

    public void rename(int faceId, String name) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("rename  faceId:");
            stringBuilder.append(faceId);
            stringBuilder.append(" name:");
            stringBuilder.append(name);
            Slog.d(str, stringBuilder.toString());
        }
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                binderCallRename(this.mMiuiFaceService, faceId, 0, name);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact fail. ");
            stringBuilder2.append(e);
            Log.e(str2, stringBuilder2.toString());
        }
    }

    public void addLockoutResetCallback(LockoutResetCallback callback) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addLockoutResetCallback  callback:");
            stringBuilder.append(callback);
            Slog.d(str, stringBuilder.toString());
        }
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                this.mLockoutResetCallback = callback;
                binderCallAddLoackoutResetCallback(this.mMiuiFaceService, this.mServiceReceiver);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact fail. ");
            stringBuilder2.append(e);
            Log.e(str2, stringBuilder2.toString());
        }
    }

    public void resetTimeout(byte[] token) {
        if (DEBUG) {
            Slog.d(TAG, "resetTimeout");
        }
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                binderCallRestTimeout(this.mMiuiFaceService, token);
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
    }

    public List<Miuiface> getEnrolledFaces() {
        StringBuilder stringBuilder;
        List<Miuiface> res = new ArrayList();
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                res = binderCallGetEnrolledFaces(this.mMiuiFaceService, 0, this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
            String str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
        if (DEBUG) {
            String str2;
            String str3 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getEnrolledFaces   res:");
            if (res == null || res.size() == 0) {
                str2 = " is null";
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(res.size());
                str2 = stringBuilder.toString();
            }
            stringBuilder2.append(str2);
            Slog.d(str3, stringBuilder2.toString());
        }
        return res;
    }

    public int hasEnrolledFaces() {
        try {
            if (this.mHasFaceData && this.mIsValid) {
                return 1;
            }
            if (this.mHasFaceData) {
                return -1;
            }
            return 0;
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
            return -2;
        }
    }

    public void preInitAuthen() {
        try {
            initService();
            if (this.mMiuiFaceService != null) {
                this.mHasInit = false;
                binderCallPpreInitAuthen(this.mMiuiFaceService, this.mToken, this.mContext.getPackageName(), this.mServiceReceiver);
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
    }

    public boolean isReleased() {
        return false;
    }

    public void release() {
    }

    private void binderCallPpreInitAuthen(IBinder service, IBinder token, String packName, IBinder receiver) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeStrongBinder(token == null ? null : token);
        request.writeString(packName);
        request.writeStrongBinder(receiver);
        service.transact(2, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private int binderCallAuthenticate(IBinder service, IBinder token, long sessionId, int userId, IBinder receiver, int flags, String packName, int timeout) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        IBinder iBinder = null;
        request.writeStrongBinder(token == null ? null : token);
        request.writeLong(sessionId);
        request.writeInt(userId);
        if (receiver != null) {
            iBinder = receiver;
        }
        request.writeStrongBinder(iBinder);
        request.writeInt(flags);
        request.writeString(packName);
        request.writeInt(timeout);
        service.transact(3, request, reply, 0);
        reply.readException();
        int res = reply.readInt();
        request.recycle();
        reply.recycle();
        return res;
    }

    private void binderCallCancelAuthention(IBinder service, IBinder token, String packName) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeStrongBinder(token == null ? null : token);
        request.writeString(packName);
        service.transact(4, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private void binderCallEnroll(IBinder service, IBinder token, byte[] cryptoToken, int groupId, IBinder receiver, int flags, String packName, Surface surface, RectF detectArea, int timeout) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        IBinder iBinder = null;
        request.writeStrongBinder(token == null ? null : token);
        request.writeByteArray(cryptoToken);
        request.writeInt(groupId);
        if (receiver != null) {
            iBinder = receiver;
        }
        request.writeStrongBinder(iBinder);
        request.writeInt(flags);
        request.writeString(packName);
        if (surface != null) {
            request.writeInt(1);
            surface.writeToParcel(request, 0);
        } else {
            request.writeInt(0);
        }
        if (detectArea != null) {
            request.writeInt(1);
            detectArea.writeToParcel(request, 0);
        } else {
            request.writeInt(0);
        }
        request.writeInt(timeout);
        service.transact(5, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private void binderCallCancelEnrollment(IBinder service, IBinder token) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeStrongBinder(token == null ? null : token);
        service.transact(6, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private void binderCallRemove(IBinder service, IBinder token, int faceId, int groupId, int userId, IBinder receiver) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        IBinder iBinder = null;
        request.writeStrongBinder(token == null ? null : token);
        request.writeInt(faceId);
        request.writeInt(groupId);
        request.writeInt(userId);
        if (receiver != null) {
            iBinder = receiver;
        }
        request.writeStrongBinder(iBinder);
        service.transact(7, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private void binderCallRename(IBinder service, int faceId, int groupId, String name) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeInt(faceId);
        request.writeInt(groupId);
        request.writeString(name);
        service.transact(8, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private List<Miuiface> binderCallGetEnrolledFaces(IBinder service, int groupId, String packName) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeInt(groupId);
        request.writeString(packName);
        service.transact(9, request, reply, 0);
        reply.readException();
        List<Miuiface> res = reply.createTypedArrayList(Miuiface.CREATOR);
        request.recycle();
        reply.recycle();
        return res;
    }

    private void binderCallPreEnroll(IBinder service, IBinder token) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeStrongBinder(token == null ? null : token);
        service.transact(10, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private void binderCallPostEnroll(IBinder service, IBinder token) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeStrongBinder(token == null ? null : token);
        service.transact(11, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private int binderCallHasEnrolledFaces(IBinder service, int groupId, String packName) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeInt(groupId);
        request.writeString(packName);
        service.transact(12, request, reply, 0);
        reply.readException();
        int res = reply.readInt();
        request.recycle();
        reply.recycle();
        return res;
    }

    private long binderCallAuthenticatorId(IBinder service, String packName) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeString(packName);
        service.transact(14, request, reply, 0);
        reply.readException();
        long res = reply.readLong();
        request.recycle();
        reply.recycle();
        return res;
    }

    private void binderCallRestTimeout(IBinder service, byte[] cryptoToken) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeByteArray(cryptoToken);
        service.transact(15, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private void binderCallAddLoackoutResetCallback(IBinder service, IBinder callback) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeStrongBinder(callback == null ? null : callback);
        service.transact(16, request, reply, 0);
        reply.readException();
        request.recycle();
        reply.recycle();
    }

    private String binderCallGetVendorInfo(IBinder service, String packName) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        request.writeString(packName);
        service.transact(17, request, reply, 0);
        reply.readException();
        String res = reply.readString();
        request.recycle();
        reply.recycle();
        return res;
    }

    private int binderCallExtCmd(IBinder service, IBinder token, IBinder receiver, int cmd, int param, String packName) throws RemoteException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        request.writeInterfaceToken(SERVICE_DESCRIPTOR);
        IBinder iBinder = null;
        request.writeStrongBinder(token == null ? null : token);
        if (receiver != null) {
            iBinder = receiver;
        }
        request.writeStrongBinder(iBinder);
        request.writeInt(cmd);
        request.writeInt(param);
        request.writeString(packName);
        service.transact(101, request, reply, 0);
        reply.readException();
        int res = reply.readInt();
        request.recycle();
        reply.recycle();
        return res;
    }

    private void useHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = new ClientHandler(this, handler.getLooper(), null);
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new ClientHandler(this, this.mContext.getMainLooper(), null);
        }
    }

    private void sendRemovedResult(Miuiface face, int remaining) {
        if (this.mRemovalCallback != null) {
            if (face == null || this.mRemovalMiuiface == null) {
                Slog.d(TAG, "Received MSG_REMOVED, but face or mRemovalMiuiface is null, ");
                return;
            }
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sendRemovedResult faceId:");
            stringBuilder.append(face.getMiuifaceId());
            stringBuilder.append("  remaining:");
            stringBuilder.append(remaining);
            Slog.d(str, stringBuilder.toString());
            int faceId = face.getMiuifaceId();
            int reqFaceId = this.mRemovalMiuiface.getMiuifaceId();
            if (reqFaceId == 0 || faceId == 0 || faceId == reqFaceId) {
                this.mRemovalCallback.onRemovalSucceeded(face, remaining);
                return;
            }
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Face id didn't match: ");
            stringBuilder2.append(faceId);
            stringBuilder2.append(" != ");
            stringBuilder2.append(reqFaceId);
            Slog.d(str2, stringBuilder2.toString());
        }
    }

    private void sendErrorResult(long deviceId, int errMsgId, int vendorCode) {
        String errorMsg = getMessageInfo(errMsgId);
        EnrollmentCallback enrollmentCallback = this.mEnrollmentCallback;
        if (enrollmentCallback != null) {
            enrollmentCallback.onEnrollmentError(errMsgId, errorMsg);
            return;
        }
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationError(errMsgId, errorMsg);
            return;
        }
        RemovalCallback removalCallback = this.mRemovalCallback;
        if (removalCallback != null) {
            removalCallback.onRemovalError(this.mRemovalMiuiface, errMsgId, errorMsg);
        }
    }

    private void sendEnrollResult(Miuiface face, int remaining) {
        EnrollmentCallback enrollmentCallback = this.mEnrollmentCallback;
        if (enrollmentCallback != null) {
            enrollmentCallback.onEnrollmentProgress(remaining, face.getMiuifaceId());
        }
    }

    private void sendAuthenticatedSucceeded(Miuiface face, int userId) {
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationSucceeded(face);
        }
    }

    private void sendAuthenticatedFailed() {
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationFailed();
        }
    }

    private void sendLockoutReset() {
        LockoutResetCallback lockoutResetCallback = this.mLockoutResetCallback;
        if (lockoutResetCallback != null) {
            lockoutResetCallback.onLockoutReset();
        }
    }

    private void sendAcquiredResult(long deviceId, int clientInfo, int vendorCode) {
        String msg = getMessageInfo(clientInfo);
        EnrollmentCallback enrollmentCallback = this.mEnrollmentCallback;
        if (enrollmentCallback != null) {
            enrollmentCallback.onEnrollmentHelp(clientInfo, msg);
            return;
        }
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationHelp(clientInfo, msg);
        }
    }

    private String getMessageInfo(int msgId) {
        String msg = "define by client";
        if (msgId == 1000) {
            return "打开相机失败";
        }
        if (msgId == 1001) {
            return "打开相机成功";
        }
        if (msgId == 2000) {
            return "取消成功";
        }
        if (msgId == 2100) {
            return "binder调用异常";
        }
        switch (msgId) {
            case 1:
                return "参数不合法";
            case 2:
                return "Handler不正确";
            case 3:
                return "解锁失败（内部错误）";
            case 4:
                return "传入数据质量不好";
            case 5:
                return "未检测到人脸";
            case 6:
                return "脸太小";
            case 7:
                return "脸太大";
            case 8:
                return "脸偏左";
            case 9:
                return "脸偏上";
            case 10:
                return "脸偏右";
            case 11:
                return "脸偏下";
            case 12:
                return "对比失败";
            case 13:
                return "活体攻击警告";
            case 14:
                return "活体检测失败";
            case 15:
                return "向左转头";
            case 16:
                return "抬头";
            case 17:
                return "向右转头";
            case 18:
                return "低头";
            case 19:
                return "继续调用传入数据";
            case 20:
                return "图片模糊";
            case 21:
                return "眼部遮挡";
            case 22:
                return "闭眼";
            case 23:
                return "嘴部遮挡";
            case 24:
                return "处理Feature读取异常";
            case 25:
                return "Feature版本错误";
            case 26:
                return "光线不好";
            case 27:
                return "多张人脸";
            case 28:
                return "脸部模糊";
            case 29:
                return "脸部不完整";
            case 30:
                return "光线太暗";
            case 31:
                return "光线太亮";
            case 32:
                return "阴阳脸";
            default:
                if (!DEBUG) {
                    return msg;
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("default msgId: ");
                stringBuilder.append(msgId);
                Log.d(str, stringBuilder.toString());
                return msg;
        }
    }
}
