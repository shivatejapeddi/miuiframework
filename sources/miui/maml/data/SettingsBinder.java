package miui.maml.data;

import android.content.ContentResolver;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class SettingsBinder extends VariableBinder {
    public static final String TAG_NAME = "SettingsBinder";
    private boolean mConst;
    private ContentResolver mContentResolver = this.mRoot.getContext().mContext.getContentResolver();

    /* renamed from: miui.maml.data.SettingsBinder$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$SettingsBinder$Category = new int[Category.values().length];

        static {
            try {
                $SwitchMap$miui$maml$data$SettingsBinder$Category[Category.System.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$SettingsBinder$Category[Category.Secure.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private enum Category {
        Secure,
        System
    }

    private class Variable extends miui.maml.data.VariableBinder.Variable {
        public Category mCategory;
        public String mKey;

        public Variable(Element node, Variables var) {
            super(node, var);
            this.mCategory = "secure".equals(node.getAttribute("category")) ? Category.Secure : Category.System;
            this.mKey = node.getAttribute("key");
        }

        public void query() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$data$SettingsBinder$Category[this.mCategory.ordinal()];
            String str;
            if (i == 1) {
                i = this.mType;
                if (i == 2) {
                    str = System.getString(SettingsBinder.this.mContentResolver, this.mKey);
                    set(str == null ? this.mDefStringValue : str);
                } else if (i == 3) {
                    set((double) System.getInt(SettingsBinder.this.mContentResolver, this.mKey, (int) this.mDefNumberValue));
                } else if (i == 4) {
                    set((double) System.getLong(SettingsBinder.this.mContentResolver, this.mKey, (long) this.mDefNumberValue));
                } else if (i == 5 || i == 6) {
                    set((double) System.getFloat(SettingsBinder.this.mContentResolver, this.mKey, (float) this.mDefNumberValue));
                }
            } else if (i == 2) {
                i = this.mType;
                if (i == 2) {
                    str = Secure.getString(SettingsBinder.this.mContentResolver, this.mKey);
                    set(str == null ? this.mDefStringValue : str);
                } else if (i == 3) {
                    set((double) Secure.getInt(SettingsBinder.this.mContentResolver, this.mKey, (int) this.mDefNumberValue));
                } else if (i == 4) {
                    set((double) Secure.getLong(SettingsBinder.this.mContentResolver, this.mKey, (long) this.mDefNumberValue));
                } else if (i == 5 || i == 6) {
                    set((double) Secure.getFloat(SettingsBinder.this.mContentResolver, this.mKey, (float) this.mDefNumberValue));
                }
            }
        }
    }

    public SettingsBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        if (node != null) {
            loadVariables(node);
            this.mConst = "false".equalsIgnoreCase(node.getAttribute("const")) ^ 1;
        }
    }

    public void resume() {
        super.resume();
        if (!this.mConst) {
            startQuery();
        }
    }

    public void refresh() {
        super.refresh();
        startQuery();
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getContext().mVariables);
    }

    public void startQuery() {
        Iterator it = this.mVariables.iterator();
        while (it.hasNext()) {
            ((Variable) ((miui.maml.data.VariableBinder.Variable) it.next())).query();
        }
        onUpdateComplete();
    }
}
