package miui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class CircleProgressView extends FrameLayout {
    private static int MAX_PROGRESS = 100;
    private static String TAG = "CircleProgressView";
    private int mAngle;
    private RectF mArcRect;
    private int mCurProgress;
    private int mMaxProgress;
    private Bitmap mMemBitmap;
    private Canvas mMemCanvas;
    private Paint mPaint;
    private Drawable mProgressDrawable;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMaxProgress = MAX_PROGRESS;
        setBackgroundResource(R.drawable.circle_progress_background);
        setProgressResource(R.drawable.circle_progress_foreground);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(0);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    }

    public void setProgressResource(int id) {
        this.mProgressDrawable = this.mContext.getResources().getDrawable(id);
        int width = this.mProgressDrawable.getIntrinsicWidth();
        int height = this.mProgressDrawable.getIntrinsicHeight();
        this.mProgressDrawable.setBounds(0, 0, width, height);
        this.mMemBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        this.mMemCanvas = new Canvas(this.mMemBitmap);
        this.mArcRect = new RectF(0.0f, 0.0f, (float) width, (float) height);
        requestLayout();
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress > 0 && this.mMaxProgress != maxProgress) {
            this.mMaxProgress = maxProgress;
            setProgress(this.mCurProgress);
        }
    }

    public int getMaxProgress() {
        return this.mMaxProgress;
    }

    public int getProgress() {
        return this.mCurProgress;
    }

    public void setProgress(int progress) {
        this.mCurProgress = Math.min(progress, this.mMaxProgress);
        this.mCurProgress = Math.max(0, this.mCurProgress);
        int i = this.mMaxProgress;
        int angle = ((i - this.mCurProgress) * 360) / i;
        if (angle != this.mAngle) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("progress:");
            stringBuilder.append(this.mCurProgress);
            Log.i(str, stringBuilder.toString());
            this.mAngle = angle;
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mProgressDrawable != null) {
            this.mMemBitmap.eraseColor(0);
            this.mProgressDrawable.draw(this.mMemCanvas);
            Canvas canvas2 = this.mMemCanvas;
            RectF rectF = this.mArcRect;
            int i = this.mAngle;
            canvas2.drawArc(rectF, (float) (270 - i), (float) i, true, this.mPaint);
            canvas.drawBitmap(this.mMemBitmap, (float) ((getWidth() - this.mMemBitmap.getWidth()) / 2), (float) ((getHeight() - this.mMemBitmap.getHeight()) / 2), null);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Math.max(getMeasuredWidth(), getSuggestedMinimumWidth()), Math.max(getMeasuredHeight(), getSuggestedMinimumHeight()));
    }
}
