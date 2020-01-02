package com.android.internal.content;

import android.annotation.UnsupportedAppUsage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.Preconditions;
import java.util.HashSet;

public abstract class PackageMonitor extends BroadcastReceiver {
    public static final int PACKAGE_PERMANENT_CHANGE = 3;
    public static final int PACKAGE_TEMPORARY_CHANGE = 2;
    public static final int PACKAGE_UNCHANGED = 0;
    public static final int PACKAGE_UPDATING = 1;
    static final IntentFilter sExternalFilt = new IntentFilter();
    static final IntentFilter sNonDataFilt = new IntentFilter();
    static final IntentFilter sPackageFilt = new IntentFilter();
    String[] mAppearingPackages;
    int mChangeType;
    int mChangeUserId = -10000;
    String[] mDisappearingPackages;
    String[] mModifiedComponents;
    String[] mModifiedPackages;
    Context mRegisteredContext;
    Handler mRegisteredHandler;
    boolean mSomePackagesChanged;
    String[] mTempArray = new String[1];
    final HashSet<String> mUpdatingPackages = new HashSet();

    static {
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_ADDED);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_REMOVED);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_CHANGED);
        sPackageFilt.addAction(Intent.ACTION_QUERY_PACKAGE_RESTART);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        sPackageFilt.addDataScheme("package");
        sNonDataFilt.addAction(Intent.ACTION_UID_REMOVED);
        sNonDataFilt.addAction(Intent.ACTION_USER_STOPPED);
        sNonDataFilt.addAction(Intent.ACTION_PACKAGES_SUSPENDED);
        sNonDataFilt.addAction(Intent.ACTION_PACKAGES_UNSUSPENDED);
        sExternalFilt.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sExternalFilt.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
    }

    @UnsupportedAppUsage
    public void register(Context context, Looper thread, boolean externalStorage) {
        register(context, thread, null, externalStorage);
    }

    @UnsupportedAppUsage
    public void register(Context context, Looper thread, UserHandle user, boolean externalStorage) {
        register(context, user, externalStorage, thread == null ? BackgroundThread.getHandler() : new Handler(thread));
    }

    public void register(Context context, UserHandle user, boolean externalStorage, Handler handler) {
        if (this.mRegisteredContext == null) {
            this.mRegisteredContext = context;
            this.mRegisteredHandler = (Handler) Preconditions.checkNotNull(handler);
            if (user != null) {
                Context context2 = context;
                UserHandle userHandle = user;
                context2.registerReceiverAsUser(this, userHandle, sPackageFilt, null, this.mRegisteredHandler);
                context2.registerReceiverAsUser(this, userHandle, sNonDataFilt, null, this.mRegisteredHandler);
                if (externalStorage) {
                    context.registerReceiverAsUser(this, user, sExternalFilt, null, this.mRegisteredHandler);
                    return;
                }
                return;
            }
            context.registerReceiver(this, sPackageFilt, null, this.mRegisteredHandler);
            context.registerReceiver(this, sNonDataFilt, null, this.mRegisteredHandler);
            if (externalStorage) {
                context.registerReceiver(this, sExternalFilt, null, this.mRegisteredHandler);
                return;
            }
            return;
        }
        throw new IllegalStateException("Already registered");
    }

    public Handler getRegisteredHandler() {
        return this.mRegisteredHandler;
    }

    @UnsupportedAppUsage
    public void unregister() {
        Context context = this.mRegisteredContext;
        if (context != null) {
            context.unregisterReceiver(this);
            this.mRegisteredContext = null;
            return;
        }
        throw new IllegalStateException("Not registered");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPackageUpdating(String packageName) {
        boolean contains;
        synchronized (this.mUpdatingPackages) {
            contains = this.mUpdatingPackages.contains(packageName);
        }
        return contains;
    }

    public void onBeginPackageChanges() {
    }

    public void onPackageAdded(String packageName, int uid) {
    }

    @UnsupportedAppUsage
    public void onPackageRemoved(String packageName, int uid) {
    }

    public void onPackageRemovedAllUsers(String packageName, int uid) {
    }

    public void onPackageUpdateStarted(String packageName, int uid) {
    }

    public void onPackageUpdateFinished(String packageName, int uid) {
    }

    @UnsupportedAppUsage
    public boolean onPackageChanged(String packageName, int uid, String[] components) {
        if (components != null) {
            for (String name : components) {
                if (packageName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onHandleForceStop(Intent intent, String[] packages, int uid, boolean doit) {
        return false;
    }

    public void onHandleUserStop(Intent intent, int userHandle) {
    }

    public void onUidRemoved(int uid) {
    }

    public void onPackagesAvailable(String[] packages) {
    }

    public void onPackagesUnavailable(String[] packages) {
    }

    public void onPackagesSuspended(String[] packages) {
    }

    public void onPackagesSuspended(String[] packages, Bundle launcherExtras) {
        onPackagesSuspended(packages);
    }

    public void onPackagesUnsuspended(String[] packages) {
    }

    public void onPackageDisappeared(String packageName, int reason) {
    }

    public void onPackageAppeared(String packageName, int reason) {
    }

    public void onPackageModified(String packageName) {
    }

    public boolean didSomePackagesChange() {
        return this.mSomePackagesChanged;
    }

    public int isPackageAppearing(String packageName) {
        String[] strArr = this.mAppearingPackages;
        if (strArr != null) {
            for (int i = strArr.length - 1; i >= 0; i--) {
                if (packageName.equals(this.mAppearingPackages[i])) {
                    return this.mChangeType;
                }
            }
        }
        return 0;
    }

    public boolean anyPackagesAppearing() {
        return this.mAppearingPackages != null;
    }

    @UnsupportedAppUsage
    public int isPackageDisappearing(String packageName) {
        String[] strArr = this.mDisappearingPackages;
        if (strArr != null) {
            for (int i = strArr.length - 1; i >= 0; i--) {
                if (packageName.equals(this.mDisappearingPackages[i])) {
                    return this.mChangeType;
                }
            }
        }
        return 0;
    }

    public boolean anyPackagesDisappearing() {
        return this.mDisappearingPackages != null;
    }

    public boolean isReplacing() {
        return this.mChangeType == 1;
    }

    @UnsupportedAppUsage
    public boolean isPackageModified(String packageName) {
        String[] strArr = this.mModifiedPackages;
        if (strArr != null) {
            for (int i = strArr.length - 1; i >= 0; i--) {
                if (packageName.equals(this.mModifiedPackages[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isComponentModified(String className) {
        if (className != null) {
            String[] strArr = this.mModifiedComponents;
            if (strArr != null) {
                for (int i = strArr.length - 1; i >= 0; i--) {
                    if (className.equals(this.mModifiedComponents[i])) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public void onSomePackagesChanged() {
    }

    public void onFinishPackageChanges() {
    }

    public void onPackageDataCleared(String packageName, int uid) {
    }

    public int getChangingUserId() {
        return this.mChangeUserId;
    }

    /* Access modifiers changed, original: 0000 */
    public String getPackageName(Intent intent) {
        Uri uri = intent.getData();
        return uri != null ? uri.getSchemeSpecificPart() : null;
    }

    public void onReceive(Context context, Intent intent) {
        this.mChangeUserId = intent.getIntExtra(Intent.EXTRA_USER_HANDLE, -10000);
        if (this.mChangeUserId == -10000) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Intent broadcast does not contain user handle: ");
            stringBuilder.append(intent);
            Slog.w("PackageMonitor", stringBuilder.toString());
            return;
        }
        onBeginPackageChanges();
        this.mAppearingPackages = null;
        this.mDisappearingPackages = null;
        this.mSomePackagesChanged = false;
        this.mModifiedComponents = null;
        String action = intent.getAction();
        String pkg;
        int uid;
        String[] strArr;
        int uid2;
        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            pkg = getPackageName(intent);
            uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
            this.mSomePackagesChanged = true;
            if (pkg != null) {
                strArr = this.mTempArray;
                this.mAppearingPackages = strArr;
                strArr[0] = pkg;
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    this.mModifiedPackages = this.mTempArray;
                    this.mChangeType = 1;
                    onPackageUpdateFinished(pkg, uid);
                    onPackageModified(pkg);
                } else {
                    this.mChangeType = 3;
                    onPackageAdded(pkg, uid);
                }
                onPackageAppeared(pkg, this.mChangeType);
                if (this.mChangeType == 1) {
                    synchronized (this.mUpdatingPackages) {
                        this.mUpdatingPackages.remove(pkg);
                    }
                }
            }
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
            pkg = getPackageName(intent);
            uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
            if (pkg != null) {
                strArr = this.mTempArray;
                this.mDisappearingPackages = strArr;
                strArr[0] = pkg;
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    this.mChangeType = 1;
                    synchronized (this.mUpdatingPackages) {
                    }
                    onPackageUpdateStarted(pkg, uid);
                } else {
                    this.mChangeType = 3;
                    this.mSomePackagesChanged = true;
                    onPackageRemoved(pkg, uid);
                    if (intent.getBooleanExtra(Intent.EXTRA_REMOVED_FOR_ALL_USERS, false)) {
                        onPackageRemovedAllUsers(pkg, uid);
                    }
                }
                onPackageDisappeared(pkg, this.mChangeType);
            }
        } else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
            pkg = getPackageName(intent);
            uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
            this.mModifiedComponents = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
            if (pkg != null) {
                strArr = this.mTempArray;
                this.mModifiedPackages = strArr;
                strArr[0] = pkg;
                this.mChangeType = 3;
                if (onPackageChanged(pkg, uid, this.mModifiedComponents)) {
                    this.mSomePackagesChanged = true;
                }
                onPackageModified(pkg);
            }
        } else if (Intent.ACTION_PACKAGE_DATA_CLEARED.equals(action)) {
            pkg = getPackageName(intent);
            uid2 = intent.getIntExtra(Intent.EXTRA_UID, 0);
            if (pkg != null) {
                onPackageDataCleared(pkg, uid2);
            }
        } else {
            int i = 2;
            String[] pkgList;
            String[] pkgList2;
            if (Intent.ACTION_QUERY_PACKAGE_RESTART.equals(action)) {
                this.mDisappearingPackages = intent.getStringArrayExtra(Intent.EXTRA_PACKAGES);
                this.mChangeType = 2;
                if (onHandleForceStop(intent, this.mDisappearingPackages, intent.getIntExtra(Intent.EXTRA_UID, 0), false)) {
                    setResultCode(-1);
                }
            } else if (Intent.ACTION_PACKAGE_RESTARTED.equals(action)) {
                this.mDisappearingPackages = new String[]{getPackageName(intent)};
                this.mChangeType = 2;
                onHandleForceStop(intent, this.mDisappearingPackages, intent.getIntExtra(Intent.EXTRA_UID, 0), true);
            } else if (Intent.ACTION_UID_REMOVED.equals(action)) {
                onUidRemoved(intent.getIntExtra(Intent.EXTRA_UID, 0));
            } else if (Intent.ACTION_USER_STOPPED.equals(action)) {
                if (intent.hasExtra(Intent.EXTRA_USER_HANDLE)) {
                    onHandleUserStop(intent, intent.getIntExtra(Intent.EXTRA_USER_HANDLE, 0));
                }
            } else if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) {
                pkgList = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                this.mAppearingPackages = pkgList;
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    i = 1;
                }
                this.mChangeType = i;
                this.mSomePackagesChanged = true;
                if (pkgList != null) {
                    onPackagesAvailable(pkgList);
                    for (String onPackageAppeared : pkgList) {
                        onPackageAppeared(onPackageAppeared, this.mChangeType);
                    }
                }
            } else if (Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
                pkgList = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                this.mDisappearingPackages = pkgList;
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    i = 1;
                }
                this.mChangeType = i;
                this.mSomePackagesChanged = true;
                if (pkgList != null) {
                    onPackagesUnavailable(pkgList);
                    for (String onPackageAppeared2 : pkgList) {
                        onPackageDisappeared(onPackageAppeared2, this.mChangeType);
                    }
                }
            } else if (Intent.ACTION_PACKAGES_SUSPENDED.equals(action)) {
                pkgList2 = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                Bundle launcherExtras = intent.getBundleExtra(Intent.EXTRA_LAUNCHER_EXTRAS);
                this.mSomePackagesChanged = true;
                onPackagesSuspended(pkgList2, launcherExtras);
            } else if (Intent.ACTION_PACKAGES_UNSUSPENDED.equals(action)) {
                pkgList2 = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                this.mSomePackagesChanged = true;
                onPackagesUnsuspended(pkgList2);
            }
        }
        if (this.mSomePackagesChanged) {
            onSomePackagesChanged();
        }
        onFinishPackageChanges();
        this.mChangeUserId = -10000;
    }
}
