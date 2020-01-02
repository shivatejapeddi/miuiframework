package miui.util;

import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Process;
import android.os.SystemProperties;
import java.lang.reflect.Field;
import java.util.WeakHashMap;

public class DumpBitmapInfoUtils {
    static final boolean ENABLE = MiuiFeatureUtils.isSystemFeatureSupported("DumpBitmapInfo", true);
    static int sBitmapThresholdSize;
    static WeakHashMap<Bitmap, CharSequence> sBitmapTitles = new WeakHashMap();
    static int sCurrProcess;

    static {
        if (ENABLE) {
        }
    }

    public static void putBitmap(Bitmap bmp, CharSequence title) {
        if (ENABLE && bmp != null) {
            try {
                if (isTrackingNeeded(bmp)) {
                    synchronized (sBitmapTitles) {
                        sBitmapTitles.put(bmp, title);
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00fa A:{SYNTHETIC, Splitter:B:53:0x00fa} */
    /* JADX WARNING: Missing block: B:69:?, code skipped:
            java.util.Collections.sort(r1, new miui.util.DumpBitmapInfoUtils.AnonymousClass1());
            r11.printf("All big bitmaps (debug.bitmap_threshold_size = %d k):\n", new java.lang.Object[]{java.lang.Integer.valueOf(sBitmapThresholdSize)});
            r15 = android.content.res.Resources.getSystem().getPreloadedDrawables().clone();
            r16 = r1.iterator();
     */
    public static void dumpBitmapInfo(java.io.FileDescriptor r25, java.lang.String[] r26) {
        /*
        r1 = r26;
        r0 = ENABLE;
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = r1.length;
        r8 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r2;
        r2 = r0;
        r0 = 0;
    L_0x0013:
        if (r0 >= r6) goto L_0x007d;
    L_0x0015:
        r9 = r1[r0];
        r10 = "--bitmap";
        r10 = r10.equalsIgnoreCase(r9);
        if (r10 != 0) goto L_0x0027;
    L_0x001f:
        r10 = "-b";
        r10 = r10.equalsIgnoreCase(r9);
        if (r10 == 0) goto L_0x0028;
    L_0x0027:
        r2 = 1;
    L_0x0028:
        r10 = "--exportbitmap";
        r10 = r10.equalsIgnoreCase(r9);
        if (r10 != 0) goto L_0x0038;
    L_0x0030:
        r10 = "-e";
        r10 = r10.equalsIgnoreCase(r9);
        if (r10 == 0) goto L_0x0039;
    L_0x0038:
        r3 = 1;
    L_0x0039:
        r10 = "--nogc";
        r10 = r10.equalsIgnoreCase(r9);
        if (r10 == 0) goto L_0x0042;
    L_0x0041:
        r4 = 1;
    L_0x0042:
        r10 = "--includepreload";
        r10 = r10.equalsIgnoreCase(r9);
        if (r10 == 0) goto L_0x004b;
    L_0x004a:
        r5 = 1;
    L_0x004b:
        r10 = "--recycle:";
        r10 = r9.startsWith(r10);
        if (r10 == 0) goto L_0x007a;
    L_0x0053:
        r10 = "--recycle:0x";
        r10 = r9.startsWith(r10);
        r11 = 16;
        if (r10 == 0) goto L_0x006c;
    L_0x005d:
        r10 = "--recycle:0x";
        r10 = r10.length();
        r10 = r9.substring(r10);
        r8 = java.lang.Integer.parseInt(r10, r11);
        goto L_0x007a;
    L_0x006c:
        r10 = "--recycle:";
        r10 = r10.length();
        r10 = r9.substring(r10);
        r8 = java.lang.Integer.parseInt(r10, r11);
    L_0x007a:
        r0 = r0 + 1;
        goto L_0x0013;
    L_0x007d:
        if (r2 != 0) goto L_0x0082;
    L_0x007f:
        if (r3 != 0) goto L_0x0082;
    L_0x0081:
        return;
    L_0x0082:
        if (r4 != 0) goto L_0x0087;
    L_0x0084:
        java.lang.System.gc();
    L_0x0087:
        r0 = android.app.ActivityThread.currentApplication();
        if (r0 == 0) goto L_0x00c8;
    L_0x008d:
        r0 = android.app.ActivityThread.currentPackageName();
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x00c8;
    L_0x0097:
        r0 = android.app.ActivityThread.currentPackageName();
        r6 = "system";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x00a4;
    L_0x00a3:
        goto L_0x00c8;
    L_0x00a4:
        r0 = new java.io.File;
        r6 = android.app.ActivityThread.currentApplication();
        r6 = r6.getCacheDir();
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "_exportbitmap/";
        r9.append(r10);
        r10 = android.app.ActivityThread.currentProcessName();
        r9.append(r10);
        r9 = r9.toString();
        r0.<init>(r6, r9);
        r6 = r0;
        goto L_0x00d0;
    L_0x00c8:
        r0 = new java.io.File;
        r6 = "/data/system/_exportbitmap/";
        r0.<init>(r6);
        r6 = r0;
    L_0x00d0:
        if (r3 == 0) goto L_0x00e6;
    L_0x00d2:
        r0 = r6.exists();
        if (r0 != 0) goto L_0x00dc;
    L_0x00d8:
        r6.mkdirs();
        goto L_0x00e6;
    L_0x00dc:
        libcore.io.IoUtils.deleteContents(r6);	 Catch:{ Exception -> 0x00e0 }
        goto L_0x00e6;
    L_0x00e0:
        r0 = move-exception;
        r9 = r0;
        r0 = r9;
        r0.printStackTrace();
    L_0x00e6:
        r0 = new java.io.FileOutputStream;
        r9 = r25;
        r0.<init>(r9);
        r10 = r0;
        r0 = new com.android.internal.util.FastPrintWriter;
        r0.<init>(r10);
        r11 = r0;
        r12 = 0;
        r14 = 0;
        r15 = sBitmapTitles;	 Catch:{ all -> 0x02e4 }
        monitor-enter(r15);	 Catch:{ all -> 0x02e4 }
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x02d8 }
        r0.<init>();	 Catch:{ all -> 0x02d8 }
        r16 = r0;
        r0 = sBitmapTitles;	 Catch:{ all -> 0x02d8 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x02d8 }
        r0 = r0.iterator();	 Catch:{ all -> 0x02d8 }
    L_0x010b:
        r17 = r0.hasNext();	 Catch:{ all -> 0x02d8 }
        if (r17 == 0) goto L_0x013f;
    L_0x0111:
        r17 = r0.next();	 Catch:{ all -> 0x0137 }
        r17 = (java.util.Map.Entry) r17;	 Catch:{ all -> 0x0137 }
        r18 = r17;
        r7 = new java.util.AbstractMap$SimpleEntry;	 Catch:{ all -> 0x0137 }
        r19 = r0;
        r0 = r18;
        r7.<init>(r0);	 Catch:{ all -> 0x0137 }
        r18 = r7.getKey();	 Catch:{ all -> 0x0137 }
        if (r18 == 0) goto L_0x012e;
    L_0x0128:
        r1 = r16;
        r1.add(r7);	 Catch:{ all -> 0x0137 }
        goto L_0x0130;
    L_0x012e:
        r1 = r16;
    L_0x0130:
        r16 = r1;
        r0 = r19;
        r1 = r26;
        goto L_0x010b;
    L_0x0137:
        r0 = move-exception;
        r22 = r2;
        r7 = r4;
        r19 = r5;
        goto L_0x02de;
    L_0x013f:
        r1 = r16;
        monitor-exit(r15);	 Catch:{ all -> 0x02d8 }
        r0 = new miui.util.DumpBitmapInfoUtils$1;	 Catch:{ all -> 0x02e4 }
        r0.<init>();	 Catch:{ all -> 0x02e4 }
        java.util.Collections.sort(r1, r0);	 Catch:{ all -> 0x02e4 }
        r0 = "All big bitmaps (debug.bitmap_threshold_size = %d k):\n";
        r7 = 1;
        r15 = new java.lang.Object[r7];	 Catch:{ all -> 0x02e4 }
        r16 = sBitmapThresholdSize;	 Catch:{ all -> 0x02e4 }
        r16 = java.lang.Integer.valueOf(r16);	 Catch:{ all -> 0x02e4 }
        r17 = 0;
        r15[r17] = r16;	 Catch:{ all -> 0x02e4 }
        r11.printf(r0, r15);	 Catch:{ all -> 0x02e4 }
        r0 = android.content.res.Resources.getSystem();	 Catch:{ all -> 0x02e4 }
        r0 = r0.getPreloadedDrawables();	 Catch:{ all -> 0x02e4 }
        r0 = r0.clone();	 Catch:{ all -> 0x02e4 }
        r15 = r0;
        r16 = r1.iterator();	 Catch:{ all -> 0x02e4 }
    L_0x016d:
        r0 = r16.hasNext();	 Catch:{ all -> 0x02e4 }
        if (r0 == 0) goto L_0x026e;
    L_0x0173:
        r0 = r16.next();	 Catch:{ all -> 0x02e4 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x02e4 }
        r18 = r0;
        r0 = r18.getKey();	 Catch:{ all -> 0x02e4 }
        r0 = (android.graphics.Bitmap) r0;	 Catch:{ all -> 0x02e4 }
        r19 = r0;
        r0 = r19.isRecycled();	 Catch:{ all -> 0x02e4 }
        if (r0 == 0) goto L_0x018e;
    L_0x0189:
        r21 = r1;
        r22 = r2;
        goto L_0x01d0;
    L_0x018e:
        r0 = 0;
        r17 = 0;
        r20 = r17;
        r7 = r20;
    L_0x0195:
        r20 = r0;
        r0 = r15.size();	 Catch:{ all -> 0x02e4 }
        if (r7 >= r0) goto L_0x01c4;
    L_0x019d:
        r0 = r15.valueAt(r7);	 Catch:{ all -> 0x01bc }
        r0 = (android.graphics.drawable.Drawable.ConstantState) r0;	 Catch:{ all -> 0x01bc }
        r21 = r1;
        r1 = getBitmapFromDrawableState(r0);	 Catch:{ all -> 0x01bc }
        r22 = r2;
        r2 = r19;
        if (r1 != r2) goto L_0x01b1;
    L_0x01af:
        r1 = 1;
        goto L_0x01cc;
    L_0x01b1:
        r7 = r7 + 1;
        r19 = r2;
        r0 = r20;
        r1 = r21;
        r2 = r22;
        goto L_0x0195;
    L_0x01bc:
        r0 = move-exception;
        r22 = r2;
        r7 = r4;
        r19 = r5;
        goto L_0x02ea;
    L_0x01c4:
        r21 = r1;
        r22 = r2;
        r2 = r19;
        r1 = r20;
    L_0x01cc:
        if (r1 == 0) goto L_0x01d6;
    L_0x01ce:
        if (r5 != 0) goto L_0x01d6;
    L_0x01d0:
        r1 = r21;
        r2 = r22;
        r7 = 1;
        goto L_0x016d;
    L_0x01d6:
        r0 = r2.getByteCount();	 Catch:{ all -> 0x0268 }
        r7 = r4;
        r19 = r5;
        r4 = (long) r0;
        r12 = r12 + r4;
        r14 = r14 + 1;
        r0 = r18.getValue();	 Catch:{ all -> 0x02e0 }
        r0 = (java.lang.CharSequence) r0;	 Catch:{ all -> 0x02e0 }
        r4 = 0;
        r0 = getBitmapMsg(r2, r0, r4, r1);	 Catch:{ all -> 0x02e0 }
        r4 = r0;
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02e0 }
        r0.<init>();	 Catch:{ all -> 0x02e0 }
        r5 = "  ";
        r0.append(r5);	 Catch:{ all -> 0x02e0 }
        r0.append(r4);	 Catch:{ all -> 0x02e0 }
        r0 = r0.toString();	 Catch:{ all -> 0x02e0 }
        r11.print(r0);	 Catch:{ all -> 0x02e0 }
        if (r3 == 0) goto L_0x0247;
    L_0x0203:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x023d }
        r0.<init>();	 Catch:{ Exception -> 0x023d }
        r0.append(r14);	 Catch:{ Exception -> 0x023d }
        r5 = "_";
        r0.append(r5);	 Catch:{ Exception -> 0x023d }
        r5 = r18.getValue();	 Catch:{ Exception -> 0x023d }
        r5 = (java.lang.CharSequence) r5;	 Catch:{ Exception -> 0x023d }
        r20 = r4;
        r4 = 1;
        r5 = getBitmapMsg(r2, r5, r4, r1);	 Catch:{ Exception -> 0x023b }
        r0.append(r5);	 Catch:{ Exception -> 0x023b }
        r0 = r0.toString();	 Catch:{ Exception -> 0x023b }
        r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x023b }
        r5 = new java.io.File;	 Catch:{ Exception -> 0x023b }
        r5.<init>(r6, r0);	 Catch:{ Exception -> 0x023b }
        r4.<init>(r5);	 Catch:{ Exception -> 0x023b }
        r5 = android.graphics.Bitmap.CompressFormat.PNG;	 Catch:{ Exception -> 0x023b }
        r23 = r0;
        r0 = 100;
        r2.compress(r5, r0, r4);	 Catch:{ Exception -> 0x023b }
        r4.close();	 Catch:{ Exception -> 0x023b }
        goto L_0x0249;
    L_0x023b:
        r0 = move-exception;
        goto L_0x0240;
    L_0x023d:
        r0 = move-exception;
        r20 = r4;
    L_0x0240:
        r0.printStackTrace(r11);	 Catch:{ all -> 0x02e0 }
        r0.printStackTrace();	 Catch:{ all -> 0x02e0 }
        goto L_0x0249;
    L_0x0247:
        r20 = r4;
    L_0x0249:
        if (r8 == 0) goto L_0x0259;
    L_0x024b:
        r0 = r2.hashCode();	 Catch:{ all -> 0x02e0 }
        if (r0 != r8) goto L_0x0259;
    L_0x0251:
        r2.recycle();	 Catch:{ all -> 0x02e0 }
        r0 = "  now recycled.";
        r11.print(r0);	 Catch:{ all -> 0x02e0 }
    L_0x0259:
        r0 = 10;
        r11.print(r0);	 Catch:{ all -> 0x02e0 }
        r4 = r7;
        r5 = r19;
        r1 = r21;
        r2 = r22;
        r7 = 1;
        goto L_0x016d;
    L_0x0268:
        r0 = move-exception;
        r7 = r4;
        r19 = r5;
        goto L_0x02ea;
    L_0x026e:
        r21 = r1;
        r22 = r2;
        r7 = r4;
        r19 = r5;
        r0 = "Total count: %d, size: %dM\n";
        r1 = 2;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x02e0 }
        r2 = java.lang.Integer.valueOf(r14);	 Catch:{ all -> 0x02e0 }
        r4 = 0;
        r1[r4] = r2;	 Catch:{ all -> 0x02e0 }
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r23 = r12 / r4;
        r23 = r23 / r4;
        r2 = java.lang.Long.valueOf(r23);	 Catch:{ all -> 0x02e0 }
        r4 = 1;
        r1[r4] = r2;	 Catch:{ all -> 0x02e0 }
        r11.printf(r0, r1);	 Catch:{ all -> 0x02e0 }
        if (r3 == 0) goto L_0x02ca;
    L_0x0293:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02e0 }
        r0.<init>();	 Catch:{ all -> 0x02e0 }
        r1 = "Export bitmap. Path: ";
        r0.append(r1);	 Catch:{ all -> 0x02e0 }
        r1 = r6.getAbsolutePath();	 Catch:{ all -> 0x02e0 }
        r0.append(r1);	 Catch:{ all -> 0x02e0 }
        r1 = "\n";
        r0.append(r1);	 Catch:{ all -> 0x02e0 }
        r0 = r0.toString();	 Catch:{ all -> 0x02e0 }
        r11.print(r0);	 Catch:{ all -> 0x02e0 }
        r0 = "DumpBitmapInfo";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02e0 }
        r1.<init>();	 Catch:{ all -> 0x02e0 }
        r2 = "Export bitmaps finished. Path: ";
        r1.append(r2);	 Catch:{ all -> 0x02e0 }
        r2 = r6.getAbsolutePath();	 Catch:{ all -> 0x02e0 }
        r1.append(r2);	 Catch:{ all -> 0x02e0 }
        r1 = r1.toString();	 Catch:{ all -> 0x02e0 }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x02e0 }
    L_0x02ca:
        r0 = "\n";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x02e0 }
        r11.printf(r0, r1);	 Catch:{ all -> 0x02e0 }
        r11.flush();
        return;
    L_0x02d8:
        r0 = move-exception;
        r22 = r2;
        r7 = r4;
        r19 = r5;
    L_0x02de:
        monitor-exit(r15);	 Catch:{ all -> 0x02e2 }
        throw r0;	 Catch:{ all -> 0x02e0 }
    L_0x02e0:
        r0 = move-exception;
        goto L_0x02ea;
    L_0x02e2:
        r0 = move-exception;
        goto L_0x02de;
    L_0x02e4:
        r0 = move-exception;
        r22 = r2;
        r7 = r4;
        r19 = r5;
    L_0x02ea:
        r11.flush();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.util.DumpBitmapInfoUtils.dumpBitmapInfo(java.io.FileDescriptor, java.lang.String[]):void");
    }

    private static boolean isTrackingNeeded(Bitmap bmp) {
        if (sCurrProcess != Process.myPid()) {
            sBitmapThresholdSize = SystemProperties.getInt("debug.bitmap_threshold_size", 100);
            sCurrProcess = Process.myPid();
        }
        return (bmp.getWidth() * bmp.getHeight()) / 256 >= sBitmapThresholdSize;
    }

    private static String getBitmapMsg(Bitmap bmp, CharSequence title, boolean forFileName, boolean isPreload) {
        String msg = formatMsg(bmp, title, isPreload);
        if (!forFileName) {
            return msg;
        }
        int overLength = msg.length() - 240;
        if (overLength > 0 && title != null && title.length() > overLength) {
            msg = formatMsg(bmp, title.toString().substring(overLength), isPreload);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(msg.replace(' ', '_').replace('\\', '-').replace('/', '-'));
        stringBuilder.append(".png");
        return stringBuilder.toString();
    }

    private static Bitmap getBitmapFromDrawableState(ConstantState c) {
        try {
            String clsName = c.getClass().getSimpleName();
            Field f;
            if (clsName.equals("BitmapState")) {
                f = c.getClass().getDeclaredField("mBitmap");
                f.setAccessible(true);
                return (Bitmap) f.get(c);
            } else if (!clsName.equals("NinePatchState")) {
                return null;
            } else {
                f = c.getClass().getDeclaredField("mNinePatch");
                f.setAccessible(true);
                NinePatch np = (NinePatch) f.get(c);
                if (np == null) {
                    return null;
                }
                return np.getBitmap();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String formatMsg(Bitmap bmp, CharSequence title, boolean isPreload) {
        String msgFormat = "0x%8x %,6dk %dx%d %s %s";
        Object[] objArr = new Object[6];
        objArr[0] = Integer.valueOf(bmp.hashCode());
        objArr[1] = Integer.valueOf(bmp.getByteCount() / 1024);
        objArr[2] = Integer.valueOf(bmp.getWidth());
        objArr[3] = Integer.valueOf(bmp.getHeight());
        String str = "";
        objArr[4] = isPreload ? "preload" : str;
        if (title != null) {
            str = title.toString();
        }
        objArr[5] = str;
        return String.format("0x%8x %,6dk %dx%d %s %s", objArr);
    }
}
