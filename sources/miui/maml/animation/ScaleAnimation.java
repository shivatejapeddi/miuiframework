package miui.maml.animation;

import miui.maml.animation.BaseAnimation.AnimationItem;
import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class ScaleAnimation extends BaseAnimation {
    public static final String TAG_NAME = "ScaleAnimation";
    private double mDelayValueX;
    private double mDelayValueY;

    public ScaleAnimation(Element node, ScreenElement screenElement) {
        super(node, ListItemElement.TAG_NAME, new String[]{"value", "x", "y"}, screenElement);
        AnimationItem ai = getItem(null);
        this.mDelayValueX = getItemX(ai);
        this.mDelayValueY = getItemY(ai);
    }

    public final double getScaleX() {
        return getCurValue(1);
    }

    public final double getScaleY() {
        return getCurValue(2);
    }

    /* Access modifiers changed, original: protected */
    public void onTick(AnimationItem item1, AnimationItem item2, float ratio) {
        if (item1 != null || item2 != null) {
            double x = getItemX(item1);
            setCurValue(1, ((getItemX(item2) - x) * ((double) ratio)) + x);
            double y = getItemY(item1);
            setCurValue(2, ((getItemY(item2) - y) * ((double) ratio)) + y);
        }
    }

    private double getItemX(AnimationItem item) {
        if (item == null) {
            return 1.0d;
        }
        int i = 0;
        if (!item.attrExists(0)) {
            i = 1;
        }
        return item.get(i);
    }

    private double getItemY(AnimationItem item) {
        if (item == null) {
            return 1.0d;
        }
        int i = 0;
        if (!item.attrExists(0)) {
            i = 2;
        }
        return item.get(i);
    }

    /* Access modifiers changed, original: protected */
    public double getDelayValue(int i) {
        return (i == 0 || i == 1) ? this.mDelayValueX : this.mDelayValueY;
    }
}
