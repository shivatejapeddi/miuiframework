package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.IServiceConnection;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.TimedRemoteCaller;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViews.OnViewAppliedListener;
import com.android.internal.R;
import com.android.internal.widget.IRemoteViewsFactory;
import com.android.internal.widget.IRemoteViewsFactory.Stub;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executor;

public class RemoteViewsAdapter extends BaseAdapter implements Callback {
    private static final int CACHE_RESET_CONFIG_FLAGS = -1073737216;
    private static final int DEFAULT_CACHE_SIZE = 40;
    private static final int DEFAULT_LOADING_VIEW_HEIGHT = 50;
    static final int MSG_LOAD_NEXT_ITEM = 3;
    private static final int MSG_MAIN_HANDLER_COMMIT_METADATA = 1;
    private static final int MSG_MAIN_HANDLER_REMOTE_ADAPTER_CONNECTED = 3;
    private static final int MSG_MAIN_HANDLER_REMOTE_ADAPTER_DISCONNECTED = 4;
    private static final int MSG_MAIN_HANDLER_REMOTE_VIEWS_LOADED = 5;
    private static final int MSG_MAIN_HANDLER_SUPER_NOTIFY_DATA_SET_CHANGED = 2;
    static final int MSG_NOTIFY_DATA_SET_CHANGED = 2;
    static final int MSG_REQUEST_BIND = 1;
    static final int MSG_UNBIND_SERVICE = 4;
    private static final int REMOTE_VIEWS_CACHE_DURATION = 5000;
    private static final String TAG = "RemoteViewsAdapter";
    private static final int UNBIND_SERVICE_DELAY = 5000;
    private static Handler sCacheRemovalQueue;
    private static HandlerThread sCacheRemovalThread;
    private static final HashMap<RemoteViewsCacheKey, FixedSizeRemoteViewsCache> sCachedRemoteViewsCaches = new HashMap();
    private static final HashMap<RemoteViewsCacheKey, Runnable> sRemoteViewsCacheRemoveRunnables = new HashMap();
    private final int mAppWidgetId;
    private final Executor mAsyncViewLoadExecutor;
    @UnsupportedAppUsage
    private final FixedSizeRemoteViewsCache mCache;
    private final RemoteAdapterConnectionCallback mCallback;
    private final Context mContext;
    private boolean mDataReady = false;
    private final Intent mIntent;
    private ApplicationInfo mLastRemoteViewAppInfo;
    private final Handler mMainHandler;
    private final boolean mOnLightBackground;
    private OnClickHandler mRemoteViewsOnClickHandler;
    private RemoteViewsFrameLayoutRefSet mRequestedViews;
    private final RemoteServiceHandler mServiceHandler;
    private int mVisibleWindowLowerBound;
    private int mVisibleWindowUpperBound;
    @UnsupportedAppUsage
    private final HandlerThread mWorkerThread;

    public interface RemoteAdapterConnectionCallback {
        void deferNotifyDataSetChanged();

        boolean onRemoteAdapterConnected();

        void onRemoteAdapterDisconnected();

        void setRemoteViewsAdapter(Intent intent, boolean z);
    }

    public static class AsyncRemoteAdapterAction implements Runnable {
        private final RemoteAdapterConnectionCallback mCallback;
        private final Intent mIntent;

        public AsyncRemoteAdapterAction(RemoteAdapterConnectionCallback callback, Intent intent) {
            this.mCallback = callback;
            this.mIntent = intent;
        }

        public void run() {
            this.mCallback.setRemoteViewsAdapter(this.mIntent, true);
        }
    }

    private static class FixedSizeRemoteViewsCache {
        private static final float sMaxCountSlackPercent = 0.75f;
        private static final int sMaxMemoryLimitInBytes = 2097152;
        private final Configuration mConfiguration;
        private final SparseArray<RemoteViewsIndexMetaData> mIndexMetaData = new SparseArray();
        private final SparseArray<RemoteViews> mIndexRemoteViews = new SparseArray();
        private final SparseBooleanArray mIndicesToLoad = new SparseBooleanArray();
        private int mLastRequestedIndex;
        private final int mMaxCount;
        private final int mMaxCountSlack;
        private final RemoteViewsMetaData mMetaData = new RemoteViewsMetaData();
        private int mPreloadLowerBound;
        private int mPreloadUpperBound;
        private final RemoteViewsMetaData mTemporaryMetaData = new RemoteViewsMetaData();

        FixedSizeRemoteViewsCache(int maxCacheSize, Configuration configuration) {
            this.mMaxCount = maxCacheSize;
            this.mMaxCountSlack = Math.round(((float) (this.mMaxCount / 2)) * 0.75f);
            this.mPreloadLowerBound = 0;
            this.mPreloadUpperBound = -1;
            this.mLastRequestedIndex = -1;
            this.mConfiguration = new Configuration(configuration);
        }

