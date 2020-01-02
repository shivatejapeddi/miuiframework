package miui.maml.util;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import miui.maml.StylesManager.Style;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import org.w3c.dom.Element;

public class TextFormatter {
    private static final String LOG_TAG = "TextFormatter";
    private String mFormat;
    private Expression mFormatExpression;
    private IndexedVariable mIndexedFormatVar;
    private IndexedVariable mIndexedTextVar;
    private FormatPara[] mParas;
    private Object[] mParasValue;
    private String mText;
    private Expression mTextExpression;

    private static abstract class FormatPara {
        public abstract Object evaluate();

        private FormatPara() {
        }

        public static FormatPara[] buildArray(Variables vars, String exp) {
            int bracketCount = 0;
            int start = 0;
            ArrayList<FormatPara> exps = new ArrayList();
            for (int i = 0; i < exp.length(); i++) {
                char c = exp.charAt(i);
                if (bracketCount == 0 && c == ',') {
                    FormatPara para = build(vars, exp.substring(start, i));
                    if (para == null) {
                        return null;
                    }
                    exps.add(para);
                    start = i + 1;
                }
                if (c == '(') {
                    bracketCount++;
                } else if (c == ')') {
                    bracketCount--;
                }
            }
            FormatPara para2 = build(vars, exp.substring(start));
            if (para2 == null) {
                return null;
            }
            exps.add(para2);
            return (FormatPara[]) exps.toArray(new FormatPara[exps.size()]);
        }

        public static FormatPara build(Variables vars, String para) {
            String exp = para.trim();
            if (exp.startsWith("@")) {
                return new StringVarPara(vars, exp.substring(1));
            }
            Expression expression = Expression.build(vars, exp);
            if (expression != null) {
                return new ExpressioPara(expression);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid parameter expression:");
            stringBuilder.append(para);
            Log.e(TextFormatter.LOG_TAG, stringBuilder.toString());
            return null;
        }
    }

    private static class ExpressioPara extends FormatPara {
        private Expression mExp;

        public ExpressioPara(Expression exp) {
            super();
            this.mExp = exp;
        }

        public Object evaluate() {
            return Long.valueOf((long) this.mExp.evaluate());
        }
    }

    private static class StringVarPara extends FormatPara {
        private IndexedVariable mVar;
        private String mVariable;

        public StringVarPara(Variables vars, String name) {
            super();
            this.mVariable = name;
            this.mVar = new IndexedVariable(this.mVariable, vars, false);
        }

        public Object evaluate() {
            String string = this.mVar.getString();
            return string == null ? "" : string;
        }
    }

    public TextFormatter(Variables vars, String text) {
        String str = "";
        this(vars, text, str, str);
    }

    public TextFormatter(Variables vars, String format, String paras) {
        this(vars, "", format, paras);
    }

    public TextFormatter(Variables vars, String text, String format, String paras) {
        this.mText = text;
        String str = "@";
        if (this.mText.startsWith(str)) {
            this.mText = this.mText.substring(1);
            if (!this.mText.startsWith(str)) {
                this.mIndexedTextVar = new IndexedVariable(this.mText, vars, false);
            }
        }
        this.mFormat = format;
        if (this.mFormat.startsWith(str)) {
            this.mFormat = this.mFormat.substring(1);
            if (!this.mFormat.startsWith(str)) {
                this.mIndexedFormatVar = new IndexedVariable(this.mFormat, vars, false);
            }
        }
        if (!TextUtils.isEmpty(paras)) {
            this.mParas = FormatPara.buildArray(vars, paras);
            FormatPara[] formatParaArr = this.mParas;
            if (formatParaArr != null) {
                this.mParasValue = new Object[formatParaArr.length];
            }
        }
    }

    public TextFormatter(Variables vars, String text, Expression textExp) {
        this(vars, text, "", "", textExp, null);
    }

    public TextFormatter(Variables vars, String text, String format, String paras, Expression textExp, Expression formatExp) {
        this(vars, text, format, paras);
        this.mTextExpression = textExp;
        this.mFormatExpression = formatExp;
    }

    public void setParams(Object... params) {
        if (params != null) {
            this.mParas = null;
            int parasLength = params.length;
            if (this.mParasValue == null) {
                this.mParasValue = new Object[parasLength];
            }
            Object[] objArr = this.mParasValue;
            if (objArr.length < parasLength) {
                parasLength = objArr.length;
            }
            this.mParasValue = Arrays.copyOf(params, parasLength);
        }
    }

    public void setText(String text) {
        this.mText = text;
        this.mFormat = "";
    }

    public String getFormat() {
        Expression expression = this.mFormatExpression;
        if (expression != null) {
            return expression.evaluateStr();
        }
        IndexedVariable indexedVariable = this.mIndexedFormatVar;
        if (indexedVariable != null) {
            return indexedVariable.getString();
        }
        return this.mFormat;
    }

    public boolean hasFormat() {
        return TextUtils.isEmpty(this.mFormat) ^ 1;
    }

    public String getText() {
        Expression expression = this.mTextExpression;
        if (expression != null) {
            return expression.evaluateStr();
        }
        String format = getFormat();
        if (!TextUtils.isEmpty(format)) {
            if (this.mParas != null) {
                int i = 0;
                while (true) {
                    FormatPara[] formatParaArr = this.mParas;
                    if (i >= formatParaArr.length) {
                        break;
                    }
                    this.mParasValue[i] = formatParaArr[i].evaluate();
                    i++;
                }
            }
            Object[] objArr = this.mParasValue;
            if (objArr != null) {
                try {
                    return String.format(format, objArr);
                } catch (IllegalFormatException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Format error: ");
                    stringBuilder.append(format);
                    return stringBuilder.toString();
                }
            }
        }
        IndexedVariable indexedVariable = this.mIndexedTextVar;
        if (indexedVariable != null) {
            return indexedVariable.getString();
        }
        return this.mText;
    }

    public static TextFormatter fromElement(Variables vars, Element e, Style style) {
        String params = StyleHelper.getAttr(e, "paras", style);
        if (TextUtils.isEmpty(params)) {
            params = StyleHelper.getAttr(e, "params", style);
        }
        return new TextFormatter(vars, StyleHelper.getAttr(e, "text", style), StyleHelper.getAttr(e, "format", style), params, Expression.build(vars, StyleHelper.getAttr(e, "textExp", style)), Expression.build(vars, StyleHelper.getAttr(e, "formatExp", style)));
    }

    public static TextFormatter fromElement(Variables vars, Element e, String textAttr, String formatAttr, String parasAttr, String textExpAttr, String formatExpAttr) {
        return new TextFormatter(vars, e.getAttribute(textAttr), e.getAttribute(formatAttr), e.getAttribute(parasAttr), Expression.build(vars, e.getAttribute(textExpAttr)), Expression.build(vars, e.getAttribute(formatExpAttr)));
    }
}
