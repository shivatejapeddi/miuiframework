package com.android.internal.app;

import android.app.prediction.AppPredictor;
import android.app.prediction.AppTarget;
import android.app.prediction.AppTarget.Builder;
import android.app.prediction.AppTargetEvent;
import android.app.prediction.AppTargetId;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.app.ResolverActivity.ResolvedComponentInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

class AppPredictionServiceResolverComparator extends AbstractResolverComparator {
    private static final boolean DEBUG = false;
    private static final String TAG = "APSResolverComparator";
    private final AppPredictor mAppPredictor;
    private final Context mContext;
    private final Intent mIntent;
    private final String mReferrerPackage;
    private ResolverRankerServiceResolverComparator mResolverRankerService;
    private final Map<ComponentName, Integer> mTargetRanks = new HashMap();
    private final UserHandle mUser;

    AppPredictionServiceResolverComparator(Context context, Intent intent, String referrerPackage, AppPredictor appPredictor, UserHandle user) {
        super(context, intent);
        this.mContext = context;
        this.mIntent = intent;
        this.mAppPredictor = appPredictor;
        this.mUser = user;
        this.mReferrerPackage = referrerPackage;
    }

    /* Access modifiers changed, original: 0000 */
    public int compare(ResolveInfo lhs, ResolveInfo rhs) {
        ResolverRankerServiceResolverComparator resolverRankerServiceResolverComparator = this.mResolverRankerService;
        if (resolverRankerServiceResolverComparator != null) {
            return resolverRankerServiceResolverComparator.compare(lhs, rhs);
        }
        Integer lhsRank = (Integer) this.mTargetRanks.get(new ComponentName(lhs.activityInfo.packageName, lhs.activityInfo.name));
        Integer rhsRank = (Integer) this.mTargetRanks.get(new ComponentName(rhs.activityInfo.packageName, rhs.activityInfo.name));
        if (lhsRank == null && rhsRank == null) {
            return 0;
        }
        if (lhsRank == null) {
            return -1;
        }
        if (rhsRank == null) {
            return 1;
        }
        return lhsRank.intValue() - rhsRank.intValue();
    }

    /* Access modifiers changed, original: 0000 */
    public void doCompute(List<ResolvedComponentInfo> targets) {
        if (targets.isEmpty()) {
            this.mHandler.sendEmptyMessage(0);
            return;
        }
        List<AppTarget> appTargets = new ArrayList();
        for (ResolvedComponentInfo target : targets) {
            appTargets.add(new Builder(new AppTargetId(target.name.flattenToString()), target.name.getPackageName(), this.mUser).setClassName(target.name.getClassName()).build());
        }
        this.mAppPredictor.sortTargets(appTargets, Executors.newSingleThreadExecutor(), new -$$Lambda$AppPredictionServiceResolverComparator$PQ-i16vesHTtkDyBgU_HkS0uF1A(this, targets));
    }

    public /* synthetic */ void lambda$doCompute$1$AppPredictionServiceResolverComparator(List targets, List sortedAppTargets) {
        if (sortedAppTargets.isEmpty()) {
            this.mResolverRankerService = new ResolverRankerServiceResolverComparator(this.mContext, this.mIntent, this.mReferrerPackage, new -$$Lambda$AppPredictionServiceResolverComparator$25gj8kU_BfxuxUXCZ0QzLVhZs9Y(this));
            this.mResolverRankerService.compute(targets);
            return;
        }
        Message.obtain(this.mHandler, 0, sortedAppTargets).sendToTarget();
    }

    public /* synthetic */ void lambda$doCompute$0$AppPredictionServiceResolverComparator() {
        this.mHandler.sendEmptyMessage(0);
    }

    /* Access modifiers changed, original: 0000 */
    public void handleResultMessage(Message msg) {
        if (msg.what == 0 && msg.obj != null) {
            List<AppTarget> sortedAppTargets = msg.obj;
            for (int i = 0; i < sortedAppTargets.size(); i++) {
                this.mTargetRanks.put(new ComponentName(((AppTarget) sortedAppTargets.get(i)).getPackageName(), ((AppTarget) sortedAppTargets.get(i)).getClassName()), Integer.valueOf(i));
            }
        } else if (msg.obj == null && this.mResolverRankerService == null) {
            Log.e(TAG, "Unexpected null result");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float getScore(ComponentName name) {
        ResolverRankerServiceResolverComparator resolverRankerServiceResolverComparator = this.mResolverRankerService;
        if (resolverRankerServiceResolverComparator != null) {
            return resolverRankerServiceResolverComparator.getScore(name);
        }
        Integer rank = (Integer) this.mTargetRanks.get(name);
        if (rank == null) {
            Log.w(TAG, "Score requested for unknown component.");
            return 0.0f;
        }
        return 1.0f - (((float) rank.intValue()) / ((float) (((this.mTargetRanks.size() - 1) * this.mTargetRanks.size()) / 2)));
    }

    /* Access modifiers changed, original: 0000 */
    public void updateModel(ComponentName componentName) {
        ResolverRankerServiceResolverComparator resolverRankerServiceResolverComparator = this.mResolverRankerService;
        if (resolverRankerServiceResolverComparator != null) {
            resolverRankerServiceResolverComparator.updateModel(componentName);
        } else {
            this.mAppPredictor.notifyAppTargetEvent(new AppTargetEvent.Builder(new Builder(new AppTargetId(componentName.toString()), componentName.getPackageName(), this.mUser).setClassName(componentName.getClassName()).build(), 1).build());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        ResolverRankerServiceResolverComparator resolverRankerServiceResolverComparator = this.mResolverRankerService;
        if (resolverRankerServiceResolverComparator != null) {
            resolverRankerServiceResolverComparator.destroy();
            this.mResolverRankerService = null;
        }
    }
}
