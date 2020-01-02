package miui.maml;

import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CommandTriggers {
    private static final String LOG_TAG = "CommandTriggers";
    public static final String TAG_NAME = "Triggers";
    private ArrayList<CommandTrigger> mTriggers = new ArrayList();

    public CommandTriggers(Element node, ScreenElement ele) {
        if (node != null) {
            load(node, ele);
        }
    }

    public void onAction(String action) {
        Iterator it = this.mTriggers.iterator();
        while (it.hasNext()) {
            ((CommandTrigger) it.next()).onAction(action);
        }
    }

    public void init() {
        Iterator it = this.mTriggers.iterator();
        while (it.hasNext()) {
            ((CommandTrigger) it.next()).init();
        }
    }

    public void finish() {
        Iterator it = this.mTriggers.iterator();
        while (it.hasNext()) {
            ((CommandTrigger) it.next()).finish();
        }
    }

    public void pause() {
        Iterator it = this.mTriggers.iterator();
        while (it.hasNext()) {
            ((CommandTrigger) it.next()).pause();
        }
    }

    public void resume() {
        Iterator it = this.mTriggers.iterator();
        while (it.hasNext()) {
            ((CommandTrigger) it.next()).resume();
        }
    }

    public void add(CommandTrigger t) {
        this.mTriggers.add(t);
    }

    public CommandTrigger find(String action) {
        Iterator it = this.mTriggers.iterator();
        while (it.hasNext()) {
            CommandTrigger t = (CommandTrigger) it.next();
            if (t.isAction(action)) {
                return t;
            }
        }
        return null;
    }

    private void load(Element node, ScreenElement ele) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == (short) 1) {
                Element item = (Element) children.item(i);
                if (item.getNodeName().equals(CommandTrigger.TAG_NAME)) {
                    this.mTriggers.add(new CommandTrigger(item, ele));
                }
            }
        }
    }
}
