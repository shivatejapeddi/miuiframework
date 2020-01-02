package miui.maml.elements;

import miui.maml.RendererController;
import miui.maml.ScreenElementRoot;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public abstract class ElementGroupRC extends ElementGroup {
    protected RendererController mController;
    private float mFrameRate;

    public abstract void onControllerCreated(RendererController rendererController);

    public ElementGroupRC(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mFrameRate = Utils.getAttrAsFloat(node, "frameRate", 0.0f);
        if (this.mFrameRate >= 0.0f) {
            this.mController = new RendererController();
            onControllerCreated(this.mController);
            this.mController.init();
        }
    }

    public void init() {
        super.init();
        requestFramerate(this.mFrameRate);
    }

    public RendererController getRendererController() {
        RendererController rendererController = this.mController;
        return rendererController != null ? rendererController : super.getRendererController();
    }
}
