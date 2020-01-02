package com.android.internal.os;

import android.app.LoadedApk;
import android.app.ZygotePreload;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.net.LocalSocket;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;

class AppZygoteInit {
    public static final String TAG = "AppZygoteInit";
    private static ZygoteServer sServer;

    private static class AppZygoteConnection extends ZygoteConnection {
        AppZygoteConnection(LocalSocket socket, String abiList) throws IOException {
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
            String str = AppZygoteInit.TAG;
            Log.i(str, stringBuilder2);
            ClassLoader loader = new LoadedApk(null, appInfo, null, null, false, true, false).getClassLoader();
            Zygote.allowAppFilesAcrossFork(appInfo);
            int i = 1;
            if (appInfo.zygotePreloadName != null) {
                try {
                    ComponentName preloadName = ComponentName.createRelative(appInfo.packageName, appInfo.zygotePreloadName);
                    Class<?> cl = Class.forName(preloadName.getClassName(), true, loader);
                    if (ZygotePreload.class.isAssignableFrom(cl)) {
                        ((ZygotePreload) cl.getConstructor(new Class[0]).newInstance(new Object[0])).doPreload(appInfo);
                    } else {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(preloadName.getClassName());
                        stringBuilder3.append(" does not implement ");
                        stringBuilder3.append(ZygotePreload.class.getName());
                        Log.e(str, stringBuilder3.toString());
                    }
                } catch (ReflectiveOperationException e) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("AppZygote application preload failed for ");
                    stringBuilder4.append(appInfo.zygotePreloadName);
                    Log.e(str, stringBuilder4.toString(), e);
                }
            } else {
                Log.i(str, "No zygotePreloadName attribute specified.");
            }
            try {
                DataOutputStream socketOut = getSocketOutputStream();
                if (loader == null) {
                    i = 0;
                }
                socketOut.writeInt(i);
                Log.i(str, "Application preload done");
            } catch (IOException e2) {
                throw new IllegalStateException("Error writing to command socket", e2);
            }
        }
    }

    private static class AppZygoteServer extends ZygoteServer {
        private AppZygoteServer() {
        }

        /* Access modifiers changed, original: protected */
        public ZygoteConnection createNewConnection(LocalSocket socket, String abiList) throws IOException {
            return new AppZygoteConnection(socket, abiList);
        }
    }

    AppZygoteInit() {
    }

    public static void main(String[] argv) {
        ChildZygoteInit.runZygoteServer(new AppZygoteServer(), argv);
    }
}
