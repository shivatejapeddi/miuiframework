package android.view.contentcapture;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.util.LocalLog;
import android.util.Log;
import android.util.TimeUtils;
import android.view.autofill.AutofillId;
import android.view.contentcapture.ViewNode.ViewStructureImpl;
import com.android.internal.os.IResultReceiver.Stub;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MainContentCaptureSession extends ContentCaptureSession {
    public static final String EXTRA_BINDER = "binder";
    public static final String EXTRA_ENABLED_STATE = "enabled";
    private static final boolean FORCE_FLUSH = true;
    private static final int MSG_FLUSH = 1;
    private static final String TAG = MainContentCaptureSession.class.getSimpleName();
    private IBinder mApplicationToken;
    private ComponentName mComponentName;
    private final Context mContext;
    private IContentCaptureDirectManager mDirectServiceInterface;
    private DeathRecipient mDirectServiceVulture;
    private final AtomicBoolean mDisabled = new AtomicBoolean(false);
    private ArrayList<ContentCaptureEvent> mEvents;
    private final LocalLog mFlushHistory;
    private final Handler mHandler;
    private final ContentCaptureManager mManager;
    private long mNextFlush;
    private boolean mNextFlushForTextChanged = false;
    private final Stub mSessionStateReceiver;
    private int mState = 0;
    private final IContentCaptureManager mSystemServerInterface;

    protected MainContentCaptureSession(Context context, ContentCaptureManager manager, Handler handler, IContentCaptureManager systemServerInterface) {
        this.mContext = context;
        this.mManager = manager;
        this.mHandler = handler;
        this.mSystemServerInterface = systemServerInterface;
        int logHistorySize = this.mManager.mOptions.logHistorySize;
        this.mFlushHistory = logHistorySize > 0 ? new LocalLog(logHistorySize) : null;
        this.mSessionStateReceiver = new Stub() {
            public void send(int resultCode, Bundle resultData) {
                IBinder binder;
                if (resultData == null) {
                    binder = null;
                } else if (resultData.getBoolean("enabled")) {
                    MainContentCaptureSession.this.mDisabled.set(resultCode == 2);
                    return;
                } else {
                    binder = resultData.getBinder(MainContentCaptureSession.EXTRA_BINDER);
                    if (binder == null) {
                        Log.wtf(MainContentCaptureSession.TAG, "No binder extra result");
                        MainContentCaptureSession.this.mHandler.post(new -$$Lambda$MainContentCaptureSession$1$JPRO-nNGZpgXrKr4QC_iQiTbQx0(this));
                        return;
                    }
                }
                MainContentCaptureSession.this.mHandler.post(new -$$Lambda$MainContentCaptureSession$1$Xhq3WJibbalS1G_W3PRC2m7muhM(this, resultCode, binder));
            }

            public /* synthetic */ void lambda$send$0$MainContentCaptureSession$1() {
                MainContentCaptureSession.this.resetSession(260);
            }

            public /* synthetic */ void lambda$send$1$MainContentCaptureSession$1(int resultCode, IBinder binder) {
                MainContentCaptureSession.this.onSessionStarted(resultCode, binder);
            }
        };
    }

    /* Access modifiers changed, original: 0000 */
    public MainContentCaptureSession getMainCaptureSession() {
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public ContentCaptureSession newChild(ContentCaptureContext clientContext) {
        ContentCaptureSession child = new ChildContentCaptureSession(this, clientContext);
        notifyChildSessionStarted(this.mId, child.mId, clientContext);
        return child;
    }

    /* Access modifiers changed, original: 0000 */
    public void start(IBinder token, ComponentName component, int flags) {
        if (isContentCaptureEnabled()) {
            String str;
            StringBuilder stringBuilder;
            if (ContentCaptureHelper.sVerbose) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("start(): token=");
                stringBuilder.append(token);
                stringBuilder.append(", comp=");
                stringBuilder.append(ComponentName.flattenToShortString(component));
                Log.v(str, stringBuilder.toString());
            }
            if (hasStarted()) {
                if (ContentCaptureHelper.sDebug) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ignoring handleStartSession(");
                    stringBuilder.append(token);
                    stringBuilder.append("/");
                    stringBuilder.append(ComponentName.flattenToShortString(component));
                    stringBuilder.append(" while on state ");
                    stringBuilder.append(ContentCaptureSession.getStateAsString(this.mState));
                    Log.d(str, stringBuilder.toString());
                }
                return;
            }
            this.mState = 1;
            this.mApplicationToken = token;
            this.mComponentName = component;
            if (ContentCaptureHelper.sVerbose) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleStartSession(): token=");
                stringBuilder.append(token);
                stringBuilder.append(", act=");
                stringBuilder.append(getDebugState());
                stringBuilder.append(", id=");
                stringBuilder.append(this.mId);
                Log.v(str, stringBuilder.toString());
            }
            try {
                this.mSystemServerInterface.startSession(this.mApplicationToken, component, this.mId, flags, this.mSessionStateReceiver);
            } catch (RemoteException e) {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Error starting session for ");
                stringBuilder2.append(component.flattenToShortString());
                stringBuilder2.append(": ");
                stringBuilder2.append(e);
                Log.w(str2, stringBuilder2.toString());
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDestroy() {
        this.mHandler.removeMessages(1);
        this.mHandler.post(new -$$Lambda$MainContentCaptureSession$HTmdDf687TPcaTnLyPp3wo0gI60(this));
    }

    public /* synthetic */ void lambda$onDestroy$0$MainContentCaptureSession() {
        destroySession();
    }

    private void onSessionStarted(int resultCode, IBinder binder) {
        int i = 0;
        if (binder != null) {
            this.mDirectServiceInterface = IContentCaptureDirectManager.Stub.asInterface(binder);
            this.mDirectServiceVulture = new -$$Lambda$MainContentCaptureSession$UWslDbWedtPhv49PtRsvG4TlYWw(this);
            try {
                binder.linkToDeath(this.mDirectServiceVulture, 0);
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to link to death on ");
                stringBuilder.append(binder);
                stringBuilder.append(": ");
                stringBuilder.append(e);
                Log.w(str, stringBuilder.toString());
            }
        }
        if ((resultCode & 4) != 0) {
            resetSession(resultCode);
        } else {
            this.mState = resultCode;
            this.mDisabled.set(false);
        }
        if (ContentCaptureHelper.sVerbose) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("handleSessionStarted() result: id=");
            stringBuilder2.append(this.mId);
            stringBuilder2.append(" resultCode=");
            stringBuilder2.append(resultCode);
            stringBuilder2.append(", state=");
            stringBuilder2.append(ContentCaptureSession.getStateAsString(this.mState));
            stringBuilder2.append(", disabled=");
            stringBuilder2.append(this.mDisabled.get());
            stringBuilder2.append(", binder=");
            stringBuilder2.append(binder);
            stringBuilder2.append(", events=");
            ArrayList arrayList = this.mEvents;
            if (arrayList != null) {
                i = arrayList.size();
            }
            stringBuilder2.append(i);
            Log.v(str2, stringBuilder2.toString());
        }
    }

    public /* synthetic */ void lambda$onSessionStarted$1$MainContentCaptureSession() {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Keeping session ");
        stringBuilder.append(this.mId);
        stringBuilder.append(" when service died");
        Log.w(str, stringBuilder.toString());
        this.mState = 1024;
        this.mDisabled.set(true);
    }

    private void sendEvent(ContentCaptureEvent event) {
        sendEvent(event, false);
    }

    private void sendEvent(ContentCaptureEvent event, boolean forceFlush) {
        String str;
        StringBuilder stringBuilder;
        int eventType = event.getType();
        String str2 = "handleSendEvent(";
        if (ContentCaptureHelper.sVerbose) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(getDebugState());
            stringBuilder.append("): ");
            stringBuilder.append(event);
            Log.v(str, stringBuilder.toString());
        }
        if (!hasStarted() && eventType != -1 && eventType != 6) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(getDebugState());
            stringBuilder.append(", ");
            stringBuilder.append(ContentCaptureEvent.getTypeAsString(eventType));
            stringBuilder.append("): dropping because session not started yet");
            Log.v(str, stringBuilder.toString());
        } else if (this.mDisabled.get()) {
            if (ContentCaptureHelper.sVerbose) {
                Log.v(TAG, "handleSendEvent(): ignoring when disabled");
            }
        } else {
            ArrayList arrayList;
            ContentCaptureEvent lastEvent;
            int maxBufferSize = this.mManager.mOptions.maxBufferSize;
            if (this.mEvents == null) {
                if (ContentCaptureHelper.sVerbose) {
                    str2 = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("handleSendEvent(): creating buffer for ");
                    stringBuilder2.append(maxBufferSize);
                    stringBuilder2.append(" events");
                    Log.v(str2, stringBuilder2.toString());
                }
                this.mEvents = new ArrayList(maxBufferSize);
            }
            boolean addEvent = true;
            if (!this.mEvents.isEmpty() && eventType == 3) {
                arrayList = this.mEvents;
                lastEvent = (ContentCaptureEvent) arrayList.get(arrayList.size() - 1);
                if (lastEvent.getType() == 3 && lastEvent.getId().equals(event.getId())) {
                    if (ContentCaptureHelper.sVerbose) {
                        String str3 = TAG;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Buffering VIEW_TEXT_CHANGED event, updated text=");
                        stringBuilder3.append(ContentCaptureHelper.getSanitizedString(event.getText()));
                        Log.v(str3, stringBuilder3.toString());
                    }
                    lastEvent.mergeEvent(event);
                    addEvent = false;
                }
            }
            if (!this.mEvents.isEmpty() && eventType == 2) {
                arrayList = this.mEvents;
                lastEvent = (ContentCaptureEvent) arrayList.get(arrayList.size() - 1);
                if (lastEvent.getType() == 2 && event.getSessionId() == lastEvent.getSessionId()) {
                    if (ContentCaptureHelper.sVerbose) {
                        String str4 = TAG;
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("Buffering TYPE_VIEW_DISAPPEARED events for session ");
                        stringBuilder4.append(lastEvent.getSessionId());
                        Log.v(str4, stringBuilder4.toString());
                    }
                    lastEvent.mergeEvent(event);
                    addEvent = false;
                }
            }
            if (addEvent) {
                this.mEvents.add(event);
            }
            int numberEvents = this.mEvents.size();
            int flushReason;
            if ((numberEvents < maxBufferSize) && !forceFlush) {
                if (eventType == 3) {
                    this.mNextFlushForTextChanged = true;
                    flushReason = 6;
                } else if (this.mNextFlushForTextChanged) {
                    if (ContentCaptureHelper.sVerbose) {
                        Log.i(TAG, "Not scheduling flush because next flush is for text changed");
                    }
                    return;
                } else {
                    flushReason = 5;
                }
                scheduleFlush(flushReason, true);
            } else if (this.mState == 2 || numberEvents < maxBufferSize) {
                if (eventType == -2) {
                    flushReason = 4;
                } else if (eventType != -1) {
                    flushReason = 1;
                } else {
                    flushReason = 3;
                }
                flush(flushReason);
            } else {
                if (ContentCaptureHelper.sDebug) {
                    String str5 = TAG;
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append("Closing session for ");
                    stringBuilder5.append(getDebugState());
                    stringBuilder5.append(" after ");
                    stringBuilder5.append(numberEvents);
                    stringBuilder5.append(" delayed events");
                    Log.d(str5, stringBuilder5.toString());
                }
                resetSession(132);
            }
        }
    }

    private boolean hasStarted() {
        return this.mState != 0;
    }

    private void scheduleFlush(int reason, boolean checkExisting) {
        String str;
        StringBuilder stringBuilder;
        String str2 = "handleScheduleFlush(";
        if (ContentCaptureHelper.sVerbose) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(getDebugState(reason));
            stringBuilder.append(", checkExisting=");
            stringBuilder.append(checkExisting);
            Log.v(str, stringBuilder.toString());
        }
        if (!hasStarted()) {
            if (ContentCaptureHelper.sVerbose) {
                Log.v(TAG, "handleScheduleFlush(): session not started yet");
            }
        } else if (this.mDisabled.get()) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(getDebugState(reason));
            stringBuilder.append("): should not be called when disabled. events=");
            ArrayList arrayList = this.mEvents;
            stringBuilder.append(arrayList == null ? null : Integer.valueOf(arrayList.size()));
            Log.e(str, stringBuilder.toString());
        } else {
            int flushFrequencyMs;
            if (checkExisting && this.mHandler.hasMessages(1)) {
                this.mHandler.removeMessages(1);
            }
            if (reason == 5) {
                flushFrequencyMs = this.mManager.mOptions.idleFlushingFrequencyMs;
            } else if (reason == 6) {
                flushFrequencyMs = this.mManager.mOptions.textChangeFlushingFrequencyMs;
            } else {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(getDebugState(reason));
                stringBuilder.append("): not called with a timeout reason.");
                Log.e(str, stringBuilder.toString());
                return;
            }
            this.mNextFlush = System.currentTimeMillis() + ((long) flushFrequencyMs);
            if (ContentCaptureHelper.sVerbose) {
                String str3 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("handleScheduleFlush(): scheduled to flush in ");
                stringBuilder2.append(flushFrequencyMs);
                stringBuilder2.append("ms: ");
                stringBuilder2.append(TimeUtils.logTimeOfDay(this.mNextFlush));
                Log.v(str3, stringBuilder2.toString());
            }
            this.mHandler.postDelayed(new -$$Lambda$MainContentCaptureSession$49zT7C2BXrEdkyggyGk1Qs4d46k(this, reason), 1, (long) flushFrequencyMs);
        }
    }

    public /* synthetic */ void lambda$scheduleFlush$2$MainContentCaptureSession(int reason) {
        flushIfNeeded(reason);
    }

    private void flushIfNeeded(int reason) {
        ArrayList arrayList = this.mEvents;
        if (arrayList == null || arrayList.isEmpty()) {
            if (ContentCaptureHelper.sVerbose) {
                Log.v(TAG, "Nothing to flush");
            }
            return;
        }
        flush(reason);
    }

    /* Access modifiers changed, original: 0000 */
    public void flush(int reason) {
        if (this.mEvents != null) {
            String str = "handleForceFlush(";
            String str2;
            StringBuilder stringBuilder;
            if (this.mDisabled.get()) {
                str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(getDebugState(reason));
                stringBuilder2.append("): should not be when disabled");
                Log.e(str2, stringBuilder2.toString());
            } else if (this.mDirectServiceInterface == null) {
                if (ContentCaptureHelper.sVerbose) {
                    str2 = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(getDebugState(reason));
                    stringBuilder.append("): hold your horses, client not ready: ");
                    stringBuilder.append(this.mEvents);
                    Log.v(str2, stringBuilder.toString());
                }
                if (!this.mHandler.hasMessages(1)) {
                    scheduleFlush(reason, false);
                }
            } else {
                String str3;
                int numberEvents = this.mEvents.size();
                str = ContentCaptureSession.getFlushReasonAsString(reason);
                if (ContentCaptureHelper.sDebug) {
                    str3 = TAG;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Flushing ");
                    stringBuilder3.append(numberEvents);
                    stringBuilder3.append(" event(s) for ");
                    stringBuilder3.append(getDebugState(reason));
                    Log.d(str3, stringBuilder3.toString());
                }
                if (this.mFlushHistory != null) {
                    str3 = new StringBuilder();
                    str3.append("r=");
                    str3.append(str);
                    str3.append(" s=");
                    str3.append(numberEvents);
                    str3.append(" m=");
                    str3.append(this.mManager.mOptions.maxBufferSize);
                    str3.append(" i=");
                    str3.append(this.mManager.mOptions.idleFlushingFrequencyMs);
                    this.mFlushHistory.log(str3.toString());
                }
                try {
                    this.mHandler.removeMessages(1);
                    if (reason == 6) {
                        this.mNextFlushForTextChanged = false;
                    }
                    this.mDirectServiceInterface.sendEvents(clearEvents(), reason, this.mManager.mOptions);
                } catch (RemoteException e) {
                    String str4 = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Error sending ");
                    stringBuilder.append(numberEvents);
                    stringBuilder.append(" for ");
                    stringBuilder.append(getDebugState());
                    stringBuilder.append(": ");
                    stringBuilder.append(e);
                    Log.w(str4, stringBuilder.toString());
                }
            }
        }
    }

    public void updateContentCaptureContext(ContentCaptureContext context) {
        notifyContextUpdated(this.mId, context);
    }

    private ParceledListSlice<ContentCaptureEvent> clearEvents() {
        List<ContentCaptureEvent> events = this.mEvents;
        if (events == null) {
            events = Collections.emptyList();
        }
        this.mEvents = null;
        return new ParceledListSlice(events);
    }

    private void destroySession() {
        if (ContentCaptureHelper.sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Destroying session (ctx=");
            stringBuilder.append(this.mContext);
            stringBuilder.append(", id=");
            stringBuilder.append(this.mId);
            stringBuilder.append(") with ");
            ArrayList arrayList = this.mEvents;
            stringBuilder.append(arrayList == null ? 0 : arrayList.size());
            stringBuilder.append(" event(s) for ");
            stringBuilder.append(getDebugState());
            Log.d(str, stringBuilder.toString());
        }
        try {
            this.mSystemServerInterface.finishSession(this.mId);
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Error destroying system-service session ");
            stringBuilder2.append(this.mId);
            stringBuilder2.append(" for ");
            stringBuilder2.append(getDebugState());
            stringBuilder2.append(": ");
            stringBuilder2.append(e);
            Log.e(str2, stringBuilder2.toString());
        }
    }

    private void resetSession(int newState) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handleResetSession(");
            stringBuilder.append(getActivityName());
            stringBuilder.append("): from ");
            stringBuilder.append(ContentCaptureSession.getStateAsString(this.mState));
            stringBuilder.append(" to ");
            stringBuilder.append(ContentCaptureSession.getStateAsString(newState));
            Log.v(str, stringBuilder.toString());
        }
        this.mState = newState;
        this.mDisabled.set((newState & 4) != 0);
        this.mApplicationToken = null;
        this.mComponentName = null;
        this.mEvents = null;
        IContentCaptureDirectManager iContentCaptureDirectManager = this.mDirectServiceInterface;
        if (iContentCaptureDirectManager != null) {
            iContentCaptureDirectManager.asBinder().unlinkToDeath(this.mDirectServiceVulture, 0);
        }
        this.mDirectServiceInterface = null;
        this.mHandler.removeMessages(1);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalNotifyViewAppeared(ViewStructureImpl node) {
        notifyViewAppeared(this.mId, node);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalNotifyViewDisappeared(AutofillId id) {
        notifyViewDisappeared(this.mId, id);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalNotifyViewTextChanged(AutofillId id, CharSequence text) {
        notifyViewTextChanged(this.mId, id, text);
    }

    public void internalNotifyViewTreeEvent(boolean started) {
        notifyViewTreeEvent(this.mId, started);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isContentCaptureEnabled() {
        return super.isContentCaptureEnabled() && this.mManager.isContentCaptureEnabled();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isDisabled() {
        return this.mDisabled.get();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean setDisabled(boolean disabled) {
        return this.mDisabled.compareAndSet(disabled ^ 1, disabled);
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyChildSessionStarted(int parentSessionId, int childSessionId, ContentCaptureContext clientContext) {
        sendEvent(new ContentCaptureEvent(childSessionId, -1).setParentSessionId(parentSessionId).setClientContext(clientContext), true);
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyChildSessionFinished(int parentSessionId, int childSessionId) {
        sendEvent(new ContentCaptureEvent(childSessionId, -2).setParentSessionId(parentSessionId), true);
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyViewAppeared(int sessionId, ViewStructureImpl node) {
        sendEvent(new ContentCaptureEvent(sessionId, 1).setViewNode(node.mNode));
    }

    public void notifyViewDisappeared(int sessionId, AutofillId id) {
        sendEvent(new ContentCaptureEvent(sessionId, 2).setAutofillId(id));
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyViewTextChanged(int sessionId, AutofillId id, CharSequence text) {
        sendEvent(new ContentCaptureEvent(sessionId, 3).setAutofillId(id).setText(text));
    }

    public void notifyViewTreeEvent(int sessionId, boolean started) {
        sendEvent(new ContentCaptureEvent(sessionId, started ? 4 : 5), true);
    }

    public void notifySessionLifecycle(boolean started) {
        sendEvent(new ContentCaptureEvent(this.mId, started ? 7 : 8), true);
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyContextUpdated(int sessionId, ContentCaptureContext context) {
        sendEvent(new ContentCaptureEvent(sessionId, 6).setClientContext(context));
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(String prefix, PrintWriter pw) {
        super.dump(prefix, pw);
        pw.print(prefix);
        pw.print("mContext: ");
        pw.println(this.mContext);
        pw.print(prefix);
        pw.print("user: ");
        pw.println(this.mContext.getUserId());
        if (this.mDirectServiceInterface != null) {
            pw.print(prefix);
            pw.print("mDirectServiceInterface: ");
            pw.println(this.mDirectServiceInterface);
        }
        pw.print(prefix);
        pw.print("mDisabled: ");
        pw.println(this.mDisabled.get());
        pw.print(prefix);
        pw.print("isEnabled(): ");
        pw.println(isContentCaptureEnabled());
        pw.print(prefix);
        pw.print("state: ");
        pw.println(ContentCaptureSession.getStateAsString(this.mState));
        if (this.mApplicationToken != null) {
            pw.print(prefix);
            pw.print("app token: ");
            pw.println(this.mApplicationToken);
        }
        if (this.mComponentName != null) {
            pw.print(prefix);
            pw.print("component name: ");
            pw.println(this.mComponentName.flattenToShortString());
        }
        ArrayList arrayList = this.mEvents;
        if (!(arrayList == null || arrayList.isEmpty())) {
            int numberEvents = this.mEvents.size();
            pw.print(prefix);
            pw.print("buffered events: ");
            pw.print(numberEvents);
            pw.print('/');
            pw.println(this.mManager.mOptions.maxBufferSize);
            if (ContentCaptureHelper.sVerbose && numberEvents > 0) {
                String prefix3 = new StringBuilder();
                prefix3.append(prefix);
                prefix3.append("  ");
                prefix3 = prefix3.toString();
                for (int i = 0; i < numberEvents; i++) {
                    ContentCaptureEvent event = (ContentCaptureEvent) this.mEvents.get(i);
                    pw.print(prefix3);
                    pw.print(i);
                    pw.print(": ");
                    event.dump(pw);
                    pw.println();
                }
            }
            pw.print(prefix);
            pw.print("mNextFlushForTextChanged: ");
            pw.println(this.mNextFlushForTextChanged);
            pw.print(prefix);
            pw.print("flush frequency: ");
            if (this.mNextFlushForTextChanged) {
                pw.println(this.mManager.mOptions.textChangeFlushingFrequencyMs);
            } else {
                pw.println(this.mManager.mOptions.idleFlushingFrequencyMs);
            }
            pw.print(prefix);
            pw.print("next flush: ");
            TimeUtils.formatDuration(this.mNextFlush - System.currentTimeMillis(), pw);
            pw.print(" (");
            pw.print(TimeUtils.logTimeOfDay(this.mNextFlush));
            pw.println(")");
        }
        if (this.mFlushHistory != null) {
            pw.print(prefix);
            pw.println("flush history:");
            this.mFlushHistory.reverseDump(null, pw, null);
            pw.println();
        } else {
            pw.print(prefix);
            pw.println("not logging flush history");
        }
        super.dump(prefix, pw);
    }

    private String getActivityName() {
        StringBuilder stringBuilder;
        if (this.mComponentName == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("pkg:");
            stringBuilder.append(this.mContext.getPackageName());
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("act:");
        stringBuilder.append(this.mComponentName.flattenToShortString());
        return stringBuilder.toString();
    }

    private String getDebugState() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getActivityName());
        stringBuilder.append(" [state=");
        stringBuilder.append(ContentCaptureSession.getStateAsString(this.mState));
        stringBuilder.append(", disabled=");
        stringBuilder.append(this.mDisabled.get());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String getDebugState(int reason) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDebugState());
        stringBuilder.append(", reason=");
        stringBuilder.append(ContentCaptureSession.getFlushReasonAsString(reason));
        return stringBuilder.toString();
    }
}
