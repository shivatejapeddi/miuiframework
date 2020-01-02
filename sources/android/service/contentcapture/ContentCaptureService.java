package android.service.contentcapture;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.util.SparseIntArray;
import android.util.StatsLogInternal;
import android.view.contentcapture.ContentCaptureCondition;
import android.view.contentcapture.ContentCaptureContext;
import android.view.contentcapture.ContentCaptureEvent;
import android.view.contentcapture.ContentCaptureHelper;
import android.view.contentcapture.ContentCaptureSessionId;
import android.view.contentcapture.DataRemovalRequest;
import android.view.contentcapture.IContentCaptureDirectManager;
import android.view.contentcapture.IContentCaptureDirectManager.Stub;
import android.view.contentcapture.MainContentCaptureSession;
import com.android.internal.os.IResultReceiver;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@SystemApi
public abstract class ContentCaptureService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.contentcapture.ContentCaptureService";
    public static final String SERVICE_META_DATA = "android.content_capture";
    private static final String TAG = ContentCaptureService.class.getSimpleName();
    private IContentCaptureServiceCallback mCallback;
    private long mCallerMismatchTimeout = 1000;
    private final IContentCaptureDirectManager mClientInterface = new Stub() {
        public void sendEvents(ParceledListSlice events, int reason, ContentCaptureOptions options) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$2$nqaNcni5MOtmyGkMJfxu_qUHOk4.INSTANCE, ContentCaptureService.this, Integer.valueOf(Binder.getCallingUid()), events, Integer.valueOf(reason), options));
        }
    };
    private Handler mHandler;
    private long mLastCallerMismatchLog;
    private final IContentCaptureService mServerInterface = new IContentCaptureService.Stub() {
        public void onConnected(IBinder callback, boolean verbose, boolean debug) {
            ContentCaptureHelper.sVerbose = verbose;
            ContentCaptureHelper.sDebug = debug;
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$iP7RXM_Va9lafd6bT9eXRx_D47Q.INSTANCE, ContentCaptureService.this, callback));
        }

        public void onDisconnected() {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$wPMOb7AM5r-kHmuyl3SBSylaH1A.INSTANCE, ContentCaptureService.this));
        }

        public void onSessionStarted(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$PaMsQkJwdUJ1lCgOOaLG9Bm09t8.INSTANCE, ContentCaptureService.this, context, Integer.valueOf(sessionId), Integer.valueOf(uid), clientReceiver, Integer.valueOf(initialState)));
        }

        public void onActivitySnapshot(int sessionId, SnapshotData snapshotData) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$NhSHlL57JqxWNJ8QcsuGxEhxv1Y.INSTANCE, ContentCaptureService.this, Integer.valueOf(sessionId), snapshotData));
        }

        public void onSessionFinished(int sessionId) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$jkZQ77YuBlPDClOdklQb8tj8Kpw.INSTANCE, ContentCaptureService.this, Integer.valueOf(sessionId)));
        }

        public void onDataRemovalRequest(DataRemovalRequest request) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$sJuAS4AaQcXaSFkQpSEmVLBqyvw.INSTANCE, ContentCaptureService.this, request));
        }

        public void onActivityEvent(ActivityEvent event) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ContentCaptureService$1$V1mxGgTDjVVHroIjJrHvYfUHCKE.INSTANCE, ContentCaptureService.this, event));
        }
    };
    private final SparseIntArray mSessionUids = new SparseIntArray();

    public void onCreate() {
        super.onCreate();
        this.mHandler = new Handler(Looper.getMainLooper(), null, true);
    }

    public final IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mServerInterface.asBinder();
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tried to bind to wrong intent (should be android.service.contentcapture.ContentCaptureService: ");
        stringBuilder.append(intent);
        Log.w(str, stringBuilder.toString());
        return null;
    }

    public final void setContentCaptureWhitelist(Set<String> packages, Set<ComponentName> activities) {
        IContentCaptureServiceCallback callback = this.mCallback;
        if (callback == null) {
            Log.w(TAG, "setContentCaptureWhitelist(): no server callback");
            return;
        }
        try {
            callback.setContentCaptureWhitelist(ContentCaptureHelper.toList(packages), ContentCaptureHelper.toList(activities));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public final void setContentCaptureConditions(String packageName, Set<ContentCaptureCondition> conditions) {
        IContentCaptureServiceCallback callback = this.mCallback;
        if (callback == null) {
            Log.w(TAG, "setContentCaptureConditions(): no server callback");
            return;
        }
        try {
            callback.setContentCaptureConditions(packageName, ContentCaptureHelper.toList(conditions));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void onConnected() {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bound to ");
        stringBuilder.append(getClass().getName());
        Slog.i(str, stringBuilder.toString());
    }

    public void onCreateContentCaptureSession(ContentCaptureContext context, ContentCaptureSessionId sessionId) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onCreateContentCaptureSession(id=");
            stringBuilder.append(sessionId);
            stringBuilder.append(", ctx=");
            stringBuilder.append(context);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
    }

    public void onContentCaptureEvent(ContentCaptureSessionId sessionId, ContentCaptureEvent event) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onContentCaptureEventsRequest(id=");
            stringBuilder.append(sessionId);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
    }

    public void onDataRemovalRequest(DataRemovalRequest request) {
        if (ContentCaptureHelper.sVerbose) {
            Log.v(TAG, "onDataRemovalRequest()");
        }
    }

    public void onActivitySnapshot(ContentCaptureSessionId sessionId, SnapshotData snapshotData) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onActivitySnapshot(id=");
            stringBuilder.append(sessionId);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
    }

    public void onActivityEvent(ActivityEvent event) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onActivityEvent(): ");
            stringBuilder.append(event);
            Log.v(str, stringBuilder.toString());
        }
    }

    public void onDestroyContentCaptureSession(ContentCaptureSessionId sessionId) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onDestroyContentCaptureSession(id=");
            stringBuilder.append(sessionId);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
    }

    public final void disableSelf() {
        if (ContentCaptureHelper.sDebug) {
            Log.d(TAG, "disableSelf()");
        }
        IContentCaptureServiceCallback callback = this.mCallback;
        if (callback == null) {
            Log.w(TAG, "disableSelf(): no server callback");
            return;
        }
        try {
            callback.disableSelf();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void onDisconnected() {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unbinding from ");
        stringBuilder.append(getClass().getName());
        Slog.i(str, stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.print("Debug: ");
        pw.print(ContentCaptureHelper.sDebug);
        pw.print(" Verbose: ");
        pw.println(ContentCaptureHelper.sVerbose);
        int size = this.mSessionUids.size();
        pw.print("Number sessions: ");
        pw.println(size);
        if (size > 0) {
            String prefix = "  ";
            for (int i = 0; i < size; i++) {
                pw.print("  ");
                pw.print(this.mSessionUids.keyAt(i));
                pw.print(": uid=");
                pw.println(this.mSessionUids.valueAt(i));
            }
        }
    }

    private void handleOnConnected(IBinder callback) {
        this.mCallback = IContentCaptureServiceCallback.Stub.asInterface(callback);
        onConnected();
    }

    private void handleOnDisconnected() {
        onDisconnected();
        this.mCallback = null;
    }

    private void handleOnCreateSession(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) {
        this.mSessionUids.put(sessionId, uid);
        onCreateContentCaptureSession(context, new ContentCaptureSessionId(sessionId));
        int clientFlags = context.getFlags();
        int stateFlags = 0;
        if ((clientFlags & 2) != 0) {
            stateFlags = 0 | 32;
        }
        if ((clientFlags & 1) != 0) {
            stateFlags |= 64;
        }
        if (stateFlags == 0) {
            stateFlags = initialState;
        } else {
            stateFlags |= 4;
        }
        setClientState(clientReceiver, stateFlags, this.mClientInterface.asBinder());
    }

    private void handleSendEvents(int uid, ParceledListSlice<ContentCaptureEvent> parceledEvents, int reason, ContentCaptureOptions options) {
        int i = uid;
        List<ContentCaptureEvent> events = parceledEvents.getList();
        if (events.isEmpty()) {
            Log.w(TAG, "handleSendEvents() received empty list of events");
            return;
        }
        FlushMetrics metrics = new FlushMetrics();
        ComponentName activityComponent = null;
        int lastSessionId = 0;
        ContentCaptureSessionId sessionId = null;
        for (int i2 = 0; i2 < events.size(); i2++) {
            ContentCaptureEvent event = (ContentCaptureEvent) events.get(i2);
            if (handleIsRightCallerFor(event, i)) {
                int lastSessionId2;
                int sessionIdInt = event.getSessionId();
                if (sessionIdInt != lastSessionId) {
                    sessionId = new ContentCaptureSessionId(sessionIdInt);
                    lastSessionId2 = sessionIdInt;
                    if (i2 != 0) {
                        writeFlushMetrics(lastSessionId2, activityComponent, metrics, options, reason);
                        metrics.reset();
                    }
                    lastSessionId = lastSessionId2;
                }
                ContentCaptureContext clientContext = event.getContentCaptureContext();
                if (activityComponent == null && clientContext != null) {
                    activityComponent = clientContext.getActivityComponent();
                }
                lastSessionId2 = event.getType();
                if (lastSessionId2 == -2) {
                    this.mSessionUids.delete(sessionIdInt);
                    onDestroyContentCaptureSession(sessionId);
                    metrics.sessionFinished++;
                } else if (lastSessionId2 == -1) {
                    clientContext.setParentSessionId(event.getParentSessionId());
                    this.mSessionUids.put(sessionIdInt, i);
                    onCreateContentCaptureSession(clientContext, sessionId);
                    metrics.sessionStarted++;
                } else if (lastSessionId2 == 1) {
                    onContentCaptureEvent(sessionId, event);
                    metrics.viewAppearedCount++;
                } else if (lastSessionId2 == 2) {
                    onContentCaptureEvent(sessionId, event);
                    metrics.viewDisappearedCount++;
                } else if (lastSessionId2 != 3) {
                    onContentCaptureEvent(sessionId, event);
                } else {
                    onContentCaptureEvent(sessionId, event);
                    metrics.viewTextChangedCount++;
                }
            }
        }
        writeFlushMetrics(lastSessionId, activityComponent, metrics, options, reason);
    }

    private void handleOnActivitySnapshot(int sessionId, SnapshotData snapshotData) {
        onActivitySnapshot(new ContentCaptureSessionId(sessionId), snapshotData);
    }

    private void handleFinishSession(int sessionId) {
        this.mSessionUids.delete(sessionId);
        onDestroyContentCaptureSession(new ContentCaptureSessionId(sessionId));
    }

    private void handleOnDataRemovalRequest(DataRemovalRequest request) {
        onDataRemovalRequest(request);
    }

    private void handleOnActivityEvent(ActivityEvent event) {
        onActivityEvent(event);
    }

    private boolean handleIsRightCallerFor(ContentCaptureEvent event, int uid) {
        int type = event.getType();
        if (type == -2 || type == -1) {
            type = event.getParentSessionId();
        } else {
            type = event.getSessionId();
        }
        if (this.mSessionUids.indexOfKey(type) < 0) {
            if (ContentCaptureHelper.sVerbose) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("handleIsRightCallerFor(");
                stringBuilder.append(event);
                stringBuilder.append("): no session for ");
                stringBuilder.append(type);
                stringBuilder.append(": ");
                stringBuilder.append(this.mSessionUids);
                Log.v(str, stringBuilder.toString());
            }
            return false;
        }
        int rightUid = this.mSessionUids.get(type);
        if (rightUid == uid) {
            return true;
        }
        String str2 = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("invalid call from UID ");
        stringBuilder2.append(uid);
        stringBuilder2.append(": session ");
        stringBuilder2.append(type);
        stringBuilder2.append(" belongs to ");
        stringBuilder2.append(rightUid);
        Log.e(str2, stringBuilder2.toString());
        long now = System.currentTimeMillis();
        if (now - this.mLastCallerMismatchLog > this.mCallerMismatchTimeout) {
            StatsLogInternal.write(206, getPackageManager().getNameForUid(rightUid), getPackageManager().getNameForUid(uid));
            this.mLastCallerMismatchLog = now;
        }
        return false;
    }

    public static void setClientState(IResultReceiver clientReceiver, int sessionState, IBinder binder) {
        RemoteException e;
        if (binder != null) {
            try {
                e = new Bundle();
                e.putBinder(MainContentCaptureSession.EXTRA_BINDER, binder);
            } catch (RemoteException e2) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error async reporting result to client: ");
                stringBuilder.append(e2);
                Slog.w(str, stringBuilder.toString());
                return;
            }
        }
        e2 = null;
        clientReceiver.send(sessionState, e2);
    }

    private void writeFlushMetrics(int sessionId, ComponentName app, FlushMetrics flushMetrics, ContentCaptureOptions options, int flushReason) {
        IContentCaptureServiceCallback iContentCaptureServiceCallback = this.mCallback;
        if (iContentCaptureServiceCallback == null) {
            Log.w(TAG, "writeSessionFlush(): no server callback");
            return;
        }
        try {
            iContentCaptureServiceCallback.writeSessionFlush(sessionId, app, flushMetrics, options, flushReason);
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failed to write flush metrics: ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
    }
}
