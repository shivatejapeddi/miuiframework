package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.VoiceInteractor.PickOptionRequest;
import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.app.VoiceInteractor.Prompt;
import android.app.slice.Slice;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.BrowserContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.System;
import android.text.Html;
import android.text.TextUtils;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.AlertController.AlertParams;
import com.android.internal.content.PackageMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.widget.MaskLayout;
import com.android.internal.widget.ResolverDrawerLayout;
import com.android.internal.widget.ResolverDrawerLayout.OnDismissedListener;
import com.miui.mishare.app.connect.MiShareConnectivity;
import com.miui.mishare.app.view.MiShareTransmissionView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import miui.app.AlertActivity;
import miui.app.ToggleManager;
import miui.os.Build;
import miui.securityspace.XSpaceResolverActivityHelper;
import miui.securityspace.XSpaceUserHandle;
import miui.widget.ScreenView;

public class ResolverActivity extends AlertActivity {
    private static final boolean DEBUG = false;
    private static final String EXTRA_RESOLVE_INFOS = "rlist";
    private static final int MAX_OFFICAL_RECOMMEND_PER_SCREEN = 4;
    private static final int MAX_PER_SCREEN = 8;
    private static final Set<String> PRIV_PACKAGES = new HashSet();
    private static final String TAG = "ResolverActivity";
    private static final String WPS_LITE_PKG_NAME = "cn.wps.moffice_eng.xiaomi.lite";
    private static final String WPS_PKG_NAME = "cn.wps.moffice_eng";
    private static final ArrayList<String> XSPACE_NOT_SHOW_LIST = new ArrayList();
    private final String META_KEY_RESOLVER_ITEM_SELECT = "com.miui.action.resolver_activity_item_select";
    private final String META_KEY_RESOLVER_SHOWN = "com.miui.action.resolver_activity_shown";
    private final String PACKAGE_NAME_BROWSER = BrowserContract.AUTHORITY;
    private final String PACKAGE_NAME_VIDEOPLAYER = "com.miui.video";
    protected ResolveListAdapter mAdapter;
    private AbsListView mAdapterView;
    private Button mAlwaysButton;
    private boolean mAlwaysUseOption;
    private int mChosenIndex;
    private View mChosenView;
    private int mDefaultTitleResId;
    private ComponentName[] mFilteredComponents;
    private int mIconDpi;
    IconDrawableFactory mIconFactory;
    private int mIconSize;
    private final ArrayList<Intent> mIntents = new ArrayList();
    private int mLastSelected = -1;
    protected int mLaunchedFromUid;
    private int mLayoutId;
    private int mMaskColor;
    private int mMaxPerScreen = 8;
    private MiShareTransmissionView mMiShareView;
    private ImageView mOfficalRecommendIconIV;
    private TextView mOfficalRecommendNameTV;
    private RelativeLayout mOfficalRecommendRl;
    private Button mOnceButton;
    private final PackageMonitor mPackageMonitor = new PackageMonitor() {
        public void onSomePackagesChanged() {
            ResolverActivity.this.mAdapter.handlePackagesChanged();
            if (ResolverActivity.this.mProfileView != null) {
                ResolverActivity.this.bindProfileView();
            }
        }

        public boolean onPackageChanged(String packageName, int uid, String[] components) {
            return true;
        }
    };
    private PickTargetOptionRequest mPickOptionRequest;
    protected PackageManager mPm;
    private Runnable mPostListReadyRunnable;
    private int mProfileSwitchMessageId = -1;
    private View mProfileView;
    private boolean mRecommendFirstItem;
    private RelativeLayout mRecommendOfficalLayout;
    private boolean mRecommendable = false;
    private String mReferrerPackage;
    private boolean mRegistered;
    protected ResolverDrawerLayout mResolverDrawerLayout;
    private TextView mResolverRecommendTagTv;
    private boolean mResolvingHome = false;
    private boolean mRetainInOnStop;
    private boolean mSafeForwardingMode;
    private ScreenView mScreenView;
    private boolean mShowMoreResolver = false;
    private boolean mShowMoreResolverButton = true;
    private CharSequence mTitle;

    public class ResolveListAdapter extends BaseAdapter {
        private final List<ResolveInfo> mBaseResolveList;
        List<DisplayResolveInfo> mDisplayList;
        private boolean mFilterLastUsed;
        private boolean mHasExtendedInfo;
        protected final LayoutInflater mInflater;
        private final Intent[] mInitialIntents;
        private final List<Intent> mIntents;
        protected ResolveInfo mLastChosen;
        private int mLastChosenPosition = -1;
        private DisplayResolveInfo mOtherProfile;
        private int mPlaceholderCount;
        private ResolverListController mResolverListController;
        List<ResolvedComponentInfo> mUnfilteredResolveList;

        public ResolveListAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed, ResolverListController resolverListController) {
            this.mIntents = payloadIntents;
            this.mInitialIntents = initialIntents;
            this.mBaseResolveList = rList;
            ResolverActivity.this.mLaunchedFromUid = launchedFromUid;
            this.mInflater = LayoutInflater.from(context);
            this.mDisplayList = new ArrayList();
            this.mFilterLastUsed = filterLastUsed;
            this.mResolverListController = resolverListController;
        }

        public void handlePackagesChanged() {
            rebuildList();
            if (getCount() == 0) {
                ResolverActivity.this.finish();
            }
        }

        public void setPlaceholderCount(int count) {
            this.mPlaceholderCount = count;
        }

        public DisplayResolveInfo getFilteredItem() {
            if (this.mFilterLastUsed) {
                int i = this.mLastChosenPosition;
                if (i >= 0) {
                    return (DisplayResolveInfo) this.mDisplayList.get(i);
                }
            }
            return null;
        }

        public DisplayResolveInfo getOtherProfile() {
            return this.mOtherProfile;
        }

        public int getFilteredPosition() {
            if (this.mFilterLastUsed) {
                int i = this.mLastChosenPosition;
                if (i >= 0) {
                    return i;
                }
            }
            return -1;
        }

        public boolean hasFilteredItem() {
            return this.mFilterLastUsed && this.mLastChosenPosition >= 0;
        }

        public float getScore(DisplayResolveInfo target) {
            return this.mResolverListController.getScore(target);
        }

        public void updateModel(ComponentName componentName) {
            this.mResolverListController.updateModel(componentName);
        }

        public void updateChooserCounts(String packageName, int userId, String action) {
            this.mResolverListController.updateChooserCounts(packageName, userId, action);
        }

