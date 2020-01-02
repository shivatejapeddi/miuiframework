package android.util.apk;

import android.content.pm.PackageManager;
import android.content.pm.PackageParser.PackageParserException;
import android.content.pm.PackageParser.SigningDetails;
import android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion;
import android.content.pm.Signature;
import android.os.Trace;
import android.util.BoostFramework;
import android.util.apk.ApkSignatureSchemeV3Verifier.VerifiedSigner;
import android.util.jar.StrictJarFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import libcore.io.IoUtils;

public class ApkSignatureVerifier {
    private static final int NUMBER_OF_CORES;
    private static final String TAG = "ApkSignatureVerifier";
    private static final AtomicReference<byte[]> sBuffer = new AtomicReference();
    private static boolean sIsPerfLockAcquired = false;
    private static BoostFramework sPerfBoost = null;

    public static class Result {
        public final Certificate[][] certs;
        public final int signatureSchemeVersion;
        public final Signature[] sigs;

        public Result(Certificate[][] certs, Signature[] sigs, int signingVersion) {
            this.certs = certs;
            this.sigs = sigs;
            this.signatureSchemeVersion = signingVersion;
        }
    }

    static {
        int i = 4;
        if (Runtime.getRuntime().availableProcessors() < 4) {
            i = Runtime.getRuntime().availableProcessors();
        }
        NUMBER_OF_CORES = i;
    }

