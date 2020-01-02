package android.graphics.improve;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.improve.SuperResolution.ImproveInfo;
import android.widget.ImageView;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NewsImproveHook implements ImproveHookAble {
    private static final String ACTIVITY_THUMB = "ThumbPreviewActivity";
    private static final String DRAWABLE_FAST = "FastBitmapDrawable";
    private static final String VIEW_LARGE_ZOOM = "LargeZoomImageView";
    private Field mFBitmap;
    private Method mMGetbitmap;

    public NewsImproveHook(Context context) throws Exception {
        Class<?> clazz = context.getClassLoader().loadClass("com.ss.android.common.imagezoom.graphics.FastBitmapDrawable");
        this.mMGetbitmap = clazz.getDeclaredMethod("getBitmap", new Class[0]);
        this.mMGetbitmap.setAccessible(true);
        this.mFBitmap = clazz.getDeclaredField("mBitmap");
        this.mFBitmap.setAccessible(true);
    }

    public Bitmap getBitmap(String className, Drawable drawable, String imgName) throws InvocationTargetException, IllegalAccessException {
        if (ACTIVITY_THUMB.equals(className) && VIEW_LARGE_ZOOM.equals(imgName)) {
            if (DRAWABLE_FAST.equals(drawable.getClass().getSimpleName())) {
                return (Bitmap) this.mMGetbitmap.invoke(drawable, new Object[0]);
            }
        }
        return null;
    }

    public void setBitmap(ImproveInfo info, ImageView imageView) throws IllegalAccessException {
        if (ACTIVITY_THUMB.equals(info.clsName)) {
            this.mFBitmap.set(info.drawable, info.newBitmap);
            imageView.setImageDrawable(null);
            imageView.setImageDrawable(info.drawable);
        }
    }
}
