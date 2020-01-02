package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.GCMParameterSpec;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreAuthenticatedAESCipherSpi extends AndroidKeyStoreCipherSpiBase {
    private static final int BLOCK_SIZE_BYTES = 16;
    private byte[] mIv;
    private boolean mIvHasBeenUsed;
    private final int mKeymasterBlockMode;
    private final int mKeymasterPadding;

    private static class AdditionalAuthenticationDataStream implements Stream {
        private final KeyStore mKeyStore;
        private final IBinder mOperationToken;

        private AdditionalAuthenticationDataStream(KeyStore keyStore, IBinder operationToken) {
            this.mKeyStore = keyStore;
            this.mOperationToken = operationToken;
        }

        public OperationResult update(byte[] input) {
            KeymasterArguments keymasterArgs = new KeymasterArguments();
            keymasterArgs.addBytes(KeymasterDefs.KM_TAG_ASSOCIATED_DATA, input);
            OperationResult result = this.mKeyStore.update(this.mOperationToken, keymasterArgs, null);
            if (result.resultCode == 1) {
                return new OperationResult(result.resultCode, result.token, result.operationHandle, input.length, result.output, result.outParams);
            }
            return result;
        }

        public OperationResult finish(byte[] signature, byte[] additionalEntropy) {
            if (additionalEntropy == null || additionalEntropy.length <= 0) {
                return new OperationResult(1, this.mOperationToken, 0, 0, EmptyArray.BYTE, new KeymasterArguments());
            }
            throw new ProviderException("AAD stream does not support additional entropy");
        }
    }

    private static class BufferAllOutputUntilDoFinalStreamer implements KeyStoreCryptoOperationStreamer {
        private ByteArrayOutputStream mBufferedOutput;
        private final KeyStoreCryptoOperationStreamer mDelegate;
        private long mProducedOutputSizeBytes;

        private BufferAllOutputUntilDoFinalStreamer(KeyStoreCryptoOperationStreamer delegate) {
            this.mBufferedOutput = new ByteArrayOutputStream();
            this.mDelegate = delegate;
        }

        public byte[] update(byte[] input, int inputOffset, int inputLength) throws KeyStoreException {
            byte[] output = this.mDelegate.update(input, inputOffset, inputLength);
            if (output != null) {
                try {
                    this.mBufferedOutput.write(output);
                } catch (IOException e) {
                    throw new ProviderException("Failed to buffer output", e);
                }
            }
            return EmptyArray.BYTE;
        }

        public byte[] doFinal(byte[] input, int inputOffset, int inputLength, byte[] signature, byte[] additionalEntropy) throws KeyStoreException {
            byte[] output = this.mDelegate.doFinal(input, inputOffset, inputLength, signature, additionalEntropy);
            if (output != null) {
                try {
                    this.mBufferedOutput.write(output);
                } catch (IOException e) {
                    throw new ProviderException("Failed to buffer output", e);
                }
            }
            byte[] result = this.mBufferedOutput.toByteArray();
            this.mBufferedOutput.reset();
            this.mProducedOutputSizeBytes += (long) result.length;
            return result;
        }

        public long getConsumedInputSizeBytes() {
            return this.mDelegate.getConsumedInputSizeBytes();
        }

        public long getProducedOutputSizeBytes() {
            return this.mProducedOutputSizeBytes;
        }
    }

    static abstract class GCM extends AndroidKeyStoreAuthenticatedAESCipherSpi {
        private static final int DEFAULT_TAG_LENGTH_BITS = 128;
        private static final int IV_LENGTH_BYTES = 12;
        private static final int MAX_SUPPORTED_TAG_LENGTH_BITS = 128;
        static final int MIN_SUPPORTED_TAG_LENGTH_BITS = 96;
        private int mTagLengthBits = 128;

        public static final class NoPadding extends GCM {
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public NoPadding() {
                super(1);
            }

            /* Access modifiers changed, original: protected|final */
            public final int engineGetOutputSize(int inputLen) {
                long result;
                int tagLengthBytes = (getTagLengthBits() + 7) / 8;
                if (isEncrypting()) {
                    result = ((getConsumedInputSizeBytes() - getProducedOutputSizeBytes()) + ((long) inputLen)) + ((long) tagLengthBytes);
                } else {
                    result = ((getConsumedInputSizeBytes() - getProducedOutputSizeBytes()) + ((long) inputLen)) - ((long) tagLengthBytes);
                }
                if (result < 0) {
                    return 0;
                }
                if (result > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) result;
            }
        }

        GCM(int keymasterPadding) {
            super(32, keymasterPadding);
        }

        /* Access modifiers changed, original: protected|final */
        public final void resetAll() {
            this.mTagLengthBits = 128;
            super.resetAll();
        }

        /* Access modifiers changed, original: protected|final */
        public final void resetWhilePreservingInitState() {
            super.resetWhilePreservingInitState();
        }

        /* Access modifiers changed, original: protected|final */
        public final void initAlgorithmSpecificParameters() throws InvalidKeyException {
            if (!isEncrypting()) {
                throw new InvalidKeyException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
            if (params == null) {
                if (!isEncrypting()) {
                    throw new InvalidAlgorithmParameterException("GCMParameterSpec must be provided when decrypting");
                }
            } else if (params instanceof GCMParameterSpec) {
                GCMParameterSpec spec = (GCMParameterSpec) params;
                byte[] iv = spec.getIV();
                StringBuilder stringBuilder;
                if (iv == null) {
                    throw new InvalidAlgorithmParameterException("Null IV in GCMParameterSpec");
                } else if (iv.length == 12) {
                    int tagLengthBits = spec.getTLen();
                    if (tagLengthBits < 96 || tagLengthBits > 128 || tagLengthBits % 8 != 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unsupported tag length: ");
                        stringBuilder.append(tagLengthBits);
                        stringBuilder.append(" bits. Supported lengths: 96, 104, 112, 120, 128");
                        throw new InvalidAlgorithmParameterException(stringBuilder.toString());
                    }
                    setIv(iv);
                    this.mTagLengthBits = tagLengthBits;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unsupported IV length: ");
                    stringBuilder.append(iv.length);
                    stringBuilder.append(" bytes. Only ");
                    stringBuilder.append(12);
                    stringBuilder.append(" bytes long IV supported");
                    throw new InvalidAlgorithmParameterException(stringBuilder.toString());
                }
            } else {
                throw new InvalidAlgorithmParameterException("Only GCMParameterSpec supported");
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
            if (params != null) {
                if (KeyProperties.BLOCK_MODE_GCM.equalsIgnoreCase(params.getAlgorithm())) {
                    try {
                        initAlgorithmSpecificParameters((GCMParameterSpec) params.getParameterSpec(GCMParameterSpec.class));
                        return;
                    } catch (InvalidParameterSpecException e) {
                        if (isEncrypting()) {
                            setIv(null);
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("IV and tag length required when decrypting, but not found in parameters: ");
                        stringBuilder.append(params);
                        throw new InvalidAlgorithmParameterException(stringBuilder.toString(), e);
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unsupported AlgorithmParameters algorithm: ");
                stringBuilder2.append(params.getAlgorithm());
                stringBuilder2.append(". Supported: GCM");
                throw new InvalidAlgorithmParameterException(stringBuilder2.toString());
            } else if (!isEncrypting()) {
                throw new InvalidAlgorithmParameterException("IV required when decrypting. Use GCMParameterSpec or GCM AlgorithmParameters to provide it.");
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final AlgorithmParameters engineGetParameters() {
            byte[] iv = getIv();
            if (iv == null || iv.length <= 0) {
                return null;
            }
            try {
                AlgorithmParameters params = AlgorithmParameters.getInstance(KeyProperties.BLOCK_MODE_GCM);
                params.init(new GCMParameterSpec(this.mTagLengthBits, iv));
                return params;
            } catch (NoSuchAlgorithmException e) {
                throw new ProviderException("Failed to obtain GCM AlgorithmParameters", e);
            } catch (InvalidParameterSpecException e2) {
                throw new ProviderException("Failed to initialize GCM AlgorithmParameters", e2);
            }
        }

        /* Access modifiers changed, original: protected */
        public KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore keyStore, IBinder operationToken) {
            KeyStoreCryptoOperationStreamer streamer = new KeyStoreCryptoOperationChunkedStreamer(new MainDataStream(keyStore, operationToken));
            if (isEncrypting()) {
                return streamer;
            }
            return new BufferAllOutputUntilDoFinalStreamer(streamer);
        }

        /* Access modifiers changed, original: protected|final */
        public final KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(KeyStore keyStore, IBinder operationToken) {
            return new KeyStoreCryptoOperationChunkedStreamer(new AdditionalAuthenticationDataStream(keyStore, operationToken));
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForBegin() {
            if (getIv() == null && isEncrypting()) {
                return 12;
            }
            return 0;
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForFinish() {
            return 0;
        }

        /* Access modifiers changed, original: protected|final */
        public final void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
            super.addAlgorithmSpecificParametersToBegin(keymasterArgs);
            keymasterArgs.addUnsignedInt(KeymasterDefs.KM_TAG_MAC_LENGTH, (long) this.mTagLengthBits);
        }

        /* Access modifiers changed, original: protected|final */
        public final int getTagLengthBits() {
            return this.mTagLengthBits;
        }
    }

    AndroidKeyStoreAuthenticatedAESCipherSpi(int keymasterBlockMode, int keymasterPadding) {
        this.mKeymasterBlockMode = keymasterBlockMode;
        this.mKeymasterPadding = keymasterPadding;
    }

    /* Access modifiers changed, original: protected */
    public void resetAll() {
        this.mIv = null;
        this.mIvHasBeenUsed = false;
        super.resetAll();
    }

    /* Access modifiers changed, original: protected|final */
    public final void initKey(int opmode, Key key) throws InvalidKeyException {
        if (key instanceof AndroidKeyStoreSecretKey) {
            String algorithm = key.getAlgorithm();
            String str = KeyProperties.KEY_ALGORITHM_AES;
            if (str.equalsIgnoreCase(algorithm)) {
                setKey((AndroidKeyStoreSecretKey) key);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported key algorithm: ");
            stringBuilder.append(key.getAlgorithm());
            stringBuilder.append(". Only ");
            stringBuilder.append(str);
            stringBuilder.append(" supported");
            throw new InvalidKeyException(stringBuilder.toString());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Unsupported key: ");
        stringBuilder2.append(key != null ? key.getClass().getName() : "null");
        throw new InvalidKeyException(stringBuilder2.toString());
    }

    /* Access modifiers changed, original: protected */
    public void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
        if (isEncrypting() && this.mIvHasBeenUsed) {
            throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
        }
        keymasterArgs.addEnum(268435458, 32);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockMode);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterPadding);
        byte[] bArr = this.mIv;
        if (bArr != null) {
            keymasterArgs.addBytes(KeymasterDefs.KM_TAG_NONCE, bArr);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArgs) {
        this.mIvHasBeenUsed = true;
        byte[] returnedIv = keymasterArgs.getBytes(-1879047191, null);
        if (returnedIv != null && returnedIv.length == 0) {
            returnedIv = null;
        }
        byte[] bArr = this.mIv;
        if (bArr == null) {
            this.mIv = returnedIv;
        } else if (returnedIv != null && !Arrays.equals(returnedIv, bArr)) {
            throw new ProviderException("IV in use differs from provided IV");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineGetBlockSize() {
        return 16;
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineGetIV() {
        return ArrayUtils.cloneIfNotEmpty(this.mIv);
    }

    /* Access modifiers changed, original: protected */
    public void setIv(byte[] iv) {
        this.mIv = iv;
    }

    /* Access modifiers changed, original: protected */
    public byte[] getIv() {
        return this.mIv;
    }
}
