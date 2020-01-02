package miui.maml.elements;

import android.util.Log;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class ScreenElementFactory {
    private FactoryCallback mFactoryCallback;

    public interface FactoryCallback {
        ScreenElement onCreateInstance(Element element, ScreenElementRoot screenElementRoot);
    }

    public void setCallback(FactoryCallback factoryCallback) {
        this.mFactoryCallback = factoryCallback;
    }

    public FactoryCallback getCallback() {
        return this.mFactoryCallback;
    }

    public ScreenElement createInstance(Element ele, ScreenElementRoot root) {
        String tag = ele.getTagName();
        try {
            if (tag.equalsIgnoreCase(ImageScreenElement.TAG_NAME)) {
                return new ImageScreenElement(ele, root);
            }
            if (tag.equalsIgnoreCase(TimepanelScreenElement.TAG_NAME)) {
                return new TimepanelScreenElement(ele, root);
            }
            if (!tag.equalsIgnoreCase(ImageNumberScreenElement.TAG_NAME)) {
                if (!tag.equalsIgnoreCase(ImageNumberScreenElement.TAG_NAME1)) {
                    if (tag.equalsIgnoreCase(TextScreenElement.TAG_NAME)) {
                        return new TextScreenElement(ele, root);
                    }
                    if (tag.equalsIgnoreCase("DateTime")) {
                        return new DateTimeScreenElement(ele, root);
                    }
                    if (tag.equalsIgnoreCase(ButtonScreenElement.TAG_NAME)) {
                        return new ButtonScreenElement(ele, root);
                    }
                    if (tag.equalsIgnoreCase(MusicControlScreenElement.TAG_NAME)) {
                        return new MusicControlScreenElement(ele, root);
                    }
                    if (!tag.equalsIgnoreCase(ElementGroup.TAG_NAME)) {
                        if (!tag.equalsIgnoreCase("Group")) {
                            if (tag.equalsIgnoreCase(VariableElement.TAG_NAME)) {
                                return new VariableElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(VariableArrayElement.TAG_NAME)) {
                                return new VariableArrayElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(SpectrumVisualizerScreenElement.TAG_NAME)) {
                                return new SpectrumVisualizerScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(AdvancedSlider.TAG_NAME)) {
                                return new AdvancedSlider(ele, root);
                            }
                            if (tag.equalsIgnoreCase(FramerateController.TAG_NAME)) {
                                return new FramerateController(ele, root);
                            }
                            if (tag.equalsIgnoreCase("VirtualScreen")) {
                                return new VirtualScreen(ele, root);
                            }
                            if (tag.equalsIgnoreCase(LineScreenElement.TAG_NAME)) {
                                return new LineScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(RectangleScreenElement.TAG_NAME)) {
                                return new RectangleScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(EllipseScreenElement.TAG_NAME)) {
                                return new EllipseScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(CircleScreenElement.TAG_NAME)) {
                                return new CircleScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(ArcScreenElement.TAG_NAME)) {
                                return new ArcScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(CurveScreenElement.TAG_NAME)) {
                                return new CurveScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(ListScreenElement.TAG_NAME)) {
                                return new ListScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(PaintScreenElement.TAG_NAME)) {
                                return new PaintScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(MirrorScreenElement.TAG_NAME)) {
                                return new MirrorScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(WindowScreenElement.TAG_NAME)) {
                                return new WindowScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(ScreenElementArray.TAG_NAME)) {
                                return new ScreenElementArray(ele, root);
                            }
                            if (tag.equalsIgnoreCase(WebViewScreenElement.TAG_NAME)) {
                                return new WebViewScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(LayerScreenElement.TAG_NAME)) {
                                return new LayerScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(GLLayerScreenElement.TAG_NAME)) {
                                return new GLLayerScreenElement(ele, root);
                            }
                            if (tag.equalsIgnoreCase(CanvasDrawerElement.TAG_NAME)) {
                                return new CanvasDrawerElement(ele, root);
                            }
                            if (this.mFactoryCallback != null) {
                                return this.mFactoryCallback.onCreateInstance(ele, root);
                            }
                            return null;
                        }
                    }
                    return new ElementGroup(ele, root);
                }
            }
            return new ImageNumberScreenElement(ele, root);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fail to create element.");
            stringBuilder.append(e);
            Log.w("ScreenElementFactory", stringBuilder.toString());
        }
    }
}
