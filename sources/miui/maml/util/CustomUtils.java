package miui.maml.util;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.MediaStore;
import miui.os.Build;

public class CustomUtils {
    private CustomUtils() {
    }

    public static void replaceCameraIntentInfoOnF3M(String packageName, String className, Intent intent) {
        if ("vela".equals(Build.DEVICE) && intent != null && "com.android.camera".equals(packageName) && "com.android.camera.Camera".equals(className)) {
            intent.setComponent(new ComponentName("com.mlab.cam", "com.mtlab.camera.CameraActivity"));
            if (Intent.ACTION_MAIN.equals(intent.getAction())) {
                intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            }
        }
    }
}
