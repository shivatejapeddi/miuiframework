package com.android.internal.app;

import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.app.ResolverActivity.ResolvedComponentInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

abstract class AbstractResolverComparator implements Comparator<ResolvedComponentInfo> {
    private static final boolean DEBUG = false;
    private static final int NUM_OF_TOP_ANNOTATIONS_TO_USE = 3;
    static final int RANKER_RESULT_TIMEOUT = 1;
    static final int RANKER_SERVICE_RESULT = 0;
    private static final String TAG = "AbstractResolverComp";
    private static final int WATCHDOG_TIMEOUT_MILLIS = 500;
    protected AfterCompute mAfterCompute;
    protected String[] mAnnotations;
    protected String mContentType;
    private final String mDefaultBrowserPackageName;
    protected final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 0) {
                if (i != 1) {
                    super.handleMessage(msg);
                    return;
                }
                AbstractResolverComparator.this.mHandler.removeMessages(0);
                AbstractResolverComparator.this.afterCompute();
            } else if (AbstractResolverComparator.this.mHandler.hasMessages(1)) {
                AbstractResolverComparator.this.handleResultMessage(msg);
                AbstractResolverComparator.this.mHandler.removeMessages(1);
                AbstractResolverComparator.this.afterCompute();
            }
        }
    };
    private final boolean mHttp;
    protected final PackageManager mPm;
    protected final UsageStatsManager mUsm;

    interface AfterCompute {
        void afterCompute();
    }

    public abstract int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2);

    public abstract void doCompute(List<ResolvedComponentInfo> list);

    public abstract float getScore(ComponentName componentName);

    public abstract void handleResultMessage(Message message);

    AbstractResolverComparator(Context context, Intent intent) {
        String defaultBrowserPackageNameAsUser;
        String scheme = intent.getScheme();
        boolean z = IntentFilter.SCHEME_HTTP.equals(scheme) || IntentFilter.SCHEME_HTTPS.equals(scheme);
        this.mHttp = z;
        this.mContentType = intent.getType();
        getContentAnnotations(intent);
        this.mPm = context.getPackageManager();
        this.mUsm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (this.mHttp) {
            defaultBrowserPackageNameAsUser = this.mPm.getDefaultBrowserPackageNameAsUser(UserHandle.myUserId());
        } else {
            defaultBrowserPackageNameAsUser = null;
        }
        this.mDefaultBrowserPackageName = defaultBrowserPackageNameAsUser;
    }

    private void getContentAnnotations(Intent intent) {
        ArrayList<String> annotations = intent.getStringArrayListExtra(Intent.EXTRA_CONTENT_ANNOTATIONS);
        if (annotations != null) {
            int size = annotations.size();
            if (size > 3) {
                size = 3;
            }
            this.mAnnotations = new String[size];
            for (int i = 0; i < size; i++) {
                this.mAnnotations[i] = (String) annotations.get(i);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setCallBack(AfterCompute afterCompute) {
        this.mAfterCompute = afterCompute;
    }

    /* Access modifiers changed, original: protected|final */
    public final void afterCompute() {
        AfterCompute afterCompute = this.mAfterCompute;
        if (afterCompute != null) {
            afterCompute.afterCompute();
        }
    }

    public final int compare(ResolvedComponentInfo lhsp, ResolvedComponentInfo rhsp) {
        int i = 0;
        ResolveInfo lhs = lhsp.getResolveInfoAt(0);
        ResolveInfo rhs = rhsp.getResolveInfoAt(0);
        if (lhs.targetUserId != -2) {
            if (rhs.targetUserId == -2) {
                i = 1;
            }
            return i;
        }
        int i2 = -1;
        if (rhs.targetUserId != -2) {
            return -1;
        }
        if (this.mHttp) {
            if (isDefaultBrowser(lhs)) {
                return -1;
            }
            if (isDefaultBrowser(rhs)) {
                return 1;
            }
            boolean lhsSpecific = ResolverActivity.isSpecificUriMatch(lhs.match);
            if (lhsSpecific != ResolverActivity.isSpecificUriMatch(rhs.match)) {
                if (!lhsSpecific) {
                    i2 = 1;
                }
                return i2;
            }
        }
        return compare(lhs, rhs);
    }

    /* Access modifiers changed, original: final */
    public final void compute(List<ResolvedComponentInfo> targets) {
        beforeCompute();
        doCompute(targets);
    }

    /* Access modifiers changed, original: final */
    public final void updateChooserCounts(String packageName, int userId, String action) {
        UsageStatsManager usageStatsManager = this.mUsm;
        if (usageStatsManager != null) {
            usageStatsManager.reportChooserSelection(packageName, userId, this.mContentType, this.mAnnotations, action);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateModel(ComponentName componentName) {
    }

    /* Access modifiers changed, original: 0000 */
    public void beforeCompute() {
        Handler handler = this.mHandler;
        if (handler == null) {
            Log.d(TAG, "Error: Handler is Null; Needs to be initialized.");
        } else {
            handler.sendEmptyMessageDelayed(1, 500);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        afterCompute();
    }

    private boolean isDefaultBrowser(ResolveInfo ri) {
        if (ri.targetUserId == -2 && ri.activityInfo.packageName != null && ri.activityInfo.packageName.equals(this.mDefaultBrowserPackageName)) {
            return true;
        }
        return false;
    }
}
