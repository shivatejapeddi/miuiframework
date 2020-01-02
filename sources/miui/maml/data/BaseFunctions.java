package miui.maml.data;

import android.app.slice.Slice;
import android.app.slice.SliceItem;
import android.text.TextUtils;
import android.util.Log;
import java.math.BigDecimal;
import miui.maml.data.Expression.FunctionExpression;
import miui.maml.data.Expression.FunctionImpl;
import miui.maml.util.Utils;
import miui.util.HashUtils;

public class BaseFunctions extends FunctionImpl {
    private static final String LOG_TAG = "Expression";
    private Fun fun;
    private Expression mEvalExp;
    private String mEvalExpStr;

    /* renamed from: miui.maml.data.BaseFunctions$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$BaseFunctions$Fun = new int[Fun.values().length];

        static {
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.RAND.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.SIN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.COS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.TAN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.ASIN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.ACOS.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.ATAN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.SINH.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.COSH.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.SQRT.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.ABS.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.LEN.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.EVAL.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.PRECISE_EVAL.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.ROUND.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.INT.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.NUM.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.ISNULL.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.NOT.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.MIN.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.MAX.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.POW.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.LOG.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.LOG10.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.DIGIT.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.EQ.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.NE.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.GE.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.GT.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.LE.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.LT.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.IFELSE.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.AND.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.OR.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.EQS.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.SUBSTR.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$miui$maml$data$BaseFunctions$Fun[Fun.HASH.ordinal()] = 37;
            } catch (NoSuchFieldError e37) {
            }
        }
    }

    private enum Fun {
        INVALID,
        RAND,
        SIN,
        COS,
        TAN,
        ASIN,
        ACOS,
        ATAN,
        SINH,
        COSH,
        SQRT,
        ABS,
        LEN,
        EVAL,
        PRECISE_EVAL,
        ROUND,
        INT,
        NUM,
        MIN,
        MAX,
        POW,
        LOG,
        LOG10,
        DIGIT,
        EQ,
        NE,
        GE,
        GT,
        LE,
        LT,
        ISNULL,
        NOT,
        IFELSE,
        AND,
        OR,
        EQS,
        SUBSTR,
        HASH
    }

    private static class NullObjFunction extends FunctionImpl {
        private String mObjName;
        private int mVarIndex = -1;

        public NullObjFunction() {
            super(1);
        }

        public double evaluate(Expression[] mParaExps, Variables vars) {
            String str = mParaExps[0].evaluateStr();
            if (str != this.mObjName) {
                this.mObjName = str;
                if (TextUtils.isEmpty(this.mObjName) || !vars.existsObj(this.mObjName)) {
                    this.mVarIndex = -1;
                } else {
                    this.mVarIndex = vars.registerVariable(this.mObjName);
                }
            }
            int i = this.mVarIndex;
            double d = 1.0d;
            if (i == -1) {
                return 1.0d;
            }
            if (vars.get(i) != null) {
                d = 0.0d;
            }
            return d;
        }

        public String evaluateStr(Expression[] mParaExps, Variables vars) {
            return null;
        }
    }

    public static void load() {
        FunctionExpression.registerFunction("rand", new BaseFunctions(Fun.RAND, 0));
        FunctionExpression.registerFunction("sin", new BaseFunctions(Fun.SIN, 1));
        FunctionExpression.registerFunction("cos", new BaseFunctions(Fun.COS, 1));
        FunctionExpression.registerFunction("tan", new BaseFunctions(Fun.TAN, 1));
        FunctionExpression.registerFunction("asin", new BaseFunctions(Fun.ASIN, 1));
        FunctionExpression.registerFunction("acos", new BaseFunctions(Fun.ACOS, 1));
        FunctionExpression.registerFunction("atan", new BaseFunctions(Fun.ATAN, 1));
        FunctionExpression.registerFunction("sinh", new BaseFunctions(Fun.SINH, 1));
        FunctionExpression.registerFunction("cosh", new BaseFunctions(Fun.COSH, 1));
        FunctionExpression.registerFunction("sqrt", new BaseFunctions(Fun.SQRT, 1));
        FunctionExpression.registerFunction("abs", new BaseFunctions(Fun.ABS, 1));
        FunctionExpression.registerFunction("len", new BaseFunctions(Fun.LEN, 1));
        FunctionExpression.registerFunction("eval", new BaseFunctions(Fun.EVAL, 1));
        FunctionExpression.registerFunction("preciseeval", new BaseFunctions(Fun.PRECISE_EVAL, 2));
        FunctionExpression.registerFunction("round", new BaseFunctions(Fun.ROUND, 1));
        FunctionExpression.registerFunction(SliceItem.FORMAT_INT, new BaseFunctions(Fun.INT, 1));
        FunctionExpression.registerFunction("num", new BaseFunctions(Fun.NUM, 1));
        FunctionExpression.registerFunction("isnull", new BaseFunctions(Fun.ISNULL, 1));
        FunctionExpression.registerFunction("not", new BaseFunctions(Fun.NOT, 1));
        FunctionExpression.registerFunction("min", new BaseFunctions(Fun.MIN, 2));
        FunctionExpression.registerFunction(Slice.SUBTYPE_MAX, new BaseFunctions(Fun.MAX, 2));
        FunctionExpression.registerFunction("pow", new BaseFunctions(Fun.POW, 2));
        FunctionExpression.registerFunction("log", new BaseFunctions(Fun.LOG, 1));
        FunctionExpression.registerFunction("log10", new BaseFunctions(Fun.LOG10, 1));
        FunctionExpression.registerFunction("digit", new BaseFunctions(Fun.DIGIT, 2));
        FunctionExpression.registerFunction("eq", new BaseFunctions(Fun.EQ, 2));
        FunctionExpression.registerFunction("ne", new BaseFunctions(Fun.NE, 2));
        FunctionExpression.registerFunction("ge", new BaseFunctions(Fun.GE, 2));
        FunctionExpression.registerFunction("gt", new BaseFunctions(Fun.GT, 2));
        FunctionExpression.registerFunction("le", new BaseFunctions(Fun.LE, 2));
        FunctionExpression.registerFunction("lt", new BaseFunctions(Fun.LT, 2));
        FunctionExpression.registerFunction("ifelse", new BaseFunctions(Fun.IFELSE, 3));
        FunctionExpression.registerFunction("and", new BaseFunctions(Fun.AND, 2));
        FunctionExpression.registerFunction("or", new BaseFunctions(Fun.OR, 2));
        FunctionExpression.registerFunction("eqs", new BaseFunctions(Fun.EQS, 2));
        FunctionExpression.registerFunction("substr", new BaseFunctions(Fun.SUBSTR, 2));
        FunctionExpression.registerFunction("hash", new BaseFunctions(Fun.HASH, 2));
        FunctionExpression.registerFunction("nullobj", new NullObjFunction());
    }

    private BaseFunctions(Fun f, int i) {
        super(i);
        this.fun = f;
    }

    public double evaluate(Expression[] mParaExps, Variables vars) {
        if (AnonymousClass1.$SwitchMap$miui$maml$data$BaseFunctions$Fun[this.fun.ordinal()] == 1) {
            return Math.random();
        }
        double value0 = mParaExps[0].evaluate();
        int i = AnonymousClass1.$SwitchMap$miui$maml$data$BaseFunctions$Fun[this.fun.ordinal()];
        String str = LOG_TAG;
        double d = 1.0d;
        double d2 = 0.0d;
        String str2;
        Expression expression;
        int setScale;
        int len;
        switch (i) {
            case 2:
                return Math.sin(value0);
            case 3:
                return Math.cos(value0);
            case 4:
                return Math.tan(value0);
            case 5:
                return Math.asin(value0);
            case 6:
                return Math.acos(value0);
            case 7:
                return Math.atan(value0);
            case 8:
                return Math.sinh(value0);
            case 9:
                return Math.cosh(value0);
            case 10:
                return Math.sqrt(value0);
            case 11:
                return Math.abs(value0);
            case 12:
                str2 = mParaExps[0].evaluateStr();
                if (str2 == null) {
                    return 0.0d;
                }
                return (double) str2.length();
            case 13:
                str2 = mParaExps[0].evaluateStr();
                if (str2 == null) {
                    return 0.0d;
                }
                if (!str2.equals(this.mEvalExpStr)) {
                    this.mEvalExpStr = str2;
                    this.mEvalExp = Expression.build(vars, this.mEvalExpStr);
                }
                Expression expression2 = this.mEvalExp;
                if (expression2 != null) {
                    d2 = expression2.evaluate();
                }
                return d2;
            case 14:
                str2 = mParaExps[0].evaluateStr();
                if (str2 == null) {
                    return 0.0d;
                }
                if (!str2.equals(this.mEvalExpStr)) {
                    this.mEvalExpStr = str2;
                    this.mEvalExp = Expression.build(vars, this.mEvalExpStr);
                }
                expression = this.mEvalExp;
                BigDecimal result = expression != null ? expression.preciseEvaluate() : null;
                if (result == null) {
                    return Double.NaN;
                }
                int scale = result.scale();
                setScale = (int) mParaExps[1].evaluate();
                if (setScale > 0 && scale > setScale) {
                    result = result.setScale(setScale, 4);
                }
                return result.doubleValue();
            case 15:
                return (double) Math.round(value0);
            case 16:
                return (double) ((int) ((long) value0));
            case 17:
                return value0;
            case 18:
                if (!mParaExps[0].isNull()) {
                    d = 0.0d;
                }
                return d;
            case 19:
                if (value0 > 0.0d) {
                    d = 0.0d;
                }
                return d;
            case 20:
                return Math.min(value0, mParaExps[1].evaluate());
            case 21:
                return Math.max(value0, mParaExps[1].evaluate());
            case 22:
                return Math.pow(value0, mParaExps[1].evaluate());
            case 23:
                return Math.log(value0);
            case 24:
                return Math.log10(value0);
            case 25:
                return (double) digit((int) value0, (int) mParaExps[1].evaluate());
            case 26:
                if (value0 != mParaExps[1].evaluate()) {
                    d = 0.0d;
                }
                return d;
            case 27:
                if (value0 == mParaExps[1].evaluate()) {
                    d = 0.0d;
                }
                return d;
            case 28:
                if (value0 < mParaExps[1].evaluate()) {
                    d = 0.0d;
                }
                return d;
            case 29:
                if (value0 <= mParaExps[1].evaluate()) {
                    d = 0.0d;
                }
                return d;
            case 30:
                if (value0 > mParaExps[1].evaluate()) {
                    d = 0.0d;
                }
                return d;
            case 31:
                if (value0 >= mParaExps[1].evaluate()) {
                    d = 0.0d;
                }
                return d;
            case 32:
                len = mParaExps.length;
                if (len % 2 != 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("function parameter number should be 2*n+1: ");
                    stringBuilder.append(this.fun.toString());
                    Log.e(str, stringBuilder.toString());
                    return 0.0d;
                }
                for (i = 0; i < (len - 1) / 2; i++) {
                    if (mParaExps[i * 2].evaluate() > 0.0d) {
                        return mParaExps[(i * 2) + 1].evaluate();
                    }
                }
                return mParaExps[len - 1].evaluate();
            case 33:
                for (Expression expression3 : mParaExps) {
                    if (expression3.evaluate() <= 0.0d) {
                        return 0.0d;
                    }
                }
                return 1.0d;
            case 34:
                for (Expression expression32 : mParaExps) {
                    if (expression32.evaluate() > 0.0d) {
                        return 1.0d;
                    }
                }
                return 0.0d;
            case 35:
                if (!TextUtils.equals(mParaExps[0].evaluateStr(), mParaExps[1].evaluateStr())) {
                    d = 0.0d;
                }
                return d;
            case 36:
                return Utils.stringToDouble(evaluateStr(mParaExps, vars), 0.0d);
            default:
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("fail to evalute FunctionExpression, invalid function: ");
                stringBuilder2.append(this.fun.toString());
                Log.e(str, stringBuilder2.toString());
                return 0.0d;
        }
    }

    public String evaluateStr(Expression[] mParaExps, Variables vars) {
        int i = AnonymousClass1.$SwitchMap$miui$maml$data$BaseFunctions$Fun[this.fun.ordinal()];
        String str = null;
        String expStr;
        if (i == 13) {
            expStr = mParaExps[0].evaluateStr();
            if (expStr == null) {
                return null;
            }
            if (!expStr.equals(this.mEvalExpStr)) {
                this.mEvalExpStr = expStr;
                this.mEvalExp = Expression.build(vars, this.mEvalExpStr);
            }
            Expression expression = this.mEvalExp;
            if (expression != null) {
                str = expression.evaluateStr();
            }
            return str;
        } else if (i == 32) {
            i = mParaExps.length;
            if (i % 2 != 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("function parameter number should be 2*n+1: ");
                stringBuilder.append(this.fun.toString());
                Log.e(LOG_TAG, stringBuilder.toString());
                return null;
            }
            for (int i2 = 0; i2 < (i - 1) / 2; i2++) {
                if (mParaExps[i2 * 2].evaluate() > 0.0d) {
                    return mParaExps[(i2 * 2) + 1].evaluateStr();
                }
            }
            return mParaExps[i - 1].evaluateStr();
        } else if (i == 36) {
            expStr = mParaExps[0].evaluateStr();
            if (expStr == null) {
                return null;
            }
            int start = (int) mParaExps[1].evaluate();
            if (mParaExps.length < 3) {
                return expStr.substring(start);
            }
            try {
                int len = (int) mParaExps[2].evaluate();
                int strlen = expStr.length();
                if (len > strlen) {
                    len = strlen;
                }
                return expStr.substring(start, start + len);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        } else if (i != 37) {
            return Utils.doubleToString(evaluate(mParaExps, vars));
        } else {
            expStr = mParaExps[0].evaluateStr();
            String method = mParaExps[1].evaluateStr();
            if (expStr == null || method == null) {
                return null;
            }
            return HashUtils.getHash(expStr, method);
        }
    }

    private int digit(int number, int n) {
        int i = -1;
        if (n <= 0) {
            return -1;
        }
        if (number == 0 && n == 1) {
            return 0;
        }
        int i2 = 0;
        while (number > 0 && i2 < n - 1) {
            number /= 10;
            i2++;
        }
        if (number > 0) {
            i = number % 10;
        }
        return i;
    }
}
