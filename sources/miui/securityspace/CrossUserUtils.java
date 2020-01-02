package miui.securityspace;

import android.app.IUserSwitchObserver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telecom.Logging.Session;
import android.util.ArrayMap;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import miui.security.ISecurityManager;
import miui.security.ISecurityManager.Stub;

public class CrossUserUtils {
    public static final String ACTION_XSPACE_RESOLVER_ACTIVITY = "miui.intent.action.ACTION_XSPACE_RESOLVER_ACTIVITY";
    public static final String EXTRA_PICKED_USER_ID = "android.intent.extra.picked_user_id";
    public static final String EXTRA_XSPACE_RESOLVER_ACTIVITY_AIM_PACKAGE = "android.intent.extra.xspace_resolver_activity_aim_package";
    public static final String EXTRA_XSPACE_RESOLVER_ACTIVITY_CALLING_PACKAGE = "miui.intent.extra.xspace_resolver_activity_calling_package";
    public static final String EXTRA_XSPACE_RESOLVER_ACTIVITY_ORIGINAL_INTENT = "android.intent.extra.xspace_resolver_activity_original_intent";
    private static Map<String, String> noCheckContentProviderPermissionPkg = new HashMap();
    private static ArrayMap<Integer, WeakReference<Drawable>> sBitmapCache = new ArrayMap();
    private static ISecurityManager sISecurityManager = null;

    public static Uri addUserIdForUri(Uri uri, int userId) {
        return CrossUserUtilsCompat.addUserIdForUri(uri, userId);
    }

    public static Uri addUserIdForUri(Uri uri, Context context, String packageName, Intent intent) {
        return CrossUserUtilsCompat.addUserIdForUri(uri, context, packageName, intent);
    }

    public static boolean checkUidPermission(Context context, String packageName) {
        return CrossUserUtilsCompat.checkUidPermission(context, packageName);
    }

    public static Drawable getOriginalAppIcon(Context context, String pkgName) {
        return CrossUserUtilsCompat.getOriginalAppIcon(context, pkgName);
    }

    static {
        noCheckContentProviderPermissionPkg.put("com.android.incallui", "contacts;com.android.contacts");
    }

    public static int getCurrentUserId() {
        try {
            if (sISecurityManager == null) {
                sISecurityManager = Stub.asInterface(ServiceManager.getService(Context.SECURITY_SERVICE));
            }
            return sISecurityManager.getCurrentUserId();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getSecondSpaceId() {
        try {
            if (sISecurityManager == null) {
                sISecurityManager = Stub.asInterface(ServiceManager.getService(Context.SECURITY_SERVICE));
            }
            return sISecurityManager.getSecondSpaceId();
        } catch (RemoteException e) {
            e.printStackTrace();
            return -10000;
        }
    }

    public static String getComponentStringWithUserIdAndTaskId(ComponentName component, int userId, int taskId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(component.flattenToShortString());
        String str = Session.SESSION_SEPARATION_CHAR_CHILD;
        stringBuilder.append(str);
        stringBuilder.append(userId);
        stringBuilder.append(str);
        stringBuilder.append(taskId);
        return stringBuilder.toString();
    }

    public static String getComponentStringWithUserId(ComponentName component, int userId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(component.flattenToShortString());
        stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
        stringBuilder.append(userId);
        return stringBuilder.toString();
    }

    public static boolean needCheckUser(ProviderInfo cpi, String processName, int userId, boolean checkUser) {
        if (userId == 0 && XSpaceUserHandle.isXSpaceUserCalling()) {
            return false;
        }
        if (!(!checkUser || cpi == null || processName == null)) {
            String authority = (String) noCheckContentProviderPermissionPkg.get(processName);
            if (authority == null || !authority.equals(cpi.authority)) {
                return checkUser;
            }
            return false;
        }
        return checkUser;
    }

    public static boolean checkCrossPermission(String callingPkg, int userId) {
        if (callingPkg != null && noCheckContentProviderPermissionPkg.containsKey(callingPkg) && userId == 0) {
            return true;
        }
        return false;
    }

    public static boolean hasSecondSpace(Context context) {
        return CrossUserUtilsCompat.hasSecondSpace(context);
    }

    public static boolean hasXSpaceUser(Context context) {
        return CrossUserUtilsCompat.hasXSpaceUser(context);
    }

    public static boolean isAirSpace(Context context, int userId) {
        return false;
    }

    public static boolean hasAirSpace(Context context) {
        return false;
    }

    static Drawable createDrawableWithCache(Context context, Bitmap originBitmap) {
        synchronized (sBitmapCache) {
            WeakReference<Drawable> cachedRef = (WeakReference) sBitmapCache.get(Integer.valueOf(originBitmap.hashCode()));
            if (cachedRef == null || cachedRef.get() == null) {
                if (cachedRef != null) {
                    recycleCacheMap();
                }
                BitmapDrawable newDrawable = new BitmapDrawable(context.getResources(), originBitmap.copy(originBitmap.getConfig(), true));
                sBitmapCache.put(Integer.valueOf(originBitmap.hashCode()), new WeakReference(newDrawable));
                return newDrawable;
            }
            Drawable drawable = (Drawable) cachedRef.get();
            return drawable;
        }
    }

    private static void recycleCacheMap() {
        synchronized (sBitmapCache) {
            Iterator<Entry<Integer, WeakReference<Drawable>>> entryIterator = sBitmapCache.entrySet().iterator();
            while (entryIterator.hasNext()) {
                if (((WeakReference) ((Entry) entryIterator.next()).getValue()).get() == null) {
                    entryIterator.remove();
                }
            }
        }
    }

    public static void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
        CrossUserUtilsCompat.registerUserSwitchObserver(observer, name);
    }
}
