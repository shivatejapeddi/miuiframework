package miui.maml.animation;

import android.miui.BiometricConnect;
import java.util.Iterator;
import miui.maml.animation.BaseAnimation.AnimationItem;
import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class SizeAnimation extends BaseAnimation {
    public static final String INNER_TAG_NAME = "Size";
    public static final String TAG_NAME = "SizeAnimation";
    private double mMaxH;
    private double mMaxW;

    public SizeAnimation(Element node, ScreenElement screenElement) {
        super(node, INNER_TAG_NAME, new String[]{BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H}, screenElement);
        Iterator it = this.mItems.iterator();
        while (it.hasNext()) {
            AnimationItem ai = (AnimationItem) it.next();
            if (ai.get(0) > this.mMaxW) {
                this.mMaxW = ai.get(0);
            }
            if (ai.get(1) > this.mMaxH) {
                this.mMaxH = ai.get(1);
            }
        }
    }

    public final double getWidth() {
        return getCurValue(0);
    }

    public final double getHeight() {
        return getCurValue(1);
    }

    public final double getMaxWidth() {
        return this.mMaxW;
    }

    public final double getMaxHeight() {
        return this.mMaxH;
    }
}
