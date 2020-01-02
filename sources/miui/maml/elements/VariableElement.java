package miui.maml.elements;

import android.graphics.Canvas;
import android.util.Log;
import com.android.internal.app.DumpHeapActivity;
import miui.maml.CommandTrigger;
import miui.maml.ScreenElementRoot;
import miui.maml.animation.BaseAnimation;
import miui.maml.animation.VariableAnimation;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.VariableType;
import miui.maml.data.Variables;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class VariableElement extends ScreenElement {
    private static final String LOG_TAG = "VariableElement";
    private static final String OLD_VALUE = "old_value";
    public static final String TAG_NAME = "Var";
    private VariableAnimation mAnimation;
    private int mArraySize;
    private Expression[] mArrayValues;
    private boolean mConst;
    private Expression mExpression;
    private Expression mIndexExpression;
    private boolean mInited;
    private double mOldValue;
    private IndexedVariable mOldVar;
    private double mThreshold;
    private CommandTrigger mTrigger;
    private VariableType mType;
    private IndexedVariable mVar;

    /* renamed from: miui.maml.elements.VariableElement$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$VariableType = new int[VariableType.values().length];

        static {
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.STR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.STR_ARR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.NUM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public VariableElement(Element ele, ScreenElementRoot root) {
        super(ele, root);
        if (ele != null) {
            this.mExpression = Expression.build(getVariables(), ele.getAttribute("expression"));
            this.mIndexExpression = Expression.build(getVariables(), ele.getAttribute("index"));
            this.mThreshold = (double) Math.abs(Utils.getAttrAsFloat(ele, "threshold", 1.0f));
            this.mType = VariableType.parseType(ele.getAttribute("type"));
            this.mConst = Boolean.parseBoolean(ele.getAttribute("const"));
            this.mArraySize = Utils.getAttrAsInt(ele, DumpHeapActivity.KEY_SIZE, 0);
            Variables variables = getVariables();
            this.mVar = new IndexedVariable(this.mName, variables, this.mType.isNumber());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".");
            stringBuilder.append(OLD_VALUE);
            this.mOldVar = new IndexedVariable(stringBuilder.toString(), variables, this.mType.isNumber());
            this.mTrigger = CommandTrigger.fromParentElement(ele, root);
            if (this.mType.isArray()) {
                this.mArrayValues = Expression.buildMultiple(variables, ele.getAttribute("values"));
                Expression[] expressionArr = this.mArrayValues;
                if (expressionArr != null) {
                    this.mArraySize = expressionArr.length;
                }
                int i = this.mArraySize;
                String str = LOG_TAG;
                if (i <= 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("array size is 0:");
                    stringBuilder.append(this.mName);
                    Log.e(str, stringBuilder.toString());
                } else if (!variables.createArray(this.mName, this.mArraySize, this.mType.mTypeClass)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("fail to create array:");
                    stringBuilder2.append(this.mName);
                    Log.e(str, stringBuilder2.toString());
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public BaseAnimation onCreateAnimation(String tag, Element ele) {
        if (!VariableAnimation.TAG_NAME.equals(tag)) {
            return super.onCreateAnimation(tag, ele);
        }
        VariableAnimation variableAnimation = new VariableAnimation(ele, this);
        this.mAnimation = variableAnimation;
        return variableAnimation;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        if (!this.mConst) {
            super.doTick(currentTime);
            update();
        }
    }

    public void init() {
        super.init();
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.init();
        }
        update();
        this.mInited = true;
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimBefore() {
        this.mAnimation = null;
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimEnable(BaseAnimation ani) {
        if (ani instanceof VariableAnimation) {
            this.mAnimation = (VariableAnimation) ani;
        }
    }

    /* Access modifiers changed, original: protected */
    public void playAnim(long time, long startTime, long endTime, boolean isLoop, boolean isDelay) {
        super.playAnim(time, startTime, endTime, isLoop, isDelay);
        update();
    }

    /* Access modifiers changed, original: protected */
    public void pauseAnim(long time) {
        super.pauseAnim(time);
        update();
    }

    /* Access modifiers changed, original: protected */
    public void resumeAnim(long time) {
        super.resumeAnim(time);
        update();
    }

    public void reset(long time) {
        super.reset(time);
        update();
    }

    public void pause() {
        super.pause();
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.pause();
        }
    }

    public void resume() {
        super.resume();
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.resume();
        }
    }

    public void finish() {
        super.finish();
        CommandTrigger commandTrigger = this.mTrigger;
        if (commandTrigger != null) {
            commandTrigger.finish();
        }
        this.mInited = false;
    }

    private void update() {
        int i = AnonymousClass1.$SwitchMap$miui$maml$data$VariableType[this.mType.ordinal()];
        String str;
        Object str2;
        int index;
        Expression ex;
        if (i == 1) {
            str = this.mExpression;
            if (str != null) {
                str2 = str.evaluateStr();
                Object oldStr = this.mVar.getString();
                if (!Utils.equals(str2, oldStr)) {
                    this.mOldVar.set(oldStr);
                    this.mVar.set(str2);
                    CommandTrigger commandTrigger = this.mTrigger;
                    if (commandTrigger != null) {
                        commandTrigger.perform();
                    }
                }
            }
        } else if (i == 2) {
            str = this.mExpression;
            if (str == null) {
                return;
            }
            if (this.mIndexExpression != null) {
                str2 = str.evaluateStr();
                index = (int) this.mIndexExpression.evaluate();
                Object oldStr2 = this.mVar.getArrString(index);
                if (!Utils.equals(str2, oldStr2)) {
                    this.mOldVar.set(oldStr2);
                    this.mVar.setArr(index, str2);
                    CommandTrigger commandTrigger2 = this.mTrigger;
                    if (commandTrigger2 != null) {
                        commandTrigger2.perform();
                        return;
                    }
                    return;
                }
                return;
            }
            i = this.mArrayValues;
            if (i != 0) {
                i = i.length;
                for (index = 0; index < i; index++) {
                    ex = this.mArrayValues[index];
                    this.mVar.setArr(index, ex == null ? null : ex.evaluateStr());
                }
            }
        } else if (i == 3) {
            double value = getDouble(false, 0);
            this.mVar.set(value);
            onValueChange(value);
        } else if (this.mType.isNumberArray()) {
            Expression expression = this.mIndexExpression;
            if (expression != null) {
                i = (int) expression.evaluate();
                double value2 = getDouble(true, i);
                this.mVar.setArr(i, value2);
                onValueChange(value2);
                return;
            }
            i = this.mArrayValues;
            if (i != 0) {
                i = i.length;
                for (index = 0; index < i; index++) {
                    ex = this.mArrayValues[index];
                    this.mVar.setArr(index, ex == null ? 0.0d : ex.evaluate());
                }
            }
        }
    }

    private double getDouble(boolean isArray, int index) {
        VariableAnimation variableAnimation = this.mAnimation;
        if (variableAnimation != null) {
            return variableAnimation.getValue();
        }
        Expression expression = this.mExpression;
        if (expression != null) {
            return expression.evaluate();
        }
        IndexedVariable indexedVariable = this.mVar;
        return isArray ? indexedVariable.getArrDouble(index) : indexedVariable.getDouble();
    }

    private void onValueChange(double value) {
        if (!this.mInited) {
            this.mOldValue = value;
        }
        if (this.mTrigger != null && Math.abs(value - this.mOldValue) >= this.mThreshold) {
            this.mOldVar.set(this.mOldValue);
            this.mOldValue = value;
            this.mTrigger.perform();
        }
    }
}
