package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.admin.DevicePolicyManager;
import android.content.AutofillOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.CompatResources;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.miui.ResourcesManager;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.MediaStore.Files;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.autofill.AutofillManager.AutofillClient;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.Preconditions;
import dalvik.system.BlockGuard;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import libcore.io.Memory;

class ContextImpl extends Context {
    private static final boolean DEBUG = false;
    static final int STATE_INITIALIZING = 1;
    static final int STATE_NOT_FOUND = 3;
    static final int STATE_READY = 2;
    static final int STATE_UNINITIALIZED = 0;
    private static final String TAG = "ContextImpl";
    private static final String XATTR_INODE_CACHE = "user.inode_cache";
    private static final String XATTR_INODE_CODE_CACHE = "user.inode_code_cache";
    @GuardedBy({"ContextImpl.class"})
    @UnsupportedAppUsage
    private static ArrayMap<String, ArrayMap<File, SharedPreferencesImpl>> sSharedPrefsCache;
    private final IBinder mActivityToken;
    private AutofillClient mAutofillClient = null;
    private AutofillOptions mAutofillOptions;
    @UnsupportedAppUsage
    private final String mBasePackageName;
    @GuardedBy({"mSync"})
    private File mCacheDir;
    @UnsupportedAppUsage
    private ClassLoader mClassLoader;
    @GuardedBy({"mSync"})
    private File mCodeCacheDir;
    private ContentCaptureOptions mContentCaptureOptions = null;
    @UnsupportedAppUsage
    private final ApplicationContentResolver mContentResolver;
    @GuardedBy({"mSync"})
    private File mDatabasesDir;
    private Display mDisplay;
    @GuardedBy({"mSync"})
    private File mFilesDir;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final int mFlags;
    @UnsupportedAppUsage
    final ActivityThread mMainThread;
    @GuardedBy({"mSync"})
    private File mNoBackupFilesDir;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final String mOpPackageName;
    @UnsupportedAppUsage
    private Context mOuterContext = this;
    @UnsupportedAppUsage
    final LoadedApk mPackageInfo;
    @UnsupportedAppUsage
    private PackageManager mPackageManager;
    @GuardedBy({"mSync"})
    @UnsupportedAppUsage
    private File mPreferencesDir;
    private Context mReceiverRestrictedContext = null;
    @UnsupportedAppUsage
    private Resources mResources;
    private final ResourcesManager mResourcesManager;
    @UnsupportedAppUsage
    final Object[] mServiceCache = SystemServiceRegistry.createServiceCache();
    final int[] mServiceInitializationStateArray = new int[this.mServiceCache.length];
    @GuardedBy({"ContextImpl.class"})
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private ArrayMap<String, File> mSharedPrefsPaths;
    private String mSplitName = null;
    private final Object mSync = new Object();
    @UnsupportedAppUsage
    private Theme mTheme = null;
    @UnsupportedAppUsage
    private int mThemeResource = 0;
    private final UserHandle mUser;

    private static final class ApplicationContentResolver extends ContentResolver {
        @UnsupportedAppUsage
        private final ActivityThread mMainThread;
        private UserHandle mUser = UserHandle.of(getUserId());

        public ApplicationContentResolver(Context context, ActivityThread mainThread) {
            super(context);
            this.mMainThread = (ActivityThread) Preconditions.checkNotNull(mainThread);
        }

        public ApplicationContentResolver(Context context, ActivityThread mainThread, UserHandle user) {
            super(context);
            this.mMainThread = (ActivityThread) Preconditions.checkNotNull(mainThread);
            this.mUser = (UserHandle) Preconditions.checkNotNull(user);
        }

