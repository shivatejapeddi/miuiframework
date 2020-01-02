package com.android.internal.app;

import android.app.usage.UsageStats;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.metrics.LogMaker;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.service.resolver.IResolverRankerResult;
import android.service.resolver.IResolverRankerResult.Stub;
import android.service.resolver.IResolverRankerService;
import android.service.resolver.ResolverRankerService;
import android.service.resolver.ResolverTarget;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.app.ResolverActivity.ResolvedComponentInfo;
import com.android.internal.logging.MetricsLogger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ResolverRankerServiceResolverComparator extends AbstractResolverComparator {
    private static final int CONNECTION_COST_TIMEOUT_MILLIS = 200;
    private static final boolean DEBUG = false;
    private static final float RECENCY_MULTIPLIER = 2.0f;
    private static final long RECENCY_TIME_PERIOD = 43200000;
    private static final String TAG = "RRSResolverComparator";
    private static final long USAGE_STATS_PERIOD = 604800000;
    private String mAction;
    private final Collator mCollator;
    private CountDownLatch mConnectSignal;
    private ResolverRankerServiceConnection mConnection;
    private Context mContext;
    private final long mCurrentTime;
    private final Object mLock = new Object();
    private IResolverRankerService mRanker;
    private ComponentName mRankerServiceName;
    private final String mReferrerPackage;
    private ComponentName mResolvedRankerName;
    private final long mSinceTime;
    private final Map<String, UsageStats> mStats;
    private ArrayList<ResolverTarget> mTargets;
    private final LinkedHashMap<ComponentName, ResolverTarget> mTargetsDict = new LinkedHashMap();

    private class ResolverRankerServiceConnection implements ServiceConnection {
        private final CountDownLatch mConnectSignal;
        public final IResolverRankerResult resolverRankerResult = new Stub() {
            public void sendResult(List<ResolverTarget> targets) throws RemoteException {
                synchronized (ResolverRankerServiceResolverComparator.this.mLock) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = targets;
                    ResolverRankerServiceResolverComparator.this.mHandler.sendMessage(msg);
                }
            }
        };

        public ResolverRankerServiceConnection(CountDownLatch connectSignal) {
            this.mConnectSignal = connectSignal;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (ResolverRankerServiceResolverComparator.this.mLock) {
                ResolverRankerServiceResolverComparator.this.mRanker = IResolverRankerService.Stub.asInterface(service);
                this.mConnectSignal.countDown();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            synchronized (ResolverRankerServiceResolverComparator.this.mLock) {
                destroy();
            }
        }

        public void destroy() {
            synchronized (ResolverRankerServiceResolverComparator.this.mLock) {
                ResolverRankerServiceResolverComparator.this.mRanker = null;
            }
        }
    }

    public ResolverRankerServiceResolverComparator(Context context, Intent intent, String referrerPackage, AfterCompute afterCompute) {
        super(context, intent);
        this.mCollator = Collator.getInstance(context.getResources().getConfiguration().locale);
        this.mReferrerPackage = referrerPackage;
        this.mContext = context;
        this.mCurrentTime = System.currentTimeMillis();
        this.mSinceTime = this.mCurrentTime - 604800000;
        this.mStats = this.mUsm.queryAndAggregateUsageStats(this.mSinceTime, this.mCurrentTime);
        this.mAction = intent.getAction();
        this.mRankerServiceName = new ComponentName(this.mContext, getClass());
        setCallBack(afterCompute);
    }

    public void handleResultMessage(Message msg) {
        if (msg.what == 0) {
            Object obj = msg.obj;
            String str = TAG;
            if (obj == null) {
                Log.e(str, "Receiving null prediction results.");
                return;
            }
            List<ResolverTarget> receivedTargets = msg.obj;
            if (receivedTargets == null || this.mTargets == null || receivedTargets.size() != this.mTargets.size()) {
                Log.e(str, "Sizes of sent and received ResolverTargets diff.");
            } else {
                int size = this.mTargets.size();
                boolean isUpdated = false;
                for (int i = 0; i < size; i++) {
                    float predictedProb = ((ResolverTarget) receivedTargets.get(i)).getSelectProbability();
                    if (predictedProb != ((ResolverTarget) this.mTargets.get(i)).getSelectProbability()) {
                        ((ResolverTarget) this.mTargets.get(i)).setSelectProbability(predictedProb);
                        isUpdated = true;
                    }
                }
                if (isUpdated) {
                    this.mRankerServiceName = this.mResolvedRankerName;
                }
            }
        }
    }

    public void doCompute(List<ResolvedComponentInfo> targets) {
        long recentSinceTime = this.mCurrentTime - 43200000;
        Iterator it = targets.iterator();
        float mostRecencyScore = 1.0f;
        float mostTimeSpentScore = 1.0f;
        float mostLaunchScore = 1.0f;
        float mostChooserScore = 1.0f;
        while (it.hasNext()) {
            Iterator it2;
            ResolvedComponentInfo target = (ResolvedComponentInfo) it.next();
            ResolverTarget resolverTarget = new ResolverTarget();
            this.mTargetsDict.put(target.name, resolverTarget);
            UsageStats pkStats = (UsageStats) this.mStats.get(target.name.getPackageName());
            UsageStats usageStats;
            if (pkStats != null) {
                float recencyScore;
                if (target.name.getPackageName().equals(this.mReferrerPackage)) {
                    it2 = it;
                } else if (isPersistentProcess(target)) {
                    it2 = it;
                } else {
                    it2 = it;
                    recencyScore = (float) Math.max(pkStats.getLastTimeUsed() - recentSinceTime, 0);
                    resolverTarget.setRecencyScore(recencyScore);
                    if (recencyScore > mostRecencyScore) {
                        mostRecencyScore = recencyScore;
                    }
                }
                recencyScore = (float) pkStats.getTotalTimeInForeground();
                resolverTarget.setTimeSpentScore(recencyScore);
                if (recencyScore > mostTimeSpentScore) {
                    mostTimeSpentScore = recencyScore;
                }
                float launchScore = (float) pkStats.mLaunchCount;
                resolverTarget.setLaunchScore(launchScore);
                if (launchScore > mostLaunchScore) {
                    mostLaunchScore = launchScore;
                }
                float chooserScore = 0.0f;
                if (pkStats.mChooserCounts == null || this.mAction == null) {
                    usageStats = pkStats;
                } else if (pkStats.mChooserCounts.get(this.mAction) != null) {
                    chooserScore = (float) ((Integer) ((ArrayMap) pkStats.mChooserCounts.get(this.mAction)).getOrDefault(this.mContentType, Integer.valueOf(0))).intValue();
                    if (this.mAnnotations != null) {
                        target = this.mAnnotations.length;
                        int i = 0;
                        while (i < target) {
                            chooserScore += (float) ((Integer) ((ArrayMap) pkStats.mChooserCounts.get(this.mAction)).getOrDefault(this.mAnnotations[i], Integer.valueOf(0))).intValue();
                            i++;
                            target = target;
                            pkStats = pkStats;
                        }
                        int size = target;
                        usageStats = pkStats;
                    }
                } else {
                    usageStats = pkStats;
                }
                resolverTarget.setChooserScore(chooserScore);
                if (chooserScore > mostChooserScore) {
                    mostChooserScore = chooserScore;
                }
            } else {
                usageStats = pkStats;
                it2 = it;
            }
            it = it2;
        }
        this.mTargets = new ArrayList(this.mTargetsDict.values());
        Iterator it3 = this.mTargets.iterator();
        while (it3.hasNext()) {
            ResolverTarget target2 = (ResolverTarget) it3.next();
            float recency = target2.getRecencyScore() / mostRecencyScore;
            setFeatures(target2, (recency * recency) * RECENCY_MULTIPLIER, target2.getLaunchScore() / mostLaunchScore, target2.getTimeSpentScore() / mostTimeSpentScore, target2.getChooserScore() / mostChooserScore);
            addDefaultSelectProbability(target2);
        }
        predictSelectProbabilities(this.mTargets);
    }

    public int compare(ResolveInfo lhs, ResolveInfo rhs) {
        if (this.mStats != null) {
            ResolverTarget lhsTarget = (ResolverTarget) this.mTargetsDict.get(new ComponentName(lhs.activityInfo.packageName, lhs.activityInfo.name));
            ResolverTarget rhsTarget = (ResolverTarget) this.mTargetsDict.get(new ComponentName(rhs.activityInfo.packageName, rhs.activityInfo.name));
            if (!(lhsTarget == null || rhsTarget == null)) {
                int selectProbabilityDiff = Float.compare(rhsTarget.getSelectProbability(), lhsTarget.getSelectProbability());
                if (selectProbabilityDiff != 0) {
                    return selectProbabilityDiff > 0 ? 1 : -1;
                }
            }
        }
        CharSequence sa = lhs.loadLabel(this.mPm);
        if (sa == null) {
            sa = lhs.activityInfo.name;
        }
        CharSequence sb = rhs.loadLabel(this.mPm);
        if (sb == null) {
            sb = rhs.activityInfo.name;
        }
        return this.mCollator.compare(sa.toString().trim(), sb.toString().trim());
    }

    public float getScore(ComponentName name) {
        ResolverTarget target = (ResolverTarget) this.mTargetsDict.get(name);
        if (target != null) {
            return target.getSelectProbability();
        }
        return 0.0f;
    }

    public void updateModel(ComponentName componentName) {
        synchronized (this.mLock) {
            if (this.mRanker != null) {
                try {
                    int selectedPos = new ArrayList(this.mTargetsDict.keySet()).indexOf(componentName);
                    if (selectedPos >= 0 && this.mTargets != null) {
                        float selectedProbability = getScore(componentName);
                        int order = 0;
                        Iterator it = this.mTargets.iterator();
                        while (it.hasNext()) {
                            if (((ResolverTarget) it.next()).getSelectProbability() > selectedProbability) {
                                order++;
                            }
                        }
                        logMetrics(order);
                        this.mRanker.train(this.mTargets, selectedPos);
                    }
                } catch (RemoteException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error in Train: ");
                    stringBuilder.append(e);
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
    }

    public void destroy() {
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        ResolverRankerServiceConnection resolverRankerServiceConnection = this.mConnection;
        if (resolverRankerServiceConnection != null) {
            this.mContext.unbindService(resolverRankerServiceConnection);
            this.mConnection.destroy();
        }
        afterCompute();
    }

    private void logMetrics(int selectedPos) {
        if (this.mRankerServiceName != null) {
            MetricsLogger metricsLogger = new MetricsLogger();
            LogMaker log = new LogMaker(1085);
            log.setComponentName(this.mRankerServiceName);
            log.addTaggedData(1086, Integer.valueOf(this.mAnnotations == null ? 0 : 1));
            log.addTaggedData(1087, Integer.valueOf(selectedPos));
            metricsLogger.write(log);
        }
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            r0 = resolveRankerService();
     */
    /* JADX WARNING: Missing block: B:11:0x0012, code skipped:
            if (r0 != null) goto L_0x0015;
     */
    /* JADX WARNING: Missing block: B:12:0x0014, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:13:0x0015, code skipped:
            r4.mConnectSignal = new java.util.concurrent.CountDownLatch(1);
            r4.mConnection = new com.android.internal.app.ResolverRankerServiceResolverComparator.ResolverRankerServiceConnection(r4, r4.mConnectSignal);
            r5.bindServiceAsUser(r0, r4.mConnection, 1, android.os.UserHandle.SYSTEM);
     */
    /* JADX WARNING: Missing block: B:14:0x002d, code skipped:
            return;
     */
    private void initRanker(android.content.Context r5) {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.mConnection;	 Catch:{ all -> 0x002e }
        if (r1 == 0) goto L_0x000d;
    L_0x0007:
        r1 = r4.mRanker;	 Catch:{ all -> 0x002e }
        if (r1 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        r0 = r4.resolveRankerService();
        if (r0 != 0) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r1 = new java.util.concurrent.CountDownLatch;
        r2 = 1;
        r1.<init>(r2);
        r4.mConnectSignal = r1;
        r1 = new com.android.internal.app.ResolverRankerServiceResolverComparator$ResolverRankerServiceConnection;
        r3 = r4.mConnectSignal;
        r1.<init>(r3);
        r4.mConnection = r1;
        r1 = r4.mConnection;
        r3 = android.os.UserHandle.SYSTEM;
        r5.bindServiceAsUser(r0, r1, r2, r3);
        return;
    L_0x002e:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.ResolverRankerServiceResolverComparator.initRanker(android.content.Context):void");
    }

    private Intent resolveRankerService() {
        String str = "android.permission.PROVIDE_RESOLVER_RANKER_SERVICE";
        String str2 = "android.permission.BIND_RESOLVER_RANKER_SERVICE";
        String str3 = TAG;
        Intent intent = new Intent(ResolverRankerService.SERVICE_INTERFACE);
        for (ResolveInfo resolveInfo : this.mPm.queryIntentServices(intent, 0)) {
            if (!(resolveInfo == null || resolveInfo.serviceInfo == null)) {
                if (resolveInfo.serviceInfo.applicationInfo != null) {
                    ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.applicationInfo.packageName, resolveInfo.serviceInfo.name);
                    StringBuilder stringBuilder;
                    try {
                        String str4 = "ResolverRankerService ";
                        if (!str2.equals(this.mPm.getServiceInfo(componentName, 0).permission)) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str4);
                            stringBuilder.append(componentName);
                            stringBuilder.append(" does not require permission ");
                            stringBuilder.append(str2);
                            stringBuilder.append(" - this service will not be queried for ResolverRankerServiceResolverComparator. add android:permission=\"");
                            stringBuilder.append(str2);
                            stringBuilder.append("\" to the <service> tag for ");
                            stringBuilder.append(componentName);
                            stringBuilder.append(" in the manifest.");
                            Log.w(str3, stringBuilder.toString());
                        } else if (this.mPm.checkPermission(str, resolveInfo.serviceInfo.packageName) != 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str4);
                            stringBuilder.append(componentName);
                            stringBuilder.append(" does not hold permission ");
                            stringBuilder.append(str);
                            stringBuilder.append(" - this service will not be queried for ResolverRankerServiceResolverComparator.");
                            Log.w(str3, stringBuilder.toString());
                        } else {
                            this.mResolvedRankerName = componentName;
                            intent.setComponent(componentName);
                            return intent;
                        }
                    } catch (NameNotFoundException e) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Could not look up service ");
                        stringBuilder.append(componentName);
                        stringBuilder.append("; component name not found");
                        Log.e(str3, stringBuilder.toString());
                    }
                }
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void beforeCompute() {
        super.beforeCompute();
        this.mTargetsDict.clear();
        this.mTargets = null;
        this.mRankerServiceName = new ComponentName(this.mContext, getClass());
        this.mResolvedRankerName = null;
        initRanker(this.mContext);
    }

    private void predictSelectProbabilities(List<ResolverTarget> targets) {
        if (this.mConnection != null) {
            try {
                this.mConnectSignal.await(200, TimeUnit.MILLISECONDS);
                synchronized (this.mLock) {
                    if (this.mRanker != null) {
                        this.mRanker.predict(targets, this.mConnection.resolverRankerResult);
                        return;
                    }
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Error in Wait for Service Connection.");
            } catch (RemoteException e2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error in Predict: ");
                stringBuilder.append(e2);
                Log.e(TAG, stringBuilder.toString());
            }
        }
        afterCompute();
    }

    private void addDefaultSelectProbability(ResolverTarget target) {
        target.setSelectProbability((float) (1.0d / (Math.exp((double) (1.6568f - ((((target.getLaunchScore() * 2.5543f) + (target.getTimeSpentScore() * 2.8412f)) + (target.getRecencyScore() * 0.269f)) + (target.getChooserScore() * 4.2222f)))) + 1.0d)));
    }

    private void setFeatures(ResolverTarget target, float recencyScore, float launchScore, float timeSpentScore, float chooserScore) {
        target.setRecencyScore(recencyScore);
        target.setLaunchScore(launchScore);
        target.setTimeSpentScore(timeSpentScore);
        target.setChooserScore(chooserScore);
    }

    static boolean isPersistentProcess(ResolvedComponentInfo rci) {
        boolean z = false;
        if (rci == null || rci.getCount() <= 0) {
            return false;
        }
        if ((rci.getResolveInfoAt(0).activityInfo.applicationInfo.flags & 8) != 0) {
            z = true;
        }
        return z;
    }
}
