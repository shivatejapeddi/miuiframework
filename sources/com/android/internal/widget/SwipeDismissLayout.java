package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.android.internal.R;

public class SwipeDismissLayout extends FrameLayout {
    private static final float MAX_DIST_THRESHOLD = 0.33f;
    private static final float MIN_DIST_THRESHOLD = 0.1f;
    private static final String TAG = "SwipeDismissLayout";
    private int mActiveTouchId;
    private boolean mActivityTranslucencyConverted = false;
    private boolean mBlockGesture = false;
    private boolean mDiscardIntercept;
    private final DismissAnimator mDismissAnimator = new DismissAnimator();
    private boolean mDismissable = true;
    private boolean mDismissed;
    private OnDismissedListener mDismissedListener;
    private float mDownX;
    private float mDownY;
    private boolean mIsWindowNativelyTranslucent;
    private float mLastX;
    private int mMinFlingVelocity;
    private OnSwipeProgressChangedListener mProgressListener;
    private IntentFilter mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
    private BroadcastReceiver mScreenOffReceiver;
    private int mSlop;
    private boolean mSwiping;
    private VelocityTracker mVelocityTracker;

    public interface OnDismissedListener {
        void onDismissed(SwipeDismissLayout swipeDismissLayout);
    }

    public interface OnSwipeProgressChangedListener {
        void onSwipeCancelled(SwipeDismissLayout swipeDismissLayout);

        void onSwipeProgressChanged(SwipeDismissLayout swipeDismissLayout, float f, float f2);
    }

    private class DismissAnimator implements AnimatorUpdateListener, AnimatorListener {
        private final long DISMISS_DURATION = 250;
        private final TimeInterpolator DISMISS_INTERPOLATOR = new DecelerateInterpolator(1.5f);
        private final ValueAnimator mDismissAnimator = new ValueAnimator();
        private boolean mDismissOnComplete = false;
        private boolean mWasCanceled = false;

        DismissAnimator() {
            this.mDismissAnimator.addUpdateListener(this);
            this.mDismissAnimator.addListener(this);
        }

        /* Access modifiers changed, original: 0000 */
        public void animateDismissal(float currentTranslation) {
            animate(currentTranslation / ((float) SwipeDismissLayout.this.getWidth()), 1.0f, 250, this.DISMISS_INTERPOLATOR, true);
        }

