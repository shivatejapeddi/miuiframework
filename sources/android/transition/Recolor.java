package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Map;

public class Recolor extends Transition {
    private static final String PROPNAME_BACKGROUND = "android:recolor:background";
    private static final String PROPNAME_TEXT_COLOR = "android:recolor:textColor";

    public Recolor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_TEXT_COLOR, Integer.valueOf(((TextView) transitionValues.view).getCurrentTextColor()));
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        View view = endValues.view;
        Map map = startValues.values;
        String str = PROPNAME_BACKGROUND;
        Drawable startBackground = (Drawable) map.get(str);
        Object endBackground = (Drawable) endValues.values.get(str);
        if ((startBackground instanceof ColorDrawable) && (endBackground instanceof ColorDrawable)) {
            ColorDrawable startColor = (ColorDrawable) startBackground;
            ColorDrawable endColor = (ColorDrawable) endBackground;
            if (startColor.getColor() != endColor.getColor()) {
                endColor.setColor(startColor.getColor());
                return ObjectAnimator.ofArgb(endBackground, "color", startColor.getColor(), endColor.getColor());
            }
        }
        if (view instanceof TextView) {
            Object textView = (TextView) view;
            Map map2 = startValues.values;
            String str2 = PROPNAME_TEXT_COLOR;
            int start = ((Integer) map2.get(str2)).intValue();
            int end = ((Integer) endValues.values.get(str2)).intValue();
            if (start != end) {
                textView.setTextColor(end);
                return ObjectAnimator.ofArgb(textView, "textColor", start, end);
            }
        }
        return null;
    }
}
