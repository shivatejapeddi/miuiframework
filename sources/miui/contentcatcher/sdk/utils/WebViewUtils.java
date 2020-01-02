package miui.contentcatcher.sdk.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WebViewUtils {
    private static final int COMPARISON_CHAR_INDEX_FROM_TAIL = 5;
    private static final boolean DBG = false;
    private static final int MATCHER_ARRAY_SIZE = 50;
    private static final String TAG = "WebViewUtils";
    public static final String WEBVIEW_NAME_ANDROID = "android.webkit.WebView";
    public static final String WEBVIEW_NAME_BAIDU = "com.baidu.webkit.sdk.WebView";
    public static final String WEBVIEW_NAME_BAIDU1 = "com.baidu.blink.WebView";
    public static final String WEBVIEW_NAME_MIUI = "com.miui.webkit.WebView";
    public static final String WEBVIEW_NAME_SOGOU = "sogou.webkit.WebView";
    public static final String WEBVIEW_NAME_TENCENT = "com.tencent.smtt.webkit.WebView";
    public static final String WEBVIEW_NAME_TENCENT2 = "com.tencent.mm.ui.widget.MMWebView";
    public static final String WEBVIEW_NAME_UC = "com.uc.webview.export.WebView";
    private static final WebViewClassMatcher[] mWebViewClassMatcherArray = new WebViewClassMatcher[50];

    static class NativeWebViewUtils {
        NativeWebViewUtils() {
        }

        public static void setJavaScriptEnabled(View view, boolean enabled) {
            if (view != null) {
                try {
                    if (view instanceof WebView) {
                        WebSettings setting = ((WebView) view).getSettings();
                        if (setting != null) {
                            setting.setJavaScriptEnabled(enabled);
                        }
                    }
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("e:");
                    stringBuilder.append(e);
                    Log.w(WebViewUtils.TAG, stringBuilder.toString());
                }
            }
        }

        public static void addJavascriptInterface(View view, Object object, String name) {
            if (view != null && object != null && name != null) {
                try {
                    if (view instanceof WebView) {
                        ((WebView) view).addJavascriptInterface(object, name);
                    }
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("e:");
                    stringBuilder.append(e);
                    Log.w(WebViewUtils.TAG, stringBuilder.toString());
                }
            }
        }
    }

    static class ThirdWebViewUtils {
        ThirdWebViewUtils() {
        }

        public static void setJavaScriptEnabled(View view, boolean enabled) {
            StringBuilder stringBuilder;
            String str = "e:";
            String str2 = WebViewUtils.TAG;
            try {
                Method method = view.getClass().getMethod("getSettings", new Class[0]);
                if (method != null) {
                    method.setAccessible(true);
                    Object settingObj = method.invoke(view, new Object[0]);
                    if (settingObj != null) {
                        Method m2 = settingObj.getClass().getMethod("setJavaScriptEnabled", new Class[]{Boolean.TYPE});
                        if (m2 != null) {
                            m2.setAccessible(true);
                            m2.invoke(settingObj, new Object[]{Boolean.valueOf(enabled)});
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e);
                Log.e(str2, stringBuilder.toString());
            } catch (IllegalAccessException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e2);
                Log.e(str2, stringBuilder.toString());
            } catch (InvocationTargetException e3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e3);
                Log.e(str2, stringBuilder.toString());
                Throwable t = e3.getTargetException();
                stringBuilder = new StringBuilder();
                stringBuilder.append("InvocationTargetException-target:");
                stringBuilder.append(t);
                Log.e(str2, stringBuilder.toString());
            } catch (Exception e4) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e4);
                Log.e(str2, stringBuilder.toString());
            }
        }

        public static void addJavascriptInterface(View view, Object object, String name) {
            StringBuilder stringBuilder;
            String str = "e:";
            String str2 = WebViewUtils.TAG;
            try {
                Method method = view.getClass().getMethod("addJavascriptInterface", new Class[]{Object.class, String.class});
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(view, new Object[]{object, name});
                }
            } catch (NoSuchMethodException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e);
                Log.e(str2, stringBuilder.toString());
            } catch (IllegalAccessException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e2);
                Log.e(str2, stringBuilder.toString());
            } catch (InvocationTargetException e3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e3);
                Log.e(str2, stringBuilder.toString());
                Throwable t = e3.getTargetException();
                stringBuilder = new StringBuilder();
                stringBuilder.append("InvocationTargetException-target:");
                stringBuilder.append(t);
                Log.e(str2, stringBuilder.toString());
            } catch (Exception e4) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(e4);
                Log.e(str2, stringBuilder.toString());
            }
        }
    }

    private static class WebViewClassMatcher {
        public List<String> mMatchList;

        private WebViewClassMatcher() {
            this.mMatchList = new ArrayList();
        }

        private synchronized void addToMatchList(String className) {
            if (!this.mMatchList.contains(className)) {
                this.mMatchList.add(className);
            }
        }

        private synchronized void removeFromMatchList(String className) {
            this.mMatchList.remove(className);
        }
    }

    static {
        mWebViewClassMatcherArray[20] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[20].mMatchList.add(WEBVIEW_NAME_SOGOU);
        mWebViewClassMatcherArray[22] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[22].mMatchList.add(WEBVIEW_NAME_ANDROID);
        mWebViewClassMatcherArray[23] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[23].mMatchList.add(WEBVIEW_NAME_MIUI);
        mWebViewClassMatcherArray[23].mMatchList.add(WEBVIEW_NAME_BAIDU1);
        mWebViewClassMatcherArray[28] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[28].mMatchList.add(WEBVIEW_NAME_BAIDU);
        mWebViewClassMatcherArray[29] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[29].mMatchList.add(WEBVIEW_NAME_UC);
        mWebViewClassMatcherArray[31] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[31].mMatchList.add(WEBVIEW_NAME_TENCENT);
        mWebViewClassMatcherArray[34] = new WebViewClassMatcher();
        mWebViewClassMatcherArray[34].mMatchList.add(WEBVIEW_NAME_TENCENT2);
    }

    private static boolean foundFromWebViewClassMatcher(String className, WebViewClassMatcher[] matcherArray) {
        int length = className.length();
        if (length >= 50) {
            return false;
        }
        WebViewClassMatcher matcher = matcherArray[length];
        if (matcher == null) {
            return false;
        }
        int comparisonIndex = length - 5;
        for (String matcherClassName : matcher.mMatchList) {
            if (className.charAt(0) == matcherClassName.charAt(0) && className.charAt(comparisonIndex) == matcherClassName.charAt(comparisonIndex) && className.equals(matcherClassName)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:20:0x0031, code skipped:
            return;
     */
    public static synchronized void addToWebViewClassMatcher(java.lang.String r5) {
        /*
        r0 = miui.contentcatcher.sdk.utils.WebViewUtils.class;
        monitor-enter(r0);
        r1 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0032 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);
        return;
    L_0x000b:
        r1 = r5.length();	 Catch:{ all -> 0x0032 }
        r2 = 50;
        if (r1 >= r2) goto L_0x0030;
    L_0x0013:
        r2 = 5;
        if (r1 > r2) goto L_0x0017;
    L_0x0016:
        goto L_0x0030;
    L_0x0017:
        r2 = mWebViewClassMatcherArray;	 Catch:{ all -> 0x0032 }
        r2 = r2[r1];	 Catch:{ all -> 0x0032 }
        if (r2 != 0) goto L_0x0027;
    L_0x001d:
        r2 = mWebViewClassMatcherArray;	 Catch:{ all -> 0x0032 }
        r3 = new miui.contentcatcher.sdk.utils.WebViewUtils$WebViewClassMatcher;	 Catch:{ all -> 0x0032 }
        r4 = 0;
        r3.<init>();	 Catch:{ all -> 0x0032 }
        r2[r1] = r3;	 Catch:{ all -> 0x0032 }
    L_0x0027:
        r2 = mWebViewClassMatcherArray;	 Catch:{ all -> 0x0032 }
        r2 = r2[r1];	 Catch:{ all -> 0x0032 }
        r2.addToMatchList(r5);	 Catch:{ all -> 0x0032 }
        monitor-exit(r0);
        return;
    L_0x0030:
        monitor-exit(r0);
        return;
    L_0x0032:
        r5 = move-exception;
        monitor-exit(r0);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.contentcatcher.sdk.utils.WebViewUtils.addToWebViewClassMatcher(java.lang.String):void");
    }

    /* JADX WARNING: Missing block: B:17:0x0025, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:19:0x0027, code skipped:
            return;
     */
    public static synchronized void removeFromWebViewClassMatcher(java.lang.String r3) {
        /*
        r0 = miui.contentcatcher.sdk.utils.WebViewUtils.class;
        monitor-enter(r0);
        r1 = android.text.TextUtils.isEmpty(r3);	 Catch:{ all -> 0x0028 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);
        return;
    L_0x000b:
        r1 = r3.length();	 Catch:{ all -> 0x0028 }
        r2 = 50;
        if (r1 >= r2) goto L_0x0026;
    L_0x0013:
        r2 = 5;
        if (r1 > r2) goto L_0x0017;
    L_0x0016:
        goto L_0x0026;
    L_0x0017:
        r2 = mWebViewClassMatcherArray;	 Catch:{ all -> 0x0028 }
        r2 = r2[r1];	 Catch:{ all -> 0x0028 }
        if (r2 == 0) goto L_0x0024;
    L_0x001d:
        r2 = mWebViewClassMatcherArray;	 Catch:{ all -> 0x0028 }
        r2 = r2[r1];	 Catch:{ all -> 0x0028 }
        r2.removeFromMatchList(r3);	 Catch:{ all -> 0x0028 }
    L_0x0024:
        monitor-exit(r0);
        return;
    L_0x0026:
        monitor-exit(r0);
        return;
    L_0x0028:
        r3 = move-exception;
        monitor-exit(r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.contentcatcher.sdk.utils.WebViewUtils.removeFromWebViewClassMatcher(java.lang.String):void");
    }

    public static synchronized boolean isWebView(View view) {
        synchronized (WebViewUtils.class) {
            if (view == null) {
                return false;
            }
            for (Class<?> clazz = view.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                if (foundFromWebViewClassMatcher(clazz.getName(), mWebViewClassMatcherArray)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean checkViewMatchedByClassName(View view, String className) {
        if (view == null || TextUtils.isEmpty(className)) {
            return true;
        }
        for (Class<?> clazz = view.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            if (className.equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSystemView(String className) {
        if (TextUtils.isEmpty(className) || (!className.startsWith("android.view") && !className.startsWith("android.widget"))) {
            return false;
        }
        return true;
    }

    public static void initWebViewJsInterface(View view, Object object, String name) {
        if (view instanceof WebView) {
            NativeWebViewUtils.addJavascriptInterface(view, object, name);
        } else {
            ThirdWebViewUtils.addJavascriptInterface(view, object, name);
        }
    }
}
