package miui.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import miui.graphics.BitmapFactory;

public class ScreenshotDrawable extends Drawable implements AnimatorUpdateListener {
    private static final String TAG = "ScreenshotDrawable";
    private static boolean isdDisplayOled = "oled".equals(SystemProperties.get("ro.display.type"));
    private static boolean sHasRealBlur = SystemProperties.getBoolean("ro.miui.has_real_blur", false);
    private static int[] sTempLoc = new int[2];
    private int mBgColor;
    private Bitmap mBluredBitmap;
    private Drawable mOriginalDrawable;
    private View mOwnerView;
    private Paint mPaint = new Paint(3);
    private Rect mSrcRect = new Rect();
    private ValueAnimator mVisibilityChangeAnimator;

    public ScreenshotDrawable(View ownerView) {
        this.mOwnerView = ownerView;
        this.mBgColor = ownerView.getResources().getColor(isdDisplayOled ? R.color.realtimeblur_bg_oled : R.color.realtimeblur_bg);
    }

    public static void processBlurBehindFlag(View view, LayoutParams params, boolean isUpdate) {
        if ((((WindowManager.LayoutParams) params).flags & 4) != 0) {
            if (!(view.getBackground() instanceof ScreenshotDrawable)) {
                ScreenshotDrawable screenshotDrawable = new ScreenshotDrawable(view);
                screenshotDrawable.setOriginalDrawable(view.getBackground());
                view.setBackground(screenshotDrawable);
            }
        } else if (isUpdate && (view.getBackground() instanceof ScreenshotDrawable)) {
            ((ScreenshotDrawable) view.getBackground()).startVisibilityAnimator(false);
        }
    }

    private void rebuildBluredBitmap() {
        if (!sHasRealBlur) {
            try {
                this.mBluredBitmap = BitmapFactory.fastBlur(ScreenshotUtils.getScreenshot(this.mOwnerView.getContext(), 1.0f / ((float) ScreenshotUtils.REAL_BLUR_MINIFY), 0, 0, true), this.mBluredBitmap, (int) (((float) ScreenshotUtils.REAL_BLUR_RADIUS) * Resources.getSystem().getDisplayMetrics().density));
            } catch (Throwable ex) {
                Log.e(TAG, "Screenshot and fastblur failed.", ex);
            }
        }
    }

    public void setOriginalDrawable(Drawable originalDrawable) {
        this.mOriginalDrawable = originalDrawable;
    }

    public Drawable getOriginalDrawable() {
        return this.mOriginalDrawable;
    }

    public void draw(Canvas canvas) {
        if (this.mBluredBitmap != null) {
            if (this.mSrcRect.isEmpty()) {
                this.mOwnerView.getLocationOnScreen(sTempLoc);
                int left = sTempLoc[0] / ScreenshotUtils.REAL_BLUR_MINIFY;
                int top = sTempLoc[1] / ScreenshotUtils.REAL_BLUR_MINIFY;
                this.mSrcRect.set(left, top, left + (getBounds().width() / ScreenshotUtils.REAL_BLUR_MINIFY), top + (getBounds().height() / ScreenshotUtils.REAL_BLUR_MINIFY));
            }
            canvas.drawBitmap(this.mBluredBitmap, this.mSrcRect, getBounds(), this.mPaint);
        }
        canvas.drawColor(mixColor(this.mBgColor, this.mPaint.getAlpha()));
        Drawable drawable = this.mOriginalDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    private int mixColor(int color, int alpha) {
        return (((Color.alpha(color) * alpha) / 255) << 24) | (16777215 & color);
    }

    public boolean setVisible(boolean visible, boolean restart) {
        if (visible) {
            if (this.mOwnerView.getRootView().getLayoutParams() instanceof WindowManager.LayoutParams) {
                processShow();
            } else {
                this.mOwnerView.getRootView().addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                    public void onViewDetachedFromWindow(View v) {
                    }

                    public void onViewAttachedToWindow(View v) {
                        ScreenshotDrawable.this.processShow();
                        ScreenshotDrawable.this.mOwnerView.getRootView().removeOnAttachStateChangeListener(this);
                    }
                });
            }
            this.mSrcRect.setEmpty();
            rebuildBluredBitmap();
        }
        return super.setVisible(visible, restart);
    }

    /* Access modifiers changed, original: 0000 */
    public void processShow() {
        boolean hasWinEnterAnimator = false;
        if (this.mOwnerView.getRootView().getLayoutParams() instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) this.mOwnerView.getRootView().getLayoutParams();
            if (lp.windowAnimations != 0) {
                TypedArray attrs = this.mOwnerView.getContext().obtainStyledAttributes(lp.windowAnimations, com.android.internal.R.styleable.WindowAnimation);
                int anim = attrs.getResourceId(0, 0);
                attrs.recycle();
                if (anim != 0) {
                    hasWinEnterAnimator = true;
                }
            }
        }
        if (hasWinEnterAnimator) {
            ValueAnimator valueAnimator = this.mVisibilityChangeAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mVisibilityChangeAnimator.end();
            }
            setAlpha(255);
            return;
        }
        startVisibilityAnimator(true);
    }

    private void startVisibilityAnimator(boolean visible) {
        ValueAnimator valueAnimator = this.mVisibilityChangeAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mVisibilityChangeAnimator.end();
        }
        if (visible) {
            this.mVisibilityChangeAnimator = ValueAnimator.ofInt(0, 255);
            setAlpha(0);
        } else {
            this.mVisibilityChangeAnimator = ValueAnimator.ofInt(this.mPaint.getAlpha(), 0);
            this.mVisibilityChangeAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (ScreenshotDrawable.this.mBluredBitmap != null) {
                        ScreenshotDrawable.this.mBluredBitmap.recycle();
                        ScreenshotDrawable.this.mBluredBitmap = null;
                    }
                    if (ScreenshotDrawable.this.getCallback() instanceof View) {
                        ((View) ScreenshotDrawable.this.getCallback()).setBackground(ScreenshotDrawable.this.getOriginalDrawable());
                    }
                }
            });
        }
        this.mVisibilityChangeAnimator.setDuration(200);
        this.mVisibilityChangeAnimator.addUpdateListener(this);
        this.mVisibilityChangeAnimator.start();
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.mSrcRect.setEmpty();
        Drawable drawable = this.mOriginalDrawable;
        if (drawable != null) {
            drawable.setBounds(left, top, right, bottom);
        }
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
        Drawable drawable = this.mOriginalDrawable;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter cf) {
    }

    public int getOpacity() {
        return 0;
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        setAlpha(((Integer) animation.getAnimatedValue()).intValue());
    }
}
