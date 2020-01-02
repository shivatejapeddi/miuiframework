package miui.maml.shader;

import android.graphics.Shader;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class ShadersElement {
    public static final String FILL_TAG_NAME = "FillShaders";
    public static final String STROKE_TAG_NAME = "StrokeShaders";
    private ShaderElement mShaderElement;

    public ShadersElement(Element node, ScreenElementRoot root) {
        loadShaderElements(node, root);
    }

    private void loadShaderElements(Element node, ScreenElementRoot root) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (child.getNodeType() == (short) 1) {
                Element ele = (Element) child;
                String tag = ele.getTagName();
                if (tag.equalsIgnoreCase(LinearGradientElement.TAG_NAME)) {
                    this.mShaderElement = new LinearGradientElement(ele, root);
                } else if (tag.equalsIgnoreCase(RadialGradientElement.TAG_NAME)) {
                    this.mShaderElement = new RadialGradientElement(ele, root);
                } else if (tag.equalsIgnoreCase(SweepGradientElement.TAG_NAME)) {
                    this.mShaderElement = new SweepGradientElement(ele, root);
                } else if (tag.equalsIgnoreCase(BitmapShaderElement.TAG_NAME)) {
                    this.mShaderElement = new BitmapShaderElement(ele, root);
                }
                if (this.mShaderElement != null) {
                    return;
                }
            }
        }
    }

    public void updateShader() {
        ShaderElement shaderElement = this.mShaderElement;
        if (shaderElement != null) {
            shaderElement.updateShader();
        }
    }

    public Shader getShader() {
        ShaderElement shaderElement = this.mShaderElement;
        return shaderElement != null ? shaderElement.getShader() : null;
    }
}
