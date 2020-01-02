package miui.contentcatcher;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.annotations.GuardedBy;
import com.miui.internal.contentcatcher.IInterceptor;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miui.contentcatcher.sdk.WebViewDetector;
import miui.contentcatcher.sdk.utils.WebViewUtils;
import miui.os.Build;
import miui.process.IMiuiApplicationThread;
import miui.process.MiuiApplicationThread;
import miui.process.ProcessManager;
import miui.util.ObjectReference;
import miui.util.ReflectionUtils;

public class InterceptorProxy implements IInterceptor, IInterceptorContainer {
    static final boolean DBG = SystemProperties.getBoolean("interceptor.debug.on", false);
    static final boolean INTERCEPTOR_ENABLED = SystemProperties.getBoolean("interceptor.enabled", true);
    private static final String TAG = "InterceptorProxy";
    private static IMiuiApplicationThread mMiuiApplicationThread = null;
    private static final ArrayList<String> sBlackList = new ArrayList();
    private static final ArrayList<String> sSpecialContexts = new ArrayList();
    private static Handler sUiHandler = new Handler(Looper.getMainLooper());
    @GuardedBy({"InterceptorProxy.class"})
    private static volatile Handler sWorkHandler;
    @GuardedBy({"InterceptorProxy.class"})
    private static volatile HandlerThread sWorkerThread;
    private Handler mHandler;
    private IInterceptor mInterceptor;

    private static class H extends Handler {
        public static final int ACTIVITY_CREATE = 1;
        public static final int ACTIVITY_DESTROY = 6;
        public static final int ACTIVITY_PAUSE = 4;
        public static final int ACTIVITY_RESUME = 3;
        public static final int ACTIVITY_START = 2;
        public static final int ACTIVITY_STOP = 5;
        public static final int CONTENT_CHANGED = 7;
        public static final int CREATE_INJECTOR = 0;
        private WeakReference<Activity> mActivityRef;
        private WeakReference<IInterceptorContainer> mInterceptorProxyRef;

        public H(Looper looper, Activity activity, IInterceptorContainer interceptorProxy) {
            super(looper);
            this.mActivityRef = new WeakReference(activity);
            this.mInterceptorProxyRef = new WeakReference(interceptorProxy);
        }

