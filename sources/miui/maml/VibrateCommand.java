package miui.maml;

import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public class VibrateCommand extends ActionCommand {
    private static final String LOG_TAG = "VibrateCommand";
    public static final String TAG_NAME = "VibrateCommand";

    public VibrateCommand(ScreenElement screenElement, Element ele) {
        super(screenElement);
    }

    /* Access modifiers changed, original: protected */
    public void doPerform() {
    }
}
