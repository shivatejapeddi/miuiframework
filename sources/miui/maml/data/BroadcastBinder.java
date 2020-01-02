package miui.maml.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class BroadcastBinder extends VariableBinder {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "BroadcastBinder";
    public static final String TAG_NAME = "BroadcastBinder";
    private String mAction;
    private IntentFilter mIntentFilter;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onNotify: ");
            stringBuilder.append(BroadcastBinder.this.toString());
            Log.i("BroadcastBinder", stringBuilder.toString());
            BroadcastBinder.this.onNotify(context, intent, null);
        }
    };
    private boolean mRegistered;

    private static class Variable extends miui.maml.data.VariableBinder.Variable {
        public String mExtraName;

        public Variable(Element node, Variables var) {
            super(node, var);
            this.mExtraName = node.getAttribute("extra");
        }
    }

    public BroadcastBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    /* Access modifiers changed, original: protected */
    public void onNotify(Context context, Intent intent, Object object) {
        updateVariables(intent);
        onUpdateComplete();
    }

    /* Access modifiers changed, original: protected */
    public void onRegister() {
        updateVariables(getContext().mContext.registerReceiver(this.mIntentReceiver, this.mIntentFilter));
        onUpdateComplete();
    }

    /* Access modifiers changed, original: protected */
    public void onUnregister() {
        getContext().mContext.unregisterReceiver(this.mIntentReceiver);
    }

    /* Access modifiers changed, original: protected */
    public void register() {
        if (!this.mRegistered) {
            onRegister();
            this.mRegistered = true;
        }
    }

    /* Access modifiers changed, original: protected */
    public void unregister() {
        if (this.mRegistered) {
            try {
                onUnregister();
            } catch (IllegalArgumentException e) {
            }
            this.mRegistered = false;
        }
    }

    private void load(Element node) {
        String str = "BroadcastBinder";
        if (node != null) {
            this.mAction = node.getAttribute("action");
            if (TextUtils.isEmpty(this.mAction)) {
                Log.e(str, "no action in broadcast binder");
                throw new IllegalArgumentException("no action in broadcast binder element");
            }
            this.mIntentFilter = new IntentFilter(this.mAction);
            loadVariables(node);
            return;
        }
        Log.e(str, "ContentProviderBinder node is null");
        throw new NullPointerException("node is null");
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getContext().mVariables);
    }

    /* Access modifiers changed, original: protected */
    public void addVariable(Variable v) {
        this.mVariables.add(v);
    }

    private void updateVariables(Intent intent) {
        Intent intent2 = intent;
        if (intent2 != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String str = "updateVariables: ";
            stringBuilder.append(str);
            stringBuilder.append(intent2);
            String str2 = "BroadcastBinder";
            Log.d(str2, stringBuilder.toString());
            Iterator it = this.mVariables.iterator();
            while (it.hasNext()) {
                String valueStr;
                Variable v = (Variable) ((miui.maml.data.VariableBinder.Variable) it.next());
                double value = 0.0d;
                if (v.mType != 2) {
                    int i = v.mType;
                    if (i == 3) {
                        value = (double) intent2.getIntExtra(v.mExtraName, (int) v.mDefNumberValue);
                    } else if (i == 4) {
                        value = (double) intent2.getLongExtra(v.mExtraName, (long) v.mDefNumberValue);
                    } else if (i == 5) {
                        value = (double) intent2.getFloatExtra(v.mExtraName, (float) v.mDefNumberValue);
                    } else if (i != 6) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("invalide type");
                        stringBuilder2.append(v.mTypeStr);
                        Log.w(str2, stringBuilder2.toString());
                    } else {
                        value = intent2.getDoubleExtra(v.mExtraName, v.mDefNumberValue);
                    }
                    v.set(value);
                    valueStr = String.format("%f", new Object[]{Double.valueOf(value)});
                } else {
                    valueStr = intent2.getStringExtra(v.mExtraName);
                    v.set(valueStr == null ? v.mDefStringValue : valueStr);
                }
                String info = String.format("name:%s type:%s value:%s", new Object[]{v.mName, v.mTypeStr, valueStr});
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(info);
                Log.d(str2, stringBuilder3.toString());
            }
        }
    }

    public void init() {
        super.init();
        register();
    }

    public void finish() {
        super.finish();
        unregister();
    }
}
