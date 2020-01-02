package com.miui.hybrid.hook;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HapLinksInjector {
    private static final String TAG = "HapLinksInjector";
    private static Map<String, List<String>> sData = new ConcurrentHashMap();
    private static IntentFilter sFilter = new IntentFilter();
    private static ResolveInfo sResolveInfo = null;

    static {
        sFilter.addAction("android.intent.action.VIEW");
        sFilter.addCategory(Intent.CATEGORY_DEFAULT);
        sFilter.addCategory(Intent.CATEGORY_BROWSABLE);
        sFilter.addDataScheme(IntentFilter.SCHEME_HTTP);
        sFilter.addDataScheme(IntentFilter.SCHEME_HTTPS);
    }

    private HapLinksInjector() {
    }

    /* JADX WARNING: Missing block: B:10:0x002f, code skipped:
            return;
     */
    public static synchronized void setData(java.util.Map<java.lang.String, java.util.List<java.lang.String>> r3, android.content.pm.ActivityInfo r4) {
        /*
        r0 = com.miui.hybrid.hook.HapLinksInjector.class;
        monitor-enter(r0);
        if (r3 == 0) goto L_0x0030;
    L_0x0005:
        if (r4 != 0) goto L_0x0008;
    L_0x0007:
        goto L_0x0030;
    L_0x0008:
        sData = r3;	 Catch:{ all -> 0x0039 }
        r1 = sData;	 Catch:{ all -> 0x0039 }
        r1 = r1.size();	 Catch:{ all -> 0x0039 }
        if (r1 <= 0) goto L_0x002b;
    L_0x0012:
        r1 = new android.content.pm.ResolveInfo;	 Catch:{ all -> 0x0039 }
        r1.<init>();	 Catch:{ all -> 0x0039 }
        sResolveInfo = r1;	 Catch:{ all -> 0x0039 }
        r1 = sResolveInfo;	 Catch:{ all -> 0x0039 }
        r1.activityInfo = r4;	 Catch:{ all -> 0x0039 }
        r1 = sResolveInfo;	 Catch:{ all -> 0x0039 }
        r2 = 0;
        r1.priority = r2;	 Catch:{ all -> 0x0039 }
        r1 = sResolveInfo;	 Catch:{ all -> 0x0039 }
        r1.preferredOrder = r2;	 Catch:{ all -> 0x0039 }
        r1 = sResolveInfo;	 Catch:{ all -> 0x0039 }
        r1.match = r2;	 Catch:{ all -> 0x0039 }
        goto L_0x002e;
    L_0x002b:
        r1 = 0;
        sResolveInfo = r1;	 Catch:{ all -> 0x0039 }
    L_0x002e:
        monitor-exit(r0);
        return;
    L_0x0030:
        r1 = "HapLinksInjector";
        r2 = "Illegal params for setData";
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x0039 }
        monitor-exit(r0);
        return;
    L_0x0039:
        r3 = move-exception;
        monitor-exit(r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.hybrid.hook.HapLinksInjector.setData(java.util.Map, android.content.pm.ActivityInfo):void");
    }

    /* JADX WARNING: Missing block: B:34:0x0074, code skipped:
            return null;
     */
    /* JADX WARNING: Missing block: B:36:0x0076, code skipped:
            return null;
     */
    public static synchronized android.content.pm.ResolveInfo resolveHapLinks(android.content.Intent r11) {
        /*
        r0 = com.miui.hybrid.hook.HapLinksInjector.class;
        monitor-enter(r0);
        r1 = 0;
        r2 = sResolveInfo;	 Catch:{ all -> 0x0077 }
        if (r2 != 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r0);
        return r1;
    L_0x000a:
        if (r11 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r0);
        return r1;
    L_0x000e:
        r2 = r11.getData();	 Catch:{ all -> 0x0077 }
        if (r2 != 0) goto L_0x0016;
    L_0x0014:
        monitor-exit(r0);
        return r1;
    L_0x0016:
        r3 = sFilter;	 Catch:{ all -> 0x0077 }
        r4 = r11.getAction();	 Catch:{ all -> 0x0077 }
        r5 = r11.getType();	 Catch:{ all -> 0x0077 }
        r6 = r11.getScheme();	 Catch:{ all -> 0x0077 }
        r8 = r11.getCategories();	 Catch:{ all -> 0x0077 }
        r9 = "HapLinksInjector";
        r7 = r2;
        r3 = r3.match(r4, r5, r6, r7, r8, r9);	 Catch:{ all -> 0x0077 }
        if (r3 < 0) goto L_0x0075;
    L_0x0031:
        r3 = r2.getHost();	 Catch:{ all -> 0x0077 }
        r4 = android.text.TextUtils.isEmpty(r3);	 Catch:{ all -> 0x0077 }
        if (r4 != 0) goto L_0x0073;
    L_0x003b:
        r4 = sData;	 Catch:{ all -> 0x0077 }
        r4 = r4.containsKey(r3);	 Catch:{ all -> 0x0077 }
        if (r4 != 0) goto L_0x0044;
    L_0x0043:
        goto L_0x0073;
    L_0x0044:
        r4 = r2.toString();	 Catch:{ all -> 0x0077 }
        r5 = sData;	 Catch:{ all -> 0x0077 }
        r5 = r5.get(r3);	 Catch:{ all -> 0x0077 }
        r5 = (java.util.List) r5;	 Catch:{ all -> 0x0077 }
        r6 = r5.iterator();	 Catch:{ all -> 0x0077 }
    L_0x0054:
        r7 = r6.hasNext();	 Catch:{ all -> 0x0077 }
        if (r7 == 0) goto L_0x0075;
    L_0x005a:
        r7 = r6.next();	 Catch:{ all -> 0x0077 }
        r7 = (java.lang.String) r7;	 Catch:{ all -> 0x0077 }
        r8 = java.util.regex.Pattern.compile(r7);	 Catch:{ all -> 0x0077 }
        r9 = r8.matcher(r4);	 Catch:{ all -> 0x0077 }
        r10 = r9.matches();	 Catch:{ all -> 0x0077 }
        if (r10 == 0) goto L_0x0072;
    L_0x006e:
        r1 = sResolveInfo;	 Catch:{ all -> 0x0077 }
        monitor-exit(r0);
        return r1;
    L_0x0072:
        goto L_0x0054;
    L_0x0073:
        monitor-exit(r0);
        return r1;
    L_0x0075:
        monitor-exit(r0);
        return r1;
    L_0x0077:
        r2 = move-exception;
        r3 = "HapLinksInjector";
        r4 = "resolve HapLinks failed";
        android.util.Log.e(r3, r4, r2);	 Catch:{ all -> 0x0082 }
        monitor-exit(r0);
        return r1;
    L_0x0082:
        r11 = move-exception;
        monitor-exit(r0);
        throw r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.hybrid.hook.HapLinksInjector.resolveHapLinks(android.content.Intent):android.content.pm.ResolveInfo");
    }
}
