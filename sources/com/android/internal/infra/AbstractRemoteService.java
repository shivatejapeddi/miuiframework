package com.android.internal.infra;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class AbstractRemoteService<S extends AbstractRemoteService<S, I>, I extends IInterface> implements DeathRecipient {
    protected static final int LAST_PRIVATE_MSG = 2;
    private static final int MSG_BIND = 1;
    protected static final int MSG_UNBIND = 2;
    public static final long PERMANENT_BOUND_TIMEOUT_MS = 0;
    private boolean mBinding;
    private final int mBindingFlags;
    private boolean mCompleted;
    protected final ComponentName mComponentName;
    private final Context mContext;
    private boolean mDestroyed;
    protected final Handler mHandler;
    private final Intent mIntent;
    private long mNextUnbind;
    protected I mService;
    private final ServiceConnection mServiceConnection = new RemoteServiceConnection();
    private boolean mServiceDied;
    protected final String mTag = getClass().getSimpleName();
    private final ArrayList<BasePendingRequest<S, I>> mUnfinishedRequests = new ArrayList();
    private final int mUserId;
    public final boolean mVerbose;
    private final VultureCallback<S> mVultureCallback;

    public interface AsyncRequest<I extends IInterface> {
        void run(I i) throws RemoteException;
    }

    public static abstract class BasePendingRequest<S extends AbstractRemoteService<S, I>, I extends IInterface> implements Runnable {
        @GuardedBy({"mLock"})
        boolean mCancelled;
        @GuardedBy({"mLock"})
        boolean mCompleted;
        protected final Object mLock = new Object();
        protected final String mTag = getClass().getSimpleName();
        final WeakReference<S> mWeakService;

        BasePendingRequest(S service) {
            this.mWeakService = new WeakReference(service);
        }

        /* Access modifiers changed, original: protected|final */
        public final S getService() {
            return (AbstractRemoteService) this.mWeakService.get();
        }

        /* Access modifiers changed, original: protected|final */
        /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r0 = (com.android.internal.infra.AbstractRemoteService) r2.mWeakService.get();
     */
        /* JADX WARNING: Missing block: B:11:0x0018, code skipped:
            if (r0 == null) goto L_0x001d;
     */
        /* JADX WARNING: Missing block: B:12:0x001a, code skipped:
            r0.finishRequest(r2);
     */
        /* JADX WARNING: Missing block: B:13:0x001d, code skipped:
            onFinished();
     */
        /* JADX WARNING: Missing block: B:14:0x0020, code skipped:
            return true;
     */
        public final boolean finish() {
            /*
            r2 = this;
            r0 = r2.mLock;
            monitor-enter(r0);
            r1 = r2.mCompleted;	 Catch:{ all -> 0x0024 }
            if (r1 != 0) goto L_0x0021;
        L_0x0007:
            r1 = r2.mCancelled;	 Catch:{ all -> 0x0024 }
            if (r1 == 0) goto L_0x000c;
        L_0x000b:
            goto L_0x0021;
        L_0x000c:
            r1 = 1;
            r2.mCompleted = r1;	 Catch:{ all -> 0x0024 }
            monitor-exit(r0);	 Catch:{ all -> 0x0024 }
            r0 = r2.mWeakService;
            r0 = r0.get();
            r0 = (com.android.internal.infra.AbstractRemoteService) r0;
            if (r0 == 0) goto L_0x001d;
        L_0x001a:
            r0.finishRequest(r2);
        L_0x001d:
            r2.onFinished();
            return r1;
        L_0x0021:
            r1 = 0;
            monitor-exit(r0);	 Catch:{ all -> 0x0024 }
            return r1;
        L_0x0024:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0024 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.infra.AbstractRemoteService$BasePendingRequest.finish():boolean");
        }

        /* Access modifiers changed, original: 0000 */
        public void onFinished() {
        }

        /* Access modifiers changed, original: protected */
        public void onFailed() {
        }

        /* Access modifiers changed, original: protected|final */
        @GuardedBy({"mLock"})
        public final boolean isCancelledLocked() {
            return this.mCancelled;
        }

        public boolean cancel() {
            synchronized (this.mLock) {
                if (!this.mCancelled) {
                    if (!this.mCompleted) {
                        this.mCancelled = true;
                        onCancel();
                        return true;
                    }
                }
                return false;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void onCancel() {
        }

        /* Access modifiers changed, original: protected */
        public boolean isFinal() {
            return false;
        }

        /* Access modifiers changed, original: protected */
        public boolean isRequestCompleted() {
            boolean z;
            synchronized (this.mLock) {
                z = this.mCompleted;
            }
            return z;
        }
    }

    private static final class MyAsyncPendingRequest<S extends AbstractRemoteService<S, I>, I extends IInterface> extends BasePendingRequest<S, I> {
        private static final String TAG = MyAsyncPendingRequest.class.getSimpleName();
        private final AsyncRequest<I> mRequest;

        protected MyAsyncPendingRequest(S service, AsyncRequest<I> request) {
            super(service);
            this.mRequest = request;
        }

        public void run() {
            S remoteService = getService();
            if (remoteService != null) {
                try {
                    this.mRequest.run(remoteService.mService);
                } catch (RemoteException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("exception handling async request (");
                    stringBuilder.append(this);
                    stringBuilder.append("): ");
                    stringBuilder.append(e);
                    Slog.w(str, stringBuilder.toString());
                } catch (Throwable th) {
                    finish();
                }
                finish();
            }
        }
    }

    public static abstract class PendingRequest<S extends AbstractRemoteService<S, I>, I extends IInterface> extends BasePendingRequest<S, I> {
        private final Handler mServiceHandler;
        protected final Runnable mTimeoutTrigger;

        public abstract void onTimeout(S s);

        protected PendingRequest(S service) {
            super(service);
            this.mServiceHandler = service.mHandler;
            this.mTimeoutTrigger = new -$$Lambda$AbstractRemoteService$PendingRequest$IBoaBGXZQEXJr69u3aJF-LCJ42Y(this, service);
            this.mServiceHandler.postAtTime(this.mTimeoutTrigger, SystemClock.uptimeMillis() + service.getRemoteRequestMillis());
        }

        /* JADX WARNING: Missing block: B:9:0x000d, code skipped:
            r0 = (com.android.internal.infra.AbstractRemoteService) r5.mWeakService.get();
     */
        /* JADX WARNING: Missing block: B:10:0x0015, code skipped:
            if (r0 == null) goto L_0x003e;
     */
        /* JADX WARNING: Missing block: B:11:0x0017, code skipped:
            r1 = r5.mTag;
            r2 = new java.lang.StringBuilder();
            r2.append("timed out after ");
            r2.append(r6.getRemoteRequestMillis());
            r2.append(" ms");
            android.util.Slog.w(r1, r2.toString());
            r0.finishRequest(r5);
            onTimeout(r0);
     */
        /* JADX WARNING: Missing block: B:12:0x003e, code skipped:
            android.util.Slog.w(r5.mTag, "timed out (no service)");
     */
        /* JADX WARNING: Missing block: B:13:0x0046, code skipped:
            return;
     */
        public /* synthetic */ void lambda$new$0$AbstractRemoteService$PendingRequest(com.android.internal.infra.AbstractRemoteService r6) {
            /*
            r5 = this;
            r0 = r5.mLock;
            monitor-enter(r0);
            r1 = r5.mCancelled;	 Catch:{ all -> 0x0047 }
            if (r1 == 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            return;
        L_0x0009:
            r1 = 1;
            r5.mCompleted = r1;	 Catch:{ all -> 0x0047 }
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            r0 = r5.mWeakService;
            r0 = r0.get();
            r0 = (com.android.internal.infra.AbstractRemoteService) r0;
            if (r0 == 0) goto L_0x003e;
        L_0x0017:
            r1 = r5.mTag;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "timed out after ";
            r2.append(r3);
            r3 = r6.getRemoteRequestMillis();
            r2.append(r3);
            r3 = " ms";
            r2.append(r3);
            r2 = r2.toString();
            android.util.Slog.w(r1, r2);
            r0.finishRequest(r5);
            r5.onTimeout(r0);
            goto L_0x0046;
        L_0x003e:
            r1 = r5.mTag;
            r2 = "timed out (no service)";
            android.util.Slog.w(r1, r2);
        L_0x0046:
            return;
        L_0x0047:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.infra.AbstractRemoteService$PendingRequest.lambda$new$0$AbstractRemoteService$PendingRequest(com.android.internal.infra.AbstractRemoteService):void");
        }

        /* Access modifiers changed, original: final */
        public final void onFinished() {
            this.mServiceHandler.removeCallbacks(this.mTimeoutTrigger);
        }

        /* Access modifiers changed, original: final */
        public final void onCancel() {
            this.mServiceHandler.removeCallbacks(this.mTimeoutTrigger);
        }
    }

    private class RemoteServiceConnection implements ServiceConnection {
        private RemoteServiceConnection() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (AbstractRemoteService.this.mVerbose) {
                Slog.v(AbstractRemoteService.this.mTag, "onServiceConnected()");
            }
            if (AbstractRemoteService.this.mDestroyed || !AbstractRemoteService.this.mBinding) {
                Slog.wtf(AbstractRemoteService.this.mTag, "onServiceConnected() was dispatched after unbindService.");
                return;
            }
            AbstractRemoteService.this.mBinding = false;
            try {
                service.linkToDeath(AbstractRemoteService.this, 0);
                AbstractRemoteService abstractRemoteService = AbstractRemoteService.this;
                abstractRemoteService.mService = abstractRemoteService.getServiceInterface(service);
                AbstractRemoteService.this.handleOnConnectedStateChangedInternal(true);
                AbstractRemoteService.this.mServiceDied = false;
            } catch (RemoteException e) {
                AbstractRemoteService.this.handleBinderDied();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            if (AbstractRemoteService.this.mVerbose) {
                Slog.v(AbstractRemoteService.this.mTag, "onServiceDisconnected()");
            }
            AbstractRemoteService.this.mBinding = true;
            AbstractRemoteService.this.mService = null;
        }

        public void onBindingDied(ComponentName name) {
            if (AbstractRemoteService.this.mVerbose) {
                Slog.v(AbstractRemoteService.this.mTag, "onBindingDied()");
            }
            AbstractRemoteService.this.scheduleUnbind(false);
        }
    }

    public interface VultureCallback<T> {
        void onServiceDied(T t);
    }

    public abstract I getServiceInterface(IBinder iBinder);

    public abstract long getTimeoutIdleBindMillis();

    public abstract void handleBindFailure();

    public abstract void handleOnDestroy();

    public abstract void handlePendingRequestWhileUnBound(BasePendingRequest<S, I> basePendingRequest);

    public abstract void handlePendingRequests();

    AbstractRemoteService(Context context, String serviceInterface, ComponentName componentName, int userId, VultureCallback<S> callback, Handler handler, int bindingFlags, boolean verbose) {
        this.mContext = context;
        this.mVultureCallback = callback;
        this.mVerbose = verbose;
        this.mComponentName = componentName;
        this.mIntent = new Intent(serviceInterface).setComponent(this.mComponentName);
        this.mUserId = userId;
        this.mHandler = new Handler(handler.getLooper());
        this.mBindingFlags = bindingFlags;
    }

    public final void destroy() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AbstractRemoteService$9IBVTCLLZgndvH7fu1P14PW1_1o.INSTANCE, this));
    }

    public final boolean isDestroyed() {
        return this.mDestroyed;
    }

    public final ComponentName getComponentName() {
        return this.mComponentName;
    }

    private void handleOnConnectedStateChangedInternal(boolean connected) {
        handleOnConnectedStateChanged(connected);
        if (connected) {
            handlePendingRequests();
        }
    }

    /* Access modifiers changed, original: protected */
    public void handleOnConnectedStateChanged(boolean state) {
    }

    /* Access modifiers changed, original: protected */
    public long getRemoteRequestMillis() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("not implemented by ");
        stringBuilder.append(getClass());
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public final I getServiceInterface() {
        return this.mService;
    }

    private void handleDestroy() {
        if (!checkIfDestroyed()) {
            handleOnDestroy();
            handleEnsureUnbound();
            this.mDestroyed = true;
        }
    }

    public void binderDied() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AbstractRemoteService$ocrHd68Md9x6FfAzVQ6w8MAjFqY.INSTANCE, this));
    }

    private void handleBinderDied() {
        if (!checkIfDestroyed()) {
            IInterface iInterface = this.mService;
            if (iInterface != null) {
                iInterface.asBinder().unlinkToDeath(this, 0);
            }
            this.mService = null;
            this.mServiceDied = true;
            cancelScheduledUnbind();
            this.mVultureCallback.onServiceDied(this);
            handleBindFailure();
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        String tab = "  ";
        pw.append(prefix).append("service:").println();
        pw.append(prefix).append(tab).append("userId=").append(String.valueOf(this.mUserId)).println();
        pw.append(prefix).append(tab).append("componentName=").append(this.mComponentName.flattenToString()).println();
        pw.append(prefix).append(tab).append("destroyed=").append(String.valueOf(this.mDestroyed)).println();
        pw.append(prefix).append(tab).append("numUnfinishedRequests=").append(String.valueOf(this.mUnfinishedRequests.size())).println();
        boolean bound = handleIsBound();
        pw.append(prefix).append(tab).append("bound=").append(String.valueOf(bound));
        long idleTimeout = getTimeoutIdleBindMillis();
        if (bound) {
            if (idleTimeout > 0) {
                pw.append(" (unbind in : ");
                TimeUtils.formatDuration(this.mNextUnbind - SystemClock.elapsedRealtime(), pw);
                pw.append(")");
            } else {
                pw.append(" (permanently bound)");
            }
        }
        pw.println();
        pw.append(prefix).append("mBindingFlags=").println(this.mBindingFlags);
        String str = "s\n";
        pw.append(prefix).append("idleTimeout=").append(Long.toString(idleTimeout / 1000)).append(str);
        pw.append(prefix).append("requestTimeout=");
        try {
            pw.append(Long.toString(getRemoteRequestMillis() / 1000)).append(str);
        } catch (UnsupportedOperationException e) {
            pw.append("not supported\n");
        }
        pw.println();
    }

    /* Access modifiers changed, original: protected */
    public void scheduleRequest(BasePendingRequest<S, I> pendingRequest) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$7-CJJfrUZBVuXZyYFEWBNh8Mky8.INSTANCE, this, pendingRequest));
    }

    /* Access modifiers changed, original: 0000 */
    public void finishRequest(BasePendingRequest<S, I> finshedRequest) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AbstractRemoteService$6FcEKfZ-7TXLg6dcCU8EMuMNAy4.INSTANCE, this, finshedRequest));
    }

    private void handleFinishRequest(BasePendingRequest<S, I> finshedRequest) {
        this.mUnfinishedRequests.remove(finshedRequest);
        if (this.mUnfinishedRequests.isEmpty()) {
            scheduleUnbind();
        }
    }

    /* Access modifiers changed, original: protected */
    public void scheduleAsyncRequest(AsyncRequest<I> request) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$EbzSql2RHkXox5Myj8A-7kLC4_A.INSTANCE, this, new MyAsyncPendingRequest(this, request)));
    }

    private void cancelScheduledUnbind() {
        this.mHandler.removeMessages(2);
    }

    /* Access modifiers changed, original: protected */
    public void scheduleBind() {
        if (this.mHandler.hasMessages(1)) {
            if (this.mVerbose) {
                Slog.v(this.mTag, "scheduleBind(): already scheduled");
            }
            return;
        }
        this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AbstractRemoteService$YSUzqqi1Pbrg2dlwMGMtKWbGXck.INSTANCE, this).setWhat(1));
    }

    /* Access modifiers changed, original: protected */
    public void scheduleUnbind() {
        scheduleUnbind(true);
    }

    private void scheduleUnbind(boolean delay) {
        long unbindDelay = getTimeoutIdleBindMillis();
        String str;
        StringBuilder stringBuilder;
        if (unbindDelay <= 0) {
            if (this.mVerbose) {
                str = this.mTag;
                stringBuilder = new StringBuilder();
                stringBuilder.append("not scheduling unbind when value is ");
                stringBuilder.append(unbindDelay);
                Slog.v(str, stringBuilder.toString());
            }
            return;
        }
        if (!delay) {
            unbindDelay = 0;
        }
        cancelScheduledUnbind();
        this.mNextUnbind = SystemClock.elapsedRealtime() + unbindDelay;
        if (this.mVerbose) {
            str = this.mTag;
            stringBuilder = new StringBuilder();
            stringBuilder.append("unbinding in ");
            stringBuilder.append(unbindDelay);
            stringBuilder.append("ms: ");
            stringBuilder.append(this.mNextUnbind);
            Slog.v(str, stringBuilder.toString());
        }
        this.mHandler.sendMessageDelayed(PooledLambda.obtainMessage(-$$Lambda$hAi6iX1ESDxtGWb1kxE0l5KZG8Q.INSTANCE, this).setWhat(2), unbindDelay);
    }

    /* Access modifiers changed, original: protected */
    public void handleUnbind() {
        if (!checkIfDestroyed()) {
            handleEnsureUnbound();
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void handlePendingRequest(BasePendingRequest<S, I> pendingRequest) {
        if (!checkIfDestroyed() && !this.mCompleted) {
            String str;
            StringBuilder stringBuilder;
            if (handleIsBound()) {
                if (this.mVerbose) {
                    str = this.mTag;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("handlePendingRequest(): ");
                    stringBuilder.append(pendingRequest);
                    Slog.v(str, stringBuilder.toString());
                }
                this.mUnfinishedRequests.add(pendingRequest);
                cancelScheduledUnbind();
                pendingRequest.run();
                if (pendingRequest.isFinal()) {
                    this.mCompleted = true;
                }
            } else {
                if (this.mVerbose) {
                    str = this.mTag;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("handlePendingRequest(): queuing ");
                    stringBuilder.append(pendingRequest);
                    Slog.v(str, stringBuilder.toString());
                }
                handlePendingRequestWhileUnBound(pendingRequest);
                handleEnsureBound();
            }
        }
    }

    private boolean handleIsBound() {
        return this.mService != null;
    }

    private void handleEnsureBound() {
        if (!handleIsBound() && !this.mBinding) {
            if (this.mVerbose) {
                Slog.v(this.mTag, "ensureBound()");
            }
            this.mBinding = true;
            int flags = 67108865 | this.mBindingFlags;
            if (!this.mContext.bindServiceAsUser(this.mIntent, this.mServiceConnection, flags, this.mHandler, new UserHandle(this.mUserId))) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("could not bind to ");
                stringBuilder.append(this.mIntent);
                stringBuilder.append(" using flags ");
                stringBuilder.append(flags);
                Slog.w(str, stringBuilder.toString());
                this.mBinding = false;
                if (!this.mServiceDied) {
                    handleBinderDied();
                }
            }
        }
    }

    private void handleEnsureUnbound() {
        if (handleIsBound() || this.mBinding) {
            if (this.mVerbose) {
                Slog.v(this.mTag, "ensureUnbound()");
            }
            this.mBinding = false;
            if (handleIsBound()) {
                handleOnConnectedStateChangedInternal(false);
                IInterface iInterface = this.mService;
                if (iInterface != null) {
                    iInterface.asBinder().unlinkToDeath(this, 0);
                    this.mService = null;
                }
            }
            this.mNextUnbind = 0;
            this.mContext.unbindService(this.mServiceConnection);
        }
    }

    private boolean checkIfDestroyed() {
        if (this.mDestroyed && this.mVerbose) {
            String str = this.mTag;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Not handling operation as service for ");
            stringBuilder.append(this.mComponentName);
            stringBuilder.append(" is already destroyed");
            Slog.v(str, stringBuilder.toString());
        }
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append("[");
        stringBuilder.append(this.mComponentName);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(System.identityHashCode(this));
        stringBuilder.append(this.mService != null ? " (bound)" : " (unbound)");
        stringBuilder.append(this.mDestroyed ? " (destroyed)" : "");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