        public void insert(int position, RemoteViews v, long itemId, int[] visibleWindow) {
            if (this.mIndexRemoteViews.size() >= this.mMaxCount) {
                this.mIndexRemoteViews.remove(getFarthestPositionFrom(position, visibleWindow));
            }
            int pruneFromPosition = this.mLastRequestedIndex;
            if (pruneFromPosition <= -1) {
                pruneFromPosition = position;
            }
            while (getRemoteViewsBitmapMemoryUsage() >= 2097152) {
                int trimIndex = getFarthestPositionFrom(pruneFromPosition, visibleWindow);
                if (trimIndex < 0) {
                    break;
                }
                this.mIndexRemoteViews.remove(trimIndex);
            }
            RemoteViewsIndexMetaData metaData = (RemoteViewsIndexMetaData) this.mIndexMetaData.get(position);
            if (metaData != null) {
                metaData.set(v, itemId);
            } else {
                this.mIndexMetaData.put(position, new RemoteViewsIndexMetaData(v, itemId));
            }
            this.mIndexRemoteViews.put(position, v);
        }

        public RemoteViewsMetaData getMetaData() {
            return this.mMetaData;
        }

        public RemoteViewsMetaData getTemporaryMetaData() {
            return this.mTemporaryMetaData;
        }

        public RemoteViews getRemoteViewsAt(int position) {
            return (RemoteViews) this.mIndexRemoteViews.get(position);
        }

        public RemoteViewsIndexMetaData getMetaDataAt(int position) {
            return (RemoteViewsIndexMetaData) this.mIndexMetaData.get(position);
        }

        public void commitTemporaryMetaData() {
            synchronized (this.mTemporaryMetaData) {
                synchronized (this.mMetaData) {
                    this.mMetaData.set(this.mTemporaryMetaData);
                }
            }
        }

        private int getRemoteViewsBitmapMemoryUsage() {
            int mem = 0;
            for (int i = this.mIndexRemoteViews.size() - 1; i >= 0; i--) {
                RemoteViews v = (RemoteViews) this.mIndexRemoteViews.valueAt(i);
                if (v != null) {
                    mem += v.estimateMemoryUsage();
                }
            }
            return mem;
        }

        private int getFarthestPositionFrom(int pos, int[] visibleWindow) {
            int maxDist = 0;
            int maxDistIndex = -1;
            int maxDistNotVisible = 0;
            int maxDistIndexNotVisible = -1;
            for (int i = this.mIndexRemoteViews.size() - 1; i >= 0; i--) {
                int index = this.mIndexRemoteViews.keyAt(i);
                int dist = Math.abs(index - pos);
                if (dist > maxDistNotVisible && Arrays.binarySearch(visibleWindow, index) < 0) {
                    maxDistIndexNotVisible = index;
                    maxDistNotVisible = dist;
                }
                if (dist >= maxDist) {
                    maxDistIndex = index;
                    maxDist = dist;
                }
            }
            if (maxDistIndexNotVisible > -1) {
                return maxDistIndexNotVisible;
            }
            return maxDistIndex;
        }

        public void queueRequestedPositionToLoad(int position) {
            this.mLastRequestedIndex = position;
            synchronized (this.mIndicesToLoad) {
                this.mIndicesToLoad.put(position, true);
            }
        }

        public boolean queuePositionsToBePreloadedFromRequestedPosition(int position) {
            int i;
            int i2 = this.mPreloadLowerBound;
            if (i2 <= position) {
                i = this.mPreloadUpperBound;
                if (position <= i && Math.abs(position - ((i + i2) / 2)) < this.mMaxCountSlack) {
                    return false;
                }
            }
            synchronized (this.mMetaData) {
                i = this.mMetaData.count;
            }
            synchronized (this.mIndicesToLoad) {
                for (i2 = this.mIndicesToLoad.size() - 1; i2 >= 0; i2--) {
                    if (!this.mIndicesToLoad.valueAt(i2)) {
                        this.mIndicesToLoad.removeAt(i2);
                    }
                }
                i2 = this.mMaxCount / 2;
                this.mPreloadLowerBound = position - i2;
                this.mPreloadUpperBound = position + i2;
                int effectiveLowerBound = Math.max(0, this.mPreloadLowerBound);
                int effectiveUpperBound = Math.min(this.mPreloadUpperBound, i - 1);
                int i3 = effectiveLowerBound;
                while (i3 <= effectiveUpperBound) {
                    if (this.mIndexRemoteViews.indexOfKey(i3) < 0 && !this.mIndicesToLoad.get(i3)) {
                        this.mIndicesToLoad.put(i3, false);
                    }
                    i3++;
                }
            }
            return true;
        }

        public int getNextIndexToLoad() {
            synchronized (this.mIndicesToLoad) {
                int index = this.mIndicesToLoad.indexOfValue(true);
                if (index < 0) {
                    index = this.mIndicesToLoad.indexOfValue(false);
                }
                if (index < 0) {
                    return -1;
                }
                int key = this.mIndicesToLoad.keyAt(index);
                this.mIndicesToLoad.removeAt(index);
                return key;
            }
        }

