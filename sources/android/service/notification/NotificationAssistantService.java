package android.service.notification;

import android.annotation.SystemApi;
import android.app.Notification.Action;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.service.notification.NotificationListenerService.RankingMap;
import android.util.Log;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@SystemApi
public abstract class NotificationAssistantService extends NotificationListenerService {
    public static final String SERVICE_INTERFACE = "android.service.notification.NotificationAssistantService";
    public static final int SOURCE_FROM_APP = 0;
    public static final int SOURCE_FROM_ASSISTANT = 1;
    private static final String TAG = "NotificationAssistants";
    protected Handler mHandler;

    private final class MyHandler extends Handler {
        public static final int MSG_ON_ACTION_INVOKED = 7;
        public static final int MSG_ON_ALLOWED_ADJUSTMENTS_CHANGED = 8;
        public static final int MSG_ON_NOTIFICATIONS_SEEN = 3;
        public static final int MSG_ON_NOTIFICATION_DIRECT_REPLY_SENT = 5;
        public static final int MSG_ON_NOTIFICATION_ENQUEUED = 1;
        public static final int MSG_ON_NOTIFICATION_EXPANSION_CHANGED = 4;
        public static final int MSG_ON_NOTIFICATION_SNOOZED = 2;
        public static final int MSG_ON_SUGGESTED_REPLY_SENT = 6;

        public MyHandler(Looper looper) {
            super(looper, null, false);
        }

        public void handleMessage(Message msg) {
            SomeArgs args;
            StatusBarNotification sbn;
            String key;
            int source;
            switch (msg.what) {
                case 1:
                    args = (SomeArgs) msg.obj;
                    sbn = (StatusBarNotification) args.arg1;
                    NotificationChannel channel = args.arg2;
                    args.recycle();
                    Adjustment adjustment = NotificationAssistantService.this.onNotificationEnqueued(sbn, channel);
                    NotificationAssistantService.this.setAdjustmentIssuer(adjustment);
                    if (adjustment != null) {
                        boolean isBound = NotificationAssistantService.this.isBound();
                        String str = NotificationAssistantService.TAG;
                        if (isBound) {
                            try {
                                NotificationAssistantService.this.getNotificationInterface().applyEnqueuedAdjustmentFromAssistant(NotificationAssistantService.this.mWrapper, adjustment);
                                break;
                            } catch (RemoteException ex) {
                                Log.v(str, "Unable to contact notification manager", ex);
                                throw ex.rethrowFromSystemServer();
                            } catch (SecurityException e) {
                                Log.w(str, "Enqueue adjustment failed; no longer connected", e);
                                break;
                            }
                        }
                        Log.w(str, "MSG_ON_NOTIFICATION_ENQUEUED: service not bound, skip.");
                        return;
                    }
                    break;
                case 2:
                    args = (SomeArgs) msg.obj;
                    sbn = args.arg1;
                    String snoozeCriterionId = args.arg2;
                    args.recycle();
                    NotificationAssistantService.this.onNotificationSnoozedUntilContext(sbn, snoozeCriterionId);
                    break;
                case 3:
                    args = (SomeArgs) msg.obj;
                    List<String> keys = args.arg1;
                    args.recycle();
                    NotificationAssistantService.this.onNotificationsSeen(keys);
                    break;
                case 4:
                    args = (SomeArgs) msg.obj;
                    key = (String) args.arg1;
                    boolean isExpanded = false;
                    boolean isUserAction = args.argi1 == 1;
                    if (args.argi2 == 1) {
                        isExpanded = true;
                    }
                    args.recycle();
                    NotificationAssistantService.this.onNotificationExpansionChanged(key, isUserAction, isExpanded);
                    break;
                case 5:
                    args = (SomeArgs) msg.obj;
                    key = (String) args.arg1;
                    args.recycle();
                    NotificationAssistantService.this.onNotificationDirectReplied(key);
                    break;
                case 6:
                    args = (SomeArgs) msg.obj;
                    key = (String) args.arg1;
                    CharSequence reply = args.arg2;
                    source = args.argi2;
                    args.recycle();
                    NotificationAssistantService.this.onSuggestedReplySent(key, reply, source);
                    break;
                case 7:
                    args = msg.obj;
                    key = args.arg1;
                    Action action = args.arg2;
                    source = args.argi2;
                    args.recycle();
                    NotificationAssistantService.this.onActionInvoked(key, action, source);
                    break;
                case 8:
                    NotificationAssistantService.this.onAllowedAdjustmentsChanged();
                    break;
            }
        }
    }

