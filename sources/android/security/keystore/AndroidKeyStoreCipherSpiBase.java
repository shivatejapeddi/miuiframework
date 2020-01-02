package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreCipherSpiBase extends CipherSpi implements KeyStoreCryptoOperation {
    private KeyStoreCryptoOperationStreamer mAdditionalAuthenticationDataStreamer;
    private boolean mAdditionalAuthenticationDataStreamerClosed;
    private Exception mCachedException;
    private boolean mEncrypting;
    private AndroidKeyStoreKey mKey;
    private final KeyStore mKeyStore = KeyStore.getInstance();
    private int mKeymasterPurposeOverride = -1;
    private KeyStoreCryptoOperationStreamer mMainDataStreamer;
    private long mOperationHandle;
    private IBinder mOperationToken;
    private SecureRandom mRng;

    public abstract void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArguments);

    public abstract AlgorithmParameters engineGetParameters();

    public abstract int getAdditionalEntropyAmountForBegin();

    public abstract int getAdditionalEntropyAmountForFinish();

    public abstract void initAlgorithmSpecificParameters() throws InvalidKeyException;

    public abstract void initAlgorithmSpecificParameters(AlgorithmParameters algorithmParameters) throws InvalidAlgorithmParameterException;

    public abstract void initAlgorithmSpecificParameters(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException;

    public abstract void initKey(int i, Key key) throws InvalidKeyException;

    public abstract void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArguments);

    AndroidKeyStoreCipherSpiBase() {
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        resetAll();
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters();
            ensureKeystoreOperationInitialized();
            if (!true) {
                resetAll();
            }
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidKeyException(e);
        } catch (Throwable th) {
            if (!false) {
                resetAll();
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters(params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters(params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0032  */
    private void init(int r4, java.security.Key r5, java.security.SecureRandom r6) throws java.security.InvalidKeyException {
        /*
        r3 = this;
        r0 = 1;
        if (r4 == r0) goto L_0x0028;
    L_0x0003:
        r1 = 2;
        if (r4 == r1) goto L_0x0024;
    L_0x0006:
        r1 = 3;
        if (r4 == r1) goto L_0x0028;
    L_0x0009:
        r0 = 4;
        if (r4 != r0) goto L_0x000d;
    L_0x000c:
        goto L_0x0024;
    L_0x000d:
        r0 = new java.security.InvalidParameterException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unsupported opmode: ";
        r1.append(r2);
        r1.append(r4);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0024:
        r0 = 0;
        r3.mEncrypting = r0;
        goto L_0x002b;
    L_0x0028:
        r3.mEncrypting = r0;
    L_0x002b:
        r3.initKey(r4, r5);
        r0 = r3.mKey;
        if (r0 == 0) goto L_0x0035;
    L_0x0032:
        r3.mRng = r6;
        return;
    L_0x0035:
        r0 = new java.security.ProviderException;
        r1 = "initKey did not initialize the key";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.AndroidKeyStoreCipherSpiBase.init(int, java.security.Key, java.security.SecureRandom):void");
    }

    /* Access modifiers changed, original: protected */
    public void resetAll() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mKeyStore.abort(operationToken);
        }
        this.mEncrypting = false;
        this.mKeymasterPurposeOverride = -1;
        this.mKey = null;
        this.mRng = null;
        this.mOperationToken = null;
        this.mOperationHandle = 0;
        this.mMainDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamerClosed = false;
        this.mCachedException = null;
    }

    /* Access modifiers changed, original: protected */
    public void resetWhilePreservingInitState() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mKeyStore.abort(operationToken);
        }
        this.mOperationToken = null;
        this.mOperationHandle = 0;
        this.mMainDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamerClosed = false;
        this.mCachedException = null;
    }

    private void ensureKeystoreOperationInitialized() throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (this.mMainDataStreamer != null || this.mCachedException != null) {
            return;
        }
        if (this.mKey != null) {
            int purpose;
            KeymasterArguments keymasterInputArgs = new KeymasterArguments();
            addAlgorithmSpecificParametersToBegin(keymasterInputArgs);
            byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, getAdditionalEntropyAmountForBegin());
            if (this.mKeymasterPurposeOverride != -1) {
                purpose = this.mKeymasterPurposeOverride;
            } else {
                purpose = !this.mEncrypting;
            }
            OperationResult opResult = this.mKeyStore.begin(this.mKey.getAlias(), purpose, true, keymasterInputArgs, additionalEntropy, this.mKey.getUid());
            if (opResult != null) {
                this.mOperationToken = opResult.token;
                this.mOperationHandle = opResult.operationHandle;
                GeneralSecurityException e = KeyStoreCryptoOperationUtils.getExceptionForCipherInit(this.mKeyStore, this.mKey, opResult.resultCode);
                if (e != null) {
                    if (e instanceof InvalidKeyException) {
                        throw ((InvalidKeyException) e);
                    } else if (e instanceof InvalidAlgorithmParameterException) {
                        throw ((InvalidAlgorithmParameterException) e);
                    } else {
                        throw new ProviderException("Unexpected exception type", e);
                    }
                } else if (this.mOperationToken == null) {
                    throw new ProviderException("Keystore returned null operation token");
                } else if (this.mOperationHandle != 0) {
                    loadAlgorithmSpecificParametersFromBeginResult(opResult.outParams);
                    this.mMainDataStreamer = createMainDataStreamer(this.mKeyStore, opResult.token);
                    this.mAdditionalAuthenticationDataStreamer = createAdditionalAuthenticationDataStreamer(this.mKeyStore, opResult.token);
                    this.mAdditionalAuthenticationDataStreamerClosed = false;
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

    /* Access modifiers changed, original: protected */
    public KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(KeyStore keyStore, IBinder operationToken) {
        return null;
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        if (this.mCachedException != null) {
            return null;
        }
        try {
            ensureKeystoreOperationInitialized();
            if (inputLen == 0) {
                return null;
            }
            try {
                flushAAD();
                byte[] output = this.mMainDataStreamer.update(input, inputOffset, inputLen);
                if (output.length == 0) {
                    return null;
                }
                return output;
            } catch (KeyStoreException e) {
                this.mCachedException = e;
                return null;
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
            this.mCachedException = e2;
            return null;
        }
    }

    private void flushAAD() throws KeyStoreException {
        byte[] output = this.mAdditionalAuthenticationDataStreamer;
        if (output != null && !this.mAdditionalAuthenticationDataStreamerClosed) {
            try {
                output = output.doFinal(EmptyArray.BYTE, 0, 0, null, null);
                if (output != null && output.length > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("AAD update unexpectedly returned data: ");
                    stringBuilder.append(output.length);
                    stringBuilder.append(" bytes");
                    throw new ProviderException(stringBuilder.toString());
                }
            } finally {
                this.mAdditionalAuthenticationDataStreamerClosed = true;
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        byte[] outputCopy = engineUpdate(input, inputOffset, inputLen);
        if (outputCopy == null) {
            return 0;
        }
        int outputAvailable = output.length - outputOffset;
        if (outputCopy.length <= outputAvailable) {
            System.arraycopy(outputCopy, 0, output, outputOffset, outputCopy.length);
            return outputCopy.length;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Output buffer too short. Produced: ");
        stringBuilder.append(outputCopy.length);
        stringBuilder.append(", available: ");
        stringBuilder.append(outputAvailable);
        throw new ShortBufferException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineUpdate(ByteBuffer input, ByteBuffer output) throws ShortBufferException {
        if (input == null) {
            throw new NullPointerException("input == null");
        } else if (output != null) {
            byte[] outputArray;
            int inputSize = input.remaining();
            int outputSize = 0;
            if (input.hasArray()) {
                outputArray = engineUpdate(input.array(), input.arrayOffset() + input.position(), inputSize);
                input.position(input.position() + inputSize);
            } else {
                outputArray = new byte[inputSize];
                input.get(outputArray);
                outputArray = engineUpdate(outputArray, 0, inputSize);
            }
            if (outputArray != null) {
                outputSize = outputArray.length;
            }
            if (outputSize > 0) {
                int outputBufferAvailable = output.remaining();
                try {
                    output.put(outputArray);
                } catch (BufferOverflowException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Output buffer too small. Produced: ");
                    stringBuilder.append(outputSize);
                    stringBuilder.append(", available: ");
                    stringBuilder.append(outputBufferAvailable);
                    throw new ShortBufferException(stringBuilder.toString());
                }
            }
            return outputSize;
        } else {
            throw new NullPointerException("output == null");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineUpdateAAD(byte[] input, int inputOffset, int inputLen) {
        if (this.mCachedException == null) {
            try {
                ensureKeystoreOperationInitialized();
                if (this.mAdditionalAuthenticationDataStreamerClosed) {
                    throw new IllegalStateException("AAD can only be provided before Cipher.update is invoked");
                }
                byte[] output = this.mAdditionalAuthenticationDataStreamer;
                if (output != null) {
                    try {
                        output = output.update(input, inputOffset, inputLen);
                        if (output != null && output.length > 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("AAD update unexpectedly produced output: ");
                            stringBuilder.append(output.length);
                            stringBuilder.append(" bytes");
                            throw new ProviderException(stringBuilder.toString());
                        }
                        return;
                    } catch (KeyStoreException e) {
                        this.mCachedException = e;
                        return;
                    }
                }
                throw new IllegalStateException("This cipher does not support AAD");
            } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
                this.mCachedException = e2;
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineUpdateAAD(ByteBuffer src) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        } else if (src.hasRemaining()) {
            byte[] input;
            int inputOffset;
            int inputLen;
            if (src.hasArray()) {
                input = src.array();
                inputOffset = src.arrayOffset() + src.position();
                inputLen = src.remaining();
                src.position(src.limit());
            } else {
                input = new byte[src.remaining()];
                inputOffset = 0;
                inputLen = input.length;
                src.get(input);
            }
            engineUpdateAAD(input, inputOffset, inputLen);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        if (this.mCachedException == null) {
            try {
                ensureKeystoreOperationInitialized();
                try {
                    flushAAD();
                    byte[] output = this.mMainDataStreamer.doFinal(input, inputOffset, inputLen, null, KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, getAdditionalEntropyAmountForFinish()));
                    resetWhilePreservingInitState();
                    return output;
                } catch (KeyStoreException e) {
                    int errorCode = e.getErrorCode();
                    if (errorCode == -38) {
                        throw ((BadPaddingException) new BadPaddingException().initCause(e));
                    } else if (errorCode == -30) {
                        throw ((AEADBadTagException) new AEADBadTagException().initCause(e));
                    } else if (errorCode != -21) {
                        throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e));
                    } else {
                        throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e));
                    }
                }
            } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
                throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e2));
            }
        }
        throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(this.mCachedException));
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] outputCopy = engineDoFinal(input, inputOffset, inputLen);
        if (outputCopy == null) {
            return 0;
        }
        int outputAvailable = output.length - outputOffset;
        if (outputCopy.length <= outputAvailable) {
            System.arraycopy(outputCopy, 0, output, outputOffset, outputCopy.length);
            return outputCopy.length;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Output buffer too short. Produced: ");
        stringBuilder.append(outputCopy.length);
        stringBuilder.append(", available: ");
        stringBuilder.append(outputAvailable);
        throw new ShortBufferException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineDoFinal(ByteBuffer input, ByteBuffer output) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        if (input == null) {
            throw new NullPointerException("input == null");
        } else if (output != null) {
            byte[] outputArray;
            int inputSize = input.remaining();
            int outputSize = 0;
            if (input.hasArray()) {
                outputArray = engineDoFinal(input.array(), input.arrayOffset() + input.position(), inputSize);
                input.position(input.position() + inputSize);
            } else {
                outputArray = new byte[inputSize];
                input.get(outputArray);
                outputArray = engineDoFinal(outputArray, 0, inputSize);
            }
            if (outputArray != null) {
                outputSize = outputArray.length;
            }
            if (outputSize > 0) {
                int outputBufferAvailable = output.remaining();
                try {
                    output.put(outputArray);
                } catch (BufferOverflowException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Output buffer too small. Produced: ");
                    stringBuilder.append(outputSize);
                    stringBuilder.append(", available: ");
                    stringBuilder.append(outputBufferAvailable);
                    throw new ShortBufferException(stringBuilder.toString());
                }
            }
            return outputSize;
        } else {
            throw new NullPointerException("output == null");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        if (this.mKey == null) {
            throw new IllegalStateException("Not initilized");
        } else if (!isEncrypting()) {
            throw new IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
        } else if (key != null) {
            byte[] encoded = null;
            String str = "Failed to wrap key because it does not export its key material";
            if (key instanceof SecretKey) {
                if ("RAW".equalsIgnoreCase(key.getFormat())) {
                    encoded = key.getEncoded();
                }
                if (encoded == null) {
                    try {
                        encoded = ((SecretKeySpec) SecretKeyFactory.getInstance(key.getAlgorithm()).getKeySpec((SecretKey) key, SecretKeySpec.class)).getEncoded();
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        throw new InvalidKeyException(str, e);
                    }
                }
            } else if (key instanceof PrivateKey) {
                if ("PKCS8".equalsIgnoreCase(key.getFormat())) {
                    encoded = key.getEncoded();
                }
                if (encoded == null) {
                    try {
                        encoded = ((PKCS8EncodedKeySpec) KeyFactory.getInstance(key.getAlgorithm()).getKeySpec(key, PKCS8EncodedKeySpec.class)).getEncoded();
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
                        throw new InvalidKeyException(str, e2);
                    }
                }
            } else if (key instanceof PublicKey) {
                if ("X.509".equalsIgnoreCase(key.getFormat())) {
                    encoded = key.getEncoded();
                }
                if (encoded == null) {
                    try {
                        encoded = ((X509EncodedKeySpec) KeyFactory.getInstance(key.getAlgorithm()).getKeySpec(key, X509EncodedKeySpec.class)).getEncoded();
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e22) {
                        throw new InvalidKeyException(str, e22);
                    }
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported key type: ");
                stringBuilder.append(key.getClass().getName());
                throw new InvalidKeyException(stringBuilder.toString());
            }
            if (encoded != null) {
                try {
                    return engineDoFinal(encoded, 0, encoded.length);
                } catch (BadPaddingException e3) {
                    throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e3));
                }
            }
            throw new InvalidKeyException(str);
        } else {
            throw new NullPointerException("key == null");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final Key engineUnwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException, NoSuchAlgorithmException {
        if (this.mKey == null) {
            throw new IllegalStateException("Not initilized");
        } else if (isEncrypting()) {
            throw new IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
        } else if (wrappedKey != null) {
            try {
                byte[] encoded = engineDoFinal(wrappedKey, null, wrappedKey.length);
                if (wrappedKeyType == 1) {
                    try {
                        return KeyFactory.getInstance(wrappedKeyAlgorithm).generatePublic(new X509EncodedKeySpec(encoded));
                    } catch (InvalidKeySpecException e) {
                        throw new InvalidKeyException("Failed to create public key from its X.509 encoded form", e);
                    }
                } else if (wrappedKeyType == 2) {
                    try {
                        return KeyFactory.getInstance(wrappedKeyAlgorithm).generatePrivate(new PKCS8EncodedKeySpec(encoded));
                    } catch (InvalidKeySpecException e2) {
                        throw new InvalidKeyException("Failed to create private key from its PKCS#8 encoded form", e2);
                    }
                } else if (wrappedKeyType == 3) {
                    return new SecretKeySpec(encoded, wrappedKeyAlgorithm);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unsupported wrappedKeyType: ");
                    stringBuilder.append(wrappedKeyType);
                    throw new InvalidParameterException(stringBuilder.toString());
                }
            } catch (BadPaddingException | IllegalBlockSizeException e3) {
                throw new InvalidKeyException("Failed to unwrap key", e3);
            }
        } else {
            throw new NullPointerException("wrappedKey == null");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineSetMode(String mode) throws NoSuchAlgorithmException {
        throw new UnsupportedOperationException();
    }

    /* Access modifiers changed, original: protected|final */
    public final void engineSetPadding(String arg0) throws NoSuchPaddingException {
        throw new UnsupportedOperationException();
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineGetKeySize(Key key) throws InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    public void finalize() throws Throwable {
        try {
            IBinder operationToken = this.mOperationToken;
            if (operationToken != null) {
                this.mKeyStore.abort(operationToken);
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public final long getOperationHandle() {
        return this.mOperationHandle;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setKey(AndroidKeyStoreKey key) {
        this.mKey = key;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setKeymasterPurposeOverride(int keymasterPurpose) {
        this.mKeymasterPurposeOverride = keymasterPurpose;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getKeymasterPurposeOverride() {
        return this.mKeymasterPurposeOverride;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEncrypting() {
        return this.mEncrypting;
    }

    /* Access modifiers changed, original: protected|final */
    public final KeyStore getKeyStore() {
        return this.mKeyStore;
    }

    /* Access modifiers changed, original: protected|final */
    public final long getConsumedInputSizeBytes() {
        KeyStoreCryptoOperationStreamer keyStoreCryptoOperationStreamer = this.mMainDataStreamer;
        if (keyStoreCryptoOperationStreamer != null) {
            return keyStoreCryptoOperationStreamer.getConsumedInputSizeBytes();
        }
        throw new IllegalStateException("Not initialized");
    }

    /* Access modifiers changed, original: protected|final */
    public final long getProducedOutputSizeBytes() {
        KeyStoreCryptoOperationStreamer keyStoreCryptoOperationStreamer = this.mMainDataStreamer;
        if (keyStoreCryptoOperationStreamer != null) {
            return keyStoreCryptoOperationStreamer.getProducedOutputSizeBytes();
        }
        throw new IllegalStateException("Not initialized");
    }

    static String opmodeToString(int opmode) {
        if (opmode == 1) {
            return "ENCRYPT_MODE";
        }
        if (opmode == 2) {
            return "DECRYPT_MODE";
        }
        if (opmode == 3) {
            return "WRAP_MODE";
        }
        if (opmode != 4) {
            return String.valueOf(opmode);
        }
        return "UNWRAP_MODE";
    }
}
