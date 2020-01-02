package android.app.admin;

import android.annotation.UnsupportedAppUsage;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.StringParceledListSlice;
import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keystore.ParcelableKeyGenParameterSpec;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public interface IDevicePolicyManager extends IInterface {

    public static class Default implements IDevicePolicyManager {
        public void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
        }

        public int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        public int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
        }

        public long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
            return false;
        }

        public boolean isProfileActivePasswordSufficientForParent(int userHandle) throws RemoteException {
            return false;
        }

        public int getPasswordComplexity() throws RemoteException {
            return 0;
        }

        public boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
            return false;
        }

        public int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
        }

        public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public boolean resetPassword(String password, int flags) throws RemoteException {
            return false;
        }

        public void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
        }

        public long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
        }

        public long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
            return 0;
        }

        public void lockNow(int flags, boolean parent) throws RemoteException {
        }

        public void wipeDataWithReason(int flags, String wipeReasonForUser) throws RemoteException {
        }

        public ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
            return null;
        }

        public ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
            return null;
        }

        public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
        }

        public int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
            return 0;
        }

        public boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        public int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
            return 0;
        }

        public boolean requestBugreport(ComponentName who) throws RemoteException {
            return false;
        }

        public void setCameraDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        public boolean getCameraDisabled(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        public void setScreenCaptureDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        public boolean getScreenCaptureDisabled(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        public void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
        }

        public int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
        }

        public boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
            return false;
        }

        public List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
            return null;
        }

        public boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
            return false;
        }

        public void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
        }

        public void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
        }

        public void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
        }

        public boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
            return false;
        }

        public void setActivePasswordState(PasswordMetrics metrics, int userHandle) throws RemoteException {
        }

        public void reportPasswordChanged(int userId) throws RemoteException {
        }

        public void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
        }

        public void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
        }

        public void reportFailedBiometricAttempt(int userHandle) throws RemoteException {
        }

        public void reportSuccessfulBiometricAttempt(int userHandle) throws RemoteException {
        }

        public void reportKeyguardDismissed(int userHandle) throws RemoteException {
        }

        public void reportKeyguardSecured(int userHandle) throws RemoteException {
        }

        public boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
            return false;
        }

        public ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
            return null;
        }

        public boolean hasDeviceOwner() throws RemoteException {
            return false;
        }

        public String getDeviceOwnerName() throws RemoteException {
            return null;
        }

        public void clearDeviceOwner(String packageName) throws RemoteException {
        }

        public int getDeviceOwnerUserId() throws RemoteException {
            return 0;
        }

        public boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
            return false;
        }

        public ComponentName getProfileOwnerAsUser(int userHandle) throws RemoteException {
            return null;
        }

        public ComponentName getProfileOwner(int userHandle) throws RemoteException {
            return null;
        }

        public String getProfileOwnerName(int userHandle) throws RemoteException {
            return null;
        }

        public void setProfileEnabled(ComponentName who) throws RemoteException {
        }

        public void setProfileName(ComponentName who, String profileName) throws RemoteException {
        }

        public void clearProfileOwner(ComponentName who) throws RemoteException {
        }

        public boolean hasUserSetupCompleted() throws RemoteException {
            return false;
        }

        public boolean checkDeviceIdentifierAccess(String packageName, int pid, int uid) throws RemoteException {
            return false;
        }

        public void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
        }

        public CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
            return null;
        }

        public String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
            return null;
        }

        public boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        public boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
            return false;
        }

        public void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
        }

        public void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
        }

        public boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
            return false;
        }

        public boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
            return false;
        }

        public boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
            return false;
        }

        public boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
            return false;
        }

        public boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
            return false;
        }

        public boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
            return false;
        }

        public void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
        }

        public void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> list) throws RemoteException {
        }

        public List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
            return null;
        }

        public List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
            return null;
        }

        public void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
        }

        public String getCertInstallerPackage(ComponentName who) throws RemoteException {
            return null;
        }

        public boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown, List<String> list) throws RemoteException {
            return false;
        }

        public String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
            return null;
        }

        public boolean isAlwaysOnVpnLockdownEnabled(ComponentName who) throws RemoteException {
            return false;
        }

        public List<String> getAlwaysOnVpnLockdownWhitelist(ComponentName who) throws RemoteException {
            return null;
        }

        public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
        }

        public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
        }

        public void setDefaultSmsApplication(ComponentName admin, String packageName) throws RemoteException {
        }

        public void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
        }

        public Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
            return null;
        }

        public boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        public String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
            return null;
        }

        public boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
            return false;
        }

        public void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
        }

        public ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
            return null;
        }

        public void setUserRestriction(ComponentName who, String key, boolean enable) throws RemoteException {
        }

        public Bundle getUserRestrictions(ComponentName who) throws RemoteException {
            return null;
        }

        public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
        }

        public void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
        }

        public boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
            return false;
        }

        public List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
            return null;
        }

        public List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
            return null;
        }

        public boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean setPermittedInputMethods(ComponentName admin, List packageList) throws RemoteException {
            return false;
        }

        public List getPermittedInputMethods(ComponentName admin) throws RemoteException {
            return null;
        }

        public List getPermittedInputMethodsForCurrentUser() throws RemoteException {
            return null;
        }

        public boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> list) throws RemoteException {
            return false;
        }

        public List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
            return null;
        }

        public boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
            return false;
        }

        public Intent createAdminSupportIntent(String restriction) throws RemoteException {
            return null;
        }

        public boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden) throws RemoteException {
            return false;
        }

        public boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        public UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
            return null;
        }

        public boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return false;
        }

        public boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return false;
        }

        public int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
            return 0;
        }

        public int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return 0;
        }

        public int logoutUser(ComponentName who) throws RemoteException {
            return 0;
        }

        public List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
            return null;
        }

        public void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
        }

        public int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
            return 0;
        }

        public boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        public void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled) throws RemoteException {
        }

        public String[] getAccountTypesWithManagementDisabled() throws RemoteException {
            return null;
        }

        public String[] getAccountTypesWithManagementDisabledAsUser(int userId) throws RemoteException {
            return null;
        }

        public void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
        }

        public String[] getLockTaskPackages(ComponentName who) throws RemoteException {
            return null;
        }

        public boolean isLockTaskPermitted(String pkg) throws RemoteException {
            return false;
        }

        public void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
        }

        public int getLockTaskFeatures(ComponentName who) throws RemoteException {
            return 0;
        }

        public void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        public void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        public void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        public boolean setTime(ComponentName who, long millis) throws RemoteException {
            return false;
        }

        public boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
            return false;
        }

        public void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
        }

        public boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
            return false;
        }

        public void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
        }

        public void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
        }

        public boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        public void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        public boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        public boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        public void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        public boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        public boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        public void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
        }

        public void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        public boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        public boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        public void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
        }

        public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
            return null;
        }

        public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        public List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
            return null;
        }

        public void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
        }

        public boolean getAutoTimeRequired() throws RemoteException {
            return false;
        }

        public void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
        }

        public boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
            return false;
        }

        public boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
            return false;
        }

        public void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
        }

        public void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
        }

        public SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
            return null;
        }

        public void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
        }

        public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        public boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
            return false;
        }

        public boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
            return false;
        }

        public void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
        }

        public SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
            return null;
        }

        public void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
        }

        public int getPermissionPolicy(ComponentName admin) throws RemoteException {
            return 0;
        }

        public void setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState, RemoteCallback resultReceiver) throws RemoteException {
        }

        public int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
            return 0;
        }

        public boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
            return false;
        }

        public int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
            return 0;
        }

        public void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> list) throws RemoteException {
        }

        public List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
            return null;
        }

        public boolean isManagedProfile(ComponentName admin) throws RemoteException {
            return false;
        }

        public boolean isSystemOnlyUser(ComponentName admin) throws RemoteException {
            return false;
        }

        public String getWifiMacAddress(ComponentName admin) throws RemoteException {
            return null;
        }

        public void reboot(ComponentName admin) throws RemoteException {
        }

        public void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
        }

        public CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        public void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
        }

        public CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        public CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
            return null;
        }

        public CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
            return null;
        }

        public boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
            return false;
        }

        public void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
        }

        public void setOrganizationColorForUser(int color, int userId) throws RemoteException {
        }

        public int getOrganizationColor(ComponentName admin) throws RemoteException {
            return 0;
        }

        public int getOrganizationColorForUser(int userHandle) throws RemoteException {
            return 0;
        }

        public void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
        }

        public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
            return null;
        }

        public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
            return null;
        }

        public CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
            return null;
        }

        public int getUserProvisioningState() throws RemoteException {
            return 0;
        }

        public void setUserProvisioningState(int state, int userHandle) throws RemoteException {
        }

        public void setAffiliationIds(ComponentName admin, List<String> list) throws RemoteException {
        }

        public List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
            return null;
        }

        public boolean isAffiliatedUser() throws RemoteException {
            return false;
        }

        public void setSecurityLoggingEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        public boolean isSecurityLoggingEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        public ParceledListSlice retrieveSecurityLogs(ComponentName admin) throws RemoteException {
            return null;
        }

        public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin) throws RemoteException {
            return null;
        }

        public long forceNetworkLogs() throws RemoteException {
            return 0;
        }

        public long forceSecurityLogs() throws RemoteException {
            return 0;
        }

        public boolean isUninstallInQueue(String packageName) throws RemoteException {
            return false;
        }

        public void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
        }

        public boolean isDeviceProvisioned() throws RemoteException {
            return false;
        }

        public boolean isDeviceProvisioningConfigApplied() throws RemoteException {
            return false;
        }

        public void setDeviceProvisioningConfigApplied() throws RemoteException {
        }

        public void forceUpdateUserSetupComplete() throws RemoteException {
        }

        public void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        public boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        public void setNetworkLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
        }

        public boolean isNetworkLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        public List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, String packageName, long batchToken) throws RemoteException {
            return null;
        }

        public boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
            return false;
        }

        public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
            return null;
        }

        public boolean isEphemeralUser(ComponentName admin) throws RemoteException {
            return false;
        }

        public long getLastSecurityLogRetrievalTime() throws RemoteException {
            return 0;
        }

        public long getLastBugReportRequestTime() throws RemoteException {
            return 0;
        }

        public long getLastNetworkLogRetrievalTime() throws RemoteException {
            return 0;
        }

        public boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
            return false;
        }

        public boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
            return false;
        }

        public boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
            return false;
        }

        public boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
            return false;
        }

        public boolean isCurrentInputMethodSetByOwner() throws RemoteException {
            return false;
        }

        public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
            return null;
        }

        public void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
        }

        public void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        public boolean isLogoutEnabled() throws RemoteException {
            return false;
        }

        public List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
            return null;
        }

        public void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
        }

        public PersistableBundle getTransferOwnershipBundle() throws RemoteException {
            return null;
        }

        public void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
        }

        public void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
        }

        public CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        public CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        public List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> list) throws RemoteException {
            return null;
        }

        public List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        public int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
            return 0;
        }

        public boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
            return false;
        }

        public boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
            return false;
        }

        public List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
            return null;
        }

        public void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        public boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        public boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        public int setGlobalPrivateDns(ComponentName admin, int mode, String privateDnsHost) throws RemoteException {
            return 0;
        }

        public int getGlobalPrivateDnsMode(ComponentName admin) throws RemoteException {
            return 0;
        }

        public String getGlobalPrivateDnsHost(ComponentName admin) throws RemoteException {
            return null;
        }

        public void grantDeviceIdsAccessToProfileOwner(ComponentName who, int userId) throws RemoteException {
        }

        public void installUpdateFromFile(ComponentName admin, ParcelFileDescriptor updateFileDescriptor, StartInstallingUpdateCallback listener) throws RemoteException {
        }

        public void setCrossProfileCalendarPackages(ComponentName admin, List<String> list) throws RemoteException {
        }

        public List<String> getCrossProfileCalendarPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        public boolean isPackageAllowedToAccessCalendarForUser(String packageName, int userHandle) throws RemoteException {
            return false;
        }

        public List<String> getCrossProfileCalendarPackagesForUser(int userHandle) throws RemoteException {
            return null;
        }

        public boolean isManagedKiosk() throws RemoteException {
            return false;
        }

        public boolean isUnattendedManagedKiosk() throws RemoteException {
            return false;
        }

        public boolean startViewCalendarEventInManagedProfile(String packageName, long eventId, long start, long end, boolean allDay, int flags) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDevicePolicyManager {
        private static final String DESCRIPTOR = "android.app.admin.IDevicePolicyManager";
        static final int TRANSACTION_addCrossProfileIntentFilter = 116;
        static final int TRANSACTION_addCrossProfileWidgetProvider = 172;
        static final int TRANSACTION_addOverrideApn = 262;
        static final int TRANSACTION_addPersistentPreferredActivity = 104;
        static final int TRANSACTION_approveCaCert = 88;
        static final int TRANSACTION_bindDeviceAdminServiceAsUser = 238;
        static final int TRANSACTION_checkDeviceIdentifierAccess = 80;
        static final int TRANSACTION_checkProvisioningPreCondition = 194;
        static final int TRANSACTION_choosePrivateKeyAlias = 94;
        static final int TRANSACTION_clearApplicationUserData = 250;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 117;
        static final int TRANSACTION_clearDeviceOwner = 70;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 105;
        static final int TRANSACTION_clearProfileOwner = 78;
        static final int TRANSACTION_clearResetPasswordToken = 245;
        static final int TRANSACTION_clearSystemUpdatePolicyFreezePeriodRecord = 183;
        static final int TRANSACTION_createAdminSupportIntent = 129;
        static final int TRANSACTION_createAndManageUser = 132;
        static final int TRANSACTION_enableSystemApp = 139;
        static final int TRANSACTION_enableSystemAppWithIntent = 140;
        static final int TRANSACTION_enforceCanManageCaCerts = 87;
        static final int TRANSACTION_forceNetworkLogs = 225;
        static final int TRANSACTION_forceRemoveActiveAdmin = 56;
        static final int TRANSACTION_forceSecurityLogs = 226;
        static final int TRANSACTION_forceUpdateUserSetupComplete = 232;
        static final int TRANSACTION_generateKeyPair = 92;
        static final int TRANSACTION_getAccountTypesWithManagementDisabled = 143;
        static final int TRANSACTION_getAccountTypesWithManagementDisabledAsUser = 144;
        static final int TRANSACTION_getActiveAdmins = 52;
        static final int TRANSACTION_getAffiliationIds = 219;
        static final int TRANSACTION_getAlwaysOnVpnLockdownWhitelist = 103;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 101;
        static final int TRANSACTION_getApplicationRestrictions = 108;
        static final int TRANSACTION_getApplicationRestrictionsManagingPackage = 110;
        static final int TRANSACTION_getAutoTimeRequired = 176;
        static final int TRANSACTION_getBindDeviceAdminTargetUsers = 239;
        static final int TRANSACTION_getBluetoothContactSharingDisabled = 168;
        static final int TRANSACTION_getBluetoothContactSharingDisabledForUser = 169;
        static final int TRANSACTION_getCameraDisabled = 45;
        static final int TRANSACTION_getCertInstallerPackage = 99;
        static final int TRANSACTION_getCrossProfileCalendarPackages = 275;
        static final int TRANSACTION_getCrossProfileCalendarPackagesForUser = 277;
        static final int TRANSACTION_getCrossProfileCallerIdDisabled = 161;
        static final int TRANSACTION_getCrossProfileCallerIdDisabledForUser = 162;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabled = 164;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabledForUser = 165;
        static final int TRANSACTION_getCrossProfileWidgetProviders = 174;
        static final int TRANSACTION_getCurrentFailedPasswordAttempts = 26;
        static final int TRANSACTION_getDelegatePackages = 97;
        static final int TRANSACTION_getDelegatedScopes = 96;
        static final int TRANSACTION_getDeviceOwnerComponent = 67;
        static final int TRANSACTION_getDeviceOwnerLockScreenInfo = 82;
        static final int TRANSACTION_getDeviceOwnerName = 69;
        static final int TRANSACTION_getDeviceOwnerOrganizationName = 214;
        static final int TRANSACTION_getDeviceOwnerUserId = 71;
        static final int TRANSACTION_getDisallowedSystemApps = 253;
        static final int TRANSACTION_getDoNotAskCredentialsOnBoot = 186;
        static final int TRANSACTION_getEndUserSessionMessage = 259;
        static final int TRANSACTION_getForceEphemeralUsers = 178;
        static final int TRANSACTION_getGlobalPrivateDnsHost = 271;
        static final int TRANSACTION_getGlobalPrivateDnsMode = 270;
        static final int TRANSACTION_getGlobalProxyAdmin = 38;
        static final int TRANSACTION_getKeepUninstalledPackages = 196;
        static final int TRANSACTION_getKeyguardDisabledFeatures = 49;
        static final int TRANSACTION_getLastBugReportRequestTime = 242;
        static final int TRANSACTION_getLastNetworkLogRetrievalTime = 243;
        static final int TRANSACTION_getLastSecurityLogRetrievalTime = 241;
        static final int TRANSACTION_getLockTaskFeatures = 149;
        static final int TRANSACTION_getLockTaskPackages = 146;
        static final int TRANSACTION_getLongSupportMessage = 204;
        static final int TRANSACTION_getLongSupportMessageForUser = 206;
        static final int TRANSACTION_getMaximumFailedPasswordsForWipe = 29;
        static final int TRANSACTION_getMaximumTimeToLock = 32;
        static final int TRANSACTION_getMeteredDataDisabledPackages = 261;
        static final int TRANSACTION_getOrganizationColor = 210;
        static final int TRANSACTION_getOrganizationColorForUser = 211;
        static final int TRANSACTION_getOrganizationName = 213;
        static final int TRANSACTION_getOrganizationNameForUser = 215;
        static final int TRANSACTION_getOverrideApns = 265;
        static final int TRANSACTION_getOwnerInstalledCaCerts = 249;
        static final int TRANSACTION_getPasswordComplexity = 24;
        static final int TRANSACTION_getPasswordExpiration = 21;
        static final int TRANSACTION_getPasswordExpirationTimeout = 20;
        static final int TRANSACTION_getPasswordHistoryLength = 18;
        static final int TRANSACTION_getPasswordMinimumLength = 4;
        static final int TRANSACTION_getPasswordMinimumLetters = 10;
        static final int TRANSACTION_getPasswordMinimumLowerCase = 8;
        static final int TRANSACTION_getPasswordMinimumNonLetter = 16;
        static final int TRANSACTION_getPasswordMinimumNumeric = 12;
        static final int TRANSACTION_getPasswordMinimumSymbols = 14;
        static final int TRANSACTION_getPasswordMinimumUpperCase = 6;
        static final int TRANSACTION_getPasswordQuality = 2;
        static final int TRANSACTION_getPendingSystemUpdate = 188;
        static final int TRANSACTION_getPermissionGrantState = 192;
        static final int TRANSACTION_getPermissionPolicy = 190;
        static final int TRANSACTION_getPermittedAccessibilityServices = 119;
        static final int TRANSACTION_getPermittedAccessibilityServicesForUser = 120;
        static final int TRANSACTION_getPermittedCrossProfileNotificationListeners = 127;
        static final int TRANSACTION_getPermittedInputMethods = 123;
        static final int TRANSACTION_getPermittedInputMethodsForCurrentUser = 124;
        static final int TRANSACTION_getProfileOwner = 74;
        static final int TRANSACTION_getProfileOwnerAsUser = 73;
        static final int TRANSACTION_getProfileOwnerName = 75;
        static final int TRANSACTION_getProfileWithMinimumFailedPasswordsForWipe = 27;
        static final int TRANSACTION_getRemoveWarning = 54;
        static final int TRANSACTION_getRequiredStrongAuthTimeout = 34;
        static final int TRANSACTION_getRestrictionsProvider = 113;
        static final int TRANSACTION_getScreenCaptureDisabled = 47;
        static final int TRANSACTION_getSecondaryUsers = 138;
        static final int TRANSACTION_getShortSupportMessage = 202;
        static final int TRANSACTION_getShortSupportMessageForUser = 205;
        static final int TRANSACTION_getStartUserSessionMessage = 258;
        static final int TRANSACTION_getStorageEncryption = 41;
        static final int TRANSACTION_getStorageEncryptionStatus = 42;
        static final int TRANSACTION_getSystemUpdatePolicy = 182;
        static final int TRANSACTION_getTransferOwnershipBundle = 255;
        static final int TRANSACTION_getTrustAgentConfiguration = 171;
        static final int TRANSACTION_getUserProvisioningState = 216;
        static final int TRANSACTION_getUserRestrictions = 115;
        static final int TRANSACTION_getWifiMacAddress = 199;
        static final int TRANSACTION_grantDeviceIdsAccessToProfileOwner = 272;
        static final int TRANSACTION_hasDeviceOwner = 68;
        static final int TRANSACTION_hasGrantedPolicy = 57;
        static final int TRANSACTION_hasUserSetupCompleted = 79;
        static final int TRANSACTION_installCaCert = 85;
        static final int TRANSACTION_installExistingPackage = 141;
        static final int TRANSACTION_installKeyPair = 90;
        static final int TRANSACTION_installUpdateFromFile = 273;
        static final int TRANSACTION_isAccessibilityServicePermittedByAdmin = 121;
        static final int TRANSACTION_isActivePasswordSufficient = 22;
        static final int TRANSACTION_isAdminActive = 51;
        static final int TRANSACTION_isAffiliatedUser = 220;
        static final int TRANSACTION_isAlwaysOnVpnLockdownEnabled = 102;
        static final int TRANSACTION_isApplicationHidden = 131;
        static final int TRANSACTION_isBackupServiceEnabled = 234;
        static final int TRANSACTION_isCaCertApproved = 89;
        static final int TRANSACTION_isCallerApplicationRestrictionsManagingPackage = 111;
        static final int TRANSACTION_isCurrentInputMethodSetByOwner = 248;
        static final int TRANSACTION_isDeviceProvisioned = 229;
        static final int TRANSACTION_isDeviceProvisioningConfigApplied = 230;
        static final int TRANSACTION_isEphemeralUser = 240;
        static final int TRANSACTION_isInputMethodPermittedByAdmin = 125;
        static final int TRANSACTION_isLockTaskPermitted = 147;
        static final int TRANSACTION_isLogoutEnabled = 252;
        static final int TRANSACTION_isManagedKiosk = 278;
        static final int TRANSACTION_isManagedProfile = 197;
        static final int TRANSACTION_isMasterVolumeMuted = 156;
        static final int TRANSACTION_isMeteredDataDisabledPackageForUser = 268;
        static final int TRANSACTION_isNetworkLoggingEnabled = 236;
        static final int TRANSACTION_isNotificationListenerServicePermitted = 128;
        static final int TRANSACTION_isOverrideApnEnabled = 267;
        static final int TRANSACTION_isPackageAllowedToAccessCalendarForUser = 276;
        static final int TRANSACTION_isPackageSuspended = 84;
        static final int TRANSACTION_isProfileActivePasswordSufficientForParent = 23;
        static final int TRANSACTION_isProvisioningAllowed = 193;
        static final int TRANSACTION_isRemovingAdmin = 179;
        static final int TRANSACTION_isResetPasswordTokenActive = 246;
        static final int TRANSACTION_isSecurityLoggingEnabled = 222;
        static final int TRANSACTION_isSeparateProfileChallengeAllowed = 207;
        static final int TRANSACTION_isSystemOnlyUser = 198;
        static final int TRANSACTION_isUnattendedManagedKiosk = 279;
        static final int TRANSACTION_isUninstallBlocked = 159;
        static final int TRANSACTION_isUninstallInQueue = 227;
        static final int TRANSACTION_isUsingUnifiedPassword = 25;
        static final int TRANSACTION_lockNow = 35;
        static final int TRANSACTION_logoutUser = 137;
        static final int TRANSACTION_notifyLockTaskModeChanged = 157;
        static final int TRANSACTION_notifyPendingSystemUpdate = 187;
        static final int TRANSACTION_packageHasActiveAdmins = 53;
        static final int TRANSACTION_reboot = 200;
        static final int TRANSACTION_removeActiveAdmin = 55;
        static final int TRANSACTION_removeCrossProfileWidgetProvider = 173;
        static final int TRANSACTION_removeKeyPair = 91;
        static final int TRANSACTION_removeOverrideApn = 264;
        static final int TRANSACTION_removeUser = 133;
        static final int TRANSACTION_reportFailedBiometricAttempt = 62;
        static final int TRANSACTION_reportFailedPasswordAttempt = 60;
        static final int TRANSACTION_reportKeyguardDismissed = 64;
        static final int TRANSACTION_reportKeyguardSecured = 65;
        static final int TRANSACTION_reportPasswordChanged = 59;
        static final int TRANSACTION_reportSuccessfulBiometricAttempt = 63;
        static final int TRANSACTION_reportSuccessfulPasswordAttempt = 61;
        static final int TRANSACTION_requestBugreport = 43;
        static final int TRANSACTION_resetPassword = 30;
        static final int TRANSACTION_resetPasswordWithToken = 247;
        static final int TRANSACTION_retrieveNetworkLogs = 237;
        static final int TRANSACTION_retrievePreRebootSecurityLogs = 224;
        static final int TRANSACTION_retrieveSecurityLogs = 223;
        static final int TRANSACTION_setAccountManagementDisabled = 142;
        static final int TRANSACTION_setActiveAdmin = 50;
        static final int TRANSACTION_setActivePasswordState = 58;
        static final int TRANSACTION_setAffiliationIds = 218;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 100;
        static final int TRANSACTION_setApplicationHidden = 130;
        static final int TRANSACTION_setApplicationRestrictions = 107;
        static final int TRANSACTION_setApplicationRestrictionsManagingPackage = 109;
        static final int TRANSACTION_setAutoTimeRequired = 175;
        static final int TRANSACTION_setBackupServiceEnabled = 233;
        static final int TRANSACTION_setBluetoothContactSharingDisabled = 167;
        static final int TRANSACTION_setCameraDisabled = 44;
        static final int TRANSACTION_setCertInstallerPackage = 98;
        static final int TRANSACTION_setCrossProfileCalendarPackages = 274;
        static final int TRANSACTION_setCrossProfileCallerIdDisabled = 160;
        static final int TRANSACTION_setCrossProfileContactsSearchDisabled = 163;
        static final int TRANSACTION_setDefaultSmsApplication = 106;
        static final int TRANSACTION_setDelegatedScopes = 95;
        static final int TRANSACTION_setDeviceOwner = 66;
        static final int TRANSACTION_setDeviceOwnerLockScreenInfo = 81;
        static final int TRANSACTION_setDeviceProvisioningConfigApplied = 231;
        static final int TRANSACTION_setEndUserSessionMessage = 257;
        static final int TRANSACTION_setForceEphemeralUsers = 177;
        static final int TRANSACTION_setGlobalPrivateDns = 269;
        static final int TRANSACTION_setGlobalProxy = 37;
        static final int TRANSACTION_setGlobalSetting = 150;
        static final int TRANSACTION_setKeepUninstalledPackages = 195;
        static final int TRANSACTION_setKeyPairCertificate = 93;
        static final int TRANSACTION_setKeyguardDisabled = 184;
        static final int TRANSACTION_setKeyguardDisabledFeatures = 48;
        static final int TRANSACTION_setLockTaskFeatures = 148;
        static final int TRANSACTION_setLockTaskPackages = 145;
        static final int TRANSACTION_setLogoutEnabled = 251;
        static final int TRANSACTION_setLongSupportMessage = 203;
        static final int TRANSACTION_setMasterVolumeMuted = 155;
        static final int TRANSACTION_setMaximumFailedPasswordsForWipe = 28;
        static final int TRANSACTION_setMaximumTimeToLock = 31;
        static final int TRANSACTION_setMeteredDataDisabledPackages = 260;
        static final int TRANSACTION_setNetworkLoggingEnabled = 235;
        static final int TRANSACTION_setOrganizationColor = 208;
        static final int TRANSACTION_setOrganizationColorForUser = 209;
        static final int TRANSACTION_setOrganizationName = 212;
        static final int TRANSACTION_setOverrideApnsEnabled = 266;
        static final int TRANSACTION_setPackagesSuspended = 83;
        static final int TRANSACTION_setPasswordExpirationTimeout = 19;
        static final int TRANSACTION_setPasswordHistoryLength = 17;
        static final int TRANSACTION_setPasswordMinimumLength = 3;
        static final int TRANSACTION_setPasswordMinimumLetters = 9;
        static final int TRANSACTION_setPasswordMinimumLowerCase = 7;
        static final int TRANSACTION_setPasswordMinimumNonLetter = 15;
        static final int TRANSACTION_setPasswordMinimumNumeric = 11;
        static final int TRANSACTION_setPasswordMinimumSymbols = 13;
        static final int TRANSACTION_setPasswordMinimumUpperCase = 5;
        static final int TRANSACTION_setPasswordQuality = 1;
        static final int TRANSACTION_setPermissionGrantState = 191;
        static final int TRANSACTION_setPermissionPolicy = 189;
        static final int TRANSACTION_setPermittedAccessibilityServices = 118;
        static final int TRANSACTION_setPermittedCrossProfileNotificationListeners = 126;
        static final int TRANSACTION_setPermittedInputMethods = 122;
        static final int TRANSACTION_setProfileEnabled = 76;
        static final int TRANSACTION_setProfileName = 77;
        static final int TRANSACTION_setProfileOwner = 72;
        static final int TRANSACTION_setRecommendedGlobalProxy = 39;
        static final int TRANSACTION_setRequiredStrongAuthTimeout = 33;
        static final int TRANSACTION_setResetPasswordToken = 244;
        static final int TRANSACTION_setRestrictionsProvider = 112;
        static final int TRANSACTION_setScreenCaptureDisabled = 46;
        static final int TRANSACTION_setSecureSetting = 152;
        static final int TRANSACTION_setSecurityLoggingEnabled = 221;
        static final int TRANSACTION_setShortSupportMessage = 201;
        static final int TRANSACTION_setStartUserSessionMessage = 256;
        static final int TRANSACTION_setStatusBarDisabled = 185;
        static final int TRANSACTION_setStorageEncryption = 40;
        static final int TRANSACTION_setSystemSetting = 151;
        static final int TRANSACTION_setSystemUpdatePolicy = 181;
        static final int TRANSACTION_setTime = 153;
        static final int TRANSACTION_setTimeZone = 154;
        static final int TRANSACTION_setTrustAgentConfiguration = 170;
        static final int TRANSACTION_setUninstallBlocked = 158;
        static final int TRANSACTION_setUserIcon = 180;
        static final int TRANSACTION_setUserProvisioningState = 217;
        static final int TRANSACTION_setUserRestriction = 114;
        static final int TRANSACTION_startManagedQuickContact = 166;
        static final int TRANSACTION_startUserInBackground = 135;
        static final int TRANSACTION_startViewCalendarEventInManagedProfile = 280;
        static final int TRANSACTION_stopUser = 136;
        static final int TRANSACTION_switchUser = 134;
        static final int TRANSACTION_transferOwnership = 254;
        static final int TRANSACTION_uninstallCaCerts = 86;
        static final int TRANSACTION_uninstallPackageWithActiveAdmins = 228;
        static final int TRANSACTION_updateOverrideApn = 263;
        static final int TRANSACTION_wipeDataWithReason = 36;

        private static class Proxy implements IDevicePolicyManager {
            public static IDevicePolicyManager sDefaultImpl;
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

            public void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(quality);
                    _data.writeInt(parent ? 1 : 0);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordQuality(who, quality, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordQuality(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumLength(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumLength(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumUpperCase(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumUpperCase(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumLowerCase(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumLowerCase(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumLetters(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumLetters(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumNumeric(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumNumeric(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumSymbols(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumSymbols(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordMinimumNonLetter(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordMinimumNonLetter(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordHistoryLength(who, length, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getPasswordHistoryLength(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(expiration);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPasswordExpirationTimeout(who, expiration, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    long j = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getPasswordExpirationTimeout(who, userHandle, parent);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    long j = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getPasswordExpiration(who, userHandle, parent);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    _data.writeInt(parent ? 1 : 0);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isActivePasswordSufficient(userHandle, parent);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isProfileActivePasswordSufficientForParent(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isProfileActivePasswordSufficientForParent(userHandle);
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

            public int getPasswordComplexity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPasswordComplexity();
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

            public boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isUsingUnifiedPassword(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(26, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getCurrentFailedPasswordAttempts(userHandle, parent);
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

            public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getProfileWithMinimumFailedPasswordsForWipe(userHandle, parent);
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

            public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(num);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMaximumFailedPasswordsForWipe(admin, num, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getMaximumFailedPasswordsForWipe(admin, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean resetPassword(String password, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    _data.writeInt(flags);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().resetPassword(password, flags);
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

            public void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMaximumTimeToLock(who, timeMs, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    long j = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getMaximumTimeToLock(who, userHandle, parent);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRequiredStrongAuthTimeout(who, timeMs, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    long j = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getRequiredStrongAuthTimeout(who, userId, parent);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void lockNow(int flags, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(parent ? 1 : 0);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().lockNow(flags, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void wipeDataWithReason(int flags, String wipeReasonForUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeString(wipeReasonForUser);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().wipeDataWithReason(flags, wipeReasonForUser);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(proxySpec);
                    _data.writeString(exclusionList);
                    ComponentName componentName = this.mRemote;
                    if (!componentName.transact(37, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != null) {
                            componentName = Stub.getDefaultImpl().setGlobalProxy(admin, proxySpec, exclusionList);
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

            public ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    ComponentName componentName = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getGlobalProxyAdmin(userHandle);
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

            public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (proxyInfo != null) {
                        _data.writeInt(1);
                        proxyInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRecommendedGlobalProxy(admin, proxyInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!encrypt) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().setStorageEncryption(who, encrypt);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getStorageEncryption(who, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeInt(userHandle);
                    int i = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getStorageEncryptionStatus(callerPackage, userHandle);
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

            public boolean requestBugreport(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().requestBugreport(who);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCameraDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCameraDisabled(who, disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCameraDisabled(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getCameraDisabled(who, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScreenCaptureDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setScreenCaptureDisabled(who, disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getScreenCaptureDisabled(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getScreenCaptureDisabled(who, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(which);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setKeyguardDisabledFeatures(who, which, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    int i2 = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().getKeyguardDisabledFeatures(who, userHandle, parent);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!refreshing) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setActiveAdmin(policyReceiver, refreshing, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isAdminActive(policyReceiver, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    List<ComponentName> list = 52;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getActiveAdmins(userHandle);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ComponentName.CREATOR);
                    List<ComponentName> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(53, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().packageHasActiveAdmins(packageName, userHandle);
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

            public void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getRemoveWarning(policyReceiver, result, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeActiveAdmin(policyReceiver, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceRemoveActiveAdmin(policyReceiver, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(usesPolicy);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().hasGrantedPolicy(policyReceiver, usesPolicy, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setActivePasswordState(PasswordMetrics metrics, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (metrics != null) {
                        _data.writeInt(1);
                        metrics.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setActivePasswordState(metrics, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportPasswordChanged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportPasswordChanged(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportFailedPasswordAttempt(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportSuccessfulPasswordAttempt(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportFailedBiometricAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportFailedBiometricAttempt(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportSuccessfulBiometricAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportSuccessfulBiometricAttempt(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportKeyguardDismissed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportKeyguardDismissed(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportKeyguardSecured(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportKeyguardSecured(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setDeviceOwner(who, ownerName, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUserOnly ? 1 : 0);
                    ComponentName componentName = this.mRemote;
                    if (!componentName.transact(67, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != null) {
                            componentName = Stub.getDefaultImpl().getDeviceOwnerComponent(callingUserOnly);
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

            public boolean hasDeviceOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasDeviceOwner();
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

            public String getDeviceOwnerName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 69;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceOwnerName();
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

            public void clearDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearDeviceOwner(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDeviceOwnerUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 71;
                    if (!this.mRemote.transact(71, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDeviceOwnerUserId();
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

            public boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerName);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setProfileOwner(who, ownerName, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getProfileOwnerAsUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    ComponentName componentName = 73;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getProfileOwnerAsUser(userHandle);
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

            public ComponentName getProfileOwner(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    ComponentName componentName = 74;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getProfileOwner(userHandle);
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

            public String getProfileOwnerName(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    String str = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getProfileOwnerName(userHandle);
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

            public void setProfileEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setProfileEnabled(who);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setProfileName(ComponentName who, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(profileName);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setProfileName(who, profileName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearProfileOwner(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearProfileOwner(who);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasUserSetupCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(79, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasUserSetupCompleted();
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

            public boolean checkDeviceIdentifierAccess(String packageName, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(80, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkDeviceIdentifierAccess(packageName, pid, uid);
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

            public void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deviceOwnerInfo != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(deviceOwnerInfo, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDeviceOwnerLockScreenInfo(who, deviceOwnerInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CharSequence charSequence = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().getDeviceOwnerLockScreenInfo();
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

            public String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    String[] strArr = 0;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(packageNames);
                    if (!suspended) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().setPackagesSuspended(admin, callerPackage, packageNames, suspended);
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

            public boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isPackageSuspended(admin, callerPackage, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeByteArray(certBuffer);
                    if (this.mRemote.transact(85, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().installCaCert(admin, callerPackage, certBuffer);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(aliases);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().uninstallCaCerts(admin, callerPackage, aliases);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enforceCanManageCaCerts(admin, callerPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    _data.writeInt(approval ? 1 : 0);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().approveCaCert(alias, userHandle, approval);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCaCertApproved(alias, userHandle);
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

            public boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
                Throwable th;
                byte[] bArr;
                byte[] bArr2;
                ComponentName componentName = who;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        bArr = privKeyBuffer;
                        bArr2 = certBuffer;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(privKeyBuffer);
                    } catch (Throwable th3) {
                        th = th3;
                        bArr2 = certBuffer;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(certBuffer);
                        _data.writeByteArray(certChainBuffer);
                        _data.writeString(alias);
                        _data.writeInt(requestAccess ? 1 : 0);
                        _data.writeInt(isUserSelectable ? 1 : 0);
                        if (this.mRemote.transact(90, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().installKeyPair(who, callerPackage, privKeyBuffer, certBuffer, certChainBuffer, alias, requestAccess, isUserSelectable);
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
                    String str = callerPackage;
                    bArr = privKeyBuffer;
                    bArr2 = certBuffer;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    if (this.mRemote.transact(91, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeKeyPair(who, callerPackage, alias);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
                Throwable th;
                KeymasterCertificateChain keymasterCertificateChain;
                int i;
                String str;
                ComponentName componentName = who;
                ParcelableKeyGenParameterSpec parcelableKeyGenParameterSpec = keySpec;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(algorithm);
                            if (parcelableKeyGenParameterSpec != null) {
                                _data.writeInt(1);
                                parcelableKeyGenParameterSpec.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(idAttestationFlags);
                            } catch (Throwable th2) {
                                th = th2;
                                keymasterCertificateChain = attestationChain;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            i = idAttestationFlags;
                            keymasterCertificateChain = attestationChain;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                if (_reply.readInt() != 0) {
                                    try {
                                        attestationChain.readFromParcel(_reply);
                                    } catch (Throwable th4) {
                                        th = th4;
                                    }
                                } else {
                                    keymasterCertificateChain = attestationChain;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().generateKeyPair(who, callerPackage, algorithm, keySpec, idAttestationFlags, attestationChain);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th5) {
                            th = th5;
                            keymasterCertificateChain = attestationChain;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str = algorithm;
                        i = idAttestationFlags;
                        keymasterCertificateChain = attestationChain;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = callerPackage;
                    str = algorithm;
                    i = idAttestationFlags;
                    keymasterCertificateChain = attestationChain;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
                Throwable th;
                String str;
                byte[] bArr;
                byte[] bArr2;
                ComponentName componentName = who;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        str = alias;
                        bArr = certBuffer;
                        bArr2 = certChainBuffer;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(alias);
                    } catch (Throwable th3) {
                        th = th3;
                        bArr = certBuffer;
                        bArr2 = certChainBuffer;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(certBuffer);
                        try {
                            _data.writeByteArray(certChainBuffer);
                            _data.writeInt(isUserSelectable ? 1 : 0);
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(93, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().setKeyPairCertificate(who, callerPackage, alias, certBuffer, certChainBuffer, isUserSelectable);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        bArr2 = certChainBuffer;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = callerPackage;
                    str = alias;
                    bArr = certBuffer;
                    bArr2 = certChainBuffer;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(alias);
                    _data.writeStrongBinder(aliasCallback);
                    if (this.mRemote.transact(94, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().choosePrivateKeyAlias(uid, uri, alias, aliasCallback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> scopes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(delegatePackage);
                    _data.writeStringList(scopes);
                    if (this.mRemote.transact(95, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDelegatedScopes(who, delegatePackage, scopes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(delegatePackage);
                    List<String> list = this.mRemote;
                    if (!list.transact(96, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getDelegatedScopes(who, delegatePackage);
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

            public List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scope);
                    List<String> list = this.mRemote;
                    if (!list.transact(97, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getDelegatePackages(who, scope);
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

            public void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(installerPackage);
                    if (this.mRemote.transact(98, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCertInstallerPackage(who, installerPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCertInstallerPackage(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(99, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getCertInstallerPackage(who);
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

            public boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown, List<String> lockdownWhitelist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(vpnPackage);
                    _data.writeInt(lockdown ? 1 : 0);
                    _data.writeStringList(lockdownWhitelist);
                    if (this.mRemote.transact(100, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAlwaysOnVpnPackage(who, vpnPackage, lockdown, lockdownWhitelist);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(101, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getAlwaysOnVpnPackage(who);
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

            public boolean isAlwaysOnVpnLockdownEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(102, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isAlwaysOnVpnLockdownEnabled(who);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getAlwaysOnVpnLockdownWhitelist(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<String> list = this.mRemote;
                    if (!list.transact(103, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getAlwaysOnVpnLockdownWhitelist(who);
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

            public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
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
                    if (this.mRemote.transact(104, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPersistentPreferredActivity(admin, filter, activity);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(105, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearPackagePersistentPreferredActivities(admin, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDefaultSmsApplication(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(106, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultSmsApplication(admin, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(107, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationRestrictions(who, callerPackage, packageName, settings);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    Bundle bundle = this.mRemote;
                    if (!bundle.transact(108, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != null) {
                            bundle = Stub.getDefaultImpl().getApplicationRestrictions(who, callerPackage, packageName);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(109, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setApplicationRestrictionsManagingPackage(admin, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(110, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getApplicationRestrictionsManagingPackage(admin);
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

            public boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(111, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCallerApplicationRestrictionsManagingPackage(callerPackage);
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

            public void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(112, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRestrictionsProvider(who, provider);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    ComponentName componentName = 113;
                    if (!this.mRemote.transact(113, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getRestrictionsProvider(userHandle);
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

            public void setUserRestriction(ComponentName who, String key, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    if (!enable) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(114, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserRestriction(who, key, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getUserRestrictions(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    Bundle bundle = this.mRemote;
                    if (!bundle.transact(115, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != null) {
                            bundle = Stub.getDefaultImpl().getUserRestrictions(who);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (this.mRemote.transact(116, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addCrossProfileIntentFilter(admin, filter, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(117, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearCrossProfileIntentFilters(admin);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeList(packageList);
                    if (this.mRemote.transact(118, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPermittedAccessibilityServices(admin, packageList);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List list = this.mRemote;
                    if (!list.transact(119, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getPermittedAccessibilityServices(admin);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = getClass().getClassLoader();
                    ArrayList _result = _reply.readArrayList(list);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List list = 120;
                    if (!this.mRemote.transact(120, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPermittedAccessibilityServicesForUser(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = getClass().getClassLoader();
                    ArrayList _result = _reply.readArrayList(list);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(121, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isAccessibilityServicePermittedByAdmin(admin, packageName, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPermittedInputMethods(ComponentName admin, List packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeList(packageList);
                    if (this.mRemote.transact(122, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPermittedInputMethods(admin, packageList);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getPermittedInputMethods(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List list = this.mRemote;
                    if (!list.transact(123, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getPermittedInputMethods(admin);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = getClass().getClassLoader();
                    ArrayList _result = _reply.readArrayList(list);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getPermittedInputMethodsForCurrentUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List list = 124;
                    if (!this.mRemote.transact(124, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPermittedInputMethodsForCurrentUser();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = getClass().getClassLoader();
                    ArrayList _result = _reply.readArrayList(list);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(125, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isInputMethodPermittedByAdmin(admin, packageName, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageList);
                    if (this.mRemote.transact(126, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPermittedCrossProfileNotificationListeners(admin, packageList);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<String> list = this.mRemote;
                    if (!list.transact(127, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getPermittedCrossProfileNotificationListeners(admin);
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

            public boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(128, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNotificationListenerServicePermitted(packageName, userId);
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

            public Intent createAdminSupportIntent(String restriction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restriction);
                    Intent intent = 129;
                    if (!this.mRemote.transact(129, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().createAdminSupportIntent(restriction);
                            return intent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    if (this.mRemote.transact(130, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setApplicationHidden(admin, callerPackage, packageName, hidden);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(131, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isApplicationHidden(admin, callerPackage, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    UserHandle userHandle = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    if (profileOwner != null) {
                        _data.writeInt(1);
                        profileOwner.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (adminExtras != null) {
                        _data.writeInt(1);
                        adminExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (!this.mRemote.transact(132, _data, _reply, 0)) {
                        userHandle = Stub.getDefaultImpl();
                        if (userHandle != 0) {
                            userHandle = Stub.getDefaultImpl().createAndManageUser(who, name, profileOwner, adminExtras, flags);
                            return userHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userHandle = (UserHandle) UserHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        userHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(133, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeUser(who, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(134, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().switchUser(who, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(135, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().startUserInBackground(who, userHandle);
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

            public int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(136, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().stopUser(who, userHandle);
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

            public int logoutUser(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(137, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().logoutUser(who);
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

            public List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<UserHandle> list = this.mRemote;
                    if (!list.transact(138, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getSecondaryUsers(who);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(UserHandle.CREATOR);
                    List<UserHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(139, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableSystemApp(admin, callerPackage, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(140, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().enableSystemAppWithIntent(admin, callerPackage, intent);
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

            public boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(141, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().installExistingPackage(admin, callerPackage, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(accountType);
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(142, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAccountManagementDisabled(who, accountType, disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAccountTypesWithManagementDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 143;
                    if (!this.mRemote.transact(143, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getAccountTypesWithManagementDisabled();
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

            public String[] getAccountTypesWithManagementDisabledAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String[] strArr = 144;
                    if (!this.mRemote.transact(144, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getAccountTypesWithManagementDisabledAsUser(userId);
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

            public void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packages);
                    if (this.mRemote.transact(145, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLockTaskPackages(who, packages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getLockTaskPackages(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String[] strArr = this.mRemote;
                    if (!strArr.transact(146, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != null) {
                            strArr = Stub.getDefaultImpl().getLockTaskPackages(who);
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

            public boolean isLockTaskPermitted(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(147, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLockTaskPermitted(pkg);
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

            public void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (this.mRemote.transact(148, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLockTaskFeatures(who, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLockTaskFeatures(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(149, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getLockTaskFeatures(who);
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

            public void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    if (this.mRemote.transact(150, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGlobalSetting(who, setting, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    if (this.mRemote.transact(151, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemSetting(who, setting, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    if (this.mRemote.transact(152, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSecureSetting(who, setting, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setTime(ComponentName who, long millis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(millis);
                    if (this.mRemote.transact(153, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setTime(who, millis);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(timeZone);
                    if (this.mRemote.transact(154, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setTimeZone(who, timeZone);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!on) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(155, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMasterVolumeMuted(admin, on);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(156, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isMasterVolumeMuted(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(157, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyLockTaskModeChanged(isEnabled, pkg, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (!uninstallBlocked) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(158, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUninstallBlocked(admin, callerPackage, packageName, uninstallBlocked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(159, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isUninstallBlocked(admin, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(160, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCrossProfileCallerIdDisabled(who, disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
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
                    _result = Stub.getDefaultImpl().getCrossProfileCallerIdDisabled(who);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(162, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getCrossProfileCallerIdDisabledForUser(userId);
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

            public void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(163, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCrossProfileContactsSearchDisabled(who, disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(164, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getCrossProfileContactsSearchDisabled(who);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(165, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getCrossProfileContactsSearchDisabledForUser(userId);
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

            public void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
                Throwable th;
                long j;
                Intent intent = originalIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(lookupKey);
                        try {
                            _data.writeLong(contactId);
                            _data.writeInt(isContactIdIgnored ? 1 : 0);
                            _data.writeLong(directoryId);
                            if (intent != null) {
                                _data.writeInt(1);
                                intent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(166, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().startManagedQuickContact(lookupKey, contactId, isContactIdIgnored, directoryId, originalIntent);
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
                        j = contactId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str = lookupKey;
                    j = contactId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(167, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBluetoothContactSharingDisabled(who, disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(168, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getBluetoothContactSharingDisabled(who);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(169, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getBluetoothContactSharingDisabledForUser(userId);
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

            public void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (agent != null) {
                        _data.writeInt(1);
                        agent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(170, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTrustAgentConfiguration(admin, agent, args, parent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    List<PersistableBundle> list = 0;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (agent != null) {
                        _data.writeInt(1);
                        agent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(171, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getTrustAgentConfiguration(admin, agent, userId, parent);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PersistableBundle.CREATOR);
                    List<PersistableBundle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(172, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addCrossProfileWidgetProvider(admin, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(173, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeCrossProfileWidgetProvider(admin, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<String> list = this.mRemote;
                    if (!list.transact(174, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getCrossProfileWidgetProviders(admin);
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

            public void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!required) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(175, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAutoTimeRequired(who, required);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getAutoTimeRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(176, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getAutoTimeRequired();
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

            public void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!forceEpehemeralUsers) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(177, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForceEphemeralUsers(who, forceEpehemeralUsers);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(178, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getForceEphemeralUsers(who);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (adminReceiver != null) {
                        _data.writeInt(1);
                        adminReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(179, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isRemovingAdmin(adminReceiver, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(180, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserIcon(admin, icon);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(181, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemUpdatePolicy(who, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    SystemUpdatePolicy systemUpdatePolicy = 182;
                    if (!this.mRemote.transact(182, _data, _reply, 0)) {
                        systemUpdatePolicy = Stub.getDefaultImpl();
                        if (systemUpdatePolicy != 0) {
                            systemUpdatePolicy = Stub.getDefaultImpl().getSystemUpdatePolicy();
                            return systemUpdatePolicy;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        systemUpdatePolicy = (SystemUpdatePolicy) SystemUpdatePolicy.CREATOR.createFromParcel(_reply);
                    } else {
                        systemUpdatePolicy = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return systemUpdatePolicy;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(183, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearSystemUpdatePolicyFreezePeriodRecord();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    if (this.mRemote.transact(184, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setKeyguardDisabled(admin, disabled);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    if (this.mRemote.transact(185, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setStatusBarDisabled(who, disabled);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(186, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getDoNotAskCredentialsOnBoot();
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

            public void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(187, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPendingSystemUpdate(info);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    SystemUpdateInfo systemUpdateInfo = this.mRemote;
                    if (!systemUpdateInfo.transact(188, _data, _reply, 0)) {
                        systemUpdateInfo = Stub.getDefaultImpl();
                        if (systemUpdateInfo != null) {
                            systemUpdateInfo = Stub.getDefaultImpl().getPendingSystemUpdate(admin);
                            return systemUpdateInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        systemUpdateInfo = (SystemUpdateInfo) SystemUpdateInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        systemUpdateInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return systemUpdateInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeInt(policy);
                    if (this.mRemote.transact(189, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPermissionPolicy(admin, callerPackage, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPermissionPolicy(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(190, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getPermissionPolicy(admin);
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

            public void setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState, RemoteCallback resultReceiver) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String str2;
                ComponentName componentName = admin;
                RemoteCallback remoteCallback = resultReceiver;
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
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(packageName);
                        } catch (Throwable th2) {
                            th = th2;
                            str = permission;
                            i = grantState;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(permission);
                        } catch (Throwable th3) {
                            th = th3;
                            i = grantState;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str2 = packageName;
                        str = permission;
                        i = grantState;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(grantState);
                        if (remoteCallback != null) {
                            _data.writeInt(1);
                            remoteCallback.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(191, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().setPermissionGrantState(admin, callerPackage, packageName, permission, grantState, resultReceiver);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str3 = callerPackage;
                    str2 = packageName;
                    str = permission;
                    i = grantState;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    int i = this.mRemote;
                    if (!i.transact(192, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getPermissionGrantState(admin, callerPackage, packageName, permission);
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

            public boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(193, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isProvisioningAllowed(action, packageName);
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

            public int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    int i = 194;
                    if (!this.mRemote.transact(194, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkProvisioningPreCondition(action, packageName);
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

            public void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringList(packageList);
                    if (this.mRemote.transact(195, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setKeepUninstalledPackages(admin, callerPackage, packageList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    List<String> list = this.mRemote;
                    if (!list.transact(196, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getKeepUninstalledPackages(admin, callerPackage);
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

            public boolean isManagedProfile(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(197, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isManagedProfile(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSystemOnlyUser(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(198, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isSystemOnlyUser(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getWifiMacAddress(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(199, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getWifiMacAddress(admin);
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

            public void reboot(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(200, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reboot(admin);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(201, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShortSupportMessage(admin, message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(202, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getShortSupportMessage(admin);
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

            public void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(203, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLongSupportMessage(admin, message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(204, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getLongSupportMessage(admin);
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

            public CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(205, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getShortSupportMessageForUser(admin, userHandle);
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

            public CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(206, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getLongSupportMessageForUser(admin, userHandle);
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

            public boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(207, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSeparateProfileChallengeAllowed(userHandle);
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

            public void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(color);
                    if (this.mRemote.transact(208, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOrganizationColor(admin, color);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOrganizationColorForUser(int color, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(color);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(209, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOrganizationColorForUser(color, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOrganizationColor(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(210, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getOrganizationColor(admin);
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

            public int getOrganizationColorForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    int i = 211;
                    if (!this.mRemote.transact(211, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getOrganizationColorForUser(userHandle);
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

            public void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (title != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(title, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(212, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOrganizationName(admin, title);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(213, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getOrganizationName(admin);
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

            public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CharSequence charSequence = 214;
                    if (!this.mRemote.transact(214, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().getDeviceOwnerOrganizationName();
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

            public CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    CharSequence charSequence = 215;
                    if (!this.mRemote.transact(215, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().getOrganizationNameForUser(userHandle);
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

            public int getUserProvisioningState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 216;
                    if (!this.mRemote.transact(216, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUserProvisioningState();
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

            public void setUserProvisioningState(int state, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(217, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserProvisioningState(state, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAffiliationIds(ComponentName admin, List<String> ids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(ids);
                    if (this.mRemote.transact(218, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAffiliationIds(admin, ids);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<String> list = this.mRemote;
                    if (!list.transact(219, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getAffiliationIds(admin);
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

            public boolean isAffiliatedUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(220, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAffiliatedUser();
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

            public void setSecurityLoggingEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(221, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSecurityLoggingEnabled(admin, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSecurityLoggingEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(222, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isSecurityLoggingEnabled(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice retrieveSecurityLogs(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(223, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().retrieveSecurityLogs(admin);
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

            public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(224, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().retrievePreRebootSecurityLogs(admin);
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

            public long forceNetworkLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 225;
                    if (!this.mRemote.transact(225, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().forceNetworkLogs();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long forceSecurityLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 226;
                    if (!this.mRemote.transact(226, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().forceSecurityLogs();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUninstallInQueue(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(227, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUninstallInQueue(packageName);
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

            public void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(228, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().uninstallPackageWithActiveAdmins(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDeviceProvisioned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(229, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceProvisioned();
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

            public boolean isDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(230, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceProvisioningConfigApplied();
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

            public void setDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(231, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDeviceProvisioningConfigApplied();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceUpdateUserSetupComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(232, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceUpdateUserSetupComplete();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(233, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBackupServiceEnabled(admin, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(234, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isBackupServiceEnabled(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(235, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNetworkLoggingEnabled(admin, packageName, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNetworkLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(236, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isNetworkLoggingEnabled(admin, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, String packageName, long batchToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeLong(batchToken);
                    List<NetworkEvent> list = this.mRemote;
                    if (!list.transact(237, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().retrieveNetworkLogs(admin, packageName, batchToken);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(NetworkEvent.CREATOR);
                    List<NetworkEvent> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                ComponentName componentName = admin;
                Intent intent = service;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeStrongBinder(token);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (connection != null) {
                            iBinder = connection.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        try {
                            _data.writeInt(flags);
                        } catch (Throwable th2) {
                            th = th2;
                            i = targetUserId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = flags;
                        i = targetUserId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(targetUserId);
                        if (this.mRemote.transact(238, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().bindDeviceAdminServiceAsUser(admin, caller, token, service, connection, flags, targetUserId);
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
                    IBinder iBinder2 = token;
                    i2 = flags;
                    i = targetUserId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<UserHandle> list = this.mRemote;
                    if (!list.transact(239, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getBindDeviceAdminTargetUsers(admin);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(UserHandle.CREATOR);
                    List<UserHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEphemeralUser(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(240, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isEphemeralUser(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getLastSecurityLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 241;
                    if (!this.mRemote.transact(241, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getLastSecurityLogRetrievalTime();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getLastBugReportRequestTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 242;
                    if (!this.mRemote.transact(242, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getLastBugReportRequestTime();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getLastNetworkLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 243;
                    if (!this.mRemote.transact(243, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getLastNetworkLogRetrievalTime();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(token);
                    if (this.mRemote.transact(244, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setResetPasswordToken(admin, token);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(245, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().clearResetPasswordToken(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(246, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isResetPasswordTokenActive(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    _data.writeByteArray(token);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(247, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().resetPasswordWithToken(admin, password, token, flags);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isCurrentInputMethodSetByOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(248, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCurrentInputMethodSetByOwner();
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

            public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    StringParceledListSlice stringParceledListSlice = this.mRemote;
                    if (!stringParceledListSlice.transact(249, _data, _reply, 0)) {
                        stringParceledListSlice = Stub.getDefaultImpl();
                        if (stringParceledListSlice != null) {
                            stringParceledListSlice = Stub.getDefaultImpl().getOwnerInstalledCaCerts(user);
                            return stringParceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        stringParceledListSlice = (StringParceledListSlice) StringParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        stringParceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return stringParceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(250, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearApplicationUserData(admin, packageName, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(251, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLogoutEnabled(admin, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isLogoutEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(252, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLogoutEnabled();
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

            public List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeString(provisioningAction);
                    List<String> list = this.mRemote;
                    if (!list.transact(253, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getDisallowedSystemApps(admin, userId, provisioningAction);
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

            public void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(254, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().transferOwnership(admin, target, bundle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PersistableBundle getTransferOwnershipBundle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PersistableBundle persistableBundle = 255;
                    if (!this.mRemote.transact(255, _data, _reply, 0)) {
                        persistableBundle = Stub.getDefaultImpl();
                        if (persistableBundle != 0) {
                            persistableBundle = Stub.getDefaultImpl().getTransferOwnershipBundle();
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

            public void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (startUserSessionMessage != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(startUserSessionMessage, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(256, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStartUserSessionMessage(admin, startUserSessionMessage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (endUserSessionMessage != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(endUserSessionMessage, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(257, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setEndUserSessionMessage(admin, endUserSessionMessage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(258, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getStartUserSessionMessage(admin);
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

            public CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    CharSequence charSequence = this.mRemote;
                    if (!charSequence.transact(259, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != null) {
                            charSequence = Stub.getDefaultImpl().getEndUserSessionMessage(admin);
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

            public List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageNames);
                    List<String> list = this.mRemote;
                    if (!list.transact(260, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().setMeteredDataDisabledPackages(admin, packageNames);
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

            public List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<String> list = this.mRemote;
                    if (!list.transact(261, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getMeteredDataDisabledPackages(admin);
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

            public int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (apnSetting != null) {
                        _data.writeInt(1);
                        apnSetting.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(262, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addOverrideApn(admin, apnSetting);
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

            public boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(apnId);
                    if (apnSetting != null) {
                        _data.writeInt(1);
                        apnSetting.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(263, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().updateOverrideApn(admin, apnId, apnSetting);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(apnId);
                    if (this.mRemote.transact(264, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeOverrideApn(admin, apnId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<ApnSetting> list = this.mRemote;
                    if (!list.transact(265, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getOverrideApns(admin);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ApnSetting.CREATOR);
                    List<ApnSetting> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(266, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOverrideApnsEnabled(admin, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(267, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isOverrideApnEnabled(admin);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(268, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isMeteredDataDisabledPackageForUser(admin, packageName, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setGlobalPrivateDns(ComponentName admin, int mode, String privateDnsHost) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeString(privateDnsHost);
                    int i = this.mRemote;
                    if (!i.transact(269, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().setGlobalPrivateDns(admin, mode, privateDnsHost);
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

            public int getGlobalPrivateDnsMode(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(270, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getGlobalPrivateDnsMode(admin);
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

            public String getGlobalPrivateDnsHost(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(271, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getGlobalPrivateDnsHost(admin);
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

            public void grantDeviceIdsAccessToProfileOwner(ComponentName who, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(272, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantDeviceIdsAccessToProfileOwner(who, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void installUpdateFromFile(ComponentName admin, ParcelFileDescriptor updateFileDescriptor, StartInstallingUpdateCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (updateFileDescriptor != null) {
                        _data.writeInt(1);
                        updateFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(273, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().installUpdateFromFile(admin, updateFileDescriptor, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCrossProfileCalendarPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageNames);
                    if (this.mRemote.transact(274, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCrossProfileCalendarPackages(admin, packageNames);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getCrossProfileCalendarPackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<String> list = this.mRemote;
                    if (!list.transact(275, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getCrossProfileCalendarPackages(admin);
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

            public boolean isPackageAllowedToAccessCalendarForUser(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(276, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPackageAllowedToAccessCalendarForUser(packageName, userHandle);
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

            public List<String> getCrossProfileCalendarPackagesForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    List<String> list = 277;
                    if (!this.mRemote.transact(277, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getCrossProfileCalendarPackagesForUser(userHandle);
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

            public boolean isManagedKiosk() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(278, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isManagedKiosk();
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

            public boolean isUnattendedManagedKiosk() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(279, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUnattendedManagedKiosk();
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

            public boolean startViewCalendarEventInManagedProfile(String packageName, long eventId, long start, long end, boolean allDay, int flags) throws RemoteException {
                Throwable th;
                long j;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        j = eventId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(eventId);
                        _data.writeLong(start);
                        _data.writeLong(end);
                        boolean _result = true;
                        _data.writeInt(allDay ? 1 : 0);
                        _data.writeInt(flags);
                        if (this.mRemote.transact(280, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().startViewCalendarEventInManagedProfile(packageName, eventId, start, end, allDay, flags);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = packageName;
                    j = eventId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDevicePolicyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDevicePolicyManager)) {
                return new Proxy(obj);
            }
            return (IDevicePolicyManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setPasswordQuality";
                case 2:
                    return "getPasswordQuality";
                case 3:
                    return "setPasswordMinimumLength";
                case 4:
                    return "getPasswordMinimumLength";
                case 5:
                    return "setPasswordMinimumUpperCase";
                case 6:
                    return "getPasswordMinimumUpperCase";
                case 7:
                    return "setPasswordMinimumLowerCase";
                case 8:
                    return "getPasswordMinimumLowerCase";
                case 9:
                    return "setPasswordMinimumLetters";
                case 10:
                    return "getPasswordMinimumLetters";
                case 11:
                    return "setPasswordMinimumNumeric";
                case 12:
                    return "getPasswordMinimumNumeric";
                case 13:
                    return "setPasswordMinimumSymbols";
                case 14:
                    return "getPasswordMinimumSymbols";
                case 15:
                    return "setPasswordMinimumNonLetter";
                case 16:
                    return "getPasswordMinimumNonLetter";
                case 17:
                    return "setPasswordHistoryLength";
                case 18:
                    return "getPasswordHistoryLength";
                case 19:
                    return "setPasswordExpirationTimeout";
                case 20:
                    return "getPasswordExpirationTimeout";
                case 21:
                    return "getPasswordExpiration";
                case 22:
                    return "isActivePasswordSufficient";
                case 23:
                    return "isProfileActivePasswordSufficientForParent";
                case 24:
                    return "getPasswordComplexity";
                case 25:
                    return "isUsingUnifiedPassword";
                case 26:
                    return "getCurrentFailedPasswordAttempts";
                case 27:
                    return "getProfileWithMinimumFailedPasswordsForWipe";
                case 28:
                    return "setMaximumFailedPasswordsForWipe";
                case 29:
                    return "getMaximumFailedPasswordsForWipe";
                case 30:
                    return "resetPassword";
                case 31:
                    return "setMaximumTimeToLock";
                case 32:
                    return "getMaximumTimeToLock";
                case 33:
                    return "setRequiredStrongAuthTimeout";
                case 34:
                    return "getRequiredStrongAuthTimeout";
                case 35:
                    return "lockNow";
                case 36:
                    return "wipeDataWithReason";
                case 37:
                    return "setGlobalProxy";
                case 38:
                    return "getGlobalProxyAdmin";
                case 39:
                    return "setRecommendedGlobalProxy";
                case 40:
                    return "setStorageEncryption";
                case 41:
                    return "getStorageEncryption";
                case 42:
                    return "getStorageEncryptionStatus";
                case 43:
                    return "requestBugreport";
                case 44:
                    return "setCameraDisabled";
                case 45:
                    return "getCameraDisabled";
                case 46:
                    return "setScreenCaptureDisabled";
                case 47:
                    return "getScreenCaptureDisabled";
                case 48:
                    return "setKeyguardDisabledFeatures";
                case 49:
                    return "getKeyguardDisabledFeatures";
                case 50:
                    return "setActiveAdmin";
                case 51:
                    return "isAdminActive";
                case 52:
                    return "getActiveAdmins";
                case 53:
                    return "packageHasActiveAdmins";
                case 54:
                    return "getRemoveWarning";
                case 55:
                    return "removeActiveAdmin";
                case 56:
                    return "forceRemoveActiveAdmin";
                case 57:
                    return "hasGrantedPolicy";
                case 58:
                    return "setActivePasswordState";
                case 59:
                    return "reportPasswordChanged";
                case 60:
                    return "reportFailedPasswordAttempt";
                case 61:
                    return "reportSuccessfulPasswordAttempt";
                case 62:
                    return "reportFailedBiometricAttempt";
                case 63:
                    return "reportSuccessfulBiometricAttempt";
                case 64:
                    return "reportKeyguardDismissed";
                case 65:
                    return "reportKeyguardSecured";
                case 66:
                    return "setDeviceOwner";
                case 67:
                    return "getDeviceOwnerComponent";
                case 68:
                    return "hasDeviceOwner";
                case 69:
                    return "getDeviceOwnerName";
                case 70:
                    return "clearDeviceOwner";
                case 71:
                    return "getDeviceOwnerUserId";
                case 72:
                    return "setProfileOwner";
                case 73:
                    return "getProfileOwnerAsUser";
                case 74:
                    return "getProfileOwner";
                case 75:
                    return "getProfileOwnerName";
                case 76:
                    return "setProfileEnabled";
                case 77:
                    return "setProfileName";
                case 78:
                    return "clearProfileOwner";
                case 79:
                    return "hasUserSetupCompleted";
                case 80:
                    return "checkDeviceIdentifierAccess";
                case 81:
                    return "setDeviceOwnerLockScreenInfo";
                case 82:
                    return "getDeviceOwnerLockScreenInfo";
                case 83:
                    return "setPackagesSuspended";
                case 84:
                    return "isPackageSuspended";
                case 85:
                    return "installCaCert";
                case 86:
                    return "uninstallCaCerts";
                case 87:
                    return "enforceCanManageCaCerts";
                case 88:
                    return "approveCaCert";
                case 89:
                    return "isCaCertApproved";
                case 90:
                    return "installKeyPair";
                case 91:
                    return "removeKeyPair";
                case 92:
                    return "generateKeyPair";
                case 93:
                    return "setKeyPairCertificate";
                case 94:
                    return "choosePrivateKeyAlias";
                case 95:
                    return "setDelegatedScopes";
                case 96:
                    return "getDelegatedScopes";
                case 97:
                    return "getDelegatePackages";
                case 98:
                    return "setCertInstallerPackage";
                case 99:
                    return "getCertInstallerPackage";
                case 100:
                    return "setAlwaysOnVpnPackage";
                case 101:
                    return "getAlwaysOnVpnPackage";
                case 102:
                    return "isAlwaysOnVpnLockdownEnabled";
                case 103:
                    return "getAlwaysOnVpnLockdownWhitelist";
                case 104:
                    return "addPersistentPreferredActivity";
                case 105:
                    return "clearPackagePersistentPreferredActivities";
                case 106:
                    return "setDefaultSmsApplication";
                case 107:
                    return "setApplicationRestrictions";
                case 108:
                    return "getApplicationRestrictions";
                case 109:
                    return "setApplicationRestrictionsManagingPackage";
                case 110:
                    return "getApplicationRestrictionsManagingPackage";
                case 111:
                    return "isCallerApplicationRestrictionsManagingPackage";
                case 112:
                    return "setRestrictionsProvider";
                case 113:
                    return "getRestrictionsProvider";
                case 114:
                    return "setUserRestriction";
                case 115:
                    return "getUserRestrictions";
                case 116:
                    return "addCrossProfileIntentFilter";
                case 117:
                    return "clearCrossProfileIntentFilters";
                case 118:
                    return "setPermittedAccessibilityServices";
                case 119:
                    return "getPermittedAccessibilityServices";
                case 120:
                    return "getPermittedAccessibilityServicesForUser";
                case 121:
                    return "isAccessibilityServicePermittedByAdmin";
                case 122:
                    return "setPermittedInputMethods";
                case 123:
                    return "getPermittedInputMethods";
                case 124:
                    return "getPermittedInputMethodsForCurrentUser";
                case 125:
                    return "isInputMethodPermittedByAdmin";
                case 126:
                    return "setPermittedCrossProfileNotificationListeners";
                case 127:
                    return "getPermittedCrossProfileNotificationListeners";
                case 128:
                    return "isNotificationListenerServicePermitted";
                case 129:
                    return "createAdminSupportIntent";
                case 130:
                    return "setApplicationHidden";
                case 131:
                    return "isApplicationHidden";
                case 132:
                    return "createAndManageUser";
                case 133:
                    return "removeUser";
                case 134:
                    return "switchUser";
                case 135:
                    return "startUserInBackground";
                case 136:
                    return "stopUser";
                case 137:
                    return "logoutUser";
                case 138:
                    return "getSecondaryUsers";
                case 139:
                    return "enableSystemApp";
                case 140:
                    return "enableSystemAppWithIntent";
                case 141:
                    return "installExistingPackage";
                case 142:
                    return "setAccountManagementDisabled";
                case 143:
                    return "getAccountTypesWithManagementDisabled";
                case 144:
                    return "getAccountTypesWithManagementDisabledAsUser";
                case 145:
                    return "setLockTaskPackages";
                case 146:
                    return "getLockTaskPackages";
                case 147:
                    return "isLockTaskPermitted";
                case 148:
                    return "setLockTaskFeatures";
                case 149:
                    return "getLockTaskFeatures";
                case 150:
                    return "setGlobalSetting";
                case 151:
                    return "setSystemSetting";
                case 152:
                    return "setSecureSetting";
                case 153:
                    return "setTime";
                case 154:
                    return "setTimeZone";
                case 155:
                    return "setMasterVolumeMuted";
                case 156:
                    return "isMasterVolumeMuted";
                case 157:
                    return "notifyLockTaskModeChanged";
                case 158:
                    return "setUninstallBlocked";
                case 159:
                    return "isUninstallBlocked";
                case 160:
                    return "setCrossProfileCallerIdDisabled";
                case 161:
                    return "getCrossProfileCallerIdDisabled";
                case 162:
                    return "getCrossProfileCallerIdDisabledForUser";
                case 163:
                    return "setCrossProfileContactsSearchDisabled";
                case 164:
                    return "getCrossProfileContactsSearchDisabled";
                case 165:
                    return "getCrossProfileContactsSearchDisabledForUser";
                case 166:
                    return "startManagedQuickContact";
                case 167:
                    return "setBluetoothContactSharingDisabled";
                case 168:
                    return "getBluetoothContactSharingDisabled";
                case 169:
                    return "getBluetoothContactSharingDisabledForUser";
                case 170:
                    return "setTrustAgentConfiguration";
                case 171:
                    return "getTrustAgentConfiguration";
                case 172:
                    return "addCrossProfileWidgetProvider";
                case 173:
                    return "removeCrossProfileWidgetProvider";
                case 174:
                    return "getCrossProfileWidgetProviders";
                case 175:
                    return "setAutoTimeRequired";
                case 176:
                    return "getAutoTimeRequired";
                case 177:
                    return "setForceEphemeralUsers";
                case 178:
                    return "getForceEphemeralUsers";
                case 179:
                    return "isRemovingAdmin";
                case 180:
                    return "setUserIcon";
                case 181:
                    return "setSystemUpdatePolicy";
                case 182:
                    return "getSystemUpdatePolicy";
                case 183:
                    return "clearSystemUpdatePolicyFreezePeriodRecord";
                case 184:
                    return "setKeyguardDisabled";
                case 185:
                    return "setStatusBarDisabled";
                case 186:
                    return "getDoNotAskCredentialsOnBoot";
                case 187:
                    return "notifyPendingSystemUpdate";
                case 188:
                    return "getPendingSystemUpdate";
                case 189:
                    return "setPermissionPolicy";
                case 190:
                    return "getPermissionPolicy";
                case 191:
                    return "setPermissionGrantState";
                case 192:
                    return "getPermissionGrantState";
                case 193:
                    return "isProvisioningAllowed";
                case 194:
                    return "checkProvisioningPreCondition";
                case 195:
                    return "setKeepUninstalledPackages";
                case 196:
                    return "getKeepUninstalledPackages";
                case 197:
                    return "isManagedProfile";
                case 198:
                    return "isSystemOnlyUser";
                case 199:
                    return "getWifiMacAddress";
                case 200:
                    return "reboot";
                case 201:
                    return "setShortSupportMessage";
                case 202:
                    return "getShortSupportMessage";
                case 203:
                    return "setLongSupportMessage";
                case 204:
                    return "getLongSupportMessage";
                case 205:
                    return "getShortSupportMessageForUser";
                case 206:
                    return "getLongSupportMessageForUser";
                case 207:
                    return "isSeparateProfileChallengeAllowed";
                case 208:
                    return "setOrganizationColor";
                case 209:
                    return "setOrganizationColorForUser";
                case 210:
                    return "getOrganizationColor";
                case 211:
                    return "getOrganizationColorForUser";
                case 212:
                    return "setOrganizationName";
                case 213:
                    return "getOrganizationName";
                case 214:
                    return "getDeviceOwnerOrganizationName";
                case 215:
                    return "getOrganizationNameForUser";
                case 216:
                    return "getUserProvisioningState";
                case 217:
                    return "setUserProvisioningState";
                case 218:
                    return "setAffiliationIds";
                case 219:
                    return "getAffiliationIds";
                case 220:
                    return "isAffiliatedUser";
                case 221:
                    return "setSecurityLoggingEnabled";
                case 222:
                    return "isSecurityLoggingEnabled";
                case 223:
                    return "retrieveSecurityLogs";
                case 224:
                    return "retrievePreRebootSecurityLogs";
                case 225:
                    return "forceNetworkLogs";
                case 226:
                    return "forceSecurityLogs";
                case 227:
                    return "isUninstallInQueue";
                case 228:
                    return "uninstallPackageWithActiveAdmins";
                case 229:
                    return "isDeviceProvisioned";
                case 230:
                    return "isDeviceProvisioningConfigApplied";
                case 231:
                    return "setDeviceProvisioningConfigApplied";
                case 232:
                    return "forceUpdateUserSetupComplete";
                case 233:
                    return "setBackupServiceEnabled";
                case 234:
                    return "isBackupServiceEnabled";
                case 235:
                    return "setNetworkLoggingEnabled";
                case 236:
                    return "isNetworkLoggingEnabled";
                case 237:
                    return "retrieveNetworkLogs";
                case 238:
                    return "bindDeviceAdminServiceAsUser";
                case 239:
                    return "getBindDeviceAdminTargetUsers";
                case 240:
                    return "isEphemeralUser";
                case 241:
                    return "getLastSecurityLogRetrievalTime";
                case 242:
                    return "getLastBugReportRequestTime";
                case 243:
                    return "getLastNetworkLogRetrievalTime";
                case 244:
                    return "setResetPasswordToken";
                case 245:
                    return "clearResetPasswordToken";
                case 246:
                    return "isResetPasswordTokenActive";
                case 247:
                    return "resetPasswordWithToken";
                case 248:
                    return "isCurrentInputMethodSetByOwner";
                case 249:
                    return "getOwnerInstalledCaCerts";
                case 250:
                    return "clearApplicationUserData";
                case 251:
                    return "setLogoutEnabled";
                case 252:
                    return "isLogoutEnabled";
                case 253:
                    return "getDisallowedSystemApps";
                case 254:
                    return "transferOwnership";
                case 255:
                    return "getTransferOwnershipBundle";
                case 256:
                    return "setStartUserSessionMessage";
                case 257:
                    return "setEndUserSessionMessage";
                case 258:
                    return "getStartUserSessionMessage";
                case 259:
                    return "getEndUserSessionMessage";
                case 260:
                    return "setMeteredDataDisabledPackages";
                case 261:
                    return "getMeteredDataDisabledPackages";
                case 262:
                    return "addOverrideApn";
                case 263:
                    return "updateOverrideApn";
                case 264:
                    return "removeOverrideApn";
                case 265:
                    return "getOverrideApns";
                case 266:
                    return "setOverrideApnsEnabled";
                case 267:
                    return "isOverrideApnEnabled";
                case 268:
                    return "isMeteredDataDisabledPackageForUser";
                case 269:
                    return "setGlobalPrivateDns";
                case 270:
                    return "getGlobalPrivateDnsMode";
                case 271:
                    return "getGlobalPrivateDnsHost";
                case 272:
                    return "grantDeviceIdsAccessToProfileOwner";
                case 273:
                    return "installUpdateFromFile";
                case 274:
                    return "setCrossProfileCalendarPackages";
                case 275:
                    return "getCrossProfileCalendarPackages";
                case 276:
                    return "isPackageAllowedToAccessCalendarForUser";
                case 277:
                    return "getCrossProfileCalendarPackagesForUser";
                case 278:
                    return "isManagedKiosk";
                case 279:
                    return "isUnattendedManagedKiosk";
                case 280:
                    return "startViewCalendarEventInManagedProfile";
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
                boolean z = false;
                ComponentName _arg0;
                int _arg1;
                int _result;
                long _arg12;
                long _result2;
                int _arg02;
                boolean _result3;
                boolean _result4;
                int _result5;
                ComponentName _result6;
                boolean _result7;
                boolean _result8;
                String _result9;
                String _result10;
                CharSequence _arg13;
                CharSequence _result11;
                ComponentName _arg03;
                String _arg14;
                List<String> _result12;
                boolean _result13;
                List<String> _result14;
                IntentFilter _arg15;
                String _arg2;
                Bundle _arg3;
                List _result15;
                UserHandle _result16;
                UserHandle _arg16;
                List<UserHandle> _result17;
                String[] _result18;
                PersistableBundle _arg22;
                CharSequence _result19;
                ParceledListSlice _result20;
                long _result21;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordQuality(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordQuality(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumLength(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumLength(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumUpperCase(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumUpperCase(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumLowerCase(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumLowerCase(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumLetters(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumLetters(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumNumeric(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumNumeric(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumSymbols(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumSymbols(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordMinimumNonLetter(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordMinimumNonLetter(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordHistoryLength(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getPasswordHistoryLength(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg12 = data.readLong();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPasswordExpirationTimeout(_arg0, _arg12, z);
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result2 = getPasswordExpirationTimeout(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeLong(_result2);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result2 = getPasswordExpiration(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeLong(_result2);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result3 = isActivePasswordSufficient(_arg02, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result4 = isProfileActivePasswordSufficientForParent(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getPasswordComplexity();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isUsingUnifiedPassword(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result5 = getCurrentFailedPasswordAttempts(_arg02, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result5 = getProfileWithMinimumFailedPasswordsForWipe(_arg02, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setMaximumFailedPasswordsForWipe(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getMaximumFailedPasswordsForWipe(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result3 = resetPassword(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg12 = data.readLong();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setMaximumTimeToLock(_arg0, _arg12, z);
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result2 = getMaximumTimeToLock(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeLong(_result2);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg12 = data.readLong();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setRequiredStrongAuthTimeout(_arg0, _arg12, z);
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result2 = getRequiredStrongAuthTimeout(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeLong(_result2);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        lockNow(_arg02, z);
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        wipeDataWithReason(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        ComponentName _result22 = setGlobalProxy(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result22 != null) {
                            parcel2.writeInt(1);
                            _result22.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _result6 = getGlobalProxyAdmin(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 39:
                        ProxyInfo _arg17;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg17 = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        setRecommendedGlobalProxy(_arg0, _arg17);
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result5 = setStorageEncryption(_arg0, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = getStorageEncryption(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _result5 = getStorageEncryptionStatus(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = requestBugreport(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setCameraDisabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = getCameraDisabled(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setScreenCaptureDisabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = getScreenCaptureDisabled(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setKeyguardDisabledFeatures(_arg0, _arg1, z);
                        reply.writeNoException();
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result = getKeyguardDisabledFeatures(_arg0, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setActiveAdmin(_arg0, z, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = isAdminActive(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        List<ComponentName> _result23 = getActiveAdmins(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result23);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        _result3 = packageHasActiveAdmins(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 54:
                        RemoteCallback _arg18;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg18 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        getRemoveWarning(_arg0, _arg18, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        removeActiveAdmin(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        forceRemoveActiveAdmin(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = hasGrantedPolicy(_arg0, data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 58:
                        PasswordMetrics _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (PasswordMetrics) PasswordMetrics.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        setActivePasswordState(_arg04, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        reportPasswordChanged(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        reportFailedPasswordAttempt(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        reportSuccessfulPasswordAttempt(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        reportFailedBiometricAttempt(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        reportSuccessfulBiometricAttempt(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        reportKeyguardDismissed(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        reportKeyguardSecured(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setDeviceOwner(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _result6 = getDeviceOwnerComponent(data.readInt() != 0);
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        _result8 = hasDeviceOwner();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        _result9 = getDeviceOwnerName();
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        clearDeviceOwner(data.readString());
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getDeviceOwnerUserId();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setProfileOwner(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _result6 = getProfileOwnerAsUser(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        _result6 = getProfileOwner(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _result10 = getProfileOwnerName(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setProfileEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setProfileName(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        clearProfileOwner(_arg0);
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        _result8 = hasUserSetupCompleted();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        _result7 = checkDeviceIdentifierAccess(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setDeviceOwnerLockScreenInfo(_arg0, _arg13);
                        reply.writeNoException();
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        _result11 = getDeviceOwnerLockScreenInfo();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result11, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        String[] _arg23 = data.createStringArray();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        String[] _result24 = setPackagesSuspended(_arg0, _result10, _arg23, z);
                        reply.writeNoException();
                        parcel2.writeStringArray(_result24);
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = isPackageSuspended(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = installCaCert(_arg0, data.readString(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        uninstallCaCerts(_arg0, data.readString(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        enforceCanManageCaCerts(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        _result9 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result7 = approveCaCert(_result9, _arg1, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        _result3 = isCaCertApproved(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 90:
                        return onTransact$installKeyPair$(parcel, parcel2);
                    case 91:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = removeKeyPair(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 92:
                        ParcelableKeyGenParameterSpec _arg32;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg14 = data.readString();
                        String _arg24 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (ParcelableKeyGenParameterSpec) ParcelableKeyGenParameterSpec.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        int _arg4 = data.readInt();
                        KeymasterCertificateChain _arg5 = new KeymasterCertificateChain();
                        KeymasterCertificateChain _arg52 = _arg5;
                        _result8 = generateKeyPair(_arg03, _arg14, _arg24, _arg32, _arg4, _arg5);
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        parcel2.writeInt(1);
                        _arg52.writeToParcel(parcel2, 1);
                        return true;
                    case 93:
                        return onTransact$setKeyPairCertificate$(parcel, parcel2);
                    case 94:
                        Uri _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg19 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        choosePrivateKeyAlias(_arg02, _arg19, data.readString(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setDelegatedScopes(_arg0, data.readString(), data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 96:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result12 = getDelegatedScopes(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result12);
                        return true;
                    case 97:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result12 = getDelegatePackages(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result12);
                        return true;
                    case 98:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setCertInstallerPackage(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 99:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = getCertInstallerPackage(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 100:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result13 = setAlwaysOnVpnPackage(_arg0, _result10, z, data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeInt(_result13);
                        return true;
                    case 101:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = getAlwaysOnVpnPackage(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 102:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isAlwaysOnVpnLockdownEnabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 103:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result14 = getAlwaysOnVpnLockdownWhitelist(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 104:
                        ComponentName _arg25;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg15 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg25 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        addPersistentPreferredActivity(_arg0, _arg15, _arg25);
                        reply.writeNoException();
                        return true;
                    case 105:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        clearPackagePersistentPreferredActivities(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 106:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setDefaultSmsApplication(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 107:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        setApplicationRestrictions(_arg0, _result10, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 108:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg3 = getApplicationRestrictions(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        if (_arg3 != null) {
                            parcel2.writeInt(1);
                            _arg3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 109:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = setApplicationRestrictionsManagingPackage(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 110:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = getApplicationRestrictionsManagingPackage(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 111:
                        parcel.enforceInterface(descriptor);
                        _result4 = isCallerApplicationRestrictionsManagingPackage(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 112:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _result6 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _result6 = null;
                        }
                        setRestrictionsProvider(_arg0, _result6);
                        reply.writeNoException();
                        return true;
                    case 113:
                        parcel.enforceInterface(descriptor);
                        _result6 = getRestrictionsProvider(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 114:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setUserRestriction(_arg0, _result10, z);
                        reply.writeNoException();
                        return true;
                    case 115:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        Bundle _result25 = getUserRestrictions(_arg0);
                        reply.writeNoException();
                        if (_result25 != null) {
                            parcel2.writeInt(1);
                            _result25.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 116:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg15 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        addCrossProfileIntentFilter(_arg0, _arg15, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 117:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        clearCrossProfileIntentFilters(_arg0);
                        reply.writeNoException();
                        return true;
                    case 118:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setPermittedAccessibilityServices(_arg0, parcel.readArrayList(getClass().getClassLoader()));
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 119:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result15 = getPermittedAccessibilityServices(_arg0);
                        reply.writeNoException();
                        parcel2.writeList(_result15);
                        return true;
                    case 120:
                        parcel.enforceInterface(descriptor);
                        _result15 = getPermittedAccessibilityServicesForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeList(_result15);
                        return true;
                    case 121:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = isAccessibilityServicePermittedByAdmin(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 122:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setPermittedInputMethods(_arg0, parcel.readArrayList(getClass().getClassLoader()));
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 123:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result15 = getPermittedInputMethods(_arg0);
                        reply.writeNoException();
                        parcel2.writeList(_result15);
                        return true;
                    case 124:
                        parcel.enforceInterface(descriptor);
                        List _result26 = getPermittedInputMethodsForCurrentUser();
                        reply.writeNoException();
                        parcel2.writeList(_result26);
                        return true;
                    case 125:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = isInputMethodPermittedByAdmin(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 126:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = setPermittedCrossProfileNotificationListeners(_arg0, data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 127:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result14 = getPermittedCrossProfileNotificationListeners(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 128:
                        parcel.enforceInterface(descriptor);
                        _result3 = isNotificationListenerServicePermitted(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 129:
                        parcel.enforceInterface(descriptor);
                        Intent _result27 = createAdminSupportIntent(data.readString());
                        reply.writeNoException();
                        if (_result27 != null) {
                            parcel2.writeInt(1);
                            _result27.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 130:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result13 = setApplicationHidden(_arg0, _result10, _arg2, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result13);
                        return true;
                    case 131:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = isApplicationHidden(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 132:
                        ComponentName _arg26;
                        PersistableBundle _arg33;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg26 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg26 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg33 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg33 = null;
                        }
                        _result16 = createAndManageUser(_arg03, _arg14, _arg26, _arg33, data.readInt());
                        reply.writeNoException();
                        if (_result16 != null) {
                            parcel2.writeInt(1);
                            _result16.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 133:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        _result3 = removeUser(_arg0, _arg16);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 134:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        _result3 = switchUser(_arg0, _arg16);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 135:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        _result5 = startUserInBackground(_arg0, _arg16);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 136:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        _result5 = stopUser(_arg0, _arg16);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 137:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = logoutUser(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 138:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result17 = getSecondaryUsers(_arg0);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result17);
                        return true;
                    case 139:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        enableSystemApp(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 140:
                        Intent _arg27;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            _arg27 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg27 = null;
                        }
                        _result = enableSystemAppWithIntent(_arg0, _result10, _arg27);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 141:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = installExistingPackage(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 142:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAccountManagementDisabled(_arg0, _result10, z);
                        reply.writeNoException();
                        return true;
                    case 143:
                        parcel.enforceInterface(descriptor);
                        String[] _result28 = getAccountTypesWithManagementDisabled();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result28);
                        return true;
                    case 144:
                        parcel.enforceInterface(descriptor);
                        _result18 = getAccountTypesWithManagementDisabledAsUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result18);
                        return true;
                    case 145:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setLockTaskPackages(_arg0, data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 146:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result18 = getLockTaskPackages(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringArray(_result18);
                        return true;
                    case 147:
                        parcel.enforceInterface(descriptor);
                        _result4 = isLockTaskPermitted(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 148:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setLockTaskFeatures(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 149:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = getLockTaskFeatures(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 150:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setGlobalSetting(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 151:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setSystemSetting(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 152:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setSecureSetting(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 153:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setTime(_arg0, data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 154:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = setTimeZone(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 155:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setMasterVolumeMuted(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 156:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isMasterVolumeMuted(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 157:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        notifyLockTaskModeChanged(z, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 158:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setUninstallBlocked(_arg0, _result10, _arg2, z);
                        reply.writeNoException();
                        return true;
                    case 159:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = isUninstallBlocked(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 160:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setCrossProfileCallerIdDisabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 161:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = getCrossProfileCallerIdDisabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 162:
                        parcel.enforceInterface(descriptor);
                        _result4 = getCrossProfileCallerIdDisabledForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 163:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setCrossProfileContactsSearchDisabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 164:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = getCrossProfileContactsSearchDisabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 165:
                        parcel.enforceInterface(descriptor);
                        _result4 = getCrossProfileContactsSearchDisabledForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 166:
                        Intent _arg42;
                        parcel.enforceInterface(descriptor);
                        _arg14 = data.readString();
                        long _arg110 = data.readLong();
                        _result13 = data.readInt() != 0;
                        long _arg34 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg42 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        startManagedQuickContact(_arg14, _arg110, _result13, _arg34, _arg42);
                        reply.writeNoException();
                        return true;
                    case 167:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setBluetoothContactSharingDisabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 168:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = getBluetoothContactSharingDisabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 169:
                        parcel.enforceInterface(descriptor);
                        _result4 = getBluetoothContactSharingDisabledForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 170:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _result6 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _result6 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setTrustAgentConfiguration(_arg0, _result6, _arg22, z);
                        reply.writeNoException();
                        return true;
                    case 171:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _result6 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _result6 = null;
                        }
                        _result5 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        List<PersistableBundle> _result29 = getTrustAgentConfiguration(_arg0, _result6, _result5, z);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result29);
                        return true;
                    case 172:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = addCrossProfileWidgetProvider(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 173:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = removeCrossProfileWidgetProvider(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 174:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result14 = getCrossProfileWidgetProviders(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 175:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAutoTimeRequired(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 176:
                        parcel.enforceInterface(descriptor);
                        _result8 = getAutoTimeRequired();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 177:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setForceEphemeralUsers(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 178:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = getForceEphemeralUsers(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 179:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = isRemovingAdmin(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 180:
                        Bitmap _arg111;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg111 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg111 = null;
                        }
                        setUserIcon(_arg0, _arg111);
                        reply.writeNoException();
                        return true;
                    case 181:
                        SystemUpdatePolicy _arg112;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg112 = (SystemUpdatePolicy) SystemUpdatePolicy.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg112 = null;
                        }
                        setSystemUpdatePolicy(_arg0, _arg112);
                        reply.writeNoException();
                        return true;
                    case 182:
                        parcel.enforceInterface(descriptor);
                        SystemUpdatePolicy _result30 = getSystemUpdatePolicy();
                        reply.writeNoException();
                        if (_result30 != null) {
                            parcel2.writeInt(1);
                            _result30.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 183:
                        parcel.enforceInterface(descriptor);
                        clearSystemUpdatePolicyFreezePeriodRecord();
                        reply.writeNoException();
                        return true;
                    case 184:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result3 = setKeyguardDisabled(_arg0, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 185:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result3 = setStatusBarDisabled(_arg0, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 186:
                        parcel.enforceInterface(descriptor);
                        _result8 = getDoNotAskCredentialsOnBoot();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 187:
                        SystemUpdateInfo _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (SystemUpdateInfo) SystemUpdateInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        notifyPendingSystemUpdate(_arg05);
                        reply.writeNoException();
                        return true;
                    case 188:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        SystemUpdateInfo _result31 = getPendingSystemUpdate(_arg0);
                        reply.writeNoException();
                        if (_result31 != null) {
                            parcel2.writeInt(1);
                            _result31.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 189:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setPermissionPolicy(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 190:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = getPermissionPolicy(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 191:
                        return onTransact$setPermissionGrantState$(parcel, parcel2);
                    case 192:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        int _result32 = getPermissionGrantState(_arg0, data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result32);
                        return true;
                    case 193:
                        parcel.enforceInterface(descriptor);
                        _result3 = isProvisioningAllowed(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 194:
                        parcel.enforceInterface(descriptor);
                        _result5 = checkProvisioningPreCondition(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 195:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setKeepUninstalledPackages(_arg0, data.readString(), data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 196:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result12 = getKeepUninstalledPackages(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result12);
                        return true;
                    case 197:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isManagedProfile(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 198:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isSystemOnlyUser(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 199:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = getWifiMacAddress(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 200:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        reboot(_arg0);
                        reply.writeNoException();
                        return true;
                    case 201:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setShortSupportMessage(_arg0, _arg13);
                        reply.writeNoException();
                        return true;
                    case 202:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg13 = getShortSupportMessage(_arg0);
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_arg13, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 203:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setLongSupportMessage(_arg0, _arg13);
                        reply.writeNoException();
                        return true;
                    case 204:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg13 = getLongSupportMessage(_arg0);
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_arg13, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 205:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result19 = getShortSupportMessageForUser(_arg0, data.readInt());
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result19, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 206:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result19 = getLongSupportMessageForUser(_arg0, data.readInt());
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result19, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 207:
                        parcel.enforceInterface(descriptor);
                        _result4 = isSeparateProfileChallengeAllowed(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 208:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setOrganizationColor(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 209:
                        parcel.enforceInterface(descriptor);
                        setOrganizationColorForUser(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 210:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = getOrganizationColor(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 211:
                        parcel.enforceInterface(descriptor);
                        _arg1 = getOrganizationColorForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 212:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setOrganizationName(_arg0, _arg13);
                        reply.writeNoException();
                        return true;
                    case 213:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg13 = getOrganizationName(_arg0);
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_arg13, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 214:
                        parcel.enforceInterface(descriptor);
                        _result11 = getDeviceOwnerOrganizationName();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result11, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 215:
                        parcel.enforceInterface(descriptor);
                        _arg13 = getOrganizationNameForUser(data.readInt());
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_arg13, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 216:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getUserProvisioningState();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 217:
                        parcel.enforceInterface(descriptor);
                        setUserProvisioningState(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 218:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setAffiliationIds(_arg0, data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 219:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result14 = getAffiliationIds(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 220:
                        parcel.enforceInterface(descriptor);
                        _result8 = isAffiliatedUser();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 221:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setSecurityLoggingEnabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 222:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isSecurityLoggingEnabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 223:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result20 = retrieveSecurityLogs(_arg0);
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(1);
                            _result20.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 224:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result20 = retrievePreRebootSecurityLogs(_arg0);
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(1);
                            _result20.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 225:
                        parcel.enforceInterface(descriptor);
                        _result21 = forceNetworkLogs();
                        reply.writeNoException();
                        parcel2.writeLong(_result21);
                        return true;
                    case 226:
                        parcel.enforceInterface(descriptor);
                        _result21 = forceSecurityLogs();
                        reply.writeNoException();
                        parcel2.writeLong(_result21);
                        return true;
                    case 227:
                        parcel.enforceInterface(descriptor);
                        _result4 = isUninstallInQueue(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 228:
                        parcel.enforceInterface(descriptor);
                        uninstallPackageWithActiveAdmins(data.readString());
                        reply.writeNoException();
                        return true;
                    case 229:
                        parcel.enforceInterface(descriptor);
                        _result8 = isDeviceProvisioned();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 230:
                        parcel.enforceInterface(descriptor);
                        _result8 = isDeviceProvisioningConfigApplied();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 231:
                        parcel.enforceInterface(descriptor);
                        setDeviceProvisioningConfigApplied();
                        reply.writeNoException();
                        return true;
                    case 232:
                        parcel.enforceInterface(descriptor);
                        forceUpdateUserSetupComplete();
                        reply.writeNoException();
                        return true;
                    case 233:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setBackupServiceEnabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 234:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isBackupServiceEnabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 235:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setNetworkLoggingEnabled(_arg0, _result10, z);
                        reply.writeNoException();
                        return true;
                    case 236:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = isNetworkLoggingEnabled(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 237:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        List<NetworkEvent> _result33 = retrieveNetworkLogs(_arg0, data.readString(), data.readLong());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result33);
                        return true;
                    case 238:
                        return onTransact$bindDeviceAdminServiceAsUser$(parcel, parcel2);
                    case 239:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result17 = getBindDeviceAdminTargetUsers(_arg0);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result17);
                        return true;
                    case 240:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isEphemeralUser(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 241:
                        parcel.enforceInterface(descriptor);
                        _result21 = getLastSecurityLogRetrievalTime();
                        reply.writeNoException();
                        parcel2.writeLong(_result21);
                        return true;
                    case 242:
                        parcel.enforceInterface(descriptor);
                        _result21 = getLastBugReportRequestTime();
                        reply.writeNoException();
                        parcel2.writeLong(_result21);
                        return true;
                    case 243:
                        parcel.enforceInterface(descriptor);
                        _result21 = getLastNetworkLogRetrievalTime();
                        reply.writeNoException();
                        parcel2.writeLong(_result21);
                        return true;
                    case 244:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = setResetPasswordToken(_arg0, data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 245:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = clearResetPasswordToken(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 246:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isResetPasswordTokenActive(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 247:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result13 = resetPasswordWithToken(_arg0, data.readString(), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result13);
                        return true;
                    case 248:
                        parcel.enforceInterface(descriptor);
                        _result8 = isCurrentInputMethodSetByOwner();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 249:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result16 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _result16 = null;
                        }
                        StringParceledListSlice _result34 = getOwnerInstalledCaCerts(_result16);
                        reply.writeNoException();
                        if (_result34 != null) {
                            parcel2.writeInt(1);
                            _result34.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 250:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        clearApplicationUserData(_arg0, data.readString(), android.content.pm.IPackageDataObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 251:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setLogoutEnabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 252:
                        parcel.enforceInterface(descriptor);
                        _result8 = isLogoutEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 253:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        List<String> _result35 = getDisallowedSystemApps(_arg0, data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result35);
                        return true;
                    case 254:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _result6 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _result6 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        transferOwnership(_arg0, _result6, _arg22);
                        reply.writeNoException();
                        return true;
                    case 255:
                        parcel.enforceInterface(descriptor);
                        PersistableBundle _result36 = getTransferOwnershipBundle();
                        reply.writeNoException();
                        if (_result36 != null) {
                            parcel2.writeInt(1);
                            _result36.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 256:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setStartUserSessionMessage(_arg0, _arg13);
                        reply.writeNoException();
                        return true;
                    case 257:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setEndUserSessionMessage(_arg0, _arg13);
                        reply.writeNoException();
                        return true;
                    case 258:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg13 = getStartUserSessionMessage(_arg0);
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_arg13, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 259:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg13 = getEndUserSessionMessage(_arg0);
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_arg13, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 260:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result12 = setMeteredDataDisabledPackages(_arg0, data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeStringList(_result12);
                        return true;
                    case 261:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result14 = getMeteredDataDisabledPackages(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 262:
                        ApnSetting _arg113;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg113 = (ApnSetting) ApnSetting.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg113 = null;
                        }
                        _result5 = addOverrideApn(_arg0, _arg113);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 263:
                        ApnSetting _arg28;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg28 = (ApnSetting) ApnSetting.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg28 = null;
                        }
                        _result7 = updateOverrideApn(_arg0, _arg1, _arg28);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 264:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = removeOverrideApn(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 265:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        List<ApnSetting> _result37 = getOverrideApns(_arg0);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result37);
                        return true;
                    case 266:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setOverrideApnsEnabled(_arg0, z);
                        reply.writeNoException();
                        return true;
                    case 267:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = isOverrideApnEnabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 268:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = isMeteredDataDisabledPackageForUser(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 269:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = setGlobalPrivateDns(_arg0, data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 270:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = getGlobalPrivateDnsMode(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 271:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result10 = getGlobalPrivateDnsHost(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 272:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        grantDeviceIdsAccessToProfileOwner(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 273:
                        ParcelFileDescriptor _arg114;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg114 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg114 = null;
                        }
                        installUpdateFromFile(_arg0, _arg114, android.app.admin.StartInstallingUpdateCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 274:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setCrossProfileCalendarPackages(_arg0, data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 275:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result14 = getCrossProfileCalendarPackages(_arg0);
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 276:
                        parcel.enforceInterface(descriptor);
                        _result3 = isPackageAllowedToAccessCalendarForUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 277:
                        parcel.enforceInterface(descriptor);
                        _result14 = getCrossProfileCalendarPackagesForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 278:
                        parcel.enforceInterface(descriptor);
                        _result8 = isManagedKiosk();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 279:
                        parcel.enforceInterface(descriptor);
                        _result8 = isUnattendedManagedKiosk();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 280:
                        return onTransact$startViewCalendarEventInManagedProfile$(parcel, parcel2);
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        private boolean onTransact$installKeyPair$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            Parcel parcel = data;
            parcel.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
            } else {
                _arg0 = null;
            }
            boolean _result = installKeyPair(_arg0, data.readString(), data.createByteArray(), data.createByteArray(), data.createByteArray(), data.readString(), data.readInt() != 0, data.readInt() != 0);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$setKeyPairCertificate$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            boolean _result = setKeyPairCertificate(_arg0, data.readString(), data.readString(), data.createByteArray(), data.createByteArray(), data.readInt() != 0);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$setPermissionGrantState$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            RemoteCallback _arg5;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            String _arg3 = data.readString();
            int _arg4 = data.readInt();
            if (data.readInt() != 0) {
                _arg5 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(data);
            } else {
                _arg5 = null;
            }
            setPermissionGrantState(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$bindDeviceAdminServiceAsUser$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            Intent _arg3;
            Parcel parcel = data;
            parcel.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
            } else {
                _arg0 = null;
            }
            IApplicationThread _arg1 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
            IBinder _arg2 = data.readStrongBinder();
            if (data.readInt() != 0) {
                _arg3 = (Intent) Intent.CREATOR.createFromParcel(parcel);
            } else {
                _arg3 = null;
            }
            boolean _result = bindDeviceAdminServiceAsUser(_arg0, _arg1, _arg2, _arg3, android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$startViewCalendarEventInManagedProfile$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            boolean _result = startViewCalendarEventInManagedProfile(data.readString(), data.readLong(), data.readLong(), data.readLong(), data.readInt() != 0, data.readInt());
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        public static boolean setDefaultImpl(IDevicePolicyManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDevicePolicyManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addCrossProfileIntentFilter(ComponentName componentName, IntentFilter intentFilter, int i) throws RemoteException;

    boolean addCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    int addOverrideApn(ComponentName componentName, ApnSetting apnSetting) throws RemoteException;

    void addPersistentPreferredActivity(ComponentName componentName, IntentFilter intentFilter, ComponentName componentName2) throws RemoteException;

    boolean approveCaCert(String str, int i, boolean z) throws RemoteException;

    boolean bindDeviceAdminServiceAsUser(ComponentName componentName, IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    boolean checkDeviceIdentifierAccess(String str, int i, int i2) throws RemoteException;

    int checkProvisioningPreCondition(String str, String str2) throws RemoteException;

    void choosePrivateKeyAlias(int i, Uri uri, String str, IBinder iBinder) throws RemoteException;

    void clearApplicationUserData(ComponentName componentName, String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void clearCrossProfileIntentFilters(ComponentName componentName) throws RemoteException;

    void clearDeviceOwner(String str) throws RemoteException;

    void clearPackagePersistentPreferredActivities(ComponentName componentName, String str) throws RemoteException;

    void clearProfileOwner(ComponentName componentName) throws RemoteException;

    boolean clearResetPasswordToken(ComponentName componentName) throws RemoteException;

    void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException;

    Intent createAdminSupportIntent(String str) throws RemoteException;

    UserHandle createAndManageUser(ComponentName componentName, String str, ComponentName componentName2, PersistableBundle persistableBundle, int i) throws RemoteException;

    void enableSystemApp(ComponentName componentName, String str, String str2) throws RemoteException;

    int enableSystemAppWithIntent(ComponentName componentName, String str, Intent intent) throws RemoteException;

    void enforceCanManageCaCerts(ComponentName componentName, String str) throws RemoteException;

    long forceNetworkLogs() throws RemoteException;

    void forceRemoveActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    long forceSecurityLogs() throws RemoteException;

    void forceUpdateUserSetupComplete() throws RemoteException;

    boolean generateKeyPair(ComponentName componentName, String str, String str2, ParcelableKeyGenParameterSpec parcelableKeyGenParameterSpec, int i, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    String[] getAccountTypesWithManagementDisabled() throws RemoteException;

    String[] getAccountTypesWithManagementDisabledAsUser(int i) throws RemoteException;

    List<ComponentName> getActiveAdmins(int i) throws RemoteException;

    List<String> getAffiliationIds(ComponentName componentName) throws RemoteException;

    List<String> getAlwaysOnVpnLockdownWhitelist(ComponentName componentName) throws RemoteException;

    String getAlwaysOnVpnPackage(ComponentName componentName) throws RemoteException;

    Bundle getApplicationRestrictions(ComponentName componentName, String str, String str2) throws RemoteException;

    String getApplicationRestrictionsManagingPackage(ComponentName componentName) throws RemoteException;

    boolean getAutoTimeRequired() throws RemoteException;

    List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName componentName) throws RemoteException;

    boolean getBluetoothContactSharingDisabled(ComponentName componentName) throws RemoteException;

    boolean getBluetoothContactSharingDisabledForUser(int i) throws RemoteException;

    boolean getCameraDisabled(ComponentName componentName, int i) throws RemoteException;

    String getCertInstallerPackage(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileCalendarPackages(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileCalendarPackagesForUser(int i) throws RemoteException;

    boolean getCrossProfileCallerIdDisabled(ComponentName componentName) throws RemoteException;

    boolean getCrossProfileCallerIdDisabledForUser(int i) throws RemoteException;

    boolean getCrossProfileContactsSearchDisabled(ComponentName componentName) throws RemoteException;

    boolean getCrossProfileContactsSearchDisabledForUser(int i) throws RemoteException;

    List<String> getCrossProfileWidgetProviders(ComponentName componentName) throws RemoteException;

    int getCurrentFailedPasswordAttempts(int i, boolean z) throws RemoteException;

    List<String> getDelegatePackages(ComponentName componentName, String str) throws RemoteException;

    List<String> getDelegatedScopes(ComponentName componentName, String str) throws RemoteException;

    ComponentName getDeviceOwnerComponent(boolean z) throws RemoteException;

    CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException;

    String getDeviceOwnerName() throws RemoteException;

    CharSequence getDeviceOwnerOrganizationName() throws RemoteException;

    int getDeviceOwnerUserId() throws RemoteException;

    List<String> getDisallowedSystemApps(ComponentName componentName, int i, String str) throws RemoteException;

    boolean getDoNotAskCredentialsOnBoot() throws RemoteException;

    CharSequence getEndUserSessionMessage(ComponentName componentName) throws RemoteException;

    boolean getForceEphemeralUsers(ComponentName componentName) throws RemoteException;

    String getGlobalPrivateDnsHost(ComponentName componentName) throws RemoteException;

    int getGlobalPrivateDnsMode(ComponentName componentName) throws RemoteException;

    ComponentName getGlobalProxyAdmin(int i) throws RemoteException;

    List<String> getKeepUninstalledPackages(ComponentName componentName, String str) throws RemoteException;

    int getKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getLastBugReportRequestTime() throws RemoteException;

    long getLastNetworkLogRetrievalTime() throws RemoteException;

    long getLastSecurityLogRetrievalTime() throws RemoteException;

    int getLockTaskFeatures(ComponentName componentName) throws RemoteException;

    String[] getLockTaskPackages(ComponentName componentName) throws RemoteException;

    CharSequence getLongSupportMessage(ComponentName componentName) throws RemoteException;

    CharSequence getLongSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    int getMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getMaximumTimeToLock(ComponentName componentName, int i, boolean z) throws RemoteException;

    List<String> getMeteredDataDisabledPackages(ComponentName componentName) throws RemoteException;

    int getOrganizationColor(ComponentName componentName) throws RemoteException;

    int getOrganizationColorForUser(int i) throws RemoteException;

    CharSequence getOrganizationName(ComponentName componentName) throws RemoteException;

    CharSequence getOrganizationNameForUser(int i) throws RemoteException;

    List<ApnSetting> getOverrideApns(ComponentName componentName) throws RemoteException;

    StringParceledListSlice getOwnerInstalledCaCerts(UserHandle userHandle) throws RemoteException;

    int getPasswordComplexity() throws RemoteException;

    long getPasswordExpiration(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getPasswordExpirationTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    SystemUpdateInfo getPendingSystemUpdate(ComponentName componentName) throws RemoteException;

    int getPermissionGrantState(ComponentName componentName, String str, String str2, String str3) throws RemoteException;

    int getPermissionPolicy(ComponentName componentName) throws RemoteException;

    List getPermittedAccessibilityServices(ComponentName componentName) throws RemoteException;

    List getPermittedAccessibilityServicesForUser(int i) throws RemoteException;

    List<String> getPermittedCrossProfileNotificationListeners(ComponentName componentName) throws RemoteException;

    List getPermittedInputMethods(ComponentName componentName) throws RemoteException;

    List getPermittedInputMethodsForCurrentUser() throws RemoteException;

    ComponentName getProfileOwner(int i) throws RemoteException;

    ComponentName getProfileOwnerAsUser(int i) throws RemoteException;

    String getProfileOwnerName(int i) throws RemoteException;

    int getProfileWithMinimumFailedPasswordsForWipe(int i, boolean z) throws RemoteException;

    void getRemoveWarning(ComponentName componentName, RemoteCallback remoteCallback, int i) throws RemoteException;

    long getRequiredStrongAuthTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    ComponentName getRestrictionsProvider(int i) throws RemoteException;

    boolean getScreenCaptureDisabled(ComponentName componentName, int i) throws RemoteException;

    List<UserHandle> getSecondaryUsers(ComponentName componentName) throws RemoteException;

    CharSequence getShortSupportMessage(ComponentName componentName) throws RemoteException;

    CharSequence getShortSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    CharSequence getStartUserSessionMessage(ComponentName componentName) throws RemoteException;

    boolean getStorageEncryption(ComponentName componentName, int i) throws RemoteException;

    int getStorageEncryptionStatus(String str, int i) throws RemoteException;

    SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException;

    PersistableBundle getTransferOwnershipBundle() throws RemoteException;

    List<PersistableBundle> getTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, int i, boolean z) throws RemoteException;

    int getUserProvisioningState() throws RemoteException;

    Bundle getUserRestrictions(ComponentName componentName) throws RemoteException;

    String getWifiMacAddress(ComponentName componentName) throws RemoteException;

    void grantDeviceIdsAccessToProfileOwner(ComponentName componentName, int i) throws RemoteException;

    boolean hasDeviceOwner() throws RemoteException;

    boolean hasGrantedPolicy(ComponentName componentName, int i, int i2) throws RemoteException;

    boolean hasUserSetupCompleted() throws RemoteException;

    boolean installCaCert(ComponentName componentName, String str, byte[] bArr) throws RemoteException;

    boolean installExistingPackage(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean installKeyPair(ComponentName componentName, String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, boolean z, boolean z2) throws RemoteException;

    void installUpdateFromFile(ComponentName componentName, ParcelFileDescriptor parcelFileDescriptor, StartInstallingUpdateCallback startInstallingUpdateCallback) throws RemoteException;

    boolean isAccessibilityServicePermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isActivePasswordSufficient(int i, boolean z) throws RemoteException;

    boolean isAdminActive(ComponentName componentName, int i) throws RemoteException;

    boolean isAffiliatedUser() throws RemoteException;

    boolean isAlwaysOnVpnLockdownEnabled(ComponentName componentName) throws RemoteException;

    boolean isApplicationHidden(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean isBackupServiceEnabled(ComponentName componentName) throws RemoteException;

    boolean isCaCertApproved(String str, int i) throws RemoteException;

    boolean isCallerApplicationRestrictionsManagingPackage(String str) throws RemoteException;

    boolean isCurrentInputMethodSetByOwner() throws RemoteException;

    boolean isDeviceProvisioned() throws RemoteException;

    boolean isDeviceProvisioningConfigApplied() throws RemoteException;

    boolean isEphemeralUser(ComponentName componentName) throws RemoteException;

    boolean isInputMethodPermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isLockTaskPermitted(String str) throws RemoteException;

    boolean isLogoutEnabled() throws RemoteException;

    boolean isManagedKiosk() throws RemoteException;

    boolean isManagedProfile(ComponentName componentName) throws RemoteException;

    boolean isMasterVolumeMuted(ComponentName componentName) throws RemoteException;

    boolean isMeteredDataDisabledPackageForUser(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isNetworkLoggingEnabled(ComponentName componentName, String str) throws RemoteException;

    boolean isNotificationListenerServicePermitted(String str, int i) throws RemoteException;

    boolean isOverrideApnEnabled(ComponentName componentName) throws RemoteException;

    boolean isPackageAllowedToAccessCalendarForUser(String str, int i) throws RemoteException;

    boolean isPackageSuspended(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean isProfileActivePasswordSufficientForParent(int i) throws RemoteException;

    boolean isProvisioningAllowed(String str, String str2) throws RemoteException;

    boolean isRemovingAdmin(ComponentName componentName, int i) throws RemoteException;

    boolean isResetPasswordTokenActive(ComponentName componentName) throws RemoteException;

    boolean isSecurityLoggingEnabled(ComponentName componentName) throws RemoteException;

    boolean isSeparateProfileChallengeAllowed(int i) throws RemoteException;

    boolean isSystemOnlyUser(ComponentName componentName) throws RemoteException;

    boolean isUnattendedManagedKiosk() throws RemoteException;

    boolean isUninstallBlocked(ComponentName componentName, String str) throws RemoteException;

    boolean isUninstallInQueue(String str) throws RemoteException;

    boolean isUsingUnifiedPassword(ComponentName componentName) throws RemoteException;

    void lockNow(int i, boolean z) throws RemoteException;

    int logoutUser(ComponentName componentName) throws RemoteException;

    void notifyLockTaskModeChanged(boolean z, String str, int i) throws RemoteException;

    void notifyPendingSystemUpdate(SystemUpdateInfo systemUpdateInfo) throws RemoteException;

    @UnsupportedAppUsage
    boolean packageHasActiveAdmins(String str, int i) throws RemoteException;

    void reboot(ComponentName componentName) throws RemoteException;

    void removeActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    boolean removeCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    boolean removeKeyPair(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean removeOverrideApn(ComponentName componentName, int i) throws RemoteException;

    boolean removeUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    void reportFailedBiometricAttempt(int i) throws RemoteException;

    void reportFailedPasswordAttempt(int i) throws RemoteException;

    void reportKeyguardDismissed(int i) throws RemoteException;

    void reportKeyguardSecured(int i) throws RemoteException;

    void reportPasswordChanged(int i) throws RemoteException;

    void reportSuccessfulBiometricAttempt(int i) throws RemoteException;

    void reportSuccessfulPasswordAttempt(int i) throws RemoteException;

    boolean requestBugreport(ComponentName componentName) throws RemoteException;

    boolean resetPassword(String str, int i) throws RemoteException;

    boolean resetPasswordWithToken(ComponentName componentName, String str, byte[] bArr, int i) throws RemoteException;

    List<NetworkEvent> retrieveNetworkLogs(ComponentName componentName, String str, long j) throws RemoteException;

    ParceledListSlice retrievePreRebootSecurityLogs(ComponentName componentName) throws RemoteException;

    ParceledListSlice retrieveSecurityLogs(ComponentName componentName) throws RemoteException;

    void setAccountManagementDisabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setActiveAdmin(ComponentName componentName, boolean z, int i) throws RemoteException;

    void setActivePasswordState(PasswordMetrics passwordMetrics, int i) throws RemoteException;

    void setAffiliationIds(ComponentName componentName, List<String> list) throws RemoteException;

    boolean setAlwaysOnVpnPackage(ComponentName componentName, String str, boolean z, List<String> list) throws RemoteException;

    boolean setApplicationHidden(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    void setApplicationRestrictions(ComponentName componentName, String str, String str2, Bundle bundle) throws RemoteException;

    boolean setApplicationRestrictionsManagingPackage(ComponentName componentName, String str) throws RemoteException;

    void setAutoTimeRequired(ComponentName componentName, boolean z) throws RemoteException;

    void setBackupServiceEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setBluetoothContactSharingDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCameraDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCertInstallerPackage(ComponentName componentName, String str) throws RemoteException;

    void setCrossProfileCalendarPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setCrossProfileCallerIdDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCrossProfileContactsSearchDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setDefaultSmsApplication(ComponentName componentName, String str) throws RemoteException;

    void setDelegatedScopes(ComponentName componentName, String str, List<String> list) throws RemoteException;

    boolean setDeviceOwner(ComponentName componentName, String str, int i) throws RemoteException;

    void setDeviceOwnerLockScreenInfo(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setDeviceProvisioningConfigApplied() throws RemoteException;

    void setEndUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setForceEphemeralUsers(ComponentName componentName, boolean z) throws RemoteException;

    int setGlobalPrivateDns(ComponentName componentName, int i, String str) throws RemoteException;

    ComponentName setGlobalProxy(ComponentName componentName, String str, String str2) throws RemoteException;

    void setGlobalSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setKeepUninstalledPackages(ComponentName componentName, String str, List<String> list) throws RemoteException;

    boolean setKeyPairCertificate(ComponentName componentName, String str, String str2, byte[] bArr, byte[] bArr2, boolean z) throws RemoteException;

    boolean setKeyguardDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setLockTaskFeatures(ComponentName componentName, int i) throws RemoteException;

    void setLockTaskPackages(ComponentName componentName, String[] strArr) throws RemoteException;

    void setLogoutEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setLongSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setMasterVolumeMuted(ComponentName componentName, boolean z) throws RemoteException;

    void setMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setMaximumTimeToLock(ComponentName componentName, long j, boolean z) throws RemoteException;

    List<String> setMeteredDataDisabledPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setNetworkLoggingEnabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setOrganizationColor(ComponentName componentName, int i) throws RemoteException;

    void setOrganizationColorForUser(int i, int i2) throws RemoteException;

    void setOrganizationName(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setOverrideApnsEnabled(ComponentName componentName, boolean z) throws RemoteException;

    String[] setPackagesSuspended(ComponentName componentName, String str, String[] strArr, boolean z) throws RemoteException;

    void setPasswordExpirationTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    void setPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPermissionGrantState(ComponentName componentName, String str, String str2, String str3, int i, RemoteCallback remoteCallback) throws RemoteException;

    void setPermissionPolicy(ComponentName componentName, String str, int i) throws RemoteException;

    boolean setPermittedAccessibilityServices(ComponentName componentName, List list) throws RemoteException;

    boolean setPermittedCrossProfileNotificationListeners(ComponentName componentName, List<String> list) throws RemoteException;

    boolean setPermittedInputMethods(ComponentName componentName, List list) throws RemoteException;

    void setProfileEnabled(ComponentName componentName) throws RemoteException;

    void setProfileName(ComponentName componentName, String str) throws RemoteException;

    boolean setProfileOwner(ComponentName componentName, String str, int i) throws RemoteException;

    void setRecommendedGlobalProxy(ComponentName componentName, ProxyInfo proxyInfo) throws RemoteException;

    void setRequiredStrongAuthTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    boolean setResetPasswordToken(ComponentName componentName, byte[] bArr) throws RemoteException;

    void setRestrictionsProvider(ComponentName componentName, ComponentName componentName2) throws RemoteException;

    void setScreenCaptureDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSecureSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setSecurityLoggingEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setShortSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setStartUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    boolean setStatusBarDisabled(ComponentName componentName, boolean z) throws RemoteException;

    int setStorageEncryption(ComponentName componentName, boolean z) throws RemoteException;

    void setSystemSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setSystemUpdatePolicy(ComponentName componentName, SystemUpdatePolicy systemUpdatePolicy) throws RemoteException;

    boolean setTime(ComponentName componentName, long j) throws RemoteException;

    boolean setTimeZone(ComponentName componentName, String str) throws RemoteException;

    void setTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle, boolean z) throws RemoteException;

    void setUninstallBlocked(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    void setUserIcon(ComponentName componentName, Bitmap bitmap) throws RemoteException;

    void setUserProvisioningState(int i, int i2) throws RemoteException;

    void setUserRestriction(ComponentName componentName, String str, boolean z) throws RemoteException;

    void startManagedQuickContact(String str, long j, boolean z, long j2, Intent intent) throws RemoteException;

    int startUserInBackground(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean startViewCalendarEventInManagedProfile(String str, long j, long j2, long j3, boolean z, int i) throws RemoteException;

    int stopUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean switchUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    void transferOwnership(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle) throws RemoteException;

    void uninstallCaCerts(ComponentName componentName, String str, String[] strArr) throws RemoteException;

    void uninstallPackageWithActiveAdmins(String str) throws RemoteException;

    boolean updateOverrideApn(ComponentName componentName, int i, ApnSetting apnSetting) throws RemoteException;

    void wipeDataWithReason(int i, String str) throws RemoteException;
}
