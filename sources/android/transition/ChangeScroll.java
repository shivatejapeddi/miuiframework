package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import java.util.Map;

public class ChangeScroll extends Transition {
    private static final String[] PROPERTIES = new String[]{PROPNAME_SCROLL_X, PROPNAME_SCROLL_Y};
    private static final String PROPNAME_SCROLL_X = "android:changeScroll:x";
    private static final String PROPNAME_SCROLL_Y = "android:changeScroll:y";

    public ChangeScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public String[] getTransitionProperties() {
        return PROPERTIES;
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_SCROLL_X, Integer.valueOf(transitionValues.view.getScrollX()));
        transitionValues.values.put(PROPNAME_SCROLL_Y, Integer.valueOf(transitionValues.view.getScrollY()));
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Object view = endValues.view;
        Map map = startValues.values;
        String str = PROPNAME_SCROLL_X;
        int startX = ((Integer) map.get(str)).intValue();
        int endX = ((Integer) endValues.values.get(str)).intValue();
        Map map2 = startValues.values;
        String str2 = PROPNAME_SCROLL_Y;
        int startY = ((Integer) map2.get(str2)).intValue();
        int endY = ((Integer) endValues.values.get(str2)).intValue();
        Animator scrollXAnimator = null;
        Animator scrollYAnimator = null;
        if (startX != endX) {
            view.setScrollX(startX);
            scrollXAnimator = ObjectAnimator.ofInt(view, "scrollX", startX, endX);
        }
        if (startY != endY) {
            view.setScrollY(startY);
            scrollYAnimator = ObjectAnimator.ofInt(view, "scrollY", startY, endY);
        }
        return TransitionUtils.mergeAnimators(scrollXAnimator, scrollYAnimator);
    }
}
