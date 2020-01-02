package miui.maml.elements;

import android.graphics.Canvas;
import android.util.Log;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class MirrorScreenElement extends AnimatedScreenElement {
    private static final String LOG_TAG = "MirrorScreenElement";
    public static final String TAG_NAME = "Mirror";
    private boolean mMirrorTranslation;
    private ScreenElement mTarget;
    private String mTargetName;

    public MirrorScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mTargetName = node.getAttribute("target");
        this.mMirrorTranslation = Boolean.parseBoolean(node.getAttribute("mirrorTranslation"));
    }

    public void init() {
        super.init();
        this.mTarget = this.mRoot.findElement(this.mTargetName);
        if (this.mTarget == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("the target does not exist: ");
            stringBuilder.append(this.mTargetName);
            Log.e(LOG_TAG, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        ScreenElement screenElement = this.mTarget;
        if (screenElement == null) {
            return;
        }
        if (this.mMirrorTranslation && (screenElement instanceof AnimatedScreenElement)) {
            ((AnimatedScreenElement) screenElement).doRenderWithTranslation(c);
        } else {
            this.mTarget.doRender(c);
        }
    }
}
