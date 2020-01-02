package android.view;

import android.annotation.UnsupportedAppUsage;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.util.Log;
import android.util.Slog;
import android.util.SparseIntArray;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public abstract class InputEventReceiver {
    private static final int INPUT_DISPATCH_THRESHOLD_MS = 100;
    private static final int SLOW_INPUT_THRESHOLD_MS = 300;
    private static final String TAG = "InputEventReceiver";
    Choreographer mChoreographer;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private int mDispatchSeqNumber;
    private long mDispatchStartTime;
    private InputChannel mInputChannel;
    private MessageQueue mMessageQueue;
    private long mReceiverPtr;
    private final SparseIntArray mSeqMap = new SparseIntArray();

    public interface Factory {
        InputEventReceiver createInputEventReceiver(InputChannel inputChannel, Looper looper);
    }

    private static native boolean nativeConsumeBatchedInputEvents(long j, long j2);

    private static native void nativeDispose(long j);

    private static native void nativeFinishInputEvent(long j, int i, boolean z);

    private static native long nativeInit(WeakReference<InputEventReceiver> weakReference, InputChannel inputChannel, MessageQueue messageQueue);

    public InputEventReceiver(InputChannel inputChannel, Looper looper) {
        if (inputChannel == null) {
            throw new IllegalArgumentException("inputChannel must not be null");
        } else if (looper != null) {
            this.mInputChannel = inputChannel;
            this.mMessageQueue = looper.getQueue();
            this.mReceiverPtr = nativeInit(new WeakReference(this), inputChannel, this.mMessageQueue);
            this.mCloseGuard.open("dispose");
        } else {
            throw new IllegalArgumentException("looper must not be null");
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public void dispose() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            if (finalized) {
                closeGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        long j = this.mReceiverPtr;
        if (j != 0) {
            nativeDispose(j);
            this.mReceiverPtr = 0;
        }
        this.mInputChannel = null;
        this.mMessageQueue = null;
    }

    @UnsupportedAppUsage
    public void onInputEvent(InputEvent event) {
        finishInputEvent(event, false);
    }

    public void onBatchedInputEventPending() {
        consumeBatchedInputEvents(-1);
    }

    public final void finishInputEvent(InputEvent event, boolean handled) {
        if (event != null) {
            int i = (this.mReceiverPtr > 0 ? 1 : (this.mReceiverPtr == 0 ? 0 : -1));
            String str = TAG;
            if (i == 0) {
                Log.w(str, "Attempted to finish an input event but the input event receiver has already been disposed.");
            } else {
                i = this.mSeqMap.indexOfKey(event.getSequenceNumber());
                if (i < 0) {
                    Log.w(str, "Attempted to finish an input event that is not in progress.");
                } else {
                    int seq = this.mSeqMap.valueAt(i);
                    this.mSeqMap.removeAt(i);
                    nativeFinishInputEvent(this.mReceiverPtr, seq, handled);
                    if (event.getSequenceNumber() == this.mDispatchSeqNumber) {
                        long duration = SystemClock.uptimeMillis() - this.mDispatchStartTime;
                        if (duration > 100) {
                            String str2 = ")";
                            String str3 = ", action=";
                            String str4 = ", seq=";
                            String str5 = "App Input: Dispatching InputEvent took ";
                            StringBuilder stringBuilder;
                            if (event instanceof KeyEvent) {
                                KeyEvent keyEvent = (KeyEvent) event;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str5);
                                stringBuilder.append(duration);
                                stringBuilder.append("ms in main thread! (KeyEvent: event_seq=");
                                stringBuilder.append(event.getSequenceNumber());
                                stringBuilder.append(str4);
                                stringBuilder.append(seq);
                                stringBuilder.append(", code=");
                                stringBuilder.append(KeyEvent.keyCodeToString(keyEvent.getKeyCode()));
                                stringBuilder.append(str3);
                                stringBuilder.append(KeyEvent.actionToString(keyEvent.getAction()));
                                stringBuilder.append(str2);
                                Slog.w(str, stringBuilder.toString());
                            } else if (event instanceof MotionEvent) {
                                MotionEvent motionEvent = (MotionEvent) event;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str5);
                                stringBuilder.append(duration);
                                stringBuilder.append("ms in main thread! (MotionEvent: event_seq=");
                                stringBuilder.append(event.getSequenceNumber());
                                stringBuilder.append(str4);
                                stringBuilder.append(seq);
                                stringBuilder.append(str3);
                                stringBuilder.append(MotionEvent.actionToString(motionEvent.getAction()));
                                stringBuilder.append(str2);
                                Slog.w(str, stringBuilder.toString());
                            }
                        }
                    }
                }
            }
            event.recycleIfNeededAfterDispatch();
            return;
        }
        throw new IllegalArgumentException("event must not be null");
    }

    public final boolean consumeBatchedInputEvents(long frameTimeNanos) {
        long j = this.mReceiverPtr;
        if (j != 0) {
            return nativeConsumeBatchedInputEvents(j, frameTimeNanos);
        }
        Log.w(TAG, "Attempted to consume batched input events but the input event receiver has already been disposed.");
        return false;
    }

    @UnsupportedAppUsage
    private void dispatchInputEvent(int seq, InputEvent event) {
        this.mDispatchStartTime = SystemClock.uptimeMillis();
        this.mDispatchSeqNumber = event.getSequenceNumber();
        Looper.myLooper().getMessageMonitor().checkInputEvent(event);
        long duration = this.mDispatchStartTime - event.getEventTime();
        if (duration > 300) {
            boolean z = event instanceof KeyEvent;
            String str = ")";
            String str2 = ", action=";
            String str3 = ", seq=";
            String str4 = "App Input: ";
            String str5 = TAG;
            StringBuilder stringBuilder;
            if (z) {
                KeyEvent keyEvent = (KeyEvent) event;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str4);
                stringBuilder.append(duration);
                stringBuilder.append("ms before dispatchInputEvent (KeyEvent: event_seq=");
                stringBuilder.append(event.getSequenceNumber());
                stringBuilder.append(str3);
                stringBuilder.append(seq);
                stringBuilder.append(", code=");
                stringBuilder.append(KeyEvent.keyCodeToString(keyEvent.getKeyCode()));
                stringBuilder.append(str2);
                stringBuilder.append(KeyEvent.actionToString(keyEvent.getAction()));
                stringBuilder.append(str);
                Slog.w(str5, stringBuilder.toString());
            } else if (event instanceof MotionEvent) {
                MotionEvent motionEvent = (MotionEvent) event;
                if (motionEvent.getAction() != 2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str4);
                    stringBuilder.append(duration);
                    stringBuilder.append("ms before dispatchInputEvent (MotionEvent: event_seq=");
                    stringBuilder.append(event.getSequenceNumber());
                    stringBuilder.append(str3);
                    stringBuilder.append(seq);
                    stringBuilder.append(str2);
                    stringBuilder.append(MotionEvent.actionToString(motionEvent.getAction()));
                    stringBuilder.append(str);
                    Slog.w(str5, stringBuilder.toString());
                }
            }
        }
        this.mSeqMap.put(event.getSequenceNumber(), seq);
        onInputEvent(event);
    }

    @UnsupportedAppUsage
    private void dispatchBatchedInputEventPending() {
        onBatchedInputEventPending();
    }

    private void dispatchMotionEventInfo(int motionEventType, int touchMoveNum) {
        try {
            if (this.mChoreographer == null) {
                this.mChoreographer = Choreographer.getInstance();
            }
            if (this.mChoreographer != null) {
                this.mChoreographer.setMotionEventInfo(motionEventType, touchMoveNum);
            }
        } catch (Exception e) {
            Log.e(TAG, "cannot invoke setMotionEventInfo.");
        }
    }
}
