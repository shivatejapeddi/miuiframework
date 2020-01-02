package miui.process;

import android.os.BaseLooper;
import android.os.Looper;
import android.util.Log;
import miui.process.IMiuiApplicationThread.Stub;
import miui.util.LongScreenshotUtils.ContentPort;
import miui.util.ReflectionUtils;

public class MiuiApplicationThread extends Stub {
    private static final String TAG = "MiuiApplicationThread";
    private ContentPort mContentPort;

    public boolean longScreenshot(int cmd, int navBarHeight) {
        if (this.mContentPort == null) {
            this.mContentPort = new ContentPort();
        }
        return this.mContentPort.longScreenshot(cmd, navBarHeight);
    }

    public String dumpMessage() {
        String result = "";
        try {
            return (String) ReflectionUtils.findMethodExact(BaseLooper.class, "dumpAll", String.class).invoke(Looper.getMainLooper(), new Object[]{""});
        } catch (Exception e) {
            Log.e(TAG, "error in dumpMessage.", e);
            return result;
        }
    }
}
