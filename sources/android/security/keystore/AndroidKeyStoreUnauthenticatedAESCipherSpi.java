package android.security.keystore;

import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.IvParameterSpec;

class AndroidKeyStoreUnauthenticatedAESCipherSpi extends AndroidKeyStoreCipherSpiBase {
    private static final int BLOCK_SIZE_BYTES = 16;
    private byte[] mIv;
    private boolean mIvHasBeenUsed;
    private final boolean mIvRequired;
    private final int mKeymasterBlockMode;
    private final int mKeymasterPadding;

    static abstract class CBC extends AndroidKeyStoreUnauthenticatedAESCipherSpi {

        public static class NoPadding extends CBC {
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public NoPadding() {
                super(1);
            }
        }

        public static class PKCS7Padding extends CBC {
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public PKCS7Padding() {
                super(64);
            }
        }

        protected CBC(int keymasterPadding) {
            super(2, keymasterPadding, true);
        }
    }

    static abstract class CTR extends AndroidKeyStoreUnauthenticatedAESCipherSpi {

        public static class NoPadding extends CTR {
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public NoPadding() {
                super(1);
            }
        }

        protected CTR(int keymasterPadding) {
            super(3, keymasterPadding, true);
        }
    }

    static abstract class ECB extends AndroidKeyStoreUnauthenticatedAESCipherSpi {

        public static class NoPadding extends ECB {
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public NoPadding() {
                super(1);
            }
        }

        public static class PKCS7Padding extends ECB {
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public PKCS7Padding() {
                super(64);
            }
        }

        protected ECB(int keymasterPadding) {
            super(1, keymasterPadding, false);
        }
    }

    AndroidKeyStoreUnauthenticatedAESCipherSpi(int keymasterBlockMode, int keymasterPadding, boolean ivRequired) {
        this.mKeymasterBlockMode = keymasterBlockMode;
        this.mKeymasterPadding = keymasterPadding;
        this.mIvRequired = ivRequired;
    }

    /* Access modifiers changed, original: protected|final */
    public final void resetAll() {
        this.mIv = null;
        this.mIvHasBeenUsed = false;
        super.resetAll();
    }

    /* Access modifiers changed, original: protected|final */
    public final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
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

    /* Access modifiers changed, original: protected|final */
    public final void initAlgorithmSpecificParameters() throws InvalidKeyException {
        if (this.mIvRequired && !isEncrypting()) {
            throw new InvalidKeyException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
        if (this.mIvRequired) {
            if (params == null) {
                if (!isEncrypting()) {
                    throw new InvalidAlgorithmParameterException("IvParameterSpec must be provided when decrypting");
                }
            } else if (params instanceof IvParameterSpec) {
                this.mIv = ((IvParameterSpec) params).getIV();
                if (this.mIv == null) {
                    throw new InvalidAlgorithmParameterException("Null IV in IvParameterSpec");
                }
            } else {
                throw new InvalidAlgorithmParameterException("Only IvParameterSpec supported");
            }
        } else if (params != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported parameters: ");
            stringBuilder.append(params);
            throw new InvalidAlgorithmParameterException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
        StringBuilder stringBuilder;
        if (this.mIvRequired) {
            if (params != null) {
                if (KeyProperties.KEY_ALGORITHM_AES.equalsIgnoreCase(params.getAlgorithm())) {
                    try {
                        this.mIv = ((IvParameterSpec) params.getParameterSpec(IvParameterSpec.class)).getIV();
                        if (this.mIv == null) {
                            throw new InvalidAlgorithmParameterException("Null IV in AlgorithmParameters");
                        }
                        return;
                    } catch (InvalidParameterSpecException e) {
                        if (isEncrypting()) {
                            this.mIv = null;
                            return;
                        }
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("IV required when decrypting, but not found in parameters: ");
                        stringBuilder2.append(params);
                        throw new InvalidAlgorithmParameterException(stringBuilder2.toString(), e);
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported AlgorithmParameters algorithm: ");
                stringBuilder.append(params.getAlgorithm());
                stringBuilder.append(". Supported: AES");
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            } else if (!isEncrypting()) {
                throw new InvalidAlgorithmParameterException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
            }
        } else if (params != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported parameters: ");
            stringBuilder.append(params);
            throw new InvalidAlgorithmParameterException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final int getAdditionalEntropyAmountForBegin() {
        if (this.mIvRequired && this.mIv == null && isEncrypting()) {
            return 16;
        }
        return 0;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getAdditionalEntropyAmountForFinish() {
        return 0;
    }

    /* Access modifiers changed, original: protected|final */
    public final void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
        if (isEncrypting() && this.mIvRequired && this.mIvHasBeenUsed) {
            throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
        }
        keymasterArgs.addEnum(268435458, 32);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockMode);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterPadding);
        if (this.mIvRequired) {
            byte[] bArr = this.mIv;
            if (bArr != null) {
                keymasterArgs.addBytes(KeymasterDefs.KM_TAG_NONCE, bArr);
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArgs) {
        this.mIvHasBeenUsed = true;
        byte[] returnedIv = keymasterArgs.getBytes(-1879047191, null);
        if (returnedIv != null && returnedIv.length == 0) {
            returnedIv = null;
        }
        if (this.mIvRequired) {
            byte[] bArr = this.mIv;
            if (bArr == null) {
                this.mIv = returnedIv;
            } else if (returnedIv != null && !Arrays.equals(returnedIv, bArr)) {
                throw new ProviderException("IV in use differs from provided IV");
            }
        } else if (returnedIv != null) {
            throw new ProviderException("IV in use despite IV not being used by this transformation");
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineGetBlockSize() {
        return 16;
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineGetOutputSize(int inputLen) {
        return inputLen + 48;
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineGetIV() {
        return ArrayUtils.cloneIfNotEmpty(this.mIv);
    }

    /* Access modifiers changed, original: protected|final */
    public final AlgorithmParameters engineGetParameters() {
        if (!this.mIvRequired) {
            return null;
        }
        byte[] bArr = this.mIv;
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance(KeyProperties.KEY_ALGORITHM_AES);
            params.init(new IvParameterSpec(this.mIv));
            return params;
        } catch (NoSuchAlgorithmException e) {
            throw new ProviderException("Failed to obtain AES AlgorithmParameters", e);
        } catch (InvalidParameterSpecException e2) {
            throw new ProviderException("Failed to initialize AES AlgorithmParameters with an IV", e2);
        }
    }
}
