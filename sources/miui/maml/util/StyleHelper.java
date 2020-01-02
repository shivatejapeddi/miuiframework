package miui.maml.util;

import android.text.TextUtils;
import miui.maml.StylesManager.Style;
import org.w3c.dom.Element;

public class StyleHelper {
    public static String getAttr(Element node, String attrName, Style style) {
        String text = node.getAttribute(attrName);
        if (!TextUtils.isEmpty(text)) {
            return text;
        }
        if (style != null) {
            text = style.getAttr(attrName);
        }
        return text != null ? text : "";
    }
}
