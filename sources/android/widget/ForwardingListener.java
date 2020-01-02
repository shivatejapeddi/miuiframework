package android.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import com.android.internal.view.menu.ShowableListMenu;

public abstract class ForwardingListener implements OnTouchListener, OnAttachStateChangeListener {
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout = ((this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2);
    private final float mScaledTouchSlop;
    private final View mSrc;
    private final int mTapTimeout = ViewConfiguration.getTapTimeout();
    private Runnable mTriggerLongPress;

    private class DisallowIntercept implements Runnable {
        private DisallowIntercept() {
        }

        public void run() {
            ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private class TriggerLongPress implements Runnable {
        private TriggerLongPress() {
        }

        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }

    public abstract ShowableListMenu getPopup();

    public ForwardingListener(View src) {
        this.mSrc = src;
        src.setLongClickable(true);
        src.addOnAttachStateChangeListener(this);
        this.mScaledTouchSlop = (float) ViewConfiguration.get(src.getContext()).getScaledTouchSlop();
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean forwarding;
        MotionEvent motionEvent = event;
        boolean wasForwarding = this.mForwarding;
        if (wasForwarding) {
            forwarding = onTouchForwarded(motionEvent) || !onForwardingStopped();
        } else {
            forwarding = onTouchObserved(motionEvent) && onForwardingStarted();
            if (forwarding) {
                long now = SystemClock.uptimeMillis();
                MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                this.mSrc.onTouchEvent(e);
                e.recycle();
            }
        }
        this.mForwarding = forwarding;
        if (forwarding || wasForwarding) {
            return true;
        }
        return false;
    }

    public void onViewAttachedToWindow(View v) {
    }

    public void onViewDetachedFromWindow(View v) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        Runnable runnable = this.mDisallowIntercept;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onForwardingStarted() {
        ShowableListMenu popup = getPopup();
        if (!(popup == null || popup.isShowing())) {
            popup.show();
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean onForwardingStopped() {
        ShowableListMenu popup = getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
        return true;
    }

    /* JADX WARNING: Missing block: B:10:0x0017, code skipped:
            if (r1 != 3) goto L_0x0070;
     */
    private boolean onTouchObserved(android.view.MotionEvent r9) {
        /*
        r8 = this;
        r0 = r8.mSrc;
        r1 = r0.isEnabled();
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r1 = r9.getActionMasked();
        if (r1 == 0) goto L_0x0042;
    L_0x0010:
        r3 = 1;
        if (r1 == r3) goto L_0x003e;
    L_0x0013:
        r4 = 2;
        if (r1 == r4) goto L_0x001a;
    L_0x0016:
        r3 = 3;
        if (r1 == r3) goto L_0x003e;
    L_0x0019:
        goto L_0x0070;
    L_0x001a:
        r4 = r8.mActivePointerId;
        r4 = r9.findPointerIndex(r4);
        if (r4 < 0) goto L_0x0070;
    L_0x0022:
        r5 = r9.getX(r4);
        r6 = r9.getY(r4);
        r7 = r8.mScaledTouchSlop;
        r7 = r0.pointInView(r5, r6, r7);
        if (r7 != 0) goto L_0x003d;
    L_0x0032:
        r8.clearCallbacks();
        r2 = r0.getParent();
        r2.requestDisallowInterceptTouchEvent(r3);
        return r3;
    L_0x003d:
        goto L_0x0070;
    L_0x003e:
        r8.clearCallbacks();
        goto L_0x0070;
    L_0x0042:
        r3 = r9.getPointerId(r2);
        r8.mActivePointerId = r3;
        r3 = r8.mDisallowIntercept;
        r4 = 0;
        if (r3 != 0) goto L_0x0054;
    L_0x004d:
        r3 = new android.widget.ForwardingListener$DisallowIntercept;
        r3.<init>();
        r8.mDisallowIntercept = r3;
    L_0x0054:
        r3 = r8.mDisallowIntercept;
        r5 = r8.mTapTimeout;
        r5 = (long) r5;
        r0.postDelayed(r3, r5);
        r3 = r8.mTriggerLongPress;
        if (r3 != 0) goto L_0x0067;
    L_0x0060:
        r3 = new android.widget.ForwardingListener$TriggerLongPress;
        r3.<init>();
        r8.mTriggerLongPress = r3;
    L_0x0067:
        r3 = r8.mTriggerLongPress;
        r4 = r8.mLongPressTimeout;
        r4 = (long) r4;
        r0.postDelayed(r3, r4);
    L_0x0070:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ForwardingListener.onTouchObserved(android.view.MotionEvent):boolean");
    }

    private void clearCallbacks() {
        Runnable runnable = this.mTriggerLongPress;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
        runnable = this.mDisallowIntercept;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0038, code skipped:
            return;
     */
    private void onLongPress() {
        /*
        r13 = this;
        r13.clearCallbacks();
        r0 = r13.mSrc;
        r1 = r0.isEnabled();
        if (r1 == 0) goto L_0x0038;
    L_0x000b:
        r1 = r0.isLongClickable();
        if (r1 == 0) goto L_0x0012;
    L_0x0011:
        goto L_0x0038;
    L_0x0012:
        r1 = r13.onForwardingStarted();
        if (r1 != 0) goto L_0x0019;
    L_0x0018:
        return;
    L_0x0019:
        r1 = r0.getParent();
        r2 = 1;
        r1.requestDisallowInterceptTouchEvent(r2);
        r11 = android.os.SystemClock.uptimeMillis();
        r7 = 3;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r3 = r11;
        r5 = r11;
        r1 = android.view.MotionEvent.obtain(r3, r5, r7, r8, r9, r10);
        r0.onTouchEvent(r1);
        r1.recycle();
        r13.mForwarding = r2;
        return;
    L_0x0038:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ForwardingListener.onLongPress():void");
    }

    private boolean onTouchForwarded(MotionEvent srcEvent) {
        View src = this.mSrc;
        ShowableListMenu popup = getPopup();
        boolean z = false;
        if (popup == null || !popup.isShowing()) {
            return false;
        }
        DropDownListView dst = (DropDownListView) popup.getListView();
        if (dst == null || !dst.isShown()) {
            return false;
        }
        MotionEvent dstEvent = MotionEvent.obtainNoHistory(srcEvent);
        src.toGlobalMotionEvent(dstEvent);
        dst.toLocalMotionEvent(dstEvent);
        boolean handled = dst.onForwardedEvent(dstEvent, this.mActivePointerId);
        dstEvent.recycle();
        int action = srcEvent.getActionMasked();
        boolean keepForwarding = (action == 1 || action == 3) ? false : true;
        if (handled && keepForwarding) {
            z = true;
        }
        return z;
    }
}
