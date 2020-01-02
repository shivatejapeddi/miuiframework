package android.filterfw.core;

import android.util.Log;
import java.util.HashSet;
import java.util.Iterator;

public class FilterFactory {
    private static final String TAG = "FilterFactory";
    private static Object mClassLoaderGuard = new Object();
    private static ClassLoader mCurrentClassLoader = Thread.currentThread().getContextClassLoader();
    private static HashSet<String> mLibraries = new HashSet();
    private static boolean mLogVerbose = Log.isLoggable(TAG, 2);
    private static FilterFactory mSharedFactory;
    private HashSet<String> mPackages = new HashSet();

    public static FilterFactory sharedFactory() {
        if (mSharedFactory == null) {
            mSharedFactory = new FilterFactory();
        }
        return mSharedFactory;
    }

    /* JADX WARNING: Missing block: B:12:0x0031, code skipped:
            return;
     */
    public static void addFilterLibrary(java.lang.String r3) {
        /*
        r0 = mLogVerbose;
        if (r0 == 0) goto L_0x001a;
    L_0x0004:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Adding filter library ";
        r0.append(r1);
        r0.append(r3);
        r0 = r0.toString();
        r1 = "FilterFactory";
        android.util.Log.v(r1, r0);
    L_0x001a:
        r0 = mClassLoaderGuard;
        monitor-enter(r0);
        r1 = mLibraries;	 Catch:{ all -> 0x0042 }
        r1 = r1.contains(r3);	 Catch:{ all -> 0x0042 }
        if (r1 == 0) goto L_0x0032;
    L_0x0025:
        r1 = mLogVerbose;	 Catch:{ all -> 0x0042 }
        if (r1 == 0) goto L_0x0030;
    L_0x0029:
        r1 = "FilterFactory";
        r2 = "Library already added";
        android.util.Log.v(r1, r2);	 Catch:{ all -> 0x0042 }
    L_0x0030:
        monitor-exit(r0);	 Catch:{ all -> 0x0042 }
        return;
    L_0x0032:
        r1 = mLibraries;	 Catch:{ all -> 0x0042 }
        r1.add(r3);	 Catch:{ all -> 0x0042 }
        r1 = new dalvik.system.PathClassLoader;	 Catch:{ all -> 0x0042 }
        r2 = mCurrentClassLoader;	 Catch:{ all -> 0x0042 }
        r1.<init>(r3, r2);	 Catch:{ all -> 0x0042 }
        mCurrentClassLoader = r1;	 Catch:{ all -> 0x0042 }
        monitor-exit(r0);	 Catch:{ all -> 0x0042 }
        return;
    L_0x0042:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0042 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.filterfw.core.FilterFactory.addFilterLibrary(java.lang.String):void");
    }

    public void addPackage(String packageName) {
        if (mLogVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Adding package ");
            stringBuilder.append(packageName);
            Log.v(TAG, stringBuilder.toString());
        }
        this.mPackages.add(packageName);
    }

    public Filter createFilterByClassName(String className, String filterName) {
        if (mLogVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Looking up class ");
            stringBuilder.append(className);
            Log.v(TAG, stringBuilder.toString());
        }
        Class filterClass = null;
        Iterator it = this.mPackages.iterator();
        while (it.hasNext()) {
            String packageName = (String) it.next();
            try {
                if (mLogVerbose) {
                    String str = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Trying ");
                    stringBuilder2.append(packageName);
                    stringBuilder2.append(".");
                    stringBuilder2.append(className);
                    Log.v(str, stringBuilder2.toString());
                }
                synchronized (mClassLoaderGuard) {
                    ClassLoader classLoader = mCurrentClassLoader;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(packageName);
                    stringBuilder3.append(".");
                    stringBuilder3.append(className);
                    filterClass = classLoader.loadClass(stringBuilder3.toString());
                }
                if (filterClass != null) {
                    break;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        if (filterClass != null) {
            return createFilterByClass(filterClass, filterName);
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Unknown filter class '");
        stringBuilder4.append(className);
        stringBuilder4.append("'!");
        throw new IllegalArgumentException(stringBuilder4.toString());
    }

    public Filter createFilterByClass(Class filterClass, String filterName) {
        try {
            filterClass.asSubclass(Filter.class);
            StringBuilder stringBuilder;
            try {
                Filter filter = null;
                try {
                    filter = (Filter) filterClass.getConstructor(new Class[]{String.class}).newInstance(new Object[]{filterName});
                } catch (Throwable th) {
                }
                if (filter != null) {
                    return filter;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not construct the filter '");
                stringBuilder.append(filterName);
                stringBuilder.append("'!");
                throw new IllegalArgumentException(stringBuilder.toString());
            } catch (NoSuchMethodException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("The filter class '");
                stringBuilder.append(filterClass);
                stringBuilder.append("' does not have a constructor of the form <init>(String name)!");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (ClassCastException e2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Attempting to allocate class '");
            stringBuilder2.append(filterClass);
            stringBuilder2.append("' which is not a subclass of Filter!");
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }
}
