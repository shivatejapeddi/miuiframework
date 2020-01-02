package miui.maml;

import java.util.HashMap;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class StylesManager {
    private HashMap<String, Style> mStyles = new HashMap();

    public class Style {
        public static final String TAG = "Style";
        private Style base;
        private HashMap<String, String> mAttrs = new HashMap();
        public String name;

        public Style(Element style) {
            NamedNodeMap nnm = style.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node item = nnm.item(i);
                String nodeName = item.getNodeName();
                String nodeValue = item.getNodeValue();
                if (nodeName.equals("name")) {
                    this.name = nodeValue;
                } else if (nodeName.equals("base")) {
                    this.base = (Style) StylesManager.this.mStyles.get(nodeValue);
                } else {
                    this.mAttrs.put(nodeName, nodeValue);
                }
            }
        }

        public String getAttr(String name) {
            String ret = (String) this.mAttrs.get(name);
            if (ret != null) {
                return ret;
            }
            Style style = this.base;
            if (style != null) {
                return style.getAttr(name);
            }
            return null;
        }
    }

    public StylesManager(Element styles) {
        Utils.traverseXmlElementChildren(styles, Style.TAG, new XmlTraverseListener() {
            public void onChild(Element child) {
                Style s = new Style(child);
                StylesManager.this.mStyles.put(s.name, s);
            }
        });
    }

    public Style getStyle(String name) {
        return (Style) this.mStyles.get(name);
    }
}
