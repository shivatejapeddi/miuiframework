package miui.cameraanimation;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.miui.R;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

public class CameraAnimationView extends ImageView {
    private AnimationDrawable anim;
    private Handler mHandler;

    public CameraAnimationView(Context context, Looper looper) {
        super(context);
        init(looper);
    }

    private void init(Looper looper) {
        setBackgroundResource(R.drawable.camera_anima);
        this.anim = (AnimationDrawable) getBackground();
        this.mHandler = new Handler(looper);
        setVisibility(8);
    }

    public void startAnimation() {
        setVisibility(0);
        AnimationDrawable animationDrawable = this.anim;
        if (animationDrawable != null) {
            animationDrawable.setOneShot(true);
            this.anim.stop();
            this.anim.start();
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    CameraAnimationView.this.setVisibility(8);
                }
            }, (long) (this.anim.getNumberOfFrames() * this.anim.getDuration(0)));
        }
    }

    public void stopAnimation() {
        setVisibility(8);
        AnimationDrawable animationDrawable = this.anim;
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }
}