        public boolean containsRemoteViewAt(int position) {
            return this.mIndexRemoteViews.indexOfKey(position) >= 0;
        }

        public boolean containsMetaDataAt(int position) {
            return this.mIndexMetaData.indexOfKey(position) >= 0;
        }

        public void reset() {
            this.mPreloadLowerBound = 0;
            this.mPreloadUpperBound = -1;
            this.mLastRequestedIndex = -1;
            this.mIndexRemoteViews.clear();
            this.mIndexMetaData.clear();
            synchronized (this.mIndicesToLoad) {
                this.mIndicesToLoad.clear();
            }
        }
    }

    private static class HandlerThreadExecutor implements Executor {
        private final HandlerThread mThread;

        HandlerThreadExecutor(HandlerThread thread) {
            this.mThread = thread;
        }

        public void execute(Runnable runnable) {
            if (Thread.currentThread().getId() == this.mThread.getId()) {
                runnable.run();
            } else {
                new Handler(this.mThread.getLooper()).post(runnable);
            }
        }
    }

    private static class LoadingViewTemplate {
        public int defaultHeight;
        public final RemoteViews remoteViews;

        LoadingViewTemplate(RemoteViews views, Context context) {
            this.remoteViews = views;
            this.defaultHeight = Math.round(50.0f * context.getResources().getDisplayMetrics().density);
        }

        public void loadFirstViewHeight(RemoteViews firstView, Context context, Executor executor) {
            firstView.applyAsync(context, new RemoteViewsFrameLayout(context, null), executor, new OnViewAppliedListener() {
                public void onViewApplied(View v) {
                    try {
                        v.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                        LoadingViewTemplate.this.defaultHeight = v.getMeasuredHeight();
                    } catch (Exception e) {
                        onError(e);
                    }
                }

                public void onError(Exception e) {
                    Log.w(RemoteViewsAdapter.TAG, "Error inflating first RemoteViews", e);
                }
            });
        }
    }

    private static class RemoteServiceHandler extends Handler implements ServiceConnection {
        private final WeakReference<RemoteViewsAdapter> mAdapter;
        private boolean mBindRequested = false;
        private final Context mContext;
        private boolean mNotifyDataSetChangedPending = false;
        private IRemoteViewsFactory mRemoteViewsFactory;

