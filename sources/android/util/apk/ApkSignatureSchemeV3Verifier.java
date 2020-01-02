package android.util.apk;

import android.os.Build.VERSION;
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
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ApkSignatureSchemeV3Verifier {
    private static final int APK_SIGNATURE_SCHEME_V3_BLOCK_ID = -262969152;
    private static final int PROOF_OF_ROTATION_ATTR_ID = 1000370060;
    public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 3;

    private static class PlatformNotSupportedException extends Exception {
        PlatformNotSupportedException(String s) {
            super(s);
        }
    }

    public static class VerifiedProofOfRotation {
        public final List<X509Certificate> certs;
        public final List<Integer> flagsList;

        public VerifiedProofOfRotation(List<X509Certificate> certs, List<Integer> flagsList) {
            this.certs = certs;
            this.flagsList = flagsList;
        }
    }

    public static class VerifiedSigner {
        public final X509Certificate[] certs;
        public final VerifiedProofOfRotation por;
        public byte[] verityRootHash;

        public VerifiedSigner(X509Certificate[] certs, VerifiedProofOfRotation por) {
            this.certs = certs;
            this.por = por;
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
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV3Verifier.hasSignature(java.lang.String):boolean");
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

    public static VerifiedSigner verify(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, true);
    }

    public static VerifiedSigner unsafeGetCertsWithoutVerification(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, false);
    }

    /* JADX WARNING: Missing block: B:9:0x0014, code skipped:
            $closeResource(r1, r0);
     */
    private static android.util.apk.ApkSignatureSchemeV3Verifier.VerifiedSigner verify(java.lang.String r3, boolean r4) throws android.util.apk.SignatureNotFoundException, java.lang.SecurityException, java.io.IOException {
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
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV3Verifier.verify(java.lang.String, boolean):android.util.apk.ApkSignatureSchemeV3Verifier$VerifiedSigner");
    }

    private static VerifiedSigner verify(RandomAccessFile apk, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apk, findSignature(apk), verifyIntegrity);
    }

    private static SignatureInfo findSignature(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        return ApkSigningBlockUtils.findSignature(apk, APK_SIGNATURE_SCHEME_V3_BLOCK_ID);
    }

    private static VerifiedSigner verify(RandomAccessFile apk, SignatureInfo signatureInfo, boolean doVerifyIntegrity) throws SecurityException, IOException {
        int signerCount = 0;
        Map<Integer, byte[]> contentDigests = new ArrayMap();
        VerifiedSigner result = null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try {
                ByteBuffer signers = ApkSigningBlockUtils.getLengthPrefixedSlice(signatureInfo.signatureBlock);
                while (signers.hasRemaining()) {
                    try {
                        result = verifySigner(ApkSigningBlockUtils.getLengthPrefixedSlice(signers), contentDigests, certFactory);
                        signerCount++;
                    } catch (PlatformNotSupportedException e) {
                    } catch (IOException | SecurityException | BufferUnderflowException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to parse/verify signer #");
                        stringBuilder.append(signerCount);
                        stringBuilder.append(" block");
                        throw new SecurityException(stringBuilder.toString(), e2);
                    }
                }
                if (signerCount < 1 || result == null) {
                    throw new SecurityException("No signers found");
                } else if (signerCount != 1) {
                    throw new SecurityException("APK Signature Scheme V3 only supports one signer: multiple signers found.");
                } else if (contentDigests.isEmpty()) {
                    throw new SecurityException("No content digests found");
                } else {
                    if (doVerifyIntegrity) {
                        ApkSigningBlockUtils.verifyIntegrity(contentDigests, apk, signatureInfo);
                    }
                    if (contentDigests.containsKey(Integer.valueOf(3))) {
                        result.verityRootHash = ApkSigningBlockUtils.parseVerityDigestAndVerifySourceLength((byte[]) contentDigests.get(Integer.valueOf(3)), apk.length(), signatureInfo);
                    }
                    return result;
                }
            } catch (IOException e3) {
                throw new SecurityException("Failed to read list of signers", e3);
            }
        } catch (CertificateException e4) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e4);
        }
    }

    private static VerifiedSigner verifySigner(ByteBuffer signerBlock, Map<Integer, byte[]> contentDigests, CertificateFactory certFactory) throws SecurityException, IOException, PlatformNotSupportedException {
        Exception e;
        GeneralSecurityException e2;
        ByteBuffer byteBuffer;
        List<Integer> list;
        Pair<String, ? extends AlgorithmParameterSpec> pair;
        StringBuilder stringBuilder;
        CertificateFactory certificateFactory = certFactory;
        ByteBuffer signedData = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        int minSdkVersion = signerBlock.getInt();
        int maxSdkVersion = signerBlock.getInt();
        StringBuilder stringBuilder2;
        if (VERSION.SDK_INT < minSdkVersion || VERSION.SDK_INT > maxSdkVersion) {
            Map<Integer, byte[]> map = contentDigests;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Signer not supported by this platform version. This platform: ");
            stringBuilder2.append(VERSION.SDK_INT);
            stringBuilder2.append(", signer minSdkVersion: ");
            stringBuilder2.append(minSdkVersion);
            stringBuilder2.append(", maxSdkVersion: ");
            stringBuilder2.append(maxSdkVersion);
            throw new PlatformNotSupportedException(stringBuilder2.toString());
        }
        ByteBuffer signature;
        ByteBuffer signatures = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        byte[] publicKeyBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signerBlock);
        int bestSigAlgorithm = -1;
        List<Integer> signaturesSigAlgorithms = new ArrayList();
        byte[] bestSigAlgorithmSignatureBytes = null;
        int signatureCount = 0;
        while (signatures.hasRemaining()) {
            signatureCount++;
            try {
                signature = ApkSigningBlockUtils.getLengthPrefixedSlice(signatures);
                if (signature.remaining() >= 8) {
                    int sigAlgorithm = signature.getInt();
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
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Failed to parse signature record #");
                stringBuilder3.append(signatureCount);
                throw new SecurityException(stringBuilder3.toString(), e3);
            }
        }
        if (bestSigAlgorithm != -1) {
            String keyAlgorithm = ApkSigningBlockUtils.getSignatureAlgorithmJcaKeyAlgorithm(bestSigAlgorithm);
            Pair<String, ? extends AlgorithmParameterSpec> signatureAlgorithmParams = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(bestSigAlgorithm);
            String jcaSignatureAlgorithm = signatureAlgorithmParams.first;
            AlgorithmParameterSpec jcaSignatureAlgorithmParams = signatureAlgorithmParams.second;
            int i;
            int i2;
            byte[] bArr;
            String str;
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
                        str = keyAlgorithm;
                        pair = signatureAlgorithmParams;
                        bestSigAlgorithmSignatureBytes = contentDigests;
                    }
                }
                sig.update(signedData);
                boolean sigVerified = sig.verify(bestSigAlgorithmSignatureBytes);
                if (sigVerified) {
                    boolean sigVerified2;
                    signedData.clear();
                    ByteBuffer digests = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
                    List<Integer> digestsSigAlgorithms = new ArrayList();
                    int digestCount = 0;
                    signatures = null;
                    while (digests.hasRemaining()) {
                        i2 = signatureCount;
                        signatureCount = digestCount + 1;
                        List<Integer> keyAlgorithm2;
                        try {
                            signature = ApkSigningBlockUtils.getLengthPrefixedSlice(digests);
                            bArr = bestSigAlgorithmSignatureBytes;
                            try {
                                sigVerified2 = sigVerified;
                                if (signature.remaining() >= true) {
                                    int sigAlgorithm2;
                                    try {
                                        sigAlgorithm2 = signature.getInt();
                                        str = keyAlgorithm;
                                        keyAlgorithm2 = digestsSigAlgorithms;
                                    } catch (IOException | BufferUnderflowException e5) {
                                        e3 = e5;
                                        str = keyAlgorithm;
                                        keyAlgorithm2 = digestsSigAlgorithms;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("Failed to parse digest record #");
                                        stringBuilder.append(signatureCount);
                                        throw new IOException(stringBuilder.toString(), e3);
                                    }
                                    try {
                                        keyAlgorithm2.add(Integer.valueOf(sigAlgorithm2));
                                        if (sigAlgorithm2 == bestSigAlgorithm) {
                                            signatures = ApkSigningBlockUtils.readLengthPrefixedByteArray(signature);
                                        }
                                        digestCount = signatureCount;
                                        digestsSigAlgorithms = keyAlgorithm2;
                                        signatureCount = i2;
                                        bestSigAlgorithmSignatureBytes = bArr;
                                        sigVerified = sigVerified2;
                                        keyAlgorithm = str;
                                    } catch (IOException | BufferUnderflowException e6) {
                                        e3 = e6;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("Failed to parse digest record #");
                                        stringBuilder.append(signatureCount);
                                        throw new IOException(stringBuilder.toString(), e3);
                                    }
                                }
                                keyAlgorithm2 = digestsSigAlgorithms;
                                throw new IOException("Record too short");
                            } catch (IOException | BufferUnderflowException e7) {
                                e3 = e7;
                                sigVerified2 = sigVerified;
                                str = keyAlgorithm;
                                keyAlgorithm2 = digestsSigAlgorithms;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Failed to parse digest record #");
                                stringBuilder.append(signatureCount);
                                throw new IOException(stringBuilder.toString(), e3);
                            }
                        } catch (IOException | BufferUnderflowException e8) {
                            e3 = e8;
                            bArr = bestSigAlgorithmSignatureBytes;
                            sigVerified2 = sigVerified;
                            str = keyAlgorithm;
                            keyAlgorithm2 = digestsSigAlgorithms;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Failed to parse digest record #");
                            stringBuilder.append(signatureCount);
                            throw new IOException(stringBuilder.toString(), e3);
                        }
                    }
                    bArr = bestSigAlgorithmSignatureBytes;
                    sigVerified2 = sigVerified;
                    str = keyAlgorithm;
                    pair = signatureAlgorithmParams;
                    if (signaturesSigAlgorithms.equals(digestsSigAlgorithms)) {
                        int digestAlgorithm;
                        ByteBuffer certificates;
                        signatureCount = ApkSigningBlockUtils.getSignatureAlgorithmContentDigestAlgorithm(bestSigAlgorithm);
                        byte[] sigVerified3 = (byte[]) contentDigests.put(Integer.valueOf(signatureCount), signatures);
                        byte[] contentDigest;
                        if (sigVerified3 == null) {
                            contentDigest = signatures;
                        } else if (MessageDigest.isEqual(sigVerified3, signatures)) {
                            ByteBuffer byteBuffer2 = signatures;
                        } else {
                            StringBuilder stringBuilder4 = new StringBuilder();
                            contentDigest = signatures;
                            stringBuilder4.append(ApkSigningBlockUtils.getContentDigestAlgorithmJcaDigestAlgorithm(signatureCount));
                            stringBuilder4.append(" contents digest does not match the digest specified by a preceding signer");
                            throw new SecurityException(stringBuilder4.toString());
                        }
                        signatures = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
                        ArrayList signatureAlgorithmParams2 = new ArrayList();
                        int certificateCount = 0;
                        while (signatures.hasRemaining()) {
                            i = bestSigAlgorithm;
                            bestSigAlgorithm = certificateCount + 1;
                            digestAlgorithm = signatureCount;
                            byte[] encodedCert = ApkSigningBlockUtils.readLengthPrefixedByteArray(signatures);
                            try {
                                certificates = signatures;
                                signatureAlgorithmParams2.add(new VerbatimX509Certificate((X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(encodedCert)), encodedCert));
                                certificateCount = bestSigAlgorithm;
                                bestSigAlgorithm = i;
                                signatureCount = digestAlgorithm;
                                signatures = certificates;
                            } catch (CertificateException certificateCount2) {
                                certificates = signatures;
                                signatureCount = new StringBuilder();
                                signatureCount.append("Failed to decode certificate #");
                                signatureCount.append(bestSigAlgorithm);
                                throw new SecurityException(signatureCount.toString(), certificateCount2);
                            }
                        }
                        certificates = signatures;
                        i = bestSigAlgorithm;
                        digestAlgorithm = signatureCount;
                        list = signaturesSigAlgorithms;
                        if (signatureAlgorithmParams2.isEmpty() != null) {
                            throw new SecurityException("No certificates listed");
                        } else if (!Arrays.equals(publicKeyBytes, ((X509Certificate) signatureAlgorithmParams2.get(null)).getPublicKey().getEncoded())) {
                            throw new SecurityException("Public key mismatch between certificate and signature record");
                        } else if (signedData.getInt() != minSdkVersion) {
                            throw new SecurityException("minSdkVersion mismatch between signed and unsigned in v3 signer block.");
                        } else if (signedData.getInt() == maxSdkVersion) {
                            return verifyAdditionalAttributes(ApkSigningBlockUtils.getLengthPrefixedSlice(signedData), signatureAlgorithmParams2, certificateFactory);
                        } else {
                            throw new SecurityException("maxSdkVersion mismatch between signed and unsigned in v3 signer block.");
                        }
                    }
                    throw new SecurityException("Signature algorithms don't match between digests and signatures records");
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(jcaSignatureAlgorithm);
                stringBuilder2.append(" signature did not verify");
                throw new SecurityException(stringBuilder2.toString());
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e9) {
                e2 = e9;
                byteBuffer = signatures;
                i = bestSigAlgorithm;
                i2 = signatureCount;
                list = signaturesSigAlgorithms;
                bArr = bestSigAlgorithmSignatureBytes;
                str = keyAlgorithm;
                pair = signatureAlgorithmParams;
                bestSigAlgorithmSignatureBytes = contentDigests;
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append("Failed to verify ");
                stringBuilder5.append(jcaSignatureAlgorithm);
                stringBuilder5.append(" signature");
                throw new SecurityException(stringBuilder5.toString(), e2);
            }
        } else if (signatureCount == 0) {
            throw new SecurityException("No signatures found");
        } else {
            throw new SecurityException("No supported signatures found");
        }
    }

    private static VerifiedSigner verifyAdditionalAttributes(ByteBuffer attrs, List<X509Certificate> certs, CertificateFactory certFactory) throws IOException {
        X509Certificate[] certChain = (X509Certificate[]) certs.toArray(new X509Certificate[certs.size()]);
        VerifiedProofOfRotation por = null;
        while (attrs.hasRemaining()) {
            ByteBuffer attr = ApkSigningBlockUtils.getLengthPrefixedSlice(attrs);
            if (attr.remaining() < 4) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remaining buffer too short to contain additional attribute ID. Remaining: ");
                stringBuilder.append(attr.remaining());
                throw new IOException(stringBuilder.toString());
            } else if (attr.getInt() == PROOF_OF_ROTATION_ATTR_ID) {
                if (por == null) {
                    por = verifyProofOfRotationStruct(attr, certFactory);
                    try {
                        if (por.certs.size() <= 0) {
                            continue;
                        } else if (!Arrays.equals(((X509Certificate) por.certs.get(por.certs.size() - 1)).getEncoded(), certChain[0].getEncoded())) {
                            throw new SecurityException("Terminal certificate in Proof-of-rotation record does not match APK signing certificate");
                        }
                    } catch (CertificateEncodingException e) {
                        throw new SecurityException("Failed to encode certificate when comparing Proof-of-rotation record and signing certificate", e);
                    }
                }
                throw new SecurityException("Encountered multiple Proof-of-rotation records when verifying APK Signature Scheme v3 signature");
            }
        }
        return new VerifiedSigner(certChain, por);
    }

    private static VerifiedProofOfRotation verifyProofOfRotationStruct(ByteBuffer porBuf, CertificateFactory certFactory) throws SecurityException, IOException {
        Exception e;
        GeneralSecurityException e2;
        StringBuilder stringBuilder;
        CertificateException e3;
        String str = " when verifying Proof-of-rotation record";
        int levelCount = 0;
        int lastSigAlgorithm = -1;
        X509Certificate lastCert = null;
        ArrayList certs = new ArrayList();
        ArrayList flagsList = new ArrayList();
        CertificateFactory certificateFactory;
        try {
            porBuf.getInt();
            HashSet<X509Certificate> certHistorySet = new HashSet();
            while (porBuf.hasRemaining()) {
                levelCount++;
                ByteBuffer level = ApkSigningBlockUtils.getLengthPrefixedSlice(porBuf);
                ByteBuffer signedData = ApkSigningBlockUtils.getLengthPrefixedSlice(level);
                int flags = level.getInt();
                int sigAlgorithm = level.getInt();
                byte[] signature = ApkSigningBlockUtils.readLengthPrefixedByteArray(level);
                byte[] bArr;
                if (lastCert != null) {
                    Pair<String, ? extends AlgorithmParameterSpec> sigAlgParams = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(lastSigAlgorithm);
                    PublicKey publicKey = lastCert.getPublicKey();
                    Signature sig = Signature.getInstance((String) sigAlgParams.first);
                    sig.initVerify(publicKey);
                    if (sigAlgParams.second != null) {
                        sig.setParameter((AlgorithmParameterSpec) sigAlgParams.second);
                    }
                    sig.update(signedData);
                    if (sig.verify(signature)) {
                        ByteBuffer byteBuffer = level;
                        bArr = signature;
                    } else {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Unable to verify signature of certificate #");
                        stringBuilder2.append(levelCount);
                        stringBuilder2.append(" using ");
                        stringBuilder2.append((String) sigAlgParams.first);
                        stringBuilder2.append(str);
                        throw new SecurityException(stringBuilder2.toString());
                    }
                }
                bArr = signature;
                signedData.rewind();
                byte[] encodedCert = ApkSigningBlockUtils.readLengthPrefixedByteArray(signedData);
                int signedSigAlgorithm = signedData.getInt();
                if (lastCert != null) {
                    if (lastSigAlgorithm != signedSigAlgorithm) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Signing algorithm ID mismatch for certificate #");
                        stringBuilder3.append(levelCount);
                        stringBuilder3.append(str);
                        throw new SecurityException(stringBuilder3.toString());
                    }
                }
                try {
                    lastCert = new VerbatimX509Certificate((X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(encodedCert)), encodedCert);
                    lastSigAlgorithm = sigAlgorithm;
                    if (certHistorySet.contains(lastCert)) {
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("Encountered duplicate entries in Proof-of-rotation record at certificate #");
                        stringBuilder4.append(levelCount);
                        stringBuilder4.append(".  All signing certificates should be unique");
                        throw new SecurityException(stringBuilder4.toString());
                    }
                    certHistorySet.add(lastCert);
                    certs.add(lastCert);
                    flagsList.add(Integer.valueOf(flags));
                } catch (IOException | BufferUnderflowException e4) {
                    e = e4;
                    throw new IOException("Failed to parse Proof-of-rotation record", e);
                } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e5) {
                    e2 = e5;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to verify signature over signed data for certificate #");
                    stringBuilder.append(levelCount);
                    stringBuilder.append(str);
                    throw new SecurityException(stringBuilder.toString(), e2);
                } catch (CertificateException e6) {
                    e3 = e6;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to decode certificate #");
                    stringBuilder.append(levelCount);
                    stringBuilder.append(str);
                    throw new SecurityException(stringBuilder.toString(), e3);
                }
            }
            certificateFactory = certFactory;
            return new VerifiedProofOfRotation(certs, flagsList);
        } catch (IOException | BufferUnderflowException e7) {
            e = e7;
            certificateFactory = certFactory;
            throw new IOException("Failed to parse Proof-of-rotation record", e);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e8) {
            e2 = e8;
            certificateFactory = certFactory;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to verify signature over signed data for certificate #");
            stringBuilder.append(levelCount);
            stringBuilder.append(str);
            throw new SecurityException(stringBuilder.toString(), e2);
        } catch (CertificateException e9) {
            e3 = e9;
            certificateFactory = certFactory;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to decode certificate #");
            stringBuilder.append(levelCount);
            stringBuilder.append(str);
            throw new SecurityException(stringBuilder.toString(), e3);
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
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV3Verifier.getVerityRootHash(java.lang.String):byte[]");
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
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV3Verifier.generateApkVerity(java.lang.String, android.util.apk.ByteBufferFactory):byte[]");
    }

    /* JADX WARNING: Missing block: B:15:0x002c, code skipped:
            $closeResource(r1, r0);
     */
    static byte[] generateApkVerityRootHash(java.lang.String r5) throws java.security.NoSuchAlgorithmException, java.security.DigestException, java.io.IOException, android.util.apk.SignatureNotFoundException {
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
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV3Verifier.generateApkVerityRootHash(java.lang.String):byte[]");
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
