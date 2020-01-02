package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.Vibrator;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.android.internal.R;

public class SlidingTab extends ViewGroup {
    private static final int ANIM_DURATION = 250;
    private static final int ANIM_TARGET_TIME = 500;
    private static final boolean DBG = false;
    private static final int HORIZONTAL = 0;
    private static final String LOG_TAG = "SlidingTab";
    private static final float THRESHOLD = 0.6666667f;
    private static final int TRACKING_MARGIN = 50;
    private static final int VERTICAL = 1;
    private static final long VIBRATE_LONG = 40;
    private static final long VIBRATE_SHORT = 30;
    private static final AudioAttributes VIBRATION_ATTRIBUTES = new Builder().setContentType(4).setUsage(13).build();
    private boolean mAnimating;
    @UnsupportedAppUsage
    private final AnimationListener mAnimationDoneListener;
    private Slider mCurrentSlider;
    private final float mDensity;
    private int mGrabbedState;
    private boolean mHoldLeftOnTransition;
    private boolean mHoldRightOnTransition;
    @UnsupportedAppUsage
    private final Slider mLeftSlider;
    private OnTriggerListener mOnTriggerListener;
    private final int mOrientation;
    private Slider mOtherSlider;
    @UnsupportedAppUsage
    private final Slider mRightSlider;
    private float mThreshold;
    private final Rect mTmpRect;
    private boolean mTracking;
    private boolean mTriggered;
    private Vibrator mVibrator;

    public interface OnTriggerListener {
        public static final int LEFT_HANDLE = 1;
        public static final int NO_HANDLE = 0;
        public static final int RIGHT_HANDLE = 2;

        void onGrabbedStateChange(View view, int i);

        void onTrigger(View view, int i);
    }

    private static class Slider {
        public static final int ALIGN_BOTTOM = 3;
        public static final int ALIGN_LEFT = 0;
        public static final int ALIGN_RIGHT = 1;
        public static final int ALIGN_TOP = 2;
        public static final int ALIGN_UNKNOWN = 4;
        private static final int STATE_ACTIVE = 2;
        private static final int STATE_NORMAL = 0;
        private static final int STATE_PRESSED = 1;
        private int alignment = 4;
        private int alignment_value;
        private int currentState = 0;
        @UnsupportedAppUsage
        private final ImageView tab;
        private final ImageView target;
        @UnsupportedAppUsage
        private final TextView text;

        Slider(ViewGroup parent, int tabId, int barId, int targetId) {
            this.tab = new ImageView(parent.getContext());
            this.tab.setBackgroundResource(tabId);
            this.tab.setScaleType(ScaleType.CENTER);
            this.tab.setLayoutParams(new LayoutParams(-2, -2));
            this.text = new TextView(parent.getContext());
            this.text.setLayoutParams(new LayoutParams(-2, -1));
            this.text.setBackgroundResource(barId);
            this.text.setTextAppearance(parent.getContext(), R.style.TextAppearance_SlidingTabNormal);
            this.target = new ImageView(parent.getContext());
            this.target.setImageResource(targetId);
            this.target.setScaleType(ScaleType.CENTER);
            this.target.setLayoutParams(new LayoutParams(-2, -2));
            this.target.setVisibility(4);
            parent.addView(this.target);
            parent.addView(this.tab);
            parent.addView(this.text);
        }

        /* Access modifiers changed, original: 0000 */
        public void setIcon(int iconId) {
            this.tab.setImageResource(iconId);
        }

        /* Access modifiers changed, original: 0000 */
        public void setTabBackgroundResource(int tabId) {
            this.tab.setBackgroundResource(tabId);
        }

        /* Access modifiers changed, original: 0000 */
        public void setBarBackgroundResource(int barId) {
            this.text.setBackgroundResource(barId);
        }

        /* Access modifiers changed, original: 0000 */
        public void setHintText(int resId) {
            this.text.setText(resId);
        }

