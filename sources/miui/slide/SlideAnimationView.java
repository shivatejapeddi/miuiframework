package miui.slide;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.camera2.legacy.LegacyCameraDevice;
import android.miui.R;
import android.util.DisplayMetrics;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import miui.view.animation.QuarticEaseOutInterpolator;
import miui.view.animation.SineEaseOutInterpolator;

public class SlideAnimationView extends View {
    public static final String TAG = "SlideAnimationView";
    public static final int TYPE_ANIMATION_DOWN = 1;
    public static final int TYPE_ANIMATION_TIP = 2;
    public static final int TYPE_ANIMATION_UP = 0;
    public static final int TYPE_BITMAP_SIDE = 1;
    public static final int TYPE_BITMAP_TOP = 0;
    private float mEdgeAlpha;
    private Bitmap mEdgeColor;
    private float mEdgeColorAlpha;
    private Bitmap mEdgeLeft;
    private Bitmap mEdgeLeftOri;
    private Bitmap mEdgeRight;
    private Bitmap mEdgeRightOri;
    private float mEdgeScaleX;
    private float mEdgeY;
    private ValueAnimator mFlowingAnimator;
    private Matrix mMatrix;
    private Paint mPaint = new Paint();
    private Bitmap mTop;
    private float mTopAlpha;
    private Bitmap mTopOri;
    private float mTopScaleY;

