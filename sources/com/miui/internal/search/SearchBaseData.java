package com.miui.internal.search;

import android.content.Context;
import android.text.format.DateFormat;
import java.util.Locale;

public abstract class SearchBaseData {
    public Context context;
    public String extras;
    public int iconResId;
    public String intentAction;
    public String intentTargetClass;
    public String intentTargetPackage;
    public String keywords;
    public Locale locale;
    public String other;
    public String packageName;
    public String summaryOff;
    public String summaryOn;
    public String title;
    public String uriString;

    public SearchBaseData() {
        this.locale = Locale.getDefault();
    }

    public SearchBaseData(Context context) {
        this();
        this.context = context;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SearchBaseData{context=");
        stringBuilder.append(this.context);
        stringBuilder.append(", locale=");
        stringBuilder.append(this.locale);
        stringBuilder.append(", title='");
        stringBuilder.append(this.title);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", packageName='");
        stringBuilder.append(this.packageName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", summaryOn='");
        stringBuilder.append(this.summaryOn);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", summaryOff='");
        stringBuilder.append(this.summaryOff);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", keywords='");
        stringBuilder.append(this.keywords);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", iconResId=");
        stringBuilder.append(this.iconResId);
        stringBuilder.append(", intentAction='");
        stringBuilder.append(this.intentAction);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", intentTargetPackage='");
        stringBuilder.append(this.intentTargetPackage);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", intentTargetClass='");
        stringBuilder.append(this.intentTargetClass);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", uriString='");
        stringBuilder.append(this.uriString);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", extras=");
        stringBuilder.append(this.extras);
        stringBuilder.append(", other='");
        stringBuilder.append(this.other);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
