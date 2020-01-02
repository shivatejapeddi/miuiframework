package com.android.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewOutlineProvider;
import android.view.Window.WindowControllerCallback;
import com.android.internal.R;
import com.android.internal.policy.PhoneWindow;
import java.util.ArrayList;

public class DecorCaptionView extends ViewGroup implements OnTouchListener, OnGestureListener {
    private static final String TAG = "DecorCaptionView";
    private View mCaption;
    private boolean mCheckForDragging;
    private View mClickTarget;
    private View mClose;
    private final Rect mCloseRect = new Rect();
    private View mContent;
    private int mDragSlop;
    private boolean mDragging = false;
    private GestureDetector mGestureDetector;
    private View mMaximize;
    private final Rect mMaximizeRect = new Rect();
    private boolean mOverlayWithAppContent = false;
    private PhoneWindow mOwner = null;
    private int mRootScrollY;
    private boolean mShow = false;
    private ArrayList<View> mTouchDispatchList = new ArrayList(2);
    private int mTouchDownX;
    private int mTouchDownY;

    public DecorCaptionView(Context context) {
        super(context);
        init(context);
    }

    public DecorCaptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DecorCaptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mDragSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mGestureDetector = new GestureDetector(context, (OnGestureListener) this);
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mCaption = getChildAt(0);
    }

    public void setPhoneWindow(PhoneWindow owner, boolean show) {
        this.mOwner = owner;
        this.mShow = show;
        this.mOverlayWithAppContent = owner.isOverlayWithDecorCaptionEnabled();
        if (this.mOverlayWithAppContent) {
            this.mCaption.setBackgroundColor(0);
        }
        updateCaptionVisibility();
        this.mOwner.getDecorView().setOutlineProvider(ViewOutlineProvider.BOUNDS);
        this.mMaximize = findViewById(R.id.maximize_window);
        this.mClose = findViewById(R.id.close_window);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (this.mMaximizeRect.contains(x, y - this.mRootScrollY)) {
                this.mClickTarget = this.mMaximize;
            }
            if (this.mCloseRect.contains(x, y - this.mRootScrollY)) {
                this.mClickTarget = this.mClose;
            }
        }
        return this.mClickTarget != null;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mClickTarget == null) {
            return false;
        }
        this.mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        if (action == 1 || action == 3) {
            this.mClickTarget = null;
        }
        return true;
    }

    /* JADX WARNING: Missing block: B:13:0x002f, code skipped:
            if (r7 != 3) goto L_0x0071;
     */
    public boolean onTouch(android.view.View r10, android.view.MotionEvent r11) {
        /*
        r9 = this;
        r0 = r11.getX();
        r0 = (int) r0;
        r1 = r11.getY();
        r1 = (int) r1;
        r2 = r11.getActionIndex();
        r2 = r11.getToolType(r2);
        r3 = 3;
        r4 = 0;
        r5 = 1;
        if (r2 != r3) goto L_0x0019;
    L_0x0017:
        r2 = r5;
        goto L_0x001a;
    L_0x0019:
        r2 = r4;
    L_0x001a:
        r6 = r11.getButtonState();
        r6 = r6 & r5;
        if (r6 == 0) goto L_0x0023;
    L_0x0021:
        r6 = r5;
        goto L_0x0024;
    L_0x0023:
        r6 = r4;
    L_0x0024:
        r7 = r11.getActionMasked();
        if (r7 == 0) goto L_0x0062;
    L_0x002a:
        if (r7 == r5) goto L_0x0052;
    L_0x002c:
        r8 = 2;
        if (r7 == r8) goto L_0x0032;
    L_0x002f:
        if (r7 == r3) goto L_0x0052;
    L_0x0031:
        goto L_0x0071;
    L_0x0032:
        r3 = r9.mDragging;
        if (r3 != 0) goto L_0x0071;
    L_0x0036:
        r3 = r9.mCheckForDragging;
        if (r3 == 0) goto L_0x0071;
    L_0x003a:
        if (r2 != 0) goto L_0x0042;
    L_0x003c:
        r3 = r9.passedSlop(r0, r1);
        if (r3 == 0) goto L_0x0071;
    L_0x0042:
        r9.mCheckForDragging = r4;
        r9.mDragging = r5;
        r3 = r11.getRawX();
        r8 = r11.getRawY();
        r9.startMovingTask(r3, r8);
        goto L_0x0071;
    L_0x0052:
        r3 = r9.mDragging;
        if (r3 != 0) goto L_0x0057;
    L_0x0056:
        goto L_0x0071;
    L_0x0057:
        if (r7 != r5) goto L_0x005c;
    L_0x0059:
        r9.finishMovingTask();
    L_0x005c:
        r9.mDragging = r4;
        r3 = r9.mCheckForDragging;
        r3 = r3 ^ r5;
        return r3;
    L_0x0062:
        r3 = r9.mShow;
        if (r3 != 0) goto L_0x0067;
    L_0x0066:
        return r4;
    L_0x0067:
        if (r2 == 0) goto L_0x006b;
    L_0x0069:
        if (r6 == 0) goto L_0x0071;
    L_0x006b:
        r9.mCheckForDragging = r5;
        r9.mTouchDownX = r0;
        r9.mTouchDownY = r1;
    L_0x0071:
        r3 = r9.mDragging;
        if (r3 != 0) goto L_0x0079;
    L_0x0075:
        r3 = r9.mCheckForDragging;
        if (r3 == 0) goto L_0x007a;
    L_0x0079:
        r4 = r5;
    L_0x007a:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.DecorCaptionView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public ArrayList<View> buildTouchDispatchChildList() {
        this.mTouchDispatchList.ensureCapacity(3);
        View view = this.mCaption;
        if (view != null) {
            this.mTouchDispatchList.add(view);
        }
        view = this.mContent;
        if (view != null) {
            this.mTouchDispatchList.add(view);
        }
        return this.mTouchDispatchList;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    private boolean passedSlop(int x, int y) {
        return Math.abs(x - this.mTouchDownX) > this.mDragSlop || Math.abs(y - this.mTouchDownY) > this.mDragSlop;
    }

    public void onConfigurationChanged(boolean show) {
        this.mShow = show;
        updateCaptionVisibility();
    }

    public void addView(View child, int index, LayoutParams params) {
        if (!(params instanceof MarginLayoutParams)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("params ");
            stringBuilder.append(params);
            stringBuilder.append(" must subclass MarginLayoutParams");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (index >= 2 || getChildCount() >= 2) {
            throw new IllegalStateException("DecorCaptionView can only handle 1 client view");
        } else {
            super.addView(child, 0, params);
            this.mContent = child;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int captionHeight;
        if (this.mCaption.getVisibility() != 8) {
            measureChildWithMargins(this.mCaption, widthMeasureSpec, 0, heightMeasureSpec, 0);
            captionHeight = this.mCaption.getMeasuredHeight();
        } else {
            captionHeight = 0;
        }
        View view = this.mContent;
        if (view != null) {
            if (this.mOverlayWithAppContent) {
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0);
            } else {
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, captionHeight);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int captionHeight;
        if (this.mCaption.getVisibility() != 8) {
            View view = this.mCaption;
            view.layout(0, 0, view.getMeasuredWidth(), this.mCaption.getMeasuredHeight());
            captionHeight = this.mCaption.getBottom() - this.mCaption.getTop();
            this.mMaximize.getHitRect(this.mMaximizeRect);
            this.mClose.getHitRect(this.mCloseRect);
        } else {
            captionHeight = 0;
            this.mMaximizeRect.setEmpty();
            this.mCloseRect.setEmpty();
        }
        View view2 = this.mContent;
        if (view2 != null) {
            if (this.mOverlayWithAppContent) {
                view2.layout(0, 0, view2.getMeasuredWidth(), this.mContent.getMeasuredHeight());
            } else {
                view2.layout(0, captionHeight, view2.getMeasuredWidth(), this.mContent.getMeasuredHeight() + captionHeight);
            }
        }
        this.mOwner.notifyRestrictedCaptionAreaCallback(this.mMaximize.getLeft(), this.mMaximize.getTop(), this.mClose.getRight(), this.mClose.getBottom());
    }

    private void updateCaptionVisibility() {
        this.mCaption.setVisibility(this.mShow ? 0 : 8);
        this.mCaption.setOnTouchListener(this);
    }

    private void toggleFreeformWindowingMode() {
        WindowControllerCallback callback = this.mOwner.getWindowControllerCallback();
        if (callback != null) {
            try {
                callback.toggleFreeformWindowingMode();
            } catch (RemoteException e) {
                Log.e(TAG, "Cannot change task workspace.");
            }
        }
    }

    public boolean isCaptionShowing() {
        return this.mShow;
    }

    public int getCaptionHeight() {
        View view = this.mCaption;
        return view != null ? view.getHeight() : 0;
    }

    public void removeContentView() {
        View view = this.mContent;
        if (view != null) {
            removeView(view);
            this.mContent = null;
        }
    }

    public View getCaption() {
        return this.mCaption;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -1);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        View view = this.mClickTarget;
        if (view == this.mMaximize) {
            toggleFreeformWindowingMode();
        } else if (view == this.mClose) {
            this.mOwner.dispatchOnWindowDismissed(true, false);
        }
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void onRootViewScrollYChanged(int scrollY) {
        View view = this.mCaption;
        if (view != null) {
            this.mRootScrollY = scrollY;
            view.setTranslationY((float) scrollY);
        }
    }
}
