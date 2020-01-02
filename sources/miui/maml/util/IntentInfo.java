package miui.maml.util;

import android.app.slice.SliceItem;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.Condition;
import android.text.TextUtils;
import android.util.Log;
import com.miui.enterprise.sdk.ApplicationManager;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.data.Expression;
import miui.maml.data.Variables;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public class IntentInfo {
    private static final String LOG_TAG = "TaskVariable";
    private Expression mClassNameExp;
    private ArrayList<Extra> mExtraList = new ArrayList();
    private Expression mPackageNameExp;
    private Task mTask;
    private String mUri;
    private Expression mUriExp;
    private Variables mVariables;

    private class Extra {
        public static final String TAG_NAME = "Extra";
        private Expression mCondition;
        private Expression mExpression;
        private String mName;
        protected Type mType = Type.DOUBLE;

        public Extra(Element node) {
            load(node);
        }

        private void load(Element node) {
            String str = IntentInfo.LOG_TAG;
            if (node == null) {
                Log.e(str, "node is null");
                return;
            }
            this.mName = node.getAttribute("name");
            String type = node.getAttribute("type");
            if ("string".equalsIgnoreCase(type)) {
                this.mType = Type.STRING;
            } else if (SliceItem.FORMAT_INT.equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type)) {
                this.mType = Type.INT;
            } else if ("long".equalsIgnoreCase(type)) {
                this.mType = Type.LONG;
            } else if (ApplicationManager.FLOAT.equalsIgnoreCase(type)) {
                this.mType = Type.FLOAT;
            } else if ("double".equalsIgnoreCase(type)) {
                this.mType = Type.DOUBLE;
            } else if ("boolean".equalsIgnoreCase(type)) {
                this.mType = Type.BOOLEAN;
            }
            this.mExpression = Expression.build(IntentInfo.this.mVariables, node.getAttribute("expression"));
            if (this.mExpression == null) {
                Log.e(str, "invalid expression in IntentCommand");
            }
            this.mCondition = Expression.build(IntentInfo.this.mVariables, node.getAttribute(Condition.SCHEME));
        }

        public String getString() {
            Expression expression = this.mExpression;
            if (expression == null) {
                return null;
            }
            return expression.evaluateStr();
        }

        public double getDouble() {
            Expression expression = this.mExpression;
            if (expression == null) {
                return 0.0d;
            }
            return expression.evaluate();
        }

        public String getName() {
            return this.mName;
        }

        public boolean isConditionTrue() {
            Expression expression = this.mCondition;
            boolean z = true;
            if (expression == null) {
                return true;
            }
            if (expression.evaluate() <= 0.0d) {
                z = false;
            }
            return z;
        }
    }

    private enum Type {
        STRING,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN
    }

    private void loadExtras(Element node) {
        Utils.traverseXmlElementChildren(node, Extra.TAG_NAME, new XmlTraverseListener() {
            public void onChild(Element child) {
                IntentInfo.this.mExtraList.add(new Extra(child));
            }
        });
    }

    public IntentInfo(Element ele, Variables vars) {
        if (ele != null) {
            this.mTask = Task.load(ele);
            this.mVariables = vars;
            this.mPackageNameExp = Expression.build(vars, ele.getAttribute("packageExp"));
            this.mClassNameExp = Expression.build(vars, ele.getAttribute("classExp"));
            this.mUri = ele.getAttribute("uri");
            this.mUriExp = Expression.build(vars, ele.getAttribute("uriExp"));
            loadExtras(ele);
        }
    }

    public void set(Task task) {
        this.mTask = task;
    }

    public String getId() {
        Task task = this.mTask;
        return task != null ? task.id : null;
    }

    public String getAction() {
        Task task = this.mTask;
        return task != null ? task.action : null;
    }

    public void update(Intent intent) {
        Task task = this.mTask;
        String className = null;
        String action = task != null ? task.action : null;
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        Task task2 = this.mTask;
        String type = task2 != null ? task2.type : null;
        if (!TextUtils.isEmpty(type)) {
            intent.setType(type);
        }
        Task task3 = this.mTask;
        String category = task3 != null ? task3.category : null;
        if (!TextUtils.isEmpty(category)) {
            intent.addCategory(category);
        }
        Task task4 = this.mTask;
        String packageName = task4 != null ? task4.packageName : null;
        Expression expression = this.mPackageNameExp;
        if (expression != null) {
            packageName = expression.evaluateStr();
        }
        Task task5 = this.mTask;
        if (task5 != null) {
            className = task5.className;
        }
        expression = this.mClassNameExp;
        if (expression != null) {
            className = expression.evaluateStr();
        }
        if (!TextUtils.isEmpty(packageName)) {
            if (TextUtils.isEmpty(className)) {
                intent.setPackage(packageName);
            } else {
                intent.setClassName(packageName, className);
            }
        }
        CustomUtils.replaceCameraIntentInfoOnF3M(packageName, className, intent);
        String uri = this.mUri;
        Expression expression2 = this.mUriExp;
        if (expression2 != null) {
            uri = expression2.evaluateStr();
        }
        if (!TextUtils.isEmpty(uri)) {
            intent.setData(Uri.parse(uri));
        }
        ArrayList arrayList = this.mExtraList;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Extra extra = (Extra) it.next();
                if (extra.isConditionTrue()) {
                    switch (extra.mType) {
                        case STRING:
                            intent.putExtra(extra.getName(), extra.getString());
                            break;
                        case INT:
                            intent.putExtra(extra.getName(), (int) extra.getDouble());
                            break;
                        case LONG:
                            intent.putExtra(extra.getName(), (long) extra.getDouble());
                            break;
                        case FLOAT:
                            intent.putExtra(extra.getName(), (float) extra.getDouble());
                            break;
                        case DOUBLE:
                            intent.putExtra(extra.getName(), extra.getDouble());
                            break;
                        case BOOLEAN:
                            intent.putExtra(extra.getName(), extra.getDouble() > 0.0d);
                            break;
                        default:
                            break;
                    }
                }
                intent.removeExtra(extra.getName());
            }
        }
    }
}