    private class NotificationAssistantServiceWrapper extends NotificationListenerWrapper {
        private NotificationAssistantServiceWrapper() {
            super();
        }

        public void onNotificationEnqueuedWithChannel(IStatusBarNotificationHolder sbnHolder, NotificationChannel channel) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = sbn;
                args.arg2 = channel;
                NotificationAssistantService.this.mHandler.obtainMessage(1, args).sendToTarget();
            } catch (RemoteException e) {
                Log.w(NotificationAssistantService.TAG, "onNotificationEnqueued: Error receiving StatusBarNotification", e);
            }
        }

        public void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder sbnHolder, String snoozeCriterionId) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = sbn;
                args.arg2 = snoozeCriterionId;
                NotificationAssistantService.this.mHandler.obtainMessage(2, args).sendToTarget();
            } catch (RemoteException e) {
                Log.w(NotificationAssistantService.TAG, "onNotificationSnoozed: Error receiving StatusBarNotification", e);
            }
        }

        public void onNotificationsSeen(List<String> keys) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = keys;
            NotificationAssistantService.this.mHandler.obtainMessage(3, args).sendToTarget();
        }

        public void onNotificationExpansionChanged(String key, boolean isUserAction, boolean isExpanded) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = key;
            args.argi1 = isUserAction;
            args.argi2 = isExpanded;
            NotificationAssistantService.this.mHandler.obtainMessage(4, args).sendToTarget();
        }

        public void onNotificationDirectReply(String key) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = key;
            NotificationAssistantService.this.mHandler.obtainMessage(5, args).sendToTarget();
        }

        public void onSuggestedReplySent(String key, CharSequence reply, int source) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = key;
            args.arg2 = reply;
            args.argi2 = source;
            NotificationAssistantService.this.mHandler.obtainMessage(6, args).sendToTarget();
        }

        public void onActionClicked(String key, Action action, int source) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = key;
            args.arg2 = action;
            args.argi2 = source;
            NotificationAssistantService.this.mHandler.obtainMessage(7, args).sendToTarget();
        }

        public void onAllowedAdjustmentsChanged() {
            NotificationAssistantService.this.mHandler.obtainMessage(8).sendToTarget();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
    }

    public abstract Adjustment onNotificationEnqueued(StatusBarNotification statusBarNotification);

    public abstract void onNotificationSnoozedUntilContext(StatusBarNotification statusBarNotification, String str);

    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new MyHandler(getContext().getMainLooper());
    }

    public final IBinder onBind(Intent intent) {
        if (this.mWrapper == null) {
            this.mWrapper = new NotificationAssistantServiceWrapper();
        }
        return this.mWrapper;
    }

    public Adjustment onNotificationEnqueued(StatusBarNotification sbn, NotificationChannel channel) {
        return onNotificationEnqueued(sbn);
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, NotificationStats stats, int reason) {
        onNotificationRemoved(sbn, rankingMap, reason);
    }

    public void onNotificationsSeen(List<String> list) {
    }

    public void onNotificationExpansionChanged(String key, boolean isUserAction, boolean isExpanded) {
    }

    public void onNotificationDirectReplied(String key) {
    }

    public void onSuggestedReplySent(String key, CharSequence reply, int source) {
    }

    public void onActionInvoked(String key, Action action, int source) {
    }

    public void onAllowedAdjustmentsChanged() {
    }

    public final void adjustNotification(Adjustment adjustment) {
        if (isBound()) {
            try {
                setAdjustmentIssuer(adjustment);
                getNotificationInterface().applyEnqueuedAdjustmentFromAssistant(this.mWrapper, adjustment);
            } catch (RemoteException ex) {
                Log.v(TAG, "Unable to contact notification manager", ex);
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    public final void adjustNotifications(List<Adjustment> adjustments) {
        if (isBound()) {
            try {
                for (Adjustment adjustment : adjustments) {
                    setAdjustmentIssuer(adjustment);
                }
                getNotificationInterface().applyAdjustmentsFromAssistant(this.mWrapper, adjustments);
            } catch (RemoteException ex) {
                Log.v(TAG, "Unable to contact notification manager", ex);
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    public final void unsnoozeNotification(String key) {
        if (isBound()) {
            try {
                getNotificationInterface().unsnoozeNotificationFromAssistant(this.mWrapper, key);
            } catch (RemoteException ex) {
                Log.v(TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    private void setAdjustmentIssuer(Adjustment adjustment) {
        if (adjustment != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getOpPackageName());
            stringBuilder.append("/");
            stringBuilder.append(getClass().getName());
            adjustment.setIssuer(stringBuilder.toString());
        }
    }
}
