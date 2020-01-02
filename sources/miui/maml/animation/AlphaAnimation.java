package miui.maml.animation;

import android.app.backup.FullBackup;
import android.text.TextUtils;
import miui.maml.animation.BaseAnimation.AnimationItem;
import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class AlphaAnimation extends BaseAnimation {
    public static final String INNER_TAG_NAME = "Alpha";
    public static final String TAG_NAME = "AlphaAnimation";
    private int mDelayValue;

    public AlphaAnimation(Element node, ScreenElement screenElement) {
        super(node, INNER_TAG_NAME, FullBackup.APK_TREE_TOKEN, screenElement);
        String delayValue = node.getAttribute("delayValue");
        if (TextUtils.isEmpty(delayValue)) {
            AnimationItem ai = getItem(0);
            if (ai != null) {
                this.mDelayValue = (int) ai.get(0);
                return;
            }
            return;
        }
        try {
            this.mDelayValue = Integer.parseInt(delayValue);
        } catch (NumberFormatException e) {
        }
    }

    public final int getAlpha() {
        return (int) getCurValue(0);
    }

    /* Access modifiers changed, original: protected */
    public double getDelayValue(int i) {
        return (double) this.mDelayValue;
    }

    /* Access modifiers changed, original: protected */
    public double getDefaultValue() {
        return 255.0d;
    }
}
