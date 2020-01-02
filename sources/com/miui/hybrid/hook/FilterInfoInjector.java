package com.miui.hybrid.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.internal.app.IPerfShielder;
import com.android.internal.app.QuickAppResolveInfo;
import com.miui.daemon.performance.PerfShielderManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class FilterInfoInjector {
    private static final String EXTRA_APP = "EXTRA_APP";
    private static final String EXTRA_INTENT_FILTER = "extra_intent_filter";
    private static final String EXTRA_LABEL = "extra_filter_label";
    private static final String EXTRA_ORIGINAL_INTENT = "extra_original_intent";
    private static final String EXTRA_PACKAGE = "extra_package";
    private static final String EXTRA_PAGE_FILTERS = "extra_page_filters";
    private static final String EXTRA_PAGE_PATH = "extra_page_path";
    private static final String EXTRA_URI_PATTERN = "extra_uri_pattern";
    private static final String HYBRID_APP_MANAGER_ACTION = "com.miui.hybrid.action.APP_DETAIL_MANAGER";
    private static final String HYBRID_FILTER_ACTION = "com.miui.hybrid.action.FILTER";
    private static final Object LOCK = new Object();
    private static final String PACKAGE_NAME_HYBRID = "com.miui.hybrid";
    public static final String TAG = "FilterInfoInjector";
    private Map<String, List<QuickAppIntentFilter>> mFilterInfoMap = new ConcurrentHashMap();
    private QuickAppResolver mQuickAppResolver = new QuickAppResolver();

    public static class DisplayInfo {
        public Drawable icon;
        public String label;
        public Intent longClickIntent;
        public ResolveInfo resolveInfo;
        public Intent resolvedIntent;

        public DisplayInfo(ResolveInfo resolveInfo, Drawable icon, String label, Intent resolvedIntent, Intent longClickIntent) {
            this.resolveInfo = resolveInfo;
            this.icon = icon;
            this.label = label;
            this.resolvedIntent = resolvedIntent;
            this.longClickIntent = longClickIntent;
        }
    }

    private static class Holder {
        static final FilterInfoInjector INSTANCE = new FilterInfoInjector();

        private Holder() {
        }
    }

    public static class QuickAppIntentFilter extends IntentFilter {
        private QuickAppResolveInfo appInfo;
        private Pattern uriPattern;

        public QuickAppIntentFilter(IntentFilter intentFilter) {
            super(intentFilter);
        }

        /* Access modifiers changed, original: 0000 */
        public void setAppInfo(QuickAppResolveInfo appInfo) {
            this.appInfo = appInfo;
        }

        /* Access modifiers changed, original: 0000 */
        public void setUriPattern(String uriPatternText) {
            this.uriPattern = Pattern.compile(uriPatternText);
        }

        public QuickAppResolveInfo getAppInfo() {
            return this.appInfo;
        }

        public Pattern getUriPattern() {
            return this.uriPattern;
        }
    }

    public static class QuickAppResolver extends IntentResolver<QuickAppIntentFilter, QuickAppIntentFilter> {
        /* Access modifiers changed, original: protected */
        public boolean isPackageForFilter(String packageName, QuickAppIntentFilter filter) {
            return TextUtils.equals(packageName, filter.getAppInfo().packageName);
        }

        /* Access modifiers changed, original: protected */
        public QuickAppIntentFilter[] newArray(int size) {
            return new QuickAppIntentFilter[size];
        }

        /* Access modifiers changed, original: protected */
        public QuickAppIntentFilter newResult(QuickAppIntentFilter filter, int match, int userId) {
            return filter;
        }

        /* Access modifiers changed, original: protected */
        public void sortResults(List<QuickAppIntentFilter> list) {
        }
    }

    public static FilterInfoInjector getInstance() {
        return Holder.INSTANCE;
    }

    public static List<DisplayInfo> processQuickApps(Context context, Intent targetIntent) {
        Parcelable parcelable = targetIntent;
        ArrayList displayInfos = new ArrayList();
        IPerfShielder perfShielder = PerfShielderManager.getService();
        if (perfShielder == null) {
            return displayInfos;
        }
        Intent intent = new Intent();
        String str = HYBRID_FILTER_ACTION;
        intent.setAction(str);
        ResolveInfo hybridPlatform = context.getPackageManager().resolveActivity(intent, 65536);
        if (hybridPlatform != null) {
            if (PACKAGE_NAME_HYBRID.equals(hybridPlatform.activityInfo.packageName)) {
                try {
                    for (QuickAppResolveInfo appInfo : perfShielder.resolveQuickAppInfos(parcelable)) {
                        Intent resolvedIntent = new Intent();
                        resolvedIntent.setComponent(new ComponentName(hybridPlatform.activityInfo.packageName, hybridPlatform.activityInfo.name));
                        resolvedIntent.setAction(str);
                        resolvedIntent.putExtra(EXTRA_PACKAGE, appInfo.packageName);
                        resolvedIntent.putExtra(EXTRA_ORIGINAL_INTENT, parcelable);
                        resolvedIntent.addFlags(View.SCROLLBARS_OUTSIDE_INSET);
                        Intent longClickIntent = new Intent(HYBRID_APP_MANAGER_ACTION);
                        longClickIntent.putExtra(EXTRA_APP, appInfo.packageName);
                        Drawable displayIcon = getDisplayIcon(context, appInfo.iconUri);
                        if (displayIcon != null) {
                            displayInfos.add(new DisplayInfo(hybridPlatform, displayIcon, appInfo.label, resolvedIntent, longClickIntent));
                        }
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "fail to processQuickAppList", e);
                }
            }
        }
        return displayInfos;
    }

    private static Drawable getDisplayIcon(Context context, Uri iconUri) {
        String str = TAG;
        if (iconUri == null) {
            Log.e(str, "Fail to getDisplayIcon: iconUri is null!");
            return null;
        }
        Bitmap iconBitmap = null;
        try {
            iconBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(iconUri));
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to decode stream for: ");
            stringBuilder.append(iconUri);
            Log.e(str, stringBuilder.toString(), e);
        }
        if (iconBitmap != null) {
            return new BitmapDrawable(context.getResources(), iconBitmap);
        }
        Log.e(str, "Fail to getDisplayIcon: iconBitmap is null!");
        return null;
    }

    public boolean insertFilterInfo(String packageName, String name, Uri iconUri, List<Bundle> filterInfos) {
        String str = packageName;
        Uri uri = iconUri;
        List<Bundle> list = filterInfos;
        if (TextUtils.isEmpty(packageName) || uri == null || list == null || filterInfos.size() == 0) {
            Log.e(TAG, "Fail to insertFilterInfo: illegal arguments!");
            return false;
        }
        deleteFilterInfo(packageName);
        ArrayList filters = new ArrayList();
        for (int i = 0; i < filterInfos.size(); i++) {
            Bundle pageFilterInfo = (Bundle) list.get(i);
            String pagePath = pageFilterInfo.getString(EXTRA_PAGE_PATH);
            Iterator it = pageFilterInfo.getParcelableArrayList(EXTRA_PAGE_FILTERS).iterator();
            while (it.hasNext()) {
                Iterator it2;
                Bundle filter = (Bundle) it.next();
                String label = filter.getString(EXTRA_LABEL);
                IntentFilter intentFilter = (IntentFilter) filter.getParcelable(EXTRA_INTENT_FILTER);
                String uriPattern = filter.getString(EXTRA_URI_PATTERN);
                QuickAppResolveInfo appInfo = new QuickAppResolveInfo(str, pagePath, TextUtils.isEmpty(label) ? name : label, uri);
                QuickAppIntentFilter quickAppIntentFilter = new QuickAppIntentFilter(intentFilter);
                quickAppIntentFilter.setAppInfo(appInfo);
                quickAppIntentFilter.setUriPattern(uriPattern);
                synchronized (LOCK) {
                    it2 = it;
                    this.mQuickAppResolver.addFilter(quickAppIntentFilter);
                }
                filters.add(quickAppIntentFilter);
                it = it2;
            }
        }
        this.mFilterInfoMap.put(str, filters);
        return true;
    }

    public boolean deleteFilterInfo(String packageName) {
        List<QuickAppIntentFilter> filters = (List) this.mFilterInfoMap.get(packageName);
        if (filters == null) {
            return false;
        }
        synchronized (LOCK) {
            for (QuickAppIntentFilter filter : filters) {
                this.mQuickAppResolver.removeFilter(filter);
            }
        }
        this.mFilterInfoMap.remove(packageName);
        return true;
    }

    public List<QuickAppResolveInfo> resolveAppInfos(Context context, Intent targetIntent) {
        List<QuickAppResolveInfo> result = new ArrayList();
        if (targetIntent == null || targetIntent.getComponent() != null) {
            return result;
        }
        Intent intentWithoutUri = new Intent(targetIntent);
        String type = intentWithoutUri.getType();
        Uri uri = intentWithoutUri.getData();
        intentWithoutUri.setType(type);
        synchronized (LOCK) {
            List<QuickAppIntentFilter> filterResult = this.mQuickAppResolver.queryIntent(intentWithoutUri, type, false, 0);
        }
        for (QuickAppIntentFilter filter : filterResult) {
            Pattern filterUriPattern = filter.getUriPattern();
            if (TextUtils.isEmpty(filterUriPattern.toString()) || (uri != null && filterUriPattern.matcher(uri.toString()).find())) {
                result.add(filter.getAppInfo());
            }
        }
        return result;
    }
}
