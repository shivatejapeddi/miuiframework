package miui.maml.elements;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.Variables;
import miui.maml.util.TextFormatter;
import miui.net.ConnectivityHelper;
import org.w3c.dom.Element;

public class WebViewScreenElement extends AnimatedScreenElement {
    private static final String LOG_TAG = "MAML WebViewScreenElement";
    public static final String TAG_NAME = "WebView";
    private static final int USE_NETWORK_ALL = 2;
    private static final int USE_NETWORK_WIFI = 1;
    private boolean mCachePage;
    private String mCurUrl;
    private Handler mHandler;
    private LayoutParams mLayoutParams;
    private TextFormatter mUriFormatter;
    private int mUseNetwork = 2;
    private Expression mUseNetworkExp;
    private boolean mViewAdded;
    private WebView mWebView;
    private Context mWindowContext;

    private class MamlInterface {
        private MamlInterface() {
        }

        /* synthetic */ MamlInterface(WebViewScreenElement x0, AnonymousClass1 x1) {
            this();
        }

        @JavascriptInterface
        public void putObj(String name, Object value) {
            WebViewScreenElement.this.getVariables().put(name, value);
        }

        @JavascriptInterface
        public void putString(String name, String value) {
            WebViewScreenElement.this.getVariables().put(name, (Object) value);
        }

        @JavascriptInterface
        public void putDouble(String name, double value) {
            WebViewScreenElement.this.getVariables().put(name, value);
        }

        @JavascriptInterface
        public void putInt(String name, int value) {
            WebViewScreenElement.this.getVariables().put(name, (double) value);
        }

        @JavascriptInterface
        public int registerDoubleVariable(String name) {
            return WebViewScreenElement.this.getVariables().registerDoubleVariable(name);
        }

        @JavascriptInterface
        public int registerVariable(String name) {
            return WebViewScreenElement.this.getVariables().registerVariable(name);
        }

        @JavascriptInterface
        public double getDouble(int index) {
            return WebViewScreenElement.this.getVariables().getDouble(index);
        }

        @JavascriptInterface
        public double getDouble(String name) {
            return WebViewScreenElement.this.getVariables().getDouble(name);
        }

        @JavascriptInterface
        public String getString(int index) {
            return WebViewScreenElement.this.getVariables().getString(index);
        }

        @JavascriptInterface
        public String getString(String name) {
            return WebViewScreenElement.this.getVariables().getString(name);
        }

        @JavascriptInterface
        public Object getObj(String name) {
            return WebViewScreenElement.this.getVariables().get(name);
        }

        @JavascriptInterface
        public Object getObj(int index) {
            return WebViewScreenElement.this.getVariables().get(index);
        }

        @JavascriptInterface
        public void doAction(String action) {
            WebViewScreenElement.this.performAction(action);
        }
    }

