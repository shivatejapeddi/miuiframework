package android.util.apk;

import android.os.Process;
import android.util.ArrayMap;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ApkSignatureSchemeV2Verifier {
    private static final int APK_SIGNATURE_SCHEME_V2_BLOCK_ID = 1896449818;
    public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 2;
    private static final int STRIPPING_PROTECTION_ATTR_ID = -1091571699;

    public static class VerifiedSigner {
        public final X509Certificate[][] certs;
        public final byte[] verityRootHash;

        public VerifiedSigner(X509Certificate[][] certs, byte[] verityRootHash) {
            this.certs = certs;
            this.verityRootHash = verityRootHash;
        }
    }

    /* JADX WARNING: Missing block: B:14:?, code skipped:
            $closeResource(r1, r0);
     */
    public static boolean hasSignature(java.lang.String r3) throws java.io.IOException {
        /*
        r0 = new java.io.RandomAccessFile;	 Catch:{ SignatureNotFoundException -> 0x0018 }
        r1 = "r";
        r0.<init>(r3, r1);	 Catch:{ SignatureNotFoundException -> 0x0018 }
        r1 = 0;
        findSignature(r0);	 Catch:{ all -> 0x0011 }
        r2 = 1;
        $closeResource(r1, r0);	 Catch:{ SignatureNotFoundException -> 0x0018 }
        return r2;
    L_0x0011:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0013 }
    L_0x0013:
        r2 = move-exception;
        $closeResource(r1, r0);	 Catch:{ SignatureNotFoundException -> 0x0018 }
        throw r2;	 Catch:{ SignatureNotFoundException -> 0x0018 }
    L_0x0018:
        r0 = move-exception;
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV2Verifier.hasSignature(java.lang.String):boolean");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    public static X509Certificate[][] verify(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, true).certs;
    }

    public static X509Certificate[][] unsafeGetCertsWithoutVerification(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, (boolean) null).certs;
    }

    /* JADX WARNING: Missing block: B:9:0x0014, code skipped:
            $closeResource(r1, r0);
     */
    private static android.util.apk.ApkSignatureSchemeV2Verifier.VerifiedSigner verify(java.lang.String r3, boolean r4) throws android.util.apk.SignatureNotFoundException, java.lang.SecurityException, java.io.IOException {
        /*
        r0 = new java.io.RandomAccessFile;
        r1 = "r";
        r0.<init>(r3, r1);
        r1 = verify(r0, r4);	 Catch:{ all -> 0x0011 }
        r2 = 0;
        $closeResource(r2, r0);
        return r1;
    L_0x0011:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0013 }
    L_0x0013:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV2Verifier.verify(java.lang.String, boolean):android.util.apk.ApkSignatureSchemeV2Verifier$VerifiedSigner");
    }

    private static VerifiedSigner verify(RandomAccessFile apk, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apk, findSignature(apk), verifyIntegrity);
    }

    private static SignatureInfo findSignature(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        return ApkSigningBlockUtils.findSignature(apk, APK_SIGNATURE_SCHEME_V2_BLOCK_ID);
    }

    private static VerifiedSigner verify(RandomAccessFile apk, SignatureInfo signatureInfo, boolean doVerifyIntegrity) throws SecurityException, IOException {
        int signerCount = 0;
        Map<Integer, byte[]> contentDigests = new ArrayMap();
        List<X509Certificate[]> signerCerts = new ArrayList();
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try {
                ByteBuffer signers = ApkSigningBlockUtils.getLengthPrefixedSlice(signatureInfo.signatureBlock);
                while (signers.hasRemaining()) {
                    signerCount++;
                    try {
                        signerCerts.add(verifySigner(ApkSigningBlockUtils.getLengthPrefixedSlice(signers), contentDigests, certFactory));
                    } catch (IOException | SecurityException | BufferUnderflowException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to parse/verify signer #");
                        stringBuilder.append(signerCount);
                        stringBuilder.append(" block");
                        throw new SecurityException(stringBuilder.toString(), e);
                    }
                }
                if (signerCount < 1) {
                    throw new SecurityException("No signers found");
                } else if (contentDigests.isEmpty()) {
                    throw new SecurityException("No content digests found");
                } else {
                    if (doVerifyIntegrity) {
                        ApkSigningBlockUtils.verifyIntegrity(contentDigests, apk, signatureInfo);
                    }
                    byte[] verityRootHash = null;
                    if (contentDigests.containsKey(Integer.valueOf(3))) {
                        verityRootHash = ApkSigningBlockUtils.parseVerityDigestAndVerifySourceLength((byte[]) contentDigests.get(Integer.valueOf(3)), apk.length(), signatureInfo);
                    }
                    return new VerifiedSigner((X509Certificate[][]) signerCerts.toArray(new X509Certificate[signerCerts.size()][]), verityRootHash);
                }
            } catch (IOException e2) {
                throw new SecurityException("Failed to read list of signers", e2);
            }
        } catch (CertificateException e3) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e3);
        }
    }

    private static X509Certificate[] verifySigner(ByteBuffer signerBlock, Map<Integer, byte[]> contentDigests, CertificateFactory certFactory) throws SecurityException, IOException {
        int sigAlgorithm;
        Exception e;
        GeneralSecurityException e2;
        int i;
        int i2;
        byte[] bArr;
        StringBuilder stringBuilder;
        ByteBuffer signedData = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        ByteBuffer signatures = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        byte[] publicKeyBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signerBlock);
        int bestSigAlgorithm = -1;
        List<Integer> signaturesSigAlgorithms = new ArrayList();
        byte[] bestSigAlgorithmSignatureBytes = null;
        int signatureCount = 0;
        while (signatures.hasRemaining()) {
            signatureCount++;
            try {
                ByteBuffer signature = ApkSigningBlockUtils.getLengthPrefixedSlice(signatures);
                if (signature.remaining() >= 8) {
                    sigAlgorithm = signature.getInt();
                    signaturesSigAlgorithms.add(Integer.valueOf(sigAlgorithm));
                    if (isSupportedSignatureAlgorithm(sigAlgorithm)) {
                        if (bestSigAlgorithm == -1 || ApkSigningBlockUtils.compareSignatureAlgorithm(sigAlgorithm, bestSigAlgorithm) > 0) {
                            bestSigAlgorithm = sigAlgorithm;
                            bestSigAlgorithmSignatureBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signature);
                        }
                    }
                } else {
                    throw new SecurityException("Signature record too short");
                }
            } catch (IOException | BufferUnderflowException e3) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to parse signature record #");
                stringBuilder2.append(signatureCount);
                throw new SecurityException(stringBuilder2.toString(), e3);
            }
        }
        if (bestSigAlgorithm != -1) {
            String keyAlgorithm = ApkSigningBlockUtils.getSignatureAlgorithmJcaKeyAlgorithm(bestSigAlgorithm);
            Pair<String, ? extends AlgorithmParameterSpec> signatureAlgorithmParams = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(bestSigAlgorithm);
            String jcaSignatureAlgorithm = signatureAlgorithmParams.first;
            AlgorithmParameterSpec jcaSignatureAlgorithmParams = signatureAlgorithmParams.second;
            ByteBuffer byteBuffer;
            List<Integer> list;
            StringBuilder stringBuilder3;
            try {
                PublicKey publicKey = KeyFactory.getInstance(keyAlgorithm).generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                Signature sig = Signature.getInstance(jcaSignatureAlgorithm);
                sig.initVerify(publicKey);
                if (jcaSignatureAlgorithmParams != null) {
                    try {
                        sig.setParameter(jcaSignatureAlgorithmParams);
                    } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e4) {
                        e2 = e4;
                        byteBuffer = signatures;
                        i = bestSigAlgorithm;
                        i2 = signatureCount;
                        list = signaturesSigAlgorithms;
                        bArr = bestSigAlgorithmSignatureBytes;
                    }
                }
                sig.update(signedData);
                if (sig.verify(bestSigAlgorithmSignatureBytes)) {
                    int sigAlgorithm2;
                    signedData.clear();
                    ByteBuffer digests = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
                    List<Integer> digestsSigAlgorithms = new ArrayList();
                    int digestCount = 0;
                    byte[] contentDigest = null;
                    while (digests.hasRemaining()) {
                        sigAlgorithm = digestCount + 1;
                        try {
                            ByteBuffer digest = ApkSigningBlockUtils.getLengthPrefixedSlice(digests);
                            byteBuffer = signatures;
                            if (digest.remaining() >= 8) {
                                try {
                                    sigAlgorithm2 = digest.getInt();
                                    digestsSigAlgorithms.add(Integer.valueOf(sigAlgorithm2));
                                    if (sigAlgorithm2 == bestSigAlgorithm) {
                                        contentDigest = ApkSigningBlockUtils.readLengthPrefixedByteArray(digest);
                                    }
                                    digestCount = sigAlgorithm;
                                    signatures = byteBuffer;
                                } catch (IOException | BufferUnderflowException e5) {
                                    e3 = e5;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Failed to parse digest record #");
                                    stringBuilder.append(sigAlgorithm);
                                    throw new IOException(stringBuilder.toString(), e3);
                                }
                            }
                            throw new IOException("Record too short");
                        } catch (IOException | BufferUnderflowException e6) {
                            e3 = e6;
                            byteBuffer = signatures;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Failed to parse digest record #");
                            stringBuilder.append(sigAlgorithm);
                            throw new IOException(stringBuilder.toString(), e3);
                        }
                    }
                    i2 = signatureCount;
                    bArr = bestSigAlgorithmSignatureBytes;
                    if (signaturesSigAlgorithms.equals(digestsSigAlgorithms)) {
                        int digestAlgorithm;
                        ByteBuffer certificates;
                        signatures = ApkSigningBlockUtils.getSignatureAlgorithmContentDigestAlgorithm(bestSigAlgorithm);
                        bestSigAlgorithmSignatureBytes = contentDigest;
                        byte[] previousSignerDigest = (byte[]) contentDigests.put(Integer.valueOf(signatures), bestSigAlgorithmSignatureBytes);
                        if (previousSignerDigest == null) {
                            contentDigest = bestSigAlgorithm;
                        } else if (MessageDigest.isEqual(previousSignerDigest, bestSigAlgorithmSignatureBytes)) {
                            i = bestSigAlgorithm;
                        } else {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(ApkSigningBlockUtils.getContentDigestAlgorithmJcaDigestAlgorithm(signatures));
                            stringBuilder3.append(" contents digest does not match the digest specified by a preceding signer");
                            throw new SecurityException(stringBuilder3.toString());
                        }
                        ByteBuffer certificates2 = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
                        signatureCount = new ArrayList();
                        sigAlgorithm2 = 0;
                        while (certificates2.hasRemaining()) {
                            digestAlgorithm = signatures;
                            signatures = sigAlgorithm2 + 1;
                            list = signaturesSigAlgorithms;
                            byte[] encodedCert = ApkSigningBlockUtils.readLengthPrefixedByteArray(certificates2);
                            try {
                                certificates = certificates2;
                                try {
                                    signatureCount.add(new VerbatimX509Certificate((X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(encodedCert)), encodedCert));
                                    sigAlgorithm2 = signatures;
                                    signatures = digestAlgorithm;
                                    signaturesSigAlgorithms = list;
                                    certificates2 = certificates;
                                } catch (CertificateException e7) {
                                    sigAlgorithm2 = e7;
                                    signaturesSigAlgorithms = new StringBuilder();
                                    signaturesSigAlgorithms.append("Failed to decode certificate #");
                                    signaturesSigAlgorithms.append(signatures);
                                    throw new SecurityException(signaturesSigAlgorithms.toString(), sigAlgorithm2);
                                }
                            } catch (CertificateException e8) {
                                sigAlgorithm2 = e8;
                                certificates = certificates2;
                                signaturesSigAlgorithms = new StringBuilder();
                                signaturesSigAlgorithms.append("Failed to decode certificate #");
                                signaturesSigAlgorithms.append(signatures);
                                throw new SecurityException(signaturesSigAlgorithms.toString(), sigAlgorithm2);
                            }
                        }
                        digestAlgorithm = signatures;
                        certificates = certificates2;
                        list = signaturesSigAlgorithms;
                        byte[] bArr2 = bestSigAlgorithmSignatureBytes;
                        if (signatureCount.isEmpty() != null) {
                            throw new SecurityException("No certificates listed");
                        } else if (Arrays.equals(publicKeyBytes, ((X509Certificate) signatureCount.get(null)).getPublicKey().getEncoded())) {
                            verifyAdditionalAttributes(ApkSigningBlockUtils.getLengthPrefixedSlice(signedData));
                            return (X509Certificate[]) signatureCount.toArray(new X509Certificate[signatureCount.size()]);
                        } else {
                            throw new SecurityException("Public key mismatch between certificate and signature record");
                        }
                    }
                    throw new SecurityException("Signature algorithms don't match between digests and signatures records");
                }
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(jcaSignatureAlgorithm);
                stringBuilder4.append(" signature did not verify");
                throw new SecurityException(stringBuilder4.toString());
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e9) {
                e2 = e9;
                byteBuffer = signatures;
                i = bestSigAlgorithm;
                i2 = signatureCount;
                list = signaturesSigAlgorithms;
                bArr = bestSigAlgorithmSignatureBytes;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Failed to verify ");
                stringBuilder3.append(jcaSignatureAlgorithm);
                stringBuilder3.append(" signature");
                throw new SecurityException(stringBuilder3.toString(), e2);
            }
        } else if (signatureCount == 0) {
            throw new SecurityException("No signatures found");
        } else {
            throw new SecurityException("No supported signatures found");
        }
    }

    private static void verifyAdditionalAttributes(ByteBuffer attrs) throws SecurityException, IOException {
        while (attrs.hasRemaining()) {
            ByteBuffer attr = ApkSigningBlockUtils.getLengthPrefixedSlice(attrs);
            if (attr.remaining() < 4) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remaining buffer too short to contain additional attribute ID. Remaining: ");
                stringBuilder.append(attr.remaining());
                throw new IOException(stringBuilder.toString());
            } else if (attr.getInt() == STRIPPING_PROTECTION_ATTR_ID) {
                if (attr.remaining() < 4) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("V2 Signature Scheme Stripping Protection Attribute  value too small.  Expected 4 bytes, but found ");
                    stringBuilder2.append(attr.remaining());
                    throw new IOException(stringBuilder2.toString());
                } else if (attr.getInt() == 3) {
                    throw new SecurityException("V2 signature indicates APK is signed using APK Signature Scheme v3, but none was found. Signature stripped?");
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:9:0x001b, code skipped:
            $closeResource(r1, r0);
     */
    static byte[] getVerityRootHash(java.lang.String r5) throws java.io.IOException, android.util.apk.SignatureNotFoundException, java.lang.SecurityException {
        /*
        r0 = new java.io.RandomAccessFile;
        r1 = "r";
        r0.<init>(r5, r1);
        r1 = findSignature(r0);	 Catch:{ all -> 0x0018 }
        r2 = 0;
        r2 = verify(r0, r2);	 Catch:{ all -> 0x0018 }
        r3 = r2.verityRootHash;	 Catch:{ all -> 0x0018 }
        r4 = 0;
        $closeResource(r4, r0);
        return r3;
    L_0x0018:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x001a }
    L_0x001a:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV2Verifier.getVerityRootHash(java.lang.String):byte[]");
    }

    /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            $closeResource(r1, r0);
     */
    static byte[] generateApkVerity(java.lang.String r4, android.util.apk.ByteBufferFactory r5) throws java.io.IOException, android.util.apk.SignatureNotFoundException, java.lang.SecurityException, java.security.DigestException, java.security.NoSuchAlgorithmException {
        /*
        r0 = new java.io.RandomAccessFile;
        r1 = "r";
        r0.<init>(r4, r1);
        r1 = findSignature(r0);	 Catch:{ all -> 0x0015 }
        r2 = android.util.apk.VerityBuilder.generateApkVerity(r4, r5, r1);	 Catch:{ all -> 0x0015 }
        r3 = 0;
        $closeResource(r3, r0);
        return r2;
    L_0x0015:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0017 }
    L_0x0017:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV2Verifier.generateApkVerity(java.lang.String, android.util.apk.ByteBufferFactory):byte[]");
    }

    /* JADX WARNING: Missing block: B:15:0x002c, code skipped:
            $closeResource(r1, r0);
     */
    static byte[] generateApkVerityRootHash(java.lang.String r5) throws java.io.IOException, android.util.apk.SignatureNotFoundException, java.security.DigestException, java.security.NoSuchAlgorithmException {
        /*
        r0 = new java.io.RandomAccessFile;
        r1 = "r";
        r0.<init>(r5, r1);
        r1 = findSignature(r0);	 Catch:{ all -> 0x0029 }
        r2 = 0;
        r2 = verify(r0, r2);	 Catch:{ all -> 0x0029 }
        r3 = r2.verityRootHash;	 Catch:{ all -> 0x0029 }
        r4 = 0;
        if (r3 != 0) goto L_0x001b;
        $closeResource(r4, r0);
        return r4;
    L_0x001b:
        r3 = r2.verityRootHash;	 Catch:{ all -> 0x0029 }
        r3 = java.nio.ByteBuffer.wrap(r3);	 Catch:{ all -> 0x0029 }
        r3 = android.util.apk.VerityBuilder.generateApkVerityRootHash(r0, r3, r1);	 Catch:{ all -> 0x0029 }
        $closeResource(r4, r0);
        return r3;
    L_0x0029:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x002b }
    L_0x002b:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV2Verifier.generateApkVerityRootHash(java.lang.String):byte[]");
    }

    private static boolean isSupportedSignatureAlgorithm(int sigAlgorithm) {
        if (!(sigAlgorithm == 513 || sigAlgorithm == 514 || sigAlgorithm == 769 || sigAlgorithm == 1057 || sigAlgorithm == 1059 || sigAlgorithm == Process.OTA_UPDATE_UID)) {
            switch (sigAlgorithm) {
                case 257:
                case 258:
                case 259:
                case 260:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
