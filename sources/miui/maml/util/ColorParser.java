package miui.maml.util;

import android.graphics.Color;
import android.util.Log;
import miui.maml.StylesManager.Style;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import org.w3c.dom.Element;

public class ColorParser {
    private static final int DEFAULT_COLOR = -16777216;
    private static final String LOG_TAG = "ColorParser";
    private int mColor = -16777216;
    private String mColorExpression;
    private String mCurColorString;
    private IndexedVariable mIndexedColorVar;
    private Expression[] mRGBExpression;
    private ExpressionType mType;

    /* renamed from: miui.maml.util.ColorParser$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$util$ColorParser$ExpressionType = new int[ExpressionType.values().length];

        static {
            try {
                $SwitchMap$miui$maml$util$ColorParser$ExpressionType[ExpressionType.CONST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$util$ColorParser$ExpressionType[ExpressionType.VARIABLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$util$ColorParser$ExpressionType[ExpressionType.ARGB.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private enum ExpressionType {
        CONST,
        VARIABLE,
        ARGB,
        INVALID
    }

    public ColorParser(Variables vars, String expression) {
        this.mColorExpression = expression.trim();
        if (this.mColorExpression.startsWith("#")) {
            this.mType = ExpressionType.CONST;
            try {
                this.mColor = Color.parseColor(this.mColorExpression);
            } catch (IllegalArgumentException e) {
                this.mColor = -16777216;
            }
        } else if (this.mColorExpression.startsWith("@")) {
            this.mType = ExpressionType.VARIABLE;
            this.mIndexedColorVar = new IndexedVariable(this.mColorExpression.substring(1), vars, false);
        } else if (this.mColorExpression.startsWith("argb(") && this.mColorExpression.endsWith(")")) {
            this.mType = ExpressionType.ARGB;
            String str = this.mColorExpression;
            this.mRGBExpression = Expression.buildMultiple(vars, str.substring(5, str.length() - 1));
            Expression[] expressionArr = this.mRGBExpression;
            if (expressionArr != null && expressionArr.length != 4) {
                Log.e(LOG_TAG, "bad expression format");
                throw new IllegalArgumentException("bad expression format.");
            }
        } else {
            this.mType = ExpressionType.INVALID;
        }
    }

    public int getColor() {
        int i = AnonymousClass1.$SwitchMap$miui$maml$util$ColorParser$ExpressionType[this.mType.ordinal()];
        if (i != 1) {
            int i2 = -16777216;
            if (i == 2) {
                String colorString = this.mIndexedColorVar.getString();
                if (!Utils.equals(colorString, this.mCurColorString)) {
                    if (colorString != null) {
                        i2 = Color.parseColor(colorString);
                    }
                    this.mColor = i2;
                    this.mCurColorString = colorString;
                }
            } else if (i != 3) {
                this.mColor = -16777216;
            } else {
                this.mColor = Color.argb((int) this.mRGBExpression[0].evaluate(), (int) this.mRGBExpression[1].evaluate(), (int) this.mRGBExpression[2].evaluate(), (int) this.mRGBExpression[3].evaluate());
            }
        }
        return this.mColor;
    }

    public static ColorParser fromElement(Variables vars, Element e) {
        return new ColorParser(vars, e.getAttribute("color"));
    }

    public static ColorParser fromElement(Variables vars, Element e, Style style) {
        return new ColorParser(vars, StyleHelper.getAttr(e, "color", style));
    }

    public static ColorParser fromElement(Variables vars, Element e, String attr, Style style) {
        return new ColorParser(vars, StyleHelper.getAttr(e, attr, style));
    }
}
