package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.PerformanceCollector;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.TestLooperManager;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.util.SeempLog;
import android.util.TimedRemoteCaller;
import android.view.IWindowManager.Stub;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManagerGlobal;
import com.android.internal.content.ReferrerIntent;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class Instrumentation {
    public static final String REPORT_KEY_IDENTIFIER = "id";
    public static final String REPORT_KEY_STREAMRESULT = "stream";
    private static final String TAG = "Instrumentation";
    private List<ActivityMonitor> mActivityMonitors;
    private final Object mAnimationCompleteLock = new Object();
    private Context mAppContext;
    private boolean mAutomaticPerformanceSnapshots = false;
    private ComponentName mComponent;
    private Context mInstrContext;
    private MessageQueue mMessageQueue = null;
    private Bundle mPerfMetrics = new Bundle();
    private PerformanceCollector mPerformanceCollector;
    private Thread mRunner;
    private final Object mSync = new Object();
    private ActivityThread mThread = null;
    private UiAutomation mUiAutomation;
    private IUiAutomationConnection mUiAutomationConnection;
    private List<ActivityWaiter> mWaitingActivities;
    private IInstrumentationWatcher mWatcher;

    /* renamed from: android.app.Instrumentation$1ContextMenuRunnable */
    class AnonymousClass1ContextMenuRunnable implements Runnable {
        private final Activity activity;
        private final int flags;
        private final int identifier;
        boolean returnValue;

        public AnonymousClass1ContextMenuRunnable(Activity _activity, int _identifier, int _flags) {
            this.activity = _activity;
            this.identifier = _identifier;
            this.flags = _flags;
        }

        public void run() {
            this.returnValue = this.activity.getWindow().performContextMenuIdentifierAction(this.identifier, this.flags);
        }
    }

    /* renamed from: android.app.Instrumentation$1MenuRunnable */
    class AnonymousClass1MenuRunnable implements Runnable {
        private final Activity activity;
        private final int flags;
        private final int identifier;
        boolean returnValue;

        public AnonymousClass1MenuRunnable(Activity _activity, int _identifier, int _flags) {
            this.activity = _activity;
            this.identifier = _identifier;
            this.flags = _flags;
        }

        public void run() {
            this.returnValue = this.activity.getWindow().performPanelIdentifierAction(0, this.identifier, this.flags);
        }
    }

    private final class ActivityGoing implements IdleHandler {
        private final ActivityWaiter mWaiter;

        public ActivityGoing(ActivityWaiter waiter) {
            this.mWaiter = waiter;
        }

        public final boolean queueIdle() {
            synchronized (Instrumentation.this.mSync) {
                Instrumentation.this.mWaitingActivities.remove(this.mWaiter);
                Instrumentation.this.mSync.notifyAll();
            }
            return false;
        }
    }

    public static class ActivityMonitor {
        private final boolean mBlock;
        private final String mClass;
        int mHits;
        private final boolean mIgnoreMatchingSpecificIntents;
        Activity mLastActivity;
        private final ActivityResult mResult;
        private final IntentFilter mWhich;

        public ActivityMonitor(IntentFilter which, ActivityResult result, boolean block) {
            this.mHits = 0;
            this.mLastActivity = null;
            this.mWhich = which;
            this.mClass = null;
            this.mResult = result;
            this.mBlock = block;
            this.mIgnoreMatchingSpecificIntents = false;
        }

        public ActivityMonitor(String cls, ActivityResult result, boolean block) {
            this.mHits = 0;
            this.mLastActivity = null;
            this.mWhich = null;
            this.mClass = cls;
            this.mResult = result;
            this.mBlock = block;
            this.mIgnoreMatchingSpecificIntents = false;
        }

        public ActivityMonitor() {
            this.mHits = 0;
            this.mLastActivity = null;
            this.mWhich = null;
            this.mClass = null;
            this.mResult = null;
            this.mBlock = false;
            this.mIgnoreMatchingSpecificIntents = true;
        }

        /* Access modifiers changed, original: final */
        public final boolean ignoreMatchingSpecificIntents() {
            return this.mIgnoreMatchingSpecificIntents;
        }

        public final IntentFilter getFilter() {
            return this.mWhich;
        }

        public final ActivityResult getResult() {
            return this.mResult;
        }

        public final boolean isBlocking() {
            return this.mBlock;
        }

        public final int getHits() {
            return this.mHits;
        }

        public final Activity getLastActivity() {
            return this.mLastActivity;
        }

        public final Activity waitForActivity() {
            Activity res;
            synchronized (this) {
                while (this.mLastActivity == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                res = this.mLastActivity;
                this.mLastActivity = null;
            }
            return res;
        }

        public final Activity waitForActivityWithTimeout(long timeOut) {
            synchronized (this) {
                if (this.mLastActivity == null) {
                    try {
                        wait(timeOut);
                    } catch (InterruptedException e) {
                    }
                }
                if (this.mLastActivity == null) {
                    return null;
                }
                Activity res = this.mLastActivity;
                this.mLastActivity = null;
                return res;
            }
        }

        public ActivityResult onStartActivity(Intent intent) {
            return null;
        }

        /* Access modifiers changed, original: final */
        /* JADX WARNING: Missing block: B:23:0x0047, code skipped:
            return false;
     */
        /* JADX WARNING: Missing block: B:27:0x0050, code skipped:
            return true;
     */
        public final boolean match(android.content.Context r6, android.app.Activity r7, android.content.Intent r8) {
            /*
            r5 = this;
            r0 = r5.mIgnoreMatchingSpecificIntents;
            r1 = 0;
            if (r0 == 0) goto L_0x0006;
        L_0x0005:
            return r1;
        L_0x0006:
            monitor-enter(r5);
            r0 = r5.mWhich;	 Catch:{ all -> 0x0051 }
            r2 = 1;
            if (r0 == 0) goto L_0x001c;
        L_0x000c:
            r0 = r5.mWhich;	 Catch:{ all -> 0x0051 }
            r3 = r6.getContentResolver();	 Catch:{ all -> 0x0051 }
            r4 = "Instrumentation";
            r0 = r0.match(r3, r8, r2, r4);	 Catch:{ all -> 0x0051 }
            if (r0 >= 0) goto L_0x001c;
        L_0x001a:
            monitor-exit(r5);	 Catch:{ all -> 0x0051 }
            return r1;
        L_0x001c:
            r0 = r5.mClass;	 Catch:{ all -> 0x0051 }
            if (r0 == 0) goto L_0x0048;
        L_0x0020:
            r0 = 0;
            if (r7 == 0) goto L_0x002d;
        L_0x0023:
            r3 = r7.getClass();	 Catch:{ all -> 0x0051 }
            r3 = r3.getName();	 Catch:{ all -> 0x0051 }
            r0 = r3;
            goto L_0x003c;
        L_0x002d:
            r3 = r8.getComponent();	 Catch:{ all -> 0x0051 }
            if (r3 == 0) goto L_0x003c;
        L_0x0033:
            r3 = r8.getComponent();	 Catch:{ all -> 0x0051 }
            r3 = r3.getClassName();	 Catch:{ all -> 0x0051 }
            r0 = r3;
        L_0x003c:
            if (r0 == 0) goto L_0x0046;
        L_0x003e:
            r3 = r5.mClass;	 Catch:{ all -> 0x0051 }
            r3 = r3.equals(r0);	 Catch:{ all -> 0x0051 }
            if (r3 != 0) goto L_0x0048;
        L_0x0046:
            monitor-exit(r5);	 Catch:{ all -> 0x0051 }
            return r1;
        L_0x0048:
            if (r7 == 0) goto L_0x004f;
        L_0x004a:
            r5.mLastActivity = r7;	 Catch:{ all -> 0x0051 }
            r5.notifyAll();	 Catch:{ all -> 0x0051 }
        L_0x004f:
            monitor-exit(r5);	 Catch:{ all -> 0x0051 }
            return r2;
        L_0x0051:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0051 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation$ActivityMonitor.match(android.content.Context, android.app.Activity, android.content.Intent):boolean");
        }
    }

    public static final class ActivityResult {
        private final int mResultCode;
        private final Intent mResultData;

        public ActivityResult(int resultCode, Intent resultData) {
            this.mResultCode = resultCode;
            this.mResultData = resultData;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public Intent getResultData() {
            return this.mResultData;
        }
    }

    private static final class ActivityWaiter {
        public Activity activity;
        public final Intent intent;

        public ActivityWaiter(Intent _intent) {
            this.intent = _intent;
        }
    }

    private static final class EmptyRunnable implements Runnable {
        private EmptyRunnable() {
        }

        public void run() {
        }
    }

    private static final class Idler implements IdleHandler {
        private final Runnable mCallback;
        private boolean mIdle = false;

        public Idler(Runnable callback) {
            this.mCallback = callback;
        }

        public final boolean queueIdle() {
            Runnable runnable = this.mCallback;
            if (runnable != null) {
                runnable.run();
            }
            synchronized (this) {
                this.mIdle = true;
                notifyAll();
            }
            return false;
        }

        public void waitForIdle() {
            synchronized (this) {
                while (!this.mIdle) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private final class InstrumentationThread extends Thread {
        public InstrumentationThread(String name) {
            super(name);
        }

        public void run() {
            try {
                Process.setThreadPriority(-8);
            } catch (RuntimeException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception setting priority of instrumentation thread ");
                stringBuilder.append(Process.myTid());
                Log.w(Instrumentation.TAG, stringBuilder.toString(), e);
            }
            if (Instrumentation.this.mAutomaticPerformanceSnapshots) {
                Instrumentation.this.startPerformanceSnapshot();
            }
            Instrumentation.this.onStart();
        }
    }

    private static final class SyncRunnable implements Runnable {
        private boolean mComplete;
        private final Runnable mTarget;

        public SyncRunnable(Runnable target) {
            this.mTarget = target;
        }

        public void run() {
            this.mTarget.run();
            synchronized (this) {
                this.mComplete = true;
                notifyAll();
            }
        }

        public void waitForComplete() {
            synchronized (this) {
                while (!this.mComplete) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UiAutomationFlags {
    }

    private void checkInstrumenting(String method) {
        if (this.mInstrContext == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(method);
            stringBuilder.append(" cannot be called outside of instrumented processes");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void onCreate(Bundle arguments) {
    }

    public void start() {
        if (this.mRunner == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Instr: ");
            stringBuilder.append(getClass().getName());
            this.mRunner = new InstrumentationThread(stringBuilder.toString());
            this.mRunner.start();
            return;
        }
        throw new RuntimeException("Instrumentation already started");
    }

    public void onStart() {
    }

    public boolean onException(Object obj, Throwable e) {
        return false;
    }

    public void sendStatus(int resultCode, Bundle results) {
        IInstrumentationWatcher iInstrumentationWatcher = this.mWatcher;
        if (iInstrumentationWatcher != null) {
            try {
                iInstrumentationWatcher.instrumentationStatus(this.mComponent, resultCode, results);
            } catch (RemoteException e) {
                this.mWatcher = null;
            }
        }
    }

    public void addResults(Bundle results) {
        try {
            ActivityManager.getService().addInstrumentationResults(this.mThread.getApplicationThread(), results);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void finish(int resultCode, Bundle results) {
        if (this.mAutomaticPerformanceSnapshots) {
            endPerformanceSnapshot();
        }
        if (this.mPerfMetrics != null) {
            if (results == null) {
                results = new Bundle();
            }
            results.putAll(this.mPerfMetrics);
        }
        UiAutomation uiAutomation = this.mUiAutomation;
        if (!(uiAutomation == null || uiAutomation.isDestroyed())) {
            this.mUiAutomation.disconnect();
            this.mUiAutomation = null;
        }
        this.mThread.finishInstrumentation(resultCode, results);
    }

    public void setAutomaticPerformanceSnapshots() {
        this.mAutomaticPerformanceSnapshots = true;
        this.mPerformanceCollector = new PerformanceCollector();
    }

    public void startPerformanceSnapshot() {
        if (!isProfiling()) {
            this.mPerformanceCollector.beginSnapshot(null);
        }
    }

    public void endPerformanceSnapshot() {
        if (!isProfiling()) {
            this.mPerfMetrics = this.mPerformanceCollector.endSnapshot();
        }
    }

    public void onDestroy() {
    }

    public Context getContext() {
        return this.mInstrContext;
    }

    public ComponentName getComponentName() {
        return this.mComponent;
    }

    public Context getTargetContext() {
        return this.mAppContext;
    }

    public String getProcessName() {
        return this.mThread.getProcessName();
    }

    public boolean isProfiling() {
        return this.mThread.isProfiling();
    }

    public void startProfiling() {
        if (this.mThread.isProfiling()) {
            File file = new File(this.mThread.getProfileFilePath());
            file.getParentFile().mkdirs();
            Debug.startMethodTracing(file.toString(), 8388608);
        }
    }

    public void stopProfiling() {
        if (this.mThread.isProfiling()) {
            Debug.stopMethodTracing();
        }
    }

    public void setInTouchMode(boolean inTouch) {
        try {
            Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE)).setInTouchMode(inTouch);
        } catch (RemoteException e) {
        }
    }

    public void waitForIdle(Runnable recipient) {
        this.mMessageQueue.addIdleHandler(new Idler(recipient));
        this.mThread.getHandler().post(new EmptyRunnable());
    }

    public void waitForIdleSync() {
        validateNotAppThread();
        Idler idler = new Idler(null);
        this.mMessageQueue.addIdleHandler(idler);
        this.mThread.getHandler().post(new EmptyRunnable());
        idler.waitForIdle();
    }

    private void waitForEnterAnimationComplete(Activity activity) {
        synchronized (this.mAnimationCompleteLock) {
            long timeout = TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS;
            while (timeout > 0) {
                try {
                    if (!activity.mEnterAnimationComplete) {
                        long startTime = System.currentTimeMillis();
                        this.mAnimationCompleteLock.wait(timeout);
                        timeout -= System.currentTimeMillis() - startTime;
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void onEnterAnimationComplete() {
        synchronized (this.mAnimationCompleteLock) {
            this.mAnimationCompleteLock.notifyAll();
        }
    }

    public void runOnMainSync(Runnable runner) {
        validateNotAppThread();
        SyncRunnable sr = new SyncRunnable(runner);
        this.mThread.getHandler().post(sr);
        sr.waitForComplete();
    }

    public Activity startActivitySync(Intent intent) {
        return startActivitySync(intent, null);
    }

    /* JADX WARNING: Missing block: B:30:?, code skipped:
            r4.close();
     */
    /* JADX WARNING: Missing block: B:31:0x0088, code skipped:
            r7 = move-exception;
     */
    /* JADX WARNING: Missing block: B:33:?, code skipped:
            r5.addSuppressed(r7);
     */
    public android.app.Activity startActivitySync(android.content.Intent r9, android.os.Bundle r10) {
        /*
        r8 = this;
        r0 = r9.toString();
        r1 = 376; // 0x178 float:5.27E-43 double:1.86E-321;
        android.util.SeempLog.record_str(r1, r0);
        r8.validateNotAppThread();
        r0 = r8.mSync;
        monitor-enter(r0);
        r1 = new android.content.Intent;	 Catch:{ all -> 0x00cd }
        r1.<init>(r9);	 Catch:{ all -> 0x00cd }
        r9 = r1;
        r1 = r8.getTargetContext();	 Catch:{ all -> 0x00cd }
        r1 = r1.getPackageManager();	 Catch:{ all -> 0x00cd }
        r2 = 0;
        r1 = r9.resolveActivityInfo(r1, r2);	 Catch:{ all -> 0x00cd }
        if (r1 == 0) goto L_0x00b6;
    L_0x0025:
        r2 = r8.mThread;	 Catch:{ all -> 0x00cd }
        r2 = r2.getProcessName();	 Catch:{ all -> 0x00cd }
        r3 = r1.processName;	 Catch:{ all -> 0x00cd }
        r3 = r3.equals(r2);	 Catch:{ all -> 0x00cd }
        if (r3 == 0) goto L_0x008d;
    L_0x0033:
        r3 = new android.content.ComponentName;	 Catch:{ all -> 0x00cd }
        r4 = r1.applicationInfo;	 Catch:{ all -> 0x00cd }
        r4 = r4.packageName;	 Catch:{ all -> 0x00cd }
        r5 = r1.name;	 Catch:{ all -> 0x00cd }
        r3.<init>(r4, r5);	 Catch:{ all -> 0x00cd }
        r9.setComponent(r3);	 Catch:{ all -> 0x00cd }
        r3 = new android.app.Instrumentation$ActivityWaiter;	 Catch:{ all -> 0x00cd }
        r3.<init>(r9);	 Catch:{ all -> 0x00cd }
        r4 = r8.mWaitingActivities;	 Catch:{ all -> 0x00cd }
        if (r4 != 0) goto L_0x0051;
    L_0x004a:
        r4 = new java.util.ArrayList;	 Catch:{ all -> 0x00cd }
        r4.<init>();	 Catch:{ all -> 0x00cd }
        r8.mWaitingActivities = r4;	 Catch:{ all -> 0x00cd }
    L_0x0051:
        r4 = r8.mWaitingActivities;	 Catch:{ all -> 0x00cd }
        r4.add(r3);	 Catch:{ all -> 0x00cd }
        r4 = r8.getTargetContext();	 Catch:{ all -> 0x00cd }
        r4.startActivity(r9, r10);	 Catch:{ all -> 0x00cd }
    L_0x005d:
        r4 = r8.mSync;	 Catch:{ InterruptedException -> 0x0063 }
        r4.wait();	 Catch:{ InterruptedException -> 0x0063 }
        goto L_0x0064;
    L_0x0063:
        r4 = move-exception;
    L_0x0064:
        r4 = r8.mWaitingActivities;	 Catch:{ all -> 0x00cd }
        r4 = r4.contains(r3);	 Catch:{ all -> 0x00cd }
        if (r4 != 0) goto L_0x005d;
    L_0x006c:
        r4 = r3.activity;	 Catch:{ all -> 0x00cd }
        r8.waitForEnterAnimationComplete(r4);	 Catch:{ all -> 0x00cd }
        r4 = new android.view.SurfaceControl$Transaction;	 Catch:{ all -> 0x00cd }
        r4.<init>();	 Catch:{ all -> 0x00cd }
        r5 = 1;
        r4.apply(r5);	 Catch:{ all -> 0x0081 }
        r4.close();	 Catch:{ all -> 0x00cd }
        r4 = r3.activity;	 Catch:{ all -> 0x00cd }
        monitor-exit(r0);	 Catch:{ all -> 0x00cd }
        return r4;
    L_0x0081:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x0083 }
    L_0x0083:
        r6 = move-exception;
        r4.close();	 Catch:{ all -> 0x0088 }
        goto L_0x008c;
    L_0x0088:
        r7 = move-exception;
        r5.addSuppressed(r7);	 Catch:{ all -> 0x00cd }
    L_0x008c:
        throw r6;	 Catch:{ all -> 0x00cd }
    L_0x008d:
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x00cd }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cd }
        r4.<init>();	 Catch:{ all -> 0x00cd }
        r5 = "Intent in process ";
        r4.append(r5);	 Catch:{ all -> 0x00cd }
        r4.append(r2);	 Catch:{ all -> 0x00cd }
        r5 = " resolved to different process ";
        r4.append(r5);	 Catch:{ all -> 0x00cd }
        r5 = r1.processName;	 Catch:{ all -> 0x00cd }
        r4.append(r5);	 Catch:{ all -> 0x00cd }
        r5 = ": ";
        r4.append(r5);	 Catch:{ all -> 0x00cd }
        r4.append(r9);	 Catch:{ all -> 0x00cd }
        r4 = r4.toString();	 Catch:{ all -> 0x00cd }
        r3.<init>(r4);	 Catch:{ all -> 0x00cd }
        throw r3;	 Catch:{ all -> 0x00cd }
    L_0x00b6:
        r2 = new java.lang.RuntimeException;	 Catch:{ all -> 0x00cd }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cd }
        r3.<init>();	 Catch:{ all -> 0x00cd }
        r4 = "Unable to resolve activity for: ";
        r3.append(r4);	 Catch:{ all -> 0x00cd }
        r3.append(r9);	 Catch:{ all -> 0x00cd }
        r3 = r3.toString();	 Catch:{ all -> 0x00cd }
        r2.<init>(r3);	 Catch:{ all -> 0x00cd }
        throw r2;	 Catch:{ all -> 0x00cd }
    L_0x00cd:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00cd }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.startActivitySync(android.content.Intent, android.os.Bundle):android.app.Activity");
    }

    public void addMonitor(ActivityMonitor monitor) {
        synchronized (this.mSync) {
            if (this.mActivityMonitors == null) {
                this.mActivityMonitors = new ArrayList();
            }
            this.mActivityMonitors.add(monitor);
        }
    }

    public ActivityMonitor addMonitor(IntentFilter filter, ActivityResult result, boolean block) {
        ActivityMonitor am = new ActivityMonitor(filter, result, block);
        addMonitor(am);
        return am;
    }

    public ActivityMonitor addMonitor(String cls, ActivityResult result, boolean block) {
        ActivityMonitor am = new ActivityMonitor(cls, result, block);
        addMonitor(am);
        return am;
    }

    public boolean checkMonitorHit(ActivityMonitor monitor, int minHits) {
        waitForIdleSync();
        synchronized (this.mSync) {
            if (monitor.getHits() < minHits) {
                return false;
            }
            this.mActivityMonitors.remove(monitor);
            return true;
        }
    }

    public Activity waitForMonitor(ActivityMonitor monitor) {
        Activity activity = monitor.waitForActivity();
        synchronized (this.mSync) {
            this.mActivityMonitors.remove(monitor);
        }
        return activity;
    }

    public Activity waitForMonitorWithTimeout(ActivityMonitor monitor, long timeOut) {
        Activity activity = monitor.waitForActivityWithTimeout(timeOut);
        synchronized (this.mSync) {
            this.mActivityMonitors.remove(monitor);
        }
        return activity;
    }

    public void removeMonitor(ActivityMonitor monitor) {
        synchronized (this.mSync) {
            this.mActivityMonitors.remove(monitor);
        }
    }

    public boolean invokeMenuActionSync(Activity targetActivity, int id, int flag) {
        AnonymousClass1MenuRunnable mr = new AnonymousClass1MenuRunnable(targetActivity, id, flag);
        runOnMainSync(mr);
        return mr.returnValue;
    }

    public boolean invokeContextMenuAction(Activity targetActivity, int id, int flag) {
        validateNotAppThread();
        sendKeySync(new KeyEvent(0, 23));
        waitForIdleSync();
        try {
            Thread.sleep((long) ViewConfiguration.getLongPressTimeout());
            sendKeySync(new KeyEvent(1, 23));
            waitForIdleSync();
            AnonymousClass1ContextMenuRunnable cmr = new AnonymousClass1ContextMenuRunnable(targetActivity, id, flag);
            runOnMainSync(cmr);
            return cmr.returnValue;
        } catch (InterruptedException e) {
            Log.e(TAG, "Could not sleep for long press timeout", e);
            return false;
        }
    }

    public void sendStringSync(String text) {
        if (text != null) {
            KeyEvent[] events = KeyCharacterMap.load(-1).getEvents(text.toCharArray());
            if (events != null) {
                for (KeyEvent changeTimeRepeat : events) {
                    sendKeySync(KeyEvent.changeTimeRepeat(changeTimeRepeat, SystemClock.uptimeMillis(), 0));
                }
            }
        }
    }

    public void sendKeySync(KeyEvent event) {
        validateNotAppThread();
        long downTime = event.getDownTime();
        long eventTime = event.getEventTime();
        int source = event.getSource();
        if (source == 0) {
            source = 257;
        }
        if (eventTime == 0) {
            eventTime = SystemClock.uptimeMillis();
        }
        if (downTime == 0) {
            downTime = eventTime;
        }
        KeyEvent newEvent = new KeyEvent(event);
        newEvent.setTime(downTime, eventTime);
        newEvent.setSource(source);
        newEvent.setFlags(event.getFlags() | 8);
        InputManager.getInstance().injectInputEvent(newEvent, 2);
    }

    public void sendKeyDownUpSync(int key) {
        sendKeySync(new KeyEvent(0, key));
        sendKeySync(new KeyEvent(1, key));
    }

    public void sendCharacterSync(int keyCode) {
        sendKeySync(new KeyEvent(0, keyCode));
        sendKeySync(new KeyEvent(1, keyCode));
    }

    public void sendPointerSync(MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & 2) == 0) {
            event.setSource(4098);
        }
        try {
            WindowManagerGlobal.getWindowManagerService().injectInputAfterTransactionsApplied(event, 2);
        } catch (RemoteException e) {
        }
    }

    public void sendTrackballEventSync(MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & 4) == 0) {
            event.setSource(65540);
        }
        InputManager.getInstance().injectInputEvent(event, 2);
    }

    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Application app = getFactory(context.getPackageName()).instantiateApplication(cl, className);
        app.attach(context);
        return app;
    }

    public static Application newApplication(Class<?> clazz, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Application app = (Application) clazz.newInstance();
        app.attach(context);
        return app;
    }

    public void callApplicationOnCreate(Application app) {
        app.onCreate();
    }

    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
        Application application2;
        Activity activity = (Activity) clazz.newInstance();
        if (application == null) {
            application2 = new Application();
        } else {
            application2 = application;
        }
        NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) lastNonConfigurationInstance;
        Configuration configuration = r1;
        Configuration configuration2 = new Configuration();
        activity.attach(context, null, this, token, 0, application2, intent, info, title, parent, id, nonConfigurationInstances, configuration, null, null, null, null, null);
        return activity;
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String pkg = (intent == null || intent.getComponent() == null) ? null : intent.getComponent().getPackageName();
        return getFactory(pkg).instantiateActivity(cl, className, intent);
    }

    private AppComponentFactory getFactory(String pkg) {
        String str = TAG;
        if (pkg == null) {
            Log.e(str, "No pkg specified, disabling AppComponentFactory");
            return AppComponentFactory.DEFAULT;
        }
        ActivityThread activityThread = this.mThread;
        if (activityThread == null) {
            Log.e(str, "Uninitialized ActivityThread, likely app-created Instrumentation, disabling AppComponentFactory", new Throwable());
            return AppComponentFactory.DEFAULT;
        }
        LoadedApk apk = activityThread.peekPackageInfo(pkg, true);
        if (apk == null) {
            apk = this.mThread.getSystemContext().mPackageInfo;
        }
        return apk.getAppFactory();
    }

    private void prePerformCreate(Activity activity) {
        if (this.mWaitingActivities != null) {
            synchronized (this.mSync) {
                int N = this.mWaitingActivities.size();
                for (int i = 0; i < N; i++) {
                    ActivityWaiter aw = (ActivityWaiter) this.mWaitingActivities.get(i);
                    if (aw.intent.filterEquals(activity.getIntent())) {
                        aw.activity = activity;
                        this.mMessageQueue.addIdleHandler(new ActivityGoing(aw));
                    }
                }
            }
        }
    }

    private void postPerformCreate(Activity activity) {
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    ((ActivityMonitor) this.mActivityMonitors.get(i)).match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        prePerformCreate(activity);
        activity.performCreate(icicle);
        postPerformCreate(activity);
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        prePerformCreate(activity);
        activity.performCreate(icicle, persistentState);
        postPerformCreate(activity);
    }

    public void callActivityOnDestroy(Activity activity) {
        activity.performDestroy();
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        activity.performRestoreInstanceState(savedInstanceState);
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
        activity.performRestoreInstanceState(savedInstanceState, persistentState);
    }

    public void callActivityOnPostCreate(Activity activity, Bundle savedInstanceState) {
        activity.onPostCreate(savedInstanceState);
    }

    public void callActivityOnPostCreate(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
        activity.onPostCreate(savedInstanceState, persistentState);
    }

    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        activity.performNewIntent(intent);
    }

    @UnsupportedAppUsage
    public void callActivityOnNewIntent(Activity activity, ReferrerIntent intent) {
        String oldReferrer = activity.mReferrer;
        if (intent != null) {
            try {
                activity.mReferrer = intent.mReferrer;
            } catch (Throwable th) {
                activity.mReferrer = oldReferrer;
            }
        }
        callActivityOnNewIntent(activity, intent != null ? new Intent((Intent) intent) : null);
        activity.mReferrer = oldReferrer;
    }

    public void callActivityOnStart(Activity activity) {
        activity.onStart();
    }

    public void callActivityOnRestart(Activity activity) {
        activity.onRestart();
    }

    public void callActivityOnResume(Activity activity) {
        activity.mResumed = true;
        activity.onResume();
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    ((ActivityMonitor) this.mActivityMonitors.get(i)).match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    public void callActivityOnStop(Activity activity) {
        activity.onStop();
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        activity.performSaveInstanceState(outState);
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState, PersistableBundle outPersistentState) {
        activity.performSaveInstanceState(outState, outPersistentState);
    }

    public void callActivityOnPause(Activity activity) {
        activity.performPause();
    }

    public void callActivityOnUserLeaving(Activity activity) {
        activity.performUserLeaving();
    }

    @Deprecated
    public void startAllocCounting() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().gc();
        Debug.resetAllCounts();
        Debug.startAllocCounting();
    }

    @Deprecated
    public void stopAllocCounting() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().gc();
        Debug.stopAllocCounting();
    }

    private void addValue(String key, int value, Bundle results) {
        if (results.containsKey(key)) {
            List<Integer> list = results.getIntegerArrayList(key);
            if (list != null) {
                list.add(Integer.valueOf(value));
                return;
            }
            return;
        }
        ArrayList<Integer> list2 = new ArrayList();
        list2.add(Integer.valueOf(value));
        results.putIntegerArrayList(key, list2);
    }

    public Bundle getAllocCounts() {
        Bundle results = new Bundle();
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_ALLOC_COUNT, (long) Debug.getGlobalAllocCount());
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_ALLOC_SIZE, (long) Debug.getGlobalAllocSize());
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_FREED_COUNT, (long) Debug.getGlobalFreedCount());
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_FREED_SIZE, (long) Debug.getGlobalFreedSize());
        results.putLong(PerformanceCollector.METRIC_KEY_GC_INVOCATION_COUNT, (long) Debug.getGlobalGcInvocationCount());
        return results;
    }

    public Bundle getBinderCounts() {
        Bundle results = new Bundle();
        results.putLong(PerformanceCollector.METRIC_KEY_SENT_TRANSACTIONS, (long) Debug.getBinderSentTransactions());
        results.putLong(PerformanceCollector.METRIC_KEY_RECEIVED_TRANSACTIONS, (long) Debug.getBinderReceivedTransactions());
        return results;
    }

    /* JADX WARNING: Missing block: B:28:0x006d, code skipped:
            return r0;
     */
    @android.annotation.UnsupportedAppUsage
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context r19, android.os.IBinder r20, android.os.IBinder r21, android.app.Activity r22, android.content.Intent r23, int r24, android.os.Bundle r25) {
        /*
        r18 = this;
        r1 = r18;
        r2 = r19;
        r3 = r22;
        r15 = r23;
        r0 = r23.toString();
        r4 = 377; // 0x179 float:5.28E-43 double:1.863E-321;
        android.util.SeempLog.record_str(r4, r0);
        r16 = r20;
        r16 = (android.app.IApplicationThread) r16;
        r0 = 0;
        if (r3 == 0) goto L_0x001d;
    L_0x0018:
        r4 = r22.onProvideReferrer();
        goto L_0x001e;
    L_0x001d:
        r4 = r0;
    L_0x001e:
        r14 = r4;
        if (r14 == 0) goto L_0x0026;
    L_0x0021:
        r4 = "android.intent.extra.REFERRER";
        r15.putExtra(r4, r14);
    L_0x0026:
        r4 = r1.mActivityMonitors;
        if (r4 == 0) goto L_0x0076;
    L_0x002a:
        r4 = r1.mSync;
        monitor-enter(r4);
        r5 = r1.mActivityMonitors;	 Catch:{ all -> 0x0073 }
        r5 = r5.size();	 Catch:{ all -> 0x0073 }
        r6 = 0;
    L_0x0034:
        if (r6 >= r5) goto L_0x0071;
    L_0x0036:
        r7 = r1.mActivityMonitors;	 Catch:{ all -> 0x0073 }
        r7 = r7.get(r6);	 Catch:{ all -> 0x0073 }
        r7 = (android.app.Instrumentation.ActivityMonitor) r7;	 Catch:{ all -> 0x0073 }
        r8 = 0;
        r9 = r7.ignoreMatchingSpecificIntents();	 Catch:{ all -> 0x0073 }
        if (r9 == 0) goto L_0x004a;
    L_0x0045:
        r9 = r7.onStartActivity(r15);	 Catch:{ all -> 0x0073 }
        r8 = r9;
    L_0x004a:
        if (r8 == 0) goto L_0x0054;
    L_0x004c:
        r0 = r7.mHits;	 Catch:{ all -> 0x0073 }
        r0 = r0 + 1;
        r7.mHits = r0;	 Catch:{ all -> 0x0073 }
        monitor-exit(r4);	 Catch:{ all -> 0x0073 }
        return r8;
    L_0x0054:
        r9 = r7.match(r2, r0, r15);	 Catch:{ all -> 0x0073 }
        if (r9 == 0) goto L_0x006e;
    L_0x005a:
        r9 = r7.mHits;	 Catch:{ all -> 0x0073 }
        r9 = r9 + 1;
        r7.mHits = r9;	 Catch:{ all -> 0x0073 }
        r9 = r7.isBlocking();	 Catch:{ all -> 0x0073 }
        if (r9 == 0) goto L_0x0071;
    L_0x0066:
        if (r24 < 0) goto L_0x006c;
    L_0x0068:
        r0 = r7.getResult();	 Catch:{ all -> 0x0073 }
    L_0x006c:
        monitor-exit(r4);	 Catch:{ all -> 0x0073 }
        return r0;
    L_0x006e:
        r6 = r6 + 1;
        goto L_0x0034;
    L_0x0071:
        monitor-exit(r4);	 Catch:{ all -> 0x0073 }
        goto L_0x0076;
    L_0x0073:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0073 }
        throw r0;
    L_0x0076:
        r23.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x00ca }
        r15.prepareToLeaveProcess(r2);	 Catch:{ RemoteException -> 0x00ca }
        r4 = "Timeline";
        r5 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x00ca }
        r5.<init>();	 Catch:{ RemoteException -> 0x00ca }
        r6 = "Timeline: Activity_launch_request time:";
        r5.append(r6);	 Catch:{ RemoteException -> 0x00ca }
        r6 = android.os.SystemClock.uptimeMillis();	 Catch:{ RemoteException -> 0x00ca }
        r5.append(r6);	 Catch:{ RemoteException -> 0x00ca }
        r5 = r5.toString();	 Catch:{ RemoteException -> 0x00ca }
        android.util.Slog.i(r4, r5);	 Catch:{ RemoteException -> 0x00ca }
        r4 = android.app.ActivityTaskManager.getService();	 Catch:{ RemoteException -> 0x00ca }
        r6 = r19.getBasePackageName();	 Catch:{ RemoteException -> 0x00ca }
        r5 = r19.getContentResolver();	 Catch:{ RemoteException -> 0x00ca }
        r8 = r15.resolveTypeIfNeeded(r5);	 Catch:{ RemoteException -> 0x00ca }
        if (r3 == 0) goto L_0x00b0;
    L_0x00a8:
        r5 = r3.mEmbeddedID;	 Catch:{ RemoteException -> 0x00ac }
        r10 = r5;
        goto L_0x00b1;
    L_0x00ac:
        r0 = move-exception;
        r17 = r14;
        goto L_0x00cd;
    L_0x00b0:
        r10 = r0;
    L_0x00b1:
        r12 = 0;
        r13 = 0;
        r5 = r16;
        r7 = r23;
        r9 = r21;
        r11 = r24;
        r17 = r14;
        r14 = r25;
        r4 = r4.startActivity(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14);	 Catch:{ RemoteException -> 0x00c8 }
        checkStartActivityResult(r4, r15);	 Catch:{ RemoteException -> 0x00c8 }
        return r0;
    L_0x00c8:
        r0 = move-exception;
        goto L_0x00cd;
    L_0x00ca:
        r0 = move-exception;
        r17 = r14;
    L_0x00cd:
        r4 = new java.lang.RuntimeException;
        r5 = "Failure from system";
        r4.<init>(r5, r0);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivity(android.content.Context, android.os.IBinder, android.os.IBinder, android.app.Activity, android.content.Intent, int, android.os.Bundle):android.app.Instrumentation$ActivityResult");
    }

    @UnsupportedAppUsage
    public void execStartActivities(Context who, IBinder contextThread, IBinder token, Activity target, Intent[] intents, Bundle options) {
        execStartActivitiesAsUser(who, contextThread, token, target, intents, options, who.getUserId());
    }

    @UnsupportedAppUsage
    public int execStartActivitiesAsUser(Context who, IBinder contextThread, IBinder token, Activity target, Intent[] intents, Bundle options, int userId) {
        Context context = who;
        Intent[] intentArr = intents;
        SeempLog.record_str(378, intents.toString());
        IApplicationThread whoThread = (IApplicationThread) contextThread;
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                int i = 0;
                while (i < N) {
                    ActivityMonitor am = (ActivityMonitor) this.mActivityMonitors.get(i);
                    ActivityResult result = null;
                    if (am.ignoreMatchingSpecificIntents()) {
                        result = am.onStartActivity(intentArr[0]);
                    }
                    if (result != null) {
                        am.mHits++;
                        return -96;
                    } else if (am.match(who, null, intentArr[0])) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return -96;
                        }
                    } else {
                        i++;
                    }
                }
            }
        }
        try {
            int i2;
            String[] resolvedTypes = new String[intentArr.length];
            for (i2 = 0; i2 < intentArr.length; i2++) {
                intentArr[i2].migrateExtraStreamToClipData();
                intentArr[i2].prepareToLeaveProcess(who);
                resolvedTypes[i2] = intentArr[i2].resolveTypeIfNeeded(who.getContentResolver());
            }
            i2 = ActivityTaskManager.getService().startActivities(whoThread, who.getBasePackageName(), intents, resolvedTypes, token, options, userId);
            checkStartActivityResult(i2, intentArr[0]);
            return i2;
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    /* JADX WARNING: Missing block: B:21:0x005b, code skipped:
            return r13;
     */
    @android.annotation.UnsupportedAppUsage
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context r17, android.os.IBinder r18, android.os.IBinder r19, java.lang.String r20, android.content.Intent r21, int r22, android.os.Bundle r23) {
        /*
        r16 = this;
        r1 = r16;
        r2 = r17;
        r14 = r21;
        r0 = r21.toString();
        r3 = 377; // 0x179 float:5.28E-43 double:1.863E-321;
        android.util.SeempLog.record_str(r3, r0);
        r15 = r18;
        r15 = (android.app.IApplicationThread) r15;
        r0 = r1.mActivityMonitors;
        r13 = 0;
        if (r0 == 0) goto L_0x0064;
    L_0x0018:
        r3 = r1.mSync;
        monitor-enter(r3);
        r0 = r1.mActivityMonitors;	 Catch:{ all -> 0x0061 }
        r0 = r0.size();	 Catch:{ all -> 0x0061 }
        r4 = 0;
    L_0x0022:
        if (r4 >= r0) goto L_0x005f;
    L_0x0024:
        r5 = r1.mActivityMonitors;	 Catch:{ all -> 0x0061 }
        r5 = r5.get(r4);	 Catch:{ all -> 0x0061 }
        r5 = (android.app.Instrumentation.ActivityMonitor) r5;	 Catch:{ all -> 0x0061 }
        r6 = 0;
        r7 = r5.ignoreMatchingSpecificIntents();	 Catch:{ all -> 0x0061 }
        if (r7 == 0) goto L_0x0038;
    L_0x0033:
        r7 = r5.onStartActivity(r14);	 Catch:{ all -> 0x0061 }
        r6 = r7;
    L_0x0038:
        if (r6 == 0) goto L_0x0042;
    L_0x003a:
        r7 = r5.mHits;	 Catch:{ all -> 0x0061 }
        r7 = r7 + 1;
        r5.mHits = r7;	 Catch:{ all -> 0x0061 }
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        return r6;
    L_0x0042:
        r7 = r5.match(r2, r13, r14);	 Catch:{ all -> 0x0061 }
        if (r7 == 0) goto L_0x005c;
    L_0x0048:
        r7 = r5.mHits;	 Catch:{ all -> 0x0061 }
        r7 = r7 + 1;
        r5.mHits = r7;	 Catch:{ all -> 0x0061 }
        r7 = r5.isBlocking();	 Catch:{ all -> 0x0061 }
        if (r7 == 0) goto L_0x005f;
    L_0x0054:
        if (r22 < 0) goto L_0x005a;
    L_0x0056:
        r13 = r5.getResult();	 Catch:{ all -> 0x0061 }
    L_0x005a:
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        return r13;
    L_0x005c:
        r4 = r4 + 1;
        goto L_0x0022;
    L_0x005f:
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        goto L_0x0064;
    L_0x0061:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        throw r0;
    L_0x0064:
        r21.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x0091 }
        r14.prepareToLeaveProcess(r2);	 Catch:{ RemoteException -> 0x0091 }
        r3 = android.app.ActivityTaskManager.getService();	 Catch:{ RemoteException -> 0x0091 }
        r5 = r17.getBasePackageName();	 Catch:{ RemoteException -> 0x0091 }
        r0 = r17.getContentResolver();	 Catch:{ RemoteException -> 0x0091 }
        r7 = r14.resolveTypeIfNeeded(r0);	 Catch:{ RemoteException -> 0x0091 }
        r11 = 0;
        r12 = 0;
        r4 = r15;
        r6 = r21;
        r8 = r19;
        r9 = r20;
        r10 = r22;
        r0 = r13;
        r13 = r23;
        r3 = r3.startActivity(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);	 Catch:{ RemoteException -> 0x0091 }
        checkStartActivityResult(r3, r14);	 Catch:{ RemoteException -> 0x0091 }
        return r0;
    L_0x0091:
        r0 = move-exception;
        r3 = new java.lang.RuntimeException;
        r4 = "Failure from system";
        r3.<init>(r4, r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivity(android.content.Context, android.os.IBinder, android.os.IBinder, java.lang.String, android.content.Intent, int, android.os.Bundle):android.app.Instrumentation$ActivityResult");
    }

    /* JADX WARNING: Missing block: B:21:0x005b, code skipped:
            return r14;
     */
    @android.annotation.UnsupportedAppUsage
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context r19, android.os.IBinder r20, android.os.IBinder r21, java.lang.String r22, android.content.Intent r23, int r24, android.os.Bundle r25, android.os.UserHandle r26) {
        /*
        r18 = this;
        r1 = r18;
        r2 = r19;
        r15 = r23;
        r0 = r23.toString();
        r3 = 377; // 0x179 float:5.28E-43 double:1.863E-321;
        android.util.SeempLog.record_str(r3, r0);
        r16 = r20;
        r16 = (android.app.IApplicationThread) r16;
        r0 = r1.mActivityMonitors;
        r14 = 0;
        if (r0 == 0) goto L_0x0064;
    L_0x0018:
        r3 = r1.mSync;
        monitor-enter(r3);
        r0 = r1.mActivityMonitors;	 Catch:{ all -> 0x0061 }
        r0 = r0.size();	 Catch:{ all -> 0x0061 }
        r4 = 0;
    L_0x0022:
        if (r4 >= r0) goto L_0x005f;
    L_0x0024:
        r5 = r1.mActivityMonitors;	 Catch:{ all -> 0x0061 }
        r5 = r5.get(r4);	 Catch:{ all -> 0x0061 }
        r5 = (android.app.Instrumentation.ActivityMonitor) r5;	 Catch:{ all -> 0x0061 }
        r6 = 0;
        r7 = r5.ignoreMatchingSpecificIntents();	 Catch:{ all -> 0x0061 }
        if (r7 == 0) goto L_0x0038;
    L_0x0033:
        r7 = r5.onStartActivity(r15);	 Catch:{ all -> 0x0061 }
        r6 = r7;
    L_0x0038:
        if (r6 == 0) goto L_0x0042;
    L_0x003a:
        r7 = r5.mHits;	 Catch:{ all -> 0x0061 }
        r7 = r7 + 1;
        r5.mHits = r7;	 Catch:{ all -> 0x0061 }
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        return r6;
    L_0x0042:
        r7 = r5.match(r2, r14, r15);	 Catch:{ all -> 0x0061 }
        if (r7 == 0) goto L_0x005c;
    L_0x0048:
        r7 = r5.mHits;	 Catch:{ all -> 0x0061 }
        r7 = r7 + 1;
        r5.mHits = r7;	 Catch:{ all -> 0x0061 }
        r7 = r5.isBlocking();	 Catch:{ all -> 0x0061 }
        if (r7 == 0) goto L_0x005f;
    L_0x0054:
        if (r24 < 0) goto L_0x005a;
    L_0x0056:
        r14 = r5.getResult();	 Catch:{ all -> 0x0061 }
    L_0x005a:
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        return r14;
    L_0x005c:
        r4 = r4 + 1;
        goto L_0x0022;
    L_0x005f:
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        goto L_0x0064;
    L_0x0061:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0061 }
        throw r0;
    L_0x0064:
        r23.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x0098 }
        r15.prepareToLeaveProcess(r2);	 Catch:{ RemoteException -> 0x0098 }
        r3 = android.app.ActivityTaskManager.getService();	 Catch:{ RemoteException -> 0x0098 }
        r5 = r19.getBasePackageName();	 Catch:{ RemoteException -> 0x0098 }
        r0 = r19.getContentResolver();	 Catch:{ RemoteException -> 0x0098 }
        r7 = r15.resolveTypeIfNeeded(r0);	 Catch:{ RemoteException -> 0x0098 }
        r11 = 0;
        r12 = 0;
        r0 = r26.getIdentifier();	 Catch:{ RemoteException -> 0x0098 }
        r4 = r16;
        r6 = r23;
        r8 = r21;
        r9 = r22;
        r10 = r24;
        r13 = r25;
        r17 = r14;
        r14 = r0;
        r0 = r3.startActivityAsUser(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14);	 Catch:{ RemoteException -> 0x0098 }
        checkStartActivityResult(r0, r15);	 Catch:{ RemoteException -> 0x0098 }
        return r17;
    L_0x0098:
        r0 = move-exception;
        r3 = new java.lang.RuntimeException;
        r4 = "Failure from system";
        r3.<init>(r4, r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivity(android.content.Context, android.os.IBinder, android.os.IBinder, java.lang.String, android.content.Intent, int, android.os.Bundle, android.os.UserHandle):android.app.Instrumentation$ActivityResult");
    }

    /* JADX WARNING: Missing block: B:21:0x0054, code skipped:
            return r14;
     */
    @android.annotation.UnsupportedAppUsage
    public android.app.Instrumentation.ActivityResult execStartActivityAsCaller(android.content.Context r20, android.os.IBinder r21, android.os.IBinder r22, android.app.Activity r23, android.content.Intent r24, int r25, android.os.Bundle r26, android.os.IBinder r27, boolean r28, int r29) {
        /*
        r19 = this;
        r1 = r19;
        r2 = r20;
        r3 = r23;
        r15 = r24;
        r18 = r21;
        r18 = (android.app.IApplicationThread) r18;
        r0 = r1.mActivityMonitors;
        r14 = 0;
        if (r0 == 0) goto L_0x005d;
    L_0x0011:
        r4 = r1.mSync;
        monitor-enter(r4);
        r0 = r1.mActivityMonitors;	 Catch:{ all -> 0x005a }
        r0 = r0.size();	 Catch:{ all -> 0x005a }
        r5 = 0;
    L_0x001b:
        if (r5 >= r0) goto L_0x0058;
    L_0x001d:
        r6 = r1.mActivityMonitors;	 Catch:{ all -> 0x005a }
        r6 = r6.get(r5);	 Catch:{ all -> 0x005a }
        r6 = (android.app.Instrumentation.ActivityMonitor) r6;	 Catch:{ all -> 0x005a }
        r7 = 0;
        r8 = r6.ignoreMatchingSpecificIntents();	 Catch:{ all -> 0x005a }
        if (r8 == 0) goto L_0x0031;
    L_0x002c:
        r8 = r6.onStartActivity(r15);	 Catch:{ all -> 0x005a }
        r7 = r8;
    L_0x0031:
        if (r7 == 0) goto L_0x003b;
    L_0x0033:
        r8 = r6.mHits;	 Catch:{ all -> 0x005a }
        r8 = r8 + 1;
        r6.mHits = r8;	 Catch:{ all -> 0x005a }
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        return r7;
    L_0x003b:
        r8 = r6.match(r2, r14, r15);	 Catch:{ all -> 0x005a }
        if (r8 == 0) goto L_0x0055;
    L_0x0041:
        r8 = r6.mHits;	 Catch:{ all -> 0x005a }
        r8 = r8 + 1;
        r6.mHits = r8;	 Catch:{ all -> 0x005a }
        r8 = r6.isBlocking();	 Catch:{ all -> 0x005a }
        if (r8 == 0) goto L_0x0058;
    L_0x004d:
        if (r25 < 0) goto L_0x0053;
    L_0x004f:
        r14 = r6.getResult();	 Catch:{ all -> 0x005a }
    L_0x0053:
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        return r14;
    L_0x0055:
        r5 = r5 + 1;
        goto L_0x001b;
    L_0x0058:
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        goto L_0x005d;
    L_0x005a:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        throw r0;
    L_0x005d:
        r24.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x009e }
        r15.prepareToLeaveProcess(r2);	 Catch:{ RemoteException -> 0x009e }
        r4 = android.app.ActivityTaskManager.getService();	 Catch:{ RemoteException -> 0x009e }
        r6 = r20.getBasePackageName();	 Catch:{ RemoteException -> 0x009e }
        r0 = r20.getContentResolver();	 Catch:{ RemoteException -> 0x009e }
        r8 = r15.resolveTypeIfNeeded(r0);	 Catch:{ RemoteException -> 0x009e }
        if (r3 == 0) goto L_0x0079;
    L_0x0075:
        r0 = r3.mEmbeddedID;	 Catch:{ RemoteException -> 0x009e }
        r10 = r0;
        goto L_0x007a;
    L_0x0079:
        r10 = r14;
    L_0x007a:
        r12 = 0;
        r13 = 0;
        r5 = r18;
        r7 = r24;
        r9 = r22;
        r11 = r25;
        r0 = r14;
        r14 = r26;
        r15 = r27;
        r16 = r28;
        r17 = r29;
        r4 = r4.startActivityAsCaller(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);	 Catch:{ RemoteException -> 0x009a }
        r5 = r24;
        checkStartActivityResult(r4, r5);	 Catch:{ RemoteException -> 0x0098 }
        return r0;
    L_0x0098:
        r0 = move-exception;
        goto L_0x00a0;
    L_0x009a:
        r0 = move-exception;
        r5 = r24;
        goto L_0x00a0;
    L_0x009e:
        r0 = move-exception;
        r5 = r15;
    L_0x00a0:
        r4 = new java.lang.RuntimeException;
        r6 = "Failure from system";
        r4.<init>(r6, r0);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivityAsCaller(android.content.Context, android.os.IBinder, android.os.IBinder, android.app.Activity, android.content.Intent, int, android.os.Bundle, android.os.IBinder, boolean, int):android.app.Instrumentation$ActivityResult");
    }

    @UnsupportedAppUsage
    public void execStartActivityFromAppTask(Context who, IBinder contextThread, IAppTask appTask, Intent intent, Bundle options) {
        SeempLog.record_str(380, intent.toString());
        IApplicationThread whoThread = (IApplicationThread) contextThread;
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                int i = 0;
                while (i < N) {
                    ActivityMonitor am = (ActivityMonitor) this.mActivityMonitors.get(i);
                    ActivityResult result = null;
                    if (am.ignoreMatchingSpecificIntents()) {
                        result = am.onStartActivity(intent);
                    }
                    if (result != null) {
                        am.mHits++;
                        return;
                    } else if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return;
                        }
                    } else {
                        i++;
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            checkStartActivityResult(appTask.startActivity(whoThread.asBinder(), who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), options), intent);
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    /* Access modifiers changed, original: final */
    public final void init(ActivityThread thread, Context instrContext, Context appContext, ComponentName component, IInstrumentationWatcher watcher, IUiAutomationConnection uiAutomationConnection) {
        this.mThread = thread;
        this.mThread.getLooper();
        this.mMessageQueue = Looper.myQueue();
        this.mInstrContext = instrContext;
        this.mAppContext = appContext;
        this.mComponent = component;
        this.mWatcher = watcher;
        this.mUiAutomationConnection = uiAutomationConnection;
    }

    /* Access modifiers changed, original: final */
    public final void basicInit(ActivityThread thread) {
        this.mThread = thread;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static void checkStartActivityResult(int res, Object intent) {
        if (ActivityManager.isStartResultFatalError(res)) {
            StringBuilder stringBuilder;
            switch (res) {
                case -100:
                    throw new IllegalStateException("Cannot start voice activity on a hidden session");
                case ActivityManager.START_VOICE_NOT_ACTIVE_SESSION /*-99*/:
                    throw new IllegalStateException("Session calling startVoiceActivity does not match active session");
                case ActivityManager.START_NOT_VOICE_COMPATIBLE /*-97*/:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Starting under voice control not allowed for: ");
                    stringBuilder.append(intent);
                    throw new SecurityException(stringBuilder.toString());
                case ActivityManager.START_CANCELED /*-96*/:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Activity could not be started for ");
                    stringBuilder.append(intent);
                    throw new AndroidRuntimeException(stringBuilder.toString());
                case ActivityManager.START_NOT_ACTIVITY /*-95*/:
                    throw new IllegalArgumentException("PendingIntent is not an activity");
                case ActivityManager.START_PERMISSION_DENIED /*-94*/:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Not allowed to start activity ");
                    stringBuilder.append(intent);
                    throw new SecurityException(stringBuilder.toString());
                case ActivityManager.START_FORWARD_AND_REQUEST_CONFLICT /*-93*/:
                    throw new AndroidRuntimeException("FORWARD_RESULT_FLAG used while also requesting a result");
                case ActivityManager.START_CLASS_NOT_FOUND /*-92*/:
                case ActivityManager.START_INTENT_NOT_RESOLVED /*-91*/:
                    if (!(intent instanceof Intent) || ((Intent) intent).getComponent() == null) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("No Activity found to handle ");
                        stringBuilder.append(intent);
                        throw new ActivityNotFoundException(stringBuilder.toString());
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to find explicit activity class ");
                    stringBuilder.append(((Intent) intent).getComponent().toShortString());
                    stringBuilder.append("; have you declared this activity in your AndroidManifest.xml?");
                    throw new ActivityNotFoundException(stringBuilder.toString());
                case ActivityManager.START_ASSISTANT_HIDDEN_SESSION /*-90*/:
                    throw new IllegalStateException("Cannot start assistant activity on a hidden session");
                case ActivityManager.START_ASSISTANT_NOT_ACTIVE_SESSION /*-89*/:
                    throw new IllegalStateException("Session calling startAssistantActivity does not match active session");
                default:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown error code ");
                    stringBuilder.append(res);
                    stringBuilder.append(" when starting ");
                    stringBuilder.append(intent);
                    throw new AndroidRuntimeException(stringBuilder.toString());
            }
        }
    }

    private final void validateNotAppThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("This method can not be called from the main application thread");
        }
    }

    public UiAutomation getUiAutomation() {
        return getUiAutomation(0);
    }

    public UiAutomation getUiAutomation(int flags) {
        UiAutomation uiAutomation = this.mUiAutomation;
        boolean mustCreateNewAutomation = uiAutomation == null || uiAutomation.isDestroyed();
        if (this.mUiAutomationConnection == null) {
            return null;
        }
        if (!mustCreateNewAutomation && this.mUiAutomation.getFlags() == flags) {
            return this.mUiAutomation;
        }
        if (mustCreateNewAutomation) {
            this.mUiAutomation = new UiAutomation(getTargetContext().getMainLooper(), this.mUiAutomationConnection);
        } else {
            this.mUiAutomation.disconnect();
        }
        this.mUiAutomation.connect(flags);
        return this.mUiAutomation;
    }

    public TestLooperManager acquireLooperManager(Looper looper) {
        checkInstrumenting("acquireLooperManager");
        return new TestLooperManager(looper);
    }
}
