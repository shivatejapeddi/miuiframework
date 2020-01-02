package android.hardware.fingerprint;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.biometrics.IBiometricServiceLockoutResetCallback;
import android.hardware.fingerprint.IFingerprintServiceReceiver.Stub;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.R;
import java.security.Signature;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@Deprecated
public class FingerprintManager implements BiometricAuthenticator, BiometricFingerprintConstants {
    private static final boolean DEBUG = true;
    private static final int FINGERPRINT_CMD_CHECKERBOARD_TEST = 9;
    private static final int FINGERPRINT_CMD_FP_DISABLE = 4;
    private static final int FINGERPRINT_CMD_FP_ENABLE = 3;
    private static final int FINGERPRINT_CMD_HBM_CYAN_STATUS = 10;
    private static final int FINGERPRINT_CMD_SELF_TEST = 8;
    private static final int FINGERPRINT_CMD_SET_ACTIVE_MODE = 1;
    private static final int FINGERPRINT_CMD_SET_DEFAULT_MODE = 2;
    private static final int FINGERPRINT_CMD_SET_SECURITY_LEVEL = 7;
    private static final int FINGERPRINT_CMD_START_SET_MODE = 5;
    private static final int FINGERPRINT_CMD_STOP_SET_MODE = 6;
    private static final int FINGERPRINT_MODE_KEY = 3;
    private static final int FINGERPRINT_MODE_NAV = 2;
    private static final int FINGERPRINT_MODE_SLEEP = 1;
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ENUMERATED = 106;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    private static final String TAG = "FingerprintManager";
    private AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    private CryptoObject mCryptoObject;
    private EnrollmentCallback mEnrollmentCallback;
    private EnumerateCallback mEnumerateCallback;
    private Handler mHandler;
    private RemovalCallback mRemovalCallback;
    private Fingerprint mRemovalFingerprint;
    private IFingerprintService mService;
    private IFingerprintServiceReceiver mServiceReceiver = new Stub() {
        public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(100, remaining, 0, new Fingerprint(null, groupId, fingerId, deviceId)).sendToTarget();
        }

        public void onAcquired(long deviceId, int acquireInfo, int vendorCode) {
            FingerprintManager.this.mHandler.obtainMessage(101, acquireInfo, vendorCode, Long.valueOf(deviceId)).sendToTarget();
        }

        public void onAuthenticationSucceeded(long deviceId, Fingerprint fp, int userId) {
            FingerprintManager.this.mHandler.obtainMessage(102, userId, 0, fp).sendToTarget();
        }

        public void onAuthenticationFailed(long deviceId) {
            FingerprintManager.this.mHandler.obtainMessage(103).sendToTarget();
        }

        public void onError(long deviceId, int error, int vendorCode) {
            FingerprintManager.this.mHandler.obtainMessage(104, error, vendorCode, Long.valueOf(deviceId)).sendToTarget();
        }

        public void onRemoved(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(105, remaining, 0, new Fingerprint(null, groupId, fingerId, deviceId)).sendToTarget();
        }

