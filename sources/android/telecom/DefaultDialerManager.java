package android.telecom;

import android.app.ActivityManager;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Process;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultDialerManager {
    private static final String TAG = "DefaultDialerManager";

    public static boolean setDefaultDialerApplication(Context context, String packageName) {
        return setDefaultDialerApplication(context, packageName, ActivityManager.getCurrentUser());
    }

    public static boolean setDefaultDialerApplication(Context context, String packageName, int user) {
        long identity = Binder.clearCallingIdentity();
        try {
            CompletableFuture<Void> future = new CompletableFuture();
            String str = packageName;
            ((RoleManager) context.getSystemService(RoleManager.class)).addRoleHolderAsUser(RoleManager.ROLE_DIALER, str, 0, UserHandle.of(user), AsyncTask.THREAD_POOL_EXECUTOR, new -$$Lambda$DefaultDialerManager$csTSL_1G9gDs8ZsH7BZ6UatLUF0(future));
            future.get(5, TimeUnit.SECONDS);
            Binder.restoreCallingIdentity(identity);
            return true;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            String str2 = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to set default dialer to ");
            stringBuilder.append(packageName);
            stringBuilder.append(" for user ");
            stringBuilder.append(user);
            Slog.e(str2, stringBuilder.toString(), e);
            Binder.restoreCallingIdentity(identity);
            return false;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    static /* synthetic */ void lambda$setDefaultDialerApplication$0(CompletableFuture future, Boolean successful) {
        if (successful.booleanValue()) {
            future.complete(null);
        } else {
            future.completeExceptionally(new RuntimeException());
        }
    }

    public static String getDefaultDialerApplication(Context context) {
        return getDefaultDialerApplication(context, context.getUserId());
    }

    public static String getDefaultDialerApplication(Context context, int user) {
        try {
            String str = (String) CollectionUtils.firstOrNull(((RoleManager) context.getSystemService(RoleManager.class)).getRoleHoldersAsUser(RoleManager.ROLE_DIALER, UserHandle.of(user)));
            return str;
        } finally {
            Binder.restoreCallingIdentity(Binder.clearCallingIdentity());
        }
    }

    public static List<String> getInstalledDialerApplications(Context context, int userId) {
        PackageManager packageManager = context.getPackageManager();
        String str = Intent.ACTION_DIAL;
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivitiesAsUser(new Intent(str), (int) null, userId);
        List<String> packageNames = new ArrayList();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (!(activityInfo == null || packageNames.contains(activityInfo.packageName) || resolveInfo.targetUserId != -2)) {
                packageNames.add(activityInfo.packageName);
            }
        }
        Intent dialIntentWithTelScheme = new Intent(str);
        dialIntentWithTelScheme.setData(Uri.fromParts(PhoneAccount.SCHEME_TEL, "", null));
        return filterByIntent(context, packageNames, dialIntentWithTelScheme, userId);
    }

    public static List<String> getInstalledDialerApplications(Context context) {
        return getInstalledDialerApplications(context, Process.myUserHandle().getIdentifier());
    }

    public static boolean isDefaultOrSystemDialer(Context context, String packageName) {
        boolean z = false;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        TelecomManager tm = getTelecomManager(context);
        if (packageName.equals(tm.getDefaultDialerPackage()) || packageName.equals(tm.getSystemDialerPackage())) {
            z = true;
        }
        return z;
    }

    private static List<String> filterByIntent(Context context, List<String> packageNames, Intent intent, int userId) {
        if (packageNames == null || packageNames.isEmpty()) {
            return new ArrayList();
        }
        List<String> result = new ArrayList();
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivitiesAsUser(intent, 0, userId);
        int length = resolveInfoList.size();
        for (int i = 0; i < length; i++) {
            ActivityInfo info = ((ResolveInfo) resolveInfoList.get(i)).activityInfo;
            if (!(info == null || !packageNames.contains(info.packageName) || result.contains(info.packageName))) {
                result.add(info.packageName);
            }
        }
        return result;
    }

    private static TelecomManager getTelecomManager(Context context) {
        return (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
    }
}
