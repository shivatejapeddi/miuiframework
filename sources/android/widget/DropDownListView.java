package android.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.widget.AutoScrollHelper.AbsListViewAutoScroller;

public class DropDownListView extends ListView {
    private boolean mDrawsInPressedState;
    private boolean mHijackFocus;
    private boolean mListSelectionHidden;
    private ResolveHoverRunnable mResolveHoverRunnable;
    private AbsListViewAutoScroller mScrollHelper;

    private class ResolveHoverRunnable implements Runnable {
        private ResolveHoverRunnable() {
        }

        public void run() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.drawableStateChanged();
        }

        public void cancel() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.removeCallbacks(this);
        }

        public void post() {
            DropDownListView.this.post(this);
        }
    }

    public DropDownListView(Context context, boolean hijackFocus) {
        this(context, hijackFocus, 16842861);
    }

    public DropDownListView(Context context, boolean hijackFocus, int defStyleAttr) {
        super(context, null, defStyleAttr);
        this.mHijackFocus = hijackFocus;
        setCacheColorHint(0);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldShowSelector() {
        return isHovered() || super.shouldShowSelector();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        ResolveHoverRunnable resolveHoverRunnable = this.mResolveHoverRunnable;
        if (resolveHoverRunnable != null) {
            resolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(ev);
    }

    public boolean onHoverEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 10 && this.mResolveHoverRunnable == null) {
            this.mResolveHoverRunnable = new ResolveHoverRunnable();
            this.mResolveHoverRunnable.post();
        }
        boolean handled = super.onHoverEvent(ev);
        if (action == 9 || action == 7) {
            int position = pointToPosition((int) ev.getX(), (int) ev.getY());
            if (!(position == -1 || position == this.mSelectedPosition)) {
                View hoveredItem = getChildAt(position - getFirstVisiblePosition());
                if (hoveredItem.isEnabled()) {
                    requestFocus();
                    positionSelector(position, hoveredItem);
                    setSelectedPositionInt(position);
                    setNextSelectedPositionInt(position);
                }
                updateSelectorState();
            }
        } else if (!super.shouldShowSelector()) {
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
        }
        return handled;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        if (this.mResolveHoverRunnable == null) {
            super.drawableStateChanged();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0050  */
    public boolean onForwardedEvent(android.view.MotionEvent r12, int r13) {
        /*
        r11 = this;
        r0 = 1;
        r1 = 0;
        r2 = r12.getActionMasked();
        r3 = 1;
        if (r2 == r3) goto L_0x0012;
    L_0x0009:
        r4 = 2;
        if (r2 == r4) goto L_0x0013;
    L_0x000c:
        r4 = 3;
        if (r2 == r4) goto L_0x0010;
    L_0x000f:
        goto L_0x0047;
    L_0x0010:
        r0 = 0;
        goto L_0x0047;
    L_0x0012:
        r0 = 0;
    L_0x0013:
        r4 = r12.findPointerIndex(r13);
        if (r4 >= 0) goto L_0x001b;
    L_0x0019:
        r0 = 0;
        goto L_0x0047;
    L_0x001b:
        r5 = r12.getX(r4);
        r5 = (int) r5;
        r6 = r12.getY(r4);
        r6 = (int) r6;
        r7 = r11.pointToPosition(r5, r6);
        r8 = -1;
        if (r7 != r8) goto L_0x002e;
    L_0x002c:
        r1 = 1;
        goto L_0x0047;
    L_0x002e:
        r8 = r11.getFirstVisiblePosition();
        r8 = r7 - r8;
        r8 = r11.getChildAt(r8);
        r9 = (float) r5;
        r10 = (float) r6;
        r11.setPressedItem(r8, r7, r9, r10);
        r0 = 1;
        if (r2 != r3) goto L_0x0047;
    L_0x0040:
        r9 = r11.getItemIdAtPosition(r7);
        r11.performItemClick(r8, r7, r9);
    L_0x0047:
        if (r0 == 0) goto L_0x004b;
    L_0x0049:
        if (r1 == 0) goto L_0x004e;
    L_0x004b:
        r11.clearPressedItem();
    L_0x004e:
        if (r0 == 0) goto L_0x0066;
    L_0x0050:
        r4 = r11.mScrollHelper;
        if (r4 != 0) goto L_0x005b;
    L_0x0054:
        r4 = new com.android.internal.widget.AutoScrollHelper$AbsListViewAutoScroller;
        r4.<init>(r11);
        r11.mScrollHelper = r4;
    L_0x005b:
        r4 = r11.mScrollHelper;
        r4.setEnabled(r3);
        r3 = r11.mScrollHelper;
        r3.onTouch(r11, r12);
        goto L_0x006e;
    L_0x0066:
        r3 = r11.mScrollHelper;
        if (r3 == 0) goto L_0x006e;
    L_0x006a:
        r4 = 0;
        r3.setEnabled(r4);
    L_0x006e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.DropDownListView.onForwardedEvent(android.view.MotionEvent, int):boolean");
    }

    public void setListSelectionHidden(boolean hideListSelection) {
        this.mListSelectionHidden = hideListSelection;
    }

    private void clearPressedItem() {
        this.mDrawsInPressedState = false;
        setPressed(false);
        updateSelectorState();
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
    }

    private void setPressedItem(View child, int position, float x, float y) {
        this.mDrawsInPressedState = true;
        drawableHotspotChanged(x, y);
        if (!isPressed()) {
            setPressed(true);
        }
        if (this.mDataChanged) {
            layoutChildren();
        }
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (!(motionView == null || motionView == child || !motionView.isPressed())) {
            motionView.setPressed(false);
        }
        this.mMotionPosition = position;
        child.drawableHotspotChanged(x - ((float) child.getLeft()), y - ((float) child.getTop()));
        if (!child.isPressed()) {
            child.setPressed(true);
        }
        setSelectedPositionInt(position);
        positionSelectorLikeTouch(position, child, x, y);
        refreshDrawableState();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean touchModeDrawsInPressedState() {
        return this.mDrawsInPressedState || super.touchModeDrawsInPressedState();
    }

    /* Access modifiers changed, original: 0000 */
    public View obtainView(int position, boolean[] isScrap) {
        View view = super.obtainView(position, isScrap);
        if (view instanceof TextView) {
            ((TextView) view).setHorizontallyScrolling(true);
        }
        return view;
    }

    public boolean isInTouchMode() {
        return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
    }

    public boolean hasWindowFocus() {
        return this.mHijackFocus || super.hasWindowFocus();
    }

    public boolean isFocused() {
        return this.mHijackFocus || super.isFocused();
    }

    public boolean hasFocus() {
        return this.mHijackFocus || super.hasFocus();
    }
}
