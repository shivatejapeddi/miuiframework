package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.ComponentInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.IDexModuleRegisterCallback.Stub;
import android.content.pm.IOnPermissionsChangeListener;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageMoveObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.InstantAppInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.IntentFilterVerificationInfo;
import android.content.pm.KeySet;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.DexModuleRegisterCallback;
import android.content.pm.PackageManager.MoveCallback;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager.OnPermissionsChangedListener;
import android.content.pm.ParceledListSlice;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.SuspendDialogInfo;
import android.content.pm.SuspendDialogInfo.Builder;
import android.content.pm.VerifierDeviceIdentity;
import android.content.pm.VersionedPackage;
import android.content.pm.dex.ArtManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.LayerDrawable;
import android.miui.AppOpsUtils;
import android.miui.ResourcesManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.ContactsContract;
import android.provider.Settings.Global;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IconDrawableFactory;
import android.util.LauncherIcons;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import com.android.internal.util.UserIcons;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import libcore.util.EmptyArray;
import miui.securityspace.XSpaceUserHandle;

public class ApplicationPackageManager extends PackageManager {
    @VisibleForTesting
    public static final int[] CORP_BADGE_LABEL_RES_ID = new int[]{R.string.managed_profile_label_badge, R.string.managed_profile_label_badge_2, R.string.managed_profile_label_badge_3};
    private static final boolean DEBUG_ICONS = false;
    private static final int DEFAULT_EPHEMERAL_COOKIE_MAX_SIZE_BYTES = 16384;
    private static final String TAG = "ApplicationPackageManager";
    private static final int sDefaultFlags = 1024;
    private static ArrayMap<ResourceName, WeakReference<ConstantState>> sIconCache = new ArrayMap();
    private static ArrayMap<ResourceName, WeakReference<CharSequence>> sStringCache = new ArrayMap();
    private static final Object sSync = new Object();
    @GuardedBy({"mLock"})
    private ArtManager mArtManager;
    volatile int mCachedSafeMode = -1;
    private final ContextImpl mContext;
    @GuardedBy({"mDelegates"})
    private final ArrayList<MoveCallbackDelegate> mDelegates = new ArrayList();
    @GuardedBy({"mLock"})
    private PackageInstaller mInstaller;
    private final Object mLock = new Object();
    @UnsupportedAppUsage
    private final IPackageManager mPM;
    private final Map<OnPermissionsChangedListener, IOnPermissionsChangeListener> mPermissionListeners = new ArrayMap();
    @GuardedBy({"mLock"})
    private String mPermissionsControllerPackageName;
    @GuardedBy({"mLock"})
    private UserManager mUserManager;
    private volatile boolean mUserUnlocked = false;

    private static class DexModuleRegisterCallbackDelegate extends Stub implements Callback {
        private static final int MSG_DEX_MODULE_REGISTERED = 1;
        private final DexModuleRegisterCallback callback;
        private final Handler mHandler = new Handler(Looper.getMainLooper(), (Callback) this);

        DexModuleRegisterCallbackDelegate(DexModuleRegisterCallback callback) {
            this.callback = callback;
        }

        public void onDexModuleRegistered(String dexModulePath, boolean success, String message) throws RemoteException {
            this.mHandler.obtainMessage(1, new DexModuleRegisterResult(dexModulePath, success, message)).sendToTarget();
        }

        public boolean handleMessage(Message msg) {
            if (msg.what != 1) {
                return false;
            }
            DexModuleRegisterResult result = msg.obj;
            this.callback.onDexModuleRegistered(result.dexModulePath, result.success, result.message);
            return true;
        }
    }

    private static class DexModuleRegisterResult {
        final String dexModulePath;
        final String message;
        final boolean success;

        private DexModuleRegisterResult(String dexModulePath, boolean success, String message) {
            this.dexModulePath = dexModulePath;
            this.success = success;
            this.message = message;
        }
    }

    private static class MoveCallbackDelegate extends IPackageMoveObserver.Stub implements Callback {
        private static final int MSG_CREATED = 1;
        private static final int MSG_STATUS_CHANGED = 2;
        final MoveCallback mCallback;
        final Handler mHandler;

        public MoveCallbackDelegate(MoveCallback callback, Looper looper) {
            this.mCallback = callback;
            this.mHandler = new Handler(looper, (Callback) this);
        }

        public boolean handleMessage(Message msg) {
            int i = msg.what;
            SomeArgs args;
            if (i == 1) {
                args = (SomeArgs) msg.obj;
                this.mCallback.onCreated(args.argi1, (Bundle) args.arg2);
                args.recycle();
                return true;
            } else if (i != 2) {
                return false;
            } else {
                args = msg.obj;
                this.mCallback.onStatusChanged(args.argi1, args.argi2, ((Long) args.arg3).longValue());
                args.recycle();
                return true;
            }
        }

        public void onCreated(int moveId, Bundle extras) {
            SomeArgs args = SomeArgs.obtain();
            args.argi1 = moveId;
            args.arg2 = extras;
            this.mHandler.obtainMessage(1, args).sendToTarget();
        }

        public void onStatusChanged(int moveId, int status, long estMillis) {
            SomeArgs args = SomeArgs.obtain();
            args.argi1 = moveId;
            args.argi2 = status;
            args.arg3 = Long.valueOf(estMillis);
            this.mHandler.obtainMessage(2, args).sendToTarget();
        }
    }

    public class OnPermissionsChangeListenerDelegate extends IOnPermissionsChangeListener.Stub implements Callback {
        private static final int MSG_PERMISSIONS_CHANGED = 1;
        private final Handler mHandler;
        private final OnPermissionsChangedListener mListener;

        public OnPermissionsChangeListenerDelegate(OnPermissionsChangedListener listener, Looper looper) {
            this.mListener = listener;
            this.mHandler = new Handler(looper, (Callback) this);
        }

        public void onPermissionsChanged(int uid) {
            this.mHandler.obtainMessage(1, uid, 0).sendToTarget();
        }

        public boolean handleMessage(Message msg) {
            if (msg.what != 1) {
                return false;
            }
            this.mListener.onPermissionsChanged(msg.arg1);
            return true;
        }
    }

    private static final class ResourceName {
        final int iconId;
        final String packageName;

        ResourceName(String _packageName, int _iconId) {
            this.packageName = _packageName;
            this.iconId = _iconId;
        }

        ResourceName(ApplicationInfo aInfo, int _iconId) {
            this(aInfo.packageName, _iconId);
        }

        ResourceName(ComponentInfo cInfo, int _iconId) {
            this(cInfo.applicationInfo.packageName, _iconId);
        }

