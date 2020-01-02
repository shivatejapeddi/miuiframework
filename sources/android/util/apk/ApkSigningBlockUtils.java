package android.util.apk;

import android.security.keystore.KeyProperties;
import android.util.ArrayMap;
import android.util.Pair;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Map;

final class ApkSigningBlockUtils {
    private static final long APK_SIG_BLOCK_MAGIC_HI = 3617552046287187010L;
    private static final long APK_SIG_BLOCK_MAGIC_LO = 2334950737559900225L;
    private static final int APK_SIG_BLOCK_MIN_SIZE = 32;
    private static final int CHUNK_SIZE_BYTES = 1048576;
    static final int CONTENT_DIGEST_CHUNKED_SHA256 = 1;
    static final int CONTENT_DIGEST_CHUNKED_SHA512 = 2;
    static final int CONTENT_DIGEST_VERITY_CHUNKED_SHA256 = 3;
    static final int SIGNATURE_DSA_WITH_SHA256 = 769;
    static final int SIGNATURE_ECDSA_WITH_SHA256 = 513;
    static final int SIGNATURE_ECDSA_WITH_SHA512 = 514;
    static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 = 259;
    static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 = 260;
    static final int SIGNATURE_RSA_PSS_WITH_SHA256 = 257;
    static final int SIGNATURE_RSA_PSS_WITH_SHA512 = 258;
    static final int SIGNATURE_VERITY_DSA_WITH_SHA256 = 1061;
    static final int SIGNATURE_VERITY_ECDSA_WITH_SHA256 = 1059;
    static final int SIGNATURE_VERITY_RSA_PKCS1_V1_5_WITH_SHA256 = 1057;

    private static class MultipleDigestDataDigester implements DataDigester {
        private final MessageDigest[] mMds;

        MultipleDigestDataDigester(MessageDigest[] mds) {
            this.mMds = mds;
        }

        public void consume(ByteBuffer buffer) {
            buffer = buffer.slice();
            for (MessageDigest md : this.mMds) {
                buffer.position(0);
                md.update(buffer);
            }
        }
    }

    private ApkSigningBlockUtils() {
    }

    static SignatureInfo findSignature(RandomAccessFile apk, int blockId) throws IOException, SignatureNotFoundException {
        RandomAccessFile randomAccessFile = apk;
        Pair<ByteBuffer, Long> eocdAndOffsetInFile = getEocd(apk);
        ByteBuffer eocd = eocdAndOffsetInFile.first;
        long eocdOffset = ((Long) eocdAndOffsetInFile.second).longValue();
        if (ZipUtils.isZip64EndOfCentralDirectoryLocatorPresent(randomAccessFile, eocdOffset)) {
            throw new SignatureNotFoundException("ZIP64 APK not supported");
        }
        long centralDirOffset = getCentralDirOffset(eocd, eocdOffset);
        Pair<ByteBuffer, Long> apkSigningBlockAndOffsetInFile = findApkSigningBlock(randomAccessFile, centralDirOffset);
        ByteBuffer apkSigningBlock = apkSigningBlockAndOffsetInFile.first;
        return new SignatureInfo(findApkSignatureSchemeBlock(apkSigningBlock, blockId), ((Long) apkSigningBlockAndOffsetInFile.second).longValue(), centralDirOffset, eocdOffset, eocd);
    }

