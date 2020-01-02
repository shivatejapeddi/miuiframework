package android.content;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.IActivityManager;
import android.app.QueuedWork;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.util.TimedRemoteCaller;

public abstract class BroadcastReceiver {
    private boolean mDebugUnregister;
    @UnsupportedAppUsage
    private PendingResult mPendingResult;

    public static class PendingResult {
        private static final int QUEUEDWORK_FINISH_TIMEOUT = 5000;
        public static final int TYPE_COMPONENT = 0;
        public static final int TYPE_REGISTERED = 1;
        public static final int TYPE_UNREGISTERED = 2;
        @UnsupportedAppUsage
        boolean mAbortBroadcast;
        @UnsupportedAppUsage
        boolean mFinished;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        final int mFlags;
        Handler mH;
        boolean mHaveSendFinish = false;
        @UnsupportedAppUsage
        final boolean mInitialStickyHint;
        @UnsupportedAppUsage
        final boolean mOrderedHint;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        int mResultCode;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        String mResultData;
        @UnsupportedAppUsage
        Bundle mResultExtras;
        @UnsupportedAppUsage
        final int mSendingUser;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        final IBinder mToken;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        final int mType;

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public PendingResult(int resultCode, String resultData, Bundle resultExtras, int type, boolean ordered, boolean sticky, IBinder token, int userId, int flags) {
            this.mResultCode = resultCode;
            this.mResultData = resultData;
            this.mResultExtras = resultExtras;
            this.mType = type;
            this.mOrderedHint = ordered;
            this.mInitialStickyHint = sticky;
            this.mToken = token;
            this.mSendingUser = userId;
            this.mFlags = flags;
        }

        public final void setResultCode(int code) {
            checkSynchronousHint();
            this.mResultCode = code;
        }

        public final int getResultCode() {
            return this.mResultCode;
        }

        public final void setResultData(String data) {
            checkSynchronousHint();
            this.mResultData = data;
        }

        public final String getResultData() {
            return this.mResultData;
        }

        public final void setResultExtras(Bundle extras) {
            checkSynchronousHint();
            this.mResultExtras = extras;
        }

        public final Bundle getResultExtras(boolean makeMap) {
            Bundle e = this.mResultExtras;
            if (!makeMap) {
                return e;
            }
            if (e == null) {
                Bundle bundle = new Bundle();
                e = bundle;
                this.mResultExtras = bundle;
            }
            return e;
        }

        public final void setResult(int code, String data, Bundle extras) {
            checkSynchronousHint();
            this.mResultCode = code;
            this.mResultData = data;
            this.mResultExtras = extras;
        }

        public final boolean getAbortBroadcast() {
            return this.mAbortBroadcast;
        }

        public final void abortBroadcast() {
            checkSynchronousHint();
            this.mAbortBroadcast = true;
        }

        public final void clearAbortBroadcast() {
            this.mAbortBroadcast = false;
        }

        public final void finish() {
            int i = this.mType;
            if (i == 0) {
                final IActivityManager mgr = ActivityManager.getService();
                if (QueuedWork.hasPendingWork()) {
                    final Runnable sendFinishRunnable = new Runnable() {
                        public void run() {
                            Slog.i(ActivityThread.TAG, "broadcast wait to finish, took too long 5000ms");
                            synchronized (PendingResult.this) {
                                if (!PendingResult.this.mHaveSendFinish) {
                                    PendingResult.this.mHaveSendFinish = true;
                                    PendingResult.this.sendFinished(mgr);
                                }
                            }
                        }
                    };
                    Handler handler = this.mH;
                    if (handler != null) {
                        handler.postDelayed(sendFinishRunnable, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                    }
                    QueuedWork.queue(new Runnable() {
                        public void run() {
                            if (PendingResult.this.mH != null) {
                                PendingResult.this.mH.removeCallbacks(sendFinishRunnable);
                            }
                            synchronized (PendingResult.this) {
                                if (!PendingResult.this.mHaveSendFinish) {
                                    PendingResult.this.mHaveSendFinish = true;
                                    PendingResult.this.sendFinished(mgr);
                                }
                            }
                        }
                    }, false);
                    return;
                }
                sendFinished(mgr);
            } else if (this.mOrderedHint && i != 2) {
                sendFinished(ActivityManager.getService());
            }
        }

        public void setExtrasClassLoader(ClassLoader cl) {
            Bundle bundle = this.mResultExtras;
            if (bundle != null) {
                bundle.setClassLoader(cl);
            }
        }

        public void sendFinished(IActivityManager am) {
            synchronized (this) {
                if (this.mFinished) {
                    throw new IllegalStateException("Broadcast already finished");
                }
                this.mFinished = true;
                try {
                    if (this.mResultExtras != null) {
                        this.mResultExtras.setAllowFds(false);
                    }
                    if (this.mOrderedHint) {
                        am.finishReceiver(this.mToken, this.mResultCode, this.mResultData, this.mResultExtras, this.mAbortBroadcast, this.mFlags);
                    } else {
                        am.finishReceiver(this.mToken, 0, null, null, false, this.mFlags);
                    }
                } catch (RemoteException e) {
                }
            }
        }

        public int getSendingUserId() {
            return this.mSendingUser;
        }

        /* Access modifiers changed, original: 0000 */
        public void checkSynchronousHint() {
            if (!this.mOrderedHint && !this.mInitialStickyHint) {
                RuntimeException e = new RuntimeException("BroadcastReceiver trying to return result during a non-ordered broadcast");
                e.fillInStackTrace();
                Log.e("BroadcastReceiver", e.getMessage(), e);
            }
        }

        public void setHandler(Handler handler) {
            this.mH = handler;
        }
    }