    public static SigningDetails verify(String apkPath, @SignatureSchemeVersion int minSignatureSchemeVersion) throws PackageParserException {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        StringBuilder stringBuilder3;
        String str = apkPath;
        int i = minSignatureSchemeVersion;
        String str2 = "Failed to collect certificates from ";
        String str3 = " or newer for package ";
        String str4 = "No signature found in package of version ";
        if (i <= 3) {
            Trace.traceBegin(262144, "verifyV3");
            try {
                VerifiedSigner vSigner = ApkSignatureSchemeV3Verifier.verify(apkPath);
                Certificate[][] signerCerts = new Certificate[1][];
                int i2 = 0;
                signerCerts[0] = vSigner.certs;
                Signature[] signerSigs = convertToSignatures(signerCerts);
                Signature[] pastSignerSigs = null;
                if (vSigner.por != null) {
                    pastSignerSigs = new Signature[vSigner.por.certs.size()];
                    while (i2 < pastSignerSigs.length) {
                        pastSignerSigs[i2] = new Signature(((X509Certificate) vSigner.por.certs.get(i2)).getEncoded());
                        pastSignerSigs[i2].setFlags(((Integer) vSigner.por.flagsList.get(i2)).intValue());
                        i2++;
                    }
                }
                SigningDetails signingDetails = new SigningDetails(signerSigs, 3, pastSignerSigs);
                Trace.traceEnd(262144);
                return signingDetails;
            } catch (SignatureNotFoundException e) {
                if (i < 3) {
                    Trace.traceEnd(262144);
                    if (i <= 2) {
                        Trace.traceBegin(262144, "verifyV2");
                        try {
                            SigningDetails signingDetails2 = new SigningDetails(convertToSignatures(ApkSignatureSchemeV2Verifier.verify(apkPath)), 2);
                            Trace.traceEnd(262144);
                            return signingDetails2;
                        } catch (SignatureNotFoundException e2) {
                            if (i < 2) {
                                Trace.traceEnd(262144);
                                if (i <= 1) {
                                    return verifyV1Signature(str, true);
                                }
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str4);
                                stringBuilder.append(i);
                                stringBuilder.append(str3);
                                stringBuilder.append(str);
                                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder.toString());
                            }
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("No APK Signature Scheme v2 signature in package ");
                            stringBuilder2.append(str);
                            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder2.toString(), e2);
                        } catch (Exception e3) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(str2);
                            stringBuilder3.append(str);
                            stringBuilder3.append(" using APK Signature Scheme v2");
                            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder3.toString(), e3);
                        } catch (Throwable th) {
                            Trace.traceEnd(262144);
                        }
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(i);
                        stringBuilder.append(str3);
                        stringBuilder.append(str);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder.toString());
                    }
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("No APK Signature Scheme v3 signature in package ");
                stringBuilder2.append(str);
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder2.toString(), e2);
            } catch (Exception e32) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append(str);
                stringBuilder3.append(" using APK Signature Scheme v3");
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder3.toString(), e32);
            } catch (Throwable th2) {
                Trace.traceEnd(262144);
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str4);
            stringBuilder.append(i);
            stringBuilder.append(str3);
            stringBuilder.append(str);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:96:0x0203 A:{Splitter:B:5:0x0018, ExcHandler: IOException | RuntimeException (r0_3 'e' java.lang.Throwable)} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:48:0x010e, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            throw r0;
     */
    /* JADX WARNING: Missing block: B:96:0x0203, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:97:0x0204, code skipped:
            r1 = r0;
     */
    /* JADX WARNING: Missing block: B:99:?, code skipped:
            r4 = new java.lang.StringBuilder();
            r4.append("Failed to collect certificates from ");
            r4.append(r8);
     */
    /* JADX WARNING: Missing block: B:100:0x021d, code skipped:
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, r4.toString(), r1);
     */
    /* JADX WARNING: Missing block: B:101:0x021e, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:102:0x021f, code skipped:
            r1 = r0;
            r5 = new java.lang.StringBuilder();
            r5.append("Failed to collect certificates from ");
            r5.append(r8);
     */
    /* JADX WARNING: Missing block: B:103:0x0238, code skipped:
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, r5.toString(), r1);
     */
    private static android.content.pm.PackageParser.SigningDetails verifyV1Signature(java.lang.String r26, boolean r27) throws android.content.pm.PackageParser.PackageParserException {
        /*
        r8 = r26;
        r9 = r27;
        r10 = 1;
        if (r9 == 0) goto L_0x000a;
    L_0x0007:
        r1 = NUMBER_OF_CORES;
        goto L_0x000b;
    L_0x000a:
        r1 = r10;
    L_0x000b:
        r11 = r1;
        r12 = new android.util.jar.StrictJarFile[r11];
        r3 = new android.util.ArrayMap;
        r3.<init>();
        r14 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r16 = 0;
        r1 = "strictJarFileCtor";
        android.os.Trace.traceBegin(r14, r1);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = sPerfBoost;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 != 0) goto L_0x0029;
    L_0x0022:
        r1 = new android.util.BoostFramework;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        sPerfBoost = r1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x0029:
        r1 = sPerfBoost;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 == 0) goto L_0x0048;
    L_0x002d:
        r1 = sIsPerfLockAcquired;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 != 0) goto L_0x0048;
    L_0x0031:
        if (r9 == 0) goto L_0x0048;
    L_0x0033:
        r1 = sPerfBoost;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = 4232; // 0x1088 float:5.93E-42 double:2.091E-320;
        r4 = 0;
        r5 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r6 = -1;
        r1.perfHint(r2, r4, r5, r6);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = "ApkSignatureVerifier";
        r2 = "Perflock acquired for PackageInstall ";
        android.util.Slog.d(r1, r2);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        sIsPerfLockAcquired = r10;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x0048:
        r1 = 0;
    L_0x0049:
        if (r1 >= r11) goto L_0x0055;
    L_0x004b:
        r2 = new android.util.jar.StrictJarFile;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2.<init>(r8, r10, r9);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r12[r1] = r2;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = r1 + 1;
        goto L_0x0049;
    L_0x0055:
        r1 = new java.util.ArrayList;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r7 = r1;
        r1 = r12[r16];	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = "AndroidManifest.xml";
        r1 = r1.findEntry(r2);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r6 = r1;
        if (r6 == 0) goto L_0x01de;
    L_0x0066:
        r1 = r12[r16];	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = loadCertificates(r1, r6);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r17 = r1;
        r1 = com.android.internal.util.ArrayUtils.isEmpty(r17);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 != 0) goto L_0x01b7;
    L_0x0074:
        r1 = convertToSignatures(r17);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = r1;
        if (r9 == 0) goto L_0x0184;
    L_0x007b:
        r1 = r12[r16];	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = r1.iterator();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r18 = r1;
    L_0x0083:
        r1 = r18.hasNext();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 == 0) goto L_0x00b1;
    L_0x0089:
        r1 = r18.next();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = (java.util.zip.ZipEntry) r1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = r1.isDirectory();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r2 == 0) goto L_0x0096;
    L_0x0095:
        goto L_0x0083;
    L_0x0096:
        r2 = r1.getName();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5 = "META-INF/";
        r5 = r2.startsWith(r5);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r5 == 0) goto L_0x00a3;
    L_0x00a2:
        goto L_0x0083;
    L_0x00a3:
        r5 = "AndroidManifest.xml";
        r5 = r2.equals(r5);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r5 == 0) goto L_0x00ac;
    L_0x00ab:
        goto L_0x0083;
    L_0x00ac:
        r7.add(r1);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        goto L_0x0083;
    L_0x00b1:
        r1 = new android.util.apk.ApkSignatureVerifier$1VerificationData;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = r1;
        r1 = new java.lang.Object;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2.objWaitAll = r1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = new java.util.concurrent.ThreadPoolExecutor;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r20 = NUMBER_OF_CORES;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r21 = NUMBER_OF_CORES;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r22 = 1;
        r24 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r25 = new java.util.concurrent.LinkedBlockingQueue;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r25.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r19 = r1;
        r19.<init>(r20, r21, r22, r24, r25);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r19 = r7.iterator();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x00d6:
        r5 = r19.hasNext();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r5 == 0) goto L_0x0112;
    L_0x00dc:
        r5 = r19.next();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5 = (java.util.zip.ZipEntry) r5;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r20 = new android.util.apk.ApkSignatureVerifier$1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r13 = r1;
        r1 = r20;
        r22 = r2;
        r23 = r4;
        r4 = r12;
        r24 = r6;
        r6 = r26;
        r25 = r7;
        r7 = r23;
        r1.<init>(r2, r3, r4, r5, r6, r7);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1 = r20;
        r2 = r22;
        r4 = r2.objWaitAll;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        monitor-enter(r4);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r6 = r2.exceptionFlag;	 Catch:{ all -> 0x010e }
        if (r6 != 0) goto L_0x0105;
    L_0x0102:
        r13.execute(r1);	 Catch:{ all -> 0x010e }
    L_0x0105:
        monitor-exit(r4);	 Catch:{ all -> 0x010e }
        r1 = r13;
        r4 = r23;
        r6 = r24;
        r7 = r25;
        goto L_0x00d6;
    L_0x010e:
        r0 = move-exception;
        r6 = r0;
        monitor-exit(r4);	 Catch:{ all -> 0x010e }
        throw r6;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x0112:
        r13 = r1;
        r23 = r4;
        r24 = r6;
        r25 = r7;
        r2.wait = r10;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r13.shutdown();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x011e:
        r1 = r2.wait;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 == 0) goto L_0x0164;
    L_0x0122:
        r1 = r2.exceptionFlag;	 Catch:{ InterruptedException -> 0x0159 }
        if (r1 == 0) goto L_0x0148;
    L_0x0126:
        r1 = r2.shutDown;	 Catch:{ InterruptedException -> 0x0159 }
        if (r1 != 0) goto L_0x0148;
    L_0x012a:
        r1 = "ApkSignatureVerifier";
        r4 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x0159 }
        r4.<init>();	 Catch:{ InterruptedException -> 0x0159 }
        r5 = "verifyV1 Exception ";
        r4.append(r5);	 Catch:{ InterruptedException -> 0x0159 }
        r5 = r2.exceptionFlag;	 Catch:{ InterruptedException -> 0x0159 }
        r4.append(r5);	 Catch:{ InterruptedException -> 0x0159 }
        r4 = r4.toString();	 Catch:{ InterruptedException -> 0x0159 }
        android.util.Slog.w(r1, r4);	 Catch:{ InterruptedException -> 0x0159 }
        r13.shutdownNow();	 Catch:{ InterruptedException -> 0x0159 }
        r2.shutDown = r10;	 Catch:{ InterruptedException -> 0x0159 }
    L_0x0148:
        r4 = 50;
        r1 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0159 }
        r1 = r13.awaitTermination(r4, r1);	 Catch:{ InterruptedException -> 0x0159 }
        if (r1 != 0) goto L_0x0154;
    L_0x0152:
        r1 = r10;
        goto L_0x0156;
    L_0x0154:
        r1 = r16;
    L_0x0156:
        r2.wait = r1;	 Catch:{ InterruptedException -> 0x0159 }
        goto L_0x011e;
    L_0x0159:
        r0 = move-exception;
        r1 = r0;
        r4 = "ApkSignatureVerifier";
        r5 = "VerifyV1 interrupted while awaiting all threads done...";
        android.util.Slog.w(r4, r5);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        goto L_0x011e;
    L_0x0164:
        r1 = r2.exceptionFlag;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        if (r1 != 0) goto L_0x0169;
    L_0x0168:
        goto L_0x018a;
    L_0x0169:
        r1 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = r2.exceptionFlag;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5 = new java.lang.StringBuilder;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r6 = "Failed to collect certificates from ";
        r5.append(r6);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5.append(r8);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5 = r5.toString();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r6 = r2.exception;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1.<init>(r4, r5, r6);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        throw r1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x0184:
        r23 = r4;
        r24 = r6;
        r25 = r7;
    L_0x018a:
        r1 = new android.content.pm.PackageParser$SigningDetails;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = r23;
        r1.<init>(r2, r10);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = sIsPerfLockAcquired;
        if (r4 == 0) goto L_0x01a5;
    L_0x0195:
        r4 = sPerfBoost;
        if (r4 == 0) goto L_0x01a5;
    L_0x0199:
        r4.perfLockRelease();
        sIsPerfLockAcquired = r16;
        r4 = "ApkSignatureVerifier";
        r5 = "Perflock released for PackageInstall ";
        android.util.Slog.d(r4, r5);
    L_0x01a5:
        r3.clear();
        android.os.Trace.traceEnd(r14);
        r4 = 0;
    L_0x01ac:
        if (r4 >= r11) goto L_0x01b6;
    L_0x01ae:
        r5 = r12[r4];
        closeQuietly(r5);
        r4 = r4 + 1;
        goto L_0x01ac;
    L_0x01b6:
        return r1;
    L_0x01b7:
        r24 = r6;
        r25 = r7;
        r1 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = new java.lang.StringBuilder;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = "Package ";
        r2.append(r4);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2.append(r8);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = " has no certificates at entry ";
        r2.append(r4);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = "AndroidManifest.xml";
        r2.append(r4);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = r2.toString();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = -103; // 0xffffffffffffff99 float:NaN double:NaN;
        r1.<init>(r4, r2);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        throw r1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x01de:
        r24 = r6;
        r25 = r7;
        r1 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r2 = -101; // 0xffffffffffffff9b float:NaN double:NaN;
        r4 = new java.lang.StringBuilder;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4.<init>();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5 = "Package ";
        r4.append(r5);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4.append(r8);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r5 = " has no manifest";
        r4.append(r5);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r4 = r4.toString();	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        r1.<init>(r2, r4);	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
        throw r1;	 Catch:{ GeneralSecurityException -> 0x021e, IOException | RuntimeException -> 0x0203, IOException | RuntimeException -> 0x0203 }
    L_0x0200:
        r0 = move-exception;
        r1 = r0;
        goto L_0x0239;
    L_0x0203:
        r0 = move-exception;
        r1 = r0;
        r2 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ all -> 0x0200 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0200 }
        r4.<init>();	 Catch:{ all -> 0x0200 }
        r5 = "Failed to collect certificates from ";
        r4.append(r5);	 Catch:{ all -> 0x0200 }
        r4.append(r8);	 Catch:{ all -> 0x0200 }
        r4 = r4.toString();	 Catch:{ all -> 0x0200 }
        r5 = -103; // 0xffffffffffffff99 float:NaN double:NaN;
        r2.<init>(r5, r4, r1);	 Catch:{ all -> 0x0200 }
        throw r2;	 Catch:{ all -> 0x0200 }
    L_0x021e:
        r0 = move-exception;
        r1 = r0;
        r2 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ all -> 0x0200 }
        r4 = -105; // 0xffffffffffffff97 float:NaN double:NaN;
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0200 }
        r5.<init>();	 Catch:{ all -> 0x0200 }
        r6 = "Failed to collect certificates from ";
        r5.append(r6);	 Catch:{ all -> 0x0200 }
        r5.append(r8);	 Catch:{ all -> 0x0200 }
        r5 = r5.toString();	 Catch:{ all -> 0x0200 }
        r2.<init>(r4, r5, r1);	 Catch:{ all -> 0x0200 }
        throw r2;	 Catch:{ all -> 0x0200 }
    L_0x0239:
        r2 = sIsPerfLockAcquired;
        if (r2 == 0) goto L_0x024d;
    L_0x023d:
        r2 = sPerfBoost;
        if (r2 == 0) goto L_0x024d;
    L_0x0241:
        r2.perfLockRelease();
        sIsPerfLockAcquired = r16;
        r2 = "ApkSignatureVerifier";
        r4 = "Perflock released for PackageInstall ";
        android.util.Slog.d(r2, r4);
    L_0x024d:
        r3.clear();
        android.os.Trace.traceEnd(r14);
        r2 = 0;
    L_0x0254:
        if (r2 >= r11) goto L_0x025e;
    L_0x0256:
        r4 = r12[r2];
        closeQuietly(r4);
        r2 = r2 + 1;
        goto L_0x0254;
    L_0x025e:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureVerifier.verifyV1Signature(java.lang.String, boolean):android.content.pm.PackageParser$SigningDetails");
    }

    private static Certificate[][] loadCertificates(StrictJarFile jarFile, ZipEntry entry) throws PackageParserException {
        InputStream is = null;
        try {
            is = jarFile.getInputStream(entry);
            readFullyIgnoringContents(is);
            Certificate[][] certificateChains = jarFile.getCertificateChains(entry);
            IoUtils.closeQuietly(is);
            return certificateChains;
        } catch (IOException | RuntimeException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed reading ");
            stringBuilder.append(entry.getName());
            stringBuilder.append(" in ");
            stringBuilder.append(jarFile);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e);
        } catch (Throwable th) {
            IoUtils.closeQuietly(is);
        }
    }

    private static void readFullyIgnoringContents(InputStream in) throws IOException {
        byte[] buffer = (byte[]) sBuffer.getAndSet(null);
        if (buffer == null) {
            buffer = new byte[4096];
        }
        int count = 0;
        while (true) {
            int read = in.read(buffer, 0, buffer.length);
            int n = read;
            if (read != -1) {
                count += n;
            } else {
                sBuffer.set(buffer);
                return;
            }
        }
    }

    public static Signature[] convertToSignatures(Certificate[][] certs) throws CertificateEncodingException {
        Signature[] res = new Signature[certs.length];
        for (int i = 0; i < certs.length; i++) {
            res[i] = new Signature(certs[i]);
        }
        return res;
    }

    private static void closeQuietly(StrictJarFile jarFile) {
        if (jarFile != null) {
            try {
                jarFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static SigningDetails unsafeGetCertsWithoutVerification(String apkPath, int minSignatureSchemeVersion) throws PackageParserException {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        StringBuilder stringBuilder3;
        String str = apkPath;
        int i = minSignatureSchemeVersion;
        String str2 = "Failed to collect certificates from ";
        String str3 = " or newer for package ";
        String str4 = "No signature found in package of version ";
        if (i <= 3) {
            Trace.traceBegin(262144, "certsOnlyV3");
            try {
                VerifiedSigner vSigner = ApkSignatureSchemeV3Verifier.unsafeGetCertsWithoutVerification(apkPath);
                Signature[] signerSigs = convertToSignatures(new Certificate[][]{vSigner.certs});
                Signature[] pastSignerSigs = null;
                if (vSigner.por != null) {
                    pastSignerSigs = new Signature[vSigner.por.certs.size()];
                    for (int i2 = 0; i2 < pastSignerSigs.length; i2++) {
                        pastSignerSigs[i2] = new Signature(((X509Certificate) vSigner.por.certs.get(i2)).getEncoded());
                        pastSignerSigs[i2].setFlags(((Integer) vSigner.por.flagsList.get(i2)).intValue());
                    }
                }
                SigningDetails signingDetails = new SigningDetails(signerSigs, 3, pastSignerSigs);
                Trace.traceEnd(262144);
                return signingDetails;
            } catch (SignatureNotFoundException e) {
                if (i < 3) {
                    Trace.traceEnd(262144);
                    if (i <= 2) {
                        Trace.traceBegin(262144, "certsOnlyV2");
                        try {
                            SigningDetails signingDetails2 = new SigningDetails(convertToSignatures(ApkSignatureSchemeV2Verifier.unsafeGetCertsWithoutVerification(apkPath)), 2);
                            Trace.traceEnd(262144);
                            return signingDetails2;
                        } catch (SignatureNotFoundException e2) {
                            if (i < 2) {
                                Trace.traceEnd(262144);
                                if (i <= 1) {
                                    return verifyV1Signature(str, false);
                                }
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str4);
                                stringBuilder.append(i);
                                stringBuilder.append(str3);
                                stringBuilder.append(str);
                                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder.toString());
                            }
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("No APK Signature Scheme v2 signature in package ");
                            stringBuilder2.append(str);
                            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder2.toString(), e2);
                        } catch (Exception e3) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(str2);
                            stringBuilder3.append(str);
                            stringBuilder3.append(" using APK Signature Scheme v2");
                            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder3.toString(), e3);
                        } catch (Throwable th) {
                            Trace.traceEnd(262144);
                        }
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(i);
                        stringBuilder.append(str3);
                        stringBuilder.append(str);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder.toString());
                    }
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("No APK Signature Scheme v3 signature in package ");
                stringBuilder2.append(str);
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder2.toString(), e2);
            } catch (Exception e32) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append(str);
                stringBuilder3.append(" using APK Signature Scheme v3");
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder3.toString(), e32);
            } catch (Throwable th2) {
                Trace.traceEnd(262144);
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str4);
            stringBuilder.append(i);
            stringBuilder.append(str3);
            stringBuilder.append(str);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, stringBuilder.toString());
        }
    }

    public static byte[] getVerityRootHash(String apkPath) throws IOException, SecurityException {
        try {
            return ApkSignatureSchemeV3Verifier.getVerityRootHash(apkPath);
        } catch (SignatureNotFoundException e) {
            try {
                return ApkSignatureSchemeV2Verifier.getVerityRootHash(apkPath);
            } catch (SignatureNotFoundException e2) {
                return null;
            }
        }
    }

    public static byte[] generateApkVerity(String apkPath, ByteBufferFactory bufferFactory) throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException {
        try {
            return ApkSignatureSchemeV3Verifier.generateApkVerity(apkPath, bufferFactory);
        } catch (SignatureNotFoundException e) {
            return ApkSignatureSchemeV2Verifier.generateApkVerity(apkPath, bufferFactory);
        }
    }

    public static byte[] generateApkVerityRootHash(String apkPath) throws NoSuchAlgorithmException, DigestException, IOException {
        try {
            return ApkSignatureSchemeV3Verifier.generateApkVerityRootHash(apkPath);
        } catch (SignatureNotFoundException e) {
            try {
                return ApkSignatureSchemeV2Verifier.generateApkVerityRootHash(apkPath);
            } catch (SignatureNotFoundException e2) {
                return null;
            }
        }
    }
}
