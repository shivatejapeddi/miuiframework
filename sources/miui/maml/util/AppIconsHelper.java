package miui.maml.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import miui.content.res.IconCustomizer;
import miui.maml.AnimatingDrawable;
import miui.maml.FancyDrawable;
import miui.maml.LifecycleResourceManager;
import miui.maml.RenderThread;
import miui.maml.ResourceManager;
import miui.maml.ScreenElementRoot;
import miui.maml.util.RendererCoreCache.OnCreateRootCallback;
import miui.maml.util.RendererCoreCache.RendererCoreInfo;

public class AppIconsHelper {
    public static final int TIME_DAY = 86400000;
    public static final int TIME_HOUR = 3600000;
    public static final int TIME_MIN = 60000;
    private static HashMap<String, WeakReference<ResourceManager>> mAnimatingIconsResourceManagers = new HashMap();
    private static final OnCreateRootCallback mOnCreateRootCallback = new OnCreateRootCallback() {
        public void onCreateRoot(ScreenElementRoot root) {
            if (root != null) {
                root.setScaleByDensity(true);
            }
        }
    };
    private static RendererCoreCache mRendererCoreCache;
    private static int mThemeChanged;

    private AppIconsHelper() {
    }

    public static void cleanUp() {
        RenderThread.globalThreadStop();
    }

    public static Drawable getIconDrawable(Context context, ResolveInfo info, PackageManager pm) {
        return getIconDrawable(context, info, pm, 0);
    }

    public static Drawable getIconDrawable(Context context, ResolveInfo info, PackageManager pm, long cacheTime) {
        return getIconDrawable(context, info.activityInfo != null ? info.activityInfo : info.serviceInfo, pm, cacheTime);
    }

    public static Drawable getIconDrawable(Context context, PackageItemInfo info, PackageManager pm) {
        return getIconDrawable(context, info, pm, 0);
    }

    public static Drawable getIconDrawable(Context context, PackageItemInfo info, PackageManager pm, long cacheTime) {
        return getIconDrawable(context, info, pm, cacheTime, new UserHandle(context.getUserId()));
    }

    public static Drawable getIconDrawable(Context context, PackageItemInfo info, PackageManager pm, long cacheTime, UserHandle user) {
        String activityName;
        String packageName = info.packageName;
        if (VERSION.SDK_INT <= 24 || !(info instanceof ApplicationInfo)) {
            activityName = info.name;
        } else {
            activityName = null;
        }
        Drawable d = getIconDrawable(context, info, packageName, activityName, cacheTime, user);
        if (d != null) {
            return d;
        }
        return info.loadIcon(pm);
    }

    public static Drawable getIconDrawable(Context context, String packageName, String activityName, long cacheTime) {
        return getIconDrawable(context, packageName, activityName, cacheTime, new UserHandle(context.getUserId()));
    }

    public static Drawable getIconDrawable(Context context, String packageName, String activityName, long cacheTime, UserHandle user) {
        PackageItemInfo info = null;
        try {
            info = context.getPackageManager().getActivityInfo(new ComponentName(packageName, activityName), 0);
        } catch (Exception e) {
        }
        return getIconDrawable(context, info, packageName, activityName, cacheTime, user);
    }

    public static Drawable getFancyIconDrawable(Context context, String packageName, String activityName, long cacheTime, UserHandle user) {
        PackageItemInfo info = null;
        try {
            info = context.getPackageManager().getActivityInfo(new ComponentName(packageName, activityName), 0);
        } catch (Exception e) {
        }
        return getIconDrawable(context, info, packageName, activityName, cacheTime, user, true);
    }

    public static Drawable getIconDrawable(Context context, PackageItemInfo info, String packageName, String activityName, long cacheTime) {
        return getIconDrawable(context, info, packageName, activityName, cacheTime, new UserHandle(context.getUserId()));
    }

    public static Drawable getIconDrawable(Context context, PackageItemInfo info, String packageName, String activityName, long cacheTime, UserHandle user) {
        return getIconDrawable(context, info, packageName, activityName, cacheTime, user, false);
    }

