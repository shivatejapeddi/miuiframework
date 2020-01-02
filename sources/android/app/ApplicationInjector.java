package android.app;

import miui.contentcatcher.InterceptorProxy;
import miui.process.IMiuiApplicationThread;

public class ApplicationInjector {
    private static IMiuiApplicationThread mMiuiApplicationThread = null;

    static void onCreate(Application application) {
        InterceptorProxy.addMiuiApplication();
    }
}
