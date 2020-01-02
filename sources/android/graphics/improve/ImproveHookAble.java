package android.graphics.improve;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.improve.SuperResolution.ImproveInfo;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;

public interface ImproveHookAble {
    Bitmap getBitmap(String str, Drawable drawable, String str2) throws Exception;

    void setBitmap(ImproveInfo improveInfo, ImageView imageView) throws IllegalAccessException, InvocationTargetException;
}