    public WebViewScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mWindowContext = root.getContext().mContext;
        this.mWebView = new WebView(this.mWindowContext);
        this.mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        this.mWebView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                WebViewScreenElement.this.mRoot.onUIInteractive(WebViewScreenElement.this, "touch");
                return false;
            }
        });
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setInitialScale(100);
        String ua = node.getAttribute("userAgent");
        if (!TextUtils.isEmpty(ua)) {
            this.mWebView.getSettings().setUserAgentString(ua);
        }
        this.mWebView.addJavascriptInterface(new MamlInterface(this, null), "maml");
        this.mLayoutParams = new LayoutParams(-1, -1);
        this.mHandler = getContext().getHandler();
        Variables vars = getVariables();
        this.mUriFormatter = new TextFormatter(vars, node.getAttribute("uri"), Expression.build(vars, node.getAttribute("uriExp")));
        this.mCachePage = Boolean.parseBoolean(node.getAttribute("cachePage"));
        String useNetwork = node.getAttribute("useNetwork");
        if (TextUtils.isEmpty(useNetwork) || "all".equalsIgnoreCase(useNetwork)) {
            this.mUseNetwork = 2;
        } else if ("wifi".equalsIgnoreCase(useNetwork)) {
            this.mUseNetwork = 1;
        } else {
            this.mUseNetworkExp = Expression.build(vars, useNetwork);
        }
    }

    public void init() {
        super.init();
        Expression expression = this.mUseNetworkExp;
        if (expression != null) {
            this.mUseNetwork = (int) expression.evaluate();
        }
        if (this.mRoot.getViewManager() != null) {
            initWebView();
        } else {
            Log.e(LOG_TAG, "ViewManager must be set before init");
        }
    }

    public void finish() {
        super.finish();
        finishWebView();
        if (!this.mCachePage) {
            this.mCurUrl = null;
        }
    }

    public void render(Canvas c) {
    }

    public void loadUrl(final String url) {
        if (canUseNetwork() || !url.startsWith(IntentFilter.SCHEME_HTTP)) {
            this.mCurUrl = url;
            this.mHandler.post(new Runnable() {
                public void run() {
                    WebViewScreenElement.this.mWebView.loadUrl(url);
                }
            });
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("loadUrl canceled due to useNetwork setting.");
        stringBuilder.append(url);
        Log.d(LOG_TAG, stringBuilder.toString());
    }

    public void runjs(final String jsfun) {
        this.mHandler.post(new Runnable() {
            public void run() {
                WebView access$100 = WebViewScreenElement.this.mWebView;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("javascript:");
                stringBuilder.append(jsfun);
                access$100.loadUrl(stringBuilder.toString());
            }
        });
    }

    public void reload() {
        this.mHandler.post(new Runnable() {
            public void run() {
                WebViewScreenElement.this.mWebView.reload();
            }
        });
    }

    public void goBack() {
        this.mHandler.post(new Runnable() {
            public void run() {
                WebViewScreenElement.this.mWebView.goBack();
            }
        });
    }

    private boolean canUseNetwork() {
        int i = this.mUseNetwork;
        if (i == 2) {
            return true;
        }
        if (i == 1 && ConnectivityHelper.getInstance().isWifiConnected()) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        String url = this.mUriFormatter.getText();
        if (!(TextUtils.isEmpty(url) || TextUtils.equals(this.mCurUrl, url))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("loadUrl: ");
            stringBuilder.append(url);
            Log.d(LOG_TAG, stringBuilder.toString());
            loadUrl(url);
        }
        updateView();
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        super.onVisibilityChange(visible);
        final boolean _v = visible;
        this.mHandler.post(new Runnable() {
            public void run() {
                WebViewScreenElement.this.mWebView.setVisibility(_v ? 0 : 4);
            }
        });
    }

    public void pause() {
        super.pause();
        if (this.mViewAdded) {
            pauseWebView(true);
        }
    }

    public void resume() {
        super.resume();
        if (this.mViewAdded) {
            pauseWebView(false);
        }
    }

    private void pauseWebView(final boolean pause) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (pause) {
                    WebViewScreenElement.this.mWebView.onPause();
                } else {
                    WebViewScreenElement.this.mWebView.onResume();
                }
            }
        });
    }

    private final void initWebView() {
        if (!this.mViewAdded || this.mCachePage) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (!WebViewScreenElement.this.mViewAdded) {
                        WebViewScreenElement webViewScreenElement = WebViewScreenElement.this;
                        webViewScreenElement.updateLayoutParams(webViewScreenElement.mLayoutParams);
                        Log.d(WebViewScreenElement.LOG_TAG, "addWebView");
                        WebViewScreenElement.this.mRoot.getViewManager().addView(WebViewScreenElement.this.mWebView, WebViewScreenElement.this.mLayoutParams);
                        WebViewScreenElement.this.mViewAdded = true;
                    } else if (WebViewScreenElement.this.mCachePage) {
                        WebViewScreenElement.this.mWebView.onResume();
                    }
                }
            });
        }
    }

    private final void finishWebView() {
        this.mHandler.post(new Runnable() {
            public void run() {
                WebViewScreenElement.this.mRoot.getViewManager().removeView(WebViewScreenElement.this.mWebView);
                WebViewScreenElement.this.mViewAdded = false;
                if (WebViewScreenElement.this.mCachePage) {
                    WebViewScreenElement.this.mWebView.onPause();
                } else {
                    WebViewScreenElement.this.mWebView.loadUrl("about:blank");
                }
            }
        });
    }

    private final void updateView() {
        if (this.mViewAdded) {
            this.mWebView.setX(getAbsoluteLeft());
            this.mWebView.setY(getAbsoluteTop());
            if (updateLayoutParams(this.mLayoutParams)) {
                this.mWebView.setLayoutParams(this.mLayoutParams);
            }
        }
    }

    private boolean updateLayoutParams(LayoutParams lp) {
        boolean changed = false;
        int width = (int) getWidth();
        if (lp.width != width) {
            lp.width = width;
            changed = true;
        }
        int height = (int) getHeight();
        if (lp.height == height) {
            return changed;
        }
        lp.height = height;
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
    }
}
