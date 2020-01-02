package miui.slide;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class SlideAnimationController {
    public static final int SLIDER_CLOSE = 1;
    public static final int SLIDER_MOVE = 2;
    public static final int SLIDER_OPEN = 0;
    public static final int SLIDER_TIP = 3;
    private static String TAG = "SlideAnimationController";
    private SlideAnimationView mAnimationView = new SlideAnimationView(this.mContext);
    private boolean mAnimationViewAdded;
    private Context mContext;
    private Handler mHandler;
    private WindowManager mWindowManager = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE));

    private class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            try {
                int width = SlideAnimationController.this.mContext.getResources().getDisplayMetrics().widthPixels;
                LayoutParams layoutParams = SlideAnimationController.this.getWindowParam(-1);
                int i = msg.what;
                if (i == 0) {
                    Slog.d(SlideAnimationController.TAG, "slider open animation");
                    if (!SlideAnimationController.this.mAnimationViewAdded) {
                        layoutParams.width = width;
                        SlideAnimationController.this.mWindowManager.addView(SlideAnimationController.this.mAnimationView, layoutParams);
                        SlideAnimationController.this.mAnimationView.startAnimating(0);
                        SlideAnimationController.this.mAnimationViewAdded = true;
                    }
                } else if (i == 1) {
                    Slog.d(SlideAnimationController.TAG, "slider close animation");
                    if (!SlideAnimationController.this.mAnimationViewAdded) {
                        layoutParams.width = width;
                        layoutParams.gravity = 17;
                        SlideAnimationController.this.mWindowManager.addView(SlideAnimationController.this.mAnimationView, layoutParams);
                        SlideAnimationController.this.mAnimationView.startAnimating(1);
                        SlideAnimationController.this.mAnimationViewAdded = true;
                    }
                } else if (i == 2) {
                    Slog.d(SlideAnimationController.TAG, "slider move animation");
                    if (SlideAnimationController.this.mAnimationViewAdded) {
                        SlideAnimationController.this.mAnimationView.stopAnimator();
                        SlideAnimationController.this.mWindowManager.removeView(SlideAnimationController.this.mAnimationView);
                        SlideAnimationController.this.mAnimationViewAdded = false;
                    }
                } else if (i == 3) {
                    Slog.d(SlideAnimationController.TAG, "slider tip animation");
                    if (!SlideAnimationController.this.mAnimationViewAdded) {
                        layoutParams.width = width;
                        layoutParams.gravity = 17;
                        SlideAnimationController.this.mWindowManager.addView(SlideAnimationController.this.mAnimationView, layoutParams);
                        SlideAnimationController.this.mAnimationView.startAnimating(2);
                        SlideAnimationController.this.mAnimationViewAdded = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SlideAnimationController(Context context, Looper looper) {
        this.mContext = context;
        this.mHandler = new H(looper);
    }

    public void showView(int state) {
        Message message = Message.obtain();
        message.what = state;
        this.mHandler.sendMessage(message);
    }

    private LayoutParams getWindowParam(int height) {
        int h;
        if (height > 0) {
            h = height;
        } else {
            h = new DisplayMetrics();
            this.mWindowManager.getDefaultDisplay().getRealMetrics(h);
            h = h.heightPixels;
        }
        LayoutParams lp = new LayoutParams(-1, h, 2015, 1336, -3);
        lp.privateFlags |= 64;
        lp.privateFlags |= 16;
        lp.setTitle("slideAnimation");
        return lp;
    }
}
