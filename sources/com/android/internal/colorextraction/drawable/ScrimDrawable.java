package com.android.internal.colorextraction.drawable;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;

public class ScrimDrawable extends Drawable {
    private static final long COLOR_ANIMATION_DURATION = 2000;
    private static final String TAG = "ScrimDrawable";
    private int mAlpha = 255;
    private ValueAnimator mColorAnimation;
    private int mMainColor;
    private int mMainColorTo;
    private final Paint mPaint = new Paint();

    public ScrimDrawable() {
        this.mPaint.setStyle(Style.FILL);
    }

    /* JADX WARNING: Incorrect type for fill-array insn 0x001d, element type: float, insn element type: null */
    public void setColor(int r5, boolean r6) {
        /*
        r4 = this;
        r0 = r4.mMainColorTo;
        if (r5 != r0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r4.mColorAnimation;
        if (r0 == 0) goto L_0x0014;
    L_0x0009:
        r0 = r0.isRunning();
        if (r0 == 0) goto L_0x0014;
    L_0x000f:
        r0 = r4.mColorAnimation;
        r0.cancel();
    L_0x0014:
        r4.mMainColorTo = r5;
        if (r6 == 0) goto L_0x0047;
    L_0x0018:
        r0 = r4.mMainColor;
        r1 = 2;
        r1 = new float[r1];
        r1 = {0, 1065353216};
        r1 = android.animation.ValueAnimator.ofFloat(r1);
        r2 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r1.setDuration(r2);
        r2 = new com.android.internal.colorextraction.drawable.-$$Lambda$ScrimDrawable$UWtyAZ9Ss5P5TukFNvAyvh0pNf0;
        r2.<init>(r4, r0, r5);
        r1.addUpdateListener(r2);
        r2 = new com.android.internal.colorextraction.drawable.ScrimDrawable$1;
        r2.<init>();
        r1.addListener(r2);
        r2 = new android.view.animation.DecelerateInterpolator;
        r2.<init>();
        r1.setInterpolator(r2);
        r1.start();
        r4.mColorAnimation = r1;
        goto L_0x004c;
    L_0x0047:
        r4.mMainColor = r5;
        r4.invalidateSelf();
    L_0x004c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.colorextraction.drawable.ScrimDrawable.setColor(int, boolean):void");
    }

    public /* synthetic */ void lambda$setColor$0$ScrimDrawable(int mainFrom, int mainColor, ValueAnimator animation) {
        this.mMainColor = ColorUtils.blendARGB(mainFrom, mainColor, ((Float) animation.getAnimatedValue()).floatValue());
        invalidateSelf();
    }

    public void setAlpha(int alpha) {
        if (alpha != this.mAlpha) {
            this.mAlpha = alpha;
            invalidateSelf();
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setXfermode(Xfermode mode) {
        this.mPaint.setXfermode(mode);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public int getOpacity() {
        return -3;
    }

    public void draw(Canvas canvas) {
        this.mPaint.setColor(this.mMainColor);
        this.mPaint.setAlpha(this.mAlpha);
        canvas.drawRect(getBounds(), this.mPaint);
    }

    @VisibleForTesting
    public int getMainColor() {
        return this.mMainColor;
    }
}
