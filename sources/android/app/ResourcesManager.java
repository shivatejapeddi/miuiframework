package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageParser;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.AssetManager.Builder;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.MiuiResources;
import android.content.res.MiuiResourcesImpl;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import android.hardware.display.DisplayManagerGlobal;
import android.os.IBinder;
import android.os.Process;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.miui.internal.variable.api.v29.Android_App_ResourcesManager.Extension;
import com.miui.internal.variable.api.v29.Android_App_ResourcesManager.Interface;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class ResourcesManager {
    private static final boolean DEBUG = false;
    private static final boolean ENABLE_APK_ASSETS_CACHE = true;
    static final String TAG = "ResourcesManager";
    private static final Predicate<WeakReference<Resources>> sEmptyReferencePredicate = -$$Lambda$ResourcesManager$QJ7UiVk_XS90KuXAsIjIEym1DnM.INSTANCE;
    private static ResourcesManager sResourcesManager;
    @UnsupportedAppUsage
    private final WeakHashMap<IBinder, ActivityResources> mActivityResourceReferences = new WeakHashMap();
    private final ArrayMap<Pair<Integer, DisplayAdjustments>, WeakReference<Display>> mAdjustedDisplays = new ArrayMap();
    private final ArrayMap<ApkKey, WeakReference<ApkAssets>> mCachedApkAssets = new ArrayMap();
    private final LruCache<ApkKey, ApkAssets> mLoadedApkAssets = new LruCache(3);
    private CompatibilityInfo mResCompatibilityInfo;
    @UnsupportedAppUsage
    private final Configuration mResConfiguration = new Configuration();
    @UnsupportedAppUsage
    private final ArrayMap<ResourcesKey, WeakReference<ResourcesImpl>> mResourceImpls = new ArrayMap();
    @UnsupportedAppUsage
    private final ArrayList<WeakReference<Resources>> mResourceReferences = new ArrayList();

    private static class ActivityResources {
        public final ArrayList<WeakReference<Resources>> activityResources;
        public final Configuration overrideConfig;

        private ActivityResources() {
            this.overrideConfig = new Configuration();
            this.activityResources = new ArrayList();
        }

        /* synthetic */ ActivityResources(AnonymousClass1 x0) {
            this();
        }
    }

    private static class ApkKey {
        public final boolean overlay;
        public final String path;
        public final boolean sharedLib;

        ApkKey(String path, boolean sharedLib, boolean overlay) {
            this.path = path;
            this.sharedLib = sharedLib;
            this.overlay = overlay;
        }

        public int hashCode() {
            return (((((1 * 31) + this.path.hashCode()) * 31) + Boolean.hashCode(this.sharedLib)) * 31) + Boolean.hashCode(this.overlay);
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof ApkKey)) {
                return false;
            }
            ApkKey other = (ApkKey) obj;
            if (this.path.equals(other.path) && this.sharedLib == other.sharedLib && this.overlay == other.overlay) {
                z = true;
            }
            return z;
        }
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public ResourcesImpl createResourcesImpl(Object o, ResourcesKey resourcesKey) {
                return ((ResourcesManager) o).originalCreateResourcesImpl(resourcesKey);
            }
        });
    }

    static /* synthetic */ boolean lambda$static$0(WeakReference weakRef) {
        return weakRef == null || weakRef.get() == null;
    }

    @UnsupportedAppUsage
    public static ResourcesManager getInstance() {
        ResourcesManager resourcesManager;
        synchronized (ResourcesManager.class) {
            if (sResourcesManager == null) {
                sResourcesManager = new ResourcesManager();
            }
            resourcesManager = sResourcesManager;
        }
        return resourcesManager;
    }

    public void invalidatePath(String path) {
        synchronized (this) {
            int count = 0;
            int i = 0;
            while (i < this.mResourceImpls.size()) {
                ResourcesKey key = (ResourcesKey) this.mResourceImpls.keyAt(i);
                if (key.isPathReferenced(path)) {
                    cleanupResourceImpl(key);
                    count++;
                } else {
                    i++;
                }
            }
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalidated ");
            stringBuilder.append(count);
            stringBuilder.append(" asset managers that referenced ");
            stringBuilder.append(path);
            Log.i(str, stringBuilder.toString());
        }
    }

    public Configuration getConfiguration() {
        Configuration configuration;
        synchronized (this) {
            configuration = this.mResConfiguration;
        }
        return configuration;
    }

    /* Access modifiers changed, original: 0000 */
    public DisplayMetrics getDisplayMetrics() {
        return getDisplayMetrics(0, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public DisplayMetrics getDisplayMetrics(int displayId, DisplayAdjustments da) {
        DisplayMetrics dm = new DisplayMetrics();
        Display display = getAdjustedDisplay(displayId, da);
        if (display != null) {
            display.getMetrics(dm);
        } else {
            dm.setToDefaults();
        }
        return dm;
    }

    private static void applyNonDefaultDisplayMetricsToConfiguration(DisplayMetrics dm, Configuration config) {
        config.touchscreen = 1;
        config.densityDpi = dm.densityDpi;
        config.screenWidthDp = (int) (((float) dm.widthPixels) / dm.density);
        config.screenHeightDp = (int) (((float) dm.heightPixels) / dm.density);
        int sl = Configuration.resetScreenLayout(config.screenLayout);
        if (dm.widthPixels > dm.heightPixels) {
            config.orientation = 2;
            config.screenLayout = Configuration.reduceScreenLayout(sl, config.screenWidthDp, config.screenHeightDp);
        } else {
            config.orientation = 1;
            config.screenLayout = Configuration.reduceScreenLayout(sl, config.screenHeightDp, config.screenWidthDp);
        }
        config.smallestScreenWidthDp = Math.min(config.screenWidthDp, config.screenHeightDp);
        config.compatScreenWidthDp = config.screenWidthDp;
        config.compatScreenHeightDp = config.screenHeightDp;
        config.compatSmallestScreenWidthDp = config.smallestScreenWidthDp;
    }

    public boolean applyCompatConfigurationLocked(int displayDensity, Configuration compatConfiguration) {
        CompatibilityInfo compatibilityInfo = this.mResCompatibilityInfo;
        if (compatibilityInfo == null || compatibilityInfo.supportsScreen()) {
            return false;
        }
        this.mResCompatibilityInfo.applyToConfiguration(displayDensity, compatConfiguration);
        return true;
    }

    /* JADX WARNING: Missing block: B:21:0x0049, code skipped:
            return r4;
     */
    private android.view.Display getAdjustedDisplay(int r8, android.view.DisplayAdjustments r9) {
        /*
        r7 = this;
        if (r9 == 0) goto L_0x0008;
    L_0x0002:
        r0 = new android.view.DisplayAdjustments;
        r0.<init>(r9);
        goto L_0x000d;
    L_0x0008:
        r0 = new android.view.DisplayAdjustments;
        r0.<init>();
        r1 = java.lang.Integer.valueOf(r8);
        r1 = android.util.Pair.create(r1, r0);
        monitor-enter(r7);
        r2 = r7.mAdjustedDisplays;	 Catch:{ all -> 0x004a }
        r2 = r2.get(r1);	 Catch:{ all -> 0x004a }
        r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x004a }
        if (r2 == 0) goto L_0x002b;
    L_0x0021:
        r3 = r2.get();	 Catch:{ all -> 0x004a }
        r3 = (android.view.Display) r3;	 Catch:{ all -> 0x004a }
        if (r3 == 0) goto L_0x002b;
    L_0x0029:
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        return r3;
    L_0x002b:
        r3 = android.hardware.display.DisplayManagerGlobal.getInstance();	 Catch:{ all -> 0x004a }
        if (r3 != 0) goto L_0x0034;
    L_0x0031:
        r4 = 0;
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        return r4;
    L_0x0034:
        r4 = r1.second;	 Catch:{ all -> 0x004a }
        r4 = (android.view.DisplayAdjustments) r4;	 Catch:{ all -> 0x004a }
        r4 = r3.getCompatibleDisplay(r8, r4);	 Catch:{ all -> 0x004a }
        if (r4 == 0) goto L_0x0048;
    L_0x003e:
        r5 = r7.mAdjustedDisplays;	 Catch:{ all -> 0x004a }
        r6 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x004a }
        r6.<init>(r4);	 Catch:{ all -> 0x004a }
        r5.put(r1, r6);	 Catch:{ all -> 0x004a }
    L_0x0048:
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        return r4;
    L_0x004a:
        r2 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ResourcesManager.getAdjustedDisplay(int, android.view.DisplayAdjustments):android.view.Display");
    }

    public Display getAdjustedDisplay(int displayId, Resources resources) {
        synchronized (this) {
            DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
            if (dm == null) {
                return null;
            }
            Display compatibleDisplay = dm.getCompatibleDisplay(displayId, resources);
            return compatibleDisplay;
        }
    }

    private void cleanupResourceImpl(ResourcesKey removedKey) {
        ResourcesImpl res = (ResourcesImpl) ((WeakReference) this.mResourceImpls.remove(removedKey)).get();
        if (res != null) {
            res.flushLayoutCache();
        }
    }

    private static String overlayPathToIdmapPath(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/data/resource-cache/");
        stringBuilder.append(path.substring(1).replace('/', '@'));
        stringBuilder.append("@idmap");
        return stringBuilder.toString();
    }

    private ApkAssets loadApkAssets(String path, boolean sharedLib, boolean overlay) throws IOException {
        ApkAssets apkAssets;
        LruCache lruCache;
        ApkKey newKey = new ApkKey(path, sharedLib, overlay);
        LruCache lruCache2 = this.mLoadedApkAssets;
        if (lruCache2 != null) {
            apkAssets = (ApkAssets) lruCache2.get(newKey);
            if (apkAssets != null) {
                return apkAssets;
            }
        }
        WeakReference<ApkAssets> apkAssetsRef = (WeakReference) this.mCachedApkAssets.get(newKey);
        if (apkAssetsRef != null) {
            apkAssets = (ApkAssets) apkAssetsRef.get();
            if (apkAssets != null) {
                lruCache = this.mLoadedApkAssets;
                if (lruCache != null) {
                    lruCache.put(newKey, apkAssets);
                }
                return apkAssets;
            }
            this.mCachedApkAssets.remove(newKey);
        }
        if (overlay) {
            apkAssets = ApkAssets.loadOverlayFromPath(overlayPathToIdmapPath(path), false);
        } else {
            apkAssets = ApkAssets.loadFromPath(path, false, sharedLib);
        }
        lruCache = this.mLoadedApkAssets;
        if (lruCache != null) {
            lruCache.put(newKey, apkAssets);
        }
        this.mCachedApkAssets.put(newKey, new WeakReference(apkAssets));
        return apkAssets;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    @UnsupportedAppUsage
    public AssetManager createAssetManager(ResourcesKey key) {
        StringBuilder stringBuilder;
        int length;
        int i;
        String splitResDir;
        StringBuilder stringBuilder2;
        Builder builder = new Builder();
        String str = key.mResDir;
        String str2 = TAG;
        if (str != null) {
            try {
                builder.addApkAssets(loadApkAssets(key.mResDir, false, false));
            } catch (IOException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("failed to add asset path ");
                stringBuilder.append(key.mResDir);
                Log.e(str2, stringBuilder.toString());
                return null;
            }
        }
        if (key.mSplitResDirs != null) {
            String[] strArr = key.mSplitResDirs;
            length = strArr.length;
            i = 0;
            while (i < length) {
                splitResDir = strArr[i];
                try {
                    builder.addApkAssets(loadApkAssets(splitResDir, false, false));
                    i++;
                } catch (IOException e2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("failed to add split asset path ");
                    stringBuilder.append(splitResDir);
                    Log.e(str2, stringBuilder.toString());
                    return null;
                }
            }
        }
        if (key.mOverlayDirs != null) {
            for (String splitResDir2 : key.mOverlayDirs) {
                try {
                    builder.addApkAssets(loadApkAssets(splitResDir2, false, true));
                } catch (IOException e3) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("failed to add overlay path ");
                    stringBuilder2.append(splitResDir2);
                    Log.w(str2, stringBuilder2.toString());
                }
            }
        }
        if (key.mLibDirs != null) {
            for (String splitResDir22 : key.mLibDirs) {
                if (splitResDir22.endsWith(PackageParser.APK_FILE_EXTENSION)) {
                    try {
                        builder.addApkAssets(loadApkAssets(splitResDir22, true, false));
                    } catch (IOException e4) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Asset path '");
                        stringBuilder2.append(splitResDir22);
                        stringBuilder2.append("' does not exist or contains no resources.");
                        Log.w(str2, stringBuilder2.toString());
                    }
                }
            }
        }
        return builder.build();
    }

    private static <T> int countLiveReferences(Collection<WeakReference<T>> collection) {
        int count = 0;
        for (WeakReference<T> ref : collection) {
            if ((ref != null ? ref.get() : null) != null) {
                count++;
            }
        }
        return count;
    }

    public void dump(String prefix, PrintWriter printWriter) {
        synchronized (this) {
            int i;
            IndentingPrintWriter pw = new IndentingPrintWriter(printWriter, "  ");
            for (i = 0; i < prefix.length() / 2; i++) {
                pw.increaseIndent();
            }
            pw.println("ResourcesManager:");
            pw.increaseIndent();
            if (this.mLoadedApkAssets != null) {
                pw.print("cached apks: total=");
                pw.print(this.mLoadedApkAssets.size());
                pw.print(" created=");
                pw.print(this.mLoadedApkAssets.createCount());
                pw.print(" evicted=");
                pw.print(this.mLoadedApkAssets.evictionCount());
                pw.print(" hit=");
                pw.print(this.mLoadedApkAssets.hitCount());
                pw.print(" miss=");
                pw.print(this.mLoadedApkAssets.missCount());
                pw.print(" max=");
                pw.print(this.mLoadedApkAssets.maxSize());
            } else {
                pw.print("cached apks: 0 [cache disabled]");
            }
            pw.println();
            pw.print("total apks: ");
            pw.println(countLiveReferences(this.mCachedApkAssets.values()));
            pw.print("resources: ");
            i = countLiveReferences(this.mResourceReferences);
            for (ActivityResources activityResources : this.mActivityResourceReferences.values()) {
                i += countLiveReferences(activityResources.activityResources);
            }
            pw.println(i);
            pw.print("resource impls: ");
            pw.println(countLiveReferences(this.mResourceImpls.values()));
        }
    }

    private Configuration generateConfig(ResourcesKey key, DisplayMetrics dm) {
        boolean isDefaultDisplay = key.mDisplayId == 0;
        boolean hasOverrideConfig = key.hasOverrideConfiguration();
        if (isDefaultDisplay && !hasOverrideConfig) {
            return getConfiguration();
        }
        Configuration config = new Configuration(getConfiguration());
        if (!isDefaultDisplay) {
            applyNonDefaultDisplayMetricsToConfiguration(dm, config);
        }
        if (!hasOverrideConfig) {
            return config;
        }
        config.updateFrom(key.mOverrideConfiguration);
        return config;
    }

    private ResourcesImpl createResourcesImpl(ResourcesKey key) {
        if (Extension.get().getExtension() != null) {
            return ((Interface) Extension.get().getExtension().asInterface()).createResourcesImpl(this, key);
        }
        return originalCreateResourcesImpl(key);
    }

    /* Access modifiers changed, original: 0000 */
    public ResourcesImpl originalCreateResourcesImpl(ResourcesKey key) {
        DisplayAdjustments daj = new DisplayAdjustments(key.mOverrideConfiguration);
        daj.setCompatibilityInfo(key.mCompatInfo);
        AssetManager assets = createAssetManager(key);
        if (assets == null) {
            return null;
        }
        DisplayMetrics dm = getDisplayMetrics(key.mDisplayId, daj);
        return new MiuiResourcesImpl(assets, dm, generateConfig(key, dm), daj);
    }

    private ResourcesImpl findResourcesImplForKeyLocked(ResourcesKey key) {
        WeakReference<ResourcesImpl> weakImplRef = (WeakReference) this.mResourceImpls.get(key);
        ResourcesImpl impl = weakImplRef != null ? (ResourcesImpl) weakImplRef.get() : null;
        if (impl == null || !impl.getAssets().isUpToDate()) {
            return null;
        }
        return impl;
    }

    private ResourcesImpl findOrCreateResourcesImplForKeyLocked(ResourcesKey key) {
        ResourcesImpl impl = findResourcesImplForKeyLocked(key);
        if (impl == null) {
            impl = createResourcesImpl(key);
            if (impl != null) {
                this.mResourceImpls.put(key, new WeakReference(impl));
            }
        }
        return impl;
    }

    private ResourcesKey findKeyForResourceImplLocked(ResourcesImpl resourceImpl) {
        int refCount = this.mResourceImpls.size();
        int i = 0;
        while (true) {
            ResourcesImpl impl = null;
            if (i >= refCount) {
                return null;
            }
            WeakReference<ResourcesImpl> weakImplRef = (WeakReference) this.mResourceImpls.valueAt(i);
            if (weakImplRef != null) {
                impl = (ResourcesImpl) weakImplRef.get();
            }
            if (impl != null && resourceImpl == impl) {
                return (ResourcesKey) this.mResourceImpls.keyAt(i);
            }
            i++;
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:13:0x0018, code skipped:
            return r1;
     */
    /* JADX WARNING: Missing block: B:25:0x0034, code skipped:
            return r1;
     */
    public boolean isSameResourcesOverrideConfig(android.os.IBinder r5, android.content.res.Configuration r6) {
        /*
        r4 = this;
        monitor-enter(r4);
        if (r5 == 0) goto L_0x000e;
    L_0x0003:
        r0 = r4.mActivityResourceReferences;	 Catch:{ all -> 0x000c }
        r0 = r0.get(r5);	 Catch:{ all -> 0x000c }
        r0 = (android.app.ResourcesManager.ActivityResources) r0;	 Catch:{ all -> 0x000c }
        goto L_0x000f;
    L_0x000c:
        r0 = move-exception;
        goto L_0x0035;
    L_0x000e:
        r0 = 0;
    L_0x000f:
        r1 = 1;
        r2 = 0;
        if (r0 != 0) goto L_0x0019;
    L_0x0013:
        if (r6 != 0) goto L_0x0016;
    L_0x0015:
        goto L_0x0017;
    L_0x0016:
        r1 = r2;
    L_0x0017:
        monitor-exit(r4);	 Catch:{ all -> 0x000c }
        return r1;
    L_0x0019:
        r3 = r0.overrideConfig;	 Catch:{ all -> 0x000c }
        r3 = java.util.Objects.equals(r3, r6);	 Catch:{ all -> 0x000c }
        if (r3 != 0) goto L_0x0032;
    L_0x0021:
        if (r6 == 0) goto L_0x0030;
    L_0x0023:
        r3 = r0.overrideConfig;	 Catch:{ all -> 0x000c }
        if (r3 == 0) goto L_0x0030;
    L_0x0027:
        r3 = r0.overrideConfig;	 Catch:{ all -> 0x000c }
        r3 = r6.diffPublicOnly(r3);	 Catch:{ all -> 0x000c }
        if (r3 != 0) goto L_0x0030;
    L_0x002f:
        goto L_0x0032;
    L_0x0030:
        r1 = r2;
        goto L_0x0033;
    L_0x0033:
        monitor-exit(r4);	 Catch:{ all -> 0x000c }
        return r1;
    L_0x0035:
        monitor-exit(r4);	 Catch:{ all -> 0x000c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ResourcesManager.isSameResourcesOverrideConfig(android.os.IBinder, android.content.res.Configuration):boolean");
    }

    private ActivityResources getOrCreateActivityResourcesStructLocked(IBinder activityToken) {
        ActivityResources activityResources = (ActivityResources) this.mActivityResourceReferences.get(activityToken);
        if (activityResources != null) {
            return activityResources;
        }
        activityResources = new ActivityResources();
        this.mActivityResourceReferences.put(activityToken, activityResources);
        return activityResources;
    }

    private Resources getOrCreateResourcesForActivityLocked(IBinder activityToken, ClassLoader classLoader, ResourcesImpl impl, CompatibilityInfo compatInfo) {
        ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
        int refCount = activityResources.activityResources.size();
        for (int i = 0; i < refCount; i++) {
            Resources resources = (Resources) ((WeakReference) activityResources.activityResources.get(i)).get();
            if (resources != null && Objects.equals(resources.getClassLoader(), classLoader) && resources.getImpl() == impl) {
                return resources;
            }
        }
        Resources resources2 = new MiuiResources(classLoader);
        resources2.setImpl(impl);
        activityResources.activityResources.add(new WeakReference(resources2));
        return resources2;
    }

    private Resources getOrCreateResourcesLocked(ClassLoader classLoader, ResourcesImpl impl, CompatibilityInfo compatInfo) {
        int refCount = this.mResourceReferences.size();
        for (int i = 0; i < refCount; i++) {
            Resources resources = (Resources) ((WeakReference) this.mResourceReferences.get(i)).get();
            if (resources != null && Objects.equals(resources.getClassLoader(), classLoader) && resources.getImpl() == impl) {
                return resources;
            }
        }
        Resources resources2 = new MiuiResources(classLoader);
        resources2.setImpl(impl);
        this.mResourceReferences.add(new WeakReference(resources2));
        return resources2;
    }

    public Resources createBaseActivityResources(IBinder activityToken, String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo, ClassLoader classLoader) {
        int i;
        Throwable th;
        IBinder iBinder = activityToken;
        Configuration configuration = overrideConfig;
        ClassLoader classLoader2;
        try {
            Trace.traceBegin(8192, "ResourcesManager#createBaseActivityResources");
            ResourcesKey key = new ResourcesKey(resDir, splitResDirs, overlayDirs, libDirs, displayId, configuration != null ? new Configuration(configuration) : null, compatInfo);
            classLoader2 = classLoader != null ? classLoader : ClassLoader.getSystemClassLoader();
            try {
                synchronized (this) {
                    try {
                        getOrCreateActivityResourcesStructLocked(activityToken);
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                updateResourcesForActivity(activityToken, configuration, displayId, false);
                Resources orCreateResources = getOrCreateResources(activityToken, key, classLoader2);
                Trace.traceEnd(8192);
                return orCreateResources;
            } catch (Throwable th3) {
                th = th3;
                i = displayId;
                Trace.traceEnd(8192);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            i = displayId;
            classLoader2 = classLoader;
            Trace.traceEnd(8192);
            throw th;
        }
    }

    /* JADX WARNING: Missing block: B:29:0x0077, code skipped:
            return r1;
     */
    private android.content.res.Resources getOrCreateResources(android.os.IBinder r4, android.content.res.ResourcesKey r5, java.lang.ClassLoader r6) {
        /*
        r3 = this;
        monitor-enter(r3);
        if (r4 == 0) goto L_0x003f;
        r0 = r3.getOrCreateActivityResourcesStructLocked(r4);	 Catch:{ all -> 0x0078 }
        r1 = r0.activityResources;	 Catch:{ all -> 0x0078 }
        r2 = sEmptyReferencePredicate;	 Catch:{ all -> 0x0078 }
        com.android.internal.util.ArrayUtils.unstableRemoveIf(r1, r2);	 Catch:{ all -> 0x0078 }
        r1 = r5.hasOverrideConfiguration();	 Catch:{ all -> 0x0078 }
        if (r1 == 0) goto L_0x0030;
    L_0x0015:
        r1 = r0.overrideConfig;	 Catch:{ all -> 0x0078 }
        r2 = android.content.res.Configuration.EMPTY;	 Catch:{ all -> 0x0078 }
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0078 }
        if (r1 != 0) goto L_0x0030;
    L_0x001f:
        r1 = new android.content.res.Configuration;	 Catch:{ all -> 0x0078 }
        r2 = r0.overrideConfig;	 Catch:{ all -> 0x0078 }
        r1.<init>(r2);	 Catch:{ all -> 0x0078 }
        r2 = r5.mOverrideConfiguration;	 Catch:{ all -> 0x0078 }
        r1.updateFrom(r2);	 Catch:{ all -> 0x0078 }
        r2 = r5.mOverrideConfiguration;	 Catch:{ all -> 0x0078 }
        r2.setTo(r1);	 Catch:{ all -> 0x0078 }
    L_0x0030:
        r1 = r3.findResourcesImplForKeyLocked(r5);	 Catch:{ all -> 0x0078 }
        if (r1 == 0) goto L_0x003e;
    L_0x0036:
        r2 = r5.mCompatInfo;	 Catch:{ all -> 0x0078 }
        r2 = r3.getOrCreateResourcesForActivityLocked(r4, r6, r1, r2);	 Catch:{ all -> 0x0078 }
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        return r2;
    L_0x003e:
        goto L_0x0054;
    L_0x003f:
        r0 = r3.mResourceReferences;	 Catch:{ all -> 0x0078 }
        r1 = sEmptyReferencePredicate;	 Catch:{ all -> 0x0078 }
        com.android.internal.util.ArrayUtils.unstableRemoveIf(r0, r1);	 Catch:{ all -> 0x0078 }
        r0 = r3.findResourcesImplForKeyLocked(r5);	 Catch:{ all -> 0x0078 }
        if (r0 == 0) goto L_0x0054;
    L_0x004c:
        r1 = r5.mCompatInfo;	 Catch:{ all -> 0x0078 }
        r1 = r3.getOrCreateResourcesLocked(r6, r0, r1);	 Catch:{ all -> 0x0078 }
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        return r1;
    L_0x0054:
        r0 = r3.createResourcesImpl(r5);	 Catch:{ all -> 0x0078 }
        if (r0 != 0) goto L_0x005d;
    L_0x005a:
        r1 = 0;
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        return r1;
    L_0x005d:
        r1 = r3.mResourceImpls;	 Catch:{ all -> 0x0078 }
        r2 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x0078 }
        r2.<init>(r0);	 Catch:{ all -> 0x0078 }
        r1.put(r5, r2);	 Catch:{ all -> 0x0078 }
        if (r4 == 0) goto L_0x0070;
    L_0x0069:
        r1 = r5.mCompatInfo;	 Catch:{ all -> 0x0078 }
        r1 = r3.getOrCreateResourcesForActivityLocked(r4, r6, r0, r1);	 Catch:{ all -> 0x0078 }
        goto L_0x0076;
    L_0x0070:
        r1 = r5.mCompatInfo;	 Catch:{ all -> 0x0078 }
        r1 = r3.getOrCreateResourcesLocked(r6, r0, r1);	 Catch:{ all -> 0x0078 }
    L_0x0076:
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        return r1;
    L_0x0078:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ResourcesManager.getOrCreateResources(android.os.IBinder, android.content.res.ResourcesKey, java.lang.ClassLoader):android.content.res.Resources");
    }

    public Resources getResources(IBinder activityToken, String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo, ClassLoader classLoader) {
        Throwable th;
        Configuration configuration = overrideConfig;
        IBinder iBinder;
        try {
            Trace.traceBegin(8192, "ResourcesManager#getResources");
            iBinder = activityToken;
            try {
                Resources orCreateResources = getOrCreateResources(activityToken, new ResourcesKey(resDir, splitResDirs, overlayDirs, libDirs, displayId, configuration != null ? new Configuration(configuration) : null, compatInfo), classLoader != null ? classLoader : ClassLoader.getSystemClassLoader());
                Trace.traceEnd(8192);
                return orCreateResources;
            } catch (Throwable th2) {
                th = th2;
                Trace.traceEnd(8192);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            iBinder = activityToken;
            ClassLoader classLoader2 = classLoader;
            Trace.traceEnd(8192);
            throw th;
        }
    }

    public void updateResourcesForActivity(IBinder activityToken, Configuration overrideConfig, int displayId, boolean movedToDifferentDisplay) {
        Configuration configuration = overrideConfig;
        try {
            Trace.traceBegin(8192, "ResourcesManager#updateResourcesForActivity");
            synchronized (this) {
                ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
                if (!Objects.equals(activityResources.overrideConfig, configuration) || movedToDifferentDisplay) {
                    Configuration oldConfig = new Configuration(activityResources.overrideConfig);
                    if (configuration != null) {
                        activityResources.overrideConfig.setTo(configuration);
                    } else {
                        activityResources.overrideConfig.unset();
                    }
                    boolean activityHasOverrideConfig = !activityResources.overrideConfig.equals(Configuration.EMPTY);
                    int refCount = activityResources.activityResources.size();
                    for (int i = 0; i < refCount; i++) {
                        Resources resources = (Resources) ((WeakReference) activityResources.activityResources.get(i)).get();
                        if (resources != null) {
                            ResourcesKey oldKey = findKeyForResourceImplLocked(resources.getImpl());
                            if (oldKey == null) {
                                String str = TAG;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("can't find ResourcesKey for resources impl=");
                                stringBuilder.append(resources.getImpl());
                                Slog.e(str, stringBuilder.toString());
                            } else {
                                Configuration rebasedOverrideConfig = new Configuration();
                                if (configuration != null) {
                                    rebasedOverrideConfig.setTo(configuration);
                                }
                                if (activityHasOverrideConfig && oldKey.hasOverrideConfiguration()) {
                                    rebasedOverrideConfig.updateFrom(Configuration.generateDelta(oldConfig, oldKey.mOverrideConfiguration));
                                }
                                ResourcesKey newKey = new ResourcesKey(oldKey.mResDir, oldKey.mSplitResDirs, oldKey.mOverlayDirs, oldKey.mLibDirs, displayId, rebasedOverrideConfig, oldKey.mCompatInfo);
                                ResourcesImpl resourcesImpl = findResourcesImplForKeyLocked(newKey);
                                if (resourcesImpl == null) {
                                    resourcesImpl = createResourcesImpl(newKey);
                                    if (resourcesImpl != null) {
                                        this.mResourceImpls.put(newKey, new WeakReference(resourcesImpl));
                                    }
                                }
                                if (!(resourcesImpl == null || resourcesImpl == resources.getImpl())) {
                                    resources.setImpl(resourcesImpl);
                                }
                            }
                        }
                    }
                    Trace.traceEnd(8192);
                    return;
                }
                Trace.traceEnd(8192);
            }
        } catch (Throwable th) {
            Trace.traceEnd(8192);
        }
    }

    public final boolean applyConfigurationToResourcesLocked(Configuration config, CompatibilityInfo compat) {
        Configuration configuration = config;
        CompatibilityInfo compatibilityInfo = compat;
        try {
            Trace.traceBegin(8192, "ResourcesManager#applyConfigurationToResourcesLocked");
            boolean z = false;
            if (this.mResConfiguration.isOtherSeqNewer(configuration) || compatibilityInfo != null) {
                int changes = this.mResConfiguration.updateFrom(configuration);
                this.mAdjustedDisplays.clear();
                DisplayMetrics defaultDisplayMetrics = getDisplayMetrics();
                if (compatibilityInfo != null && (this.mResCompatibilityInfo == null || !this.mResCompatibilityInfo.equals(compatibilityInfo))) {
                    this.mResCompatibilityInfo = compatibilityInfo;
                    changes |= 3328;
                }
                MiuiThemeHelper.handleExtraConfigurationChanges(changes, configuration);
                Resources.updateSystemConfiguration(configuration, defaultDisplayMetrics, compatibilityInfo);
                ApplicationPackageManager.configurationChanged();
                Configuration tmpConfig = null;
                boolean z2 = true;
                int i = this.mResourceImpls.size() - 1;
                while (i >= 0) {
                    ResourcesKey key = (ResourcesKey) this.mResourceImpls.keyAt(i);
                    WeakReference<ResourcesImpl> weakImplRef = (WeakReference) this.mResourceImpls.valueAt(i);
                    ResourcesImpl r = weakImplRef != null ? (ResourcesImpl) weakImplRef.get() : null;
                    if (r != null) {
                        DisplayAdjustments daj;
                        int displayId = key.mDisplayId;
                        boolean isDefaultDisplay = displayId == 0 ? z2 : z;
                        DisplayMetrics dm = defaultDisplayMetrics;
                        boolean hasOverrideConfiguration = key.hasOverrideConfiguration();
                        if (!isDefaultDisplay) {
                        } else if (hasOverrideConfiguration) {
                            DisplayMetrics displayMetrics = dm;
                        } else {
                            r.updateConfiguration(configuration, dm, compatibilityInfo);
                        }
                        if (tmpConfig == null) {
                            tmpConfig = new Configuration();
                        }
                        tmpConfig.setTo(configuration);
                        DisplayAdjustments daj2 = r.getDisplayAdjustments();
                        if (compatibilityInfo != null) {
                            daj = new DisplayAdjustments(daj2);
                            daj.setCompatibilityInfo(compatibilityInfo);
                        } else {
                            daj = daj2;
                        }
                        DisplayMetrics dm2 = getDisplayMetrics(displayId, daj);
                        if (!isDefaultDisplay) {
                            applyNonDefaultDisplayMetricsToConfiguration(dm2, tmpConfig);
                        }
                        if (hasOverrideConfiguration) {
                            tmpConfig.updateFrom(key.mOverrideConfiguration);
                        }
                        r.updateConfiguration(tmpConfig, dm2, compatibilityInfo);
                    } else {
                        this.mResourceImpls.removeAt(i);
                    }
                    i--;
                    z = false;
                    z2 = true;
                }
                boolean z3 = changes != 0;
                Trace.traceEnd(8192);
                return z3;
            }
            Trace.traceEnd(8192);
            return false;
        } catch (Throwable th) {
            Trace.traceEnd(8192);
        }
    }

    @UnsupportedAppUsage
    public void appendLibAssetForMainAssetPath(String assetPath, String libAsset) {
        appendLibAssetsForMainAssetPath(assetPath, new String[]{libAsset});
    }

    public void appendLibAssetsForMainAssetPath(String assetPath, String[] libAssets) {
        Throwable th;
        String[] strArr = libAssets;
        synchronized (this) {
            String str;
            try {
                ArrayMap<ResourcesImpl, ResourcesKey> updatedResourceKeys = new ArrayMap();
                int implCount = this.mResourceImpls.size();
                int i = 0;
                while (i < implCount) {
                    ResourcesKey key = (ResourcesKey) this.mResourceImpls.keyAt(i);
                    WeakReference<ResourcesImpl> weakImplRef = (WeakReference) this.mResourceImpls.valueAt(i);
                    ResourcesImpl impl = weakImplRef != null ? (ResourcesImpl) weakImplRef.get() : null;
                    if (impl == null) {
                        str = assetPath;
                    } else if (Objects.equals(key.mResDir, assetPath)) {
                        String[] newLibAssets = key.mLibDirs;
                        for (String libAsset : strArr) {
                            newLibAssets = (String[]) ArrayUtils.appendElement(String.class, newLibAssets, libAsset);
                        }
                        if (newLibAssets != key.mLibDirs) {
                            updatedResourceKeys.put(impl, new ResourcesKey(key.mResDir, key.mSplitResDirs, key.mOverlayDirs, newLibAssets, key.mDisplayId, key.mOverrideConfiguration, key.mCompatInfo));
                        }
                    }
                    i++;
                    strArr = libAssets;
                }
                str = assetPath;
                redirectResourcesToNewImplLocked(updatedResourceKeys);
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final void applyNewResourceDirsLocked(ApplicationInfo appInfo, String[] oldPaths) {
        Throwable th;
        ApplicationInfo applicationInfo = appInfo;
        String[] strArr;
        try {
            String[] strArr2;
            int i;
            int implCount;
            Trace.traceBegin(8192, "ResourcesManager#applyNewResourceDirsLocked");
            String baseCodePath = appInfo.getBaseCodePath();
            if (applicationInfo.uid == Process.myUid()) {
                strArr2 = applicationInfo.splitSourceDirs;
            } else {
                strArr2 = applicationInfo.splitPublicSourceDirs;
            }
            String[] copiedSplitDirs = (String[]) ArrayUtils.cloneOrNull((Object[]) strArr2);
            String[] copiedResourceDirs = (String[]) ArrayUtils.cloneOrNull(applicationInfo.resourceDirs);
            ArrayMap<ResourcesImpl, ResourcesKey> updatedResourceKeys = new ArrayMap();
            int implCount2 = this.mResourceImpls.size();
            int i2 = 0;
            while (i2 < implCount2) {
                String baseCodePath2;
                ResourcesKey key = (ResourcesKey) this.mResourceImpls.keyAt(i2);
                WeakReference<ResourcesImpl> weakImplRef = (WeakReference) this.mResourceImpls.valueAt(i2);
                ResourcesImpl impl = weakImplRef != null ? (ResourcesImpl) weakImplRef.get() : null;
                if (impl == null) {
                    baseCodePath2 = baseCodePath;
                    i = i2;
                    implCount = implCount2;
                } else {
                    if (key.mResDir == null) {
                        strArr = oldPaths;
                    } else if (key.mResDir.equals(baseCodePath)) {
                        strArr = oldPaths;
                    } else if (!ArrayUtils.contains((Object[]) oldPaths, key.mResDir)) {
                        baseCodePath2 = baseCodePath;
                        i = i2;
                        implCount = implCount2;
                    }
                    String[] strArr3 = key.mLibDirs;
                    i = key.mDisplayId;
                    Configuration configuration = key.mOverrideConfiguration;
                    int implCount3 = implCount2;
                    CompatibilityInfo compatibilityInfo = key.mCompatInfo;
                    ResourcesKey resourcesKey = r5;
                    String str = baseCodePath;
                    baseCodePath2 = baseCodePath;
                    ResourcesImpl baseCodePath3 = impl;
                    String[] strArr4 = strArr3;
                    int i3 = i;
                    i = i2;
                    Configuration configuration2 = configuration;
                    implCount = implCount3;
                    ResourcesKey resourcesKey2 = new ResourcesKey(str, copiedSplitDirs, copiedResourceDirs, strArr4, i3, configuration2, compatibilityInfo);
                    updatedResourceKeys.put(baseCodePath3, resourcesKey);
                }
                i2 = i + 1;
                implCount2 = implCount;
                baseCodePath = baseCodePath2;
                implCount = appInfo;
            }
            i = i2;
            implCount = implCount2;
            redirectResourcesToNewImplLocked(updatedResourceKeys);
            Trace.traceEnd(8192);
        } catch (Throwable th2) {
            th = th2;
            Trace.traceEnd(8192);
            throw th;
        }
    }

    private void redirectResourcesToNewImplLocked(ArrayMap<ResourcesImpl, ResourcesKey> updatedResourceKeys) {
        if (!updatedResourceKeys.isEmpty()) {
            int resourcesCount = this.mResourceReferences.size();
            int i = 0;
            while (true) {
                String str = "failed to redirect ResourcesImpl";
                Resources r = null;
                if (i < resourcesCount) {
                    WeakReference<Resources> ref = (WeakReference) this.mResourceReferences.get(i);
                    if (ref != null) {
                        r = (Resources) ref.get();
                    }
                    if (r != null) {
                        ResourcesKey key = (ResourcesKey) updatedResourceKeys.get(r.getImpl());
                        if (key != null) {
                            ResourcesImpl impl = findOrCreateResourcesImplForKeyLocked(key);
                            if (impl != null) {
                                r.setImpl(impl);
                            } else {
                                throw new NotFoundException(str);
                            }
                        }
                        continue;
                    }
                    i++;
                } else {
                    for (ActivityResources activityResources : this.mActivityResourceReferences.values()) {
                        int resCount = activityResources.activityResources.size();
                        for (int i2 = 0; i2 < resCount; i2++) {
                            WeakReference<Resources> ref2 = (WeakReference) activityResources.activityResources.get(i2);
                            Resources r2 = ref2 != null ? (Resources) ref2.get() : null;
                            if (r2 != null) {
                                ResourcesKey key2 = (ResourcesKey) updatedResourceKeys.get(r2.getImpl());
                                if (key2 != null) {
                                    ResourcesImpl impl2 = findOrCreateResourcesImplForKeyLocked(key2);
                                    if (impl2 != null) {
                                        r2.setImpl(impl2);
                                    } else {
                                        throw new NotFoundException(str);
                                    }
                                }
                                continue;
                            }
                        }
                    }
                    return;
                }
            }
        }
    }
}
