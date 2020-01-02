package android.app.slice;

import android.app.slice.ISliceManager.Stub;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.UserHandle;
import android.util.ArraySet;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SliceManager {
    public static final String ACTION_REQUEST_SLICE_PERMISSION = "com.android.intent.action.REQUEST_SLICE_PERMISSION";
    public static final String CATEGORY_SLICE = "android.app.slice.category.SLICE";
    public static final String SLICE_METADATA_KEY = "android.metadata.SLICE_URI";
    private static final String TAG = "SliceManager";
    private final Context mContext;
    private final ISliceManager mService;
    private final IBinder mToken = new Binder();

    public SliceManager(Context context, Handler handler) throws ServiceNotFoundException {
        this.mContext = context;
        this.mService = Stub.asInterface(ServiceManager.getServiceOrThrow("slice"));
    }

    public void pinSlice(Uri uri, Set<SliceSpec> specs) {
        try {
            this.mService.pinSlice(this.mContext.getPackageName(), uri, (SliceSpec[]) specs.toArray(new SliceSpec[specs.size()]), this.mToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void pinSlice(Uri uri, List<SliceSpec> specs) {
        pinSlice(uri, new ArraySet((Collection) specs));
    }

    public void unpinSlice(Uri uri) {
        try {
            this.mService.unpinSlice(this.mContext.getPackageName(), uri, this.mToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasSliceAccess() {
        try {
            return this.mService.hasSliceAccess(this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Set<SliceSpec> getPinnedSpecs(Uri uri) {
        try {
            return new ArraySet(Arrays.asList(this.mService.getPinnedSpecs(uri, this.mContext.getPackageName())));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<Uri> getPinnedSlices() {
        try {
            return Arrays.asList(this.mService.getPinnedSlices(this.mContext.getPackageName()));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARNING: Missing block: B:12:0x002a, code skipped:
            if (r1 != null) goto L_0x002c;
     */
    /* JADX WARNING: Missing block: B:14:?, code skipped:
            $closeResource(r2, r1);
     */
    public java.util.Collection<android.net.Uri> getSliceDescendants(android.net.Uri r7) {
        /*
        r6 = this;
        r0 = r6.mContext;
        r0 = r0.getContentResolver();
        r1 = r0.acquireUnstableContentProviderClient(r7);	 Catch:{ RemoteException -> 0x0030 }
        r2 = new android.os.Bundle;	 Catch:{ all -> 0x0027 }
        r2.<init>();	 Catch:{ all -> 0x0027 }
        r3 = "slice_uri";
        r2.putParcelable(r3, r7);	 Catch:{ all -> 0x0027 }
        r3 = "get_descendants";
        r4 = 0;
        r3 = r1.call(r3, r4, r2);	 Catch:{ all -> 0x0027 }
        r5 = "slice_descendants";
        r5 = r3.getParcelableArrayList(r5);	 Catch:{ all -> 0x0027 }
        $closeResource(r4, r1);	 Catch:{ RemoteException -> 0x0030 }
        return r5;
    L_0x0027:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0029 }
    L_0x0029:
        r3 = move-exception;
        if (r1 == 0) goto L_0x002f;
    L_0x002c:
        $closeResource(r2, r1);	 Catch:{ RemoteException -> 0x0030 }
    L_0x002f:
        throw r3;	 Catch:{ RemoteException -> 0x0030 }
    L_0x0030:
        r1 = move-exception;
        r2 = "SliceManager";
        r3 = "Unable to get slice descendants";
        android.util.Log.e(r2, r3, r1);
        r1 = java.util.Collections.emptyList();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.slice.SliceManager.getSliceDescendants(android.net.Uri):java.util.Collection");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    /* JADX WARNING: Missing block: B:26:0x0061, code skipped:
            if (r2 != null) goto L_0x0063;
     */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            $closeResource(r3, r2);
     */
    public android.app.slice.Slice bindSlice(android.net.Uri r8, java.util.Set<android.app.slice.SliceSpec> r9) {
        /*
        r7 = this;
        r0 = "uri";
        com.android.internal.util.Preconditions.checkNotNull(r8, r0);
        r0 = r7.mContext;
        r0 = r0.getContentResolver();
        r1 = 0;
        r2 = r0.acquireUnstableContentProviderClient(r8);	 Catch:{ RemoteException -> 0x0067 }
        r3 = 1;
        if (r2 != 0) goto L_0x002b;
    L_0x0014:
        r4 = "SliceManager";
        r5 = "Unknown URI: %s";
        r3 = new java.lang.Object[r3];	 Catch:{ all -> 0x005e }
        r6 = 0;
        r3[r6] = r8;	 Catch:{ all -> 0x005e }
        r3 = java.lang.String.format(r5, r3);	 Catch:{ all -> 0x005e }
        android.util.Log.w(r4, r3);	 Catch:{ all -> 0x005e }
        if (r2 == 0) goto L_0x002a;
    L_0x0027:
        $closeResource(r1, r2);	 Catch:{ RemoteException -> 0x0067 }
    L_0x002a:
        return r1;
    L_0x002b:
        r4 = new android.os.Bundle;	 Catch:{ all -> 0x005e }
        r4.<init>();	 Catch:{ all -> 0x005e }
        r5 = "slice_uri";
        r4.putParcelable(r5, r8);	 Catch:{ all -> 0x005e }
        r5 = "supported_specs";
        r6 = new java.util.ArrayList;	 Catch:{ all -> 0x005e }
        r6.<init>(r9);	 Catch:{ all -> 0x005e }
        r4.putParcelableArrayList(r5, r6);	 Catch:{ all -> 0x005e }
        r5 = "bind_slice";
        r5 = r2.call(r5, r1, r4);	 Catch:{ all -> 0x005e }
        android.os.Bundle.setDefusable(r5, r3);	 Catch:{ all -> 0x005e }
        if (r5 != 0) goto L_0x0051;
        $closeResource(r1, r2);	 Catch:{ RemoteException -> 0x0067 }
        return r1;
    L_0x0051:
        r3 = "slice";
        r3 = r5.getParcelable(r3);	 Catch:{ all -> 0x005e }
        r3 = (android.app.slice.Slice) r3;	 Catch:{ all -> 0x005e }
        $closeResource(r1, r2);	 Catch:{ RemoteException -> 0x0067 }
        return r3;
    L_0x005e:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0060 }
    L_0x0060:
        r4 = move-exception;
        if (r2 == 0) goto L_0x0066;
    L_0x0063:
        $closeResource(r3, r2);	 Catch:{ RemoteException -> 0x0067 }
    L_0x0066:
        throw r4;	 Catch:{ RemoteException -> 0x0067 }
    L_0x0067:
        r2 = move-exception;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.slice.SliceManager.bindSlice(android.net.Uri, java.util.Set):android.app.slice.Slice");
    }

    @Deprecated
    public Slice bindSlice(Uri uri, List<SliceSpec> supportedSpecs) {
        return bindSlice(uri, new ArraySet((Collection) supportedSpecs));
    }

    /* JADX WARNING: Missing block: B:31:0x006e, code skipped:
            if (r5 != null) goto L_0x0070;
     */
    /* JADX WARNING: Missing block: B:33:?, code skipped:
            $closeResource(r6, r5);
     */
    public android.net.Uri mapIntentToUri(android.content.Intent r11) {
        /*
        r10 = this;
        r0 = r10.mContext;
        r0 = r0.getContentResolver();
        r1 = r10.resolveStatic(r11, r0);
        if (r1 == 0) goto L_0x000d;
    L_0x000c:
        return r1;
    L_0x000d:
        r2 = r10.getAuthority(r11);
        r3 = 0;
        if (r2 != 0) goto L_0x0015;
    L_0x0014:
        return r3;
    L_0x0015:
        r4 = new android.net.Uri$Builder;
        r4.<init>();
        r5 = "content";
        r4 = r4.scheme(r5);
        r4 = r4.authority(r2);
        r4 = r4.build();
        r5 = r0.acquireUnstableContentProviderClient(r4);	 Catch:{ RemoteException -> 0x0074 }
        if (r5 != 0) goto L_0x0046;
    L_0x002e:
        r6 = "SliceManager";
        r7 = "Unknown URI: %s";
        r8 = 1;
        r8 = new java.lang.Object[r8];	 Catch:{ all -> 0x006b }
        r9 = 0;
        r8[r9] = r4;	 Catch:{ all -> 0x006b }
        r7 = java.lang.String.format(r7, r8);	 Catch:{ all -> 0x006b }
        android.util.Log.w(r6, r7);	 Catch:{ all -> 0x006b }
        if (r5 == 0) goto L_0x0045;
    L_0x0042:
        $closeResource(r3, r5);	 Catch:{ RemoteException -> 0x0074 }
    L_0x0045:
        return r3;
    L_0x0046:
        r6 = new android.os.Bundle;	 Catch:{ all -> 0x006b }
        r6.<init>();	 Catch:{ all -> 0x006b }
        r7 = "slice_intent";
        r6.putParcelable(r7, r11);	 Catch:{ all -> 0x006b }
        r7 = "map_only";
        r7 = r5.call(r7, r3, r6);	 Catch:{ all -> 0x006b }
        if (r7 != 0) goto L_0x005e;
        $closeResource(r3, r5);	 Catch:{ RemoteException -> 0x0074 }
        return r3;
    L_0x005e:
        r8 = "slice";
        r8 = r7.getParcelable(r8);	 Catch:{ all -> 0x006b }
        r8 = (android.net.Uri) r8;	 Catch:{ all -> 0x006b }
        $closeResource(r3, r5);	 Catch:{ RemoteException -> 0x0074 }
        return r8;
    L_0x006b:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x006d }
    L_0x006d:
        r7 = move-exception;
        if (r5 == 0) goto L_0x0073;
    L_0x0070:
        $closeResource(r6, r5);	 Catch:{ RemoteException -> 0x0074 }
    L_0x0073:
        throw r7;	 Catch:{ RemoteException -> 0x0074 }
    L_0x0074:
        r5 = move-exception;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.slice.SliceManager.mapIntentToUri(android.content.Intent):android.net.Uri");
    }

    private String getAuthority(Intent intent) {
        Intent queryIntent = new Intent(intent);
        String str = CATEGORY_SLICE;
        if (!queryIntent.hasCategory(str)) {
            queryIntent.addCategory(str);
        }
        List<ResolveInfo> providers = this.mContext.getPackageManager().queryIntentContentProviders(queryIntent, 0);
        if (providers == null || providers.isEmpty()) {
            return null;
        }
        return ((ResolveInfo) providers.get(0)).providerInfo.authority;
    }

    private Uri resolveStatic(Intent intent, ContentResolver resolver) {
        Preconditions.checkNotNull(intent, "intent");
        boolean z = (intent.getComponent() == null && intent.getPackage() == null && intent.getData() == null) ? false : true;
        Preconditions.checkArgument(z, "Slice intent must be explicit %s", intent);
        Uri intentData = intent.getData();
        if (intentData != null) {
            if (SliceProvider.SLICE_TYPE.equals(resolver.getType(intentData))) {
                return intentData;
            }
        }
        ResolveInfo resolve = this.mContext.getPackageManager().resolveActivity(intent, 128);
        if (!(resolve == null || resolve.activityInfo == null || resolve.activityInfo.metaData == null)) {
            Bundle bundle = resolve.activityInfo.metaData;
            String str = SLICE_METADATA_KEY;
            if (bundle.containsKey(str)) {
                return Uri.parse(resolve.activityInfo.metaData.getString(str));
            }
        }
        return null;
    }

    /* JADX WARNING: Missing block: B:40:0x0096, code skipped:
            if (r7 != null) goto L_0x0098;
     */
    /* JADX WARNING: Missing block: B:42:?, code skipped:
            $closeResource(r1, r7);
     */
    public android.app.slice.Slice bindSlice(android.content.Intent r11, java.util.Set<android.app.slice.SliceSpec> r12) {
        /*
        r10 = this;
        r0 = "intent";
        com.android.internal.util.Preconditions.checkNotNull(r11, r0);
        r0 = r11.getComponent();
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x001c;
    L_0x000d:
        r0 = r11.getPackage();
        if (r0 != 0) goto L_0x001c;
    L_0x0013:
        r0 = r11.getData();
        if (r0 == 0) goto L_0x001a;
    L_0x0019:
        goto L_0x001c;
    L_0x001a:
        r0 = r1;
        goto L_0x001d;
    L_0x001c:
        r0 = r2;
    L_0x001d:
        r3 = new java.lang.Object[r2];
        r3[r1] = r11;
        r4 = "Slice intent must be explicit %s";
        com.android.internal.util.Preconditions.checkArgument(r0, r4, r3);
        r0 = r10.mContext;
        r0 = r0.getContentResolver();
        r3 = r10.resolveStatic(r11, r0);
        if (r3 == 0) goto L_0x0037;
    L_0x0032:
        r1 = r10.bindSlice(r3, r12);
        return r1;
    L_0x0037:
        r4 = r10.getAuthority(r11);
        r5 = 0;
        if (r4 != 0) goto L_0x003f;
    L_0x003e:
        return r5;
    L_0x003f:
        r6 = new android.net.Uri$Builder;
        r6.<init>();
        r7 = "content";
        r6 = r6.scheme(r7);
        r6 = r6.authority(r4);
        r6 = r6.build();
        r7 = r0.acquireUnstableContentProviderClient(r6);	 Catch:{ RemoteException -> 0x009c }
        if (r7 != 0) goto L_0x006e;
    L_0x0058:
        r8 = "SliceManager";
        r9 = "Unknown URI: %s";
        r2 = new java.lang.Object[r2];	 Catch:{ all -> 0x0093 }
        r2[r1] = r6;	 Catch:{ all -> 0x0093 }
        r1 = java.lang.String.format(r9, r2);	 Catch:{ all -> 0x0093 }
        android.util.Log.w(r8, r1);	 Catch:{ all -> 0x0093 }
        if (r7 == 0) goto L_0x006d;
    L_0x006a:
        $closeResource(r5, r7);	 Catch:{ RemoteException -> 0x009c }
    L_0x006d:
        return r5;
    L_0x006e:
        r1 = new android.os.Bundle;	 Catch:{ all -> 0x0093 }
        r1.<init>();	 Catch:{ all -> 0x0093 }
        r2 = "slice_intent";
        r1.putParcelable(r2, r11);	 Catch:{ all -> 0x0093 }
        r2 = "map_slice";
        r2 = r7.call(r2, r5, r1);	 Catch:{ all -> 0x0093 }
        if (r2 != 0) goto L_0x0086;
        $closeResource(r5, r7);	 Catch:{ RemoteException -> 0x009c }
        return r5;
    L_0x0086:
        r8 = "slice";
        r8 = r2.getParcelable(r8);	 Catch:{ all -> 0x0093 }
        r8 = (android.app.slice.Slice) r8;	 Catch:{ all -> 0x0093 }
        $closeResource(r5, r7);	 Catch:{ RemoteException -> 0x009c }
        return r8;
    L_0x0093:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0095 }
    L_0x0095:
        r2 = move-exception;
        if (r7 == 0) goto L_0x009b;
    L_0x0098:
        $closeResource(r1, r7);	 Catch:{ RemoteException -> 0x009c }
    L_0x009b:
        throw r2;	 Catch:{ RemoteException -> 0x009c }
    L_0x009c:
        r1 = move-exception;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.slice.SliceManager.bindSlice(android.content.Intent, java.util.Set):android.app.slice.Slice");
    }

    @Deprecated
    public Slice bindSlice(Intent intent, List<SliceSpec> supportedSpecs) {
        return bindSlice(intent, new ArraySet((Collection) supportedSpecs));
    }

    public int checkSlicePermission(Uri uri, int pid, int uid) {
        try {
            return this.mService.checkSlicePermission(uri, this.mContext.getPackageName(), null, pid, uid, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void grantSlicePermission(String toPackage, Uri uri) {
        try {
            this.mService.grantSlicePermission(this.mContext.getPackageName(), toPackage, uri);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void revokeSlicePermission(String toPackage, Uri uri) {
        try {
            this.mService.revokeSlicePermission(this.mContext.getPackageName(), toPackage, uri);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void enforceSlicePermission(Uri uri, String pkg, int pid, int uid, String[] autoGrantPermissions) {
        try {
            if (!UserHandle.isSameApp(uid, Process.myUid())) {
                if (pkg != null) {
                    if (this.mService.checkSlicePermission(uri, this.mContext.getPackageName(), pkg, pid, uid, autoGrantPermissions) == -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("User ");
                        stringBuilder.append(uid);
                        stringBuilder.append(" does not have slice permission for ");
                        stringBuilder.append(uri);
                        stringBuilder.append(".");
                        throw new SecurityException(stringBuilder.toString());
                    }
                    return;
                }
                throw new SecurityException("No pkg specified");
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void grantPermissionFromUser(Uri uri, String pkg, boolean allSlices) {
        try {
            this.mService.grantPermissionFromUser(uri, pkg, this.mContext.getPackageName(), allSlices);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
