package miui.maml.data;

import android.app.slice.SliceItem;
import android.text.TextUtils;
import com.miui.enterprise.sdk.ApplicationManager;
import java.util.ArrayList;
import miui.maml.CommandTrigger;
import miui.maml.ScreenContext;
import miui.maml.ScreenElementRoot;
import miui.maml.data.ContentProviderBinder.QueryCompleteListener;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public abstract class VariableBinder {
    private String mDependency;
    protected boolean mFinished;
    protected String mName;
    protected boolean mPaused;
    protected boolean mQueryAtStart = true;
    private QueryCompleteListener mQueryCompletedListener;
    protected ScreenElementRoot mRoot;
    protected CommandTrigger mTrigger;
    protected ArrayList<Variable> mVariables = new ArrayList();

    public static class TypedValue {
        public static final int BITMAP = 7;
        public static final int DOUBLE = 6;
        public static final int FLOAT = 5;
        public static final int INT = 3;
        public static final int LONG = 4;
        public static final int NUM_ARR = 8;
        public static final int STRING = 2;
        public static final int STR_ARR = 9;
        public static final int TYPE_BASE = 1000;
        public String mName;
        public int mType;
        public String mTypeStr;

        public TypedValue(String name, String type) {
            initInner(name, type);
        }

        public TypedValue(Element node) {
            if (node != null) {
                initInner(node.getAttribute("name"), node.getAttribute("type"));
                return;
            }
            throw new NullPointerException("node is null");
        }

        private void initInner(String name, String type) {
            this.mName = name;
            this.mTypeStr = type;
            this.mType = parseType(this.mTypeStr);
        }

        /* Access modifiers changed, original: protected */
        public int parseType(String type) {
            if ("string".equalsIgnoreCase(type)) {
                return 2;
            }
            if ("double".equalsIgnoreCase(type) || "number".equalsIgnoreCase(type)) {
                return 6;
            }
            if (ApplicationManager.FLOAT.equalsIgnoreCase(type)) {
                return 5;
            }
            if (SliceItem.FORMAT_INT.equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type)) {
                return 3;
            }
            if ("long".equalsIgnoreCase(type)) {
                return 4;
            }
            if ("bitmap".equalsIgnoreCase(type)) {
                return 7;
            }
            if ("number[]".equalsIgnoreCase(type)) {
                return 8;
            }
            if ("string[]".equalsIgnoreCase(type)) {
                return 9;
            }
            return 6;
        }

        public boolean isNumber() {
            int i = this.mType;
            return i >= 3 && i <= 6;
        }

        public boolean isArray() {
            int i = this.mType;
            return i == 8 || i == 9;
        }
    }

    public static class Variable extends TypedValue {
        public static final String TAG_NAME = "Variable";
        private Expression mArrayIndex;
        protected double mDefNumberValue;
        protected String mDefStringValue;
        protected IndexedVariable mVar;

        public Variable(String name, String type, Variables vars) {
            super(name, type);
            this.mVar = new IndexedVariable(this.mName, vars, isNumber());
        }

        public Variable(Element node, Variables vars) {
            super(node);
            this.mArrayIndex = Expression.build(vars, node.getAttribute("arrIndex"));
            String str = this.mName;
            boolean z = isNumber() && this.mArrayIndex == null;
            this.mVar = new IndexedVariable(str, vars, z);
            this.mDefStringValue = node.getAttribute("default");
            if (isNumber()) {
                try {
                    this.mDefNumberValue = Double.parseDouble(this.mDefStringValue);
                } catch (NumberFormatException e) {
                    this.mDefStringValue = null;
                    this.mDefNumberValue = 0.0d;
                }
            }
        }

        public void set(double value) {
            Expression expression = this.mArrayIndex;
            if (expression != null) {
                this.mVar.setArr((int) expression.evaluate(), value);
            } else {
                this.mVar.set(value);
            }
        }

        public void set(Object value) {
            if (isNumber()) {
                double d = 0.0d;
                if (value instanceof String) {
                    try {
                        d = Utils.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                    }
                } else if (value instanceof Number) {
                    d = ((Number) value).doubleValue();
                }
                Expression expression = this.mArrayIndex;
                if (expression != null) {
                    this.mVar.setArr((int) expression.evaluate(), d);
                    return;
                } else {
                    this.mVar.set(d);
                    return;
                }
            }
            if (value instanceof Number) {
                value = Utils.numberToString((Number) value);
            }
            Expression expression2 = this.mArrayIndex;
            if (expression2 != null) {
                this.mVar.setArr((int) expression2.evaluate(), value);
            } else {
                this.mVar.set(value);
            }
        }
    }

    public void init() {
        this.mFinished = false;
        this.mPaused = false;
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.init();
        }
        if (TextUtils.isEmpty(getDependency()) && this.mQueryAtStart) {
            startQuery();
        }
    }

    public void tick() {
    }

    public void finish() {
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.finish();
        }
        this.mFinished = true;
    }

    public void pause() {
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.pause();
        }
        this.mPaused = true;
    }

    public void resume() {
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.resume();
        }
        this.mPaused = false;
    }

    public String getName() {
        return this.mName;
    }

    public String getDependency() {
        return this.mDependency;
    }

    public void refresh() {
    }

    public void startQuery() {
    }

    public VariableBinder(Element node, ScreenElementRoot root) {
        this.mRoot = root;
        if (node != null) {
            this.mName = node.getAttribute("name");
            this.mDependency = node.getAttribute("dependency");
            this.mQueryAtStart = 1 ^ "false".equalsIgnoreCase(node.getAttribute("queryAtStart"));
            this.mTrigger = CommandTrigger.fromParentElement(node, this.mRoot);
        }
    }

    public final void accept(VariableBinderVisitor v) {
        v.visit(this);
    }

    /* Access modifiers changed, original: protected */
    public ScreenContext getContext() {
        return this.mRoot.getContext();
    }

    public Variables getVariables() {
        return this.mRoot.getVariables();
    }

    public void setQueryCompleteListener(QueryCompleteListener l) {
        this.mQueryCompletedListener = l;
    }

    /* Access modifiers changed, original: protected|final */
    public final void onUpdateComplete() {
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.perform();
        }
        if (!(this.mQueryCompletedListener == null || TextUtils.isEmpty(this.mName))) {
            this.mQueryCompletedListener.onQueryCompleted(this.mName);
        }
        this.mRoot.requestUpdate();
    }

    /* Access modifiers changed, original: protected */
    public void addVariable(Variable v) {
        this.mVariables.add(v);
    }

    /* Access modifiers changed, original: protected */
    public void loadVariables(Element node) {
        Utils.traverseXmlElementChildren(node, Variable.TAG_NAME, new XmlTraverseListener() {
            public void onChild(Element child) {
                Variable var = VariableBinder.this.onLoadVariable(child);
                if (var != null) {
                    VariableBinder.this.mVariables.add(var);
                }
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return null;
    }
}
