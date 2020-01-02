package android.security.keystore;

import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties.Digest;
import android.security.keystore.KeyProperties.EncryptionPadding;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.PSource.PSpecified;

abstract class AndroidKeyStoreRSACipherSpi extends AndroidKeyStoreCipherSpiBase {
    private final int mKeymasterPadding;
    private int mKeymasterPaddingOverride;
    private int mModulusSizeBytes = -1;

    public static final class NoPadding extends AndroidKeyStoreRSACipherSpi {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public NoPadding() {
            super(1);
        }

        /* Access modifiers changed, original: protected */
        public boolean adjustConfigForEncryptingWithPrivateKey() {
            setKeymasterPurposeOverride(2);
            return true;
        }

        /* Access modifiers changed, original: protected */
        public void initAlgorithmSpecificParameters() throws InvalidKeyException {
        }

        /* Access modifiers changed, original: protected */
        public void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
            if (params != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected parameters: ");
                stringBuilder.append(params);
                stringBuilder.append(". No parameters supported");
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: protected */
        public void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
            if (params != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected parameters: ");
                stringBuilder.append(params);
                stringBuilder.append(". No parameters supported");
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: protected */
        public AlgorithmParameters engineGetParameters() {
            return null;
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForBegin() {
            return 0;
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForFinish() {
            return 0;
        }
    }

    static abstract class OAEPWithMGF1Padding extends AndroidKeyStoreRSACipherSpi {
        private static final String MGF_ALGORITGM_MGF1 = "MGF1";
        private int mDigestOutputSizeBytes;
        private int mKeymasterDigest = -1;

        OAEPWithMGF1Padding(int keymasterDigest) {
            super(2);
            this.mKeymasterDigest = keymasterDigest;
            this.mDigestOutputSizeBytes = (KeymasterUtils.getDigestOutputSizeBits(keymasterDigest) + 7) / 8;
        }

        /* Access modifiers changed, original: protected|final */
        public final void initAlgorithmSpecificParameters() throws InvalidKeyException {
        }

        /* Access modifiers changed, original: protected|final */
        public final void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
            String str = "Unsupported digest: ";
            if (params != null) {
                if (params instanceof OAEPParameterSpec) {
                    OAEPParameterSpec spec = (OAEPParameterSpec) params;
                    String mGFAlgorithm = spec.getMGFAlgorithm();
                    String str2 = MGF_ALGORITGM_MGF1;
                    String str3 = " supported";
                    String str4 = ". Only ";
                    if (str2.equalsIgnoreCase(mGFAlgorithm)) {
                        mGFAlgorithm = spec.getDigestAlgorithm();
                        StringBuilder stringBuilder;
                        try {
                            int keymasterDigest = Digest.toKeymaster(mGFAlgorithm);
                            if (keymasterDigest == 2 || keymasterDigest == 3 || keymasterDigest == 4 || keymasterDigest == 5 || keymasterDigest == 6) {
                                AlgorithmParameterSpec mgfParams = spec.getMGFParameters();
                                if (mgfParams == null) {
                                    throw new InvalidAlgorithmParameterException("MGF parameters must be provided");
                                } else if (mgfParams instanceof MGF1ParameterSpec) {
                                    String mgf1JcaDigest = ((MGF1ParameterSpec) mgfParams).getDigestAlgorithm();
                                    String str5 = KeyProperties.DIGEST_SHA1;
                                    StringBuilder stringBuilder2;
                                    if (str5.equalsIgnoreCase(mgf1JcaDigest)) {
                                        PSource pSource = spec.getPSource();
                                        str5 = ". Only pSpecifiedEmpty (PSource.PSpecified.DEFAULT) supported";
                                        String str6 = "Unsupported source of encoding input P: ";
                                        if (pSource instanceof PSpecified) {
                                            byte[] pSourceValue = ((PSpecified) pSource).getValue();
                                            if (pSourceValue == null || pSourceValue.length <= 0) {
                                                this.mKeymasterDigest = keymasterDigest;
                                                this.mDigestOutputSizeBytes = (KeymasterUtils.getDigestOutputSizeBits(keymasterDigest) + 7) / 8;
                                                return;
                                            }
                                            StringBuilder stringBuilder3 = new StringBuilder();
                                            stringBuilder3.append(str6);
                                            stringBuilder3.append(pSource);
                                            stringBuilder3.append(str5);
                                            throw new InvalidAlgorithmParameterException(stringBuilder3.toString());
                                        }
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append(str6);
                                        stringBuilder2.append(pSource);
                                        stringBuilder2.append(str5);
                                        throw new InvalidAlgorithmParameterException(stringBuilder2.toString());
                                    }
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Unsupported MGF1 digest: ");
                                    stringBuilder2.append(mgf1JcaDigest);
                                    stringBuilder2.append(str4);
                                    stringBuilder2.append(str5);
                                    stringBuilder2.append(str3);
                                    throw new InvalidAlgorithmParameterException(stringBuilder2.toString());
                                } else {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Unsupported MGF parameters: ");
                                    stringBuilder.append(mgfParams);
                                    stringBuilder.append(". Only MGF1ParameterSpec supported");
                                    throw new InvalidAlgorithmParameterException(stringBuilder.toString());
                                }
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append(mGFAlgorithm);
                            throw new InvalidAlgorithmParameterException(stringBuilder.toString());
                        } catch (IllegalArgumentException e) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append(mGFAlgorithm);
                            throw new InvalidAlgorithmParameterException(stringBuilder.toString(), e);
                        }
                    }
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("Unsupported MGF: ");
                    stringBuilder4.append(spec.getMGFAlgorithm());
                    stringBuilder4.append(str4);
                    stringBuilder4.append(str2);
                    stringBuilder4.append(str3);
                    throw new InvalidAlgorithmParameterException(stringBuilder4.toString());
                }
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append("Unsupported parameter spec: ");
                stringBuilder5.append(params);
                stringBuilder5.append(". Only OAEPParameterSpec supported");
                throw new InvalidAlgorithmParameterException(stringBuilder5.toString());
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
            if (params != null) {
                StringBuilder stringBuilder;
                try {
                    AlgorithmParameterSpec spec = (OAEPParameterSpec) params.getParameterSpec(OAEPParameterSpec.class);
                    if (spec != null) {
                        initAlgorithmSpecificParameters(spec);
                        return;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("OAEP parameters required, but not provided in parameters: ");
                    stringBuilder.append(params);
                    throw new InvalidAlgorithmParameterException(stringBuilder.toString());
                } catch (InvalidParameterSpecException e) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("OAEP parameters required, but not found in parameters: ");
                    stringBuilder.append(params);
                    throw new InvalidAlgorithmParameterException(stringBuilder.toString(), e);
                }
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final AlgorithmParameters engineGetParameters() {
            OAEPParameterSpec spec = new OAEPParameterSpec(Digest.fromKeymaster(this.mKeymasterDigest), MGF_ALGORITGM_MGF1, MGF1ParameterSpec.SHA1, PSpecified.DEFAULT);
            try {
                AlgorithmParameters params = AlgorithmParameters.getInstance("OAEP");
                params.init(spec);
                return params;
            } catch (NoSuchAlgorithmException e) {
                throw new ProviderException("Failed to obtain OAEP AlgorithmParameters", e);
            } catch (InvalidParameterSpecException e2) {
                throw new ProviderException("Failed to initialize OAEP AlgorithmParameters with an IV", e2);
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
            super.addAlgorithmSpecificParametersToBegin(keymasterArgs);
            keymasterArgs.addEnum(KeymasterDefs.KM_TAG_DIGEST, this.mKeymasterDigest);
        }

        /* Access modifiers changed, original: protected|final */
        public final void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArgs) {
            super.loadAlgorithmSpecificParametersFromBeginResult(keymasterArgs);
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForBegin() {
            return 0;
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForFinish() {
            return isEncrypting() ? this.mDigestOutputSizeBytes : 0;
        }
    }

    public static class OAEPWithSHA1AndMGF1Padding extends OAEPWithMGF1Padding {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public OAEPWithSHA1AndMGF1Padding() {
            super(2);
        }
    }

    public static class OAEPWithSHA224AndMGF1Padding extends OAEPWithMGF1Padding {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public OAEPWithSHA224AndMGF1Padding() {
            super(3);
        }
    }

    public static class OAEPWithSHA256AndMGF1Padding extends OAEPWithMGF1Padding {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public OAEPWithSHA256AndMGF1Padding() {
            super(4);
        }
    }

    public static class OAEPWithSHA384AndMGF1Padding extends OAEPWithMGF1Padding {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public OAEPWithSHA384AndMGF1Padding() {
            super(5);
        }
    }

    public static class OAEPWithSHA512AndMGF1Padding extends OAEPWithMGF1Padding {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public OAEPWithSHA512AndMGF1Padding() {
            super(6);
        }
    }

    public static final class PKCS1Padding extends AndroidKeyStoreRSACipherSpi {
        public /* bridge */ /* synthetic */ void finalize() throws Throwable {
            super.finalize();
        }

        public PKCS1Padding() {
            super(4);
        }

        /* Access modifiers changed, original: protected */
        public boolean adjustConfigForEncryptingWithPrivateKey() {
            setKeymasterPurposeOverride(2);
            setKeymasterPaddingOverride(5);
            return true;
        }

        /* Access modifiers changed, original: protected */
        public void initAlgorithmSpecificParameters() throws InvalidKeyException {
        }

        /* Access modifiers changed, original: protected */
        public void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
            if (params != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected parameters: ");
                stringBuilder.append(params);
                stringBuilder.append(". No parameters supported");
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: protected */
        public void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
            if (params != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected parameters: ");
                stringBuilder.append(params);
                stringBuilder.append(". No parameters supported");
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: protected */
        public AlgorithmParameters engineGetParameters() {
            return null;
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForBegin() {
            return 0;
        }

        /* Access modifiers changed, original: protected|final */
        public final int getAdditionalEntropyAmountForFinish() {
            return isEncrypting() ? getModulusSizeBytes() : 0;
        }
    }

    AndroidKeyStoreRSACipherSpi(int keymasterPadding) {
        this.mKeymasterPadding = keymasterPadding;
    }

    /* Access modifiers changed, original: protected|final */
    public final void initKey(int opmode, Key key) throws InvalidKeyException {
        if (key != null) {
            String algorithm = key.getAlgorithm();
            String str = KeyProperties.KEY_ALGORITHM_RSA;
            StringBuilder stringBuilder;
            if (str.equalsIgnoreCase(algorithm)) {
                AndroidKeyStoreKey keystoreKey;
                StringBuilder stringBuilder2;
                if (key instanceof AndroidKeyStorePrivateKey) {
                    keystoreKey = (AndroidKeyStoreKey) key;
                } else if (key instanceof AndroidKeyStorePublicKey) {
                    keystoreKey = (AndroidKeyStoreKey) key;
                } else {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unsupported key type: ");
                    stringBuilder3.append(key);
                    throw new InvalidKeyException(stringBuilder3.toString());
                }
                String str2 = " and padding ";
                StringBuilder stringBuilder4;
                if (keystoreKey instanceof PrivateKey) {
                    if (opmode != 1) {
                        if (opmode != 2) {
                            if (opmode != 3) {
                                if (opmode != 4) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("RSA private keys cannot be used with opmode: ");
                                    stringBuilder.append(opmode);
                                    throw new InvalidKeyException(stringBuilder.toString());
                                }
                            }
                        }
                    }
                    if (!adjustConfigForEncryptingWithPrivateKey()) {
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("RSA private keys cannot be used with ");
                        stringBuilder4.append(AndroidKeyStoreCipherSpiBase.opmodeToString(opmode));
                        stringBuilder4.append(str2);
                        stringBuilder4.append(EncryptionPadding.fromKeymaster(this.mKeymasterPadding));
                        stringBuilder4.append(". Only RSA public keys supported for this mode");
                        throw new InvalidKeyException(stringBuilder4.toString());
                    }
                } else if (opmode != 1) {
                    str = "RSA public keys cannot be used with ";
                    if (opmode != 2) {
                        if (opmode != 3) {
                            if (opmode != 4) {
                                stringBuilder4 = new StringBuilder();
                                stringBuilder4.append(str);
                                stringBuilder4.append(AndroidKeyStoreCipherSpiBase.opmodeToString(opmode));
                                throw new InvalidKeyException(stringBuilder4.toString());
                            }
                        }
                    }
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(AndroidKeyStoreCipherSpiBase.opmodeToString(opmode));
                    stringBuilder2.append(str2);
                    stringBuilder2.append(EncryptionPadding.fromKeymaster(this.mKeymasterPadding));
                    stringBuilder2.append(". Only RSA private keys supported for this opmode.");
                    throw new InvalidKeyException(stringBuilder2.toString());
                }
                KeyCharacteristics keyCharacteristics = new KeyCharacteristics();
                int errorCode = getKeyStore().getKeyCharacteristics(keystoreKey.getAlias(), null, null, keystoreKey.getUid(), keyCharacteristics);
                if (errorCode == 1) {
                    long keySizeBits = keyCharacteristics.getUnsignedInt(KeymasterDefs.KM_TAG_KEY_SIZE, -1);
                    if (keySizeBits == -1) {
                        throw new InvalidKeyException("Size of key not known");
                    } else if (keySizeBits <= 2147483647L) {
                        this.mModulusSizeBytes = (int) ((7 + keySizeBits) / 8);
                        setKey(keystoreKey);
                        return;
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Key too large: ");
                        stringBuilder2.append(keySizeBits);
                        stringBuilder2.append(" bits");
                        throw new InvalidKeyException(stringBuilder2.toString());
                    }
                }
                throw getKeyStore().getInvalidKeyException(keystoreKey.getAlias(), keystoreKey.getUid(), errorCode);
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported key algorithm: ");
            stringBuilder.append(key.getAlgorithm());
            stringBuilder.append(". Only ");
            stringBuilder.append(str);
            stringBuilder.append(" supported");
            throw new InvalidKeyException(stringBuilder.toString());
        }
        throw new InvalidKeyException("Unsupported key: null");
    }

    /* Access modifiers changed, original: protected */
    public boolean adjustConfigForEncryptingWithPrivateKey() {
        return false;
    }

    /* Access modifiers changed, original: protected|final */
    public final void resetAll() {
        this.mModulusSizeBytes = -1;
        this.mKeymasterPaddingOverride = -1;
        super.resetAll();
    }

    /* Access modifiers changed, original: protected|final */
    public final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
    }

    /* Access modifiers changed, original: protected */
    public void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
        keymasterArgs.addEnum(268435458, 1);
        int keymasterPadding = getKeymasterPaddingOverride();
        if (keymasterPadding == -1) {
            keymasterPadding = this.mKeymasterPadding;
        }
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_PADDING, keymasterPadding);
        int purposeOverride = getKeymasterPurposeOverride();
        if (purposeOverride == -1) {
            return;
        }
        if (purposeOverride == 2 || purposeOverride == 3) {
            keymasterArgs.addEnum(KeymasterDefs.KM_TAG_DIGEST, 0);
        }
    }

    /* Access modifiers changed, original: protected */
    public void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArgs) {
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineGetBlockSize() {
        return 0;
    }

    /* Access modifiers changed, original: protected|final */
    public final byte[] engineGetIV() {
        return null;
    }

    /* Access modifiers changed, original: protected|final */
    public final int engineGetOutputSize(int inputLen) {
        return getModulusSizeBytes();
    }

    /* Access modifiers changed, original: protected|final */
    public final int getModulusSizeBytes() {
        int i = this.mModulusSizeBytes;
        if (i != -1) {
            return i;
        }
        throw new IllegalStateException("Not initialized");
    }

    /* Access modifiers changed, original: protected|final */
    public final void setKeymasterPaddingOverride(int keymasterPadding) {
        this.mKeymasterPaddingOverride = keymasterPadding;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getKeymasterPaddingOverride() {
        return this.mKeymasterPaddingOverride;
    }
}