        /* Access modifiers changed, original: 0000 */
        public void animateRecovery(float currentTranslation) {
            animate(currentTranslation / ((float) SwipeDismissLayout.this.getWidth()), 0.0f, 250, this.DISMISS_INTERPOLATOR, false);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isAnimating() {
            return this.mDismissAnimator.isStarted();
        }

        private void animate(float from, float to, long duration, TimeInterpolator interpolator, boolean dismissOnComplete) {
            this.mDismissAnimator.cancel();
            this.mDismissOnComplete = dismissOnComplete;
            this.mDismissAnimator.setFloatValues(from, to);
            this.mDismissAnimator.setDuration(duration);
            this.mDismissAnimator.setInterpolator(interpolator);
            this.mDismissAnimator.start();
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            SwipeDismissLayout swipeDismissLayout = SwipeDismissLayout.this;
            swipeDismissLayout.setProgress(((float) swipeDismissLayout.getWidth()) * value);
        }

        public void onAnimationStart(Animator animation) {
            this.mWasCanceled = false;
        }

        public void onAnimationCancel(Animator animation) {
            this.mWasCanceled = true;
        }

        public void onAnimationEnd(Animator animation) {
            if (!this.mWasCanceled) {
                if (this.mDismissOnComplete) {
                    SwipeDismissLayout.this.dismiss();
                } else {
                    SwipeDismissLayout.this.cancel();
                }
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    public SwipeDismissLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        TypedArray a = context.getTheme().obtainStyledAttributes(R.styleable.Theme);
        this.mIsWindowNativelyTranslucent = a.getBoolean(5, false);
        a.recycle();
    }

    public void setOnDismissedListener(OnDismissedListener listener) {
        this.mDismissedListener = listener;
    }

    public void setOnSwipeProgressChangedListener(OnSwipeProgressChangedListener listener) {
        this.mProgressListener = listener;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            this.mScreenOffReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    SwipeDismissLayout.this.post(new -$$Lambda$SwipeDismissLayout$1$NDXsqpv65OVP2OutTHt-hDxJywg(this));
                }

                public /* synthetic */ void lambda$onReceive$0$SwipeDismissLayout$1() {
                    if (SwipeDismissLayout.this.mDismissed) {
                        SwipeDismissLayout.this.dismiss();
                    } else {
                        SwipeDismissLayout.this.cancel();
                    }
                    SwipeDismissLayout.this.resetMembers();
                }
            };
            getContext().registerReceiver(this.mScreenOffReceiver, this.mScreenOffFilter);
        } catch (ReceiverCallNotAllowedException e) {
            this.mScreenOffReceiver = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        if (this.mScreenOffReceiver != null) {
            getContext().unregisterReceiver(this.mScreenOffReceiver);
            this.mScreenOffReceiver = null;
        }
        super.onDetachedFromWindow();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        checkGesture(ev);
        boolean z = true;
        if (this.mBlockGesture) {
            return true;
        }
        if (!this.mDismissable) {
            return super.onInterceptTouchEvent(ev);
        }
        ev.offsetLocation(ev.getRawX() - ev.getX(), 0.0f);
        int actionMasked = ev.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            this.mActiveTouchId = ev.getPointerId(ev.getActionIndex());
                        } else if (actionMasked == 6) {
                            actionMasked = ev.getActionIndex();
                            if (ev.getPointerId(actionMasked) == this.mActiveTouchId) {
                                this.mActiveTouchId = ev.getPointerId(actionMasked == 0 ? 1 : 0);
                            }
                        }
                    }
                } else if (!(this.mVelocityTracker == null || this.mDiscardIntercept)) {
                    actionMasked = ev.findPointerIndex(this.mActiveTouchId);
                    if (actionMasked == -1) {
                        Log.e(TAG, "Invalid pointer index: ignoring.");
                        this.mDiscardIntercept = true;
                    } else {
                        float dx = ev.getRawX() - this.mDownX;
                        float x = ev.getX(actionMasked);
                        float y = ev.getY(actionMasked);
                        if (dx == 0.0f || !canScroll(this, false, dx, x, y)) {
                            updateSwiping(ev);
                        } else {
                            this.mDiscardIntercept = true;
                        }
                    }
                }
            }
            resetMembers();
        } else {
            resetMembers();
            this.mDownX = ev.getRawX();
            this.mDownY = ev.getRawY();
            this.mActiveTouchId = ev.getPointerId(0);
            this.mVelocityTracker = VelocityTracker.obtain("int1");
            this.mVelocityTracker.addMovement(ev);
        }
        if (this.mDiscardIntercept || !this.mSwiping) {
            z = false;
        }
        return z;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        checkGesture(ev);
        if (this.mBlockGesture) {
            return true;
        }
        if (this.mVelocityTracker == null || !this.mDismissable) {
            return super.onTouchEvent(ev);
        }
        ev.offsetLocation(ev.getRawX() - ev.getX(), 0.0f);
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 1) {
            updateDismiss(ev);
            if (this.mDismissed) {
                this.mDismissAnimator.animateDismissal(ev.getRawX() - this.mDownX);
            } else if (this.mSwiping && this.mLastX != -2.14748365E9f) {
                this.mDismissAnimator.animateRecovery(ev.getRawX() - this.mDownX);
            }
            resetMembers();
        } else if (actionMasked == 2) {
            this.mVelocityTracker.addMovement(ev);
            this.mLastX = ev.getRawX();
            updateSwiping(ev);
            if (this.mSwiping) {
                setProgress(ev.getRawX() - this.mDownX);
            }
        } else if (actionMasked == 3) {
            cancel();
            resetMembers();
        }
        return true;
    }

    private void setProgress(float deltaX) {
        OnSwipeProgressChangedListener onSwipeProgressChangedListener = this.mProgressListener;
        if (onSwipeProgressChangedListener != null && deltaX >= 0.0f) {
            onSwipeProgressChangedListener.onSwipeProgressChanged(this, progressToAlpha(deltaX / ((float) getWidth())), deltaX);
        }
    }

    private void dismiss() {
        OnDismissedListener onDismissedListener = this.mDismissedListener;
        if (onDismissedListener != null) {
            onDismissedListener.onDismissed(this);
        }
    }

    /* Access modifiers changed, original: protected */
    public void cancel() {
        if (!this.mIsWindowNativelyTranslucent) {
            Activity activity = findActivity();
            if (activity != null && this.mActivityTranslucencyConverted) {
                activity.convertFromTranslucent();
                this.mActivityTranslucencyConverted = false;
            }
        }
        OnSwipeProgressChangedListener onSwipeProgressChangedListener = this.mProgressListener;
        if (onSwipeProgressChangedListener != null) {
            onSwipeProgressChangedListener.onSwipeCancelled(this);
        }
    }

    private void resetMembers() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.mVelocityTracker = null;
        this.mDownX = 0.0f;
        this.mLastX = -2.14748365E9f;
        this.mDownY = 0.0f;
        this.mSwiping = false;
        this.mDismissed = false;
        this.mDiscardIntercept = false;
    }

    private void updateSwiping(MotionEvent ev) {
        boolean oldSwiping = this.mSwiping;
        if (!this.mSwiping) {
            float deltaX = ev.getRawX() - this.mDownX;
            float deltaY = ev.getRawY() - this.mDownY;
            float f = (deltaX * deltaX) + (deltaY * deltaY);
            int i = this.mSlop;
            boolean z = false;
            if (f > ((float) (i * i))) {
                if (deltaX > ((float) (i * 2)) && Math.abs(deltaY) < Math.abs(deltaX)) {
                    z = true;
                }
                this.mSwiping = z;
            } else {
                this.mSwiping = false;
            }
        }
        if (this.mSwiping && !oldSwiping && !this.mIsWindowNativelyTranslucent) {
            Activity activity = findActivity();
            if (activity != null) {
                this.mActivityTranslucencyConverted = activity.convertToTranslucent(null, null);
            }
        }
    }

    private void updateDismiss(MotionEvent ev) {
        float deltaX = ev.getRawX() - this.mDownX;
        this.mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.mVelocityTracker.getXVelocity();
        if (this.mLastX == -2.14748365E9f) {
            xVelocity = deltaX / ((float) ((ev.getEventTime() - ev.getDownTime()) / 1000));
        }
        if (!this.mDismissed && ((deltaX > ((float) getWidth()) * Math.max(Math.min(((-0.23000002f * xVelocity) / ((float) this.mMinFlingVelocity)) + MAX_DIST_THRESHOLD, MAX_DIST_THRESHOLD), 0.1f) && ev.getRawX() >= this.mLastX) || xVelocity >= ((float) this.mMinFlingVelocity))) {
            this.mDismissed = true;
        }
        if (this.mDismissed && this.mSwiping && xVelocity < ((float) (-this.mMinFlingVelocity))) {
            this.mDismissed = false;
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:18:0x007a, code skipped:
            if (r15.canScrollHorizontally((int) (-r17)) != false) goto L_0x0080;
     */
    public boolean canScroll(android.view.View r15, boolean r16, float r17, float r18, float r19) {
        /*
        r14 = this;
        r0 = r15;
        r1 = r0 instanceof android.view.ViewGroup;
        r2 = 1;
        if (r1 == 0) goto L_0x0070;
    L_0x0006:
        r1 = r0;
        r1 = (android.view.ViewGroup) r1;
        r3 = r15.getScrollX();
        r4 = r15.getScrollY();
        r5 = r1.getChildCount();
        r6 = r5 + -1;
    L_0x0017:
        if (r6 < 0) goto L_0x0070;
    L_0x0019:
        r13 = r1.getChildAt(r6);
        r7 = (float) r3;
        r7 = r18 + r7;
        r8 = r13.getLeft();
        r8 = (float) r8;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 < 0) goto L_0x006d;
    L_0x0029:
        r7 = (float) r3;
        r7 = r18 + r7;
        r8 = r13.getRight();
        r8 = (float) r8;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 >= 0) goto L_0x006d;
    L_0x0035:
        r7 = (float) r4;
        r7 = r19 + r7;
        r8 = r13.getTop();
        r8 = (float) r8;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 < 0) goto L_0x006d;
    L_0x0041:
        r7 = (float) r4;
        r7 = r19 + r7;
        r8 = r13.getBottom();
        r8 = (float) r8;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 >= 0) goto L_0x006d;
    L_0x004d:
        r9 = 1;
        r7 = (float) r3;
        r7 = r18 + r7;
        r8 = r13.getLeft();
        r8 = (float) r8;
        r11 = r7 - r8;
        r7 = (float) r4;
        r7 = r19 + r7;
        r8 = r13.getTop();
        r8 = (float) r8;
        r12 = r7 - r8;
        r7 = r14;
        r8 = r13;
        r10 = r17;
        r7 = r7.canScroll(r8, r9, r10, r11, r12);
        if (r7 == 0) goto L_0x006d;
    L_0x006c:
        return r2;
    L_0x006d:
        r6 = r6 + -1;
        goto L_0x0017;
    L_0x0070:
        if (r16 == 0) goto L_0x007d;
    L_0x0072:
        r1 = r17;
        r3 = -r1;
        r3 = (int) r3;
        r3 = r15.canScrollHorizontally(r3);
        if (r3 == 0) goto L_0x007f;
    L_0x007c:
        goto L_0x0080;
    L_0x007d:
        r1 = r17;
    L_0x007f:
        r2 = 0;
    L_0x0080:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.SwipeDismissLayout.canScroll(android.view.View, boolean, float, float, float):boolean");
    }

    public void setDismissable(boolean dismissable) {
        if (!dismissable && this.mDismissable) {
            cancel();
            resetMembers();
        }
        this.mDismissable = dismissable;
    }

    private void checkGesture(MotionEvent ev) {
        if (ev.getActionMasked() == 0) {
            this.mBlockGesture = this.mDismissAnimator.isAnimating();
        }
    }

    private float progressToAlpha(float progress) {
        return 1.0f - ((progress * progress) * progress);
    }

    private Activity findActivity() {
        for (Context context = getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }
}