    public abstract void onReceive(Context context, Intent intent);

    public final PendingResult goAsync() {
        PendingResult res = this.mPendingResult;
        this.mPendingResult = null;
        return res;
    }

    public IBinder peekService(Context myContext, Intent service) {
        IActivityManager am = ActivityManager.getService();
        try {
            service.prepareToLeaveProcess(myContext);
            return am.peekService(service, service.resolveTypeIfNeeded(myContext.getContentResolver()), myContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        }
    }

    public final void setResultCode(int code) {
        checkSynchronousHint();
        this.mPendingResult.mResultCode = code;
    }

    public final int getResultCode() {
        PendingResult pendingResult = this.mPendingResult;
        return pendingResult != null ? pendingResult.mResultCode : 0;
    }

    public final void setResultData(String data) {
        checkSynchronousHint();
        this.mPendingResult.mResultData = data;
    }

    public final String getResultData() {
        PendingResult pendingResult = this.mPendingResult;
        return pendingResult != null ? pendingResult.mResultData : null;
    }

    public final void setResultExtras(Bundle extras) {
        checkSynchronousHint();
        this.mPendingResult.mResultExtras = extras;
    }

    public final Bundle getResultExtras(boolean makeMap) {
        Bundle e = this.mPendingResult;
        if (e == null) {
            return null;
        }
        e = e.mResultExtras;
        if (!makeMap) {
            return e;
        }
        if (e == null) {
            PendingResult pendingResult = this.mPendingResult;
            Bundle bundle = new Bundle();
            e = bundle;
            pendingResult.mResultExtras = bundle;
        }
        return e;
    }

    public final void setResult(int code, String data, Bundle extras) {
        checkSynchronousHint();
        PendingResult pendingResult = this.mPendingResult;
        pendingResult.mResultCode = code;
        pendingResult.mResultData = data;
        pendingResult.mResultExtras = extras;
    }

    public final boolean getAbortBroadcast() {
        PendingResult pendingResult = this.mPendingResult;
        return pendingResult != null ? pendingResult.mAbortBroadcast : false;
    }

    public final void abortBroadcast() {
        checkSynchronousHint();
        this.mPendingResult.mAbortBroadcast = true;
    }

    public final void clearAbortBroadcast() {
        PendingResult pendingResult = this.mPendingResult;
        if (pendingResult != null) {
            pendingResult.mAbortBroadcast = false;
        }
    }

    public final boolean isOrderedBroadcast() {
        PendingResult pendingResult = this.mPendingResult;
        return pendingResult != null ? pendingResult.mOrderedHint : false;
    }

    public final boolean isInitialStickyBroadcast() {
        PendingResult pendingResult = this.mPendingResult;
        return pendingResult != null ? pendingResult.mInitialStickyHint : false;
    }

    public final void setOrderedHint(boolean isOrdered) {
    }

    @UnsupportedAppUsage
    public final void setPendingResult(PendingResult result) {
        this.mPendingResult = result;
    }

    @UnsupportedAppUsage
    public final PendingResult getPendingResult() {
        return this.mPendingResult;
    }

    public int getSendingUserId() {
        return this.mPendingResult.mSendingUser;
    }

    public final void setDebugUnregister(boolean debug) {
        this.mDebugUnregister = debug;
    }

    public final boolean getDebugUnregister() {
        return this.mDebugUnregister;
    }

    /* Access modifiers changed, original: 0000 */
    public void checkSynchronousHint() {
        PendingResult pendingResult = this.mPendingResult;
        if (pendingResult == null) {
            throw new IllegalStateException("Call while result is not pending");
        } else if (!pendingResult.mOrderedHint && !this.mPendingResult.mInitialStickyHint) {
            RuntimeException e = new RuntimeException("BroadcastReceiver trying to return result during a non-ordered broadcast");
            e.fillInStackTrace();
            Log.e("BroadcastReceiver", e.getMessage(), e);
        }
    }
}