        /* Access modifiers changed, original: 0000 */
        public void hide() {
            int i = this.alignment;
            int dy = 0;
            boolean horiz = i == 0 || i == 1;
            int dx = horiz ? this.alignment == 0 ? this.alignment_value - this.tab.getRight() : this.alignment_value - this.tab.getLeft() : 0;
            if (!horiz) {
                if (this.alignment == 2) {
                    dy = this.alignment_value - this.tab.getBottom();
                } else {
                    dy = this.alignment_value - this.tab.getTop();
                }
            }
            Animation trans = new TranslateAnimation(0.0f, (float) dx, 0.0f, (float) dy);
            trans.setDuration(250);
            trans.setFillAfter(true);
            this.tab.startAnimation(trans);
            this.text.startAnimation(trans);
            this.target.setVisibility(4);
        }

        /* Access modifiers changed, original: 0000 */
        public void show(boolean animate) {
            int dy = 0;
            this.text.setVisibility(0);
            this.tab.setVisibility(0);
            if (animate) {
                int i = this.alignment;
                boolean z = true;
                if (!(i == 0 || i == 1)) {
                    z = false;
                }
                boolean horiz = z;
                int dx = horiz ? this.alignment == 0 ? this.tab.getWidth() : -this.tab.getWidth() : 0;
                if (!horiz) {
                    dy = this.alignment == 2 ? this.tab.getHeight() : -this.tab.getHeight();
                }
                Animation trans = new TranslateAnimation((float) (-dx), 0.0f, (float) (-dy), 0.0f);
                trans.setDuration(250);
                this.tab.startAnimation(trans);
                this.text.startAnimation(trans);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void setState(int state) {
            this.text.setPressed(state == 1);
            this.tab.setPressed(state == 1);
            if (state == 2) {
                int[] activeState = new int[]{16842914};
                if (this.text.getBackground().isStateful()) {
                    this.text.getBackground().setState(activeState);
                }
                if (this.tab.getBackground().isStateful()) {
                    this.tab.getBackground().setState(activeState);
                }
                TextView textView = this.text;
                textView.setTextAppearance(textView.getContext(), R.style.TextAppearance_SlidingTabActive);
            } else {
                TextView textView2 = this.text;
                textView2.setTextAppearance(textView2.getContext(), R.style.TextAppearance_SlidingTabNormal);
            }
            this.currentState = state;
        }

        /* Access modifiers changed, original: 0000 */
        public void showTarget() {
            AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
            alphaAnim.setDuration(500);
            this.target.startAnimation(alphaAnim);
            this.target.setVisibility(0);
        }

        /* Access modifiers changed, original: 0000 */
        public void reset(boolean animate) {
            setState(0);
            this.text.setVisibility(0);
            TextView textView = this.text;
            textView.setTextAppearance(textView.getContext(), R.style.TextAppearance_SlidingTabNormal);
            this.tab.setVisibility(0);
            this.target.setVisibility(4);
            int i = this.alignment;
            boolean z = true;
            if (!(i == 0 || i == 1)) {
                z = false;
            }
            boolean horiz = z;
            int dx = horiz ? this.alignment == 0 ? this.alignment_value - this.tab.getLeft() : this.alignment_value - this.tab.getRight() : 0;
            int dy = horiz ? 0 : this.alignment == 2 ? this.alignment_value - this.tab.getTop() : this.alignment_value - this.tab.getBottom();
            if (animate) {
                TranslateAnimation trans = new TranslateAnimation(0.0f, (float) dx, 0.0f, (float) dy);
                trans.setDuration(250);
                trans.setFillAfter(false);
                this.text.startAnimation(trans);
                this.tab.startAnimation(trans);
                return;
            }
            if (horiz) {
                this.text.offsetLeftAndRight(dx);
                this.tab.offsetLeftAndRight(dx);
            } else {
                this.text.offsetTopAndBottom(dy);
                this.tab.offsetTopAndBottom(dy);
            }
            this.text.clearAnimation();
            this.tab.clearAnimation();
            this.target.clearAnimation();
        }

        /* Access modifiers changed, original: 0000 */
        public void setTarget(int targetId) {
            this.target.setImageResource(targetId);
        }

        /* Access modifiers changed, original: 0000 */
        public void layout(int l, int t, int r, int b, int alignment) {
            int handleWidth;
            int targetWidth;
            int parentWidth;
            int leftTarget;
            int rightTarget;
            int i = l;
            int i2 = t;
            int i3 = r;
            int i4 = b;
            int i5 = alignment;
            this.alignment = i5;
            Drawable tabBackground = this.tab.getBackground();
            int handleWidth2 = tabBackground.getIntrinsicWidth();
            int handleHeight = tabBackground.getIntrinsicHeight();
            Drawable targetDrawable = this.target.getDrawable();
            int targetWidth2 = targetDrawable.getIntrinsicWidth();
            int targetHeight = targetDrawable.getIntrinsicHeight();
            int parentWidth2 = i3 - i;
            int parentHeight = i4 - i2;
            int leftTarget2 = (((int) (((float) parentWidth2) * SlidingTab.THRESHOLD)) - targetWidth2) + (handleWidth2 / 2);
            int rightTarget2 = ((int) (((float) parentWidth2) * 0.3333333f)) - (handleWidth2 / 2);
            int left = (parentWidth2 - handleWidth2) / 2;
            int right = left + handleWidth2;
            if (i5 == 0) {
                handleWidth = handleWidth2;
                targetWidth = targetWidth2;
                parentWidth = parentWidth2;
                leftTarget = leftTarget2;
                rightTarget = rightTarget2;
            } else if (i5 == 1) {
                handleWidth = handleWidth2;
                targetWidth = targetWidth2;
                parentWidth = parentWidth2;
                leftTarget = leftTarget2;
                rightTarget = rightTarget2;
            } else {
                i3 = (parentWidth2 - targetWidth2) / 2;
                rightTarget = rightTarget2;
                rightTarget2 = (parentWidth2 + targetWidth2) / 2;
                i = (((int) (((float) parentHeight) * SlidingTab.THRESHOLD)) + (handleHeight / 2)) - targetHeight;
                targetWidth2 = ((int) (((float) parentHeight) * 0.3333333f)) - (handleHeight / 2);
                leftTarget = leftTarget2;
                if (i5 == 2) {
                    this.tab.layout(left, 0, right, handleHeight);
                    handleWidth = handleWidth2;
                    this.text.layout(left, 0 - parentHeight, right, 0);
                    this.target.layout(i3, i, rightTarget2, i + targetHeight);
                    this.alignment_value = i2;
                    i2 = r;
                    rightTarget2 = leftTarget;
                    i4 = rightTarget;
                    leftTarget2 = handleWidth;
                    return;
                }
                handleWidth = handleWidth2;
                parentWidth = parentWidth2;
                this.tab.layout(left, parentHeight - handleHeight, right, parentHeight);
                this.text.layout(left, parentHeight, right, parentHeight + parentHeight);
                this.target.layout(i3, targetWidth2, rightTarget2, targetWidth2 + targetHeight);
                this.alignment_value = i4;
                i2 = r;
                rightTarget2 = leftTarget;
                i4 = rightTarget;
                leftTarget2 = handleWidth;
                return;
            }
            i = (parentHeight - targetHeight) / 2;
            handleWidth2 = i + targetHeight;
            targetWidth2 = (parentHeight - handleHeight) / 2;
            parentWidth2 = (parentHeight + handleHeight) / 2;
            if (i5 == 0) {
                this.tab.layout(0, targetWidth2, handleWidth, parentWidth2);
                this.text.layout(0 - parentWidth, targetWidth2, 0, parentWidth2);
                this.text.setGravity(5);
                this.target.layout(leftTarget, i, leftTarget + targetWidth, handleWidth2);
                this.alignment_value = l;
                i2 = r;
                i4 = rightTarget;
                return;
            }
            i2 = l;
            rightTarget2 = leftTarget;
            i4 = parentWidth;
            this.tab.layout(parentWidth - handleWidth, targetWidth2, i4, parentWidth2);
            this.text.layout(i4, targetWidth2, i4 + i4, parentWidth2);
            this.target.layout(rightTarget, i, rightTarget + targetWidth, handleWidth2);
            this.text.setGravity(48);
            this.alignment_value = r;
        }

        public void updateDrawableStates() {
            setState(this.currentState);
        }

        public void measure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            this.tab.measure(MeasureSpec.makeSafeMeasureSpec(width, 0), MeasureSpec.makeSafeMeasureSpec(height, 0));
            this.text.measure(MeasureSpec.makeSafeMeasureSpec(width, 0), MeasureSpec.makeSafeMeasureSpec(height, 0));
        }

        public int getTabWidth() {
            return this.tab.getMeasuredWidth();
        }

        public int getTabHeight() {
            return this.tab.getMeasuredHeight();
        }

        public void startAnimation(Animation anim1, Animation anim2) {
            this.tab.startAnimation(anim1);
            this.text.startAnimation(anim2);
        }

        public void hideTarget() {
            this.target.clearAnimation();
            this.target.setVisibility(4);
        }
    }

