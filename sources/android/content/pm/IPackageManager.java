package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.dex.IArtManager;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public interface IPackageManager extends IInterface {

    public static class Default implements IPackageManager {
        public void checkPackageStartable(String packageName, int userId) throws RemoteException {
        }

        public boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
            return false;
        }

        public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        public PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int flags, int userId) throws RemoteException {
            return null;
        }

        public int getPackageUid(String packageName, int flags, int userId) throws RemoteException {
            return 0;
        }

        public int[] getPackageGids(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        public String[] currentToCanonicalPackageNames(String[] names) throws RemoteException {
            return null;
        }

        public String[] canonicalToCurrentPackageNames(String[] names) throws RemoteException {
            return null;
        }

        public PermissionInfo getPermissionInfo(String name, String packageName, int flags) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryPermissionsByGroup(String group, int flags) throws RemoteException {
            return null;
        }

        public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
            return null;
        }

        public ParceledListSlice getAllPermissionGroups(int flags) throws RemoteException {
            return null;
        }

        public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        public ActivityInfo getActivityInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        public boolean activitySupportsIntent(ComponentName className, Intent intent, String resolvedType) throws RemoteException {
            return false;
        }

        public ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        public ServiceInfo getServiceInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        public ProviderInfo getProviderInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        public int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
            return 0;
        }

        public int checkUidPermission(String permName, int uid) throws RemoteException {
            return 0;
        }

        public boolean addPermission(PermissionInfo info) throws RemoteException {
            return false;
        }

        public void removePermission(String name) throws RemoteException {
        }

        public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        }

        public void revokeRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        }

        public void resetRuntimePermissions() throws RemoteException {
        }

        public int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
            return 0;
        }

        public void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, int userId) throws RemoteException {
        }

        public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) throws RemoteException {
        }

        public List<String> getWhitelistedRestrictedPermissions(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        public boolean addWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
            return false;
        }

        public boolean removeWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
            return false;
        }

        public boolean shouldShowRequestPermissionRationale(String permissionName, String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean isProtectedBroadcast(String actionName) throws RemoteException {
            return false;
        }

        public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
            return 0;
        }

        public int checkUidSignatures(int uid1, int uid2) throws RemoteException {
            return 0;
        }

        public List<String> getAllPackages() throws RemoteException {
            return null;
        }

        public String[] getPackagesForUid(int uid) throws RemoteException {
            return null;
        }

        public String getNameForUid(int uid) throws RemoteException {
            return null;
        }

        public String[] getNamesForUids(int[] uids) throws RemoteException {
            return null;
        }

        public int getUidForSharedUser(String sharedUserName) throws RemoteException {
            return 0;
        }

        public int getFlagsForUid(int uid) throws RemoteException {
            return 0;
        }

        public int getPrivateFlagsForUid(int uid) throws RemoteException {
            return 0;
        }

        public boolean isUidPrivileged(int uid) throws RemoteException {
            return false;
        }

        public String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
            return null;
        }

        public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ResolveInfo findPersistentPreferredActivity(Intent intent, int userId) throws RemoteException {
            return null;
        }

        public boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId) throws RemoteException {
            return false;
        }

        public ParceledListSlice queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryIntentActivityOptions(ComponentName caller, Intent[] specifics, String[] specificTypes, Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getPackagesHoldingPermissions(String[] permissions, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getPersistentApplications(int flags) throws RemoteException {
            return null;
        }

        public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
            return null;
        }

        public void querySyncProviders(List<String> list, List<ProviderInfo> list2) throws RemoteException {
        }

        public ParceledListSlice queryContentProviders(String processName, int uid, int flags, String metaDataKey) throws RemoteException {
            return null;
        }

        public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryInstrumentation(String targetPackage, int flags) throws RemoteException {
            return null;
        }

        public void finishPackageInstall(int token, boolean didLaunch) throws RemoteException {
        }

        public void setInstallerPackageName(String targetPackage, String installerPackageName) throws RemoteException {
        }

        public void setApplicationCategoryHint(String packageName, int categoryHint, String callerPackageName) throws RemoteException {
        }

        public void deletePackageAsUser(String packageName, int versionCode, IPackageDeleteObserver observer, int userId, int flags) throws RemoteException {
        }

        public void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 observer, int userId, int flags) throws RemoteException {
        }

        public String getInstallerPackageName(String packageName) throws RemoteException {
            return null;
        }

        public void resetApplicationPreferences(int userId) throws RemoteException {
        }

        public ResolveInfo getLastChosenActivity(Intent intent, String resolvedType, int flags) throws RemoteException {
            return null;
        }

        public void setLastChosenActivity(Intent intent, String resolvedType, int flags, IntentFilter filter, int match, ComponentName activity) throws RemoteException {
        }

        public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
        }

        public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
        }

        public void clearPackagePreferredActivities(String packageName) throws RemoteException {
        }

        public int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list2, String packageName) throws RemoteException {
            return 0;
        }

        public void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId) throws RemoteException {
        }

        public void clearPackagePersistentPreferredActivities(String packageName, int userId) throws RemoteException {
        }

        public void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int sourceUserId, int targetUserId, int flags) throws RemoteException {
        }

        public void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage) throws RemoteException {
        }

        public String[] setDistractingPackageRestrictionsAsUser(String[] packageNames, int restrictionFlags, int userId) throws RemoteException {
            return null;
        }

        public String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo, String callingPackage, int userId) throws RemoteException {
            return null;
        }

        public String[] getUnsuspendablePackagesForUser(String[] packageNames, int userId) throws RemoteException {
            return null;
        }

        public boolean isPackageSuspendedForUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        public PersistableBundle getSuspendedPackageAppExtras(String packageName, int userId) throws RemoteException {
            return null;
        }

        public byte[] getPreferredActivityBackup(int userId) throws RemoteException {
            return null;
        }

        public void restorePreferredActivities(byte[] backup, int userId) throws RemoteException {
        }

        public byte[] getDefaultAppsBackup(int userId) throws RemoteException {
            return null;
        }

        public void restoreDefaultApps(byte[] backup, int userId) throws RemoteException {
        }

        public byte[] getIntentFilterVerificationBackup(int userId) throws RemoteException {
            return null;
        }

        public void restoreIntentFilterVerification(byte[] backup, int userId) throws RemoteException {
        }

        public ComponentName getHomeActivities(List<ResolveInfo> list) throws RemoteException {
            return null;
        }

        public void setHomeActivity(ComponentName className, int userId) throws RemoteException {
        }

        public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
        }

        public int getComponentEnabledSetting(ComponentName componentName, int userId) throws RemoteException {
            return 0;
        }

        public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
        }

        public int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
            return 0;
        }

        public void logAppProcessStartIfNeeded(String processName, int uid, String seinfo, String apkFile, int pid) throws RemoteException {
        }

        public void flushPackageRestrictionsAsUser(int userId) throws RemoteException {
        }

        public void setPackageStoppedState(String packageName, boolean stopped, int userId) throws RemoteException {
        }

        public void freeStorageAndNotify(String volumeUuid, long freeStorageSize, int storageFlags, IPackageDataObserver observer) throws RemoteException {
        }

        public void freeStorage(String volumeUuid, long freeStorageSize, int storageFlags, IntentSender pi) throws RemoteException {
        }

        public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) throws RemoteException {
        }

        public void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer) throws RemoteException {
        }

        public void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) throws RemoteException {
        }

        public void clearApplicationProfileData(String packageName) throws RemoteException {
        }

        public void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) throws RemoteException {
        }

        public String[] getSystemSharedLibraryNames() throws RemoteException {
            return null;
        }

        public ParceledListSlice getSystemAvailableFeatures() throws RemoteException {
            return null;
        }

        public boolean hasSystemFeature(String name, int version) throws RemoteException {
            return false;
        }

        public void enterSafeMode() throws RemoteException {
        }

        public boolean isSafeMode() throws RemoteException {
            return false;
        }

        public void systemReady() throws RemoteException {
        }

        public boolean hasSystemUidErrors() throws RemoteException {
            return false;
        }

        public void performFstrimIfNeeded() throws RemoteException {
        }

        public void updatePackagesIfNeeded() throws RemoteException {
        }

        public void notifyPackageUse(String packageName, int reason) throws RemoteException {
        }

        public void notifyDexLoad(String loadingPackageName, List<String> list, List<String> list2, String loaderIsa) throws RemoteException {
        }

        public void registerDexModule(String packageName, String dexModulePath, boolean isSharedModule, IDexModuleRegisterCallback callback) throws RemoteException {
        }

        public boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName) throws RemoteException {
            return false;
        }

        public boolean performDexOptSecondary(String packageName, String targetCompilerFilter, boolean force) throws RemoteException {
            return false;
        }

        public boolean compileLayouts(String packageName) throws RemoteException {
            return false;
        }

        public void dumpProfiles(String packageName) throws RemoteException {
        }

        public void forceDexOpt(String packageName) throws RemoteException {
        }

        public boolean runBackgroundDexoptJob(List<String> list) throws RemoteException {
            return false;
        }

        public void reconcileSecondaryDexFiles(String packageName) throws RemoteException {
        }

        public int getMoveStatus(int moveId) throws RemoteException {
            return 0;
        }

        public void registerMoveCallback(IPackageMoveObserver callback) throws RemoteException {
        }

        public void unregisterMoveCallback(IPackageMoveObserver callback) throws RemoteException {
        }

        public int movePackage(String packageName, String volumeUuid) throws RemoteException {
            return 0;
        }

        public int movePrimaryStorage(String volumeUuid) throws RemoteException {
            return 0;
        }

        public boolean addPermissionAsync(PermissionInfo info) throws RemoteException {
            return false;
        }

        public boolean setInstallLocation(int loc) throws RemoteException {
            return false;
        }

        public int getInstallLocation() throws RemoteException {
            return 0;
        }

        public int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> list) throws RemoteException {
            return 0;
        }

        public void verifyPendingInstall(int id, int verificationCode) throws RemoteException {
        }

        public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) throws RemoteException {
        }

        public void verifyIntentFilter(int id, int verificationCode, List<String> list) throws RemoteException {
        }

        public int getIntentVerificationStatus(String packageName, int userId) throws RemoteException {
            return 0;
        }

        public boolean updateIntentVerificationStatus(String packageName, int status, int userId) throws RemoteException {
            return false;
        }

        public ParceledListSlice getIntentFilterVerifications(String packageName) throws RemoteException {
            return null;
        }

        public ParceledListSlice getAllIntentFilters(String packageName) throws RemoteException {
            return null;
        }

        public boolean setDefaultBrowserPackageName(String packageName, int userId) throws RemoteException {
            return false;
        }

        public String getDefaultBrowserPackageName(int userId) throws RemoteException {
            return null;
        }

        public VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
            return null;
        }

        public boolean isFirstBoot() throws RemoteException {
            return false;
        }

        public boolean isOnlyCoreApps() throws RemoteException {
            return false;
        }

        public boolean isDeviceUpgrading() throws RemoteException {
            return false;
        }

        public void setPermissionEnforced(String permission, boolean enforced) throws RemoteException {
        }

        public boolean isPermissionEnforced(String permission) throws RemoteException {
            return false;
        }

        public boolean isStorageLow() throws RemoteException {
            return false;
        }

        public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId) throws RemoteException {
            return false;
        }

        public boolean getApplicationHiddenSettingAsUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void setSystemAppHiddenUntilInstalled(String packageName, boolean hidden) throws RemoteException {
        }

        public boolean setSystemAppInstallState(String packageName, boolean installed, int userId) throws RemoteException {
            return false;
        }

        public IPackageInstaller getPackageInstaller() throws RemoteException {
            return null;
        }

        public boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId) throws RemoteException {
            return false;
        }

        public boolean getBlockUninstallForUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        public KeySet getKeySetByAlias(String packageName, String alias) throws RemoteException {
            return null;
        }

        public KeySet getSigningKeySet(String packageName) throws RemoteException {
            return null;
        }

        public boolean isPackageSignedByKeySet(String packageName, KeySet ks) throws RemoteException {
            return false;
        }

        public boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks) throws RemoteException {
            return false;
        }

        public void addOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
        }

        public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
        }

        public void grantDefaultPermissionsToEnabledCarrierApps(String[] packageNames, int userId) throws RemoteException {
        }

        public void grantDefaultPermissionsToEnabledImsServices(String[] packageNames, int userId) throws RemoteException {
        }

        public void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
        }

        public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
        }

        public void grantDefaultPermissionsToActiveLuiApp(String packageName, int userId) throws RemoteException {
        }

        public void revokeDefaultPermissionsFromLuiApps(String[] packageNames, int userId) throws RemoteException {
        }

        public boolean isPermissionRevokedByPolicy(String permission, String packageName, int userId) throws RemoteException {
            return false;
        }

        public String getPermissionControllerPackageName() throws RemoteException {
            return null;
        }

        public ParceledListSlice getInstantApps(int userId) throws RemoteException {
            return null;
        }

        public byte[] getInstantAppCookie(String packageName, int userId) throws RemoteException {
            return null;
        }

        public boolean setInstantAppCookie(String packageName, byte[] cookie, int userId) throws RemoteException {
            return false;
        }

        public Bitmap getInstantAppIcon(String packageName, int userId) throws RemoteException {
            return null;
        }

        public boolean isInstantApp(String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean setRequiredForSystemUser(String packageName, boolean systemUserApp) throws RemoteException {
            return false;
        }

        public void setUpdateAvailable(String packageName, boolean updateAvaialble) throws RemoteException {
        }

        public String getServicesSystemSharedLibraryPackageName() throws RemoteException {
            return null;
        }

        public String getSharedSystemSharedLibraryPackageName() throws RemoteException {
            return null;
        }

        public ChangedPackages getChangedPackages(int sequenceNumber, int userId) throws RemoteException {
            return null;
        }

        public boolean isPackageDeviceAdminOnAnyUser(String packageName) throws RemoteException {
            return false;
        }

        public int getInstallReason(String packageName, int userId) throws RemoteException {
            return 0;
        }

        public ParceledListSlice getSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getDeclaredSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        public boolean canRequestPackageInstalls(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void deletePreloadsFileCache() throws RemoteException {
        }

        public ComponentName getInstantAppResolverComponent() throws RemoteException {
            return null;
        }

        public ComponentName getInstantAppResolverSettingsComponent() throws RemoteException {
            return null;
        }

        public ComponentName getInstantAppInstallerComponent() throws RemoteException {
            return null;
        }

        public String getInstantAppAndroidId(String packageName, int userId) throws RemoteException {
            return null;
        }

        public IArtManager getArtManager() throws RemoteException {
            return null;
        }

        public void setHarmfulAppWarning(String packageName, CharSequence warning, int userId) throws RemoteException {
        }

        public CharSequence getHarmfulAppWarning(String packageName, int userId) throws RemoteException {
            return null;
        }

        public boolean hasSigningCertificate(String packageName, byte[] signingCertificate, int flags) throws RemoteException {
            return false;
        }

        public boolean hasUidSigningCertificate(int uid, byte[] signingCertificate, int flags) throws RemoteException {
            return false;
        }

        public String getSystemTextClassifierPackageName() throws RemoteException {
            return null;
        }

        public String getAttentionServicePackageName() throws RemoteException {
            return null;
        }

        public String getWellbeingPackageName() throws RemoteException {
            return null;
        }

        public String getAppPredictionServicePackageName() throws RemoteException {
            return null;
        }

        public String getSystemCaptionsServicePackageName() throws RemoteException {
            return null;
        }

        public String getIncidentReportApproverPackageName() throws RemoteException {
            return null;
        }

        public boolean isPackageStateProtected(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void sendDeviceCustomizationReadyBroadcast() throws RemoteException {
        }

        public List<ModuleInfo> getInstalledModules(int flags) throws RemoteException {
            return null;
        }

        public ModuleInfo getModuleInfo(String packageName, int flags) throws RemoteException {
            return null;
        }

        public void revokeRuntimePermissionNotKill(String packageName, String permissionName, int userId) throws RemoteException {
        }

        public int getRuntimePermissionsVersion(int userId) throws RemoteException {
            return 0;
        }

        public void setRuntimePermissionsVersion(int version, int userId) throws RemoteException {
        }

        public void notifyPackagesReplacedReceived(String[] packages) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPackageManager {
        private static final String DESCRIPTOR = "android.content.pm.IPackageManager";
        static final int TRANSACTION_activitySupportsIntent = 15;
        static final int TRANSACTION_addCrossProfileIntentFilter = 78;
        static final int TRANSACTION_addOnPermissionsChangeListener = 162;
        static final int TRANSACTION_addPermission = 21;
        static final int TRANSACTION_addPermissionAsync = 131;
        static final int TRANSACTION_addPersistentPreferredActivity = 76;
        static final int TRANSACTION_addPreferredActivity = 72;
        static final int TRANSACTION_addWhitelistedRestrictedPermission = 30;
        static final int TRANSACTION_canForwardTo = 47;
        static final int TRANSACTION_canRequestPackageInstalls = 186;
        static final int TRANSACTION_canonicalToCurrentPackageNames = 8;
        static final int TRANSACTION_checkPackageStartable = 1;
        static final int TRANSACTION_checkPermission = 19;
        static final int TRANSACTION_checkSignatures = 34;
        static final int TRANSACTION_checkUidPermission = 20;
        static final int TRANSACTION_checkUidSignatures = 35;
        static final int TRANSACTION_clearApplicationProfileData = 105;
        static final int TRANSACTION_clearApplicationUserData = 104;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 79;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 77;
        static final int TRANSACTION_clearPackagePreferredActivities = 74;
        static final int TRANSACTION_compileLayouts = 121;
        static final int TRANSACTION_currentToCanonicalPackageNames = 7;
        static final int TRANSACTION_deleteApplicationCacheFiles = 102;
        static final int TRANSACTION_deleteApplicationCacheFilesAsUser = 103;
        static final int TRANSACTION_deletePackageAsUser = 66;
        static final int TRANSACTION_deletePackageVersioned = 67;
        static final int TRANSACTION_deletePreloadsFileCache = 187;
        static final int TRANSACTION_dumpProfiles = 122;
        static final int TRANSACTION_enterSafeMode = 110;
        static final int TRANSACTION_extendVerificationTimeout = 136;
        static final int TRANSACTION_findPersistentPreferredActivity = 46;
        static final int TRANSACTION_finishPackageInstall = 63;
        static final int TRANSACTION_flushPackageRestrictionsAsUser = 98;
        static final int TRANSACTION_forceDexOpt = 123;
        static final int TRANSACTION_freeStorage = 101;
        static final int TRANSACTION_freeStorageAndNotify = 100;
        static final int TRANSACTION_getActivityInfo = 14;
        static final int TRANSACTION_getAllIntentFilters = 141;
        static final int TRANSACTION_getAllPackages = 36;
        static final int TRANSACTION_getAllPermissionGroups = 12;
        static final int TRANSACTION_getAppOpPermissionPackages = 44;
        static final int TRANSACTION_getAppPredictionServicePackageName = 200;
        static final int TRANSACTION_getApplicationEnabledSetting = 96;
        static final int TRANSACTION_getApplicationHiddenSettingAsUser = 152;
        static final int TRANSACTION_getApplicationInfo = 13;
        static final int TRANSACTION_getArtManager = 192;
        static final int TRANSACTION_getAttentionServicePackageName = 198;
        static final int TRANSACTION_getBlockUninstallForUser = 157;
        static final int TRANSACTION_getChangedPackages = 181;
        static final int TRANSACTION_getComponentEnabledSetting = 94;
        static final int TRANSACTION_getDeclaredSharedLibraries = 185;
        static final int TRANSACTION_getDefaultAppsBackup = 87;
        static final int TRANSACTION_getDefaultBrowserPackageName = 143;
        static final int TRANSACTION_getFlagsForUid = 41;
        static final int TRANSACTION_getHarmfulAppWarning = 194;
        static final int TRANSACTION_getHomeActivities = 91;
        static final int TRANSACTION_getIncidentReportApproverPackageName = 202;
        static final int TRANSACTION_getInstallLocation = 133;
        static final int TRANSACTION_getInstallReason = 183;
        static final int TRANSACTION_getInstalledApplications = 56;
        static final int TRANSACTION_getInstalledModules = 205;
        static final int TRANSACTION_getInstalledPackages = 54;
        static final int TRANSACTION_getInstallerPackageName = 68;
        static final int TRANSACTION_getInstantAppAndroidId = 191;
        static final int TRANSACTION_getInstantAppCookie = 173;
        static final int TRANSACTION_getInstantAppIcon = 175;
        static final int TRANSACTION_getInstantAppInstallerComponent = 190;
        static final int TRANSACTION_getInstantAppResolverComponent = 188;
        static final int TRANSACTION_getInstantAppResolverSettingsComponent = 189;
        static final int TRANSACTION_getInstantApps = 172;
        static final int TRANSACTION_getInstrumentationInfo = 61;
        static final int TRANSACTION_getIntentFilterVerificationBackup = 89;
        static final int TRANSACTION_getIntentFilterVerifications = 140;
        static final int TRANSACTION_getIntentVerificationStatus = 138;
        static final int TRANSACTION_getKeySetByAlias = 158;
        static final int TRANSACTION_getLastChosenActivity = 70;
        static final int TRANSACTION_getModuleInfo = 206;
        static final int TRANSACTION_getMoveStatus = 126;
        static final int TRANSACTION_getNameForUid = 38;
        static final int TRANSACTION_getNamesForUids = 39;
        static final int TRANSACTION_getPackageGids = 6;
        static final int TRANSACTION_getPackageInfo = 3;
        static final int TRANSACTION_getPackageInfoVersioned = 4;
        static final int TRANSACTION_getPackageInstaller = 155;
        static final int TRANSACTION_getPackageSizeInfo = 106;
        static final int TRANSACTION_getPackageUid = 5;
        static final int TRANSACTION_getPackagesForUid = 37;
        static final int TRANSACTION_getPackagesHoldingPermissions = 55;
        static final int TRANSACTION_getPermissionControllerPackageName = 171;
        static final int TRANSACTION_getPermissionFlags = 26;
        static final int TRANSACTION_getPermissionGroupInfo = 11;
        static final int TRANSACTION_getPermissionInfo = 9;
        static final int TRANSACTION_getPersistentApplications = 57;
        static final int TRANSACTION_getPreferredActivities = 75;
        static final int TRANSACTION_getPreferredActivityBackup = 85;
        static final int TRANSACTION_getPrivateFlagsForUid = 42;
        static final int TRANSACTION_getProviderInfo = 18;
        static final int TRANSACTION_getReceiverInfo = 16;
        static final int TRANSACTION_getRuntimePermissionsVersion = 208;
        static final int TRANSACTION_getServiceInfo = 17;
        static final int TRANSACTION_getServicesSystemSharedLibraryPackageName = 179;
        static final int TRANSACTION_getSharedLibraries = 184;
        static final int TRANSACTION_getSharedSystemSharedLibraryPackageName = 180;
        static final int TRANSACTION_getSigningKeySet = 159;
        static final int TRANSACTION_getSuspendedPackageAppExtras = 84;
        static final int TRANSACTION_getSystemAvailableFeatures = 108;
        static final int TRANSACTION_getSystemCaptionsServicePackageName = 201;
        static final int TRANSACTION_getSystemSharedLibraryNames = 107;
        static final int TRANSACTION_getSystemTextClassifierPackageName = 197;
        static final int TRANSACTION_getUidForSharedUser = 40;
        static final int TRANSACTION_getUnsuspendablePackagesForUser = 82;
        static final int TRANSACTION_getVerifierDeviceIdentity = 144;
        static final int TRANSACTION_getWellbeingPackageName = 199;
        static final int TRANSACTION_getWhitelistedRestrictedPermissions = 29;
        static final int TRANSACTION_grantDefaultPermissionsToActiveLuiApp = 168;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledCarrierApps = 164;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledImsServices = 165;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledTelephonyDataServices = 166;
        static final int TRANSACTION_grantRuntimePermission = 23;
        static final int TRANSACTION_hasSigningCertificate = 195;
        static final int TRANSACTION_hasSystemFeature = 109;
        static final int TRANSACTION_hasSystemUidErrors = 113;
        static final int TRANSACTION_hasUidSigningCertificate = 196;
        static final int TRANSACTION_installExistingPackageAsUser = 134;
        static final int TRANSACTION_isDeviceUpgrading = 147;
        static final int TRANSACTION_isFirstBoot = 145;
        static final int TRANSACTION_isInstantApp = 176;
        static final int TRANSACTION_isOnlyCoreApps = 146;
        static final int TRANSACTION_isPackageAvailable = 2;
        static final int TRANSACTION_isPackageDeviceAdminOnAnyUser = 182;
        static final int TRANSACTION_isPackageSignedByKeySet = 160;
        static final int TRANSACTION_isPackageSignedByKeySetExactly = 161;
        static final int TRANSACTION_isPackageStateProtected = 203;
        static final int TRANSACTION_isPackageSuspendedForUser = 83;
        static final int TRANSACTION_isPermissionEnforced = 149;
        static final int TRANSACTION_isPermissionRevokedByPolicy = 170;
        static final int TRANSACTION_isProtectedBroadcast = 33;
        static final int TRANSACTION_isSafeMode = 111;
        static final int TRANSACTION_isStorageLow = 150;
        static final int TRANSACTION_isUidPrivileged = 43;
        static final int TRANSACTION_logAppProcessStartIfNeeded = 97;
        static final int TRANSACTION_movePackage = 129;
        static final int TRANSACTION_movePrimaryStorage = 130;
        static final int TRANSACTION_notifyDexLoad = 117;
        static final int TRANSACTION_notifyPackageUse = 116;
        static final int TRANSACTION_notifyPackagesReplacedReceived = 210;
        static final int TRANSACTION_performDexOptMode = 119;
        static final int TRANSACTION_performDexOptSecondary = 120;
        static final int TRANSACTION_performFstrimIfNeeded = 114;
        static final int TRANSACTION_queryContentProviders = 60;
        static final int TRANSACTION_queryInstrumentation = 62;
        static final int TRANSACTION_queryIntentActivities = 48;
        static final int TRANSACTION_queryIntentActivityOptions = 49;
        static final int TRANSACTION_queryIntentContentProviders = 53;
        static final int TRANSACTION_queryIntentReceivers = 50;
        static final int TRANSACTION_queryIntentServices = 52;
        static final int TRANSACTION_queryPermissionsByGroup = 10;
        static final int TRANSACTION_querySyncProviders = 59;
        static final int TRANSACTION_reconcileSecondaryDexFiles = 125;
        static final int TRANSACTION_registerDexModule = 118;
        static final int TRANSACTION_registerMoveCallback = 127;
        static final int TRANSACTION_removeOnPermissionsChangeListener = 163;
        static final int TRANSACTION_removePermission = 22;
        static final int TRANSACTION_removeWhitelistedRestrictedPermission = 31;
        static final int TRANSACTION_replacePreferredActivity = 73;
        static final int TRANSACTION_resetApplicationPreferences = 69;
        static final int TRANSACTION_resetRuntimePermissions = 25;
        static final int TRANSACTION_resolveContentProvider = 58;
        static final int TRANSACTION_resolveIntent = 45;
        static final int TRANSACTION_resolveService = 51;
        static final int TRANSACTION_restoreDefaultApps = 88;
        static final int TRANSACTION_restoreIntentFilterVerification = 90;
        static final int TRANSACTION_restorePreferredActivities = 86;
        static final int TRANSACTION_revokeDefaultPermissionsFromDisabledTelephonyDataServices = 167;
        static final int TRANSACTION_revokeDefaultPermissionsFromLuiApps = 169;
        static final int TRANSACTION_revokeRuntimePermission = 24;
        static final int TRANSACTION_revokeRuntimePermissionNotKill = 207;
        static final int TRANSACTION_runBackgroundDexoptJob = 124;
        static final int TRANSACTION_sendDeviceCustomizationReadyBroadcast = 204;
        static final int TRANSACTION_setApplicationCategoryHint = 65;
        static final int TRANSACTION_setApplicationEnabledSetting = 95;
        static final int TRANSACTION_setApplicationHiddenSettingAsUser = 151;
        static final int TRANSACTION_setBlockUninstallForUser = 156;
        static final int TRANSACTION_setComponentEnabledSetting = 93;
        static final int TRANSACTION_setDefaultBrowserPackageName = 142;
        static final int TRANSACTION_setDistractingPackageRestrictionsAsUser = 80;
        static final int TRANSACTION_setHarmfulAppWarning = 193;
        static final int TRANSACTION_setHomeActivity = 92;
        static final int TRANSACTION_setInstallLocation = 132;
        static final int TRANSACTION_setInstallerPackageName = 64;
        static final int TRANSACTION_setInstantAppCookie = 174;
        static final int TRANSACTION_setLastChosenActivity = 71;
        static final int TRANSACTION_setPackageStoppedState = 99;
        static final int TRANSACTION_setPackagesSuspendedAsUser = 81;
        static final int TRANSACTION_setPermissionEnforced = 148;
        static final int TRANSACTION_setRequiredForSystemUser = 177;
        static final int TRANSACTION_setRuntimePermissionsVersion = 209;
        static final int TRANSACTION_setSystemAppHiddenUntilInstalled = 153;
        static final int TRANSACTION_setSystemAppInstallState = 154;
        static final int TRANSACTION_setUpdateAvailable = 178;
        static final int TRANSACTION_shouldShowRequestPermissionRationale = 32;
        static final int TRANSACTION_systemReady = 112;
        static final int TRANSACTION_unregisterMoveCallback = 128;
        static final int TRANSACTION_updateIntentVerificationStatus = 139;
        static final int TRANSACTION_updatePackagesIfNeeded = 115;
        static final int TRANSACTION_updatePermissionFlags = 27;
        static final int TRANSACTION_updatePermissionFlagsForAllApps = 28;
        static final int TRANSACTION_verifyIntentFilter = 137;
        static final int TRANSACTION_verifyPendingInstall = 135;

        private static class Proxy implements IPackageManager {
            public static IPackageManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void checkPackageStartable(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().checkPackageStartable(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPackageAvailable(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    PackageInfo packageInfo = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        packageInfo = Stub.getDefaultImpl();
                        if (packageInfo != 0) {
                            packageInfo = Stub.getDefaultImpl().getPackageInfo(packageName, flags, userId);
                            return packageInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        packageInfo = (PackageInfo) PackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        packageInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return packageInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (versionedPackage != null) {
                        _data.writeInt(1);
                        versionedPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    PackageInfo packageInfo = this.mRemote;
                    if (!packageInfo.transact(4, _data, _reply, 0)) {
                        packageInfo = Stub.getDefaultImpl();
                        if (packageInfo != null) {
                            packageInfo = Stub.getDefaultImpl().getPackageInfoVersioned(versionedPackage, flags, userId);
                            return packageInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        packageInfo = (PackageInfo) PackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        packageInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return packageInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPackageUid(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPackageUid(packageName, flags, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getPackageGids(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    int[] iArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getPackageGids(packageName, flags, userId);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] currentToCanonicalPackageNames(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    String[] strArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().currentToCanonicalPackageNames(names);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] canonicalToCurrentPackageNames(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    String[] strArr = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().canonicalToCurrentPackageNames(names);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PermissionInfo getPermissionInfo(String name, String packageName, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    PermissionInfo permissionInfo = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        permissionInfo = Stub.getDefaultImpl();
                        if (permissionInfo != 0) {
                            permissionInfo = Stub.getDefaultImpl().getPermissionInfo(name, packageName, flags);
                            return permissionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        permissionInfo = (PermissionInfo) PermissionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        permissionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return permissionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryPermissionsByGroup(String group, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(group);
                    _data.writeInt(flags);
                    ParceledListSlice parceledListSlice = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().queryPermissionsByGroup(group, flags);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    PermissionGroupInfo permissionGroupInfo = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        permissionGroupInfo = Stub.getDefaultImpl();
                        if (permissionGroupInfo != 0) {
                            permissionGroupInfo = Stub.getDefaultImpl().getPermissionGroupInfo(name, flags);
                            return permissionGroupInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        permissionGroupInfo = (PermissionGroupInfo) PermissionGroupInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        permissionGroupInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return permissionGroupInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAllPermissionGroups(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    ParceledListSlice parceledListSlice = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAllPermissionGroups(flags);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ApplicationInfo applicationInfo = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        applicationInfo = Stub.getDefaultImpl();
                        if (applicationInfo != 0) {
                            applicationInfo = Stub.getDefaultImpl().getApplicationInfo(packageName, flags, userId);
                            return applicationInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        applicationInfo = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        applicationInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return applicationInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ActivityInfo getActivityInfo(ComponentName className, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ActivityInfo activityInfo = this.mRemote;
                    if (!activityInfo.transact(14, _data, _reply, 0)) {
                        activityInfo = Stub.getDefaultImpl();
                        if (activityInfo != null) {
                            activityInfo = Stub.getDefaultImpl().getActivityInfo(className, flags, userId);
                            return activityInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        activityInfo = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        activityInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return activityInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean activitySupportsIntent(ComponentName className, Intent intent, String resolvedType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().activitySupportsIntent(className, intent, resolvedType);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ActivityInfo activityInfo = this.mRemote;
                    if (!activityInfo.transact(16, _data, _reply, 0)) {
                        activityInfo = Stub.getDefaultImpl();
                        if (activityInfo != null) {
                            activityInfo = Stub.getDefaultImpl().getReceiverInfo(className, flags, userId);
                            return activityInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        activityInfo = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        activityInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return activityInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ServiceInfo getServiceInfo(ComponentName className, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ServiceInfo serviceInfo = this.mRemote;
                    if (!serviceInfo.transact(17, _data, _reply, 0)) {
                        serviceInfo = Stub.getDefaultImpl();
                        if (serviceInfo != null) {
                            serviceInfo = Stub.getDefaultImpl().getServiceInfo(className, flags, userId);
                            return serviceInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        serviceInfo = (ServiceInfo) ServiceInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        serviceInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return serviceInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ProviderInfo getProviderInfo(ComponentName className, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ProviderInfo providerInfo = this.mRemote;
                    if (!providerInfo.transact(18, _data, _reply, 0)) {
                        providerInfo = Stub.getDefaultImpl();
                        if (providerInfo != null) {
                            providerInfo = Stub.getDefaultImpl().getProviderInfo(className, flags, userId);
                            return providerInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        providerInfo = (ProviderInfo) ProviderInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        providerInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return providerInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    int i = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkPermission(permName, pkgName, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkUidPermission(String permName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeInt(uid);
                    int i = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkUidPermission(permName, uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addPermission(PermissionInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addPermission(info);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePermission(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePermission(name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantRuntimePermission(packageName, permissionName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeRuntimePermission(packageName, permissionName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetRuntimePermissions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetRuntimePermissions();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPermissionFlags(permissionName, packageName, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, int userId) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(permissionName);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = flagMask;
                        i2 = flagValues;
                        i3 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                        try {
                            _data.writeInt(flagMask);
                            try {
                                _data.writeInt(flagValues);
                                _data.writeInt(checkAdjustPolicyFlagPermission ? 1 : 0);
                            } catch (Throwable th3) {
                                th = th3;
                                i3 = userId;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                            try {
                                _data.writeInt(userId);
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            i2 = flagValues;
                            i3 = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i = flagMask;
                        i2 = flagValues;
                        i3 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().updatePermissionFlags(permissionName, packageName, flagMask, flagValues, checkAdjustPolicyFlagPermission, userId);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str2 = permissionName;
                    str = packageName;
                    i = flagMask;
                    i2 = flagValues;
                    i3 = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flagMask);
                    _data.writeInt(flagValues);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getWhitelistedRestrictedPermissions(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    List<String> list = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWhitelistedRestrictedPermissions(packageName, flags, userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(whitelistFlags);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addWhitelistedRestrictedPermission(packageName, permission, whitelistFlags, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(whitelistFlags);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeWhitelistedRestrictedPermission(packageName, permission, whitelistFlags, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldShowRequestPermissionRationale(String permissionName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldShowRequestPermissionRationale(permissionName, packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isProtectedBroadcast(String actionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(actionName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isProtectedBroadcast(actionName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg1);
                    _data.writeString(pkg2);
                    int i = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkSignatures(pkg1, pkg2);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkUidSignatures(int uid1, int uid2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid1);
                    _data.writeInt(uid2);
                    int i = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkUidSignatures(uid1, uid2);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getAllPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllPackages();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getPackagesForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    String[] strArr = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getPackagesForUid(uid);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNameForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    String str = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getNameForUid(uid);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getNamesForUids(int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uids);
                    String[] strArr = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getNamesForUids(uids);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getUidForSharedUser(String sharedUserName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sharedUserName);
                    int i = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUidForSharedUser(sharedUserName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFlagsForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFlagsForUid(uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPrivateFlagsForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPrivateFlagsForUid(uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUidPrivileged(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUidPrivileged(uid);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    String[] strArr = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getAppOpPermissionPackages(permissionName);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ResolveInfo resolveInfo = this.mRemote;
                    if (!resolveInfo.transact(45, _data, _reply, 0)) {
                        resolveInfo = Stub.getDefaultImpl();
                        if (resolveInfo != null) {
                            resolveInfo = Stub.getDefaultImpl().resolveIntent(intent, resolvedType, flags, userId);
                            return resolveInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        resolveInfo = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        resolveInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return resolveInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ResolveInfo findPersistentPreferredActivity(Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    ResolveInfo resolveInfo = this.mRemote;
                    if (!resolveInfo.transact(46, _data, _reply, 0)) {
                        resolveInfo = Stub.getDefaultImpl();
                        if (resolveInfo != null) {
                            resolveInfo = Stub.getDefaultImpl().findPersistentPreferredActivity(intent, userId);
                            return resolveInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        resolveInfo = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        resolveInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return resolveInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(sourceUserId);
                    _data.writeInt(targetUserId);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().canForwardTo(intent, resolvedType, sourceUserId, targetUserId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(48, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().queryIntentActivities(intent, resolvedType, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryIntentActivityOptions(ComponentName caller, Intent[] specifics, String[] specificTypes, Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Throwable th;
                String str;
                String[] strArr;
                ComponentName componentName = caller;
                Intent intent2 = intent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeTypedArray(specifics, 0);
                        try {
                            _data.writeStringArray(specificTypes);
                            if (intent2 != null) {
                                _data.writeInt(1);
                                intent2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeString(resolvedType);
                                _data.writeInt(flags);
                                _data.writeInt(userId);
                                ParceledListSlice _result;
                                if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    if (_reply.readInt() != 0) {
                                        _result = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                                    } else {
                                        _result = null;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                }
                                _result = Stub.getDefaultImpl().queryIntentActivityOptions(caller, specifics, specificTypes, intent, resolvedType, flags, userId);
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            str = resolvedType;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        strArr = specificTypes;
                        str = resolvedType;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    Intent[] intentArr = specifics;
                    strArr = specificTypes;
                    str = resolvedType;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public ParceledListSlice queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(50, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().queryIntentReceivers(intent, resolvedType, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ResolveInfo resolveInfo = this.mRemote;
                    if (!resolveInfo.transact(51, _data, _reply, 0)) {
                        resolveInfo = Stub.getDefaultImpl();
                        if (resolveInfo != null) {
                            resolveInfo = Stub.getDefaultImpl().resolveService(intent, resolvedType, flags, userId);
                            return resolveInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        resolveInfo = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        resolveInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return resolveInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(52, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().queryIntentServices(intent, resolvedType, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(53, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().queryIntentContentProviders(intent, resolvedType, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 54;
                    if (!this.mRemote.transact(54, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getInstalledPackages(flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getPackagesHoldingPermissions(String[] permissions, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(permissions);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 55;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getPackagesHoldingPermissions(permissions, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 56;
                    if (!this.mRemote.transact(56, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getInstalledApplications(flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getPersistentApplications(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    ParceledListSlice parceledListSlice = 57;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getPersistentApplications(flags);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ProviderInfo providerInfo = 58;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        providerInfo = Stub.getDefaultImpl();
                        if (providerInfo != 0) {
                            providerInfo = Stub.getDefaultImpl().resolveContentProvider(name, flags, userId);
                            return providerInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        providerInfo = (ProviderInfo) ProviderInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        providerInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return providerInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(outNames);
                    _data.writeTypedList(outInfo);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.readStringList(outNames);
                        _reply.readTypedList(outInfo, ProviderInfo.CREATOR);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().querySyncProviders(outNames, outInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryContentProviders(String processName, int uid, int flags, String metaDataKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    _data.writeString(metaDataKey);
                    ParceledListSlice parceledListSlice = 60;
                    if (!this.mRemote.transact(60, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().queryContentProviders(processName, uid, flags, metaDataKey);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    InstrumentationInfo instrumentationInfo = this.mRemote;
                    if (!instrumentationInfo.transact(61, _data, _reply, 0)) {
                        instrumentationInfo = Stub.getDefaultImpl();
                        if (instrumentationInfo != null) {
                            instrumentationInfo = Stub.getDefaultImpl().getInstrumentationInfo(className, flags);
                            return instrumentationInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        instrumentationInfo = (InstrumentationInfo) InstrumentationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        instrumentationInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return instrumentationInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryInstrumentation(String targetPackage, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackage);
                    _data.writeInt(flags);
                    ParceledListSlice parceledListSlice = 62;
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().queryInstrumentation(targetPackage, flags);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishPackageInstall(int token, boolean didLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(didLaunch ? 1 : 0);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishPackageInstall(token, didLaunch);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInstallerPackageName(String targetPackage, String installerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackage);
                    _data.writeString(installerPackageName);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInstallerPackageName(targetPackage, installerPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationCategoryHint(String packageName, int categoryHint, String callerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(categoryHint);
                    _data.writeString(callerPackageName);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationCategoryHint(packageName, categoryHint, callerPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deletePackageAsUser(String packageName, int versionCode, IPackageDeleteObserver observer, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(versionCode);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deletePackageAsUser(packageName, versionCode, observer, userId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 observer, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (versionedPackage != null) {
                        _data.writeInt(1);
                        versionedPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deletePackageVersioned(versionedPackage, observer, userId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getInstallerPackageName(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    String str = 68;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getInstallerPackageName(packageName);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetApplicationPreferences(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetApplicationPreferences(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ResolveInfo getLastChosenActivity(Intent intent, String resolvedType, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    ResolveInfo resolveInfo = this.mRemote;
                    if (!resolveInfo.transact(70, _data, _reply, 0)) {
                        resolveInfo = Stub.getDefaultImpl();
                        if (resolveInfo != null) {
                            resolveInfo = Stub.getDefaultImpl().getLastChosenActivity(intent, resolvedType, flags);
                            return resolveInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        resolveInfo = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        resolveInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return resolveInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLastChosenActivity(Intent intent, String resolvedType, int flags, IntentFilter filter, int match, ComponentName activity) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                Intent intent2 = intent;
                IntentFilter intentFilter = filter;
                ComponentName componentName = activity;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent2 != null) {
                        _data.writeInt(1);
                        intent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(resolvedType);
                    } catch (Throwable th2) {
                        th = th2;
                        i = flags;
                        i2 = match;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(flags);
                        if (intentFilter != null) {
                            _data.writeInt(1);
                            intentFilter.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(match);
                            if (componentName != null) {
                                _data.writeInt(1);
                                componentName.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().setLastChosenActivity(intent, resolvedType, flags, filter, match, activity);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = match;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str = resolvedType;
                    i = flags;
                    i2 = match;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(match);
                    _data.writeTypedArray(set, 0);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPreferredActivity(filter, match, set, activity, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(match);
                    _data.writeTypedArray(set, 0);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().replacePreferredActivity(filter, match, set, activity, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPackagePreferredActivities(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(74, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearPackagePreferredActivities(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int _result = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        _result = Stub.getDefaultImpl();
                        if (_result != 0) {
                            _result = Stub.getDefaultImpl().getPreferredActivities(outFilters, outActivities, packageName);
                            return _result;
                        }
                    }
                    _reply.readException();
                    _result = _reply.readInt();
                    _reply.readTypedList(outFilters, IntentFilter.CREATOR);
                    _reply.readTypedList(outActivities, ComponentName.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPersistentPreferredActivity(filter, activity, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPackagePersistentPreferredActivities(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearPackagePersistentPreferredActivities(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int sourceUserId, int targetUserId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intentFilter != null) {
                        _data.writeInt(1);
                        intentFilter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerPackage);
                    _data.writeInt(sourceUserId);
                    _data.writeInt(targetUserId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addCrossProfileIntentFilter(intentFilter, ownerPackage, sourceUserId, targetUserId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sourceUserId);
                    _data.writeString(ownerPackage);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearCrossProfileIntentFilters(sourceUserId, ownerPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] setDistractingPackageRestrictionsAsUser(String[] packageNames, int restrictionFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(restrictionFlags);
                    _data.writeInt(userId);
                    String[] strArr = 80;
                    if (!this.mRemote.transact(80, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().setDistractingPackageRestrictionsAsUser(packageNames, restrictionFlags, userId);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo, String callingPackage, int userId) throws RemoteException {
                Throwable th;
                String str;
                PersistableBundle persistableBundle = appExtras;
                PersistableBundle persistableBundle2 = launcherExtras;
                SuspendDialogInfo suspendDialogInfo = dialogInfo;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStringArray(packageNames);
                        _data.writeInt(suspended ? 1 : 0);
                        if (persistableBundle != null) {
                            _data.writeInt(1);
                            persistableBundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (persistableBundle2 != null) {
                            _data.writeInt(1);
                            persistableBundle2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (suspendDialogInfo != null) {
                            _data.writeInt(1);
                            suspendDialogInfo.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                        _data.writeInt(userId);
                        if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            String[] _result = _reply.createStringArray();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        String[] packagesSuspendedAsUser = Stub.getDefaultImpl().setPackagesSuspendedAsUser(packageNames, suspended, appExtras, launcherExtras, dialogInfo, callingPackage, userId);
                        _reply.recycle();
                        _data.recycle();
                        return packagesSuspendedAsUser;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String[] strArr = packageNames;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String[] getUnsuspendablePackagesForUser(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    String[] strArr = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getUnsuspendablePackagesForUser(packageNames, userId);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageSuspendedForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPackageSuspendedForUser(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PersistableBundle getSuspendedPackageAppExtras(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    PersistableBundle persistableBundle = 84;
                    if (!this.mRemote.transact(84, _data, _reply, 0)) {
                        persistableBundle = Stub.getDefaultImpl();
                        if (persistableBundle != 0) {
                            persistableBundle = Stub.getDefaultImpl().getSuspendedPackageAppExtras(packageName, userId);
                            return persistableBundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        persistableBundle = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(_reply);
                    } else {
                        persistableBundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return persistableBundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getPreferredActivityBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    byte[] bArr = 85;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getPreferredActivityBackup(userId);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restorePreferredActivities(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restorePreferredActivities(backup, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getDefaultAppsBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    byte[] bArr = 87;
                    if (!this.mRemote.transact(87, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getDefaultAppsBackup(userId);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreDefaultApps(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreDefaultApps(backup, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getIntentFilterVerificationBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    byte[] bArr = 89;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getIntentFilterVerificationBackup(userId);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreIntentFilterVerification(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(90, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreIntentFilterVerification(backup, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getHomeActivities(List<ResolveInfo> outHomeCandidates) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 91;
                    if (!this.mRemote.transact(91, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getHomeActivities(outHomeCandidates);
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.readTypedList(outHomeCandidates, ResolveInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHomeActivity(ComponentName className, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setHomeActivity(className, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(newState);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(93, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setComponentEnabledSetting(componentName, newState, flags, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getComponentEnabledSetting(ComponentName componentName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    int i = this.mRemote;
                    if (!i.transact(94, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getComponentEnabledSetting(componentName, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(newState);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(95, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationEnabledSetting(packageName, newState, flags, userId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 96;
                    if (!this.mRemote.transact(96, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getApplicationEnabledSetting(packageName, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void logAppProcessStartIfNeeded(String processName, int uid, String seinfo, String apkFile, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeString(seinfo);
                    _data.writeString(apkFile);
                    _data.writeInt(pid);
                    if (this.mRemote.transact(97, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().logAppProcessStartIfNeeded(processName, uid, seinfo, apkFile, pid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void flushPackageRestrictionsAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(98, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().flushPackageRestrictionsAsUser(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPackageStoppedState(String packageName, boolean stopped, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(stopped ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(99, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPackageStoppedState(packageName, stopped, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void freeStorageAndNotify(String volumeUuid, long freeStorageSize, int storageFlags, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(freeStorageSize);
                    _data.writeInt(storageFlags);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(100, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().freeStorageAndNotify(volumeUuid, freeStorageSize, storageFlags, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void freeStorage(String volumeUuid, long freeStorageSize, int storageFlags, IntentSender pi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(freeStorageSize);
                    _data.writeInt(storageFlags);
                    if (pi != null) {
                        _data.writeInt(1);
                        pi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(101, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().freeStorage(volumeUuid, freeStorageSize, storageFlags, pi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(102, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteApplicationCacheFiles(packageName, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(103, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteApplicationCacheFilesAsUser(packageName, userId, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(104, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearApplicationUserData(packageName, observer, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearApplicationProfileData(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(105, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearApplicationProfileData(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(106, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getPackageSizeInfo(packageName, userHandle, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getSystemSharedLibraryNames() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 107;
                    if (!this.mRemote.transact(107, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getSystemSharedLibraryNames();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getSystemAvailableFeatures() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 108;
                    if (!this.mRemote.transact(108, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getSystemAvailableFeatures();
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasSystemFeature(String name, int version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(version);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(109, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasSystemFeature(name, version);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(110, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enterSafeMode();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(111, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSafeMode();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void systemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(112, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().systemReady();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasSystemUidErrors() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(113, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasSystemUidErrors();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void performFstrimIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(114, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().performFstrimIfNeeded();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePackagesIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(115, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updatePackagesIfNeeded();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPackageUse(String packageName, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(116, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyPackageUse(packageName, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyDexLoad(String loadingPackageName, List<String> classLoadersNames, List<String> classPaths, String loaderIsa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(loadingPackageName);
                    _data.writeStringList(classLoadersNames);
                    _data.writeStringList(classPaths);
                    _data.writeString(loaderIsa);
                    if (this.mRemote.transact(117, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyDexLoad(loadingPackageName, classLoadersNames, classPaths, loaderIsa);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registerDexModule(String packageName, String dexModulePath, boolean isSharedModule, IDexModuleRegisterCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(dexModulePath);
                    _data.writeInt(isSharedModule ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(118, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerDexModule(packageName, dexModulePath, isSharedModule, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    boolean _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                        _result = true;
                        _data.writeInt(checkProfiles ? 1 : 0);
                    } catch (Throwable th2) {
                        th = th2;
                        str = targetCompilerFilter;
                        str2 = splitName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(targetCompilerFilter);
                        _data.writeInt(force ? 1 : 0);
                        _data.writeInt(bootComplete ? 1 : 0);
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = splitName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(splitName);
                        try {
                            if (this.mRemote.transact(119, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().performDexOptMode(packageName, checkProfiles, targetCompilerFilter, force, bootComplete, splitName);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str3 = packageName;
                    str = targetCompilerFilter;
                    str2 = splitName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean performDexOptSecondary(String packageName, String targetCompilerFilter, boolean force) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(targetCompilerFilter);
                    boolean _result = true;
                    _data.writeInt(force ? 1 : 0);
                    if (this.mRemote.transact(120, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().performDexOptSecondary(packageName, targetCompilerFilter, force);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean compileLayouts(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(121, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().compileLayouts(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dumpProfiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(122, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dumpProfiles(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceDexOpt(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(123, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceDexOpt(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean runBackgroundDexoptJob(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(124, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().runBackgroundDexoptJob(packageNames);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reconcileSecondaryDexFiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(125, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reconcileSecondaryDexFiles(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMoveStatus(int moveId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(moveId);
                    int i = 126;
                    if (!this.mRemote.transact(126, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMoveStatus(moveId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerMoveCallback(IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(127, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerMoveCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterMoveCallback(IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(128, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterMoveCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int movePackage(String packageName, String volumeUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(volumeUuid);
                    int i = 129;
                    if (!this.mRemote.transact(129, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().movePackage(packageName, volumeUuid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int movePrimaryStorage(String volumeUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    int i = 130;
                    if (!this.mRemote.transact(130, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().movePrimaryStorage(volumeUuid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addPermissionAsync(PermissionInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(131, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addPermissionAsync(info);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setInstallLocation(int loc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(loc);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(132, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setInstallLocation(loc);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInstallLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 133;
                    if (!this.mRemote.transact(133, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getInstallLocation();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> whiteListedPermissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(installFlags);
                    _data.writeInt(installReason);
                    _data.writeStringList(whiteListedPermissions);
                    int i = 134;
                    if (!this.mRemote.transact(134, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().installExistingPackageAsUser(packageName, userId, installFlags, installReason, whiteListedPermissions);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void verifyPendingInstall(int id, int verificationCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCode);
                    if (this.mRemote.transact(135, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().verifyPendingInstall(id, verificationCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCodeAtTimeout);
                    _data.writeLong(millisecondsToDelay);
                    if (this.mRemote.transact(136, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void verifyIntentFilter(int id, int verificationCode, List<String> failedDomains) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCode);
                    _data.writeStringList(failedDomains);
                    if (this.mRemote.transact(137, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().verifyIntentFilter(id, verificationCode, failedDomains);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getIntentVerificationStatus(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 138;
                    if (!this.mRemote.transact(138, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getIntentVerificationStatus(packageName, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateIntentVerificationStatus(String packageName, int status, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(status);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(139, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().updateIntentVerificationStatus(packageName, status, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getIntentFilterVerifications(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    ParceledListSlice parceledListSlice = 140;
                    if (!this.mRemote.transact(140, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getIntentFilterVerifications(packageName);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAllIntentFilters(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    ParceledListSlice parceledListSlice = 141;
                    if (!this.mRemote.transact(141, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAllIntentFilters(packageName);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDefaultBrowserPackageName(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(142, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setDefaultBrowserPackageName(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDefaultBrowserPackageName(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String str = 143;
                    if (!this.mRemote.transact(143, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultBrowserPackageName(userId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    VerifierDeviceIdentity verifierDeviceIdentity = 144;
                    if (!this.mRemote.transact(144, _data, _reply, 0)) {
                        verifierDeviceIdentity = Stub.getDefaultImpl();
                        if (verifierDeviceIdentity != 0) {
                            verifierDeviceIdentity = Stub.getDefaultImpl().getVerifierDeviceIdentity();
                            return verifierDeviceIdentity;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        verifierDeviceIdentity = (VerifierDeviceIdentity) VerifierDeviceIdentity.CREATOR.createFromParcel(_reply);
                    } else {
                        verifierDeviceIdentity = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return verifierDeviceIdentity;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFirstBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(145, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isFirstBoot();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOnlyCoreApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(146, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOnlyCoreApps();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDeviceUpgrading() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(147, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceUpgrading();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPermissionEnforced(String permission, boolean enforced) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(enforced ? 1 : 0);
                    if (this.mRemote.transact(148, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPermissionEnforced(permission, enforced);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPermissionEnforced(String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(149, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPermissionEnforced(permission);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isStorageLow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(150, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isStorageLow();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(hidden ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(151, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setApplicationHiddenSettingAsUser(packageName, hidden, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getApplicationHiddenSettingAsUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(152, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getApplicationHiddenSettingAsUser(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemAppHiddenUntilInstalled(String packageName, boolean hidden) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    if (this.mRemote.transact(153, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemAppHiddenUntilInstalled(packageName, hidden);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setSystemAppInstallState(String packageName, boolean installed, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(installed ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(154, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setSystemAppInstallState(packageName, installed, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IPackageInstaller getPackageInstaller() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IPackageInstaller iPackageInstaller = 155;
                    if (!this.mRemote.transact(155, _data, _reply, 0)) {
                        iPackageInstaller = Stub.getDefaultImpl();
                        if (iPackageInstaller != 0) {
                            iPackageInstaller = Stub.getDefaultImpl().getPackageInstaller();
                            return iPackageInstaller;
                        }
                    }
                    _reply.readException();
                    iPackageInstaller = android.content.pm.IPackageInstaller.Stub.asInterface(_reply.readStrongBinder());
                    IPackageInstaller _result = iPackageInstaller;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(blockUninstall ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(156, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setBlockUninstallForUser(packageName, blockUninstall, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getBlockUninstallForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(157, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getBlockUninstallForUser(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeySet getKeySetByAlias(String packageName, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(alias);
                    KeySet keySet = 158;
                    if (!this.mRemote.transact(158, _data, _reply, 0)) {
                        keySet = Stub.getDefaultImpl();
                        if (keySet != 0) {
                            keySet = Stub.getDefaultImpl().getKeySetByAlias(packageName, alias);
                            return keySet;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        keySet = (KeySet) KeySet.CREATOR.createFromParcel(_reply);
                    } else {
                        keySet = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return keySet;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeySet getSigningKeySet(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    KeySet keySet = 159;
                    if (!this.mRemote.transact(159, _data, _reply, 0)) {
                        keySet = Stub.getDefaultImpl();
                        if (keySet != 0) {
                            keySet = Stub.getDefaultImpl().getSigningKeySet(packageName);
                            return keySet;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        keySet = (KeySet) KeySet.CREATOR.createFromParcel(_reply);
                    } else {
                        keySet = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return keySet;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageSignedByKeySet(String packageName, KeySet ks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (ks != null) {
                        _data.writeInt(1);
                        ks.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(160, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isPackageSignedByKeySet(packageName, ks);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (ks != null) {
                        _data.writeInt(1);
                        ks.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(161, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isPackageSignedByKeySetExactly(packageName, ks);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(162, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOnPermissionsChangeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(163, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeOnPermissionsChangeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantDefaultPermissionsToEnabledCarrierApps(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(164, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantDefaultPermissionsToEnabledCarrierApps(packageNames, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantDefaultPermissionsToEnabledImsServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(165, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantDefaultPermissionsToEnabledImsServices(packageNames, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(166, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantDefaultPermissionsToEnabledTelephonyDataServices(packageNames, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(167, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeDefaultPermissionsFromDisabledTelephonyDataServices(packageNames, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantDefaultPermissionsToActiveLuiApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(168, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantDefaultPermissionsToActiveLuiApp(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeDefaultPermissionsFromLuiApps(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(169, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeDefaultPermissionsFromLuiApps(packageNames, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPermissionRevokedByPolicy(String permission, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(170, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPermissionRevokedByPolicy(permission, packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPermissionControllerPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 171;
                    if (!this.mRemote.transact(171, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPermissionControllerPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getInstantApps(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 172;
                    if (!this.mRemote.transact(172, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getInstantApps(userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getInstantAppCookie(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    byte[] bArr = 173;
                    if (!this.mRemote.transact(173, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getInstantAppCookie(packageName, userId);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setInstantAppCookie(String packageName, byte[] cookie, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeByteArray(cookie);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(174, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setInstantAppCookie(packageName, cookie, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap getInstantAppIcon(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    Bitmap bitmap = 175;
                    if (!this.mRemote.transact(175, _data, _reply, 0)) {
                        bitmap = Stub.getDefaultImpl();
                        if (bitmap != 0) {
                            bitmap = Stub.getDefaultImpl().getInstantAppIcon(packageName, userId);
                            return bitmap;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        bitmap = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bitmap;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInstantApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(176, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInstantApp(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRequiredForSystemUser(String packageName, boolean systemUserApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(systemUserApp ? 1 : 0);
                    if (this.mRemote.transact(177, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setRequiredForSystemUser(packageName, systemUserApp);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUpdateAvailable(String packageName, boolean updateAvaialble) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(updateAvaialble ? 1 : 0);
                    if (this.mRemote.transact(178, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUpdateAvailable(packageName, updateAvaialble);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getServicesSystemSharedLibraryPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 179;
                    if (!this.mRemote.transact(179, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getServicesSystemSharedLibraryPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSharedSystemSharedLibraryPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 180;
                    if (!this.mRemote.transact(180, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSharedSystemSharedLibraryPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ChangedPackages getChangedPackages(int sequenceNumber, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequenceNumber);
                    _data.writeInt(userId);
                    ChangedPackages changedPackages = 181;
                    if (!this.mRemote.transact(181, _data, _reply, 0)) {
                        changedPackages = Stub.getDefaultImpl();
                        if (changedPackages != 0) {
                            changedPackages = Stub.getDefaultImpl().getChangedPackages(sequenceNumber, userId);
                            return changedPackages;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        changedPackages = (ChangedPackages) ChangedPackages.CREATOR.createFromParcel(_reply);
                    } else {
                        changedPackages = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return changedPackages;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageDeviceAdminOnAnyUser(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(182, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPackageDeviceAdminOnAnyUser(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInstallReason(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 183;
                    if (!this.mRemote.transact(183, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getInstallReason(packageName, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 184;
                    if (!this.mRemote.transact(184, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getSharedLibraries(packageName, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getDeclaredSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 185;
                    if (!this.mRemote.transact(185, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getDeclaredSharedLibraries(packageName, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canRequestPackageInstalls(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(186, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canRequestPackageInstalls(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deletePreloadsFileCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(187, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deletePreloadsFileCache();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getInstantAppResolverComponent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 188;
                    if (!this.mRemote.transact(188, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getInstantAppResolverComponent();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getInstantAppResolverSettingsComponent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 189;
                    if (!this.mRemote.transact(189, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getInstantAppResolverSettingsComponent();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getInstantAppInstallerComponent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 190;
                    if (!this.mRemote.transact(190, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getInstantAppInstallerComponent();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getInstantAppAndroidId(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    String str = 191;
                    if (!this.mRemote.transact(191, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getInstantAppAndroidId(packageName, userId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IArtManager getArtManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IArtManager iArtManager = 192;
                    if (!this.mRemote.transact(192, _data, _reply, 0)) {
                        iArtManager = Stub.getDefaultImpl();
                        if (iArtManager != 0) {
                            iArtManager = Stub.getDefaultImpl().getArtManager();
                            return iArtManager;
                        }
                    }
                    _reply.readException();
                    iArtManager = android.content.pm.dex.IArtManager.Stub.asInterface(_reply.readStrongBinder());
                    IArtManager _result = iArtManager;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHarmfulAppWarning(String packageName, CharSequence warning, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (warning != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(warning, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(193, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setHarmfulAppWarning(packageName, warning, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence getHarmfulAppWarning(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    CharSequence charSequence = 194;
                    if (!this.mRemote.transact(194, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().getHarmfulAppWarning(packageName, userId);
                            return charSequence;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        charSequence = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(_reply);
                    } else {
                        charSequence = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return charSequence;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasSigningCertificate(String packageName, byte[] signingCertificate, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeByteArray(signingCertificate);
                    _data.writeInt(flags);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(195, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasSigningCertificate(packageName, signingCertificate, flags);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasUidSigningCertificate(int uid, byte[] signingCertificate, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(signingCertificate);
                    _data.writeInt(flags);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(196, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasUidSigningCertificate(uid, signingCertificate, flags);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSystemTextClassifierPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 197;
                    if (!this.mRemote.transact(197, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSystemTextClassifierPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAttentionServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 198;
                    if (!this.mRemote.transact(198, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAttentionServicePackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getWellbeingPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 199;
                    if (!this.mRemote.transact(199, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getWellbeingPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAppPredictionServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 200;
                    if (!this.mRemote.transact(200, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAppPredictionServicePackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSystemCaptionsServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 201;
                    if (!this.mRemote.transact(201, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSystemCaptionsServicePackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIncidentReportApproverPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 202;
                    if (!this.mRemote.transact(202, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIncidentReportApproverPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageStateProtected(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(203, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPackageStateProtected(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDeviceCustomizationReadyBroadcast() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(204, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendDeviceCustomizationReadyBroadcast();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ModuleInfo> getInstalledModules(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    List<ModuleInfo> list = 205;
                    if (!this.mRemote.transact(205, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getInstalledModules(flags);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ModuleInfo.CREATOR);
                    List<ModuleInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ModuleInfo getModuleInfo(String packageName, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    ModuleInfo moduleInfo = 206;
                    if (!this.mRemote.transact(206, _data, _reply, 0)) {
                        moduleInfo = Stub.getDefaultImpl();
                        if (moduleInfo != 0) {
                            moduleInfo = Stub.getDefaultImpl().getModuleInfo(packageName, flags);
                            return moduleInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        moduleInfo = (ModuleInfo) ModuleInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        moduleInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return moduleInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeRuntimePermissionNotKill(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(207, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeRuntimePermissionNotKill(packageName, permissionName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRuntimePermissionsVersion(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 208;
                    if (!this.mRemote.transact(208, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRuntimePermissionsVersion(userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRuntimePermissionsVersion(int version, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(version);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(209, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRuntimePermissionsVersion(version, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPackagesReplacedReceived(String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packages);
                    if (this.mRemote.transact(210, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPackagesReplacedReceived(packages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPackageManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPackageManager)) {
                return new Proxy(obj);
            }
            return (IPackageManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "checkPackageStartable";
                case 2:
                    return "isPackageAvailable";
                case 3:
                    return "getPackageInfo";
                case 4:
                    return "getPackageInfoVersioned";
                case 5:
                    return "getPackageUid";
                case 6:
                    return "getPackageGids";
                case 7:
                    return "currentToCanonicalPackageNames";
                case 8:
                    return "canonicalToCurrentPackageNames";
                case 9:
                    return "getPermissionInfo";
                case 10:
                    return "queryPermissionsByGroup";
                case 11:
                    return "getPermissionGroupInfo";
                case 12:
                    return "getAllPermissionGroups";
                case 13:
                    return "getApplicationInfo";
                case 14:
                    return "getActivityInfo";
                case 15:
                    return "activitySupportsIntent";
                case 16:
                    return "getReceiverInfo";
                case 17:
                    return "getServiceInfo";
                case 18:
                    return "getProviderInfo";
                case 19:
                    return "checkPermission";
                case 20:
                    return "checkUidPermission";
                case 21:
                    return "addPermission";
                case 22:
                    return "removePermission";
                case 23:
                    return "grantRuntimePermission";
                case 24:
                    return "revokeRuntimePermission";
                case 25:
                    return "resetRuntimePermissions";
                case 26:
                    return "getPermissionFlags";
                case 27:
                    return "updatePermissionFlags";
                case 28:
                    return "updatePermissionFlagsForAllApps";
                case 29:
                    return "getWhitelistedRestrictedPermissions";
                case 30:
                    return "addWhitelistedRestrictedPermission";
                case 31:
                    return "removeWhitelistedRestrictedPermission";
                case 32:
                    return "shouldShowRequestPermissionRationale";
                case 33:
                    return "isProtectedBroadcast";
                case 34:
                    return "checkSignatures";
                case 35:
                    return "checkUidSignatures";
                case 36:
                    return "getAllPackages";
                case 37:
                    return "getPackagesForUid";
                case 38:
                    return "getNameForUid";
                case 39:
                    return "getNamesForUids";
                case 40:
                    return "getUidForSharedUser";
                case 41:
                    return "getFlagsForUid";
                case 42:
                    return "getPrivateFlagsForUid";
                case 43:
                    return "isUidPrivileged";
                case 44:
                    return "getAppOpPermissionPackages";
                case 45:
                    return "resolveIntent";
                case 46:
                    return "findPersistentPreferredActivity";
                case 47:
                    return "canForwardTo";
                case 48:
                    return "queryIntentActivities";
                case 49:
                    return "queryIntentActivityOptions";
                case 50:
                    return "queryIntentReceivers";
                case 51:
                    return "resolveService";
                case 52:
                    return "queryIntentServices";
                case 53:
                    return "queryIntentContentProviders";
                case 54:
                    return "getInstalledPackages";
                case 55:
                    return "getPackagesHoldingPermissions";
                case 56:
                    return "getInstalledApplications";
                case 57:
                    return "getPersistentApplications";
                case 58:
                    return "resolveContentProvider";
                case 59:
                    return "querySyncProviders";
                case 60:
                    return "queryContentProviders";
                case 61:
                    return "getInstrumentationInfo";
                case 62:
                    return "queryInstrumentation";
                case 63:
                    return "finishPackageInstall";
                case 64:
                    return "setInstallerPackageName";
                case 65:
                    return "setApplicationCategoryHint";
                case 66:
                    return "deletePackageAsUser";
                case 67:
                    return "deletePackageVersioned";
                case 68:
                    return "getInstallerPackageName";
                case 69:
                    return "resetApplicationPreferences";
                case 70:
                    return "getLastChosenActivity";
                case 71:
                    return "setLastChosenActivity";
                case 72:
                    return "addPreferredActivity";
                case 73:
                    return "replacePreferredActivity";
                case 74:
                    return "clearPackagePreferredActivities";
                case 75:
                    return "getPreferredActivities";
                case 76:
                    return "addPersistentPreferredActivity";
                case 77:
                    return "clearPackagePersistentPreferredActivities";
                case 78:
                    return "addCrossProfileIntentFilter";
                case 79:
                    return "clearCrossProfileIntentFilters";
                case 80:
                    return "setDistractingPackageRestrictionsAsUser";
                case 81:
                    return "setPackagesSuspendedAsUser";
                case 82:
                    return "getUnsuspendablePackagesForUser";
                case 83:
                    return "isPackageSuspendedForUser";
                case 84:
                    return "getSuspendedPackageAppExtras";
                case 85:
                    return "getPreferredActivityBackup";
                case 86:
                    return "restorePreferredActivities";
                case 87:
                    return "getDefaultAppsBackup";
                case 88:
                    return "restoreDefaultApps";
                case 89:
                    return "getIntentFilterVerificationBackup";
                case 90:
                    return "restoreIntentFilterVerification";
                case 91:
                    return "getHomeActivities";
                case 92:
                    return "setHomeActivity";
                case 93:
                    return "setComponentEnabledSetting";
                case 94:
                    return "getComponentEnabledSetting";
                case 95:
                    return "setApplicationEnabledSetting";
                case 96:
                    return "getApplicationEnabledSetting";
                case 97:
                    return "logAppProcessStartIfNeeded";
                case 98:
                    return "flushPackageRestrictionsAsUser";
                case 99:
                    return "setPackageStoppedState";
                case 100:
                    return "freeStorageAndNotify";
                case 101:
                    return "freeStorage";
                case 102:
                    return "deleteApplicationCacheFiles";
                case 103:
                    return "deleteApplicationCacheFilesAsUser";
                case 104:
                    return "clearApplicationUserData";
                case 105:
                    return "clearApplicationProfileData";
                case 106:
                    return "getPackageSizeInfo";
                case 107:
                    return "getSystemSharedLibraryNames";
                case 108:
                    return "getSystemAvailableFeatures";
                case 109:
                    return "hasSystemFeature";
                case 110:
                    return "enterSafeMode";
                case 111:
                    return "isSafeMode";
                case 112:
                    return "systemReady";
                case 113:
                    return "hasSystemUidErrors";
                case 114:
                    return "performFstrimIfNeeded";
                case 115:
                    return "updatePackagesIfNeeded";
                case 116:
                    return "notifyPackageUse";
                case 117:
                    return "notifyDexLoad";
                case 118:
                    return "registerDexModule";
                case 119:
                    return "performDexOptMode";
                case 120:
                    return "performDexOptSecondary";
                case 121:
                    return "compileLayouts";
                case 122:
                    return "dumpProfiles";
                case 123:
                    return "forceDexOpt";
                case 124:
                    return "runBackgroundDexoptJob";
                case 125:
                    return "reconcileSecondaryDexFiles";
                case 126:
                    return "getMoveStatus";
                case 127:
                    return "registerMoveCallback";
                case 128:
                    return "unregisterMoveCallback";
                case 129:
                    return "movePackage";
                case 130:
                    return "movePrimaryStorage";
                case 131:
                    return "addPermissionAsync";
                case 132:
                    return "setInstallLocation";
                case 133:
                    return "getInstallLocation";
                case 134:
                    return "installExistingPackageAsUser";
                case 135:
                    return "verifyPendingInstall";
                case 136:
                    return "extendVerificationTimeout";
                case 137:
                    return "verifyIntentFilter";
                case 138:
                    return "getIntentVerificationStatus";
                case 139:
                    return "updateIntentVerificationStatus";
                case 140:
                    return "getIntentFilterVerifications";
                case 141:
                    return "getAllIntentFilters";
                case 142:
                    return "setDefaultBrowserPackageName";
                case 143:
                    return "getDefaultBrowserPackageName";
                case 144:
                    return "getVerifierDeviceIdentity";
                case 145:
                    return "isFirstBoot";
                case 146:
                    return "isOnlyCoreApps";
                case 147:
                    return "isDeviceUpgrading";
                case 148:
                    return "setPermissionEnforced";
                case 149:
                    return "isPermissionEnforced";
                case 150:
                    return "isStorageLow";
                case 151:
                    return "setApplicationHiddenSettingAsUser";
                case 152:
                    return "getApplicationHiddenSettingAsUser";
                case 153:
                    return "setSystemAppHiddenUntilInstalled";
                case 154:
                    return "setSystemAppInstallState";
                case 155:
                    return "getPackageInstaller";
                case 156:
                    return "setBlockUninstallForUser";
                case 157:
                    return "getBlockUninstallForUser";
                case 158:
                    return "getKeySetByAlias";
                case 159:
                    return "getSigningKeySet";
                case 160:
                    return "isPackageSignedByKeySet";
                case 161:
                    return "isPackageSignedByKeySetExactly";
                case 162:
                    return "addOnPermissionsChangeListener";
                case 163:
                    return "removeOnPermissionsChangeListener";
                case 164:
                    return "grantDefaultPermissionsToEnabledCarrierApps";
                case 165:
                    return "grantDefaultPermissionsToEnabledImsServices";
                case 166:
                    return "grantDefaultPermissionsToEnabledTelephonyDataServices";
                case 167:
                    return "revokeDefaultPermissionsFromDisabledTelephonyDataServices";
                case 168:
                    return "grantDefaultPermissionsToActiveLuiApp";
                case 169:
                    return "revokeDefaultPermissionsFromLuiApps";
                case 170:
                    return "isPermissionRevokedByPolicy";
                case 171:
                    return "getPermissionControllerPackageName";
                case 172:
                    return "getInstantApps";
                case 173:
                    return "getInstantAppCookie";
                case 174:
                    return "setInstantAppCookie";
                case 175:
                    return "getInstantAppIcon";
                case 176:
                    return "isInstantApp";
                case 177:
                    return "setRequiredForSystemUser";
                case 178:
                    return "setUpdateAvailable";
                case 179:
                    return "getServicesSystemSharedLibraryPackageName";
                case 180:
                    return "getSharedSystemSharedLibraryPackageName";
                case 181:
                    return "getChangedPackages";
                case 182:
                    return "isPackageDeviceAdminOnAnyUser";
                case 183:
                    return "getInstallReason";
                case 184:
                    return "getSharedLibraries";
                case 185:
                    return "getDeclaredSharedLibraries";
                case 186:
                    return "canRequestPackageInstalls";
                case 187:
                    return "deletePreloadsFileCache";
                case 188:
                    return "getInstantAppResolverComponent";
                case 189:
                    return "getInstantAppResolverSettingsComponent";
                case 190:
                    return "getInstantAppInstallerComponent";
                case 191:
                    return "getInstantAppAndroidId";
                case 192:
                    return "getArtManager";
                case 193:
                    return "setHarmfulAppWarning";
                case 194:
                    return "getHarmfulAppWarning";
                case 195:
                    return "hasSigningCertificate";
                case 196:
                    return "hasUidSigningCertificate";
                case 197:
                    return "getSystemTextClassifierPackageName";
                case 198:
                    return "getAttentionServicePackageName";
                case 199:
                    return "getWellbeingPackageName";
                case 200:
                    return "getAppPredictionServicePackageName";
                case 201:
                    return "getSystemCaptionsServicePackageName";
                case 202:
                    return "getIncidentReportApproverPackageName";
                case 203:
                    return "isPackageStateProtected";
                case 204:
                    return "sendDeviceCustomizationReadyBroadcast";
                case 205:
                    return "getInstalledModules";
                case 206:
                    return "getModuleInfo";
                case 207:
                    return "revokeRuntimePermissionNotKill";
                case 208:
                    return "getRuntimePermissionsVersion";
                case 209:
                    return "setRuntimePermissionsVersion";
                case 210:
                    return "notifyPackagesReplacedReceived";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                IBinder iBinder = null;
                boolean z = false;
                boolean _result;
                PackageInfo _result2;
                VersionedPackage _arg0;
                int _result3;
                String[] _result4;
                ParceledListSlice _result5;
                ParceledListSlice _result6;
                ComponentName _arg02;
                ActivityInfo _result7;
                boolean _result8;
                ProviderInfo _result9;
                int _result10;
                PermissionInfo _arg03;
                boolean _result11;
                boolean _result12;
                List<String> _result13;
                String _result14;
                int _result15;
                Intent _arg04;
                ResolveInfo _result16;
                ParceledListSlice _result17;
                ComponentName _arg05;
                ParceledListSlice _result18;
                ParceledListSlice _result19;
                int _arg06;
                IntentFilter _arg07;
                int _arg1;
                ComponentName[] _arg2;
                ComponentName _arg12;
                String[] _result20;
                byte[] _result21;
                String _arg08;
                boolean _result22;
                KeySet _result23;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        checkPackageStartable(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = isPackageAvailable(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result2 = getPackageInfo(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (VersionedPackage) VersionedPackage.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getPackageInfoVersioned(_arg0, data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result3 = getPackageUid(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        int[] _result24 = getPackageGids(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result24);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result4 = currentToCanonicalPackageNames(data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result4 = canonicalToCurrentPackageNames(data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        PermissionInfo _result25 = getPermissionInfo(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result25 != null) {
                            parcel2.writeInt(1);
                            _result25.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result5 = queryPermissionsByGroup(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        PermissionGroupInfo _result26 = getPermissionGroupInfo(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result26 != null) {
                            parcel2.writeInt(1);
                            _result26.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result6 = getAllPermissionGroups(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        ApplicationInfo _result27 = getApplicationInfo(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result27 != null) {
                            parcel2.writeInt(1);
                            _result27.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result7 = getActivityInfo(_arg02, data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 15:
                        Intent _arg13;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _result8 = activitySupportsIntent(_arg02, _arg13, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result7 = getReceiverInfo(_arg02, data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        ServiceInfo _result28 = getServiceInfo(_arg02, data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result28 != null) {
                            parcel2.writeInt(1);
                            _result28.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result9 = getProviderInfo(_arg02, data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result3 = checkPermission(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result10 = checkUidPermission(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PermissionInfo) PermissionInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result11 = addPermission(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        removePermission(data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        grantRuntimePermission(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        revokeRuntimePermission(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        resetRuntimePermissions();
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _result3 = getPermissionFlags(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        updatePermissionFlags(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        updatePermissionFlagsForAllApps(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        List<String> _result29 = getWhitelistedRestrictedPermissions(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result29);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result12 = addWhitelistedRestrictedPermission(data.readString(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result12);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result12 = removeWhitelistedRestrictedPermission(data.readString(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result12);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result8 = shouldShowRequestPermissionRationale(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result11 = isProtectedBroadcast(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _result10 = checkSignatures(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _result10 = checkUidSignatures(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result13 = getAllPackages();
                        reply.writeNoException();
                        parcel2.writeStringList(_result13);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _result4 = getPackagesForUid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _result14 = getNameForUid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result14);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        _result4 = getNamesForUids(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _result15 = getUidForSharedUser(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result15);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _result15 = getFlagsForUid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result15);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _result15 = getPrivateFlagsForUid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result15);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _result11 = isUidPrivileged(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _result4 = getAppOpPermissionPackages(data.readString());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result16 = resolveIntent(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result16 != null) {
                            parcel2.writeInt(1);
                            _result16.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        ResolveInfo _result30 = findPersistentPreferredActivity(_arg04, data.readInt());
                        reply.writeNoException();
                        if (_result30 != null) {
                            parcel2.writeInt(1);
                            _result30.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result12 = canForwardTo(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result12);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result17 = queryIntentActivities(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 49:
                        Intent _arg3;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        Intent[] _arg14 = (Intent[]) parcel.createTypedArray(Intent.CREATOR);
                        String[] _arg22 = data.createStringArray();
                        if (data.readInt() != 0) {
                            _arg3 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result18 = queryIntentActivityOptions(_arg05, _arg14, _arg22, _arg3, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result18 != null) {
                            parcel2.writeInt(1);
                            _result18.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result17 = queryIntentReceivers(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result16 = resolveService(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result16 != null) {
                            parcel2.writeInt(1);
                            _result16.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result17 = queryIntentServices(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result17 = queryIntentContentProviders(_arg04, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        _result5 = getInstalledPackages(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _result19 = getPackagesHoldingPermissions(data.createStringArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        _result5 = getInstalledApplications(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        _result6 = getPersistentApplications(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        _result9 = resolveContentProvider(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        _result13 = data.createStringArrayList();
                        List<ProviderInfo> _arg15 = parcel.createTypedArrayList(ProviderInfo.CREATOR);
                        querySyncProviders(_result13, _arg15);
                        reply.writeNoException();
                        parcel2.writeStringList(_result13);
                        parcel2.writeTypedList(_arg15);
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        _result17 = queryContentProviders(data.readString(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        InstrumentationInfo _result31 = getInstrumentationInfo(_arg02, data.readInt());
                        reply.writeNoException();
                        if (_result31 != null) {
                            parcel2.writeInt(1);
                            _result31.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        _result5 = queryInstrumentation(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        _arg06 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        finishPackageInstall(_arg06, z);
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        setInstallerPackageName(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        setApplicationCategoryHint(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        deletePackageAsUser(data.readString(), data.readInt(), android.content.pm.IPackageDeleteObserver.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (VersionedPackage) VersionedPackage.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        deletePackageVersioned(_arg0, android.content.pm.IPackageDeleteObserver2.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        _result14 = getInstallerPackageName(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result14);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        resetApplicationPreferences(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        ResolveInfo _result32 = getLastChosenActivity(_arg04, data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result32 != null) {
                            parcel2.writeInt(1);
                            _result32.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 71:
                        Intent _arg09;
                        IntentFilter _arg32;
                        ComponentName _arg5;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        String _arg16 = data.readString();
                        int _arg23 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        setLastChosenActivity(_arg09, _arg16, _arg23, _arg32, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        _arg1 = data.readInt();
                        _arg2 = (ComponentName[]) parcel.createTypedArray(ComponentName.CREATOR);
                        if (data.readInt() != 0) {
                            _arg05 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        addPreferredActivity(_arg07, _arg1, _arg2, _arg05, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        _arg1 = data.readInt();
                        _arg2 = (ComponentName[]) parcel.createTypedArray(ComponentName.CREATOR);
                        if (data.readInt() != 0) {
                            _arg05 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        replacePreferredActivity(_arg07, _arg1, _arg2, _arg05, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        clearPackagePreferredActivities(data.readString());
                        reply.writeNoException();
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        List<IntentFilter> _arg010 = new ArrayList();
                        List<ComponentName> _arg17 = new ArrayList();
                        _result3 = getPreferredActivities(_arg010, _arg17, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        parcel2.writeTypedList(_arg010);
                        parcel2.writeTypedList(_arg17);
                        return true;
                    case 76:
                        IntentFilter _arg011;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg011 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg011 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        addPersistentPreferredActivity(_arg011, _arg12, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        clearPackagePersistentPreferredActivities(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        addCrossProfileIntentFilter(_arg07, data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        clearCrossProfileIntentFilters(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        String[] _result33 = setDistractingPackageRestrictionsAsUser(data.createStringArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result33);
                        return true;
                    case 81:
                        PersistableBundle _arg24;
                        PersistableBundle _arg33;
                        SuspendDialogInfo _arg42;
                        parcel.enforceInterface(descriptor);
                        String[] _arg012 = data.createStringArray();
                        _result = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg24 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg33 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg33 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg42 = (SuspendDialogInfo) SuspendDialogInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        _result20 = setPackagesSuspendedAsUser(_arg012, _result, _arg24, _arg33, _arg42, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result20);
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        String[] _result34 = getUnsuspendablePackagesForUser(data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result34);
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        _result = isPackageSuspendedForUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        PersistableBundle _result35 = getSuspendedPackageAppExtras(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result35 != null) {
                            parcel2.writeInt(1);
                            _result35.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        _result21 = getPreferredActivityBackup(data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result21);
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        restorePreferredActivities(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        _result21 = getDefaultAppsBackup(data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result21);
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        restoreDefaultApps(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        _result21 = getIntentFilterVerificationBackup(data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result21);
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        restoreIntentFilterVerification(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 91:
                        parcel.enforceInterface(descriptor);
                        List<ResolveInfo> _arg013 = new ArrayList();
                        _arg12 = getHomeActivities(_arg013);
                        reply.writeNoException();
                        if (_arg12 != null) {
                            parcel2.writeInt(1);
                            _arg12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        parcel2.writeTypedList(_arg013);
                        return true;
                    case 92:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        setHomeActivity(_arg02, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 93:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        setComponentEnabledSetting(_arg02, data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 94:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result10 = getComponentEnabledSetting(_arg02, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        setApplicationEnabledSetting(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 96:
                        parcel.enforceInterface(descriptor);
                        _result10 = getApplicationEnabledSetting(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 97:
                        parcel.enforceInterface(descriptor);
                        logAppProcessStartIfNeeded(data.readString(), data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 98:
                        parcel.enforceInterface(descriptor);
                        flushPackageRestrictionsAsUser(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 99:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPackageStoppedState(_arg08, z, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 100:
                        parcel.enforceInterface(descriptor);
                        freeStorageAndNotify(data.readString(), data.readLong(), data.readInt(), android.content.pm.IPackageDataObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 101:
                        IntentSender _arg34;
                        parcel.enforceInterface(descriptor);
                        String _arg014 = data.readString();
                        long _arg18 = data.readLong();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg34 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg34 = null;
                        }
                        freeStorage(_arg014, _arg18, _arg1, _arg34);
                        reply.writeNoException();
                        return true;
                    case 102:
                        parcel.enforceInterface(descriptor);
                        deleteApplicationCacheFiles(data.readString(), android.content.pm.IPackageDataObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 103:
                        parcel.enforceInterface(descriptor);
                        deleteApplicationCacheFilesAsUser(data.readString(), data.readInt(), android.content.pm.IPackageDataObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 104:
                        parcel.enforceInterface(descriptor);
                        clearApplicationUserData(data.readString(), android.content.pm.IPackageDataObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 105:
                        parcel.enforceInterface(descriptor);
                        clearApplicationProfileData(data.readString());
                        reply.writeNoException();
                        return true;
                    case 106:
                        parcel.enforceInterface(descriptor);
                        getPackageSizeInfo(data.readString(), data.readInt(), android.content.pm.IPackageStatsObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 107:
                        parcel.enforceInterface(descriptor);
                        _result20 = getSystemSharedLibraryNames();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result20);
                        return true;
                    case 108:
                        parcel.enforceInterface(descriptor);
                        _result18 = getSystemAvailableFeatures();
                        reply.writeNoException();
                        if (_result18 != null) {
                            parcel2.writeInt(1);
                            _result18.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 109:
                        parcel.enforceInterface(descriptor);
                        _result = hasSystemFeature(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 110:
                        parcel.enforceInterface(descriptor);
                        enterSafeMode();
                        reply.writeNoException();
                        return true;
                    case 111:
                        parcel.enforceInterface(descriptor);
                        _result22 = isSafeMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 112:
                        parcel.enforceInterface(descriptor);
                        systemReady();
                        reply.writeNoException();
                        return true;
                    case 113:
                        parcel.enforceInterface(descriptor);
                        _result22 = hasSystemUidErrors();
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 114:
                        parcel.enforceInterface(descriptor);
                        performFstrimIfNeeded();
                        reply.writeNoException();
                        return true;
                    case 115:
                        parcel.enforceInterface(descriptor);
                        updatePackagesIfNeeded();
                        reply.writeNoException();
                        return true;
                    case 116:
                        parcel.enforceInterface(descriptor);
                        notifyPackageUse(data.readString(), data.readInt());
                        return true;
                    case 117:
                        parcel.enforceInterface(descriptor);
                        notifyDexLoad(data.readString(), data.createStringArrayList(), data.createStringArrayList(), data.readString());
                        return true;
                    case 118:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        _result14 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        registerDexModule(_arg08, _result14, z, android.content.pm.IDexModuleRegisterCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 119:
                        parcel.enforceInterface(descriptor);
                        _result22 = performDexOptMode(data.readString(), data.readInt() != 0, data.readString(), data.readInt() != 0, data.readInt() != 0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 120:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        _result14 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result8 = performDexOptSecondary(_arg08, _result14, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 121:
                        parcel.enforceInterface(descriptor);
                        _result11 = compileLayouts(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 122:
                        parcel.enforceInterface(descriptor);
                        dumpProfiles(data.readString());
                        reply.writeNoException();
                        return true;
                    case 123:
                        parcel.enforceInterface(descriptor);
                        forceDexOpt(data.readString());
                        reply.writeNoException();
                        return true;
                    case 124:
                        parcel.enforceInterface(descriptor);
                        _result11 = runBackgroundDexoptJob(data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 125:
                        parcel.enforceInterface(descriptor);
                        reconcileSecondaryDexFiles(data.readString());
                        reply.writeNoException();
                        return true;
                    case 126:
                        parcel.enforceInterface(descriptor);
                        _result15 = getMoveStatus(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result15);
                        return true;
                    case 127:
                        parcel.enforceInterface(descriptor);
                        registerMoveCallback(android.content.pm.IPackageMoveObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 128:
                        parcel.enforceInterface(descriptor);
                        unregisterMoveCallback(android.content.pm.IPackageMoveObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 129:
                        parcel.enforceInterface(descriptor);
                        _result10 = movePackage(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 130:
                        parcel.enforceInterface(descriptor);
                        _result15 = movePrimaryStorage(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result15);
                        return true;
                    case 131:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PermissionInfo) PermissionInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result11 = addPermissionAsync(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 132:
                        parcel.enforceInterface(descriptor);
                        _result11 = setInstallLocation(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 133:
                        parcel.enforceInterface(descriptor);
                        _arg06 = getInstallLocation();
                        reply.writeNoException();
                        parcel2.writeInt(_arg06);
                        return true;
                    case 134:
                        parcel.enforceInterface(descriptor);
                        _arg06 = installExistingPackageAsUser(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeInt(_arg06);
                        return true;
                    case 135:
                        parcel.enforceInterface(descriptor);
                        verifyPendingInstall(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 136:
                        parcel.enforceInterface(descriptor);
                        extendVerificationTimeout(data.readInt(), data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 137:
                        parcel.enforceInterface(descriptor);
                        verifyIntentFilter(data.readInt(), data.readInt(), data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 138:
                        parcel.enforceInterface(descriptor);
                        _result10 = getIntentVerificationStatus(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 139:
                        parcel.enforceInterface(descriptor);
                        _result8 = updateIntentVerificationStatus(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 140:
                        parcel.enforceInterface(descriptor);
                        _result6 = getIntentFilterVerifications(data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 141:
                        parcel.enforceInterface(descriptor);
                        _result6 = getAllIntentFilters(data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 142:
                        parcel.enforceInterface(descriptor);
                        _result = setDefaultBrowserPackageName(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 143:
                        parcel.enforceInterface(descriptor);
                        _result14 = getDefaultBrowserPackageName(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result14);
                        return true;
                    case 144:
                        parcel.enforceInterface(descriptor);
                        VerifierDeviceIdentity _result36 = getVerifierDeviceIdentity();
                        reply.writeNoException();
                        if (_result36 != null) {
                            parcel2.writeInt(1);
                            _result36.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 145:
                        parcel.enforceInterface(descriptor);
                        _result22 = isFirstBoot();
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 146:
                        parcel.enforceInterface(descriptor);
                        _result22 = isOnlyCoreApps();
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 147:
                        parcel.enforceInterface(descriptor);
                        _result22 = isDeviceUpgrading();
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 148:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPermissionEnforced(_arg08, z);
                        reply.writeNoException();
                        return true;
                    case 149:
                        parcel.enforceInterface(descriptor);
                        _result11 = isPermissionEnforced(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 150:
                        parcel.enforceInterface(descriptor);
                        _result22 = isStorageLow();
                        reply.writeNoException();
                        parcel2.writeInt(_result22);
                        return true;
                    case 151:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result8 = setApplicationHiddenSettingAsUser(_arg08, z, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 152:
                        parcel.enforceInterface(descriptor);
                        _result = getApplicationHiddenSettingAsUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 153:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setSystemAppHiddenUntilInstalled(_arg08, z);
                        reply.writeNoException();
                        return true;
                    case 154:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result8 = setSystemAppInstallState(_arg08, z, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 155:
                        parcel.enforceInterface(descriptor);
                        IPackageInstaller _result37 = getPackageInstaller();
                        reply.writeNoException();
                        if (_result37 != null) {
                            iBinder = _result37.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 156:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result8 = setBlockUninstallForUser(_arg08, z, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 157:
                        parcel.enforceInterface(descriptor);
                        _result = getBlockUninstallForUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 158:
                        parcel.enforceInterface(descriptor);
                        KeySet _result38 = getKeySetByAlias(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result38 != null) {
                            parcel2.writeInt(1);
                            _result38.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 159:
                        parcel.enforceInterface(descriptor);
                        _result23 = getSigningKeySet(data.readString());
                        reply.writeNoException();
                        if (_result23 != null) {
                            parcel2.writeInt(1);
                            _result23.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 160:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            _result23 = (KeySet) KeySet.CREATOR.createFromParcel(parcel);
                        } else {
                            _result23 = null;
                        }
                        _result = isPackageSignedByKeySet(_arg08, _result23);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 161:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            _result23 = (KeySet) KeySet.CREATOR.createFromParcel(parcel);
                        } else {
                            _result23 = null;
                        }
                        _result = isPackageSignedByKeySetExactly(_arg08, _result23);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 162:
                        parcel.enforceInterface(descriptor);
                        addOnPermissionsChangeListener(android.content.pm.IOnPermissionsChangeListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 163:
                        parcel.enforceInterface(descriptor);
                        removeOnPermissionsChangeListener(android.content.pm.IOnPermissionsChangeListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 164:
                        parcel.enforceInterface(descriptor);
                        grantDefaultPermissionsToEnabledCarrierApps(data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 165:
                        parcel.enforceInterface(descriptor);
                        grantDefaultPermissionsToEnabledImsServices(data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 166:
                        parcel.enforceInterface(descriptor);
                        grantDefaultPermissionsToEnabledTelephonyDataServices(data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 167:
                        parcel.enforceInterface(descriptor);
                        revokeDefaultPermissionsFromDisabledTelephonyDataServices(data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 168:
                        parcel.enforceInterface(descriptor);
                        grantDefaultPermissionsToActiveLuiApp(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 169:
                        parcel.enforceInterface(descriptor);
                        revokeDefaultPermissionsFromLuiApps(data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 170:
                        parcel.enforceInterface(descriptor);
                        _result8 = isPermissionRevokedByPolicy(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 171:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getPermissionControllerPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 172:
                        parcel.enforceInterface(descriptor);
                        _result6 = getInstantApps(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 173:
                        parcel.enforceInterface(descriptor);
                        byte[] _result39 = getInstantAppCookie(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result39);
                        return true;
                    case 174:
                        parcel.enforceInterface(descriptor);
                        _result8 = setInstantAppCookie(data.readString(), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 175:
                        parcel.enforceInterface(descriptor);
                        Bitmap _result40 = getInstantAppIcon(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result40 != null) {
                            parcel2.writeInt(1);
                            _result40.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 176:
                        parcel.enforceInterface(descriptor);
                        _result = isInstantApp(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 177:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = setRequiredForSystemUser(_arg08, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 178:
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setUpdateAvailable(_arg08, z);
                        reply.writeNoException();
                        return true;
                    case 179:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getServicesSystemSharedLibraryPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 180:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getSharedSystemSharedLibraryPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 181:
                        parcel.enforceInterface(descriptor);
                        ChangedPackages _result41 = getChangedPackages(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result41 != null) {
                            parcel2.writeInt(1);
                            _result41.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 182:
                        parcel.enforceInterface(descriptor);
                        _result11 = isPackageDeviceAdminOnAnyUser(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 183:
                        parcel.enforceInterface(descriptor);
                        _result10 = getInstallReason(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 184:
                        parcel.enforceInterface(descriptor);
                        _result19 = getSharedLibraries(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 185:
                        parcel.enforceInterface(descriptor);
                        _result19 = getDeclaredSharedLibraries(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 186:
                        parcel.enforceInterface(descriptor);
                        _result = canRequestPackageInstalls(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 187:
                        parcel.enforceInterface(descriptor);
                        deletePreloadsFileCache();
                        reply.writeNoException();
                        return true;
                    case 188:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getInstantAppResolverComponent();
                        reply.writeNoException();
                        if (_arg02 != null) {
                            parcel2.writeInt(1);
                            _arg02.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 189:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getInstantAppResolverSettingsComponent();
                        reply.writeNoException();
                        if (_arg02 != null) {
                            parcel2.writeInt(1);
                            _arg02.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 190:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getInstantAppInstallerComponent();
                        reply.writeNoException();
                        if (_arg02 != null) {
                            parcel2.writeInt(1);
                            _arg02.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 191:
                        parcel.enforceInterface(descriptor);
                        String _result42 = getInstantAppAndroidId(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result42);
                        return true;
                    case 192:
                        parcel.enforceInterface(descriptor);
                        IArtManager _result43 = getArtManager();
                        reply.writeNoException();
                        if (_result43 != null) {
                            iBinder = _result43.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 193:
                        CharSequence _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg08 = data.readString();
                        if (data.readInt() != 0) {
                            _arg19 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        setHarmfulAppWarning(_arg08, _arg19, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 194:
                        parcel.enforceInterface(descriptor);
                        CharSequence _result44 = getHarmfulAppWarning(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result44 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result44, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 195:
                        parcel.enforceInterface(descriptor);
                        _result8 = hasSigningCertificate(data.readString(), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 196:
                        parcel.enforceInterface(descriptor);
                        _result8 = hasUidSigningCertificate(data.readInt(), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 197:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getSystemTextClassifierPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 198:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getAttentionServicePackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 199:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getWellbeingPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 200:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getAppPredictionServicePackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 201:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getSystemCaptionsServicePackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 202:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getIncidentReportApproverPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg08);
                        return true;
                    case 203:
                        parcel.enforceInterface(descriptor);
                        _result = isPackageStateProtected(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 204:
                        parcel.enforceInterface(descriptor);
                        sendDeviceCustomizationReadyBroadcast();
                        reply.writeNoException();
                        return true;
                    case 205:
                        parcel.enforceInterface(descriptor);
                        List<ModuleInfo> _result45 = getInstalledModules(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result45);
                        return true;
                    case 206:
                        parcel.enforceInterface(descriptor);
                        ModuleInfo _result46 = getModuleInfo(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result46 != null) {
                            parcel2.writeInt(1);
                            _result46.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 207:
                        parcel.enforceInterface(descriptor);
                        revokeRuntimePermissionNotKill(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 208:
                        parcel.enforceInterface(descriptor);
                        _result15 = getRuntimePermissionsVersion(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result15);
                        return true;
                    case 209:
                        parcel.enforceInterface(descriptor);
                        setRuntimePermissionsVersion(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 210:
                        parcel.enforceInterface(descriptor);
                        notifyPackagesReplacedReceived(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPackageManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPackageManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean activitySupportsIntent(ComponentName componentName, Intent intent, String str) throws RemoteException;

    void addCrossProfileIntentFilter(IntentFilter intentFilter, String str, int i, int i2, int i3) throws RemoteException;

    void addOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) throws RemoteException;

    @UnsupportedAppUsage
    boolean addPermission(PermissionInfo permissionInfo) throws RemoteException;

    @UnsupportedAppUsage
    boolean addPermissionAsync(PermissionInfo permissionInfo) throws RemoteException;

    void addPersistentPreferredActivity(IntentFilter intentFilter, ComponentName componentName, int i) throws RemoteException;

    void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) throws RemoteException;

    boolean addWhitelistedRestrictedPermission(String str, String str2, int i, int i2) throws RemoteException;

    boolean canForwardTo(Intent intent, String str, int i, int i2) throws RemoteException;

    boolean canRequestPackageInstalls(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    String[] canonicalToCurrentPackageNames(String[] strArr) throws RemoteException;

    void checkPackageStartable(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkPermission(String str, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkSignatures(String str, String str2) throws RemoteException;

    int checkUidPermission(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkUidSignatures(int i, int i2) throws RemoteException;

    void clearApplicationProfileData(String str) throws RemoteException;

    void clearApplicationUserData(String str, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    void clearCrossProfileIntentFilters(int i, String str) throws RemoteException;

    void clearPackagePersistentPreferredActivities(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void clearPackagePreferredActivities(String str) throws RemoteException;

    boolean compileLayouts(String str) throws RemoteException;

    @UnsupportedAppUsage
    String[] currentToCanonicalPackageNames(String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    void deleteApplicationCacheFiles(String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void deleteApplicationCacheFilesAsUser(String str, int i, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void deletePackageAsUser(String str, int i, IPackageDeleteObserver iPackageDeleteObserver, int i2, int i3) throws RemoteException;

    void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 iPackageDeleteObserver2, int i, int i2) throws RemoteException;

    void deletePreloadsFileCache() throws RemoteException;

    void dumpProfiles(String str) throws RemoteException;

    void enterSafeMode() throws RemoteException;

    void extendVerificationTimeout(int i, int i2, long j) throws RemoteException;

    ResolveInfo findPersistentPreferredActivity(Intent intent, int i) throws RemoteException;

    void finishPackageInstall(int i, boolean z) throws RemoteException;

    void flushPackageRestrictionsAsUser(int i) throws RemoteException;

    void forceDexOpt(String str) throws RemoteException;

    void freeStorage(String str, long j, int i, IntentSender intentSender) throws RemoteException;

    void freeStorageAndNotify(String str, long j, int i, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    @UnsupportedAppUsage
    ActivityInfo getActivityInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    ParceledListSlice getAllIntentFilters(String str) throws RemoteException;

    List<String> getAllPackages() throws RemoteException;

    ParceledListSlice getAllPermissionGroups(int i) throws RemoteException;

    @UnsupportedAppUsage
    String[] getAppOpPermissionPackages(String str) throws RemoteException;

    String getAppPredictionServicePackageName() throws RemoteException;

    @UnsupportedAppUsage
    int getApplicationEnabledSetting(String str, int i) throws RemoteException;

    boolean getApplicationHiddenSettingAsUser(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ApplicationInfo getApplicationInfo(String str, int i, int i2) throws RemoteException;

    IArtManager getArtManager() throws RemoteException;

    String getAttentionServicePackageName() throws RemoteException;

    @UnsupportedAppUsage
    boolean getBlockUninstallForUser(String str, int i) throws RemoteException;

    ChangedPackages getChangedPackages(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    int getComponentEnabledSetting(ComponentName componentName, int i) throws RemoteException;

    ParceledListSlice getDeclaredSharedLibraries(String str, int i, int i2) throws RemoteException;

    byte[] getDefaultAppsBackup(int i) throws RemoteException;

    String getDefaultBrowserPackageName(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getFlagsForUid(int i) throws RemoteException;

    CharSequence getHarmfulAppWarning(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ComponentName getHomeActivities(List<ResolveInfo> list) throws RemoteException;

    String getIncidentReportApproverPackageName() throws RemoteException;

    @UnsupportedAppUsage
    int getInstallLocation() throws RemoteException;

    int getInstallReason(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice getInstalledApplications(int i, int i2) throws RemoteException;

    List<ModuleInfo> getInstalledModules(int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice getInstalledPackages(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getInstallerPackageName(String str) throws RemoteException;

    String getInstantAppAndroidId(String str, int i) throws RemoteException;

    byte[] getInstantAppCookie(String str, int i) throws RemoteException;

    Bitmap getInstantAppIcon(String str, int i) throws RemoteException;

    ComponentName getInstantAppInstallerComponent() throws RemoteException;

    ComponentName getInstantAppResolverComponent() throws RemoteException;

    ComponentName getInstantAppResolverSettingsComponent() throws RemoteException;

    ParceledListSlice getInstantApps(int i) throws RemoteException;

    @UnsupportedAppUsage
    InstrumentationInfo getInstrumentationInfo(ComponentName componentName, int i) throws RemoteException;

    byte[] getIntentFilterVerificationBackup(int i) throws RemoteException;

    ParceledListSlice getIntentFilterVerifications(String str) throws RemoteException;

    int getIntentVerificationStatus(String str, int i) throws RemoteException;

    KeySet getKeySetByAlias(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    ResolveInfo getLastChosenActivity(Intent intent, String str, int i) throws RemoteException;

    ModuleInfo getModuleInfo(String str, int i) throws RemoteException;

    int getMoveStatus(int i) throws RemoteException;

    @UnsupportedAppUsage
    String getNameForUid(int i) throws RemoteException;

    String[] getNamesForUids(int[] iArr) throws RemoteException;

    int[] getPackageGids(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    PackageInfo getPackageInfo(String str, int i, int i2) throws RemoteException;

    PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    IPackageInstaller getPackageInstaller() throws RemoteException;

    void getPackageSizeInfo(String str, int i, IPackageStatsObserver iPackageStatsObserver) throws RemoteException;

    @UnsupportedAppUsage
    int getPackageUid(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String[] getPackagesForUid(int i) throws RemoteException;

    ParceledListSlice getPackagesHoldingPermissions(String[] strArr, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getPermissionControllerPackageName() throws RemoteException;

    int getPermissionFlags(String str, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    PermissionGroupInfo getPermissionGroupInfo(String str, int i) throws RemoteException;

    PermissionInfo getPermissionInfo(String str, String str2, int i) throws RemoteException;

    ParceledListSlice getPersistentApplications(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list2, String str) throws RemoteException;

    byte[] getPreferredActivityBackup(int i) throws RemoteException;

    int getPrivateFlagsForUid(int i) throws RemoteException;

    @UnsupportedAppUsage
    ProviderInfo getProviderInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    ActivityInfo getReceiverInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    int getRuntimePermissionsVersion(int i) throws RemoteException;

    @UnsupportedAppUsage
    ServiceInfo getServiceInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getServicesSystemSharedLibraryPackageName() throws RemoteException;

    ParceledListSlice getSharedLibraries(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getSharedSystemSharedLibraryPackageName() throws RemoteException;

    KeySet getSigningKeySet(String str) throws RemoteException;

    PersistableBundle getSuspendedPackageAppExtras(String str, int i) throws RemoteException;

    ParceledListSlice getSystemAvailableFeatures() throws RemoteException;

    String getSystemCaptionsServicePackageName() throws RemoteException;

    @UnsupportedAppUsage
    String[] getSystemSharedLibraryNames() throws RemoteException;

    String getSystemTextClassifierPackageName() throws RemoteException;

    @UnsupportedAppUsage
    int getUidForSharedUser(String str) throws RemoteException;

    String[] getUnsuspendablePackagesForUser(String[] strArr, int i) throws RemoteException;

    VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException;

    String getWellbeingPackageName() throws RemoteException;

    List<String> getWhitelistedRestrictedPermissions(String str, int i, int i2) throws RemoteException;

    void grantDefaultPermissionsToActiveLuiApp(String str, int i) throws RemoteException;

    void grantDefaultPermissionsToEnabledCarrierApps(String[] strArr, int i) throws RemoteException;

    void grantDefaultPermissionsToEnabledImsServices(String[] strArr, int i) throws RemoteException;

    void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] strArr, int i) throws RemoteException;

    @UnsupportedAppUsage
    void grantRuntimePermission(String str, String str2, int i) throws RemoteException;

    boolean hasSigningCertificate(String str, byte[] bArr, int i) throws RemoteException;

    boolean hasSystemFeature(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean hasSystemUidErrors() throws RemoteException;

    boolean hasUidSigningCertificate(int i, byte[] bArr, int i2) throws RemoteException;

    int installExistingPackageAsUser(String str, int i, int i2, int i3, List<String> list) throws RemoteException;

    boolean isDeviceUpgrading() throws RemoteException;

    boolean isFirstBoot() throws RemoteException;

    boolean isInstantApp(String str, int i) throws RemoteException;

    boolean isOnlyCoreApps() throws RemoteException;

    @UnsupportedAppUsage
    boolean isPackageAvailable(String str, int i) throws RemoteException;

    boolean isPackageDeviceAdminOnAnyUser(String str) throws RemoteException;

    boolean isPackageSignedByKeySet(String str, KeySet keySet) throws RemoteException;

    boolean isPackageSignedByKeySetExactly(String str, KeySet keySet) throws RemoteException;

    boolean isPackageStateProtected(String str, int i) throws RemoteException;

    boolean isPackageSuspendedForUser(String str, int i) throws RemoteException;

    boolean isPermissionEnforced(String str) throws RemoteException;

    boolean isPermissionRevokedByPolicy(String str, String str2, int i) throws RemoteException;

    boolean isProtectedBroadcast(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isSafeMode() throws RemoteException;

    @UnsupportedAppUsage
    boolean isStorageLow() throws RemoteException;

    @UnsupportedAppUsage
    boolean isUidPrivileged(int i) throws RemoteException;

    void logAppProcessStartIfNeeded(String str, int i, String str2, String str3, int i2) throws RemoteException;

    int movePackage(String str, String str2) throws RemoteException;

    int movePrimaryStorage(String str) throws RemoteException;

    void notifyDexLoad(String str, List<String> list, List<String> list2, String str2) throws RemoteException;

    void notifyPackageUse(String str, int i) throws RemoteException;

    void notifyPackagesReplacedReceived(String[] strArr) throws RemoteException;

    boolean performDexOptMode(String str, boolean z, String str2, boolean z2, boolean z3, String str3) throws RemoteException;

    boolean performDexOptSecondary(String str, String str2, boolean z) throws RemoteException;

    void performFstrimIfNeeded() throws RemoteException;

    ParceledListSlice queryContentProviders(String str, int i, int i2, String str2) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice queryInstrumentation(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice queryIntentActivities(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentActivityOptions(ComponentName componentName, Intent[] intentArr, String[] strArr, Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentContentProviders(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentReceivers(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentServices(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryPermissionsByGroup(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void querySyncProviders(List<String> list, List<ProviderInfo> list2) throws RemoteException;

    void reconcileSecondaryDexFiles(String str) throws RemoteException;

    void registerDexModule(String str, String str2, boolean z, IDexModuleRegisterCallback iDexModuleRegisterCallback) throws RemoteException;

    void registerMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    void removeOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) throws RemoteException;

    @UnsupportedAppUsage
    void removePermission(String str) throws RemoteException;

    boolean removeWhitelistedRestrictedPermission(String str, String str2, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void replacePreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) throws RemoteException;

    void resetApplicationPreferences(int i) throws RemoteException;

    void resetRuntimePermissions() throws RemoteException;

    ProviderInfo resolveContentProvider(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    ResolveInfo resolveIntent(Intent intent, String str, int i, int i2) throws RemoteException;

    ResolveInfo resolveService(Intent intent, String str, int i, int i2) throws RemoteException;

    void restoreDefaultApps(byte[] bArr, int i) throws RemoteException;

    void restoreIntentFilterVerification(byte[] bArr, int i) throws RemoteException;

    void restorePreferredActivities(byte[] bArr, int i) throws RemoteException;

    void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] strArr, int i) throws RemoteException;

    void revokeDefaultPermissionsFromLuiApps(String[] strArr, int i) throws RemoteException;

    void revokeRuntimePermission(String str, String str2, int i) throws RemoteException;

    void revokeRuntimePermissionNotKill(String str, String str2, int i) throws RemoteException;

    boolean runBackgroundDexoptJob(List<String> list) throws RemoteException;

    void sendDeviceCustomizationReadyBroadcast() throws RemoteException;

    void setApplicationCategoryHint(String str, int i, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void setApplicationEnabledSetting(String str, int i, int i2, int i3, String str2) throws RemoteException;

    @UnsupportedAppUsage
    boolean setApplicationHiddenSettingAsUser(String str, boolean z, int i) throws RemoteException;

    boolean setBlockUninstallForUser(String str, boolean z, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setComponentEnabledSetting(ComponentName componentName, int i, int i2, int i3) throws RemoteException;

    boolean setDefaultBrowserPackageName(String str, int i) throws RemoteException;

    String[] setDistractingPackageRestrictionsAsUser(String[] strArr, int i, int i2) throws RemoteException;

    void setHarmfulAppWarning(String str, CharSequence charSequence, int i) throws RemoteException;

    void setHomeActivity(ComponentName componentName, int i) throws RemoteException;

    boolean setInstallLocation(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setInstallerPackageName(String str, String str2) throws RemoteException;

    boolean setInstantAppCookie(String str, byte[] bArr, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setLastChosenActivity(Intent intent, String str, int i, IntentFilter intentFilter, int i2, ComponentName componentName) throws RemoteException;

    @UnsupportedAppUsage
    void setPackageStoppedState(String str, boolean z, int i) throws RemoteException;

    String[] setPackagesSuspendedAsUser(String[] strArr, boolean z, PersistableBundle persistableBundle, PersistableBundle persistableBundle2, SuspendDialogInfo suspendDialogInfo, String str, int i) throws RemoteException;

    void setPermissionEnforced(String str, boolean z) throws RemoteException;

    boolean setRequiredForSystemUser(String str, boolean z) throws RemoteException;

    void setRuntimePermissionsVersion(int i, int i2) throws RemoteException;

    void setSystemAppHiddenUntilInstalled(String str, boolean z) throws RemoteException;

    boolean setSystemAppInstallState(String str, boolean z, int i) throws RemoteException;

    void setUpdateAvailable(String str, boolean z) throws RemoteException;

    boolean shouldShowRequestPermissionRationale(String str, String str2, int i) throws RemoteException;

    void systemReady() throws RemoteException;

    void unregisterMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    boolean updateIntentVerificationStatus(String str, int i, int i2) throws RemoteException;

    void updatePackagesIfNeeded() throws RemoteException;

    void updatePermissionFlags(String str, String str2, int i, int i2, boolean z, int i3) throws RemoteException;

    void updatePermissionFlagsForAllApps(int i, int i2, int i3) throws RemoteException;

    void verifyIntentFilter(int i, int i2, List<String> list) throws RemoteException;

    void verifyPendingInstall(int i, int i2) throws RemoteException;
}