        ResourceName(ResolveInfo rInfo, int _iconId) {
            this(rInfo.activityInfo.applicationInfo.packageName, _iconId);
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ResourceName that = (ResourceName) o;
            if (this.iconId != that.iconId) {
                return false;
            }
            String str = this.packageName;
            if (str == null ? that.packageName != null : !str.equals(that.packageName)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (this.packageName.hashCode() * 31) + this.iconId;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ResourceName ");
            stringBuilder.append(this.packageName);
            stringBuilder.append(" / ");
            stringBuilder.append(this.iconId);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public UserManager getUserManager() {
        UserManager userManager;
        synchronized (this.mLock) {
            if (this.mUserManager == null) {
                this.mUserManager = UserManager.get(this.mContext);
            }
            userManager = this.mUserManager;
        }
        return userManager;
    }

    public int getUserId() {
        return this.mContext.getUserId();
    }

    public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
        return getPackageInfoAsUser(packageName, flags, getUserId());
    }

    public PackageInfo getPackageInfo(VersionedPackage versionedPackage, int flags) throws NameNotFoundException {
        int userId = getUserId();
        try {
            PackageInfo pi = this.mPM.getPackageInfoVersioned(versionedPackage, updateFlagsForPackage(flags, userId), userId);
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(versionedPackage.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public PackageInfo getPackageInfoAsUser(String packageName, int flags, int userId) throws NameNotFoundException {
        try {
            PackageInfo pi = this.mPM.getPackageInfo(packageName, updateFlagsForPackage(flags, userId), userId);
            if (pi != null) {
                return ApplicationPackageManagerInjector.hookGetPackageInfo(this.mContext, pi, flags);
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] currentToCanonicalPackageNames(String[] names) {
        try {
            return this.mPM.currentToCanonicalPackageNames(names);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] canonicalToCurrentPackageNames(String[] names) {
        try {
            return this.mPM.canonicalToCurrentPackageNames(names);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Intent getLaunchIntentForPackage(String packageName) {
        if (AppOpsUtils.isXOptMode() && (TextUtils.equals(packageName, "com.android.providers.downloads.ui") || TextUtils.equals(packageName, "com.miui.huanji") || TextUtils.equals(packageName, "com.android.thememanager"))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("return null, getLaunchIntentForPackage : ");
            stringBuilder.append(packageName);
            Log.d(TAG, stringBuilder.toString());
            return null;
        }
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        String str = Intent.CATEGORY_INFO;
        intentToResolve.addCategory(str);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = queryIntentActivities(intentToResolve, 0);
        if (ris == null || ris.size() <= 0) {
            intentToResolve.removeCategory(str);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = queryIntentActivities(intentToResolve, 0);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(268435456);
        intent.setClassName(((ResolveInfo) ris.get(0)).activityInfo.packageName, ((ResolveInfo) ris.get(0)).activityInfo.name);
        return intent;
    }

    public Intent getLeanbackLaunchIntentForPackage(String packageName) {
        return getLaunchIntentForPackageAndCategory(packageName, Intent.CATEGORY_LEANBACK_LAUNCHER);
    }

    public Intent getCarLaunchIntentForPackage(String packageName) {
        return getLaunchIntentForPackageAndCategory(packageName, Intent.CATEGORY_CAR_LAUNCHER);
    }

    private Intent getLaunchIntentForPackageAndCategory(String packageName, String category) {
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(category);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = queryIntentActivities(intentToResolve, 0);
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(268435456);
        intent.setClassName(((ResolveInfo) ris.get(0)).activityInfo.packageName, ((ResolveInfo) ris.get(0)).activityInfo.name);
        return intent;
    }

    public int[] getPackageGids(String packageName) throws NameNotFoundException {
        return getPackageGids(packageName, 0);
    }

    public int[] getPackageGids(String packageName, int flags) throws NameNotFoundException {
        int userId = getUserId();
        try {
            int[] gids = this.mPM.getPackageGids(packageName, updateFlagsForPackage(flags, userId), userId);
            if (gids != null) {
                return gids;
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPackageUid(String packageName, int flags) throws NameNotFoundException {
        return getPackageUidAsUser(packageName, flags, getUserId());
    }

    public int getPackageUidAsUser(String packageName, int userId) throws NameNotFoundException {
        return getPackageUidAsUser(packageName, 0, userId);
    }

    public int getPackageUidAsUser(String packageName, int flags, int userId) throws NameNotFoundException {
        try {
            int uid = this.mPM.getPackageUid(packageName, updateFlagsForPackage(flags, userId), userId);
            if (uid >= 0) {
                return uid;
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public PermissionInfo getPermissionInfo(String name, int flags) throws NameNotFoundException {
        try {
            PermissionInfo pi = this.mPM.getPermissionInfo(name, this.mContext.getOpPackageName(), flags);
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(name);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws NameNotFoundException {
        try {
            ParceledListSlice<PermissionInfo> parceledList = this.mPM.queryPermissionsByGroup(group, flags);
            if (parceledList != null) {
                List<PermissionInfo> pi = parceledList.getList();
                if (pi != null) {
                    return pi;
                }
            }
            throw new NameNotFoundException(group);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean arePermissionsIndividuallyControlled() {
        return this.mContext.getResources().getBoolean(R.bool.config_permissionsIndividuallyControlled);
    }

    public boolean isWirelessConsentModeEnabled() {
        return this.mContext.getResources().getBoolean(R.bool.config_wirelessConsentRequired);
    }

    public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws NameNotFoundException {
        try {
            PermissionGroupInfo pgi = this.mPM.getPermissionGroupInfo(name, flags);
            if (pgi != null) {
                return pgi;
            }
            throw new NameNotFoundException(name);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
        try {
            ParceledListSlice<PermissionGroupInfo> parceledList = this.mPM.getAllPermissionGroups(flags);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
        return getApplicationInfoAsUser(packageName, flags, getUserId());
    }

    public ApplicationInfo getApplicationInfoAsUser(String packageName, int flags, int userId) throws NameNotFoundException {
        try {
            ApplicationInfo ai = this.mPM.getApplicationInfo(packageName, updateFlagsForApplication(flags, userId), userId);
            if (ai != null) {
                if (AppOpsUtils.isXOptMode() && (TextUtils.equals(packageName, "com.miui.screenrecorder") || TextUtils.equals(packageName, "com.xiaomi.drivemode") || TextUtils.equals(packageName, "com.xiaomi.aiasst.service"))) {
                    ai.flags |= 1;
                }
                return maybeAdjustApplicationInfo(ai);
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static ApplicationInfo maybeAdjustApplicationInfo(ApplicationInfo info) {
        if (!(info.primaryCpuAbi == null || info.secondaryCpuAbi == null)) {
            String runtimeIsa = VMRuntime.getRuntime().vmInstructionSet();
            String secondaryIsa = VMRuntime.getInstructionSet(info.secondaryCpuAbi);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ro.dalvik.vm.isa.");
            stringBuilder.append(secondaryIsa);
            String secondaryDexCodeIsa = SystemProperties.get(stringBuilder.toString());
            if (runtimeIsa.equals(secondaryDexCodeIsa.isEmpty() ? secondaryIsa : secondaryDexCodeIsa)) {
                ApplicationInfo modified = new ApplicationInfo(info);
                modified.nativeLibraryDir = info.secondaryNativeLibraryDir;
                return modified;
            }
        }
        return info;
    }

    public ActivityInfo getActivityInfo(ComponentName className, int flags) throws NameNotFoundException {
        int userId = getUserId();
        try {
            ActivityInfo ai = this.mPM.getActivityInfo(className, updateFlagsForComponent(flags, userId, null), userId);
            if (ai != null) {
                return ai;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ActivityInfo getReceiverInfo(ComponentName className, int flags) throws NameNotFoundException {
        int userId = getUserId();
        try {
            ActivityInfo ai = this.mPM.getReceiverInfo(className, updateFlagsForComponent(flags, userId, null), userId);
            if (ai != null) {
                return ai;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ServiceInfo getServiceInfo(ComponentName className, int flags) throws NameNotFoundException {
        int userId = getUserId();
        try {
            ServiceInfo si = this.mPM.getServiceInfo(className, updateFlagsForComponent(flags, userId, null), userId);
            if (si != null) {
                return si;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ProviderInfo getProviderInfo(ComponentName className, int flags) throws NameNotFoundException {
        int userId = getUserId();
        try {
            ProviderInfo pi = this.mPM.getProviderInfo(className, updateFlagsForComponent(flags, userId, null), userId);
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getSystemSharedLibraryNames() {
        try {
            return this.mPM.getSystemSharedLibraryNames();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<SharedLibraryInfo> getSharedLibraries(int flags) {
        return getSharedLibrariesAsUser(flags, getUserId());
    }

    public List<SharedLibraryInfo> getSharedLibrariesAsUser(int flags, int userId) {
        try {
            ParceledListSlice<SharedLibraryInfo> sharedLibs = this.mPM.getSharedLibraries(this.mContext.getOpPackageName(), flags, userId);
            if (sharedLibs == null) {
                return Collections.emptyList();
            }
            return sharedLibs.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<SharedLibraryInfo> getDeclaredSharedLibraries(String packageName, int flags) {
        try {
            ParceledListSlice<SharedLibraryInfo> sharedLibraries = this.mPM.getDeclaredSharedLibraries(packageName, flags, this.mContext.getUserId());
            return sharedLibraries != null ? sharedLibraries.getList() : Collections.emptyList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getServicesSystemSharedLibraryPackageName() {
        try {
            return this.mPM.getServicesSystemSharedLibraryPackageName();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getSharedSystemSharedLibraryPackageName() {
        try {
            return this.mPM.getSharedSystemSharedLibraryPackageName();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ChangedPackages getChangedPackages(int sequenceNumber) {
        try {
            return this.mPM.getChangedPackages(sequenceNumber, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public FeatureInfo[] getSystemAvailableFeatures() {
        try {
            ParceledListSlice<FeatureInfo> parceledList = this.mPM.getSystemAvailableFeatures();
            int i = 0;
            if (parceledList == null) {
                return new FeatureInfo[0];
            }
            List<FeatureInfo> list = parceledList.getList();
            FeatureInfo[] res = new FeatureInfo[list.size()];
            while (i < res.length) {
                res[i] = (FeatureInfo) list.get(i);
                i++;
            }
            return res;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasSystemFeature(String name) {
        return hasSystemFeature(name, 0);
    }

    public boolean hasSystemFeature(String name, int version) {
        try {
            return this.mPM.hasSystemFeature(name, version);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int checkPermission(String permName, String pkgName) {
        try {
            if ("com.google.android.permission.gts".equals(this.mContext.getPackageName()) && (ContactsContract.AUTHORITY.equals(pkgName) || "com.miui.yellowpage".equals(pkgName) || "com.android.providers.contacts".equals(pkgName))) {
                return -1;
            }
            return this.mPM.checkPermission(permName, pkgName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isPermissionRevokedByPolicy(String permName, String pkgName) {
        try {
            return this.mPM.isPermissionRevokedByPolicy(permName, pkgName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getPermissionControllerPackageName() {
        String str;
        synchronized (this.mLock) {
            if (this.mPermissionsControllerPackageName == null) {
                try {
                    this.mPermissionsControllerPackageName = this.mPM.getPermissionControllerPackageName();
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            str = this.mPermissionsControllerPackageName;
        }
        return str;
    }

    public boolean addPermission(PermissionInfo info) {
        try {
            return this.mPM.addPermission(info);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean addPermissionAsync(PermissionInfo info) {
        try {
            return this.mPM.addPermissionAsync(info);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removePermission(String name) {
        try {
            this.mPM.removePermission(name);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void grantRuntimePermission(String packageName, String permissionName, UserHandle user) {
        try {
            this.mPM.grantRuntimePermission(packageName, permissionName, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void revokeRuntimePermission(String packageName, String permissionName, UserHandle user) {
        try {
            this.mPM.revokeRuntimePermission(packageName, permissionName, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPermissionFlags(String permissionName, String packageName, UserHandle user) {
        try {
            return this.mPM.getPermissionFlags(permissionName, packageName, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, UserHandle user) {
        try {
            this.mPM.updatePermissionFlags(permissionName, packageName, flagMask, flagValues, this.mContext.getApplicationInfo().targetSdkVersion >= 29, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Set<String> getWhitelistedRestrictedPermissions(String packageName, int whitelistFlags) {
        try {
            Collection whitelist = this.mPM.getWhitelistedRestrictedPermissions(packageName, whitelistFlags, getUserId());
            if (whitelist != null) {
                return new ArraySet(whitelist);
            }
            return Collections.emptySet();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean addWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags) {
        try {
            return this.mPM.addWhitelistedRestrictedPermission(packageName, permission, whitelistFlags, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean removeWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags) {
        try {
            return this.mPM.removeWhitelistedRestrictedPermission(packageName, permission, whitelistFlags, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public boolean shouldShowRequestPermissionRationale(String permission) {
        try {
            return this.mPM.shouldShowRequestPermissionRationale(permission, this.mContext.getPackageName(), getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int checkSignatures(String pkg1, String pkg2) {
        try {
            return this.mPM.checkSignatures(pkg1, pkg2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int checkSignatures(int uid1, int uid2) {
        try {
            return this.mPM.checkUidSignatures(uid1, uid2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasSigningCertificate(String packageName, byte[] certificate, int type) {
        try {
            return this.mPM.hasSigningCertificate(packageName, certificate, type);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasSigningCertificate(int uid, byte[] certificate, int type) {
        try {
            return this.mPM.hasUidSigningCertificate(uid, certificate, type);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getPackagesForUid(int uid) {
        try {
            return this.mPM.getPackagesForUid(uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getNameForUid(int uid) {
        try {
            return this.mPM.getNameForUid(uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getNamesForUids(int[] uids) {
        try {
            return this.mPM.getNamesForUids(uids);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getUidForSharedUser(String sharedUserName) throws NameNotFoundException {
        try {
            int uid = this.mPM.getUidForSharedUser(sharedUserName);
            if (uid != -1) {
                return uid;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No shared userid for user:");
            stringBuilder.append(sharedUserName);
            throw new NameNotFoundException(stringBuilder.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ModuleInfo> getInstalledModules(int flags) {
        try {
            return this.mPM.getInstalledModules(flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ModuleInfo getModuleInfo(String packageName, int flags) throws NameNotFoundException {
        try {
            ModuleInfo mi = this.mPM.getModuleInfo(packageName, flags);
            if (mi != null) {
                return mi;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No module info for package: ");
            stringBuilder.append(packageName);
            throw new NameNotFoundException(stringBuilder.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<PackageInfo> getInstalledPackages(int flags) {
        return getInstalledPackagesAsUser(flags, getUserId());
    }

    public List<PackageInfo> getInstalledPackagesAsUser(int flags, int userId) {
        try {
            ParceledListSlice<PackageInfo> parceledList = this.mPM.getInstalledPackages(updateFlagsForPackage(flags, userId), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<PackageInfo> getPackagesHoldingPermissions(String[] permissions, int flags) {
        int userId = getUserId();
        try {
            ParceledListSlice<PackageInfo> parceledList = this.mPM.getPackagesHoldingPermissions(permissions, updateFlagsForPackage(flags, userId), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ApplicationInfo> getInstalledApplications(int flags) {
        return getInstalledApplicationsAsUser(flags, getUserId());
    }

    public List<ApplicationInfo> getInstalledApplicationsAsUser(int flags, int userId) {
        try {
            ParceledListSlice<ApplicationInfo> parceledList = this.mPM.getInstalledApplications(updateFlagsForApplication(flags, userId), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InstantAppInfo> getInstantApps() {
        try {
            ParceledListSlice<InstantAppInfo> slice = this.mPM.getInstantApps(getUserId());
            if (slice != null) {
                return slice.getList();
            }
            return Collections.emptyList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Drawable getInstantAppIcon(String packageName) {
        try {
            Bitmap bitmap = this.mPM.getInstantAppIcon(packageName, getUserId());
            if (bitmap != null) {
                return new BitmapDrawable(null, bitmap);
            }
            return null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isInstantApp() {
        return isInstantApp(this.mContext.getPackageName());
    }

    public boolean isInstantApp(String packageName) {
        try {
            return this.mPM.isInstantApp(packageName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getInstantAppCookieMaxBytes() {
        return Global.getInt(this.mContext.getContentResolver(), Global.EPHEMERAL_COOKIE_MAX_SIZE_BYTES, 16384);
    }

    public int getInstantAppCookieMaxSize() {
        return getInstantAppCookieMaxBytes();
    }

    public byte[] getInstantAppCookie() {
        try {
            byte[] cookie = this.mPM.getInstantAppCookie(this.mContext.getPackageName(), getUserId());
            if (cookie != null) {
                return cookie;
            }
            return EmptyArray.BYTE;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearInstantAppCookie() {
        updateInstantAppCookie(null);
    }

    public void updateInstantAppCookie(byte[] cookie) {
        if (cookie == null || cookie.length <= getInstantAppCookieMaxBytes()) {
            try {
                this.mPM.setInstantAppCookie(this.mContext.getPackageName(), cookie, getUserId());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("instant cookie longer than ");
        stringBuilder.append(getInstantAppCookieMaxBytes());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @UnsupportedAppUsage
    public boolean setInstantAppCookie(byte[] cookie) {
        try {
            return this.mPM.setInstantAppCookie(this.mContext.getPackageName(), cookie, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ResolveInfo resolveActivity(Intent intent, int flags) {
        return resolveActivityAsUser(intent, flags, getUserId());
    }

    public ResolveInfo resolveActivityAsUser(Intent intent, int flags, int userId) {
        try {
            return this.mPM.resolveIntent(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), updateFlagsForComponent(flags, userId, intent), userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
        return queryIntentActivitiesAsUser(intent, flags, getUserId());
    }

    public List<ResolveInfo> queryIntentActivitiesAsUser(Intent intent, int flags, int userId) {
        try {
            ParceledListSlice<ResolveInfo> parceledList = this.mPM.queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), updateFlagsForComponent(flags, userId, intent), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller, Intent[] specifics, Intent intent, int flags) {
        String[] specificTypes;
        int userId = getUserId();
        ContentResolver resolver = this.mContext.getContentResolver();
        String[] specificTypes2 = null;
        if (specifics != null) {
            int N = specifics.length;
            for (int i = 0; i < N; i++) {
                Intent sp = specifics[i];
                if (sp != null) {
                    String t = sp.resolveTypeIfNeeded(resolver);
                    if (t != null) {
                        if (specificTypes2 == null) {
                            specificTypes2 = new String[N];
                        }
                        specificTypes2[i] = t;
                    }
                }
            }
            specificTypes = specificTypes2;
        } else {
            specificTypes = null;
        }
        try {
            ParceledListSlice<ResolveInfo> parceledList = this.mPM.queryIntentActivityOptions(caller, specifics, specificTypes, intent, intent.resolveTypeIfNeeded(resolver), updateFlagsForComponent(flags, userId, intent), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ResolveInfo> queryBroadcastReceiversAsUser(Intent intent, int flags, int userId) {
        try {
            ParceledListSlice<ResolveInfo> parceledList = this.mPM.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), updateFlagsForComponent(flags, userId, intent), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
        return queryBroadcastReceiversAsUser(intent, flags, getUserId());
    }

    public ResolveInfo resolveServiceAsUser(Intent intent, int flags, int userId) {
        try {
            return this.mPM.resolveService(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), updateFlagsForComponent(flags, userId, intent), userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ResolveInfo resolveService(Intent intent, int flags) {
        return resolveServiceAsUser(intent, flags, getUserId());
    }

    public List<ResolveInfo> queryIntentServicesAsUser(Intent intent, int flags, int userId) {
        try {
            ParceledListSlice<ResolveInfo> parceledList = this.mPM.queryIntentServices(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), updateFlagsForComponent(flags, userId, intent), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
        return queryIntentServicesAsUser(intent, flags, getUserId());
    }

    public List<ResolveInfo> queryIntentContentProvidersAsUser(Intent intent, int flags, int userId) {
        try {
            ParceledListSlice<ResolveInfo> parceledList = this.mPM.queryIntentContentProviders(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), updateFlagsForComponent(flags, userId, intent), userId);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ResolveInfo> queryIntentContentProviders(Intent intent, int flags) {
        return queryIntentContentProvidersAsUser(intent, flags, getUserId());
    }

    public ProviderInfo resolveContentProvider(String name, int flags) {
        return resolveContentProviderAsUser(name, flags, getUserId());
    }

    public ProviderInfo resolveContentProviderAsUser(String name, int flags, int userId) {
        try {
            return this.mPM.resolveContentProvider(name, updateFlagsForComponent(flags, userId, null), userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
        return queryContentProviders(processName, uid, flags, null);
    }

    public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags, String metaDataKey) {
        try {
            ParceledListSlice<ProviderInfo> slice = this.mPM.queryContentProviders(processName, uid, updateFlagsForComponent(flags, UserHandle.getUserId(uid), null), metaDataKey);
            return slice != null ? slice.getList() : Collections.emptyList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws NameNotFoundException {
        try {
            InstrumentationInfo ii = this.mPM.getInstrumentationInfo(className, flags);
            if (ii != null) {
                return ii;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InstrumentationInfo> queryInstrumentation(String targetPackage, int flags) {
        try {
            ParceledListSlice<InstrumentationInfo> parceledList = this.mPM.queryInstrumentation(targetPackage, flags);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Drawable getDrawable(String packageName, int resId, ApplicationInfo appInfo) {
        StringBuilder stringBuilder;
        String str = "Failure retrieving resources for ";
        String str2 = "PackageManager";
        ResourceName name = new ResourceName(packageName, resId);
        Drawable cachedIcon = getCachedIcon(name);
        if (cachedIcon != null) {
            return cachedIcon;
        }
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, 1024);
            } catch (NameNotFoundException e) {
                return null;
            }
        }
        if (resId != 0) {
            try {
                Drawable dr = getResourcesForApplication(appInfo).getDrawable(resId, null);
                if (dr != null) {
                    putCachedIcon(name, dr);
                }
                return dr;
            } catch (NameNotFoundException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(appInfo.packageName);
                Log.w(str2, stringBuilder.toString());
            } catch (NotFoundException e3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(appInfo.packageName);
                stringBuilder.append(": ");
                stringBuilder.append(e3.getMessage());
                Log.w(str2, stringBuilder.toString());
            } catch (Exception e4) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failure retrieving icon 0x");
                stringBuilder2.append(Integer.toHexString(resId));
                stringBuilder2.append(" in package ");
                stringBuilder2.append(packageName);
                Log.w(str2, stringBuilder2.toString(), e4);
            }
        }
        return null;
    }

    public Drawable getActivityIcon(ComponentName activityName) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadIcon(this);
    }

    public Drawable getActivityIcon(Intent intent) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityIcon(intent.getComponent());
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadIcon(this);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getDefaultActivityIcon() {
        return this.mContext.getDrawable(17301651);
    }

    public Drawable getApplicationIcon(ApplicationInfo info) {
        return info.loadIcon(this);
    }

    public Drawable getApplicationIcon(String packageName) throws NameNotFoundException {
        return getApplicationIcon(getApplicationInfo(packageName, 1024));
    }

    public Drawable getActivityBanner(ComponentName activityName) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadBanner(this);
    }

    public Drawable getActivityBanner(Intent intent) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityBanner(intent.getComponent());
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadBanner(this);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getApplicationBanner(ApplicationInfo info) {
        return info.loadBanner(this);
    }

    public Drawable getApplicationBanner(String packageName) throws NameNotFoundException {
        return getApplicationBanner(getApplicationInfo(packageName, 1024));
    }

    public Drawable getActivityLogo(ComponentName activityName) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadLogo(this);
    }

    public Drawable getActivityLogo(Intent intent) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityLogo(intent.getComponent());
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadLogo(this);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getApplicationLogo(ApplicationInfo info) {
        return info.loadLogo(this);
    }

    public Drawable getApplicationLogo(String packageName) throws NameNotFoundException {
        return getApplicationLogo(getApplicationInfo(packageName, 1024));
    }

    public Drawable getUserBadgedIcon(Drawable icon, UserHandle user) {
        if (XSpaceUserHandle.isXSpaceUser(user)) {
            return getBadgedDrawable(icon, getDrawable("system", 285671584, null), null, false);
        }
        if (isManagedProfile(user.getIdentifier())) {
            return getBadgedDrawable(icon, new LauncherIcons(this.mContext).getBadgeDrawable(R.drawable.ic_corp_icon_badge_case, getUserBadgeColor(user)), null, true);
        }
        return icon;
    }

    public Drawable getUserBadgedDrawableForDensity(Drawable drawable, UserHandle user, Rect badgeLocation, int badgeDensity) {
        Drawable badgeDrawable = getUserBadgeForDensity(user, badgeDensity);
        if (badgeDrawable == null) {
            return drawable;
        }
        return getBadgedDrawable(drawable, badgeDrawable, badgeLocation, true);
    }

    private int getUserBadgeColor(UserHandle user) {
        return IconDrawableFactory.getUserBadgeColor(getUserManager(), user.getIdentifier());
    }

    public Drawable getUserBadgeForDensity(UserHandle user, int density) {
        if (getManagedProfileIconForDensity(user, R.drawable.ic_corp_badge_color, density) == null) {
            return null;
        }
        getDrawableForDensity(R.drawable.ic_corp_badge_case, density).setTint(getUserBadgeColor(user));
        return new LayerDrawable(new Drawable[]{badgeColor, badgeForeground});
    }

    public Drawable getUserBadgeForDensityNoBackground(UserHandle user, int density) {
        Drawable badge = getManagedProfileIconForDensity(user, R.drawable.ic_corp_badge_no_background, density);
        if (badge != null) {
            badge.setTint(getUserBadgeColor(user));
        }
        return badge;
    }

    private Drawable getDrawableForDensity(int drawableId, int density) {
        if (density <= 0) {
            density = this.mContext.getResources().getDisplayMetrics().densityDpi;
        }
        return this.mContext.getResources().getDrawableForDensity(drawableId, density);
    }

    private Drawable getManagedProfileIconForDensity(UserHandle user, int drawableId, int density) {
        if (isManagedProfile(user.getIdentifier())) {
            return getDrawableForDensity(drawableId, density);
        }
        return null;
    }

    public CharSequence getUserBadgedLabel(CharSequence label, UserHandle user) {
        if (!isManagedProfile(user.getIdentifier())) {
            return label;
        }
        int badge = getUserManager().getManagedProfileBadge(user.getIdentifier());
        int resourceId = CORP_BADGE_LABEL_RES_ID;
        resourceId = resourceId[badge % resourceId.length];
        return Resources.getSystem().getString(resourceId, label);
    }

    public Resources getResourcesForActivity(ComponentName activityName) throws NameNotFoundException {
        return getResourcesForApplication(getActivityInfo(activityName, 1024).applicationInfo);
    }

    public Resources getResourcesForApplication(ApplicationInfo app) throws NameNotFoundException {
        if (app.packageName.equals("system")) {
            return this.mContext.mMainThread.getSystemUiContext().getResources();
        }
        boolean sameUid = app.uid == Process.myUid();
        Resources r = this.mContext.mMainThread.getTopLevelResources(sameUid ? app.sourceDir : app.publicSourceDir, sameUid ? app.splitSourceDirs : app.splitPublicSourceDirs, app.resourceDirs, app.sharedLibraryFiles, 0, this.mContext.mPackageInfo);
        ResourcesManager.initMiuiResource(r, app.packageName);
        if (r != null) {
            return r;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to open ");
        stringBuilder.append(app.publicSourceDir);
        throw new NameNotFoundException(stringBuilder.toString());
    }

    public Resources getResourcesForApplication(String appPackageName) throws NameNotFoundException {
        return getResourcesForApplication(getApplicationInfo(appPackageName, 1024));
    }

    public Resources getResourcesForApplicationAsUser(String appPackageName, int userId) throws NameNotFoundException {
        StringBuilder stringBuilder;
        if (userId < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Call does not support special user #");
            stringBuilder.append(userId);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if ("system".equals(appPackageName)) {
            return this.mContext.mMainThread.getSystemUiContext().getResources();
        } else {
            try {
                ApplicationInfo ai = this.mPM.getApplicationInfo(appPackageName, 1024, userId);
                if (ai != null) {
                    return getResourcesForApplication(ai);
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Package ");
                stringBuilder.append(appPackageName);
                stringBuilder.append(" doesn't exist");
                throw new NameNotFoundException(stringBuilder.toString());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public boolean isSafeMode() {
        try {
            if (this.mCachedSafeMode < 0) {
                this.mCachedSafeMode = this.mPM.isSafeMode() ? 1 : 0;
            }
            if (this.mCachedSafeMode != 0) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addOnPermissionsChangeListener(OnPermissionsChangedListener listener) {
        synchronized (this.mPermissionListeners) {
            if (this.mPermissionListeners.get(listener) != null) {
                return;
            }
            OnPermissionsChangeListenerDelegate delegate = new OnPermissionsChangeListenerDelegate(listener, Looper.getMainLooper());
            try {
                this.mPM.addOnPermissionsChangeListener(delegate);
                this.mPermissionListeners.put(listener, delegate);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void removeOnPermissionsChangeListener(OnPermissionsChangedListener listener) {
        synchronized (this.mPermissionListeners) {
            IOnPermissionsChangeListener delegate = (IOnPermissionsChangeListener) this.mPermissionListeners.get(listener);
            if (delegate != null) {
                try {
                    this.mPM.removeOnPermissionsChangeListener(delegate);
                    this.mPermissionListeners.remove(listener);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @UnsupportedAppUsage
    static void configurationChanged() {
        synchronized (sSync) {
            sIconCache.clear();
            sStringCache.clear();
        }
    }

    @UnsupportedAppUsage
    protected ApplicationPackageManager(ContextImpl context, IPackageManager pm) {
        this.mContext = context;
        this.mPM = pm;
    }

    private int updateFlagsForPackage(int flags, int userId) {
        if ((flags & 15) != 0 && (269221888 & flags) == 0) {
            onImplicitDirectBoot(userId);
        }
        return flags;
    }

    private int updateFlagsForApplication(int flags, int userId) {
        return updateFlagsForPackage(flags, userId);
    }

    private int updateFlagsForComponent(int flags, int userId, Intent intent) {
        if (!(intent == null || (intent.getFlags() & 256) == 0)) {
            flags |= 268435456;
        }
        if ((269221888 & flags) == 0) {
            onImplicitDirectBoot(userId);
        }
        return flags;
    }

    private void onImplicitDirectBoot(int userId) {
        if (StrictMode.vmImplicitDirectBootEnabled()) {
            if (userId == UserHandle.myUserId()) {
                if (!this.mUserUnlocked) {
                    if (((UserManager) this.mContext.getSystemService(UserManager.class)).isUserUnlockingOrUnlocked(userId)) {
                        this.mUserUnlocked = true;
                    } else {
                        StrictMode.onImplicitDirectBoot();
                    }
                }
            } else if (!((UserManager) this.mContext.getSystemService(UserManager.class)).isUserUnlockingOrUnlocked(userId)) {
                StrictMode.onImplicitDirectBoot();
            }
        }
    }

    /* JADX WARNING: Missing block: B:13:0x0022, code skipped:
            return null;
     */
    private android.graphics.drawable.Drawable getCachedIcon(android.app.ApplicationPackageManager.ResourceName r5) {
        /*
        r4 = this;
        r0 = sSync;
        monitor-enter(r0);
        r1 = sIconCache;	 Catch:{ all -> 0x0023 }
        r1 = r1.get(r5);	 Catch:{ all -> 0x0023 }
        r1 = (java.lang.ref.WeakReference) r1;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0020;
    L_0x000d:
        r2 = r1.get();	 Catch:{ all -> 0x0023 }
        r2 = (android.graphics.drawable.Drawable.ConstantState) r2;	 Catch:{ all -> 0x0023 }
        if (r2 == 0) goto L_0x001b;
    L_0x0015:
        r3 = r2.newDrawable();	 Catch:{ all -> 0x0023 }
        monitor-exit(r0);	 Catch:{ all -> 0x0023 }
        return r3;
    L_0x001b:
        r3 = sIconCache;	 Catch:{ all -> 0x0023 }
        r3.remove(r5);	 Catch:{ all -> 0x0023 }
    L_0x0020:
        monitor-exit(r0);	 Catch:{ all -> 0x0023 }
        r0 = 0;
        return r0;
    L_0x0023:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0023 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationPackageManager.getCachedIcon(android.app.ApplicationPackageManager$ResourceName):android.graphics.drawable.Drawable");
    }

    private void putCachedIcon(ResourceName name, Drawable dr) {
        synchronized (sSync) {
            sIconCache.put(name, new WeakReference(dr.getConstantState()));
        }
    }

    static void handlePackageBroadcast(int cmd, String[] pkgList, boolean hasPkgInfo) {
        boolean immediateGc = false;
        if (cmd == 1) {
            immediateGc = true;
        }
        if (pkgList != null && pkgList.length > 0) {
            boolean needCleanup = false;
            for (String ssp : pkgList) {
                synchronized (sSync) {
                    int i;
                    for (i = sIconCache.size() - 1; i >= 0; i--) {
                        if (((ResourceName) sIconCache.keyAt(i)).packageName.equals(ssp)) {
                            sIconCache.removeAt(i);
                            needCleanup = true;
                        }
                    }
                    for (i = sStringCache.size() - 1; i >= 0; i--) {
                        if (((ResourceName) sStringCache.keyAt(i)).packageName.equals(ssp)) {
                            sStringCache.removeAt(i);
                            needCleanup = true;
                        }
                    }
                }
            }
            if (!needCleanup && !hasPkgInfo) {
                return;
            }
            if (immediateGc) {
                Runtime.getRuntime().gc();
            } else {
                ActivityThread.currentActivityThread().scheduleGcIdler();
            }
        }
    }

    /* JADX WARNING: Missing block: B:12:0x001e, code skipped:
            return null;
     */
    private java.lang.CharSequence getCachedString(android.app.ApplicationPackageManager.ResourceName r5) {
        /*
        r4 = this;
        r0 = sSync;
        monitor-enter(r0);
        r1 = sStringCache;	 Catch:{ all -> 0x001f }
        r1 = r1.get(r5);	 Catch:{ all -> 0x001f }
        r1 = (java.lang.ref.WeakReference) r1;	 Catch:{ all -> 0x001f }
        if (r1 == 0) goto L_0x001c;
    L_0x000d:
        r2 = r1.get();	 Catch:{ all -> 0x001f }
        r2 = (java.lang.CharSequence) r2;	 Catch:{ all -> 0x001f }
        if (r2 == 0) goto L_0x0017;
    L_0x0015:
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        return r2;
    L_0x0017:
        r3 = sStringCache;	 Catch:{ all -> 0x001f }
        r3.remove(r5);	 Catch:{ all -> 0x001f }
    L_0x001c:
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        r0 = 0;
        return r0;
    L_0x001f:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationPackageManager.getCachedString(android.app.ApplicationPackageManager$ResourceName):java.lang.CharSequence");
    }

    private void putCachedString(ResourceName name, CharSequence cs) {
        synchronized (sSync) {
            sStringCache.put(name, new WeakReference(cs));
        }
    }

    public CharSequence getText(String packageName, int resid, ApplicationInfo appInfo) {
        StringBuilder stringBuilder;
        String str = "PackageManager";
        ResourceName name = new ResourceName(packageName, resid);
        CharSequence text = getCachedString(name);
        if (text != null) {
            return text;
        }
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, 1024);
            } catch (NameNotFoundException e) {
                return null;
            }
        }
        try {
            text = getResourcesForApplication(appInfo).getText(resid);
            putCachedString(name, text);
            return text;
        } catch (NameNotFoundException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failure retrieving resources for ");
            stringBuilder.append(appInfo.packageName);
            Log.w(str, stringBuilder.toString());
            return null;
        } catch (RuntimeException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failure retrieving text 0x");
            stringBuilder.append(Integer.toHexString(resid));
            stringBuilder.append(" in package ");
            stringBuilder.append(packageName);
            Log.w(str, stringBuilder.toString(), e3);
            return null;
        }
    }

    public XmlResourceParser getXml(String packageName, int resid, ApplicationInfo appInfo) {
        StringBuilder stringBuilder;
        String str = "PackageManager";
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, 1024);
            } catch (NameNotFoundException e) {
                return null;
            }
        }
        try {
            str = getResourcesForApplication(appInfo).getXml(resid);
            return str;
        } catch (RuntimeException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failure retrieving xml 0x");
            stringBuilder.append(Integer.toHexString(resid));
            stringBuilder.append(" in package ");
            stringBuilder.append(packageName);
            Log.w(str, stringBuilder.toString(), e2);
            return null;
        } catch (NameNotFoundException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failure retrieving resources for ");
            stringBuilder.append(appInfo.packageName);
            Log.w(str, stringBuilder.toString());
            return null;
        }
    }

    public CharSequence getApplicationLabel(ApplicationInfo info) {
        return info.loadLabel(this);
    }

    public int installExistingPackage(String packageName) throws NameNotFoundException {
        return installExistingPackage(packageName, 0);
    }

    public int installExistingPackage(String packageName, int installReason) throws NameNotFoundException {
        return installExistingPackageAsUser(packageName, installReason, getUserId());
    }

    public int installExistingPackageAsUser(String packageName, int userId) throws NameNotFoundException {
        return installExistingPackageAsUser(packageName, 0, userId);
    }

    private int installExistingPackageAsUser(String packageName, int installReason, int userId) throws NameNotFoundException {
        try {
            int res = this.mPM.installExistingPackageAsUser(packageName, userId, 4194304, installReason, null);
            if (res != -3) {
                return res;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Package ");
            stringBuilder.append(packageName);
            stringBuilder.append(" doesn't exist");
            throw new NameNotFoundException(stringBuilder.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void verifyPendingInstall(int id, int response) {
        try {
            this.mPM.verifyPendingInstall(id, response);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {
        try {
            this.mPM.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void verifyIntentFilter(int id, int verificationCode, List<String> failedDomains) {
        try {
            this.mPM.verifyIntentFilter(id, verificationCode, failedDomains);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getIntentVerificationStatusAsUser(String packageName, int userId) {
        try {
            return this.mPM.getIntentVerificationStatus(packageName, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean updateIntentVerificationStatusAsUser(String packageName, int status, int userId) {
        try {
            return this.mPM.updateIntentVerificationStatus(packageName, status, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<IntentFilterVerificationInfo> getIntentFilterVerifications(String packageName) {
        try {
            ParceledListSlice<IntentFilterVerificationInfo> parceledList = this.mPM.getIntentFilterVerifications(packageName);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<IntentFilter> getAllIntentFilters(String packageName) {
        try {
            ParceledListSlice<IntentFilter> parceledList = this.mPM.getAllIntentFilters(packageName);
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getDefaultBrowserPackageNameAsUser(int userId) {
        try {
            return this.mPM.getDefaultBrowserPackageName(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setDefaultBrowserPackageNameAsUser(String packageName, int userId) {
        try {
            return this.mPM.setDefaultBrowserPackageName(packageName, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setInstallerPackageName(String targetPackage, String installerPackageName) {
        try {
            this.mPM.setInstallerPackageName(targetPackage, installerPackageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setUpdateAvailable(String packageName, boolean updateAvailable) {
        try {
            this.mPM.setUpdateAvailable(packageName, updateAvailable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getInstallerPackageName(String packageName) {
        try {
            return this.mPM.getInstallerPackageName(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMoveStatus(int moveId) {
        try {
            return this.mPM.getMoveStatus(moveId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerMoveCallback(MoveCallback callback, Handler handler) {
        synchronized (this.mDelegates) {
            MoveCallbackDelegate delegate = new MoveCallbackDelegate(callback, handler.getLooper());
            try {
                this.mPM.registerMoveCallback(delegate);
                this.mDelegates.add(delegate);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void unregisterMoveCallback(MoveCallback callback) {
        synchronized (this.mDelegates) {
            Iterator<MoveCallbackDelegate> i = this.mDelegates.iterator();
            while (i.hasNext()) {
                MoveCallbackDelegate delegate = (MoveCallbackDelegate) i.next();
                if (delegate.mCallback == callback) {
                    try {
                        this.mPM.unregisterMoveCallback(delegate);
                        i.remove();
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }
    }

    public int movePackage(String packageName, VolumeInfo vol) {
        try {
            String volumeUuid;
            if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.id)) {
                volumeUuid = StorageManager.UUID_PRIVATE_INTERNAL;
            } else if (vol.isPrimaryPhysical()) {
                volumeUuid = StorageManager.UUID_PRIMARY_PHYSICAL;
            } else {
                volumeUuid = (String) Preconditions.checkNotNull(vol.fsUuid);
            }
            return this.mPM.movePackage(packageName, volumeUuid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public VolumeInfo getPackageCurrentVolume(ApplicationInfo app) {
        return getPackageCurrentVolume(app, (StorageManager) this.mContext.getSystemService(StorageManager.class));
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public VolumeInfo getPackageCurrentVolume(ApplicationInfo app, StorageManager storage) {
        if (app.isInternal()) {
            return storage.findVolumeById(VolumeInfo.ID_PRIVATE_INTERNAL);
        }
        return storage.findVolumeByUuid(app.volumeUuid);
    }

    public List<VolumeInfo> getPackageCandidateVolumes(ApplicationInfo app) {
        return getPackageCandidateVolumes(app, (StorageManager) this.mContext.getSystemService(StorageManager.class), this.mPM);
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<VolumeInfo> getPackageCandidateVolumes(ApplicationInfo app, StorageManager storageManager, IPackageManager pm) {
        VolumeInfo currentVol = getPackageCurrentVolume(app, storageManager);
        List<VolumeInfo> vols = storageManager.getVolumes();
        List<VolumeInfo> candidates = new ArrayList();
        for (VolumeInfo vol : vols) {
            if (Objects.equals(vol, currentVol) || isPackageCandidateVolume(this.mContext, app, vol, pm)) {
                candidates.add(vol);
            }
        }
        return candidates;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public boolean isForceAllowOnExternal(Context context) {
        return Global.getInt(context.getContentResolver(), Global.FORCE_ALLOW_ON_EXTERNAL, 0) != 0;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public boolean isAllow3rdPartyOnInternal(Context context) {
        return context.getResources().getBoolean(R.bool.config_allow3rdPartyAppOnInternal);
    }

    /* JADX WARNING: Missing block: B:16:0x0034, code skipped:
            return false;
     */
    private boolean isPackageCandidateVolume(android.app.ContextImpl r6, android.content.pm.ApplicationInfo r7, android.os.storage.VolumeInfo r8, android.content.pm.IPackageManager r9) {
        /*
        r5 = this;
        r0 = r5.isForceAllowOnExternal(r6);
        r1 = r8.getId();
        r2 = "private";
        r1 = r2.equals(r1);
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x0022;
    L_0x0013:
        r1 = r7.isSystemApp();
        if (r1 != 0) goto L_0x0021;
    L_0x0019:
        r1 = r5.isAllow3rdPartyOnInternal(r6);
        if (r1 == 0) goto L_0x0020;
    L_0x001f:
        goto L_0x0021;
    L_0x0020:
        r2 = r3;
    L_0x0021:
        return r2;
    L_0x0022:
        r1 = r7.isSystemApp();
        if (r1 == 0) goto L_0x0029;
    L_0x0028:
        return r3;
    L_0x0029:
        if (r0 != 0) goto L_0x0035;
    L_0x002b:
        r1 = r7.installLocation;
        if (r1 == r2) goto L_0x0034;
    L_0x002f:
        r1 = r7.installLocation;
        r4 = -1;
        if (r1 != r4) goto L_0x0035;
    L_0x0034:
        return r3;
    L_0x0035:
        r1 = r8.isMountedWritable();
        if (r1 != 0) goto L_0x003c;
    L_0x003b:
        return r3;
    L_0x003c:
        r1 = r8.isPrimaryPhysical();
        if (r1 == 0) goto L_0x0047;
    L_0x0042:
        r1 = r7.isInternal();
        return r1;
    L_0x0047:
        r1 = r7.packageName;	 Catch:{ RemoteException -> 0x005a }
        r1 = r9.isPackageDeviceAdminOnAnyUser(r1);	 Catch:{ RemoteException -> 0x005a }
        if (r1 == 0) goto L_0x0050;
    L_0x004f:
        return r3;
        r1 = r8.getType();
        if (r1 != r2) goto L_0x0058;
    L_0x0057:
        goto L_0x0059;
    L_0x0058:
        r2 = r3;
    L_0x0059:
        return r2;
    L_0x005a:
        r1 = move-exception;
        r2 = r1.rethrowFromSystemServer();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationPackageManager.isPackageCandidateVolume(android.app.ContextImpl, android.content.pm.ApplicationInfo, android.os.storage.VolumeInfo, android.content.pm.IPackageManager):boolean");
    }

    public int movePrimaryStorage(VolumeInfo vol) {
        try {
            String volumeUuid;
            if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.id)) {
                volumeUuid = StorageManager.UUID_PRIVATE_INTERNAL;
            } else if (vol.isPrimaryPhysical()) {
                volumeUuid = StorageManager.UUID_PRIMARY_PHYSICAL;
            } else {
                volumeUuid = (String) Preconditions.checkNotNull(vol.fsUuid);
            }
            return this.mPM.movePrimaryStorage(volumeUuid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public VolumeInfo getPrimaryStorageCurrentVolume() {
        StorageManager storage = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        return storage.findVolumeByQualifiedUuid(storage.getPrimaryStorageUuid());
    }

    public List<VolumeInfo> getPrimaryStorageCandidateVolumes() {
        StorageManager storage = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        VolumeInfo currentVol = getPrimaryStorageCurrentVolume();
        List<VolumeInfo> vols = storage.getVolumes();
        List<VolumeInfo> candidates = new ArrayList();
        if (!Objects.equals(StorageManager.UUID_PRIMARY_PHYSICAL, storage.getPrimaryStorageUuid()) || currentVol == null) {
            for (VolumeInfo vol : vols) {
                if (Objects.equals(vol, currentVol) || isPrimaryStorageCandidateVolume(vol)) {
                    candidates.add(vol);
                }
            }
        } else {
            candidates.add(currentVol);
        }
        return candidates;
    }

    private static boolean isPrimaryStorageCandidateVolume(VolumeInfo vol) {
        boolean z = true;
        if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.getId())) {
            return true;
        }
        if (!vol.isMountedWritable()) {
            return false;
        }
        if (vol.getType() != 1) {
            z = false;
        }
        return z;
    }

    @UnsupportedAppUsage
    public void deletePackage(String packageName, IPackageDeleteObserver observer, int flags) {
        deletePackageAsUser(packageName, observer, flags, getUserId());
    }

    public void deletePackageAsUser(String packageName, IPackageDeleteObserver observer, int flags, int userId) {
        try {
            this.mPM.deletePackageAsUser(packageName, -1, observer, userId, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearApplicationUserData(String packageName, IPackageDataObserver observer) {
        try {
            this.mPM.clearApplicationUserData(packageName, observer, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) {
        try {
            this.mPM.deleteApplicationCacheFiles(packageName, observer);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer) {
        try {
            this.mPM.deleteApplicationCacheFilesAsUser(packageName, userId, observer);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void freeStorageAndNotify(String volumeUuid, long idealStorageSize, IPackageDataObserver observer) {
        try {
            this.mPM.freeStorageAndNotify(volumeUuid, idealStorageSize, 0, observer);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void freeStorage(String volumeUuid, long freeStorageSize, IntentSender pi) {
        try {
            this.mPM.freeStorage(volumeUuid, freeStorageSize, 0, pi);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] setDistractingPackageRestrictions(String[] packages, int distractionFlags) {
        try {
            return this.mPM.setDistractingPackageRestrictionsAsUser(packages, distractionFlags, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] setPackagesSuspended(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, String dialogMessage) {
        SuspendDialogInfo dialogInfo;
        if (TextUtils.isEmpty(dialogMessage)) {
            dialogInfo = null;
        } else {
            dialogInfo = new Builder().setMessage(dialogMessage).build();
        }
        return setPackagesSuspended(packageNames, suspended, appExtras, launcherExtras, dialogInfo);
    }

    public String[] setPackagesSuspended(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo) {
        try {
            return this.mPM.setPackagesSuspendedAsUser(packageNames, suspended, appExtras, launcherExtras, dialogInfo, this.mContext.getOpPackageName(), getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getUnsuspendablePackages(String[] packageNames) {
        try {
            return this.mPM.getUnsuspendablePackagesForUser(packageNames, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Bundle getSuspendedPackageAppExtras() {
        try {
            PersistableBundle extras = this.mPM.getSuspendedPackageAppExtras(this.mContext.getOpPackageName(), getUserId());
            return extras != null ? new Bundle(extras.deepCopy()) : null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isPackageSuspendedForUser(String packageName, int userId) {
        try {
            return this.mPM.isPackageSuspendedForUser(packageName, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isPackageSuspended(String packageName) throws NameNotFoundException {
        try {
            return isPackageSuspendedForUser(packageName, getUserId());
        } catch (IllegalArgumentException e) {
            throw new NameNotFoundException(packageName);
        }
    }

    public boolean isPackageSuspended() {
        return isPackageSuspendedForUser(this.mContext.getOpPackageName(), getUserId());
    }

    public void setApplicationCategoryHint(String packageName, int categoryHint) {
        try {
            this.mPM.setApplicationCategoryHint(packageName, categoryHint, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void getPackageSizeInfoAsUser(String packageName, int userHandle, IPackageStatsObserver observer) {
        String msg = "Shame on you for calling the hidden API getPackageSizeInfoAsUser(). Shame!";
        String str = "Shame on you for calling the hidden API getPackageSizeInfoAsUser(). Shame!";
        if (this.mContext.getApplicationInfo().targetSdkVersion >= 26) {
            throw new UnsupportedOperationException(str);
        } else if (observer != null) {
            Log.d(TAG, str);
            try {
                observer.onGetStatsCompleted(null, false);
            } catch (RemoteException e) {
            }
        }
    }

    public void addPackageToPreferred(String packageName) {
        Log.w(TAG, "addPackageToPreferred() is a no-op");
    }

    public void removePackageFromPreferred(String packageName) {
        Log.w(TAG, "removePackageFromPreferred() is a no-op");
    }

    public List<PackageInfo> getPreferredPackages(int flags) {
        Log.w(TAG, "getPreferredPackages() is a no-op");
        return Collections.emptyList();
    }

    public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
        try {
            this.mPM.addPreferredActivity(filter, match, set, activity, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addPreferredActivityAsUser(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) {
        try {
            this.mPM.addPreferredActivity(filter, match, set, activity, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
        try {
            this.mPM.replacePreferredActivity(filter, match, set, activity, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void replacePreferredActivityAsUser(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) {
        try {
            this.mPM.replacePreferredActivity(filter, match, set, activity, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearPackagePreferredActivities(String packageName) {
        try {
            this.mPM.clearPackagePreferredActivities(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) {
        try {
            return this.mPM.getPreferredActivities(outFilters, outActivities, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ComponentName getHomeActivities(List<ResolveInfo> outActivities) {
        try {
            return this.mPM.getHomeActivities(outActivities);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSyntheticAppDetailsActivityEnabled(String packageName, boolean enabled) {
        try {
            int i;
            ComponentName componentName = new ComponentName(packageName, PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME);
            IPackageManager iPackageManager = this.mPM;
            if (enabled) {
                i = 0;
            } else {
                i = 2;
            }
            iPackageManager.setComponentEnabledSetting(componentName, i, 1, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getSyntheticAppDetailsActivityEnabled(String packageName) {
        try {
            int state = this.mPM.getComponentEnabledSetting(new ComponentName(packageName, PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME), getUserId());
            return state == 1 || state == 0;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
        try {
            this.mPM.setComponentEnabledSetting(componentName, newState, flags, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getComponentEnabledSetting(ComponentName componentName) {
        try {
            return this.mPM.getComponentEnabledSetting(componentName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
        try {
            this.mPM.setApplicationEnabledSetting(packageName, newState, flags, getUserId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getApplicationEnabledSetting(String packageName) {
        try {
            return this.mPM.getApplicationEnabledSetting(packageName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void flushPackageRestrictionsAsUser(int userId) {
        try {
            this.mPM.flushPackageRestrictionsAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, UserHandle user) {
        try {
            return this.mPM.setApplicationHiddenSettingAsUser(packageName, hidden, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getApplicationHiddenSettingAsUser(String packageName, UserHandle user) {
        try {
            return this.mPM.getApplicationHiddenSettingAsUser(packageName, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public KeySet getKeySetByAlias(String packageName, String alias) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(alias);
        try {
            return this.mPM.getKeySetByAlias(packageName, alias);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public KeySet getSigningKeySet(String packageName) {
        Preconditions.checkNotNull(packageName);
        try {
            return this.mPM.getSigningKeySet(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSignedBy(String packageName, KeySet ks) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(ks);
        try {
            return this.mPM.isPackageSignedByKeySet(packageName, ks);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSignedByExactly(String packageName, KeySet ks) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(ks);
        try {
            return this.mPM.isPackageSignedByKeySetExactly(packageName, ks);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public VerifierDeviceIdentity getVerifierDeviceIdentity() {
        try {
            return this.mPM.getVerifierDeviceIdentity();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isUpgrade() {
        return isDeviceUpgrading();
    }

    public boolean isDeviceUpgrading() {
        try {
            return this.mPM.isDeviceUpgrading();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public PackageInstaller getPackageInstaller() {
        PackageInstaller packageInstaller;
        synchronized (this.mLock) {
            if (this.mInstaller == null) {
                try {
                    this.mInstaller = new PackageInstaller(this.mPM.getPackageInstaller(), this.mContext.getPackageName(), getUserId());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            packageInstaller = this.mInstaller;
        }
        return packageInstaller;
    }

    public boolean isPackageAvailable(String packageName) {
        try {
            return this.mPM.isPackageAvailable(packageName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addCrossProfileIntentFilter(IntentFilter filter, int sourceUserId, int targetUserId, int flags) {
        try {
            this.mPM.addCrossProfileIntentFilter(filter, this.mContext.getOpPackageName(), sourceUserId, targetUserId, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearCrossProfileIntentFilters(int sourceUserId) {
        try {
            this.mPM.clearCrossProfileIntentFilters(sourceUserId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Drawable loadItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo) {
        Drawable dr = loadUnbadgedItemIcon(itemInfo, appInfo);
        if (itemInfo.showUserIcon != -10000) {
            return dr;
        }
        return getUserBadgedIcon(dr, new UserHandle(getUserId()));
    }

    public Drawable loadUnbadgedItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo) {
        Drawable drawable = MiuiThemeHelper.getDrawable(this, itemInfo.packageName, itemInfo.name, itemInfo.icon, appInfo);
        if (drawable != null) {
            return drawable;
        }
        if (itemInfo.showUserIcon != -10000) {
            return UserIcons.getDefaultUserIcon(this.mContext.getResources(), itemInfo.showUserIcon, false);
        }
        Drawable dr = null;
        if (itemInfo.packageName != null) {
            dr = getDrawable(itemInfo.packageName, itemInfo.icon, appInfo);
        }
        if (!(dr != null || itemInfo == appInfo || appInfo == null)) {
            dr = loadUnbadgedItemIcon(appInfo, appInfo);
        }
        if (dr == null) {
            dr = itemInfo.loadDefaultIcon(this);
        }
        return dr;
    }

    private Drawable getBadgedDrawable(Drawable drawable, Drawable badgeDrawable, Rect badgeLocation, boolean tryBadgeInPlace) {
        int badgedWidth = drawable.getIntrinsicWidth();
        int badgedHeight = drawable.getIntrinsicHeight();
        boolean canBadgeInPlace = tryBadgeInPlace && (drawable instanceof BitmapDrawable) && ((BitmapDrawable) drawable).getBitmap().isMutable();
        if (ApplicationPackageManagerInjector.hookGetBadgedDrawable(drawable, badgeDrawable, badgeLocation)) {
            Log.d(TAG, "MIUILOG-Maml ApplicationPackageManagerInjector.hookGetBadgedDrawable return true");
            return drawable;
        }
        Bitmap bitmap;
        if (canBadgeInPlace) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(badgedWidth, badgedHeight, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        if (!canBadgeInPlace) {
            drawable.setBounds(0, 0, badgedWidth, badgedHeight);
            drawable.draw(canvas);
        }
        if (badgeLocation == null) {
            badgeDrawable.setBounds(0, 0, badgedWidth, badgedHeight);
            badgeDrawable.draw(canvas);
        } else if (badgeLocation.left < 0 || badgeLocation.top < 0 || badgeLocation.width() > badgedWidth || badgeLocation.height() > badgedHeight) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Badge location ");
            stringBuilder.append(badgeLocation);
            stringBuilder.append(" not in badged drawable bounds ");
            stringBuilder.append(new Rect(0, 0, badgedWidth, badgedHeight));
            throw new IllegalArgumentException(stringBuilder.toString());
        } else {
            badgeDrawable.setBounds(0, 0, badgeLocation.width(), badgeLocation.height());
            canvas.save();
            canvas.translate((float) badgeLocation.left, (float) badgeLocation.top);
            badgeDrawable.draw(canvas);
            canvas.restore();
        }
        if (canBadgeInPlace) {
            return drawable;
        }
        BitmapDrawable mergedDrawable = new BitmapDrawable(this.mContext.getResources(), bitmap);
        if (drawable instanceof BitmapDrawable) {
            mergedDrawable.setTargetDensity(((BitmapDrawable) drawable).getBitmap().getDensity());
        }
        return mergedDrawable;
    }

    private boolean isManagedProfile(int userId) {
        return getUserManager().isManagedProfile(userId);
    }

    public int getInstallReason(String packageName, UserHandle user) {
        try {
            return this.mPM.getInstallReason(packageName, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean canRequestPackageInstalls() {
        try {
            return this.mPM.canRequestPackageInstalls(this.mContext.getPackageName(), getUserId());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public ComponentName getInstantAppResolverSettingsComponent() {
        try {
            return this.mPM.getInstantAppResolverSettingsComponent();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public ComponentName getInstantAppInstallerComponent() {
        try {
            return this.mPM.getInstantAppInstallerComponent();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getInstantAppAndroidId(String packageName, UserHandle user) {
        try {
            return this.mPM.getInstantAppAndroidId(packageName, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ContextImpl getContext() {
        return this.mContext;
    }

    public void registerDexModule(String dexModule, DexModuleRegisterCallback callback) {
        boolean isSharedModule = false;
        try {
            if ((OsConstants.S_IROTH & Os.stat(dexModule).st_mode) != 0) {
                isSharedModule = true;
            }
            DexModuleRegisterCallbackDelegate callbackDelegate = null;
            if (callback != null) {
                callbackDelegate = new DexModuleRegisterCallbackDelegate(callback);
            }
            try {
                this.mPM.registerDexModule(this.mContext.getPackageName(), dexModule, isSharedModule, callbackDelegate);
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        } catch (ErrnoException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not get stat the module file: ");
            stringBuilder.append(e2.getMessage());
            callback.onDexModuleRegistered(dexModule, false, stringBuilder.toString());
        }
    }

    public CharSequence getHarmfulAppWarning(String packageName) {
        try {
            return this.mPM.getHarmfulAppWarning(packageName, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setHarmfulAppWarning(String packageName, CharSequence warning) {
        try {
            this.mPM.setHarmfulAppWarning(packageName, warning, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public ArtManager getArtManager() {
        ArtManager artManager;
        synchronized (this.mLock) {
            if (this.mArtManager == null) {
                try {
                    this.mArtManager = new ArtManager(this.mContext, this.mPM.getArtManager());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            artManager = this.mArtManager;
        }
        return artManager;
    }

    public String getSystemTextClassifierPackageName() {
        try {
            return this.mPM.getSystemTextClassifierPackageName();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getAttentionServicePackageName() {
        try {
            return this.mPM.getAttentionServicePackageName();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getWellbeingPackageName() {
        try {
            return this.mPM.getWellbeingPackageName();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getAppPredictionServicePackageName() {
        try {
            return this.mPM.getAppPredictionServicePackageName();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getSystemCaptionsServicePackageName() {
        try {
            return this.mPM.getSystemCaptionsServicePackageName();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getIncidentReportApproverPackageName() {
        try {
            return this.mPM.getIncidentReportApproverPackageName();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isPackageStateProtected(String packageName, int userId) {
        try {
            return this.mPM.isPackageStateProtected(packageName, userId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void sendDeviceCustomizationReadyBroadcast() {
        try {
            this.mPM.sendDeviceCustomizationReadyBroadcast();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }
}