    public SlidingTab(Context context) {
        this(context, null);
    }

    public SlidingTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHoldLeftOnTransition = true;
        this.mHoldRightOnTransition = true;
        this.mGrabbedState = 0;
        this.mTriggered = false;
        this.mAnimationDoneListener = new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SlidingTab.this.onAnimationDone();
            }
        };
        this.mTmpRect = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingTab);
        this.mOrientation = a.getInt(0, 0);
        a.recycle();
        this.mDensity = getResources().getDisplayMetrics().density;
        this.mLeftSlider = new Slider(this, R.drawable.jog_tab_left_generic, R.drawable.jog_tab_bar_left_generic, R.drawable.jog_tab_target_gray);
        this.mRightSlider = new Slider(this, R.drawable.jog_tab_right_generic, R.drawable.jog_tab_bar_right_generic, R.drawable.jog_tab_target_gray);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        this.mLeftSlider.measure(widthMeasureSpec, heightMeasureSpec);
        this.mRightSlider.measure(widthMeasureSpec, heightMeasureSpec);
        int leftTabWidth = this.mLeftSlider.getTabWidth();
        int rightTabWidth = this.mRightSlider.getTabWidth();
        int leftTabHeight = this.mLeftSlider.getTabHeight();
        int rightTabHeight = this.mRightSlider.getTabHeight();
        if (isHorizontal()) {
            width = Math.max(widthSpecSize, leftTabWidth + rightTabWidth);
            height = Math.max(leftTabHeight, rightTabHeight);
        } else {
            width = Math.max(leftTabWidth, rightTabHeight);
            height = Math.max(heightSpecSize, leftTabHeight + rightTabHeight);
        }
        setMeasuredDimension(width, height);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        if (this.mAnimating) {
            return false;
        }
        this.mLeftSlider.tab.getHitRect(this.mTmpRect);
        boolean leftHit = this.mTmpRect.contains((int) x, (int) y);
        this.mRightSlider.tab.getHitRect(this.mTmpRect);
        boolean rightHit = this.mTmpRect.contains((int) x, (int) y);
        if (!this.mTracking && !leftHit && !rightHit) {
            return false;
        }
        if (action == 0) {
            this.mTracking = true;
            this.mTriggered = false;
            vibrate(VIBRATE_SHORT);
            float f = 0.3333333f;
            if (leftHit) {
                this.mCurrentSlider = this.mLeftSlider;
                this.mOtherSlider = this.mRightSlider;
                if (isHorizontal()) {
                    f = THRESHOLD;
                }
                this.mThreshold = f;
                setGrabbedState(1);
            } else {
                this.mCurrentSlider = this.mRightSlider;
                this.mOtherSlider = this.mLeftSlider;
                if (!isHorizontal()) {
                    f = THRESHOLD;
                }
                this.mThreshold = f;
                setGrabbedState(2);
            }
            this.mCurrentSlider.setState(1);
            this.mCurrentSlider.showTarget();
            this.mOtherSlider.hide();
        }
        return true;
    }

    public void reset(boolean animate) {
        this.mLeftSlider.reset(animate);
        this.mRightSlider.reset(animate);
        if (!animate) {
            this.mAnimating = false;
        }
    }

    public void setVisibility(int visibility) {
        if (visibility != getVisibility() && visibility == 4) {
            reset(false);
        }
        super.setVisibility(visibility);
    }

    /* JADX WARNING: Missing block: B:7:0x0018, code skipped:
            if (r0 != 3) goto L_0x009d;
     */
    public boolean onTouchEvent(android.view.MotionEvent r12) {
        /*
        r11 = this;
        r0 = r11.mTracking;
        r1 = 0;
        r2 = 1;
        if (r0 == 0) goto L_0x009d;
    L_0x0006:
        r0 = r12.getAction();
        r3 = r12.getX();
        r4 = r12.getY();
        if (r0 == r2) goto L_0x009a;
    L_0x0014:
        r5 = 2;
        if (r0 == r5) goto L_0x001c;
    L_0x0017:
        r5 = 3;
        if (r0 == r5) goto L_0x009a;
    L_0x001a:
        goto L_0x009d;
    L_0x001c:
        r6 = r11.withinView(r3, r4, r11);
        if (r6 == 0) goto L_0x009a;
    L_0x0022:
        r11.moveHandle(r3, r4);
        r6 = r11.isHorizontal();
        if (r6 == 0) goto L_0x002d;
    L_0x002b:
        r6 = r3;
        goto L_0x002e;
    L_0x002d:
        r6 = r4;
    L_0x002e:
        r7 = r11.mThreshold;
        r8 = r11.isHorizontal();
        if (r8 == 0) goto L_0x003b;
    L_0x0036:
        r8 = r11.getWidth();
        goto L_0x003f;
    L_0x003b:
        r8 = r11.getHeight();
    L_0x003f:
        r8 = (float) r8;
        r7 = r7 * r8;
        r8 = r11.isHorizontal();
        if (r8 == 0) goto L_0x005a;
    L_0x0047:
        r8 = r11.mCurrentSlider;
        r9 = r11.mLeftSlider;
        if (r8 != r9) goto L_0x0052;
    L_0x004d:
        r8 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r8 <= 0) goto L_0x0058;
    L_0x0051:
        goto L_0x0056;
    L_0x0052:
        r8 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r8 >= 0) goto L_0x0058;
    L_0x0056:
        r8 = r2;
        goto L_0x0059;
    L_0x0058:
        r8 = r1;
    L_0x0059:
        goto L_0x006c;
    L_0x005a:
        r8 = r11.mCurrentSlider;
        r9 = r11.mLeftSlider;
        if (r8 != r9) goto L_0x0065;
    L_0x0060:
        r8 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r8 >= 0) goto L_0x006b;
    L_0x0064:
        goto L_0x0069;
    L_0x0065:
        r8 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r8 <= 0) goto L_0x006b;
    L_0x0069:
        r8 = r2;
        goto L_0x006c;
    L_0x006b:
        r8 = r1;
    L_0x006c:
        r9 = r11.mTriggered;
        if (r9 != 0) goto L_0x009d;
    L_0x0070:
        if (r8 == 0) goto L_0x009d;
    L_0x0072:
        r11.mTriggered = r2;
        r11.mTracking = r1;
        r9 = r11.mCurrentSlider;
        r9.setState(r5);
        r9 = r11.mCurrentSlider;
        r10 = r11.mLeftSlider;
        if (r9 != r10) goto L_0x0083;
    L_0x0081:
        r9 = r2;
        goto L_0x0084;
    L_0x0083:
        r9 = r1;
    L_0x0084:
        if (r9 == 0) goto L_0x0088;
    L_0x0086:
        r5 = r2;
        goto L_0x0089;
    L_0x0089:
        r11.dispatchTriggerEvent(r5);
        if (r9 == 0) goto L_0x0091;
    L_0x008e:
        r5 = r11.mHoldLeftOnTransition;
        goto L_0x0093;
    L_0x0091:
        r5 = r11.mHoldRightOnTransition;
    L_0x0093:
        r11.startAnimating(r5);
        r11.setGrabbedState(r1);
        goto L_0x009d;
    L_0x009a:
        r11.cancelGrab();
    L_0x009d:
        r0 = r11.mTracking;
        if (r0 != 0) goto L_0x00a7;
    L_0x00a1:
        r0 = super.onTouchEvent(r12);
        if (r0 == 0) goto L_0x00a8;
    L_0x00a7:
        r1 = r2;
    L_0x00a8:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.SlidingTab.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void cancelGrab() {
        this.mTracking = false;
        this.mTriggered = false;
        this.mOtherSlider.show(true);
        this.mCurrentSlider.reset(false);
        this.mCurrentSlider.hideTarget();
        this.mCurrentSlider = null;
        this.mOtherSlider = null;
        setGrabbedState(0);
    }

    /* Access modifiers changed, original: 0000 */
    public void startAnimating(final boolean holdAfter) {
        int right;
        int dx;
        this.mAnimating = true;
        Slider slider = this.mCurrentSlider;
        Slider other = this.mOtherSlider;
        int holdOffset = 0;
        int width;
        int left;
        int viewWidth;
        if (isHorizontal()) {
            right = slider.tab.getRight();
            width = slider.tab.getWidth();
            left = slider.tab.getLeft();
            viewWidth = getWidth();
            if (!holdAfter) {
                holdOffset = width;
            }
            if (slider == this.mRightSlider) {
                dx = -((right + viewWidth) - holdOffset);
            } else {
                dx = ((viewWidth - left) + viewWidth) - holdOffset;
            }
            right = 0;
        } else {
            int i;
            right = slider.tab.getTop();
            width = slider.tab.getBottom();
            left = slider.tab.getHeight();
            viewWidth = getHeight();
            if (!holdAfter) {
                holdOffset = left;
            }
            dx = 0;
            if (slider == this.mRightSlider) {
                i = (right + viewWidth) - holdOffset;
            } else {
                i = -(((viewWidth - width) + viewWidth) - holdOffset);
            }
            right = i;
        }
        Animation trans1 = new TranslateAnimation(0.0f, (float) dx, 0.0f, (float) right);
        trans1.setDuration(250);
        trans1.setInterpolator(new LinearInterpolator());
        trans1.setFillAfter(true);
        Animation trans2 = new TranslateAnimation(0.0f, (float) dx, 0.0f, (float) right);
        trans2.setDuration(250);
        trans2.setInterpolator(new LinearInterpolator());
        trans2.setFillAfter(true);
        trans1.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                Animation anim;
                if (holdAfter) {
                    int i = dx;
                    float f = (float) i;
                    float f2 = (float) i;
                    int i2 = right;
                    anim = new TranslateAnimation(f, f2, (float) i2, (float) i2);
                    anim.setDuration(1000);
                    SlidingTab.this.mAnimating = false;
                } else {
                    anim = new AlphaAnimation(0.5f, 1.0f);
                    anim.setDuration(250);
                    SlidingTab.this.resetView();
                }
                anim.setAnimationListener(SlidingTab.this.mAnimationDoneListener);
                SlidingTab.this.mLeftSlider.startAnimation(anim, anim);
                SlidingTab.this.mRightSlider.startAnimation(anim, anim);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        slider.hideTarget();
        slider.startAnimation(trans1, trans2);
    }

    @UnsupportedAppUsage
    private void onAnimationDone() {
        resetView();
        this.mAnimating = false;
    }

    private boolean withinView(float x, float y, View view) {
        return (isHorizontal() && y > -50.0f && y < ((float) (view.getHeight() + 50))) || (!isHorizontal() && x > -50.0f && x < ((float) (view.getWidth() + 50)));
    }

    private boolean isHorizontal() {
        return this.mOrientation == 0;
    }

    @UnsupportedAppUsage
    private void resetView() {
        this.mLeftSlider.reset(false);
        this.mRightSlider.reset(false);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            this.mLeftSlider.layout(l, t, r, b, isHorizontal() ? 0 : 3);
            this.mRightSlider.layout(l, t, r, b, isHorizontal() ? 1 : 2);
        }
    }

    private void moveHandle(float x, float y) {
        View handle = this.mCurrentSlider.tab;
        View content = this.mCurrentSlider.text;
        int deltaX;
        if (isHorizontal()) {
            deltaX = (((int) x) - handle.getLeft()) - (handle.getWidth() / 2);
            handle.offsetLeftAndRight(deltaX);
            content.offsetLeftAndRight(deltaX);
        } else {
            deltaX = (((int) y) - handle.getTop()) - (handle.getHeight() / 2);
            handle.offsetTopAndBottom(deltaX);
            content.offsetTopAndBottom(deltaX);
        }
        invalidate();
    }

    @UnsupportedAppUsage
    public void setLeftTabResources(int iconId, int targetId, int barId, int tabId) {
        this.mLeftSlider.setIcon(iconId);
        this.mLeftSlider.setTarget(targetId);
        this.mLeftSlider.setBarBackgroundResource(barId);
        this.mLeftSlider.setTabBackgroundResource(tabId);
        this.mLeftSlider.updateDrawableStates();
    }

    @UnsupportedAppUsage
    public void setLeftHintText(int resId) {
        if (isHorizontal()) {
            this.mLeftSlider.setHintText(resId);
        }
    }

    @UnsupportedAppUsage
    public void setRightTabResources(int iconId, int targetId, int barId, int tabId) {
        this.mRightSlider.setIcon(iconId);
        this.mRightSlider.setTarget(targetId);
        this.mRightSlider.setBarBackgroundResource(barId);
        this.mRightSlider.setTabBackgroundResource(tabId);
        this.mRightSlider.updateDrawableStates();
    }

    @UnsupportedAppUsage
    public void setRightHintText(int resId) {
        if (isHorizontal()) {
            this.mRightSlider.setHintText(resId);
        }
    }

    @UnsupportedAppUsage
    public void setHoldAfterTrigger(boolean holdLeft, boolean holdRight) {
        this.mHoldLeftOnTransition = holdLeft;
        this.mHoldRightOnTransition = holdRight;
    }

    private synchronized void vibrate(long duration) {
        boolean z = true;
        if (System.getIntForUser(this.mContext.getContentResolver(), System.HAPTIC_FEEDBACK_ENABLED, 1, -2) == 0) {
            z = false;
        }
        if (z) {
            if (this.mVibrator == null) {
                this.mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            this.mVibrator.vibrate(duration, VIBRATION_ATTRIBUTES);
        }
    }

    @UnsupportedAppUsage
    public void setOnTriggerListener(OnTriggerListener listener) {
        this.mOnTriggerListener = listener;
    }

    private void dispatchTriggerEvent(int whichHandle) {
        vibrate(VIBRATE_LONG);
        OnTriggerListener onTriggerListener = this.mOnTriggerListener;
        if (onTriggerListener != null) {
            onTriggerListener.onTrigger(this, whichHandle);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this && visibility != 0 && this.mGrabbedState != 0) {
            cancelGrab();
        }
    }

    private void setGrabbedState(int newState) {
        if (newState != this.mGrabbedState) {
            this.mGrabbedState = newState;
            OnTriggerListener onTriggerListener = this.mOnTriggerListener;
            if (onTriggerListener != null) {
                onTriggerListener.onGrabbedStateChange(this, this.mGrabbedState);
            }
        }
    }

    private void log(String msg) {
        Log.d(LOG_TAG, msg);
    }
}
