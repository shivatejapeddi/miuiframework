package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreSignatureSpiBase extends SignatureSpi implements KeyStoreCryptoOperation {
    private Exception mCachedException;
    private AndroidKeyStoreKey mKey;
    private final KeyStore mKeyStore = KeyStore.getInstance();
    private KeyStoreCryptoOperationStreamer mMessageStreamer;
    private long mOperationHandle;
    private IBinder mOperationToken;
    private boolean mSigning;

    public abstract void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArguments);

    public abstract int getAdditionalEntropyAmountForSign();

    AndroidKeyStoreSignatureSpiBase() {
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineInitSign(PrivateKey key) throws InvalidKeyException {
        engineInitSign(key, null);
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineInitSign(PrivateKey privateKey, SecureRandom random) throws InvalidKeyException {
        resetAll();
        if (privateKey != null) {
            try {
                if (privateKey instanceof AndroidKeyStorePrivateKey) {
                    AndroidKeyStoreKey keystoreKey = (AndroidKeyStoreKey) privateKey;
                    this.mSigning = true;
                    initKey(keystoreKey);
                    this.appRandom = random;
                    ensureKeystoreOperationInitialized();
                    if (!true) {
                        resetAll();
                        return;
                    }
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported private key type: ");
                stringBuilder.append(privateKey);
                throw new InvalidKeyException(stringBuilder.toString());
            } catch (Throwable th) {
                if (!false) {
                    resetAll();
                }
            }
        } else {
            throw new InvalidKeyException("Unsupported key: null");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        resetAll();
        if (publicKey != null) {
            try {
                if (publicKey instanceof AndroidKeyStorePublicKey) {
                    AndroidKeyStorePublicKey keystoreKey = (AndroidKeyStorePublicKey) publicKey;
                    this.mSigning = false;
                    initKey(keystoreKey);
                    this.appRandom = null;
                    ensureKeystoreOperationInitialized();
                    if (!true) {
                        resetAll();
                        return;
                    }
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported public key type: ");
                stringBuilder.append(publicKey);
                throw new InvalidKeyException(stringBuilder.toString());
            } catch (Throwable th) {
                if (!false) {
                    resetAll();
                }
            }
        } else {
            throw new InvalidKeyException("Unsupported key: null");
        }
    }

    /* Access modifiers changed, original: protected */
    public void initKey(AndroidKeyStoreKey key) throws InvalidKeyException {
        this.mKey = key;
    }

    /* Access modifiers changed, original: protected */
    public void resetAll() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mOperationToken = null;
            this.mKeyStore.abort(operationToken);
        }
        this.mSigning = false;
        this.mKey = null;
        this.appRandom = null;
        this.mOperationToken = null;
        this.mOperationHandle = 0;
        this.mMessageStreamer = null;
        this.mCachedException = null;
    }

    /* Access modifiers changed, original: protected */
    public void resetWhilePreservingInitState() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mOperationToken = null;
            this.mKeyStore.abort(operationToken);
        }
        this.mOperationHandle = 0;
        this.mMessageStreamer = null;
        this.mCachedException = null;
    }

    private void ensureKeystoreOperationInitialized() throws InvalidKeyException {
        if (this.mMessageStreamer != null || this.mCachedException != null) {
            return;
        }
        if (this.mKey != null) {
            KeymasterArguments keymasterInputArgs = new KeymasterArguments();
            addAlgorithmSpecificParametersToBegin(keymasterInputArgs);
            OperationResult opResult = this.mKeyStore.begin(this.mKey.getAlias(), this.mSigning ? 2 : 3, true, keymasterInputArgs, null, this.mKey.getUid());
            if (opResult != null) {
                this.mOperationToken = opResult.token;
                this.mOperationHandle = opResult.operationHandle;
                InvalidKeyException e = KeyStoreCryptoOperationUtils.getInvalidKeyExceptionForInit(this.mKeyStore, this.mKey, opResult.resultCode);
                if (e != null) {
                    throw e;
                } else if (this.mOperationToken == null) {
                    throw new ProviderException("Keystore returned null operation token");
                } else if (this.mOperationHandle != 0) {
                    this.mMessageStreamer = createMainDataStreamer(this.mKeyStore, opResult.token);
                    return;
                } else {
                    throw new ProviderException("Keystore returned invalid operation handle");
                }
            }
            throw new KeyStoreConnectException();
        }
        throw new IllegalStateException("Not initialized");
    }

    /* Access modifiers changed, original: protected */
    public KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore keyStore, IBinder operationToken) {
        return new KeyStoreCryptoOperationChunkedStreamer(new MainDataStream(keyStore, operationToken));
    }

    public final long getOperationHandle() {
        return this.mOperationHandle;
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineUpdate(byte[] b, int off, int len) throws SignatureException {
        Exception exception = this.mCachedException;
        if (exception == null) {
            try {
                ensureKeystoreOperationInitialized();
                if (len != 0) {
                    try {
                        byte[] output = this.mMessageStreamer.update(b, off, len);
                        if (output.length != 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Update operation unexpectedly produced output: ");
                            stringBuilder.append(output.length);
                            stringBuilder.append(" bytes");
                            throw new ProviderException(stringBuilder.toString());
                        }
                        return;
                    } catch (KeyStoreException e) {
                        throw new SignatureException(e);
                    }
                }
                return;
            } catch (InvalidKeyException e2) {
                throw new SignatureException(e2);
            }
        }
        throw new SignatureException(exception);
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineUpdate(byte b) throws SignatureException {
        engineUpdate(new byte[]{b}, 0, 1);
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineUpdate(ByteBuffer input) {
        byte[] b;
        int off;
        int len = input.remaining();
        if (input.hasArray()) {
            b = input.array();
            off = input.arrayOffset() + input.position();
            input.position(input.limit());
        } else {
            b = new byte[len];
            off = 0;
            input.get(b);
        }
        try {
            engineUpdate(b, off, len);
        } catch (SignatureException e) {
            this.mCachedException = e;
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineSign(byte[] out, int outOffset, int outLen) throws SignatureException {
        return super.engineSign(out, outOffset, outLen);
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineSign() throws SignatureException {
        Exception exception = this.mCachedException;
        if (exception == null) {
            try {
                ensureKeystoreOperationInitialized();
                byte[] signature = this.mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.appRandom, getAdditionalEntropyAmountForSign()));
                resetWhilePreservingInitState();
                return signature;
            } catch (KeyStoreException | InvalidKeyException exception2) {
                throw new SignatureException(exception2);
            }
        }
        throw new SignatureException(exception2);
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean engineVerify(byte[] signature) throws SignatureException {
        Exception exception = this.mCachedException;
        if (exception == null) {
            try {
                ensureKeystoreOperationInitialized();
                KeyStoreException e;
                try {
                    byte[] output = this.mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, signature, null);
                    if (output.length == 0) {
                        e = true;
                        resetWhilePreservingInitState();
                        return e;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Signature verification unexpected produced output: ");
                    stringBuilder.append(output.length);
                    stringBuilder.append(" bytes");
                    throw new ProviderException(stringBuilder.toString());
                } catch (KeyStoreException e2) {
                    if (e2.getErrorCode() == -30) {
                        e2 = false;
                    } else {
                        throw new SignatureException(e2);
                    }
                }
            } catch (InvalidKeyException e3) {
                throw new SignatureException(e3);
            }
        }
        throw new SignatureException(exception);
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean engineVerify(byte[] sigBytes, int offset, int len) throws SignatureException {
        return engineVerify(ArrayUtils.subarray(sigBytes, offset, len));
    }

    /* Access modifiers changed, original: protected|final */
    @Deprecated
    public final Object engineGetParameter(String param) throws InvalidParameterException {
        throw new InvalidParameterException();
    }

    /* Access modifiers changed, original: protected|final */
    @Deprecated
    public final void engineSetParameter(String param, Object value) throws InvalidParameterException {
        throw new InvalidParameterException();
    }

    /* Access modifiers changed, original: protected|final */
    public final KeyStore getKeyStore() {
        return this.mKeyStore;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isSigning() {
        return this.mSigning;
    }
}