    static void verifyIntegrity(Map<Integer, byte[]> expectedDigests, RandomAccessFile apk, SignatureInfo signatureInfo) throws SecurityException {
        if (expectedDigests.isEmpty()) {
            throw new SecurityException("No digests provided");
        }
        boolean neverVerified = true;
        Map<Integer, byte[]> expected1MbChunkDigests = new ArrayMap();
        if (expectedDigests.containsKey(Integer.valueOf(1))) {
            expected1MbChunkDigests.put(Integer.valueOf(1), (byte[]) expectedDigests.get(Integer.valueOf(1)));
        }
        if (expectedDigests.containsKey(Integer.valueOf(2))) {
            expected1MbChunkDigests.put(Integer.valueOf(2), (byte[]) expectedDigests.get(Integer.valueOf(2)));
        }
        if (!expected1MbChunkDigests.isEmpty()) {
            try {
                verifyIntegrityFor1MbChunkBasedAlgorithm(expected1MbChunkDigests, apk.getFD(), signatureInfo);
                neverVerified = false;
            } catch (IOException e) {
                throw new SecurityException("Cannot get FD", e);
            }
        }
        if (expectedDigests.containsKey(Integer.valueOf(3))) {
            verifyIntegrityForVerityBasedAlgorithm((byte[]) expectedDigests.get(Integer.valueOf(3)), apk, signatureInfo);
            neverVerified = false;
        }
        if (neverVerified) {
            throw new SecurityException("No known digest exists for integrity check");
        }
    }

