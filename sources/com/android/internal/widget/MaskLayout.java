package com.android.internal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.View;
import android.widget.FrameLayout;

public class MaskLayout extends FrameLayout {
    private boolean mDrawTouchMask;
    private int mMaskColor = 0;

    public MaskLayout(Context context) {
        super(context);
    }

    public MaskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] stateSet = getDrawableState();
        boolean drawMask = StateSet.stateSetMatches(PRESSED_STATE_SET, stateSet) || StateSet.stateSetMatches(FOCUSED_WINDOW_FOCUSED_STATE_SET, stateSet);
        if (this.mDrawTouchMask != drawMask) {
            this.mDrawTouchMask = drawMask;
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (!this.mDrawTouchMask) {
            return super.drawChild(canvas, child, drawingTime);
        }
        canvas.saveLayer((float) child.getLeft(), (float) child.getTop(), (float) child.getRight(), (float) child.getBottom(), null, 31);
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.drawColor(this.mMaskColor, Mode.SRC_ATOP);
        canvas.restore();
        return result;
    }

    public void setMaskColor(int maskColor) {
        this.mMaskColor = maskColor;
    }
}
