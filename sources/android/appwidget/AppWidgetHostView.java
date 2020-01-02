package android.appwidget;

import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RemoteViews;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViews.OnViewAppliedListener;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;
import android.widget.TextView;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class AppWidgetHostView extends FrameLayout {
    private static final Filter INFLATER_FILTER = -$$Lambda$AppWidgetHostView$AzPWN1sIsRb7M-0Ss1rK2mksT-o.INSTANCE;
    private static final String KEY_JAILED_ARRAY = "jail";
    static final boolean LOGD = false;
    static final String TAG = "AppWidgetHostView";
    static final int VIEW_MODE_CONTENT = 1;
    static final int VIEW_MODE_DEFAULT = 3;
    static final int VIEW_MODE_ERROR = 2;
    static final int VIEW_MODE_NOINIT = 0;
    @UnsupportedAppUsage
    int mAppWidgetId;
    private Executor mAsyncExecutor;
    Context mContext;
    @UnsupportedAppUsage
    AppWidgetProviderInfo mInfo;
    private CancellationSignal mLastExecutionSignal;
    int mLayoutId;
    private OnClickHandler mOnClickHandler;
    private boolean mOnLightBackground;
    Context mRemoteContext;
    View mView;
    int mViewMode;

    private class ViewApplyListener implements OnViewAppliedListener {
        private final boolean mIsReapply;
        private final int mLayoutId;
        private final RemoteViews mViews;

        public ViewApplyListener(RemoteViews views, int layoutId, boolean isReapply) {
            this.mViews = views;
            this.mLayoutId = layoutId;
            this.mIsReapply = isReapply;
        }

        public void onViewApplied(View v) {
            AppWidgetHostView appWidgetHostView = AppWidgetHostView.this;
            appWidgetHostView.mLayoutId = this.mLayoutId;
            appWidgetHostView.mViewMode = 1;
            appWidgetHostView.applyContent(v, this.mIsReapply, null);
        }

        public void onError(Exception e) {
            if (this.mIsReapply) {
                AppWidgetHostView appWidgetHostView = AppWidgetHostView.this;
                RemoteViews remoteViews = this.mViews;
                Context context = appWidgetHostView.mContext;
                AppWidgetHostView appWidgetHostView2 = AppWidgetHostView.this;
                appWidgetHostView.mLastExecutionSignal = remoteViews.applyAsync(context, appWidgetHostView2, appWidgetHostView2.mAsyncExecutor, new ViewApplyListener(this.mViews, this.mLayoutId, false), AppWidgetHostView.this.mOnClickHandler);
                return;
            }
            AppWidgetHostView.this.applyContent(null, false, e);
        }
    }

    public AppWidgetHostView(Context context) {
        this(context, 17432576, 17432577);
    }

    public AppWidgetHostView(Context context, OnClickHandler handler) {
        this(context, 17432576, 17432577);
        this.mOnClickHandler = handler;
    }

    public AppWidgetHostView(Context context, int animationIn, int animationOut) {
        super(context);
        this.mViewMode = 0;
        this.mLayoutId = -1;
        this.mContext = context;
        setIsRootNamespace(true);
    }

    public void setOnClickHandler(OnClickHandler handler) {
        this.mOnClickHandler = handler;
    }

    public void setAppWidget(int appWidgetId, AppWidgetProviderInfo info) {
        this.mAppWidgetId = appWidgetId;
        this.mInfo = info;
        Rect padding = getDefaultPadding();
        setPadding(padding.left, padding.top, padding.right, padding.bottom);
        if (info != null) {
            String description = info.loadLabel(getContext().getPackageManager());
            if ((info.providerInfo.applicationInfo.flags & 1073741824) != 0) {
                description = Resources.getSystem().getString(R.string.suspended_widget_accessibility, description);
            }
            setContentDescription(description);
        }
    }

    public static Rect getDefaultPaddingForWidget(Context context, ComponentName component, Rect padding) {
        return getDefaultPaddingForWidget(context, padding);
    }

    private static Rect getDefaultPaddingForWidget(Context context, Rect padding) {
        if (padding == null) {
            padding = new Rect(0, 0, 0, 0);
        } else {
            padding.set(0, 0, 0, 0);
        }
        Resources r = context.getResources();
        padding.left = r.getDimensionPixelSize(R.dimen.default_app_widget_padding_left);
        padding.right = r.getDimensionPixelSize(R.dimen.default_app_widget_padding_right);
        padding.top = r.getDimensionPixelSize(R.dimen.default_app_widget_padding_top);
        padding.bottom = r.getDimensionPixelSize(R.dimen.default_app_widget_padding_bottom);
        return padding;
    }

    private Rect getDefaultPadding() {
        return getDefaultPaddingForWidget(this.mContext, null);
    }

    public int getAppWidgetId() {
        return this.mAppWidgetId;
    }

    public AppWidgetProviderInfo getAppWidgetInfo() {
        return this.mInfo;
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        SparseArray<Parcelable> jail = new SparseArray();
        super.dispatchSaveInstanceState(jail);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(KEY_JAILED_ARRAY, jail);
        container.put(generateId(), bundle);
    }

    private int generateId() {
        int id = getId();
        return id == -1 ? this.mAppWidgetId : id;
    }

    /* Access modifiers changed, original: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        Parcelable parcelable = (Parcelable) container.get(generateId());
        SparseArray<Parcelable> jail = null;
        if (parcelable instanceof Bundle) {
            jail = ((Bundle) parcelable).getSparseParcelableArray(KEY_JAILED_ARRAY);
        }
        if (jail == null) {
            jail = new SparseArray();
        }
        try {
            super.dispatchRestoreInstanceState(jail);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failed to restoreInstanceState for widget id: ");
            stringBuilder.append(this.mAppWidgetId);
            stringBuilder.append(", ");
            AppWidgetProviderInfo appWidgetProviderInfo = this.mInfo;
            stringBuilder.append(appWidgetProviderInfo == null ? "null" : appWidgetProviderInfo.provider);
            Log.e(TAG, stringBuilder.toString(), e);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            super.onLayout(changed, left, top, right, bottom);
        } catch (RuntimeException e) {
            Log.e(TAG, "Remote provider threw runtime exception, using error view instead.", e);
            removeViewInLayout(this.mView);
            View child = getErrorView();
            prepareView(child);
            addViewInLayout(child, 0, child.getLayoutParams());
            measureChild(child, MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
            child.layout(0, 0, (child.getMeasuredWidth() + this.mPaddingLeft) + this.mPaddingRight, (child.getMeasuredHeight() + this.mPaddingTop) + this.mPaddingBottom);
            this.mView = child;
            this.mViewMode = 2;
        }
    }

    public void updateAppWidgetSize(Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight) {
        updateAppWidgetSize(newOptions, minWidth, minHeight, maxWidth, maxHeight, false);
    }

    @UnsupportedAppUsage
    public void updateAppWidgetSize(Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight, boolean ignorePadding) {
        Bundle newOptions2;
        if (newOptions == null) {
            newOptions2 = new Bundle();
        } else {
            newOptions2 = newOptions;
        }
        Rect padding = getDefaultPadding();
        float density = getResources().getDisplayMetrics().density;
        int xPaddingDips = (int) (((float) (padding.left + padding.right)) / density);
        int yPaddingDips = (int) (((float) (padding.top + padding.bottom)) / density);
        int newMaxHeight = 0;
        int newMinWidth = minWidth - (ignorePadding ? 0 : xPaddingDips);
        int newMinHeight = minHeight - (ignorePadding ? 0 : yPaddingDips);
        int newMaxWidth = maxWidth - (ignorePadding ? 0 : xPaddingDips);
        if (!ignorePadding) {
            newMaxHeight = yPaddingDips;
        }
        newMaxHeight = maxHeight - newMaxHeight;
        Bundle oldOptions = AppWidgetManager.getInstance(this.mContext).getAppWidgetOptions(this.mAppWidgetId);
        boolean needsUpdate = false;
        String str = AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH;
        int i = oldOptions.getInt(str);
        String str2 = AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT;
        String str3 = AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH;
        String str4 = AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT;
        if (!(newMinWidth == i && newMinHeight == oldOptions.getInt(str4) && newMaxWidth == oldOptions.getInt(str3) && newMaxHeight == oldOptions.getInt(str2))) {
            needsUpdate = true;
        }
        if (needsUpdate) {
            newOptions2.putInt(str, newMinWidth);
            newOptions2.putInt(str4, newMinHeight);
            newOptions2.putInt(str3, newMaxWidth);
            newOptions2.putInt(str2, newMaxHeight);
            updateAppWidgetOptions(newOptions2);
        }
    }

    public void updateAppWidgetOptions(Bundle options) {
        AppWidgetManager.getInstance(this.mContext).updateAppWidgetOptions(this.mAppWidgetId, options);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Context context = this.mRemoteContext;
        if (context == null) {
            context = this.mContext;
        }
        return new LayoutParams(context, attrs);
    }

    public void setExecutor(Executor executor) {
        CancellationSignal cancellationSignal = this.mLastExecutionSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mLastExecutionSignal = null;
        }
        this.mAsyncExecutor = executor;
    }

    public void setOnLightBackground(boolean onLightBackground) {
        this.mOnLightBackground = onLightBackground;
    }

    /* Access modifiers changed, original: 0000 */
    public void resetAppWidget(AppWidgetProviderInfo info) {
        setAppWidget(this.mAppWidgetId, info);
        this.mViewMode = 0;
        updateAppWidget(null);
    }

    public void updateAppWidget(RemoteViews remoteViews) {
        applyRemoteViews(remoteViews, true);
    }

    /* Access modifiers changed, original: protected */
    public void applyRemoteViews(RemoteViews remoteViews, boolean useAsyncIfPossible) {
        boolean recycled = false;
        View content = null;
        Exception exception = null;
        CancellationSignal cancellationSignal = this.mLastExecutionSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mLastExecutionSignal = null;
        }
        if (remoteViews != null) {
            if (this.mOnLightBackground) {
                remoteViews = remoteViews.getDarkTextViews();
            }
            if (this.mAsyncExecutor == null || !useAsyncIfPossible) {
                this.mRemoteContext = getRemoteContext();
                int layoutId = remoteViews.getLayoutId();
                if (null == null && layoutId == this.mLayoutId) {
                    try {
                        remoteViews.reapply(this.mContext, this.mView, this.mOnClickHandler);
                        content = this.mView;
                        recycled = true;
                    } catch (RuntimeException e) {
                        exception = e;
                    }
                }
                if (content == null) {
                    try {
                        content = remoteViews.apply(this.mContext, this, this.mOnClickHandler);
                    } catch (RuntimeException e2) {
                        exception = e2;
                    }
                }
                this.mLayoutId = layoutId;
                this.mViewMode = 1;
            } else {
                inflateAsync(remoteViews);
                return;
            }
        } else if (this.mViewMode != 3) {
            content = getDefaultView();
            this.mLayoutId = -1;
            this.mViewMode = 3;
        } else {
            return;
        }
        applyContent(content, recycled, exception);
    }

    private void applyContent(View content, boolean recycled, Exception exception) {
        if (content == null) {
            if (this.mViewMode != 2) {
                if (exception != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error inflating RemoteViews : ");
                    stringBuilder.append(exception.toString());
                    Log.w(TAG, stringBuilder.toString());
                }
                content = getErrorView();
                this.mViewMode = 2;
            } else {
                return;
            }
        }
        if (!recycled) {
            prepareView(content);
            addView(content);
        }
        View view = this.mView;
        if (view != content) {
            removeView(view);
            this.mView = content;
        }
    }

    private void inflateAsync(RemoteViews remoteViews) {
        this.mRemoteContext = getRemoteContext();
        int layoutId = remoteViews.getLayoutId();
        if (layoutId == this.mLayoutId) {
            View view = this.mView;
            if (view != null) {
                try {
                    this.mLastExecutionSignal = remoteViews.reapplyAsync(this.mContext, view, this.mAsyncExecutor, new ViewApplyListener(remoteViews, layoutId, true), this.mOnClickHandler);
                } catch (Exception e) {
                }
            }
        }
        if (this.mLastExecutionSignal == null) {
            this.mLastExecutionSignal = remoteViews.applyAsync(this.mContext, this, this.mAsyncExecutor, new ViewApplyListener(remoteViews, layoutId, false), this.mOnClickHandler);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void viewDataChanged(int viewId) {
        View v = findViewById(viewId);
        if (v != null && (v instanceof AdapterView)) {
            AdapterView<?> adapterView = (AdapterView) v;
            Adapter adapter = adapterView.getAdapter();
            if (adapter instanceof BaseAdapter) {
                ((BaseAdapter) adapter).notifyDataSetChanged();
            } else if (adapter == null && (adapterView instanceof RemoteAdapterConnectionCallback)) {
                ((RemoteAdapterConnectionCallback) adapterView).deferNotifyDataSetChanged();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Context getRemoteContext() {
        try {
            return this.mContext.createApplicationContext(this.mInfo.providerInfo.applicationInfo, 4);
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Package name ");
            stringBuilder.append(this.mInfo.providerInfo.packageName);
            stringBuilder.append(" not found");
            Log.e(TAG, stringBuilder.toString());
            return this.mContext;
        }
    }

    /* Access modifiers changed, original: protected */
    public void prepareView(View view) {
        LayoutParams requested = (LayoutParams) view.getLayoutParams();
        if (requested == null) {
            requested = new LayoutParams(-1, -1);
        }
        requested.gravity = 17;
        view.setLayoutParams(requested);
    }

    /* Access modifiers changed, original: protected */
    public View getDefaultView() {
        int category = AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY;
        String str = TAG;
        View defaultView = null;
        Exception exception = null;
        try {
            if (this.mInfo != null) {
                Context theirContext = getRemoteContext();
                this.mRemoteContext = theirContext;
                LayoutInflater inflater = ((LayoutInflater) theirContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).cloneInContext(theirContext);
                inflater.setFilter(INFLATER_FILTER);
                Bundle options = AppWidgetManager.getInstance(this.mContext).getAppWidgetOptions(this.mAppWidgetId);
                int layoutId = this.mInfo.initialLayout;
                if (options.containsKey(category) && options.getInt(category) == 2) {
                    int kgLayoutId = this.mInfo.initialKeyguardLayout;
                    layoutId = kgLayoutId == 0 ? layoutId : kgLayoutId;
                }
                defaultView = inflater.inflate(layoutId, (ViewGroup) this, false);
                defaultView.setOnClickListener(new -$$Lambda$AppWidgetHostView$ab7zr5jJn3-7TaWMNA8VPkK4SdQ(this));
            } else {
                Log.w(str, "can't inflate defaultView because mInfo is missing");
            }
        } catch (RuntimeException e) {
            exception = e;
        }
        if (exception != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error inflating AppWidget ");
            stringBuilder.append(this.mInfo);
            stringBuilder.append(": ");
            stringBuilder.append(exception.toString());
            Log.w(str, stringBuilder.toString());
        }
        if (defaultView == null) {
            return getErrorView();
        }
        return defaultView;
    }

    private void onDefaultViewClicked(View view) {
        if (this.mInfo != null) {
            LauncherApps launcherApps = (LauncherApps) getContext().getSystemService(LauncherApps.class);
            List<LauncherActivityInfo> activities = launcherApps.getActivityList(this.mInfo.provider.getPackageName(), this.mInfo.getProfile());
            if (!activities.isEmpty()) {
                LauncherActivityInfo ai = (LauncherActivityInfo) activities.get(0);
                launcherApps.startMainActivity(ai.getComponentName(), ai.getUser(), RemoteViews.getSourceBounds(view), null);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public View getErrorView() {
        TextView tv = new TextView(this.mContext);
        tv.setText((int) R.string.gadget_host_error_inflating);
        tv.setBackgroundColor(Color.argb(127, 0, 0, 0));
        return tv;
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setClassName(AppWidgetHostView.class.getName());
    }

    public ActivityOptions createSharedElementActivityOptions(int[] sharedViewIds, String[] sharedViewNames, Intent fillInIntent) {
        Context parentContext = getContext();
        while ((parentContext instanceof ContextWrapper) && !(parentContext instanceof Activity)) {
            parentContext = ((ContextWrapper) parentContext).getBaseContext();
        }
        if (!(parentContext instanceof Activity)) {
            return null;
        }
        List<Pair<View, String>> sharedElements = new ArrayList();
        Bundle extras = new Bundle();
        for (int i = 0; i < sharedViewIds.length; i++) {
            View view = findViewById(sharedViewIds[i]);
            if (view != null) {
                sharedElements.add(Pair.create(view, sharedViewNames[i]));
                extras.putParcelable(sharedViewNames[i], RemoteViews.getSourceBounds(view));
            }
        }
        if (sharedElements.isEmpty()) {
            return null;
        }
        fillInIntent.putExtra(RemoteViews.EXTRA_SHARED_ELEMENT_BOUNDS, extras);
        ActivityOptions opts = ActivityOptions.makeSceneTransitionAnimation((Activity) parentContext, (Pair[]) sharedElements.toArray(new Pair[sharedElements.size()]));
        opts.setPendingIntentLaunchFlags(268435456);
        return opts;
    }
}
