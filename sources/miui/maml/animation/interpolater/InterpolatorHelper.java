package miui.maml.animation.interpolater;

import android.text.TextUtils;
import android.view.animation.Interpolator;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import org.w3c.dom.Element;

public class InterpolatorHelper {
    private static final String VAR_RATIO = "__ratio";
    private Expression mEaseExp;
    private Interpolator mInterpolator;
    private IndexedVariable mRatioVar;

    public static InterpolatorHelper create(Variables vars, Element node) {
        return create(vars, node.getAttribute("easeType"), node.getAttribute("easeExp"));
    }

    public static InterpolatorHelper create(Variables vars, String easeType, String easeExp) {
        if (TextUtils.isEmpty(easeType) && TextUtils.isEmpty(easeExp)) {
            return null;
        }
        return new InterpolatorHelper(vars, easeType, easeExp);
    }

    public InterpolatorHelper(Variables vars, String easeType, String easeExp) {
        this.mInterpolator = InterpolatorFactory.create(easeType);
        this.mEaseExp = Expression.build(vars, easeExp);
        if (this.mEaseExp != null) {
            this.mRatioVar = new IndexedVariable(VAR_RATIO, vars, true);
        }
    }

    public float get(float ratio) {
        if (this.mEaseExp != null) {
            this.mRatioVar.set((double) ratio);
            return (float) this.mEaseExp.evaluate();
        }
        Interpolator interpolator = this.mInterpolator;
        if (interpolator != null) {
            return interpolator.getInterpolation(ratio);
        }
        return ratio;
    }

    public boolean isValid() {
        return (this.mEaseExp == null && this.mInterpolator == null) ? false : true;
    }
}
