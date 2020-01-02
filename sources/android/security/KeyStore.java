package android.security;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.KeymasterDefs;
import android.security.keymaster.OperationResult;
import android.security.keystore.IKeystoreCertificateChainCallback.Stub;
import android.security.keystore.IKeystoreExportKeyCallback;
import android.security.keystore.IKeystoreKeyCharacteristicsCallback;
import android.security.keystore.IKeystoreOperationResultCallback;
import android.security.keystore.IKeystoreResponseCallback;
import android.security.keystore.IKeystoreService;
import android.security.keystore.KeyExpiredException;
import android.security.keystore.KeyNotYetValidException;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.security.keystore.KeyProperties.KeyAlgorithm;
import android.security.keystore.KeystoreResponse;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import com.android.org.bouncycastle.asn1.ASN1InputStream;
import com.android.org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

public class KeyStore {
    public static final int CANNOT_ATTEST_IDS = -66;
    public static final int CONFIRMATIONUI_ABORTED = 2;
    public static final int CONFIRMATIONUI_CANCELED = 1;
    public static final int CONFIRMATIONUI_IGNORED = 4;
    public static final int CONFIRMATIONUI_OK = 0;
    public static final int CONFIRMATIONUI_OPERATION_PENDING = 3;
    public static final int CONFIRMATIONUI_SYSTEM_ERROR = 5;
    public static final int CONFIRMATIONUI_UIERROR = 65536;
    public static final int CONFIRMATIONUI_UIERROR_MALFORMED_UTF8_ENCODING = 65539;
    public static final int CONFIRMATIONUI_UIERROR_MESSAGE_TOO_LONG = 65538;
    public static final int CONFIRMATIONUI_UIERROR_MISSING_GLYPH = 65537;
    public static final int CONFIRMATIONUI_UNEXPECTED = 7;
    public static final int CONFIRMATIONUI_UNIMPLEMENTED = 6;
    public static final int FLAG_CRITICAL_TO_DEVICE_ENCRYPTION = 8;
    public static final int FLAG_ENCRYPTED = 1;
    public static final int FLAG_NONE = 0;
    public static final int FLAG_SOFTWARE = 2;
    public static final int FLAG_STRONGBOX = 16;
    public static final int HARDWARE_TYPE_UNAVAILABLE = -68;
    public static final int KEY_ALREADY_EXISTS = 16;
    public static final int KEY_NOT_FOUND = 7;
    public static final int KEY_PERMANENTLY_INVALIDATED = 17;
    public static final int LOCKED = 2;
    @UnsupportedAppUsage
    public static final int NO_ERROR = 1;
    public static final int OP_AUTH_NEEDED = 15;
    public static final int PERMISSION_DENIED = 6;
    public static final int PROTOCOL_ERROR = 5;
    public static final int SYSTEM_ERROR = 4;
    private static final String TAG = "KeyStore";
    public static final int UID_SELF = -1;
    public static final int UNDEFINED_ACTION = 9;
    public static final int UNINITIALIZED = 3;
    public static final int VALUE_CORRUPTED = 8;
    public static final int WRONG_PASSWORD = 10;
    private final IKeystoreService mBinder;
    private final Context mContext;
    private int mError = 1;
    private IBinder mToken;

    private class CertificateChainPromise extends Stub implements DeathRecipient {
        private final CompletableFuture<KeyAttestationCallbackResult> future;

        private CertificateChainPromise() {
            this.future = new CompletableFuture();
        }

        public void onFinished(KeystoreResponse keystoreResponse, KeymasterCertificateChain certificateChain) throws RemoteException {
            this.future.complete(new KeyAttestationCallbackResult(keystoreResponse, certificateChain));
        }

