package com.android.internal.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.service.chooser.IChooserTargetResult;
import android.service.chooser.IChooserTargetResult.Stub;
import android.service.chooser.IChooserTargetService;
import android.text.TextUtils;
import android.util.FloatProperty;
import android.util.Log;
import android.util.Slog;
import android.util.TimedRemoteCaller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ResolverActivity.DisplayResolveInfo;
import com.android.internal.app.ResolverActivity.ResolveListAdapter;
import com.android.internal.app.ResolverActivity.TargetInfo;
import com.android.internal.logging.MetricsLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooserActivity extends ResolverActivity {
    private static final float CALLER_TARGET_SCORE_BOOST = 900.0f;
    private static final int CHOOSER_TARGET_SERVICE_RESULT = 1;
    private static final int CHOOSER_TARGET_SERVICE_WATCHDOG_TIMEOUT = 2;
    private static final boolean DEBUG = false;
    public static final String EXTRA_PRIVATE_RETAIN_IN_ON_STOP = "com.android.internal.app.ChooserActivity.EXTRA_PRIVATE_RETAIN_IN_ON_STOP";
    private static final String PINNED_SHARED_PREFS_NAME = "chooser_pin_settings";
    private static final float PINNED_TARGET_SCORE_BOOST = 1000.0f;
    private static final int QUERY_TARGET_SERVICE_LIMIT = 5;
    private static final String TAG = "ChooserActivity";
    private static final String TARGET_DETAILS_FRAGMENT_TAG = "targetDetailsFragment";
    private static final int WATCHDOG_TIMEOUT_MILLIS = 5000;
    private ChooserTarget[] mCallerChooserTargets;
    private final Handler mChooserHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 1) {
                if (i != 2) {
                    super.handleMessage(msg);
                    return;
                }
                ChooserActivity.this.unbindRemainingServices();
                ChooserActivity.this.sendVoiceChoicesIfNeeded();
                ChooserActivity.this.mChooserListAdapter.setShowServiceTargets(true);
            } else if (!ChooserActivity.this.isDestroyed()) {
                ServiceResultInfo sri = msg.obj;
                if (ChooserActivity.this.mServiceConnections.contains(sri.connection)) {
                    if (sri.resultTargets != null) {
                        ChooserActivity.this.mChooserListAdapter.addServiceResults(sri.originalTarget, sri.resultTargets);
                    }
                    ChooserActivity.this.unbindService(sri.connection);
                    sri.connection.destroy();
                    ChooserActivity.this.mServiceConnections.remove(sri.connection);
                    if (ChooserActivity.this.mServiceConnections.isEmpty()) {
                        ChooserActivity.this.mChooserHandler.removeMessages(2);
                        ChooserActivity.this.sendVoiceChoicesIfNeeded();
                        ChooserActivity.this.mChooserListAdapter.setShowServiceTargets(true);
                        return;
                    }
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ChooserTargetServiceConnection ");
                stringBuilder.append(sri.connection);
                stringBuilder.append(" returned after being removed from active connections. Have you considered returning results faster?");
                Log.w(ChooserActivity.TAG, stringBuilder.toString());
            }
        }
    };
    private ChooserListAdapter mChooserListAdapter;
    private ChooserRowAdapter mChooserRowAdapter;
    private long mChooserShownTime;
    private IntentSender mChosenComponentSender;
    private ComponentName[] mFilteredComponentNames;
    protected boolean mIsSuccessfullySelected;
    private SharedPreferences mPinnedSharedPrefs;
    private Intent mReferrerFillInIntent;
    private IntentSender mRefinementIntentSender;
    private RefinementResultReceiver mRefinementResultReceiver;
    private Bundle mReplacementExtras;
    private final List<ChooserTargetServiceConnection> mServiceConnections = new ArrayList();

    static class BaseChooserTargetComparator implements Comparator<ChooserTarget> {
        BaseChooserTargetComparator() {
        }

        public int compare(ChooserTarget lhs, ChooserTarget rhs) {
            return (int) Math.signum(rhs.getScore() - lhs.getScore());
        }
    }

    public class ChooserListAdapter extends ResolveListAdapter {
        private static final int MAX_SERVICE_TARGETS = 8;
        private static final int MAX_TARGETS_PER_SERVICE = 4;
        public static final int TARGET_BAD = -1;
        public static final int TARGET_CALLER = 0;
        public static final int TARGET_SERVICE = 1;
        public static final int TARGET_STANDARD = 2;
        private final BaseChooserTargetComparator mBaseTargetComparator = new BaseChooserTargetComparator();
        private final List<TargetInfo> mCallerTargets = new ArrayList();
        private float mLateFee = 1.0f;
        private final List<ChooserTargetInfo> mServiceTargets = new ArrayList();
        private boolean mShowServiceTargets;
        final /* synthetic */ ChooserActivity this$0;

        public ChooserListAdapter(ChooserActivity this$0, Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed, ResolverListController resolverListController) {
            ChooserActivity chooserActivity = this$0;
            Intent[] intentArr = initialIntents;
            this.this$0 = chooserActivity;
            super(context, payloadIntents, null, rList, launchedFromUid, filterLastUsed, resolverListController);
            if (intentArr != null) {
                PackageManager pm = this$0.getPackageManager();
                int i = 0;
                while (i < intentArr.length) {
                    PackageManager pm2;
                    Intent ii = intentArr[i];
                    if (ii == null) {
                        pm2 = pm;
                    } else {
                        ResolveInfo ri;
                        ActivityInfo ai;
                        ResolveInfo ri2 = null;
                        ActivityInfo ai2 = null;
                        if (ii.getComponent() != null) {
                            try {
                                ai2 = pm.getActivityInfo(ii.getComponent(), 0);
                                ri2 = new ResolveInfo();
                                ri2.activityInfo = ai2;
                            } catch (NameNotFoundException e) {
                            }
                        }
                        if (ai2 == null) {
                            ResolveInfo ri3 = pm.resolveActivity(ii, 65536);
                            ri = ri3;
                            ai = ri3 != null ? ri3.activityInfo : null;
                        } else {
                            ri = ri2;
                            ai = ai2;
                        }
                        if (ai == null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("No activity found for ");
                            stringBuilder.append(ii);
                            Log.w(ChooserActivity.TAG, stringBuilder.toString());
                            pm2 = pm;
                        } else {
                            UserManager userManager = (UserManager) chooserActivity.getSystemService("user");
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
                            DisplayResolveInfo displayResolveInfo = r1;
                            pm2 = pm;
                            List list = this.mCallerTargets;
                            DisplayResolveInfo displayResolveInfo2 = new DisplayResolveInfo(ii, ri, ri.loadLabel(pm), null, ii);
                            list.add(displayResolveInfo);
                        }
                    }
                    i++;
                    pm = pm2;
                }
            }
        }

        public boolean showsExtendedInfo(TargetInfo info) {
            return false;
        }

        public boolean isComponentPinned(ComponentName name) {
            return this.this$0.mPinnedSharedPrefs.getBoolean(name.flattenToString(), false);
        }

        public float getScore(DisplayResolveInfo target) {
            if (target == null) {
                return ChooserActivity.CALLER_TARGET_SCORE_BOOST;
            }
            float score = super.getScore(target);
            if (target.isPinned()) {
                score += ChooserActivity.PINNED_TARGET_SCORE_BOOST;
            }
            return score;
        }

        public View onCreateView(ViewGroup parent) {
            return this.mInflater.inflate((int) R.layout.resolve_grid_item, parent, false);
        }

        public void onListRebuilt() {
            if (this.mServiceTargets != null) {
                pruneServiceTargets();
            }
            this.this$0.queryTargetServices(this);
        }

        public boolean shouldGetResolvedFilter() {
            return true;
        }

        public int getCount() {
            return (super.getCount() + getServiceTargetCount()) + getCallerTargetCount();
        }

        public int getUnfilteredCount() {
            return (super.getUnfilteredCount() + getServiceTargetCount()) + getCallerTargetCount();
        }

        public int getCallerTargetCount() {
            return this.mCallerTargets.size();
        }

        public int getServiceTargetCount() {
            if (this.mShowServiceTargets) {
                return Math.min(this.mServiceTargets.size(), 8);
            }
            return 0;
        }

        public int getStandardTargetCount() {
            return super.getCount();
        }

        public int getPositionTargetType(int position) {
            int callerTargetCount = getCallerTargetCount();
            if (position < callerTargetCount) {
                return 0;
            }
            int offset = 0 + callerTargetCount;
            int serviceTargetCount = getServiceTargetCount();
            if (position - offset < serviceTargetCount) {
                return 1;
            }
            if (position - (offset + serviceTargetCount) < super.getCount()) {
                return 2;
            }
            return -1;
        }

        public TargetInfo getItem(int position) {
            return targetInfoForPosition(position, true);
        }

        public TargetInfo targetInfoForPosition(int position, boolean filtered) {
            int callerTargetCount = getCallerTargetCount();
            if (position < callerTargetCount) {
                return (TargetInfo) this.mCallerTargets.get(position);
            }
            int offset = 0 + callerTargetCount;
            if (offset > position || position >= super.getCount() + offset) {
                return (TargetInfo) this.mServiceTargets.get(position - (offset + super.getCount()));
            }
            TargetInfo item;
            if (filtered) {
                item = super.getItem(position - offset);
            } else {
                item = getDisplayInfoAt(position - offset);
            }
            return item;
        }

        public void addServiceResults(DisplayResolveInfo origTarget, List<ChooserTarget> targets) {
            float parentScore = getScore(origTarget);
            Collections.sort(targets, this.mBaseTargetComparator);
            float lastScore = 0.0f;
            int N = Math.min(targets.size(), 4);
            for (int i = 0; i < N; i++) {
                ChooserTarget target = (ChooserTarget) targets.get(i);
                float targetScore = (target.getScore() * parentScore) * this.mLateFee;
                if (i > 0 && targetScore >= lastScore) {
                    targetScore = lastScore * 0.95f;
                }
                insertServiceTarget(new ChooserTargetInfo(origTarget, target, targetScore));
                lastScore = targetScore;
            }
            this.mLateFee *= 0.95f;
            notifyDataSetChanged();
        }

        public void setShowServiceTargets(boolean show) {
            this.mShowServiceTargets = show;
            notifyDataSetChanged();
        }

        private void insertServiceTarget(ChooserTargetInfo chooserTargetInfo) {
            float newScore = chooserTargetInfo.getModifiedScore();
            int N = this.mServiceTargets.size();
            for (int i = 0; i < N; i++) {
                if (newScore > ((ChooserTargetInfo) this.mServiceTargets.get(i)).getModifiedScore()) {
                    this.mServiceTargets.add(i, chooserTargetInfo);
                    return;
                }
            }
            this.mServiceTargets.add(chooserTargetInfo);
        }

        private void pruneServiceTargets() {
            for (int i = this.mServiceTargets.size() - 1; i >= 0; i--) {
                if (!hasResolvedTarget(((ChooserTargetInfo) this.mServiceTargets.get(i)).getResolveInfo())) {
                    this.mServiceTargets.remove(i);
                }
            }
        }
    }

    public class ChooserListController extends ResolverListController {
        public ChooserListController(Context context, PackageManager pm, Intent targetIntent, String referrerPackageName, int launchedFromUid) {
            super(context, pm, targetIntent, referrerPackageName, launchedFromUid);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isComponentPinned(ComponentName name) {
            return ChooserActivity.this.mPinnedSharedPrefs.getBoolean(name.flattenToString(), false);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isComponentFiltered(ComponentName name) {
            if (ChooserActivity.this.mFilteredComponentNames == null) {
                return false;
            }
            for (ComponentName filteredComponentName : ChooserActivity.this.mFilteredComponentNames) {
                if (name.equals(filteredComponentName)) {
                    return true;
                }
            }
            return false;
        }

        public float getScore(DisplayResolveInfo target) {
            if (target == null) {
                return ChooserActivity.CALLER_TARGET_SCORE_BOOST;
            }
            float score = super.getScore(target);
            if (target.isPinned()) {
                score += ChooserActivity.PINNED_TARGET_SCORE_BOOST;
            }
            return score;
        }
    }

    class ChooserRowAdapter extends BaseAdapter {
        private int mAnimationCount = 0;
        private ChooserListAdapter mChooserListAdapter;
        private final int mColumnCount = 4;
        private final Interpolator mInterpolator;
        private final LayoutInflater mLayoutInflater;
        private RowScale[] mServiceTargetScale;

        public ChooserRowAdapter(ChooserListAdapter wrappedAdapter) {
            this.mChooserListAdapter = wrappedAdapter;
            this.mLayoutInflater = LayoutInflater.from(ChooserActivity.this);
            this.mInterpolator = AnimationUtils.loadInterpolator(ChooserActivity.this, 17563653);
            wrappedAdapter.registerDataSetObserver(new DataSetObserver(ChooserActivity.this) {
                public void onChanged() {
                    super.onChanged();
                    int rcount = ChooserRowAdapter.this.getServiceTargetRowCount();
                    if (ChooserRowAdapter.this.mServiceTargetScale == null || ChooserRowAdapter.this.mServiceTargetScale.length != rcount) {
                        int i;
                        RowScale[] old = ChooserRowAdapter.this.mServiceTargetScale;
                        int oldRCount = old != null ? old.length : 0;
                        ChooserRowAdapter.this.mServiceTargetScale = new RowScale[rcount];
                        if (old != null && rcount > 0) {
                            System.arraycopy(old, 0, ChooserRowAdapter.this.mServiceTargetScale, 0, Math.min(old.length, rcount));
                        }
                        for (i = rcount; i < oldRCount; i++) {
                            old[i].cancelAnimation();
                        }
                        for (i = oldRCount; i < rcount; i++) {
                            ChooserRowAdapter.this.mServiceTargetScale[i] = new RowScale(ChooserRowAdapter.this, 0.0f, 1.0f).setInterpolator(ChooserRowAdapter.this.mInterpolator);
                        }
                        for (i = oldRCount; i < rcount; i++) {
                            ChooserRowAdapter.this.mServiceTargetScale[i].startAnimation();
                        }
                    }
                    ChooserRowAdapter.this.notifyDataSetChanged();
                }

                public void onInvalidated() {
                    super.onInvalidated();
                    ChooserRowAdapter.this.notifyDataSetInvalidated();
                    if (ChooserRowAdapter.this.mServiceTargetScale != null) {
                        for (RowScale rs : ChooserRowAdapter.this.mServiceTargetScale) {
                            rs.cancelAnimation();
                        }
                    }
                }
            });
        }

        private float getRowScale(int rowPosition) {
            int start = getCallerTargetRowCount();
            int end = getServiceTargetRowCount() + start;
            if (rowPosition < start || rowPosition >= end) {
                return 1.0f;
            }
            return this.mServiceTargetScale[rowPosition - start].get();
        }

        public void onAnimationStart() {
            boolean lock = this.mAnimationCount == 0;
            this.mAnimationCount++;
            if (lock) {
                ChooserActivity.this.mResolverDrawerLayout.setDismissLocked(true);
            }
        }

        public void onAnimationEnd() {
            this.mAnimationCount--;
            if (this.mAnimationCount == 0) {
                ChooserActivity.this.mResolverDrawerLayout.setDismissLocked(false);
            }
        }

        public int getCount() {
            return (int) (((double) (getCallerTargetRowCount() + getServiceTargetRowCount())) + Math.ceil((double) (((float) this.mChooserListAdapter.getStandardTargetCount()) / 4.0f)));
        }

        public int getCallerTargetRowCount() {
            return (int) Math.ceil((double) (((float) this.mChooserListAdapter.getCallerTargetCount()) / 4.0f));
        }

        public int getServiceTargetRowCount() {
            return (int) Math.ceil((double) (((float) this.mChooserListAdapter.getServiceTargetCount()) / 4.0f));
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RowViewHolder holder;
            if (convertView == null) {
                holder = createViewHolder(parent);
            } else {
                holder = (RowViewHolder) convertView.getTag();
            }
            bindViewHolder(position, holder);
            return holder.row;
        }

        /* Access modifiers changed, original: 0000 */
        public RowViewHolder createViewHolder(ViewGroup parent) {
            ViewGroup row = (ViewGroup) this.mLayoutInflater.inflate((int) R.layout.chooser_row, parent, false);
            final RowViewHolder holder = new RowViewHolder(row, 4);
            int spec = MeasureSpec.makeMeasureSpec(0, 0);
            for (int i = 0; i < 4; i++) {
                View v = this.mChooserListAdapter.createView(row);
                final int column = i;
                v.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ChooserActivity.this.startSelected(holder.itemIndices[column], false, true);
                    }
                });
                v.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        ChooserActivity.this.showTargetDetails(ChooserRowAdapter.this.mChooserListAdapter.resolveInfoForPosition(holder.itemIndices[column], true));
                        return true;
                    }
                });
                row.addView(v);
                holder.cells[i] = v;
                LayoutParams lp = v.getLayoutParams();
                v.measure(spec, spec);
                if (lp == null) {
                    row.setLayoutParams(new LayoutParams(-1, v.getMeasuredHeight()));
                } else {
                    lp.height = v.getMeasuredHeight();
                }
            }
            holder.measure();
            LayoutParams lp2 = row.getLayoutParams();
            if (lp2 == null) {
                row.setLayoutParams(new LayoutParams(-1, holder.measuredRowHeight));
            } else {
                lp2.height = holder.measuredRowHeight;
            }
            row.setTag(holder);
            return holder;
        }

        /* Access modifiers changed, original: 0000 */
        public void bindViewHolder(int rowPosition, RowViewHolder holder) {
            int start = getFirstRowPosition(rowPosition);
            int startType = this.mChooserListAdapter.getPositionTargetType(start);
            int end = (start + 4) - 1;
            while (this.mChooserListAdapter.getPositionTargetType(end) != startType && end >= start) {
                end--;
            }
            if (startType == 1) {
                holder.row.setBackgroundColor(ChooserActivity.this.getColor(R.color.chooser_service_row_background_color));
            } else {
                holder.row.setBackgroundColor(0);
            }
            int oldHeight = holder.row.getLayoutParams().height;
            holder.row.getLayoutParams().height = Math.max(1, (int) (((float) holder.measuredRowHeight) * getRowScale(rowPosition)));
            if (holder.row.getLayoutParams().height != oldHeight) {
                holder.row.requestLayout();
            }
            for (int i = 0; i < 4; i++) {
                View v = holder.cells[i];
                if (start + i <= end) {
                    v.setVisibility(0);
                    holder.itemIndices[i] = start + i;
                    this.mChooserListAdapter.bindView(holder.itemIndices[i], v);
                } else {
                    v.setVisibility(8);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public int getFirstRowPosition(int row) {
            int callerCount = this.mChooserListAdapter.getCallerTargetCount();
            int callerRows = (int) Math.ceil((double) (((float) callerCount) / 4.0f));
            if (row < callerRows) {
                return row * 4;
            }
            int serviceCount = this.mChooserListAdapter.getServiceTargetCount();
            int serviceRows = (int) Math.ceil((double) (((float) serviceCount) / 4.0f));
            if (row < callerRows + serviceRows) {
                return ((row - callerRows) * 4) + callerCount;
            }
            return (callerCount + serviceCount) + (((row - callerRows) - serviceRows) * 4);
        }
    }

    final class ChooserTargetInfo implements TargetInfo {
        private final ResolveInfo mBackupResolveInfo;
        private CharSequence mBadgeContentDescription;
        private Drawable mBadgeIcon = null;
        private final ChooserTarget mChooserTarget;
        private Drawable mDisplayIcon;
        private final int mFillInFlags;
        private final Intent mFillInIntent;
        private final float mModifiedScore;
        private final DisplayResolveInfo mSourceInfo;

        public ChooserTargetInfo(DisplayResolveInfo sourceInfo, ChooserTarget chooserTarget, float modifiedScore) {
            this.mSourceInfo = sourceInfo;
            this.mChooserTarget = chooserTarget;
            this.mModifiedScore = modifiedScore;
            if (sourceInfo != null) {
                ResolveInfo ri = sourceInfo.getResolveInfo();
                if (ri != null) {
                    ActivityInfo ai = ri.activityInfo;
                    if (!(ai == null || ai.applicationInfo == null)) {
                        PackageManager pm = ChooserActivity.this.getPackageManager();
                        this.mBadgeIcon = pm.getApplicationIcon(ai.applicationInfo);
                        this.mBadgeContentDescription = pm.getApplicationLabel(ai.applicationInfo);
                    }
                }
            }
            Icon icon = chooserTarget.getIcon();
            this.mDisplayIcon = icon != null ? icon.loadDrawable(ChooserActivity.this) : null;
            if (sourceInfo != null) {
                this.mBackupResolveInfo = null;
            } else {
                this.mBackupResolveInfo = ChooserActivity.this.getPackageManager().resolveActivity(getResolvedIntent(), 0);
            }
            this.mFillInIntent = null;
            this.mFillInFlags = 0;
        }

        private ChooserTargetInfo(ChooserTargetInfo other, Intent fillInIntent, int flags) {
            this.mSourceInfo = other.mSourceInfo;
            this.mBackupResolveInfo = other.mBackupResolveInfo;
            this.mChooserTarget = other.mChooserTarget;
            this.mBadgeIcon = other.mBadgeIcon;
            this.mBadgeContentDescription = other.mBadgeContentDescription;
            this.mDisplayIcon = other.mDisplayIcon;
            this.mFillInIntent = fillInIntent;
            this.mFillInFlags = flags;
            this.mModifiedScore = other.mModifiedScore;
        }

        public float getModifiedScore() {
            return this.mModifiedScore;
        }

        public Intent getResolvedIntent() {
            DisplayResolveInfo displayResolveInfo = this.mSourceInfo;
            if (displayResolveInfo != null) {
                return displayResolveInfo.getResolvedIntent();
            }
            Intent targetIntent = new Intent(ChooserActivity.this.getTargetIntent());
            targetIntent.setComponent(this.mChooserTarget.getComponentName());
            targetIntent.putExtras(this.mChooserTarget.getIntentExtras());
            return targetIntent;
        }

        public ComponentName getResolvedComponentName() {
            DisplayResolveInfo displayResolveInfo = this.mSourceInfo;
            if (displayResolveInfo != null) {
                return displayResolveInfo.getResolvedComponentName();
            }
            ResolveInfo resolveInfo = this.mBackupResolveInfo;
            if (resolveInfo != null) {
                return new ComponentName(resolveInfo.activityInfo.packageName, this.mBackupResolveInfo.activityInfo.name);
            }
            return null;
        }

        private Intent getBaseIntentToSend() {
            Intent result = getResolvedIntent();
            if (result == null) {
                Log.e(ChooserActivity.TAG, "ChooserTargetInfo: no base intent available to send");
            } else {
                result = new Intent(result);
                Intent intent = this.mFillInIntent;
                if (intent != null) {
                    result.fillIn(intent, this.mFillInFlags);
                }
                result.fillIn(ChooserActivity.this.mReferrerFillInIntent, 0);
            }
            return result;
        }

        public boolean start(Activity activity, Bundle options) {
            throw new RuntimeException("ChooserTargets should be started as caller.");
        }

        public boolean startAsCaller(Activity activity, Bundle options, int userId) {
            Intent intent = getBaseIntentToSend();
            if (intent == null) {
                return false;
            }
            intent.setComponent(this.mChooserTarget.getComponentName());
            intent.putExtras(this.mChooserTarget.getIntentExtras());
            DisplayResolveInfo displayResolveInfo = this.mSourceInfo;
            boolean ignoreTargetSecurity = displayResolveInfo != null && displayResolveInfo.getResolvedComponentName().getPackageName().equals(this.mChooserTarget.getComponentName().getPackageName());
            activity.startActivityAsCaller(intent, options, null, ignoreTargetSecurity, userId);
            return true;
        }

        public boolean startAsUser(Activity activity, Bundle options, UserHandle user) {
            throw new RuntimeException("ChooserTargets should be started as caller.");
        }

        public ResolveInfo getResolveInfo() {
            DisplayResolveInfo displayResolveInfo = this.mSourceInfo;
            return displayResolveInfo != null ? displayResolveInfo.getResolveInfo() : this.mBackupResolveInfo;
        }

        public CharSequence getDisplayLabel() {
            return this.mChooserTarget.getTitle();
        }

        public CharSequence getExtendedInfo() {
            return null;
        }

        public Drawable getDisplayIcon() {
            return this.mDisplayIcon;
        }

        public Drawable getBadgeIcon() {
            return this.mBadgeIcon;
        }

        public CharSequence getBadgeContentDescription() {
            return this.mBadgeContentDescription;
        }

        public TargetInfo cloneFilledIn(Intent fillInIntent, int flags) {
            return new ChooserTargetInfo(this, fillInIntent, flags);
        }

        public List<Intent> getAllSourceIntents() {
            List<Intent> results = new ArrayList();
            DisplayResolveInfo displayResolveInfo = this.mSourceInfo;
            if (displayResolveInfo != null) {
                results.add((Intent) displayResolveInfo.getAllSourceIntents().get(0));
            }
            return results;
        }

        public boolean isPinned() {
            DisplayResolveInfo displayResolveInfo = this.mSourceInfo;
            return displayResolveInfo != null ? displayResolveInfo.isPinned() : false;
        }
    }

    static class ChooserTargetServiceConnection implements ServiceConnection {
        private ChooserActivity mChooserActivity;
        private final IChooserTargetResult mChooserTargetResult = new Stub() {
            public void sendResult(List<ChooserTarget> targets) throws RemoteException {
                synchronized (ChooserTargetServiceConnection.this.mLock) {
                    if (ChooserTargetServiceConnection.this.mChooserActivity == null) {
                        String str = ChooserActivity.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("destroyed ChooserTargetServiceConnection received result from ");
                        stringBuilder.append(ChooserTargetServiceConnection.this.mConnectedComponent);
                        stringBuilder.append("; ignoring...");
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    ChooserTargetServiceConnection.this.mChooserActivity.filterServiceTargets(ChooserTargetServiceConnection.this.mOriginalTarget.getResolveInfo().activityInfo.packageName, targets);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = new ServiceResultInfo(ChooserTargetServiceConnection.this.mOriginalTarget, targets, ChooserTargetServiceConnection.this);
                    ChooserTargetServiceConnection.this.mChooserActivity.mChooserHandler.sendMessage(msg);
                }
            }
        };
        private ComponentName mConnectedComponent;
        private final Object mLock = new Object();
        private DisplayResolveInfo mOriginalTarget;

        public ChooserTargetServiceConnection(ChooserActivity chooserActivity, DisplayResolveInfo dri) {
            this.mChooserActivity = chooserActivity;
            this.mOriginalTarget = dri;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (this.mLock) {
                if (this.mChooserActivity == null) {
                    Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection got onServiceConnected");
                    return;
                }
                try {
                    IChooserTargetService.Stub.asInterface(service).getChooserTargets(this.mOriginalTarget.getResolvedComponentName(), this.mOriginalTarget.getResolveInfo().filter, this.mChooserTargetResult);
                } catch (RemoteException e) {
                    String str = ChooserActivity.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Querying ChooserTargetService ");
                    stringBuilder.append(name);
                    stringBuilder.append(" failed.");
                    Log.e(str, stringBuilder.toString(), e);
                    this.mChooserActivity.unbindService(this);
                    destroy();
                    this.mChooserActivity.mServiceConnections.remove(this);
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            synchronized (this.mLock) {
                if (this.mChooserActivity == null) {
                    Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection got onServiceDisconnected");
                    return;
                }
                this.mChooserActivity.unbindService(this);
                destroy();
                this.mChooserActivity.mServiceConnections.remove(this);
                if (this.mChooserActivity.mServiceConnections.isEmpty()) {
                    this.mChooserActivity.mChooserHandler.removeMessages(2);
                    this.mChooserActivity.sendVoiceChoicesIfNeeded();
                }
                this.mConnectedComponent = null;
            }
        }

        public void destroy() {
            synchronized (this.mLock) {
                this.mChooserActivity = null;
                this.mOriginalTarget = null;
            }
        }

        public String toString() {
            String activityInfo;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ChooserTargetServiceConnection{service=");
            stringBuilder.append(this.mConnectedComponent);
            stringBuilder.append(", activity=");
            DisplayResolveInfo displayResolveInfo = this.mOriginalTarget;
            if (displayResolveInfo != null) {
                activityInfo = displayResolveInfo.getResolveInfo().activityInfo.toString();
            } else {
                activityInfo = "<connection destroyed>";
            }
            stringBuilder.append(activityInfo);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    class OffsetDataSetObserver extends DataSetObserver {
        private View mCachedView;
        private int mCachedViewType = -1;
        private final AbsListView mListView;

        public OffsetDataSetObserver(AbsListView listView) {
            this.mListView = listView;
        }

        public void onChanged() {
            if (ChooserActivity.this.mResolverDrawerLayout != null) {
                int chooserTargetRows = ChooserActivity.this.mChooserRowAdapter.getServiceTargetRowCount();
                int offset = 0;
                for (int i = 0; i < chooserTargetRows; i++) {
                    int pos = ChooserActivity.this.mChooserRowAdapter.getCallerTargetRowCount() + i;
                    int vt = ChooserActivity.this.mChooserRowAdapter.getItemViewType(pos);
                    if (vt != this.mCachedViewType) {
                        this.mCachedView = null;
                    }
                    View v = ChooserActivity.this.mChooserRowAdapter.getView(pos, this.mCachedView, this.mListView);
                    offset += (int) (((float) ((RowViewHolder) v.getTag()).measuredRowHeight) * ChooserActivity.this.mChooserRowAdapter.getRowScale(pos));
                    if (vt >= 0) {
                        this.mCachedViewType = vt;
                        this.mCachedView = v;
                    } else {
                        this.mCachedViewType = -1;
                    }
                }
                ChooserActivity.this.mResolverDrawerLayout.setCollapsibleHeightReserved(offset);
            }
        }
    }

    static class RefinementResultReceiver extends ResultReceiver {
        private ChooserActivity mChooserActivity;
        private TargetInfo mSelectedTarget;

        public RefinementResultReceiver(ChooserActivity host, TargetInfo target, Handler handler) {
            super(handler);
            this.mChooserActivity = host;
            this.mSelectedTarget = target;
        }

        /* Access modifiers changed, original: protected */
        public void onReceiveResult(int resultCode, Bundle resultData) {
            ChooserActivity chooserActivity = this.mChooserActivity;
            String str = ChooserActivity.TAG;
            if (chooserActivity == null) {
                Log.e(str, "Destroyed RefinementResultReceiver received a result");
            } else if (resultData == null) {
                Log.e(str, "RefinementResultReceiver received null resultData");
            } else {
                if (resultCode == -1) {
                    Parcelable intentParcelable = resultData.getParcelable(Intent.EXTRA_INTENT);
                    if (intentParcelable instanceof Intent) {
                        this.mChooserActivity.onRefinementResult(this.mSelectedTarget, (Intent) intentParcelable);
                    } else {
                        Log.e(str, "RefinementResultReceiver received RESULT_OK but no Intent in resultData with key Intent.EXTRA_INTENT");
                    }
                } else if (resultCode != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown result code ");
                    stringBuilder.append(resultCode);
                    stringBuilder.append(" sent to RefinementResultReceiver");
                    Log.w(str, stringBuilder.toString());
                } else {
                    chooserActivity.onRefinementCanceled();
                }
            }
        }

        public void destroy() {
            this.mChooserActivity = null;
            this.mSelectedTarget = null;
        }
    }

    static class RowScale {
        private static final int DURATION = 400;
        public static final FloatProperty<RowScale> PROPERTY = new FloatProperty<RowScale>(BatteryManager.EXTRA_SCALE) {
            public void setValue(RowScale object, float value) {
                object.mScale = value;
                object.mAdapter.notifyDataSetChanged();
            }

            public Float get(RowScale object) {
                return Float.valueOf(object.mScale);
            }
        };
        ChooserRowAdapter mAdapter;
        private final ObjectAnimator mAnimator;
        float mScale;

        public RowScale(ChooserRowAdapter adapter, float from, float to) {
            this.mAdapter = adapter;
            this.mScale = from;
            if (from == to) {
                this.mAnimator = null;
                return;
            }
            this.mAnimator = ObjectAnimator.ofFloat((Object) this, PROPERTY, from, to).setDuration(400);
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    RowScale.this.mAdapter.onAnimationStart();
                }

                public void onAnimationEnd(Animator animation) {
                    RowScale.this.mAdapter.onAnimationEnd();
                }
            });
        }

        public RowScale setInterpolator(Interpolator interpolator) {
            ObjectAnimator objectAnimator = this.mAnimator;
            if (objectAnimator != null) {
                objectAnimator.setInterpolator(interpolator);
            }
            return this;
        }

        public float get() {
            return this.mScale;
        }

        public void startAnimation() {
            ObjectAnimator objectAnimator = this.mAnimator;
            if (objectAnimator != null) {
                objectAnimator.start();
            }
        }

        public void cancelAnimation() {
            ObjectAnimator objectAnimator = this.mAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
        }
    }

    static class RowViewHolder {
        final View[] cells;
        int[] itemIndices;
        int measuredRowHeight;
        final ViewGroup row;

        public RowViewHolder(ViewGroup row, int cellCount) {
            this.row = row;
            this.cells = new View[cellCount];
            this.itemIndices = new int[cellCount];
        }

        public void measure() {
            int spec = MeasureSpec.makeMeasureSpec(0, 0);
            this.row.measure(spec, spec);
            this.measuredRowHeight = this.row.getMeasuredHeight();
        }
    }

    static class ServiceResultInfo {
        public final ChooserTargetServiceConnection connection;
        public final DisplayResolveInfo originalTarget;
        public final List<ChooserTarget> resultTargets;

        public ServiceResultInfo(DisplayResolveInfo ot, List<ChooserTarget> rt, ChooserTargetServiceConnection c) {
            this.originalTarget = ot;
            this.resultTargets = rt;
            this.connection = c;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        long intentReceivedTime = System.currentTimeMillis();
        this.mIsSuccessfullySelected = false;
        Intent intent = getIntent();
        Parcelable targetParcelable = intent.getParcelableExtra(Intent.EXTRA_INTENT);
        boolean z = targetParcelable instanceof Intent;
        String str = TAG;
        StringBuilder stringBuilder;
        if (z) {
            Intent target;
            Intent[] initialIntents;
            Intent target2 = (Intent) targetParcelable;
            if (target2 != null) {
                modifyTargetIntent(target2);
            }
            Parcelable[] targetsParcelable = intent.getParcelableArrayExtra(Intent.EXTRA_ALTERNATE_INTENTS);
            if (targetsParcelable != null) {
                boolean offset = target2 == null;
                Intent[] additionalTargets = targetsParcelable.length;
                if (offset) {
                    additionalTargets -= 1;
                }
                additionalTargets = new Intent[additionalTargets];
                int i = 0;
                while (i < targetsParcelable.length) {
                    if (targetsParcelable[i] instanceof Intent) {
                        Intent additionalTarget = targetsParcelable[i];
                        if (i == 0 && target2 == null) {
                            target2 = additionalTarget;
                            modifyTargetIntent(target2);
                        } else {
                            additionalTargets[offset ? i - 1 : i] = additionalTarget;
                            modifyTargetIntent(additionalTarget);
                        }
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("EXTRA_ALTERNATE_INTENTS array entry #");
                        stringBuilder.append(i);
                        stringBuilder.append(" is not an Intent: ");
                        stringBuilder.append(targetsParcelable[i]);
                        Log.w(str, stringBuilder.toString());
                        finish();
                        super.onCreate(null);
                        return;
                    }
                }
                setAdditionalTargets(additionalTargets);
                target = target2;
            } else {
                target = target2;
            }
            this.mReplacementExtras = intent.getBundleExtra(Intent.EXTRA_REPLACEMENT_EXTRAS);
            CharSequence title = intent.getCharSequenceExtra(Intent.EXTRA_TITLE);
            int defaultTitleRes = title == null ? R.string.chooseActivity : 0;
            Parcelable[] pa = intent.getParcelableArrayExtra(Intent.EXTRA_INITIAL_INTENTS);
            if (pa != null) {
                Intent[] initialIntents2 = new Intent[pa.length];
                int i2 = 0;
                while (i2 < pa.length) {
                    if (pa[i2] instanceof Intent) {
                        Intent in = pa[i2];
                        modifyTargetIntent(in);
                        initialIntents2[i2] = in;
                        i2++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Initial intent #");
                        stringBuilder.append(i2);
                        stringBuilder.append(" not an Intent: ");
                        stringBuilder.append(pa[i2]);
                        Log.w(str, stringBuilder.toString());
                        finish();
                        super.onCreate(null);
                        return;
                    }
                }
                initialIntents = initialIntents2;
            } else {
                initialIntents = null;
            }
            this.mReferrerFillInIntent = new Intent().putExtra(Intent.EXTRA_REFERRER, getReferrer());
            this.mChosenComponentSender = (IntentSender) intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT_INTENT_SENDER);
            this.mRefinementIntentSender = (IntentSender) intent.getParcelableExtra(Intent.EXTRA_CHOOSER_REFINEMENT_INTENT_SENDER);
            setSafeForwardingMode(true);
            pa = intent.getParcelableArrayExtra(Intent.EXTRA_EXCLUDE_COMPONENTS);
            if (pa != null) {
                ComponentName[] names = new ComponentName[pa.length];
                for (int i3 = 0; i3 < pa.length; i3++) {
                    if (!(pa[i3] instanceof ComponentName)) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Filtered component #");
                        stringBuilder2.append(i3);
                        stringBuilder2.append(" not a ComponentName: ");
                        stringBuilder2.append(pa[i3]);
                        Log.w(str, stringBuilder2.toString());
                        names = null;
                        break;
                    }
                    names[i3] = (ComponentName) pa[i3];
                }
                this.mFilteredComponentNames = names;
            }
            Parcelable[] pa2 = intent.getParcelableArrayExtra(Intent.EXTRA_CHOOSER_TARGETS);
            if (pa2 != null) {
                ChooserTarget[] targets = new ChooserTarget[pa2.length];
                for (int i4 = 0; i4 < pa2.length; i4++) {
                    if (!(pa2[i4] instanceof ChooserTarget)) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Chooser target #");
                        stringBuilder3.append(i4);
                        stringBuilder3.append(" not a ChooserTarget: ");
                        stringBuilder3.append(pa2[i4]);
                        Log.w(str, stringBuilder3.toString());
                        targets = null;
                        break;
                    }
                    targets[i4] = (ChooserTarget) pa2[i4];
                }
                this.mCallerChooserTargets = targets;
            }
            this.mPinnedSharedPrefs = getPinnedSharedPrefs(this);
            setRetainInOnStop(intent.getBooleanExtra(EXTRA_PRIVATE_RETAIN_IN_ON_STOP, false));
            super.onCreate(savedInstanceState, target, title, defaultTitleRes, initialIntents, null, false);
            MetricsLogger.action(this, 214);
            this.mChooserShownTime = System.currentTimeMillis();
            MetricsLogger.histogram(null, "system_cost_for_smart_sharing", (int) (this.mChooserShownTime - intentReceivedTime));
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Target is not an intent: ");
        stringBuilder.append(targetParcelable);
        Log.w(str, stringBuilder.toString());
        finish();
        super.onCreate(null);
    }

    static SharedPreferences getPinnedSharedPrefs(Context context) {
        return context.getSharedPreferences(new File(new File(Environment.getDataUserCePackageDirectory(StorageManager.UUID_PRIVATE_INTERNAL, context.getUserId(), context.getPackageName()), "shared_prefs"), "chooser_pin_settings.xml"), 0);
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        RefinementResultReceiver refinementResultReceiver = this.mRefinementResultReceiver;
        if (refinementResultReceiver != null) {
            refinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        unbindRemainingServices();
        this.mChooserHandler.removeMessages(1);
    }

    public Intent getReplacementIntent(ActivityInfo aInfo, Intent defIntent) {
        Intent result = defIntent;
        Bundle replExtras = this.mReplacementExtras;
        if (replExtras != null) {
            replExtras = replExtras.getBundle(aInfo.packageName);
            if (replExtras != null) {
                result = new Intent(defIntent);
                result.putExtras(replExtras);
            }
        }
        if (!aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_PARENT) && !aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE)) {
            return result;
        }
        result = Intent.createChooser(result, getIntent().getCharSequenceExtra(Intent.EXTRA_TITLE));
        result.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, false);
        return result;
    }

    public void onActivityStarted(TargetInfo cti) {
        if (this.mChosenComponentSender != null) {
            Parcelable target = cti.getResolvedComponentName();
            if (target != null) {
                try {
                    this.mChosenComponentSender.sendIntent(this, -1, new Intent().putExtra(Intent.EXTRA_CHOSEN_COMPONENT, target), null, null);
                } catch (SendIntentException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to launch supplied IntentSender to report the chosen component: ");
                    stringBuilder.append(e);
                    Slog.e(TAG, stringBuilder.toString());
                }
            }
        }
    }

    public void onPrepareAdapterView(AbsListView adapterView, ResolveListAdapter adapter) {
        this.mChooserListAdapter = (ChooserListAdapter) adapter;
    }

    public int getLayoutResource() {
        return R.layout.chooser_grid;
    }

    public boolean shouldGetActivityMetadata() {
        return true;
    }

    public boolean shouldAutoLaunchSingleChoice(TargetInfo target) {
        return getIntent().getBooleanExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, super.shouldAutoLaunchSingleChoice(target));
    }

    public void showTargetDetails(ResolveInfo ri) {
        super.showTargetDetails(ri);
    }

    private void modifyTargetIntent(Intent in) {
        String action = in.getAction();
        if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            in.addFlags(134742016);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onTargetSelected(TargetInfo target, boolean alwaysCheck) {
        if (this.mRefinementIntentSender != null) {
            Intent fillIn = new Intent();
            List<Intent> sourceIntents = target.getAllSourceIntents();
            if (!sourceIntents.isEmpty()) {
                fillIn.putExtra(Intent.EXTRA_INTENT, (Parcelable) sourceIntents.get(0));
                if (sourceIntents.size() > 1) {
                    Parcelable[] alts = new Intent[(sourceIntents.size() - 1)];
                    int N = sourceIntents.size();
                    for (int i = 1; i < N; i++) {
                        alts[i - 1] = (Intent) sourceIntents.get(i);
                    }
                    fillIn.putExtra(Intent.EXTRA_ALTERNATE_INTENTS, alts);
                }
                RefinementResultReceiver refinementResultReceiver = this.mRefinementResultReceiver;
                if (refinementResultReceiver != null) {
                    refinementResultReceiver.destroy();
                }
                this.mRefinementResultReceiver = new RefinementResultReceiver(this, target, null);
                fillIn.putExtra(Intent.EXTRA_RESULT_RECEIVER, this.mRefinementResultReceiver);
                try {
                    this.mRefinementIntentSender.sendIntent(this, 0, fillIn, null, null);
                    return false;
                } catch (SendIntentException e) {
                    Log.e(TAG, "Refinement IntentSender failed to send", e);
                }
            }
        }
        updateModelAndChooserCounts(target);
        return super.onTargetSelected(target, alwaysCheck);
    }

    public void startSelected(int which, boolean always, boolean filtered) {
        long selectionCost = System.currentTimeMillis() - this.mChooserShownTime;
        super.startSelected(which, always, filtered);
        ChooserListAdapter chooserListAdapter = this.mChooserListAdapter;
        if (chooserListAdapter != null) {
            int cat = 0;
            int value = which;
            int positionTargetType = chooserListAdapter.getPositionTargetType(which);
            if (positionTargetType == 0) {
                cat = 215;
            } else if (positionTargetType == 1) {
                cat = 216;
                value -= this.mChooserListAdapter.getCallerTargetCount();
            } else if (positionTargetType == 2) {
                cat = 217;
                value -= this.mChooserListAdapter.getCallerTargetCount() + this.mChooserListAdapter.getServiceTargetCount();
            }
            if (cat != 0) {
                MetricsLogger.action((Context) this, cat, value);
            }
            if (this.mIsSuccessfullySelected) {
                MetricsLogger.histogram(null, "user_selection_cost_for_smart_sharing", (int) selectionCost);
                MetricsLogger.histogram(null, "app_position_for_smart_sharing", value);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void queryTargetServices(ChooserListAdapter adapter) {
        ChooserListAdapter chooserListAdapter = adapter;
        String str = TAG;
        String str2 = "android.permission.BIND_CHOOSER_TARGET_SERVICE";
        PackageManager pm = getPackageManager();
        int i = 0;
        int N = adapter.getDisplayResolveInfoCount();
        int targetsToQuery = 0;
        while (i < N) {
            DisplayResolveInfo dri = chooserListAdapter.getDisplayResolveInfo(i);
            if (!(dri.getIsShowMore() || chooserListAdapter.getScore(dri) == 0.0f)) {
                String convertServiceName;
                ActivityInfo ai = dri.getResolveInfo().activityInfo;
                Bundle md = ai.metaData;
                if (md != null) {
                    convertServiceName = convertServiceName(ai.packageName, md.getString(ChooserTargetService.META_DATA_NAME));
                } else {
                    convertServiceName = null;
                }
                String serviceName = convertServiceName;
                if (serviceName != null) {
                    ComponentName serviceComponent = new ComponentName(ai.packageName, serviceName);
                    Intent serviceIntent = new Intent(ChooserTargetService.SERVICE_INTERFACE).setComponent(serviceComponent);
                    try {
                        if (!str2.equals(pm.getServiceInfo(serviceComponent, 0).permission)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("ChooserTargetService ");
                            stringBuilder.append(serviceComponent);
                            stringBuilder.append(" does not require permission ");
                            stringBuilder.append(str2);
                            stringBuilder.append(" - this service will not be queried for ChooserTargets. add android:permission=\"");
                            stringBuilder.append(str2);
                            stringBuilder.append("\" to the <service> tag for ");
                            stringBuilder.append(serviceComponent);
                            stringBuilder.append(" in the manifest.");
                            Log.w(str, stringBuilder.toString());
                        } else if (ChooserActivityInjector.canBindService(this, serviceIntent, UserHandle.myUserId())) {
                            ChooserTargetServiceConnection conn = new ChooserTargetServiceConnection(this, dri);
                            if (bindServiceAsUser(serviceIntent, conn, 5, Process.myUserHandle())) {
                                this.mServiceConnections.add(conn);
                                targetsToQuery++;
                            }
                        } else {
                            continue;
                        }
                    } catch (NameNotFoundException e) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Could not look up service ");
                        stringBuilder2.append(serviceComponent);
                        stringBuilder2.append("; component name not found");
                        Log.e(str, stringBuilder2.toString());
                    }
                }
                if (targetsToQuery >= 5) {
                    break;
                }
            }
            i++;
            chooserListAdapter = adapter;
        }
        if (this.mServiceConnections.isEmpty()) {
            sendVoiceChoicesIfNeeded();
        } else {
            this.mChooserHandler.sendEmptyMessageDelayed(2, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
        }
    }

    private String convertServiceName(String packageName, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) {
            return null;
        }
        String fullName;
        if (serviceName.startsWith(".")) {
            fullName = new StringBuilder();
            fullName.append(packageName);
            fullName.append(serviceName);
            fullName = fullName.toString();
        } else if (serviceName.indexOf(46) >= 0) {
            fullName = serviceName;
        } else {
            fullName = null;
        }
        return fullName;
    }

    /* Access modifiers changed, original: 0000 */
    public void unbindRemainingServices() {
        int N = this.mServiceConnections.size();
        for (int i = 0; i < N; i++) {
            ChooserTargetServiceConnection conn = (ChooserTargetServiceConnection) this.mServiceConnections.get(i);
            unbindService(conn);
            conn.destroy();
        }
        this.mServiceConnections.clear();
        this.mChooserHandler.removeMessages(2);
    }

    public void onSetupVoiceInteraction() {
    }

    /* Access modifiers changed, original: 0000 */
    public void updateModelAndChooserCounts(TargetInfo info) {
        if (info != null) {
            ResolveInfo ri = info.getResolveInfo();
            Intent targetIntent = getTargetIntent();
            if (!(ri == null || ri.activityInfo == null || targetIntent == null || this.mAdapter == null)) {
                this.mAdapter.updateModel(info.getResolvedComponentName());
                this.mAdapter.updateChooserCounts(ri.activityInfo.packageName, getUserId(), targetIntent.getAction());
            }
        }
        this.mIsSuccessfullySelected = true;
    }

    /* Access modifiers changed, original: 0000 */
    public void onRefinementResult(TargetInfo selectedTarget, Intent matchingIntent) {
        RefinementResultReceiver refinementResultReceiver = this.mRefinementResultReceiver;
        if (refinementResultReceiver != null) {
            refinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        String str = TAG;
        if (selectedTarget == null) {
            Log.e(str, "Refinement result intent did not match any known targets; canceling");
        } else if (checkTargetSourceIntent(selectedTarget, matchingIntent)) {
            TargetInfo clonedTarget = selectedTarget.cloneFilledIn(matchingIntent, 0);
            if (super.onTargetSelected(clonedTarget, false)) {
                updateModelAndChooserCounts(clonedTarget);
                finish();
                return;
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onRefinementResult: Selected target ");
            stringBuilder.append(selectedTarget);
            stringBuilder.append(" cannot match refined source intent ");
            stringBuilder.append(matchingIntent);
            Log.e(str, stringBuilder.toString());
        }
        onRefinementCanceled();
    }

    /* Access modifiers changed, original: 0000 */
    public void onRefinementCanceled() {
        RefinementResultReceiver refinementResultReceiver = this.mRefinementResultReceiver;
        if (refinementResultReceiver != null) {
            refinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        finish();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean checkTargetSourceIntent(TargetInfo target, Intent matchingIntent) {
        List<Intent> targetIntents = target.getAllSourceIntents();
        int N = targetIntents.size();
        for (int i = 0; i < N; i++) {
            if (((Intent) targetIntents.get(i)).filterEquals(matchingIntent)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void filterServiceTargets(String packageName, List<ChooserTarget> targets) {
        if (targets != null) {
            PackageManager pm = getPackageManager();
            for (int i = targets.size() - 1; i >= 0; i--) {
                ChooserTarget target = (ChooserTarget) targets.get(i);
                ComponentName targetName = target.getComponentName();
                if (packageName == null || !packageName.equals(targetName.getPackageName())) {
                    boolean remove = false;
                    try {
                        ActivityInfo ai = pm.getActivityInfo(targetName, 0);
                        if (!(ai.exported && ai.permission == null)) {
                            remove = true;
                        }
                    } catch (NameNotFoundException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Target ");
                        stringBuilder.append(target);
                        stringBuilder.append(" returned by ");
                        stringBuilder.append(packageName);
                        stringBuilder.append(" component not found");
                        Log.e(TAG, stringBuilder.toString());
                        remove = true;
                    }
                    if (remove) {
                        targets.remove(i);
                    }
                }
            }
        }
    }

    public ResolveListAdapter createAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
        return new ChooserListAdapter(this, context, payloadIntents, initialIntents, rList, launchedFromUid, filterLastUsed, createListController());
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public ResolverListController createListController() {
        return new ChooserListController(this, this.mPm, getTargetIntent(), getReferrerPackageName(), this.mLaunchedFromUid);
    }
}
