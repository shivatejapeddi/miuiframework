package miui.maml.util;

import android.text.TextUtils;
import android.util.Log;

public class Variable {
    private String mObjectName;
    private String mPropertyName;

    public Variable(String var) {
        int dot = var.indexOf(46);
        if (dot == -1) {
            this.mObjectName = null;
            this.mPropertyName = var;
        } else {
            this.mObjectName = var.substring(0, dot);
            this.mPropertyName = var.substring(dot + 1);
        }
        if (TextUtils.isEmpty(this.mPropertyName)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid variable name:");
            stringBuilder.append(var);
            Log.e(miui.maml.data.VariableBinder.Variable.TAG_NAME, stringBuilder.toString());
        }
    }

    public String getObjName() {
        return this.mObjectName;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }
}