        public void onEnumerated(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(106, fingerId, groupId, Long.valueOf(deviceId)).sendToTarget();
        }
    };
    private IBinder mToken = new Binder();

    @Deprecated
    public static abstract class AuthenticationCallback extends android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationAcquired(int acquireInfo) {
        }
    }

    public static abstract class AuthenticationFidoCallback extends AuthenticationCallback {
        public void onAuthenticationFidoSucceeded(AuthenticationResult result, FingerprintFidoOut fidoOut) {
        }
    }

    @Deprecated
    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private Fingerprint mFingerprint;
        private int mUserId;

        public AuthenticationResult(CryptoObject crypto, Fingerprint fingerprint, int userId) {
            this.mCryptoObject = crypto;
            this.mFingerprint = fingerprint;
            this.mUserId = userId;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        @UnsupportedAppUsage
        public Fingerprint getFingerprint() {
            return this.mFingerprint;
        }

        public int getUserId() {
            return this.mUserId;
        }
    }

    @Deprecated
    public static final class CryptoObject extends android.hardware.biometrics.CryptoObject {
        public CryptoObject(Signature signature) {
            super(signature);
        }

        public CryptoObject(Cipher cipher) {
            super(cipher);
        }

        public CryptoObject(Mac mac) {
            super(mac);
        }

        public Signature getSignature() {
            return super.getSignature();
        }

        public Cipher getCipher() {
            return super.getCipher();
        }

        public Mac getMac() {
            return super.getMac();
        }
    }

    public static abstract class EnrollmentCallback {
        public void onEnrollmentError(int errMsgId, CharSequence errString) {
        }

        public void onEnrollmentHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onEnrollmentProgress(int remaining) {
        }
    }

    public static abstract class EnumerateCallback {
        public void onEnumerateError(int errMsgId, CharSequence errString) {
        }

        public void onEnumerate(Fingerprint fingerprint) {
        }
    }

    public static abstract class LockoutResetCallback {
        public void onLockoutReset() {
        }
    }

    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    FingerprintManager.this.sendEnrollResult((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 101:
                    FingerprintManager.this.sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case 102:
                    FingerprintManager.this.sendAuthenticatedSucceeded((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 103:
                    FingerprintManager.this.sendAuthenticatedFailed();
                    return;
                case 104:
                    FingerprintManager.this.sendErrorResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case 105:
                    FingerprintManager.this.sendRemovedResult((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 106:
                    FingerprintManager.this.sendEnumeratedResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                default:
                    return;
            }
        }
    }

    private class OnAuthenticationCancelListener implements OnCancelListener {
        private android.hardware.biometrics.CryptoObject mCrypto;

        public OnAuthenticationCancelListener(android.hardware.biometrics.CryptoObject crypto) {
            this.mCrypto = crypto;
        }

        public void onCancel() {
            FingerprintManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    private class OnEnrollCancelListener implements OnCancelListener {
        private OnEnrollCancelListener() {
        }

        /* synthetic */ OnEnrollCancelListener(FingerprintManager x0, AnonymousClass1 x1) {
            this();
        }

        public void onCancel() {
            FingerprintManager.this.cancelEnrollment();
        }
    }

    public static abstract class RemovalCallback {
        public void onRemovalError(Fingerprint fp, int errMsgId, CharSequence errString) {
        }

        public void onRemovalSucceeded(Fingerprint fp, int remaining) {
        }
    }

    @Deprecated
    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler) {
        authenticate(crypto, cancel, flags, callback, handler, this.mContext.getUserId());
    }

    private void useHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = new MyHandler(this, handler.getLooper(), null);
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new MyHandler(this, this.mContext.getMainLooper(), null);
        }
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int userId) {
        android.hardware.biometrics.CryptoObject cryptoObject = crypto;
        CancellationSignal cancellationSignal = cancel;
        AuthenticationCallback authenticationCallback = callback;
        Handler handler2;
        if (authenticationCallback != null) {
            String str = TAG;
            if (cancellationSignal != null) {
                if (cancel.isCanceled()) {
                    Slog.w(str, "authentication already canceled");
                    return;
                }
                cancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(cryptoObject));
            }
            if (this.mService != null) {
                try {
                    useHandler(handler);
                    this.mAuthenticationCallback = authenticationCallback;
                    this.mCryptoObject = cryptoObject;
                    this.mService.authenticate(this.mToken, cryptoObject != null ? crypto.getOpId() : 0, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName());
                } catch (RemoteException e) {
                    Slog.w(str, "Remote exception while authenticating: ", e);
                    authenticationCallback.onAuthenticationError(1, getErrorString(this.mContext, 1, 0));
                }
            } else {
                handler2 = handler;
            }
            return;
        }
        handler2 = handler;
        throw new IllegalArgumentException("Must supply an authentication callback");
    }

    public void enroll(byte[] token, CancellationSignal cancel, int flags, int userId, EnrollmentCallback callback) {
        if (userId == -2) {
            userId = getCurrentUserId();
        }
        if (callback == null || token == null) {
            throw new IllegalArgumentException("Must supply an enrollment callback ,and token");
        }
        String str = TAG;
        if (cancel != null) {
            if (cancel.isCanceled()) {
                Slog.w(str, "enrollment already canceled");
                return;
            }
            cancel.setOnCancelListener(new OnEnrollCancelListener(this, null));
        }
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                this.mEnrollmentCallback = callback;
                iFingerprintService.enroll(this.mToken, token, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Slog.w(str, "Remote exception in enroll: ", e);
                callback.onEnrollmentError(1, getErrorString(this.mContext, 1, 0));
            }
        }
    }

    public long preEnroll() {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService == null) {
            return 0;
        }
        try {
            return iFingerprintService.preEnroll(this.mToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int postEnroll() {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService == null) {
            return 0;
        }
        try {
            return iFingerprintService.postEnroll(this.mToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setActiveUser(int userId) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                iFingerprintService.setActiveUser(userId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void remove(Fingerprint fp, int userId, RemovalCallback callback) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                this.mRemovalCallback = callback;
                this.mRemovalFingerprint = fp;
                iFingerprintService.remove(this.mToken, fp.getBiometricId(), fp.getGroupId(), userId, this.mServiceReceiver);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in remove: ", e);
                if (callback != null) {
                    callback.onRemovalError(fp, 1, getErrorString(this.mContext, 1, 0));
                }
            }
        }
    }

    public void enumerate(int userId, EnumerateCallback callback) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                this.mEnumerateCallback = callback;
                iFingerprintService.enumerate(this.mToken, userId, this.mServiceReceiver);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in enumerate: ", e);
                if (callback != null) {
                    callback.onEnumerateError(1, getErrorString(this.mContext, 1, 0));
                }
            }
        }
    }

    public void rename(int fpId, int userId, String newName) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                iFingerprintService.rename(fpId, userId, newName);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "rename(): Service not connected!");
    }

    @UnsupportedAppUsage
    public List<Fingerprint> getEnrolledFingerprints(int userId) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService == null) {
            return null;
        }
        try {
            return iFingerprintService.getEnrolledFingerprints(userId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public List<Fingerprint> getEnrolledFingerprints() {
        return getEnrolledFingerprints(this.mContext.getUserId());
    }

    public boolean hasEnrolledTemplates() {
        return hasEnrolledFingerprints();
    }

    public boolean hasEnrolledTemplates(int userId) {
        return hasEnrolledFingerprints(userId);
    }

    @Deprecated
    public boolean hasEnrolledFingerprints() {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService == null) {
            return false;
        }
        try {
            return iFingerprintService.hasEnrolledFingerprints(this.mContext.getUserId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasEnrolledFingerprints(int userId) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService == null) {
            return false;
        }
        try {
            return iFingerprintService.hasEnrolledFingerprints(userId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean isHardwareDetected() {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                return iFingerprintService.isHardwareDetected(0, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "isFingerprintHardwareDetected(): Service not connected!");
        return false;
    }

    @UnsupportedAppUsage
    public long getAuthenticatorId() {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                return iFingerprintService.getAuthenticatorId(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "getAuthenticatorId(): Service not connected!");
        return 0;
    }

    public void addLockoutResetCallback(final LockoutResetCallback callback) {
        if (this.mService != null) {
            try {
                final PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
                this.mService.addLockoutResetCallback(new IBiometricServiceLockoutResetCallback.Stub() {
                    public void onLockoutReset(long deviceId, IRemoteCallback serverCallback) throws RemoteException {
                        try {
                            WakeLock wakeLock = powerManager.newWakeLock(1, "lockoutResetCallback");
                            wakeLock.acquire();
                            FingerprintManager.this.mHandler.post(new -$$Lambda$FingerprintManager$1$4i3tUU8mafgvA9HaB2UPD31L6UY(callback, wakeLock));
                        } finally {
                            serverCallback.sendResult(null);
                        }
                    }

                    static /* synthetic */ void lambda$onLockoutReset$0(LockoutResetCallback callback, WakeLock wakeLock) {
                        try {
                            callback.onLockoutReset();
                        } finally {
                            wakeLock.release();
                        }
                    }
                });
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "addLockoutResetCallback(): Service not connected!");
    }

    private void sendRemovedResult(Fingerprint fingerprint, int remaining) {
        if (this.mRemovalCallback != null) {
            String str = TAG;
            if (fingerprint == null) {
                Slog.e(str, "Received MSG_REMOVED, but fingerprint is null");
                return;
            }
            int fingerId = fingerprint.getBiometricId();
            int reqFingerId = this.mRemovalFingerprint.getBiometricId();
            String str2 = " != ";
            if (reqFingerId == 0 || fingerId == 0 || fingerId == reqFingerId) {
                int groupId = fingerprint.getGroupId();
                int reqGroupId = this.mRemovalFingerprint.getGroupId();
                if (groupId != reqGroupId) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Group id didn't match: ");
                    stringBuilder.append(groupId);
                    stringBuilder.append(str2);
                    stringBuilder.append(reqGroupId);
                    Slog.w(str, stringBuilder.toString());
                    return;
                }
                this.mRemovalCallback.onRemovalSucceeded(fingerprint, remaining);
                return;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Finger id didn't match: ");
            stringBuilder2.append(fingerId);
            stringBuilder2.append(str2);
            stringBuilder2.append(reqFingerId);
            Slog.w(str, stringBuilder2.toString());
        }
    }

    private void sendEnumeratedResult(long deviceId, int fingerId, int groupId) {
        EnumerateCallback enumerateCallback = this.mEnumerateCallback;
        if (enumerateCallback != null) {
            enumerateCallback.onEnumerate(new Fingerprint(null, groupId, fingerId, deviceId));
        }
    }

    private void sendEnrollResult(Fingerprint fp, int remaining) {
        EnrollmentCallback enrollmentCallback = this.mEnrollmentCallback;
        if (enrollmentCallback != null) {
            enrollmentCallback.onEnrollmentProgress(remaining);
        }
    }

    private void sendAuthenticatedSucceeded(Fingerprint fp, int userId) {
        if (this.mAuthenticationCallback != null) {
            this.mAuthenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(this.mCryptoObject, fp, userId));
        }
    }

    private void sendAuthenticatedFailed() {
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationFailed();
        }
    }

    private void sendAcquiredResult(long deviceId, int acquireInfo, int vendorCode) {
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationAcquired(acquireInfo);
        }
        String msg = getAcquiredString(this.mContext, acquireInfo, vendorCode);
        if (msg != null) {
            int clientInfo = acquireInfo == 6 ? vendorCode + 1000 : acquireInfo;
            EnrollmentCallback enrollmentCallback = this.mEnrollmentCallback;
            if (enrollmentCallback != null) {
                enrollmentCallback.onEnrollmentHelp(clientInfo, msg);
            } else {
                AuthenticationCallback authenticationCallback2 = this.mAuthenticationCallback;
                if (authenticationCallback2 != null) {
                    authenticationCallback2.onAuthenticationHelp(clientInfo, msg);
                }
            }
        }
    }

    private void sendErrorResult(long deviceId, int errMsgId, int vendorCode) {
        int clientErrMsgId = errMsgId == 8 ? vendorCode + 1000 : errMsgId;
        EnrollmentCallback enrollmentCallback = this.mEnrollmentCallback;
        if (enrollmentCallback != null) {
            enrollmentCallback.onEnrollmentError(clientErrMsgId, getErrorString(this.mContext, errMsgId, vendorCode));
            return;
        }
        AuthenticationCallback authenticationCallback = this.mAuthenticationCallback;
        if (authenticationCallback != null) {
            authenticationCallback.onAuthenticationError(clientErrMsgId, getErrorString(this.mContext, errMsgId, vendorCode));
            return;
        }
        RemovalCallback removalCallback = this.mRemovalCallback;
        if (removalCallback != null) {
            removalCallback.onRemovalError(this.mRemovalFingerprint, clientErrMsgId, getErrorString(this.mContext, errMsgId, vendorCode));
            return;
        }
        EnumerateCallback enumerateCallback = this.mEnumerateCallback;
        if (enumerateCallback != null) {
            enumerateCallback.onEnumerateError(clientErrMsgId, getErrorString(this.mContext, errMsgId, vendorCode));
        }
    }

    public FingerprintManager(Context context, IFingerprintService service) {
        this.mContext = context;
        this.mService = service;
        if (this.mService == null) {
            Slog.v(TAG, "FingerprintManagerService was null");
        }
        this.mHandler = new MyHandler(this, context, null);
    }

    private int getCurrentUserId() {
        try {
            return ActivityManager.getService().getCurrentUser().id;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void cancelEnrollment() {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                iFingerprintService.cancelEnrollment(this.mToken);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    private void cancelAuthentication(android.hardware.biometrics.CryptoObject cryptoObject) {
        IFingerprintService iFingerprintService = this.mService;
        if (iFingerprintService != null) {
            try {
                iFingerprintService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public static String getErrorString(Context context, int errMsg, int vendorCode) {
        switch (errMsg) {
            case 1:
                return context.getString(R.string.fingerprint_error_hw_not_available);
            case 2:
                return context.getString(R.string.fingerprint_error_unable_to_process);
            case 3:
                return context.getString(R.string.fingerprint_error_timeout);
            case 4:
                return context.getString(R.string.fingerprint_error_no_space);
            case 5:
                return context.getString(R.string.fingerprint_error_canceled);
            case 7:
                return context.getString(R.string.fingerprint_error_lockout);
            case 8:
                String[] msgArray = context.getResources().getStringArray(R.array.fingerprint_error_vendor);
                if (vendorCode < msgArray.length) {
                    return msgArray[vendorCode];
                }
                break;
            case 9:
                return context.getString(R.string.fingerprint_error_lockout_permanent);
            case 10:
                return context.getString(R.string.fingerprint_error_user_canceled);
            case 11:
                return context.getString(R.string.fingerprint_error_no_fingerprints);
            case 12:
                return context.getString(R.string.fingerprint_error_hw_not_present);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid error message: ");
        stringBuilder.append(errMsg);
        stringBuilder.append(", ");
        stringBuilder.append(vendorCode);
        Slog.w(TAG, stringBuilder.toString());
        return null;
    }

    public static String getAcquiredString(Context context, int acquireInfo, int vendorCode) {
        switch (acquireInfo) {
            case 0:
                return null;
            case 1:
                return context.getString(R.string.fingerprint_acquired_partial);
            case 2:
                return context.getString(R.string.fingerprint_acquired_insufficient);
            case 3:
                return context.getString(R.string.fingerprint_acquired_imager_dirty);
            case 4:
                return context.getString(R.string.fingerprint_acquired_too_slow);
            case 5:
                return context.getString(R.string.fingerprint_acquired_too_fast);
            case 6:
                String[] msgArray = context.getResources().getStringArray(R.array.fingerprint_acquired_vendor);
                if (vendorCode < msgArray.length) {
                    return msgArray[vendorCode];
                }
                break;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid acquired message: ");
        stringBuilder.append(acquireInfo);
        stringBuilder.append(", ");
        stringBuilder.append(vendorCode);
        Slog.w(TAG, stringBuilder.toString());
        return null;
    }

    public void authenticateFido(CryptoObject crypto, CancellationSignal cancel, AuthenticationFidoCallback callback, FingerprintFidoIn fidoIn) {
        CancellationSignal cancellationSignal = cancel;
        AuthenticationCallback authenticationCallback = callback;
        int userId = UserHandle.myUserId();
        if (authenticationCallback != null) {
            String str = TAG;
            if (cancellationSignal != null) {
                if (cancel.isCanceled()) {
                    Slog.w(str, "authentication already canceled");
                    return;
                }
                cancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(null));
            }
            if (this.mService != null) {
                try {
                    useHandler(null);
                    this.mAuthenticationCallback = authenticationCallback;
                    long sessionId = challenge2long(fidoIn.getNonce());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("authenticateFido, v1.2, sessionId:");
                    stringBuilder.append(sessionId);
                    Slog.i(str, stringBuilder.toString());
                    this.mService.authenticate(this.mToken, sessionId, userId, this.mServiceReceiver, 0, this.mContext.getOpPackageName());
                } catch (RemoteException e) {
                    Slog.w(str, "Remote exception while authenticating: ", e);
                    authenticationCallback.onAuthenticationError(1, getErrorString(this.mContext, 1, 0));
                }
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an authentication callback");
    }

    private long challenge2long(byte[] bytes) {
        long res = 0;
        for (int i = 0; i < 8; i++) {
            res = (res << 8) | ((long) (bytes[i] & 255));
        }
        return res;
    }

    public int extCmd(int cmd, int param) {
        IFingerprintService iFingerprintService = this.mService;
        String str = TAG;
        if (iFingerprintService != null) {
            try {
                return iFingerprintService.extCmd(this.mToken, getCurrentUserId(), cmd, param, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Slog.v(str, "Remote exception in extCmd(): ", e);
            }
        } else {
            Slog.w(str, "extCmd(): Service not connected!");
            return -1;
        }
    }

    public void setNavigationEnable(boolean enable) {
        if (enable) {
            extCmd(2, 3);
        } else {
            extCmd(2, 1);
        }
    }
}
