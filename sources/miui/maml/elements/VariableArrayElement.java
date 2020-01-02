package miui.maml.elements;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.HashSet;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public class VariableArrayElement extends ScreenElement {
    public static final String TAG_NAME = "VarArray";
    private ArrayList<Item> mArray = new ArrayList();
    Object[] mData;
    private int mItemCount;
    private IndexedVariable mItemCountVar;
    Type mType = Type.DOUBLE;
    HashSet<VarObserver> mVarObserver = new HashSet();
    private ArrayList<Var> mVars = new ArrayList();

    public interface VarObserver {
        void onDataChange(Object[] objArr);
    }

    private class Item {
        public Expression mExpression;
        public Object mValue;

        public Item(Variables vars, Element ele) {
            if (ele != null) {
                this.mExpression = Expression.build(vars, ele.getAttribute("expression"));
                String valueStr = ele.getAttribute("value");
                if (VariableArrayElement.this.mType == Type.DOUBLE) {
                    try {
                        this.mValue = Double.valueOf(Double.parseDouble(valueStr));
                        return;
                    } catch (NumberFormatException e) {
                        return;
                    }
                }
                this.mValue = valueStr;
            }
        }

        public Item(Object value) {
            this.mValue = value;
            this.mExpression = null;
        }

        public boolean isExpression() {
            return this.mExpression != null;
        }

        public String evaluateStr() {
            Expression expression = this.mExpression;
            if (expression != null) {
                return expression.evaluateStr();
            }
            Object obj = this.mValue;
            return obj instanceof String ? (String) obj : null;
        }

        public Double evaluate() {
            Expression expression = this.mExpression;
            Double d = null;
            if (expression != null) {
                if (!expression.isNull()) {
                    d = Double.valueOf(this.mExpression.evaluate());
                }
                return d;
            }
            Object obj = this.mValue;
            if (obj instanceof Number) {
                d = Double.valueOf(((Number) obj).doubleValue());
            }
            return d;
        }
    }

    public enum Type {
        DOUBLE,
        STRING
    }

    private class Var {
        private boolean mConst;
        private boolean mCurrentItemIsExpression;
        private int mIndex = -1;
        private Expression mIndexExpression;
        private String mName;
        private IndexedVariable mVar;

        public Var(Variables vars, Element ele) {
            if (ele != null) {
                this.mName = ele.getAttribute("name");
                this.mIndexExpression = Expression.build(vars, ele.getAttribute("index"));
                this.mConst = Boolean.parseBoolean(ele.getAttribute("const"));
                this.mVar = new IndexedVariable(this.mName, VariableArrayElement.this.getVariables(), VariableArrayElement.this.mType != Type.STRING);
            }
        }

        public void tick() {
            if (!this.mConst) {
                update();
            }
        }

        public void init() {
            this.mIndex = -1;
            update();
        }

        private void update() {
            Expression expression = this.mIndexExpression;
            if (expression != null) {
                int index = (int) expression.evaluate();
                if (index < 0 || index >= VariableArrayElement.this.mArray.size()) {
                    if (VariableArrayElement.this.mType == Type.STRING) {
                        this.mVar.set(null);
                    } else if (VariableArrayElement.this.mType == Type.DOUBLE) {
                        this.mVar.set(0.0d);
                    }
                } else if (this.mIndex != index || this.mCurrentItemIsExpression) {
                    Item item = (Item) VariableArrayElement.this.mArray.get(index);
                    if (this.mIndex != index) {
                        this.mIndex = index;
                        this.mCurrentItemIsExpression = item.isExpression();
                    }
                    if (VariableArrayElement.this.mType == Type.STRING) {
                        this.mVar.set(item.evaluateStr());
                    } else if (VariableArrayElement.this.mType == Type.DOUBLE) {
                        this.mVar.set(item.evaluate());
                    }
                }
            }
        }
    }

    public VariableArrayElement(Element ele, ScreenElementRoot root) {
        super(ele, root);
        if (ele != null) {
            if ("string".equalsIgnoreCase(ele.getAttribute("type"))) {
                this.mType = Type.STRING;
            } else {
                this.mType = Type.DOUBLE;
            }
            final Variables vars = getVariables();
            Utils.traverseXmlElementChildren(Utils.getChild(ele, "Vars"), VariableElement.TAG_NAME, new XmlTraverseListener() {
                public void onChild(Element child) {
                    VariableArrayElement.this.mVars.add(new Var(vars, child));
                }
            });
            Utils.traverseXmlElementChildren(Utils.getChild(ele, "Items"), ListItemElement.TAG_NAME, new XmlTraverseListener() {
                public void onChild(Element child) {
                    VariableArrayElement.this.mArray.add(new Item(vars, child));
                }
            });
            if (this.mHasName) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".count");
                this.mItemCountVar = new IndexedVariable(stringBuilder.toString(), vars, true);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        int N = this.mVars.size();
        for (int i = 0; i < N; i++) {
            ((Var) this.mVars.get(i)).tick();
        }
    }

    public void init() {
        int i;
        super.init();
        int N = this.mVars.size();
        for (i = 0; i < N; i++) {
            ((Var) this.mVars.get(i)).init();
        }
        this.mItemCount = this.mArray.size();
        IndexedVariable indexedVariable = this.mItemCountVar;
        if (indexedVariable != null) {
            indexedVariable.set((double) this.mItemCount);
        }
        if (this.mData == null) {
            this.mData = new Object[this.mItemCount];
            for (i = 0; i < this.mItemCount; i++) {
                this.mData[i] = ((Item) this.mArray.get(i)).mValue;
            }
        }
    }

    public void registerVarObserver(VarObserver observer, boolean reg) {
        if (observer != null) {
            if (reg) {
                this.mVarObserver.add(observer);
                observer.onDataChange(this.mData);
            } else {
                this.mVarObserver.remove(observer);
            }
        }
    }

    public int getItemSize() {
        return this.mItemCount;
    }
}
