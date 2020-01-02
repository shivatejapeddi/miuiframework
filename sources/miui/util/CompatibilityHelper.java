package miui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IWindowManager.Stub;
import android.view.SurfaceControl;

public class CompatibilityHelper {
    public static Bitmap screenshot(int width, int height, int minLayer, int maxLayer) {
        Bitmap hardwareBmp = SurfaceControl.screenshot(new Rect(), width, height, false, 0);
        if (hardwareBmp == null) {
            return hardwareBmp;
        }
        Bitmap tmpBmp = hardwareBmp.copy(Config.ARGB_8888, true);
        hardwareBmp.recycle();
        return tmpBmp;
    }

    public static Bitmap screenshot(int width, int height) {
        Bitmap hardwareBmp = SurfaceControl.screenshot(new Rect(), width, height, 0);
        if (hardwareBmp == null) {
            return hardwareBmp;
        }
        Bitmap tmpBmp = hardwareBmp.copy(Config.ARGB_8888, true);
        hardwareBmp.recycle();
        return tmpBmp;
    }

    public static boolean hasNavigationBar(int displayId) throws RemoteException {
        return Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE)).hasNavigationBar(displayId);
    }
}
