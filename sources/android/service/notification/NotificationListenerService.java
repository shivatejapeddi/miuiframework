package android.service.notification;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.INotificationManager;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.Person;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.security.keystore.KeyProperties;
import android.service.notification.INotificationListener.Stub;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class NotificationListenerService extends Service {
    public static final int HINT_HOST_DISABLE_CALL_EFFECTS = 4;
    public static final int HINT_HOST_DISABLE_EFFECTS = 1;
    public static final int HINT_HOST_DISABLE_NOTIFICATION_EFFECTS = 2;
    public static final int INTERRUPTION_FILTER_ALARMS = 4;
    public static final int INTERRUPTION_FILTER_ALL = 1;
    public static final int INTERRUPTION_FILTER_NONE = 3;
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;
    public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
    public static final int NOTIFICATION_CHANNEL_OR_GROUP_ADDED = 1;
    public static final int NOTIFICATION_CHANNEL_OR_GROUP_DELETED = 3;
    public static final int NOTIFICATION_CHANNEL_OR_GROUP_UPDATED = 2;
    public static final int REASON_APP_CANCEL = 8;
    public static final int REASON_APP_CANCEL_ALL = 9;
    public static final int REASON_CANCEL = 2;
    public static final int REASON_CANCEL_ALL = 3;
    public static final int REASON_CHANNEL_BANNED = 17;
    public static final int REASON_CLICK = 1;
    public static final int REASON_ERROR = 4;
    public static final int REASON_GROUP_OPTIMIZATION = 13;
    public static final int REASON_GROUP_SUMMARY_CANCELED = 12;
    public static final int REASON_LISTENER_CANCEL = 10;
    public static final int REASON_LISTENER_CANCEL_ALL = 11;
    public static final int REASON_PACKAGE_BANNED = 7;
    public static final int REASON_PACKAGE_CHANGED = 5;
    public static final int REASON_PACKAGE_SUSPENDED = 14;
    public static final int REASON_PROFILE_TURNED_OFF = 15;
    public static final int REASON_SNOOZED = 18;
    public static final int REASON_TIMEOUT = 19;
    public static final int REASON_UNAUTOBUNDLED = 16;
    public static final int REASON_USER_STOPPED = 6;
    public static final String SERVICE_INTERFACE = "android.service.notification.NotificationListenerService";
    @Deprecated
    public static final int SUPPRESSED_EFFECT_SCREEN_OFF = 1;
    @Deprecated
    public static final int SUPPRESSED_EFFECT_SCREEN_ON = 2;
    @SystemApi
    public static final int TRIM_FULL = 0;
    @SystemApi
    public static final int TRIM_LIGHT = 1;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final String TAG = getClass().getSimpleName();
    private boolean isConnected = false;
    protected int mCurrentUser;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Handler mHandler;
    private final Object mLock = new Object();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    protected INotificationManager mNoMan;
    @GuardedBy({"mLock"})
    private RankingMap mRankingMap;
    protected Context mSystemContext;
    @UnsupportedAppUsage
    protected NotificationListenerWrapper mWrapper = null;

    protected class NotificationListenerWrapper extends Stub {
        protected NotificationListenerWrapper() {
        }

        public void onNotificationPosted(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                try {
                    NotificationListenerService.this.createLegacyIconExtras(sbn.getNotification());
                    NotificationListenerService.this.maybePopulateRemoteViews(sbn.getNotification());
                    NotificationListenerService.this.maybePopulatePeople(sbn.getNotification());
                } catch (IllegalArgumentException e) {
                    String access$000 = NotificationListenerService.this.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onNotificationPosted: can't rebuild notification from ");
                    stringBuilder.append(sbn.getPackageName());
                    Log.w(access$000, stringBuilder.toString());
                    sbn = null;
                }
                synchronized (NotificationListenerService.this.mLock) {
                    NotificationListenerService.this.applyUpdateLocked(update);
                    if (sbn != null) {
                        SomeArgs args = SomeArgs.obtain();
                        args.arg1 = sbn;
                        args.arg2 = NotificationListenerService.this.mRankingMap;
                        NotificationListenerService.this.mHandler.obtainMessage(1, args).sendToTarget();
                    } else {
                        NotificationListenerService.this.mHandler.obtainMessage(4, NotificationListenerService.this.mRankingMap).sendToTarget();
                    }
                }
            } catch (RemoteException e2) {
                Log.w(NotificationListenerService.this.TAG, "onNotificationPosted: Error receiving StatusBarNotification", e2);
            }
        }

        public void onNotificationRemoved(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update, NotificationStats stats, int reason) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                synchronized (NotificationListenerService.this.mLock) {
                    NotificationListenerService.this.applyUpdateLocked(update);
                    SomeArgs args = SomeArgs.obtain();
                    args.arg1 = sbn;
                    args.arg2 = NotificationListenerService.this.mRankingMap;
                    args.arg3 = Integer.valueOf(reason);
                    args.arg4 = stats;
                    NotificationListenerService.this.mHandler.obtainMessage(2, args).sendToTarget();
                }
            } catch (RemoteException e) {
                Log.w(NotificationListenerService.this.TAG, "onNotificationRemoved: Error receiving StatusBarNotification", e);
            }
        }

        public void onListenerConnected(NotificationRankingUpdate update) {
            synchronized (NotificationListenerService.this.mLock) {
                NotificationListenerService.this.applyUpdateLocked(update);
            }
            NotificationListenerService.this.isConnected = true;
            NotificationListenerService.this.mHandler.obtainMessage(3).sendToTarget();
        }

        public void onNotificationRankingUpdate(NotificationRankingUpdate update) throws RemoteException {
            synchronized (NotificationListenerService.this.mLock) {
                NotificationListenerService.this.applyUpdateLocked(update);
                NotificationListenerService.this.mHandler.obtainMessage(4, NotificationListenerService.this.mRankingMap).sendToTarget();
            }
        }

        public void onListenerHintsChanged(int hints) throws RemoteException {
            NotificationListenerService.this.mHandler.obtainMessage(5, hints, 0).sendToTarget();
        }

        public void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
            NotificationListenerService.this.mHandler.obtainMessage(6, interruptionFilter, 0).sendToTarget();
        }

        public void onNotificationEnqueuedWithChannel(IStatusBarNotificationHolder notificationHolder, NotificationChannel channel) throws RemoteException {
        }

        public void onNotificationsSeen(List<String> list) throws RemoteException {
        }

        public void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder notificationHolder, String snoozeCriterionId) throws RemoteException {
        }

        public void onNotificationExpansionChanged(String key, boolean isUserAction, boolean isExpanded) {
        }

        public void onNotificationDirectReply(String key) {
        }

        public void onSuggestedReplySent(String key, CharSequence reply, int source) {
        }

        public void onActionClicked(String key, Action action, int source) {
        }

        public void onAllowedAdjustmentsChanged() {
        }

        public void onNotificationChannelModification(String pkgName, UserHandle user, NotificationChannel channel, int modificationType) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = pkgName;
            args.arg2 = user;
            args.arg3 = channel;
            args.arg4 = Integer.valueOf(modificationType);
            NotificationListenerService.this.mHandler.obtainMessage(7, args).sendToTarget();
        }

        public void onNotificationChannelGroupModification(String pkgName, UserHandle user, NotificationChannelGroup group, int modificationType) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = pkgName;
            args.arg2 = user;
            args.arg3 = group;
            args.arg4 = Integer.valueOf(modificationType);
            NotificationListenerService.this.mHandler.obtainMessage(8, args).sendToTarget();
        }

        public void onStatusBarIconsBehaviorChanged(boolean hideSilentStatusIcons) {
            NotificationListenerService.this.mHandler.obtainMessage(9, Boolean.valueOf(hideSilentStatusIcons)).sendToTarget();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ChannelOrGroupModificationTypes {
    }

    private final class MyHandler extends Handler {
        public static final int MSG_ON_INTERRUPTION_FILTER_CHANGED = 6;
        public static final int MSG_ON_LISTENER_CONNECTED = 3;
        public static final int MSG_ON_LISTENER_HINTS_CHANGED = 5;
        public static final int MSG_ON_NOTIFICATION_CHANNEL_GROUP_MODIFIED = 8;
        public static final int MSG_ON_NOTIFICATION_CHANNEL_MODIFIED = 7;
        public static final int MSG_ON_NOTIFICATION_POSTED = 1;
        public static final int MSG_ON_NOTIFICATION_RANKING_UPDATE = 4;
        public static final int MSG_ON_NOTIFICATION_REMOVED = 2;
        public static final int MSG_ON_STATUS_BAR_ICON_BEHAVIOR_CHANGED = 9;

        public MyHandler(Looper looper) {
            super(looper, null, false);
        }

        public void handleMessage(Message msg) {
            if (NotificationListenerService.this.isConnected) {
                SomeArgs args;
                StatusBarNotification sbn;
                RankingMap rankingMap;
                switch (msg.what) {
                    case 1:
                        args = (SomeArgs) msg.obj;
                        sbn = (StatusBarNotification) args.arg1;
                        rankingMap = (RankingMap) args.arg2;
                        args.recycle();
                        NotificationListenerService.this.onNotificationPosted(sbn, rankingMap);
                        break;
                    case 2:
                        args = msg.obj;
                        sbn = args.arg1;
                        rankingMap = args.arg2;
                        int reason = ((Integer) args.arg3).intValue();
                        NotificationStats stats = args.arg4;
                        args.recycle();
                        NotificationListenerService.this.onNotificationRemoved(sbn, rankingMap, stats, reason);
                        break;
                    case 3:
                        NotificationListenerService.this.onListenerConnected();
                        break;
                    case 4:
                        NotificationListenerService.this.onNotificationRankingUpdate(msg.obj);
                        break;
                    case 5:
                        NotificationListenerService.this.onListenerHintsChanged(msg.arg1);
                        break;
                    case 6:
                        NotificationListenerService.this.onInterruptionFilterChanged(msg.arg1);
                        break;
                    case 7:
                        args = (SomeArgs) msg.obj;
                        NotificationListenerService.this.onNotificationChannelModified((String) args.arg1, (UserHandle) args.arg2, args.arg3, ((Integer) args.arg4).intValue());
                        break;
                    case 8:
                        args = msg.obj;
                        NotificationListenerService.this.onNotificationChannelGroupModified(args.arg1, args.arg2, args.arg3, ((Integer) args.arg4).intValue());
                        break;
                    case 9:
                        NotificationListenerService.this.onSilentStatusBarIconsVisibilityChanged(((Boolean) msg.obj).booleanValue());
                        break;
                }
            }
        }
    }

    public static class Ranking {
        private static final int PARCEL_VERSION = 2;
        public static final int USER_SENTIMENT_NEGATIVE = -1;
        public static final int USER_SENTIMENT_NEUTRAL = 0;
        public static final int USER_SENTIMENT_POSITIVE = 1;
        public static final int VISIBILITY_NO_OVERRIDE = -1000;
        private boolean mCanBubble;
        private NotificationChannel mChannel;
        private boolean mHidden;
        private int mImportance;
        private CharSequence mImportanceExplanation;
        private boolean mIsAmbient;
        private String mKey;
        private long mLastAudiblyAlertedMs;
        private boolean mMatchesInterruptionFilter;
        private boolean mNoisy;
        private String mOverrideGroupKey;
        private ArrayList<String> mOverridePeople;
        private int mRank = -1;
        private boolean mShowBadge;
        private ArrayList<Action> mSmartActions;
        private ArrayList<CharSequence> mSmartReplies;
        private ArrayList<SnoozeCriterion> mSnoozeCriteria;
        private int mSuppressedVisualEffects;
        private int mUserSentiment = 0;
        private int mVisibilityOverride;

        @Retention(RetentionPolicy.SOURCE)
        public @interface UserSentiment {
        }

        @VisibleForTesting
        public void writeToParcel(Parcel out, int flags) {
            long start = (long) out.dataPosition();
            out.writeInt(2);
            out.writeString(this.mKey);
            out.writeInt(this.mRank);
            out.writeBoolean(this.mIsAmbient);
            out.writeBoolean(this.mMatchesInterruptionFilter);
            out.writeInt(this.mVisibilityOverride);
            out.writeInt(this.mSuppressedVisualEffects);
            out.writeInt(this.mImportance);
            out.writeCharSequence(this.mImportanceExplanation);
            out.writeString(this.mOverrideGroupKey);
            out.writeParcelable(this.mChannel, flags);
            out.writeStringList(this.mOverridePeople);
            out.writeTypedList(this.mSnoozeCriteria, flags);
            out.writeBoolean(this.mShowBadge);
            out.writeInt(this.mUserSentiment);
            out.writeBoolean(this.mHidden);
            out.writeLong(this.mLastAudiblyAlertedMs);
            out.writeBoolean(this.mNoisy);
            out.writeTypedList(this.mSmartActions, flags);
            out.writeCharSequenceList(this.mSmartReplies);
            out.writeBoolean(this.mCanBubble);
        }

        @VisibleForTesting
        public Ranking(Parcel in) {
            ClassLoader cl = getClass().getClassLoader();
            int version = in.readInt();
            if (version == 2) {
                this.mKey = in.readString();
                this.mRank = in.readInt();
                this.mIsAmbient = in.readBoolean();
                this.mMatchesInterruptionFilter = in.readBoolean();
                this.mVisibilityOverride = in.readInt();
                this.mSuppressedVisualEffects = in.readInt();
                this.mImportance = in.readInt();
                this.mImportanceExplanation = in.readCharSequence();
                this.mOverrideGroupKey = in.readString();
                this.mChannel = (NotificationChannel) in.readParcelable(cl);
                this.mOverridePeople = in.createStringArrayList();
                this.mSnoozeCriteria = in.createTypedArrayList(SnoozeCriterion.CREATOR);
                this.mShowBadge = in.readBoolean();
                this.mUserSentiment = in.readInt();
                this.mHidden = in.readBoolean();
                this.mLastAudiblyAlertedMs = in.readLong();
                this.mNoisy = in.readBoolean();
                this.mSmartActions = in.createTypedArrayList(Action.CREATOR);
                this.mSmartReplies = in.readCharSequenceList();
                this.mCanBubble = in.readBoolean();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("malformed Ranking parcel: ");
            stringBuilder.append(in);
            stringBuilder.append(" version ");
            stringBuilder.append(version);
            stringBuilder.append(", expected ");
            stringBuilder.append(2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public String getKey() {
            return this.mKey;
        }

        public int getRank() {
            return this.mRank;
        }

        public boolean isAmbient() {
            return this.mIsAmbient;
        }

        @UnsupportedAppUsage
        public int getVisibilityOverride() {
            return this.mVisibilityOverride;
        }

        public int getSuppressedVisualEffects() {
            return this.mSuppressedVisualEffects;
        }

        public boolean matchesInterruptionFilter() {
            return this.mMatchesInterruptionFilter;
        }

        public int getImportance() {
            return this.mImportance;
        }

        public CharSequence getImportanceExplanation() {
            return this.mImportanceExplanation;
        }

        public String getOverrideGroupKey() {
            return this.mOverrideGroupKey;
        }

        public NotificationChannel getChannel() {
            return this.mChannel;
        }

        public int getUserSentiment() {
            return this.mUserSentiment;
        }

        @SystemApi
        public List<String> getAdditionalPeople() {
            return this.mOverridePeople;
        }

        @SystemApi
        public List<SnoozeCriterion> getSnoozeCriteria() {
            return this.mSnoozeCriteria;
        }

        public List<Action> getSmartActions() {
            return this.mSmartActions;
        }

        public List<CharSequence> getSmartReplies() {
            return this.mSmartReplies;
        }

        public boolean canShowBadge() {
            return this.mShowBadge;
        }

        public boolean isSuspended() {
            return this.mHidden;
        }

        public long getLastAudiblyAlertedMillis() {
            return this.mLastAudiblyAlertedMs;
        }

        public boolean canBubble() {
            return this.mCanBubble;
        }

        public boolean isNoisy() {
            return this.mNoisy;
        }

        @VisibleForTesting
        public void populate(String key, int rank, boolean matchesInterruptionFilter, int visibilityOverride, int suppressedVisualEffects, int importance, CharSequence explanation, String overrideGroupKey, NotificationChannel channel, ArrayList<String> overridePeople, ArrayList<SnoozeCriterion> snoozeCriteria, boolean showBadge, int userSentiment, boolean hidden, long lastAudiblyAlertedMs, boolean noisy, ArrayList<Action> smartActions, ArrayList<CharSequence> smartReplies, boolean canBubble) {
            int i = importance;
            this.mKey = key;
            this.mRank = rank;
            this.mIsAmbient = i < 2;
            this.mMatchesInterruptionFilter = matchesInterruptionFilter;
            this.mVisibilityOverride = visibilityOverride;
            this.mSuppressedVisualEffects = suppressedVisualEffects;
            this.mImportance = i;
            this.mImportanceExplanation = explanation;
            this.mOverrideGroupKey = overrideGroupKey;
            this.mChannel = channel;
            this.mOverridePeople = overridePeople;
            this.mSnoozeCriteria = snoozeCriteria;
            this.mShowBadge = showBadge;
            this.mUserSentiment = userSentiment;
            this.mHidden = hidden;
            this.mLastAudiblyAlertedMs = lastAudiblyAlertedMs;
            this.mNoisy = noisy;
            this.mSmartActions = smartActions;
            this.mSmartReplies = smartReplies;
            this.mCanBubble = canBubble;
        }

        public void populate(Ranking other) {
            Ranking ranking = other;
            String str = ranking.mKey;
            String str2 = str;
            populate(str2, ranking.mRank, ranking.mMatchesInterruptionFilter, ranking.mVisibilityOverride, ranking.mSuppressedVisualEffects, ranking.mImportance, ranking.mImportanceExplanation, ranking.mOverrideGroupKey, ranking.mChannel, ranking.mOverridePeople, ranking.mSnoozeCriteria, ranking.mShowBadge, ranking.mUserSentiment, ranking.mHidden, ranking.mLastAudiblyAlertedMs, ranking.mNoisy, ranking.mSmartActions, ranking.mSmartReplies, ranking.mCanBubble);
        }

        public static String importanceToString(int importance) {
            if (importance == -1000) {
                return "UNSPECIFIED";
            }
            if (importance == 0) {
                return KeyProperties.DIGEST_NONE;
            }
            if (importance == 1) {
                return "MIN";
            }
            if (importance == 2) {
                return "LOW";
            }
            if (importance == 3) {
                return "DEFAULT";
            }
            if (importance == 4 || importance == 5) {
                return "HIGH";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UNKNOWN(");
            stringBuilder.append(String.valueOf(importance));
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        /* JADX WARNING: Missing block: B:51:0x0136, code skipped:
            if (java.util.Objects.equals(java.lang.Boolean.valueOf(r6.mCanBubble), java.lang.Boolean.valueOf(r2.mCanBubble)) != false) goto L_0x013a;
     */
        public boolean equals(java.lang.Object r7) {
            /*
            r6 = this;
            r0 = 1;
            if (r6 != r7) goto L_0x0004;
        L_0x0003:
            return r0;
        L_0x0004:
            r1 = 0;
            if (r7 == 0) goto L_0x013b;
        L_0x0007:
            r2 = r6.getClass();
            r3 = r7.getClass();
            if (r2 == r3) goto L_0x0013;
        L_0x0011:
            goto L_0x013b;
        L_0x0013:
            r2 = r7;
            r2 = (android.service.notification.NotificationListenerService.Ranking) r2;
            r3 = r6.mKey;
            r4 = r2.mKey;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0020:
            r3 = r6.mRank;
            r3 = java.lang.Integer.valueOf(r3);
            r4 = r2.mRank;
            r4 = java.lang.Integer.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0032:
            r3 = r6.mMatchesInterruptionFilter;
            r3 = java.lang.Boolean.valueOf(r3);
            r4 = r2.mMatchesInterruptionFilter;
            r4 = java.lang.Boolean.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0044:
            r3 = r6.mVisibilityOverride;
            r3 = java.lang.Integer.valueOf(r3);
            r4 = r2.mVisibilityOverride;
            r4 = java.lang.Integer.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0056:
            r3 = r6.mSuppressedVisualEffects;
            r3 = java.lang.Integer.valueOf(r3);
            r4 = r2.mSuppressedVisualEffects;
            r4 = java.lang.Integer.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0068:
            r3 = r6.mImportance;
            r3 = java.lang.Integer.valueOf(r3);
            r4 = r2.mImportance;
            r4 = java.lang.Integer.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x007a:
            r3 = r6.mImportanceExplanation;
            r4 = r2.mImportanceExplanation;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0084:
            r3 = r6.mOverrideGroupKey;
            r4 = r2.mOverrideGroupKey;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x008e:
            r3 = r6.mChannel;
            r4 = r2.mChannel;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0098:
            r3 = r6.mOverridePeople;
            r4 = r2.mOverridePeople;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x00a2:
            r3 = r6.mSnoozeCriteria;
            r4 = r2.mSnoozeCriteria;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x00ac:
            r3 = r6.mShowBadge;
            r3 = java.lang.Boolean.valueOf(r3);
            r4 = r2.mShowBadge;
            r4 = java.lang.Boolean.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x00be:
            r3 = r6.mUserSentiment;
            r3 = java.lang.Integer.valueOf(r3);
            r4 = r2.mUserSentiment;
            r4 = java.lang.Integer.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x00d0:
            r3 = r6.mHidden;
            r3 = java.lang.Boolean.valueOf(r3);
            r4 = r2.mHidden;
            r4 = java.lang.Boolean.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x00e2:
            r3 = r6.mLastAudiblyAlertedMs;
            r3 = java.lang.Long.valueOf(r3);
            r4 = r2.mLastAudiblyAlertedMs;
            r4 = java.lang.Long.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x00f4:
            r3 = r6.mNoisy;
            r3 = java.lang.Boolean.valueOf(r3);
            r4 = r2.mNoisy;
            r4 = java.lang.Boolean.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0106:
            r3 = r6.mSmartActions;
            if (r3 != 0) goto L_0x010c;
        L_0x010a:
            r3 = r1;
            goto L_0x0110;
        L_0x010c:
            r3 = r3.size();
        L_0x0110:
            r4 = r2.mSmartActions;
            if (r4 != 0) goto L_0x0116;
        L_0x0114:
            r4 = r1;
            goto L_0x011a;
        L_0x0116:
            r4 = r4.size();
        L_0x011a:
            if (r3 != r4) goto L_0x0139;
        L_0x011c:
            r3 = r6.mSmartReplies;
            r4 = r2.mSmartReplies;
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0126:
            r3 = r6.mCanBubble;
            r3 = java.lang.Boolean.valueOf(r3);
            r4 = r2.mCanBubble;
            r4 = java.lang.Boolean.valueOf(r4);
            r3 = java.util.Objects.equals(r3, r4);
            if (r3 == 0) goto L_0x0139;
        L_0x0138:
            goto L_0x013a;
        L_0x0139:
            r0 = r1;
        L_0x013a:
            return r0;
        L_0x013b:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.service.notification.NotificationListenerService$Ranking.equals(java.lang.Object):boolean");
        }
    }

    public static class RankingMap implements Parcelable {
        public static final Creator<RankingMap> CREATOR = new Creator<RankingMap>() {
            public RankingMap createFromParcel(Parcel source) {
                return new RankingMap(source);
            }

            public RankingMap[] newArray(int size) {
                return new RankingMap[size];
            }
        };
        private ArrayList<String> mOrderedKeys;
        private ArrayMap<String, Ranking> mRankings;

        public RankingMap(Ranking[] rankings) {
            this.mOrderedKeys = new ArrayList();
            this.mRankings = new ArrayMap();
            for (int i = 0; i < rankings.length; i++) {
                String key = rankings[i].getKey();
                this.mOrderedKeys.add(key);
                this.mRankings.put(key, rankings[i]);
            }
        }

        private RankingMap(Parcel in) {
            this.mOrderedKeys = new ArrayList();
            this.mRankings = new ArrayMap();
            ClassLoader cl = getClass().getClassLoader();
            int count = in.readInt();
            this.mOrderedKeys.ensureCapacity(count);
            this.mRankings.ensureCapacity(count);
            for (int i = 0; i < count; i++) {
                Ranking r = new Ranking(in);
                String key = r.getKey();
                this.mOrderedKeys.add(key);
                this.mRankings.put(key, r);
            }
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RankingMap other = (RankingMap) o;
            if (!(this.mOrderedKeys.equals(other.mOrderedKeys) && this.mRankings.equals(other.mRankings))) {
                z = false;
            }
            return z;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            int count = this.mOrderedKeys.size();
            out.writeInt(count);
            for (int i = 0; i < count; i++) {
                ((Ranking) this.mRankings.get(this.mOrderedKeys.get(i))).writeToParcel(out, flags);
            }
        }

        public String[] getOrderedKeys() {
            return (String[]) this.mOrderedKeys.toArray(new String[0]);
        }

        public boolean getRanking(String key, Ranking outRanking) {
            if (!this.mRankings.containsKey(key)) {
                return false;
            }
            outRanking.populate((Ranking) this.mRankings.get(key));
            return true;
        }

        @VisibleForTesting
        public Ranking getRawRankingObject(String key) {
            return (Ranking) this.mRankings.get(key);
        }
    }

    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new MyHandler(getMainLooper());
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        onNotificationPosted(sbn);
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        onNotificationRemoved(sbn);
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, int reason) {
        onNotificationRemoved(sbn, rankingMap);
    }

    @SystemApi
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, NotificationStats stats, int reason) {
        onNotificationRemoved(sbn, rankingMap, reason);
    }

    public void onListenerConnected() {
    }

    public void onListenerDisconnected() {
    }

    public void onNotificationRankingUpdate(RankingMap rankingMap) {
    }

    public void onListenerHintsChanged(int hints) {
    }

    public void onSilentStatusBarIconsVisibilityChanged(boolean hideSilentStatusIcons) {
    }

    public void onNotificationChannelModified(String pkg, UserHandle user, NotificationChannel channel, int modificationType) {
    }

    public void onNotificationChannelGroupModified(String pkg, UserHandle user, NotificationChannelGroup group, int modificationType) {
    }

    public void onInterruptionFilterChanged(int interruptionFilter) {
    }

    /* Access modifiers changed, original: protected|final */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final INotificationManager getNotificationInterface() {
        if (this.mNoMan == null) {
            this.mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        }
        return this.mNoMan;
    }

    @Deprecated
    public final void cancelNotification(String pkg, String tag, int id) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationFromListener(this.mWrapper, pkg, tag, id);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void cancelNotification(String key) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationsFromListener(this.mWrapper, new String[]{key});
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void cancelAllNotifications() {
        cancelNotifications(null);
    }

    public final void cancelNotifications(String[] keys) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationsFromListener(this.mWrapper, keys);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    @SystemApi
    public final void snoozeNotification(String key, String snoozeCriterionId) {
        if (isBound()) {
            try {
                getNotificationInterface().snoozeNotificationUntilContextFromListener(this.mWrapper, key, snoozeCriterionId);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void snoozeNotification(String key, long durationMs) {
        if (isBound()) {
            try {
                getNotificationInterface().snoozeNotificationUntilFromListener(this.mWrapper, key, durationMs);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void setNotificationsShown(String[] keys) {
        if (isBound()) {
            try {
                getNotificationInterface().setNotificationsShownFromListener(this.mWrapper, keys);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void updateNotificationChannel(String pkg, UserHandle user, NotificationChannel channel) {
        if (isBound()) {
            try {
                getNotificationInterface().updateNotificationChannelFromPrivilegedListener(this.mWrapper, pkg, user, channel);
            } catch (RemoteException e) {
                Log.v(this.TAG, "Unable to contact notification manager", e);
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public final List<NotificationChannel> getNotificationChannels(String pkg, UserHandle user) {
        if (!isBound()) {
            return null;
        }
        try {
            return getNotificationInterface().getNotificationChannelsFromPrivilegedListener(this.mWrapper, pkg, user).getList();
        } catch (RemoteException e) {
            Log.v(this.TAG, "Unable to contact notification manager", e);
            throw e.rethrowFromSystemServer();
        }
    }

    public final List<NotificationChannelGroup> getNotificationChannelGroups(String pkg, UserHandle user) {
        if (!isBound()) {
            return null;
        }
        try {
            return getNotificationInterface().getNotificationChannelGroupsFromPrivilegedListener(this.mWrapper, pkg, user).getList();
        } catch (RemoteException e) {
            Log.v(this.TAG, "Unable to contact notification manager", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public final void setOnNotificationPostedTrim(int trim) {
        if (isBound()) {
            try {
                getNotificationInterface().setOnNotificationPostedTrimFromListener(this.mWrapper, trim);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public StatusBarNotification[] getActiveNotifications() {
        StatusBarNotification[] activeNotifications = getActiveNotifications(null, 0);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    public final StatusBarNotification[] getSnoozedNotifications() {
        try {
            return cleanUpNotificationList(getNotificationInterface().getSnoozedNotificationsFromListener(this.mWrapper, 0));
        } catch (RemoteException ex) {
            Log.v(this.TAG, "Unable to contact notification manager", ex);
            return null;
        }
    }

    @SystemApi
    public StatusBarNotification[] getActiveNotifications(int trim) {
        StatusBarNotification[] activeNotifications = getActiveNotifications(null, trim);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        StatusBarNotification[] activeNotifications = getActiveNotifications(keys, 0);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    @SystemApi
    public StatusBarNotification[] getActiveNotifications(String[] keys, int trim) {
        StatusBarNotification[] statusBarNotificationArr = null;
        if (!isBound()) {
            return null;
        }
        try {
            statusBarNotificationArr = cleanUpNotificationList(getNotificationInterface().getActiveNotificationsFromListener(this.mWrapper, keys, trim));
            return statusBarNotificationArr;
        } catch (RemoteException ex) {
            Log.v(this.TAG, "Unable to contact notification manager", ex);
            return statusBarNotificationArr;
        }
    }

    private StatusBarNotification[] cleanUpNotificationList(ParceledListSlice<StatusBarNotification> parceledList) {
        if (parceledList == null || parceledList.getList() == null) {
            return new StatusBarNotification[0];
        }
        List<StatusBarNotification> list = parceledList.getList();
        ArrayList<StatusBarNotification> corruptNotifications = null;
        int N = list.size();
        for (int i = 0; i < N; i++) {
            StatusBarNotification sbn = (StatusBarNotification) list.get(i);
            Notification notification = sbn.getNotification();
            try {
                createLegacyIconExtras(notification);
                maybePopulateRemoteViews(notification);
                maybePopulatePeople(notification);
            } catch (IllegalArgumentException e) {
                if (corruptNotifications == null) {
                    corruptNotifications = new ArrayList(N);
                }
                corruptNotifications.add(sbn);
                String str = this.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get(Active/Snoozed)Notifications: can't rebuild notification from ");
                stringBuilder.append(sbn.getPackageName());
                Log.w(str, stringBuilder.toString());
            }
        }
        if (corruptNotifications != null) {
            list.removeAll(corruptNotifications);
        }
        return (StatusBarNotification[]) list.toArray(new StatusBarNotification[list.size()]);
    }

    public final int getCurrentListenerHints() {
        if (!isBound()) {
            return 0;
        }
        try {
            return getNotificationInterface().getHintsFromListener(this.mWrapper);
        } catch (RemoteException ex) {
            Log.v(this.TAG, "Unable to contact notification manager", ex);
            return 0;
        }
    }

    public final int getCurrentInterruptionFilter() {
        if (!isBound()) {
            return 0;
        }
        try {
            return getNotificationInterface().getInterruptionFilterFromListener(this.mWrapper);
        } catch (RemoteException ex) {
            Log.v(this.TAG, "Unable to contact notification manager", ex);
            return 0;
        }
    }

    public final void clearRequestedListenerHints() {
        if (isBound()) {
            try {
                getNotificationInterface().clearRequestedListenerHints(this.mWrapper);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void requestListenerHints(int hints) {
        if (isBound()) {
            try {
                getNotificationInterface().requestHintsFromListener(this.mWrapper, hints);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void requestInterruptionFilter(int interruptionFilter) {
        if (isBound()) {
            try {
                getNotificationInterface().requestInterruptionFilterFromListener(this.mWrapper, interruptionFilter);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public RankingMap getCurrentRanking() {
        RankingMap rankingMap;
        synchronized (this.mLock) {
            rankingMap = this.mRankingMap;
        }
        return rankingMap;
    }

    public IBinder onBind(Intent intent) {
        if (this.mWrapper == null) {
            this.mWrapper = new NotificationListenerWrapper();
        }
        return this.mWrapper;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public boolean isBound() {
        if (this.mWrapper != null) {
            return true;
        }
        Log.w(this.TAG, "Notification listener service not yet bound.");
        return false;
    }

    public void onDestroy() {
        onListenerDisconnected();
        super.onDestroy();
    }

    @SystemApi
    public void registerAsSystemService(Context context, ComponentName componentName, int currentUser) throws RemoteException {
        if (this.mWrapper == null) {
            this.mWrapper = new NotificationListenerWrapper();
        }
        this.mSystemContext = context;
        INotificationManager noMan = getNotificationInterface();
        this.mHandler = new MyHandler(context.getMainLooper());
        this.mCurrentUser = currentUser;
        noMan.registerListener(this.mWrapper, componentName, currentUser);
    }

    @SystemApi
    public void unregisterAsSystemService() throws RemoteException {
        if (this.mWrapper != null) {
            getNotificationInterface().unregisterListener(this.mWrapper, this.mCurrentUser);
        }
    }

    public static void requestRebind(ComponentName componentName) {
        try {
            INotificationManager.Stub.asInterface(ServiceManager.getService("notification")).requestBindListener(componentName);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public final void requestUnbind() {
        if (this.mWrapper != null) {
            try {
                getNotificationInterface().requestUnbindListener(this.mWrapper);
                this.isConnected = false;
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    public final void createLegacyIconExtras(Notification n) {
        if (getContext().getApplicationInfo().targetSdkVersion < 23) {
            Icon smallIcon = n.getSmallIcon();
            Icon largeIcon = n.getLargeIcon();
            if (smallIcon != null && smallIcon.getType() == 2) {
                n.extras.putInt(Notification.EXTRA_SMALL_ICON, smallIcon.getResId());
                n.icon = smallIcon.getResId();
            }
            if (largeIcon != null) {
                Drawable d = largeIcon.loadDrawable(getContext());
                if (d != null && (d instanceof BitmapDrawable)) {
                    Bitmap largeIconBits = ((BitmapDrawable) d).getBitmap();
                    n.extras.putParcelable(Notification.EXTRA_LARGE_ICON, largeIconBits);
                    n.largeIcon = largeIconBits;
                }
            }
        }
    }

    private void maybePopulateRemoteViews(Notification notification) {
        if (getContext().getApplicationInfo().targetSdkVersion < 24) {
            Builder builder = Builder.recoverBuilder(getContext(), notification);
            RemoteViews content = builder.createContentView();
            RemoteViews big = builder.createBigContentView();
            RemoteViews headsUp = builder.createHeadsUpContentView();
            notification.contentView = content;
            notification.bigContentView = big;
            notification.headsUpContentView = headsUp;
        }
    }

    private void maybePopulatePeople(Notification notification) {
        if (getContext().getApplicationInfo().targetSdkVersion < 28) {
            ArrayList<Person> people = notification.extras.getParcelableArrayList(Notification.EXTRA_PEOPLE_LIST);
            if (people != null && people.isEmpty()) {
                int size = people.size();
                String[] peopleArray = new String[size];
                for (int i = 0; i < size; i++) {
                    peopleArray[i] = ((Person) people.get(i)).resolveToLegacyUri();
                }
                notification.extras.putStringArray(Notification.EXTRA_PEOPLE, peopleArray);
            }
        }
    }

    @GuardedBy({"mLock"})
    public final void applyUpdateLocked(NotificationRankingUpdate update) {
        this.mRankingMap = update.getRankingMap();
    }

    /* Access modifiers changed, original: protected */
    public Context getContext() {
        Context context = this.mSystemContext;
        if (context != null) {
            return context;
        }
        return this;
    }
}
