package android.webkit;

import android.content.Context;
import org.egret.plugin.mi.runtime.EgretLoader;

class WebViewInjector {
    WebViewInjector() {
    }

    static void initEgretLoader(WebView wv, Context context) {
        EgretLoader egret = new EgretLoader(context);
        if (!egret.checkEgretContext()) {
            wv.addJavascriptInterface(egret, "GameEngine");
        }
    }
}
