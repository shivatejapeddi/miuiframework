package org.egret.plugin.mi.android.util.launcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetClass {
    private static final int BUFFER_SIZE = 1024;
    private static final int TIME_OUT = 30000;

    public interface OnNetListener {
        void onError(String str);

        void onProgress(int i, int i2);

        void onSuccess(String str);
    }

    /* JADX WARNING: Missing block: B:73:?, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:74:0x00f2, code skipped:
            if (r3 == null) goto L_0x00f7;
     */
    /* JADX WARNING: Missing block: B:75:0x00f4, code skipped:
            r3.close();
     */
    /* JADX WARNING: Missing block: B:76:0x00f7, code skipped:
            r4.close();
     */
    /* JADX WARNING: Missing block: B:77:0x00fc, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:78:0x00fd, code skipped:
            r5.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:79:0x0100, code skipped:
            if (r14 == null) goto L_0x012e;
     */
    /* JADX WARNING: Missing block: B:80:0x0102, code skipped:
            r14.onError(r0);
     */
    /* JADX WARNING: Missing block: B:97:0x012b, code skipped:
            if (r14 == null) goto L_0x012e;
     */
    private void doRequest(java.lang.String r11, java.lang.String r12, java.io.OutputStream r13, org.egret.plugin.mi.android.util.launcher.NetClass.OnNetListener r14) {
        /*
        r10 = this;
        r0 = "fail to close";
        if (r11 == 0) goto L_0x015b;
    L_0x0004:
        if (r13 != 0) goto L_0x0008;
    L_0x0006:
        goto L_0x015b;
    L_0x0008:
        r1 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x014d }
        r1.<init>(r11);	 Catch:{ MalformedURLException -> 0x014d }
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = r1.openConnection();	 Catch:{ IOException -> 0x0108 }
        r5 = (java.net.HttpURLConnection) r5;	 Catch:{ IOException -> 0x0108 }
        r2 = r5;
        if (r2 != 0) goto L_0x004e;
    L_0x001a:
        if (r14 == 0) goto L_0x0030;
    L_0x001c:
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0108 }
        r5.<init>();	 Catch:{ IOException -> 0x0108 }
        r6 = "unable to open ";
        r5.append(r6);	 Catch:{ IOException -> 0x0108 }
        r5.append(r11);	 Catch:{ IOException -> 0x0108 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0108 }
        r14.onError(r5);	 Catch:{ IOException -> 0x0108 }
    L_0x0030:
        if (r2 == 0) goto L_0x0038;
    L_0x0032:
        r2.disconnect();	 Catch:{ IOException -> 0x0036 }
        goto L_0x0038;
    L_0x0036:
        r5 = move-exception;
        goto L_0x0043;
    L_0x0038:
        if (r3 == 0) goto L_0x003d;
    L_0x003a:
        r3.close();	 Catch:{ IOException -> 0x0036 }
    L_0x003d:
        if (r4 == 0) goto L_0x004c;
    L_0x003f:
        r4.close();	 Catch:{ IOException -> 0x0036 }
        goto L_0x004c;
    L_0x0043:
        r5.printStackTrace();
        if (r14 == 0) goto L_0x004d;
    L_0x0048:
        r14.onError(r0);
        goto L_0x004d;
    L_0x004d:
        return;
    L_0x004e:
        r5 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r2.setConnectTimeout(r5);	 Catch:{ IOException -> 0x0108 }
        if (r12 == 0) goto L_0x0099;
    L_0x0055:
        r5 = 1;
        r2.setDoOutput(r5);	 Catch:{ IOException -> 0x0108 }
        r5 = r2.getOutputStream();	 Catch:{ IOException -> 0x0108 }
        r3 = r5;
        if (r3 != 0) goto L_0x008f;
    L_0x0060:
        if (r14 == 0) goto L_0x0076;
    L_0x0062:
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0108 }
        r5.<init>();	 Catch:{ IOException -> 0x0108 }
        r6 = "unable to open post: ";
        r5.append(r6);	 Catch:{ IOException -> 0x0108 }
        r5.append(r11);	 Catch:{ IOException -> 0x0108 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0108 }
        r14.onError(r5);	 Catch:{ IOException -> 0x0108 }
        r2.disconnect();	 Catch:{ IOException -> 0x0085 }
        if (r3 == 0) goto L_0x007f;
    L_0x007c:
        r3.close();	 Catch:{ IOException -> 0x0085 }
    L_0x007f:
        if (r4 == 0) goto L_0x0084;
    L_0x0081:
        r4.close();	 Catch:{ IOException -> 0x0085 }
    L_0x0084:
        goto L_0x008e;
    L_0x0085:
        r5 = move-exception;
        r5.printStackTrace();
        if (r14 == 0) goto L_0x008e;
    L_0x008b:
        r14.onError(r0);
    L_0x008e:
        return;
    L_0x008f:
        r5 = r12.getBytes();	 Catch:{ IOException -> 0x0108 }
        r3.write(r5);	 Catch:{ IOException -> 0x0108 }
        r3.close();	 Catch:{ IOException -> 0x0108 }
    L_0x0099:
        r5 = r2.getResponseCode();	 Catch:{ IOException -> 0x0108 }
        r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r5 == r6) goto L_0x00c1;
    L_0x00a1:
        if (r14 == 0) goto L_0x00a8;
    L_0x00a3:
        r5 = "response code is HTTP_OK";
        r14.onError(r5);	 Catch:{ IOException -> 0x0108 }
        r2.disconnect();	 Catch:{ IOException -> 0x00b7 }
        if (r3 == 0) goto L_0x00b1;
    L_0x00ae:
        r3.close();	 Catch:{ IOException -> 0x00b7 }
    L_0x00b1:
        if (r4 == 0) goto L_0x00b6;
    L_0x00b3:
        r4.close();	 Catch:{ IOException -> 0x00b7 }
    L_0x00b6:
        goto L_0x00c0;
    L_0x00b7:
        r5 = move-exception;
        r5.printStackTrace();
        if (r14 == 0) goto L_0x00c0;
    L_0x00bd:
        r14.onError(r0);
    L_0x00c0:
        return;
    L_0x00c1:
        r5 = r2.getContentLength();	 Catch:{ IOException -> 0x0108 }
        r6 = 0;
        r7 = r2.getInputStream();	 Catch:{ IOException -> 0x0108 }
        r4 = r7;
        r7 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r7 = new byte[r7];	 Catch:{ IOException -> 0x0108 }
    L_0x00cf:
        r8 = r4.read(r7);	 Catch:{ IOException -> 0x0108 }
        r9 = r8;
        if (r8 <= 0) goto L_0x00ee;
    L_0x00d6:
        if (r14 == 0) goto L_0x00e3;
    L_0x00d8:
        r8 = org.egret.plugin.mi.android.util.launcher.ExecutorLab.getInstance();	 Catch:{ IOException -> 0x0108 }
        r8 = r8.isRunning();	 Catch:{ IOException -> 0x0108 }
        if (r8 != 0) goto L_0x00e3;
    L_0x00e2:
        goto L_0x00ee;
    L_0x00e3:
        r8 = 0;
        r13.write(r7, r8, r9);	 Catch:{ IOException -> 0x0108 }
        r6 = r6 + r9;
        if (r14 == 0) goto L_0x00cf;
    L_0x00ea:
        r14.onProgress(r6, r5);	 Catch:{ IOException -> 0x0108 }
        goto L_0x00cf;
        r2.disconnect();	 Catch:{ IOException -> 0x00fc }
        if (r3 == 0) goto L_0x00f7;
    L_0x00f4:
        r3.close();	 Catch:{ IOException -> 0x00fc }
        r4.close();	 Catch:{ IOException -> 0x00fc }
    L_0x00fb:
        goto L_0x012e;
    L_0x00fc:
        r5 = move-exception;
        r5.printStackTrace();
        if (r14 == 0) goto L_0x0105;
    L_0x0102:
        r14.onError(r0);
    L_0x0105:
        goto L_0x012e;
    L_0x0106:
        r5 = move-exception;
        goto L_0x012f;
    L_0x0108:
        r5 = move-exception;
        r5.printStackTrace();	 Catch:{ all -> 0x0106 }
        if (r14 == 0) goto L_0x0115;
    L_0x010e:
        r6 = r5.toString();	 Catch:{ all -> 0x0106 }
        r14.onError(r6);	 Catch:{ all -> 0x0106 }
    L_0x0115:
        if (r2 == 0) goto L_0x011d;
    L_0x0117:
        r2.disconnect();	 Catch:{ IOException -> 0x011b }
        goto L_0x011d;
    L_0x011b:
        r5 = move-exception;
        goto L_0x0128;
    L_0x011d:
        if (r3 == 0) goto L_0x0122;
    L_0x011f:
        r3.close();	 Catch:{ IOException -> 0x011b }
    L_0x0122:
        if (r4 == 0) goto L_0x00fb;
    L_0x0124:
        r4.close();	 Catch:{ IOException -> 0x011b }
        goto L_0x00fb;
    L_0x0128:
        r5.printStackTrace();
        if (r14 == 0) goto L_0x0105;
    L_0x012d:
        goto L_0x0102;
    L_0x012e:
        return;
    L_0x012f:
        if (r2 == 0) goto L_0x0137;
    L_0x0131:
        r2.disconnect();	 Catch:{ IOException -> 0x0135 }
        goto L_0x0137;
    L_0x0135:
        r6 = move-exception;
        goto L_0x0142;
    L_0x0137:
        if (r3 == 0) goto L_0x013c;
    L_0x0139:
        r3.close();	 Catch:{ IOException -> 0x0135 }
    L_0x013c:
        if (r4 == 0) goto L_0x014b;
    L_0x013e:
        r4.close();	 Catch:{ IOException -> 0x0135 }
        goto L_0x014b;
    L_0x0142:
        r6.printStackTrace();
        if (r14 == 0) goto L_0x014c;
    L_0x0147:
        r14.onError(r0);
        goto L_0x014c;
    L_0x014c:
        throw r5;
    L_0x014d:
        r0 = move-exception;
        r0.printStackTrace();
        if (r14 == 0) goto L_0x015a;
    L_0x0153:
        r1 = r0.toString();
        r14.onError(r1);
    L_0x015a:
        return;
    L_0x015b:
        r0 = "NetTool";
        r1 = "url, out may be null";
        android.util.Log.e(r0, r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.egret.plugin.mi.android.util.launcher.NetClass.doRequest(java.lang.String, java.lang.String, java.io.OutputStream, org.egret.plugin.mi.android.util.launcher.NetClass$OnNetListener):void");
    }

    private void request(String url, String data, File dst, OnNetListener listener) {
        String str = "net thread is cancelled";
        if (dst != null) {
            try {
                FileOutputStream out = new FileOutputStream(dst);
                doRequest(url, null, out, listener);
                out.close();
                if (listener != null) {
                    if (ExecutorLab.getInstance().isRunning()) {
                        listener.onSuccess(null);
                    } else {
                        listener.onError(str);
                    }
                }
                return;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                if (listener != null) {
                    listener.onError(ioe.toString());
                    return;
                }
                return;
            }
        }
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        doRequest(url, data, out2, listener);
        out2.close();
        if (listener == null) {
            return;
        }
        if (ExecutorLab.getInstance().isRunning()) {
            listener.onSuccess(new String(out2.toByteArray()));
        } else {
            listener.onError(str);
        }
    }

    public void writeResponseToFile(String url, File dst, OnNetListener listener) {
        if (url != null && dst != null) {
            request(url, null, dst, listener);
        }
    }

    public void postRequest(String url, String data, OnNetListener listener) {
        if (url != null) {
            request(url, data, null, listener);
        }
    }

    public void getRequest(String url, OnNetListener listener) {
        postRequest(url, null, listener);
    }

    public String postRequest(String url, String data) {
        if (url == null) {
            return null;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doRequest(url, data, out, null);
            out.close();
            return new String(out.toByteArray());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    public String getRequest(String url) {
        return postRequest(url, null);
    }
}
