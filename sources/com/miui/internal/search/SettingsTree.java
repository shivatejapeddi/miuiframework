package com.miui.internal.search;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsTree extends IndexTree {
    public static final String ACTION_SETTINGS_SEARCH_INIT = "miui.intent.action.SETTINGS_SEARCH_INIT";
    public static final int DISABLED = 1;
    public static final int ENABLED = 3;
    public static final int FLAG_AVAILABLE = 2;
    public static final int FLAG_IGNORED = 4;
    public static final int FLAG_VISIBLE = 1;
    public static final int INVISIBLE = 0;
    public static final String SETTINGS_PACKAGE = "com.android.settings";
    private static final String TAG = "SettingsTree";
    private String mCategory;
    private String mCategoryString;
    protected final Context mContext;
    private String mFeature;
    private String mFragment;
    private String mIcon;
    private Intent mIntent;
    private boolean mIsCheckBox;
    private boolean mIsOldman;
    private boolean mIsSecondSpace;
    private List<String> mKeywords;
    private String mKeywordsResource;
    private int mLevel;
    private Locale mLocale;
    private String mResource;
    private int mStatus;
    private String mSummary;
    private String mTitle;
    private boolean mTmp;

    public static SettingsTree newInstance(Context context, JSONObject json, boolean initialize) throws JSONException {
        return newInstance(context, json, null, initialize);
    }

    protected static SettingsTree newInstance(Context context, JSONObject json, SettingsTree parent) throws JSONException {
        return newInstance(context, json, parent, true);
    }

    protected static SettingsTree newInstance(Context context, JSONObject json, SettingsTree parent, boolean initialize) throws JSONException {
        StringBuilder stringBuilder;
        String className = json.optString(Function.CLASS);
        String pkg = json.optString("package");
        boolean isEmpty = TextUtils.isEmpty(className);
        String str = SETTINGS_PACKAGE;
        SettingsTree settingsTree = null;
        if (isEmpty) {
            if (parent != null && !parent.getClass().equals(SettingsTree.class)) {
                className = parent.getClass().getName();
            } else if (TextUtils.isEmpty(pkg) || TextUtils.equals(pkg, str)) {
                SettingsTree ret = new SettingsTree(context, json, parent, initialize);
                if (!(initialize && ret.dispatchInitialize())) {
                    settingsTree = ret;
                }
                return settingsTree;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(pkg);
                stringBuilder.append(".search.CustomSettingsTree");
                className = stringBuilder.toString();
            }
        }
        if (TextUtils.isEmpty(pkg)) {
            pkg = className;
        }
        isEmpty = SearchUtils.isPackageExisted(context, pkg);
        String str2 = TAG;
        if (isEmpty) {
            try {
                if (!className.contains(str)) {
                    str = pkg;
                }
                Class clazz = Class.forName(className, true, SearchUtils.getPackageContext(context, str).getClassLoader());
                if (SettingsTree.class.isAssignableFrom(clazz)) {
                    Constructor c = clazz.getDeclaredConstructor(new Class[]{Context.class, JSONObject.class, SettingsTree.class, Boolean.TYPE});
                    c.setAccessible(true);
                    SettingsTree ret2 = (SettingsTree) c.newInstance(new Object[]{context, json, parent, Boolean.valueOf(initialize)});
                    if (!initialize || !ret2.dispatchInitialize()) {
                        settingsTree = ret2;
                    }
                    return settingsTree;
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("cannot cast ");
                stringBuilder2.append(clazz.getName());
                stringBuilder2.append(" to ");
                stringBuilder2.append(SettingsTree.class.getName());
                throw new ClassCastException(stringBuilder2.toString());
            } catch (Exception e) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("drop subtree under ");
                stringBuilder3.append(parent.getTitle(false));
                Log.e(str2, stringBuilder3.toString(), e);
                return null;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("package ");
        stringBuilder.append(pkg);
        stringBuilder.append(" doesn't exist any more");
        Log.w(str2, stringBuilder.toString());
        return null;
    }

    protected SettingsTree(Context context, JSONObject json, SettingsTree parent, boolean initialize) throws JSONException {
        super(parent);
        if (parent == null) {
            this.mLevel = 0;
        } else {
            this.mLevel = parent.mLevel + 1;
        }
        this.mContext = SearchUtils.getPackageContext(context, json.optString("package", getPackage()));
        this.mResource = json.optString(Function.RESOURCE);
        this.mCategory = json.optString("category");
        this.mTitle = null;
        this.mKeywordsResource = json.optString("keywords");
        this.mSummary = json.optString("summary");
        this.mIcon = json.optString("icon");
        this.mFragment = json.optString(Function.FRAGMENT);
        try {
            this.mIntent = new TinyIntent(json.getJSONObject("intent")).toIntent();
        } catch (JSONException e) {
            this.mIntent = null;
        }
        this.mFeature = json.optString(Function.FEATURE);
        this.mIsSecondSpace = json.optBoolean(Function.SECOND_SPACE, true);
        this.mIsCheckBox = json.optBoolean(Function.IS_CHECKBOX, false);
        this.mIsOldman = json.optBoolean(Function.IS_OLDMAN, true);
        JSONArray sons = json.optJSONArray(Function.SON);
        if (!(sons == null || SearchUtils.removeViaSecondSpace(this.mIsSecondSpace) || SearchUtils.removeViaFeature(this.mFeature))) {
            for (int i = 0; i < sons.length(); i++) {
                addSon(newInstance(context, sons.getJSONObject(i), this, initialize));
            }
        }
        this.mStatus = json.optInt("status", 3);
        this.mTmp = json.optBoolean(Function.TEMPORARY, false);
        this.mLocale = null;
    }

    public final JSONObject toJSONObject() {
        if (this.mTmp) {
            return null;
        }
        JSONObject json = new JSONObject();
        try {
            if (!getPackage().equals(SETTINGS_PACKAGE)) {
                json.put("package", getPackage());
            }
            if (!getClass().equals(SettingsTree.class)) {
                json.put(Function.CLASS, getClass().getName());
            }
            if (!TextUtils.isEmpty(this.mResource)) {
                json.put(Function.RESOURCE, this.mResource);
            }
            if (!TextUtils.isEmpty(this.mCategory)) {
                json.put("category", this.mCategory);
            }
            if (!TextUtils.isEmpty(this.mKeywordsResource)) {
                json.put("keywords", this.mKeywordsResource);
            }
            if (!TextUtils.isEmpty(this.mSummary)) {
                json.put("summary", this.mSummary);
            }
            if (!TextUtils.isEmpty(this.mIcon)) {
                json.put("icon", this.mIcon);
            }
            if (!TextUtils.isEmpty(this.mFragment)) {
                json.put(Function.FRAGMENT, this.mFragment);
            }
            if (this.mIntent != null) {
                json.put("intent", new TinyIntent(this.mIntent).toJSONObject());
            }
            if (this.mIsCheckBox) {
                json.put(Function.IS_CHECKBOX, true);
            }
            if (!this.mIsOldman) {
                json.put(Function.IS_OLDMAN, false);
            }
            if (getSons() != null) {
                JSONArray sons = new JSONArray();
                Iterator it = getSons().iterator();
                while (it.hasNext()) {
                    JSONObject o = ((SettingsTree) it.next()).toJSONObject();
                    if (o != null) {
                        sons.put(o);
                    }
                }
                json.put(Function.SON, sons);
            }
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /* Access modifiers changed, original: protected */
    public String getTitle(boolean localized) {
        if (localized) {
            updateLocale();
        }
        return (!localized || TextUtils.isEmpty(this.mTitle)) ? this.mResource.toLowerCase() : this.mTitle;
    }

    /* Access modifiers changed, original: protected */
    public String getCategory(boolean localized) {
        if (localized) {
            updateLocale();
        }
        if (localized) {
            return TextUtils.isEmpty(this.mCategoryString) ? getTitle(true) : this.mCategoryString;
        } else {
            return this.mCategory.toLowerCase();
        }
    }

    /* Access modifiers changed, original: protected */
    public String[] getKeywords() {
        updateKeywords();
        List list = this.mKeywords;
        return list == null ? new String[0] : (String[]) list.toArray(new String[0]);
    }

    /* Access modifiers changed, original: protected */
    public String getPinyin() {
        String str = null;
        if (!"zh".equals(Locale.getDefault().getLanguage())) {
            return null;
        }
        String title = getTitle(true);
        if (!title.equals(this.mResource.toLowerCase())) {
            str = SearchUtils.getPinyin(title);
        }
        return str;
    }

    /* Access modifiers changed, original: protected */
    public String getPath(boolean localized, boolean category) {
        StringBuilder stringBuilder;
        String path = getParent() == null ? "" : getParent().getPath(localized, false);
        if (!TextUtils.isEmpty(path)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(path);
            stringBuilder.append("/");
            path = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(path);
        stringBuilder.append(category ? getCategory(localized) : getTitle(localized));
        return stringBuilder.toString();
    }

    public SettingsTree getParent() {
        return (SettingsTree) super.getParent();
    }

    public LinkedList<SettingsTree> getSons() {
        return super.getSons();
    }

    public Intent getIntent() {
        Intent intent = this.mIntent;
        if (intent != null) {
            return new Intent(intent);
        }
        if (TextUtils.isEmpty(this.mFragment)) {
            return null;
        }
        intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.setClassName(SETTINGS_PACKAGE, "com.android.settings.SubSettings");
        intent.putExtra(":settings:show_fragment", this.mFragment);
        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
        return intent;
    }

    /* Access modifiers changed, original: protected */
    public Intent getIntentForStart() {
        Intent intent = getIntent();
        if (intent != null) {
            String str;
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_MAIN) && intent.hasExtra(":settings:show_fragment")) {
                str = ":settings:show_fragment_title";
                if (!intent.hasExtra(str)) {
                    intent.putExtra(str, getTitle(true));
                }
            } else {
                str = PreferenceActivity.EXTRA_SHOW_FRAGMENT_TITLE;
                if (!intent.hasExtra(str)) {
                    intent.putExtra(str, getTitle(true));
                }
            }
            return intent;
        }
        return getParent() != null ? getParent().getIntentForStart() : null;
    }

    @Deprecated
    public final void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    public String getIcon() {
        if (!TextUtils.isEmpty(this.mIcon)) {
            return this.mIcon;
        }
        return getParent() == null ? "" : getParent().getIcon();
    }

    /* Access modifiers changed, original: protected */
    public int getStatus() {
        return this.mStatus;
    }

    /* Access modifiers changed, original: protected|final */
    public final Context getPackageContext(Context context) {
        return SearchUtils.getPackageContext(context, getPackage());
    }

    public final boolean query(RankedCursor cursor, String query, String selection, String[] selectionArgs, String sortOrder, boolean fullQuery) {
        boolean available = false;
        if (SearchUtils.isOldmanMode() && !this.mIsOldman) {
            return false;
        }
        int status = 0;
        try {
            status = getStatus();
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hide because exception occurs when query: ");
            stringBuilder.append(getTitle(true));
            Log.e(TAG, stringBuilder.toString(), e);
        }
        if ((status & 1) == 0) {
            return false;
        }
        if ((status & 2) != 0) {
            available = true;
        }
        if ((status & 4) == 0 && isSelected(selection, selectionArgs)) {
            double score = fullQuery ? 1.0d : match(query);
            if (score > 0.0d) {
                getColumnValues(cursor, score, available);
            }
        }
        return available;
    }

    public final boolean update(ContentValues values, String selection, String[] selectionArgs) {
        if (!isSelected(selection, selectionArgs)) {
            return false;
        }
        String[] columns = new String[values.keySet().size()];
        values.keySet().toArray(columns);
        String[] newValues = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            newValues[i] = values.getAsString(columns[i]);
        }
        setColumnValues(columns, newValues);
        return true;
    }

    public final IndexTree insert(ContentValues values) {
        if (!TextUtils.equals(values.getAsString("parent"), this.mResource)) {
            return null;
        }
        String[] columns = new String[values.keySet().size()];
        values.keySet().toArray(columns);
        try {
            JSONObject json = new JSONObject();
            for (String column : columns) {
                json.put(column, values.get(column));
            }
            return newInstance(this.mContext, json, this);
        } catch (JSONException e) {
            return null;
        }
    }

    public final boolean delete(String selection, String[] selectionArgs) {
        return isSelected(selection, selectionArgs);
    }

    /* Access modifiers changed, original: protected|final */
    public final String getUri() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("content://com.miui.settings/");
        stringBuilder.append(getPath(false, false));
        return stringBuilder.toString();
    }

    private void getColumnValues(RankedCursor cursor, double score, boolean available) {
        String[] columns = cursor.getColumnNames();
        String[] values = new String[columns.length];
        Bundle extras = cursor.getExtras();
        for (int i = 0; i < columns.length; i++) {
            if ("intent".equals(columns[i])) {
                Intent intent = available ? getIntentForStart() : getParent() == null ? null : getParent().getIntentForStart();
                values[i] = Integer.toString(hashCode());
                while (extras.containsKey(values[i])) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(values[i]);
                    stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
                    values[i] = stringBuilder.toString();
                }
                extras.putParcelable(values[i], intent);
            } else {
                values[i] = getColumnValue(columns[i]);
            }
        }
        cursor.addRow(score, values);
    }

    public java.lang.String getColumnValue(java.lang.String r6) {
        /*
        r5 = this;
        r0 = r6.hashCode();
        r1 = 0;
        r2 = 1;
        switch(r0) {
            case -1857640538: goto L_0x00da;
            case -1650269616: goto L_0x00cf;
            case -1183762788: goto L_0x00c4;
            case -979207434: goto L_0x00b9;
            case -898763385: goto L_0x00af;
            case -892481550: goto L_0x00a3;
            case -807062458: goto L_0x0098;
            case -550265208: goto L_0x008d;
            case -361729669: goto L_0x0081;
            case -341064690: goto L_0x0075;
            case 3226745: goto L_0x0069;
            case 3433509: goto L_0x005d;
            case 50511102: goto L_0x0052;
            case 94742904: goto L_0x0047;
            case 102865796: goto L_0x003b;
            case 110371416: goto L_0x002f;
            case 523149226: goto L_0x0024;
            case 801861432: goto L_0x0018;
            case 1984986705: goto L_0x000b;
            default: goto L_0x0009;
        };
    L_0x0009:
        goto L_0x00e6;
    L_0x000b:
        r0 = "temporary";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0014:
        r0 = 17;
        goto L_0x00e7;
    L_0x0018:
        r0 = "is_checkbox";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0020:
        r0 = 14;
        goto L_0x00e7;
    L_0x0024:
        r0 = "keywords";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x002c:
        r0 = 7;
        goto L_0x00e7;
    L_0x002f:
        r0 = "title";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0038:
        r0 = 3;
        goto L_0x00e7;
    L_0x003b:
        r0 = "level";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0043:
        r0 = 18;
        goto L_0x00e7;
    L_0x0047:
        r0 = "class";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x004f:
        r0 = r2;
        goto L_0x00e7;
    L_0x0052:
        r0 = "category";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x005a:
        r0 = 4;
        goto L_0x00e7;
    L_0x005d:
        r0 = "path";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0066:
        r0 = 6;
        goto L_0x00e7;
    L_0x0069:
        r0 = "icon";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0071:
        r0 = 9;
        goto L_0x00e7;
    L_0x0075:
        r0 = "resource";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x007e:
        r0 = 2;
        goto L_0x00e7;
    L_0x0081:
        r0 = "second_space";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x008a:
        r0 = 13;
        goto L_0x00e7;
    L_0x008d:
        r0 = "is_oldman";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0095:
        r0 = 15;
        goto L_0x00e7;
    L_0x0098:
        r0 = "package";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00a1:
        r0 = r1;
        goto L_0x00e7;
    L_0x00a3:
        r0 = "status";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00ac:
        r0 = 16;
        goto L_0x00e7;
    L_0x00af:
        r0 = "category_origin";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00b7:
        r0 = 5;
        goto L_0x00e7;
    L_0x00b9:
        r0 = "feature";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00c1:
        r0 = 12;
        goto L_0x00e7;
    L_0x00c4:
        r0 = "intent";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00cc:
        r0 = 11;
        goto L_0x00e7;
    L_0x00cf:
        r0 = "fragment";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00d7:
        r0 = 10;
        goto L_0x00e7;
    L_0x00da:
        r0 = "summary";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x00e3:
        r0 = 8;
        goto L_0x00e7;
    L_0x00e6:
        r0 = -1;
    L_0x00e7:
        switch(r0) {
            case 0: goto L_0x0192;
            case 1: goto L_0x0189;
            case 2: goto L_0x0186;
            case 3: goto L_0x0181;
            case 4: goto L_0x017c;
            case 5: goto L_0x0179;
            case 6: goto L_0x0174;
            case 7: goto L_0x0171;
            case 8: goto L_0x016e;
            case 9: goto L_0x0169;
            case 10: goto L_0x0166;
            case 11: goto L_0x015d;
            case 12: goto L_0x0146;
            case 13: goto L_0x0146;
            case 14: goto L_0x013f;
            case 15: goto L_0x0138;
            case 16: goto L_0x010f;
            case 17: goto L_0x0108;
            case 18: goto L_0x0101;
            default: goto L_0x00ea;
        };
    L_0x00ea:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unknown column: ";
        r1.append(r2);
        r1.append(r6);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0101:
        r0 = r5.mLevel;
        r0 = java.lang.Integer.toString(r0);
        return r0;
    L_0x0108:
        r0 = r5.mTmp;
        r0 = java.lang.Boolean.toString(r0);
        return r0;
    L_0x010f:
        r0 = r5.getStatus();	 Catch:{ Exception -> 0x0118 }
        r0 = java.lang.Integer.toString(r0);	 Catch:{ Exception -> 0x0118 }
        return r0;
    L_0x0118:
        r0 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "hide because exception occurs when get status: ";
        r3.append(r4);
        r2 = r5.getTitle(r2);
        r3.append(r2);
        r2 = r3.toString();
        r3 = "SettingsTree";
        android.util.Log.e(r3, r2, r0);
        r1 = java.lang.Integer.toString(r1);
        return r1;
    L_0x0138:
        r0 = r5.mIsOldman;
        r0 = java.lang.Boolean.toString(r0);
        return r0;
    L_0x013f:
        r0 = r5.mIsCheckBox;
        r0 = java.lang.Boolean.toString(r0);
        return r0;
    L_0x0146:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r6);
        r2 = " was removed once initialized";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x015d:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "use getIntentForStart() instead";
        r0.<init>(r1);
        throw r0;
    L_0x0166:
        r0 = r5.mFragment;
        return r0;
    L_0x0169:
        r0 = r5.getIcon();
        return r0;
    L_0x016e:
        r0 = r5.mSummary;
        return r0;
    L_0x0171:
        r0 = r5.mKeywordsResource;
        return r0;
    L_0x0174:
        r0 = r5.getPath(r2, r2);
        return r0;
    L_0x0179:
        r0 = r5.mCategory;
        return r0;
    L_0x017c:
        r0 = r5.getCategory(r2);
        return r0;
    L_0x0181:
        r0 = r5.getTitle(r2);
        return r0;
    L_0x0186:
        r0 = r5.mResource;
        return r0;
    L_0x0189:
        r0 = r5.getClass();
        r0 = r0.getName();
        return r0;
    L_0x0192:
        r0 = r5.getPackage();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.internal.search.SettingsTree.getColumnValue(java.lang.String):java.lang.String");
    }

    private void setColumnValues(String[] columns, String[] values) {
        for (int i = 0; i < columns.length; i++) {
            setColumnValue(columns[i], values[i]);
        }
    }

    public void setColumnValue(java.lang.String r4, java.lang.String r5) {
        /*
        r3 = this;
        r0 = r4.hashCode();
        switch(r0) {
            case -1857640538: goto L_0x00c0;
            case -1650269616: goto L_0x00b6;
            case -1183762788: goto L_0x00ab;
            case -979207434: goto L_0x00a0;
            case -892481550: goto L_0x0094;
            case -807062458: goto L_0x0088;
            case -550265208: goto L_0x007d;
            case -361729669: goto L_0x0071;
            case -341064690: goto L_0x0066;
            case 3226745: goto L_0x005c;
            case 3433509: goto L_0x0050;
            case 50511102: goto L_0x0045;
            case 94742904: goto L_0x0039;
            case 110371416: goto L_0x002d;
            case 523149226: goto L_0x0022;
            case 801861432: goto L_0x0016;
            case 1984986705: goto L_0x0009;
            default: goto L_0x0007;
        };
    L_0x0007:
        goto L_0x00cb;
    L_0x0009:
        r0 = "temporary";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0012:
        r0 = 16;
        goto L_0x00cc;
    L_0x0016:
        r0 = "is_checkbox";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x001e:
        r0 = 11;
        goto L_0x00cc;
    L_0x0022:
        r0 = "keywords";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x002a:
        r0 = 4;
        goto L_0x00cc;
    L_0x002d:
        r0 = "title";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0036:
        r0 = 2;
        goto L_0x00cc;
    L_0x0039:
        r0 = "class";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0041:
        r0 = 14;
        goto L_0x00cc;
    L_0x0045:
        r0 = "category";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x004d:
        r0 = 1;
        goto L_0x00cc;
    L_0x0050:
        r0 = "path";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0059:
        r0 = 3;
        goto L_0x00cc;
    L_0x005c:
        r0 = "icon";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0064:
        r0 = 6;
        goto L_0x00cc;
    L_0x0066:
        r0 = "resource";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x006f:
        r0 = 0;
        goto L_0x00cc;
    L_0x0071:
        r0 = "second_space";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x007a:
        r0 = 10;
        goto L_0x00cc;
    L_0x007d:
        r0 = "is_oldman";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0085:
        r0 = 12;
        goto L_0x00cc;
    L_0x0088:
        r0 = "package";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0091:
        r0 = 13;
        goto L_0x00cc;
    L_0x0094:
        r0 = "status";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x009d:
        r0 = 15;
        goto L_0x00cc;
    L_0x00a0:
        r0 = "feature";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00a8:
        r0 = 9;
        goto L_0x00cc;
    L_0x00ab:
        r0 = "intent";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00b3:
        r0 = 8;
        goto L_0x00cc;
    L_0x00b6:
        r0 = "fragment";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00be:
        r0 = 7;
        goto L_0x00cc;
    L_0x00c0:
        r0 = "summary";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00c9:
        r0 = 5;
        goto L_0x00cc;
    L_0x00cb:
        r0 = -1;
    L_0x00cc:
        switch(r0) {
            case 0: goto L_0x0146;
            case 1: goto L_0x0140;
            case 2: goto L_0x0121;
            case 3: goto L_0x0121;
            case 4: goto L_0x011e;
            case 5: goto L_0x011b;
            case 6: goto L_0x0118;
            case 7: goto L_0x0115;
            case 8: goto L_0x010d;
            case 9: goto L_0x00f6;
            case 10: goto L_0x00f6;
            case 11: goto L_0x00ef;
            case 12: goto L_0x00e8;
            case 13: goto L_0x00d1;
            case 14: goto L_0x00d1;
            case 15: goto L_0x00d1;
            case 16: goto L_0x00d1;
            default: goto L_0x00cf;
        };
    L_0x00cf:
        goto L_0x014c;
    L_0x00d1:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r4);
        r2 = " cannot be modified";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x00e8:
        r0 = java.lang.Boolean.parseBoolean(r5);
        r3.mIsOldman = r0;
        goto L_0x014c;
    L_0x00ef:
        r0 = java.lang.Boolean.parseBoolean(r5);
        r3.mIsCheckBox = r0;
        goto L_0x014c;
    L_0x00f6:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r4);
        r2 = " was removed once initialized";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x010d:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Use setIntent() instead";
        r0.<init>(r1);
        throw r0;
    L_0x0115:
        r3.mFragment = r5;
        goto L_0x014c;
    L_0x0118:
        r3.mIcon = r5;
        goto L_0x014c;
    L_0x011b:
        r3.mSummary = r5;
        goto L_0x014c;
    L_0x011e:
        r3.mKeywordsResource = r5;
        goto L_0x014c;
    L_0x0121:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r4);
        r2 = " should not be modified, you may modify ";
        r1.append(r2);
        r1.append(r4);
        r2 = " via resource";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0140:
        r3.mCategory = r5;
        r3.updateCategoryRelated();
        goto L_0x014c;
    L_0x0146:
        r3.mResource = r5;
        r3.updateResourceRelated();
    L_0x014c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.internal.search.SettingsTree.setColumnValue(java.lang.String, java.lang.String):void");
    }

    public String getPackage() {
        return SETTINGS_PACKAGE;
    }

    private double match(String str) {
        String toLowerCase = str.toLowerCase();
        String str2 = "";
        String str3 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String str4 = "-";
        String str5 = "‑";
        toLowerCase = toLowerCase.replace(str3, str2).replace(str5, str4);
        String title = getTitle(true);
        if (title == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("null title: resource = ");
            stringBuilder.append(this.mResource);
            stringBuilder.append(", class = ");
            stringBuilder.append(getClass().getSimpleName());
            Log.e(TAG, stringBuilder.toString());
            return 0.0d;
        }
        double score = Math.max(0.0d, SearchUtils.doSimpleMatch(toLowerCase, title.toLowerCase().replace(str3, str2).replace(str5, str4)));
        if (score >= 1.0d) {
            return 1.0d;
        }
        for (String keyword : getKeywords()) {
            score = Math.max(score, SearchUtils.doSimpleMatch(keyword.toLowerCase().replace(str3, str2).replace(str5, str4), toLowerCase) - 0.1d);
            if (score >= 1.0d) {
                return 1.0d;
            }
        }
        score = Math.max(score, SearchUtils.doPinyinMatch(toLowerCase, getPinyin()) - 0.4d);
        if (score < 0.0d) {
            score = 0.0d;
        }
        return score;
    }

    private boolean isSelected(String selection, String[] selectionArgs) {
        String str = selection;
        int i = 1;
        if (str == null) {
        } else if (selectionArgs == null) {
        } else {
            String[] split = str.split(",");
            int length = split.length;
            int questionCount = 0;
            int questionCount2 = 0;
            while (questionCount2 < length) {
                String[] ss = split[questionCount2].split("=");
                String key = ss[0].trim();
                String expect = ss[i].trim();
                if (expect.equals("?")) {
                    int questionCount3 = questionCount + 1;
                    expect = selectionArgs[questionCount];
                    questionCount = questionCount3;
                }
                for (String function : Function.FUNCTIONS) {
                    if (key.contains(function)) {
                        Object value = getColumnValue(function);
                        if (value == null || !expect.equals(value)) {
                            return false;
                        }
                    }
                }
                questionCount2++;
                i = 1;
            }
            return true;
        }
        return true;
    }

    public void commit(StringBuilder builder) {
        if (getParent() == null) {
            JSONObject tree = toJSONObject();
            builder.append(tree == null ? "" : tree.toString());
        }
    }

    private boolean dispatchInitialize() {
        return SearchUtils.removeViaSecondSpace(this.mIsSecondSpace) || SearchUtils.removeViaFeature(this.mFeature) || initialize();
    }

    public boolean initialize() {
        return false;
    }

    public final void dispatchOnReceive(Context context, Intent intent) {
        onReceive(context, intent);
        if (getSons() != null) {
            Iterator it = getSons().iterator();
            while (it.hasNext()) {
                ((SettingsTree) it.next()).dispatchOnReceive(context, intent);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
    }

    public BackgroundCheckable getCheckable() {
        return null;
    }

    private void updateLocale() {
        if (!Objects.equals(Locale.getDefault(), this.mLocale)) {
            updateResourceRelated();
            updateCategoryRelated();
            this.mLocale = Locale.getDefault();
        }
    }

    private void updateResourceRelated() {
        if (!TextUtils.isEmpty(this.mResource)) {
            this.mTitle = SearchUtils.getString(getPackageContext(this.mContext), this.mResource);
        }
    }

    private void updateCategoryRelated() {
        if (!TextUtils.isEmpty(this.mCategory)) {
            this.mCategoryString = SearchUtils.getString(getPackageContext(this.mContext), this.mCategory);
        }
    }

    private void updateKeywords() {
        String[] keywords = getCloudKeywords();
        if ((keywords != null && keywords.length > 0) || !TextUtils.isEmpty(this.mKeywordsResource)) {
            if (keywords == null || keywords.length <= 0) {
                keywords = SearchUtils.getString(getPackageContext(this.mContext), this.mKeywordsResource).split(";");
            }
            saveKeywords(keywords);
        }
    }

    private void saveKeywords(String[] keywords) {
        if (keywords != null && keywords.length > 0) {
            List list = this.mKeywords;
            if (list == null) {
                this.mKeywords = new ArrayList(keywords.length);
            } else {
                list.clear();
            }
            Collections.addAll(this.mKeywords, keywords);
        }
    }

    private String[] getCloudKeywords() {
        String resourceName = new StringBuilder();
        resourceName.append("search_");
        resourceName.append(this.mResource);
        String keywords = KeywordsCloudConfigHelper.getInstance(this.mContext).getKeywords(resourceName.toString());
        if (TextUtils.isEmpty(keywords)) {
            return new String[0];
        }
        return keywords.split(";");
    }
}
