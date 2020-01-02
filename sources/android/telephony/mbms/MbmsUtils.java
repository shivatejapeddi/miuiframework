package android.telephony.mbms;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.telecom.Logging.Session;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MbmsUtils {
    private static final String LOG_TAG = "MbmsUtils";

    public static boolean isContainedIn(File parent, File child) {
        try {
            return child.getCanonicalPath().startsWith(parent.getCanonicalPath());
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to resolve canonical paths: ");
            stringBuilder.append(e);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public static ComponentName toComponentName(ComponentInfo ci) {
        return new ComponentName(ci.packageName, ci.name);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004d A:{SYNTHETIC, Splitter:B:25:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004c A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004c A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004d A:{SYNTHETIC, Splitter:B:25:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004d A:{SYNTHETIC, Splitter:B:25:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004c A:{RETURN} */
    public static android.content.ComponentName getOverrideServiceName(android.content.Context r5, java.lang.String r6) {
        /*
        r0 = 0;
        r1 = r6.hashCode();
        r2 = -1374878107; // 0xffffffffae0d0665 float:-3.2065368E-11 double:NaN;
        r3 = 2;
        r4 = 1;
        if (r1 == r2) goto L_0x002b;
    L_0x000c:
        r2 = -407466459; // 0xffffffffe7b68e25 float:-1.7241856E24 double:NaN;
        if (r1 == r2) goto L_0x0021;
    L_0x0011:
        r2 = 1752202112; // 0x68707b80 float:4.5425845E24 double:8.65702868E-315;
        if (r1 == r2) goto L_0x0017;
    L_0x0016:
        goto L_0x0035;
    L_0x0017:
        r1 = "android.telephony.action.EmbmsGroupCall";
        r1 = r6.equals(r1);
        if (r1 == 0) goto L_0x0016;
    L_0x001f:
        r1 = r3;
        goto L_0x0036;
    L_0x0021:
        r1 = "android.telephony.action.EmbmsDownload";
        r1 = r6.equals(r1);
        if (r1 == 0) goto L_0x0016;
    L_0x0029:
        r1 = 0;
        goto L_0x0036;
    L_0x002b:
        r1 = "android.telephony.action.EmbmsStreaming";
        r1 = r6.equals(r1);
        if (r1 == 0) goto L_0x0016;
    L_0x0033:
        r1 = r4;
        goto L_0x0036;
    L_0x0035:
        r1 = -1;
    L_0x0036:
        if (r1 == 0) goto L_0x0045;
    L_0x0038:
        if (r1 == r4) goto L_0x0041;
    L_0x003a:
        if (r1 == r3) goto L_0x003d;
    L_0x003c:
        goto L_0x0049;
    L_0x003d:
        r0 = "mbms-group-call-service-override";
        goto L_0x0049;
    L_0x0041:
        r0 = "mbms-streaming-service-override";
        goto L_0x0049;
    L_0x0045:
        r0 = "mbms-download-service-override";
    L_0x0049:
        r1 = 0;
        if (r0 != 0) goto L_0x004d;
    L_0x004c:
        return r1;
    L_0x004d:
        r2 = r5.getPackageManager();	 Catch:{ NameNotFoundException -> 0x006f }
        r3 = r5.getPackageName();	 Catch:{ NameNotFoundException -> 0x006f }
        r4 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r2 = r2.getApplicationInfo(r3, r4);	 Catch:{ NameNotFoundException -> 0x006f }
        r3 = r2.metaData;
        if (r3 != 0) goto L_0x0061;
    L_0x0060:
        return r1;
    L_0x0061:
        r3 = r2.metaData;
        r3 = r3.getString(r0);
        if (r3 != 0) goto L_0x006a;
    L_0x0069:
        return r1;
    L_0x006a:
        r1 = android.content.ComponentName.unflattenFromString(r3);
        return r1;
    L_0x006f:
        r2 = move-exception;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.mbms.MbmsUtils.getOverrideServiceName(android.content.Context, java.lang.String):android.content.ComponentName");
    }

    public static ServiceInfo getMiddlewareServiceInfo(Context context, String serviceAction) {
        List<ResolveInfo> services;
        PackageManager packageManager = context.getPackageManager();
        Intent queryIntent = new Intent();
        queryIntent.setAction(serviceAction);
        ComponentName overrideService = getOverrideServiceName(context, serviceAction);
        if (overrideService == null) {
            services = packageManager.queryIntentServices(queryIntent, 1048576);
        } else {
            queryIntent.setComponent(overrideService);
            services = packageManager.queryIntentServices(queryIntent, 131072);
        }
        String str = LOG_TAG;
        if (services == null || services.size() == 0) {
            Log.w(str, "No MBMS services found, cannot get service info");
            return null;
        } else if (services.size() <= 1) {
            return ((ResolveInfo) services.get(0)).serviceInfo;
        } else {
            Log.w(str, "More than one MBMS service found, cannot get unique service");
            return null;
        }
    }

    public static int startBinding(Context context, String serviceAction, ServiceConnection serviceConnection) {
        Intent bindIntent = new Intent();
        ServiceInfo mbmsServiceInfo = getMiddlewareServiceInfo(context, serviceAction);
        if (mbmsServiceInfo == null) {
            return 1;
        }
        bindIntent.setComponent(toComponentName(mbmsServiceInfo));
        context.bindService(bindIntent, serviceConnection, 1);
        return 0;
    }

    public static File getEmbmsTempFileDirForService(Context context, String serviceId) {
        return new File(MbmsTempFileProvider.getEmbmsTempFileDir(context), serviceId.replaceAll("[^a-zA-Z0-9_]", Session.SESSION_SEPARATION_CHAR_CHILD));
    }
}
