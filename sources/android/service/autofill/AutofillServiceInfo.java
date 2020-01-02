package android.service.autofill;

import android.Manifest.permission;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.metrics.LogMaker;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;

public final class AutofillServiceInfo {
    private static final String TAG = "AutofillServiceInfo";
    private static final String TAG_AUTOFILL_SERVICE = "autofill-service";
    private static final String TAG_COMPATIBILITY_PACKAGE = "compatibility-package";
    private final ArrayMap<String, Long> mCompatibilityPackages;
    private final ServiceInfo mServiceInfo;
    private final String mSettingsActivity;

    private static ServiceInfo getServiceInfoOrThrow(ComponentName comp, int userHandle) throws NameNotFoundException {
        try {
            ServiceInfo si = AppGlobals.getPackageManager().getServiceInfo(comp, 128, userHandle);
            if (si != null) {
                return si;
            }
        } catch (RemoteException e) {
        }
        throw new NameNotFoundException(comp.toString());
    }

    public AutofillServiceInfo(Context context, ComponentName comp, int userHandle) throws NameNotFoundException {
        this(context, getServiceInfoOrThrow(comp, userHandle));
    }

    public AutofillServiceInfo(Context context, ServiceInfo si) {
        String str = si.permission;
        String str2 = permission.BIND_AUTOFILL_SERVICE;
        boolean equals = str2.equals(str);
        String str3 = TAG;
        if (!equals) {
            str = si.permission;
            String str4 = permission.BIND_AUTOFILL;
            String str5 = "AutofillService from '";
            StringBuilder stringBuilder;
            if (str4.equals(str)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str5);
                stringBuilder.append(si.packageName);
                stringBuilder.append("' uses unsupported permission ");
                stringBuilder.append(str4);
                stringBuilder.append(". It works for now, but might not be supported on future releases");
                Log.w(str3, stringBuilder.toString());
                new MetricsLogger().write(new LogMaker((int) MetricsEvent.AUTOFILL_INVALID_PERMISSION).setPackageName(si.packageName));
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str5);
                stringBuilder.append(si.packageName);
                stringBuilder.append("' does not require permission ");
                stringBuilder.append(str2);
                Log.w(str3, stringBuilder.toString());
                throw new SecurityException("Service does not require permission android.permission.BIND_AUTOFILL_SERVICE");
            }
        }
        this.mServiceInfo = si;
        XmlResourceParser parser = si.loadXmlMetaData(context.getPackageManager(), AutofillService.SERVICE_META_DATA);
        if (parser == null) {
            this.mSettingsActivity = null;
            this.mCompatibilityPackages = null;
            return;
        }
        str2 = null;
        ArrayMap<String, Long> compatibilityPackages = null;
        TypedArray afsAttributes;
        try {
            Resources resources = context.getPackageManager().getResourcesForApplication(si.applicationInfo);
            int type = 0;
            while (type != 1 && type != 2) {
                type = parser.next();
            }
            if (TAG_AUTOFILL_SERVICE.equals(parser.getName())) {
                afsAttributes = null;
                afsAttributes = resources.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.AutofillService);
                str2 = afsAttributes.getString(0);
                afsAttributes.recycle();
                compatibilityPackages = parseCompatibilityPackages(parser, resources);
            } else {
                Log.e(str3, "Meta-data does not start with autofill-service tag");
            }
        } catch (NameNotFoundException | IOException | XmlPullParserException e) {
            Log.e(str3, "Error parsing auto fill service meta-data", e);
        } catch (Throwable th) {
            if (afsAttributes != null) {
                afsAttributes.recycle();
            }
        }
        this.mSettingsActivity = str2;
        this.mCompatibilityPackages = compatibilityPackages;
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x00e3  */
    /* JADX WARNING: Missing block: B:23:?, code skipped:
            r0 = new java.lang.StringBuilder();
            r0.append("Invalid compatibility package:");
            r0.append(r9);
            android.util.Log.e(r10, r0.toString());
     */
    /* JADX WARNING: Missing block: B:24:0x0063, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r16);
            r6.recycle();
     */
    private android.util.ArrayMap<java.lang.String, java.lang.Long> parseCompatibilityPackages(org.xmlpull.v1.XmlPullParser r16, android.content.res.Resources r17) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r15 = this;
        r1 = "Invalid compatibility max version code:";
        r0 = 0;
        r2 = r16.getDepth();
        r3 = r0;
    L_0x0008:
        r0 = r16.next();
        r4 = r0;
        r5 = 1;
        if (r0 == r5) goto L_0x00ef;
    L_0x0010:
        r0 = 3;
        if (r4 != r0) goto L_0x001e;
    L_0x0013:
        r6 = r16.getDepth();
        if (r6 <= r2) goto L_0x001a;
    L_0x0019:
        goto L_0x001e;
    L_0x001a:
        r8 = r17;
        goto L_0x00f1;
    L_0x001e:
        if (r4 == r0) goto L_0x00eb;
    L_0x0020:
        r0 = 4;
        if (r4 != r0) goto L_0x0026;
    L_0x0023:
        r8 = r17;
        goto L_0x0008;
    L_0x0026:
        r0 = r16.getName();
        r6 = "compatibility-package";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x00e7;
    L_0x0032:
        r6 = 0;
        r0 = android.util.Xml.asAttributeSet(r16);	 Catch:{ all -> 0x00db }
        r7 = r0;
        r0 = com.android.internal.R.styleable.AutofillService_CompatibilityPackage;	 Catch:{ all -> 0x00db }
        r8 = r17;
        r0 = r8.obtainAttributes(r7, r0);	 Catch:{ all -> 0x00d9 }
        r6 = r0;
        r0 = 0;
        r0 = r6.getString(r0);	 Catch:{ all -> 0x00d9 }
        r9 = r0;
        r0 = android.text.TextUtils.isEmpty(r9);	 Catch:{ all -> 0x00d9 }
        r10 = "AutofillServiceInfo";
        if (r0 == 0) goto L_0x006c;
    L_0x004f:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d9 }
        r0.<init>();	 Catch:{ all -> 0x00d9 }
        r1 = "Invalid compatibility package:";
        r0.append(r1);	 Catch:{ all -> 0x00d9 }
        r0.append(r9);	 Catch:{ all -> 0x00d9 }
        r0 = r0.toString();	 Catch:{ all -> 0x00d9 }
        android.util.Log.e(r10, r0);	 Catch:{ all -> 0x00d9 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r16);
        r6.recycle();
        goto L_0x00f1;
    L_0x006c:
        r0 = r6.getString(r5);	 Catch:{ all -> 0x00d9 }
        r5 = r0;
        if (r5 == 0) goto L_0x00bb;
    L_0x0073:
        r11 = java.lang.Long.parseLong(r5);	 Catch:{ NumberFormatException -> 0x00a0 }
        r0 = java.lang.Long.valueOf(r11);	 Catch:{ NumberFormatException -> 0x00a0 }
        r11 = r0.longValue();	 Catch:{ all -> 0x00d9 }
        r13 = 0;
        r11 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r11 >= 0) goto L_0x00c4;
    L_0x0086:
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d9 }
        r11.<init>();	 Catch:{ all -> 0x00d9 }
        r11.append(r1);	 Catch:{ all -> 0x00d9 }
        r11.append(r0);	 Catch:{ all -> 0x00d9 }
        r1 = r11.toString();	 Catch:{ all -> 0x00d9 }
        android.util.Log.e(r10, r1);	 Catch:{ all -> 0x00d9 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r16);
        r6.recycle();
        goto L_0x00f1;
    L_0x00a0:
        r0 = move-exception;
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d9 }
        r11.<init>();	 Catch:{ all -> 0x00d9 }
        r11.append(r1);	 Catch:{ all -> 0x00d9 }
        r11.append(r5);	 Catch:{ all -> 0x00d9 }
        r1 = r11.toString();	 Catch:{ all -> 0x00d9 }
        android.util.Log.e(r10, r1);	 Catch:{ all -> 0x00d9 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r16);
        r6.recycle();
        goto L_0x00f1;
    L_0x00bb:
        r10 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r0 = java.lang.Long.valueOf(r10);	 Catch:{ all -> 0x00d9 }
    L_0x00c4:
        if (r3 != 0) goto L_0x00cc;
    L_0x00c6:
        r10 = new android.util.ArrayMap;	 Catch:{ all -> 0x00d9 }
        r10.<init>();	 Catch:{ all -> 0x00d9 }
        r3 = r10;
    L_0x00cc:
        r3.put(r9, r0);	 Catch:{ all -> 0x00d9 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r16);
        r6.recycle();
        goto L_0x0008;
    L_0x00d9:
        r0 = move-exception;
        goto L_0x00de;
    L_0x00db:
        r0 = move-exception;
        r8 = r17;
    L_0x00de:
        com.android.internal.util.XmlUtils.skipCurrentTag(r16);
        if (r6 == 0) goto L_0x00e6;
    L_0x00e3:
        r6.recycle();
    L_0x00e6:
        throw r0;
    L_0x00e7:
        r8 = r17;
        goto L_0x0008;
    L_0x00eb:
        r8 = r17;
        goto L_0x0008;
    L_0x00ef:
        r8 = r17;
    L_0x00f1:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.autofill.AutofillServiceInfo.parseCompatibilityPackages(org.xmlpull.v1.XmlPullParser, android.content.res.Resources):android.util.ArrayMap");
    }

    public ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public String getSettingsActivity() {
        return this.mSettingsActivity;
    }

    public ArrayMap<String, Long> getCompatibilityPackages() {
        return this.mCompatibilityPackages;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("[");
        builder.append(this.mServiceInfo);
        builder.append(", settings:");
        builder.append(this.mSettingsActivity);
        builder.append(", hasCompatPckgs:");
        ArrayMap arrayMap = this.mCompatibilityPackages;
        boolean z = (arrayMap == null || arrayMap.isEmpty()) ? false : true;
        builder.append(z);
        builder.append("]");
        return builder.toString();
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("Component: ");
        pw.println(getServiceInfo().getComponentName());
        pw.print(prefix);
        pw.print("Settings: ");
        pw.println(this.mSettingsActivity);
        pw.print(prefix);
        pw.print("Compat packages: ");
        pw.println(this.mCompatibilityPackages);
    }
}
