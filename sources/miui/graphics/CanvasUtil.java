package miui.graphics;

import android.graphics.Canvas;

@Deprecated
public class CanvasUtil {
    public static void release(Canvas canvas) {
        canvas.release();
    }
}