        /* Access modifiers changed, original: protected */
        public boolean rebuildList() {
            List<ResolvedComponentInfo> currentResolveList;
            String str = ResolverActivity.TAG;
            try {
                Intent primaryIntent = ResolverActivity.this.getTargetIntent();
                this.mLastChosen = AppGlobals.getPackageManager().getLastChosenActivity(primaryIntent, primaryIntent.resolveTypeIfNeeded(ResolverActivity.this.getContentResolver()), 65536);
            } catch (RemoteException re) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling setLastChosenActivity\n");
                stringBuilder.append(re);
                Log.d(str, stringBuilder.toString());
            }
            this.mOtherProfile = null;
            this.mLastChosen = null;
            this.mLastChosenPosition = -1;
            this.mDisplayList.clear();
            if (this.mBaseResolveList != null) {
                ArrayList arrayList = new ArrayList();
                this.mUnfilteredResolveList = arrayList;
                currentResolveList = arrayList;
                this.mResolverListController.addResolveListDedupe(currentResolveList, ResolverActivity.this.getTargetIntent(), this.mBaseResolveList);
            } else {
                List<ResolvedComponentInfo> resolversForIntent = this.mResolverListController.getResolversForIntent(shouldGetResolvedFilter(), ResolverActivity.this.shouldGetActivityMetadata(), this.mIntents);
                this.mUnfilteredResolveList = resolversForIntent;
                currentResolveList = resolversForIntent;
                if (currentResolveList == null) {
                    processSortedList(currentResolveList);
                    return true;
                }
                resolversForIntent = this.mResolverListController.filterIneligibleActivities(currentResolveList, true);
                if (resolversForIntent != null) {
                    this.mUnfilteredResolveList = resolversForIntent;
                }
            }
            for (ResolvedComponentInfo info : currentResolveList) {
                if (info.getResolveInfoAt(0).targetUserId != -2) {
                    this.mOtherProfile = new DisplayResolveInfo(info.getIntentAt(0), info.getResolveInfoAt(0), info.getResolveInfoAt(0).loadLabel(ResolverActivity.this.mPm), info.getResolveInfoAt(0).loadLabel(ResolverActivity.this.mPm), ResolverActivity.this.getReplacementIntent(info.getResolveInfoAt(0).activityInfo, info.getIntentAt(0)));
                    currentResolveList.remove(info);
                    break;
                }
            }
            if (this.mOtherProfile == null) {
                try {
                    this.mLastChosen = this.mResolverListController.getLastChosen();
                } catch (RemoteException re2) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Error calling getLastChosenActivity\n");
                    stringBuilder2.append(re2);
                    Log.d(str, stringBuilder2.toString());
                }
            }
            int size = currentResolveList.size();
            int N = size;
            if (size > 0) {
                List<ResolvedComponentInfo> originalList = this.mResolverListController.filterLowPriority(currentResolveList, this.mUnfilteredResolveList == currentResolveList);
                if (originalList != null) {
                    this.mUnfilteredResolveList = originalList;
                }
                if (currentResolveList.size() > 1) {
                    setPlaceholderCount(currentResolveList.size());
                    new AsyncTask<List<ResolvedComponentInfo>, Void, List<ResolvedComponentInfo>>() {
                        /* Access modifiers changed, original: protected|varargs */
                        public List<ResolvedComponentInfo> doInBackground(List<ResolvedComponentInfo>... params) {
                            ResolveListAdapter.this.mResolverListController.sort(params[0]);
                            return params[0];
                        }

                        /* Access modifiers changed, original: protected */
                        public void onPostExecute(List<ResolvedComponentInfo> sortedComponents) {
                            ResolveListAdapter.this.processSortedList(sortedComponents);
                            ResolveListAdapter.this.resetAlwaysUseButton();
                            if (ResolverActivity.this.mProfileView != null) {
                                ResolverActivity.this.bindProfileView();
                            }
                            ResolveListAdapter.this.notifyDataSetChanged();
                            ResolverActivity.this.bindOfficalRecommendView();
                            ResolverActivity.this.initGridViews();
                        }
                    }.execute(currentResolveList);
                    postListReadyRunnable();
                    return false;
                }
                processSortedList(currentResolveList);
                return true;
            }
            processSortedList(currentResolveList);
            return true;
        }

        private void resetAlwaysUseButton() {
            if (ResolverActivity.this.mAlwaysUseOption && !ResolverActivity.this.mRecommendable) {
                CheckBox alwaysOption = (CheckBox) ResolverActivity.this.findViewById(R.id.always_option);
                if (alwaysOption != null) {
                    alwaysOption.setVisibility(0);
                    if (!ResolverActivity.this.mShowMoreResolverButton || !Build.IS_INTERNATIONAL_BUILD || ResolverActivity.this.mAdapter.mDisplayList == null || ResolverActivity.this.mAdapter.mDisplayList.size() <= 1) {
                        alwaysOption.setChecked(false);
                        return;
                    } else {
                        alwaysOption.setChecked(true);
                        return;
                    }
                }
                ResolverActivity.this.mAlwaysUseOption = false;
            }
        }

        private void processSortedList(List<ResolvedComponentInfo> sortedComponents) {
            List<ResolvedComponentInfo> list = sortedComponents;
            if (list != null) {
                int size = sortedComponents.size();
                int N = size;
                if (size != 0) {
                    if (this.mInitialIntents != null) {
                        size = 0;
                        while (true) {
                            Intent ii = this.mInitialIntents;
                            if (size >= ii.length) {
                                break;
                            }
                            ii = ii[size];
                            if (ii != null) {
                                ActivityInfo ai = ii.resolveActivityInfo(ResolverActivity.this.getPackageManager(), 0);
                                if (ai == null) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("No activity found for ");
                                    stringBuilder.append(ii);
                                    Log.w(ResolverActivity.TAG, stringBuilder.toString());
                                } else {
                                    ResolveInfo ri = new ResolveInfo();
                                    ri.activityInfo = ai;
                                    UserManager userManager = (UserManager) ResolverActivity.this.getSystemService("user");
                                    if (ii instanceof LabeledIntent) {
                                        LabeledIntent li = (LabeledIntent) ii;
                                        ri.resolvePackageName = li.getSourcePackage();
                                        ri.labelRes = li.getLabelResource();
                                        ri.nonLocalizedLabel = li.getNonLocalizedLabel();
                                        ri.icon = li.getIconResource();
                                        ri.iconResourceId = ri.icon;
                                    }
                                    if (userManager.isManagedProfile()) {
                                        ri.noResourceId = true;
                                        ri.icon = 0;
                                    }
                                    ResolverActivity resolverActivity = ResolverActivity.this;
                                    addResolveInfo(new DisplayResolveInfo(ii, ri, ri.loadLabel(resolverActivity.getPackageManager()), null, ii));
                                }
                            }
                            size++;
                        }
                    }
                    ResolvedComponentInfo rci0 = (ResolvedComponentInfo) list.get(0);
                    ResolveInfo r0 = rci0.getResolveInfoAt(0);
                    CharSequence r0Label = r0.loadLabel(ResolverActivity.this.mPm);
                    this.mHasExtendedInfo = false;
                    ResolvedComponentInfo rci02 = rci0;
                    ResolveInfo r02 = r0;
                    int start = 0;
                    CharSequence r0Label2 = r0Label;
                    for (int i = 1; i < N; i++) {
                        CharSequence riLabel;
                        if (r0Label2 == null) {
                            r0Label2 = r02.activityInfo.packageName;
                        }
                        ResolvedComponentInfo rci = (ResolvedComponentInfo) list.get(i);
                        ResolveInfo ri2 = rci.getResolveInfoAt(0);
                        CharSequence riLabel2 = ri2.loadLabel(ResolverActivity.this.mPm);
                        if (riLabel2 == null) {
                            riLabel = ri2.activityInfo.packageName;
                        } else {
                            riLabel = riLabel2;
                        }
                        if (!riLabel.equals(r0Label2)) {
                            CharSequence riLabel3 = riLabel;
                            ResolveInfo ri3 = ri2;
                            processGroup(sortedComponents, start, i - 1, rci02, r0Label2);
                            rci02 = rci;
                            start = i;
                            r02 = ri3;
                            r0Label2 = riLabel3;
                        }
                    }
                    processGroup(sortedComponents, start, N - 1, rci02, r0Label2);
                }
            }
            customResolver();
            if (this.mOtherProfile != null && this.mLastChosenPosition >= 0) {
                this.mLastChosenPosition = -1;
                this.mFilterLastUsed = false;
            }
            postListReadyRunnable();
        }

        private void customResolver() {
            List list = this.mDisplayList;
            if (list != null && list.size() > 1) {
                ResolverActivity.this.mShowMoreResolver = addMoreResolverButton();
                if (!ResolverActivity.this.mShowMoreResolver) {
                    for (int i = 0; i < this.mDisplayList.size(); i++) {
                        DisplayResolveInfo info = (DisplayResolveInfo) this.mDisplayList.get(i);
                        if (ResolverActivity.PRIV_PACKAGES.contains(info.mResolveInfo.activityInfo.packageName)) {
                            this.mDisplayList.add(0, info);
                            this.mDisplayList.remove(i + 1);
                        }
                    }
                }
                processRecommendedApp();
                ResolverActivity.this.sendResolverShownAnalyticsBroadcast(this.mDisplayList, hasOfficalRecommendation());
            }
        }

        private void processRecommendedApp() {
            ResolverActivity.this.mRecommendFirstItem = false;
            int i = 0;
            while (i < this.mDisplayList.size()) {
                DisplayResolveInfo info = (DisplayResolveInfo) this.mDisplayList.get(i);
                if (ResolverActivity.this.mRecommendFirstItem || !shouldRecommend(info)) {
                    i++;
                } else {
                    if (i != 0) {
                        List list = this.mDisplayList;
                        list.add(0, (DisplayResolveInfo) list.remove(i));
                    }
                    ResolverActivity.this.mRecommendFirstItem = true;
                    return;
                }
            }
        }

        private boolean shouldRecommend(DisplayResolveInfo info) {
            if ("com.miui.video".equals(info.mResolveInfo.activityInfo.packageName)) {
                return VideoTypeParseUtils.getInstance().shouldSystemVideoCanResolve(ResolverActivity.this);
            }
            if (ResolverActivity.WPS_LITE_PKG_NAME.equals(info.mResolveInfo.activityInfo.packageName)) {
                return WPSTypeParseUtils.getInstance().isDocTypeResolved(ResolverActivity.this);
            }
            Bundle meta = info.getResolveInfo().activityInfo.metaData;
            boolean z = false;
            if (meta == null) {
                return false;
            }
            String recommendEnableKey = meta.getString("mi_appchooser_reommended_enabled_key");
            if (recommendEnableKey == null) {
                return false;
            }
            if (System.getInt(ResolverActivity.this.getContentResolver(), recommendEnableKey, -1) == 1) {
                z = true;
            }
            return z;
        }

        private boolean addMoreResolverButton() {
            if (!ResolverActivity.this.mShowMoreResolverButton || !Build.IS_INTERNATIONAL_BUILD) {
                return false;
            }
            ResolverActivity resolverActivity = ResolverActivity.this;
            boolean z = hasSystemBrowser() || hasSystemVideoPlayer() || hasSystemWps();
            resolverActivity.mShowMoreResolverButton = z;
            if (!ResolverActivity.this.mShowMoreResolverButton) {
                return false;
            }
            List<DisplayResolveInfo> newDisplayList = new ArrayList();
            DisplayResolveInfo info = null;
            for (int i = 0; i < this.mDisplayList.size(); i++) {
                info = (DisplayResolveInfo) this.mDisplayList.get(i);
                if (!BrowserContract.AUTHORITY.equals(info.mResolveInfo.activityInfo.packageName)) {
                    if (!"com.miui.video".equals(info.mResolveInfo.activityInfo.packageName)) {
                        if (!ResolverActivity.WPS_LITE_PKG_NAME.equals(info.mResolveInfo.activityInfo.packageName)) {
                            if (isPlatformApp(info.mResolveInfo.activityInfo.packageName)) {
                                newDisplayList.add(0, info);
                            }
                        }
                    }
                }
                newDisplayList.add(info);
            }
            if (this.mDisplayList.size() > newDisplayList.size() && info != null) {
                ResolverActivity resolverActivity2 = ResolverActivity.this;
                newDisplayList.add(new DisplayResolveInfo(resolverActivity2, info, resolverActivity2.getResources().getDrawable(com.android.internal.R.drawable.resolver_more, ResolverActivity.this.getTheme()), ResolverActivity.this.getResources().getString(com.android.internal.R.string.more_item_label), null));
            }
            this.mDisplayList.clear();
            this.mDisplayList.addAll(newDisplayList);
            return true;
        }

        private boolean hasSystemBrowser() {
            int i = 0;
            while (true) {
                int size = this.mDisplayList.size();
                String str = BrowserContract.AUTHORITY;
                if (i >= size) {
                    for (i = 0; i < this.mUnfilteredResolveList.size(); i++) {
                        ResolvedComponentInfo info = (ResolvedComponentInfo) this.mUnfilteredResolveList.get(i);
                        if (info != null && info.getResolveInfoSize() != 0 && str.equals(info.getResolveInfoAt(0).activityInfo.packageName)) {
                            return true;
                        }
                    }
                    return false;
                } else if (str.equals(((DisplayResolveInfo) this.mDisplayList.get(i)).mResolveInfo.activityInfo.packageName)) {
                    return true;
                } else {
                    i++;
                }
            }
        }

        private boolean hasSystemVideoPlayer() {
            if (!VideoTypeParseUtils.getInstance().shouldSystemVideoCanResolve(ResolverActivity.this)) {
                return false;
            }
            int i = 0;
            while (true) {
                String str = "com.miui.video";
                if (i >= this.mDisplayList.size()) {
                    for (i = 0; i < this.mUnfilteredResolveList.size(); i++) {
                        ResolvedComponentInfo info = (ResolvedComponentInfo) this.mUnfilteredResolveList.get(i);
                        if (info != null && info.getResolveInfoSize() != 0 && str.equals(info.getResolveInfoAt(0).activityInfo.packageName)) {
                            return true;
                        }
                    }
                    return false;
                } else if (str.equals(((DisplayResolveInfo) this.mDisplayList.get(i)).mResolveInfo.activityInfo.packageName)) {
                    return true;
                } else {
                    i++;
                }
            }
        }

        private boolean hasSystemWps() {
            if (!WPSTypeParseUtils.getInstance().isDocTypeResolved(ResolverActivity.this)) {
                return false;
            }
            int i = 0;
            while (true) {
                int size = this.mDisplayList.size();
                String str = ResolverActivity.WPS_LITE_PKG_NAME;
                if (i >= size) {
                    for (i = 0; i < this.mUnfilteredResolveList.size(); i++) {
                        ResolvedComponentInfo info = (ResolvedComponentInfo) this.mUnfilteredResolveList.get(i);
                        if (info != null && info.getResolveInfoSize() != 0 && str.equals(info.getResolveInfoAt(0).activityInfo.packageName)) {
                            return true;
                        }
                    }
                    return false;
                } else if (str.equals(((DisplayResolveInfo) this.mDisplayList.get(i)).mResolveInfo.activityInfo.packageName)) {
                    return true;
                } else {
                    i++;
                }
            }
        }

        public boolean hasOfficalRecommendation() {
            ResolverActivity resolverActivity = ResolverActivity.this;
            boolean z = (hasSystemBrowser() || hasSystemVideoPlayer() || hasSystemWps()) && !Build.IS_INTERNATIONAL_BUILD;
            resolverActivity.mRecommendable = z;
            return ResolverActivity.this.mRecommendable;
        }

        private void filterDisplayResolveInfo(List<ResolvedComponentInfo> currentResolveList) {
            if (currentResolveList != null && currentResolveList.size() > 1) {
                boolean hasWps = false;
                int wpsLitePos = -1;
                for (int i = 0; i < currentResolveList.size(); i++) {
                    ResolvedComponentInfo info = (ResolvedComponentInfo) currentResolveList.get(i);
                    if (info.name != null) {
                        if (ResolverActivity.WPS_PKG_NAME.equals(info.name.getPackageName())) {
                            hasWps = true;
                        } else {
                            if (ResolverActivity.WPS_LITE_PKG_NAME.equals(info.name.getPackageName())) {
                                wpsLitePos = i;
                            }
                        }
                    }
                }
                if (hasWps && -1 != wpsLitePos) {
                    currentResolveList.remove(wpsLitePos);
                }
            }
        }

        private boolean isPlatformApp(String pkgName) {
            return ResolverActivity.this.getPackageManager().checkSignatures("android", pkgName) == 0;
        }

        private void postListReadyRunnable() {
            if (ResolverActivity.this.mPostListReadyRunnable == null) {
                ResolverActivity.this.mPostListReadyRunnable = new Runnable() {
                    public void run() {
                        ResolverActivity.this.setTitleAndIcon();
                        ResolverActivity.this.resetAlwaysOrOnceButtonBar();
                        ResolveListAdapter.this.onListRebuilt();
                        ResolverActivity.this.mPostListReadyRunnable = null;
                    }
                };
                ResolverActivity.this.getMainThreadHandler().post(ResolverActivity.this.mPostListReadyRunnable);
            }
        }

        public void onListRebuilt() {
        }

        public boolean shouldGetResolvedFilter() {
            return this.mFilterLastUsed;
        }

        private void processGroup(List<ResolvedComponentInfo> rList, int start, int end, ResolvedComponentInfo ro, CharSequence roLabel) {
            if ((end - start) + 1 == 1) {
                addResolveInfoWithAlternates(ro, null, roLabel);
                return;
            }
            CharSequence jApp;
            this.mHasExtendedInfo = true;
            boolean usePkg = false;
            CharSequence startApp = ro.getResolveInfoAt(0).activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
            if (startApp == null) {
                usePkg = true;
            }
            if (!usePkg) {
                HashSet<CharSequence> duplicates = new HashSet();
                duplicates.add(startApp);
                int j = start + 1;
                while (j <= end) {
                    jApp = ((ResolvedComponentInfo) rList.get(j)).getResolveInfoAt(0).activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
                    if (jApp == null || duplicates.contains(jApp)) {
                        usePkg = true;
                        break;
                    } else {
                        duplicates.add(jApp);
                        j++;
                    }
                }
                duplicates.clear();
            }
            for (int k = start; k <= end; k++) {
                ResolvedComponentInfo rci = (ResolvedComponentInfo) rList.get(k);
                ResolveInfo add = rci.getResolveInfoAt(0);
                if (usePkg) {
                    jApp = add.activityInfo.packageName;
                } else {
                    jApp = add.activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
                }
                addResolveInfoWithAlternates(rci, jApp, roLabel);
            }
        }

        private void addResolveInfoWithAlternates(ResolvedComponentInfo rci, CharSequence extraInfo, CharSequence roLabel) {
            int count = rci.getCount();
            Intent intent = rci.getIntentAt(0);
            ResolveInfo add = rci.getResolveInfoAt(0);
            Intent replaceIntent = ResolverActivity.this.getReplacementIntent(add.activityInfo, intent);
            DisplayResolveInfo dri = new DisplayResolveInfo(intent, add, roLabel, extraInfo, replaceIntent);
            dri.setPinned(rci.isPinned());
            addResolveInfo(dri);
            if (replaceIntent == intent) {
                int N = count;
                for (int i = 1; i < N; i++) {
                    dri.addAlternateSourceIntent(rci.getIntentAt(i));
                }
            }
            updateLastChosenPosition(add);
        }

        private void updateLastChosenPosition(ResolveInfo info) {
            if (this.mOtherProfile != null) {
                this.mLastChosenPosition = -1;
                return;
            }
            ResolveInfo resolveInfo = this.mLastChosen;
            if (resolveInfo != null && resolveInfo.activityInfo.packageName.equals(info.activityInfo.packageName) && this.mLastChosen.activityInfo.name.equals(info.activityInfo.name)) {
                this.mLastChosenPosition = this.mDisplayList.size() - 1;
            }
        }

        private void addResolveInfo(DisplayResolveInfo dri) {
            if (XSpaceUserHandle.isXSpaceUserId(UserHandle.myUserId()) && ResolverActivity.XSPACE_NOT_SHOW_LIST.contains(dri.mResolveInfo.activityInfo.packageName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(dri.mResolveInfo.activityInfo.packageName);
                stringBuilder.append("should not show");
                Log.i(ResolverActivity.TAG, stringBuilder.toString());
                return;
            }
            if (dri.mResolveInfo.targetUserId == -2) {
                this.mDisplayList.add(dri);
            }
        }

        public ResolveInfo resolveInfoForPosition(int position, boolean filtered) {
            TargetInfo target = targetInfoForPosition(position, filtered);
            if (target != null) {
                return target.getResolveInfo();
            }
            return null;
        }

        public TargetInfo targetInfoForPosition(int position, boolean filtered) {
            if (filtered) {
                return getItem(position);
            }
            if (this.mDisplayList.size() > position) {
                return (TargetInfo) this.mDisplayList.get(position);
            }
            return null;
        }

        public int getCount() {
            int totalSize;
            List list = this.mDisplayList;
            if (list == null || list.isEmpty()) {
                totalSize = this.mPlaceholderCount;
            } else {
                totalSize = this.mDisplayList.size();
            }
            return totalSize - ResolverActivity.this.mRecommendable;
        }

        public int getUnfilteredCount() {
            return this.mDisplayList.size();
        }

        public int getDisplayInfoCount() {
            return this.mDisplayList.size();
        }

        public DisplayResolveInfo getDisplayInfoAt(int index) {
            return (DisplayResolveInfo) this.mDisplayList.get(index);
        }

        public List<DisplayResolveInfo> getDisplayList() {
            return this.mDisplayList;
        }

        public TargetInfo getItem(int position) {
            if (this.mDisplayList.size() > position) {
                return (TargetInfo) this.mDisplayList.get(ResolverActivity.this.mRecommendable + position);
            }
            return null;
        }

        public TargetInfo getRecommendItem() {
            List list = this.mDisplayList;
            if (list == null || list.size() == 0) {
                return null;
            }
            return (TargetInfo) this.mDisplayList.get(0);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public boolean hasExtendedInfo() {
            return this.mHasExtendedInfo;
        }

        public boolean hasResolvedTarget(ResolveInfo info) {
            int N = this.mDisplayList.size();
            for (int i = 0; i < N; i++) {
                if (ResolverActivity.resolveInfoMatch(info, ((DisplayResolveInfo) this.mDisplayList.get(i)).getResolveInfo())) {
                    return true;
                }
            }
            return false;
        }

        public int getDisplayResolveInfoCount() {
            return this.mDisplayList.size();
        }

        public DisplayResolveInfo getDisplayResolveInfo(int index) {
            return (DisplayResolveInfo) this.mDisplayList.get(index);
        }

        public final View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = createView(parent);
            }
            onBindView(view, getItem(position));
            return view;
        }

        public final View createView(ViewGroup parent) {
            View view = onCreateView(parent);
            view.setTag(new ViewHolder(view));
            return view;
        }

        public View onCreateView(ViewGroup parent) {
            return this.mInflater.inflate((int) com.android.internal.R.layout.resolve_list_item, parent, false);
        }

        public boolean showsExtendedInfo(TargetInfo info) {
            return TextUtils.isEmpty(info.getExtendedInfo()) ^ 1;
        }

        public boolean isComponentPinned(ComponentName name) {
            return false;
        }

        public final void bindView(int position, View view) {
            onBindView(view, getItem(position));
        }

        private void onBindView(View view, TargetInfo info) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.text.setText(info.getDisplayLabel());
            if (showsExtendedInfo(info)) {
                holder.text2.setVisibility(0);
                holder.text2.setText(info.getExtendedInfo());
            } else {
                holder.text2.setVisibility(8);
            }
            if ((info instanceof DisplayResolveInfo) && !((DisplayResolveInfo) info).hasDisplayIcon()) {
                new LoadAdapterIconTask(ResolverActivity.this, (DisplayResolveInfo) info).execute((Object[]) new Void[0]);
            }
            holder.icon.setImageDrawable(info.getDisplayIcon());
            if (holder.badge != null) {
                Drawable badge = info.getBadgeIcon();
                if (badge != null) {
                    holder.badge.setImageDrawable(badge);
                    holder.badge.setContentDescription(info.getBadgeContentDescription());
                    holder.badge.setVisibility(0);
                    return;
                }
                holder.badge.setVisibility(8);
            }
        }
    }

    public interface TargetInfo {
        TargetInfo cloneFilledIn(Intent intent, int i);

        List<Intent> getAllSourceIntents();

        CharSequence getBadgeContentDescription();

        Drawable getBadgeIcon();

        Drawable getDisplayIcon();

        CharSequence getDisplayLabel();

        CharSequence getExtendedInfo();

        ResolveInfo getResolveInfo();

        ComponentName getResolvedComponentName();

        Intent getResolvedIntent();

        boolean isPinned();

        boolean start(Activity activity, Bundle bundle);

        boolean startAsCaller(Activity activity, Bundle bundle, int i);

        boolean startAsUser(Activity activity, Bundle bundle, UserHandle userHandle);
    }

    private enum ActionTitle {
        VIEW("android.intent.action.VIEW", com.android.internal.R.string.whichViewApplication, com.android.internal.R.string.whichViewApplicationNamed, com.android.internal.R.string.whichViewApplicationLabel),
        EDIT(Intent.ACTION_EDIT, com.android.internal.R.string.whichEditApplication, com.android.internal.R.string.whichEditApplicationNamed, com.android.internal.R.string.whichEditApplicationLabel),
        SEND(Intent.ACTION_SEND, com.android.internal.R.string.whichSendApplication, com.android.internal.R.string.whichSendApplicationNamed, com.android.internal.R.string.whichSendApplicationLabel),
        SENDTO(Intent.ACTION_SENDTO, com.android.internal.R.string.whichSendToApplication, com.android.internal.R.string.whichSendToApplicationNamed, com.android.internal.R.string.whichSendToApplicationLabel),
        SEND_MULTIPLE(Intent.ACTION_SEND_MULTIPLE, com.android.internal.R.string.whichSendApplication, com.android.internal.R.string.whichSendApplicationNamed, com.android.internal.R.string.whichSendApplicationLabel),
        CAPTURE_IMAGE(MediaStore.ACTION_IMAGE_CAPTURE, com.android.internal.R.string.whichImageCaptureApplication, com.android.internal.R.string.whichImageCaptureApplicationNamed, com.android.internal.R.string.whichImageCaptureApplicationLabel),
        DEFAULT(null, com.android.internal.R.string.whichApplication, com.android.internal.R.string.whichApplicationNamed, com.android.internal.R.string.whichApplicationLabel),
        HOME(Intent.ACTION_MAIN, com.android.internal.R.string.whichHomeApplication, com.android.internal.R.string.whichHomeApplicationNamed, com.android.internal.R.string.whichHomeApplicationLabel);
        
        public final String action;
        public final int labelRes;
        public final int namedTitleRes;
        public final int titleRes;

        private ActionTitle(String action, int titleRes, int namedTitleRes, int labelRes) {
            this.action = action;
            this.titleRes = titleRes;
            this.namedTitleRes = namedTitleRes;
            this.labelRes = labelRes;
        }

        public static ActionTitle forAction(String action) {
            for (ActionTitle title : values()) {
                if (title != HOME && action != null && action.equals(title.action)) {
                    return title;
                }
            }
            return DEFAULT;
        }
    }

    public final class DisplayResolveInfo implements TargetInfo {
        private Drawable mDisplayIcon;
        private final CharSequence mDisplayLabel;
        private final CharSequence mExtendedInfo;
        private boolean mPinned;
        private final ResolveInfo mResolveInfo;
        private final Intent mResolvedIntent;
        private boolean mShowMore;
        private final List<Intent> mSourceIntents;

        /* synthetic */ DisplayResolveInfo(ResolverActivity x0, DisplayResolveInfo x1, Drawable x2, CharSequence x3, AnonymousClass1 x4) {
            this(x1, x2, x3);
        }

        public DisplayResolveInfo(Intent originalIntent, ResolveInfo pri, CharSequence pLabel, CharSequence pInfo, Intent pOrigIntent) {
            Intent intent;
            this.mSourceIntents = new ArrayList();
            this.mShowMore = false;
            this.mSourceIntents.add(originalIntent);
            this.mResolveInfo = pri;
            this.mDisplayLabel = pLabel;
            this.mExtendedInfo = pInfo;
            if (pOrigIntent != null) {
                intent = pOrigIntent;
            } else {
                intent = ResolverActivity.this.getReplacementIntent(pri.activityInfo, ResolverActivity.this.getTargetIntent());
            }
            Intent intent2 = new Intent(intent);
            intent2.addFlags(View.SCROLLBARS_OUTSIDE_INSET);
            ActivityInfo ai = this.mResolveInfo.activityInfo;
            intent2.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
            this.mResolvedIntent = intent2;
            this.mShowMore = false;
        }

        private DisplayResolveInfo(DisplayResolveInfo other, Drawable displayIcon, CharSequence displayLabel) {
            this.mSourceIntents = new ArrayList();
            this.mShowMore = false;
            this.mDisplayIcon = displayIcon;
            this.mDisplayLabel = displayLabel;
            this.mExtendedInfo = displayLabel;
            this.mSourceIntents.addAll(other.getAllSourceIntents());
            this.mResolveInfo = other.mResolveInfo;
            this.mResolvedIntent = other.getResolvedIntent();
            this.mPinned = other.mPinned;
            this.mShowMore = true;
        }

        private DisplayResolveInfo(DisplayResolveInfo other, Intent fillInIntent, int flags) {
            this.mSourceIntents = new ArrayList();
            this.mShowMore = false;
            this.mSourceIntents.addAll(other.getAllSourceIntents());
            this.mResolveInfo = other.mResolveInfo;
            this.mDisplayLabel = other.mDisplayLabel;
            this.mDisplayIcon = other.mDisplayIcon;
            this.mExtendedInfo = other.mExtendedInfo;
            this.mResolvedIntent = new Intent(other.mResolvedIntent);
            this.mResolvedIntent.fillIn(fillInIntent, flags);
            this.mPinned = other.mPinned;
            this.mShowMore = false;
        }

        public boolean getIsShowMore() {
            return this.mShowMore;
        }

        public ResolveInfo getResolveInfo() {
            return this.mResolveInfo;
        }

        public CharSequence getDisplayLabel() {
            return this.mDisplayLabel;
        }

        public Drawable getDisplayIcon() {
            return this.mDisplayIcon;
        }

        public Drawable getBadgeIcon() {
            return null;
        }

        public CharSequence getBadgeContentDescription() {
            return null;
        }

        public TargetInfo cloneFilledIn(Intent fillInIntent, int flags) {
            return new DisplayResolveInfo(this, fillInIntent, flags);
        }

        public List<Intent> getAllSourceIntents() {
            return this.mSourceIntents;
        }

        public void addAlternateSourceIntent(Intent alt) {
            this.mSourceIntents.add(alt);
        }

        public void setDisplayIcon(Drawable icon) {
            this.mDisplayIcon = icon;
        }

        public boolean hasDisplayIcon() {
            return this.mDisplayIcon != null;
        }

        public CharSequence getExtendedInfo() {
            return this.mExtendedInfo;
        }

        public Intent getResolvedIntent() {
            return this.mResolvedIntent;
        }

        public ComponentName getResolvedComponentName() {
            return new ComponentName(this.mResolveInfo.activityInfo.packageName, this.mResolveInfo.activityInfo.name);
        }

        public boolean start(Activity activity, Bundle options) {
            activity.startActivity(this.mResolvedIntent, options);
            return true;
        }

        public boolean startAsCaller(Activity activity, Bundle options, int userId) {
            if (ResolverActivityInjector.checkMMShare(activity, this.mResolvedIntent, options, userId)) {
                return true;
            }
            activity.startActivityAsCaller(this.mResolvedIntent, options, null, false, userId);
            return true;
        }

        public boolean startAsUser(Activity activity, Bundle options, UserHandle user) {
            activity.startActivityAsUser(this.mResolvedIntent, options, user);
            return false;
        }

        public boolean isPinned() {
            return this.mPinned;
        }

        public void setPinned(boolean pinned) {
            this.mPinned = pinned;
        }
    }

    class ItemClickListener implements OnItemClickListener, OnItemLongClickListener {
        private int start;

        public ItemClickListener(int page) {
            this.start = (page - 1) * ResolverActivity.this.mMaxPerScreen;
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = parent instanceof ListView ? (ListView) parent : null;
            if (listView != null) {
                position -= listView.getHeaderViewsCount();
            }
            if (position >= 0) {
                boolean always = false;
                if (ResolverActivity.this.mRecommendable) {
                    if (!(ResolverActivity.this.mChosenView == view || ResolverActivity.this.mChosenView == null)) {
                        ResolverActivity.this.mChosenView.setSelected(false);
                        ResolverActivity.this.mChosenView = view;
                        ResolverActivity.this.mChosenIndex = (this.start + position) + 1;
                    }
                    view.setSelected(true);
                } else {
                    CheckBox alwaysOption = (CheckBox) ResolverActivity.this.findViewById(R.id.always_option);
                    if (alwaysOption.getVisibility() == 0 && alwaysOption.isChecked()) {
                        always = true;
                    }
                    ResolverActivity.this.startSelected(this.start + position, always, true);
                }
            }
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = parent instanceof ListView ? (ListView) parent : null;
            if (listView != null) {
                position -= listView.getHeaderViewsCount();
            }
            if (position < 0) {
                return false;
            }
            TargetInfo target = ResolverActivity.this.mAdapter.targetInfoForPosition(this.start + position, true);
            if ((target instanceof DisplayResolveInfo) && Build.IS_INTERNATIONAL_BUILD && ((DisplayResolveInfo) target).mShowMore) {
                ResolverActivity.this.showMore();
                return true;
            }
            ResolverActivity.this.showTargetDetails(ResolverActivity.this.mAdapter.resolveInfoForPosition(this.start + position, true));
            return true;
        }
    }

    abstract class LoadIconTask extends AsyncTask<Void, Void, Drawable> {
        protected final DisplayResolveInfo mDisplayResolveInfo;
        private final ResolveInfo mResolveInfo;

        public LoadIconTask(DisplayResolveInfo dri) {
            this.mDisplayResolveInfo = dri;
            this.mResolveInfo = dri.getResolveInfo();
        }

        /* Access modifiers changed, original: protected|varargs */
        public Drawable doInBackground(Void... params) {
            if (XSpaceUserHandle.isXSpaceUserId(UserHandle.myUserId())) {
                return ResolverActivity.this.loadIconForResolveInfo(this.mResolveInfo);
            }
            return this.mResolveInfo.activityInfo.loadIcon(ResolverActivity.this.getPackageManager());
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Drawable d) {
            this.mDisplayResolveInfo.setDisplayIcon(d);
        }
    }

    class LoadAdapterIconTask extends LoadIconTask {
        private BaseAdapter adapter;
        private boolean bindIconOnly;

        public LoadAdapterIconTask(ResolverActivity this$0, DisplayResolveInfo dri) {
            this(null, dri);
            this.bindIconOnly = false;
        }

        public LoadAdapterIconTask(BaseAdapter adapter, DisplayResolveInfo dri) {
            super(dri);
            this.adapter = adapter;
            this.bindIconOnly = adapter == null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Drawable d) {
            super.onPostExecute(d);
            if (this.bindIconOnly) {
                ResolverActivity.this.bindOfficalRecommendIcon();
                return;
            }
            if (ResolverActivity.this.mProfileView != null && ResolverActivity.this.mAdapter.getOtherProfile() == this.mDisplayResolveInfo) {
                ResolverActivity.this.bindProfileView();
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    class LoadIconIntoViewTask extends LoadIconTask {
        private final ImageView mTargetView;

        public LoadIconIntoViewTask(DisplayResolveInfo dri, ImageView target) {
            super(dri);
            this.mTargetView = target;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Drawable d) {
            super.onPostExecute(d);
            this.mTargetView.setImageDrawable(d);
        }
    }

    private final class PageGridAdapter extends ArrayAdapter<TargetInfo> {
        public PageGridAdapter(Context context, int page) {
            super(context, 0);
            int start = (page - 1) * ResolverActivity.this.mMaxPerScreen;
            int end = Math.min((ResolverActivity.this.mMaxPerScreen + start) - 1, ResolverActivity.this.mAdapter.getCount() - 1);
            for (int i = start; i <= end; i++) {
                add(ResolverActivity.this.mAdapter.getItem(i));
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate((int) R.layout.resolve_list_item, parent, false);
                ((MaskLayout) view.findViewById(R.id.masklayout)).setMaskColor(ResolverActivity.this.mRecommendable ? ResolverActivity.this.getResources().getColor(17170445) : ResolverActivity.this.mMaskColor);
                ViewHolder holder = new ViewHolder(view);
                if (ResolverActivity.this.mRecommendFirstItem && !ResolverActivity.this.mRecommendable) {
                    holder.text.setMinLines(3);
                    holder.text.setMaxLines(3);
                    holder.text.setMaxEms(6);
                }
                if (ResolverActivity.this.mRecommendable) {
                    holder.icon.setEnabled(ResolverActivity.this.mRecommendable ^ 1);
                    holder.icon.setClickable(ResolverActivity.this.mRecommendable ^ 1);
                    holder.icon.setPressed(ResolverActivity.this.mRecommendable ^ 1);
                    holder.text.setTextSize(2, 11.0f);
                    holder.text.setTextColor(ResolverActivity.this.getResources().getColor(R.color.resolver_recommend_other_app_text_color));
                }
                view.setTag(holder);
                LayoutParams lp = holder.icon.getLayoutParams();
                int access$2600 = ResolverActivity.this.mIconSize;
                lp.height = access$2600;
                lp.width = access$2600;
            } else {
                view = convertView;
            }
            bindView(view, (TargetInfo) getItem(position), position);
            return view;
        }

        private final void bindView(View view, TargetInfo info, int position) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.text2.setVisibility(8);
            if (info == null) {
                holder.icon.setImageDrawable(ResolverActivity.this.getDrawable(com.android.internal.R.drawable.resolver_icon_placeholder));
                return;
            }
            if (ResolverActivity.this.mRecommendFirstItem && position == 0 && !ResolverActivity.this.mRecommendable) {
                holder.text.setText(Html.fromHtml(ResolverActivity.this.getString(R.string.resolver_recommended_app_name, new Object[]{info.getDisplayLabel(), ResolverActivity.this.getString(R.string.resolver_app_recommend_flag)})));
            } else {
                holder.text.setText(info.getDisplayLabel());
            }
            if ((info instanceof DisplayResolveInfo) && !((DisplayResolveInfo) info).hasDisplayIcon()) {
                new LoadAdapterIconTask(this, (DisplayResolveInfo) info).execute((Object[]) new Void[0]);
            }
            holder.icon.setImageDrawable(info.getDisplayIcon());
            if (holder.badge != null) {
                Drawable badge = info.getBadgeIcon();
                if (badge == null || ResolverActivity.this.mRecommendable) {
                    holder.badge.setVisibility(8);
                } else {
                    holder.badge.setImageDrawable(badge);
                    holder.badge.setContentDescription(info.getBadgeContentDescription());
                    holder.badge.setVisibility(0);
                }
            }
        }
    }

    static class PickTargetOptionRequest extends PickOptionRequest {
        public PickTargetOptionRequest(Prompt prompt, Option[] options, Bundle extras) {
            super(prompt, options, extras);
        }

        public void onCancel() {
            super.onCancel();
            ResolverActivity ra = (ResolverActivity) getActivity();
            if (ra != null) {
                ra.mPickOptionRequest = null;
                ra.finish();
            }
        }

        public void onPickOptionResult(boolean finished, Option[] selections, Bundle result) {
            super.onPickOptionResult(finished, selections, result);
            if (selections.length == 1) {
                ResolverActivity ra = (ResolverActivity) getActivity();
                if (ra != null && ra.onTargetSelected(ra.mAdapter.getItem(selections[0].getIndex()), false)) {
                    ra.mPickOptionRequest = null;
                    ra.finish();
                }
            }
        }
    }

    @VisibleForTesting
    public static final class ResolvedComponentInfo {
        private final List<Intent> mIntents = new ArrayList();
        private boolean mPinned;
        private final List<ResolveInfo> mResolveInfos = new ArrayList();
        public final ComponentName name;

        public ResolvedComponentInfo(ComponentName name, Intent intent, ResolveInfo info) {
            this.name = name;
            add(intent, info);
        }

        public void add(Intent intent, ResolveInfo info) {
            this.mIntents.add(intent);
            this.mResolveInfos.add(info);
        }

        public int getCount() {
            return this.mIntents.size();
        }

        public Intent getIntentAt(int index) {
            return index >= 0 ? (Intent) this.mIntents.get(index) : null;
        }

        public ResolveInfo getResolveInfoAt(int index) {
            return index >= 0 ? (ResolveInfo) this.mResolveInfos.get(index) : null;
        }

        public int getResolveInfoSize() {
            return this.mResolveInfos.size();
        }

        public int findIntent(Intent intent) {
            int N = this.mIntents.size();
            for (int i = 0; i < N; i++) {
                if (intent.equals(this.mIntents.get(i))) {
                    return i;
                }
            }
            return -1;
        }

        public int findResolveInfo(ResolveInfo info) {
            int N = this.mResolveInfos.size();
            for (int i = 0; i < N; i++) {
                if (info.equals(this.mResolveInfos.get(i))) {
                    return i;
                }
            }
            return -1;
        }

        public boolean isPinned() {
            return this.mPinned;
        }

        public void setPinned(boolean pinned) {
            this.mPinned = pinned;
        }
    }

    static class VideoTypeParseUtils {
        public static final String[] VIDEO_EXTENSIONS = new String[]{"3G2", "3GP", "3GP2", "3GPP", "3GPP2", "AVI", "AVB", "ASF", "ASX", "AVS", "BOX", "DIVX", "FLV", "F4V", "M2V", "M4V", "MKV", "MOV", "MP4", "MPG", "MPEG", "NDIVX", "RA", "RM", "RAM", "RMVB", "TS", "V8", "VOB", "WMV", "XVID", "M4A", "MJ2", "MPE", "M1V", "MP2", "MOD", "QT", "PFV", "WEBM", "DAT", "VSTREAM", "DV", "H264", "H261", "H263"};
        private static final HashSet<String> mHashVideo = new HashSet(Arrays.asList(VIDEO_EXTENSIONS));

        private static class Holder {
            private static VideoTypeParseUtils INSTANCE = new VideoTypeParseUtils();

            private Holder() {
            }
        }

        /* synthetic */ VideoTypeParseUtils(AnonymousClass1 x0) {
            this();
        }

        private VideoTypeParseUtils() {
        }

        public static VideoTypeParseUtils getInstance() {
            return Holder.INSTANCE;
        }

        public boolean shouldSystemVideoCanResolve(Activity activity) {
            String str = ".";
            if (activity == null) {
                return false;
            }
            Intent intent = activity.getIntent();
            if (intent != null) {
                try {
                    String resolveType = intent.resolveType((Context) activity);
                    if (!TextUtils.isEmpty(resolveType) && resolveType.contains("video")) {
                        return true;
                    }
                    String decode = Uri.decode(intent.getData().toString());
                    String fileName = decode.substring(decode.lastIndexOf("/") + 1);
                    if (!TextUtils.isEmpty(fileName) && fileName.contains(str)) {
                        if (mHashVideo.contains(fileName.substring(fileName.lastIndexOf(str) + 1).toUpperCase())) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    static class ViewHolder {
        public ImageView badge;
        public ImageView icon;
        public TextView text;
        public TextView text2;

        public ViewHolder(View view) {
            this.text = (TextView) view.findViewById(16908308);
            this.text2 = (TextView) view.findViewById(16908309);
            this.icon = (ImageView) view.findViewById(16908294);
            this.badge = (ImageView) view.findViewById(R.id.target_badge);
        }
    }

    static class WPSTypeParseUtils {
        public static final String[] DOC_SUFFIX = new String[]{"DOC", "DOCX", "XLS", "XLSX", "PPT", "PPTX", "PDF"};
        private static final HashSet<String> mDocTypes = new HashSet(Arrays.asList(DOC_SUFFIX));

        private static class Holder {
            private static WPSTypeParseUtils INSTANCE = new WPSTypeParseUtils();

            private Holder() {
            }
        }

        /* synthetic */ WPSTypeParseUtils(AnonymousClass1 x0) {
            this();
        }

        private WPSTypeParseUtils() {
        }

        public static WPSTypeParseUtils getInstance() {
            return Holder.INSTANCE;
        }

        public boolean isDocTypeResolved(Activity activity) {
            String str = ".";
            if (activity == null) {
                return false;
            }
            Intent intent = activity.getIntent();
            if (intent != null) {
                try {
                    String decode = Uri.decode(intent.getData().toString());
                    String fileName = decode.substring(decode.lastIndexOf("/") + 1);
                    if (!TextUtils.isEmpty(fileName) && fileName.contains(str)) {
                        if (mDocTypes.contains(fileName.substring(fileName.lastIndexOf(str) + 1).toUpperCase())) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    static {
        PRIV_PACKAGES.add("com.miui.personalassistant.favorite");
        if (Build.IS_INTERNATIONAL_BUILD) {
            PRIV_PACKAGES.add(ToggleManager.PKG_NAME_MIDROP);
            PRIV_PACKAGES.add(BrowserContract.AUTHORITY);
            XSPACE_NOT_SHOW_LIST.add("com.android.chrome");
        }
    }

    public static int getLabelRes(String action) {
        return ActionTitle.forAction(action).labelRes;
    }

    private Intent makeMyIntent() {
        Intent intent = new Intent(getIntent());
        intent.setComponent(null);
        intent.setFlags(intent.getFlags() & -8388609);
        return intent;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == -1 && result != null) {
            startActivityAsCaller(result, null, null, false, -10000);
        }
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = makeMyIntent();
        Set<String> categories = intent.getCategories();
        if (Intent.ACTION_MAIN.equals(intent.getAction()) && categories != null && categories.size() == 1 && categories.contains(Intent.CATEGORY_HOME)) {
            this.mResolvingHome = true;
        }
        setSafeForwardingMode(true);
        onCreate(savedInstanceState, intent, null, 0, null, null, true);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, Intent[] initialIntents, List<ResolveInfo> rList, boolean alwaysUseOption) {
        onCreate(savedInstanceState, intent, title, 0, initialIntents, rList, alwaysUseOption);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, int defaultTitleRes, Intent[] initialIntents, List<ResolveInfo> rList, boolean alwaysUseOption) {
        setTheme(miui.R.style.Theme_DayNight_Dialog_Alert);
        String str = "android.intent.action.VIEW";
        boolean equals = str.equals(intent.getAction());
        String str2 = Intent.ACTION_WEB_SEARCH;
        if (!(equals || str2.equals(intent.getAction()))) {
            this.mShowMoreResolverButton = false;
        }
        if (this.mShowMoreResolverButton && initialIntents != null) {
            for (Intent ii : initialIntents) {
                if (!str.equals(ii.getAction()) && !str2.equals(ii.getAction())) {
                    this.mShowMoreResolverButton = false;
                    break;
                }
            }
        }
        BaseBundle.setShouldDefuse(true);
        super.onCreate(savedInstanceState);
        setProfileSwitchMessageId(intent.getContentUserHint());
        Set<String> categoriesM = intent.getCategories();
        if (!(Build.IS_INTERNATIONAL_BUILD || Build.IS_TABLET)) {
            if (Intent.ACTION_MAIN.equals(intent.getAction()) && categoriesM != null && categoriesM.contains(Intent.CATEGORY_HOME)) {
                Log.d(TAG, "Default Home Already Exists");
                finish();
                return;
            }
        }
        try {
            this.mLaunchedFromUid = ActivityTaskManager.getService().getLaunchedFromUid(getActivityToken());
        } catch (RemoteException e) {
            this.mLaunchedFromUid = -1;
        }
        int i = this.mLaunchedFromUid;
        if (i < 0 || UserHandle.isIsolated(i)) {
            finish();
            return;
        }
        this.mPm = getPackageManager();
        this.mIntents.add(0, new Intent(intent));
        CharSequence charSequence = null;
        if (ChooserActivityInjector.canInterceptByMiAppStore(this, intent)) {
            ChooserActivityInjector.startInterceptByMiAppStore(this, intent, createListController(), null, this.mLaunchedFromUid);
            return;
        }
        this.mPackageMonitor.register(this, getMainLooper(), false);
        this.mRegistered = true;
        this.mReferrerPackage = getReferrerPackageName();
        this.mAlwaysUseOption = alwaysUseOption;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        this.mIconDpi = am.getLauncherLargeIconDensity();
        this.mIconSize = am.getLauncherLargeIconSize();
        this.mMaskColor = getResources().getColor(R.color.resolver_icon_mask);
        this.mTitle = title;
        this.mDefaultTitleResId = defaultTitleRes;
        this.mIconFactory = IconDrawableFactory.newInstance(this, true);
        configureContentView(this.mIntents, initialIntents, rList);
        if (XSpaceResolverActivityHelper.checkAndResolve(this, getIntent(), this.mAlertParams)) {
            setupAlert();
            if (this.mRegistered) {
                this.mPackageMonitor.unregister();
                this.mRegistered = false;
            }
            return;
        }
        int i2;
        ResolverDrawerLayout rdl = (ResolverDrawerLayout) findViewById(com.android.internal.R.id.contentPanel);
        if (rdl != null) {
            rdl.setOnDismissedListener(new OnDismissedListener() {
                public void onDismissed() {
                    ResolverActivity.this.finish();
                }
            });
            if (isVoiceInteraction()) {
                rdl.setCollapsed(false);
            }
            this.mResolverDrawerLayout = rdl;
        }
        if (title == null) {
            title = getTitleForAction(intent.getAction(), defaultTitleRes);
        }
        AlertParams alertParams = this.mAlertParams;
        if (!isActionSend()) {
            charSequence = title;
        }
        alertParams.mTitle = charSequence;
        setupAlert();
        getWindow().clearFlags(131072);
        if (alwaysUseOption && !this.mRecommendable) {
            CheckBox alwaysOption = (CheckBox) findViewById(R.id.always_option);
            if (alwaysOption != null) {
                alwaysOption.setVisibility(0);
                if (!this.mShowMoreResolverButton || !Build.IS_INTERNATIONAL_BUILD || this.mAdapter.mDisplayList == null || this.mAdapter.mDisplayList.size() <= 1) {
                    alwaysOption.setChecked(false);
                } else {
                    alwaysOption.setChecked(true);
                }
            } else {
                this.mAlwaysUseOption = false;
            }
        }
        this.mProfileView = findViewById(R.id.profile_button);
        View view = this.mProfileView;
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DisplayResolveInfo dri = ResolverActivity.this.mAdapter.getOtherProfile();
                    if (dri != null) {
                        ResolverActivity.this.mProfileSwitchMessageId = -1;
                        ResolverActivity.this.onTargetSelected(dri, false);
                        ResolverActivity.this.finish();
                    }
                }
            });
            bindProfileView();
        }
        if (isVoiceInteraction()) {
            onSetupVoiceInteraction();
        }
        Set<String> categories = intent.getCategories();
        if (this.mAdapter.hasFilteredItem()) {
            i2 = MetricsEvent.ACTION_SHOW_APP_DISAMBIG_APP_FEATURED;
        } else {
            i2 = MetricsEvent.ACTION_SHOW_APP_DISAMBIG_NONE_FEATURED;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(intent.getAction());
        String str3 = ":";
        stringBuilder.append(str3);
        stringBuilder.append(intent.getType());
        stringBuilder.append(str3);
        stringBuilder.append(categories != null ? Arrays.toString(categories.toArray()) : "");
        MetricsLogger.action((Context) this, i2, stringBuilder.toString());
    }

    private boolean isActionSend() {
        Bundle bundle = getIntent().getExtras();
        boolean z = false;
        if (bundle != null) {
            String str = Intent.EXTRA_INTENT;
            if (bundle.getParcelable(str) != null && (bundle.getParcelable(str) instanceof Intent)) {
                Intent intent = (Intent) bundle.getParcelable(str);
                if (TextUtils.equals(intent.getAction(), Intent.ACTION_SEND) || TextUtils.equals(intent.getAction(), Intent.ACTION_SEND_MULTIPLE)) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mAdapter.handlePackagesChanged();
    }

    public void onSetupVoiceInteraction() {
        sendVoiceChoicesIfNeeded();
    }

    public void sendVoiceChoicesIfNeeded() {
        if (isVoiceInteraction()) {
            Option[] options = new Option[this.mAdapter.getCount()];
            int N = options.length;
            for (int i = 0; i < N; i++) {
                options[i] = optionForChooserTarget(this.mAdapter.getItem(i), i);
            }
            this.mPickOptionRequest = new PickTargetOptionRequest(new Prompt(getTitle()), options, null);
            getVoiceInteractor().submitRequest(this.mPickOptionRequest);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Option optionForChooserTarget(TargetInfo target, int index) {
        return new Option(target.getDisplayLabel(), index);
    }

    /* Access modifiers changed, original: protected|final */
    public final void setAdditionalTargets(Intent[] intents) {
        if (intents != null) {
            for (Intent intent : intents) {
                this.mIntents.add(intent);
            }
        }
    }

    public Intent getTargetIntent() {
        return this.mIntents.isEmpty() ? null : (Intent) this.mIntents.get(0);
    }

    /* Access modifiers changed, original: protected */
    public String getReferrerPackageName() {
        Uri referrer = getReferrer();
        if (referrer != null) {
            if ("android-app".equals(referrer.getScheme())) {
                return referrer.getHost();
            }
        }
        return null;
    }

    public int getLayoutResource() {
        return com.android.internal.R.layout.resolver_list;
    }

    /* Access modifiers changed, original: 0000 */
    public void bindProfileView() {
        DisplayResolveInfo dri = this.mAdapter.getOtherProfile();
        if (dri != null) {
            this.mProfileView.setVisibility(0);
            ImageView icon = (ImageView) this.mProfileView.findViewById(R.id.icon);
            TextView text = (TextView) this.mProfileView.findViewById(16908308);
            if (!dri.hasDisplayIcon()) {
                new LoadIconIntoViewTask(dri, icon).execute((Object[]) new Void[0]);
            }
            icon.setImageDrawable(dri.getDisplayIcon());
            text.setText(dri.getDisplayLabel());
            return;
        }
        this.mProfileView.setVisibility(8);
    }

    private void setProfileSwitchMessageId(int contentUserHint) {
        if (contentUserHint != -2 && contentUserHint != UserHandle.myUserId()) {
            boolean originIsManaged;
            UserManager userManager = (UserManager) getSystemService("user");
            UserInfo originUserInfo = userManager.getUserInfo(contentUserHint);
            if (originUserInfo != null) {
                originIsManaged = originUserInfo.isManagedProfile();
            } else {
                originIsManaged = false;
            }
            boolean targetIsManaged = userManager.isManagedProfile();
            if (originIsManaged && !targetIsManaged) {
                this.mProfileSwitchMessageId = R.string.android_forward_intent_to_owner;
            } else if (!originIsManaged && targetIsManaged) {
                this.mProfileSwitchMessageId = R.string.android_forward_intent_to_work;
            }
        }
    }

    public void setSafeForwardingMode(boolean safeForwarding) {
        this.mSafeForwardingMode = safeForwarding;
    }

    /* Access modifiers changed, original: protected */
    public CharSequence getTitleForAction(String action, int defaultTitleRes) {
        ActionTitle title = this.mResolvingHome ? ActionTitle.HOME : ActionTitle.forAction(action);
        if (title != ActionTitle.DEFAULT || defaultTitleRes == 0) {
            return getString(title.titleRes);
        }
        return getString(defaultTitleRes);
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable getIcon(Resources res, int resId) {
        try {
            return res.getDrawableForDensity(resId, this.mIconDpi);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable loadIconForResolveInfo(ResolveInfo ri) {
        try {
            if (!(ri.resolvePackageName == null || ri.icon == 0)) {
                Drawable dr = getIcon(this.mPm.getResourcesForApplication(ri.resolvePackageName), ri.icon);
                if (dr != null) {
                    return this.mIconFactory.getShadowedIcon(dr);
                }
            }
            int iconRes = ri.getIconResource();
            if (iconRes != 0) {
                Drawable dr2 = getIcon(this.mPm.getResourcesForApplication(ri.activityInfo.packageName), iconRes);
                if (dr2 != null) {
                    return this.mIconFactory.getShadowedIcon(dr2);
                }
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Couldn't find resources for package", e);
        }
        return this.mIconFactory.getBadgedIcon(ri.activityInfo.applicationInfo);
    }

    /* Access modifiers changed, original: protected */
    public void onRestart() {
        super.onRestart();
        if (!ChooserActivityInjector.isInterceptedByMiAppStore(this)) {
            if (!this.mRegistered) {
                this.mPackageMonitor.register(this, getMainLooper(), false);
                this.mRegistered = true;
            }
            this.mAdapter.handlePackagesChanged();
            if (this.mProfileView != null) {
                bindProfileView();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        if (!((getIntent().getFlags() & 268435456) == 0 || isVoiceInteraction() || this.mResolvingHome || this.mRetainInOnStop || isChangingConfigurations())) {
            finish();
        }
        if (!ChooserActivityInjector.isInterceptedByMiAppStore(this) && this.mRegistered) {
            this.mPackageMonitor.unregister();
            this.mRegistered = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        if (ChooserActivityInjector.isInterceptedByMiAppStore(this)) {
            ChooserActivityInjector.stopInterceptByMiAppStore(this);
            return;
        }
        if (!isChangingConfigurations()) {
            PickTargetOptionRequest pickTargetOptionRequest = this.mPickOptionRequest;
            if (pickTargetOptionRequest != null) {
                pickTargetOptionRequest.cancel();
            }
        }
        if (this.mPostListReadyRunnable != null) {
            getMainThreadHandler().removeCallbacks(this.mPostListReadyRunnable);
            this.mPostListReadyRunnable = null;
        }
        ResolveListAdapter resolveListAdapter = this.mAdapter;
        if (!(resolveListAdapter == null || resolveListAdapter.mResolverListController == null)) {
            this.mAdapter.mResolverListController.destroy();
        }
        MiShareTransmissionView miShareTransmissionView = this.mMiShareView;
        if (miShareTransmissionView != null) {
            miShareTransmissionView.unbind();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (!ChooserActivityInjector.isInterceptedByMiAppStore(this)) {
            resetAlwaysOrOnceButtonBar();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private boolean hasManagedProfile() {
        UserManager userManager = (UserManager) getSystemService("user");
        if (userManager == null) {
            return false;
        }
        try {
            for (UserInfo userInfo : userManager.getProfiles(getUserId())) {
                if (userInfo != null && userInfo.isManagedProfile()) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    private boolean supportsManagedProfiles(ResolveInfo resolveInfo) {
        boolean z = false;
        try {
            if (getPackageManager().getApplicationInfo(resolveInfo.activityInfo.packageName, 0).targetSdkVersion >= 21) {
                z = true;
            }
            return z;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private void setAlwaysButtonEnabled(boolean hasValidSelection, int checkedPos, boolean filtered) {
        boolean enabled = false;
        if (hasValidSelection) {
            ResolveInfo ri = this.mAdapter.resolveInfoForPosition(checkedPos, filtered);
            String str = TAG;
            if (ri == null) {
                Log.e(str, "Invalid position supplied to setAlwaysButtonEnabled");
                return;
            } else if (ri.targetUserId != -2) {
                Log.e(str, "Attempted to set selection to resolve info for another user");
                return;
            } else {
                enabled = true;
            }
        }
        this.mAlwaysButton.setEnabled(enabled);
    }

    private void showMore() {
        this.mShowMoreResolverButton = false;
        CheckBox alwaysOption = (CheckBox) findViewById(R.id.always_option);
        if (alwaysOption != null && alwaysOption.getVisibility() == 0) {
            alwaysOption.setChecked(false);
        }
        this.mAdapter.handlePackagesChanged();
        initGridViews();
        for (int i = 0; i < this.mScreenView.getScreenCount(); i++) {
            ((ArrayAdapter) ((GridView) this.mScreenView.getScreen(i)).getAdapter()).notifyDataSetChanged();
        }
    }

    public void startSelected(int which, boolean always, boolean hasIndexBeenFiltered) {
        if (!isFinishing()) {
            TargetInfo target = this.mAdapter.targetInfoForPosition(which, hasIndexBeenFiltered);
            if (target != null) {
                if ((target instanceof DisplayResolveInfo) && Build.IS_INTERNATIONAL_BUILD && ((DisplayResolveInfo) target).mShowMore) {
                    showMore();
                    return;
                }
                ResolveInfo ri = this.mAdapter.resolveInfoForPosition(which, hasIndexBeenFiltered);
                if (this.mResolvingHome && hasManagedProfile() && !supportsManagedProfiles(ri)) {
                    Toast.makeText((Context) this, String.format(getResources().getString(com.android.internal.R.string.activity_resolver_work_profiles_support), new Object[]{ri.activityInfo.loadLabel(getPackageManager()).toString()}), 1).show();
                    return;
                }
                if (onTargetSelected(target, always) && !this.mRecommendable) {
                    int i;
                    if (always && this.mAlwaysUseOption) {
                        MetricsLogger.action((Context) this, (int) MetricsEvent.ACTION_APP_DISAMBIG_ALWAYS);
                    } else if (this.mAlwaysUseOption) {
                        MetricsLogger.action((Context) this, (int) MetricsEvent.ACTION_APP_DISAMBIG_JUST_ONCE);
                    } else {
                        MetricsLogger.action((Context) this, (int) MetricsEvent.ACTION_APP_DISAMBIG_TAP);
                    }
                    if (this.mAdapter.hasFilteredItem()) {
                        i = MetricsEvent.ACTION_HIDE_APP_DISAMBIG_APP_FEATURED;
                    } else {
                        i = MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED;
                    }
                    MetricsLogger.action((Context) this, i);
                    sendItemSelectedAnalyticsBroadcast(this.mAdapter.getDisplayList(), which, false, always ? "always" : "once");
                    finish();
                }
            }
        }
    }

    public Intent getReplacementIntent(ActivityInfo aInfo, Intent defIntent) {
        return defIntent;
    }

    private boolean shouldBlockThirdDesktop(String packageName) {
        boolean z = false;
        try {
            Bundle bundle = getContentResolver().call(Uri.parse("content://com.miui.sec.THIRD_DESKTOP"), "getModeAndList", null, null);
            boolean mode = bundle.getInt("mode", 0);
            ArrayList<String> list = bundle.getStringArrayList(Slice.HINT_LIST);
            boolean z2 = list != null && list.contains(packageName);
            if (mode == z2) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            Log.e(TAG, "get third desktop provider exception!", e);
            return false;
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0250  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0243  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0258 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0232  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0243  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0250  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0258 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x0266  */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x01dc  */
    /* JADX WARNING: Missing block: B:45:0x00c8, code skipped:
            if ("content".equals(r10.getScheme()) == false) goto L_0x00ca;
     */
    public boolean onTargetSelected(com.android.internal.app.ResolverActivity.TargetInfo r25, boolean r26) {
        /*
        r24 = this;
        r1 = r24;
        r2 = r25.getResolveInfo();
        r0 = r25.getResolvedIntent();
        r3 = r0;
        r4 = 1;
        if (r3 == 0) goto L_0x0297;
    L_0x000e:
        r0 = r1.mAlwaysUseOption;
        if (r0 != 0) goto L_0x001f;
    L_0x0012:
        r0 = r1.mAdapter;
        r0 = r0.hasFilteredItem();
        if (r0 == 0) goto L_0x001b;
    L_0x001a:
        goto L_0x001f;
    L_0x001b:
        r16 = r2;
        goto L_0x0299;
    L_0x001f:
        r0 = r1.mAdapter;
        r0 = r0.mUnfilteredResolveList;
        if (r0 == 0) goto L_0x0297;
    L_0x0025:
        r0 = new android.content.IntentFilter;
        r0.<init>();
        r5 = r0;
        r0 = r3.getSelector();
        if (r0 == 0) goto L_0x0037;
    L_0x0031:
        r0 = r3.getSelector();
        r6 = r0;
        goto L_0x0039;
    L_0x0037:
        r0 = r3;
        r6 = r0;
    L_0x0039:
        r7 = r6.getAction();
        if (r7 == 0) goto L_0x0042;
    L_0x003f:
        r5.addAction(r7);
    L_0x0042:
        r8 = r6.getCategories();
        if (r8 == 0) goto L_0x005c;
    L_0x0048:
        r0 = r8.iterator();
    L_0x004c:
        r9 = r0.hasNext();
        if (r9 == 0) goto L_0x005c;
    L_0x0052:
        r9 = r0.next();
        r9 = (java.lang.String) r9;
        r5.addCategory(r9);
        goto L_0x004c;
    L_0x005c:
        r0 = "android.intent.category.DEFAULT";
        r5.addCategory(r0);
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 != 0) goto L_0x0088;
    L_0x0065:
        r0 = "android.intent.category.HOME";
        r0 = r5.hasCategory(r0);
        if (r0 == 0) goto L_0x0088;
    L_0x006d:
        r0 = r2.activityInfo;
        r0 = r0.packageName;
        r0 = r1.shouldBlockThirdDesktop(r0);
        if (r0 == 0) goto L_0x0088;
    L_0x0077:
        r0 = new android.content.Intent;
        r0.<init>();
        r9 = "com.miui.securitycenter";
        r10 = "com.miui.securitycenter.activity.ThirdDesktopAlertActivity";
        r0 = r0.setClassName(r9, r10);
        r1.startActivity(r0);
        return r4;
    L_0x0088:
        r0 = r2.match;
        r9 = 268369920; // 0xfff0000 float:2.5144941E-29 double:1.32592358E-315;
        r9 = r9 & r0;
        r10 = r6.getData();
        r11 = "ResolverActivity";
        r12 = 6291456; // 0x600000 float:8.816208E-39 double:3.1083923E-317;
        if (r9 != r12) goto L_0x00a8;
    L_0x0097:
        r13 = r6.resolveType(r1);
        if (r13 == 0) goto L_0x00a8;
    L_0x009d:
        r5.addDataType(r13);	 Catch:{ MalformedMimeTypeException -> 0x00a1 }
        goto L_0x00a8;
    L_0x00a1:
        r0 = move-exception;
        r14 = r0;
        r0 = r14;
        android.util.Log.w(r11, r0);
        r5 = 0;
    L_0x00a8:
        if (r10 == 0) goto L_0x015d;
    L_0x00aa:
        r13 = r10.getScheme();
        if (r13 == 0) goto L_0x015d;
    L_0x00b0:
        if (r9 != r12) goto L_0x00ca;
    L_0x00b2:
        r12 = r10.getScheme();
        r13 = "file";
        r12 = r13.equals(r12);
        if (r12 != 0) goto L_0x015d;
    L_0x00be:
        r12 = r10.getScheme();
        r13 = "content";
        r12 = r13.equals(r12);
        if (r12 != 0) goto L_0x015d;
    L_0x00ca:
        r12 = r10.getScheme();
        r5.addDataScheme(r12);
        r12 = r2.filter;
        r12 = r12.schemeSpecificPartsIterator();
        if (r12 == 0) goto L_0x00fe;
    L_0x00d9:
        r13 = r10.getSchemeSpecificPart();
    L_0x00dd:
        if (r13 == 0) goto L_0x00fe;
    L_0x00df:
        r14 = r12.hasNext();
        if (r14 == 0) goto L_0x00fe;
    L_0x00e5:
        r14 = r12.next();
        r14 = (android.os.PatternMatcher) r14;
        r15 = r14.match(r13);
        if (r15 == 0) goto L_0x00fd;
    L_0x00f1:
        r15 = r14.getPath();
        r0 = r14.getType();
        r5.addDataSchemeSpecificPart(r15, r0);
        goto L_0x00fe;
    L_0x00fd:
        goto L_0x00dd;
    L_0x00fe:
        r0 = r2.filter;
        r0 = r0.authoritiesIterator();
        if (r0 == 0) goto L_0x0130;
    L_0x0106:
        r13 = r0.hasNext();
        if (r13 == 0) goto L_0x0130;
    L_0x010c:
        r13 = r0.next();
        r13 = (android.content.IntentFilter.AuthorityEntry) r13;
        r14 = r13.match(r10);
        if (r14 < 0) goto L_0x012e;
    L_0x0118:
        r14 = r13.getPort();
        r15 = r13.getHost();
        if (r14 < 0) goto L_0x0129;
    L_0x0122:
        r17 = java.lang.Integer.toString(r14);
        r4 = r17;
        goto L_0x012a;
    L_0x0129:
        r4 = 0;
    L_0x012a:
        r5.addDataAuthority(r15, r4);
        goto L_0x0130;
    L_0x012e:
        r4 = 1;
        goto L_0x0106;
    L_0x0130:
        r4 = r2.filter;
        r4 = r4.pathsIterator();
        if (r4 == 0) goto L_0x015d;
    L_0x0138:
        r12 = r10.getPath();
    L_0x013c:
        if (r12 == 0) goto L_0x015d;
    L_0x013e:
        r13 = r4.hasNext();
        if (r13 == 0) goto L_0x015d;
    L_0x0144:
        r13 = r4.next();
        r13 = (android.os.PatternMatcher) r13;
        r14 = r13.match(r12);
        if (r14 == 0) goto L_0x015c;
    L_0x0150:
        r14 = r13.getPath();
        r15 = r13.getType();
        r5.addDataPath(r14, r15);
        goto L_0x015d;
    L_0x015c:
        goto L_0x013c;
    L_0x015d:
        if (r5 == 0) goto L_0x028e;
    L_0x015f:
        r0 = r1.mAdapter;
        r0 = r0.mUnfilteredResolveList;
        r4 = r0.size();
        r0 = r1.mAdapter;
        r0 = r0.mOtherProfile;
        if (r0 == 0) goto L_0x0171;
    L_0x016f:
        r0 = 1;
        goto L_0x0172;
    L_0x0171:
        r0 = 0;
    L_0x0172:
        r13 = r0;
        if (r13 != 0) goto L_0x0179;
    L_0x0175:
        r0 = new android.content.ComponentName[r4];
        r14 = r0;
        goto L_0x017e;
    L_0x0179:
        r0 = r4 + 1;
        r0 = new android.content.ComponentName[r0];
        r14 = r0;
    L_0x017e:
        r0 = 0;
        r15 = 0;
    L_0x0180:
        if (r15 >= r4) goto L_0x01b5;
    L_0x0182:
        r12 = r1.mAdapter;
        r12 = r12.mUnfilteredResolveList;
        r12 = r12.get(r15);
        r12 = (com.android.internal.app.ResolverActivity.ResolvedComponentInfo) r12;
        r19 = r6;
        r6 = 0;
        r12 = r12.getResolveInfoAt(r6);
        r6 = new android.content.ComponentName;
        r20 = r9;
        r9 = r12.activityInfo;
        r9 = r9.packageName;
        r21 = r11;
        r11 = r12.activityInfo;
        r11 = r11.name;
        r6.<init>(r9, r11);
        r14[r15] = r6;
        r6 = r12.match;
        if (r6 <= r0) goto L_0x01ac;
    L_0x01aa:
        r0 = r12.match;
    L_0x01ac:
        r15 = r15 + 1;
        r6 = r19;
        r9 = r20;
        r11 = r21;
        goto L_0x0180;
    L_0x01b5:
        r19 = r6;
        r20 = r9;
        r21 = r11;
        if (r13 == 0) goto L_0x01d9;
    L_0x01bd:
        r6 = r1.mAdapter;
        r6 = r6.mOtherProfile;
        r6 = r6.getResolvedComponentName();
        r14[r4] = r6;
        r6 = r1.mAdapter;
        r6 = r6.mOtherProfile;
        r6 = r6.getResolveInfo();
        r6 = r6.match;
        if (r6 <= r0) goto L_0x01d9;
    L_0x01d7:
        r0 = r6;
        goto L_0x01da;
    L_0x01d9:
        r6 = r0;
    L_0x01da:
        if (r26 == 0) goto L_0x0266;
    L_0x01dc:
        r0 = r24.getUserId();
        r9 = r24.getPackageManager();
        r11 = r3.getComponent();
        r9.addPreferredActivity(r5, r6, r14, r11);
        r11 = r2.handleAllWebDataURI;
        if (r11 == 0) goto L_0x0207;
    L_0x01ef:
        r11 = r9.getDefaultBrowserPackageNameAsUser(r0);
        r12 = android.text.TextUtils.isEmpty(r11);
        if (r12 == 0) goto L_0x0200;
    L_0x01f9:
        r12 = r2.activityInfo;
        r12 = r12.packageName;
        r9.setDefaultBrowserPackageNameAsUser(r12, r0);
    L_0x0200:
        r16 = r2;
        r22 = r4;
        r23 = r7;
        goto L_0x0265;
    L_0x0207:
        r11 = r3.getComponent();
        r12 = r11.getPackageName();
        if (r10 == 0) goto L_0x0216;
    L_0x0211:
        r15 = r10.getScheme();
        goto L_0x0217;
    L_0x0216:
        r15 = 0;
    L_0x0217:
        if (r15 == 0) goto L_0x022d;
    L_0x0219:
        r16 = r2;
        r2 = "http";
        r2 = r15.equals(r2);
        if (r2 != 0) goto L_0x022b;
    L_0x0223:
        r2 = "https";
        r2 = r15.equals(r2);
        if (r2 == 0) goto L_0x022f;
    L_0x022b:
        r2 = 1;
        goto L_0x0230;
    L_0x022d:
        r16 = r2;
    L_0x022f:
        r2 = 0;
    L_0x0230:
        if (r7 == 0) goto L_0x023e;
    L_0x0232:
        r22 = r4;
        r4 = "android.intent.action.VIEW";
        r4 = r7.equals(r4);
        if (r4 == 0) goto L_0x0240;
    L_0x023c:
        r4 = 1;
        goto L_0x0241;
    L_0x023e:
        r22 = r4;
    L_0x0240:
        r4 = 0;
    L_0x0241:
        if (r8 == 0) goto L_0x0250;
    L_0x0243:
        r23 = r7;
        r7 = "android.intent.category.BROWSABLE";
        r7 = r8.contains(r7);
        if (r7 == 0) goto L_0x0252;
    L_0x024d:
        r18 = 1;
        goto L_0x0254;
    L_0x0250:
        r23 = r7;
    L_0x0252:
        r18 = 0;
    L_0x0254:
        r7 = r18;
        if (r2 == 0) goto L_0x0263;
    L_0x0258:
        if (r4 == 0) goto L_0x0263;
    L_0x025a:
        if (r7 == 0) goto L_0x0263;
    L_0x025c:
        r18 = r2;
        r2 = 2;
        r9.updateIntentVerificationStatusAsUser(r12, r2, r0);
        goto L_0x0265;
    L_0x0263:
        r18 = r2;
    L_0x0265:
        goto L_0x0299;
    L_0x0266:
        r16 = r2;
        r22 = r4;
        r23 = r7;
        r0 = r1.mAdapter;	 Catch:{ RemoteException -> 0x0276 }
        r0 = r0.mResolverListController;	 Catch:{ RemoteException -> 0x0276 }
        r0.setLastChosen(r3, r5, r6);	 Catch:{ RemoteException -> 0x0276 }
        goto L_0x0299;
    L_0x0276:
        r0 = move-exception;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Error calling setLastChosenActivity\n";
        r2.append(r4);
        r2.append(r0);
        r2 = r2.toString();
        r4 = r21;
        android.util.Log.d(r4, r2);
        goto L_0x0299;
    L_0x028e:
        r16 = r2;
        r19 = r6;
        r23 = r7;
        r20 = r9;
        goto L_0x0299;
    L_0x0297:
        r16 = r2;
        r24.safelyStartActivity(r25);
        r2 = 1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.ResolverActivity.onTargetSelected(com.android.internal.app.ResolverActivity$TargetInfo, boolean):boolean");
    }

    public void safelyStartActivity(TargetInfo cti) {
        StrictMode.disableDeathOnFileUriExposure();
        try {
            safelyStartActivityInternal(cti);
        } finally {
            StrictMode.enableDeathOnFileUriExposure();
        }
    }

    private void safelyStartActivityInternal(TargetInfo cti) {
        int i = this.mProfileSwitchMessageId;
        if (i != -1) {
            Toast.makeText((Context) this, getString(i), 1).show();
        }
        if (this.mSafeForwardingMode) {
            try {
                if (cti.startAsCaller(this, null, -10000)) {
                    onActivityStarted(cti);
                }
            } catch (RuntimeException e) {
                String launchedFromPackage;
                try {
                    launchedFromPackage = ActivityTaskManager.getService().getLaunchedFromPackage(getActivityToken());
                } catch (RemoteException e2) {
                    launchedFromPackage = "??";
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to launch as uid ");
                stringBuilder.append(this.mLaunchedFromUid);
                stringBuilder.append(" package ");
                stringBuilder.append(launchedFromPackage);
                stringBuilder.append(", while running in ");
                stringBuilder.append(ActivityThread.currentProcessName());
                Slog.wtf(TAG, stringBuilder.toString(), e);
            }
            return;
        }
        if (cti.start(this, null)) {
            onActivityStarted(cti);
        }
    }

    public void onActivityStarted(TargetInfo cti) {
    }

    public boolean shouldGetActivityMetadata() {
        return true;
    }

    public boolean shouldAutoLaunchSingleChoice(TargetInfo target) {
        return true;
    }

    public void showTargetDetails(ResolveInfo ri) {
        startActivity(new Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", ri.activityInfo.packageName, null)).addFlags(524288));
        dismiss();
    }

    public ResolveListAdapter createAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
        return new ResolveListAdapter(context, payloadIntents, initialIntents, rList, launchedFromUid, filterLastUsed, createListController());
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public ResolverListController createListController() {
        return new ResolverListController(this, this.mPm, getTargetIntent(), getReferrerPackageName(), this.mLaunchedFromUid);
    }

    public void configureContentView(List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList) {
        this.mAlertParams.mView = getLayoutInflater().inflate((int) R.layout.resolver_screen, null);
        this.mScreenView = (ScreenView) this.mAlertParams.mView.findViewById(R.id.screen_view);
        this.mScreenView.setScreenTransitionType(1);
        this.mAlertParams.mNegativeButtonText = getResources().getString(17039360);
        this.mAlertParams.mNegativeButtonListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ResolverActivity.this.finish();
            }
        };
        int i = this.mLaunchedFromUid;
        boolean z = this.mAlwaysUseOption && !isVoiceInteraction();
        this.mAdapter = createAdapter(this, payloadIntents, initialIntents, rList, i, z);
        boolean rebuildCompleted = this.mAdapter.rebuildList();
        int count = this.mAdapter.getUnfilteredCount();
        if (rebuildCompleted && count == 1 && this.mAdapter.getOtherProfile() == null) {
            TargetInfo target = this.mAdapter.targetInfoForPosition(0, false);
            if (shouldAutoLaunchSingleChoice(target)) {
                safelyStartActivity(target);
                this.mPackageMonitor.unregister();
                this.mRegistered = false;
                finish();
                return;
            }
        }
        if (count == 0 && this.mAdapter.mPlaceholderCount == 0) {
            this.mAlertParams.mMessage = getText(R.string.android_noApplications);
        } else {
            bindOfficalRecommendView();
            initGridViews();
            initMiShareView();
            onPrepareAdapterView(null, this.mAdapter);
            if (this.mRecommendable) {
                this.mAlertParams.mNegativeButtonText = getResources().getString(R.string.resolver_checkbox_once);
                this.mAlertParams.mNegativeButtonListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ResolverActivity resolverActivity = ResolverActivity.this;
                        resolverActivity.startSelected(resolverActivity.mChosenIndex, false, false);
                        resolverActivity = ResolverActivity.this;
                        resolverActivity.sendItemSelectedAnalyticsBroadcast(resolverActivity.mAdapter.mDisplayList, ResolverActivity.this.mChosenIndex, true, "once");
                        ResolverActivity.this.finish();
                    }
                };
                this.mAlertParams.mPositiveButtonText = getResources().getString(R.string.resolver_checkbox_always);
                this.mAlertParams.mPositiveButtonListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ResolverActivity resolverActivity = ResolverActivity.this;
                        resolverActivity.startSelected(resolverActivity.mChosenIndex, true, false);
                        resolverActivity = ResolverActivity.this;
                        resolverActivity.sendItemSelectedAnalyticsBroadcast(resolverActivity.mAdapter.mDisplayList, ResolverActivity.this.mChosenIndex, true, "always");
                        ResolverActivity.this.finish();
                    }
                };
            } else {
                this.mAlertParams.mNegativeButtonText = getResources().getString(17039360);
                this.mAlertParams.mNegativeButtonListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ResolverActivity.this.finish();
                    }
                };
            }
        }
    }

    private void initGridViews() {
        this.mScreenView.removeAllScreens();
        int count = this.mAdapter.getCount();
        int i = this.mMaxPerScreen;
        int divider = count / i;
        i = count % i == 0 ? divider : divider + 1;
        this.mScreenView.removeAllViews();
        int i2 = 2;
        int count2;
        if (i == 1) {
            this.mScreenView.setSeekBarVisibility(8);
            GridView grid = inflateGridView(i);
            grid.setPadding(grid.getPaddingLeft(), grid.getPaddingTop(), grid.getPaddingRight(), 0);
            count2 = grid.getCount();
            int i3 = this.mMaxPerScreen;
            if (this.mRecommendable) {
                i2 = 1;
            }
            grid.setNumColumns(Math.min(count2, i3 / i2));
            this.mScreenView.addView(grid);
        } else {
            this.mScreenView.setSeekBarPosition(new FrameLayout.LayoutParams(-2, -2, 81));
            this.mScreenView.setSeekBarVisibility(0);
            for (count2 = 0; count2 < i; count2++) {
                GridView grid2 = inflateGridView(count2 + 1);
                grid2.setNumColumns(this.mMaxPerScreen / (this.mRecommendable ? 1 : 2));
                this.mScreenView.addView(grid2);
            }
        }
        this.mScreenView.setCurrentScreen(0);
    }

    private GridView inflateGridView(int page) {
        GridView grid = (GridView) LayoutInflater.from(this).inflate((int) R.layout.resolver_grid_view, this.mScreenView, false);
        grid.setAdapter(new PageGridAdapter(this, page));
        ItemClickListener listener = new ItemClickListener(page);
        grid.setSelector((Drawable) new ColorDrawable(0));
        grid.setOnItemClickListener(listener);
        grid.setOnItemLongClickListener(listener);
        return grid;
    }

    public void onPrepareAdapterView(AbsListView adapterView, ResolveListAdapter adapter) {
    }

    public void setTitleAndIcon() {
    }

    public void resetAlwaysOrOnceButtonBar() {
    }

    private boolean useLayoutWithDefault() {
        return this.mAlwaysUseOption && this.mAdapter.hasFilteredItem();
    }

    /* Access modifiers changed, original: protected */
    public void setRetainInOnStop(boolean retainInOnStop) {
        this.mRetainInOnStop = retainInOnStop;
    }

    static boolean resolveInfoMatch(ResolveInfo lhs, ResolveInfo rhs) {
        if (lhs == null) {
            return rhs == null;
        } else {
            if (lhs.activityInfo == null) {
                if (rhs.activityInfo == null) {
                    return true;
                }
                return false;
            } else if (Objects.equals(lhs.activityInfo.name, rhs.activityInfo.name) && Objects.equals(lhs.activityInfo.packageName, rhs.activityInfo.packageName)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void sendItemSelectedAnalyticsBroadcast(List<DisplayResolveInfo> displayResolveInfos, int pos, boolean isOfficalType, String selectedType) {
        Bundle data = collectAnalyticsData(displayResolveInfos);
        data.putInt("selectedItem", pos);
        data.putBoolean("hasOfficalRecommendation", isOfficalType);
        data.putString("selectedType", selectedType);
        data.putString("refApp", this.mReferrerPackage);
        sendAnalyticBroadcast(displayResolveInfos, "com.miui.action.resolver_activity_item_select", data);
    }

    private void sendResolverShownAnalyticsBroadcast(List<DisplayResolveInfo> displayResolveInfos, boolean isOfficalType) {
        Bundle data = collectAnalyticsData(displayResolveInfos);
        data.putBoolean("hasOfficalRecommendation", isOfficalType);
        data.putString("refApp", this.mReferrerPackage);
        sendAnalyticBroadcast(displayResolveInfos, "com.miui.action.resolver_activity_shown", data);
    }

    private Bundle collectAnalyticsData(List<DisplayResolveInfo> displayResolveInfos) {
        ArrayList<String> pkgList = new ArrayList();
        for (DisplayResolveInfo info : displayResolveInfos) {
            pkgList.add(info.getResolveInfo().activityInfo.packageName);
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("displayPackages", pkgList);
        bundle.putInt("chooserId", hashCode());
        bundle.putBoolean("recommendFirstItem", this.mRecommendFirstItem);
        bundle.putInt("maxCountPerScreen", this.mMaxPerScreen);
        return bundle;
    }

    private void sendAnalyticBroadcast(List<DisplayResolveInfo> displayResolveInfos, String metaKey, Bundle data) {
        for (DisplayResolveInfo info : displayResolveInfos) {
            Bundle meta = info.getResolveInfo().activityInfo.metaData;
            String action = meta != null ? meta.getString(metaKey) : null;
            if (action != null) {
                Intent intent = new Intent(action);
                intent.putExtras(data);
                intent.setPackage(info.getResolveInfo().activityInfo.packageName);
                sendBroadcast(intent, "miui.permission.USE_INTERNAL_GENERAL_API");
            }
        }
    }

    private void initMiShareView() {
        this.mMiShareView = (MiShareTransmissionView) this.mAlertParams.mView.findViewById(R.id.mishare_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String str = Intent.EXTRA_INTENT;
            if (bundle.getParcelable(str) != null && MiShareConnectivity.isAvailable(this)) {
                Intent intent = (Intent) bundle.getParcelable(str);
                if (intent == null || intent.getClipData() == null) {
                    this.mMiShareView.setVisibility(8);
                    return;
                }
                ClipData data = intent.getClipData();
                this.mMiShareView.showFileDetailGroup(true);
                this.mMiShareView.bind();
                this.mMiShareView.setIntent(intent.hasExtra("android.intent.extra.PACKAGE_NAME") ? intent : null);
                List<Uri> files = new ArrayList();
                int count = data.getItemCount();
                for (int i = 0; i < count; i++) {
                    Item item = data.getItemAt(i);
                    if (!(item == null || item.getUri() == null)) {
                        files.add(item.getUri());
                    }
                }
                if (files.isEmpty()) {
                    this.mMiShareView.setVisibility(8);
                } else {
                    this.mMiShareView.setVisibility(0);
                    this.mMiShareView.setFiles(files);
                }
                return;
            }
        }
        this.mMiShareView.setVisibility(8);
    }

    private void bindOfficalRecommendView() {
        ResolveListAdapter resolveListAdapter = this.mAdapter;
        if (resolveListAdapter != null && resolveListAdapter.hasOfficalRecommendation() && this.mAdapter.getRecommendItem() != null) {
            this.mRecommendOfficalLayout = (RelativeLayout) this.mAlertParams.mView.findViewById(R.id.resolver_recommend_offical_layout);
            this.mMaxPerScreen = 4;
            this.mRecommendOfficalLayout.setVisibility(0);
            initOfficalRecommendView(this.mRecommendOfficalLayout);
        }
    }

    private void initOfficalRecommendView(RelativeLayout recommendOfficalLayout) {
        this.mIconSize = getResources().getDimensionPixelSize(R.dimen.resolver_screenview_other_icon_length);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mScreenView.getLayoutParams();
        params.setMargins(getResources().getDimensionPixelOffset(R.dimen.resolver_screenview_marign_left), 0, getResources().getDimensionPixelOffset(R.dimen.resolver_screenview_marign_right), 0);
        this.mScreenView.setLayoutParams(params);
        this.mOfficalRecommendIconIV = (ImageView) recommendOfficalLayout.findViewById(R.id.resolver_offical_recommend_icon_iv);
        this.mOfficalRecommendNameTV = (TextView) recommendOfficalLayout.findViewById(R.id.resolver_offical_recommend_name_tv);
        this.mResolverRecommendTagTv = (TextView) recommendOfficalLayout.findViewById(R.id.resolver_recommend_tag_tv);
        this.mOfficalRecommendRl = (RelativeLayout) recommendOfficalLayout.findViewById(R.id.resolver_offical_recommend_rl);
        this.mOfficalRecommendRl.setSelected(true);
        this.mChosenView = this.mOfficalRecommendRl;
        this.mChosenIndex = 0;
        TargetInfo recommendInfo = this.mAdapter.getRecommendItem();
        if ((recommendInfo instanceof DisplayResolveInfo) && !((DisplayResolveInfo) recommendInfo).hasDisplayIcon()) {
            new LoadAdapterIconTask(null, (DisplayResolveInfo) recommendInfo).execute((Object[]) new Void[0]);
        }
        bindOfficalRecommendIcon();
        this.mOfficalRecommendRl.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ResolverActivity.this.mChosenView != v && ResolverActivity.this.mChosenView != null) {
                    ResolverActivity.this.mChosenView.setSelected(false);
                    v.setSelected(true);
                    ResolverActivity.this.mChosenView = v;
                    ResolverActivity.this.mChosenIndex = 0;
                }
            }
        });
        this.mOfficalRecommendRl.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                ResolverActivity.this.showTargetDetails(ResolverActivity.this.mAdapter.resolveInfoForPosition(-1, true));
                return true;
            }
        });
    }

    private void bindOfficalRecommendIcon() {
        TargetInfo recommendInfo = this.mAdapter.getRecommendItem();
        if (recommendInfo != null && (!(recommendInfo instanceof DisplayResolveInfo) || ((DisplayResolveInfo) recommendInfo).hasDisplayIcon())) {
            this.mResolverRecommendTagTv.setTextColor(getResources().getColor(R.color.resolver_recommend_tag_tv_color));
            this.mOfficalRecommendIconIV.setImageDrawable(recommendInfo.getDisplayIcon());
            CharSequence displayLabel = recommendInfo.getDisplayLabel() == null ? "" : recommendInfo.getDisplayLabel().toString();
            this.mOfficalRecommendRl.setBackgroundResource(R.drawable.resolver_offical_recommend_bg);
            this.mResolverRecommendTagTv.setBackgroundResource(R.drawable.resolver_recommend_tag_bg);
            if (BrowserContract.AUTHORITY.equals(recommendInfo.getResolveInfo().activityInfo.packageName)) {
                displayLabel = getResources().getString(R.string.resolver_mi_browser);
            } else {
                if ("com.miui.video".equals(recommendInfo.getResolveInfo().activityInfo.packageName)) {
                    this.mResolverRecommendTagTv.setBackgroundResource(R.drawable.resolver_recommend_tag_video_bg);
                    this.mResolverRecommendTagTv.setTextColor(getResources().getColor(R.color.resolver_recommend_tag_tv_color_mivideo));
                    this.mOfficalRecommendRl.setBackgroundResource(R.drawable.resolver_offical_recommend_video_bg);
                }
            }
            this.mOfficalRecommendNameTV.setText(displayLabel);
        }
    }

    static final boolean isSpecificUriMatch(int match) {
        match &= IntentFilter.MATCH_CATEGORY_MASK;
        return match >= IntentFilter.MATCH_CATEGORY_HOST && match <= IntentFilter.MATCH_CATEGORY_PATH;
    }
}