        RemoteServiceHandler(Looper workerLooper, RemoteViewsAdapter adapter, Context context) {
            super(workerLooper);
            this.mAdapter = new WeakReference(adapter);
            this.mContext = context;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            this.mRemoteViewsFactory = Stub.asInterface(service);
            enqueueDeferredUnbindServiceMessage();
            RemoteViewsAdapter adapter = (RemoteViewsAdapter) this.mAdapter.get();
            if (adapter != null) {
                if (this.mNotifyDataSetChangedPending) {
                    this.mNotifyDataSetChangedPending = false;
                    Message msg = Message.obtain((Handler) this, 2);
                    handleMessage(msg);
                    msg.recycle();
                } else if (sendNotifyDataSetChange(false)) {
                    adapter.updateTemporaryMetaData(this.mRemoteViewsFactory);
                    adapter.mMainHandler.sendEmptyMessage(1);
                    adapter.mMainHandler.sendEmptyMessage(3);
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            this.mRemoteViewsFactory = null;
            RemoteViewsAdapter adapter = (RemoteViewsAdapter) this.mAdapter.get();
            if (adapter != null) {
                adapter.mMainHandler.sendEmptyMessage(4);
            }
        }

        public void handleMessage(Message msg) {
            RemoteViewsAdapter adapter = (RemoteViewsAdapter) this.mAdapter.get();
            int i = msg.what;
            if (i == 1) {
                if (adapter == null || this.mRemoteViewsFactory != null) {
                    enqueueDeferredUnbindServiceMessage();
                }
                if (!this.mBindRequested) {
                    IServiceConnection sd = this.mContext.getServiceDispatcher(this, this, InputDevice.SOURCE_HDMI);
                    try {
                        this.mBindRequested = AppWidgetManager.getInstance(this.mContext).bindRemoteViewsService(this.mContext, msg.arg1, msg.obj, sd, InputDevice.SOURCE_HDMI);
                    } catch (Exception e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to bind remoteViewsService: ");
                        stringBuilder.append(e.getMessage());
                        Log.e(RemoteViewsAdapter.TAG, stringBuilder.toString());
                    }
                }
            } else if (i == 2) {
                enqueueDeferredUnbindServiceMessage();
                if (adapter != null) {
                    if (this.mRemoteViewsFactory == null) {
                        this.mNotifyDataSetChangedPending = true;
                        adapter.requestBindService();
                    } else if (sendNotifyDataSetChange(true)) {
                        synchronized (adapter.mCache) {
                            adapter.mCache.reset();
                        }
                        adapter.updateTemporaryMetaData(this.mRemoteViewsFactory);
                        synchronized (adapter.mCache.getTemporaryMetaData()) {
                            i = adapter.mCache.getTemporaryMetaData().count;
                        }
                        for (int position : adapter.getVisibleWindow(i)) {
                            if (position < i) {
                                adapter.updateRemoteViews(this.mRemoteViewsFactory, position, false);
                            }
                        }
                        adapter.mMainHandler.sendEmptyMessage(1);
                        adapter.mMainHandler.sendEmptyMessage(2);
                    }
                }
            } else if (i != 3) {
                if (i == 4) {
                    unbindNow();
                }
            } else if (adapter != null && this.mRemoteViewsFactory != null) {
                removeMessages(4);
                i = adapter.mCache.getNextIndexToLoad();
                if (i > -1) {
                    adapter.updateRemoteViews(this.mRemoteViewsFactory, i, true);
                    sendEmptyMessage(3);
                } else {
                    enqueueDeferredUnbindServiceMessage();
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void unbindNow() {
            if (this.mBindRequested) {
                this.mBindRequested = false;
                this.mContext.unbindService(this);
            }
            this.mRemoteViewsFactory = null;
        }

        /* JADX WARNING: Missing block: B:3:0x0008, code skipped:
            if (r3.mRemoteViewsFactory.isCreated() == false) goto L_0x000a;
     */
        private boolean sendNotifyDataSetChange(boolean r4) {
            /*
            r3 = this;
            if (r4 != 0) goto L_0x000a;
        L_0x0002:
            r0 = r3.mRemoteViewsFactory;	 Catch:{ RemoteException | RuntimeException -> 0x0011, RemoteException | RuntimeException -> 0x0011 }
            r0 = r0.isCreated();	 Catch:{ RemoteException | RuntimeException -> 0x0011, RemoteException | RuntimeException -> 0x0011 }
            if (r0 != 0) goto L_0x000f;
        L_0x000a:
            r0 = r3.mRemoteViewsFactory;	 Catch:{ RemoteException | RuntimeException -> 0x0011, RemoteException | RuntimeException -> 0x0011 }
            r0.onDataSetChanged();	 Catch:{ RemoteException | RuntimeException -> 0x0011, RemoteException | RuntimeException -> 0x0011 }
        L_0x000f:
            r0 = 1;
            return r0;
        L_0x0011:
            r0 = move-exception;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Error in updateNotifyDataSetChanged(): ";
            r1.append(r2);
            r2 = r0.getMessage();
            r1.append(r2);
            r1 = r1.toString();
            r2 = "RemoteViewsAdapter";
            android.util.Log.e(r2, r1);
            r1 = 0;
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.RemoteViewsAdapter$RemoteServiceHandler.sendNotifyDataSetChange(boolean):boolean");
        }

        private void enqueueDeferredUnbindServiceMessage() {
            removeMessages(4);
            sendEmptyMessageDelayed(4, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
        }
    }

    static class RemoteViewsCacheKey {
        final FilterComparison filter;
        final int widgetId;

        RemoteViewsCacheKey(FilterComparison filter, int widgetId) {
            this.filter = filter;
            this.widgetId = widgetId;
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof RemoteViewsCacheKey)) {
                return false;
            }
            RemoteViewsCacheKey other = (RemoteViewsCacheKey) o;
            if (other.filter.equals(this.filter) && other.widgetId == this.widgetId) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            FilterComparison filterComparison = this.filter;
            return (filterComparison == null ? 0 : filterComparison.hashCode()) ^ (this.widgetId << 2);
        }
    }

    static class RemoteViewsFrameLayout extends AppWidgetHostView {
        public int cacheIndex = -1;
        private final FixedSizeRemoteViewsCache mCache;

        public RemoteViewsFrameLayout(Context context, FixedSizeRemoteViewsCache cache) {
            super(context);
            this.mCache = cache;
        }

        public void onRemoteViewsLoaded(RemoteViews view, OnClickHandler handler, boolean forceApplyAsync) {
            setOnClickHandler(handler);
            boolean z = forceApplyAsync || (view != null && view.prefersAsyncApply());
            applyRemoteViews(view, z);
        }

        /* Access modifiers changed, original: protected */
        public View getDefaultView() {
            TextView loadingTextView = (TextView) LayoutInflater.from(getContext()).inflate((int) R.layout.remote_views_adapter_default_loading_view, (ViewGroup) this, false);
            loadingTextView.setHeight(this.mCache.getMetaData().getLoadingTemplate(getContext()).defaultHeight);
            return loadingTextView;
        }

        /* Access modifiers changed, original: protected */
        public Context getRemoteContext() {
            return null;
        }

        /* Access modifiers changed, original: protected */
        public View getErrorView() {
            return getDefaultView();
        }
    }

    private class RemoteViewsFrameLayoutRefSet extends SparseArray<LinkedList<RemoteViewsFrameLayout>> {
        private RemoteViewsFrameLayoutRefSet() {
        }

        public void add(int position, RemoteViewsFrameLayout layout) {
            LinkedList<RemoteViewsFrameLayout> refs = (LinkedList) get(position);
            if (refs == null) {
                refs = new LinkedList();
                put(position, refs);
            }
            layout.cacheIndex = position;
            refs.add(layout);
        }

        public void notifyOnRemoteViewsLoaded(int position, RemoteViews view) {
            if (view != null) {
                LinkedList<RemoteViewsFrameLayout> refs = (LinkedList) removeReturnOld(position);
                if (refs != null) {
                    Iterator it = refs.iterator();
                    while (it.hasNext()) {
                        ((RemoteViewsFrameLayout) it.next()).onRemoteViewsLoaded(view, RemoteViewsAdapter.this.mRemoteViewsOnClickHandler, true);
                    }
                }
            }
        }

        public void removeView(RemoteViewsFrameLayout rvfl) {
            if (rvfl.cacheIndex >= 0) {
                LinkedList<RemoteViewsFrameLayout> refs = (LinkedList) get(rvfl.cacheIndex);
                if (refs != null) {
                    refs.remove(rvfl);
                }
                rvfl.cacheIndex = -1;
            }
        }
    }

    private static class RemoteViewsIndexMetaData {
        long itemId;
        int typeId;

        public RemoteViewsIndexMetaData(RemoteViews v, long itemId) {
            set(v, itemId);
        }

        public void set(RemoteViews v, long id) {
            this.itemId = id;
            if (v != null) {
                this.typeId = v.getLayoutId();
            } else {
                this.typeId = 0;
            }
        }
    }

    private static class RemoteViewsMetaData {
        int count;
        boolean hasStableIds;
        LoadingViewTemplate loadingTemplate;
        private final SparseIntArray mTypeIdIndexMap = new SparseIntArray();
        int viewTypeCount;

        public RemoteViewsMetaData() {
            reset();
        }

        public void set(RemoteViewsMetaData d) {
            synchronized (d) {
                this.count = d.count;
                this.viewTypeCount = d.viewTypeCount;
                this.hasStableIds = d.hasStableIds;
                this.loadingTemplate = d.loadingTemplate;
            }
        }

        public void reset() {
            this.count = 0;
            this.viewTypeCount = 1;
            this.hasStableIds = true;
            this.loadingTemplate = null;
            this.mTypeIdIndexMap.clear();
        }

        public int getMappedViewType(int typeId) {
            int mappedTypeId = this.mTypeIdIndexMap.get(typeId, -1);
            if (mappedTypeId != -1) {
                return mappedTypeId;
            }
            mappedTypeId = this.mTypeIdIndexMap.size() + 1;
            this.mTypeIdIndexMap.put(typeId, mappedTypeId);
            return mappedTypeId;
        }

        public boolean isViewTypeInRange(int typeId) {
            return getMappedViewType(typeId) < this.viewTypeCount;
        }

        public synchronized LoadingViewTemplate getLoadingTemplate(Context context) {
            if (this.loadingTemplate == null) {
                this.loadingTemplate = new LoadingViewTemplate(null, context);
            }
            return this.loadingTemplate;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ed  */
    public RemoteViewsAdapter(android.content.Context r7, android.content.Intent r8, android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback r9, boolean r10) {
        /*
        r6 = this;
        r6.<init>();
        r0 = 0;
        r6.mDataReady = r0;
        r6.mContext = r7;
        r6.mIntent = r8;
        r1 = r6.mIntent;
        if (r1 == 0) goto L_0x00f5;
    L_0x000e:
        r1 = -1;
        r2 = "remoteAdapterAppWidgetId";
        r1 = r8.getIntExtra(r2, r1);
        r6.mAppWidgetId = r1;
        r1 = new android.widget.RemoteViewsAdapter$RemoteViewsFrameLayoutRefSet;
        r2 = 0;
        r1.<init>();
        r6.mRequestedViews = r1;
        r1 = "remoteAdapterOnLightBackground";
        r0 = r8.getBooleanExtra(r1, r0);
        r6.mOnLightBackground = r0;
        r0 = "remoteAdapterAppWidgetId";
        r8.removeExtra(r0);
        r0 = "remoteAdapterOnLightBackground";
        r8.removeExtra(r0);
        r0 = new android.os.HandlerThread;
        r1 = "RemoteViewsCache-loader";
        r0.<init>(r1);
        r6.mWorkerThread = r0;
        r0 = r6.mWorkerThread;
        r0.start();
        r0 = new android.os.Handler;
        r1 = android.os.Looper.myLooper();
        r0.<init>(r1, r6);
        r6.mMainHandler = r0;
        r0 = new android.widget.RemoteViewsAdapter$RemoteServiceHandler;
        r1 = r6.mWorkerThread;
        r1 = r1.getLooper();
        r3 = r7.getApplicationContext();
        r0.<init>(r1, r6, r3);
        r6.mServiceHandler = r0;
        if (r10 == 0) goto L_0x0068;
    L_0x0061:
        r2 = new android.widget.RemoteViewsAdapter$HandlerThreadExecutor;
        r0 = r6.mWorkerThread;
        r2.<init>(r0);
    L_0x0068:
        r6.mAsyncViewLoadExecutor = r2;
        r6.mCallback = r9;
        r0 = sCacheRemovalThread;
        if (r0 != 0) goto L_0x008b;
    L_0x0070:
        r0 = new android.os.HandlerThread;
        r1 = "RemoteViewsAdapter-cachePruner";
        r0.<init>(r1);
        sCacheRemovalThread = r0;
        r0 = sCacheRemovalThread;
        r0.start();
        r0 = new android.os.Handler;
        r1 = sCacheRemovalThread;
        r1 = r1.getLooper();
        r0.<init>(r1);
        sCacheRemovalQueue = r0;
    L_0x008b:
        r0 = new android.widget.RemoteViewsAdapter$RemoteViewsCacheKey;
        r1 = new android.content.Intent$FilterComparison;
        r2 = r6.mIntent;
        r1.<init>(r2);
        r2 = r6.mAppWidgetId;
        r0.<init>(r1, r2);
        r1 = sCachedRemoteViewsCaches;
        monitor-enter(r1);
        r2 = sCachedRemoteViewsCaches;	 Catch:{ all -> 0x00f2 }
        r2 = r2.get(r0);	 Catch:{ all -> 0x00f2 }
        r2 = (android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache) r2;	 Catch:{ all -> 0x00f2 }
        r3 = r7.getResources();	 Catch:{ all -> 0x00f2 }
        r3 = r3.getConfiguration();	 Catch:{ all -> 0x00f2 }
        if (r2 == 0) goto L_0x00e0;
    L_0x00ae:
        r4 = r2.mConfiguration;	 Catch:{ all -> 0x00f2 }
        r4 = r4.diff(r3);	 Catch:{ all -> 0x00f2 }
        r5 = -1073737216; // 0xffffffffc0001200 float:-2.0010986 double:NaN;
        r4 = r4 & r5;
        if (r4 == 0) goto L_0x00bd;
    L_0x00bc:
        goto L_0x00e0;
    L_0x00bd:
        r4 = sCachedRemoteViewsCaches;	 Catch:{ all -> 0x00f2 }
        r4 = r4.get(r0);	 Catch:{ all -> 0x00f2 }
        r4 = (android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache) r4;	 Catch:{ all -> 0x00f2 }
        r6.mCache = r4;	 Catch:{ all -> 0x00f2 }
        r4 = r6.mCache;	 Catch:{ all -> 0x00f2 }
        r4 = r4.mMetaData;	 Catch:{ all -> 0x00f2 }
        monitor-enter(r4);	 Catch:{ all -> 0x00f2 }
        r5 = r6.mCache;	 Catch:{ all -> 0x00dd }
        r5 = r5.mMetaData;	 Catch:{ all -> 0x00dd }
        r5 = r5.count;	 Catch:{ all -> 0x00dd }
        if (r5 <= 0) goto L_0x00db;
    L_0x00d8:
        r5 = 1;
        r6.mDataReady = r5;	 Catch:{ all -> 0x00dd }
    L_0x00db:
        monitor-exit(r4);	 Catch:{ all -> 0x00dd }
        goto L_0x00e9;
    L_0x00dd:
        r5 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x00dd }
        throw r5;	 Catch:{ all -> 0x00f2 }
    L_0x00e0:
        r4 = new android.widget.RemoteViewsAdapter$FixedSizeRemoteViewsCache;	 Catch:{ all -> 0x00f2 }
        r5 = 40;
        r4.<init>(r5, r3);	 Catch:{ all -> 0x00f2 }
        r6.mCache = r4;	 Catch:{ all -> 0x00f2 }
    L_0x00e9:
        r4 = r6.mDataReady;	 Catch:{ all -> 0x00f2 }
        if (r4 != 0) goto L_0x00f0;
    L_0x00ed:
        r6.requestBindService();	 Catch:{ all -> 0x00f2 }
    L_0x00f0:
        monitor-exit(r1);	 Catch:{ all -> 0x00f2 }
        return;
    L_0x00f2:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x00f2 }
        throw r2;
    L_0x00f5:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Non-null Intent must be specified.";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.RemoteViewsAdapter.<init>(android.content.Context, android.content.Intent, android.widget.RemoteViewsAdapter$RemoteAdapterConnectionCallback, boolean):void");
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            this.mServiceHandler.unbindNow();
            this.mWorkerThread.quit();
        } finally {
            super.finalize();
        }
    }

    @UnsupportedAppUsage
    public boolean isDataReady() {
        return this.mDataReady;
    }

    @UnsupportedAppUsage
    public void setRemoteViewsOnClickHandler(OnClickHandler handler) {
        this.mRemoteViewsOnClickHandler = handler;
    }

    @UnsupportedAppUsage
    public void saveRemoteViewsCache() {
        RemoteViewsCacheKey key = new RemoteViewsCacheKey(new FilterComparison(this.mIntent), this.mAppWidgetId);
        synchronized (sCachedRemoteViewsCaches) {
            int metaDataCount;
            if (sRemoteViewsCacheRemoveRunnables.containsKey(key)) {
                sCacheRemovalQueue.removeCallbacks((Runnable) sRemoteViewsCacheRemoveRunnables.get(key));
                sRemoteViewsCacheRemoveRunnables.remove(key);
            }
            synchronized (this.mCache.mMetaData) {
                metaDataCount = this.mCache.mMetaData.count;
            }
            synchronized (this.mCache) {
                int numRemoteViewsCached = this.mCache.mIndexRemoteViews.size();
            }
            if (metaDataCount > 0 && numRemoteViewsCached > 0) {
                sCachedRemoteViewsCaches.put(key, this.mCache);
            }
            Runnable r = new -$$Lambda$RemoteViewsAdapter$-xHEGE7CkOWJ8u7GAjsH_hc-iiA(key);
            sRemoteViewsCacheRemoveRunnables.put(key, r);
            sCacheRemovalQueue.postDelayed(r, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
        }
    }

    static /* synthetic */ void lambda$saveRemoteViewsCache$0(RemoteViewsCacheKey key) {
        synchronized (sCachedRemoteViewsCaches) {
            if (sCachedRemoteViewsCaches.containsKey(key)) {
                sCachedRemoteViewsCaches.remove(key);
            }
            if (sRemoteViewsCacheRemoveRunnables.containsKey(key)) {
                sRemoteViewsCacheRemoveRunnables.remove(key);
            }
        }
    }

    private void updateTemporaryMetaData(IRemoteViewsFactory factory) {
        try {
            boolean hasStableIds = factory.hasStableIds();
            int viewTypeCount = factory.getViewTypeCount();
            int count = factory.getCount();
            LoadingViewTemplate loadingTemplate = new LoadingViewTemplate(factory.getLoadingView(), this.mContext);
            if (count > 0 && loadingTemplate.remoteViews == null) {
                RemoteViews firstView = factory.getViewAt(null);
                if (firstView != null) {
                    loadingTemplate.loadFirstViewHeight(firstView, this.mContext, new HandlerThreadExecutor(this.mWorkerThread));
                }
            }
            RemoteViewsMetaData tmpMetaData = this.mCache.getTemporaryMetaData();
            synchronized (tmpMetaData) {
                tmpMetaData.hasStableIds = hasStableIds;
                tmpMetaData.viewTypeCount = viewTypeCount + 1;
                tmpMetaData.count = count;
                tmpMetaData.loadingTemplate = loadingTemplate;
            }
        } catch (RemoteException | RuntimeException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error in updateMetaData: ");
            stringBuilder.append(e.getMessage());
            Log.e(TAG, stringBuilder.toString());
            synchronized (this.mCache.getMetaData()) {
                this.mCache.getMetaData().reset();
                synchronized (this.mCache) {
                    this.mCache.reset();
                    this.mMainHandler.sendEmptyMessage(2);
                }
            }
        }
    }

    private void updateRemoteViews(IRemoteViewsFactory factory, int position, boolean notifyWhenLoaded) {
        try {
            RemoteViews remoteViews = factory.getViewAt(position);
            long itemId = factory.getItemId(position);
            if (remoteViews != null) {
                boolean viewTypeInRange;
                int cacheCount;
                if (remoteViews.mApplication != null) {
                    ApplicationInfo applicationInfo = this.mLastRemoteViewAppInfo;
                    if (applicationInfo == null || !remoteViews.hasSameAppInfo(applicationInfo)) {
                        this.mLastRemoteViewAppInfo = remoteViews.mApplication;
                    } else {
                        remoteViews.mApplication = this.mLastRemoteViewAppInfo;
                    }
                }
                int layoutId = remoteViews.getLayoutId();
                RemoteViewsMetaData metaData = this.mCache.getMetaData();
                synchronized (metaData) {
                    viewTypeInRange = metaData.isViewTypeInRange(layoutId);
                    cacheCount = this.mCache.mMetaData.count;
                }
                synchronized (this.mCache) {
                    if (viewTypeInRange) {
                        this.mCache.insert(position, remoteViews, itemId, getVisibleWindow(cacheCount));
                        if (notifyWhenLoaded) {
                            Message.obtain(this.mMainHandler, 5, position, 0, remoteViews).sendToTarget();
                        }
                    } else {
                        Log.e(TAG, "Error: widget's RemoteViewsFactory returns more view types than  indicated by getViewTypeCount() ");
                    }
                }
                return;
            }
            throw new RuntimeException("Null remoteViews");
        } catch (RemoteException | RuntimeException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error in updateRemoteViews(");
            stringBuilder.append(position);
            stringBuilder.append("): ");
            stringBuilder.append(e.getMessage());
            Log.e(TAG, stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public Intent getRemoteViewsServiceIntent() {
        return this.mIntent;
    }

    public int getCount() {
        int i;
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            i = metaData.count;
        }
        return i;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        synchronized (this.mCache) {
            if (this.mCache.containsMetaDataAt(position)) {
                long j = this.mCache.getMetaDataAt(position).itemId;
                return j;
            }
            return 0;
        }
    }

    public int getItemViewType(int position) {
        synchronized (this.mCache) {
            if (this.mCache.containsMetaDataAt(position)) {
                int mappedViewType;
                int typeId = this.mCache.getMetaDataAt(position).typeId;
                RemoteViewsMetaData metaData = this.mCache.getMetaData();
                synchronized (metaData) {
                    mappedViewType = metaData.getMappedViewType(typeId);
                }
                return mappedViewType;
            }
            return 0;
        }
    }

    @UnsupportedAppUsage
    public void setVisibleRangeHint(int lowerBound, int upperBound) {
        this.mVisibleWindowLowerBound = lowerBound;
        this.mVisibleWindowUpperBound = upperBound;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RemoteViewsFrameLayout layout;
        synchronized (this.mCache) {
            RemoteViews rv = this.mCache.getRemoteViewsAt(position);
            boolean isInCache = rv != null;
            boolean hasNewItems = false;
            if (convertView != null && (convertView instanceof RemoteViewsFrameLayout)) {
                this.mRequestedViews.removeView((RemoteViewsFrameLayout) convertView);
            }
            if (isInCache) {
                hasNewItems = this.mCache.queuePositionsToBePreloadedFromRequestedPosition(position);
            } else {
                requestBindService();
            }
            if (convertView instanceof RemoteViewsFrameLayout) {
                layout = (RemoteViewsFrameLayout) convertView;
            } else {
                layout = new RemoteViewsFrameLayout(parent.getContext(), this.mCache);
                layout.setExecutor(this.mAsyncViewLoadExecutor);
                layout.setOnLightBackground(this.mOnLightBackground);
            }
            if (isInCache) {
                layout.onRemoteViewsLoaded(rv, this.mRemoteViewsOnClickHandler, false);
                if (hasNewItems) {
                    this.mServiceHandler.sendEmptyMessage(3);
                }
            } else {
                layout.onRemoteViewsLoaded(this.mCache.getMetaData().getLoadingTemplate(this.mContext).remoteViews, this.mRemoteViewsOnClickHandler, false);
                this.mRequestedViews.add(position, layout);
                this.mCache.queueRequestedPositionToLoad(position);
                this.mServiceHandler.sendEmptyMessage(3);
            }
        }
        return layout;
    }

    public int getViewTypeCount() {
        int i;
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            i = metaData.viewTypeCount;
        }
        return i;
    }

    public boolean hasStableIds() {
        boolean z;
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            z = metaData.hasStableIds;
        }
        return z;
    }

    public boolean isEmpty() {
        return getCount() <= 0;
    }

    private int[] getVisibleWindow(int count) {
        int lower = this.mVisibleWindowLowerBound;
        int upper = this.mVisibleWindowUpperBound;
        if ((lower == 0 && upper == 0) || lower < 0 || upper < 0) {
            return new int[0];
        }
        int[] window;
        int i;
        int j;
        if (lower <= upper) {
            window = new int[((upper + 1) - lower)];
            i = lower;
            j = 0;
            while (i <= upper) {
                window[j] = i;
                i++;
                j++;
            }
        } else {
            count = Math.max(count, lower);
            window = new int[(((count - lower) + upper) + 1)];
            i = 0;
            j = 0;
            while (j <= upper) {
                window[i] = j;
                j++;
                i++;
            }
            j = lower;
            while (j < count) {
                window[i] = j;
                j++;
                i++;
            }
        }
        return window;
    }

    public void notifyDataSetChanged() {
        this.mServiceHandler.removeMessages(4);
        this.mServiceHandler.sendEmptyMessage(2);
    }

    /* Access modifiers changed, original: 0000 */
    public void superNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public boolean handleMessage(Message msg) {
        int i = msg.what;
        RemoteAdapterConnectionCallback remoteAdapterConnectionCallback;
        if (i == 1) {
            this.mCache.commitTemporaryMetaData();
            return true;
        } else if (i == 2) {
            superNotifyDataSetChanged();
            return true;
        } else if (i == 3) {
            remoteAdapterConnectionCallback = this.mCallback;
            if (remoteAdapterConnectionCallback != null) {
                remoteAdapterConnectionCallback.onRemoteAdapterConnected();
            }
            return true;
        } else if (i == 4) {
            remoteAdapterConnectionCallback = this.mCallback;
            if (remoteAdapterConnectionCallback != null) {
                remoteAdapterConnectionCallback.onRemoteAdapterDisconnected();
            }
            return true;
        } else if (i != 5) {
            return false;
        } else {
            this.mRequestedViews.notifyOnRemoteViewsLoaded(msg.arg1, (RemoteViews) msg.obj);
            return true;
        }
    }

    private void requestBindService() {
        this.mServiceHandler.removeMessages(4);
        Message.obtain(this.mServiceHandler, 1, this.mAppWidgetId, 0, this.mIntent).sendToTarget();
    }
}