    public static Drawable getIconDrawable(Context context, PackageItemInfo info, String packageName, String activityName, long cacheTime, UserHandle user, boolean onlyMatchFancy) {
        Exception e;
        Context context2;
        String str;
        long j;
        UserHandle userHandle;
        if (mRendererCoreCache == null) {
            mRendererCoreCache = new RendererCoreCache(new Handler(Looper.getMainLooper()));
        }
        try {
            checkVersion(context);
            String key = new StringBuilder();
            try {
                key.append(packageName);
            } catch (Exception e2) {
                e = e2;
                context2 = context;
                str = activityName;
                j = cacheTime;
                userHandle = user;
                Log.e("MAML AppIconsHelper", e.toString());
                return null;
            }
            try {
                Drawable dr;
                key.append(activityName);
                key.append(user.getIdentifier());
                key = key.toString();
                String animatingIconRelativePath = IconCustomizer.getAnimatingIconRelativePath(info, packageName, activityName);
                if (animatingIconRelativePath == null || onlyMatchFancy) {
                    try {
                        RendererCoreInfo ri = mRendererCoreCache.get(key, cacheTime);
                        if (ri == null) {
                            String fancyIconRelativePath;
                            if (animatingIconRelativePath != null) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(animatingIconRelativePath);
                                stringBuilder.append("fancy/");
                                fancyIconRelativePath = stringBuilder.toString();
                            } else {
                                fancyIconRelativePath = IconCustomizer.getFancyIconRelativePath(info, packageName, activityName);
                            }
                            ri = mRendererCoreCache.get((Object) key, context, cacheTime, new FancyIconResourceLoader(fancyIconRelativePath), mOnCreateRootCallback);
                        }
                        Drawable fancyDrawable = (ri == null || ri.r == null) ? null : new FancyDrawable(ri.r);
                        dr = fancyDrawable;
                    } catch (Exception e3) {
                        e = e3;
                        context2 = context;
                        userHandle = user;
                        Log.e("MAML AppIconsHelper", e.toString());
                        return null;
                    }
                }
                ResourceManager rm;
                WeakReference<ResourceManager> ref = (WeakReference) mAnimatingIconsResourceManagers.get(key);
                ResourceManager rm2 = ref == null ? null : (ResourceManager) ref.get();
                if (rm2 == null) {
                    String fileDir = new StringBuilder();
                    fileDir.append(animatingIconRelativePath);
                    fileDir.append("quiet/");
                    rm2 = new LifecycleResourceManager(new FancyIconResourceLoader(fileDir.toString()), 3600000, 360000);
                    mAnimatingIconsResourceManagers.put(key, new WeakReference(rm2));
                    rm = rm2;
                } else {
                    rm = rm2;
                }
                dr = new AnimatingDrawable(context, packageName, activityName, rm, user);
                j = cacheTime;
                String str2 = animatingIconRelativePath;
                if (dr != null) {
                    try {
                        PortableUtils.getUserBadgedIcon(context, dr, user);
                    } catch (Exception e4) {
                        e = e4;
                    }
                } else {
                    context2 = context;
                    userHandle = user;
                }
                return dr;
            } catch (Exception e5) {
                e = e5;
                context2 = context;
                j = cacheTime;
                userHandle = user;
                Log.e("MAML AppIconsHelper", e.toString());
                return null;
            }
        } catch (Exception e6) {
            e = e6;
            context2 = context;
            String str3 = packageName;
            str = activityName;
            j = cacheTime;
            userHandle = user;
            Log.e("MAML AppIconsHelper", e.toString());
            return null;
        }
    }

    private static void checkVersion(Context context) {
        int version = context.getResources().getConfiguration().extraConfig.themeChanged;
        if (version > mThemeChanged) {
            clearCache();
            mThemeChanged = version;
        }
    }

    public static void clearCache() {
        RendererCoreCache rendererCoreCache = mRendererCoreCache;
        if (rendererCoreCache != null) {
            rendererCoreCache.clear();
        }
        HashMap hashMap = mAnimatingIconsResourceManagers;
        if (hashMap != null) {
            hashMap.clear();
        }
    }
}