        public final CompletableFuture<KeyAttestationCallbackResult> getFuture() {
            return this.future;
        }

        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    private class ExportKeyPromise extends IKeystoreExportKeyCallback.Stub implements DeathRecipient {
        private final CompletableFuture<ExportResult> future;

        private ExportKeyPromise() {
            this.future = new CompletableFuture();
        }

        public void onFinished(ExportResult exportKeyResult) throws RemoteException {
            this.future.complete(exportKeyResult);
        }

        public final CompletableFuture<ExportResult> getFuture() {
            return this.future;
        }

        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    private class KeyAttestationCallbackResult {
        private KeymasterCertificateChain certificateChain;
        private KeystoreResponse keystoreResponse;

        public KeyAttestationCallbackResult(KeystoreResponse keystoreResponse, KeymasterCertificateChain certificateChain) {
            this.keystoreResponse = keystoreResponse;
            this.certificateChain = certificateChain;
        }

        public KeystoreResponse getKeystoreResponse() {
            return this.keystoreResponse;
        }

        public void setKeystoreResponse(KeystoreResponse keystoreResponse) {
            this.keystoreResponse = keystoreResponse;
        }

        public KeymasterCertificateChain getCertificateChain() {
            return this.certificateChain;
        }

        public void setCertificateChain(KeymasterCertificateChain certificateChain) {
            this.certificateChain = certificateChain;
        }
    }

    private class KeyCharacteristicsCallbackResult {
        private KeyCharacteristics keyCharacteristics;
        private KeystoreResponse keystoreResponse;

        public KeyCharacteristicsCallbackResult(KeystoreResponse keystoreResponse, KeyCharacteristics keyCharacteristics) {
            this.keystoreResponse = keystoreResponse;
            this.keyCharacteristics = keyCharacteristics;
        }

        public KeystoreResponse getKeystoreResponse() {
            return this.keystoreResponse;
        }

        public void setKeystoreResponse(KeystoreResponse keystoreResponse) {
            this.keystoreResponse = keystoreResponse;
        }

        public KeyCharacteristics getKeyCharacteristics() {
            return this.keyCharacteristics;
        }

        public void setKeyCharacteristics(KeyCharacteristics keyCharacteristics) {
            this.keyCharacteristics = keyCharacteristics;
        }
    }

    private class KeyCharacteristicsPromise extends IKeystoreKeyCharacteristicsCallback.Stub implements DeathRecipient {
        private final CompletableFuture<KeyCharacteristicsCallbackResult> future;

        private KeyCharacteristicsPromise() {
            this.future = new CompletableFuture();
        }

        public void onFinished(KeystoreResponse keystoreResponse, KeyCharacteristics keyCharacteristics) throws RemoteException {
            this.future.complete(new KeyCharacteristicsCallbackResult(keystoreResponse, keyCharacteristics));
        }

        public final CompletableFuture<KeyCharacteristicsCallbackResult> getFuture() {
            return this.future;
        }

        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    private class KeystoreResultPromise extends IKeystoreResponseCallback.Stub implements DeathRecipient {
        private final CompletableFuture<KeystoreResponse> future;

        private KeystoreResultPromise() {
            this.future = new CompletableFuture();
        }

        public void onFinished(KeystoreResponse keystoreResponse) throws RemoteException {
            this.future.complete(keystoreResponse);
        }

        public final CompletableFuture<KeystoreResponse> getFuture() {
            return this.future;
        }

        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    private class OperationPromise extends IKeystoreOperationResultCallback.Stub implements DeathRecipient {
        private final CompletableFuture<OperationResult> future;

        private OperationPromise() {
            this.future = new CompletableFuture();
        }

        public void onFinished(OperationResult operationResult) throws RemoteException {
            this.future.complete(operationResult);
        }

        public final CompletableFuture<OperationResult> getFuture() {
            return this.future;
        }

        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    public enum State {
        UNLOCKED,
        LOCKED,
        UNINITIALIZED
    }

    private KeyStore(IKeystoreService binder) {
        this.mBinder = binder;
        this.mContext = getApplicationContext();
    }

    @UnsupportedAppUsage
    public static Context getApplicationContext() {
        Application application = ActivityThread.currentApplication();
        if (application != null) {
            return application;
        }
        throw new IllegalStateException("Failed to obtain application Context from ActivityThread");
    }

    @UnsupportedAppUsage
    public static KeyStore getInstance() {
        return new KeyStore(IKeystoreService.Stub.asInterface(ServiceManager.getService("android.security.keystore")));
    }

    private synchronized IBinder getToken() {
        if (this.mToken == null) {
            this.mToken = new Binder();
        }
        return this.mToken;
    }

    @UnsupportedAppUsage
    public State state(int userId) {
        try {
            int ret = this.mBinder.getState(userId);
            if (ret == 1) {
                return State.UNLOCKED;
            }
            if (ret == 2) {
                return State.LOCKED;
            }
            if (ret == 3) {
                return State.UNINITIALIZED;
            }
            throw new AssertionError(this.mError);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            throw new AssertionError(e);
        }
    }

    @UnsupportedAppUsage
    public State state() {
        return state(UserHandle.myUserId());
    }

    public boolean isUnlocked() {
        return state() == State.UNLOCKED;
    }

    public byte[] get(String key, int uid) {
        return get(key, uid, false);
    }

    @UnsupportedAppUsage
    public byte[] get(String key) {
        return get(key, -1);
    }

    public byte[] get(String key, int uid, boolean suppressKeyNotFoundWarning) {
        String str;
        String str2 = TAG;
        if (key != null) {
            str = key;
        } else {
            try {
                str = "";
            } catch (RemoteException e) {
                Log.w(str2, "Cannot connect to keystore", e);
                return null;
            } catch (ServiceSpecificException e2) {
                if (!(suppressKeyNotFoundWarning && e2.errorCode == 7)) {
                    Log.w(str2, "KeyStore exception", e2);
                }
                return null;
            }
        }
        return this.mBinder.get(str, uid);
    }

    public byte[] get(String key, boolean suppressKeyNotFoundWarning) {
        return get(key, -1, suppressKeyNotFoundWarning);
    }

    public boolean put(String key, byte[] value, int uid, int flags) {
        return insert(key, value, uid, flags) == 1;
    }

    public int insert(String key, byte[] value, int uid, int flags) {
        if (value == null) {
            try {
                value = new byte[0];
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        int error = this.mBinder.insert(key, value, uid, flags);
        if (error == 16) {
            this.mBinder.del(key, uid);
            error = this.mBinder.insert(key, value, uid, flags);
        }
        return error;
    }

    /* Access modifiers changed, original: 0000 */
    public int delete2(String key, int uid) {
        try {
            return this.mBinder.del(key, uid);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean delete(String key, int uid) {
        int ret = delete2(key, uid);
        return ret == 1 || ret == 7;
    }

    @UnsupportedAppUsage
    public boolean delete(String key) {
        return delete(key, -1);
    }

    public boolean contains(String key, int uid) {
        boolean z = false;
        try {
            if (this.mBinder.exist(key, uid) == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean contains(String key) {
        return contains(key, -1);
    }

    public String[] list(String prefix, int uid) {
        String str = TAG;
        try {
            str = this.mBinder.list(prefix, uid);
            return str;
        } catch (RemoteException e) {
            Log.w(str, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            Log.w(str, "KeyStore exception", e2);
            return null;
        }
    }

    @UnsupportedAppUsage
    public int[] listUidsOfAuthBoundKeys() {
        String str = TAG;
        List<String> uidsOut = new ArrayList();
        try {
            if (this.mBinder.listUidsOfAuthBoundKeys(uidsOut) == 1) {
                return uidsOut.stream().mapToInt(-$$Lambda$wddj3-hVVrg0MkscpMtYt3BzY8Y.INSTANCE).toArray();
            }
            Log.w(str, String.format("listUidsOfAuthBoundKeys failed with error code %d", new Object[]{Integer.valueOf(this.mBinder.listUidsOfAuthBoundKeys(uidsOut))}));
            return null;
        } catch (RemoteException e) {
            Log.w(str, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            Log.w(str, "KeyStore exception", e2);
            return null;
        }
    }

    public String[] list(String prefix) {
        return list(prefix, -1);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public boolean reset() {
        boolean z = false;
        try {
            if (this.mBinder.reset() == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock(int userId) {
        boolean z = false;
        try {
            if (this.mBinder.lock(userId) == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock() {
        return lock(UserHandle.myUserId());
    }

    public boolean unlock(int userId, String password) {
        String str;
        boolean z = false;
        if (password != null) {
            str = password;
        } else {
            try {
                str = "";
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return false;
            }
        }
        this.mError = this.mBinder.unlock(userId, str);
        if (this.mError == 1) {
            z = true;
        }
        return z;
    }

    @UnsupportedAppUsage
    public boolean unlock(String password) {
        return unlock(UserHandle.getUserId(Process.myUid()), password);
    }

    public boolean isEmpty(int userId) {
        boolean z = false;
        try {
            if (this.mBinder.isEmpty(userId) != 0) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public boolean isEmpty() {
        return isEmpty(UserHandle.myUserId());
    }

    public String grant(String key, int uid) {
        try {
            String grantAlias = this.mBinder.grant(key, uid);
            if (grantAlias == "") {
                return null;
            }
            return grantAlias;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public boolean ungrant(String key, int uid) {
        boolean z = false;
        try {
            if (this.mBinder.ungrant(key, uid) == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public long getmtime(String key, int uid) {
        try {
            long millis = this.mBinder.getmtime(key, uid);
            if (millis == -1) {
                return -1;
            }
            return 1000 * millis;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return -1;
        }
    }

    public long getmtime(String key) {
        return getmtime(key, -1);
    }

    public boolean isHardwareBacked() {
        return isHardwareBacked(KeyProperties.KEY_ALGORITHM_RSA);
    }

    public boolean isHardwareBacked(String keyType) {
        boolean z = false;
        try {
            if (this.mBinder.is_hardware_backed(keyType.toUpperCase(Locale.US)) == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean clearUid(int uid) {
        boolean z = false;
        try {
            if (this.mBinder.clear_uid((long) uid) == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int getLastError() {
        return this.mError;
    }

    public boolean addRngEntropy(byte[] data, int flags) {
        String str = TAG;
        KeystoreResultPromise promise = new KeystoreResultPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            boolean z = true;
            if (this.mBinder.addRngEntropy(promise, data, flags) == 1) {
                if (((KeystoreResponse) promise.getFuture().get()).getErrorCode() != 1) {
                    z = false;
                }
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return z;
            }
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return false;
        } catch (RemoteException e) {
            Log.w(str, "Cannot connect to keystore", e);
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return false;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(str, "AddRngEntropy completed with exception", e2);
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return false;
        } catch (Throwable th) {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            throw th;
        }
    }

    private int generateKeyInternal(String alias, KeymasterArguments args, byte[] entropy, int uid, int flags, KeyCharacteristics outCharacteristics) throws RemoteException, ExecutionException, InterruptedException {
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int error = this.mBinder.generateKey(promise, alias, args, entropy, uid, flags);
            String str = TAG;
            StringBuilder stringBuilder;
            if (error != 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("generateKeyInternal failed on request ");
                stringBuilder.append(error);
                Log.e(str, stringBuilder.toString());
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristicsCallbackResult result = (KeyCharacteristicsCallbackResult) promise.getFuture().get();
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            int error2 = result.getKeystoreResponse().getErrorCode();
            if (error2 != 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("generateKeyInternal failed on response ");
                stringBuilder.append(error2);
                Log.e(str, stringBuilder.toString());
                return error2;
            }
            KeyCharacteristics characteristics = result.getKeyCharacteristics();
            if (characteristics == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("generateKeyInternal got empty key cheractariestics ");
                stringBuilder.append(error2);
                Log.e(str, stringBuilder.toString());
                return 4;
            }
            outCharacteristics.shallowCopyFrom(characteristics);
            return 1;
        } catch (Throwable th) {
            KeyCharacteristics keyCharacteristics = outCharacteristics;
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int uid, int flags, KeyCharacteristics outCharacteristics) {
        byte[] entropy2;
        RemoteException e;
        String str;
        int i;
        KeymasterArguments keymasterArguments;
        Exception e2;
        int i2 = 4;
        String str2 = TAG;
        if (entropy != null) {
            entropy2 = entropy;
        } else {
            try {
                entropy2 = new byte[0];
            } catch (RemoteException e3) {
                e = e3;
                str = alias;
                i = uid;
                keymasterArguments = args;
                entropy2 = entropy;
                Log.w(str2, "Cannot connect to keystore", e);
                return i2;
            } catch (InterruptedException | ExecutionException e4) {
                e2 = e4;
                str = alias;
                i = uid;
                keymasterArguments = args;
                entropy2 = entropy;
                Log.e(str2, "generateKey completed with exception", e2);
                return i2;
            }
        }
        if (args != null) {
            keymasterArguments = args;
        } else {
            try {
                keymasterArguments = new KeymasterArguments();
            } catch (RemoteException e5) {
                e = e5;
                str = alias;
                i = uid;
                keymasterArguments = args;
                Log.w(str2, "Cannot connect to keystore", e);
                return i2;
            } catch (InterruptedException | ExecutionException e6) {
                e2 = e6;
                str = alias;
                i = uid;
                keymasterArguments = args;
                Log.e(str2, "generateKey completed with exception", e2);
                return i2;
            }
        }
        try {
            int error = generateKeyInternal(alias, keymasterArguments, entropy2, uid, flags, outCharacteristics);
            if (error == 16) {
                try {
                } catch (RemoteException e7) {
                    e = e7;
                    str = alias;
                    i = uid;
                    Log.w(str2, "Cannot connect to keystore", e);
                    return i2;
                } catch (InterruptedException | ExecutionException e8) {
                    e2 = e8;
                    str = alias;
                    i = uid;
                    Log.e(str2, "generateKey completed with exception", e2);
                    return i2;
                }
                try {
                    this.mBinder.del(alias, uid);
                    i2 = generateKeyInternal(alias, keymasterArguments, entropy2, uid, flags, outCharacteristics);
                    error = i2;
                } catch (RemoteException e9) {
                    e = e9;
                    Log.w(str2, "Cannot connect to keystore", e);
                    return i2;
                } catch (InterruptedException | ExecutionException e10) {
                    e2 = e10;
                    Log.e(str2, "generateKey completed with exception", e2);
                    return i2;
                }
            }
            str = alias;
            i = uid;
            return error;
        } catch (RemoteException e11) {
            e = e11;
            str = alias;
            i = uid;
            Log.w(str2, "Cannot connect to keystore", e);
            return i2;
        } catch (InterruptedException | ExecutionException e12) {
            e2 = e12;
            str = alias;
            i = uid;
            Log.e(str2, "generateKey completed with exception", e2);
            return i2;
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int flags, KeyCharacteristics outCharacteristics) {
        return generateKey(alias, args, entropy, -1, flags, outCharacteristics);
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:45:0x0092=Splitter:B:45:0x0092, B:51:0x00a5=Splitter:B:51:0x00a5} */
    public int getKeyCharacteristics(java.lang.String r11, android.security.keymaster.KeymasterBlob r12, android.security.keymaster.KeymasterBlob r13, int r14, android.security.keymaster.KeyCharacteristics r15) {
        /*
        r10 = this;
        r0 = "KeyStore";
        r1 = new android.security.KeyStore$KeyCharacteristicsPromise;
        r2 = 0;
        r1.<init>();
        r2 = 4;
        r9 = 0;
        r3 = r10.mBinder;	 Catch:{ RemoteException -> 0x00a2, InterruptedException | ExecutionException -> 0x008f, InterruptedException | ExecutionException -> 0x008f, all -> 0x008b }
        r3 = r3.asBinder();	 Catch:{ RemoteException -> 0x00a2, InterruptedException | ExecutionException -> 0x008f, InterruptedException | ExecutionException -> 0x008f, all -> 0x008b }
        r3.linkToDeath(r1, r9);	 Catch:{ RemoteException -> 0x00a2, InterruptedException | ExecutionException -> 0x008f, InterruptedException | ExecutionException -> 0x008f, all -> 0x008b }
        if (r12 == 0) goto L_0x0017;
    L_0x0015:
        r6 = r12;
        goto L_0x001f;
    L_0x0017:
        r3 = new android.security.keymaster.KeymasterBlob;	 Catch:{ RemoteException -> 0x00a2, InterruptedException | ExecutionException -> 0x008f, InterruptedException | ExecutionException -> 0x008f, all -> 0x008b }
        r4 = new byte[r9];	 Catch:{ RemoteException -> 0x00a2, InterruptedException | ExecutionException -> 0x008f, InterruptedException | ExecutionException -> 0x008f, all -> 0x008b }
        r3.<init>(r4);	 Catch:{ RemoteException -> 0x00a2, InterruptedException | ExecutionException -> 0x008f, InterruptedException | ExecutionException -> 0x008f, all -> 0x008b }
        r6 = r3;
    L_0x001f:
        if (r13 == 0) goto L_0x0023;
    L_0x0021:
        r7 = r13;
        goto L_0x002b;
    L_0x0023:
        r12 = new android.security.keymaster.KeymasterBlob;	 Catch:{ RemoteException -> 0x0089, InterruptedException | ExecutionException -> 0x0087, InterruptedException | ExecutionException -> 0x0087 }
        r3 = new byte[r9];	 Catch:{ RemoteException -> 0x0089, InterruptedException | ExecutionException -> 0x0087, InterruptedException | ExecutionException -> 0x0087 }
        r12.<init>(r3);	 Catch:{ RemoteException -> 0x0089, InterruptedException | ExecutionException -> 0x0087, InterruptedException | ExecutionException -> 0x0087 }
        r7 = r12;
    L_0x002b:
        r3 = r10.mBinder;	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r4 = r1;
        r5 = r11;
        r8 = r14;
        r12 = r3.getKeyCharacteristics(r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r13 = 1;
        if (r12 == r13) goto L_0x0041;
    L_0x0037:
        r13 = r10.mBinder;
        r13 = r13.asBinder();
        r13.unlinkToDeath(r1, r9);
        return r12;
    L_0x0041:
        r3 = r1.getFuture();	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r3 = r3.get();	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r3 = (android.security.KeyStore.KeyCharacteristicsCallbackResult) r3;	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r4 = r3.getKeystoreResponse();	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r4 = r4.getErrorCode();	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r12 = r4;
        if (r12 == r13) goto L_0x0060;
    L_0x0056:
        r13 = r10.mBinder;
        r13 = r13.asBinder();
        r13.unlinkToDeath(r1, r9);
        return r12;
    L_0x0060:
        r4 = r3.getKeyCharacteristics();	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        if (r4 != 0) goto L_0x0070;
    L_0x0066:
        r13 = r10.mBinder;
        r13 = r13.asBinder();
        r13.unlinkToDeath(r1, r9);
        return r2;
    L_0x0070:
        r15.shallowCopyFrom(r4);	 Catch:{ RemoteException -> 0x0084, InterruptedException | ExecutionException -> 0x0081, InterruptedException | ExecutionException -> 0x0081, all -> 0x007e }
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        return r13;
    L_0x007e:
        r12 = move-exception;
        r13 = r7;
        goto L_0x00b6;
    L_0x0081:
        r12 = move-exception;
        r13 = r7;
        goto L_0x0092;
    L_0x0084:
        r12 = move-exception;
        r13 = r7;
        goto L_0x00a5;
    L_0x0087:
        r12 = move-exception;
        goto L_0x0092;
    L_0x0089:
        r12 = move-exception;
        goto L_0x00a5;
    L_0x008b:
        r0 = move-exception;
        r6 = r12;
        r12 = r0;
        goto L_0x00b6;
    L_0x008f:
        r3 = move-exception;
        r6 = r12;
        r12 = r3;
    L_0x0092:
        r3 = "GetKeyCharacteristics completed with exception";
        android.util.Log.e(r0, r3, r12);	 Catch:{ all -> 0x00b5 }
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        return r2;
    L_0x00a2:
        r3 = move-exception;
        r6 = r12;
        r12 = r3;
    L_0x00a5:
        r3 = "Cannot connect to keystore";
        android.util.Log.w(r0, r3, r12);	 Catch:{ all -> 0x00b5 }
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        return r2;
    L_0x00b5:
        r12 = move-exception;
    L_0x00b6:
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.KeyStore.getKeyCharacteristics(java.lang.String, android.security.keymaster.KeymasterBlob, android.security.keymaster.KeymasterBlob, int, android.security.keymaster.KeyCharacteristics):int");
    }

    public int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appId, KeyCharacteristics outCharacteristics) {
        return getKeyCharacteristics(alias, clientId, appId, -1, outCharacteristics);
    }

    private int importKeyInternal(String alias, KeymasterArguments args, int format, byte[] keyData, int uid, int flags, KeyCharacteristics outCharacteristics) throws RemoteException, ExecutionException, InterruptedException {
        Throwable th;
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        this.mBinder.asBinder().linkToDeath(promise, 0);
        try {
            int error = this.mBinder.importKey(promise, alias, args, format, keyData, uid, flags);
            if (error != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristicsCallbackResult result = (KeyCharacteristicsCallbackResult) promise.getFuture().get();
            error = result.getKeystoreResponse().getErrorCode();
            if (error != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristics characteristics = result.getKeyCharacteristics();
            if (characteristics == null) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 4;
            }
            try {
                outCharacteristics.shallowCopyFrom(characteristics);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 1;
            } catch (Throwable th2) {
                th = th2;
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            KeyCharacteristics keyCharacteristics = outCharacteristics;
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            throw th;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int uid, int flags, KeyCharacteristics outCharacteristics) {
        String str = TAG;
        try {
            int error = importKeyInternal(alias, args, format, keyData, uid, flags, outCharacteristics);
            if (error == 16) {
                this.mBinder.del(alias, uid);
                error = importKeyInternal(alias, args, format, keyData, uid, flags, outCharacteristics);
            }
            return error;
        } catch (RemoteException e) {
            Log.w(str, "Cannot connect to keystore", e);
            return 4;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(str, "ImportKey completed with exception", e2);
            return 4;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int flags, KeyCharacteristics outCharacteristics) {
        return importKey(alias, args, format, keyData, -1, flags, outCharacteristics);
    }

    private String getAlgorithmFromPKCS8(byte[] keyData) {
        try {
            return new AlgorithmId(new ObjectIdentifier(PrivateKeyInfo.getInstance(new ASN1InputStream(new ByteArrayInputStream(keyData)).readObject()).getPrivateKeyAlgorithm().getAlgorithm().getId())).getName();
        } catch (IOException e) {
            String str = TAG;
            Log.e(str, "getAlgorithmFromPKCS8 Failed to parse key data");
            Log.e(str, Log.getStackTraceString(e));
            return null;
        }
    }

    private KeymasterArguments makeLegacyArguments(String algorithm) {
        KeymasterArguments args = new KeymasterArguments();
        args.addEnum(268435458, KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(algorithm));
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 2);
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 3);
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 0);
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 1);
        args.addEnum(KeymasterDefs.KM_TAG_PADDING, 1);
        if (algorithm.equalsIgnoreCase(KeyProperties.KEY_ALGORITHM_RSA)) {
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 2);
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 4);
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 5);
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 3);
        }
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 0);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 1);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 2);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 3);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 4);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 5);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 6);
        args.addBoolean(KeymasterDefs.KM_TAG_NO_AUTH_REQUIRED);
        args.addDate(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, new Date(Long.MAX_VALUE));
        args.addDate(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, new Date(Long.MAX_VALUE));
        args.addDate(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, new Date(0));
        return args;
    }

    public boolean importKey(String alias, byte[] keyData, int uid, int flags) {
        String algorithm = getAlgorithmFromPKCS8(keyData);
        if (algorithm == null) {
            return false;
        }
        int result = importKey(alias, makeLegacyArguments(algorithm), 1, keyData, uid, flags, new KeyCharacteristics());
        if (result == 1) {
            return true;
        }
        Log.e(TAG, Log.getStackTraceString(new KeyStoreException(result, "legacy key import failed")));
        return false;
    }

    private int importWrappedKeyInternal(String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments args, long rootSid, long fingerprintSid, KeyCharacteristics outCharacteristics) throws RemoteException, ExecutionException, InterruptedException {
        Throwable th;
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        this.mBinder.asBinder().linkToDeath(promise, 0);
        try {
            int error = this.mBinder.importWrappedKey(promise, wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, args, rootSid, fingerprintSid);
            if (error != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristicsCallbackResult result = (KeyCharacteristicsCallbackResult) promise.getFuture().get();
            error = result.getKeystoreResponse().getErrorCode();
            if (error != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristics characteristics = result.getKeyCharacteristics();
            if (characteristics == null) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 4;
            }
            try {
                outCharacteristics.shallowCopyFrom(characteristics);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 1;
            } catch (Throwable th2) {
                th = th2;
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            KeyCharacteristics keyCharacteristics = outCharacteristics;
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            throw th;
        }
    }

    public int importWrappedKey(String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments args, long rootSid, long fingerprintSid, int uid, KeyCharacteristics outCharacteristics) {
        RemoteException e;
        String str;
        Exception e2;
        String str2 = TAG;
        try {
            int error = importWrappedKeyInternal(wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, args, rootSid, fingerprintSid, outCharacteristics);
            if (error == 16) {
                try {
                } catch (RemoteException e3) {
                    e = e3;
                    str = wrappedKeyAlias;
                    Log.w(str2, "Cannot connect to keystore", e);
                    return 4;
                } catch (InterruptedException | ExecutionException e4) {
                    e2 = e4;
                    str = wrappedKeyAlias;
                    Log.e(str2, "ImportWrappedKey completed with exception", e2);
                    return 4;
                }
                try {
                    this.mBinder.del(wrappedKeyAlias, -1);
                    str2 = importWrappedKeyInternal(wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, args, rootSid, fingerprintSid, outCharacteristics);
                    error = str2;
                } catch (RemoteException e5) {
                    e = e5;
                    Log.w(str2, "Cannot connect to keystore", e);
                    return 4;
                } catch (InterruptedException | ExecutionException e6) {
                    e2 = e6;
                    Log.e(str2, "ImportWrappedKey completed with exception", e2);
                    return 4;
                }
            }
            str = wrappedKeyAlias;
            return error;
        } catch (RemoteException e7) {
            e = e7;
            str = wrappedKeyAlias;
            Log.w(str2, "Cannot connect to keystore", e);
            return 4;
        } catch (InterruptedException | ExecutionException e8) {
            e2 = e8;
            str = wrappedKeyAlias;
            Log.e(str2, "ImportWrappedKey completed with exception", e2);
            return 4;
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:34:0x007a=Splitter:B:34:0x007a, B:40:0x008f=Splitter:B:40:0x008f} */
    public android.security.keymaster.ExportResult exportKey(java.lang.String r14, int r15, android.security.keymaster.KeymasterBlob r16, android.security.keymaster.KeymasterBlob r17, int r18) {
        /*
        r13 = this;
        r1 = r13;
        r2 = "KeyStore";
        r0 = new android.security.KeyStore$ExportKeyPromise;
        r3 = 0;
        r0.<init>();
        r11 = r0;
        r12 = 0;
        r0 = r1.mBinder;	 Catch:{ RemoteException -> 0x008a, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x006f }
        r0 = r0.asBinder();	 Catch:{ RemoteException -> 0x008a, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x006f }
        r0.linkToDeath(r11, r12);	 Catch:{ RemoteException -> 0x008a, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x006f }
        if (r16 == 0) goto L_0x0019;
    L_0x0016:
        r8 = r16;
        goto L_0x0021;
    L_0x0019:
        r0 = new android.security.keymaster.KeymasterBlob;	 Catch:{ RemoteException -> 0x008a, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x006f }
        r4 = new byte[r12];	 Catch:{ RemoteException -> 0x008a, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x006f }
        r0.<init>(r4);	 Catch:{ RemoteException -> 0x008a, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x006f }
        r8 = r0;
    L_0x0021:
        if (r17 == 0) goto L_0x0026;
    L_0x0023:
        r9 = r17;
        goto L_0x002e;
    L_0x0026:
        r0 = new android.security.keymaster.KeymasterBlob;	 Catch:{ RemoteException -> 0x006b, InterruptedException | ExecutionException -> 0x0067, InterruptedException | ExecutionException -> 0x0067, all -> 0x0063 }
        r4 = new byte[r12];	 Catch:{ RemoteException -> 0x006b, InterruptedException | ExecutionException -> 0x0067, InterruptedException | ExecutionException -> 0x0067, all -> 0x0063 }
        r0.<init>(r4);	 Catch:{ RemoteException -> 0x006b, InterruptedException | ExecutionException -> 0x0067, InterruptedException | ExecutionException -> 0x0067, all -> 0x0063 }
        r9 = r0;
    L_0x002e:
        r4 = r1.mBinder;	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r5 = r11;
        r6 = r14;
        r7 = r15;
        r10 = r18;
        r0 = r4.exportKey(r5, r6, r7, r8, r9, r10);	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r4 = 1;
        if (r0 != r4) goto L_0x0050;
    L_0x003c:
        r4 = r11.getFuture();	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r4 = r4.get();	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r4 = (android.security.keymaster.ExportResult) r4;	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r11, r12);
        return r4;
    L_0x0050:
        r4 = new android.security.keymaster.ExportResult;	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r4.<init>(r0);	 Catch:{ RemoteException -> 0x0061, InterruptedException | ExecutionException -> 0x005f, InterruptedException | ExecutionException -> 0x005f }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r11, r12);
        return r4;
    L_0x005f:
        r0 = move-exception;
        goto L_0x007a;
    L_0x0061:
        r0 = move-exception;
        goto L_0x008f;
    L_0x0063:
        r0 = move-exception;
        r9 = r17;
        goto L_0x00a0;
    L_0x0067:
        r0 = move-exception;
        r9 = r17;
        goto L_0x007a;
    L_0x006b:
        r0 = move-exception;
        r9 = r17;
        goto L_0x008f;
    L_0x006f:
        r0 = move-exception;
        r8 = r16;
        r9 = r17;
        goto L_0x00a0;
    L_0x0075:
        r0 = move-exception;
        r8 = r16;
        r9 = r17;
    L_0x007a:
        r4 = "ExportKey completed with exception";
        android.util.Log.e(r2, r4, r0);	 Catch:{ all -> 0x009f }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r11, r12);
        return r3;
    L_0x008a:
        r0 = move-exception;
        r8 = r16;
        r9 = r17;
    L_0x008f:
        r4 = "Cannot connect to keystore";
        android.util.Log.w(r2, r4, r0);	 Catch:{ all -> 0x009f }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r11, r12);
        return r3;
    L_0x009f:
        r0 = move-exception;
    L_0x00a0:
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r11, r12);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.KeyStore.exportKey(java.lang.String, int, android.security.keymaster.KeymasterBlob, android.security.keymaster.KeymasterBlob, int):android.security.keymaster.ExportResult");
    }

    public ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appId) {
        return exportKey(alias, format, clientId, appId, -1);
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:40:0x0090=Splitter:B:40:0x0090, B:34:0x007b=Splitter:B:34:0x007b} */
    public android.security.keymaster.OperationResult begin(java.lang.String r16, int r17, boolean r18, android.security.keymaster.KeymasterArguments r19, byte[] r20, int r21) {
        /*
        r15 = this;
        r1 = r15;
        r2 = "KeyStore";
        r0 = new android.security.KeyStore$OperationPromise;
        r3 = 0;
        r0.<init>();
        r13 = r0;
        r14 = 0;
        r0 = r1.mBinder;	 Catch:{ RemoteException -> 0x008b, InterruptedException | ExecutionException -> 0x0076, InterruptedException | ExecutionException -> 0x0076, all -> 0x0070 }
        r0 = r0.asBinder();	 Catch:{ RemoteException -> 0x008b, InterruptedException | ExecutionException -> 0x0076, InterruptedException | ExecutionException -> 0x0076, all -> 0x0070 }
        r0.linkToDeath(r13, r14);	 Catch:{ RemoteException -> 0x008b, InterruptedException | ExecutionException -> 0x0076, InterruptedException | ExecutionException -> 0x0076, all -> 0x0070 }
        if (r19 == 0) goto L_0x0019;
    L_0x0016:
        r10 = r19;
        goto L_0x001f;
    L_0x0019:
        r0 = new android.security.keymaster.KeymasterArguments;	 Catch:{ RemoteException -> 0x008b, InterruptedException | ExecutionException -> 0x0076, InterruptedException | ExecutionException -> 0x0076, all -> 0x0070 }
        r0.<init>();	 Catch:{ RemoteException -> 0x008b, InterruptedException | ExecutionException -> 0x0076, InterruptedException | ExecutionException -> 0x0076, all -> 0x0070 }
        r10 = r0;
    L_0x001f:
        if (r20 == 0) goto L_0x0024;
    L_0x0021:
        r11 = r20;
        goto L_0x0027;
    L_0x0024:
        r0 = new byte[r14];	 Catch:{ RemoteException -> 0x006c, InterruptedException | ExecutionException -> 0x0068, InterruptedException | ExecutionException -> 0x0068, all -> 0x0064 }
        r11 = r0;
    L_0x0027:
        r4 = r1.mBinder;	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r6 = r15.getToken();	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r5 = r13;
        r7 = r16;
        r8 = r17;
        r9 = r18;
        r12 = r21;
        r0 = r4.begin(r5, r6, r7, r8, r9, r10, r11, r12);	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r4 = 1;
        if (r0 != r4) goto L_0x0051;
    L_0x003d:
        r4 = r13.getFuture();	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r4 = r4.get();	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r4 = (android.security.keymaster.OperationResult) r4;	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r13, r14);
        return r4;
    L_0x0051:
        r4 = new android.security.keymaster.OperationResult;	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r4.<init>(r0);	 Catch:{ RemoteException -> 0x0062, InterruptedException | ExecutionException -> 0x0060, InterruptedException | ExecutionException -> 0x0060 }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r13, r14);
        return r4;
    L_0x0060:
        r0 = move-exception;
        goto L_0x007b;
    L_0x0062:
        r0 = move-exception;
        goto L_0x0090;
    L_0x0064:
        r0 = move-exception;
        r11 = r20;
        goto L_0x00a1;
    L_0x0068:
        r0 = move-exception;
        r11 = r20;
        goto L_0x007b;
    L_0x006c:
        r0 = move-exception;
        r11 = r20;
        goto L_0x0090;
    L_0x0070:
        r0 = move-exception;
        r10 = r19;
        r11 = r20;
        goto L_0x00a1;
    L_0x0076:
        r0 = move-exception;
        r10 = r19;
        r11 = r20;
    L_0x007b:
        r4 = "Begin completed with exception";
        android.util.Log.e(r2, r4, r0);	 Catch:{ all -> 0x00a0 }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r13, r14);
        return r3;
    L_0x008b:
        r0 = move-exception;
        r10 = r19;
        r11 = r20;
    L_0x0090:
        r4 = "Cannot connect to keystore";
        android.util.Log.w(r2, r4, r0);	 Catch:{ all -> 0x00a0 }
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r13, r14);
        return r3;
    L_0x00a0:
        r0 = move-exception;
    L_0x00a1:
        r2 = r1.mBinder;
        r2 = r2.asBinder();
        r2.unlinkToDeath(r13, r14);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.KeyStore.begin(java.lang.String, int, boolean, android.security.keymaster.KeymasterArguments, byte[], int):android.security.keymaster.OperationResult");
    }

    public OperationResult begin(String alias, int purpose, boolean pruneable, KeymasterArguments args, byte[] entropy) {
        return begin(alias, purpose, pruneable, args != null ? args : new KeymasterArguments(), entropy != null ? entropy : new byte[0], -1);
    }

    public OperationResult update(IBinder token, KeymasterArguments arguments, byte[] input) {
        String str = TAG;
        OperationResult operationResult = null;
        OperationPromise promise = new OperationPromise();
        OperationResult operationResult2;
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int errorCode = this.mBinder.update(promise, token, arguments != null ? arguments : new KeymasterArguments(), input != null ? input : new byte[0]);
            operationResult2 = true;
            if (errorCode == 1) {
                operationResult2 = (OperationResult) promise.getFuture().get();
                return operationResult2;
            }
            operationResult2 = new OperationResult(errorCode);
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return operationResult2;
        } catch (RemoteException e) {
            operationResult2 = "Cannot connect to keystore";
            Log.w(str, operationResult2, e);
            return operationResult;
        } catch (InterruptedException | ExecutionException e2) {
            operationResult2 = "Update completed with exception";
            Log.e(str, operationResult2, e2);
            return operationResult;
        } finally {
            operationResult = this.mBinder.asBinder();
            operationResult.unlinkToDeath(promise, 0);
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:45:0x0078=Splitter:B:45:0x0078, B:51:0x008b=Splitter:B:51:0x008b} */
    public android.security.keymaster.OperationResult finish(android.os.IBinder r11, android.security.keymaster.KeymasterArguments r12, byte[] r13, byte[] r14) {
        /*
        r10 = this;
        r0 = "KeyStore";
        r1 = new android.security.KeyStore$OperationPromise;
        r2 = 0;
        r1.<init>();
        r9 = 0;
        r3 = r10.mBinder;	 Catch:{ RemoteException -> 0x0088, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x0071 }
        r3 = r3.asBinder();	 Catch:{ RemoteException -> 0x0088, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x0071 }
        r3.linkToDeath(r1, r9);	 Catch:{ RemoteException -> 0x0088, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x0071 }
        if (r12 == 0) goto L_0x0016;
    L_0x0014:
        r6 = r12;
        goto L_0x001c;
    L_0x0016:
        r3 = new android.security.keymaster.KeymasterArguments;	 Catch:{ RemoteException -> 0x0088, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x0071 }
        r3.<init>();	 Catch:{ RemoteException -> 0x0088, InterruptedException | ExecutionException -> 0x0075, InterruptedException | ExecutionException -> 0x0075, all -> 0x0071 }
        r6 = r3;
    L_0x001c:
        if (r14 == 0) goto L_0x0020;
    L_0x001e:
        r8 = r14;
        goto L_0x0023;
    L_0x0020:
        r12 = new byte[r9];	 Catch:{ RemoteException -> 0x006f, InterruptedException | ExecutionException -> 0x006d, InterruptedException | ExecutionException -> 0x006d }
        r8 = r12;
    L_0x0023:
        if (r13 == 0) goto L_0x0027;
    L_0x0025:
        r7 = r13;
        goto L_0x002a;
    L_0x0027:
        r12 = new byte[r9];	 Catch:{ RemoteException -> 0x006a, InterruptedException | ExecutionException -> 0x0067, InterruptedException | ExecutionException -> 0x0067, all -> 0x0064 }
        r7 = r12;
    L_0x002a:
        r3 = r10.mBinder;	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r4 = r1;
        r5 = r11;
        r12 = r3.finish(r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r13 = 1;
        if (r12 != r13) goto L_0x0049;
    L_0x0035:
        r13 = r1.getFuture();	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r13 = r13.get();	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r13 = (android.security.keymaster.OperationResult) r13;	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r14 = r10.mBinder;
        r14 = r14.asBinder();
        r14.unlinkToDeath(r1, r9);
        return r13;
    L_0x0049:
        r13 = new android.security.keymaster.OperationResult;	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r13.<init>(r12);	 Catch:{ RemoteException -> 0x0060, InterruptedException | ExecutionException -> 0x005c, InterruptedException | ExecutionException -> 0x005c, all -> 0x0058 }
        r14 = r10.mBinder;
        r14 = r14.asBinder();
        r14.unlinkToDeath(r1, r9);
        return r13;
    L_0x0058:
        r12 = move-exception;
        r13 = r7;
        r14 = r8;
        goto L_0x009c;
    L_0x005c:
        r12 = move-exception;
        r13 = r7;
        r14 = r8;
        goto L_0x0078;
    L_0x0060:
        r12 = move-exception;
        r13 = r7;
        r14 = r8;
        goto L_0x008b;
    L_0x0064:
        r12 = move-exception;
        r14 = r8;
        goto L_0x009c;
    L_0x0067:
        r12 = move-exception;
        r14 = r8;
        goto L_0x0078;
    L_0x006a:
        r12 = move-exception;
        r14 = r8;
        goto L_0x008b;
    L_0x006d:
        r12 = move-exception;
        goto L_0x0078;
    L_0x006f:
        r12 = move-exception;
        goto L_0x008b;
    L_0x0071:
        r0 = move-exception;
        r6 = r12;
        r12 = r0;
        goto L_0x009c;
    L_0x0075:
        r3 = move-exception;
        r6 = r12;
        r12 = r3;
    L_0x0078:
        r3 = "Finish completed with exception";
        android.util.Log.e(r0, r3, r12);	 Catch:{ all -> 0x009b }
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        return r2;
    L_0x0088:
        r3 = move-exception;
        r6 = r12;
        r12 = r3;
    L_0x008b:
        r3 = "Cannot connect to keystore";
        android.util.Log.w(r0, r3, r12);	 Catch:{ all -> 0x009b }
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        return r2;
    L_0x009b:
        r12 = move-exception;
    L_0x009c:
        r0 = r10.mBinder;
        r0 = r0.asBinder();
        r0.unlinkToDeath(r1, r9);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.KeyStore.finish(android.os.IBinder, android.security.keymaster.KeymasterArguments, byte[], byte[]):android.security.keymaster.OperationResult");
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature) {
        return finish(token, arguments, signature, null);
    }

    public int abort(IBinder token) {
        String str = TAG;
        KeystoreResultPromise promise = new KeystoreResultPromise();
        int i = 4;
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int errorCode = this.mBinder.abort(promise, token);
            if (errorCode == 1) {
                int errorCode2 = ((KeystoreResponse) promise.getFuture().get()).getErrorCode();
                return errorCode2;
            }
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return errorCode;
        } catch (RemoteException e) {
            Log.w(str, "Cannot connect to keystore", e);
            return i;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(str, "Abort completed with exception", e2);
            return i;
        } finally {
            i = this.mBinder.asBinder();
            i.unlinkToDeath(promise, 0);
            return i;
        }
    }

    public int addAuthToken(byte[] authToken) {
        try {
            return this.mBinder.addAuthToken(authToken);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean onUserPasswordChanged(int userId, String newPassword) {
        if (newPassword == null) {
            newPassword = "";
        }
        boolean z = false;
        try {
            if (this.mBinder.onUserPasswordChanged(userId, newPassword) == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public void onUserAdded(int userId, int parentId) {
        try {
            this.mBinder.onUserAdded(userId, parentId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public void onUserAdded(int userId) {
        onUserAdded(userId, -1);
    }

    public void onUserRemoved(int userId) {
        try {
            this.mBinder.onUserRemoved(userId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public boolean onUserPasswordChanged(String newPassword) {
        return onUserPasswordChanged(UserHandle.getUserId(Process.myUid()), newPassword);
    }

    public int attestKey(String alias, KeymasterArguments params, KeymasterCertificateChain outChain) {
        String str = TAG;
        CertificateChainPromise promise = new CertificateChainPromise();
        int i = 4;
        int error;
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            if (params == null) {
                params = new KeymasterArguments();
            }
            if (outChain == null) {
                outChain = new KeymasterCertificateChain();
            }
            error = this.mBinder.attestKey(promise, alias, params);
            if (error != 1) {
                return error;
            }
            KeyAttestationCallbackResult result = (KeyAttestationCallbackResult) promise.getFuture().get();
            error = result.getKeystoreResponse().getErrorCode();
            if (error == 1) {
                outChain.shallowCopyFrom(result.getCertificateChain());
            }
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return error;
        } catch (RemoteException e) {
            error = e;
            Log.w(str, "Cannot connect to keystore", error);
            return i;
        } catch (InterruptedException | ExecutionException e2) {
            error = e2;
            Log.e(str, "AttestKey completed with exception", error);
            return i;
        } finally {
            i = this.mBinder.asBinder();
            i.unlinkToDeath(promise, 0);
        }
    }

    public int attestDeviceIds(KeymasterArguments params, KeymasterCertificateChain outChain) {
        String str = TAG;
        CertificateChainPromise promise = new CertificateChainPromise();
        int i = 4;
        int error;
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            if (params == null) {
                params = new KeymasterArguments();
            }
            if (outChain == null) {
                outChain = new KeymasterCertificateChain();
            }
            error = this.mBinder.attestDeviceIds(promise, params);
            if (error != 1) {
                return error;
            }
            KeyAttestationCallbackResult result = (KeyAttestationCallbackResult) promise.getFuture().get();
            error = result.getKeystoreResponse().getErrorCode();
            if (error == 1) {
                outChain.shallowCopyFrom(result.getCertificateChain());
            }
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            return error;
        } catch (RemoteException e) {
            error = e;
            Log.w(str, "Cannot connect to keystore", error);
            return i;
        } catch (InterruptedException | ExecutionException e2) {
            error = e2;
            Log.e(str, "AttestDevicdeIds completed with exception", error);
            return i;
        } finally {
            i = this.mBinder.asBinder();
            i.unlinkToDeath(promise, 0);
        }
    }

    public void onDeviceOffBody() {
        try {
            this.mBinder.onDeviceOffBody();
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public int presentConfirmationPrompt(IBinder listener, String promptText, byte[] extraData, String locale, int uiOptionsAsFlags) {
        try {
            return this.mBinder.presentConfirmationPrompt(listener, promptText, extraData, locale, uiOptionsAsFlags);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 5;
        }
    }

    public int cancelConfirmationPrompt(IBinder listener) {
        try {
            return this.mBinder.cancelConfirmationPrompt(listener);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 5;
        }
    }

    public boolean isConfirmationPromptSupported() {
        try {
            return this.mBinder.isConfirmationPromptSupported();
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    @UnsupportedAppUsage
    public static KeyStoreException getKeyStoreException(int errorCode) {
        if (errorCode > 0) {
            if (errorCode == 1) {
                return new KeyStoreException(errorCode, "OK");
            }
            if (errorCode == 2) {
                return new KeyStoreException(errorCode, "User authentication required");
            }
            if (errorCode == 3) {
                return new KeyStoreException(errorCode, "Keystore not initialized");
            }
            if (errorCode == 4) {
                return new KeyStoreException(errorCode, "System error");
            }
            if (errorCode == 6) {
                return new KeyStoreException(errorCode, "Permission denied");
            }
            if (errorCode == 7) {
                return new KeyStoreException(errorCode, "Key not found");
            }
            if (errorCode == 8) {
                return new KeyStoreException(errorCode, "Key blob corrupted");
            }
            if (errorCode == 15) {
                return new KeyStoreException(errorCode, "Operation requires authorization");
            }
            if (errorCode != 17) {
                return new KeyStoreException(errorCode, String.valueOf(errorCode));
            }
            return new KeyStoreException(errorCode, "Key permanently invalidated");
        } else if (errorCode != -16) {
            return new KeyStoreException(errorCode, KeymasterDefs.getErrorMessage(errorCode));
        } else {
            return new KeyStoreException(errorCode, "Invalid user authentication validity duration");
        }
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, int uid, KeyStoreException e) {
        int errorCode = e.getErrorCode();
        if (errorCode == 2) {
            return new UserNotAuthenticatedException();
        }
        if (errorCode == 3) {
            return new KeyPermanentlyInvalidatedException();
        }
        if (errorCode != 15) {
            switch (errorCode) {
                case -26:
                    break;
                case -25:
                    return new KeyExpiredException();
                case -24:
                    return new KeyNotYetValidException();
                default:
                    return new InvalidKeyException("Keystore operation failed", e);
            }
        }
        KeyCharacteristics keyCharacteristics = new KeyCharacteristics();
        int getKeyCharacteristicsErrorCode = getKeyCharacteristics(keystoreKeyAlias, null, null, uid, keyCharacteristics);
        if (getKeyCharacteristicsErrorCode != 1) {
            return new InvalidKeyException("Failed to obtained key characteristics", getKeyStoreException(getKeyCharacteristicsErrorCode));
        }
        List<BigInteger> keySids = keyCharacteristics.getUnsignedLongs(KeymasterDefs.KM_TAG_USER_SECURE_ID);
        if (keySids.isEmpty()) {
            return new KeyPermanentlyInvalidatedException();
        }
        long rootSid = GateKeeper.getSecureUserId();
        if (rootSid != 0 && keySids.contains(KeymasterArguments.toUint64(rootSid))) {
            return new UserNotAuthenticatedException();
        }
        long fingerprintOnlySid = getFingerprintOnlySid();
        if (fingerprintOnlySid != 0 && keySids.contains(KeymasterArguments.toUint64(fingerprintOnlySid))) {
            return new UserNotAuthenticatedException();
        }
        long faceOnlySid = getFaceOnlySid();
        if (faceOnlySid == 0 || !keySids.contains(KeymasterArguments.toUint64(faceOnlySid))) {
            return new KeyPermanentlyInvalidatedException();
        }
        return new UserNotAuthenticatedException();
    }

    private long getFaceOnlySid() {
        if (!this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FACE)) {
            return 0;
        }
        FaceManager faceManager = (FaceManager) this.mContext.getSystemService(FaceManager.class);
        if (faceManager == null) {
            return 0;
        }
        return faceManager.getAuthenticatorId();
    }

    private long getFingerprintOnlySid() {
        if (!this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return 0;
        }
        FingerprintManager fingerprintManager = (FingerprintManager) this.mContext.getSystemService(FingerprintManager.class);
        if (fingerprintManager == null) {
            return 0;
        }
        return fingerprintManager.getAuthenticatorId();
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, int uid, int errorCode) {
        return getInvalidKeyException(keystoreKeyAlias, uid, getKeyStoreException(errorCode));
    }
}
