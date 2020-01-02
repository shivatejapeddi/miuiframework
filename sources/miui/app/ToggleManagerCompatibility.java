package miui.app;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.AppGlobals;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.miui.R;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.security.keystore.KeyProperties;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import miui.app.ToggleManager.OnToggleChangedListener;
import miui.app.ToggleManager.OnToggleOrderChangedListener;
import miui.os.Build;

public class ToggleManagerCompatibility {
    public static final int CUSTOM_BASE_ID = 10000;
    private static final String PROCESS_NAME_SYSTEM_UI = "com.android.systemui";
    public static final String TAG = "ToggleManagerCompatibility";
    private static ArrayList<Integer> mCustomIds = new ArrayList();
    private static HashMap<Integer, String> mCustomToggleComponentNames = new HashMap();
    private static HashMap<Integer, Drawable> mCustomToggleImages = new HashMap();
    private static HashMap<Integer, String> mCustomToggleLabelNames = new HashMap();
    private static int mCustomToggleOffColor;
    private static int mCustomToggleOnColor;
    private static HashMap<Integer, Boolean> mCustomToggleStatus = new HashMap();
    public static boolean mEnableCustom = true;
    private static boolean mIsSystemUI = false;
    private static Object mObjectLock = new Object();
    private static ToggleManager mToggleManager = null;
    private static ToggleManagerCompatibility mToggleManagerCompatibility = null;
    private Handler mBgHandler;
    private HandlerThread mBgThread;
    private Context mContext;
    private List<OnCustomToggleChangedListener> mCustomToggleChangedListeners;
    private CustomToggleQueryRunnable mCustomToggleQueryRunnable = new CustomToggleQueryRunnable();
    private final ContentObserver mDevelopmentObserver = new ContentObserver(this.mHandler) {
        public void onChange(boolean selfChange) {
            if (ToggleManagerCompatibility.mEnableCustom) {
                ToggleManagerCompatibility.this.queryCustomToggles();
            }
        }
    };
    private final Handler mHandler = new Handler();
    private BroadcastReceiver mPackageChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                Intent.ACTION_PACKAGE_REMOVED.equals(action);
            } else if (ToggleManagerCompatibility.mEnableCustom) {
                ToggleManagerCompatibility.this.queryCustomToggles();
            }
        }
    };
    private final ContentResolver mResolver;
    private final ContentObserver mTogglOrderObserver = new ContentObserver(this.mHandler) {
        public void onChange(boolean selfChange) {
            synchronized (ToggleManagerCompatibility.mObjectLock) {
                ArrayList<Integer> customIds = ToggleManagerCompatibility.getCustomToggleIds(ToggleManagerCompatibility.this.mContext);
                for (int i = 0; i < ToggleManagerCompatibility.mCustomIds.size(); i++) {
                    if (!customIds.contains(ToggleManagerCompatibility.mCustomIds.get(i))) {
                        ToggleManagerCompatibility.mCustomToggleComponentNames.remove(ToggleManagerCompatibility.mCustomIds.get(i));
                        ToggleManagerCompatibility.mCustomToggleLabelNames.remove(ToggleManagerCompatibility.mCustomIds.get(i));
                        ToggleManagerCompatibility.mCustomToggleStatus.remove(ToggleManagerCompatibility.mCustomIds.get(i));
                        ToggleManagerCompatibility.mCustomToggleImages.remove(ToggleManagerCompatibility.mCustomIds.get(i));
                        ToggleManagerCompatibility.mCustomIds.remove(ToggleManagerCompatibility.mCustomIds.get(i));
                    }
                }
            }
            for (OnToggleOrderChangedListener toggleOrderChangedListener : ToggleManagerCompatibility.this.mToggleOrderChangedListener) {
                toggleOrderChangedListener.OnToggleOrderChanged();
            }
            if (!ToggleManagerCompatibility.mIsSystemUI) {
                for (OnCustomToggleChangedListener onCustomToggleChangedListener : ToggleManagerCompatibility.this.mCustomToggleChangedListeners) {
                    onCustomToggleChangedListener.onCustomToggleChanged();
                }
            }
        }
    };
    private List<WeakReference<OnToggleChangedListener>> mToggleChangedListener;
    private List<OnToggleOrderChangedListener> mToggleOrderChangedListener;

    public final class CustomToggleQueryRunnable implements Runnable {
        public void run() {
            Exception e;
            PackageManager pm;
            List<ResolveInfo> services;
            boolean pm2;
            synchronized (ToggleManagerCompatibility.mObjectLock) {
                ToggleManagerCompatibility.mCustomToggleComponentNames.clear();
                ToggleManagerCompatibility.mCustomToggleLabelNames.clear();
                ToggleManagerCompatibility.mCustomToggleImages.clear();
                ToggleManagerCompatibility.mCustomIds.clear();
                PackageManager pm3 = ToggleManagerCompatibility.this.mContext.getPackageManager();
                int i = 0;
                List<ResolveInfo> services2 = pm3.queryIntentServicesAsUser(new Intent(TileService.ACTION_QS_TILE), 0, ToggleManagerCompatibility.getUserId(ToggleManagerCompatibility.this.mContext));
                if (services2.size() == 0) {
                    return;
                }
                for (ResolveInfo info : services2) {
                    String packageName = info.serviceInfo.packageName;
                    ComponentName componentName = new ComponentName(packageName, info.serviceInfo.name);
                    CharSequence appLabel = info.serviceInfo.applicationInfo.loadLabel(pm3);
                    String spec = ToggleManagerCompatibility.toSpec(componentName);
                    if (info.serviceInfo.icon != 0 || info.serviceInfo.applicationInfo.icon != 0) {
                        if (permission.BIND_QUICK_SETTINGS_TILE.equals(info.serviceInfo.permission)) {
                            int flags = 786432;
                            try {
                                int icon;
                                if (ToggleManagerCompatibility.isSystemApp(pm3, componentName)) {
                                    flags = 786432 | 512;
                                }
                                ServiceInfo serviceInfo = AppGlobals.getPackageManager().getServiceInfo(componentName, flags, ToggleManagerCompatibility.getUserId(ToggleManagerCompatibility.this.mContext));
                                if (serviceInfo.icon != 0) {
                                    try {
                                        icon = serviceInfo.icon;
                                    } catch (Exception e2) {
                                        e = e2;
                                        pm = pm3;
                                        services = services2;
                                        pm2 = i;
                                    }
                                } else {
                                    icon = serviceInfo.applicationInfo.icon;
                                }
                                Icon iconDrawable = Icon.createWithResource(packageName, icon);
                                CharSequence label = info.serviceInfo.loadLabel(pm3);
                                if (iconDrawable == null || label == null) {
                                    int i2 = flags;
                                    i = i;
                                    services2 = services2;
                                    pm3 = pm3;
                                } else {
                                    try {
                                        Integer id = Integer.valueOf(ToggleManagerCompatibility.getMd5Num(spec) + 10000);
                                        ToggleManagerCompatibility.mCustomToggleComponentNames.put(id, spec);
                                        pm = pm3;
                                        try {
                                            ToggleManagerCompatibility.mCustomToggleLabelNames.put(id, label.toString());
                                            ToggleManagerCompatibility.mCustomIds.add(id);
                                            services = services2;
                                            pm2 = false;
                                            try {
                                                ToggleManagerCompatibility.mCustomToggleStatus.put(id, Boolean.valueOf(false));
                                                ToggleManagerCompatibility.mCustomToggleImages.put(id, iconDrawable.loadDrawable(ToggleManagerCompatibility.this.mContext));
                                            } catch (Exception e3) {
                                                e = e3;
                                            }
                                        } catch (Exception e4) {
                                            e = e4;
                                            services = services2;
                                            pm2 = false;
                                            e.printStackTrace();
                                            i = pm2;
                                            services2 = services;
                                            pm3 = pm;
                                        }
                                    } catch (Exception e5) {
                                        e = e5;
                                        pm = pm3;
                                        services = services2;
                                        pm2 = false;
                                        e.printStackTrace();
                                        i = pm2;
                                        services2 = services;
                                        pm3 = pm;
                                    }
                                    i = pm2;
                                    services2 = services;
                                    pm3 = pm;
                                }
                            } catch (Exception e6) {
                                e = e6;
                                pm = pm3;
                                services = services2;
                                pm2 = i;
                                e.printStackTrace();
                                i = pm2;
                                services2 = services;
                                pm3 = pm;
                            }
                        }
                    }
                }
                services = services2;
                ArrayList<Integer> toggles = ToggleManagerCompatibility.getUserSelectedToggleOrder(ToggleManagerCompatibility.this.mContext);
                Iterator it = ToggleManagerCompatibility.mCustomIds.iterator();
                while (it.hasNext()) {
                    Integer customId = (Integer) it.next();
                    if (!toggles.contains(customId)) {
                        toggles.add(customId);
                    }
                }
                ToggleManagerCompatibility.this.setUserSelectedToggleOrder(toggles);
                ToggleManagerCompatibility.this.mHandler.post(new Runnable() {
                    public void run() {
                        for (OnCustomToggleChangedListener onCustomToggleChangedListener : ToggleManagerCompatibility.this.mCustomToggleChangedListeners) {
                            onCustomToggleChangedListener.onCustomToggleChanged();
                        }
                    }
                });
                ToggleManagerCompatibility.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        for (OnToggleOrderChangedListener toggleOrderChangedListener : ToggleManagerCompatibility.this.mToggleOrderChangedListener) {
                            toggleOrderChangedListener.OnToggleOrderChanged();
                        }
                    }
                }, 300);
            }
        }
    }

    public interface OnCustomToggleChangedListener {
        void onCustomToggleChanged();
    }

    public static class Point {
        public int mHeight;
        public int mWidth;
    }

    public static ToggleManagerCompatibility createInstance(Context context) {
        if (mToggleManagerCompatibility == null) {
            mToggleManagerCompatibility = new ToggleManagerCompatibility(context.getApplicationContext());
        }
        return mToggleManagerCompatibility;
    }

    private ToggleManagerCompatibility(Context context) {
        this.mContext = context;
        if (mToggleManager == null) {
            mToggleManager = ToggleManager.createInstance(context);
        }
        this.mBgThread = new HandlerThread(TAG, 10);
        this.mBgThread.start();
        this.mBgHandler = new Handler(this.mBgThread.getLooper());
        this.mResolver = context.getContentResolver();
        mIsSystemUI = PROCESS_NAME_SYSTEM_UI.equals(context.getApplicationInfo().packageName);
        this.mToggleChangedListener = new ArrayList();
        this.mToggleOrderChangedListener = new ArrayList();
        this.mCustomToggleChangedListeners = new ArrayList();
        mCustomToggleOnColor = context.getResources().getColor(R.color.status_bar_toggle_off_color);
        mCustomToggleOffColor = context.getResources().getColor(R.color.status_bar_toggle_on_color);
        registerListener(mIsSystemUI);
    }

    public static void resetInstance() {
        mToggleManagerCompatibility = null;
    }

    public void queryCustomToggles() {
        if (!mEnableCustom) {
            return;
        }
        if (mIsSystemUI) {
            queryCustomToggles(true);
        } else {
            queryCustomToggles(false);
        }
    }

    private void queryCustomToggles(boolean delayMore) {
        this.mBgHandler.removeCallbacks(this.mCustomToggleQueryRunnable);
        if (delayMore) {
            this.mBgHandler.postDelayed(this.mCustomToggleQueryRunnable, 300);
        } else {
            this.mBgHandler.postDelayed(this.mCustomToggleQueryRunnable, 200);
        }
    }

    public static String toSpec(ComponentName name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("custom(");
        stringBuilder.append(name.flattenToShortString());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private void registerListener(boolean isSystemUI) {
        int userId = isSystemUI ? -1 : UserHandle.myUserId();
        UserHandle user = mIsSystemUI ? UserHandle.ALL : new UserHandle(getUserId(this.mContext));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mPackageChangeReceiver, user, intentFilter, null, null);
        this.mResolver.registerContentObserver(Global.getUriFor("development_settings_enabled"), false, this.mDevelopmentObserver, userId);
        this.mResolver.registerContentObserver(System.getUriFor(MiuiSettings.System.STATUS_BAR_TOGGLE_PAGE), false, this.mTogglOrderObserver, userId);
        this.mResolver.registerContentObserver(System.getUriFor(MiuiSettings.System.STATUS_BAR_TOGGLE_LIST), false, this.mTogglOrderObserver, userId);
    }

    public static boolean isValid(Context context, int id) {
        synchronized (mObjectLock) {
            boolean contains;
            if (id >= 10000) {
                contains = mCustomIds.contains(Integer.valueOf(id));
                return contains;
            }
            ToggleManager toggleManager = mToggleManager;
            contains = ToggleManager.isValid(context, id);
            return contains;
        }
    }

    public static void onCustomTileChanged(String componentNames, boolean add, Context context) {
        synchronized (mObjectLock) {
            if (!add) {
                if (mCustomToggleComponentNames.containsValue(componentNames)) {
                    for (Integer id : mCustomToggleComponentNames.keySet()) {
                        if (((String) mCustomToggleComponentNames.get(id)).equals(componentNames)) {
                            mCustomIds.remove(id);
                            mCustomToggleImages.remove(id);
                            mCustomToggleComponentNames.remove(id);
                            mCustomToggleLabelNames.remove(id);
                            updateUserSelectedToggleOrder(context);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void updateCustomToggleImageAndText(ComponentName componentName, Drawable drawable, String labelName, List<Integer> toggleIds, boolean toggleStatus) {
        synchronized (mObjectLock) {
            String customToggleSpec = toSpec(componentName);
            if (mCustomToggleComponentNames.containsValue(customToggleSpec)) {
                Iterator it = mCustomIds.iterator();
                while (it.hasNext()) {
                    Integer id = (Integer) it.next();
                    if (((String) mCustomToggleComponentNames.get(id)).equals(customToggleSpec)) {
                        if (toggleIds != null) {
                            if (toggleIds.contains(id)) {
                                mCustomToggleImages.put(id, drawable);
                                mCustomToggleLabelNames.put(id, labelName);
                                updateToggleStatus(id.intValue(), toggleStatus);
                                if (this.mToggleChangedListener.size() > 0) {
                                    for (int i = this.mToggleChangedListener.size() - 1; i >= 0; i--) {
                                        OnToggleChangedListener l = (OnToggleChangedListener) ((WeakReference) this.mToggleChangedListener.get(i)).get();
                                        if (l == null) {
                                            this.mToggleChangedListener.remove(i);
                                        } else {
                                            l.OnToggleChanged(id.intValue());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getToggleList(Context context) {
        String settingId;
        int userId = getUserId(context);
        if (isListStyle(context, userId)) {
            settingId = MiuiSettings.System.STATUS_BAR_TOGGLE_LIST;
        } else {
            settingId = MiuiSettings.System.STATUS_BAR_TOGGLE_PAGE;
        }
        return System.getStringForUser(context.getContentResolver(), settingId, userId);
    }

    public static ArrayList<Integer> getCustomToggleIds(Context context) {
        ArrayList<Integer> customIds = new ArrayList();
        String toggleList = getToggleList(context);
        if (!TextUtils.isEmpty(toggleList)) {
            String[] toggles = toggleList.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            int i = 0;
            while (i < toggles.length) {
                try {
                    int id = Integer.valueOf(toggles[i]).intValue();
                    if (id >= 10000) {
                        customIds.add(Integer.valueOf(id));
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    customIds.clear();
                }
            }
        }
        return customIds;
    }

    public static void updateUserSelectedToggleOrder(Context context) {
        String settingId;
        boolean updateChange = false;
        if (isListStyle(context, getUserId(context))) {
            settingId = MiuiSettings.System.STATUS_BAR_TOGGLE_LIST;
        } else {
            settingId = MiuiSettings.System.STATUS_BAR_TOGGLE_PAGE;
        }
        String toggleList = getToggleList(context);
        ArrayList<Integer> result = new ArrayList();
        boolean isEmpty = TextUtils.isEmpty(toggleList);
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (!isEmpty) {
            String[] toggles = toggleList.split(str);
            int i = 0;
            while (i < toggles.length) {
                try {
                    int id = Integer.valueOf(toggles[i]).intValue();
                    if (id >= 10000) {
                        result.add(Integer.valueOf(id));
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    result.clear();
                }
            }
        }
        Iterator it = result.iterator();
        while (it.hasNext()) {
            Integer id2 = (Integer) it.next();
            if (!mCustomIds.contains(id2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(id2);
                stringBuilder.append(str);
                toggleList = toggleList.replace(stringBuilder.toString(), "");
                updateChange = true;
            }
        }
        if (updateChange) {
            System.putStringForUser(context.getContentResolver(), settingId, toggleList, getUserId(context));
        }
    }

    public static boolean isListStyle(Context context, int userId) {
        return System.getIntForUser(context.getContentResolver(), MiuiSettings.System.STATUS_BAR_STYLE, 0, userId) == 0;
    }

    private static int getUserId(Context context) {
        return PROCESS_NAME_SYSTEM_UI.equals(context.getApplicationInfo().packageName) ? ActivityManager.getCurrentUser() : UserHandle.myUserId();
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context) {
        return getUserSelectedToggleOrder(context, getUserId(context));
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context, boolean listStyle) {
        return getUserSelectedToggleOrder(context, listStyle, getUserId(context));
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context, int userId) {
        return getUserSelectedToggleOrder(context, isListStyle(context, userId), userId);
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context, boolean listStyle, int userId) {
        ArrayList<Integer> result;
        synchronized (mObjectLock) {
            String[] toggles;
            result = new ArrayList();
            String toggleList = System.getStringForUser(context.getContentResolver(), listStyle ? MiuiSettings.System.STATUS_BAR_TOGGLE_LIST : MiuiSettings.System.STATUS_BAR_TOGGLE_PAGE, userId);
            if (!TextUtils.isEmpty(toggleList)) {
                toggles = toggleList.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                int i = 0;
                while (i < toggles.length) {
                    try {
                        int id = Integer.valueOf(toggles[i]).intValue();
                        if (id >= 10000) {
                            if (mCustomIds.contains(Integer.valueOf(id))) {
                                result.add(Integer.valueOf(id));
                            }
                        } else if (ToggleManager.getName(id) != 0) {
                            result.add(Integer.valueOf(id));
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.clear();
                    }
                }
            }
            toggles = mToggleManager;
            ToggleManager.validateToggleOrder(context, result, isListStyle(context, getUserId(context)), getUserId(context));
        }
        return result;
    }

    public static ArrayList<String> getCustomToggleTileSpecs(Context context) {
        ArrayList<String> result;
        synchronized (mObjectLock) {
            result = new ArrayList();
            Iterator it = mCustomIds.iterator();
            while (it.hasNext()) {
                result.add((String) mCustomToggleComponentNames.get((Integer) it.next()));
            }
        }
        return result;
    }

    public static String getCustomToggleTileSpecById(Integer id) {
        String str;
        synchronized (mObjectLock) {
            str = (String) mCustomToggleComponentNames.get(id);
        }
        return str;
    }

    public void setOnToggleOrderChangeListener(OnToggleOrderChangedListener l) {
        if (!this.mToggleOrderChangedListener.contains(l)) {
            this.mToggleOrderChangedListener.add(l);
        }
        ToggleManager toggleManager = mToggleManager;
        if (toggleManager != null) {
            toggleManager.setOnToggleOrderChangeListener(l);
        }
    }

    public void setOnToggleChangedListener(OnToggleChangedListener l) {
        this.mToggleChangedListener.add(new WeakReference(l));
        ToggleManager toggleManager = mToggleManager;
        if (toggleManager != null) {
            toggleManager.setOnToggleChangedListener(l);
        }
    }

    public void setOnCustomToggleChangeListener(OnCustomToggleChangedListener l) {
        if (!this.mCustomToggleChangedListeners.contains(l)) {
            this.mCustomToggleChangedListeners.add(l);
        }
    }

    public void removeToggleChangedListener(OnToggleChangedListener l) {
        if (this.mToggleChangedListener.contains(l)) {
            this.mCustomToggleChangedListeners.remove(l);
        }
        ToggleManager toggleManager = mToggleManager;
        if (toggleManager != null) {
            toggleManager.removeToggleChangedListener(l);
        }
    }

    public void removeToggleOrderChangeListener(OnToggleOrderChangedListener l) {
        if (this.mToggleOrderChangedListener.contains(l)) {
            this.mToggleOrderChangedListener.remove(l);
        }
        ToggleManager toggleManager = mToggleManager;
        if (toggleManager != null) {
            toggleManager.removeToggleOrderChangeListener(l);
        }
    }

    public void removeCustomToggleChangeListener(OnCustomToggleChangedListener l) {
        if (this.mCustomToggleChangedListeners.contains(l)) {
            this.mCustomToggleChangedListeners.remove(l);
        }
    }

    public void onDestroy() {
        try {
            this.mContext.unregisterReceiver(this.mPackageChangeReceiver);
            this.mResolver.unregisterContentObserver(this.mDevelopmentObserver);
            this.mResolver.unregisterContentObserver(this.mTogglOrderObserver);
            if (mToggleManager != null) {
                mToggleManager.onDestroy();
                try {
                    Field field = mToggleManager.getClass().getDeclaredField("sToggleManager");
                    field.setAccessible(true);
                    field.set(mToggleManager, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (this.mToggleOrderChangedListener != null) {
                this.mToggleOrderChangedListener.clear();
            }
            if (this.mToggleChangedListener != null) {
                this.mToggleChangedListener.clear();
            }
            if (this.mCustomToggleChangedListeners != null) {
                this.mCustomToggleChangedListeners.clear();
            }
            if (mCustomToggleLabelNames != null) {
                mCustomToggleLabelNames.clear();
            }
            if (mCustomToggleComponentNames != null) {
                mCustomToggleComponentNames.clear();
            }
            if (mCustomToggleImages != null) {
                mCustomToggleImages.clear();
            }
            if (mCustomToggleStatus != null) {
                mCustomToggleStatus.clear();
            }
            if (this.mBgThread != null) {
                this.mBgThread.quit();
            }
            if (this.mBgHandler != null) {
                this.mBgHandler.removeCallbacksAndMessages(null);
            }
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages(null);
            }
            mToggleManager = null;
            resetInstance();
        } catch (Exception e2) {
        }
    }

    public static String getStatusName(int id, Resources res) {
        String statusName;
        synchronized (mObjectLock) {
            statusName = "";
            if (id >= 10000) {
                statusName = (String) mCustomToggleLabelNames.get(Integer.valueOf(id));
            } else {
                ToggleManager toggleManager = mToggleManager;
                statusName = ToggleManager.getStatusName(id, res);
            }
        }
        return statusName;
    }

    public static boolean getStatus(int id) {
        synchronized (mObjectLock) {
            boolean booleanValue;
            if (id >= 10000) {
                try {
                    booleanValue = ((Boolean) mCustomToggleStatus.get(Integer.valueOf(id))).booleanValue();
                    return booleanValue;
                } catch (Exception e) {
                    return false;
                }
            }
            Exception e2 = mToggleManager;
            booleanValue = ToggleManager.getStatus(id);
            return booleanValue;
        }
    }

    public static boolean isDisabled(int id) {
        if (id >= 10000) {
            return false;
        }
        ToggleManager toggleManager = mToggleManager;
        return ToggleManager.isDisabled(id);
    }

    protected static void updateToggleStatus(int id, boolean isOpen) {
        synchronized (mObjectLock) {
            if (id >= 10000) {
                mCustomToggleStatus.put(Integer.valueOf(id), Boolean.valueOf(isOpen));
            } else {
                mToggleManager.updateToggleStatus(id, isOpen);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void updateToggleDisabled(int id, boolean disabled) {
        if (id < 10000) {
            mToggleManager.updateToggleDisabled(id, disabled);
        }
    }

    public static Drawable getImageDrawable(int id, Context context) {
        return getImageDrawable(id, context, 0);
    }

    public static void updateTextView(int id, TextView textView) {
        if (textView != null) {
            textView.setText(getStatusName(id, textView.getResources()));
        }
    }

    public static Drawable getImageDrawable(int id, Context context, int color) {
        return getImageDrawable(id, getStatus(id), context, color, mCustomToggleOnColor, mCustomToggleOffColor);
    }

    public static Drawable getImageDrawable(int id, boolean isOpen, Context context) {
        return getImageDrawable(id, isOpen, context, 0, mCustomToggleOnColor, mCustomToggleOffColor);
    }

    /* JADX WARNING: Missing block: B:54:0x015d, code skipped:
            return r0;
     */
    public static android.graphics.drawable.Drawable getImageDrawable(int r15, boolean r16, android.content.Context r17, int r18, int r19, int r20) {
        /*
        r1 = r15;
        r2 = r17;
        r3 = mObjectLock;
        monitor-enter(r3);
        r0 = 0;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        if (r1 < r4) goto L_0x0027;
    L_0x000b:
        r5 = mCustomToggleImages;	 Catch:{ all -> 0x015e }
        r6 = java.lang.Integer.valueOf(r15);	 Catch:{ all -> 0x015e }
        r5 = r5.get(r6);	 Catch:{ all -> 0x015e }
        if (r5 == 0) goto L_0x0025;
    L_0x0017:
        r5 = mCustomToggleImages;	 Catch:{ all -> 0x015e }
        r6 = java.lang.Integer.valueOf(r15);	 Catch:{ all -> 0x015e }
        r5 = r5.get(r6);	 Catch:{ all -> 0x015e }
        r5 = (android.graphics.drawable.Drawable) r5;	 Catch:{ all -> 0x015e }
        r0 = r5;
        goto L_0x002e;
    L_0x0025:
        monitor-exit(r3);	 Catch:{ all -> 0x015e }
        return r0;
    L_0x0027:
        r5 = mToggleManager;	 Catch:{ all -> 0x015e }
        r5 = miui.app.ToggleManager.getImageDrawable(r15, r16, r17);	 Catch:{ all -> 0x015e }
        r0 = r5;
    L_0x002e:
        r5 = 4612811918334230528; // 0x4004000000000000 float:0.0 double:2.5;
        r7 = 0;
        if (r16 != 0) goto L_0x00c6;
    L_0x0033:
        if (r1 < r4) goto L_0x00be;
    L_0x0035:
        r4 = r17.getResources();	 Catch:{ all -> 0x00b9 }
        r8 = 285671803; // 0x1107017b float:1.0650078E-28 double:1.41140624E-315;
        r4 = r4.getDrawable(r8);	 Catch:{ all -> 0x00b9 }
        r8 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x00b9 }
        r9 = r4.getIntrinsicHeight();	 Catch:{ all -> 0x00b9 }
        r10 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ all -> 0x00b9 }
        r8 = android.graphics.Bitmap.createBitmap(r8, r9, r10);	 Catch:{ all -> 0x00b9 }
        r9 = new android.graphics.Canvas;	 Catch:{ all -> 0x00b9 }
        r9.<init>(r8);	 Catch:{ all -> 0x00b9 }
        r10 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x00b9 }
        r11 = r4.getIntrinsicHeight();	 Catch:{ all -> 0x00b9 }
        r4.setBounds(r7, r7, r10, r11);	 Catch:{ all -> 0x00b9 }
        r4.draw(r9);	 Catch:{ all -> 0x00b9 }
        r7 = new miui.app.ToggleManagerCompatibility$Point;	 Catch:{ all -> 0x00b9 }
        r7.<init>();	 Catch:{ all -> 0x00b9 }
        getTargetDrawableSize(r2, r7);	 Catch:{ all -> 0x00b9 }
        r10 = r7.mWidth;	 Catch:{ all -> 0x00b9 }
        r10 = (double) r10;	 Catch:{ all -> 0x00b9 }
        r10 = r10 / r5;
        r10 = (int) r10;	 Catch:{ all -> 0x00b9 }
        r11 = r7.mHeight;	 Catch:{ all -> 0x00b9 }
        r11 = (double) r11;	 Catch:{ all -> 0x00b9 }
        r11 = r11 / r5;
        r5 = (int) r11;	 Catch:{ all -> 0x00b9 }
        r5 = zoomDrawable(r2, r0, r10, r5);	 Catch:{ all -> 0x00b9 }
        r0 = r5;
        if (r0 == 0) goto L_0x00b1;
    L_0x007a:
        r5 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x00b9 }
        r6 = r0.getIntrinsicWidth();	 Catch:{ all -> 0x00b9 }
        r5 = r5 - r6;
        r5 = r5 / 2;
        r6 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x00b9 }
        r6 = r6 - r5;
        r10 = r4.getIntrinsicHeight();	 Catch:{ all -> 0x00b9 }
        r10 = r10 - r5;
        r0.setBounds(r5, r5, r6, r10);	 Catch:{ all -> 0x00b9 }
        r6 = android.graphics.Color.alpha(r18);	 Catch:{ all -> 0x00b9 }
        if (r6 != 0) goto L_0x00a1;
    L_0x0099:
        r6 = android.graphics.PorterDuff.Mode.SRC_IN;	 Catch:{ all -> 0x00b9 }
        r10 = r20;
        r0.setColorFilter(r10, r6);	 Catch:{ all -> 0x0152 }
        goto L_0x00a3;
    L_0x00a1:
        r10 = r20;
    L_0x00a3:
        r0.draw(r9);	 Catch:{ all -> 0x0152 }
        r6 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ all -> 0x0152 }
        r11 = r17.getResources();	 Catch:{ all -> 0x0152 }
        r6.<init>(r11, r8);	 Catch:{ all -> 0x0152 }
        r0 = r6;
        goto L_0x00b3;
    L_0x00b1:
        r10 = r20;
    L_0x00b3:
        r11 = r18;
        r12 = r19;
        goto L_0x015c;
    L_0x00b9:
        r0 = move-exception;
        r10 = r20;
        goto L_0x0153;
    L_0x00be:
        r10 = r20;
        r11 = r18;
        r12 = r19;
        goto L_0x015c;
    L_0x00c6:
        r10 = r20;
        if (r1 < r4) goto L_0x0158;
    L_0x00ca:
        r4 = r17.getResources();	 Catch:{ all -> 0x0152 }
        r8 = 285671804; // 0x1107017c float:1.065008E-28 double:1.411406243E-315;
        r4 = r4.getDrawable(r8);	 Catch:{ all -> 0x0152 }
        r8 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x0152 }
        r9 = r4.getIntrinsicHeight();	 Catch:{ all -> 0x0152 }
        r11 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ all -> 0x0152 }
        r8 = android.graphics.Bitmap.createBitmap(r8, r9, r11);	 Catch:{ all -> 0x0152 }
        r9 = new android.graphics.Canvas;	 Catch:{ all -> 0x0152 }
        r9.<init>(r8);	 Catch:{ all -> 0x0152 }
        r11 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x0152 }
        r12 = r4.getIntrinsicHeight();	 Catch:{ all -> 0x0152 }
        r4.setBounds(r7, r7, r11, r12);	 Catch:{ all -> 0x0152 }
        r7 = android.graphics.Color.alpha(r18);	 Catch:{ all -> 0x0152 }
        if (r7 == 0) goto L_0x0101;
    L_0x00f9:
        r7 = android.graphics.PorterDuff.Mode.SRC_IN;	 Catch:{ all -> 0x0152 }
        r11 = r18;
        r4.setColorFilter(r11, r7);	 Catch:{ all -> 0x0150 }
        goto L_0x0103;
    L_0x0101:
        r11 = r18;
    L_0x0103:
        r4.draw(r9);	 Catch:{ all -> 0x0150 }
        r7 = new miui.app.ToggleManagerCompatibility$Point;	 Catch:{ all -> 0x0150 }
        r7.<init>();	 Catch:{ all -> 0x0150 }
        getTargetDrawableSize(r2, r7);	 Catch:{ all -> 0x0150 }
        r12 = r7.mWidth;	 Catch:{ all -> 0x0150 }
        r12 = (double) r12;	 Catch:{ all -> 0x0150 }
        r12 = r12 / r5;
        r12 = (int) r12;	 Catch:{ all -> 0x0150 }
        r13 = r7.mHeight;	 Catch:{ all -> 0x0150 }
        r13 = (double) r13;	 Catch:{ all -> 0x0150 }
        r13 = r13 / r5;
        r5 = (int) r13;	 Catch:{ all -> 0x0150 }
        r5 = zoomDrawable(r2, r0, r12, r5);	 Catch:{ all -> 0x0150 }
        r0 = r5;
        if (r0 == 0) goto L_0x014d;
    L_0x011f:
        r5 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x0150 }
        r6 = r0.getIntrinsicWidth();	 Catch:{ all -> 0x0150 }
        r5 = r5 - r6;
        r5 = r5 / 2;
        r6 = r4.getIntrinsicWidth();	 Catch:{ all -> 0x0150 }
        r6 = r6 - r5;
        r12 = r4.getIntrinsicHeight();	 Catch:{ all -> 0x0150 }
        r12 = r12 - r5;
        r0.setBounds(r5, r5, r6, r12);	 Catch:{ all -> 0x0150 }
        r6 = android.graphics.PorterDuff.Mode.SRC_IN;	 Catch:{ all -> 0x0150 }
        r12 = r19;
        r0.setColorFilter(r12, r6);	 Catch:{ all -> 0x0167 }
        r0.draw(r9);	 Catch:{ all -> 0x0167 }
        r6 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ all -> 0x0167 }
        r13 = r17.getResources();	 Catch:{ all -> 0x0167 }
        r6.<init>(r13, r8);	 Catch:{ all -> 0x0167 }
        r0 = r6;
        goto L_0x015c;
    L_0x014d:
        r12 = r19;
        goto L_0x015c;
    L_0x0150:
        r0 = move-exception;
        goto L_0x0155;
    L_0x0152:
        r0 = move-exception;
    L_0x0153:
        r11 = r18;
    L_0x0155:
        r12 = r19;
        goto L_0x0165;
    L_0x0158:
        r11 = r18;
        r12 = r19;
    L_0x015c:
        monitor-exit(r3);	 Catch:{ all -> 0x0167 }
        return r0;
    L_0x015e:
        r0 = move-exception;
        r11 = r18;
        r12 = r19;
        r10 = r20;
    L_0x0165:
        monitor-exit(r3);	 Catch:{ all -> 0x0167 }
        throw r0;
    L_0x0167:
        r0 = move-exception;
        goto L_0x0165;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.app.ToggleManagerCompatibility.getImageDrawable(int, boolean, android.content.Context, int, int, int):android.graphics.drawable.Drawable");
    }

    public static void getTargetDrawableSize(Context context, Point point) {
        int densityDPI = context.getResources().getDisplayMetrics().densityDpi;
        if (densityDPI == 320) {
            point.mHeight = 94;
            point.mWidth = 94;
        } else if (densityDPI == 480) {
            point.mHeight = 130;
            point.mWidth = 130;
        } else if (densityDPI != 640) {
            point.mHeight = 130;
            point.mWidth = 130;
        } else {
            point.mHeight = 150;
            point.mWidth = 150;
        }
    }

    public static Drawable zoomDrawable(Context context, Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        if (oldbmp == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true));
    }

    public static void updateImageView(int id, ImageView imageView) {
        updateImageView(id, imageView, 0);
    }

    public static void updateImageView(int id, ImageView imageView, int color) {
        if (imageView != null) {
            Drawable drawable = getImageDrawable(id, imageView.getContext(), color);
            if (id < 10000 && drawable != null && Color.alpha(color) != 0) {
                drawable.setColorFilter(color, Mode.SRC_IN);
            } else if (!(id < 10000 || getStatus(id) || drawable == null || Color.alpha(color) == 0)) {
                drawable.setColorFilter(color, Mode.SRC_IN);
            }
            imageView.setImageDrawable(drawable);
        }
    }

    public boolean performToggle(int id) {
        if (id >= 10000) {
            return true;
        }
        return mToggleManager.performToggle(id);
    }

    public static void initDrawable(int id, Drawable drawable) {
    }

    private static int getMd5Num(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(KeyProperties.DIGEST_MD5);
            digest.update(input.getBytes());
            return Math.abs(new BigInteger(1, digest.digest()).intValue());
        } catch (NoSuchAlgorithmException lException) {
            throw new RuntimeException(lException);
        }
    }

    public void setUserSelectedToggleOrder(ArrayList<Integer> list) {
        mToggleManager.setUserSelectedToggleOrder(list);
    }

    public static boolean isInternationalBuilder() {
        return Build.IS_INTERNATIONAL_BUILD;
    }

    private static boolean isSystemApp(PackageManager pm, ComponentName component) {
        boolean z = false;
        try {
            z = pm.getApplicationInfo(component.getPackageName(), 0).isSystemApp();
            return z;
        } catch (Exception e) {
            return z;
        }
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width == 0 || height == 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
