package miui.maml.data;

import java.util.IllegalFormatException;
import miui.maml.data.Expression.FunctionExpression;
import miui.maml.data.Expression.FunctionImpl;

public class FormatFunctions extends FunctionImpl {
    private final Fun mFun;

    /* renamed from: miui.maml.data.FormatFunctions$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$FormatFunctions$Fun = new int[Fun.values().length];

        static {
            try {
                $SwitchMap$miui$maml$data$FormatFunctions$Fun[Fun.FORMAT_DATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$FormatFunctions$Fun[Fun.FORMAT_FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$data$FormatFunctions$Fun[Fun.FORMAT_INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private enum Fun {
        INVALID,
        FORMAT_DATE,
        FORMAT_FLOAT,
        FORMAT_INT
    }

    public static void load() {
        FunctionExpression.registerFunction("formatDate", new FormatFunctions(Fun.FORMAT_DATE, 2));
        FunctionExpression.registerFunction("formatFloat", new FormatFunctions(Fun.FORMAT_FLOAT, 2));
        FunctionExpression.registerFunction("formatInt", new FormatFunctions(Fun.FORMAT_INT, 2));
    }

    private FormatFunctions(Fun f, int i) {
        super(i);
        this.mFun = f;
    }

    public double evaluate(Expression[] paraExps, Variables var) {
        return 0.0d;
    }

    public String evaluateStr(Expression[] paraExps, Variables var) {
        String format = paraExps[0].evaluateStr();
        if (format == null) {
            return null;
        }
        int i = AnonymousClass1.$SwitchMap$miui$maml$data$FormatFunctions$Fun[this.mFun.ordinal()];
        if (i == 1) {
            return DateTimeVariableUpdater.formatDate(format, (long) paraExps[1].evaluate());
        }
        if (i == 2) {
            try {
                return String.format(format, new Object[]{Double.valueOf(paraExps[1].evaluate())});
            } catch (IllegalFormatException e) {
            }
        } else if (i == 3) {
            try {
                return String.format(format, new Object[]{Integer.valueOf((int) paraExps[1].evaluate())});
            } catch (IllegalFormatException e2) {
            }
        }
        return null;
    }
}
