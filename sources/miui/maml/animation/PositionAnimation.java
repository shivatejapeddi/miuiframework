package miui.maml.animation;

import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class PositionAnimation extends BaseAnimation {
    public static final String INNER_TAG_NAME = "Position";
    public static final String TAG_NAME = "PositionAnimation";

    public PositionAnimation(Element node, ScreenElement screenElement) {
        this(node, "Position", screenElement);
    }

    public PositionAnimation(Element node, String tagName, ScreenElement screenElement) {
        super(node, tagName, new String[]{"x", "y"}, screenElement);
    }

    public final double getX() {
        return getCurValue(0);
    }

    public final double getY() {
        return getCurValue(1);
    }
}
