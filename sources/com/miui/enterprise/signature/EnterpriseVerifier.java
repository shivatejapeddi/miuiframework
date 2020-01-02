package com.miui.enterprise.signature;

import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Slog;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import miui.telephony.TelephonyManagerUtil;

public class EnterpriseVerifier {
    private static final String CHARSET = "UTF-8";
    private static final String ENTERPRISE_CER_ENTRY = "entcert";
    private static final String ENTERPRISE_CER_FILE = "META-INF/ENTERPRISE.CER";
    private static final String ENT_PUBLIC_KEY = "-----BEGIN CERTIFICATE-----\nMIIDXzCCAkegAwIBAgIEMgLUEDANBgkqhkiG9w0BAQsFADBgMQswCQYDVQQGEwI4\nNjEPMA0GA1UECBMGQmVpSmluMQ8wDQYDVQQHEwZCZWlKaW4xDzANBgNVBAoTBlhp\nYW9NaTENMAsGA1UECxMETUlVSTEPMA0GA1UEAxMGWGlhb01pMB4XDTE4MDIwNTEw\nNTUwM1oXDTQ4MDEyOTEwNTUwM1owYDELMAkGA1UEBhMCODYxDzANBgNVBAgTBkJl\naUppbjEPMA0GA1UEBxMGQmVpSmluMQ8wDQYDVQQKEwZYaWFvTWkxDTALBgNVBAsT\nBE1JVUkxDzANBgNVBAMTBlhpYW9NaTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC\nAQoCggEBALE5VJgm5U7lzuEdK69+PXmjw43IwkOPKcZH5ygySuf1X9NJq1YteOoo\nSAYP1sOIr3gg/FauQSlFxmM1VPCXE3uLvDC9ko/vJtpzgfqD/tM/mZQmlFVlWFNW\n9+64Ri/15tO5La1oXW2ccsp0thYmVNDEQtJw/HK5G26l2PSMdGxcgZUAv61dFhbq\n9aAm9ZtvVxUJdlw9xOBE+N+fTxnteM60cY9lPsOXdloHuSo93FF/WlS+7NfyO0h1\n3zWD54OkYsLNBnefynuay33fxFqEK/OqpAHkolIRhxhy256RzoS5tC8aWBdvqZTQ\n6GEmxg3/tkK6dM40L5pFsPV6eJeEnj8CAwEAAaMhMB8wHQYDVR0OBBYEFNNMGSE0\nbeZUEt2r7beFWvvvfBoSMA0GCSqGSIb3DQEBCwUAA4IBAQBfRf59v1HFwPtjzRGA\nYjoR4Z8QdQ8Gu7fJMlm3MOaBPiVh/rTp5/PtDAAUyDjUZFUOTngmAIDk768FDNkY\nfBTn0RCZVI96FsdcA9dhCeCadnr7I6kpHX1LeqXkkVsrviV8vnqXcBIk29qu/M0s\ncBZy1SBP3YlN5ZOQXipWCdU7hNt4QdAYfeOZ8/A/DHZUmZsbPP+gq3I1u8rV1e5q\nPmF46bmuOXe4eXmqBiWNmJyGDOpP0YiT4N57kJOM2aiNWNGXMod4/896rDXUoCuM\n5xIsDV64/DorjbF4dZta94Q6KnE6JRWFl/i4ol7218EVA3ScHLRISDruJanMpU5V\nF3nx\n-----END CERTIFICATE-----";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.AM_PM, 'b', 'c', DateFormat.DATE, 'e', 'f'};
    private static final String MIUI_SIGNATURE = "3082046c30820354a0030201020209008d64f55b5ca4ef25300d06092a864886f70d0101050500308180310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e67310f300d060355040a13065869616f6d69310d300b060355040b13044d495549310d300b060355040313044d495549311e301c06092a864886f70d010901160f6d697569407869616f6d692e636f6d301e170d3131313230363033323733305a170d3339303432333033323733305a308180310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e67310f300d060355040a13065869616f6d69310d300b060355040b13044d495549310d300b060355040313044d495549311e301c06092a864886f70d010901160f6d697569407869616f6d692e636f6d30820120300d06092a864886f70d01010105000382010d00308201080282010100cfb201f02792657970678db7ae5476d6050534be0c0f308370f0b0f8f08ed8f62a39f804ac4e9dd6262759f2545abcc7aa04fa0226d0029bacab42a970806023bd740c73fad30c56b286037eb1663e79f6c4196ed04d41c92c125d130c29d801a1db681af2d89de2d46e7af218385580186eaf7b68d789574e826cc1762190d70aac565b94cf7812d7a90b7d045f3da952a9c585cf437ced4857675c859d3808a882fec9401dc6ce05140e94c918e381223aa69f7df2ded90767505cbfb1c0a8371e9886b56e85925fafccf312aeea6a892e55fda66957f0dd245e6541e188bd0388d880478557591f16a2e2fdf01b83c6d75298f2e27bf3eb0c12ab7ccac68b020103a381e83081e5301d0603551d0e041604142438de5c93ae19065adbdcb82033744bd89d25663081b50603551d230481ad3081aa80142438de5c93ae19065adbdcb82033744bd89d2566a18186a48183308180310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e67310f300d060355040a13065869616f6d69310d300b060355040b13044d495549310d300b060355040313044d495549311e301c06092a864886f70d010901160f6d697569407869616f6d692e636f6d8209008d64f55b5ca4ef25300c0603551d13040530030101ff300d06092a864886f70d01010505000382010100ccdf3afa17e8aa4abbf30cd193853ed324ba06a18af56b6aee1b2f96f16bf426406b851d76dc4b1d2f150590e6013fc64d7440f400b59af6b225ae6bd3409b0dec19331365077612e6298dd2323ae0bdb14c774df3e9cc3502841204f248d32971fc4d4b9fb98e830f95f71f5f4b31f94f33a4ac2b1ef5eeb1ecb78a71f5333a7f003c454f4bf4e633626ce330f48df62a9396324da15e676a29d8f563408b7822abf25d2d372f91e43f4dccb17ba8a248b475b71c936755efe1387e2d0511c13f85eed4d12bae3d004ae631c963f9fbf4fc1319f0ffa7de2af9f02f00dec902eb88be20e7ce3d9d3392f338adb8d79392318e057f3110c173af20d5ed1094d6";
    private static final String TAG = "EnterpriseVerifier";
    private static final String TEMP_FILE = "/data/system/ent_temp";
    private static AtomicReference<byte[]> sBuffer = new AtomicReference();

    /* JADX WARNING: Unknown top exception splitter block from list: {B:65:0x0146=Splitter:B:65:0x0146, B:86:0x01d8=Splitter:B:86:0x01d8} */
    public static boolean verify(android.content.Context r17, java.lang.String r18, java.lang.String r19) {
        /*
        r1 = r17;
        r2 = r19;
        r0 = "enterprise_licence";
        r3 = "UTF-8";
        r4 = "/data/system/ent_temp";
        r5 = "META-INF/ENTERPRISE.CER";
        r6 = "EnterpriseVerifier";
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = new java.util.jar.JarFile;	 Catch:{ IOException | GeneralSecurityException -> 0x01f9, IOException | GeneralSecurityException -> 0x01f9, all -> 0x01f4 }
        r11 = new java.io.File;	 Catch:{ IOException | GeneralSecurityException -> 0x01f9, IOException | GeneralSecurityException -> 0x01f9, all -> 0x01f4 }
        r12 = r18;
        r11.<init>(r12);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = 1;
        r10.<init>(r11, r13);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7 = r10;
        r10 = r7.getEntry(r5);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r10 != 0) goto L_0x003d;
    L_0x0025:
        r0 = "Verify failed, no enterprise cert found";
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x0038 }
        if (r8 == 0) goto L_0x0034;
    L_0x0031:
        r8.close();	 Catch:{ IOException -> 0x0038 }
    L_0x0034:
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x0038 }
        goto L_0x003c;
    L_0x0038:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x003c:
        return r9;
    L_0x003d:
        r5 = r7.getEntry(r5);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r5 = r7.getInputStream(r5);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        writeFile(r5, r4);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r5 = new java.util.jar.JarFile;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r5.<init>(r4, r13);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r8 = r5;
        r4 = collectSignature(r8);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r4 == 0) goto L_0x01d6;
    L_0x0054:
        r5 = r4.length();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r5 != 0) goto L_0x005e;
    L_0x005a:
        r16 = r4;
        goto L_0x01d8;
    L_0x005e:
        r5 = "3082046c30820354a0030201020209008d64f55b5ca4ef25300d06092a864886f70d0101050500308180310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e67310f300d060355040a13065869616f6d69310d300b060355040b13044d495549310d300b060355040313044d495549311e301c06092a864886f70d010901160f6d697569407869616f6d692e636f6d301e170d3131313230363033323733305a170d3339303432333033323733305a308180310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e67310f300d060355040a13065869616f6d69310d300b060355040b13044d495549310d300b060355040313044d495549311e301c06092a864886f70d010901160f6d697569407869616f6d692e636f6d30820120300d06092a864886f70d01010105000382010d00308201080282010100cfb201f02792657970678db7ae5476d6050534be0c0f308370f0b0f8f08ed8f62a39f804ac4e9dd6262759f2545abcc7aa04fa0226d0029bacab42a970806023bd740c73fad30c56b286037eb1663e79f6c4196ed04d41c92c125d130c29d801a1db681af2d89de2d46e7af218385580186eaf7b68d789574e826cc1762190d70aac565b94cf7812d7a90b7d045f3da952a9c585cf437ced4857675c859d3808a882fec9401dc6ce05140e94c918e381223aa69f7df2ded90767505cbfb1c0a8371e9886b56e85925fafccf312aeea6a892e55fda66957f0dd245e6541e188bd0388d880478557591f16a2e2fdf01b83c6d75298f2e27bf3eb0c12ab7ccac68b020103a381e83081e5301d0603551d0e041604142438de5c93ae19065adbdcb82033744bd89d25663081b50603551d230481ad3081aa80142438de5c93ae19065adbdcb82033744bd89d2566a18186a48183308180310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e67310f300d060355040a13065869616f6d69310d300b060355040b13044d495549310d300b060355040313044d495549311e301c06092a864886f70d010901160f6d697569407869616f6d692e636f6d8209008d64f55b5ca4ef25300c0603551d13040530030101ff300d06092a864886f70d01010505000382010100ccdf3afa17e8aa4abbf30cd193853ed324ba06a18af56b6aee1b2f96f16bf426406b851d76dc4b1d2f150590e6013fc64d7440f400b59af6b225ae6bd3409b0dec19331365077612e6298dd2323ae0bdb14c774df3e9cc3502841204f248d32971fc4d4b9fb98e830f95f71f5f4b31f94f33a4ac2b1ef5eeb1ecb78a71f5333a7f003c454f4bf4e633626ce330f48df62a9396324da15e676a29d8f563408b7822abf25d2d372f91e43f4dccb17ba8a248b475b71c936755efe1387e2d0511c13f85eed4d12bae3d004ae631c963f9fbf4fc1319f0ffa7de2af9f02f00dec902eb88be20e7ce3d9d3392f338adb8d79392318e057f3110c173af20d5ed1094d6";
        r5 = r5.equals(r4);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r5 != 0) goto L_0x007d;
    L_0x0066:
        r0 = "Verify failed, miss match signature";
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x0078 }
        r8.close();	 Catch:{ IOException -> 0x0078 }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x0078 }
        goto L_0x007c;
    L_0x0078:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x007c:
        return r9;
    L_0x007d:
        r5 = new com.miui.enterprise.signature.EnterpriseCer;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r10 = "entcert";
        r10 = r8.getEntry(r10);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r10 = r8.getInputStream(r10);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r5.<init>(r10);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r10 = r5.signature;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r10 = android.util.Base64.decode(r10, r9);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r11 = r5.getUnSignedRaw();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r11 = r11.getBytes(r3);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r14 = readPublicKey();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r15 = getSignatureAlgorithm(r14);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r15 = java.security.Signature.getInstance(r15);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = r14.getPublicKey();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r15.initVerify(r13);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r15.update(r11);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = r15.verify(r10);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r13 == 0) goto L_0x0195;
    L_0x00b6:
        r13 = new java.lang.StringBuilder;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13.<init>();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r9 = "Verify signature success for package ";
        r13.append(r9);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13.append(r2);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r9 = r13.toString();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        android.util.Slog.d(r6, r9);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r9 = verifyDate(r5);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r9 != 0) goto L_0x00e8;
    L_0x00d0:
        r0 = "Verify failed,  cert out of date";
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x00e2 }
        r8.close();	 Catch:{ IOException -> 0x00e2 }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x00e2 }
        goto L_0x00e6;
    L_0x00e2:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x00e6:
        r3 = 0;
        return r3;
    L_0x00e8:
        r9 = verifyApk(r7, r5);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r9 != 0) goto L_0x0106;
    L_0x00ee:
        r0 = "Verify failed, miss match apk/device";
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x0100 }
        r8.close();	 Catch:{ IOException -> 0x0100 }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x0100 }
        goto L_0x0104;
    L_0x0100:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0104:
        r3 = 0;
        return r3;
    L_0x0106:
        r9 = com.miui.enterprise.settings.EnterpriseSettings.getString(r1, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = android.text.TextUtils.isEmpty(r9);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r13 == 0) goto L_0x0124;
    L_0x0110:
        r13 = "enterprise_agency";
        r16 = r4;
        r4 = r5.agencyCode;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        com.miui.enterprise.settings.EnterpriseSettings.putString(r1, r13, r4);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r4 = r5.licenceCode;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        com.miui.enterprise.settings.EnterpriseSettings.putString(r1, r0, r4);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = "enterprise_package";
        com.miui.enterprise.settings.EnterpriseSettings.putString(r1, r0, r2);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        goto L_0x0146;
    L_0x0124:
        r16 = r4;
        r0 = r5.licenceCode;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = android.text.TextUtils.equals(r9, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        if (r0 != 0) goto L_0x0146;
    L_0x012e:
        r0 = "Verify failed, miss match licence code";
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x0140 }
        r8.close();	 Catch:{ IOException -> 0x0140 }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x0140 }
        goto L_0x0144;
    L_0x0140:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0144:
        r3 = 0;
        return r3;
    L_0x0146:
        r0 = new android.content.Intent;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r4 = "com.miui.enterprise.ACTION_CERT_UPDATE";
        r0.<init>(r4);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r4 = r0;
        r0 = android.os.Build.VERSION.SDK_INT;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = 26;
        if (r0 < r13) goto L_0x0159;
    L_0x0154:
        r0 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r4.setFlags(r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
    L_0x0159:
        r0 = "extra_ent_cert";
        r4.putExtra(r0, r5);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = android.os.UserHandle.OWNER;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = "com.miui.enterprise.permission.ACTIVE_ENTERPRISE_MODE";
        r1.sendBroadcastAsUser(r4, r0, r13);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = new java.io.BufferedInputStream;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13 = new java.io.ByteArrayInputStream;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r1 = r5.toString();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r1 = r1.getBytes(r3);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r13.<init>(r1);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0.<init>(r13);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r1 = "/data/system/entcert";
        writeFile(r0, r1);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = "persist cert file";
        android.util.Slog.d(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x018f }
        r8.close();	 Catch:{ IOException -> 0x018f }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x018f }
        goto L_0x0193;
    L_0x018f:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0193:
        r1 = 1;
        return r1;
    L_0x0195:
        r16 = r4;
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0.<init>();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r1 = "Verify signature failed for package: ";
        r0.append(r1);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0.append(r2);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = r0.toString();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0.<init>();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r1 = "Cert: ";
        r0.append(r1);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r1 = r5.toString();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0.append(r1);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r0 = r0.toString();	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x01d0 }
        r8.close();	 Catch:{ IOException -> 0x01d0 }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x01d0 }
        goto L_0x01d4;
    L_0x01d0:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x01d4:
        r1 = 0;
        return r1;
    L_0x01d6:
        r16 = r4;
    L_0x01d8:
        r0 = "Verify failed, failed to load enterprise cert signature";
        android.util.Slog.e(r6, r0);	 Catch:{ IOException | GeneralSecurityException -> 0x01f2, IOException | GeneralSecurityException -> 0x01f2 }
        r7.close();	 Catch:{ IOException -> 0x01ea }
        r8.close();	 Catch:{ IOException -> 0x01ea }
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x01ea }
        goto L_0x01ee;
    L_0x01ea:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x01ee:
        r1 = 0;
        return r1;
    L_0x01f0:
        r0 = move-exception;
        goto L_0x01f7;
    L_0x01f2:
        r0 = move-exception;
        goto L_0x01fc;
    L_0x01f4:
        r0 = move-exception;
        r12 = r18;
    L_0x01f7:
        r1 = r0;
        goto L_0x0228;
    L_0x01f9:
        r0 = move-exception;
        r12 = r18;
    L_0x01fc:
        r1 = r0;
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01f0 }
        r0.<init>();	 Catch:{ all -> 0x01f0 }
        r3 = "Verify failed";
        r0.append(r3);	 Catch:{ all -> 0x01f0 }
        r0.append(r1);	 Catch:{ all -> 0x01f0 }
        r0 = r0.toString();	 Catch:{ all -> 0x01f0 }
        android.util.Slog.e(r6, r0);	 Catch:{ all -> 0x01f0 }
        if (r7 == 0) goto L_0x021a;
    L_0x0214:
        r7.close();	 Catch:{ IOException -> 0x0218 }
        goto L_0x021a;
    L_0x0218:
        r0 = move-exception;
        goto L_0x0223;
    L_0x021a:
        if (r8 == 0) goto L_0x021f;
    L_0x021c:
        r8.close();	 Catch:{ IOException -> 0x0218 }
    L_0x021f:
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x0218 }
        goto L_0x0226;
    L_0x0223:
        r0.printStackTrace();
    L_0x0226:
        r3 = 0;
        return r3;
    L_0x0228:
        if (r7 == 0) goto L_0x0230;
    L_0x022a:
        r7.close();	 Catch:{ IOException -> 0x022e }
        goto L_0x0230;
    L_0x022e:
        r0 = move-exception;
        goto L_0x0239;
    L_0x0230:
        if (r8 == 0) goto L_0x0235;
    L_0x0232:
        r8.close();	 Catch:{ IOException -> 0x022e }
    L_0x0235:
        deleteTempFileIfExist();	 Catch:{ IOException -> 0x022e }
        goto L_0x023c;
    L_0x0239:
        r0.printStackTrace();
    L_0x023c:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.enterprise.signature.EnterpriseVerifier.verify(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    public static boolean verifyPlatformSign(String path) {
        String str = "Failed to close jar";
        String str2 = TAG;
        JarFile certJar = null;
        try {
            certJar = new JarFile(path, true);
            String sign = collectSignature(certJar);
            if (sign != null) {
                if (sign.length() != 0) {
                    boolean equals = MIUI_SIGNATURE.equals(sign);
                    try {
                        certJar.close();
                    } catch (IOException e) {
                        Slog.e(str2, str);
                    }
                    return equals;
                }
            }
            Slog.e(str2, "Verify failed, failed to load enterprise cert signature");
            try {
                certJar.close();
            } catch (IOException e2) {
                Slog.e(str2, str);
            }
            return false;
        } catch (IOException | CertificateException e3) {
            Slog.e(str2, "Verify failed", e3);
            if (certJar != null) {
                try {
                    certJar.close();
                } catch (IOException e4) {
                    Slog.e(str2, str);
                }
            }
            return false;
        } catch (Throwable th) {
            if (certJar != null) {
                try {
                    certJar.close();
                } catch (IOException e5) {
                    Slog.e(str2, str);
                }
            }
        }
    }

    private static boolean verifyDate(EnterpriseCer cert) {
        Date date = Calendar.getInstance().getTime();
        return date.after(cert.getValidFrom()) && date.before(cert.getValidTo());
    }

    private static boolean verifyApk(JarFile jar, EnterpriseCer cert) throws IOException, NoSuchAlgorithmException {
        String[] strArr = cert.deviceIds;
        int i = 0;
        String str = TAG;
        if (strArr != null && cert.deviceIds.length != 0) {
            String myDeviceId = TelephonyManagerUtil.getDeviceId();
            boolean match = false;
            String[] strArr2 = cert.deviceIds;
            int length = strArr2.length;
            while (i < length) {
                if (TextUtils.equals(myDeviceId, strArr2[i])) {
                    match = true;
                    break;
                }
                i++;
            }
            if (!match) {
                Slog.e(str, "Verify failed, device not authorized");
            }
            return match;
        } else if (verifyApkHashForP(cert.apkNewHash, jar)) {
            Slog.e(str, "Verify failed, new apk hash");
            return true;
        } else if (!verifyApkHash(cert.apkHash, jar)) {
            return false;
        } else {
            Slog.e(str, "Verify failed, old apk hash");
            return true;
        }
    }

    private static boolean verifyApkHashForP(String apkHash, JarFile jar) throws IOException, NoSuchAlgorithmException {
        InputStream mfInput = null;
        try {
            mfInput = jar.getInputStream(jar.getEntry("META-INF/MANIFEST.MF"));
            StringBuilder inputBuffer = new StringBuilder();
            byte[] mfBytes = new byte[2048];
            while (true) {
                int read = mfInput.read(mfBytes, 0, 2048);
                int length = read;
                if (read != -1) {
                    inputBuffer.append(new String(mfBytes, 0, length));
                } else {
                    boolean equals = TextUtils.equals(apkHash, sha256(inputBuffer.toString().getBytes(CHARSET)));
                    mfInput.close();
                    return equals;
                }
            }
        } catch (IOException e) {
            Slog.e(TAG, "Verify failed, new apk hash");
            return false;
        } finally {
            if (mfInput != null) {
                mfInput.close();
            }
        }
    }

    private static boolean verifyApkHash(String apkHash, JarFile jar) throws IOException, NoSuchAlgorithmException {
        InputStream mfInput = null;
        try {
            mfInput = jar.getInputStream(jar.getEntry("META-INF/MANIFEST.MF"));
            byte[] mfBytes = new byte[mfInput.available()];
            if (mfInput.read(mfBytes) != -1) {
                boolean equals = TextUtils.equals(apkHash, sha256(mfBytes));
                mfInput.close();
                return equals;
            }
            throw new IOException("Failed to read META-INF/MANIFEST.MF");
        } catch (IOException e) {
            Slog.e(TAG, "Verify failed, new apk hash");
            return false;
        } finally {
            if (mfInput != null) {
                mfInput.close();
            }
        }
    }

    private static X509Certificate readPublicKey() throws IOException, GeneralSecurityException {
        String str = ENT_PUBLIC_KEY;
        X509Certificate x509Certificate = CHARSET;
        ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes(x509Certificate));
        try {
            x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(input);
            return x509Certificate;
        } finally {
            input.close();
        }
    }

    private static String getSignatureAlgorithm(X509Certificate cert) {
        String keyType = cert.getPublicKey().getAlgorithm().toUpperCase(Locale.US);
        if (KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(keyType)) {
            return "SHA256withRSA";
        }
        if (KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(keyType)) {
            return "SHA256withECDSA";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unsupported key type: ");
        stringBuilder.append(keyType);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static void writeFile(InputStream in, String path) throws IOException {
        File file = new File(path);
        if (file.exists() || file.createNewFile()) {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[8192];
            while (true) {
                int read = in.read(buffer);
                int len = read;
                if (read != -1) {
                    os.write(buffer, 0, len);
                } else {
                    os.flush();
                    os.close();
                    return;
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Filed to create temp file: ");
        stringBuilder.append(file.getCanonicalPath());
        throw new IOException(stringBuilder.toString());
    }

    private static void deleteTempFileIfExist() throws IOException {
        File certFile = new File(TEMP_FILE);
        boolean success = false;
        if (certFile.exists()) {
            success = certFile.delete();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Delete temp file ");
        stringBuilder.append(success);
        Slog.d(TAG, stringBuilder.toString());
    }

    public static long readFullyIgnoringContents(InputStream in) throws IOException {
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
                return (long) count;
            }
        }
    }

    private static String collectSignature(JarFile jarFile) throws IOException, CertificateException {
        Enumeration<JarEntry> i = jarFile.entries();
        List<ZipEntry> toVerify = new ArrayList();
        while (i.hasMoreElements()) {
            JarEntry entry = (JarEntry) i.nextElement();
            if (!entry.isDirectory()) {
                String entryName = entry.getName();
                if (!entryName.startsWith("META-INF/")) {
                    toVerify.add(jarFile.getEntry(entryName));
                }
            }
        }
        Certificate[] certificates = null;
        for (ZipEntry zipEntry : toVerify) {
            InputStream is = null;
            try {
                is = jarFile.getInputStream(zipEntry);
                readFullyIgnoringContents(is);
                if (certificates == null) {
                    certificates = jarFile.getJarEntry(zipEntry.getName()).getCertificates();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Throwable th) {
                if (is != null) {
                    is.close();
                }
            }
        }
        return (certificates == null || certificates.length == 0) ? null : toHexReadable(certificates[0].getEncoded());
    }

    private static String sha256(byte[] input) throws NoSuchAlgorithmException {
        return toHexReadable(MessageDigest.getInstance(KeyProperties.DIGEST_SHA256).digest(input));
    }

    private static String toHexReadable(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int n : bytes) {
            int n2;
            if (n2 < (byte) 0) {
                n2 += 256;
            }
            int index2 = n2 & 15;
            sb.append(HEX_DIGITS[n2 >> 4]);
            sb.append(HEX_DIGITS[index2]);
        }
        return sb.toString();
    }
}
