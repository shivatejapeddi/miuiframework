package miui.maml.animation;

import miui.maml.animation.BaseAnimation.AnimationItem;
import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class SourcesAnimation extends PositionAnimation {
    public static final String TAG_NAME = "SourcesAnimation";
    private String mCurrentSrc;

    public static class Source extends AnimationItem {
        public static final String TAG_NAME = "Source";
        public String mSrc;

        public Source(BaseAnimation ani, Element node) {
            super(ani, node);
            this.mSrc = node.getAttribute("src");
        }
    }

    public SourcesAnimation(Element node, ScreenElement screenElement) {
        super(node, Source.TAG_NAME, screenElement);
    }

    public final String getSrc() {
        return this.mCurrentSrc;
    }

    /* Access modifiers changed, original: protected */
    public AnimationItem onCreateItem(BaseAnimation ani, Element ele) {
        return new Source(ani, ele);
    }

    /* Access modifiers changed, original: protected */
    public void onTick(AnimationItem item1, AnimationItem item2, float ratio) {
        if (item2 == null) {
            setCurValue(0, 0.0d);
            setCurValue(1, 0.0d);
            return;
        }
        setCurValue(0, item2.get(0));
        setCurValue(1, item2.get(1));
        this.mCurrentSrc = ((Source) item2).mSrc;
    }
}
