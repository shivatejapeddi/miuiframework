package android.graphics.improve;

import android.content.Context;
import android.graphics.Bitmap;
import java.lang.reflect.Method;

public class KingSoftBitmapImprove implements BitmapImproveAble {
    private static final String CLASS_AIKIT = "com.ksyun.ai.sr.ImageAIKit";
    private static final String CLASS_CONST = "com.ksyun.ai.sr.Constants";
    private int mType;
    private Method processImage;

    public boolean init(Context context, int model) {
        try {
            Class<?> consClazz = Class.forName(CLASS_CONST);
            Class<?> imageAIKitClazz = Class.forName(CLASS_AIKIT);
            this.mType = ((Integer) consClazz.getField("MODEL_TYPE_2X").get(null)).intValue();
            this.processImage = imageAIKitClazz.getMethod("processImage", new Class[]{Bitmap.class, Integer.TYPE});
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public Bitmap improveBitmap(Bitmap bitmap) {
        Bitmap result;
        synchronized (this) {
            try {
                result = (Bitmap) this.processImage.invoke(null, new Object[]{bitmap, Integer.valueOf(this.mType)});
                release();
            } catch (Throwable th) {
            }
        }
        return result;
    }

    public void release() {
    }
}
