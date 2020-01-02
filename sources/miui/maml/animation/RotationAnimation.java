package miui.maml.animation;

import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class RotationAnimation extends BaseAnimation {
    public static final String INNER_TAG_NAME = "Rotation";
    public static final String TAG_NAME = "RotationAnimation";

    public RotationAnimation(Element node, ScreenElement screenElement) {
        super(node, INNER_TAG_NAME, "angle", screenElement);
    }

    public final float getAngle() {
        return (float) getCurValue(0);
    }
}
