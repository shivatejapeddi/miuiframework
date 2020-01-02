package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.StatsLogInternal;

public class GestureDetector {
    private static final int DOUBLE_TAP_MIN_TIME = ViewConfiguration.getDoubleTapMinTime();
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private boolean mAlwaysInBiggerTapRegion;
    @UnsupportedAppUsage
    private boolean mAlwaysInTapRegion;
    private OnContextClickListener mContextClickListener;
    private MotionEvent mCurrentDownEvent;
    private MotionEvent mCurrentMotionEvent;
    private boolean mDeferConfirmSingleTap;
    private OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private int mDoubleTapTouchSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mHasRecordedClassification;
    private boolean mIgnoreNextUpEvent;
    private boolean mInContextClick;
    private boolean mInLongPress;
    private final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    @UnsupportedAppUsage
    private final OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    @UnsupportedAppUsage
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    @UnsupportedAppUsage
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;

    private class GestureHandler extends Handler {
        GestureHandler() {
        }

        GestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                GestureDetector.this.mListener.onShowPress(GestureDetector.this.mCurrentDownEvent);
            } else if (i == 2) {
                GestureDetector.this.recordGestureClassification(msg.arg1);
                GestureDetector.this.dispatchLongPress();
            } else if (i != 3) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown message ");
                stringBuilder.append(msg);
                throw new RuntimeException(stringBuilder.toString());
            } else if (GestureDetector.this.mDoubleTapListener == null) {
            } else {
                if (GestureDetector.this.mStillDown) {
                    GestureDetector.this.mDeferConfirmSingleTap = true;
                    return;
                }
                GestureDetector.this.recordGestureClassification(1);
                GestureDetector.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetector.this.mCurrentDownEvent);
            }
        }
    }

    public interface OnContextClickListener {
        boolean onContextClick(MotionEvent motionEvent);
    }

    public interface OnDoubleTapListener {
        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTapEvent(MotionEvent motionEvent);

        boolean onSingleTapConfirmed(MotionEvent motionEvent);
    }

    public interface OnGestureListener {
        boolean onDown(MotionEvent motionEvent);

        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onShowPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);
    }

    public static class SimpleOnGestureListener implements OnGestureListener, OnDoubleTapListener, OnContextClickListener {
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onDown(MotionEvent e) {
            return false;
        }

        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        public boolean onContextClick(MotionEvent e) {
            return false;
        }
    }

    @Deprecated
    public GestureDetector(OnGestureListener listener, Handler handler) {
        this(null, listener, handler);
    }

    @Deprecated
    public GestureDetector(OnGestureListener listener) {
        this(null, listener, null);
    }

    public GestureDetector(Context context, OnGestureListener listener) {
        this(context, listener, null);
    }

    public GestureDetector(Context context, OnGestureListener listener, Handler handler) {
        this.mInputEventConsistencyVerifier = InputEventConsistencyVerifier.isInstrumentationEnabled() ? new InputEventConsistencyVerifier(this, 0) : null;
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        } else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = listener;
        if (listener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) listener);
        }
        if (listener instanceof OnContextClickListener) {
            setContextClickListener((OnContextClickListener) listener);
        }
        init(context);
    }

    public GestureDetector(Context context, OnGestureListener listener, Handler handler, boolean unused) {
        this(context, listener, handler);
    }

    private void init(Context context) {
        if (this.mListener != null) {
            int touchSlop;
            int doubleTapTouchSlop;
            int doubleTapSlop;
            this.mIsLongpressEnabled = true;
            if (context == null) {
                touchSlop = ViewConfiguration.getTouchSlop();
                doubleTapTouchSlop = touchSlop;
                doubleTapSlop = ViewConfiguration.getDoubleTapSlop();
                this.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
                this.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            } else {
                ViewConfiguration configuration = ViewConfiguration.get(context);
                doubleTapTouchSlop = configuration.getScaledTouchSlop();
                doubleTapSlop = configuration.getScaledDoubleTapTouchSlop();
                int doubleTapSlop2 = configuration.getScaledDoubleTapSlop();
                this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
                this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
                touchSlop = doubleTapTouchSlop;
                doubleTapTouchSlop = doubleTapSlop;
                doubleTapSlop = doubleTapSlop2;
            }
            this.mTouchSlopSquare = touchSlop * touchSlop;
            this.mDoubleTapTouchSlopSquare = doubleTapTouchSlop * doubleTapTouchSlop;
            this.mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
            return;
        }
        throw new NullPointerException("OnGestureListener must not be null");
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }

    public void setContextClickListener(OnContextClickListener onContextClickListener) {
        this.mContextClickListener = onContextClickListener;
    }

    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        this.mIsLongpressEnabled = isLongpressEnabled;
    }

    public boolean isLongpressEnabled() {
        return this.mIsLongpressEnabled;
    }

    /* JADX WARNING: Removed duplicated region for block: B:153:0x03df  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x03fa  */
    public boolean onTouchEvent(android.view.MotionEvent r40) {
        /*
        r39 = this;
        r0 = r39;
        r1 = r40;
        r2 = r0.mInputEventConsistencyVerifier;
        r3 = 0;
        if (r2 == 0) goto L_0x000c;
    L_0x0009:
        r2.onTouchEvent(r1, r3);
    L_0x000c:
        r2 = r40.getAction();
        r4 = r0.mCurrentMotionEvent;
        if (r4 == 0) goto L_0x0017;
    L_0x0014:
        r4.recycle();
    L_0x0017:
        r4 = android.view.MotionEvent.obtain(r40);
        r0.mCurrentMotionEvent = r4;
        r4 = r0.mVelocityTracker;
        if (r4 != 0) goto L_0x0027;
    L_0x0021:
        r4 = android.view.VelocityTracker.obtain();
        r0.mVelocityTracker = r4;
    L_0x0027:
        r4 = r0.mVelocityTracker;
        r4.addMovement(r1);
        r4 = r2 & 255;
        r5 = 6;
        r6 = 1;
        if (r4 != r5) goto L_0x0034;
    L_0x0032:
        r4 = r6;
        goto L_0x0035;
    L_0x0034:
        r4 = r3;
    L_0x0035:
        if (r4 == 0) goto L_0x003c;
    L_0x0037:
        r7 = r40.getActionIndex();
        goto L_0x003d;
    L_0x003c:
        r7 = -1;
        r8 = r40.getFlags();
        r8 = r8 & 8;
        if (r8 == 0) goto L_0x0048;
    L_0x0046:
        r8 = r6;
        goto L_0x0049;
    L_0x0048:
        r8 = r3;
    L_0x0049:
        r9 = 0;
        r10 = 0;
        r11 = r40.getPointerCount();
        r12 = 0;
    L_0x0050:
        if (r12 >= r11) goto L_0x0062;
    L_0x0052:
        if (r7 != r12) goto L_0x0055;
    L_0x0054:
        goto L_0x005f;
    L_0x0055:
        r13 = r1.getX(r12);
        r9 = r9 + r13;
        r13 = r1.getY(r12);
        r10 = r10 + r13;
    L_0x005f:
        r12 = r12 + 1;
        goto L_0x0050;
    L_0x0062:
        if (r4 == 0) goto L_0x0067;
    L_0x0064:
        r12 = r11 + -1;
        goto L_0x0068;
    L_0x0067:
        r12 = r11;
    L_0x0068:
        r13 = (float) r12;
        r13 = r9 / r13;
        r14 = (float) r12;
        r14 = r10 / r14;
        r15 = 0;
        r3 = r2 & 255;
        if (r3 == 0) goto L_0x037c;
    L_0x0073:
        if (r3 == r6) goto L_0x02c4;
    L_0x0075:
        r6 = 5;
        r5 = 2;
        if (r3 == r5) goto L_0x014b;
    L_0x0079:
        r5 = 3;
        if (r3 == r5) goto L_0x0136;
    L_0x007c:
        if (r3 == r6) goto L_0x0119;
    L_0x007e:
        r5 = 6;
        if (r3 == r5) goto L_0x0093;
    L_0x0081:
        r16 = r2;
        r21 = r4;
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        goto L_0x02c0;
    L_0x0093:
        r0.mLastFocusX = r13;
        r0.mDownFocusX = r13;
        r0.mLastFocusY = r14;
        r0.mDownFocusY = r14;
        r3 = r0.mVelocityTracker;
        r5 = r0.mMaximumFlingVelocity;
        r5 = (float) r5;
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3.computeCurrentVelocity(r6, r5);
        r3 = r40.getActionIndex();
        r5 = r1.getPointerId(r3);
        r6 = r0.mVelocityTracker;
        r6 = r6.getXVelocity(r5);
        r16 = r2;
        r2 = r0.mVelocityTracker;
        r2 = r2.getYVelocity(r5);
        r17 = 0;
        r21 = r4;
        r4 = r17;
    L_0x00c1:
        if (r4 >= r11) goto L_0x0103;
    L_0x00c3:
        if (r4 != r3) goto L_0x00ce;
    L_0x00c5:
        r22 = r2;
        r17 = r3;
        r18 = r5;
        r19 = r6;
        goto L_0x00f8;
    L_0x00ce:
        r17 = r3;
        r3 = r1.getPointerId(r4);
        r18 = r5;
        r5 = r0.mVelocityTracker;
        r5 = r5.getXVelocity(r3);
        r5 = r5 * r6;
        r19 = r6;
        r6 = r0.mVelocityTracker;
        r6 = r6.getYVelocity(r3);
        r6 = r6 * r2;
        r20 = r5 + r6;
        r22 = 0;
        r22 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1));
        if (r22 >= 0) goto L_0x00f6;
    L_0x00ee:
        r22 = r2;
        r2 = r0.mVelocityTracker;
        r2.clear();
        goto L_0x010b;
    L_0x00f6:
        r22 = r2;
    L_0x00f8:
        r4 = r4 + 1;
        r3 = r17;
        r5 = r18;
        r6 = r19;
        r2 = r22;
        goto L_0x00c1;
    L_0x0103:
        r22 = r2;
        r17 = r3;
        r18 = r5;
        r19 = r6;
    L_0x010b:
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        goto L_0x02c0;
    L_0x0119:
        r16 = r2;
        r21 = r4;
        r0.mLastFocusX = r13;
        r0.mDownFocusX = r13;
        r0.mLastFocusY = r14;
        r0.mDownFocusY = r14;
        r39.cancelTaps();
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        goto L_0x02c0;
    L_0x0136:
        r16 = r2;
        r21 = r4;
        r39.cancel();
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        goto L_0x02c0;
    L_0x014b:
        r16 = r2;
        r21 = r4;
        r2 = r0.mInLongPress;
        if (r2 != 0) goto L_0x02b4;
    L_0x0153:
        r2 = r0.mInContextClick;
        if (r2 == 0) goto L_0x0165;
    L_0x0157:
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        goto L_0x02c0;
    L_0x0165:
        r2 = r40.getClassification();
        r3 = r0.mHandler;
        r4 = 2;
        r3 = r3.hasMessages(r4);
        r5 = r0.mLastFocusX;
        r5 = r5 - r13;
        r6 = r0.mLastFocusY;
        r6 = r6 - r14;
        r4 = r0.mIsDoubleTapping;
        if (r4 == 0) goto L_0x0196;
    L_0x017a:
        r4 = 2;
        r0.recordGestureClassification(r4);
        r4 = r0.mDoubleTapListener;
        r4 = r4.onDoubleTapEvent(r1);
        r4 = r4 | r15;
        r34 = r2;
        r33 = r3;
        r15 = r4;
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        goto L_0x0291;
    L_0x0196:
        r4 = r0.mAlwaysInTapRegion;
        if (r4 == 0) goto L_0x025a;
    L_0x019a:
        r4 = r0.mDownFocusX;
        r4 = r13 - r4;
        r4 = (int) r4;
        r22 = r7;
        r7 = r0.mDownFocusY;
        r7 = r14 - r7;
        r7 = (int) r7;
        r23 = r4 * r4;
        r24 = r7 * r7;
        r25 = r4;
        r4 = r23 + r24;
        r23 = r7;
        if (r8 == 0) goto L_0x01b4;
    L_0x01b2:
        r7 = 0;
        goto L_0x01b6;
    L_0x01b4:
        r7 = r0.mTouchSlopSquare;
    L_0x01b6:
        r24 = r9;
        r9 = 1;
        if (r2 != r9) goto L_0x01bd;
    L_0x01bb:
        r9 = 1;
        goto L_0x01be;
    L_0x01bd:
        r9 = 0;
    L_0x01be:
        if (r3 == 0) goto L_0x01c5;
    L_0x01c0:
        if (r9 == 0) goto L_0x01c5;
    L_0x01c2:
        r26 = 1;
        goto L_0x01c7;
    L_0x01c5:
        r26 = 0;
    L_0x01c7:
        if (r26 == 0) goto L_0x0215;
    L_0x01c9:
        r27 = android.view.ViewConfiguration.getAmbiguousGestureMultiplier();
        if (r4 <= r7) goto L_0x0201;
    L_0x01cf:
        r28 = r9;
        r9 = r0.mHandler;
        r29 = r10;
        r10 = 2;
        r9.removeMessages(r10);
        r9 = android.view.ViewConfiguration.getLongPressTimeout();
        r30 = r11;
        r10 = (long) r9;
        r9 = r0.mHandler;
        r34 = r2;
        r33 = r3;
        r31 = r12;
        r32 = r15;
        r3 = 0;
        r12 = 3;
        r15 = 2;
        r2 = r9.obtainMessage(r15, r12, r3);
        r35 = r40.getDownTime();
        r3 = (float) r10;
        r3 = r3 * r27;
        r37 = r10;
        r10 = (long) r3;
        r10 = r35 + r10;
        r9.sendMessageAtTime(r2, r10);
        goto L_0x020f;
    L_0x0201:
        r34 = r2;
        r33 = r3;
        r28 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
    L_0x020f:
        r2 = (float) r7;
        r3 = r27 * r27;
        r2 = r2 * r3;
        r7 = (int) r2;
        goto L_0x0223;
    L_0x0215:
        r34 = r2;
        r33 = r3;
        r28 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
    L_0x0223:
        if (r4 <= r7) goto L_0x024b;
    L_0x0225:
        r2 = 5;
        r0.recordGestureClassification(r2);
        r2 = r0.mListener;
        r3 = r0.mCurrentDownEvent;
        r15 = r2.onScroll(r3, r1, r5, r6);
        r0.mLastFocusX = r13;
        r0.mLastFocusY = r14;
        r2 = 0;
        r0.mAlwaysInTapRegion = r2;
        r2 = r0.mHandler;
        r3 = 3;
        r2.removeMessages(r3);
        r2 = r0.mHandler;
        r3 = 1;
        r2.removeMessages(r3);
        r2 = r0.mHandler;
        r3 = 2;
        r2.removeMessages(r3);
        goto L_0x024d;
    L_0x024b:
        r15 = r32;
    L_0x024d:
        if (r8 == 0) goto L_0x0251;
    L_0x024f:
        r3 = 0;
        goto L_0x0253;
    L_0x0251:
        r3 = r0.mDoubleTapTouchSlopSquare;
    L_0x0253:
        r2 = r3;
        if (r4 <= r2) goto L_0x0259;
    L_0x0256:
        r3 = 0;
        r0.mAlwaysInBiggerTapRegion = r3;
    L_0x0259:
        goto L_0x0291;
    L_0x025a:
        r34 = r2;
        r33 = r3;
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        r2 = java.lang.Math.abs(r5);
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 >= 0) goto L_0x0280;
    L_0x0274:
        r2 = java.lang.Math.abs(r6);
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 < 0) goto L_0x027d;
    L_0x027c:
        goto L_0x0280;
    L_0x027d:
        r15 = r32;
        goto L_0x0291;
    L_0x0280:
        r2 = 5;
        r0.recordGestureClassification(r2);
        r2 = r0.mListener;
        r3 = r0.mCurrentDownEvent;
        r2 = r2.onScroll(r3, r1, r5, r6);
        r0.mLastFocusX = r13;
        r0.mLastFocusY = r14;
        r15 = r2;
    L_0x0291:
        r2 = r34;
        r3 = 2;
        if (r2 != r3) goto L_0x0299;
    L_0x0296:
        r19 = 1;
        goto L_0x029b;
    L_0x0299:
        r19 = 0;
    L_0x029b:
        r3 = r19;
        if (r3 == 0) goto L_0x042e;
    L_0x029f:
        if (r33 == 0) goto L_0x042e;
    L_0x02a1:
        r4 = r0.mHandler;
        r7 = 2;
        r4.removeMessages(r7);
        r4 = r0.mHandler;
        r9 = 4;
        r10 = 0;
        r7 = r4.obtainMessage(r7, r9, r10);
        r4.sendMessage(r7);
        goto L_0x042e;
    L_0x02b4:
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
    L_0x02c0:
        r15 = r32;
        goto L_0x042e;
    L_0x02c4:
        r16 = r2;
        r21 = r4;
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        r2 = 0;
        r0.mStillDown = r2;
        r2 = android.view.MotionEvent.obtain(r40);
        r3 = r0.mIsDoubleTapping;
        if (r3 == 0) goto L_0x02ec;
    L_0x02df:
        r3 = 2;
        r0.recordGestureClassification(r3);
        r3 = r0.mDoubleTapListener;
        r3 = r3.onDoubleTapEvent(r1);
        r15 = r32 | r3;
        goto L_0x0354;
    L_0x02ec:
        r3 = r0.mInLongPress;
        if (r3 == 0) goto L_0x02fa;
    L_0x02f0:
        r3 = r0.mHandler;
        r4 = 3;
        r3.removeMessages(r4);
        r3 = 0;
        r0.mInLongPress = r3;
        goto L_0x0352;
    L_0x02fa:
        r3 = r0.mAlwaysInTapRegion;
        if (r3 == 0) goto L_0x0318;
    L_0x02fe:
        r3 = r0.mIgnoreNextUpEvent;
        if (r3 != 0) goto L_0x0318;
    L_0x0302:
        r3 = 1;
        r0.recordGestureClassification(r3);
        r3 = r0.mListener;
        r15 = r3.onSingleTapUp(r1);
        r3 = r0.mDeferConfirmSingleTap;
        if (r3 == 0) goto L_0x0354;
    L_0x0310:
        r3 = r0.mDoubleTapListener;
        if (r3 == 0) goto L_0x0354;
    L_0x0314:
        r3.onSingleTapConfirmed(r1);
        goto L_0x0354;
    L_0x0318:
        r3 = r0.mIgnoreNextUpEvent;
        if (r3 != 0) goto L_0x0352;
    L_0x031c:
        r3 = r0.mVelocityTracker;
        r4 = 0;
        r5 = r1.getPointerId(r4);
        r4 = r0.mMaximumFlingVelocity;
        r4 = (float) r4;
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3.computeCurrentVelocity(r6, r4);
        r4 = r3.getYVelocity(r5);
        r6 = r3.getXVelocity(r5);
        r7 = java.lang.Math.abs(r4);
        r9 = r0.mMinimumFlingVelocity;
        r9 = (float) r9;
        r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r7 > 0) goto L_0x0349;
    L_0x033e:
        r7 = java.lang.Math.abs(r6);
        r9 = r0.mMinimumFlingVelocity;
        r9 = (float) r9;
        r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r7 <= 0) goto L_0x0352;
    L_0x0349:
        r7 = r0.mListener;
        r9 = r0.mCurrentDownEvent;
        r15 = r7.onFling(r9, r1, r6, r4);
        goto L_0x0354;
    L_0x0352:
        r15 = r32;
    L_0x0354:
        r3 = r0.mPreviousUpEvent;
        if (r3 == 0) goto L_0x035b;
    L_0x0358:
        r3.recycle();
    L_0x035b:
        r0.mPreviousUpEvent = r2;
        r3 = r0.mVelocityTracker;
        if (r3 == 0) goto L_0x0367;
    L_0x0361:
        r3.recycle();
        r3 = 0;
        r0.mVelocityTracker = r3;
    L_0x0367:
        r3 = 0;
        r0.mIsDoubleTapping = r3;
        r0.mDeferConfirmSingleTap = r3;
        r0.mIgnoreNextUpEvent = r3;
        r3 = r0.mHandler;
        r4 = 1;
        r3.removeMessages(r4);
        r3 = r0.mHandler;
        r4 = 2;
        r3.removeMessages(r4);
        goto L_0x042e;
    L_0x037c:
        r16 = r2;
        r21 = r4;
        r22 = r7;
        r24 = r9;
        r29 = r10;
        r30 = r11;
        r31 = r12;
        r32 = r15;
        r2 = r0.mDoubleTapListener;
        if (r2 == 0) goto L_0x03d1;
    L_0x0390:
        r2 = r0.mHandler;
        r3 = 3;
        r2 = r2.hasMessages(r3);
        if (r2 == 0) goto L_0x039e;
    L_0x0399:
        r4 = r0.mHandler;
        r4.removeMessages(r3);
    L_0x039e:
        r3 = r0.mCurrentDownEvent;
        if (r3 == 0) goto L_0x03c8;
    L_0x03a2:
        r4 = r0.mPreviousUpEvent;
        if (r4 == 0) goto L_0x03c8;
    L_0x03a6:
        if (r2 == 0) goto L_0x03c8;
    L_0x03a8:
        r3 = r0.isConsideredDoubleTap(r3, r4, r1);
        if (r3 == 0) goto L_0x03c8;
    L_0x03ae:
        r3 = 1;
        r0.mIsDoubleTapping = r3;
        r3 = 2;
        r0.recordGestureClassification(r3);
        r3 = r0.mDoubleTapListener;
        r4 = r0.mCurrentDownEvent;
        r3 = r3.onDoubleTap(r4);
        r3 = r32 | r3;
        r4 = r0.mDoubleTapListener;
        r4 = r4.onDoubleTapEvent(r1);
        r15 = r3 | r4;
        goto L_0x03d3;
    L_0x03c8:
        r3 = r0.mHandler;
        r4 = DOUBLE_TAP_TIMEOUT;
        r4 = (long) r4;
        r6 = 3;
        r3.sendEmptyMessageDelayed(r6, r4);
    L_0x03d1:
        r15 = r32;
    L_0x03d3:
        r0.mLastFocusX = r13;
        r0.mDownFocusX = r13;
        r0.mLastFocusY = r14;
        r0.mDownFocusY = r14;
        r2 = r0.mCurrentDownEvent;
        if (r2 == 0) goto L_0x03e2;
    L_0x03df:
        r2.recycle();
    L_0x03e2:
        r2 = android.view.MotionEvent.obtain(r40);
        r0.mCurrentDownEvent = r2;
        r2 = 1;
        r0.mAlwaysInTapRegion = r2;
        r0.mAlwaysInBiggerTapRegion = r2;
        r0.mStillDown = r2;
        r2 = 0;
        r0.mInLongPress = r2;
        r0.mDeferConfirmSingleTap = r2;
        r0.mHasRecordedClassification = r2;
        r3 = r0.mIsLongpressEnabled;
        if (r3 == 0) goto L_0x0416;
    L_0x03fa:
        r3 = r0.mHandler;
        r4 = 2;
        r3.removeMessages(r4);
        r3 = r0.mHandler;
        r5 = 3;
        r4 = r3.obtainMessage(r4, r5, r2);
        r2 = r0.mCurrentDownEvent;
        r5 = r2.getDownTime();
        r2 = android.view.ViewConfiguration.getLongPressTimeout();
        r9 = (long) r2;
        r5 = r5 + r9;
        r3.sendMessageAtTime(r4, r5);
    L_0x0416:
        r2 = r0.mHandler;
        r3 = r0.mCurrentDownEvent;
        r3 = r3.getDownTime();
        r5 = TAP_TIMEOUT;
        r5 = (long) r5;
        r3 = r3 + r5;
        r5 = 1;
        r2.sendEmptyMessageAtTime(r5, r3);
        r2 = r0.mListener;
        r2 = r2.onDown(r1);
        r15 = r15 | r2;
    L_0x042e:
        if (r15 != 0) goto L_0x0438;
    L_0x0430:
        r2 = r0.mInputEventConsistencyVerifier;
        if (r2 == 0) goto L_0x0438;
    L_0x0434:
        r3 = 0;
        r2.onUnhandledEvent(r1, r3);
    L_0x0438:
        return r15;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.GestureDetector.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onGenericMotionEvent(MotionEvent ev) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onGenericMotionEvent(ev, 0);
        }
        int actionButton = ev.getActionButton();
        int actionMasked = ev.getActionMasked();
        if (actionMasked != 11) {
            if (actionMasked == 12 && this.mInContextClick && (actionButton == 32 || actionButton == 2)) {
                this.mInContextClick = false;
                this.mIgnoreNextUpEvent = true;
            }
        } else if (!(this.mContextClickListener == null || this.mInContextClick || this.mInLongPress || ((actionButton != 32 && actionButton != 2) || !this.mContextClickListener.onContextClick(ev)))) {
            this.mInContextClick = true;
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            return true;
        }
        return false;
    }

    private void cancel() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mIsDoubleTapping = false;
        this.mStillDown = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    private void cancelTaps() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mIsDoubleTapping = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
        boolean z = false;
        if (!this.mAlwaysInBiggerTapRegion) {
            return false;
        }
        long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
        if (deltaTime > ((long) DOUBLE_TAP_TIMEOUT) || deltaTime < ((long) DOUBLE_TAP_MIN_TIME)) {
            return false;
        }
        int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
        int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
        if ((deltaX * deltaX) + (deltaY * deltaY) < ((firstDown.getFlags() & 8) != 0 ? 0 : this.mDoubleTapSlopSquare)) {
            z = true;
        }
        return z;
    }

    private void dispatchLongPress() {
        this.mHandler.removeMessages(3);
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = true;
        this.mListener.onLongPress(this.mCurrentDownEvent);
    }

    private void recordGestureClassification(int classification) {
        if (!this.mHasRecordedClassification && classification != 0) {
            if (this.mCurrentDownEvent == null || this.mCurrentMotionEvent == null) {
                this.mHasRecordedClassification = true;
                return;
            }
            StatsLogInternal.write(177, getClass().getName(), classification, (int) (SystemClock.uptimeMillis() - this.mCurrentMotionEvent.getDownTime()), (float) Math.hypot((double) (this.mCurrentMotionEvent.getRawX() - this.mCurrentDownEvent.getRawX()), (double) (this.mCurrentMotionEvent.getRawY() - this.mCurrentDownEvent.getRawY())));
            this.mHasRecordedClassification = true;
        }
    }
}
