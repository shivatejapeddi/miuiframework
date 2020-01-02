package miui.maml.data;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import miui.maml.util.Utils;
import org.apache.miui.commons.lang3.ClassUtils;

public abstract class Expression {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "Expression";

    public static abstract class FunctionImpl {
        public int params;

        public abstract double evaluate(Expression[] expressionArr, Variables variables);

        public abstract String evaluateStr(Expression[] expressionArr, Variables variables);

        public FunctionImpl(int p) {
            this.params = p;
        }
    }

    /* renamed from: miui.maml.data.Expression$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$Expression$Ope = new int[Ope.values().length];

        static {
            $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType = new int[TokenType.values().length];
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.VAR_NUM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.VAR_STR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.NUM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.STR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.BRACKET_ROUND.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.BRACKET_SQUARE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.OPE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Tokenizer$TokenType[TokenType.FUN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.MIN.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.NOT.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.BIT_NOT.ordinal()] = 3;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.ADD.ordinal()] = 4;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.MUL.ordinal()] = 5;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.DIV.ordinal()] = 6;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.MOD.ordinal()] = 7;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.BIT_AND.ordinal()] = 8;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.BIT_OR.ordinal()] = 9;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.BIT_XOR.ordinal()] = 10;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.BIT_LSHIFT.ordinal()] = 11;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.BIT_RSHIFT.ordinal()] = 12;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.EQ.ordinal()] = 13;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.NEQ.ordinal()] = 14;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.AND.ordinal()] = 15;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.OR.ordinal()] = 16;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.GT.ordinal()] = 17;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.GE.ordinal()] = 18;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.LT.ordinal()] = 19;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$miui$maml$data$Expression$Ope[Ope.LE.ordinal()] = 20;
            } catch (NoSuchFieldError e28) {
            }
        }
    }

    static abstract class VariableExpression extends Expression {
        protected IndexedVariable mIndexedVar;
        protected String mName;

        public VariableExpression(Variables vars, String exp, boolean isNumber) {
            this.mName = exp;
            this.mIndexedVar = new IndexedVariable(this.mName, vars, isNumber);
        }

        public int getIndex() {
            return this.mIndexedVar.getIndex();
        }

        public int getVersion() {
            return this.mIndexedVar.getVersion();
        }

        public String getName() {
            return this.mName;
        }
    }

    static abstract class ArrayVariableExpression extends VariableExpression {
        protected Expression mIndexExp;

        public ArrayVariableExpression(Variables vars, String name, Expression indexExp) {
            super(vars, name, false);
            this.mIndexExp = indexExp;
        }

        public void accept(ExpressionVisitor v) {
            v.visit(this);
            this.mIndexExp.accept(v);
        }
    }

    static class BinaryExpression extends Expression {
        private Expression mExp1;
        private Expression mExp2;
        private Ope mOpe = Ope.INVALID;

        public BinaryExpression(Expression exp1, Expression exp2, Ope op) {
            this.mExp1 = exp1;
            this.mExp2 = exp2;
            this.mOpe = op;
            if (this.mOpe == Ope.INVALID) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("BinaryExpression: invalid operator:");
                stringBuilder.append(op);
                Log.e(Expression.LOG_TAG, stringBuilder.toString());
            }
        }

        public double evaluate() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$data$Expression$Ope[this.mOpe.ordinal()];
            if (i == 1) {
                return this.mExp1.evaluate() - this.mExp2.evaluate();
            }
            double d = 1.0d;
            switch (i) {
                case 4:
                    return this.mExp1.evaluate() + this.mExp2.evaluate();
                case 5:
                    return this.mExp1.evaluate() * this.mExp2.evaluate();
                case 6:
                    return this.mExp1.evaluate() / this.mExp2.evaluate();
                case 7:
                    return this.mExp1.evaluate() % this.mExp2.evaluate();
                case 8:
                    return (double) (((long) this.mExp1.evaluate()) & ((long) this.mExp2.evaluate()));
                case 9:
                    return (double) (((long) this.mExp1.evaluate()) | ((long) this.mExp2.evaluate()));
                case 10:
                    return (double) (((long) this.mExp1.evaluate()) ^ ((long) this.mExp2.evaluate()));
                case 11:
                    return (double) (((long) this.mExp1.evaluate()) << ((int) ((long) this.mExp2.evaluate())));
                case 12:
                    return (double) (((long) this.mExp1.evaluate()) >> ((int) ((long) this.mExp2.evaluate())));
                case 13:
                    if (this.mExp1.evaluate() != this.mExp2.evaluate()) {
                        d = 0.0d;
                    }
                    return d;
                case 14:
                    if (this.mExp1.evaluate() == this.mExp2.evaluate()) {
                        d = 0.0d;
                    }
                    return d;
                case 15:
                    if (this.mExp1.evaluate() <= 0.0d || this.mExp2.evaluate() <= 0.0d) {
                        d = 0.0d;
                    }
                    return d;
                case 16:
                    if (this.mExp1.evaluate() <= 0.0d && this.mExp2.evaluate() <= 0.0d) {
                        d = 0.0d;
                    }
                    return d;
                case 17:
                    if (this.mExp1.evaluate() <= this.mExp2.evaluate()) {
                        d = 0.0d;
                    }
                    return d;
                case 18:
                    if (this.mExp1.evaluate() < this.mExp2.evaluate()) {
                        d = 0.0d;
                    }
                    return d;
                case 19:
                    if (this.mExp1.evaluate() >= this.mExp2.evaluate()) {
                        d = 0.0d;
                    }
                    return d;
                case 20:
                    if (this.mExp1.evaluate() > this.mExp2.evaluate()) {
                        d = 0.0d;
                    }
                    return d;
                default:
                    Log.e(Expression.LOG_TAG, "fail to evalute BinaryExpression, invalid operator");
                    return 0.0d;
            }
        }

        public BigDecimal preciseEvaluate() {
            BigDecimal bigDecimal = null;
            if (this.mOpe != Ope.INVALID) {
                BigDecimal num1 = this.mExp1.preciseEvaluate();
                BigDecimal num2 = this.mExp2.preciseEvaluate();
                if (!(num1 == null || num2 == null)) {
                    int i = AnonymousClass1.$SwitchMap$miui$maml$data$Expression$Ope[this.mOpe.ordinal()];
                    if (i == 1) {
                        return num1.subtract(num2);
                    }
                    if (i == 4) {
                        return num1.add(num2);
                    }
                    if (i == 5) {
                        return num1.multiply(num2);
                    }
                    if (i == 6) {
                        try {
                            bigDecimal = num1.divide(num2, MathContext.DECIMAL128);
                            return bigDecimal;
                        } catch (Exception e) {
                            return bigDecimal;
                        }
                    } else if (i == 7) {
                        try {
                            bigDecimal = num1.remainder(num2);
                            return bigDecimal;
                        } catch (Exception e2) {
                            return bigDecimal;
                        }
                    }
                }
            }
            Log.e(Expression.LOG_TAG, "fail to PRECISE evalute BinaryExpression, invalid operator");
            return null;
        }

        public boolean isNull() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$data$Expression$Ope[this.mOpe.ordinal()];
            boolean z = false;
            if (i == 1 || i == 4) {
                if (this.mExp1.isNull() && this.mExp2.isNull()) {
                    z = true;
                }
                return z;
            }
            if (!(i == 5 || i == 6 || i == 7)) {
                switch (i) {
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        break;
                    default:
                        return true;
                }
            }
            if (this.mExp1.isNull() || this.mExp2.isNull()) {
                z = true;
            }
            return z;
        }

        public String evaluateStr() {
            String str1 = this.mExp1.evaluateStr();
            String str2 = this.mExp2.evaluateStr();
            if (AnonymousClass1.$SwitchMap$miui$maml$data$Expression$Ope[this.mOpe.ordinal()] != 4) {
                Log.e(Expression.LOG_TAG, "fail to evalute string BinaryExpression, invalid operator");
                return null;
            } else if (str1 == null && str2 == null) {
                return null;
            } else {
                if (str1 == null) {
                    return str2;
                }
                if (str2 == null) {
                    return str1;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str1);
                stringBuilder.append(str2);
                return stringBuilder.toString();
            }
        }

        public void accept(ExpressionVisitor v) {
            v.visit(this);
            this.mExp1.accept(v);
            this.mExp2.accept(v);
        }
    }

    public static class FunctionExpression extends Expression {
        protected static HashMap<String, FunctionImpl> sFunMap = new HashMap();
        private FunctionImpl mFun;
        private String mFunName;
        private Expression[] mParaExps;
        private Variables mVariables;

        static {
            FunctionsLoader.load();
        }

        public FunctionExpression(Variables vars, Expression[] params, String fun) throws Exception {
            this.mVariables = vars;
            this.mParaExps = params;
            this.mFunName = fun;
            parseFunction(fun);
        }

        public static void registerFunction(String f, FunctionImpl d) {
            if (((FunctionImpl) sFunMap.put(f, d)) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("duplicated function name registation: ");
                stringBuilder.append(f);
                Log.w(Expression.LOG_TAG, stringBuilder.toString());
            }
        }

        public static void removeFunction(String f, FunctionImpl d) {
            sFunMap.remove(f);
        }

        private void parseFunction(String fun) throws Exception {
            FunctionImpl fd = (FunctionImpl) sFunMap.get(fun);
            boolean z = true;
            boolean z2 = fd != null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid function:");
            stringBuilder.append(fun);
            Utils.asserts(z2, stringBuilder.toString());
            this.mFun = fd;
            if (this.mParaExps.length < fd.params) {
                z = false;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("parameters count not matching for function: ");
            stringBuilder2.append(fun);
            Utils.asserts(z, stringBuilder2.toString());
        }

        public double evaluate() {
            return this.mFun.evaluate(this.mParaExps, this.mVariables);
        }

        public String evaluateStr() {
            return this.mFun.evaluateStr(this.mParaExps, this.mVariables);
        }

        public void accept(ExpressionVisitor v) {
            v.visit(this);
            int i = 0;
            while (true) {
                Expression[] expressionArr = this.mParaExps;
                if (i < expressionArr.length) {
                    expressionArr[i].accept(v);
                    i++;
                } else {
                    return;
                }
            }
        }

        public String getFunName() {
            return this.mFunName;
        }
    }

    static class NumberArrayVariableExpression extends ArrayVariableExpression {
        public NumberArrayVariableExpression(Variables vars, String name, Expression indexExp) {
            super(vars, name, indexExp);
        }

        public double evaluate() {
            return this.mIndexedVar.getArrDouble((int) this.mIndexExp.evaluate());
        }

        public String evaluateStr() {
            return Utils.doubleToString(evaluate());
        }

        public boolean isNull() {
            return this.mIndexedVar.isNull((int) this.mIndexExp.evaluate());
        }
    }

    public static class NumberExpression extends Expression {
        private String mString;
        private double mValue;

        public NumberExpression(double num) {
            this.mValue = num;
        }

        public NumberExpression(String num) {
            String str = Expression.LOG_TAG;
            if (num == null) {
                Log.e(str, "invalid NumberExpression: null");
                return;
            }
            try {
                if (num.length() <= 2 || num.indexOf("0x") != 0) {
                    this.mValue = Double.parseDouble(num);
                } else {
                    this.mValue = (double) Long.parseLong(num.substring(2), 16);
                }
            } catch (NumberFormatException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid NumberExpression:");
                stringBuilder.append(num);
                Log.e(str, stringBuilder.toString());
            }
        }

        public void setValue(double value) {
            this.mValue = value;
        }

        public double evaluate() {
            return this.mValue;
        }

        public String evaluateStr() {
            if (this.mString == null) {
                this.mString = Utils.doubleToString(this.mValue);
            }
            return this.mString;
        }
    }

    static class NumberVariableExpression extends VariableExpression {
        public NumberVariableExpression(Variables vars, String exp) {
            super(vars, exp, true);
        }

        public double evaluate() {
            return this.mIndexedVar.getDouble();
        }

        public String evaluateStr() {
            return Utils.doubleToString(evaluate());
        }

        public boolean isNull() {
            return this.mIndexedVar.isNull();
        }
    }

    private enum Ope {
        ADD,
        MIN,
        MUL,
        DIV,
        MOD,
        BIT_AND,
        BIT_OR,
        BIT_XOR,
        BIT_NOT,
        BIT_LSHIFT,
        BIT_RSHIFT,
        NOT,
        EQ,
        NEQ,
        AND,
        OR,
        GT,
        GE,
        LT,
        LE,
        INVALID
    }

    private static class OpeInfo {
        private static final int OPE_SIZE = mOpes.length;
        private static final int[] mOpePar = new int[]{2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2};
        private static final int[] mOpePriority = new int[]{4, 4, 3, 3, 3, 8, 9, 10, 2, 5, 5, 2, 7, 7, 11, 12, 6, 6, 6, 6};
        private static final String[] mOpes = new String[]{"+", "-", "*", "/", "%", "&", "|", "^", "~", "{{", "}}", "!", "==", "!=", "**", "||", "}", "}=", "{", "{="};
        public int participants;
        public int priority;
        public String str;
        public boolean unary;

        public static class Parser {
            private int[] mFlags = new int[OpeInfo.OPE_SIZE];
            private int mMatch;
            private int mStep;

            public boolean accept(char c, boolean start) {
                if (start) {
                    for (int i = 0; i < OpeInfo.OPE_SIZE; i++) {
                        this.mFlags[i] = 0;
                    }
                    this.mStep = 0;
                    this.mMatch = -1;
                }
                boolean match = false;
                int i2 = 0;
                while (true) {
                    boolean end = true;
                    if (i2 >= OpeInfo.OPE_SIZE) {
                        break;
                    }
                    if (this.mFlags[i2] != -1) {
                        String ope = OpeInfo.mOpes[i2];
                        int length = ope.length();
                        int i3 = this.mStep;
                        if (length <= i3 || ope.charAt(i3) != c) {
                            this.mFlags[i2] = -1;
                        } else {
                            if (this.mStep != ope.length() - 1) {
                                end = false;
                            }
                            this.mFlags[i2] = 0;
                            match = true;
                            if (end) {
                                this.mMatch = i2;
                            }
                        }
                    }
                    i2++;
                }
                if (match) {
                    this.mStep++;
                }
                return match;
            }

            public Ope getMatch() {
                return this.mMatch == -1 ? Ope.INVALID : Ope.values()[this.mMatch];
            }
        }

        private OpeInfo() {
        }

        public static OpeInfo getOpeInfo(int index) {
            OpeInfo info = new OpeInfo();
            info.priority = mOpePriority[index];
            info.participants = mOpePar[index];
            info.str = mOpes[index];
            return info;
        }
    }

    static class StringArrayVariableExpression extends ArrayVariableExpression {
        public StringArrayVariableExpression(Variables vars, String name, Expression indexExp) {
            super(vars, name, indexExp);
        }

        public double evaluate() {
            String str = evaluateStr();
            double d = 0.0d;
            if (str == null) {
                return 0.0d;
            }
            try {
                d = Double.parseDouble(str);
                return d;
            } catch (NumberFormatException e) {
                return d;
            }
        }

        public String evaluateStr() {
            return this.mIndexedVar.getArrString((int) this.mIndexExp.evaluate());
        }

        public boolean isNull() {
            return this.mIndexedVar.isNull((int) this.mIndexExp.evaluate());
        }
    }

    static class StringExpression extends Expression {
        private String mValue;

        public StringExpression(String str) {
            this.mValue = str;
        }

        public double evaluate() {
            try {
                return Double.parseDouble(this.mValue);
            } catch (NumberFormatException e) {
                return 0.0d;
            }
        }

        public String evaluateStr() {
            return this.mValue;
        }
    }

    static class StringVariableExpression extends VariableExpression {
        public StringVariableExpression(Variables vars, String exp) {
            super(vars, exp, false);
        }

        public double evaluate() {
            String str = evaluateStr();
            double d = 0.0d;
            if (str == null) {
                return 0.0d;
            }
            try {
                d = Double.parseDouble(str);
                return d;
            } catch (NumberFormatException e) {
                return d;
            }
        }

        public String evaluateStr() {
            return this.mIndexedVar.getString();
        }

        public boolean isNull() {
            return this.mIndexedVar.isNull();
        }
    }

    private static class Tokenizer {
        private static final int BRACKET_MODE_NONE = 0;
        private static final int BRACKET_MODE_ROUND = 1;
        private static final int BRACKET_MODE_SQUARE = 2;
        private Parser mOpeParser = new Parser();
        private int mPos;
        private String mString;

        public static class Token {
            public OpeInfo info;
            public Ope op = Ope.INVALID;
            public String token;
            public TokenType type = TokenType.INVALID;

            public Token(TokenType t, String s) {
                this.type = t;
                this.token = s;
            }

            public Token(TokenType t, String s, Ope o) {
                this.type = t;
                this.token = s;
                this.op = o;
                this.info = OpeInfo.getOpeInfo(this.op.ordinal());
            }
        }

        public enum TokenType {
            INVALID,
            VAR_NUM,
            VAR_STR,
            NUM,
            STR,
            OPE,
            FUN,
            BRACKET_ROUND,
            BRACKET_SQUARE
        }

        public Tokenizer(String str) {
            this.mString = str;
            reset();
        }

        public void reset() {
            this.mPos = 0;
        }

        public Token getToken() {
            String str;
            char c;
            int bracketCount = 0;
            int bracketStart = -1;
            int bracketMode = 0;
            char bracket1 = 0;
            char bracket2 = 0;
            TokenType type = TokenType.INVALID;
            int len = this.mString.length();
            int i = this.mPos;
            while (true) {
                str = Expression.LOG_TAG;
                int i2;
                if (i < len) {
                    c = this.mString.charAt(i);
                    if (bracketMode == 0) {
                        if (c == '#') {
                            break;
                        } else if (c == '@') {
                            i2 = bracketStart;
                            break;
                        } else {
                            char c2 = DateFormat.QUOTE;
                            if (c == DateFormat.QUOTE) {
                                boolean slash = false;
                                int j = i + 1;
                                while (j < len) {
                                    char cc = this.mString.charAt(j);
                                    if (!slash && cc == c2) {
                                        break;
                                    }
                                    slash = cc == '\\';
                                    j++;
                                    c2 = DateFormat.QUOTE;
                                }
                                this.mPos = j + 1;
                                return new Token(TokenType.STR, this.mString.substring(i + 1, j).replace("\\'", "'"));
                            }
                            i2 = bracketStart;
                            if (c == '(') {
                                bracketMode = 1;
                            } else if (c == '[') {
                                bracketMode = 2;
                            } else if (Expression.isDigitCharStart(c) != 0) {
                                bracketStart = i + 1;
                                if (this.mString.charAt(i) == '0' && bracketStart < len && this.mString.charAt(bracketStart) == StateProperty.TARGET_X) {
                                    bracketStart++;
                                }
                                while (bracketStart < len && Expression.isDigitCharRest(this.mString.charAt(bracketStart))) {
                                    bracketStart++;
                                }
                                this.mPos = bracketStart;
                                return new Token(TokenType.NUM, this.mString.substring(i, bracketStart));
                            } else if (Expression.isFunctionCharStart(c) != 0) {
                                bracketStart = i + 1;
                                while (bracketStart < len && Expression.isFunctionCharRest(this.mString.charAt(bracketStart))) {
                                    bracketStart++;
                                }
                                this.mPos = bracketStart;
                                return new Token(TokenType.FUN, this.mString.substring(i, bracketStart));
                            } else if (this.mOpeParser.accept(c, true) != 0) {
                                bracketStart = i + 1;
                                while (bracketStart < len && this.mOpeParser.accept(this.mString.charAt(bracketStart), false)) {
                                    bracketStart++;
                                }
                                Ope ope = this.mOpeParser.getMatch();
                                if (ope != Ope.INVALID) {
                                    this.mPos = bracketStart;
                                    return new Token(TokenType.OPE, this.mString.substring(i, this.mPos), ope);
                                }
                            }
                        }
                    }
                    i2 = bracketStart;
                    if (bracketMode != 0) {
                        if (bracketCount == 0) {
                            if (bracketMode == 1) {
                                type = TokenType.BRACKET_ROUND;
                                bracket2 = ')';
                                bracket1 = '(';
                            } else if (bracketMode == 2) {
                                type = TokenType.BRACKET_SQUARE;
                                bracket2 = ']';
                                bracket1 = '[';
                            }
                            bracketStart = i + 1;
                        } else {
                            bracketStart = i2;
                        }
                        if (c == bracket1) {
                            bracketCount++;
                        } else if (c == bracket2) {
                            bracketCount--;
                            if (bracketCount == 0) {
                                this.mPos = i + 1;
                                return new Token(type, this.mString.substring(bracketStart, i));
                            }
                        } else {
                            continue;
                        }
                    } else {
                        bracketStart = i2;
                    }
                    i++;
                } else {
                    i2 = bracketStart;
                    if (bracketCount != 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("mismatched bracket:");
                        stringBuilder.append(this.mString);
                        Log.e(str, stringBuilder.toString());
                    }
                    return null;
                }
            }
            bracketStart = i + 1;
            while (bracketStart < len && Expression.isVariableChar(this.mString.charAt(bracketStart))) {
                bracketStart++;
            }
            if (bracketStart == i + 1) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("invalid variable name:");
                stringBuilder2.append(this.mString);
                Log.e(str, stringBuilder2.toString());
                return null;
            }
            this.mPos = bracketStart;
            return new Token(c == '#' ? TokenType.VAR_NUM : TokenType.VAR_STR, this.mString.substring(i + 1, bracketStart));
        }
    }

    static class UnaryExpression extends Expression {
        private Expression mExp;
        private Ope mOpe = Ope.INVALID;

        public UnaryExpression(Expression exp, Ope op) {
            this.mExp = exp;
            this.mOpe = op;
            if (this.mOpe == Ope.INVALID) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UnaryExpression: invalid operator:");
                stringBuilder.append(op);
                Log.e(Expression.LOG_TAG, stringBuilder.toString());
            }
        }

        public double evaluate() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$data$Expression$Ope[this.mOpe.ordinal()];
            double d = 0.0d;
            if (i == 1) {
                return 0.0d - this.mExp.evaluate();
            }
            if (i == 2) {
                if (this.mExp.evaluate() <= 0.0d) {
                    d = 1.0d;
                }
                return d;
            } else if (i == 3) {
                return (double) (~((int) this.mExp.evaluate()));
            } else {
                Log.e(Expression.LOG_TAG, "fail to evalute UnaryExpression, invalid operator");
                return this.mExp.evaluate();
            }
        }

        public boolean isNull() {
            return this.mExp.isNull();
        }

        public String evaluateStr() {
            return Utils.doubleToString(evaluate());
        }

        public void accept(ExpressionVisitor v) {
            v.visit(this);
            this.mExp.accept(v);
        }
    }

    public abstract double evaluate();

    private static boolean isVariableChar(char c) {
        return (c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) || ((c >= DateFormat.CAPITAL_AM_PM && c <= 'Z') || c == '_' || c == ClassUtils.PACKAGE_SEPARATOR_CHAR || (c >= '0' && c <= '9'));
    }

    private static boolean isDigitCharStart(char c) {
        return (c >= '0' && c <= '9') || c == ClassUtils.PACKAGE_SEPARATOR_CHAR;
    }

    private static boolean isDigitCharRest(char c) {
        return (c >= '0' && c <= '9') || ((c >= DateFormat.AM_PM && c <= 'f') || ((c >= DateFormat.CAPITAL_AM_PM && c <= 'F') || c == ClassUtils.PACKAGE_SEPARATOR_CHAR));
    }

    private static boolean isFunctionCharStart(char c) {
        return (c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) || (c >= DateFormat.CAPITAL_AM_PM && c <= 'Z');
    }

    private static boolean isFunctionCharRest(char c) {
        return isFunctionCharStart(c) || c == '_' || (c >= '0' && c <= '9');
    }

    public static Expression[] buildMultiple(Variables vars, String exp) {
        if (TextUtils.isEmpty(exp)) {
            return null;
        }
        Expression[] exps = buildMultipleInner(vars, exp);
        Expression[] roots = new Expression[exps.length];
        for (int i = 0; i < exps.length; i++) {
            Expression expression = exps[i];
            if (expression == null || (expression instanceof NumberExpression) || (expression instanceof StringExpression)) {
                roots[i] = expression;
            } else {
                roots[i] = new RootExpression(vars, expression);
            }
        }
        return roots;
    }

    private static Expression[] buildMultipleInner(Variables vars, String exp) {
        int bracketCount = 0;
        boolean inApostrophe = false;
        int start = 0;
        ArrayList<Expression> exps = new ArrayList();
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (!inApostrophe) {
                if (c == ',' && bracketCount == 0) {
                    exps.add(buildInner(vars, exp.substring(start, i)));
                    start = i + 1;
                } else if (c == '(') {
                    bracketCount++;
                } else if (c == ')') {
                    bracketCount--;
                }
            }
            if (c == DateFormat.QUOTE) {
                inApostrophe = !inApostrophe;
            }
        }
        if (start < exp.length()) {
            exps.add(buildInner(vars, exp.substring(start)));
        }
        return (Expression[]) exps.toArray(new Expression[exps.size()]);
    }

    public static Expression build(Variables vars, String exp) {
        Expression ex = buildInner(vars, exp);
        return ex == null ? null : new RootExpression(vars, ex);
    }

    private static Expression buildInner(Variables vars, String exp) {
        if (TextUtils.isEmpty(exp.trim())) {
            return null;
        }
        Tokenizer tk = new Tokenizer(exp);
        Token preToken = null;
        Stack<Token> opeStack = new Stack();
        Stack<Expression> expStack = new Stack();
        while (true) {
            Token token = tk.getToken();
            Token token2 = token;
            String str = LOG_TAG;
            boolean z = true;
            StringBuilder stringBuilder;
            if (token != null) {
                switch (token2.type) {
                    case VAR_NUM:
                    case VAR_STR:
                    case NUM:
                    case STR:
                    case BRACKET_ROUND:
                    case BRACKET_SQUARE:
                        Expression newExp = null;
                        StringBuilder stringBuilder2;
                        switch (token2.type) {
                            case VAR_NUM:
                                newExp = new NumberVariableExpression(vars, token2.token);
                                break;
                            case VAR_STR:
                                newExp = new StringVariableExpression(vars, token2.token);
                                break;
                            case NUM:
                                if (!(!opeStack.empty() && ((Token) opeStack.peek()).op == Ope.MIN && ((Token) opeStack.peek()).info.unary)) {
                                    z = false;
                                }
                                boolean minus = z;
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(minus ? "-" : "");
                                stringBuilder2.append(token2.token);
                                newExp = new NumberExpression(stringBuilder2.toString());
                                if (minus) {
                                    opeStack.pop();
                                    break;
                                }
                                break;
                            case STR:
                                newExp = new StringExpression(token2.token);
                                break;
                            case BRACKET_ROUND:
                                newExp = buildBracket(vars, token2, opeStack);
                                if (newExp == null) {
                                    return null;
                                }
                                break;
                            case BRACKET_SQUARE:
                                if (expStack.size() < 1) {
                                    StringBuilder stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("fail to buid: no array name before []:");
                                    stringBuilder3.append(exp);
                                    Log.e(str, stringBuilder3.toString());
                                    return null;
                                }
                                Expression lastExp = (Expression) expStack.pop();
                                if (lastExp instanceof VariableExpression) {
                                    Expression indexExp = buildInner(vars, token2.token);
                                    if (indexExp == null) {
                                        StringBuilder stringBuilder4 = new StringBuilder();
                                        stringBuilder4.append("fail to buid: no index expression in []:");
                                        stringBuilder4.append(exp);
                                        Log.e(str, stringBuilder4.toString());
                                        return null;
                                    }
                                    str = ((VariableExpression) lastExp).getName();
                                    if (lastExp instanceof NumberVariableExpression) {
                                        newExp = new NumberArrayVariableExpression(vars, str, indexExp);
                                    } else if (lastExp instanceof StringVariableExpression) {
                                        newExp = new StringArrayVariableExpression(vars, str, indexExp);
                                    }
                                } else {
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("fail to buid: the expression before [] is not a variable:");
                                    stringBuilder2.append(exp);
                                    Log.e(str, stringBuilder2.toString());
                                }
                                if (newExp == null) {
                                    return null;
                                }
                                break;
                        }
                        while (!opeStack.empty() && ((Token) opeStack.peek()).info != null && ((Token) opeStack.peek()).info.unary) {
                            newExp = new UnaryExpression(newExp, ((Token) opeStack.pop()).op);
                        }
                        expStack.push(newExp);
                        break;
                    case OPE:
                        if (token2.info.participants != 1 || (preToken != null && preToken.type != TokenType.OPE)) {
                            while (opeStack.size() > 0 && ((Token) opeStack.peek()).type == TokenType.OPE && ((Token) opeStack.peek()).info.priority - token2.info.priority <= 0) {
                                if (expStack.size() < 2) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("fail to buid: invalid operator position:");
                                    stringBuilder.append(exp);
                                    Log.e(str, stringBuilder.toString());
                                    return null;
                                }
                                expStack.push(new BinaryExpression((Expression) expStack.pop(), (Expression) expStack.pop(), ((Token) opeStack.pop()).op));
                            }
                            opeStack.push(token2);
                            break;
                        }
                        token2.info.unary = true;
                        opeStack.push(token2);
                        break;
                    case FUN:
                        opeStack.push(token2);
                        break;
                    default:
                        break;
                }
                preToken = token2;
            } else if (expStack.size() != opeStack.size() + 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("fail to buid: invalid expression:");
                stringBuilder.append(exp);
                Log.e(str, stringBuilder.toString());
                return null;
            } else {
                Expression exp2 = (Expression) expStack.pop();
                while (opeStack.size() > 0) {
                    exp2 = new BinaryExpression((Expression) expStack.pop(), exp2, ((Token) opeStack.pop()).op);
                }
                return exp2;
            }
        }
    }

    private static Expression buildBracket(Variables vars, Token token, Stack<Token> opeStack) {
        Expression[] newExps = buildMultipleInner(vars, token.token);
        boolean checkParams = checkParams(newExps);
        String str = LOG_TAG;
        StringBuilder stringBuilder;
        if (checkParams) {
            try {
                if (!opeStack.isEmpty() && ((Token) opeStack.peek()).type == TokenType.FUN) {
                    return new FunctionExpression(vars, newExps, ((Token) opeStack.pop()).token);
                }
                if (newExps.length == 1) {
                    return newExps[0];
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("fail to buid: multiple expressions in brackets, but seems no function presents:");
                stringBuilder.append(token.token);
                Log.e(str, stringBuilder.toString());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(str, e.toString());
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("invalid expressions: ");
            stringBuilder.append(token.token);
            Log.e(str, stringBuilder.toString());
            return null;
        }
    }

    private static boolean checkParams(Expression[] params) {
        for (Expression expression : params) {
            if (expression == null) {
                return false;
            }
        }
        return true;
    }

    public BigDecimal preciseEvaluate() {
        try {
            return BigDecimal.valueOf(evaluate());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isNull() {
        return false;
    }

    public String evaluateStr() {
        return null;
    }

    public void accept(ExpressionVisitor v) {
        v.visit(this);
    }
}
