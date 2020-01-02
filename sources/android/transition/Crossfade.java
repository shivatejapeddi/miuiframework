package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import java.util.Map;

public class Crossfade extends Transition {
    public static final int FADE_BEHAVIOR_CROSSFADE = 0;
    public static final int FADE_BEHAVIOR_OUT_IN = 2;
    public static final int FADE_BEHAVIOR_REVEAL = 1;
    private static final String LOG_TAG = "Crossfade";
    private static final String PROPNAME_BITMAP = "android:crossfade:bitmap";
    private static final String PROPNAME_BOUNDS = "android:crossfade:bounds";
    private static final String PROPNAME_DRAWABLE = "android:crossfade:drawable";
    public static final int RESIZE_BEHAVIOR_NONE = 0;
    public static final int RESIZE_BEHAVIOR_SCALE = 1;
    private static RectEvaluator sRectEvaluator = new RectEvaluator();
    private int mFadeBehavior = 1;
    private int mResizeBehavior = 1;

    public Crossfade setFadeBehavior(int fadeBehavior) {
        if (fadeBehavior >= 0 && fadeBehavior <= 2) {
            this.mFadeBehavior = fadeBehavior;
        }
        return this;
    }

    public int getFadeBehavior() {
        return this.mFadeBehavior;
    }

    public Crossfade setResizeBehavior(int resizeBehavior) {
        if (resizeBehavior >= 0 && resizeBehavior <= 1) {
            this.mResizeBehavior = resizeBehavior;
        }
        return this;
    }

    public int getResizeBehavior() {
        return this.mResizeBehavior;
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        boolean useParentOverlay = this.mFadeBehavior != 1;
        final View view = transitionValues2.view;
        Map<String, Object> startVals = transitionValues.values;
        Map<String, Object> endVals = transitionValues2.values;
        String str = PROPNAME_BOUNDS;
        Rect startBounds = (Rect) startVals.get(str);
        Rect endBounds = (Rect) endVals.get(str);
        str = PROPNAME_BITMAP;
        Bitmap startBitmap = (Bitmap) startVals.get(str);
        Bitmap endBitmap = (Bitmap) endVals.get(str);
        str = PROPNAME_DRAWABLE;
        final BitmapDrawable startDrawable = (BitmapDrawable) startVals.get(str);
        BitmapDrawable endDrawable = (BitmapDrawable) endVals.get(str);
        Rect rect;
        if (startDrawable == null || endDrawable == null || startBitmap.sameAs(endBitmap)) {
            Bitmap bitmap = endBitmap;
            Bitmap bitmap2 = startBitmap;
            rect = endBounds;
            return null;
        }
        ObjectAnimator anim;
        BitmapDrawable endDrawable2;
        ViewOverlay overlay = useParentOverlay ? ((ViewGroup) view.getParent()).getOverlay() : view.getOverlay();
        if (this.mFadeBehavior == 1) {
            overlay.add(endDrawable);
        }
        overlay.add(startDrawable);
        String str2 = "alpha";
        if (this.mFadeBehavior == 2) {
            anim = ObjectAnimator.ofInt((Object) startDrawable, str2, 255, 0, 0);
        } else {
            anim = ObjectAnimator.ofInt((Object) startDrawable, str2, 0);
        }
        anim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.invalidate(startDrawable.getBounds());
            }
        });
        ObjectAnimator anim1 = null;
        int i = this.mFadeBehavior;
        if (i == 2) {
            anim1 = ObjectAnimator.ofFloat((Object) view, View.ALPHA, 0.0f, 0.0f, 1.0f);
            endDrawable2 = endDrawable;
        } else if (i == 0) {
            endDrawable2 = endDrawable;
            anim1 = ObjectAnimator.ofFloat((Object) view, View.ALPHA, 0.0f, 1.0f);
        } else {
            endDrawable2 = endDrawable;
        }
        BitmapDrawable endDrawable3 = endDrawable2;
        endDrawable2 = startDrawable;
        startDrawable = useParentOverlay;
        final View view2 = view;
        final BitmapDrawable bitmapDrawable = endDrawable2;
        rect = endBounds;
        final BitmapDrawable bitmapDrawable2 = endDrawable3;
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ViewOverlay overlay = startDrawable ? ((ViewGroup) view2.getParent()).getOverlay() : view2.getOverlay();
                overlay.remove(bitmapDrawable);
                if (Crossfade.this.mFadeBehavior == 1) {
                    overlay.remove(bitmapDrawable2);
                }
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim);
        if (anim1 != null) {
            set.playTogether(anim1);
        }
        if (this.mResizeBehavior == 1 && startBounds.equals(rect) == null) {
            String str3 = "bounds";
            startDrawable = ObjectAnimator.ofObject((Object) endDrawable2, str3, sRectEvaluator, startBounds, rect);
            set.playTogether(startDrawable);
            if (this.mResizeBehavior == 1) {
                endDrawable = ObjectAnimator.ofObject((Object) endDrawable3, str3, sRectEvaluator, startBounds, rect);
                set.playTogether(endDrawable);
            }
        } else {
            BitmapDrawable endBitmap2 = endDrawable3;
        }
        return set;
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        Rect bounds = new Rect(0, 0, view.getWidth(), view.getHeight());
        if (this.mFadeBehavior != 1) {
            bounds.offset(view.getLeft(), view.getTop());
        }
        transitionValues.values.put(PROPNAME_BOUNDS, bounds);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        if (view instanceof TextureView) {
            bitmap = ((TextureView) view).getBitmap();
        } else {
            view.draw(new Canvas(bitmap));
        }
        transitionValues.values.put(PROPNAME_BITMAP, bitmap);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(bounds);
        transitionValues.values.put(PROPNAME_DRAWABLE, drawable);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }
}
