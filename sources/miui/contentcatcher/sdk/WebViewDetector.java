package miui.contentcatcher.sdk;

import android.webkit.JavascriptInterface;
import java.lang.ref.WeakReference;

public class WebViewDetector {
    public static final String DETECTOR_NAME_IN_JS = "MiWebViewDetector";
    public static final String TAG = "WebViewDetector";
    private static volatile WebViewDetector sDetector;
    private Callback mCallback;
    private WeakReference<Callback> mCallbackRef;

    public interface Callback {
        void onWebContentCatched(String str);
    }

    public static WebViewDetector getInstance() {
        if (sDetector == null) {
            synchronized (WebViewDetector.class) {
                if (sDetector == null) {
                    sDetector = new WebViewDetector();
                }
            }
        }
        return sDetector;
    }

    public void setActiveCallback(Callback callback) {
        this.mCallbackRef = new WeakReference(callback);
    }

    public void setCustomDetector(WebViewDetector detector) {
        synchronized (WebViewDetector.class) {
            sDetector = detector;
        }
    }

    @JavascriptInterface
    public void onCatch(String content) {
        WeakReference weakReference = this.mCallbackRef;
        if (weakReference != null) {
            Callback callback = (Callback) weakReference.get();
            if (callback != null) {
                callback.onWebContentCatched(content);
            }
        }
    }
}
