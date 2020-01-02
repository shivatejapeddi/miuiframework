package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.SharedLibraryInfo;
import android.os.Build.VERSION;
import android.util.ArrayMap;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationLoaders {
    private static final String TAG = "ApplicationLoaders";
    private static final ApplicationLoaders gApplicationLoaders = new ApplicationLoaders();
    @UnsupportedAppUsage
    private final ArrayMap<String, ClassLoader> mLoaders = new ArrayMap();
    private Map<String, CachedClassLoader> mSystemLibsCacheMap = null;

    private static class CachedClassLoader {
        ClassLoader loader;
        List<ClassLoader> sharedLibraries;

        private CachedClassLoader() {
        }
    }

    @UnsupportedAppUsage
    public static ApplicationLoaders getDefault() {
        return gApplicationLoaders;
    }

    /* Access modifiers changed, original: 0000 */
    public ClassLoader getClassLoader(String zip, int targetSdkVersion, boolean isBundled, String librarySearchPath, String libraryPermittedPath, ClassLoader parent, String classLoaderName) {
        return getClassLoaderWithSharedLibraries(zip, targetSdkVersion, isBundled, librarySearchPath, libraryPermittedPath, parent, classLoaderName, null);
    }

    /* Access modifiers changed, original: 0000 */
    public ClassLoader getClassLoaderWithSharedLibraries(String zip, int targetSdkVersion, boolean isBundled, String librarySearchPath, String libraryPermittedPath, ClassLoader parent, String classLoaderName, List<ClassLoader> sharedLibraries) {
        return getClassLoader(zip, targetSdkVersion, isBundled, librarySearchPath, libraryPermittedPath, parent, zip, classLoaderName, sharedLibraries);
    }

    /* Access modifiers changed, original: 0000 */
    public ClassLoader getSharedLibraryClassLoaderWithSharedLibraries(String zip, int targetSdkVersion, boolean isBundled, String librarySearchPath, String libraryPermittedPath, ClassLoader parent, String classLoaderName, List<ClassLoader> sharedLibraries) {
        ClassLoader loader = getCachedNonBootclasspathSystemLib(zip, parent, classLoaderName, sharedLibraries);
        if (loader != null) {
            return loader;
        }
        return getClassLoaderWithSharedLibraries(zip, targetSdkVersion, isBundled, librarySearchPath, libraryPermittedPath, parent, classLoaderName, sharedLibraries);
    }

    /* JADX WARNING: Missing block: B:19:0x005f, code skipped:
            return r2;
     */
    private java.lang.ClassLoader getClassLoader(java.lang.String r18, int r19, boolean r20, java.lang.String r21, java.lang.String r22, java.lang.ClassLoader r23, java.lang.String r24, java.lang.String r25, java.util.List<java.lang.ClassLoader> r26) {
        /*
        r17 = this;
        r1 = r17;
        r10 = r18;
        r11 = r24;
        r0 = java.lang.ClassLoader.getSystemClassLoader();
        r12 = r0.getParent();
        r13 = r1.mLoaders;
        monitor-enter(r13);
        if (r23 != 0) goto L_0x0016;
    L_0x0013:
        r0 = r12;
        r14 = r0;
        goto L_0x0018;
    L_0x0016:
        r14 = r23;
    L_0x0018:
        r8 = 64;
        if (r14 != r12) goto L_0x0066;
    L_0x001c:
        r0 = r1.mLoaders;	 Catch:{ all -> 0x0060 }
        r0 = r0.get(r11);	 Catch:{ all -> 0x0060 }
        r0 = (java.lang.ClassLoader) r0;	 Catch:{ all -> 0x0060 }
        if (r0 == 0) goto L_0x0028;
    L_0x0026:
        monitor-exit(r13);	 Catch:{ all -> 0x0060 }
        return r0;
    L_0x0028:
        android.os.Trace.traceBegin(r8, r10);	 Catch:{ all -> 0x0060 }
        r2 = r18;
        r3 = r21;
        r4 = r22;
        r5 = r14;
        r6 = r19;
        r7 = r20;
        r15 = r8;
        r8 = r25;
        r9 = r26;
        r2 = com.android.internal.os.ClassLoaderFactory.createClassLoader(r2, r3, r4, r5, r6, r7, r8, r9);	 Catch:{ all -> 0x0060 }
        android.os.Trace.traceEnd(r15);	 Catch:{ all -> 0x0060 }
        r3 = "setLayerPaths";
        r4 = r15;
        android.os.Trace.traceBegin(r4, r3);	 Catch:{ all -> 0x0060 }
        r3 = android.os.GraphicsEnvironment.getInstance();	 Catch:{ all -> 0x0060 }
        r6 = r21;
        r7 = r22;
        r3.setLayerPaths(r2, r6, r7);	 Catch:{ all -> 0x007e }
        android.os.Trace.traceEnd(r4);	 Catch:{ all -> 0x007e }
        if (r11 == 0) goto L_0x005e;
    L_0x0059:
        r3 = r1.mLoaders;	 Catch:{ all -> 0x007e }
        r3.put(r11, r2);	 Catch:{ all -> 0x007e }
    L_0x005e:
        monitor-exit(r13);	 Catch:{ all -> 0x007e }
        return r2;
    L_0x0060:
        r0 = move-exception;
        r6 = r21;
        r7 = r22;
        goto L_0x007f;
    L_0x0066:
        r6 = r21;
        r7 = r22;
        r4 = r8;
        android.os.Trace.traceBegin(r4, r10);	 Catch:{ all -> 0x007e }
        r0 = 0;
        r2 = r25;
        r3 = r26;
        r0 = com.android.internal.os.ClassLoaderFactory.createClassLoader(r10, r0, r14, r2, r3);	 Catch:{ all -> 0x007c }
        android.os.Trace.traceEnd(r4);	 Catch:{ all -> 0x007c }
        monitor-exit(r13);	 Catch:{ all -> 0x007c }
        return r0;
    L_0x007c:
        r0 = move-exception;
        goto L_0x0083;
    L_0x007e:
        r0 = move-exception;
    L_0x007f:
        r2 = r25;
        r3 = r26;
    L_0x0083:
        monitor-exit(r13);	 Catch:{ all -> 0x007c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationLoaders.getClassLoader(java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.ClassLoader, java.lang.String, java.lang.String, java.util.List):java.lang.ClassLoader");
    }

    public void createAndCacheNonBootclasspathSystemClassLoaders(SharedLibraryInfo[] libs) {
        if (this.mSystemLibsCacheMap == null) {
            this.mSystemLibsCacheMap = new HashMap();
            for (SharedLibraryInfo lib : libs) {
                createAndCacheNonBootclasspathSystemClassLoader(lib);
            }
            return;
        }
        throw new IllegalStateException("Already cached.");
    }

    private void createAndCacheNonBootclasspathSystemClassLoader(SharedLibraryInfo lib) {
        ArrayList<ClassLoader> sharedLibraries;
        String path = lib.getPath();
        List<SharedLibraryInfo> dependencies = lib.getDependencies();
        if (dependencies != null) {
            ArrayList<ClassLoader> sharedLibraries2 = new ArrayList(dependencies.size());
            for (SharedLibraryInfo dependency : dependencies) {
                String dependencyPath = dependency.getPath();
                CachedClassLoader cached = (CachedClassLoader) this.mSystemLibsCacheMap.get(dependencyPath);
                if (cached != null) {
                    sharedLibraries2.add(cached.loader);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to find dependency ");
                    stringBuilder.append(dependencyPath);
                    stringBuilder.append(" of cachedlibrary ");
                    stringBuilder.append(path);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            sharedLibraries = sharedLibraries2;
        } else {
            sharedLibraries = null;
        }
        ClassLoader classLoader = getClassLoader(path, VERSION.SDK_INT, true, null, null, null, null, null, sharedLibraries);
        StringBuilder stringBuilder2;
        if (classLoader != null) {
            CachedClassLoader cached2 = new CachedClassLoader();
            cached2.loader = classLoader;
            cached2.sharedLibraries = sharedLibraries;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Created zygote-cached class loader: ");
            stringBuilder2.append(path);
            Log.d(TAG, stringBuilder2.toString());
            this.mSystemLibsCacheMap.put(path, cached2);
            return;
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Failed to cache ");
        stringBuilder2.append(path);
        throw new IllegalStateException(stringBuilder2.toString());
    }

    private static boolean sharedLibrariesEquals(List<ClassLoader> lhs, List<ClassLoader> rhs) {
        if (lhs != null) {
            return lhs.equals(rhs);
        }
        return rhs == null;
    }

    public ClassLoader getCachedNonBootclasspathSystemLib(String zip, ClassLoader parent, String classLoaderName, List<ClassLoader> sharedLibraries) {
        Map map = this.mSystemLibsCacheMap;
        if (map == null || parent != null || classLoaderName != null) {
            return null;
        }
        CachedClassLoader cached = (CachedClassLoader) map.get(zip);
        if (cached == null) {
            return null;
        }
        boolean sharedLibrariesEquals = sharedLibrariesEquals(sharedLibraries, cached.sharedLibraries);
        String str = TAG;
        if (sharedLibrariesEquals) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Returning zygote-cached class loader: ");
            stringBuilder.append(zip);
            Log.d(str, stringBuilder.toString());
            return cached.loader;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Unexpected environment for cached library: (");
        stringBuilder2.append(sharedLibraries);
        stringBuilder2.append("|");
        stringBuilder2.append(cached.sharedLibraries);
        stringBuilder2.append(")");
        Log.w(str, stringBuilder2.toString());
        return null;
    }

    public ClassLoader createAndCacheWebViewClassLoader(String packagePath, String libsPath, String cacheKey) {
        return getClassLoader(packagePath, VERSION.SDK_INT, false, libsPath, null, null, cacheKey, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void addPath(ClassLoader classLoader, String dexPath) {
        if (classLoader instanceof PathClassLoader) {
            ((PathClassLoader) classLoader).addDexPath(dexPath);
            return;
        }
        throw new IllegalStateException("class loader is not a PathClassLoader");
    }

    /* Access modifiers changed, original: 0000 */
    public void addNative(ClassLoader classLoader, Collection<String> libPaths) {
        if (classLoader instanceof PathClassLoader) {
            ((PathClassLoader) classLoader).addNativePath(libPaths);
            return;
        }
        throw new IllegalStateException("class loader is not a PathClassLoader");
    }
}
