package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ActivityInfo;
import android.miui.ResourcesManager;
import android.os.ParcelFileDescriptor;
import android.system.Os;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.miui.internal.variable.api.v29.Android_Content_Res_AssetManager.Extension;
import com.miui.internal.variable.api.v29.Android_Content_Res_AssetManager.Interface;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class AssetManager implements AutoCloseable {
    public static final int ACCESS_BUFFER = 3;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_UNKNOWN = 0;
    private static final boolean DEBUG_REFS = false;
    private static final boolean FEATURE_FLAG_IDMAP2 = true;
    private static final String FRAMEWORK_APK_PATH = "/system/framework/framework-res.apk";
    private static final String TAG = "AssetManager";
    private static final ApkAssets[] sEmptyApkAssets = new ApkAssets[0];
    private static final Object sSync = new Object();
    @GuardedBy({"sSync"})
    @UnsupportedAppUsage
    static AssetManager sSystem = null;
    @GuardedBy({"sSync"})
    private static ApkAssets[] sSystemApkAssets = new ApkAssets[0];
    @GuardedBy({"sSync"})
    private static ArraySet<ApkAssets> sSystemApkAssetsSet;
    @GuardedBy({"this"})
    private ApkAssets[] mApkAssets;
    @GuardedBy({"this"})
    private int mNumRefs;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private long mObject;
    @GuardedBy({"this"})
    private final long[] mOffsets;
    @GuardedBy({"this"})
    private boolean mOpen;
    @GuardedBy({"this"})
    private HashMap<Long, RuntimeException> mRefStacks;
    @GuardedBy({"this"})
    private final TypedValue mValue;

    public final class AssetInputStream extends InputStream {
        private long mAssetNativePtr;
        private long mLength;
        private long mMarkPos;

        /* synthetic */ AssetInputStream(AssetManager x0, long x1, AnonymousClass1 x2) {
            this(x1);
        }

        @UnsupportedAppUsage
        public final int getAssetInt() {
            throw new UnsupportedOperationException();
        }

        @UnsupportedAppUsage
        public final long getNativeAsset() {
            return this.mAssetNativePtr;
        }

        private AssetInputStream(long assetNativePtr) {
            this.mAssetNativePtr = assetNativePtr;
            this.mLength = AssetManager.nativeAssetGetLength(assetNativePtr);
        }

        public final int read() throws IOException {
            ensureOpen();
            return AssetManager.nativeAssetReadChar(this.mAssetNativePtr);
        }

        public final int read(byte[] b) throws IOException {
            ensureOpen();
            Preconditions.checkNotNull(b, "b");
            return AssetManager.nativeAssetRead(this.mAssetNativePtr, b, 0, b.length);
        }

        public final int read(byte[] b, int off, int len) throws IOException {
            ensureOpen();
            Preconditions.checkNotNull(b, "b");
            return AssetManager.nativeAssetRead(this.mAssetNativePtr, b, off, len);
        }

        public final long skip(long n) throws IOException {
            ensureOpen();
            long pos = AssetManager.nativeAssetSeek(this.mAssetNativePtr, 0, 0);
            long j = pos + n;
            long j2 = this.mLength;
            if (j > j2) {
                n = j2 - pos;
            }
            if (n > 0) {
                AssetManager.nativeAssetSeek(this.mAssetNativePtr, n, 0);
            }
            return n;
        }

        public final int available() throws IOException {
            ensureOpen();
            long len = AssetManager.nativeAssetGetRemainingLength(this.mAssetNativePtr);
            return len > 2147483647L ? Integer.MAX_VALUE : (int) len;
        }

        public final boolean markSupported() {
            return true;
        }

        public final void mark(int readlimit) {
            ensureOpen();
            this.mMarkPos = AssetManager.nativeAssetSeek(this.mAssetNativePtr, 0, 0);
        }

        public final void reset() throws IOException {
            ensureOpen();
            AssetManager.nativeAssetSeek(this.mAssetNativePtr, this.mMarkPos, -1);
        }

        public final void close() throws IOException {
            long j = this.mAssetNativePtr;
            if (j != 0) {
                AssetManager.nativeAssetDestroy(j);
                this.mAssetNativePtr = 0;
                synchronized (AssetManager.this) {
                    AssetManager.this.decRefsLocked((long) hashCode());
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            close();
        }

        private void ensureOpen() {
            if (this.mAssetNativePtr == 0) {
                throw new IllegalStateException("AssetInputStream is closed");
            }
        }
    }

    public static class Builder {
        private ArrayList<ApkAssets> mUserApkAssets = new ArrayList();

        public Builder addApkAssets(ApkAssets apkAssets) {
            this.mUserApkAssets.add(apkAssets);
            return this;
        }

        public AssetManager build() {
            ApkAssets[] systemApkAssets = AssetManager.getSystem().getApkAssets();
            ApkAssets[] apkAssets = new ApkAssets[(systemApkAssets.length + this.mUserApkAssets.size())];
            System.arraycopy(systemApkAssets, 0, apkAssets, 0, systemApkAssets.length);
            int userApkAssetCount = this.mUserApkAssets.size();
            for (int i = 0; i < userApkAssetCount; i++) {
                apkAssets[systemApkAssets.length + i] = (ApkAssets) this.mUserApkAssets.get(i);
            }
            AssetManager assetManager = new AssetManager();
            assetManager.mApkAssets = apkAssets;
            AssetManager.nativeSetApkAssets(assetManager.mObject, apkAssets, false);
            return assetManager;
        }
    }

    public static native String getAssetAllocations();

    @UnsupportedAppUsage
    public static native int getGlobalAssetCount();

    @UnsupportedAppUsage
    public static native int getGlobalAssetManagerCount();

    private static native void nativeApplyStyle(long j, long j2, int i, int i2, long j3, int[] iArr, long j4, long j5);

    private static native void nativeAssetDestroy(long j);

    private static native long nativeAssetGetLength(long j);

    private static native long nativeAssetGetRemainingLength(long j);

    private static native int nativeAssetRead(long j, byte[] bArr, int i, int i2);

    private static native int nativeAssetReadChar(long j);

    private static native long nativeAssetSeek(long j, long j2, int i);

    private static native int[] nativeAttributeResolutionStack(long j, long j2, int i, int i2, int i3);

    private static native long nativeCreate();

    private static native String[] nativeCreateIdmapsForStaticOverlaysTargetingAndroid();

    private static native void nativeDestroy(long j);

    private static native SparseArray<String> nativeGetAssignedPackageIdentifiers(long j);

    private static native String nativeGetLastResourceResolution(long j);

    private static native String[] nativeGetLocales(long j, boolean z);

    private static native Map nativeGetOverlayableMap(long j, String str);

    private static native int nativeGetResourceArray(long j, int i, int[] iArr);

    private static native int nativeGetResourceArraySize(long j, int i);

    private static native int nativeGetResourceBagValue(long j, int i, int i2, TypedValue typedValue);

    private static native String nativeGetResourceEntryName(long j, int i);

    private static native int nativeGetResourceIdentifier(long j, String str, String str2, String str3);

    private static native int[] nativeGetResourceIntArray(long j, int i);

    private static native String nativeGetResourceName(long j, int i);

    private static native String nativeGetResourcePackageName(long j, int i);

    private static native String[] nativeGetResourceStringArray(long j, int i);

    private static native int[] nativeGetResourceStringArrayInfo(long j, int i);

    private static native String nativeGetResourceTypeName(long j, int i);

    private static native int nativeGetResourceValue(long j, int i, short s, TypedValue typedValue, boolean z);

    private static native Configuration[] nativeGetSizeConfigurations(long j);

    private static native int[] nativeGetStyleAttributes(long j, int i);

    private static native String[] nativeList(long j, String str) throws IOException;

    private static native long nativeOpenAsset(long j, String str, int i);

    private static native ParcelFileDescriptor nativeOpenAssetFd(long j, String str, long[] jArr) throws IOException;

    private static native long nativeOpenNonAsset(long j, int i, String str, int i2);

    private static native ParcelFileDescriptor nativeOpenNonAssetFd(long j, int i, String str, long[] jArr) throws IOException;

    private static native long nativeOpenXmlAsset(long j, int i, String str);

    private static native boolean nativeResolveAttrs(long j, long j2, int i, int i2, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    private static native boolean nativeRetrieveAttributes(long j, long j2, int[] iArr, int[] iArr2, int[] iArr3);

    private static native void nativeSetApkAssets(long j, ApkAssets[] apkAssetsArr, boolean z);

    private static native void nativeSetConfiguration(long j, int i, int i2, String str, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17);

    private static native void nativeSetResourceResolutionLoggingEnabled(long j, boolean z);

    private static native void nativeThemeApplyStyle(long j, long j2, int i, boolean z);

    static native void nativeThemeClear(long j);

    private static native void nativeThemeCopy(long j, long j2, long j3, long j4);

    private static native long nativeThemeCreate(long j);

    private static native void nativeThemeDestroy(long j);

    private static native void nativeThemeDump(long j, long j2, int i, String str, String str2);

    private static native int nativeThemeGetAttributeValue(long j, long j2, int i, TypedValue typedValue, boolean z);

    static native int nativeThemeGetChangingConfigurations(long j);

    private static native void nativeVerifySystemIdmaps();

    /* synthetic */ AssetManager(boolean x0, AnonymousClass1 x1) {
        this(x0);
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public int addAssetPath(AssetManager assetManager, String s) {
                return assetManager.originalAddAssetPath(s);
            }
        });
    }

    @UnsupportedAppUsage
    public AssetManager() {
        ApkAssets[] assets;
        this.mValue = new TypedValue();
        this.mOffsets = new long[2];
        this.mOpen = true;
        this.mNumRefs = 1;
        synchronized (sSync) {
            createSystemAssetsInZygoteLocked();
            assets = sSystemApkAssets;
        }
        this.mObject = nativeCreate();
        setApkAssets(assets, false);
        ResourcesManager.addSystemAssets(this);
    }

    private AssetManager(boolean sentinel) {
        this.mValue = new TypedValue();
        this.mOffsets = new long[2];
        this.mOpen = true;
        this.mNumRefs = 1;
        this.mObject = nativeCreate();
    }

    @GuardedBy({"sSync"})
    private static void createSystemAssetsInZygoteLocked() {
        if (sSystem == null) {
            int regid = Os.getegid();
            int reuid = Os.geteuid();
            String str = TAG;
            if (!(regid == 0 && reuid == 0)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("WTF--create system assets with illegal uid: ");
                stringBuilder.append(reuid);
                stringBuilder.append(", gid:");
                stringBuilder.append(regid);
                Log.e(str, stringBuilder.toString(), new RuntimeException().fillInStackTrace());
            }
            try {
                Collection apkAssets = new ArrayList();
                apkAssets.add(ApkAssets.loadFromPath("/system/framework/framework-res.apk", true));
                String[] systemIdmapPaths = nativeCreateIdmapsForStaticOverlaysTargetingAndroid();
                if (systemIdmapPaths != null) {
                    for (String idmapPath : systemIdmapPaths) {
                        apkAssets.add(ApkAssets.loadOverlayFromPath(idmapPath, true));
                    }
                } else {
                    Log.w(str, "'idmap2 --scan' failed: no static=\"true\" overlays targeting \"android\" will be loaded");
                }
                sSystemApkAssetsSet = new ArraySet(apkAssets);
                sSystemApkAssets = (ApkAssets[]) apkAssets.toArray(new ApkAssets[apkAssets.size()]);
                sSystem = new AssetManager(true);
                sSystem.setApkAssets(sSystemApkAssets, false);
                ResourcesManager.addSystemAssets(sSystem);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create system AssetManager", e);
            }
        }
    }

    /* JADX WARNING: Missing block: B:12:0x003d, code skipped:
            if (r2 == null) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:14:?, code skipped:
            $closeResource(null, r2);
     */
    /* JADX WARNING: Missing block: B:16:?, code skipped:
            $closeResource(null, r1);
     */
    /* JADX WARNING: Missing block: B:22:0x004d, code skipped:
            if (r2 != null) goto L_0x004f;
     */
    /* JADX WARNING: Missing block: B:24:?, code skipped:
            $closeResource(r3, r2);
     */
    /* JADX WARNING: Missing block: B:30:?, code skipped:
            $closeResource(r2, r1);
     */
    private static void loadStaticRuntimeOverlays(java.util.ArrayList<android.content.res.ApkAssets> r8) throws java.io.IOException {
        /*
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x005f }
        r1 = "/data/resource-cache/overlays.list";
        r0.<init>(r1);	 Catch:{ FileNotFoundException -> 0x005f }
        r1 = new java.io.BufferedReader;	 Catch:{ all -> 0x005a }
        r2 = new java.io.InputStreamReader;	 Catch:{ all -> 0x005a }
        r2.<init>(r0);	 Catch:{ all -> 0x005a }
        r1.<init>(r2);	 Catch:{ all -> 0x005a }
        r2 = r0.getChannel();	 Catch:{ all -> 0x0053 }
        r3 = 0;
        r5 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r7 = 1;
        r2 = r2.lock(r3, r5, r7);	 Catch:{ all -> 0x0053 }
    L_0x0023:
        r3 = r1.readLine();	 Catch:{ all -> 0x004a }
        r4 = r3;
        if (r3 == 0) goto L_0x003c;
    L_0x002a:
        r3 = " ";
        r3 = r4.split(r3);	 Catch:{ all -> 0x004a }
        r5 = 1;
        r3 = r3[r5];	 Catch:{ all -> 0x004a }
        r5 = android.content.res.ApkAssets.loadOverlayFromPath(r3, r5);	 Catch:{ all -> 0x004a }
        r8.add(r5);	 Catch:{ all -> 0x004a }
        goto L_0x0023;
    L_0x003c:
        r3 = 0;
        if (r2 == 0) goto L_0x0042;
    L_0x003f:
        $closeResource(r3, r2);	 Catch:{ all -> 0x0053 }
    L_0x0042:
        $closeResource(r3, r1);	 Catch:{ all -> 0x005a }
        libcore.io.IoUtils.closeQuietly(r0);
        return;
    L_0x004a:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x004c }
    L_0x004c:
        r4 = move-exception;
        if (r2 == 0) goto L_0x0052;
    L_0x004f:
        $closeResource(r3, r2);	 Catch:{ all -> 0x0053 }
    L_0x0052:
        throw r4;	 Catch:{ all -> 0x0053 }
    L_0x0053:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0055:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ all -> 0x005a }
        throw r3;	 Catch:{ all -> 0x005a }
    L_0x005a:
        r1 = move-exception;
        libcore.io.IoUtils.closeQuietly(r0);
        throw r1;
    L_0x005f:
        r0 = move-exception;
        r1 = "AssetManager";
        r2 = "no overlays.list file found";
        android.util.Log.i(r1, r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.AssetManager.loadStaticRuntimeOverlays(java.util.ArrayList):void");
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

    @UnsupportedAppUsage
    public static AssetManager getSystem() {
        AssetManager assetManager;
        synchronized (sSync) {
            createSystemAssetsInZygoteLocked();
            assetManager = sSystem;
        }
        return assetManager;
    }

    public void close() {
        synchronized (this) {
            if (this.mOpen) {
                this.mOpen = false;
                decRefsLocked((long) hashCode());
                return;
            }
        }
    }

    public void setApkAssets(ApkAssets[] apkAssets, boolean invalidateCaches) {
        Preconditions.checkNotNull(apkAssets, "apkAssets");
        ApkAssets[] apkAssetsArr = sSystemApkAssets;
        ApkAssets[] newApkAssets = new ApkAssets[(apkAssetsArr.length + apkAssets.length)];
        int i = 0;
        System.arraycopy(apkAssetsArr, 0, newApkAssets, 0, apkAssetsArr.length);
        int newLength = sSystemApkAssets.length;
        int length = apkAssets.length;
        while (i < length) {
            ApkAssets apkAsset = apkAssets[i];
            if (!sSystemApkAssetsSet.contains(apkAsset)) {
                int newLength2 = newLength + 1;
                newApkAssets[newLength] = apkAsset;
                newLength = newLength2;
            }
            i++;
        }
        if (newLength != newApkAssets.length) {
            newApkAssets = (ApkAssets[]) Arrays.copyOf(newApkAssets, newLength);
        }
        synchronized (this) {
            ensureOpenLocked();
            this.mApkAssets = newApkAssets;
            nativeSetApkAssets(this.mObject, this.mApkAssets, invalidateCaches);
            if (invalidateCaches) {
                invalidateCachesLocked(-1);
            }
        }
    }

    private void invalidateCachesLocked(int diff) {
    }

    @UnsupportedAppUsage
    public ApkAssets[] getApkAssets() {
        synchronized (this) {
            if (this.mOpen) {
                ApkAssets[] apkAssetsArr = this.mApkAssets;
                return apkAssetsArr;
            }
            return sEmptyApkAssets;
        }
    }

    public String[] getApkPaths() {
        synchronized (this) {
            if (this.mOpen) {
                String[] paths = new String[this.mApkAssets.length];
                int count = this.mApkAssets.length;
                for (int i = 0; i < count; i++) {
                    paths[i] = this.mApkAssets[i].getAssetPath();
                }
                return paths;
            }
            return new String[0];
        }
    }

    public int findCookieForPath(String path) {
        Preconditions.checkNotNull(path, "path");
        synchronized (this) {
            ensureValidLocked();
            int count = this.mApkAssets.length;
            for (int i = 0; i < count; i++) {
                if (path.equals(this.mApkAssets[i].getAssetPath())) {
                    int i2 = i + 1;
                    return i2;
                }
            }
            return 0;
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public int addAssetPath(String path) {
        if (Extension.get().getExtension() != null) {
            return ((Interface) Extension.get().getExtension().asInterface()).addAssetPath(this, path);
        }
        return originalAddAssetPath(path);
    }

    /* Access modifiers changed, original: 0000 */
    public int originalAddAssetPath(String path) {
        return addAssetPathInternal(path, false, false);
    }

    @Deprecated
    @UnsupportedAppUsage
    public int addAssetPathAsSharedLibrary(String path) {
        return addAssetPathInternal(path, false, true);
    }

    @Deprecated
    @UnsupportedAppUsage
    public int addOverlayPath(String path) {
        return addAssetPathInternal(path, true, false);
    }

    private int addAssetPathInternal(String path, boolean overlay, boolean appAsLib) {
        Preconditions.checkNotNull(path, "path");
        synchronized (this) {
            int i;
            ApkAssets assets;
            ensureOpenLocked();
            int count = this.mApkAssets.length;
            for (int i2 = 0; i2 < count; i2++) {
                if (this.mApkAssets[i2].getAssetPath().equals(path)) {
                    i = i2 + 1;
                    return i;
                }
            }
            if (overlay) {
                try {
                    String idmapPath = new StringBuilder();
                    idmapPath.append("/data/resource-cache/");
                    idmapPath.append(path.substring(1).replace('/', '@'));
                    idmapPath.append("@idmap");
                    assets = ApkAssets.loadOverlayFromPath(idmapPath.toString(), false);
                } catch (IOException e) {
                    return 0;
                }
            }
            assets = ApkAssets.loadFromPath(path, false, appAsLib);
            this.mApkAssets = (ApkAssets[]) Arrays.copyOf(this.mApkAssets, count + 1);
            this.mApkAssets[count] = assets;
            nativeSetApkAssets(this.mObject, this.mApkAssets, true);
            invalidateCachesLocked(-1);
            i = count + 1;
            return i;
        }
    }

    @GuardedBy({"this"})
    private void ensureValidLocked() {
        if (this.mObject == 0) {
            throw new RuntimeException("AssetManager has been destroyed");
        }
    }

    @GuardedBy({"this"})
    private void ensureOpenLocked() {
        if (!this.mOpen) {
            throw new RuntimeException("AssetManager has been closed");
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:13:0x0036, code skipped:
            return true;
     */
    @android.annotation.UnsupportedAppUsage
    public boolean getResourceValue(int r8, int r9, android.util.TypedValue r10, boolean r11) {
        /*
        r7 = this;
        r0 = "outValue";
        com.android.internal.util.Preconditions.checkNotNull(r10, r0);
        monitor-enter(r7);
        r7.ensureValidLocked();	 Catch:{ all -> 0x0037 }
        r1 = r7.mObject;	 Catch:{ all -> 0x0037 }
        r4 = (short) r9;	 Catch:{ all -> 0x0037 }
        r3 = r8;
        r5 = r10;
        r6 = r11;
        r0 = nativeGetResourceValue(r1, r3, r4, r5, r6);	 Catch:{ all -> 0x0037 }
        if (r0 > 0) goto L_0x0019;
    L_0x0016:
        r1 = 0;
        monitor-exit(r7);	 Catch:{ all -> 0x0037 }
        return r1;
    L_0x0019:
        r1 = r10.changingConfigurations;	 Catch:{ all -> 0x0037 }
        r1 = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(r1);	 Catch:{ all -> 0x0037 }
        r10.changingConfigurations = r1;	 Catch:{ all -> 0x0037 }
        r1 = r10.type;	 Catch:{ all -> 0x0037 }
        r2 = 3;
        if (r1 != r2) goto L_0x0034;
    L_0x0026:
        r1 = r7.mApkAssets;	 Catch:{ all -> 0x0037 }
        r2 = r0 + -1;
        r1 = r1[r2];	 Catch:{ all -> 0x0037 }
        r2 = r10.data;	 Catch:{ all -> 0x0037 }
        r1 = r1.getStringFromPool(r2);	 Catch:{ all -> 0x0037 }
        r10.string = r1;	 Catch:{ all -> 0x0037 }
    L_0x0034:
        monitor-exit(r7);	 Catch:{ all -> 0x0037 }
        r1 = 1;
        return r1;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0037 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.AssetManager.getResourceValue(int, int, android.util.TypedValue, boolean):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public CharSequence getResourceText(int resId) {
        synchronized (this) {
            TypedValue outValue = this.mValue;
            if (getResourceValue(resId, 0, outValue, true)) {
                CharSequence coerceToString = outValue.coerceToString();
                return coerceToString;
            }
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public CharSequence getResourceBagText(int resId, int bagEntryId) {
        synchronized (this) {
            ensureValidLocked();
            TypedValue outValue = this.mValue;
            int cookie = nativeGetResourceBagValue(this.mObject, resId, bagEntryId, outValue);
            if (cookie <= 0) {
                return null;
            }
            outValue.changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            CharSequence stringFromPool;
            if (outValue.type == 3) {
                stringFromPool = this.mApkAssets[cookie - 1].getStringFromPool(outValue.data);
                return stringFromPool;
            }
            stringFromPool = outValue.coerceToString();
            return stringFromPool;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getResourceArraySize(int resId) {
        int nativeGetResourceArraySize;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceArraySize = nativeGetResourceArraySize(this.mObject, resId);
        }
        return nativeGetResourceArraySize;
    }

    /* Access modifiers changed, original: 0000 */
    public int getResourceArray(int resId, int[] outData) {
        int nativeGetResourceArray;
        Preconditions.checkNotNull(outData, "outData");
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceArray = nativeGetResourceArray(this.mObject, resId, outData);
        }
        return nativeGetResourceArray;
    }

    /* Access modifiers changed, original: 0000 */
    public String[] getResourceStringArray(int resId) {
        String[] nativeGetResourceStringArray;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceStringArray = nativeGetResourceStringArray(this.mObject, resId);
        }
        return nativeGetResourceStringArray;
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence[] getResourceTextArray(int resId) {
        synchronized (this) {
            ensureValidLocked();
            int[] rawInfoArray = nativeGetResourceStringArrayInfo(this.mObject, resId);
            if (rawInfoArray == null) {
                return null;
            }
            int rawInfoArrayLen = rawInfoArray.length;
            CharSequence[] retArray = new CharSequence[(rawInfoArrayLen / 2)];
            int i = 0;
            int j = 0;
            while (i < rawInfoArrayLen) {
                int cookie = rawInfoArray[i];
                int index = rawInfoArray[i + 1];
                CharSequence stringFromPool = (index < 0 || cookie <= 0) ? null : this.mApkAssets[cookie - 1].getStringFromPool(index);
                retArray[j] = stringFromPool;
                i += 2;
                j++;
            }
            return retArray;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int[] getResourceIntArray(int resId) {
        int[] nativeGetResourceIntArray;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceIntArray = nativeGetResourceIntArray(this.mObject, resId);
        }
        return nativeGetResourceIntArray;
    }

    /* Access modifiers changed, original: 0000 */
    public int[] getStyleAttributes(int resId) {
        int[] nativeGetStyleAttributes;
        synchronized (this) {
            ensureValidLocked();
            nativeGetStyleAttributes = nativeGetStyleAttributes(this.mObject, resId);
        }
        return nativeGetStyleAttributes;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:13:0x0036, code skipped:
            return true;
     */
    public boolean getThemeValue(long r9, int r11, android.util.TypedValue r12, boolean r13) {
        /*
        r8 = this;
        r0 = "outValue";
        com.android.internal.util.Preconditions.checkNotNull(r12, r0);
        monitor-enter(r8);
        r8.ensureValidLocked();	 Catch:{ all -> 0x0037 }
        r1 = r8.mObject;	 Catch:{ all -> 0x0037 }
        r3 = r9;
        r5 = r11;
        r6 = r12;
        r7 = r13;
        r0 = nativeThemeGetAttributeValue(r1, r3, r5, r6, r7);	 Catch:{ all -> 0x0037 }
        if (r0 > 0) goto L_0x0019;
    L_0x0016:
        r1 = 0;
        monitor-exit(r8);	 Catch:{ all -> 0x0037 }
        return r1;
    L_0x0019:
        r1 = r12.changingConfigurations;	 Catch:{ all -> 0x0037 }
        r1 = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(r1);	 Catch:{ all -> 0x0037 }
        r12.changingConfigurations = r1;	 Catch:{ all -> 0x0037 }
        r1 = r12.type;	 Catch:{ all -> 0x0037 }
        r2 = 3;
        if (r1 != r2) goto L_0x0034;
    L_0x0026:
        r1 = r8.mApkAssets;	 Catch:{ all -> 0x0037 }
        r2 = r0 + -1;
        r1 = r1[r2];	 Catch:{ all -> 0x0037 }
        r2 = r12.data;	 Catch:{ all -> 0x0037 }
        r1 = r1.getStringFromPool(r2);	 Catch:{ all -> 0x0037 }
        r12.string = r1;	 Catch:{ all -> 0x0037 }
    L_0x0034:
        monitor-exit(r8);	 Catch:{ all -> 0x0037 }
        r1 = 1;
        return r1;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0037 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.AssetManager.getThemeValue(long, int, android.util.TypedValue, boolean):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpTheme(long theme, int priority, String tag, String prefix) {
        synchronized (this) {
            ensureValidLocked();
            nativeThemeDump(this.mObject, theme, priority, tag, prefix);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public String getResourceName(int resId) {
        String nativeGetResourceName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceName = nativeGetResourceName(this.mObject, resId);
        }
        return nativeGetResourceName;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public String getResourcePackageName(int resId) {
        String nativeGetResourcePackageName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourcePackageName = nativeGetResourcePackageName(this.mObject, resId);
        }
        return nativeGetResourcePackageName;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public String getResourceTypeName(int resId) {
        String nativeGetResourceTypeName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceTypeName = nativeGetResourceTypeName(this.mObject, resId);
        }
        return nativeGetResourceTypeName;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public String getResourceEntryName(int resId) {
        String nativeGetResourceEntryName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceEntryName = nativeGetResourceEntryName(this.mObject, resId);
        }
        return nativeGetResourceEntryName;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int getResourceIdentifier(String name, String defType, String defPackage) {
        int nativeGetResourceIdentifier;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceIdentifier = nativeGetResourceIdentifier(this.mObject, name, defType, defPackage);
        }
        return nativeGetResourceIdentifier;
    }

    public void setResourceResolutionLoggingEnabled(boolean enabled) {
        synchronized (this) {
            ensureValidLocked();
            nativeSetResourceResolutionLoggingEnabled(this.mObject, enabled);
        }
    }

    public String getLastResourceResolution() {
        String nativeGetLastResourceResolution;
        synchronized (this) {
            ensureValidLocked();
            nativeGetLastResourceResolution = nativeGetLastResourceResolution(this.mObject);
        }
        return nativeGetLastResourceResolution;
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getPooledStringForCookie(int cookie, int id) {
        return getApkAssets()[cookie - 1].getStringFromPool(id);
    }

    public InputStream open(String fileName) throws IOException {
        return open(fileName, 2);
    }

    public InputStream open(String fileName, int accessMode) throws IOException {
        AssetInputStream assetInputStream;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            long asset = nativeOpenAsset(this.mObject, fileName, accessMode);
            if (asset != 0) {
                assetInputStream = new AssetInputStream(this, asset, null);
                incRefsLocked((long) assetInputStream.hashCode());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Asset file: ");
                stringBuilder.append(fileName);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        }
        return assetInputStream;
    }

    public AssetFileDescriptor openFd(String fileName) throws IOException {
        AssetFileDescriptor assetFileDescriptor;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            ParcelFileDescriptor pfd = nativeOpenAssetFd(this.mObject, fileName, this.mOffsets);
            if (pfd != null) {
                assetFileDescriptor = new AssetFileDescriptor(pfd, this.mOffsets[0], this.mOffsets[1]);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Asset file: ");
                stringBuilder.append(fileName);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        }
        return assetFileDescriptor;
    }

    public String[] list(String path) throws IOException {
        String[] nativeList;
        Preconditions.checkNotNull(path, "path");
        synchronized (this) {
            ensureValidLocked();
            nativeList = nativeList(this.mObject, path);
        }
        return nativeList;
    }

    @UnsupportedAppUsage
    public InputStream openNonAsset(String fileName) throws IOException {
        return openNonAsset(0, fileName, 2);
    }

    @UnsupportedAppUsage
    public InputStream openNonAsset(String fileName, int accessMode) throws IOException {
        return openNonAsset(0, fileName, accessMode);
    }

    @UnsupportedAppUsage
    public InputStream openNonAsset(int cookie, String fileName) throws IOException {
        return openNonAsset(cookie, fileName, 2);
    }

    @UnsupportedAppUsage
    public InputStream openNonAsset(int cookie, String fileName, int accessMode) throws IOException {
        AssetInputStream assetInputStream;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            long asset = nativeOpenNonAsset(this.mObject, cookie, fileName, accessMode);
            if (asset != 0) {
                assetInputStream = new AssetInputStream(this, asset, null);
                incRefsLocked((long) assetInputStream.hashCode());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Asset absolute file: ");
                stringBuilder.append(fileName);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        }
        return assetInputStream;
    }

    public AssetFileDescriptor openNonAssetFd(String fileName) throws IOException {
        return openNonAssetFd(0, fileName);
    }

    public AssetFileDescriptor openNonAssetFd(int cookie, String fileName) throws IOException {
        AssetFileDescriptor assetFileDescriptor;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            ParcelFileDescriptor pfd = nativeOpenNonAssetFd(this.mObject, cookie, fileName, this.mOffsets);
            if (pfd != null) {
                assetFileDescriptor = new AssetFileDescriptor(pfd, this.mOffsets[0], this.mOffsets[1]);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Asset absolute file: ");
                stringBuilder.append(fileName);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        }
        return assetFileDescriptor;
    }

    public XmlResourceParser openXmlResourceParser(String fileName) throws IOException {
        return openXmlResourceParser(0, fileName);
    }

    /* JADX WARNING: Missing block: B:13:0x001b, code skipped:
            if (r0 != null) goto L_0x001d;
     */
    /* JADX WARNING: Missing block: B:14:0x001d, code skipped:
            $closeResource(r1, r0);
     */
    public android.content.res.XmlResourceParser openXmlResourceParser(int r5, java.lang.String r6) throws java.io.IOException {
        /*
        r4 = this;
        r0 = r4.openXmlBlockAsset(r5, r6);
        r1 = r0.newParser();	 Catch:{ all -> 0x0018 }
        if (r1 == 0) goto L_0x0010;
        r2 = 0;
        $closeResource(r2, r0);
        return r1;
    L_0x0010:
        r2 = new java.lang.AssertionError;	 Catch:{ all -> 0x0018 }
        r3 = "block.newParser() returned a null parser";
        r2.<init>(r3);	 Catch:{ all -> 0x0018 }
        throw r2;	 Catch:{ all -> 0x0018 }
    L_0x0018:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x001a }
    L_0x001a:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0020;
    L_0x001d:
        $closeResource(r1, r0);
    L_0x0020:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.AssetManager.openXmlResourceParser(int, java.lang.String):android.content.res.XmlResourceParser");
    }

    /* Access modifiers changed, original: 0000 */
    public XmlBlock openXmlBlockAsset(String fileName) throws IOException {
        return openXmlBlockAsset(0, fileName);
    }

    /* Access modifiers changed, original: 0000 */
    public XmlBlock openXmlBlockAsset(int cookie, String fileName) throws IOException {
        XmlBlock block;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            long xmlBlock = nativeOpenXmlAsset(this.mObject, cookie, fileName);
            if (xmlBlock != 0) {
                block = new XmlBlock(this, xmlBlock);
                incRefsLocked((long) block.hashCode());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Asset XML file: ");
                stringBuilder.append(fileName);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        }
        return block;
    }

    /* Access modifiers changed, original: 0000 */
    public void xmlBlockGone(int id) {
        synchronized (this) {
            decRefsLocked((long) id);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void applyStyle(long themePtr, int defStyleAttr, int defStyleRes, Parser parser, int[] inAttrs, long outValuesAddress, long outIndicesAddress) {
        Parser parser2 = parser;
        Preconditions.checkNotNull(inAttrs, "inAttrs");
        synchronized (this) {
            ensureValidLocked();
            nativeApplyStyle(this.mObject, themePtr, defStyleAttr, defStyleRes, parser2 != null ? parser2.mParseState : 0, inAttrs, outValuesAddress, outIndicesAddress);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int[] getAttributeResolutionStack(long themePtr, int defStyleAttr, int defStyleRes, int xmlStyle) {
        int[] nativeAttributeResolutionStack;
        synchronized (this) {
            nativeAttributeResolutionStack = nativeAttributeResolutionStack(this.mObject, themePtr, xmlStyle, defStyleAttr, defStyleRes);
        }
        return nativeAttributeResolutionStack;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean resolveAttrs(long themePtr, int defStyleAttr, int defStyleRes, int[] inValues, int[] inAttrs, int[] outValues, int[] outIndices) {
        boolean nativeResolveAttrs;
        Preconditions.checkNotNull(inAttrs, "inAttrs");
        Preconditions.checkNotNull(outValues, "outValues");
        Preconditions.checkNotNull(outIndices, "outIndices");
        synchronized (this) {
            ensureValidLocked();
            nativeResolveAttrs = nativeResolveAttrs(this.mObject, themePtr, defStyleAttr, defStyleRes, inValues, inAttrs, outValues, outIndices);
        }
        return nativeResolveAttrs;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean retrieveAttributes(Parser parser, int[] inAttrs, int[] outValues, int[] outIndices) {
        boolean nativeRetrieveAttributes;
        Preconditions.checkNotNull(parser, "parser");
        Preconditions.checkNotNull(inAttrs, "inAttrs");
        Preconditions.checkNotNull(outValues, "outValues");
        Preconditions.checkNotNull(outIndices, "outIndices");
        synchronized (this) {
            ensureValidLocked();
            nativeRetrieveAttributes = nativeRetrieveAttributes(this.mObject, parser.mParseState, inAttrs, outValues, outIndices);
        }
        return nativeRetrieveAttributes;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public long createTheme() {
        long themePtr;
        synchronized (this) {
            ensureValidLocked();
            themePtr = nativeThemeCreate(this.mObject);
            incRefsLocked(themePtr);
        }
        return themePtr;
    }

    /* Access modifiers changed, original: 0000 */
    public void releaseTheme(long themePtr) {
        synchronized (this) {
            nativeThemeDestroy(themePtr);
            decRefsLocked(themePtr);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void applyStyleToTheme(long themePtr, int resId, boolean force) {
        synchronized (this) {
            ensureValidLocked();
            nativeThemeApplyStyle(this.mObject, themePtr, resId, force);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setThemeTo(long dstThemePtr, AssetManager srcAssetManager, long srcThemePtr) {
        synchronized (this) {
            ensureValidLocked();
            synchronized (srcAssetManager) {
                srcAssetManager.ensureValidLocked();
                nativeThemeCopy(this.mObject, dstThemePtr, srcAssetManager.mObject, srcThemePtr);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        long j = this.mObject;
        if (j != 0) {
            nativeDestroy(j);
        }
    }

    @UnsupportedAppUsage
    public boolean isUpToDate() {
        synchronized (this) {
            if (this.mOpen) {
                ApkAssets[] apkAssetsArr = this.mApkAssets;
                int length = apkAssetsArr.length;
                int i = 0;
                while (i < length) {
                    if (apkAssetsArr[i].isUpToDate()) {
                        i++;
                    } else {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    public String[] getLocales() {
        String[] nativeGetLocales;
        synchronized (this) {
            ensureValidLocked();
            nativeGetLocales = nativeGetLocales(this.mObject, false);
        }
        return nativeGetLocales;
    }

    public String[] getNonSystemLocales() {
        String[] nativeGetLocales;
        synchronized (this) {
            ensureValidLocked();
            nativeGetLocales = nativeGetLocales(this.mObject, true);
        }
        return nativeGetLocales;
    }

    /* Access modifiers changed, original: 0000 */
    public Configuration[] getSizeConfigurations() {
        Configuration[] nativeGetSizeConfigurations;
        synchronized (this) {
            ensureValidLocked();
            nativeGetSizeConfigurations = nativeGetSizeConfigurations(this.mObject);
        }
        return nativeGetSizeConfigurations;
    }

    @UnsupportedAppUsage
    public void setConfiguration(int mcc, int mnc, String locale, int orientation, int touchscreen, int density, int keyboard, int keyboardHidden, int navigation, int screenWidth, int screenHeight, int smallestScreenWidthDp, int screenWidthDp, int screenHeightDp, int screenLayout, int uiMode, int colorMode, int majorVersion) {
        synchronized (this) {
            ensureValidLocked();
            nativeSetConfiguration(this.mObject, mcc, mnc, locale, orientation, touchscreen, density, keyboard, keyboardHidden, navigation, screenWidth, screenHeight, smallestScreenWidthDp, screenWidthDp, screenHeightDp, screenLayout, uiMode, colorMode, majorVersion);
        }
    }

    @UnsupportedAppUsage
    public SparseArray<String> getAssignedPackageIdentifiers() {
        SparseArray nativeGetAssignedPackageIdentifiers;
        synchronized (this) {
            ensureValidLocked();
            nativeGetAssignedPackageIdentifiers = nativeGetAssignedPackageIdentifiers(this.mObject);
        }
        return nativeGetAssignedPackageIdentifiers;
    }

    @GuardedBy({"this"})
    public Map<String, String> getOverlayableMap(String packageName) {
        Map nativeGetOverlayableMap;
        synchronized (this) {
            ensureValidLocked();
            nativeGetOverlayableMap = nativeGetOverlayableMap(this.mObject, packageName);
        }
        return nativeGetOverlayableMap;
    }

    @GuardedBy({"this"})
    private void incRefsLocked(long id) {
        this.mNumRefs++;
    }

    @GuardedBy({"this"})
    private void decRefsLocked(long id) {
        this.mNumRefs--;
        if (this.mNumRefs == 0) {
            long j = this.mObject;
            if (j != 0) {
                nativeDestroy(j);
                this.mObject = 0;
                this.mApkAssets = sEmptyApkAssets;
            }
        }
    }
}
