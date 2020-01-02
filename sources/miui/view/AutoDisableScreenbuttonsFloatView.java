package miui.view;

import android.content.Context;
import android.miui.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

public class AutoDisableScreenbuttonsFloatView extends FrameLayout {
    private static final int DISMISS_DELAY_TIME = 8000;
    private Runnable mDismissRunnable = new Runnable() {
        public void run() {
            AutoDisableScreenbuttonsFloatView.this.dismiss();
        }
    };
    private boolean mIsShowing;

    public AutoDisableScreenbuttonsFloatView(Context context) {
        super(context);
    }

    public AutoDisableScreenbuttonsFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public static AutoDisableScreenbuttonsFloatView inflate(Context context) {
        return (AutoDisableScreenbuttonsFloatView) LayoutInflater.from(context).inflate((int) R.layout.auto_disable_screenbuttons_float, null);
    }

    public void show() {
        if (!this.mIsShowing) {
            WindowManager wm = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                LayoutParams lp = new LayoutParams(2010);
                lp.gravity = 80;
                lp.height = -2;
                lp.width = -2;
                lp.flags = 264;
                lp.format = -3;
                lp.windowAnimations = R.style.Animation_AutoDisableScreenButtonsFloat;
                wm.addView(this, lp);
                postDelayed(this.mDismissRunnable, 8000);
                this.mIsShowing = true;
            }
        }
    }

    public void dismiss() {
        if (this.mIsShowing) {
            this.mIsShowing = false;
            removeCallbacks(this.mDismissRunnable);
            ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).removeView(this);
        }
    }
}
