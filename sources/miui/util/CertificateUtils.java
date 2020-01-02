package miui.util;

import android.content.pm.Signature;
import android.util.Slog;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.cert.Certificate;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CertificateUtils {
    private static final boolean DEBUG_JAR = false;
    private static final String TAG = CertificateUtils.class.getSimpleName();
    private static WeakReference<byte[]> sReadBuffer;

    /* JADX WARNING: Missing block: B:83:0x0178, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:118:0x025a, code skipped:
            r0 = th;
     */
    public static boolean collectCertificates(java.io.File r16, java.util.Set<android.content.pm.Signature> r17) {
        /*
        r17.clear();
        r1 = 0;
        r2 = miui.util.CertificateUtils.class;
        monitor-enter(r2);
        r3 = sReadBuffer;	 Catch:{ all -> 0x0253 }
        if (r3 == 0) goto L_0x0015;
    L_0x000b:
        r4 = 0;
        sReadBuffer = r4;	 Catch:{ all -> 0x0253 }
        r4 = r3.get();	 Catch:{ all -> 0x0253 }
        r4 = (byte[]) r4;	 Catch:{ all -> 0x0253 }
        r1 = r4;
    L_0x0015:
        if (r1 != 0) goto L_0x0022;
    L_0x0017:
        r4 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r4 = new byte[r4];	 Catch:{ all -> 0x0253 }
        r1 = r4;
        r4 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x0253 }
        r4.<init>(r1);	 Catch:{ all -> 0x0253 }
        r3 = r4;
    L_0x0022:
        monitor-exit(r2);	 Catch:{ all -> 0x0253 }
        r2 = 0;
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r5 = 21;
        r6 = 2;
        r7 = 1;
        if (r4 < r5) goto L_0x0043;
    L_0x002c:
        r4 = java.util.jar.JarFile.class;
        r5 = new java.lang.Object[r6];	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r6 = r16.getPath();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r5[r2] = r6;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r6 = java.lang.Boolean.valueOf(r7);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r5[r7] = r6;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r4 = miui.util.ReflectionUtils.newInstance(r4, r5);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r4 = (java.util.jar.JarFile) r4;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        goto L_0x0060;
    L_0x0043:
        r4 = java.util.jar.JarFile.class;
        r5 = 3;
        r5 = new java.lang.Object[r5];	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r8 = r16.getPath();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r5[r2] = r8;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r8 = java.lang.Boolean.valueOf(r7);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r5[r7] = r8;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r8 = java.lang.Boolean.valueOf(r7);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r5[r6] = r8;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r4 = miui.util.ReflectionUtils.newInstance(r4, r5);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r4 = (java.util.jar.JarFile) r4;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
    L_0x0060:
        r5 = 0;
        r6 = r4.entries();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
    L_0x0065:
        r8 = r6.hasMoreElements();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r8 == 0) goto L_0x0115;
    L_0x006b:
        r8 = r6.nextElement();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r8 = (java.util.jar.JarEntry) r8;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r9 = r8.isDirectory();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r9 == 0) goto L_0x0078;
    L_0x0077:
        goto L_0x0065;
    L_0x0078:
        r9 = r8.getName();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r10 = "META-INF/";
        r10 = r9.startsWith(r10);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r10 == 0) goto L_0x0085;
    L_0x0084:
        goto L_0x0065;
    L_0x0085:
        r10 = loadCertificates(r4, r8, r1);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r10 != 0) goto L_0x00ba;
    L_0x008b:
        r7 = TAG;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11 = new java.lang.StringBuilder;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11.<init>();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r12 = "JarFile ";
        r11.append(r12);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r12 = r16.getPath();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11.append(r12);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r12 = " has no certificates at entry ";
        r11.append(r12);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r12 = r8.getName();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11.append(r12);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r12 = "; ignoring!";
        r11.append(r12);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11 = r11.toString();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        android.util.Slog.e(r7, r11);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r4.close();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        return r2;
    L_0x00ba:
        if (r5 != 0) goto L_0x00be;
    L_0x00bc:
        r5 = r10;
        goto L_0x0113;
    L_0x00be:
        r11 = r2;
    L_0x00bf:
        r12 = r5.length;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r11 >= r12) goto L_0x0113;
    L_0x00c2:
        r12 = 0;
        r13 = r2;
    L_0x00c4:
        r14 = r10.length;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r13 >= r14) goto L_0x00da;
    L_0x00c7:
        r14 = r5[r11];	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r14 == 0) goto L_0x00d7;
    L_0x00cb:
        r14 = r5[r11];	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r15 = r10[r13];	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = r14.equals(r15);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r14 == 0) goto L_0x00d7;
    L_0x00d5:
        r12 = 1;
        goto L_0x00da;
    L_0x00d7:
        r13 = r13 + 1;
        goto L_0x00c4;
    L_0x00da:
        if (r12 == 0) goto L_0x00e4;
    L_0x00dc:
        r13 = r5.length;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = r10.length;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r13 == r14) goto L_0x00e1;
    L_0x00e0:
        goto L_0x00e4;
    L_0x00e1:
        r11 = r11 + 1;
        goto L_0x00bf;
    L_0x00e4:
        r7 = TAG;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r13 = new java.lang.StringBuilder;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r13.<init>();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = "JarFile ";
        r13.append(r14);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = r16.getPath();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r13.append(r14);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = " has mismatched certificates at entry ";
        r13.append(r14);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = r8.getName();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r13.append(r14);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r14 = "; ignoring!";
        r13.append(r14);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r13 = r13.toString();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        android.util.Slog.e(r7, r13);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r4.close();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        return r2;
    L_0x0113:
        goto L_0x0065;
    L_0x0115:
        r4.close();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r8 = miui.util.CertificateUtils.class;
        monitor-enter(r8);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        sReadBuffer = r3;	 Catch:{ all -> 0x0161 }
        monitor-exit(r8);	 Catch:{ all -> 0x0161 }
        if (r5 == 0) goto L_0x013f;
    L_0x0120:
        r8 = r5.length;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        if (r8 <= 0) goto L_0x013f;
    L_0x0123:
        r8 = r5.length;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r9 = 0;
    L_0x0125:
        if (r9 >= r8) goto L_0x013a;
    L_0x0127:
        r10 = new android.content.pm.Signature;	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11 = r5[r9];	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11 = r11.getEncoded();	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r10.<init>(r11);	 Catch:{ CertificateEncodingException -> 0x0234, IOException -> 0x0215, RuntimeException -> 0x01f6, NoSuchMethodException -> 0x01d7, InstantiationException -> 0x01b8, IllegalAccessException -> 0x0199, InvocationTargetException -> 0x017a }
        r11 = r17;
        r11.add(r10);	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r9 = r9 + 1;
        goto L_0x0125;
    L_0x013a:
        r11 = r17;
        return r7;
    L_0x013f:
        r11 = r17;
        r7 = TAG;	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r8 = new java.lang.StringBuilder;	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r8.<init>();	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r9 = "JarFile ";
        r8.append(r9);	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r9 = r16.getPath();	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r8.append(r9);	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r9 = " has no certificates; ignoring!";
        r8.append(r9);	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        r8 = r8.toString();	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        android.util.Slog.e(r7, r8);	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
        return r2;
    L_0x0161:
        r0 = move-exception;
        r11 = r17;
    L_0x0164:
        r7 = r0;
        monitor-exit(r8);	 Catch:{ all -> 0x0178 }
        throw r7;	 Catch:{ CertificateEncodingException -> 0x0175, IOException -> 0x0172, RuntimeException -> 0x016f, NoSuchMethodException -> 0x016d, InstantiationException -> 0x016b, IllegalAccessException -> 0x0169, InvocationTargetException -> 0x0167 }
    L_0x0167:
        r0 = move-exception;
        goto L_0x017d;
    L_0x0169:
        r0 = move-exception;
        goto L_0x019c;
    L_0x016b:
        r0 = move-exception;
        goto L_0x01bb;
    L_0x016d:
        r0 = move-exception;
        goto L_0x01da;
    L_0x016f:
        r0 = move-exception;
        goto L_0x01f9;
    L_0x0172:
        r0 = move-exception;
        goto L_0x0218;
    L_0x0175:
        r0 = move-exception;
        goto L_0x0237;
    L_0x0178:
        r0 = move-exception;
        goto L_0x0164;
    L_0x017a:
        r0 = move-exception;
        r11 = r17;
    L_0x017d:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x0199:
        r0 = move-exception;
        r11 = r17;
    L_0x019c:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x01b8:
        r0 = move-exception;
        r11 = r17;
    L_0x01bb:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x01d7:
        r0 = move-exception;
        r11 = r17;
    L_0x01da:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x01f6:
        r0 = move-exception;
        r11 = r17;
    L_0x01f9:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x0215:
        r0 = move-exception;
        r11 = r17;
    L_0x0218:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x0234:
        r0 = move-exception;
        r11 = r17;
    L_0x0237:
        r4 = r0;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Exception reading ";
        r6.append(r7);
        r7 = r16.getPath();
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r5, r6, r4);
        return r2;
    L_0x0253:
        r0 = move-exception;
        r11 = r17;
        r3 = r1;
        r1 = r0;
    L_0x0258:
        monitor-exit(r2);	 Catch:{ all -> 0x025a }
        throw r1;
    L_0x025a:
        r0 = move-exception;
        r1 = r0;
        goto L_0x0258;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.util.CertificateUtils.collectCertificates(java.io.File, java.util.Set):boolean");
    }

    public static int compareSignatures(Signature[] s1, Signature[] s2) {
        if (s1 == null) {
            int i;
            if (s2 == null) {
                i = 1;
            } else {
                i = -1;
            }
            return i;
        } else if (s2 == null) {
            return -2;
        } else {
            HashSet<Signature> set1 = new HashSet();
            for (Signature sig : s1) {
                set1.add(sig);
            }
            HashSet<Signature> set2 = new HashSet();
            for (Signature sig2 : s2) {
                set2.add(sig2);
            }
            if (set1.equals(set2)) {
                return 0;
            }
            return -3;
        }
    }

    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        String str;
        StringBuilder stringBuilder;
        String str2 = " in ";
        String str3 = "Exception reading ";
        Certificate[] certificateArr = null;
        try {
            InputStream is = new BufferedInputStream(jarFile.getInputStream(je));
            while (is.read(readBuffer, 0, readBuffer.length) != -1) {
            }
            is.close();
            if (je != null) {
                certificateArr = je.getCertificates();
            }
            return certificateArr;
        } catch (IOException e) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(je.getName());
            stringBuilder.append(str2);
            stringBuilder.append(jarFile.getName());
            Slog.w(str, stringBuilder.toString(), e);
            return null;
        } catch (RuntimeException e2) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(je.getName());
            stringBuilder.append(str2);
            stringBuilder.append(jarFile.getName());
            Slog.w(str, stringBuilder.toString(), e2);
            return null;
        }
    }
}
