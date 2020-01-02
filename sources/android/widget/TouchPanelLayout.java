package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class TouchPanelLayout extends LinearLayout {
    private Rect mTemRect = new Rect();

    public TouchPanelLayout(Context context) {
        super(context);
    }

    public TouchPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* JADX WARNING: Missing block: B:4:0x0015, code skipped:
            if (r0 != 2) goto L_0x0019;
     */
    public boolean dispatchTouchEvent(android.view.MotionEvent r7) {
        /*
        r6 = this;
        r0 = r7.getActionMasked();
        r1 = r7.getX();
        r1 = (int) r1;
        r2 = r7.getY();
        r2 = (int) r2;
        r3 = 0;
        r4 = 1;
        if (r0 == 0) goto L_0x001d;
    L_0x0012:
        if (r0 == r4) goto L_0x0018;
    L_0x0014:
        r5 = 2;
        if (r0 == r5) goto L_0x001d;
    L_0x0017:
        goto L_0x0019;
    L_0x0018:
        r3 = 1;
    L_0x0019:
        r6.resetChildState(r3);
        goto L_0x0021;
    L_0x001d:
        r6.checkChildState(r1, r2);
    L_0x0021:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TouchPanelLayout.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    private void resetChildState(boolean performClick) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (performClick && child.isPressed()) {
                child.performClick();
            }
            child.setPressed(false);
        }
    }

    private void checkChildState(int x, int y) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(this.mTemRect);
                child.setPressed(this.mTemRect.contains(x, y));
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFitsSystemWindows(false);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetChildState(false);
    }
}
