package miui.maml;

import android.service.notification.Condition;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ActionCommand.PropertyCommand;
import miui.maml.data.Expression;
import miui.maml.elements.ScreenElement;
import miui.maml.util.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CommandTrigger {
    public static final String TAG_NAME = "Trigger";
    private String[] mActionStrings;
    private ArrayList<ActionCommand> mCommands = new ArrayList();
    private Expression mCondition;
    private PropertyCommand mPropertyCommand;
    private ScreenElement mScreenElement;

    public CommandTrigger(Element ele, ScreenElement parent) {
        if (ele != null) {
            load(ele, parent);
        }
    }

    public static CommandTrigger fromParentElement(Element parent, ScreenElement screenElement) {
        return fromElement(Utils.getChild(parent, TAG_NAME), screenElement);
    }

    public static CommandTrigger fromElement(Element ele, ScreenElement screenElement) {
        return ele == null ? null : new CommandTrigger(ele, screenElement);
    }

    private void load(Element ele, ScreenElement screenElement) {
        this.mScreenElement = screenElement;
        String target = ele.getAttribute("target");
        String property = ele.getAttribute("property");
        String value = ele.getAttribute("value");
        if (!(TextUtils.isEmpty(property) || TextUtils.isEmpty(target) || TextUtils.isEmpty(value))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(target);
            stringBuilder.append(".");
            stringBuilder.append(property);
            this.mPropertyCommand = PropertyCommand.create(screenElement, stringBuilder.toString(), value);
        }
        this.mActionStrings = ele.getAttribute("action").split(",");
        this.mCondition = Expression.build(this.mScreenElement.getVariables(), ele.getAttribute(Condition.SCHEME));
        NodeList children = ele.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == (short) 1) {
                ActionCommand command = ActionCommand.create((Element) children.item(i), screenElement);
                if (command != null) {
                    this.mCommands.add(command);
                }
            }
        }
    }

    public void perform() {
        Expression expression = this.mCondition;
        if (expression == null || expression.evaluate() > 0.0d) {
            PropertyCommand propertyCommand = this.mPropertyCommand;
            if (propertyCommand != null) {
                propertyCommand.perform();
            }
            Iterator it = this.mCommands.iterator();
            while (it.hasNext()) {
                ((ActionCommand) it.next()).perform();
            }
        }
    }

    public void init() {
        PropertyCommand propertyCommand = this.mPropertyCommand;
        if (propertyCommand != null) {
            propertyCommand.init();
        }
        Iterator it = this.mCommands.iterator();
        while (it.hasNext()) {
            ((ActionCommand) it.next()).init();
        }
    }

    public void finish() {
        PropertyCommand propertyCommand = this.mPropertyCommand;
        if (propertyCommand != null) {
            propertyCommand.finish();
        }
        Iterator it = this.mCommands.iterator();
        while (it.hasNext()) {
            ((ActionCommand) it.next()).finish();
        }
    }

    public void pause() {
        PropertyCommand propertyCommand = this.mPropertyCommand;
        if (propertyCommand != null) {
            propertyCommand.pause();
        }
        Iterator it = this.mCommands.iterator();
        while (it.hasNext()) {
            ((ActionCommand) it.next()).pause();
        }
    }

    public void resume() {
        PropertyCommand propertyCommand = this.mPropertyCommand;
        if (propertyCommand != null) {
            propertyCommand.resume();
        }
        Iterator it = this.mCommands.iterator();
        while (it.hasNext()) {
            ((ActionCommand) it.next()).resume();
        }
    }

    public void onAction(String action) {
        if (isAction(action)) {
            perform();
        }
    }

    public boolean isAction(String action) {
        for (String s : this.mActionStrings) {
            if (s.equals(action)) {
                return true;
            }
        }
        return false;
    }
}
