package miui.animation;

import android.animation.LayoutTransition;

@Deprecated
public class LayoutTransitionUtil {
    public static void endChangingAnimations(LayoutTransition layoutTransition) {
        layoutTransition.endChangingAnimations();
    }

    public static void cancel(LayoutTransition layoutTransition) {
        layoutTransition.cancel();
    }
}
