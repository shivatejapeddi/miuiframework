package miui.maml.elements;

import android.text.TextUtils;
import miui.maml.ScreenElementRoot;
import miui.maml.data.IndexedVariable;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public class ScreenElementArray extends ElementGroup {
    private static final String DEF_INDEX_VAR_NAME = "__i";
    public static final String TAG_NAME = "Array";

    public ScreenElementArray(Element node, ScreenElementRoot root) {
        super(node, root);
        final int count = Utils.getAttrAsInt(node, "count", 0);
        final ScreenElementRoot _root = root;
        String indexVarName = node.getAttribute("indexName");
        if (TextUtils.isEmpty(indexVarName)) {
            indexVarName = DEF_INDEX_VAR_NAME;
        }
        final IndexedVariable indexVar = new IndexedVariable(indexVarName, getVariables(), true);
        Utils.traverseXmlElementChildren(node, null, new XmlTraverseListener() {
            public void onChild(Element ele) {
                ElementGroup ag = null;
                for (int i = 0; i < count; i++) {
                    ScreenElement child = super.onCreateChild(ele);
                    if (child != null) {
                        if (ag == null) {
                            ag = ElementGroup.createArrayGroup(_root, indexVar);
                            ag.setName(child.getName());
                            ScreenElementArray.this.addElement(ag);
                        }
                        ag.addElement(child);
                    }
                }
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public ScreenElement onCreateChild(Element ele) {
        return null;
    }
}
