package miui.maml.data;

import android.util.Log;
import java.util.regex.PatternSyntaxException;
import miui.maml.data.Expression.FunctionExpression;
import miui.maml.data.Expression.FunctionImpl;
import miui.maml.util.Utils;

public class StringFunctions extends FunctionImpl {
    private static final String LOG_TAG = "Expression";
    private final Fun mFun;

    private enum Fun {
        INVALID,
        STR_TOLOWER,
        STR_TOUPPER,
        STR_TRIM,
        STR_REPLACE,
        STR_REPLACEALL,
        STR_REPLACEFIRST,
        STR_CONTAINS,
        STR_STARTWITH,
        STR_ENDSWITH,
        STR_ISEMPTY,
        STR_MATCHES,
        STR_INDEXOF,
        STR_LASTINDEXOF
    }

    public static void load() {
        FunctionExpression.registerFunction("strToLowerCase", new StringFunctions(Fun.STR_TOLOWER, 1));
        FunctionExpression.registerFunction("strToUpperCase", new StringFunctions(Fun.STR_TOUPPER, 1));
        FunctionExpression.registerFunction("strTrim", new StringFunctions(Fun.STR_TRIM, 1));
        FunctionExpression.registerFunction("strReplace", new StringFunctions(Fun.STR_REPLACE, 3));
        FunctionExpression.registerFunction("strReplaceAll", new StringFunctions(Fun.STR_REPLACEALL, 3));
        FunctionExpression.registerFunction("strReplaceFirst", new StringFunctions(Fun.STR_REPLACEFIRST, 3));
        FunctionExpression.registerFunction("strContains", new StringFunctions(Fun.STR_CONTAINS, 2));
        FunctionExpression.registerFunction("strStartsWith", new StringFunctions(Fun.STR_STARTWITH, 2));
        FunctionExpression.registerFunction("strEndsWith", new StringFunctions(Fun.STR_ENDSWITH, 2));
        FunctionExpression.registerFunction("strIsEmpty", new StringFunctions(Fun.STR_ISEMPTY, 1));
        FunctionExpression.registerFunction("strMatches", new StringFunctions(Fun.STR_MATCHES, 2));
        FunctionExpression.registerFunction("strIndexOf", new StringFunctions(Fun.STR_INDEXOF, 2));
        FunctionExpression.registerFunction("strLastIndexOf", new StringFunctions(Fun.STR_LASTINDEXOF, 2));
    }

    private StringFunctions(Fun f, int i) {
        super(i);
        this.mFun = f;
    }

    public double evaluate(Expression[] paraExps, Variables var) {
        double d = 0.0d;
        switch (this.mFun) {
            case STR_REPLACE:
            case STR_REPLACEALL:
            case STR_REPLACEFIRST:
            case STR_TOLOWER:
            case STR_TOUPPER:
            case STR_TRIM:
                return Utils.stringToDouble(evaluateStr(paraExps, var), 0.0d);
            default:
                String str0 = paraExps[0].evaluateStr();
                if (AnonymousClass1.$SwitchMap$miui$maml$data$StringFunctions$Fun[this.mFun.ordinal()] != 7) {
                    String str1 = paraExps[1].evaluateStr();
                    int i = AnonymousClass1.$SwitchMap$miui$maml$data$StringFunctions$Fun[this.mFun.ordinal()];
                    double d2 = -1.0d;
                    String str = LOG_TAG;
                    switch (i) {
                        case 1:
                            if (!(str0 == null || str1 == null || !str0.contains(str1))) {
                                d = 1.0d;
                            }
                            return d;
                        case 2:
                            if (!(str0 == null || str1 == null || !str0.startsWith(str1))) {
                                d = 1.0d;
                            }
                            return d;
                        case 3:
                            if (!(str0 == null || str1 == null || !str0.endsWith(str1))) {
                                d = 1.0d;
                            }
                            return d;
                        case 4:
                            if (!(str0 == null || str1 == null)) {
                                try {
                                    if (str0.matches(str1)) {
                                        d = 1.0d;
                                    }
                                } catch (PatternSyntaxException e) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("invaid pattern of matches: ");
                                    stringBuilder.append(str1);
                                    Log.w(str, stringBuilder.toString());
                                    return 0.0d;
                                }
                            }
                            return d;
                        case 5:
                            if (!(str0 == null || str1 == null)) {
                                d2 = (double) str0.indexOf(str1);
                            }
                            return d2;
                        case 6:
                            if (!(str0 == null || str1 == null)) {
                                d2 = (double) str0.lastIndexOf(str1);
                            }
                            return d2;
                        default:
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("fail to evalute FunctionExpression, invalid function: ");
                            stringBuilder2.append(this.mFun.toString());
                            Log.e(str, stringBuilder2.toString());
                            return 0.0d;
                    }
                }
                if (str0 == null || str0.isEmpty()) {
                    d = 1.0d;
                }
                return d;
        }
    }

    public String evaluateStr(Expression[] paraExps, Variables var) {
        StringBuilder stringBuilder;
        switch (this.mFun) {
            case STR_CONTAINS:
            case STR_STARTWITH:
            case STR_ENDSWITH:
            case STR_MATCHES:
            case STR_INDEXOF:
            case STR_LASTINDEXOF:
            case STR_ISEMPTY:
                return Utils.doubleToString(evaluate(paraExps, var));
            default:
                String str0 = paraExps[0].evaluateStr();
                if (str0 == null) {
                    return null;
                }
                switch (this.mFun) {
                    case STR_TOLOWER:
                        return str0.toLowerCase();
                    case STR_TOUPPER:
                        return str0.toUpperCase();
                    case STR_TRIM:
                        return str0.trim();
                    default:
                        String str1 = paraExps[1].evaluateStr();
                        String str2 = paraExps[2].evaluateStr();
                        if (str1 == null || str2 == null) {
                            return str0;
                        }
                        int i = AnonymousClass1.$SwitchMap$miui$maml$data$StringFunctions$Fun[this.mFun.ordinal()];
                        String str = LOG_TAG;
                        switch (i) {
                            case 8:
                                return str0.replace(str1, str2);
                            case 9:
                                try {
                                    return str0.replaceAll(str1, str2);
                                } catch (PatternSyntaxException e) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("invaid pattern of replaceAll: ");
                                    stringBuilder.append(str1);
                                    Log.w(str, stringBuilder.toString());
                                    return str0;
                                }
                            case 10:
                                try {
                                    return str0.replaceFirst(str1, str2);
                                } catch (PatternSyntaxException e2) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("invaid pattern of replaceFirst:");
                                    stringBuilder.append(str1);
                                    Log.w(str, stringBuilder.toString());
                                    return str0;
                                }
                            default:
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("fail to evaluteStr FunctionExpression, invalid function: ");
                                stringBuilder.append(this.mFun.toString());
                                Log.e(str, stringBuilder.toString());
                                return null;
                        }
                }
        }
    }
}
