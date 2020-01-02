package android.view;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.ArrayMap;
import android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo;

public class TouchDelegate {
    public static final int ABOVE = 1;
    public static final int BELOW = 2;
    public static final int TO_LEFT = 4;
    public static final int TO_RIGHT = 8;
    private Rect mBounds;
    @UnsupportedAppUsage
    private boolean mDelegateTargeted;
    private View mDelegateView;
    private int mSlop;
    private Rect mSlopBounds;
    private TouchDelegateInfo mTouchDelegateInfo;

    public TouchDelegate(Rect bounds, View delegateView) {
        this.mBounds = bounds;
        this.mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        this.mSlopBounds = new Rect(bounds);
        Rect rect = this.mSlopBounds;
        int i = this.mSlop;
        rect.inset(-i, -i);
        this.mDelegateView = delegateView;
    }

    /* JADX WARNING: Missing block: B:10:0x0020, code skipped:
            if (r5 != 6) goto L_0x0042;
     */
    public boolean onTouchEvent(android.view.MotionEvent r9) {
        /*
        r8 = this;
        r0 = r9.getX();
        r0 = (int) r0;
        r1 = r9.getY();
        r1 = (int) r1;
        r2 = 0;
        r3 = 1;
        r4 = 0;
        r5 = r9.getActionMasked();
        r6 = 2;
        if (r5 == 0) goto L_0x0037;
    L_0x0014:
        r7 = 1;
        if (r5 == r7) goto L_0x0029;
    L_0x0017:
        if (r5 == r6) goto L_0x0029;
    L_0x0019:
        r7 = 3;
        if (r5 == r7) goto L_0x0023;
    L_0x001c:
        r7 = 5;
        if (r5 == r7) goto L_0x0029;
    L_0x001f:
        r7 = 6;
        if (r5 == r7) goto L_0x0029;
    L_0x0022:
        goto L_0x0042;
    L_0x0023:
        r2 = r8.mDelegateTargeted;
        r5 = 0;
        r8.mDelegateTargeted = r5;
        goto L_0x0042;
    L_0x0029:
        r2 = r8.mDelegateTargeted;
        if (r2 == 0) goto L_0x0042;
    L_0x002d:
        r5 = r8.mSlopBounds;
        r7 = r5.contains(r0, r1);
        if (r7 != 0) goto L_0x0036;
    L_0x0035:
        r3 = 0;
    L_0x0036:
        goto L_0x0042;
    L_0x0037:
        r5 = r8.mBounds;
        r5 = r5.contains(r0, r1);
        r8.mDelegateTargeted = r5;
        r2 = r8.mDelegateTargeted;
    L_0x0042:
        if (r2 == 0) goto L_0x006d;
    L_0x0044:
        if (r3 == 0) goto L_0x005a;
    L_0x0046:
        r5 = r8.mDelegateView;
        r5 = r5.getWidth();
        r5 = r5 / r6;
        r5 = (float) r5;
        r7 = r8.mDelegateView;
        r7 = r7.getHeight();
        r7 = r7 / r6;
        r6 = (float) r7;
        r9.setLocation(r5, r6);
        goto L_0x0067;
    L_0x005a:
        r5 = r8.mSlop;
        r6 = r5 * 2;
        r6 = -r6;
        r6 = (float) r6;
        r7 = r5 * 2;
        r7 = -r7;
        r7 = (float) r7;
        r9.setLocation(r6, r7);
    L_0x0067:
        r5 = r8.mDelegateView;
        r4 = r5.dispatchTouchEvent(r9);
    L_0x006d:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.TouchDelegate.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onTouchExplorationHoverEvent(MotionEvent event) {
        if (this.mBounds == null) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean hit = true;
        boolean handled = false;
        boolean isInbound = this.mBounds.contains(x, y);
        int actionMasked = event.getActionMasked();
        if (actionMasked != 7) {
            if (actionMasked == 9) {
                this.mDelegateTargeted = isInbound;
            } else if (actionMasked == 10) {
                this.mDelegateTargeted = true;
            }
        } else if (isInbound) {
            this.mDelegateTargeted = true;
        } else if (this.mDelegateTargeted && !this.mSlopBounds.contains(x, y)) {
            hit = false;
        }
        if (this.mDelegateTargeted) {
            if (hit) {
                event.setLocation((float) (this.mDelegateView.getWidth() / 2), (float) (this.mDelegateView.getHeight() / 2));
            } else {
                this.mDelegateTargeted = false;
            }
            handled = this.mDelegateView.dispatchHoverEvent(event);
        }
        return handled;
    }

    public TouchDelegateInfo getTouchDelegateInfo() {
        if (this.mTouchDelegateInfo == null) {
            ArrayMap<Region, View> targetMap = new ArrayMap(1);
            Rect bounds = this.mBounds;
            if (bounds == null) {
                bounds = new Rect();
            }
            targetMap.put(new Region(bounds), this.mDelegateView);
            this.mTouchDelegateInfo = new TouchDelegateInfo(targetMap);
        }
        return this.mTouchDelegateInfo;
    }
}