    private static void verifyIntegrityFor1MbChunkBasedAlgorithm(Map<Integer, byte[]> expectedDigests, FileDescriptor apkFileDescriptor, SignatureInfo signatureInfo) throws SecurityException {
        SignatureInfo signatureInfo2 = signatureInfo;
        MemoryMappedFileDataSource beforeApkSigningBlock = new MemoryMappedFileDataSource(apkFileDescriptor, 0, signatureInfo2.apkSigningBlockOffset);
        MemoryMappedFileDataSource centralDir = new MemoryMappedFileDataSource(apkFileDescriptor, signatureInfo2.centralDirOffset, signatureInfo2.eocdOffset - signatureInfo2.centralDirOffset);
        ByteBuffer eocdBuf = signatureInfo2.eocd.duplicate();
        eocdBuf.order(ByteOrder.LITTLE_ENDIAN);
        ZipUtils.setZipEocdCentralDirectoryOffset(eocdBuf, signatureInfo2.apkSigningBlockOffset);
        ByteBufferDataSource eocd = new ByteBufferDataSource(eocdBuf);
        int[] digestAlgorithms = new int[expectedDigests.size()];
        int digestAlgorithmCount = 0;
        for (Integer digestAlgorithm : expectedDigests.keySet()) {
            digestAlgorithms[digestAlgorithmCount] = digestAlgorithm.intValue();
            digestAlgorithmCount++;
        }
        Map<Integer, byte[]> map;
        try {
            byte[][] actualDigests = computeContentDigestsPer1MbChunk(digestAlgorithms, new DataSource[]{beforeApkSigningBlock, centralDir, eocd});
            int i = 0;
            while (i < digestAlgorithms.length) {
                int digestAlgorithm2 = digestAlgorithms[i];
                if (MessageDigest.isEqual((byte[]) expectedDigests.get(Integer.valueOf(digestAlgorithm2)), actualDigests[i])) {
                    i++;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm2));
                    stringBuilder.append(" digest of contents did not verify");
                    throw new SecurityException(stringBuilder.toString());
                }
            }
            map = expectedDigests;
        } catch (DigestException e) {
            map = expectedDigests;
            throw new SecurityException("Failed to compute digest(s) of contents", e);
        }
    }

    /* JADX WARNING: Missing block: B:38:0x0114, code skipped:
            r25 = r0;
            r23 = r9;
            r26 = r12;
            r2 = r2 + ((long) r1);
            r10 = r10 - ((long) r1);
            r15 = r15 + 1;
            r1 = r6;
            r8 = r8;
            r6 = r21;
     */
    private static byte[][] computeContentDigestsPer1MbChunk(int[] r29, android.util.apk.DataSource[] r30) throws java.security.DigestException {
        /*
        r1 = r29;
        r2 = r30;
        r3 = 0;
        r0 = r2.length;
        r5 = 0;
        r6 = r3;
        r3 = r5;
    L_0x000a:
        if (r3 >= r0) goto L_0x001a;
    L_0x000c:
        r4 = r2[r3];
        r8 = r4.size();
        r8 = getChunkCount(r8);
        r6 = r6 + r8;
        r3 = r3 + 1;
        goto L_0x000a;
    L_0x001a:
        r3 = 2097151; // 0x1fffff float:2.938734E-39 double:1.0361303E-317;
        r0 = (r6 > r3 ? 1 : (r6 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x01bb;
    L_0x0021:
        r3 = (int) r6;
        r0 = r1.length;
        r4 = new byte[r0][];
        r0 = 0;
    L_0x0026:
        r8 = r1.length;
        r9 = 5;
        r10 = 1;
        if (r0 >= r8) goto L_0x0042;
    L_0x002b:
        r8 = r1[r0];
        r11 = getContentDigestAlgorithmOutputSizeBytes(r8);
        r12 = r3 * r11;
        r12 = r12 + r9;
        r9 = new byte[r12];
        r12 = 90;
        r9[r5] = r12;
        setUnsignedInt32LittleEndian(r3, r9, r10);
        r4[r0] = r9;
        r0 = r0 + 1;
        goto L_0x0026;
    L_0x0042:
        r8 = new byte[r9];
        r0 = -91;
        r8[r5] = r0;
        r11 = 0;
        r0 = r1.length;
        r12 = new java.security.MessageDigest[r0];
        r0 = 0;
        r13 = r0;
    L_0x004e:
        r0 = r1.length;
        r14 = " digest not supported";
        if (r13 >= r0) goto L_0x0079;
    L_0x0053:
        r0 = r1[r13];
        r15 = getContentDigestAlgorithmJcaDigestAlgorithm(r0);
        r0 = java.security.MessageDigest.getInstance(r15);	 Catch:{ NoSuchAlgorithmException -> 0x0063 }
        r12[r13] = r0;	 Catch:{ NoSuchAlgorithmException -> 0x0063 }
        r13 = r13 + 1;
        goto L_0x004e;
    L_0x0063:
        r0 = move-exception;
        r5 = new java.lang.RuntimeException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r9.append(r15);
        r9.append(r14);
        r9 = r9.toString();
        r5.<init>(r9, r0);
        throw r5;
    L_0x0079:
        r0 = new android.util.apk.ApkSigningBlockUtils$MultipleDigestDataDigester;
        r0.<init>(r12);
        r13 = r0;
        r0 = 0;
        r15 = r2.length;
        r16 = r0;
    L_0x0083:
        if (r5 >= r15) goto L_0x0179;
    L_0x0085:
        r9 = r2[r5];
        r17 = 0;
        r19 = r9.size();
        r27 = r17;
        r17 = r3;
        r18 = r15;
        r2 = r27;
        r15 = r11;
        r10 = r19;
    L_0x0098:
        r19 = 0;
        r19 = (r10 > r19 ? 1 : (r10 == r19 ? 0 : -1));
        if (r19 <= 0) goto L_0x0158;
    L_0x009e:
        r0 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r0 = java.lang.Math.min(r10, r0);
        r1 = (int) r0;
        r0 = 1;
        setUnsignedInt32LittleEndian(r1, r8, r0);
        r20 = 0;
        r0 = r20;
    L_0x00ae:
        r21 = r6;
        r6 = r12.length;
        if (r0 >= r6) goto L_0x00bd;
    L_0x00b3:
        r6 = r12[r0];
        r6.update(r8);
        r0 = r0 + 1;
        r6 = r21;
        goto L_0x00ae;
    L_0x00bd:
        r9.feedIntoDataDigester(r13, r2, r1);	 Catch:{ IOException -> 0x012c }
        r0 = 0;
    L_0x00c2:
        r6 = r29;
        r7 = r6.length;
        if (r0 >= r7) goto L_0x0114;
    L_0x00c7:
        r7 = r6[r0];
        r20 = r8;
        r8 = r4[r0];
        r23 = r9;
        r9 = getContentDigestAlgorithmOutputSizeBytes(r7);
        r24 = r7;
        r7 = r12[r0];
        r25 = r15 * r9;
        r26 = r12;
        r19 = 5;
        r12 = r25 + 5;
        r12 = r7.digest(r8, r12, r9);
        if (r12 != r9) goto L_0x00ef;
    L_0x00e6:
        r0 = r0 + 1;
        r8 = r20;
        r9 = r23;
        r12 = r26;
        goto L_0x00c2;
    L_0x00ef:
        r5 = new java.lang.RuntimeException;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r25 = r0;
        r0 = "Unexpected output size of ";
        r14.append(r0);
        r0 = r7.getAlgorithm();
        r14.append(r0);
        r0 = " digest: ";
        r14.append(r0);
        r14.append(r12);
        r0 = r14.toString();
        r5.<init>(r0);
        throw r5;
    L_0x0114:
        r25 = r0;
        r20 = r8;
        r23 = r9;
        r26 = r12;
        r19 = 5;
        r7 = (long) r1;
        r2 = r2 + r7;
        r7 = (long) r1;
        r10 = r10 - r7;
        r15 = r15 + 1;
        r1 = r6;
        r8 = r20;
        r6 = r21;
        goto L_0x0098;
    L_0x012c:
        r0 = move-exception;
        r6 = r29;
        r20 = r8;
        r23 = r9;
        r26 = r12;
        r5 = r0;
        r0 = r5;
        r5 = new java.security.DigestException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Failed to digest chunk #";
        r7.append(r8);
        r7.append(r15);
        r8 = " of section #";
        r7.append(r8);
        r8 = r16;
        r7.append(r8);
        r7 = r7.toString();
        r5.<init>(r7, r0);
        throw r5;
    L_0x0158:
        r21 = r6;
        r20 = r8;
        r23 = r9;
        r26 = r12;
        r8 = r16;
        r19 = 5;
        r6 = r1;
        r16 = r8 + 1;
        r5 = r5 + 1;
        r2 = r30;
        r11 = r15;
        r3 = r17;
        r15 = r18;
        r9 = r19;
        r8 = r20;
        r6 = r21;
        r10 = 1;
        goto L_0x0083;
    L_0x0179:
        r17 = r3;
        r21 = r6;
        r20 = r8;
        r26 = r12;
        r8 = r16;
        r6 = r1;
        r0 = r6.length;
        r1 = new byte[r0][];
        r0 = 0;
        r2 = r0;
    L_0x0189:
        r0 = r6.length;
        if (r2 >= r0) goto L_0x01ba;
    L_0x018c:
        r3 = r6[r2];
        r5 = r4[r2];
        r7 = getContentDigestAlgorithmJcaDigestAlgorithm(r3);
        r0 = java.security.MessageDigest.getInstance(r7);	 Catch:{ NoSuchAlgorithmException -> 0x01a2 }
        r9 = r0.digest(r5);
        r1[r2] = r9;
        r2 = r2 + 1;
        goto L_0x0189;
    L_0x01a2:
        r0 = move-exception;
        r9 = r0;
        r0 = r9;
        r9 = new java.lang.RuntimeException;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10.append(r7);
        r10.append(r14);
        r10 = r10.toString();
        r9.<init>(r10, r0);
        throw r9;
    L_0x01ba:
        return r1;
    L_0x01bb:
        r21 = r6;
        r6 = r1;
        r0 = new java.security.DigestException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Too many chunks: ";
        r1.append(r2);
        r3 = r21;
        r1.append(r3);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSigningBlockUtils.computeContentDigestsPer1MbChunk(int[], android.util.apk.DataSource[]):byte[][]");
    }

    static byte[] parseVerityDigestAndVerifySourceLength(byte[] data, long fileSize, SignatureInfo signatureInfo) throws SecurityException {
        if (data.length == 32 + 8) {
            ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            buffer.position(32);
            if (buffer.getLong() == fileSize - (signatureInfo.centralDirOffset - signatureInfo.apkSigningBlockOffset)) {
                return Arrays.copyOfRange(data, 0, 32);
            }
            throw new SecurityException("APK content size did not verify");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Verity digest size is wrong: ");
        stringBuilder.append(data.length);
        throw new SecurityException(stringBuilder.toString());
    }

    private static void verifyIntegrityForVerityBasedAlgorithm(byte[] expectedDigest, RandomAccessFile apk, SignatureInfo signatureInfo) throws SecurityException {
        try {
            if (!Arrays.equals(parseVerityDigestAndVerifySourceLength(expectedDigest, apk.length(), signatureInfo), VerityBuilder.generateApkVerityTree(apk, signatureInfo, new ByteBufferFactory() {
                public ByteBuffer create(int capacity) {
                    return ByteBuffer.allocate(capacity);
                }
            }).rootHash)) {
                throw new SecurityException("APK verity digest of contents did not verify");
            }
        } catch (IOException | DigestException | NoSuchAlgorithmException e) {
            throw new SecurityException("Error during verification", e);
        }
    }

    static Pair<ByteBuffer, Long> getEocd(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        Pair<ByteBuffer, Long> eocdAndOffsetInFile = ZipUtils.findZipEndOfCentralDirectoryRecord(apk);
        if (eocdAndOffsetInFile != null) {
            return eocdAndOffsetInFile;
        }
        throw new SignatureNotFoundException("Not an APK file: ZIP End of Central Directory record not found");
    }

    static long getCentralDirOffset(ByteBuffer eocd, long eocdOffset) throws SignatureNotFoundException {
        long centralDirOffset = ZipUtils.getZipEocdCentralDirectoryOffset(eocd);
        if (centralDirOffset > eocdOffset) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ZIP Central Directory offset out of range: ");
            stringBuilder.append(centralDirOffset);
            stringBuilder.append(". ZIP End of Central Directory offset: ");
            stringBuilder.append(eocdOffset);
            throw new SignatureNotFoundException(stringBuilder.toString());
        } else if (centralDirOffset + ZipUtils.getZipEocdCentralDirectorySizeBytes(eocd) == eocdOffset) {
            return centralDirOffset;
        } else {
            throw new SignatureNotFoundException("ZIP Central Directory is not immediately followed by End of Central Directory");
        }
    }

    private static long getChunkCount(long inputSizeBytes) {
        return ((inputSizeBytes + 1048576) - 1) / 1048576;
    }

    static int compareSignatureAlgorithm(int sigAlgorithm1, int sigAlgorithm2) {
        return compareContentDigestAlgorithm(getSignatureAlgorithmContentDigestAlgorithm(sigAlgorithm1), getSignatureAlgorithmContentDigestAlgorithm(sigAlgorithm2));
    }

    private static int compareContentDigestAlgorithm(int digestAlgorithm1, int digestAlgorithm2) {
        String str = "Unknown digestAlgorithm2: ";
        StringBuilder stringBuilder;
        if (digestAlgorithm1 != 1) {
            if (digestAlgorithm1 == 2) {
                if (digestAlgorithm2 != 1) {
                    if (digestAlgorithm2 == 2) {
                        return 0;
                    }
                    if (digestAlgorithm2 != 3) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(digestAlgorithm2);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                return 1;
            } else if (digestAlgorithm1 != 3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown digestAlgorithm1: ");
                stringBuilder.append(digestAlgorithm1);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (digestAlgorithm2 == 1) {
                return 1;
            } else {
                if (digestAlgorithm2 == 2) {
                    return -1;
                }
                if (digestAlgorithm2 == 3) {
                    return 0;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(digestAlgorithm2);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } else if (digestAlgorithm2 == 1) {
            return 0;
        } else {
            if (digestAlgorithm2 == 2 || digestAlgorithm2 == 3) {
                return -1;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(digestAlgorithm2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    static int getSignatureAlgorithmContentDigestAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm != 513) {
            if (sigAlgorithm != 514) {
                if (sigAlgorithm != 769) {
                    if (sigAlgorithm == SIGNATURE_VERITY_RSA_PKCS1_V1_5_WITH_SHA256 || sigAlgorithm == SIGNATURE_VERITY_ECDSA_WITH_SHA256 || sigAlgorithm == 1061) {
                        return 3;
                    }
                    switch (sigAlgorithm) {
                        case 257:
                        case 259:
                            break;
                        case 258:
                        case 260:
                            break;
                        default:
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Unknown signature algorithm: 0x");
                            stringBuilder.append(Long.toHexString((long) (sigAlgorithm & -1)));
                            throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
            }
            return 2;
        }
        return 1;
    }

    static String getContentDigestAlgorithmJcaDigestAlgorithm(int digestAlgorithm) {
        if (digestAlgorithm != 1) {
            if (digestAlgorithm == 2) {
                return KeyProperties.DIGEST_SHA512;
            }
            if (digestAlgorithm != 3) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown content digest algorthm: ");
                stringBuilder.append(digestAlgorithm);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return KeyProperties.DIGEST_SHA256;
    }

    private static int getContentDigestAlgorithmOutputSizeBytes(int digestAlgorithm) {
        if (digestAlgorithm != 1) {
            if (digestAlgorithm == 2) {
                return 64;
            }
            if (digestAlgorithm != 3) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown content digest algorthm: ");
                stringBuilder.append(digestAlgorithm);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return 32;
    }

    static String getSignatureAlgorithmJcaKeyAlgorithm(int sigAlgorithm) {
        if (!(sigAlgorithm == 513 || sigAlgorithm == 514)) {
            if (sigAlgorithm != 769) {
                if (sigAlgorithm != SIGNATURE_VERITY_RSA_PKCS1_V1_5_WITH_SHA256) {
                    if (sigAlgorithm != SIGNATURE_VERITY_ECDSA_WITH_SHA256) {
                        if (sigAlgorithm != 1061) {
                            switch (sigAlgorithm) {
                                case 257:
                                case 258:
                                case 259:
                                case 260:
                                    break;
                                default:
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Unknown signature algorithm: 0x");
                                    stringBuilder.append(Long.toHexString((long) (sigAlgorithm & -1)));
                                    throw new IllegalArgumentException(stringBuilder.toString());
                            }
                        }
                    }
                }
                return KeyProperties.KEY_ALGORITHM_RSA;
            }
            return "DSA";
        }
        return KeyProperties.KEY_ALGORITHM_EC;
    }

    static Pair<String, ? extends AlgorithmParameterSpec> getSignatureAlgorithmJcaSignatureAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm != 513) {
            if (sigAlgorithm == 514) {
                return Pair.create("SHA512withECDSA", null);
            }
            if (sigAlgorithm != 769) {
                if (sigAlgorithm != SIGNATURE_VERITY_RSA_PKCS1_V1_5_WITH_SHA256) {
                    if (sigAlgorithm != SIGNATURE_VERITY_ECDSA_WITH_SHA256) {
                        if (sigAlgorithm != 1061) {
                            switch (sigAlgorithm) {
                                case 257:
                                    return Pair.create("SHA256withRSA/PSS", new PSSParameterSpec(KeyProperties.DIGEST_SHA256, "MGF1", MGF1ParameterSpec.SHA256, 32, 1));
                                case 258:
                                    return Pair.create("SHA512withRSA/PSS", new PSSParameterSpec(KeyProperties.DIGEST_SHA512, "MGF1", MGF1ParameterSpec.SHA512, 64, 1));
                                case 259:
                                    break;
                                case 260:
                                    return Pair.create("SHA512withRSA", null);
                                default:
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Unknown signature algorithm: 0x");
                                    stringBuilder.append(Long.toHexString((long) (sigAlgorithm & -1)));
                                    throw new IllegalArgumentException(stringBuilder.toString());
                            }
                        }
                    }
                }
                return Pair.create("SHA256withRSA", null);
            }
            return Pair.create("SHA256withDSA", null);
        }
        return Pair.create("SHA256withECDSA", null);
    }

    static ByteBuffer sliceFromTo(ByteBuffer source, int start, int end) {
        StringBuilder stringBuilder;
        if (start < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("start: ");
            stringBuilder.append(start);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (end >= start) {
            int capacity = source.capacity();
            if (end <= source.capacity()) {
                int originalLimit = source.limit();
                int originalPosition = source.position();
                try {
                    source.position(0);
                    source.limit(end);
                    source.position(start);
                    ByteBuffer result = source.slice();
                    result.order(source.order());
                    return result;
                } finally {
                    source.position(0);
                    source.limit(originalLimit);
                    source.position(originalPosition);
                }
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("end > capacity: ");
                stringBuilder2.append(end);
                stringBuilder2.append(" > ");
                stringBuilder2.append(capacity);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("end < start: ");
            stringBuilder.append(end);
            stringBuilder.append(" < ");
            stringBuilder.append(start);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    static ByteBuffer getByteBuffer(ByteBuffer source, int size) throws BufferUnderflowException {
        if (size >= 0) {
            int originalLimit = source.limit();
            int position = source.position();
            int limit = position + size;
            if (limit < position || limit > originalLimit) {
                throw new BufferUnderflowException();
            }
            source.limit(limit);
            try {
                ByteBuffer result = source.slice();
                result.order(source.order());
                source.position(limit);
                return result;
            } finally {
                source.limit(originalLimit);
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("size: ");
            stringBuilder.append(size);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    static ByteBuffer getLengthPrefixedSlice(ByteBuffer source) throws IOException {
        if (source.remaining() >= 4) {
            int len = source.getInt();
            if (len < 0) {
                throw new IllegalArgumentException("Negative length");
            } else if (len <= source.remaining()) {
                return getByteBuffer(source, len);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Length-prefixed field longer than remaining buffer. Field length: ");
                stringBuilder.append(len);
                stringBuilder.append(", remaining: ");
                stringBuilder.append(source.remaining());
                throw new IOException(stringBuilder.toString());
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Remaining buffer too short to contain length of length-prefixed field. Remaining: ");
        stringBuilder2.append(source.remaining());
        throw new IOException(stringBuilder2.toString());
    }

    static byte[] readLengthPrefixedByteArray(ByteBuffer buf) throws IOException {
        int len = buf.getInt();
        if (len < 0) {
            throw new IOException("Negative length");
        } else if (len <= buf.remaining()) {
            byte[] result = new byte[len];
            buf.get(result);
            return result;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Underflow while reading length-prefixed value. Length: ");
            stringBuilder.append(len);
            stringBuilder.append(", available: ");
            stringBuilder.append(buf.remaining());
            throw new IOException(stringBuilder.toString());
        }
    }

    static void setUnsignedInt32LittleEndian(int value, byte[] result, int offset) {
        result[offset] = (byte) (value & 255);
        result[offset + 1] = (byte) ((value >>> 8) & 255);
        result[offset + 2] = (byte) ((value >>> 16) & 255);
        result[offset + 3] = (byte) ((value >>> 24) & 255);
    }

    static Pair<ByteBuffer, Long> findApkSigningBlock(RandomAccessFile apk, long centralDirOffset) throws IOException, SignatureNotFoundException {
        if (centralDirOffset >= 32) {
            ByteBuffer footer = ByteBuffer.allocate(24);
            footer.order(ByteOrder.LITTLE_ENDIAN);
            apk.seek(centralDirOffset - ((long) footer.capacity()));
            apk.readFully(footer.array(), footer.arrayOffset(), footer.capacity());
            if (footer.getLong(8) == APK_SIG_BLOCK_MAGIC_LO && footer.getLong(16) == APK_SIG_BLOCK_MAGIC_HI) {
                long apkSigBlockSizeInFooter = footer.getLong(0);
                if (apkSigBlockSizeInFooter < ((long) footer.capacity()) || apkSigBlockSizeInFooter > 2147483639) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("APK Signing Block size out of range: ");
                    stringBuilder.append(apkSigBlockSizeInFooter);
                    throw new SignatureNotFoundException(stringBuilder.toString());
                }
                int totalSize = (int) (8 + apkSigBlockSizeInFooter);
                long apkSigBlockOffset = centralDirOffset - ((long) totalSize);
                if (apkSigBlockOffset >= 0) {
                    ByteBuffer apkSigBlock = ByteBuffer.allocate(totalSize);
                    apkSigBlock.order(ByteOrder.LITTLE_ENDIAN);
                    apk.seek(apkSigBlockOffset);
                    apk.readFully(apkSigBlock.array(), apkSigBlock.arrayOffset(), apkSigBlock.capacity());
                    long apkSigBlockSizeInHeader = apkSigBlock.getLong(0);
                    if (apkSigBlockSizeInHeader == apkSigBlockSizeInFooter) {
                        return Pair.create(apkSigBlock, Long.valueOf(apkSigBlockOffset));
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("APK Signing Block sizes in header and footer do not match: ");
                    stringBuilder2.append(apkSigBlockSizeInHeader);
                    stringBuilder2.append(" vs ");
                    stringBuilder2.append(apkSigBlockSizeInFooter);
                    throw new SignatureNotFoundException(stringBuilder2.toString());
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("APK Signing Block offset out of range: ");
                stringBuilder3.append(apkSigBlockOffset);
                throw new SignatureNotFoundException(stringBuilder3.toString());
            }
            throw new SignatureNotFoundException("No APK Signing Block before ZIP Central Directory");
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("APK too small for APK Signing Block. ZIP Central Directory offset: ");
        stringBuilder4.append(centralDirOffset);
        throw new SignatureNotFoundException(stringBuilder4.toString());
    }

    static ByteBuffer findApkSignatureSchemeBlock(ByteBuffer apkSigningBlock, int blockId) throws SignatureNotFoundException {
        StringBuilder stringBuilder;
        checkByteOrderLittleEndian(apkSigningBlock);
        ByteBuffer pairs = sliceFromTo(apkSigningBlock, 8, apkSigningBlock.capacity() - 24);
        int entryCount = 0;
        while (pairs.hasRemaining()) {
            entryCount++;
            if (pairs.remaining() >= 8) {
                long lenLong = pairs.getLong();
                String str = " size out of range: ";
                String str2 = "APK Signing Block entry #";
                if (lenLong < 4 || lenLong > 2147483647L) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str2);
                    stringBuilder2.append(entryCount);
                    stringBuilder2.append(str);
                    stringBuilder2.append(lenLong);
                    throw new SignatureNotFoundException(stringBuilder2.toString());
                }
                int len = (int) lenLong;
                int nextEntryPos = pairs.position() + len;
                if (len > pairs.remaining()) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str2);
                    stringBuilder3.append(entryCount);
                    stringBuilder3.append(str);
                    stringBuilder3.append(len);
                    stringBuilder3.append(", available: ");
                    stringBuilder3.append(pairs.remaining());
                    throw new SignatureNotFoundException(stringBuilder3.toString());
                } else if (pairs.getInt() == blockId) {
                    return getByteBuffer(pairs, len - 4);
                } else {
                    pairs.position(nextEntryPos);
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Insufficient data to read size of APK Signing Block entry #");
                stringBuilder.append(entryCount);
                throw new SignatureNotFoundException(stringBuilder.toString());
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("No block with ID ");
        stringBuilder.append(blockId);
        stringBuilder.append(" in APK Signing Block.");
        throw new SignatureNotFoundException(stringBuilder.toString());
    }

    private static void checkByteOrderLittleEndian(ByteBuffer buffer) {
        if (buffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }
}
