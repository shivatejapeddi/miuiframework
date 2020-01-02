package android.graphics.improve;

import android.content.Context;
import android.graphics.Bitmap;
import java.lang.reflect.Method;

public class SelfBitmapImprove implements BitmapImproveAble {
    private static final String CLASS_MODELS = "com.xiaomi.sr.models.MaceSRModel";
    private int mType;
    private Method processImage;

    public boolean init(Context context, int model) {
        try {
            Class<?> imageAIKitClazz = Class.forName(CLASS_MODELS);
            this.mType = model;
            this.processImage = imageAIKitClazz.getMethod("processImage", new Class[]{Bitmap.class, Integer.TYPE});
            return true;
        } catch (Exception e) {
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
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    release();
                    return null;
                } catch (Throwable th) {
                    release();
                }
            }
        }
        return result;
    }

    public void release() {
    }
}
