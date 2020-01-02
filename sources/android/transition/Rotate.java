package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

public class Rotate extends Transition {
    private static final String PROPNAME_ROTATION = "android:rotate:rotation";

    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_ROTATION, Float.valueOf(transitionValues.view.getRotation()));
    }

    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_ROTATION, Float.valueOf(transitionValues.view.getRotation()));
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Object view = endValues.view;
        Map map = startValues.values;
        String str = PROPNAME_ROTATION;
        float startRotation = ((Float) map.get(str)).floatValue();
        if (startRotation == ((Float) endValues.values.get(str)).floatValue()) {
            return null;
        }
        view.setRotation(startRotation);
        return ObjectAnimator.ofFloat(view, View.ROTATION, startRotation, endRotation);
    }
}
