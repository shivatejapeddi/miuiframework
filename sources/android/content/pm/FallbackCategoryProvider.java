package android.content.pm;

import android.util.ArrayMap;

public class FallbackCategoryProvider {
    private static final String TAG = "FallbackCategoryProvider";
    private static final ArrayMap<String, Integer> sFallbacks = new ArrayMap();

    /* JADX WARNING: Missing block: B:17:0x0065, code skipped:
            r0 = new java.lang.StringBuilder();
            r0.append("Found ");
            r0.append(sFallbacks.size());
            r0.append(" fallback categories");
            android.util.Log.d(r2, r0.toString());
     */
    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r4.close();
     */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            r4.close();
     */
    public static void loadFallbacks() {
        /*
        r0 = sFallbacks;
        r0.clear();
        r0 = 0;
        r1 = "fw.ignore_fb_categories";
        r1 = android.os.SystemProperties.getBoolean(r1, r0);
        r2 = "FallbackCategoryProvider";
        if (r1 == 0) goto L_0x0016;
    L_0x0010:
        r0 = "Ignoring fallback categories";
        android.util.Log.d(r2, r0);
        return;
    L_0x0016:
        r1 = new android.content.res.AssetManager;
        r1.<init>();
        r3 = "/system/framework/framework-res.apk";
        r1.addAssetPath(r3);
        r3 = new android.content.res.Resources;
        r4 = 0;
        r3.<init>(r1, r4, r4);
        r4 = new java.io.BufferedReader;	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
        r5 = new java.io.InputStreamReader;	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
        r6 = 17825796; // 0x1100004 float:2.6448634E-38 double:8.8071134E-317;
        r6 = r3.openRawResource(r6);	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
        r5.<init>(r6);	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
        r4.<init>(r5);	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
    L_0x0038:
        r5 = r4.readLine();	 Catch:{ all -> 0x0089 }
        r6 = r5;
        if (r5 == 0) goto L_0x0065;
    L_0x003f:
        r5 = r6.charAt(r0);	 Catch:{ all -> 0x0089 }
        r7 = 35;
        if (r5 != r7) goto L_0x0048;
    L_0x0047:
        goto L_0x0038;
    L_0x0048:
        r5 = ",";
        r5 = r6.split(r5);	 Catch:{ all -> 0x0089 }
        r7 = r5.length;	 Catch:{ all -> 0x0089 }
        r8 = 2;
        if (r7 != r8) goto L_0x0064;
    L_0x0052:
        r7 = sFallbacks;	 Catch:{ all -> 0x0089 }
        r8 = r5[r0];	 Catch:{ all -> 0x0089 }
        r9 = 1;
        r9 = r5[r9];	 Catch:{ all -> 0x0089 }
        r9 = java.lang.Integer.parseInt(r9);	 Catch:{ all -> 0x0089 }
        r9 = java.lang.Integer.valueOf(r9);	 Catch:{ all -> 0x0089 }
        r7.put(r8, r9);	 Catch:{ all -> 0x0089 }
    L_0x0064:
        goto L_0x0038;
    L_0x0065:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0089 }
        r0.<init>();	 Catch:{ all -> 0x0089 }
        r5 = "Found ";
        r0.append(r5);	 Catch:{ all -> 0x0089 }
        r5 = sFallbacks;	 Catch:{ all -> 0x0089 }
        r5 = r5.size();	 Catch:{ all -> 0x0089 }
        r0.append(r5);	 Catch:{ all -> 0x0089 }
        r5 = " fallback categories";
        r0.append(r5);	 Catch:{ all -> 0x0089 }
        r0 = r0.toString();	 Catch:{ all -> 0x0089 }
        android.util.Log.d(r2, r0);	 Catch:{ all -> 0x0089 }
        r4.close();	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
        goto L_0x009b;
    L_0x0089:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x008b }
    L_0x008b:
        r5 = move-exception;
        r4.close();	 Catch:{ all -> 0x0090 }
        goto L_0x0094;
    L_0x0090:
        r6 = move-exception;
        r0.addSuppressed(r6);	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
    L_0x0094:
        throw r5;	 Catch:{ IOException | NumberFormatException -> 0x0095, IOException | NumberFormatException -> 0x0095 }
    L_0x0095:
        r0 = move-exception;
        r4 = "Failed to read fallback categories";
        android.util.Log.w(r2, r4, r0);
    L_0x009b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.FallbackCategoryProvider.loadFallbacks():void");
    }

    public static int getFallbackCategory(String packageName) {
        return ((Integer) sFallbacks.getOrDefault(packageName, Integer.valueOf(-1))).intValue();
    }
}
