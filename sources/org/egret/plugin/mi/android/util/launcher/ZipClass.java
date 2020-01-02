package org.egret.plugin.mi.android.util.launcher;

import android.util.Log;
import java.io.File;

public class ZipClass {
    private static final int BUFFER_SIZE = 1024;
    private static final String TAG = "ZipClass";

    public interface OnZipListener {
        void onError(String str);

        void onFileProgress(int i, int i2);

        void onProgress(int i, int i2);

        void onSuccess();
    }

    public boolean unzip(File src, File dstRoot) {
        return doUnzip(src, dstRoot, null);
    }

    public void unzip(File src, File dstRoot, OnZipListener listener) {
        if (listener == null) {
            Log.e(TAG, "listener is null");
        }
        doUnzip(src, dstRoot, listener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:117:0x01df A:{Catch:{ all -> 0x01d4 }} */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x01e9 A:{SYNTHETIC, Splitter:B:119:0x01e9} */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x01f1 A:{Catch:{ IOException -> 0x01ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x01f6 A:{Catch:{ IOException -> 0x01ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x020c A:{SYNTHETIC, Splitter:B:133:0x020c} */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0214 A:{Catch:{ IOException -> 0x0210 }} */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0219 A:{Catch:{ IOException -> 0x0210 }} */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01df A:{Catch:{ all -> 0x01d4 }} */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x01e9 A:{SYNTHETIC, Splitter:B:119:0x01e9} */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x01f1 A:{Catch:{ IOException -> 0x01ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x01f6 A:{Catch:{ IOException -> 0x01ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x020c A:{SYNTHETIC, Splitter:B:133:0x020c} */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0214 A:{Catch:{ IOException -> 0x0210 }} */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0219 A:{Catch:{ IOException -> 0x0210 }} */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01df A:{Catch:{ all -> 0x01d4 }} */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x01e9 A:{SYNTHETIC, Splitter:B:119:0x01e9} */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x01f1 A:{Catch:{ IOException -> 0x01ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x01f6 A:{Catch:{ IOException -> 0x01ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x020c A:{SYNTHETIC, Splitter:B:133:0x020c} */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0214 A:{Catch:{ IOException -> 0x0210 }} */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0219 A:{Catch:{ IOException -> 0x0210 }} */
    private boolean doUnzip(java.io.File r20, java.io.File r21, org.egret.plugin.mi.android.util.launcher.ZipClass.OnZipListener r22) {
        /*
        r19 = this;
        r1 = r20;
        r2 = r21;
        r3 = r22;
        r0 = "ZipClass";
        r4 = 0;
        if (r1 == 0) goto L_0x022c;
    L_0x000b:
        if (r2 != 0) goto L_0x000f;
    L_0x000d:
        goto L_0x022c;
    L_0x000f:
        r5 = r21.exists();
        r6 = "fail to mkdir ";
        if (r5 != 0) goto L_0x004c;
    L_0x0017:
        r5 = r21.mkdirs();
        if (r5 != 0) goto L_0x004c;
    L_0x001d:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r6);
        r7 = r21.getAbsolutePath();
        r5.append(r7);
        r5 = r5.toString();
        android.util.Log.e(r0, r5);
        if (r3 == 0) goto L_0x004b;
    L_0x0035:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r6);
        r5 = r21.getAbsolutePath();
        r0.append(r5);
        r0 = r0.toString();
        r3.onError(r0);
    L_0x004b:
        return r4;
    L_0x004c:
        r5 = 0;
        r7 = 0;
        r8 = 0;
        r9 = new java.util.zip.ZipFile;	 Catch:{ IOException -> 0x01d8 }
        r9.<init>(r1);	 Catch:{ IOException -> 0x01d8 }
        r5 = r9;
        r9 = r5.size();	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        r10 = 0;
        r11 = r5.entries();	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
    L_0x005e:
        r12 = r11.hasMoreElements();	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        if (r12 == 0) goto L_0x0185;
    L_0x0064:
        r12 = "zip thread is cancelled";
        if (r3 == 0) goto L_0x0092;
    L_0x0068:
        r13 = org.egret.plugin.mi.android.util.launcher.ExecutorLab.getInstance();	 Catch:{ IOException -> 0x01d8 }
        r13 = r13.isRunning();	 Catch:{ IOException -> 0x01d8 }
        if (r13 != 0) goto L_0x0092;
    L_0x0072:
        r3.onError(r12);	 Catch:{ IOException -> 0x01d8 }
        r5.close();	 Catch:{ IOException -> 0x0085 }
        if (r7 == 0) goto L_0x007f;
    L_0x007c:
        r7.close();	 Catch:{ IOException -> 0x0085 }
    L_0x007f:
        if (r8 == 0) goto L_0x0084;
    L_0x0081:
        r8.close();	 Catch:{ IOException -> 0x0085 }
    L_0x0084:
        goto L_0x0091;
    L_0x0085:
        r0 = move-exception;
        r0.printStackTrace();
        r6 = r0.toString();
        r3.onError(r6);
    L_0x0091:
        return r4;
    L_0x0092:
        r10 = r10 + 1;
        if (r3 == 0) goto L_0x0099;
    L_0x0096:
        r3.onProgress(r10, r9);	 Catch:{ IOException -> 0x01d8 }
    L_0x0099:
        r13 = r11.nextElement();	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        r13 = (java.util.zip.ZipEntry) r13;	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        r14 = new java.io.File;	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        r15 = r13.getName();	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        r14.<init>(r2, r15);	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        r15 = r13.isDirectory();	 Catch:{ IOException -> 0x01d1, all -> 0x01cd }
        if (r15 == 0) goto L_0x0100;
    L_0x00ae:
        r12 = r14.mkdirs();	 Catch:{ IOException -> 0x01d8 }
        if (r12 != 0) goto L_0x005e;
    L_0x00b4:
        r12 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01d8 }
        r12.<init>();	 Catch:{ IOException -> 0x01d8 }
        r12.append(r6);	 Catch:{ IOException -> 0x01d8 }
        r15 = r14.getAbsolutePath();	 Catch:{ IOException -> 0x01d8 }
        r12.append(r15);	 Catch:{ IOException -> 0x01d8 }
        r12 = r12.toString();	 Catch:{ IOException -> 0x01d8 }
        android.util.Log.e(r0, r12);	 Catch:{ IOException -> 0x01d8 }
        if (r3 == 0) goto L_0x00e2;
    L_0x00cc:
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01d8 }
        r0.<init>();	 Catch:{ IOException -> 0x01d8 }
        r0.append(r6);	 Catch:{ IOException -> 0x01d8 }
        r6 = r14.getAbsolutePath();	 Catch:{ IOException -> 0x01d8 }
        r0.append(r6);	 Catch:{ IOException -> 0x01d8 }
        r0 = r0.toString();	 Catch:{ IOException -> 0x01d8 }
        r3.onError(r0);	 Catch:{ IOException -> 0x01d8 }
        r5.close();	 Catch:{ IOException -> 0x00f2 }
        if (r7 == 0) goto L_0x00ec;
    L_0x00e9:
        r7.close();	 Catch:{ IOException -> 0x00f2 }
    L_0x00ec:
        if (r8 == 0) goto L_0x00f1;
    L_0x00ee:
        r8.close();	 Catch:{ IOException -> 0x00f2 }
    L_0x00f1:
        goto L_0x00ff;
    L_0x00f2:
        r0 = move-exception;
        r0.printStackTrace();
        if (r3 == 0) goto L_0x00ff;
    L_0x00f8:
        r6 = r0.toString();
        r3.onError(r6);
    L_0x00ff:
        return r4;
    L_0x0100:
        r16 = r5;
        r4 = r13.getSize();	 Catch:{ IOException -> 0x0180, all -> 0x017a }
        r4 = (int) r4;
        r5 = 0;
        r15 = r16;
        r16 = r15.getInputStream(r13);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r7 = r16;
        r1 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1.<init>(r14);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r8 = r1;
        r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2 = new byte[r1];	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
    L_0x011a:
        r16 = r6;
        r17 = r9;
        r6 = 0;
        r9 = r7.read(r2, r6, r1);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r18 = r9;
        r1 = -1;
        if (r9 == r1) goto L_0x0166;
    L_0x0128:
        if (r3 == 0) goto L_0x0153;
    L_0x012a:
        r1 = org.egret.plugin.mi.android.util.launcher.ExecutorLab.getInstance();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1 = r1.isRunning();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        if (r1 != 0) goto L_0x0153;
    L_0x0134:
        r3.onError(r12);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r15.close();	 Catch:{ IOException -> 0x0145 }
        r7.close();	 Catch:{ IOException -> 0x0145 }
        r8.close();	 Catch:{ IOException -> 0x0145 }
        goto L_0x0151;
    L_0x0145:
        r0 = move-exception;
        r0.printStackTrace();
        r1 = r0.toString();
        r3.onError(r1);
    L_0x0151:
        r1 = 0;
        return r1;
    L_0x0153:
        r1 = r18;
        r6 = 0;
        r8.write(r2, r6, r1);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r5 = r5 + r1;
        if (r3 == 0) goto L_0x015f;
    L_0x015c:
        r3.onFileProgress(r5, r4);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
    L_0x015f:
        r6 = r16;
        r9 = r17;
        r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        goto L_0x011a;
    L_0x0166:
        r1 = r18;
        r8.close();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r7.close();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1 = r20;
        r2 = r21;
        r5 = r15;
        r6 = r16;
        r9 = r17;
        r4 = 0;
        goto L_0x005e;
    L_0x017a:
        r0 = move-exception;
        r15 = r16;
        r1 = r0;
        goto L_0x020a;
    L_0x0180:
        r0 = move-exception;
        r15 = r16;
        r5 = r15;
        goto L_0x01d9;
    L_0x0185:
        r15 = r5;
        r17 = r9;
        r15.close();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1.<init>();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r2 = "success to unzip ";
        r1.append(r2);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r2 = r20.getAbsolutePath();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1.append(r2);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r1 = r1.toString();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        android.util.Log.i(r0, r1);	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        if (r3 == 0) goto L_0x01a8;
    L_0x01a5:
        r22.onSuccess();	 Catch:{ IOException -> 0x01ca, all -> 0x01c7 }
        r15.close();	 Catch:{ IOException -> 0x01b8 }
        if (r7 == 0) goto L_0x01b2;
    L_0x01af:
        r7.close();	 Catch:{ IOException -> 0x01b8 }
    L_0x01b2:
        if (r8 == 0) goto L_0x01b7;
    L_0x01b4:
        r8.close();	 Catch:{ IOException -> 0x01b8 }
    L_0x01b7:
        goto L_0x01c5;
    L_0x01b8:
        r0 = move-exception;
        r0.printStackTrace();
        if (r3 == 0) goto L_0x01c5;
    L_0x01be:
        r1 = r0.toString();
        r3.onError(r1);
    L_0x01c5:
        r0 = 1;
        return r0;
    L_0x01c7:
        r0 = move-exception;
        r1 = r0;
        goto L_0x020a;
    L_0x01ca:
        r0 = move-exception;
        r5 = r15;
        goto L_0x01d9;
    L_0x01cd:
        r0 = move-exception;
        r15 = r5;
        r1 = r0;
        goto L_0x020a;
    L_0x01d1:
        r0 = move-exception;
        r15 = r5;
        goto L_0x01d9;
    L_0x01d4:
        r0 = move-exception;
        r1 = r0;
        r15 = r5;
        goto L_0x020a;
    L_0x01d8:
        r0 = move-exception;
    L_0x01d9:
        r1 = r0;
        r1.printStackTrace();	 Catch:{ all -> 0x01d4 }
        if (r3 == 0) goto L_0x01e6;
    L_0x01df:
        r0 = r1.toString();	 Catch:{ all -> 0x01d4 }
        r3.onError(r0);	 Catch:{ all -> 0x01d4 }
        if (r5 == 0) goto L_0x01ef;
    L_0x01e9:
        r5.close();	 Catch:{ IOException -> 0x01ed }
        goto L_0x01ef;
    L_0x01ed:
        r0 = move-exception;
        goto L_0x01fa;
    L_0x01ef:
        if (r7 == 0) goto L_0x01f4;
    L_0x01f1:
        r7.close();	 Catch:{ IOException -> 0x01ed }
    L_0x01f4:
        if (r8 == 0) goto L_0x0207;
    L_0x01f6:
        r8.close();	 Catch:{ IOException -> 0x01ed }
        goto L_0x0207;
    L_0x01fa:
        r0.printStackTrace();
        if (r3 == 0) goto L_0x0208;
    L_0x01ff:
        r2 = r0.toString();
        r3.onError(r2);
        goto L_0x0208;
    L_0x0208:
        r2 = 0;
        return r2;
    L_0x020a:
        if (r15 == 0) goto L_0x0212;
    L_0x020c:
        r15.close();	 Catch:{ IOException -> 0x0210 }
        goto L_0x0212;
    L_0x0210:
        r0 = move-exception;
        goto L_0x021d;
    L_0x0212:
        if (r7 == 0) goto L_0x0217;
    L_0x0214:
        r7.close();	 Catch:{ IOException -> 0x0210 }
    L_0x0217:
        if (r8 == 0) goto L_0x022a;
    L_0x0219:
        r8.close();	 Catch:{ IOException -> 0x0210 }
        goto L_0x022a;
    L_0x021d:
        r0.printStackTrace();
        if (r3 == 0) goto L_0x022b;
    L_0x0222:
        r2 = r0.toString();
        r3.onError(r2);
        goto L_0x022b;
    L_0x022b:
        throw r1;
    L_0x022c:
        r1 = "src or dstRoot may be null";
        android.util.Log.e(r0, r1);
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.egret.plugin.mi.android.util.launcher.ZipClass.doUnzip(java.io.File, java.io.File, org.egret.plugin.mi.android.util.launcher.ZipClass$OnZipListener):boolean");
    }
}
