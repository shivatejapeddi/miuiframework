package android.view.inputmethod;

import android.Manifest.permission;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.SparseArray;
import android.view.ImeInsetsSourceConsumer;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.autofill.AutofillManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.InputMethodPrivilegedOperationsRegistry;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputConnectionWrapper;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodClient;
import com.android.internal.view.IInputMethodClient.Stub;
import com.android.internal.view.IInputMethodManager;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.InputBindResult;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class InputMethodManager {
    static final boolean DEBUG = false;
    public static final int DISPATCH_HANDLED = 1;
    public static final int DISPATCH_IN_PROGRESS = -1;
    public static final int DISPATCH_NOT_HANDLED = 0;
    public static final int HIDE_IMPLICIT_ONLY = 1;
    public static final int HIDE_NOT_ALWAYS = 2;
    static final long INPUT_METHOD_NOT_RESPONDING_TIMEOUT = 2500;
    static final int MSG_APPLY_IME_VISIBILITY = 20;
    static final int MSG_BIND = 2;
    static final int MSG_DUMP = 1;
    static final int MSG_FLUSH_INPUT_EVENT = 7;
    static final int MSG_REPORT_FULLSCREEN_MODE = 10;
    static final int MSG_REPORT_PRE_RENDERED = 15;
    static final int MSG_SEND_INPUT_EVENT = 5;
    static final int MSG_SET_ACTIVE = 4;
    static final int MSG_TIMEOUT_INPUT_EVENT = 6;
    static final int MSG_UNBIND = 3;
    static final int MSG_UPDATE_ACTIVITY_VIEW_TO_SCREEN_MATRIX = 30;
    private static final int NOT_A_SUBTYPE_ID = -1;
    static final String PENDING_EVENT_COUNTER = "aq:imm";
    private static final int REQUEST_UPDATE_CURSOR_ANCHOR_INFO_NONE = 0;
    public static final int RESULT_HIDDEN = 3;
    public static final int RESULT_SHOWN = 2;
    public static final int RESULT_UNCHANGED_HIDDEN = 1;
    public static final int RESULT_UNCHANGED_SHOWN = 0;
    public static final int SHOW_FORCED = 2;
    public static final int SHOW_IMPLICIT = 1;
    public static final int SHOW_IM_PICKER_MODE_AUTO = 0;
    public static final int SHOW_IM_PICKER_MODE_EXCLUDE_AUXILIARY_SUBTYPES = 2;
    public static final int SHOW_IM_PICKER_MODE_INCLUDE_AUXILIARY_SUBTYPES = 1;
    private static final String SUBTYPE_MODE_VOICE = "voice";
    static final String TAG = "InputMethodManager";
    @GuardedBy({"sLock"})
    @Deprecated
    @UnsupportedAppUsage
    static InputMethodManager sInstance;
    @GuardedBy({"sLock"})
    private static final SparseArray<InputMethodManager> sInstanceMap = new SparseArray();
    private static final Object sLock = new Object();
    boolean mActive = false;
    private Matrix mActivityViewToScreenMatrix = null;
    int mBindSequence = -1;
    final Stub mClient = new Stub() {
        /* Access modifiers changed, original: protected */
        public void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
            CountDownLatch latch = new CountDownLatch(1);
            SomeArgs sargs = SomeArgs.obtain();
            sargs.arg1 = fd;
            sargs.arg2 = fout;
            sargs.arg3 = args;
            sargs.arg4 = latch;
            InputMethodManager.this.mH.sendMessage(InputMethodManager.this.mH.obtainMessage(1, sargs));
            try {
                if (!latch.await(5, TimeUnit.SECONDS)) {
                    fout.println("Timeout waiting for dump");
                }
            } catch (InterruptedException e) {
                fout.println("Interrupted waiting for dump");
            }
        }

        public void onBindMethod(InputBindResult res) {
            InputMethodManager.this.mH.obtainMessage(2, res).sendToTarget();
        }

        public void onUnbindMethod(int sequence, int unbindReason) {
            InputMethodManager.this.mH.obtainMessage(3, sequence, unbindReason).sendToTarget();
        }

        public void setActive(boolean active, boolean fullscreen) {
            InputMethodManager.this.mH.obtainMessage(4, active, fullscreen).sendToTarget();
        }

        public void reportFullscreenMode(boolean fullscreen) {
            InputMethodManager.this.mH.obtainMessage(10, fullscreen, 0).sendToTarget();
        }

        public void reportPreRendered(EditorInfo info) {
            InputMethodManager.this.mH.obtainMessage(15, 0, 0, info).sendToTarget();
        }

        public void applyImeVisibility(boolean setVisible) {
            InputMethodManager.this.mH.obtainMessage(20, setVisible, 0).sendToTarget();
        }

        public void updateActivityViewToScreenMatrix(int bindSequence, float[] matrixValues) {
            InputMethodManager.this.mH.obtainMessage(30, bindSequence, 0, matrixValues).sendToTarget();
        }
    };
    CompletionInfo[] mCompletions;
    InputChannel mCurChannel;
    @UnsupportedAppUsage
    String mCurId;
    @UnsupportedAppUsage
    IInputMethodSession mCurMethod;
    @UnsupportedAppUsage
    View mCurRootView;
    ImeInputEventSender mCurSender;
    EditorInfo mCurrentTextBoxAttribute;
    private CursorAnchorInfo mCursorAnchorInfo = null;
    int mCursorCandEnd;
    int mCursorCandStart;
    @UnsupportedAppUsage
    Rect mCursorRect = new Rect();
    int mCursorSelEnd;
    int mCursorSelStart;
    private final int mDisplayId;
    final InputConnection mDummyInputConnection = new BaseInputConnection(this, false);
    boolean mFullscreenMode;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    final H mH;
    final IInputContext mIInputContext;
    private ImeInsetsSourceConsumer mImeInsetsConsumer;
    final Looper mMainLooper;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    View mNextServedView;
    final Pool<PendingEvent> mPendingEventPool = new SimplePool(20);
    final SparseArray<PendingEvent> mPendingEvents = new SparseArray(20);
    private int mRequestUpdateCursorAnchorInfoMonitorMode = 0;
    boolean mRestartOnNextWindowFocus = true;
    boolean mServedConnecting;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    ControlledInputConnectionWrapper mServedInputConnectionWrapper;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    View mServedView;
    @UnsupportedAppUsage
    final IInputMethodManager mService;
    @UnsupportedAppUsage
    Rect mTmpCursorRect = new Rect();

    public interface FinishedInputEventCallback {
        void onFinishedInputEvent(Object obj, boolean z);
    }

    private static class ControlledInputConnectionWrapper extends IInputConnectionWrapper {
        private final InputMethodManager mParentInputMethodManager;

        public ControlledInputConnectionWrapper(Looper mainLooper, InputConnection conn, InputMethodManager inputMethodManager) {
            super(mainLooper, conn);
            this.mParentInputMethodManager = inputMethodManager;
        }

        public boolean isActive() {
            return this.mParentInputMethodManager.mActive && !isFinished();
        }

        /* Access modifiers changed, original: 0000 */
        public void deactivate() {
            if (!isFinished()) {
                closeConnection();
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ControlledInputConnectionWrapper{connection=");
            stringBuilder.append(getInputConnection());
            stringBuilder.append(" finished=");
            stringBuilder.append(isFinished());
            stringBuilder.append(" mParentInputMethodManager.mActive=");
            stringBuilder.append(this.mParentInputMethodManager.mActive);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    class H extends Handler {
        H(Looper looper) {
            super(looper, null, true);
        }

        /* JADX WARNING: Missing block: B:62:0x00bd, code skipped:
            if (r2 == false) goto L_0x00c9;
     */
        /* JADX WARNING: Missing block: B:63:0x00bf, code skipped:
            r11.this$0.startInputInner(6, null, 0, 0, 0);
     */
        /* JADX WARNING: Missing block: B:64:0x00c9, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:88:0x014e, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:145:0x0229, code skipped:
            return;
     */
        public void handleMessage(android.os.Message r12) {
            /*
            r11 = this;
            r0 = r12.what;
            r1 = 10;
            r2 = 1;
            r3 = 0;
            if (r0 == r1) goto L_0x026d;
        L_0x0008:
            r1 = 15;
            if (r0 == r1) goto L_0x024e;
        L_0x000c:
            r1 = 20;
            if (r0 == r1) goto L_0x022d;
        L_0x0010:
            r1 = 30;
            if (r0 == r1) goto L_0x0193;
        L_0x0014:
            switch(r0) {
                case 1: goto L_0x0152;
                case 2: goto L_0x00cd;
                case 3: goto L_0x008e;
                case 4: goto L_0x0032;
                case 5: goto L_0x0028;
                case 6: goto L_0x0020;
                case 7: goto L_0x0018;
                default: goto L_0x0017;
            };
        L_0x0017:
            return;
        L_0x0018:
            r0 = android.view.inputmethod.InputMethodManager.this;
            r1 = r12.arg1;
            r0.finishedInputEvent(r1, r3, r3);
            return;
        L_0x0020:
            r0 = android.view.inputmethod.InputMethodManager.this;
            r1 = r12.arg1;
            r0.finishedInputEvent(r1, r3, r2);
            return;
        L_0x0028:
            r0 = android.view.inputmethod.InputMethodManager.this;
            r1 = r12.obj;
            r1 = (android.view.inputmethod.InputMethodManager.PendingEvent) r1;
            r0.sendInputEventAndReportResultOnMainLooper(r1);
            return;
        L_0x0032:
            r0 = r12.arg1;
            if (r0 == 0) goto L_0x0038;
        L_0x0036:
            r0 = r2;
            goto L_0x0039;
        L_0x0038:
            r0 = r3;
        L_0x0039:
            r1 = r12.arg2;
            if (r1 == 0) goto L_0x003e;
        L_0x003d:
            r3 = r2;
        L_0x003e:
            r1 = r3;
            r3 = android.view.inputmethod.InputMethodManager.this;
            r3 = r3.mH;
            monitor-enter(r3);
            r4 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r4.mActive = r0;	 Catch:{ all -> 0x008b }
            r4 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r4.mFullscreenMode = r1;	 Catch:{ all -> 0x008b }
            if (r0 != 0) goto L_0x005b;
        L_0x004e:
            r4 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r4.mRestartOnNextWindowFocus = r2;	 Catch:{ all -> 0x008b }
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x005a }
            r2 = r2.mIInputContext;	 Catch:{ RemoteException -> 0x005a }
            r2.finishComposingText();	 Catch:{ RemoteException -> 0x005a }
            goto L_0x005b;
        L_0x005a:
            r2 = move-exception;
        L_0x005b:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r2 = r2.mServedView;	 Catch:{ all -> 0x008b }
            if (r2 == 0) goto L_0x0089;
        L_0x0061:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r2 = r2.mServedView;	 Catch:{ all -> 0x008b }
            r2 = android.view.inputmethod.InputMethodManager.canStartInput(r2);	 Catch:{ all -> 0x008b }
            if (r2 == 0) goto L_0x0089;
        L_0x006b:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r4 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r4 = r4.mRestartOnNextWindowFocus;	 Catch:{ all -> 0x008b }
            r2 = r2.checkFocusNoStartInput(r4);	 Catch:{ all -> 0x008b }
            if (r2 == 0) goto L_0x0089;
        L_0x0077:
            if (r0 == 0) goto L_0x007c;
        L_0x0079:
            r2 = 7;
            r5 = r2;
            goto L_0x007f;
        L_0x007c:
            r2 = 8;
            r5 = r2;
            r4 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x008b }
            r6 = 0;
            r7 = 0;
            r8 = 0;
            r9 = 0;
            r4.startInputInner(r5, r6, r7, r8, r9);	 Catch:{ all -> 0x008b }
        L_0x0089:
            monitor-exit(r3);	 Catch:{ all -> 0x008b }
            return;
        L_0x008b:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x008b }
            throw r2;
        L_0x008e:
            r0 = r12.arg1;
            r1 = r12.arg2;
            r3 = android.view.inputmethod.InputMethodManager.this;
            r4 = r3.mH;
            monitor-enter(r4);
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00ca }
            r3 = r3.mBindSequence;	 Catch:{ all -> 0x00ca }
            if (r3 == r0) goto L_0x009f;
        L_0x009d:
            monitor-exit(r4);	 Catch:{ all -> 0x00ca }
            return;
        L_0x009f:
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00ca }
            r3.clearBindingLocked();	 Catch:{ all -> 0x00ca }
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00ca }
            r3 = r3.mServedView;	 Catch:{ all -> 0x00ca }
            if (r3 == 0) goto L_0x00b8;
        L_0x00aa:
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00ca }
            r3 = r3.mServedView;	 Catch:{ all -> 0x00ca }
            r3 = r3.isFocused();	 Catch:{ all -> 0x00ca }
            if (r3 == 0) goto L_0x00b8;
        L_0x00b4:
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00ca }
            r3.mServedConnecting = r2;	 Catch:{ all -> 0x00ca }
        L_0x00b8:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00ca }
            r2 = r2.mActive;	 Catch:{ all -> 0x00ca }
            monitor-exit(r4);	 Catch:{ all -> 0x00ca }
            if (r2 == 0) goto L_0x00c9;
        L_0x00bf:
            r5 = android.view.inputmethod.InputMethodManager.this;
            r6 = 6;
            r7 = 0;
            r8 = 0;
            r9 = 0;
            r10 = 0;
            r5.startInputInner(r6, r7, r8, r9, r10);
        L_0x00c9:
            return;
        L_0x00ca:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x00ca }
            throw r2;
        L_0x00cd:
            r0 = r12.obj;
            r0 = (com.android.internal.view.InputBindResult) r0;
            r1 = android.view.inputmethod.InputMethodManager.this;
            r1 = r1.mH;
            monitor-enter(r1);
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r2 = r2.mBindSequence;	 Catch:{ all -> 0x014f }
            if (r2 < 0) goto L_0x0118;
        L_0x00dc:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r2 = r2.mBindSequence;	 Catch:{ all -> 0x014f }
            r4 = r0.sequence;	 Catch:{ all -> 0x014f }
            if (r2 == r4) goto L_0x00e5;
        L_0x00e4:
            goto L_0x0118;
        L_0x00e5:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r2.mRequestUpdateCursorAnchorInfoMonitorMode = r3;	 Catch:{ all -> 0x014f }
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r3 = r0.channel;	 Catch:{ all -> 0x014f }
            r2.setInputChannelLocked(r3);	 Catch:{ all -> 0x014f }
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r3 = r0.method;	 Catch:{ all -> 0x014f }
            r2.mCurMethod = r3;	 Catch:{ all -> 0x014f }
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r3 = r0.id;	 Catch:{ all -> 0x014f }
            r2.mCurId = r3;	 Catch:{ all -> 0x014f }
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r3 = r0.sequence;	 Catch:{ all -> 0x014f }
            r2.mBindSequence = r3;	 Catch:{ all -> 0x014f }
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r3 = r0.getActivityViewToScreenMatrix();	 Catch:{ all -> 0x014f }
            r2.mActivityViewToScreenMatrix = r3;	 Catch:{ all -> 0x014f }
            monitor-exit(r1);	 Catch:{ all -> 0x014f }
            r4 = android.view.inputmethod.InputMethodManager.this;
            r5 = 5;
            r6 = 0;
            r7 = 0;
            r8 = 0;
            r9 = 0;
            r4.startInputInner(r5, r6, r7, r8, r9);
            return;
        L_0x0118:
            r2 = "InputMethodManager";
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x014f }
            r3.<init>();	 Catch:{ all -> 0x014f }
            r4 = "Ignoring onBind: cur seq=";
            r3.append(r4);	 Catch:{ all -> 0x014f }
            r4 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r4 = r4.mBindSequence;	 Catch:{ all -> 0x014f }
            r3.append(r4);	 Catch:{ all -> 0x014f }
            r4 = ", given seq=";
            r3.append(r4);	 Catch:{ all -> 0x014f }
            r4 = r0.sequence;	 Catch:{ all -> 0x014f }
            r3.append(r4);	 Catch:{ all -> 0x014f }
            r3 = r3.toString();	 Catch:{ all -> 0x014f }
            android.util.Log.w(r2, r3);	 Catch:{ all -> 0x014f }
            r2 = r0.channel;	 Catch:{ all -> 0x014f }
            if (r2 == 0) goto L_0x014d;
        L_0x0140:
            r2 = r0.channel;	 Catch:{ all -> 0x014f }
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x014f }
            r3 = r3.mCurChannel;	 Catch:{ all -> 0x014f }
            if (r2 == r3) goto L_0x014d;
        L_0x0148:
            r2 = r0.channel;	 Catch:{ all -> 0x014f }
            r2.dispose();	 Catch:{ all -> 0x014f }
        L_0x014d:
            monitor-exit(r1);	 Catch:{ all -> 0x014f }
            return;
        L_0x014f:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x014f }
            throw r2;
        L_0x0152:
            r0 = r12.obj;
            r0 = (com.android.internal.os.SomeArgs) r0;
            r1 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RuntimeException -> 0x0168 }
            r2 = r0.arg1;	 Catch:{ RuntimeException -> 0x0168 }
            r2 = (java.io.FileDescriptor) r2;	 Catch:{ RuntimeException -> 0x0168 }
            r3 = r0.arg2;	 Catch:{ RuntimeException -> 0x0168 }
            r3 = (java.io.PrintWriter) r3;	 Catch:{ RuntimeException -> 0x0168 }
            r4 = r0.arg3;	 Catch:{ RuntimeException -> 0x0168 }
            r4 = (java.lang.String[]) r4;	 Catch:{ RuntimeException -> 0x0168 }
            r1.doDump(r2, r3, r4);	 Catch:{ RuntimeException -> 0x0168 }
            goto L_0x0181;
        L_0x0168:
            r1 = move-exception;
            r2 = r0.arg2;
            r2 = (java.io.PrintWriter) r2;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Exception: ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            r2.println(r3);
        L_0x0181:
            r1 = r0.arg4;
            monitor-enter(r1);
            r2 = r0.arg4;	 Catch:{ all -> 0x0190 }
            r2 = (java.util.concurrent.CountDownLatch) r2;	 Catch:{ all -> 0x0190 }
            r2.countDown();	 Catch:{ all -> 0x0190 }
            monitor-exit(r1);	 Catch:{ all -> 0x0190 }
            r0.recycle();
            return;
        L_0x0190:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0190 }
            throw r2;
        L_0x0193:
            r0 = r12.obj;
            r0 = (float[]) r0;
            r1 = r12.arg1;
            r4 = android.view.inputmethod.InputMethodManager.this;
            r4 = r4.mH;
            monitor-enter(r4);
            r5 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r5 = r5.mBindSequence;	 Catch:{ all -> 0x022a }
            if (r5 == r1) goto L_0x01a6;
        L_0x01a4:
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            return;
        L_0x01a6:
            if (r0 != 0) goto L_0x01b0;
        L_0x01a8:
            r2 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r3 = 0;
            r2.mActivityViewToScreenMatrix = r3;	 Catch:{ all -> 0x022a }
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            return;
        L_0x01b0:
            r5 = 9;
            r5 = new float[r5];	 Catch:{ all -> 0x022a }
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r6 = r6.mActivityViewToScreenMatrix;	 Catch:{ all -> 0x022a }
            r6.getValues(r5);	 Catch:{ all -> 0x022a }
            r6 = java.util.Arrays.equals(r5, r0);	 Catch:{ all -> 0x022a }
            if (r6 == 0) goto L_0x01c5;
        L_0x01c3:
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            return;
        L_0x01c5:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r6 = r6.mActivityViewToScreenMatrix;	 Catch:{ all -> 0x022a }
            r6.setValues(r0);	 Catch:{ all -> 0x022a }
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r6 = r6.mCursorAnchorInfo;	 Catch:{ all -> 0x022a }
            if (r6 == 0) goto L_0x0228;
        L_0x01d6:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r6 = r6.mCurMethod;	 Catch:{ all -> 0x022a }
            if (r6 == 0) goto L_0x0228;
        L_0x01dc:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r6 = r6.mServedInputConnectionWrapper;	 Catch:{ all -> 0x022a }
            if (r6 != 0) goto L_0x01e3;
        L_0x01e2:
            goto L_0x0228;
        L_0x01e3:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r6 = r6.mRequestUpdateCursorAnchorInfoMonitorMode;	 Catch:{ all -> 0x022a }
            r6 = r6 & 2;
            if (r6 == 0) goto L_0x01ee;
        L_0x01ed:
            goto L_0x01ef;
        L_0x01ee:
            r2 = r3;
        L_0x01ef:
            if (r2 != 0) goto L_0x01f3;
        L_0x01f1:
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            return;
        L_0x01f3:
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x020b }
            r3 = r3.mCurMethod;	 Catch:{ RemoteException -> 0x020b }
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x020b }
            r6 = r6.mCursorAnchorInfo;	 Catch:{ RemoteException -> 0x020b }
            r7 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x020b }
            r7 = r7.mActivityViewToScreenMatrix;	 Catch:{ RemoteException -> 0x020b }
            r6 = android.view.inputmethod.CursorAnchorInfo.createForAdditionalParentMatrix(r6, r7);	 Catch:{ RemoteException -> 0x020b }
            r3.updateCursorAnchorInfo(r6);	 Catch:{ RemoteException -> 0x020b }
            goto L_0x0226;
        L_0x020b:
            r3 = move-exception;
            r6 = "InputMethodManager";
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x022a }
            r7.<init>();	 Catch:{ all -> 0x022a }
            r8 = "IME died: ";
            r7.append(r8);	 Catch:{ all -> 0x022a }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x022a }
            r8 = r8.mCurId;	 Catch:{ all -> 0x022a }
            r7.append(r8);	 Catch:{ all -> 0x022a }
            r7 = r7.toString();	 Catch:{ all -> 0x022a }
            android.util.Log.w(r6, r7, r3);	 Catch:{ all -> 0x022a }
        L_0x0226:
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            return;
        L_0x0228:
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            return;
        L_0x022a:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x022a }
            throw r2;
        L_0x022d:
            r0 = android.view.inputmethod.InputMethodManager.this;
            r0 = r0.mH;
            monitor-enter(r0);
            r1 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x024b }
            r1 = r1.mImeInsetsConsumer;	 Catch:{ all -> 0x024b }
            if (r1 == 0) goto L_0x0249;
        L_0x023a:
            r1 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x024b }
            r1 = r1.mImeInsetsConsumer;	 Catch:{ all -> 0x024b }
            r4 = r12.arg1;	 Catch:{ all -> 0x024b }
            if (r4 == 0) goto L_0x0245;
        L_0x0244:
            goto L_0x0246;
        L_0x0245:
            r2 = r3;
        L_0x0246:
            r1.applyImeVisibility(r2);	 Catch:{ all -> 0x024b }
        L_0x0249:
            monitor-exit(r0);	 Catch:{ all -> 0x024b }
            return;
        L_0x024b:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x024b }
            throw r1;
        L_0x024e:
            r0 = android.view.inputmethod.InputMethodManager.this;
            r0 = r0.mH;
            monitor-enter(r0);
            r1 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x026a }
            r1 = r1.mImeInsetsConsumer;	 Catch:{ all -> 0x026a }
            if (r1 == 0) goto L_0x0268;
        L_0x025b:
            r1 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x026a }
            r1 = r1.mImeInsetsConsumer;	 Catch:{ all -> 0x026a }
            r2 = r12.obj;	 Catch:{ all -> 0x026a }
            r2 = (android.view.inputmethod.EditorInfo) r2;	 Catch:{ all -> 0x026a }
            r1.onPreRendered(r2);	 Catch:{ all -> 0x026a }
        L_0x0268:
            monitor-exit(r0);	 Catch:{ all -> 0x026a }
            return;
        L_0x026a:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x026a }
            throw r1;
        L_0x026d:
            r0 = r12.arg1;
            if (r0 == 0) goto L_0x0272;
        L_0x0271:
            goto L_0x0273;
        L_0x0272:
            r2 = r3;
        L_0x0273:
            r0 = r2;
            r1 = 0;
            r2 = android.view.inputmethod.InputMethodManager.this;
            r2 = r2.mH;
            monitor-enter(r2);
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0294 }
            r3.mFullscreenMode = r0;	 Catch:{ all -> 0x0294 }
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0294 }
            r3 = r3.mServedInputConnectionWrapper;	 Catch:{ all -> 0x0294 }
            if (r3 == 0) goto L_0x028d;
        L_0x0284:
            r3 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0294 }
            r3 = r3.mServedInputConnectionWrapper;	 Catch:{ all -> 0x0294 }
            r3 = r3.getInputConnection();	 Catch:{ all -> 0x0294 }
            r1 = r3;
        L_0x028d:
            monitor-exit(r2);	 Catch:{ all -> 0x0294 }
            if (r1 == 0) goto L_0x0293;
        L_0x0290:
            r1.reportFullscreenMode(r0);
        L_0x0293:
            return;
        L_0x0294:
            r3 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0294 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager$H.handleMessage(android.os.Message):void");
        }
    }

    private final class ImeInputEventSender extends InputEventSender {
        public ImeInputEventSender(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEventFinished(int seq, boolean handled) {
            InputMethodManager.this.finishedInputEvent(seq, handled, false);
        }
    }

    private final class PendingEvent implements Runnable {
        public FinishedInputEventCallback mCallback;
        public InputEvent mEvent;
        public boolean mHandled;
        public Handler mHandler;
        public String mInputMethodId;
        public Object mToken;

        private PendingEvent() {
        }

        /* synthetic */ PendingEvent(InputMethodManager x0, AnonymousClass1 x1) {
            this();
        }

        public void recycle() {
            this.mEvent = null;
            this.mToken = null;
            this.mInputMethodId = null;
            this.mCallback = null;
            this.mHandler = null;
            this.mHandled = false;
        }

        public void run() {
            this.mCallback.onFinishedInputEvent(this.mToken, this.mHandled);
            synchronized (InputMethodManager.this.mH) {
                InputMethodManager.this.recyclePendingEventLocked(this);
            }
        }
    }

    public static void ensureDefaultInstanceForDefaultDisplayIfNecessary() {
        forContextInternal(0, Looper.getMainLooper());
    }

    private static boolean isAutofillUIShowing(View servedView) {
        AutofillManager afm = (AutofillManager) servedView.getContext().getSystemService(AutofillManager.class);
        return afm != null && afm.isAutofillUiShowing();
    }

    private InputMethodManager getFallbackInputMethodManagerIfNecessary(View view) {
        if (view == null) {
            return null;
        }
        ViewRootImpl viewRootImpl = view.getViewRootImpl();
        if (viewRootImpl == null) {
            return null;
        }
        int viewRootDisplayId = viewRootImpl.getDisplayId();
        if (viewRootDisplayId == this.mDisplayId) {
            return null;
        }
        InputMethodManager fallbackImm = (InputMethodManager) viewRootImpl.mContext.getSystemService(InputMethodManager.class);
        String str = TAG;
        StringBuilder stringBuilder;
        if (fallbackImm == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("b/117267690: Failed to get non-null fallback IMM. view=");
            stringBuilder.append(view);
            Log.e(str, stringBuilder.toString());
            return null;
        } else if (fallbackImm.mDisplayId != viewRootDisplayId) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("b/117267690: Failed to get fallback IMM with expected displayId=");
            stringBuilder.append(viewRootDisplayId);
            stringBuilder.append(" actual IMM#displayId=");
            stringBuilder.append(fallbackImm.mDisplayId);
            stringBuilder.append(" view=");
            stringBuilder.append(view);
            Log.e(str, stringBuilder.toString());
            return null;
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("b/117267690: Display ID mismatch found. ViewRootImpl displayId=");
            stringBuilder2.append(viewRootDisplayId);
            stringBuilder2.append(" InputMethodManager displayId=");
            stringBuilder2.append(this.mDisplayId);
            stringBuilder2.append(". Use the right InputMethodManager instance to avoid performance overhead.");
            Log.w(str, stringBuilder2.toString(), new Throwable());
            return fallbackImm;
        }
    }

    private static boolean canStartInput(View servedView) {
        return servedView.hasWindowFocus() || isAutofillUIShowing(servedView);
    }

    static void tearDownEditMode() {
        if (isInEditMode()) {
            synchronized (sLock) {
                sInstance = null;
            }
            return;
        }
        throw new UnsupportedOperationException("This method must be called only from layoutlib");
    }

    private static boolean isInEditMode() {
        return false;
    }

    private static InputMethodManager createInstance(int displayId, Looper looper) {
        if (isInEditMode()) {
            return createStubInstance(displayId, looper);
        }
        return createRealInstance(displayId, looper);
    }

    private static InputMethodManager createRealInstance(int displayId, Looper looper) {
        try {
            IInputMethodManager service = IInputMethodManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.INPUT_METHOD_SERVICE));
            InputMethodManager imm = new InputMethodManager(service, displayId, looper);
            long identity = Binder.clearCallingIdentity();
            try {
                service.addClient(imm.mClient, imm.mIInputContext, displayId);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(identity);
            }
            Binder.restoreCallingIdentity(identity);
            return imm;
        } catch (ServiceNotFoundException e2) {
            throw new IllegalStateException(e2);
        }
    }

    private static InputMethodManager createStubInstance(int displayId, Looper looper) {
        return new InputMethodManager((IInputMethodManager) Proxy.newProxyInstance(IInputMethodManager.class.getClassLoader(), new Class[]{IInputMethodManager.class}, -$$Lambda$InputMethodManager$iDWn3IGSUFqIcs8Py42UhfrshxI.INSTANCE), displayId, looper);
    }

    static /* synthetic */ Object lambda$createStubInstance$0(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        Class<?> cls = Boolean.TYPE;
        Integer valueOf = Integer.valueOf(0);
        if (returnType == cls) {
            return Boolean.valueOf(false);
        }
        if (returnType == Integer.TYPE) {
            return valueOf;
        }
        if (returnType == Long.TYPE) {
            return Long.valueOf(0);
        }
        if (returnType == Short.TYPE || returnType == Character.TYPE || returnType == Byte.TYPE) {
            return valueOf;
        }
        if (returnType == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (returnType == Double.TYPE) {
            return Double.valueOf(0.0d);
        }
        return null;
    }

    private InputMethodManager(IInputMethodManager service, int displayId, Looper looper) {
        this.mService = service;
        this.mMainLooper = looper;
        this.mH = new H(looper);
        this.mDisplayId = displayId;
        this.mIInputContext = new ControlledInputConnectionWrapper(looper, this.mDummyInputConnection, this);
    }

    public static InputMethodManager forContext(Context context) {
        int displayId = context.getDisplayId();
        return forContextInternal(displayId, displayId == 0 ? Looper.getMainLooper() : context.getMainLooper());
    }

    private static InputMethodManager forContextInternal(int displayId, Looper looper) {
        boolean isDefaultDisplay = displayId == 0;
        synchronized (sLock) {
            InputMethodManager instance = (InputMethodManager) sInstanceMap.get(displayId);
            if (instance != null) {
                return instance;
            }
            instance = createInstance(displayId, looper);
            if (sInstance == null && isDefaultDisplay) {
                sInstance = instance;
            }
            sInstanceMap.put(displayId, instance);
            return instance;
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public static InputMethodManager getInstance() {
        Log.w(TAG, "InputMethodManager.getInstance() is deprecated because it cannot be compatible with multi-display. Use context.getSystemService(InputMethodManager.class) instead.", new Throwable());
        ensureDefaultInstanceForDefaultDisplayIfNecessary();
        return peekInstance();
    }

    @Deprecated
    @UnsupportedAppUsage
    public static InputMethodManager peekInstance() {
        InputMethodManager inputMethodManager;
        Log.w(TAG, "InputMethodManager.peekInstance() is deprecated because it cannot be compatible with multi-display. Use context.getSystemService(InputMethodManager.class) instead.", new Throwable());
        synchronized (sLock) {
            inputMethodManager = sInstance;
        }
        return inputMethodManager;
    }

    @UnsupportedAppUsage
    public IInputMethodClient getClient() {
        return this.mClient;
    }

    @UnsupportedAppUsage
    public IInputContext getInputContext() {
        return this.mIInputContext;
    }

    public List<InputMethodInfo> getInputMethodList() {
        try {
            return this.mService.getInputMethodList(UserHandle.myUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InputMethodInfo> getInputMethodListAsUser(int userId) {
        try {
            return this.mService.getInputMethodList(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InputMethodInfo> getEnabledInputMethodList() {
        try {
            return this.mService.getEnabledInputMethodList(UserHandle.myUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InputMethodInfo> getEnabledInputMethodListAsUser(int userId) {
        try {
            return this.mService.getEnabledInputMethodList(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(InputMethodInfo imi, boolean allowsImplicitlySelectedSubtypes) {
        try {
            return this.mService.getEnabledInputMethodSubtypeList(imi == null ? null : imi.getId(), allowsImplicitlySelectedSubtypes);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void showStatusIcon(IBinder imeToken, String packageName, int iconId) {
        InputMethodPrivilegedOperationsRegistry.get(imeToken).updateStatusIcon(packageName, iconId);
    }

    @Deprecated
    public void hideStatusIcon(IBinder imeToken) {
        InputMethodPrivilegedOperationsRegistry.get(imeToken).updateStatusIcon(null, 0);
    }

    @Deprecated
    @UnsupportedAppUsage
    public void registerSuggestionSpansForNotification(SuggestionSpan[] spans) {
        Log.w(TAG, "registerSuggestionSpansForNotification() is deprecated.  Does nothing.");
    }

    @Deprecated
    @UnsupportedAppUsage
    public void notifySuggestionPicked(SuggestionSpan span, String originalString, int index) {
        Log.w(TAG, "notifySuggestionPicked() is deprecated.  Does nothing.");
    }

    public boolean isFullscreenMode() {
        boolean z;
        synchronized (this.mH) {
            z = this.mFullscreenMode;
        }
        return z;
    }

    public boolean isActive(View view) {
        InputMethodManager fallbackImm = getFallbackInputMethodManagerIfNecessary(view);
        if (fallbackImm != null) {
            return fallbackImm.isActive(view);
        }
        boolean z;
        checkFocus();
        synchronized (this.mH) {
            z = (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) && this.mCurrentTextBoxAttribute != null;
        }
        return z;
    }

    public boolean isActive() {
        boolean z;
        checkFocus();
        synchronized (this.mH) {
            z = (this.mServedView == null || this.mCurrentTextBoxAttribute == null) ? false : true;
        }
        return z;
    }

    public boolean isAcceptingText() {
        checkFocus();
        ControlledInputConnectionWrapper controlledInputConnectionWrapper = this.mServedInputConnectionWrapper;
        return (controlledInputConnectionWrapper == null || controlledInputConnectionWrapper.getInputConnection() == null) ? false : true;
    }

    /* Access modifiers changed, original: 0000 */
    public void clearBindingLocked() {
        clearConnectionLocked();
        setInputChannelLocked(null);
        this.mBindSequence = -1;
        this.mCurId = null;
        this.mCurMethod = null;
    }

    /* Access modifiers changed, original: 0000 */
    public void setInputChannelLocked(InputChannel channel) {
        if (this.mCurChannel != channel) {
            if (this.mCurSender != null) {
                flushPendingEventsLocked();
                this.mCurSender.dispose();
                this.mCurSender = null;
            }
            InputChannel inputChannel = this.mCurChannel;
            if (inputChannel != null) {
                inputChannel.dispose();
            }
            this.mCurChannel = channel;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void clearConnectionLocked() {
        this.mCurrentTextBoxAttribute = null;
        ControlledInputConnectionWrapper controlledInputConnectionWrapper = this.mServedInputConnectionWrapper;
        if (controlledInputConnectionWrapper != null) {
            controlledInputConnectionWrapper.deactivate();
            this.mServedInputConnectionWrapper = null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void finishInputLocked() {
        this.mNextServedView = null;
        this.mActivityViewToScreenMatrix = null;
        if (this.mServedView != null) {
            this.mServedView = null;
            this.mCompletions = null;
            this.mServedConnecting = false;
            clearConnectionLocked();
        }
    }

    /* JADX WARNING: Missing block: B:14:0x0021, code skipped:
            return;
     */
    public void displayCompletions(android.view.View r5, android.view.inputmethod.CompletionInfo[] r6) {
        /*
        r4 = this;
        r0 = r4.getFallbackInputMethodManagerIfNecessary(r5);
        if (r0 == 0) goto L_0x000a;
    L_0x0006:
        r0.displayCompletions(r5, r6);
        return;
    L_0x000a:
        r4.checkFocus();
        r1 = r4.mH;
        monitor-enter(r1);
        r2 = r4.mServedView;	 Catch:{ all -> 0x0033 }
        if (r2 == r5) goto L_0x0022;
    L_0x0014:
        r2 = r4.mServedView;	 Catch:{ all -> 0x0033 }
        if (r2 == 0) goto L_0x0020;
    L_0x0018:
        r2 = r4.mServedView;	 Catch:{ all -> 0x0033 }
        r2 = r2.checkInputConnectionProxy(r5);	 Catch:{ all -> 0x0033 }
        if (r2 != 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0022:
        r4.mCompletions = r6;	 Catch:{ all -> 0x0033 }
        r2 = r4.mCurMethod;	 Catch:{ all -> 0x0033 }
        if (r2 == 0) goto L_0x0031;
    L_0x0028:
        r2 = r4.mCurMethod;	 Catch:{ RemoteException -> 0x0030 }
        r3 = r4.mCompletions;	 Catch:{ RemoteException -> 0x0030 }
        r2.displayCompletions(r3);	 Catch:{ RemoteException -> 0x0030 }
        goto L_0x0031;
    L_0x0030:
        r2 = move-exception;
    L_0x0031:
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0033:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.displayCompletions(android.view.View, android.view.inputmethod.CompletionInfo[]):void");
    }

    /* JADX WARNING: Missing block: B:14:0x0021, code skipped:
            return;
     */
    public void updateExtractedText(android.view.View r4, int r5, android.view.inputmethod.ExtractedText r6) {
        /*
        r3 = this;
        r0 = r3.getFallbackInputMethodManagerIfNecessary(r4);
        if (r0 == 0) goto L_0x000a;
    L_0x0006:
        r0.updateExtractedText(r4, r5, r6);
        return;
    L_0x000a:
        r3.checkFocus();
        r1 = r3.mH;
        monitor-enter(r1);
        r2 = r3.mServedView;	 Catch:{ all -> 0x002f }
        if (r2 == r4) goto L_0x0022;
    L_0x0014:
        r2 = r3.mServedView;	 Catch:{ all -> 0x002f }
        if (r2 == 0) goto L_0x0020;
    L_0x0018:
        r2 = r3.mServedView;	 Catch:{ all -> 0x002f }
        r2 = r2.checkInputConnectionProxy(r4);	 Catch:{ all -> 0x002f }
        if (r2 != 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        return;
    L_0x0022:
        r2 = r3.mCurMethod;	 Catch:{ all -> 0x002f }
        if (r2 == 0) goto L_0x002d;
    L_0x0026:
        r2 = r3.mCurMethod;	 Catch:{ RemoteException -> 0x002c }
        r2.updateExtractedText(r5, r6);	 Catch:{ RemoteException -> 0x002c }
        goto L_0x002d;
    L_0x002c:
        r2 = move-exception;
    L_0x002d:
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        return;
    L_0x002f:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateExtractedText(android.view.View, int, android.view.inputmethod.ExtractedText):void");
    }

    public boolean showSoftInput(View view, int flags) {
        InputMethodManager fallbackImm = getFallbackInputMethodManagerIfNecessary(view);
        if (fallbackImm != null) {
            return fallbackImm.showSoftInput(view, flags);
        }
        return showSoftInput(view, flags, null);
    }

    public boolean showSoftInput(View view, int flags, ResultReceiver resultReceiver) {
        InputMethodManager fallbackImm = getFallbackInputMethodManagerIfNecessary(view);
        if (fallbackImm != null) {
            return fallbackImm.showSoftInput(view, flags, resultReceiver);
        }
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) {
                try {
                    boolean showSoftInput = this.mService.showSoftInput(this.mClient, flags, resultReceiver);
                    return showSoftInput;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return false;
        }
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768499)
    public void showSoftInputUnchecked(int flags, ResultReceiver resultReceiver) {
        try {
            Log.w(TAG, "showSoftInputUnchecked() is a hidden method, which will be removed soon. If you are using android.support.v7.widget.SearchView, please update to version 26.0 or newer version.");
            this.mService.showSoftInput(this.mClient, flags, resultReceiver);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags) {
        return hideSoftInputFromWindow(windowToken, flags, null);
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags, ResultReceiver resultReceiver) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == null || this.mServedView.getWindowToken() != windowToken) {
                return false;
            }
            try {
                boolean hideSoftInput = this.mService.hideSoftInput(this.mClient, flags, resultReceiver);
                return hideSoftInput;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* JADX WARNING: Missing block: B:17:0x001e, code skipped:
            return;
     */
    public void toggleSoftInputFromWindow(android.os.IBinder r3, int r4, int r5) {
        /*
        r2 = this;
        r0 = r2.mH;
        monitor-enter(r0);
        r1 = r2.mServedView;	 Catch:{ all -> 0x001f }
        if (r1 == 0) goto L_0x001d;
    L_0x0007:
        r1 = r2.mServedView;	 Catch:{ all -> 0x001f }
        r1 = r1.getWindowToken();	 Catch:{ all -> 0x001f }
        if (r1 == r3) goto L_0x0010;
    L_0x000f:
        goto L_0x001d;
    L_0x0010:
        r1 = r2.mCurMethod;	 Catch:{ all -> 0x001f }
        if (r1 == 0) goto L_0x001b;
    L_0x0014:
        r1 = r2.mCurMethod;	 Catch:{ RemoteException -> 0x001a }
        r1.toggleSoftInput(r4, r5);	 Catch:{ RemoteException -> 0x001a }
        goto L_0x001b;
    L_0x001a:
        r1 = move-exception;
    L_0x001b:
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        return;
    L_0x001d:
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        return;
    L_0x001f:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.toggleSoftInputFromWindow(android.os.IBinder, int, int):void");
    }

    public void toggleSoftInput(int showFlags, int hideFlags) {
        IInputMethodSession iInputMethodSession = this.mCurMethod;
        if (iInputMethodSession != null) {
            try {
                iInputMethodSession.toggleSoftInput(showFlags, hideFlags);
            } catch (RemoteException e) {
            }
        }
    }

    /* JADX WARNING: Missing block: B:14:0x0021, code skipped:
            return;
     */
    public void restartInput(android.view.View r10) {
        /*
        r9 = this;
        r0 = r9.getFallbackInputMethodManagerIfNecessary(r10);
        if (r0 == 0) goto L_0x000a;
    L_0x0006:
        r0.restartInput(r10);
        return;
    L_0x000a:
        r9.checkFocus();
        r1 = r9.mH;
        monitor-enter(r1);
        r2 = r9.mServedView;	 Catch:{ all -> 0x0030 }
        if (r2 == r10) goto L_0x0022;
    L_0x0014:
        r2 = r9.mServedView;	 Catch:{ all -> 0x0030 }
        if (r2 == 0) goto L_0x0020;
    L_0x0018:
        r2 = r9.mServedView;	 Catch:{ all -> 0x0030 }
        r2 = r2.checkInputConnectionProxy(r10);	 Catch:{ all -> 0x0030 }
        if (r2 != 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r1);	 Catch:{ all -> 0x0030 }
        return;
    L_0x0022:
        r2 = 1;
        r9.mServedConnecting = r2;	 Catch:{ all -> 0x0030 }
        monitor-exit(r1);	 Catch:{ all -> 0x0030 }
        r4 = 3;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r3 = r9;
        r3.startInputInner(r4, r5, r6, r7, r8);
        return;
    L_0x0030:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0030 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.restartInput(android.view.View):void");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01ab A:{Splitter:B:66:0x014b, ExcHandler: IllegalArgumentException (e java.lang.IllegalArgumentException)} */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:66:0x014b, B:89:0x01a1] */
    /* JADX WARNING: Missing block: B:8:0x000e, code skipped:
            if (r28 != null) goto L_0x003e;
     */
    /* JADX WARNING: Missing block: B:9:0x0010, code skipped:
            r2 = r3.getWindowToken();
     */
    /* JADX WARNING: Missing block: B:10:0x0014, code skipped:
            if (r2 != null) goto L_0x001e;
     */
    /* JADX WARNING: Missing block: B:11:0x0016, code skipped:
            android.util.Log.e(TAG, "ABORT input: ServedView must be attached to a Window");
     */
    /* JADX WARNING: Missing block: B:12:0x001d, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:13:0x001e, code skipped:
            r4 = r29 | 1;
     */
    /* JADX WARNING: Missing block: B:14:0x0024, code skipped:
            if (r3.onCheckIsTextEditor() == false) goto L_0x0028;
     */
    /* JADX WARNING: Missing block: B:15:0x0026, code skipped:
            r4 = r4 | 2;
     */
    /* JADX WARNING: Missing block: B:16:0x0028, code skipped:
            r5 = r3.getViewRootImpl().mWindowAttributes.softInputMode;
            r6 = r3.getViewRootImpl().mWindowAttributes.flags;
            r25 = r4;
            r4 = r2;
            r2 = r25;
     */
    /* JADX WARNING: Missing block: B:17:0x003e, code skipped:
            r4 = r28;
            r2 = r29;
            r5 = r30;
            r6 = r31;
     */
    /* JADX WARNING: Missing block: B:18:0x0046, code skipped:
            r15 = r3.getHandler();
     */
    /* JADX WARNING: Missing block: B:19:0x004a, code skipped:
            if (r15 != null) goto L_0x0050;
     */
    /* JADX WARNING: Missing block: B:20:0x004c, code skipped:
            closeCurrentInput();
     */
    /* JADX WARNING: Missing block: B:21:0x004f, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:23:0x0058, code skipped:
            if (r15.getLooper() == android.os.Looper.myLooper()) goto L_0x0065;
     */
    /* JADX WARNING: Missing block: B:24:0x005a, code skipped:
            r15.post(new android.view.inputmethod.-$$Lambda$InputMethodManager$dfnCauFoZCf-HfXs1QavrkwWDf0(r1, r27));
     */
    /* JADX WARNING: Missing block: B:25:0x0064, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:26:0x0065, code skipped:
            r14 = r27;
            r13 = new android.view.inputmethod.EditorInfo();
            r13.packageName = r3.getContext().getOpPackageName();
            r13.fieldId = r3.getId();
            r12 = r3.onCreateInputConnection(r13);
            r11 = r1.mH;
     */
    /* JADX WARNING: Missing block: B:27:0x0083, code skipped:
            monitor-enter(r11);
     */
    /* JADX WARNING: Missing block: B:30:0x0086, code skipped:
            if (r1.mServedView != r3) goto L_0x01f6;
     */
    /* JADX WARNING: Missing block: B:32:0x008a, code skipped:
            if (r1.mServedConnecting != false) goto L_0x0097;
     */
    /* JADX WARNING: Missing block: B:33:0x008c, code skipped:
            r19 = r3;
            r21 = r11;
            r22 = r12;
            r10 = r13;
            r24 = r15;
     */
    /* JADX WARNING: Missing block: B:35:0x0099, code skipped:
            if (r1.mCurrentTextBoxAttribute != null) goto L_0x009d;
     */
    /* JADX WARNING: Missing block: B:36:0x009b, code skipped:
            r2 = r2 | 8;
     */
    /* JADX WARNING: Missing block: B:37:0x009d, code skipped:
            r1.mCurrentTextBoxAttribute = r13;
            maybeCallServedViewChangedLocked(r13);
            r1.mServedConnecting = false;
     */
    /* JADX WARNING: Missing block: B:39:0x00a7, code skipped:
            if (r1.mServedInputConnectionWrapper == null) goto L_0x00bd;
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            r1.mServedInputConnectionWrapper.deactivate();
            r1.mServedInputConnectionWrapper = null;
     */
    /* JADX WARNING: Missing block: B:42:0x00b1, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:43:0x00b2, code skipped:
            r19 = r3;
            r21 = r11;
            r22 = r12;
            r10 = r13;
            r24 = r15;
     */
    /* JADX WARNING: Missing block: B:44:0x00bd, code skipped:
            if (r12 == null) goto L_0x00f6;
     */
    /* JADX WARNING: Missing block: B:45:0x00bf, code skipped:
            r1.mCursorSelStart = r13.initialSelStart;
            r1.mCursorSelEnd = r13.initialSelEnd;
            r1.mCursorCandStart = -1;
            r1.mCursorCandEnd = -1;
            r1.mCursorRect.setEmpty();
            r1.mCursorAnchorInfo = null;
            r7 = android.view.inputmethod.InputConnectionInspector.getMissingMethodFlags(r12);
     */
    /* JADX WARNING: Missing block: B:46:0x00d9, code skipped:
            if ((r7 & 32) == 0) goto L_0x00dd;
     */
    /* JADX WARNING: Missing block: B:47:0x00db, code skipped:
            r8 = null;
     */
    /* JADX WARNING: Missing block: B:48:0x00dd, code skipped:
            r8 = r12.getHandler();
     */
    /* JADX WARNING: Missing block: B:50:0x00e3, code skipped:
            if (r8 == null) goto L_0x00ea;
     */
    /* JADX WARNING: Missing block: B:51:0x00e5, code skipped:
            r10 = r8.getLooper();
     */
    /* JADX WARNING: Missing block: B:52:0x00ea, code skipped:
            r10 = r15.getLooper();
     */
    /* JADX WARNING: Missing block: B:54:0x00f1, code skipped:
            r18 = r7;
            r10 = new android.view.inputmethod.InputMethodManager.ControlledInputConnectionWrapper(r10, r12, r1);
     */
    /* JADX WARNING: Missing block: B:55:0x00f6, code skipped:
            r10 = null;
            r18 = false;
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            r1.mServedInputConnectionWrapper = r10;
     */
    /* JADX WARNING: Missing block: B:59:?, code skipped:
            r7 = r1.mService;
     */
    /* JADX WARNING: Missing block: B:60:0x010b, code skipped:
            r16 = r1.mClient;
            r8 = r27;
            r19 = r3;
            r17 = r3.getContext().getApplicationInfo().targetSdkVersion;
            r3 = true;
            r21 = r11;
            r22 = r12;
            r23 = r13;
            r24 = r15;
     */
    /* JADX WARNING: Missing block: B:62:?, code skipped:
            r7 = r7.startInputOrWindowGainedFocus(r8, r16, r4, r2, r5, r6, r23, r10, r18, r17);
     */
    /* JADX WARNING: Missing block: B:63:0x012e, code skipped:
            if (r7 != null) goto L_0x0163;
     */
    /* JADX WARNING: Missing block: B:64:0x0130, code skipped:
            r8 = TAG;
            r9 = new java.lang.StringBuilder();
            r9.append("startInputOrWindowGainedFocus must not return null. startInputReason=");
            r9.append(com.android.internal.inputmethod.InputMethodDebug.startInputReasonToString(r27));
            r9.append(" editorInfo=");
     */
    /* JADX WARNING: Missing block: B:67:?, code skipped:
            r9.append(r23);
            r9.append(" startInputFlags=");
            r9.append(com.android.internal.inputmethod.InputMethodDebug.startInputFlagsToString(r2));
            android.util.Log.wtf(r8, r9.toString());
     */
    /* JADX WARNING: Missing block: B:69:?, code skipped:
            monitor-exit(r21);
     */
    /* JADX WARNING: Missing block: B:70:0x0162, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:71:0x0163, code skipped:
            r10 = r23;
     */
    /* JADX WARNING: Missing block: B:73:?, code skipped:
            r1.mActivityViewToScreenMatrix = r7.getActivityViewToScreenMatrix();
     */
    /* JADX WARNING: Missing block: B:74:0x016d, code skipped:
            if (r7.id == null) goto L_0x0181;
     */
    /* JADX WARNING: Missing block: B:75:0x016f, code skipped:
            setInputChannelLocked(r7.channel);
            r1.mBindSequence = r7.sequence;
            r1.mCurMethod = r7.method;
            r1.mCurId = r7.id;
     */
    /* JADX WARNING: Missing block: B:77:0x0183, code skipped:
            if (r7.channel == null) goto L_0x0190;
     */
    /* JADX WARNING: Missing block: B:79:0x0189, code skipped:
            if (r7.channel == r1.mCurChannel) goto L_0x0190;
     */
    /* JADX WARNING: Missing block: B:80:0x018b, code skipped:
            r7.channel.dispose();
     */
    /* JADX WARNING: Missing block: B:82:0x0194, code skipped:
            if (r7.result == 11) goto L_0x0197;
     */
    /* JADX WARNING: Missing block: B:84:0x0197, code skipped:
            r1.mRestartOnNextWindowFocus = true;
     */
    /* JADX WARNING: Missing block: B:86:0x019b, code skipped:
            if (r1.mCurMethod == null) goto L_0x01f4;
     */
    /* JADX WARNING: Missing block: B:88:0x019f, code skipped:
            if (r1.mCompletions == null) goto L_0x01f4;
     */
    /* JADX WARNING: Missing block: B:90:?, code skipped:
            r1.mCurMethod.displayCompletions(r1.mCompletions);
     */
    /* JADX WARNING: Missing block: B:92:0x01ab, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:93:0x01ad, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:94:0x01af, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:95:0x01b0, code skipped:
            r10 = r23;
     */
    /* JADX WARNING: Missing block: B:96:0x01b3, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:97:0x01b4, code skipped:
            r10 = r23;
     */
    /* JADX WARNING: Missing block: B:98:0x01b7, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:99:0x01b8, code skipped:
            r10 = r23;
     */
    /* JADX WARNING: Missing block: B:100:0x01bb, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:101:0x01bc, code skipped:
            r19 = r3;
            r20 = r10;
            r21 = r11;
            r22 = r12;
            r10 = r13;
            r24 = r15;
            r3 = true;
     */
    /* JADX WARNING: Missing block: B:103:?, code skipped:
            android.util.Log.w(TAG, r0);
     */
    /* JADX WARNING: Missing block: B:104:0x01ce, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:105:0x01cf, code skipped:
            r19 = r3;
            r20 = r10;
            r21 = r11;
            r22 = r12;
            r10 = r13;
            r24 = r15;
            r3 = true;
     */
    /* JADX WARNING: Missing block: B:106:0x01db, code skipped:
            r7 = TAG;
            r8 = new java.lang.StringBuilder();
            r8.append("IME died: ");
            r8.append(r1.mCurId);
            android.util.Log.w(r7, r8.toString(), r0);
     */
    /* JADX WARNING: Missing block: B:107:0x01f4, code skipped:
            monitor-exit(r21);
     */
    /* JADX WARNING: Missing block: B:108:0x01f5, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:109:0x01f6, code skipped:
            r19 = r3;
            r21 = r11;
            r22 = r12;
            r10 = r13;
            r24 = r15;
     */
    /* JADX WARNING: Missing block: B:110:0x01ff, code skipped:
            monitor-exit(r21);
     */
    /* JADX WARNING: Missing block: B:111:0x0200, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:112:0x0201, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:113:0x0202, code skipped:
            r19 = r3;
            r21 = r11;
            r22 = r12;
            r10 = r13;
            r24 = r15;
     */
    /* JADX WARNING: Missing block: B:114:0x020b, code skipped:
            monitor-exit(r21);
     */
    /* JADX WARNING: Missing block: B:115:0x020c, code skipped:
            throw r0;
     */
    /* JADX WARNING: Missing block: B:116:0x020d, code skipped:
            r0 = th;
     */
    public boolean startInputInner(int r27, android.os.IBinder r28, int r29, int r30, int r31) {
        /*
        r26 = this;
        r1 = r26;
        r2 = r1.mH;
        monitor-enter(r2);
        r0 = r1.mServedView;	 Catch:{ all -> 0x020f }
        r3 = r0;
        r0 = 0;
        if (r3 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r2);	 Catch:{ all -> 0x020f }
        return r0;
    L_0x000d:
        monitor-exit(r2);	 Catch:{ all -> 0x020f }
        if (r28 != 0) goto L_0x003e;
    L_0x0010:
        r2 = r3.getWindowToken();
        if (r2 != 0) goto L_0x001e;
    L_0x0016:
        r4 = "InputMethodManager";
        r5 = "ABORT input: ServedView must be attached to a Window";
        android.util.Log.e(r4, r5);
        return r0;
    L_0x001e:
        r4 = r29 | 1;
        r5 = r3.onCheckIsTextEditor();
        if (r5 == 0) goto L_0x0028;
    L_0x0026:
        r4 = r4 | 2;
    L_0x0028:
        r5 = r3.getViewRootImpl();
        r5 = r5.mWindowAttributes;
        r5 = r5.softInputMode;
        r6 = r3.getViewRootImpl();
        r6 = r6.mWindowAttributes;
        r6 = r6.flags;
        r25 = r4;
        r4 = r2;
        r2 = r25;
        goto L_0x0046;
    L_0x003e:
        r4 = r28;
        r2 = r29;
        r5 = r30;
        r6 = r31;
    L_0x0046:
        r15 = r3.getHandler();
        if (r15 != 0) goto L_0x0050;
    L_0x004c:
        r26.closeCurrentInput();
        return r0;
    L_0x0050:
        r7 = r15.getLooper();
        r8 = android.os.Looper.myLooper();
        if (r7 == r8) goto L_0x0065;
    L_0x005a:
        r7 = new android.view.inputmethod.-$$Lambda$InputMethodManager$dfnCauFoZCf-HfXs1QavrkwWDf0;
        r14 = r27;
        r7.<init>(r1, r14);
        r15.post(r7);
        return r0;
    L_0x0065:
        r14 = r27;
        r7 = new android.view.inputmethod.EditorInfo;
        r7.<init>();
        r13 = r7;
        r7 = r3.getContext();
        r7 = r7.getOpPackageName();
        r13.packageName = r7;
        r7 = r3.getId();
        r13.fieldId = r7;
        r12 = r3.onCreateInputConnection(r13);
        r11 = r1.mH;
        monitor-enter(r11);
        r7 = r1.mServedView;	 Catch:{ all -> 0x0201 }
        if (r7 != r3) goto L_0x01f6;
    L_0x0088:
        r7 = r1.mServedConnecting;	 Catch:{ all -> 0x0201 }
        if (r7 != 0) goto L_0x0097;
    L_0x008c:
        r19 = r3;
        r21 = r11;
        r22 = r12;
        r10 = r13;
        r24 = r15;
        goto L_0x01ff;
    L_0x0097:
        r7 = r1.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0201 }
        if (r7 != 0) goto L_0x009d;
    L_0x009b:
        r2 = r2 | 8;
    L_0x009d:
        r1.mCurrentTextBoxAttribute = r13;	 Catch:{ all -> 0x0201 }
        r1.maybeCallServedViewChangedLocked(r13);	 Catch:{ all -> 0x0201 }
        r1.mServedConnecting = r0;	 Catch:{ all -> 0x0201 }
        r7 = r1.mServedInputConnectionWrapper;	 Catch:{ all -> 0x0201 }
        r8 = 0;
        if (r7 == 0) goto L_0x00bd;
    L_0x00a9:
        r7 = r1.mServedInputConnectionWrapper;	 Catch:{ all -> 0x00b1 }
        r7.deactivate();	 Catch:{ all -> 0x00b1 }
        r1.mServedInputConnectionWrapper = r8;	 Catch:{ all -> 0x00b1 }
        goto L_0x00bd;
    L_0x00b1:
        r0 = move-exception;
        r19 = r3;
        r21 = r11;
        r22 = r12;
        r10 = r13;
        r24 = r15;
        goto L_0x020b;
    L_0x00bd:
        if (r12 == 0) goto L_0x00f6;
    L_0x00bf:
        r7 = r13.initialSelStart;	 Catch:{ all -> 0x00b1 }
        r1.mCursorSelStart = r7;	 Catch:{ all -> 0x00b1 }
        r7 = r13.initialSelEnd;	 Catch:{ all -> 0x00b1 }
        r1.mCursorSelEnd = r7;	 Catch:{ all -> 0x00b1 }
        r7 = -1;
        r1.mCursorCandStart = r7;	 Catch:{ all -> 0x00b1 }
        r1.mCursorCandEnd = r7;	 Catch:{ all -> 0x00b1 }
        r7 = r1.mCursorRect;	 Catch:{ all -> 0x00b1 }
        r7.setEmpty();	 Catch:{ all -> 0x00b1 }
        r1.mCursorAnchorInfo = r8;	 Catch:{ all -> 0x00b1 }
        r7 = android.view.inputmethod.InputConnectionInspector.getMissingMethodFlags(r12);	 Catch:{ all -> 0x00b1 }
        r8 = r7 & 32;
        if (r8 == 0) goto L_0x00dd;
    L_0x00db:
        r8 = 0;
        goto L_0x00e1;
    L_0x00dd:
        r8 = r12.getHandler();	 Catch:{ all -> 0x00b1 }
    L_0x00e1:
        r9 = new android.view.inputmethod.InputMethodManager$ControlledInputConnectionWrapper;	 Catch:{ all -> 0x00b1 }
        if (r8 == 0) goto L_0x00ea;
    L_0x00e5:
        r10 = r8.getLooper();	 Catch:{ all -> 0x00b1 }
        goto L_0x00ee;
    L_0x00ea:
        r10 = r15.getLooper();	 Catch:{ all -> 0x00b1 }
    L_0x00ee:
        r9.<init>(r10, r12, r1);	 Catch:{ all -> 0x00b1 }
        r8 = r9;
        r18 = r7;
        r10 = r8;
        goto L_0x00fb;
    L_0x00f6:
        r7 = 0;
        r8 = r0;
        r10 = r7;
        r18 = r8;
    L_0x00fb:
        r1.mServedInputConnectionWrapper = r10;	 Catch:{ all -> 0x0201 }
        r7 = r1.mService;	 Catch:{ RemoteException -> 0x01ce, IllegalArgumentException -> 0x01bb }
        r8 = r1.mClient;	 Catch:{ RemoteException -> 0x01ce, IllegalArgumentException -> 0x01bb }
        r16 = r3.getContext();	 Catch:{ RemoteException -> 0x01ce, IllegalArgumentException -> 0x01bb }
        r9 = r16.getApplicationInfo();	 Catch:{ RemoteException -> 0x01ce, IllegalArgumentException -> 0x01bb }
        r9 = r9.targetSdkVersion;	 Catch:{ RemoteException -> 0x01ce, IllegalArgumentException -> 0x01bb }
        r16 = r8;
        r8 = r27;
        r19 = r3;
        r17 = r9;
        r3 = 1;
        r9 = r16;
        r20 = r10;
        r10 = r4;
        r21 = r11;
        r11 = r2;
        r22 = r12;
        r12 = r5;
        r23 = r13;
        r13 = r6;
        r14 = r23;
        r24 = r15;
        r15 = r20;
        r16 = r18;
        r7 = r7.startInputOrWindowGainedFocus(r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        if (r7 != 0) goto L_0x0163;
    L_0x0130:
        r8 = "InputMethodManager";
        r9 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        r9.<init>();	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        r10 = "startInputOrWindowGainedFocus must not return null. startInputReason=";
        r9.append(r10);	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        r10 = com.android.internal.inputmethod.InputMethodDebug.startInputReasonToString(r27);	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        r9.append(r10);	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        r10 = " editorInfo=";
        r9.append(r10);	 Catch:{ RemoteException -> 0x01b7, IllegalArgumentException -> 0x01b3, all -> 0x01af }
        r10 = r23;
        r9.append(r10);	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r11 = " startInputFlags=";
        r9.append(r11);	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r11 = com.android.internal.inputmethod.InputMethodDebug.startInputFlagsToString(r2);	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r9.append(r11);	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r9 = r9.toString();	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        android.util.Log.wtf(r8, r9);	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        monitor-exit(r21);	 Catch:{ all -> 0x020d }
        return r0;
    L_0x0163:
        r10 = r23;
        r0 = r7.getActivityViewToScreenMatrix();	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r1.mActivityViewToScreenMatrix = r0;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r0 = r7.id;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        if (r0 == 0) goto L_0x0181;
    L_0x016f:
        r0 = r7.channel;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r1.setInputChannelLocked(r0);	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r0 = r7.sequence;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r1.mBindSequence = r0;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r0 = r7.method;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r1.mCurMethod = r0;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r0 = r7.id;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r1.mCurId = r0;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        goto L_0x0190;
    L_0x0181:
        r0 = r7.channel;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        if (r0 == 0) goto L_0x0190;
    L_0x0185:
        r0 = r7.channel;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r8 = r1.mCurChannel;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        if (r0 == r8) goto L_0x0190;
    L_0x018b:
        r0 = r7.channel;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r0.dispose();	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
    L_0x0190:
        r0 = r7.result;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        r8 = 11;
        if (r0 == r8) goto L_0x0197;
    L_0x0196:
        goto L_0x0199;
    L_0x0197:
        r1.mRestartOnNextWindowFocus = r3;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
    L_0x0199:
        r0 = r1.mCurMethod;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        if (r0 == 0) goto L_0x01aa;
    L_0x019d:
        r0 = r1.mCompletions;	 Catch:{ RemoteException -> 0x01ad, IllegalArgumentException -> 0x01ab }
        if (r0 == 0) goto L_0x01aa;
    L_0x01a1:
        r0 = r1.mCurMethod;	 Catch:{ RemoteException -> 0x01a9, IllegalArgumentException -> 0x01ab }
        r8 = r1.mCompletions;	 Catch:{ RemoteException -> 0x01a9, IllegalArgumentException -> 0x01ab }
        r0.displayCompletions(r8);	 Catch:{ RemoteException -> 0x01a9, IllegalArgumentException -> 0x01ab }
        goto L_0x01aa;
    L_0x01a9:
        r0 = move-exception;
    L_0x01aa:
        goto L_0x01f4;
    L_0x01ab:
        r0 = move-exception;
        goto L_0x01c8;
    L_0x01ad:
        r0 = move-exception;
        goto L_0x01db;
    L_0x01af:
        r0 = move-exception;
        r10 = r23;
        goto L_0x020b;
    L_0x01b3:
        r0 = move-exception;
        r10 = r23;
        goto L_0x01c8;
    L_0x01b7:
        r0 = move-exception;
        r10 = r23;
        goto L_0x01db;
    L_0x01bb:
        r0 = move-exception;
        r19 = r3;
        r20 = r10;
        r21 = r11;
        r22 = r12;
        r10 = r13;
        r24 = r15;
        r3 = 1;
    L_0x01c8:
        r7 = "InputMethodManager";
        android.util.Log.w(r7, r0);	 Catch:{ all -> 0x020d }
        goto L_0x01f4;
    L_0x01ce:
        r0 = move-exception;
        r19 = r3;
        r20 = r10;
        r21 = r11;
        r22 = r12;
        r10 = r13;
        r24 = r15;
        r3 = 1;
    L_0x01db:
        r7 = "InputMethodManager";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x020d }
        r8.<init>();	 Catch:{ all -> 0x020d }
        r9 = "IME died: ";
        r8.append(r9);	 Catch:{ all -> 0x020d }
        r9 = r1.mCurId;	 Catch:{ all -> 0x020d }
        r8.append(r9);	 Catch:{ all -> 0x020d }
        r8 = r8.toString();	 Catch:{ all -> 0x020d }
        android.util.Log.w(r7, r8, r0);	 Catch:{ all -> 0x020d }
    L_0x01f4:
        monitor-exit(r21);	 Catch:{ all -> 0x020d }
        return r3;
    L_0x01f6:
        r19 = r3;
        r21 = r11;
        r22 = r12;
        r10 = r13;
        r24 = r15;
    L_0x01ff:
        monitor-exit(r21);	 Catch:{ all -> 0x020d }
        return r0;
    L_0x0201:
        r0 = move-exception;
        r19 = r3;
        r21 = r11;
        r22 = r12;
        r10 = r13;
        r24 = r15;
    L_0x020b:
        monitor-exit(r21);	 Catch:{ all -> 0x020d }
        throw r0;
    L_0x020d:
        r0 = move-exception;
        goto L_0x020b;
    L_0x020f:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x020f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.startInputInner(int, android.os.IBinder, int, int, int):boolean");
    }

    public /* synthetic */ void lambda$startInputInner$1$InputMethodManager(int startInputReason) {
        startInputInner(startInputReason, null, 0, 0, 0);
    }

    @UnsupportedAppUsage
    public void windowDismissed(IBinder appWindowToken) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView != null && this.mServedView.getWindowToken() == appWindowToken) {
                finishInputLocked();
            }
            if (this.mCurRootView != null && this.mCurRootView.getWindowToken() == appWindowToken) {
                this.mCurRootView = null;
            }
        }
    }

    @UnsupportedAppUsage
    public void focusIn(View view) {
        synchronized (this.mH) {
            focusInLocked(view);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void focusInLocked(View view) {
        if ((view == null || !view.isTemporarilyDetached()) && this.mCurRootView == view.getRootView()) {
            this.mNextServedView = view;
            scheduleCheckFocusLocked(view);
        }
    }

    @UnsupportedAppUsage
    public void focusOut(View view) {
        synchronized (this.mH) {
            View view2 = this.mServedView;
        }
    }

    public void onViewDetachedFromWindow(View view) {
        synchronized (this.mH) {
            if (this.mServedView == view) {
                this.mNextServedView = null;
                scheduleCheckFocusLocked(view);
            }
        }
    }

    static void scheduleCheckFocusLocked(View view) {
        ViewRootImpl viewRootImpl = view.getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.dispatchCheckFocus();
        }
    }

    @UnsupportedAppUsage
    public void checkFocus() {
        if (checkFocusNoStartInput(false)) {
            startInputInner(4, null, 0, 0, 0);
        }
    }

    /* JADX WARNING: Missing block: B:21:0x003d, code skipped:
            if (r1 == null) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:22:0x003f, code skipped:
            r1.finishComposingText();
     */
    /* JADX WARNING: Missing block: B:23:0x0042, code skipped:
            return true;
     */
    private boolean checkFocusNoStartInput(boolean r6) {
        /*
        r5 = this;
        r0 = r5.mServedView;
        r1 = r5.mNextServedView;
        r2 = 0;
        if (r0 != r1) goto L_0x000a;
    L_0x0007:
        if (r6 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r0 = r5.mH;
        monitor-enter(r0);
        r1 = r5.mServedView;	 Catch:{ all -> 0x0043 }
        r3 = r5.mNextServedView;	 Catch:{ all -> 0x0043 }
        if (r1 != r3) goto L_0x0017;
    L_0x0013:
        if (r6 != 0) goto L_0x0017;
    L_0x0015:
        monitor-exit(r0);	 Catch:{ all -> 0x0043 }
        return r2;
    L_0x0017:
        r1 = r5.mNextServedView;	 Catch:{ all -> 0x0043 }
        if (r1 != 0) goto L_0x0023;
    L_0x001b:
        r5.finishInputLocked();	 Catch:{ all -> 0x0043 }
        r5.closeCurrentInput();	 Catch:{ all -> 0x0043 }
        monitor-exit(r0);	 Catch:{ all -> 0x0043 }
        return r2;
    L_0x0023:
        r1 = r5.mServedInputConnectionWrapper;	 Catch:{ all -> 0x0043 }
        r2 = r5.mNextServedView;	 Catch:{ all -> 0x0043 }
        r5.mServedView = r2;	 Catch:{ all -> 0x0043 }
        r2 = 0;
        r5.mCurrentTextBoxAttribute = r2;	 Catch:{ all -> 0x0043 }
        r5.mCompletions = r2;	 Catch:{ all -> 0x0043 }
        r3 = 1;
        r5.mServedConnecting = r3;	 Catch:{ all -> 0x0043 }
        r4 = r5.mServedView;	 Catch:{ all -> 0x0043 }
        r4 = r4.onCheckIsTextEditor();	 Catch:{ all -> 0x0043 }
        if (r4 != 0) goto L_0x003c;
    L_0x0039:
        r5.maybeCallServedViewChangedLocked(r2);	 Catch:{ all -> 0x0043 }
    L_0x003c:
        monitor-exit(r0);	 Catch:{ all -> 0x0043 }
        if (r1 == 0) goto L_0x0042;
    L_0x003f:
        r1.finishComposingText();
    L_0x0042:
        return r3;
    L_0x0043:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0043 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.checkFocusNoStartInput(boolean):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void closeCurrentInput() {
        try {
            this.mService.hideSoftInput(this.mClient, 2, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARNING: Missing block: B:14:0x001c, code skipped:
            r0 = 0;
     */
    /* JADX WARNING: Missing block: B:15:0x001d, code skipped:
            if (r23 == null) goto L_0x0029;
     */
    /* JADX WARNING: Missing block: B:16:0x001f, code skipped:
            r0 = 0 | 1;
     */
    /* JADX WARNING: Missing block: B:17:0x0025, code skipped:
            if (r23.onCheckIsTextEditor() == false) goto L_0x0029;
     */
    /* JADX WARNING: Missing block: B:18:0x0027, code skipped:
            r0 = r0 | 2;
     */
    /* JADX WARNING: Missing block: B:19:0x0029, code skipped:
            if (r25 == false) goto L_0x0030;
     */
    /* JADX WARNING: Missing block: B:20:0x002b, code skipped:
            r20 = r0 | 4;
     */
    /* JADX WARNING: Missing block: B:21:0x0030, code skipped:
            r20 = r0;
     */
    /* JADX WARNING: Missing block: B:23:0x0036, code skipped:
            if (checkFocusNoStartInput(r8) == false) goto L_0x004c;
     */
    /* JADX WARNING: Missing block: B:25:0x0049, code skipped:
            if (startInputInner(1, r22.getWindowToken(), r20, r24, r26) == false) goto L_0x004c;
     */
    /* JADX WARNING: Missing block: B:26:0x004b, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:27:0x004c, code skipped:
            r1 = r7.mH;
     */
    /* JADX WARNING: Missing block: B:28:0x004e, code skipped:
            monitor-enter(r1);
     */
    /* JADX WARNING: Missing block: B:30:?, code skipped:
            r7.mService.startInputOrWindowGainedFocus(2, r7.mClient, r22.getWindowToken(), r20, r24, r26, null, null, 0, r22.getContext().getApplicationInfo().targetSdkVersion);
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            monitor-exit(r1);
     */
    /* JADX WARNING: Missing block: B:33:0x0075, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:36:0x0078, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:38:0x007d, code skipped:
            throw r0.rethrowFromSystemServer();
     */
    public void onPostWindowFocus(android.view.View r22, android.view.View r23, int r24, boolean r25, int r26) {
        /*
        r21 = this;
        r7 = r21;
        r1 = 0;
        r2 = r7.mH;
        monitor-enter(r2);
        r0 = r7.mRestartOnNextWindowFocus;	 Catch:{ all -> 0x0083 }
        if (r0 == 0) goto L_0x0010;
    L_0x000a:
        r0 = 0;
        r7.mRestartOnNextWindowFocus = r0;	 Catch:{ all -> 0x0083 }
        r1 = 1;
        r8 = r1;
        goto L_0x0011;
    L_0x0010:
        r8 = r1;
    L_0x0011:
        if (r23 == 0) goto L_0x0016;
    L_0x0013:
        r0 = r23;
        goto L_0x0018;
    L_0x0016:
        r0 = r22;
    L_0x0018:
        r7.focusInLocked(r0);	 Catch:{ all -> 0x0080 }
        monitor-exit(r2);	 Catch:{ all -> 0x0080 }
        r0 = 0;
        if (r23 == 0) goto L_0x0029;
    L_0x001f:
        r0 = r0 | 1;
        r1 = r23.onCheckIsTextEditor();
        if (r1 == 0) goto L_0x0029;
    L_0x0027:
        r0 = r0 | 2;
    L_0x0029:
        if (r25 == 0) goto L_0x0030;
    L_0x002b:
        r0 = r0 | 4;
        r20 = r0;
        goto L_0x0032;
    L_0x0030:
        r20 = r0;
    L_0x0032:
        r0 = r7.checkFocusNoStartInput(r8);
        if (r0 == 0) goto L_0x004c;
    L_0x0038:
        r2 = 1;
        r3 = r22.getWindowToken();
        r1 = r21;
        r4 = r20;
        r5 = r24;
        r6 = r26;
        r0 = r1.startInputInner(r2, r3, r4, r5, r6);
        if (r0 == 0) goto L_0x004c;
    L_0x004b:
        return;
    L_0x004c:
        r1 = r7.mH;
        monitor-enter(r1);
        r9 = r7.mService;	 Catch:{ RemoteException -> 0x0078 }
        r10 = 2;
        r11 = r7.mClient;	 Catch:{ RemoteException -> 0x0078 }
        r12 = r22.getWindowToken();	 Catch:{ RemoteException -> 0x0078 }
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r0 = r22.getContext();	 Catch:{ RemoteException -> 0x0078 }
        r0 = r0.getApplicationInfo();	 Catch:{ RemoteException -> 0x0078 }
        r0 = r0.targetSdkVersion;	 Catch:{ RemoteException -> 0x0078 }
        r13 = r20;
        r14 = r24;
        r15 = r26;
        r19 = r0;
        r9.startInputOrWindowGainedFocus(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19);	 Catch:{ RemoteException -> 0x0078 }
        monitor-exit(r1);	 Catch:{ all -> 0x0076 }
        return;
    L_0x0076:
        r0 = move-exception;
        goto L_0x007e;
    L_0x0078:
        r0 = move-exception;
        r2 = r0.rethrowFromSystemServer();	 Catch:{ all -> 0x0076 }
        throw r2;	 Catch:{ all -> 0x0076 }
    L_0x007e:
        monitor-exit(r1);	 Catch:{ all -> 0x0076 }
        throw r0;
    L_0x0080:
        r0 = move-exception;
        r1 = r8;
        goto L_0x0084;
    L_0x0083:
        r0 = move-exception;
    L_0x0084:
        monitor-exit(r2);	 Catch:{ all -> 0x0083 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.onPostWindowFocus(android.view.View, android.view.View, int, boolean, int):void");
    }

    @UnsupportedAppUsage
    public void onPreWindowFocus(View rootView, boolean hasWindowFocus) {
        synchronized (this.mH) {
            if (rootView == null) {
                try {
                    this.mCurRootView = null;
                } catch (Throwable th) {
                }
            }
            if (hasWindowFocus) {
                this.mCurRootView = rootView;
            } else if (rootView == this.mCurRootView) {
                this.mCurRootView = null;
            }
        }
    }

    public void registerImeConsumer(ImeInsetsSourceConsumer imeInsetsConsumer) {
        if (imeInsetsConsumer != null) {
            synchronized (this.mH) {
                this.mImeInsetsConsumer = imeInsetsConsumer;
            }
            return;
        }
        throw new IllegalStateException("ImeInsetsSourceConsumer cannot be null.");
    }

    public void unregisterImeConsumer(ImeInsetsSourceConsumer imeInsetsConsumer) {
        if (imeInsetsConsumer != null) {
            synchronized (this.mH) {
                if (this.mImeInsetsConsumer == imeInsetsConsumer) {
                    this.mImeInsetsConsumer = null;
                }
            }
            return;
        }
        throw new IllegalStateException("ImeInsetsSourceConsumer cannot be null.");
    }

    public boolean requestImeShow(ResultReceiver resultReceiver) {
        synchronized (this.mH) {
            if (this.mServedView == null) {
                return false;
            }
            showSoftInput(this.mServedView, 0, resultReceiver);
            return true;
        }
    }

    public void notifyImeHidden() {
        synchronized (this.mH) {
            try {
                if (this.mCurMethod != null) {
                    this.mCurMethod.notifyImeHidden();
                }
            } catch (RemoteException e) {
            }
        }
    }

    /* JADX WARNING: Missing block: B:34:0x0087, code skipped:
            return;
     */
    public void updateSelection(android.view.View r18, int r19, int r20, int r21, int r22) {
        /*
        r17 = this;
        r1 = r17;
        r8 = r18;
        r15 = r19;
        r14 = r20;
        r13 = r21;
        r12 = r22;
        r16 = r17.getFallbackInputMethodManagerIfNecessary(r18);
        if (r16 == 0) goto L_0x0022;
    L_0x0012:
        r2 = r16;
        r3 = r18;
        r4 = r19;
        r5 = r20;
        r6 = r21;
        r7 = r22;
        r2.updateSelection(r3, r4, r5, r6, r7);
        return;
    L_0x0022:
        r17.checkFocus();
        r2 = r1.mH;
        monitor-enter(r2);
        r0 = r1.mServedView;	 Catch:{ all -> 0x0088 }
        if (r0 == r8) goto L_0x0038;
    L_0x002c:
        r0 = r1.mServedView;	 Catch:{ all -> 0x0088 }
        if (r0 == 0) goto L_0x0086;
    L_0x0030:
        r0 = r1.mServedView;	 Catch:{ all -> 0x0088 }
        r0 = r0.checkInputConnectionProxy(r8);	 Catch:{ all -> 0x0088 }
        if (r0 == 0) goto L_0x0086;
    L_0x0038:
        r0 = r1.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0088 }
        if (r0 == 0) goto L_0x0086;
    L_0x003c:
        r0 = r1.mCurMethod;	 Catch:{ all -> 0x0088 }
        if (r0 != 0) goto L_0x0041;
    L_0x0040:
        goto L_0x0086;
    L_0x0041:
        r0 = r1.mCursorSelStart;	 Catch:{ all -> 0x0088 }
        if (r0 != r15) goto L_0x0051;
    L_0x0045:
        r0 = r1.mCursorSelEnd;	 Catch:{ all -> 0x0088 }
        if (r0 != r14) goto L_0x0051;
    L_0x0049:
        r0 = r1.mCursorCandStart;	 Catch:{ all -> 0x0088 }
        if (r0 != r13) goto L_0x0051;
    L_0x004d:
        r0 = r1.mCursorCandEnd;	 Catch:{ all -> 0x0088 }
        if (r0 == r12) goto L_0x0084;
    L_0x0051:
        r10 = r1.mCursorSelStart;	 Catch:{ RemoteException -> 0x006b }
        r11 = r1.mCursorSelEnd;	 Catch:{ RemoteException -> 0x006b }
        r1.mCursorSelStart = r15;	 Catch:{ RemoteException -> 0x006b }
        r1.mCursorSelEnd = r14;	 Catch:{ RemoteException -> 0x006b }
        r1.mCursorCandStart = r13;	 Catch:{ RemoteException -> 0x006b }
        r1.mCursorCandEnd = r12;	 Catch:{ RemoteException -> 0x006b }
        r9 = r1.mCurMethod;	 Catch:{ RemoteException -> 0x006b }
        r12 = r19;
        r13 = r20;
        r14 = r21;
        r15 = r22;
        r9.updateSelection(r10, r11, r12, r13, r14, r15);	 Catch:{ RemoteException -> 0x006b }
        goto L_0x0084;
    L_0x006b:
        r0 = move-exception;
        r3 = "InputMethodManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0088 }
        r4.<init>();	 Catch:{ all -> 0x0088 }
        r5 = "IME died: ";
        r4.append(r5);	 Catch:{ all -> 0x0088 }
        r5 = r1.mCurId;	 Catch:{ all -> 0x0088 }
        r4.append(r5);	 Catch:{ all -> 0x0088 }
        r4 = r4.toString();	 Catch:{ all -> 0x0088 }
        android.util.Log.w(r3, r4, r0);	 Catch:{ all -> 0x0088 }
    L_0x0084:
        monitor-exit(r2);	 Catch:{ all -> 0x0088 }
        return;
    L_0x0086:
        monitor-exit(r2);	 Catch:{ all -> 0x0088 }
        return;
    L_0x0088:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0088 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateSelection(android.view.View, int, int, int, int):void");
    }

    /* JADX WARNING: Missing block: B:29:0x0054, code skipped:
            return;
     */
    @java.lang.Deprecated
    public void viewClicked(android.view.View r8) {
        /*
        r7 = this;
        r0 = r7.getFallbackInputMethodManagerIfNecessary(r8);
        if (r0 == 0) goto L_0x000a;
    L_0x0006:
        r0.viewClicked(r8);
        return;
    L_0x000a:
        r1 = r7.mServedView;
        r2 = r7.mNextServedView;
        if (r1 == r2) goto L_0x0012;
    L_0x0010:
        r1 = 1;
        goto L_0x0013;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        r7.checkFocus();
        r2 = r7.mH;
        monitor-enter(r2);
        r3 = r7.mServedView;	 Catch:{ all -> 0x0055 }
        if (r3 == r8) goto L_0x0029;
    L_0x001d:
        r3 = r7.mServedView;	 Catch:{ all -> 0x0055 }
        if (r3 == 0) goto L_0x0053;
    L_0x0021:
        r3 = r7.mServedView;	 Catch:{ all -> 0x0055 }
        r3 = r3.checkInputConnectionProxy(r8);	 Catch:{ all -> 0x0055 }
        if (r3 == 0) goto L_0x0053;
    L_0x0029:
        r3 = r7.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0055 }
        if (r3 == 0) goto L_0x0053;
    L_0x002d:
        r3 = r7.mCurMethod;	 Catch:{ all -> 0x0055 }
        if (r3 != 0) goto L_0x0032;
    L_0x0031:
        goto L_0x0053;
    L_0x0032:
        r3 = r7.mCurMethod;	 Catch:{ RemoteException -> 0x0038 }
        r3.viewClicked(r1);	 Catch:{ RemoteException -> 0x0038 }
        goto L_0x0051;
    L_0x0038:
        r3 = move-exception;
        r4 = "InputMethodManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r5.<init>();	 Catch:{ all -> 0x0055 }
        r6 = "IME died: ";
        r5.append(r6);	 Catch:{ all -> 0x0055 }
        r6 = r7.mCurId;	 Catch:{ all -> 0x0055 }
        r5.append(r6);	 Catch:{ all -> 0x0055 }
        r5 = r5.toString();	 Catch:{ all -> 0x0055 }
        android.util.Log.w(r4, r5, r3);	 Catch:{ all -> 0x0055 }
    L_0x0051:
        monitor-exit(r2);	 Catch:{ all -> 0x0055 }
        return;
    L_0x0053:
        monitor-exit(r2);	 Catch:{ all -> 0x0055 }
        return;
    L_0x0055:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0055 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.viewClicked(android.view.View):void");
    }

    @Deprecated
    public boolean isWatchingCursor(View view) {
        return false;
    }

    @UnsupportedAppUsage
    public boolean isCursorAnchorInfoEnabled() {
        boolean z;
        synchronized (this.mH) {
            z = true;
            boolean isImmediate = (this.mRequestUpdateCursorAnchorInfoMonitorMode & 1) != 0;
            boolean isMonitoring = (this.mRequestUpdateCursorAnchorInfoMonitorMode & 2) != 0;
            if (!isImmediate) {
                if (!isMonitoring) {
                    z = false;
                }
            }
        }
        return z;
    }

    @UnsupportedAppUsage
    public void setUpdateCursorAnchorInfoMode(int flags) {
        synchronized (this.mH) {
            this.mRequestUpdateCursorAnchorInfoMonitorMode = flags;
        }
    }

    /* JADX WARNING: Missing block: B:28:0x0069, code skipped:
            return;
     */
    @java.lang.Deprecated
    public void updateCursor(android.view.View r8, int r9, int r10, int r11, int r12) {
        /*
        r7 = this;
        r6 = r7.getFallbackInputMethodManagerIfNecessary(r8);
        if (r6 == 0) goto L_0x0010;
    L_0x0006:
        r0 = r6;
        r1 = r8;
        r2 = r9;
        r3 = r10;
        r4 = r11;
        r5 = r12;
        r0.updateCursor(r1, r2, r3, r4, r5);
        return;
    L_0x0010:
        r7.checkFocus();
        r0 = r7.mH;
        monitor-enter(r0);
        r1 = r7.mServedView;	 Catch:{ all -> 0x006a }
        if (r1 == r8) goto L_0x0026;
    L_0x001a:
        r1 = r7.mServedView;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x0068;
    L_0x001e:
        r1 = r7.mServedView;	 Catch:{ all -> 0x006a }
        r1 = r1.checkInputConnectionProxy(r8);	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x0068;
    L_0x0026:
        r1 = r7.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x0068;
    L_0x002a:
        r1 = r7.mCurMethod;	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x002f;
    L_0x002e:
        goto L_0x0068;
    L_0x002f:
        r1 = r7.mTmpCursorRect;	 Catch:{ all -> 0x006a }
        r1.set(r9, r10, r11, r12);	 Catch:{ all -> 0x006a }
        r1 = r7.mCursorRect;	 Catch:{ all -> 0x006a }
        r2 = r7.mTmpCursorRect;	 Catch:{ all -> 0x006a }
        r1 = r1.equals(r2);	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x0066;
    L_0x003e:
        r1 = r7.mCurMethod;	 Catch:{ RemoteException -> 0x004d }
        r2 = r7.mTmpCursorRect;	 Catch:{ RemoteException -> 0x004d }
        r1.updateCursor(r2);	 Catch:{ RemoteException -> 0x004d }
        r1 = r7.mCursorRect;	 Catch:{ RemoteException -> 0x004d }
        r2 = r7.mTmpCursorRect;	 Catch:{ RemoteException -> 0x004d }
        r1.set(r2);	 Catch:{ RemoteException -> 0x004d }
        goto L_0x0066;
    L_0x004d:
        r1 = move-exception;
        r2 = "InputMethodManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006a }
        r3.<init>();	 Catch:{ all -> 0x006a }
        r4 = "IME died: ";
        r3.append(r4);	 Catch:{ all -> 0x006a }
        r4 = r7.mCurId;	 Catch:{ all -> 0x006a }
        r3.append(r4);	 Catch:{ all -> 0x006a }
        r3 = r3.toString();	 Catch:{ all -> 0x006a }
        android.util.Log.w(r2, r3, r1);	 Catch:{ all -> 0x006a }
    L_0x0066:
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x0068:
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x006a:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateCursor(android.view.View, int, int, int, int):void");
    }

    /* JADX WARNING: Missing block: B:42:0x007e, code skipped:
            return;
     */
    public void updateCursorAnchorInfo(android.view.View r8, android.view.inputmethod.CursorAnchorInfo r9) {
        /*
        r7 = this;
        if (r8 == 0) goto L_0x0082;
    L_0x0002:
        if (r9 != 0) goto L_0x0006;
    L_0x0004:
        goto L_0x0082;
    L_0x0006:
        r0 = r7.getFallbackInputMethodManagerIfNecessary(r8);
        if (r0 == 0) goto L_0x0010;
    L_0x000c:
        r0.updateCursorAnchorInfo(r8, r9);
        return;
    L_0x0010:
        r7.checkFocus();
        r1 = r7.mH;
        monitor-enter(r1);
        r2 = r7.mServedView;	 Catch:{ all -> 0x007f }
        if (r2 == r8) goto L_0x0026;
    L_0x001a:
        r2 = r7.mServedView;	 Catch:{ all -> 0x007f }
        if (r2 == 0) goto L_0x007d;
    L_0x001e:
        r2 = r7.mServedView;	 Catch:{ all -> 0x007f }
        r2 = r2.checkInputConnectionProxy(r8);	 Catch:{ all -> 0x007f }
        if (r2 == 0) goto L_0x007d;
    L_0x0026:
        r2 = r7.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x007f }
        if (r2 == 0) goto L_0x007d;
    L_0x002a:
        r2 = r7.mCurMethod;	 Catch:{ all -> 0x007f }
        if (r2 != 0) goto L_0x002f;
    L_0x002e:
        goto L_0x007d;
    L_0x002f:
        r2 = r7.mRequestUpdateCursorAnchorInfoMonitorMode;	 Catch:{ all -> 0x007f }
        r3 = 1;
        r2 = r2 & r3;
        if (r2 == 0) goto L_0x0036;
    L_0x0035:
        goto L_0x0037;
    L_0x0036:
        r3 = 0;
    L_0x0037:
        r2 = r3;
        if (r2 != 0) goto L_0x0044;
    L_0x003a:
        r3 = r7.mCursorAnchorInfo;	 Catch:{ all -> 0x007f }
        r3 = java.util.Objects.equals(r3, r9);	 Catch:{ all -> 0x007f }
        if (r3 == 0) goto L_0x0044;
    L_0x0042:
        monitor-exit(r1);	 Catch:{ all -> 0x007f }
        return;
    L_0x0044:
        r3 = r7.mActivityViewToScreenMatrix;	 Catch:{ RemoteException -> 0x0062 }
        if (r3 == 0) goto L_0x0054;
    L_0x0048:
        r3 = r7.mCurMethod;	 Catch:{ RemoteException -> 0x0062 }
        r4 = r7.mActivityViewToScreenMatrix;	 Catch:{ RemoteException -> 0x0062 }
        r4 = android.view.inputmethod.CursorAnchorInfo.createForAdditionalParentMatrix(r9, r4);	 Catch:{ RemoteException -> 0x0062 }
        r3.updateCursorAnchorInfo(r4);	 Catch:{ RemoteException -> 0x0062 }
        goto L_0x0059;
    L_0x0054:
        r3 = r7.mCurMethod;	 Catch:{ RemoteException -> 0x0062 }
        r3.updateCursorAnchorInfo(r9);	 Catch:{ RemoteException -> 0x0062 }
    L_0x0059:
        r7.mCursorAnchorInfo = r9;	 Catch:{ RemoteException -> 0x0062 }
        r3 = r7.mRequestUpdateCursorAnchorInfoMonitorMode;	 Catch:{ RemoteException -> 0x0062 }
        r3 = r3 & -2;
        r7.mRequestUpdateCursorAnchorInfoMonitorMode = r3;	 Catch:{ RemoteException -> 0x0062 }
        goto L_0x007b;
    L_0x0062:
        r3 = move-exception;
        r4 = "InputMethodManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x007f }
        r5.<init>();	 Catch:{ all -> 0x007f }
        r6 = "IME died: ";
        r5.append(r6);	 Catch:{ all -> 0x007f }
        r6 = r7.mCurId;	 Catch:{ all -> 0x007f }
        r5.append(r6);	 Catch:{ all -> 0x007f }
        r5 = r5.toString();	 Catch:{ all -> 0x007f }
        android.util.Log.w(r4, r5, r3);	 Catch:{ all -> 0x007f }
    L_0x007b:
        monitor-exit(r1);	 Catch:{ all -> 0x007f }
        return;
    L_0x007d:
        monitor-exit(r1);	 Catch:{ all -> 0x007f }
        return;
    L_0x007f:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x007f }
        throw r2;
    L_0x0082:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateCursorAnchorInfo(android.view.View, android.view.inputmethod.CursorAnchorInfo):void");
    }

    /* JADX WARNING: Missing block: B:25:0x004b, code skipped:
            return;
     */
    public void sendAppPrivateCommand(android.view.View r7, java.lang.String r8, android.os.Bundle r9) {
        /*
        r6 = this;
        r0 = r6.getFallbackInputMethodManagerIfNecessary(r7);
        if (r0 == 0) goto L_0x000a;
    L_0x0006:
        r0.sendAppPrivateCommand(r7, r8, r9);
        return;
    L_0x000a:
        r6.checkFocus();
        r1 = r6.mH;
        monitor-enter(r1);
        r2 = r6.mServedView;	 Catch:{ all -> 0x004c }
        if (r2 == r7) goto L_0x0020;
    L_0x0014:
        r2 = r6.mServedView;	 Catch:{ all -> 0x004c }
        if (r2 == 0) goto L_0x004a;
    L_0x0018:
        r2 = r6.mServedView;	 Catch:{ all -> 0x004c }
        r2 = r2.checkInputConnectionProxy(r7);	 Catch:{ all -> 0x004c }
        if (r2 == 0) goto L_0x004a;
    L_0x0020:
        r2 = r6.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x004c }
        if (r2 == 0) goto L_0x004a;
    L_0x0024:
        r2 = r6.mCurMethod;	 Catch:{ all -> 0x004c }
        if (r2 != 0) goto L_0x0029;
    L_0x0028:
        goto L_0x004a;
    L_0x0029:
        r2 = r6.mCurMethod;	 Catch:{ RemoteException -> 0x002f }
        r2.appPrivateCommand(r8, r9);	 Catch:{ RemoteException -> 0x002f }
        goto L_0x0048;
    L_0x002f:
        r2 = move-exception;
        r3 = "InputMethodManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004c }
        r4.<init>();	 Catch:{ all -> 0x004c }
        r5 = "IME died: ";
        r4.append(r5);	 Catch:{ all -> 0x004c }
        r5 = r6.mCurId;	 Catch:{ all -> 0x004c }
        r4.append(r5);	 Catch:{ all -> 0x004c }
        r4 = r4.toString();	 Catch:{ all -> 0x004c }
        android.util.Log.w(r3, r4, r2);	 Catch:{ all -> 0x004c }
    L_0x0048:
        monitor-exit(r1);	 Catch:{ all -> 0x004c }
        return;
    L_0x004a:
        monitor-exit(r1);	 Catch:{ all -> 0x004c }
        return;
    L_0x004c:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x004c }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.sendAppPrivateCommand(android.view.View, java.lang.String, android.os.Bundle):void");
    }

    @Deprecated
    public void setInputMethod(IBinder token, String id) {
        if (token != null) {
            InputMethodPrivilegedOperationsRegistry.get(token).setInputMethod(id);
        } else if (id != null) {
            int myUid = Process.myUid();
            String str = TAG;
            if (myUid == 1000) {
                Log.w(str, "System process should not be calling setInputMethod() because almost always it is a bug under multi-user / multi-profile environment. Consider interacting with InputMethodManagerService directly via LocalServices.");
                return;
            }
            Context fallbackContext = ActivityThread.currentApplication();
            if (fallbackContext != null && fallbackContext.checkSelfPermission(permission.WRITE_SECURE_SETTINGS) == 0) {
                List<InputMethodInfo> imis = getEnabledInputMethodList();
                int numImis = imis.size();
                boolean found = false;
                for (int i = 0; i < numImis; i++) {
                    if (id.equals(((InputMethodInfo) imis.get(i)).getId())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    Log.w(str, "The undocumented behavior that setInputMethod() accepts null token when the caller has WRITE_SECURE_SETTINGS is deprecated. This behavior may be completely removed in a future version.  Update secure settings directly instead.");
                    ContentResolver resolver = fallbackContext.getContentResolver();
                    Secure.putInt(resolver, Secure.SELECTED_INPUT_METHOD_SUBTYPE, -1);
                    Secure.putString(resolver, Secure.DEFAULT_INPUT_METHOD, id);
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring setInputMethod(null, ");
                stringBuilder.append(id);
                stringBuilder.append(") because the specified id not found in enabled IMEs.");
                Log.e(str, stringBuilder.toString());
            }
        }
    }

    @Deprecated
    public void setInputMethodAndSubtype(IBinder token, String id, InputMethodSubtype subtype) {
        if (token == null) {
            Log.e(TAG, "setInputMethodAndSubtype() does not accept null token on Android Q and later.");
        } else {
            InputMethodPrivilegedOperationsRegistry.get(token).setInputMethodAndSubtype(id, subtype);
        }
    }

    @Deprecated
    public void hideSoftInputFromInputMethod(IBinder token, int flags) {
        InputMethodPrivilegedOperationsRegistry.get(token).hideMySoftInput(flags);
    }

    @Deprecated
    public void showSoftInputFromInputMethod(IBinder token, int flags) {
        InputMethodPrivilegedOperationsRegistry.get(token).showMySoftInput(flags);
    }

    public int dispatchInputEvent(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
        synchronized (this.mH) {
            if (this.mCurMethod != null) {
                if (event instanceof KeyEvent) {
                    KeyEvent keyEvent = (KeyEvent) event;
                    if (keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 63 && keyEvent.getRepeatCount() == 0) {
                        showInputMethodPickerLocked();
                        return 1;
                    }
                }
                PendingEvent p = obtainPendingEventLocked(event, token, this.mCurId, callback, handler);
                if (this.mMainLooper.isCurrentThread()) {
                    int sendInputEventOnMainLooperLocked = sendInputEventOnMainLooperLocked(p);
                    return sendInputEventOnMainLooperLocked;
                }
                Message msg = this.mH.obtainMessage(5, p);
                msg.setAsynchronous(true);
                this.mH.sendMessage(msg);
                return -1;
            }
            return 0;
        }
    }

    public void dispatchKeyEventFromInputMethod(View targetView, KeyEvent event) {
        InputMethodManager fallbackImm = getFallbackInputMethodManagerIfNecessary(targetView);
        if (fallbackImm != null) {
            fallbackImm.dispatchKeyEventFromInputMethod(targetView, event);
            return;
        }
        synchronized (this.mH) {
            ViewRootImpl viewRootImpl;
            if (targetView != null) {
                try {
                    viewRootImpl = targetView.getViewRootImpl();
                } catch (Throwable th) {
                }
            } else {
                viewRootImpl = null;
            }
            if (viewRootImpl == null && this.mServedView != null) {
                viewRootImpl = this.mServedView.getViewRootImpl();
            }
            if (viewRootImpl != null) {
                viewRootImpl.dispatchKeyFromIme(event);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void sendInputEventAndReportResultOnMainLooper(PendingEvent p) {
        synchronized (this.mH) {
            int result = sendInputEventOnMainLooperLocked(p);
            if (result == -1) {
                return;
            }
            boolean z = true;
            if (result != 1) {
                z = false;
            }
            boolean handled = z;
            invokeFinishedInputEventCallback(p, handled);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int sendInputEventOnMainLooperLocked(PendingEvent p) {
        InputChannel inputChannel = this.mCurChannel;
        if (inputChannel != null) {
            if (this.mCurSender == null) {
                this.mCurSender = new ImeInputEventSender(inputChannel, this.mH.getLooper());
            }
            InputEvent event = p.mEvent;
            int seq = event.getSequenceNumber();
            if (this.mCurSender.sendInputEvent(seq, event)) {
                this.mPendingEvents.put(seq, p);
                Trace.traceCounter(4, PENDING_EVENT_COUNTER, this.mPendingEvents.size());
                Message msg = this.mH.obtainMessage(6, seq, 0, p);
                msg.setAsynchronous(true);
                this.mH.sendMessageDelayed(msg, INPUT_METHOD_NOT_RESPONDING_TIMEOUT);
                return -1;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to send input event to IME: ");
            stringBuilder.append(this.mCurId);
            stringBuilder.append(" dropping: ");
            stringBuilder.append(event);
            Log.w(TAG, stringBuilder.toString());
        }
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:12:0x0049, code skipped:
            invokeFinishedInputEventCallback(r2, r9);
     */
    /* JADX WARNING: Missing block: B:13:0x004c, code skipped:
            return;
     */
    public void finishedInputEvent(int r8, boolean r9, boolean r10) {
        /*
        r7 = this;
        r0 = r7.mH;
        monitor-enter(r0);
        r1 = r7.mPendingEvents;	 Catch:{ all -> 0x004d }
        r1 = r1.indexOfKey(r8);	 Catch:{ all -> 0x004d }
        if (r1 >= 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        return;
    L_0x000d:
        r2 = r7.mPendingEvents;	 Catch:{ all -> 0x004d }
        r2 = r2.valueAt(r1);	 Catch:{ all -> 0x004d }
        r2 = (android.view.inputmethod.InputMethodManager.PendingEvent) r2;	 Catch:{ all -> 0x004d }
        r3 = r7.mPendingEvents;	 Catch:{ all -> 0x004d }
        r3.removeAt(r1);	 Catch:{ all -> 0x004d }
        r3 = 4;
        r5 = "aq:imm";
        r6 = r7.mPendingEvents;	 Catch:{ all -> 0x004d }
        r6 = r6.size();	 Catch:{ all -> 0x004d }
        android.os.Trace.traceCounter(r3, r5, r6);	 Catch:{ all -> 0x004d }
        if (r10 == 0) goto L_0x0042;
    L_0x0029:
        r3 = "InputMethodManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004d }
        r4.<init>();	 Catch:{ all -> 0x004d }
        r5 = "Timeout waiting for IME to handle input event after 2500 ms: ";
        r4.append(r5);	 Catch:{ all -> 0x004d }
        r5 = r2.mInputMethodId;	 Catch:{ all -> 0x004d }
        r4.append(r5);	 Catch:{ all -> 0x004d }
        r4 = r4.toString();	 Catch:{ all -> 0x004d }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x004d }
        goto L_0x0048;
    L_0x0042:
        r3 = r7.mH;	 Catch:{ all -> 0x004d }
        r4 = 6;
        r3.removeMessages(r4, r2);	 Catch:{ all -> 0x004d }
    L_0x0048:
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        r7.invokeFinishedInputEventCallback(r2, r9);
        return;
    L_0x004d:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.finishedInputEvent(int, boolean, boolean):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void invokeFinishedInputEventCallback(PendingEvent p, boolean handled) {
        p.mHandled = handled;
        if (p.mHandler.getLooper().isCurrentThread()) {
            p.run();
            return;
        }
        Message msg = Message.obtain(p.mHandler, (Runnable) p);
        msg.setAsynchronous(true);
        msg.sendToTarget();
    }

    private void flushPendingEventsLocked() {
        this.mH.removeMessages(7);
        int count = this.mPendingEvents.size();
        for (int i = 0; i < count; i++) {
            Message msg = this.mH.obtainMessage(7, this.mPendingEvents.keyAt(i), 0);
            msg.setAsynchronous(true);
            msg.sendToTarget();
        }
    }

    private PendingEvent obtainPendingEventLocked(InputEvent event, Object token, String inputMethodId, FinishedInputEventCallback callback, Handler handler) {
        PendingEvent p = (PendingEvent) this.mPendingEventPool.acquire();
        if (p == null) {
            p = new PendingEvent(this, null);
        }
        p.mEvent = event;
        p.mToken = token;
        p.mInputMethodId = inputMethodId;
        p.mCallback = callback;
        p.mHandler = handler;
        return p;
    }

    private void recyclePendingEventLocked(PendingEvent p) {
        p.recycle();
        this.mPendingEventPool.release(p);
    }

    public void showInputMethodPicker() {
        synchronized (this.mH) {
            showInputMethodPickerLocked();
        }
    }

    public void showInputMethodPickerFromSystem(boolean showAuxiliarySubtypes, int displayId) {
        int mode;
        if (showAuxiliarySubtypes) {
            mode = 1;
        } else {
            mode = 2;
        }
        try {
            this.mService.showInputMethodPickerFromSystem(this.mClient, mode, displayId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void showInputMethodPickerLocked() {
        try {
            this.mService.showInputMethodPickerFromClient(this.mClient, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isInputMethodPickerShown() {
        try {
            return this.mService.isInputMethodPickerShownForTest();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void showInputMethodAndSubtypeEnabler(String imiId) {
        try {
            this.mService.showInputMethodAndSubtypeEnablerFromClient(this.mClient, imiId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public InputMethodSubtype getCurrentInputMethodSubtype() {
        try {
            return this.mService.getCurrentInputMethodSubtype();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean setCurrentInputMethodSubtype(InputMethodSubtype subtype) {
        if (Process.myUid() == 1000) {
            Log.w(TAG, "System process should not call setCurrentInputMethodSubtype() because almost always it is a bug under multi-user / multi-profile environment. Consider directly interacting with InputMethodManagerService via LocalServices.");
            return false;
        } else if (subtype == null) {
            return false;
        } else {
            Context fallbackContext = ActivityThread.currentApplication();
            if (fallbackContext == null || fallbackContext.checkSelfPermission(permission.WRITE_SECURE_SETTINGS) != 0) {
                return false;
            }
            ContentResolver contentResolver = fallbackContext.getContentResolver();
            String imeId = Secure.getString(contentResolver, Secure.DEFAULT_INPUT_METHOD);
            if (ComponentName.unflattenFromString(imeId) == null) {
                return false;
            }
            try {
                List<InputMethodSubtype> enabledSubtypes = this.mService.getEnabledInputMethodSubtypeList(imeId, true);
                int numSubtypes = enabledSubtypes.size();
                for (int i = 0; i < numSubtypes; i++) {
                    InputMethodSubtype enabledSubtype = (InputMethodSubtype) enabledSubtypes.get(i);
                    if (enabledSubtype.equals(subtype)) {
                        Secure.putInt(contentResolver, Secure.SELECTED_INPUT_METHOD_SUBTYPE, enabledSubtype.hashCode());
                        return true;
                    }
                }
                return false;
            } catch (RemoteException e) {
                return false;
            }
        }
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 114740982)
    public void notifyUserAction() {
        Log.w(TAG, "notifyUserAction() is a hidden method, which is now just a stub method that does nothing.  Leave comments in b.android.com/114740982 if your  application still depends on the previous behavior of this method.");
    }

    public Map<InputMethodInfo, List<InputMethodSubtype>> getShortcutInputMethodsAndSubtypes() {
        List<InputMethodInfo> enabledImes = getEnabledInputMethodList();
        enabledImes.sort(Comparator.comparingInt(-$$Lambda$InputMethodManager$pvWYFFVbHzZCDCCTiZVM09Xls4w.INSTANCE));
        int numEnabledImes = enabledImes.size();
        for (int imiIndex = 0; imiIndex < numEnabledImes; imiIndex++) {
            InputMethodInfo imi = (InputMethodInfo) enabledImes.get(imiIndex);
            int subtypeCount = getEnabledInputMethodSubtypeList(imi, true).size();
            for (int subtypeIndex = 0; subtypeIndex < subtypeCount; subtypeIndex++) {
                InputMethodSubtype subtype = imi.getSubtypeAt(subtypeIndex);
                if (SUBTYPE_MODE_VOICE.equals(subtype.getMode())) {
                    return Collections.singletonMap(imi, Collections.singletonList(subtype));
                }
            }
        }
        return Collections.emptyMap();
    }

    @UnsupportedAppUsage
    public int getInputMethodWindowVisibleHeight() {
        try {
            return this.mService.getInputMethodWindowVisibleHeight();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void reportActivityView(int childDisplayId, Matrix matrix) {
        float[] matrixValues;
        if (matrix == null) {
            matrixValues = null;
        } else {
            try {
                matrixValues = new float[9];
                matrix.getValues(matrixValues);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        this.mService.reportActivityView(this.mClient, childDisplayId, matrixValues);
    }

    @Deprecated
    public boolean switchToLastInputMethod(IBinder imeToken) {
        return InputMethodPrivilegedOperationsRegistry.get(imeToken).switchToPreviousInputMethod();
    }

    @Deprecated
    public boolean switchToNextInputMethod(IBinder imeToken, boolean onlyCurrentIme) {
        return InputMethodPrivilegedOperationsRegistry.get(imeToken).switchToNextInputMethod(onlyCurrentIme);
    }

    @Deprecated
    public boolean shouldOfferSwitchingToNextInputMethod(IBinder imeToken) {
        return InputMethodPrivilegedOperationsRegistry.get(imeToken).shouldOfferSwitchingToNextInputMethod();
    }

    @Deprecated
    public void setAdditionalInputMethodSubtypes(String imiId, InputMethodSubtype[] subtypes) {
        try {
            this.mService.setAdditionalInputMethodSubtypes(imiId, subtypes);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public InputMethodSubtype getLastInputMethodSubtype() {
        try {
            return this.mService.getLastInputMethodSubtype();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void maybeCallServedViewChangedLocked(EditorInfo tba) {
        ImeInsetsSourceConsumer imeInsetsSourceConsumer = this.mImeInsetsConsumer;
        if (imeInsetsSourceConsumer != null) {
            imeInsetsSourceConsumer.onServedEditorChanged(tba);
        }
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    /* Access modifiers changed, original: 0000 */
    public void doDump(FileDescriptor fd, PrintWriter fout, String[] args) {
        Printer p = new PrintWriterPrinter(fout);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Input method client state for ");
        stringBuilder.append(this);
        stringBuilder.append(":");
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mService=");
        stringBuilder.append(this.mService);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mMainLooper=");
        stringBuilder.append(this.mMainLooper);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mIInputContext=");
        stringBuilder.append(this.mIInputContext);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mActive=");
        stringBuilder.append(this.mActive);
        stringBuilder.append(" mRestartOnNextWindowFocus=");
        stringBuilder.append(this.mRestartOnNextWindowFocus);
        stringBuilder.append(" mBindSequence=");
        stringBuilder.append(this.mBindSequence);
        stringBuilder.append(" mCurId=");
        stringBuilder.append(this.mCurId);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mFullscreenMode=");
        stringBuilder.append(this.mFullscreenMode);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mCurMethod=");
        stringBuilder.append(this.mCurMethod);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mCurRootView=");
        stringBuilder.append(this.mCurRootView);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mServedView=");
        stringBuilder.append(this.mServedView);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mNextServedView=");
        stringBuilder.append(this.mNextServedView);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mServedConnecting=");
        stringBuilder.append(this.mServedConnecting);
        p.println(stringBuilder.toString());
        if (this.mCurrentTextBoxAttribute != null) {
            p.println("  mCurrentTextBoxAttribute:");
            this.mCurrentTextBoxAttribute.dump(p, "    ");
        } else {
            p.println("  mCurrentTextBoxAttribute: null");
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mServedInputConnectionWrapper=");
        stringBuilder.append(this.mServedInputConnectionWrapper);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mCompletions=");
        stringBuilder.append(Arrays.toString(this.mCompletions));
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mCursorRect=");
        stringBuilder.append(this.mCursorRect);
        p.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  mCursorSelStart=");
        stringBuilder.append(this.mCursorSelStart);
        stringBuilder.append(" mCursorSelEnd=");
        stringBuilder.append(this.mCursorSelEnd);
        stringBuilder.append(" mCursorCandStart=");
        stringBuilder.append(this.mCursorCandStart);
        stringBuilder.append(" mCursorCandEnd=");
        stringBuilder.append(this.mCursorCandEnd);
        p.println(stringBuilder.toString());
    }

    private static String dumpViewInfo(View view) {
        if (view == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(view);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(",focus=");
        stringBuilder.append(view.hasFocus());
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",windowFocus=");
        stringBuilder.append(view.hasWindowFocus());
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",autofillUiShowing=");
        stringBuilder.append(isAutofillUIShowing(view));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",window=");
        stringBuilder.append(view.getWindowToken());
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",displayId=");
        stringBuilder.append(view.getContext().getDisplayId());
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",temporaryDetach=");
        stringBuilder.append(view.isTemporarilyDetached());
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
