package com.android.internal.os;

import android.app.ApplicationLoaders;
import android.app.LoadedApk;
import android.content.pm.ApplicationInfo;
import android.net.LocalSocket;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewFactory;
import android.webkit.WebViewFactoryProvider;
import android.webkit.WebViewLibraryLoader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

class WebViewZygoteInit {
    public static final String TAG = "WebViewZygoteInit";

    private static class WebViewZygoteConnection extends ZygoteConnection {
        WebViewZygoteConnection(LocalSocket socket, String abiList) throws IOException {
            super(socket, abiList);
        }

        /* Access modifiers changed, original: protected */
        public void preload() {
        }

        /* Access modifiers changed, original: protected */
        public boolean isPreloadComplete() {
            return true;
        }

        /* Access modifiers changed, original: protected */
        public boolean canPreloadApp() {
            return true;
        }

        /* Access modifiers changed, original: protected */
        public void handlePreloadApp(ApplicationInfo appInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Beginning application preload for ");
            stringBuilder.append(appInfo.packageName);
            String stringBuilder2 = stringBuilder.toString();
            String str = WebViewZygoteInit.TAG;
            Log.i(str, stringBuilder2);
            doPreload(new LoadedApk(null, appInfo, null, null, false, true, false).getClassLoader(), WebViewFactory.getWebViewLibrary(appInfo));
            Zygote.allowAppFilesAcrossFork(appInfo);
            Log.i(str, "Application preload done");
        }

        /* Access modifiers changed, original: protected */
        public void handlePreloadPackage(String packagePath, String libsPath, String libFileName, String cacheKey) {
            String str = WebViewZygoteInit.TAG;
            Log.i(str, "Beginning package preload");
            ClassLoader loader = ApplicationLoaders.getDefault().createAndCacheWebViewClassLoader(packagePath, libsPath, cacheKey);
            for (String packageEntry : TextUtils.split(packagePath, File.pathSeparator)) {
                Zygote.nativeAllowFileAcrossFork(packageEntry);
            }
            doPreload(loader, libFileName);
            Log.i(str, "Package preload done");
        }

        private void doPreload(ClassLoader loader, String libFileName) {
            String str = "preloadInZygote";
            String str2 = WebViewZygoteInit.TAG;
            WebViewLibraryLoader.loadNativeLibrary(loader, libFileName);
            boolean preloadSucceeded = false;
            int i = 1;
            try {
                Class<WebViewFactoryProvider> providerClass = WebViewFactory.getWebViewProviderClass(loader);
                Method preloadInZygote = providerClass.getMethod(str, new Class[0]);
                preloadInZygote.setAccessible(true);
                if (preloadInZygote.getReturnType() != Boolean.TYPE) {
                    Log.e(str2, "Unexpected return type: preloadInZygote must return boolean");
                } else {
                    preloadSucceeded = ((Boolean) providerClass.getMethod(str, new Class[0]).invoke(null, new Object[0])).booleanValue();
                    if (!preloadSucceeded) {
                        Log.e(str2, "preloadInZygote returned false");
                    }
                }
            } catch (ReflectiveOperationException e) {
                Log.e(str2, "Exception while preloading package", e);
            }
            try {
                DataOutputStream socketOut = getSocketOutputStream();
                if (!preloadSucceeded) {
                    i = 0;
                }
                socketOut.writeInt(i);
            } catch (IOException ioe) {
                throw new IllegalStateException("Error writing to command socket", ioe);
            }
        }
    }

    private static class WebViewZygoteServer extends ZygoteServer {
        private WebViewZygoteServer() {
        }

        /* Access modifiers changed, original: protected */
        public ZygoteConnection createNewConnection(LocalSocket socket, String abiList) throws IOException {
            return new WebViewZygoteConnection(socket, abiList);
        }
    }

    WebViewZygoteInit() {
    }

    public static void main(String[] argv) {
        Log.i(TAG, "Starting WebViewZygoteInit");
        ChildZygoteInit.runZygoteServer(new WebViewZygoteServer(), argv);
    }
}
