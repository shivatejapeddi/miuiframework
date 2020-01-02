package com.miui.internal.search;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.internal.search.Function.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TinyIntent {
    private String mAction;
    private ComponentName mComponent;
    private Uri mData;
    private Bundle mExtras;
    private String mPackage;

    public TinyIntent(String action) {
        setAction(action);
    }

    public TinyIntent(JSONObject json) throws JSONException {
        String action = json.optString(Intent.ACTION);
        String pkg = json.optString(Intent.PACKAGE);
        String clazz = json.optString(Intent.CLASS);
        JSONArray extra = json.optJSONArray(Intent.EXTRA);
        String data = json.optString(Intent.DATA);
        if (!TextUtils.isEmpty(action)) {
            setAction(action);
        }
        if (!TextUtils.isEmpty(pkg)) {
            if (TextUtils.isEmpty(clazz)) {
                setPackage(pkg);
            } else {
                setComponent(new ComponentName(pkg, clazz));
            }
        }
        if (extra != null) {
            for (int i = 0; i < extra.length(); i++) {
                JSONObject o = extra.getJSONObject(i);
                putExtra(o.optString("name"), o.optString("value"));
            }
        }
        if (!TextUtils.isEmpty(data)) {
            setData(Uri.parse(data));
        }
    }

    public TinyIntent(android.content.Intent intent) {
        this.mAction = intent.getAction();
        this.mPackage = intent.getPackage();
        this.mComponent = intent.getComponent();
        this.mExtras = intent.getExtras();
        this.mData = intent.getData();
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject o = new JSONObject();
        if (!TextUtils.isEmpty(this.mAction)) {
            o.put(Intent.ACTION, this.mAction);
        }
        ComponentName componentName = this.mComponent;
        String str = Intent.PACKAGE;
        if (componentName != null) {
            o.put(str, componentName.getPackageName());
            o.put(Intent.CLASS, this.mComponent.getClassName());
        } else {
            String str2 = this.mPackage;
            if (str2 != null) {
                o.put(str, str2);
            }
        }
        Bundle bundle = this.mExtras;
        if (!(bundle == null || bundle.keySet().isEmpty())) {
            JSONArray a = new JSONArray();
            for (String key : this.mExtras.keySet()) {
                a.put(new JSONObject().put("name", key).put("value", this.mExtras.get(key)));
            }
            o.put(Intent.EXTRA, a);
        }
        Uri uri = this.mData;
        if (uri != null) {
            o.put(Intent.DATA, uri.toString());
        }
        return o;
    }

    public android.content.Intent toIntent() {
        android.content.Intent intent = new android.content.Intent();
        if (!TextUtils.isEmpty(this.mAction)) {
            intent.setAction(this.mAction);
        }
        ComponentName componentName = this.mComponent;
        if (componentName != null) {
            intent.setComponent(componentName);
        } else {
            String str = this.mPackage;
            if (str != null) {
                intent.setPackage(str);
            }
        }
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        Uri uri = this.mData;
        if (uri != null) {
            intent.setData(uri);
        }
        return intent;
    }

    public String getAction() {
        return this.mAction;
    }

    public String getPackage() {
        return this.mPackage;
    }

    public ComponentName getComponent() {
        return this.mComponent;
    }

    public CharSequence getExtra(String key) {
        return this.mExtras.getCharSequence(key);
    }

    public CharSequence getExtra(String key, CharSequence defValue) {
        return this.mExtras.getCharSequence(key, defValue);
    }

    public Bundle getExtras() {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            return new Bundle(bundle);
        }
        return null;
    }

    public TinyIntent setAction(String action) {
        this.mAction = action;
        return this;
    }

    public TinyIntent setPackage(String pkg) {
        this.mPackage = pkg;
        return this;
    }

    public TinyIntent setComponent(ComponentName component) {
        this.mComponent = component;
        return this;
    }

    public TinyIntent setClassName(String packageName, String className) {
        this.mComponent = new ComponentName(packageName, className);
        return this;
    }

    public TinyIntent putExtra(String name, CharSequence value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putCharSequence(name, value);
        return this;
    }

    public TinyIntent putExtra(String name, int value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putInt(name, value);
        return this;
    }

    public TinyIntent putExtra(String name, boolean value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putBoolean(name, value);
        return this;
    }

    public TinyIntent putExtra(String name, Bundle value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putBundle(name, value);
        return this;
    }

    public TinyIntent putExtras(Bundle extras) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putAll(extras);
        return this;
    }

    public Uri getData() {
        return this.mData;
    }

    public void setData(Uri data) {
        this.mData = data;
    }
}
