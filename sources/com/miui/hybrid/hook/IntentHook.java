package com.miui.hybrid.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Slog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IntentHook {
    public static final String TAG = "IntentHook";
    private static volatile IntentHook sInstance;
    private Map<String, Map<String, RedirectRule>> ruleMap = new ConcurrentHashMap();

    private static class RedirectRule {
        String callingPkg;
        Bundle clsNameMap;
        String originDestPkg;
        String redirectPkg;

        private RedirectRule() {
        }
    }

    public static IntentHook getInstance() {
        if (sInstance == null) {
            synchronized (IntentHook.class) {
                if (sInstance == null) {
                    sInstance = new IntentHook();
                }
            }
        }
        return sInstance;
    }

    private IntentHook() {
    }

    public boolean insert(String callingPkg, String destPkg, String redirectPkg, Bundle clsNameMap) {
        boolean isEmpty = TextUtils.isEmpty(callingPkg);
        String str = TAG;
        if (isEmpty || TextUtils.isEmpty(destPkg) || TextUtils.isEmpty(redirectPkg)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IntentHook.insert(");
            stringBuilder.append(callingPkg);
            String str2 = ", ";
            stringBuilder.append(str2);
            stringBuilder.append(destPkg);
            stringBuilder.append(str2);
            stringBuilder.append(redirectPkg);
            stringBuilder.append(str2);
            stringBuilder.append(clsNameMap);
            stringBuilder.append(") failed.");
            Slog.e(str, stringBuilder.toString());
            return false;
        }
        RedirectRule rule = new RedirectRule();
        rule.callingPkg = callingPkg;
        rule.originDestPkg = destPkg;
        rule.redirectPkg = redirectPkg;
        rule.clsNameMap = clsNameMap;
        Map<String, RedirectRule> rules = (Map) this.ruleMap.get(callingPkg);
        if (rules == null) {
            rules = new ConcurrentHashMap();
            this.ruleMap.put(callingPkg, rules);
        }
        rules.put(destPkg, rule);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("insert rule ");
        stringBuilder2.append(destPkg);
        stringBuilder2.append(" -> ");
        stringBuilder2.append(redirectPkg);
        stringBuilder2.append(" for ");
        stringBuilder2.append(callingPkg);
        Slog.v(str, stringBuilder2.toString());
        return true;
    }

    public String delete(String callingPkg, String destPkg) {
        boolean isEmpty = TextUtils.isEmpty(callingPkg);
        String str = TAG;
        String str2 = null;
        if (isEmpty || TextUtils.isEmpty(destPkg)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IntentHook.delete(");
            stringBuilder.append(callingPkg);
            stringBuilder.append(", ");
            stringBuilder.append(destPkg);
            stringBuilder.append(") failed.");
            Slog.e(str, stringBuilder.toString());
            return null;
        }
        Map<String, RedirectRule> rules = (Map) this.ruleMap.get(callingPkg);
        if (rules == null) {
            return null;
        }
        RedirectRule redirectRule = (RedirectRule) rules.remove(destPkg);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("remove ");
        stringBuilder2.append(destPkg);
        stringBuilder2.append(" redirectRule:");
        stringBuilder2.append(redirectRule);
        Slog.v(str, stringBuilder2.toString());
        if (redirectRule != null) {
            str2 = redirectRule.redirectPkg;
        }
        return str2;
    }

    public Intent redirect(Intent intent, String callingPkg) {
        RedirectRule rule = getRedirectRule(intent, callingPkg);
        if (rule == null) {
            return intent;
        }
        ComponentName componentName = intent.getComponent();
        String pkgName = componentName.getPackageName();
        String clsName = componentName.getClassName();
        String redirectPkg = rule.redirectPkg;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("redirect intent: package ");
        stringBuilder.append(pkgName);
        String str = " -> ";
        stringBuilder.append(str);
        stringBuilder.append(redirectPkg);
        String stringBuilder2 = stringBuilder.toString();
        String str2 = TAG;
        Slog.d(str2, stringBuilder2);
        stringBuilder2 = clsName;
        if (rule.clsNameMap != null) {
            String mapClass = rule.clsNameMap.getString(clsName);
            if (TextUtils.isEmpty(mapClass)) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("ignore class part, ");
                stringBuilder3.append(mapClass);
                stringBuilder3.append(" is not a valid className.");
                Slog.d(str2, stringBuilder3.toString());
            } else {
                stringBuilder2 = mapClass;
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("redirect intent: class ");
                stringBuilder4.append(clsName);
                stringBuilder4.append(str);
                stringBuilder4.append(stringBuilder2);
                Slog.d(str2, stringBuilder4.toString());
            }
        } else {
            Slog.d(str2, "ignore class part, rule.clsMapper is null.");
        }
        intent.setComponent(new ComponentName(redirectPkg, stringBuilder2));
        return intent;
    }

    private RedirectRule getRedirectRule(Intent intent, String callingPackage) {
        if (intent == null || TextUtils.isEmpty(callingPackage)) {
            return null;
        }
        ComponentName componentName = intent.getComponent();
        if (componentName == null || TextUtils.isEmpty(componentName.getPackageName())) {
            return null;
        }
        Map<String, RedirectRule> rules = (Map) this.ruleMap.get(callingPackage);
        if (rules == null) {
            return null;
        }
        return (RedirectRule) rules.get(componentName.getPackageName());
    }
}