        /* Access modifiers changed, original: protected */
        @UnsupportedAppUsage
        public IContentProvider acquireProvider(Context context, String auth) {
            return this.mMainThread.acquireProvider(context, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        /* Access modifiers changed, original: protected */
        public IContentProvider acquireExistingProvider(Context context, String auth) {
            return this.mMainThread.acquireExistingProvider(context, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        public boolean releaseProvider(IContentProvider provider) {
            return this.mMainThread.releaseProvider(provider, true);
        }

        /* Access modifiers changed, original: protected */
        public IContentProvider acquireUnstableProvider(Context c, String auth) {
            return this.mMainThread.acquireProvider(c, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), false);
        }

        public boolean releaseUnstableProvider(IContentProvider icp) {
            return this.mMainThread.releaseProvider(icp, false);
        }

        public void unstableProviderDied(IContentProvider icp) {
            this.mMainThread.handleUnstableProviderDied(icp.asBinder(), true);
        }

        public void appNotRespondingViaProvider(IContentProvider icp) {
            this.mMainThread.appNotRespondingViaProvider(icp.asBinder());
        }

        /* Access modifiers changed, original: protected */
        public int resolveUserIdFromAuthority(String auth) {
            return ContentProvider.getUserIdFromAuthority(auth, this.mUser.getIdentifier());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface ServiceInitializationState {
    }

    @UnsupportedAppUsage
    static ContextImpl getImpl(Context context) {
        while (context instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            Context nextContext = baseContext;
            if (baseContext == null) {
                break;
            }
            context = nextContext;
        }
        return (ContextImpl) context;
    }

    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    public Resources getResources() {
        return this.mResources;
    }

    public PackageManager getPackageManager() {
        PackageManager packageManager = this.mPackageManager;
        if (packageManager != null) {
            return packageManager;
        }
        IPackageManager pm = ActivityThread.getPackageManager();
        if (pm == null) {
            return null;
        }
        ApplicationPackageManager applicationPackageManager = new ApplicationPackageManager(this, pm);
        this.mPackageManager = applicationPackageManager;
        return applicationPackageManager;
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public ContentResolver getContentResolverForUser(UserHandle userHandle) {
        return new ApplicationContentResolver(this, this.mMainThread, userHandle);
    }

    public Looper getMainLooper() {
        return this.mMainThread.getLooper();
    }

    public Executor getMainExecutor() {
        return this.mMainThread.getExecutor();
    }

    public Context getApplicationContext() {
        LoadedApk loadedApk = this.mPackageInfo;
        return loadedApk != null ? loadedApk.getApplication() : this.mMainThread.getApplication();
    }

    public void setTheme(int resId) {
        synchronized (this.mSync) {
            if (this.mThemeResource != resId) {
                this.mThemeResource = resId;
                initializeTheme();
            }
        }
    }

    public int getThemeResId() {
        int i;
        synchronized (this.mSync) {
            i = this.mThemeResource;
        }
        return i;
    }

    public Theme getTheme() {
        synchronized (this.mSync) {
            Theme theme;
            if (this.mTheme != null) {
                theme = this.mTheme;
                return theme;
            }
            this.mThemeResource = Resources.selectDefaultTheme(this.mThemeResource, getOuterContext().getApplicationInfo().targetSdkVersion);
            initializeTheme();
            theme = this.mTheme;
            return theme;
        }
    }

    private void initializeTheme() {
        if (this.mTheme == null) {
            this.mTheme = this.mResources.newTheme();
        }
        this.mTheme.applyStyle(this.mThemeResource, true);
    }

    public ClassLoader getClassLoader() {
        ClassLoader classLoader = this.mClassLoader;
        if (classLoader != null) {
            return classLoader;
        }
        LoadedApk loadedApk = this.mPackageInfo;
        return loadedApk != null ? loadedApk.getClassLoader() : ClassLoader.getSystemClassLoader();
    }

    public String getPackageName() {
        LoadedApk loadedApk = this.mPackageInfo;
        if (loadedApk != null) {
            return loadedApk.getPackageName();
        }
        return "android";
    }

    public String getBasePackageName() {
        String str = this.mBasePackageName;
        return str != null ? str : getPackageName();
    }

    public String getOpPackageName() {
        String str = this.mOpPackageName;
        return str != null ? str : getBasePackageName();
    }

    public ApplicationInfo getApplicationInfo() {
        LoadedApk loadedApk = this.mPackageInfo;
        if (loadedApk != null) {
            return loadedApk.getApplicationInfo();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public String getPackageResourcePath() {
        LoadedApk loadedApk = this.mPackageInfo;
        if (loadedApk != null) {
            return loadedApk.getResDir();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public String getPackageCodePath() {
        LoadedApk loadedApk = this.mPackageInfo;
        if (loadedApk != null) {
            return loadedApk.getAppDir();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        File file;
        if (this.mPackageInfo.getApplicationInfo().targetSdkVersion < 19 && name == null) {
            name = "null";
        }
        synchronized (ContextImpl.class) {
            if (this.mSharedPrefsPaths == null) {
                this.mSharedPrefsPaths = new ArrayMap();
            }
            file = (File) this.mSharedPrefsPaths.get(name);
            if (file == null) {
                file = getSharedPreferencesPath(name);
                this.mSharedPrefsPaths.put(name, file);
            }
        }
        return getSharedPreferences(file, mode);
    }

    /* JADX WARNING: Missing block: B:19:0x004b, code skipped:
            if ((r7 & 4) != 0) goto L_0x0057;
     */
    /* JADX WARNING: Missing block: B:21:0x0055, code skipped:
            if (getApplicationInfo().targetSdkVersion >= 11) goto L_0x005a;
     */
    /* JADX WARNING: Missing block: B:22:0x0057, code skipped:
            r2.startReloadIfChangedUnexpectedly();
     */
    /* JADX WARNING: Missing block: B:23:0x005a, code skipped:
            return r2;
     */
    public android.content.SharedPreferences getSharedPreferences(java.io.File r6, int r7) {
        /*
        r5 = this;
        r0 = android.app.ContextImpl.class;
        monitor-enter(r0);
        r1 = r5.getSharedPreferencesCacheLocked();	 Catch:{ all -> 0x005b }
        r2 = r1.get(r6);	 Catch:{ all -> 0x005b }
        r2 = (android.app.SharedPreferencesImpl) r2;	 Catch:{ all -> 0x005b }
        if (r2 != 0) goto L_0x0048;
    L_0x000f:
        r5.checkMode(r7);	 Catch:{ all -> 0x005b }
        r3 = r5.getApplicationInfo();	 Catch:{ all -> 0x005b }
        r3 = r3.targetSdkVersion;	 Catch:{ all -> 0x005b }
        r4 = 26;
        if (r3 < r4) goto L_0x003d;
    L_0x001c:
        r3 = r5.isCredentialProtectedStorage();	 Catch:{ all -> 0x005b }
        if (r3 == 0) goto L_0x003d;
    L_0x0022:
        r3 = android.os.UserManager.class;
        r3 = r5.getSystemService(r3);	 Catch:{ all -> 0x005b }
        r3 = (android.os.UserManager) r3;	 Catch:{ all -> 0x005b }
        r4 = android.os.UserHandle.myUserId();	 Catch:{ all -> 0x005b }
        r3 = r3.isUserUnlockingOrUnlocked(r4);	 Catch:{ all -> 0x005b }
        if (r3 == 0) goto L_0x0035;
    L_0x0034:
        goto L_0x003d;
    L_0x0035:
        r3 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x005b }
        r4 = "SharedPreferences in credential encrypted storage are not available until after user is unlocked";
        r3.<init>(r4);	 Catch:{ all -> 0x005b }
        throw r3;	 Catch:{ all -> 0x005b }
    L_0x003d:
        r3 = new android.app.SharedPreferencesImpl;	 Catch:{ all -> 0x005b }
        r3.<init>(r6, r7);	 Catch:{ all -> 0x005b }
        r2 = r3;
        r1.put(r6, r2);	 Catch:{ all -> 0x005b }
        monitor-exit(r0);	 Catch:{ all -> 0x005b }
        return r2;
    L_0x0048:
        monitor-exit(r0);	 Catch:{ all -> 0x005b }
        r0 = r7 & 4;
        if (r0 != 0) goto L_0x0057;
    L_0x004d:
        r0 = r5.getApplicationInfo();
        r0 = r0.targetSdkVersion;
        r1 = 11;
        if (r0 >= r1) goto L_0x005a;
    L_0x0057:
        r2.startReloadIfChangedUnexpectedly();
    L_0x005a:
        return r2;
    L_0x005b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x005b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ContextImpl.getSharedPreferences(java.io.File, int):android.content.SharedPreferences");
    }

    @GuardedBy({"ContextImpl.class"})
    private ArrayMap<File, SharedPreferencesImpl> getSharedPreferencesCacheLocked() {
        if (sSharedPrefsCache == null) {
            sSharedPrefsCache = new ArrayMap();
        }
        String packageName = getPackageName();
        ArrayMap<File, SharedPreferencesImpl> packagePrefs = (ArrayMap) sSharedPrefsCache.get(packageName);
        if (packagePrefs != null) {
            return packagePrefs;
        }
        packagePrefs = new ArrayMap();
        sSharedPrefsCache.put(packageName, packagePrefs);
        return packagePrefs;
    }

    public void reloadSharedPreferences() {
        ArrayList<SharedPreferencesImpl> spImpls = new ArrayList();
        synchronized (ContextImpl.class) {
            ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
            for (int i = 0; i < cache.size(); i++) {
                SharedPreferencesImpl sp = (SharedPreferencesImpl) cache.valueAt(i);
                if (sp != null) {
                    spImpls.add(sp);
                }
            }
        }
        for (int i2 = 0; i2 < spImpls.size(); i2++) {
            ((SharedPreferencesImpl) spImpls.get(i2)).startReloadIfChangedUnexpectedly();
        }
    }

    private static int moveFiles(File sourceDir, File targetDir, final String prefix) {
        File[] sourceFiles = FileUtils.listFilesOrEmpty(sourceDir, new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix);
            }
        });
        int res = 0;
        int length = sourceFiles.length;
        int i = 0;
        while (i < length) {
            File sourceFile = sourceFiles[i];
            File targetFile = new File(targetDir, sourceFile.getName());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Migrating ");
            stringBuilder.append(sourceFile);
            stringBuilder.append(" to ");
            stringBuilder.append(targetFile);
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Log.d(str, stringBuilder2);
            StringBuilder stringBuilder3;
            try {
                FileUtils.copyFileOrThrow(sourceFile, targetFile);
                FileUtils.copyPermissions(sourceFile, targetFile);
                if (sourceFile.delete()) {
                    if (res != -1) {
                        res++;
                    }
                    i++;
                } else {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Failed to clean up ");
                    stringBuilder3.append(sourceFile);
                    throw new IOException(stringBuilder3.toString());
                }
            } catch (IOException e) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Failed to migrate ");
                stringBuilder3.append(sourceFile);
                stringBuilder3.append(": ");
                stringBuilder3.append(e);
                Log.w(str, stringBuilder3.toString());
                res = -1;
            }
        }
        return res;
    }

    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        boolean z;
        synchronized (ContextImpl.class) {
            File source = sourceContext.getSharedPreferencesPath(name);
            File target = getSharedPreferencesPath(name);
            int res = moveFiles(source.getParentFile(), target.getParentFile(), source.getName());
            if (res > 0) {
                ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
                cache.remove(source);
                cache.remove(target);
            }
            z = res != -1;
        }
        return z;
    }

    public boolean deleteSharedPreferences(String name) {
        boolean z;
        synchronized (ContextImpl.class) {
            File prefs = getSharedPreferencesPath(name);
            File prefsBackup = SharedPreferencesImpl.makeBackupFile(prefs);
            getSharedPreferencesCacheLocked().remove(prefs);
            prefs.delete();
            prefsBackup.delete();
            z = (prefs.exists() || prefsBackup.exists()) ? false : true;
        }
        return z;
    }

    @UnsupportedAppUsage
    private File getPreferencesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mPreferencesDir == null) {
                this.mPreferencesDir = new File(getDataDir(), "shared_prefs");
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mPreferencesDir);
        }
        return ensurePrivateDirExists;
    }

    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return new FileInputStream(makeFilename(getFilesDir(), name));
    }

    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        checkMode(mode);
        boolean append = (32768 & mode) != 0;
        File f = makeFilename(getFilesDir(), name);
        try {
            FileOutputStream fos = new FileOutputStream(f, append);
            setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos;
        } catch (FileNotFoundException e) {
            File parent = f.getParentFile();
            parent.mkdir();
            FileUtils.setPermissions(parent.getPath(), 505, -1, -1);
            FileOutputStream fos2 = new FileOutputStream(f, append);
            setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos2;
        }
    }

    public boolean deleteFile(String name) {
        return makeFilename(getFilesDir(), name).delete();
    }

    private static File ensurePrivateDirExists(File file) {
        return ensurePrivateDirExists(file, 505, -1, null);
    }

    private static File ensurePrivateCacheDirExists(File file, String xattr) {
        return ensurePrivateDirExists(file, MetricsEvent.FIELD_PROCESS_RECORD_PROCESS_NAME, UserHandle.getCacheAppGid(Process.myUid()), xattr);
    }

    private static File ensurePrivateDirExists(File file, int mode, int gid, String xattr) {
        StringBuilder stringBuilder;
        String str = ": ";
        String str2 = TAG;
        if (!file.exists()) {
            String path = file.getAbsolutePath();
            try {
                Os.mkdir(path, mode);
                Os.chmod(path, mode);
                if (gid != -1) {
                    Os.chown(path, -1, gid);
                }
            } catch (ErrnoException e) {
                if (e.errno != OsConstants.EEXIST) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to ensure ");
                    stringBuilder.append(file);
                    stringBuilder.append(str);
                    stringBuilder.append(e.getMessage());
                    Log.w(str2, stringBuilder.toString());
                }
            }
            if (xattr != null) {
                try {
                    byte[] value = new byte[8];
                    Memory.pokeLong(value, 0, Os.stat(file.getAbsolutePath()).st_ino, ByteOrder.nativeOrder());
                    Os.setxattr(file.getParentFile().getAbsolutePath(), xattr, value, 0);
                } catch (ErrnoException e2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to update ");
                    stringBuilder.append(xattr);
                    stringBuilder.append(str);
                    stringBuilder.append(e2.getMessage());
                    Log.w(str2, stringBuilder.toString());
                }
            }
        }
        return file;
    }

    public File getFilesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mFilesDir == null) {
                this.mFilesDir = new File(getDataDir(), Files.TABLE);
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mFilesDir);
        }
        return ensurePrivateDirExists;
    }

    public File getNoBackupFilesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mNoBackupFilesDir == null) {
                this.mNoBackupFilesDir = new File(getDataDir(), "no_backup");
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mNoBackupFilesDir);
        }
        return ensurePrivateDirExists;
    }

    public File getExternalFilesDir(String type) {
        File[] dirs = getExternalFilesDirs(type);
        return (dirs == null || dirs.length <= 0) ? null : dirs[0];
    }

    public File[] getExternalFilesDirs(String type) {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            File[] dirs = Environment.buildExternalStorageAppFilesDirs(getPackageName());
            if (type != null) {
                dirs = Environment.buildPaths(dirs, type);
            }
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(dirs);
        }
        return ensureExternalDirsExistOrFilter;
    }

    public File getObbDir() {
        File[] dirs = getObbDirs();
        return (dirs == null || dirs.length <= 0) ? null : dirs[0];
    }

    public File[] getObbDirs() {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(Environment.buildExternalStorageAppObbDirs(getPackageName()));
        }
        return ensureExternalDirsExistOrFilter;
    }

    public File getCacheDir() {
        File ensurePrivateCacheDirExists;
        synchronized (this.mSync) {
            if (this.mCacheDir == null) {
                this.mCacheDir = new File(getDataDir(), "cache");
            }
            ensurePrivateCacheDirExists = ensurePrivateCacheDirExists(this.mCacheDir, XATTR_INODE_CACHE);
        }
        return ensurePrivateCacheDirExists;
    }

    public File getCodeCacheDir() {
        File ensurePrivateCacheDirExists;
        synchronized (this.mSync) {
            if (this.mCodeCacheDir == null) {
                this.mCodeCacheDir = new File(getDataDir(), "code_cache");
            }
            ensurePrivateCacheDirExists = ensurePrivateCacheDirExists(this.mCodeCacheDir, XATTR_INODE_CODE_CACHE);
        }
        return ensurePrivateCacheDirExists;
    }

    public File getExternalCacheDir() {
        File[] dirs = getExternalCacheDirs();
        return (dirs == null || dirs.length <= 0) ? null : dirs[0];
    }

    public File[] getExternalCacheDirs() {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(Environment.buildExternalStorageAppCacheDirs(getPackageName()));
        }
        return ensureExternalDirsExistOrFilter;
    }

    public File[] getExternalMediaDirs() {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(Environment.buildExternalStorageAppMediaDirs(getPackageName()));
        }
        return ensureExternalDirsExistOrFilter;
    }

    public File getPreloadsFileCache() {
        return Environment.getDataPreloadsFileCacheDirectory(getPackageName());
    }

    public File getFileStreamPath(String name) {
        return makeFilename(getFilesDir(), name);
    }

    public File getSharedPreferencesPath(String name) {
        File preferencesDir = getPreferencesDir();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(".xml");
        return makeFilename(preferencesDir, stringBuilder.toString());
    }

    public String[] fileList() {
        return FileUtils.listOrEmpty(getFilesDir());
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
        return openOrCreateDatabase(name, mode, factory, null);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler) {
        checkMode(mode);
        File f = getDatabasePath(name);
        int flags = 268435456;
        if ((mode & 8) != 0) {
            flags = 268435456 | 536870912;
        }
        if ((mode & 16) != 0) {
            flags |= 16;
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(f.getPath(), factory, flags, errorHandler);
        setFilePermissionsFromMode(f.getPath(), mode, 0);
        return db;
    }

    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        boolean z;
        synchronized (ContextImpl.class) {
            File source = sourceContext.getDatabasePath(name);
            z = moveFiles(source.getParentFile(), getDatabasePath(name).getParentFile(), source.getName()) != -1;
        }
        return z;
    }

    public boolean deleteDatabase(String name) {
        try {
            return SQLiteDatabase.deleteDatabase(getDatabasePath(name));
        } catch (Exception e) {
            return false;
        }
    }

    public File getDatabasePath(String name) {
        if (name.charAt(0) != File.separatorChar) {
            return makeFilename(getDatabasesDir(), name);
        }
        File dir = new File(name.substring(0, name.lastIndexOf(File.separatorChar)));
        File f = new File(dir, name.substring(name.lastIndexOf(File.separatorChar)));
        if (dir.isDirectory() || !dir.mkdir()) {
            return f;
        }
        FileUtils.setPermissions(dir.getPath(), 505, -1, -1);
        return f;
    }

    public String[] databaseList() {
        return FileUtils.listOrEmpty(getDatabasesDir());
    }

    private File getDatabasesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mDatabasesDir == null) {
                if ("android".equals(getPackageName())) {
                    this.mDatabasesDir = new File("/data/system");
                } else {
                    this.mDatabasesDir = new File(getDataDir(), "databases");
                }
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mDatabasesDir);
        }
        return ensurePrivateDirExists;
    }

    @Deprecated
    public Drawable getWallpaper() {
        return getWallpaperManager().getDrawable();
    }

    @Deprecated
    public Drawable peekWallpaper() {
        return getWallpaperManager().peekDrawable();
    }

    @Deprecated
    public int getWallpaperDesiredMinimumWidth() {
        return getWallpaperManager().getDesiredMinimumWidth();
    }

    @Deprecated
    public int getWallpaperDesiredMinimumHeight() {
        return getWallpaperManager().getDesiredMinimumHeight();
    }

    @Deprecated
    public void setWallpaper(Bitmap bitmap) throws IOException {
        getWallpaperManager().setBitmap(bitmap);
    }

    @Deprecated
    public void setWallpaper(InputStream data) throws IOException {
        getWallpaperManager().setStream(data);
    }

    @Deprecated
    public void clearWallpaper() throws IOException {
        getWallpaperManager().clear();
    }

    private WallpaperManager getWallpaperManager() {
        return (WallpaperManager) getSystemService(WallpaperManager.class);
    }

    public void startActivity(Intent intent) {
        warnIfCallingFromSystemProcess();
        startActivity(intent, null);
    }

    public void startActivityAsUser(Intent intent, UserHandle user) {
        startActivityAsUser(intent, null, user);
    }

    public void startActivity(Intent intent, Bundle options) {
        warnIfCallingFromSystemProcess();
        int targetSdkVersion = getApplicationInfo().targetSdkVersion;
        if ((intent.getFlags() & 268435456) != 0 || ((targetSdkVersion >= 24 && targetSdkVersion < 28) || !(options == null || ActivityOptions.fromBundle(options).getLaunchTaskId() == -1))) {
            this.mMainThread.getInstrumentation().execStartActivity(getOuterContext(), this.mMainThread.getApplicationThread(), null, (Activity) null, intent, -1, options);
            return;
        }
        throw new AndroidRuntimeException("Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?");
    }

    public void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
        try {
            ActivityTaskManager.getService().startActivityAsUser(this.mMainThread.getApplicationThread(), getBasePackageName(), intent, intent.resolveTypeIfNeeded(getContentResolver()), null, null, 0, 268435456, null, options, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startActivities(Intent[] intents) {
        warnIfCallingFromSystemProcess();
        startActivities(intents, null);
    }

    public int startActivitiesAsUser(Intent[] intents, Bundle options, UserHandle userHandle) {
        if ((intents[0].getFlags() & 268435456) != 0) {
            return this.mMainThread.getInstrumentation().execStartActivitiesAsUser(getOuterContext(), this.mMainThread.getApplicationThread(), null, (Activity) null, intents, options, userHandle.getIdentifier());
        }
        throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
    }

    public void startActivities(Intent[] intents, Bundle options) {
        warnIfCallingFromSystemProcess();
        if ((intents[0].getFlags() & 268435456) != 0) {
            this.mMainThread.getInstrumentation().execStartActivities(getOuterContext(), this.mMainThread.getApplicationThread(), null, (Activity) null, intents, options);
            return;
        }
        throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        Intent intent2 = fillInIntent;
        String resolvedType = null;
        if (intent2 != null) {
            try {
                fillInIntent.migrateExtraStreamToClipData();
                intent2.prepareToLeaveProcess(this);
                resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        int result = ActivityTaskManager.getService().startActivityIntentSender(this.mMainThread.getApplicationThread(), intent != null ? intent.getTarget() : null, intent != null ? intent.getWhitelistToken() : null, fillInIntent, resolvedType, null, null, 0, flagsMask, flagsValues, options);
        if (result != -96) {
            Instrumentation.checkStartActivityResult(result, null);
            return;
        }
        throw new SendIntentException();
    }

    public void sendBroadcast(Intent intent) {
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcast(Intent intent, String receiverPermission) {
        String[] receiverPermissions;
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcastMultiplePermissions(Intent intent, String[] receiverPermissions) {
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcastAsUserMultiplePermissions(Intent intent, UserHandle user, String[] receiverPermissions) {
        Intent intent2 = intent;
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcast(Intent intent, String receiverPermission, Bundle options) {
        String[] receiverPermissions;
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, options, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcast(Intent intent, String receiverPermission, int appOp) {
        String[] receiverPermissions;
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, appOp, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        String[] receiverPermissions;
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, true, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, -1, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, -1, resultReceiver, scheduler, initialCode, initialData, initialExtras, options);
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras, Bundle options) {
        IIntentReceiver rd;
        String[] receiverPermissions;
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        Handler scheduler2;
        Handler handler;
        if (resultReceiver == null) {
            rd = null;
        } else if (this.mPackageInfo != null) {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            handler = scheduler2;
        } else {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            handler = scheduler2;
        }
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user) {
        Intent intent2 = intent;
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {
        sendBroadcastAsUser(intent, user, receiverPermission, -1);
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, Bundle options) {
        String[] receiverPermissions;
        Intent intent2 = intent;
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, options, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp) {
        String[] receiverPermissions;
        Intent intent2 = intent;
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, appOp, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, -1, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        IIntentReceiver rd;
        String[] receiverPermissions;
        Intent intent2 = intent;
        Handler scheduler2;
        Handler handler;
        if (resultReceiver == null) {
            rd = null;
        } else if (this.mPackageInfo != null) {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            handler = scheduler2;
        } else {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            handler = scheduler2;
        }
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        if (receiverPermission == null) {
            receiverPermissions = null;
        } else {
            receiverPermissions = new String[]{receiverPermission};
        }
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void sendStickyBroadcast(Intent intent) {
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, true, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        IIntentReceiver rd;
        Intent intent2 = intent;
        warnIfCallingFromSystemProcess();
        Handler scheduler2;
        Handler handler;
        if (resultReceiver == null) {
            rd = null;
        } else if (this.mPackageInfo != null) {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            handler = scheduler2;
        } else {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            handler = scheduler2;
        }
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, null, -1, null, true, true, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void removeStickyBroadcast(Intent intent) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            intent = new Intent(intent);
            intent.setDataAndType(intent.getData(), resolvedType);
        }
        try {
            intent.prepareToLeaveProcess((Context) this);
            ActivityManager.getService().unbroadcastIntent(this.mMainThread.getApplicationThread(), intent, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
        Intent intent2 = intent;
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user, Bundle options) {
        Intent intent2 = intent;
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, options, false, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        IIntentReceiver rd;
        Intent intent2 = intent;
        Handler scheduler2;
        Handler handler;
        if (resultReceiver == null) {
            rd = null;
        } else if (this.mPackageInfo != null) {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            handler = scheduler2;
        } else {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            handler = scheduler2;
        }
        String resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
        try {
            intent2.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, null, -1, null, true, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            intent = new Intent(intent);
            intent.setDataAndType(intent.getData(), resolvedType);
        }
        try {
            intent.prepareToLeaveProcess((Context) this);
            ActivityManager.getService().unbroadcastIntent(this.mMainThread.getApplicationThread(), intent, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return registerReceiver(receiver, filter, null, null);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {
        return registerReceiver(receiver, filter, null, null, flags);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return registerReceiverInternal(receiver, getUserId(), filter, broadcastPermission, scheduler, getOuterContext(), 0);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler, int flags) {
        return registerReceiverInternal(receiver, getUserId(), filter, broadcastPermission, scheduler, getOuterContext(), flags);
    }

    public Intent registerReceiverAsUser(BroadcastReceiver receiver, UserHandle user, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return registerReceiverInternal(receiver, user.getIdentifier(), filter, broadcastPermission, scheduler, getOuterContext(), 0);
    }

    private Intent registerReceiverInternal(BroadcastReceiver receiver, int userId, IntentFilter filter, String broadcastPermission, Handler scheduler, Context context, int flags) {
        IIntentReceiver rd;
        Handler scheduler2;
        if (receiver == null) {
            rd = null;
        } else if (this.mPackageInfo == null || context == null) {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = new ReceiverDispatcher(receiver, context, scheduler2, null, true).getIIntentReceiver();
        } else {
            if (scheduler == null) {
                scheduler2 = this.mMainThread.getHandler();
            } else {
                scheduler2 = scheduler;
            }
            rd = this.mPackageInfo.getReceiverDispatcher(receiver, context, scheduler2, this.mMainThread.getInstrumentation(), true);
        }
        try {
            Intent intent = ActivityManager.getService().registerReceiver(this.mMainThread.getApplicationThread(), this.mBasePackageName, rd, filter, broadcastPermission, userId, flags);
            if (intent != null) {
                intent.setExtrasClassLoader(getClassLoader());
                intent.prepareToEnterProcess();
            }
            return intent;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        IIntentReceiver rd = this.mPackageInfo;
        if (rd != null) {
            try {
                ActivityManager.getService().unregisterReceiver(rd.forgetReceiverDispatcher(getOuterContext(), receiver));
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new RuntimeException("Not supported in system context");
    }

    private void validateServiceIntent(Intent service) {
        if (service.getComponent() != null || service.getPackage() != null) {
            return;
        }
        if (getApplicationInfo().targetSdkVersion < 21) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Implicit intents with startService are not safe: ");
            stringBuilder.append(service);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(Debug.getCallers(2, 3));
            Log.w(TAG, stringBuilder.toString());
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Service Intent must be explicit: ");
        stringBuilder2.append(service);
        throw new IllegalArgumentException(stringBuilder2.toString());
    }

    public ComponentName startService(Intent service) {
        warnIfCallingFromSystemProcess();
        return startServiceCommon(service, false, this.mUser);
    }

    public ComponentName startForegroundService(Intent service) {
        warnIfCallingFromSystemProcess();
        return startServiceCommon(service, true, this.mUser);
    }

    public boolean stopService(Intent service) {
        warnIfCallingFromSystemProcess();
        return stopServiceCommon(service, this.mUser);
    }

    public ComponentName startServiceAsUser(Intent service, UserHandle user) {
        return startServiceCommon(service, false, user);
    }

    public ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return startServiceCommon(service, true, user);
    }

    private ComponentName startServiceCommon(Intent service, boolean requireForeground, UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess((Context) this);
            ComponentName cn = ActivityManager.getService().startService(this.mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), requireForeground, getOpPackageName(), user.getIdentifier());
            if (cn != null) {
                String str = "Not allowed to start service ";
                if (cn.getPackageName().equals("!")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(service);
                    stringBuilder.append(" without permission ");
                    stringBuilder.append(cn.getClassName());
                    throw new SecurityException(stringBuilder.toString());
                }
                String str2 = ": ";
                if (cn.getPackageName().equals("!!")) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unable to start service ");
                    stringBuilder2.append(service);
                    stringBuilder2.append(str2);
                    stringBuilder2.append(cn.getClassName());
                    throw new SecurityException(stringBuilder2.toString());
                } else if (cn.getPackageName().equals("?")) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str);
                    stringBuilder3.append(service);
                    stringBuilder3.append(str2);
                    stringBuilder3.append(cn.getClassName());
                    throw new IllegalStateException(stringBuilder3.toString());
                }
            }
            return cn;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean stopServiceAsUser(Intent service, UserHandle user) {
        return stopServiceCommon(service, user);
    }

    private boolean stopServiceCommon(Intent service, UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess((Context) this);
            int res = ActivityManager.getService().stopService(this.mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), user.getIdentifier());
            if (res >= 0) {
                return res != 0;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Not allowed to stop service ");
                stringBuilder.append(service);
                throw new SecurityException(stringBuilder.toString());
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        warnIfCallingFromSystemProcess();
        return bindServiceCommon(service, conn, flags, null, this.mMainThread.getHandler(), null, getUser());
    }

    public boolean bindService(Intent service, int flags, Executor executor, ServiceConnection conn) {
        warnIfCallingFromSystemProcess();
        return bindServiceCommon(service, conn, flags, null, null, executor, getUser());
    }

    public boolean bindIsolatedService(Intent service, int flags, String instanceName, Executor executor, ServiceConnection conn) {
        warnIfCallingFromSystemProcess();
        if (instanceName != null) {
            return bindServiceCommon(service, conn, flags, instanceName, null, executor, getUser());
        }
        throw new NullPointerException("null instanceName");
    }

    public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, UserHandle user) {
        return bindServiceCommon(service, conn, flags, null, this.mMainThread.getHandler(), null, user);
    }

    public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, Handler handler, UserHandle user) {
        if (handler != null) {
            return bindServiceCommon(service, conn, flags, null, handler, null, user);
        }
        throw new IllegalArgumentException("handler must not be null.");
    }

    public IServiceConnection getServiceDispatcher(ServiceConnection conn, Handler handler, int flags) {
        return this.mPackageInfo.getServiceDispatcher(conn, getOuterContext(), handler, flags);
    }

    public IApplicationThread getIApplicationThread() {
        return this.mMainThread.getApplicationThread();
    }

    public Handler getMainThreadHandler() {
        return this.mMainThread.getHandler();
    }

    private boolean bindServiceCommon(Intent service, ServiceConnection conn, int flags, String instanceName, Handler handler, Executor executor, UserHandle user) {
        RemoteException e;
        Intent intent = service;
        ServiceConnection serviceConnection = conn;
        int i = flags;
        Handler handler2 = handler;
        Executor executor2 = executor;
        if (serviceConnection == null) {
            throw new IllegalArgumentException("connection is null");
        } else if (handler2 == null || executor2 == null) {
            LoadedApk loadedApk = this.mPackageInfo;
            if (loadedApk != null) {
                IServiceConnection sd;
                if (executor2 != null) {
                    sd = loadedApk.getServiceDispatcher(serviceConnection, getOuterContext(), executor2, i);
                } else {
                    sd = loadedApk.getServiceDispatcher(serviceConnection, getOuterContext(), handler2, i);
                }
                validateServiceIntent(service);
                int flags2;
                try {
                    if (getActivityToken() != null || (i & 1) != 0 || this.mPackageInfo == null || this.mPackageInfo.getApplicationInfo().targetSdkVersion >= 14) {
                        flags2 = i;
                    } else {
                        flags2 = i | 32;
                    }
                    try {
                        intent.prepareToLeaveProcess(this);
                        i = ActivityManager.getService().bindIsolatedService(this.mMainThread.getApplicationThread(), getActivityToken(), service, intent.resolveTypeIfNeeded(getContentResolver()), sd, flags2, instanceName, getOpPackageName(), user.getIdentifier());
                        if (i >= 0) {
                            return i != 0;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Not allowed to bind to service ");
                            stringBuilder.append(intent);
                            throw new SecurityException(stringBuilder.toString());
                        }
                    } catch (RemoteException e2) {
                        e = e2;
                        throw e.rethrowFromSystemServer();
                    }
                } catch (RemoteException e3) {
                    e = e3;
                    flags2 = i;
                    throw e.rethrowFromSystemServer();
                }
            }
            throw new RuntimeException("Not supported in system context");
        } else {
            throw new IllegalArgumentException("Handler and Executor both supplied");
        }
    }

    public void updateServiceGroup(ServiceConnection conn, int group, int importance) {
        if (conn != null) {
            IServiceConnection sd = this.mPackageInfo;
            if (sd != null) {
                sd = sd.lookupServiceDispatcher(conn, getOuterContext());
                if (sd != null) {
                    try {
                        ActivityManager.getService().updateServiceGroup(sd, group, importance);
                        return;
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ServiceConnection not currently bound: ");
                stringBuilder.append(conn);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            throw new RuntimeException("Not supported in system context");
        }
        throw new IllegalArgumentException("connection is null");
    }

    public void unbindService(ServiceConnection conn) {
        if (conn != null) {
            IServiceConnection sd = this.mPackageInfo;
            if (sd != null) {
                try {
                    ActivityManager.getService().unbindService(sd.forgetServiceDispatcher(getOuterContext(), conn));
                    return;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            throw new RuntimeException("Not supported in system context");
        }
        throw new IllegalArgumentException("connection is null");
    }

    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        if (arguments != null) {
            try {
                arguments.setAllowFds(false);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return ActivityManager.getService().startInstrumentation(className, profileFile, 0, arguments, null, null, getUserId(), null);
    }

    public Object getSystemService(String name) {
        return SystemServiceRegistry.getSystemService(this, name);
    }

    public String getSystemServiceName(Class<?> serviceClass) {
        return SystemServiceRegistry.getSystemServiceName(serviceClass);
    }

    public int checkPermission(String permission, int pid, int uid) {
        if (permission != null) {
            IActivityManager am = ActivityManager.getService();
            if (am == null) {
                int appId = UserHandle.getAppId(uid);
                String str = "Missing ActivityManager; assuming ";
                String str2 = TAG;
                StringBuilder stringBuilder;
                if (appId == 0 || appId == 1000) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(uid);
                    stringBuilder.append(" holds ");
                    stringBuilder.append(permission);
                    Slog.w(str2, stringBuilder.toString());
                    return 0;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(uid);
                stringBuilder.append(" does not hold ");
                stringBuilder.append(permission);
                Slog.w(str2, stringBuilder.toString());
                return -1;
            }
            try {
                return am.checkPermission(permission, pid, uid);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("permission is null");
    }

    public int checkPermission(String permission, int pid, int uid, IBinder callerToken) {
        if (permission != null) {
            try {
                return ActivityManager.getService().checkPermissionWithToken(permission, pid, uid, callerToken);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("permission is null");
    }

    public int checkCallingPermission(String permission) {
        if (permission != null) {
            int pid = Binder.getCallingPid();
            if (pid != Process.myPid()) {
                return checkPermission(permission, pid, Binder.getCallingUid());
            }
            return -1;
        }
        throw new IllegalArgumentException("permission is null");
    }

    public int checkCallingOrSelfPermission(String permission) {
        if (permission != null) {
            return checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid());
        }
        throw new IllegalArgumentException("permission is null");
    }

    public int checkSelfPermission(String permission) {
        if (permission != null) {
            return checkPermission(permission, Process.myPid(), Process.myUid());
        }
        throw new IllegalArgumentException("permission is null");
    }

    private void enforce(String permission, int resultOfCheck, boolean selfToo, int uid, String message) {
        if (resultOfCheck != 0) {
            StringBuilder stringBuilder;
            String stringBuilder2;
            StringBuilder stringBuilder3 = new StringBuilder();
            if (message != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(message);
                stringBuilder.append(": ");
                stringBuilder2 = stringBuilder.toString();
            } else {
                stringBuilder2 = "";
            }
            stringBuilder3.append(stringBuilder2);
            if (selfToo) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Neither user ");
                stringBuilder.append(uid);
                stringBuilder.append(" nor current process has ");
                stringBuilder2 = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("uid ");
                stringBuilder.append(uid);
                stringBuilder.append(" does not have ");
                stringBuilder2 = stringBuilder.toString();
            }
            stringBuilder3.append(stringBuilder2);
            stringBuilder3.append(permission);
            stringBuilder3.append(".");
            throw new SecurityException(stringBuilder3.toString());
        }
    }

    public void enforcePermission(String permission, int pid, int uid, String message) {
        enforce(permission, checkPermission(permission, pid, uid), false, uid, message);
    }

    public void enforceCallingPermission(String permission, String message) {
        enforce(permission, checkCallingPermission(permission), false, Binder.getCallingUid(), message);
    }

    public void enforceCallingOrSelfPermission(String permission, String message) {
        enforce(permission, checkCallingOrSelfPermission(permission), true, Binder.getCallingUid(), message);
    }

    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        try {
            ActivityManager.getService().grantUriPermission(this.mMainThread.getApplicationThread(), toPackage, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void revokeUriPermission(Uri uri, int modeFlags) {
        try {
            ActivityManager.getService().revokeUriPermission(this.mMainThread.getApplicationThread(), null, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void revokeUriPermission(String targetPackage, Uri uri, int modeFlags) {
        try {
            ActivityManager.getService().revokeUriPermission(this.mMainThread.getApplicationThread(), targetPackage, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        try {
            return ActivityManager.getService().checkUriPermission(ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags, IBinder callerToken) {
        try {
            return ActivityManager.getService().checkUriPermission(ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), callerToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private int resolveUserId(Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, getUserId());
    }

    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        int pid = Binder.getCallingPid();
        if (pid != Process.myPid()) {
            return checkUriPermission(uri, pid, Binder.getCallingUid(), modeFlags);
        }
        return -1;
    }

    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return checkUriPermission(uri, Binder.getCallingPid(), Binder.getCallingUid(), modeFlags);
    }

    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        if ((modeFlags & 1) != 0 && (readPermission == null || checkPermission(readPermission, pid, uid) == 0)) {
            return 0;
        }
        if ((modeFlags & 2) != 0 && (writePermission == null || checkPermission(writePermission, pid, uid) == 0)) {
            return 0;
        }
        int checkUriPermission;
        if (uri != null) {
            checkUriPermission = checkUriPermission(uri, pid, uid, modeFlags);
        } else {
            checkUriPermission = -1;
        }
        return checkUriPermission;
    }

    private String uriModeFlagToString(int uriModeFlags) {
        StringBuilder builder = new StringBuilder();
        if ((uriModeFlags & 1) != 0) {
            builder.append("read and ");
        }
        if ((uriModeFlags & 2) != 0) {
            builder.append("write and ");
        }
        if ((uriModeFlags & 64) != 0) {
            builder.append("persistable and ");
        }
        if ((uriModeFlags & 128) != 0) {
            builder.append("prefix and ");
        }
        if (builder.length() > 5) {
            builder.setLength(builder.length() - 5);
            return builder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown permission mode flags: ");
        stringBuilder.append(uriModeFlags);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private void enforceForUri(int modeFlags, int resultOfCheck, boolean selfToo, int uid, Uri uri, String message) {
        if (resultOfCheck != 0) {
            StringBuilder stringBuilder;
            String stringBuilder2;
            StringBuilder stringBuilder3 = new StringBuilder();
            if (message != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(message);
                stringBuilder.append(": ");
                stringBuilder2 = stringBuilder.toString();
            } else {
                stringBuilder2 = "";
            }
            stringBuilder3.append(stringBuilder2);
            if (selfToo) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Neither user ");
                stringBuilder.append(uid);
                stringBuilder.append(" nor current process has ");
                stringBuilder2 = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("User ");
                stringBuilder.append(uid);
                stringBuilder.append(" does not have ");
                stringBuilder2 = stringBuilder.toString();
            }
            stringBuilder3.append(stringBuilder2);
            stringBuilder3.append(uriModeFlagToString(modeFlags));
            stringBuilder3.append(" permission on ");
            stringBuilder3.append(uri);
            stringBuilder3.append(".");
            throw new SecurityException(stringBuilder3.toString());
        }
    }

    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, pid, uid, modeFlags), false, uid, uri, message);
    }

    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        enforceForUri(modeFlags, checkCallingUriPermission(uri, modeFlags), false, Binder.getCallingUid(), uri, message);
    }

    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        enforceForUri(modeFlags, checkCallingOrSelfUriPermission(uri, modeFlags), true, Binder.getCallingUid(), uri, message);
    }

    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags), false, uid, uri, message);
    }

    private void warnIfCallingFromSystemProcess() {
        if (ActivityThread.isSystem()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Calling a method in the system process without a qualified user: ");
            stringBuilder.append(Debug.getCallers(5));
            Slog.w(TAG, stringBuilder.toString());
        }
    }

    private static Resources createResources(IBinder activityToken, LoadedApk pi, String splitName, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo) {
        try {
            String[] splitResDirs = pi.getSplitPaths(splitName);
            ClassLoader classLoader = pi.getSplitClassLoader(splitName);
            return ResourcesManager.getInstance().getResources(activityToken, pi.getResDir(), splitResDirs, pi.getOverlayDirs(), pi.getApplicationInfo().sharedLibraryFiles, displayId, overrideConfig, compatInfo, classLoader);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Context createApplicationContext(ApplicationInfo application, int flags) throws NameNotFoundException {
        LoadedApk pi = this.mMainThread.getPackageInfo(application, this.mResources.getCompatibilityInfo(), 1073741824 | flags);
        if (pi != null) {
            ContextImpl contextImpl = new ContextImpl(this, this.mMainThread, pi, null, this.mActivityToken, new UserHandle(UserHandle.getUserId(application.uid)), flags, null, null);
            int displayId = getDisplayId();
            contextImpl.setResources(createResources(this.mActivityToken, pi, null, displayId, null, getDisplayAdjustments(displayId).getCompatibilityInfo()));
            if (contextImpl.mResources != null) {
                return contextImpl;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Application package ");
        stringBuilder.append(application.packageName);
        stringBuilder.append(" not found");
        throw new NameNotFoundException(stringBuilder.toString());
    }

    public Context createPackageContext(String packageName, int flags) throws NameNotFoundException {
        return createPackageContextAsUser(packageName, flags, this.mUser);
    }

    public Context createPackageContextAsUser(String packageName, int flags, UserHandle user) throws NameNotFoundException {
        String str = packageName;
        if (packageName.equals("system") || packageName.equals("android")) {
            return new ContextImpl(this, this.mMainThread, this.mPackageInfo, null, this.mActivityToken, user, flags, null, null);
        }
        LoadedApk pi = this.mMainThread.getPackageInfo(packageName, this.mResources.getCompatibilityInfo(), flags | 1073741824, user.getIdentifier());
        if (pi != null) {
            ContextImpl c = new ContextImpl(this, this.mMainThread, pi, null, this.mActivityToken, user, flags, null, null);
            int displayId = getDisplayId();
            c.setResources(createResources(this.mActivityToken, pi, null, displayId, null, getDisplayAdjustments(displayId).getCompatibilityInfo()));
            if (c.mResources != null) {
                return c;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Application package ");
        stringBuilder.append(packageName);
        stringBuilder.append(" not found");
        throw new NameNotFoundException(stringBuilder.toString());
    }

    public Context createContextForSplit(String splitName) throws NameNotFoundException {
        if (!this.mPackageInfo.getApplicationInfo().requestsIsolatedSplitLoading()) {
            return this;
        }
        ClassLoader classLoader = this.mPackageInfo.getSplitClassLoader(splitName);
        String[] paths = this.mPackageInfo.getSplitPaths(splitName);
        ContextImpl contextImpl = new ContextImpl(this, this.mMainThread, this.mPackageInfo, splitName, this.mActivityToken, this.mUser, this.mFlags, classLoader, null);
        int displayId = getDisplayId();
        contextImpl.setResources(ResourcesManager.getInstance().getResources(this.mActivityToken, this.mPackageInfo.getResDir(), paths, this.mPackageInfo.getOverlayDirs(), this.mPackageInfo.getApplicationInfo().sharedLibraryFiles, displayId, null, this.mPackageInfo.getCompatibilityInfo(), classLoader));
        return contextImpl;
    }

    public Context createConfigurationContext(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            ContextImpl context = new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, this.mFlags, this.mClassLoader, null);
            int displayId = getDisplayId();
            context.setResources(createResources(this.mActivityToken, this.mPackageInfo, this.mSplitName, displayId, overrideConfiguration, getDisplayAdjustments(displayId).getCompatibilityInfo()));
            return context;
        }
        throw new IllegalArgumentException("overrideConfiguration must not be null");
    }

    public Context createDisplayContext(Display display) {
        if (display != null) {
            ContextImpl context = new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, this.mFlags, this.mClassLoader, null);
            int displayId = display.getDisplayId();
            context.setResources(createResources(this.mActivityToken, this.mPackageInfo, this.mSplitName, displayId, null, getDisplayAdjustments(displayId).getCompatibilityInfo()));
            context.mDisplay = display;
            return context;
        }
        throw new IllegalArgumentException("display must not be null");
    }

    public Context createDeviceProtectedStorageContext() {
        return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, (this.mFlags & -17) | 8, this.mClassLoader, null);
    }

    public Context createCredentialProtectedStorageContext() {
        return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, (this.mFlags & -9) | 16, this.mClassLoader, null);
    }

    public boolean isRestricted() {
        return (this.mFlags & 4) != 0;
    }

    public boolean isDeviceProtectedStorage() {
        return (this.mFlags & 8) != 0;
    }

    public boolean isCredentialProtectedStorage() {
        return (this.mFlags & 16) != 0;
    }

    public boolean canLoadUnsafeResources() {
        boolean z = true;
        if (getPackageName().equals(getOpPackageName())) {
            return true;
        }
        if ((this.mFlags & 2) == 0) {
            z = false;
        }
        return z;
    }

    public Display getDisplay() {
        Display display = this.mDisplay;
        if (display == null) {
            return this.mResourcesManager.getAdjustedDisplay(0, this.mResources);
        }
        return display;
    }

    public int getDisplayId() {
        Display display = this.mDisplay;
        return display != null ? display.getDisplayId() : 0;
    }

    public void updateDisplay(int displayId) {
        this.mDisplay = this.mResourcesManager.getAdjustedDisplay(displayId, this.mResources);
    }

    public DisplayAdjustments getDisplayAdjustments(int displayId) {
        return this.mResources.getDisplayAdjustments();
    }

    public File getDataDir() {
        StringBuilder stringBuilder;
        if (this.mPackageInfo != null) {
            File res;
            if (isCredentialProtectedStorage()) {
                res = this.mPackageInfo.getCredentialProtectedDataDirFile();
            } else if (isDeviceProtectedStorage()) {
                res = this.mPackageInfo.getDeviceProtectedDataDirFile();
            } else {
                res = this.mPackageInfo.getDataDirFile();
            }
            if (res != null) {
                if (!res.exists() && Process.myUid() == 1000) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Data directory doesn't exist for package ");
                    stringBuilder.append(getPackageName());
                    Log.wtf(TAG, stringBuilder.toString(), new Throwable());
                }
                return res;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("No data directory found for package ");
            stringBuilder2.append(getPackageName());
            throw new RuntimeException(stringBuilder2.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("No package details found for package ");
        stringBuilder.append(getPackageName());
        throw new RuntimeException(stringBuilder.toString());
    }

    public File getDir(String name, int mode) {
        checkMode(mode);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("app_");
        stringBuilder.append(name);
        File file = makeFilename(getDataDir(), stringBuilder.toString());
        if (!file.exists()) {
            file.mkdir();
            setFilePermissionsFromMode(file.getPath(), mode, 505);
        }
        return file;
    }

    public UserHandle getUser() {
        return this.mUser;
    }

    public int getUserId() {
        return this.mUser.getIdentifier();
    }

    public AutofillClient getAutofillClient() {
        return this.mAutofillClient;
    }

    public void setAutofillClient(AutofillClient client) {
        this.mAutofillClient = client;
    }

    public AutofillOptions getAutofillOptions() {
        return this.mAutofillOptions;
    }

    public void setAutofillOptions(AutofillOptions options) {
        this.mAutofillOptions = options;
    }

    public ContentCaptureOptions getContentCaptureOptions() {
        return this.mContentCaptureOptions;
    }

    public void setContentCaptureOptions(ContentCaptureOptions options) {
        this.mContentCaptureOptions = options;
    }

    @UnsupportedAppUsage
    static ContextImpl createSystemContext(ActivityThread mainThread) {
        LoadedApk packageInfo = new LoadedApk(mainThread);
        ContextImpl context = new ContextImpl(null, mainThread, packageInfo, null, null, null, 0, null, null);
        context.setResources(packageInfo.getResources());
        context.mResources.updateConfiguration(context.mResourcesManager.getConfiguration(), context.mResourcesManager.getDisplayMetrics());
        return context;
    }

    static ContextImpl createSystemUiContext(ContextImpl systemContext, int displayId) {
        LoadedApk packageInfo = systemContext.mPackageInfo;
        ContextImpl context = new ContextImpl(null, systemContext.mMainThread, packageInfo, null, null, null, 0, null, null);
        context.setResources(createResources(null, packageInfo, null, displayId, null, packageInfo.getCompatibilityInfo()));
        context.updateDisplay(displayId);
        return context;
    }

    static ContextImpl createSystemUiContext(ContextImpl systemContext) {
        return createSystemUiContext(systemContext, 0);
    }

    @UnsupportedAppUsage
    static ContextImpl createAppContext(ActivityThread mainThread, LoadedApk packageInfo) {
        return createAppContext(mainThread, packageInfo, null);
    }

    static ContextImpl createAppContext(ActivityThread mainThread, LoadedApk packageInfo, String opPackageName) {
        if (packageInfo != null) {
            ContextImpl context = new ContextImpl(null, mainThread, packageInfo, null, null, null, 0, null, opPackageName);
            context.setResources(packageInfo.getResources());
            return context;
        }
        throw new IllegalArgumentException("packageInfo");
    }

    @UnsupportedAppUsage
    static ContextImpl createActivityContext(ActivityThread mainThread, LoadedApk packageInfo, ActivityInfo activityInfo, IBinder activityToken, int displayId, Configuration overrideConfiguration) {
        LoadedApk loadedApk = packageInfo;
        ActivityInfo activityInfo2 = activityInfo;
        ResourcesManager resourcesManager;
        if (loadedApk != null) {
            String[] splitDirs;
            ClassLoader classLoader;
            CompatibilityInfo compatInfo;
            String[] splitDirs2 = packageInfo.getSplitResDirs();
            ClassLoader classLoader2 = packageInfo.getClassLoader();
            if (packageInfo.getApplicationInfo().requestsIsolatedSplitLoading()) {
                Trace.traceBegin(8192, "SplitDependencies");
                try {
                    classLoader2 = loadedApk.getSplitClassLoader(activityInfo2.splitName);
                    splitDirs2 = loadedApk.getSplitPaths(activityInfo2.splitName);
                    Trace.traceEnd(8192);
                    splitDirs = splitDirs2;
                    classLoader = classLoader2;
                } catch (NameNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    Trace.traceEnd(8192);
                }
            } else {
                splitDirs = splitDirs2;
                classLoader = classLoader2;
            }
            ContextImpl context = new ContextImpl(null, mainThread, packageInfo, activityInfo2.splitName, activityToken, null, 0, classLoader, null);
            int i = displayId;
            int displayId2 = i != -1 ? i : 0;
            if (displayId2 == 0) {
                compatInfo = packageInfo.getCompatibilityInfo();
            } else {
                compatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
            }
            resourcesManager = ResourcesManager.getInstance();
            context.setResources(resourcesManager.createBaseActivityResources(activityToken, packageInfo.getResDir(), splitDirs, packageInfo.getOverlayDirs(), packageInfo.getApplicationInfo().sharedLibraryFiles, displayId2, overrideConfiguration, compatInfo, classLoader));
            context.mDisplay = resourcesManager.getAdjustedDisplay(displayId2, context.getResources());
            return context;
        }
        resourcesManager = displayId;
        throw new IllegalArgumentException("packageInfo");
    }

    private ContextImpl(ContextImpl container, ActivityThread mainThread, LoadedApk packageInfo, String splitName, IBinder activityToken, UserHandle user, int flags, ClassLoader classLoader, String overrideOpPackageName) {
        String opPackageName;
        if ((flags & 24) == 0) {
            File dataDir = packageInfo.getDataDirFile();
            if (Objects.equals(dataDir, packageInfo.getCredentialProtectedDataDirFile())) {
                flags |= 16;
            } else if (Objects.equals(dataDir, packageInfo.getDeviceProtectedDataDirFile())) {
                flags |= 8;
            }
        }
        this.mMainThread = mainThread;
        this.mActivityToken = activityToken;
        this.mFlags = flags;
        if (user == null) {
            user = Process.myUserHandle();
        }
        this.mUser = user;
        this.mPackageInfo = packageInfo;
        this.mSplitName = splitName;
        this.mClassLoader = classLoader;
        this.mResourcesManager = ResourcesManager.getInstance();
        if (container != null) {
            this.mBasePackageName = container.mBasePackageName;
            opPackageName = container.mOpPackageName;
            setResources(container.mResources);
            this.mDisplay = container.mDisplay;
        } else {
            this.mBasePackageName = packageInfo.mPackageName;
            ApplicationInfo ainfo = packageInfo.getApplicationInfo();
            if (ainfo.uid != 1000 || ainfo.uid == Process.myUid()) {
                opPackageName = this.mBasePackageName;
            } else {
                opPackageName = ActivityThread.currentPackageName();
            }
        }
        this.mOpPackageName = overrideOpPackageName != null ? overrideOpPackageName : opPackageName;
        this.mContentResolver = new ApplicationContentResolver(this, mainThread);
    }

    /* Access modifiers changed, original: 0000 */
    public void setResources(Resources r) {
        if (r instanceof CompatResources) {
            ((CompatResources) r).setContext(this);
        }
        ResourcesManager.initMiuiResource(r, this.mPackageInfo.mPackageName);
        this.mResources = r;
    }

    /* Access modifiers changed, original: 0000 */
    public void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        this.mPackageInfo.installSystemApplicationInfo(info, classLoader);
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void scheduleFinalCleanup(String who, String what) {
        this.mMainThread.scheduleContextCleanup(this, who, what);
    }

    /* Access modifiers changed, original: final */
    public final void performFinalCleanup(String who, String what) {
        this.mPackageInfo.removeContextRegistrations(getOuterContext(), who, what);
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final Context getReceiverRestrictedContext() {
        Context context = this.mReceiverRestrictedContext;
        if (context != null) {
            return context;
        }
        ReceiverRestrictedContext receiverRestrictedContext = new ReceiverRestrictedContext(getOuterContext());
        this.mReceiverRestrictedContext = receiverRestrictedContext;
        return receiverRestrictedContext;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void setOuterContext(Context context) {
        this.mOuterContext = context;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final Context getOuterContext() {
        return this.mOuterContext;
    }

    @UnsupportedAppUsage
    public IBinder getActivityToken() {
        return this.mActivityToken;
    }

    private void checkMode(int mode) {
        if (getApplicationInfo().targetSdkVersion < 24) {
            return;
        }
        if ((mode & 1) != 0) {
            throw new SecurityException("MODE_WORLD_READABLE no longer supported");
        } else if ((mode & 2) != 0) {
            throw new SecurityException("MODE_WORLD_WRITEABLE no longer supported");
        }
    }

    static void setFilePermissionsFromMode(String name, int mode, int extraPermissions) {
        int perms = extraPermissions | DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER;
        if ((mode & 1) != 0) {
            perms |= 4;
        }
        if ((mode & 2) != 0) {
            perms |= 2;
        }
        FileUtils.setPermissions(name, perms, -1, -1);
    }

    private File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            File res = new File(base, name);
            BlockGuard.getVmPolicy().onPathAccess(res.getPath());
            return res;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("File ");
        stringBuilder.append(name);
        stringBuilder.append(" contains a path separator");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private File[] ensureExternalDirsExistOrFilter(File[] dirs) {
        StorageManager sm = (StorageManager) getSystemService(StorageManager.class);
        File[] result = new File[dirs.length];
        for (int i = 0; i < dirs.length; i++) {
            File dir = dirs[i];
            if (!(dir.exists() || dir.mkdirs() || dir.exists())) {
                try {
                    sm.mkdirs(dir);
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to ensure ");
                    stringBuilder.append(dir);
                    stringBuilder.append(": ");
                    stringBuilder.append(e);
                    Log.w(TAG, stringBuilder.toString());
                    dir = null;
                }
            }
            result[i] = dir;
        }
        return result;
    }
}