    public SlideAnimationView(Context context) {
        super(context);
        setBackgroundColor(0);
        this.mMatrix = new Matrix();
        this.mPaint = new Paint();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPortrait()) {
            try {
                this.mPaint.setAlpha((int) (this.mTopAlpha * 255.0f));
                if (this.mTop != null) {
                    canvas.drawBitmap(this.mTop, 0.0f, 0.0f, this.mPaint);
                }
                this.mPaint.setAlpha((int) (this.mEdgeAlpha * 255.0f));
                if (this.mEdgeLeft != null) {
                    canvas.drawBitmap(this.mEdgeLeft, 0.0f, this.mEdgeY, this.mPaint);
                }
                if (this.mEdgeRight != null) {
                    canvas.drawBitmap(this.mEdgeRight, (float) (getDisplayWidth() - this.mEdgeRight.getWidth()), this.mEdgeY, this.mPaint);
                }
                this.mPaint.setAlpha((int) (this.mEdgeColorAlpha * 255.0f));
                if (this.mEdgeColor != null) {
                    canvas.drawBitmap(this.mEdgeColor, 0.0f, 0.0f, this.mPaint);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startAnimating(final int type) {
        if (isPortrait()) {
            setVisibility(0);
            setBitmap();
            if (type == 0) {
                this.mFlowingAnimator = ValueAnimator.ofInt(0, 2160);
                this.mFlowingAnimator.setDuration(1000);
                this.mFlowingAnimator.setInterpolator(new SineEaseOutInterpolator());
            } else if (type == 1) {
                this.mFlowingAnimator = ValueAnimator.ofInt(2160, 0);
                this.mFlowingAnimator.setDuration(800);
                this.mFlowingAnimator.setInterpolator(new QuarticEaseOutInterpolator());
            }
            this.mFlowingAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    try {
                        int y = ((Integer) SlideAnimationView.this.mFlowingAnimator.getAnimatedValue()).intValue();
                        SlideAnimationView.this.mTopAlpha = SlideAnimationView.this.rangeAlpha(y, 0, 100, 150, 900);
                        if (SlideAnimationView.this.mTopAlpha < 0.0f || type == 1) {
                            SlideAnimationView.this.mTopAlpha = 0.0f;
                        }
                        SlideAnimationView.this.mTopScaleY = (float) (((((double) SlideAnimationView.this.rangeAlpha(y, 0, 100, 150, 900)) * 1.5d) * 0.6d) + 0.4d);
                        SlideAnimationView.this.mMatrix.setScale(1.0f, SlideAnimationView.this.mTopScaleY);
                        if (SlideAnimationView.this.mTopOri != null && SlideAnimationView.this.mTopOri.getWidth() > 0 && Math.round(((float) SlideAnimationView.this.mTopOri.getHeight()) * SlideAnimationView.this.mTopScaleY) > 0) {
                            SlideAnimationView.this.mTop = Bitmap.createBitmap(SlideAnimationView.this.mTopOri, 0, 0, SlideAnimationView.this.mTopOri.getWidth(), SlideAnimationView.this.mTopOri.getHeight(), SlideAnimationView.this.mMatrix, true);
                        }
                        SlideAnimationView.this.mEdgeY = (float) (((((double) y) * 1.2d) - 300.0d) - 1170.0d);
                        SlideAnimationView.this.mEdgeAlpha = SlideAnimationView.this.rangeAlpha(y, 480, 960, 1480, LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING);
                        SlideAnimationView.this.mEdgeScaleX = (float) ((((double) SlideAnimationView.this.rangeAlpha(y, 400, 800, 1000, 1800)) * 0.9d) + 0.1d);
                        SlideAnimationView.this.mMatrix.setScale(SlideAnimationView.this.mEdgeScaleX, 1.0f);
                        if (SlideAnimationView.this.mEdgeLeftOri != null && Math.round(((float) SlideAnimationView.this.mEdgeLeftOri.getWidth()) * SlideAnimationView.this.mEdgeScaleX) > 0 && SlideAnimationView.this.mEdgeLeftOri.getHeight() > 0) {
                            SlideAnimationView.this.mEdgeLeft = Bitmap.createBitmap(SlideAnimationView.this.mEdgeLeftOri, 0, 0, SlideAnimationView.this.mEdgeLeftOri.getWidth(), SlideAnimationView.this.mEdgeLeftOri.getHeight(), SlideAnimationView.this.mMatrix, true);
                        }
                        if (SlideAnimationView.this.mEdgeRightOri != null && Math.round(((float) SlideAnimationView.this.mEdgeRightOri.getWidth()) * SlideAnimationView.this.mEdgeScaleX) > 0 && SlideAnimationView.this.mEdgeRightOri.getHeight() > 0) {
                            SlideAnimationView.this.mEdgeRight = Bitmap.createBitmap(SlideAnimationView.this.mEdgeRightOri, 0, 0, SlideAnimationView.this.mEdgeRightOri.getWidth(), SlideAnimationView.this.mEdgeRightOri.getHeight(), SlideAnimationView.this.mMatrix, true);
                        }
                        if (SlideAnimationView.this.mEdgeAlpha < 0.0f) {
                            SlideAnimationView.this.mEdgeAlpha = 0.0f;
                        }
                        SlideAnimationView.this.mEdgeColorAlpha = SlideAnimationView.this.rangeAlpha(y, 0, DisplayMetrics.DENSITY_450, DisplayMetrics.DENSITY_450, 1480);
                        if (SlideAnimationView.this.mEdgeColorAlpha < 0.0f || type == 1) {
                            SlideAnimationView.this.mEdgeColorAlpha = 0.0f;
                        }
                        SlideAnimationView.this.invalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.mFlowingAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    SlideAnimationView.this.reset();
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            this.mFlowingAnimator.start();
        }
    }

    public void stopAnimator() {
        ValueAnimator valueAnimator = this.mFlowingAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight();
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public boolean isPortrait() {
        return getResources().getConfiguration().orientation == 1;
    }

    public void setBitmap() {
        InputStream inLeft = null;
        InputStream inRight = null;
        InputStream inTop = null;
        InputStream inColor = null;
        try {
            inLeft = getResources().openRawResource(R.drawable.slide_edge_line_left);
            this.mEdgeLeftOri = BitmapFactory.decodeStream(inLeft);
            inRight = getResources().openRawResource(R.drawable.slide_edge_line_right);
            this.mEdgeRightOri = BitmapFactory.decodeStream(inRight);
            inTop = getResources().openRawResource(R.drawable.slide_top_line);
            this.mTopOri = BitmapFactory.decodeStream(inTop);
            inColor = getResources().openRawResource(R.drawable.slide_edge_color);
            this.mEdgeColor = BitmapFactory.decodeStream(inColor);
            if (inLeft != null) {
                try {
                    inLeft.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (inRight != null) {
                inRight.close();
            }
            if (inTop != null) {
                inTop.close();
            }
            if (inColor != null) {
                inColor.close();
            }
        } catch (Throwable th) {
            if (inLeft != null) {
                try {
                    inLeft.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (inRight != null) {
                inRight.close();
            }
            if (inTop != null) {
                inTop.close();
            }
            if (inColor != null) {
                inColor.close();
            }
        }
    }

    private float rangeAlpha(int input, int r0, int r1, int r2, int r3) {
        if (input < r1) {
            return ((float) (input - r0)) / ((float) (r1 - r0));
        }
        if (input >= r2) {
            return 1.0f - (((float) (input - r2)) / ((float) (r3 - r2)));
        }
        return 1.0f;
    }

    public void reset() {
        setVisibility(8);
        this.mEdgeLeft = null;
        this.mEdgeRight = null;
        this.mTop = null;
        this.mEdgeColor = null;
        this.mEdgeLeftOri = null;
        this.mEdgeRightOri = null;
        this.mTopOri = null;
        this.mPaint.setAlpha(255);
    }
}
