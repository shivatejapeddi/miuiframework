package com.android.internal.app;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.miui.R;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract.Directory;
import android.util.Log;
import com.android.internal.app.ResolverActivity.ResolvedComponentInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import miui.os.Build;
import miui.security.SecurityManager;

public class ChooserActivityInjector {
    private static final String TAG = "ChooserActivityInjector";
    private static final HashSet<Activity> sMiAppsInterceptedResolver = new HashSet();

    public static boolean canBindService(Context context, Intent service, int userId) {
        return ((SecurityManager) context.getSystemService(Context.SECURITY_SERVICE)).isAllowStartService(service, userId);
    }

    public static boolean canInterceptByMiAppStore(Activity context, Intent intent) {
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 128);
        if (resolveInfos == null) {
            return false;
        }
        for (ResolveInfo resolveInfo : resolveInfos) {
            Bundle bundle = resolveInfo.activityInfo.metaData;
            if (bundle != null) {
                try {
                    if (bundle.getBoolean("mi_use_custom_resolver") && resolveInfo.activityInfo.enabled) {
                        return true;
                    }
                } catch (Exception e) {
                    Log.w(TAG, e.getMessage());
                }
            }
        }
        return false;
    }

    public static boolean isInterceptedByMiAppStore(Activity activity) {
        return sMiAppsInterceptedResolver.contains(activity);
    }

    public static void startInterceptByMiAppStore(Activity activity, Intent intent, Object resolverController, ResolverRankerServiceResolverComparator resolverComparator, int launchedFromUid) {
        List<ResolvedComponentInfo> sortedComponentInfos;
        if (resolverController != null) {
            sortedComponentInfos = createSortedResloveListByResolverController(resolverController, intent);
        } else {
            sortedComponentInfos = createSortedResolveList(activity, intent, resolverComparator, launchedFromUid);
        }
        ArrayList<ResolveInfo> sortedResolveInfos = new ArrayList();
        for (ResolvedComponentInfo resolvedComponentInfo : sortedComponentInfos) {
            sortedResolveInfos.add(resolvedComponentInfo.getResolveInfoAt(0));
        }
        sMiAppsInterceptedResolver.add(activity);
        Intent miAppStoreResolverIntent = new Intent("com.xiaomi.market.RESOLVER");
        miAppStoreResolverIntent.putExtra("targetIntent", (Parcelable) intent);
        miAppStoreResolverIntent.putParcelableArrayListExtra("sortedResolveInfos", sortedResolveInfos);
        miAppStoreResolverIntent.setPackage("com.xiaomi.mipicks");
        miAppStoreResolverIntent.putExtra(Directory.CALLER_PACKAGE_PARAM_KEY, activity.getCallingPackage());
        miAppStoreResolverIntent.putExtra(AccountManager.KEY_CALLER_UID, launchedFromUid);
        miAppStoreResolverIntent.putExtra("appChooserTitle", activity.getString(R.string.android_whichViewApplication));
        miAppStoreResolverIntent.putExtra("moreItemLabel", activity.getString(com.android.internal.R.string.more_item_label));
        miAppStoreResolverIntent.putExtra("rememberChoiceText", activity.getString(R.string.alwaysUsePrompt));
        activity.startActivityForResult(miAppStoreResolverIntent, 100);
    }

    private static List<ResolvedComponentInfo> createSortedResloveListByResolverController(Object controller, Intent intent) {
        try {
            Method getResolvers = controller.getClass().getDeclaredMethod("getResolversForIntent", new Class[]{Boolean.TYPE, Boolean.TYPE, List.class});
            new ArrayList().add(intent);
            List<ResolvedComponentInfo> componentInfoList = (List) getResolvers.invoke(controller, new Object[]{Boolean.valueOf(true), Boolean.valueOf(true), intents});
            controller.getClass().getDeclaredMethod("filterIneligibleActivities", new Class[]{List.class, Boolean.TYPE}).invoke(controller, new Object[]{componentInfoList, Boolean.valueOf(false)});
            controller.getClass().getDeclaredMethod("sort", new Class[]{List.class}).invoke(controller, new Object[]{componentInfoList});
            return componentInfoList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return new ArrayList();
        }
    }

    private static ArrayList<ResolvedComponentInfo> createSortedResolveList(Context context, Intent intent, ResolverRankerServiceResolverComparator comparator, int launchedFromUid) {
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 65728);
        ArrayList<ResolvedComponentInfo> currentResolveList = new ArrayList();
        if (resolveInfos != null) {
            for (ResolveInfo resolveInfo : resolveInfos) {
                addResolveListDedupe(currentResolveList, intent, resolveInfo);
            }
        }
        int i = currentResolveList.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            ActivityInfo ai = ((ResolvedComponentInfo) currentResolveList.get(i)).getResolveInfoAt(0).activityInfo;
            if (ActivityManager.checkComponentPermission(ai.permission, launchedFromUid, ai.applicationInfo.uid, ai.exported) != 0 || (Build.IS_CM_CUSTOMIZATION_TEST && ai.packageName.equals("com.android.htmlviewer"))) {
                currentResolveList.remove(i);
            }
        }
        i = currentResolveList.size();
        if (i > 0) {
            ResolveInfo r0 = ((ResolvedComponentInfo) currentResolveList.get(0)).getResolveInfoAt(0);
            for (int i2 = 1; i2 < i; i2++) {
                ResolveInfo ri = ((ResolvedComponentInfo) currentResolveList.get(i2)).getResolveInfoAt(0);
                if (r0.priority != ri.priority || r0.isDefault != ri.isDefault) {
                    while (i2 < i) {
                        currentResolveList.remove(i2);
                        i--;
                    }
                }
            }
        }
        return currentResolveList;
    }

    private static void addResolveListDedupe(List<ResolvedComponentInfo> into, Intent intent, ResolveInfo resolveInfo) {
        boolean found = false;
        for (ResolvedComponentInfo componentInfo : into) {
            ActivityInfo ai = resolveInfo.activityInfo;
            if (ai.packageName.equals(componentInfo.name.getPackageName()) && ai.name.equals(componentInfo.name.getClassName())) {
                found = true;
                componentInfo.add(intent, resolveInfo);
                break;
            }
        }
        if (!found) {
            into.add(new ResolvedComponentInfo(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name), intent, resolveInfo));
        }
    }

    public static void stopInterceptByMiAppStore(Activity activity) {
        sMiAppsInterceptedResolver.remove(activity);
    }
}
