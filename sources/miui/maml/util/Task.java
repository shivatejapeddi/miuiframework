package miui.maml.util;

import com.miui.internal.search.Function;
import org.w3c.dom.Element;

public class Task {
    public static String TAG_ACTION = "action";
    public static String TAG_CATEGORY = "category";
    public static String TAG_CLASS = Function.CLASS;
    public static String TAG_ID = "id";
    public static String TAG_NAME = "name";
    public static String TAG_PACKAGE = "package";
    public static String TAG_TYPE = "type";
    public String action;
    public String category;
    public String className;
    public String id;
    public String name;
    public String packageName;
    public String type;

    public static Task load(Element ele) {
        if (ele == null) {
            return null;
        }
        Task task = new Task();
        task.id = ele.getAttribute("id");
        task.action = ele.getAttribute("action");
        task.type = ele.getAttribute("type");
        task.category = ele.getAttribute("category");
        task.packageName = ele.getAttribute("package");
        task.className = ele.getAttribute(Function.CLASS);
        task.name = ele.getAttribute("name");
        return task;
    }
}