        public void handleMessage(Message msg) {
            WeakReference weakReference = this.mInterceptorProxyRef;
            if (weakReference != null) {
                IInterceptorContainer iInterceptorContainer = (IInterceptorContainer) weakReference.get();
                IInterceptorContainer interceptorProxy = iInterceptorContainer;
                if (iInterceptorContainer != null) {
                    switch (msg.what) {
                        case 0:
                            WeakReference weakReference2 = this.mActivityRef;
                            if (weakReference2 != null) {
                                Activity activity = (Activity) weakReference2.get();
                                Activity activity2 = activity;
                                if (activity != null) {
                                    interceptorProxy.setInterceptor(InterceptorFactory.createInterceptor(activity2));
                                    break;
                                }
                            }
                            break;
                        case 1:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyActivityCreate();
                                break;
                            }
                            break;
                        case 2:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyActivityStart();
                                break;
                            }
                            break;
                        case 3:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyActivityResume();
                                break;
                            }
                            break;
                        case 4:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyActivityPause();
                                break;
                            }
                            break;
                        case 5:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyActivityStop();
                                break;
                            }
                            break;
                        case 6:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyActivityDestroy();
                                break;
                            }
                            break;
                        case 7:
                            if (interceptorProxy.getInterceptor() != null) {
                                interceptorProxy.getInterceptor().notifyContentChange();
                                break;
                            }
                            break;
                    }
                }
            }
        }
    }

    static {
        sBlackList.add("com.miui.home.launcher.Launcher");
        sBlackList.add("com.android.settings.FallbackHome");
        sBlackList.add("com.android.settings.CryptKeeper");
        sBlackList.add("com.miui.gallery.activity.HomePageActivity");
        sSpecialContexts.add("com.tencent.tbs.common.resources.PluginContextWrapper");
    }

    public static InterceptorProxy create(Activity activity) {
        addMiuiApplication();
        if (!INTERCEPTOR_ENABLED || Build.IS_INTERNATIONAL_BUILD || activity.getComponentName() == null || sBlackList.contains(activity.getComponentName().getClassName())) {
            return null;
        }
        Thread workThread = getWorkThread();
        if (workThread != null && workThread.isAlive()) {
            return new InterceptorProxy(activity);
        }
        Log.e(TAG, "Failed to create InterceptorProxy!");
        return null;
    }

    public static void addMiuiApplication() {
        if (mMiuiApplicationThread == null) {
            mMiuiApplicationThread = new MiuiApplicationThread();
            ProcessManager.addMiuiApplicationThread(mMiuiApplicationThread);
        }
    }

    public static HandlerThread getWorkThread() {
        if (sWorkerThread == null) {
            synchronized (InterceptorProxy.class) {
                if (sWorkerThread == null) {
                    HandlerThread workerThread = new HandlerThread("Binder:interceptor", -4);
                    workerThread.start();
                    sWorkerThread = workerThread;
                }
            }
        }
        return sWorkerThread;
    }

    public static Handler getWorkHandler() {
        if (sWorkHandler == null) {
            synchronized (InterceptorProxy.class) {
                if (sWorkHandler == null) {
                    sWorkHandler = new Handler(getWorkThread().getLooper());
                }
            }
        }
        return sWorkHandler;
    }

    public static void postToWorkHandler(Runnable runnable) {
        postToWorkHandler(runnable, 0);
    }

    public static void postToWorkHandler(Runnable runnable, long delayMillis) {
        if (runnable != null) {
            try {
                Handler workHandler = getWorkHandler();
                if (workHandler != null) {
                    workHandler.postDelayed(runnable, delayMillis);
                }
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("postToWorkHandler: ");
                stringBuilder.append(e.toString());
                Log.w(TAG, stringBuilder.toString());
            }
        }
    }

    public static Activity getAttachedActivity(View view) {
        if (view == null) {
            return null;
        }
        Context viewContext = view.getContext();
        while (viewContext != null && !(viewContext instanceof Activity) && (viewContext instanceof ContextWrapper)) {
            Context preViewContext = viewContext;
            viewContext = ((ContextWrapper) viewContext).getBaseContext();
            if (preViewContext == viewContext) {
                if (sSpecialContexts.contains(viewContext.getClass().getName())) {
                    ObjectReference<Context> baseRef = ReflectionUtils.tryGetObjectField(viewContext, "mBase", Context.class);
                    Context baseContext = baseRef != null ? (Context) baseRef.get() : null;
                    if (!(baseContext == null || viewContext == baseContext)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Get New base context : ");
                        stringBuilder.append(baseContext);
                        stringBuilder.append(" and Cur base context is:");
                        stringBuilder.append(viewContext.getClass().getName());
                        Log.i(TAG, stringBuilder.toString());
                        viewContext = baseContext;
                    }
                }
                return null;
            }
        }
        if (viewContext == null || !(viewContext instanceof Activity)) {
            return null;
        }
        return (Activity) viewContext;
    }

    public Handler getUiHandler() {
        return sUiHandler;
    }

    public static void postToUiHandler(Runnable runnable, Looper looper) {
        postToUiHandler(runnable, 0, looper);
    }

    public static void postToUiHandler(Runnable runnable, long delayMillis, Looper looper) {
        if (runnable != null) {
            Handler handler = sUiHandler;
            if (looper != null) {
                handler = new Handler(looper);
            }
            handler.postDelayed(runnable, delayMillis);
        }
    }

    public static boolean checkAndInitWebView(View view) {
        return checkAndInitWebView(view, null);
    }

    public static boolean checkAndInitWebView(final View view, Looper looper) {
        try {
            if (WebViewUtils.isWebView(view)) {
                postToUiHandler(new Runnable() {
                    public void run() {
                        try {
                            WebViewUtils.initWebViewJsInterface(view, WebViewDetector.getInstance(), WebViewDetector.DETECTOR_NAME_IN_JS);
                        } catch (Exception e) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("checkAndInitWebView-Runnable:");
                            stringBuilder.append(e);
                            Log.e("ContentCatcher", stringBuilder.toString());
                        }
                    }
                }, looper);
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkAndInitWebView-Exception: ");
            stringBuilder.append(e);
            Log.e("ContentCatcher", stringBuilder.toString());
        }
        return false;
    }

    private InterceptorProxy(Activity activity) {
        if (DBG) {
            Log.d(TAG, "InterceptorProxy create");
        }
        this.mHandler = new H(getWorkThread().getLooper(), activity, this);
        this.mHandler.sendEmptyMessage(0);
    }

    public void notifyActivityCreate() {
        if (DBG) {
            Log.d(TAG, "notifyActivityCreate");
        }
        this.mHandler.sendEmptyMessage(1);
    }

    public void notifyActivityStart() {
        if (DBG) {
            Log.d(TAG, "notifyActivityStart");
        }
        this.mHandler.sendEmptyMessage(2);
    }

    public void notifyActivityResume() {
        if (DBG) {
            Log.d(TAG, "notifyActivityResume");
        }
        this.mHandler.sendEmptyMessage(3);
    }

    public void notifyActivityPause() {
        if (DBG) {
            Log.d(TAG, "notifyActivityPause");
        }
        this.mHandler.sendEmptyMessage(4);
    }

    public void notifyActivityStop() {
        if (DBG) {
            Log.d(TAG, "notifyActivityStop");
        }
        this.mHandler.sendEmptyMessage(5);
    }

    public void notifyActivityDestroy() {
        if (DBG) {
            Log.d(TAG, "notifyActivityDestroy");
        }
        this.mHandler.sendEmptyMessage(6);
    }

    public void notifyContentChange() {
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyContentChange();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event, View rootView, Activity attachedActivity) {
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            return iInterceptor.dispatchTouchEvent(event, rootView, attachedActivity);
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event, View rootView, Activity attachedActivity) {
        if (this.mInterceptor == null) {
            return false;
        }
        if (DBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("dispatchKeyEvent event ");
            stringBuilder.append(event);
            stringBuilder.append(" rootView ");
            stringBuilder.append(rootView);
            Log.d(TAG, stringBuilder.toString());
        }
        return this.mInterceptor.dispatchKeyEvent(event, rootView, attachedActivity);
    }

    public void setWebView(final View view, final boolean isLoad) {
        if (DBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setWebView ");
            stringBuilder.append(view);
            Log.d(TAG, stringBuilder.toString());
        }
        sUiHandler.post(new Runnable() {
            public void run() {
                if (isLoad) {
                    WebViewUtils.initWebViewJsInterface(view, WebViewDetector.getInstance(), WebViewDetector.DETECTOR_NAME_IN_JS);
                }
                if (InterceptorProxy.this.mInterceptor != null) {
                    InterceptorProxy.this.mInterceptor.setWebView(view, isLoad);
                }
            }
        });
    }

    public void notifyWebView(View view, boolean isLoad) {
        if (DBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("notifyWebView ");
            stringBuilder.append(view);
            Log.d(TAG, stringBuilder.toString());
        }
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyWebView(view, isLoad);
        }
    }

    public void setInterceptor(IInterceptor interceptor) {
        this.mInterceptor = interceptor;
    }

    public IInterceptor getInterceptor() {
        return this.mInterceptor;
    }
}
