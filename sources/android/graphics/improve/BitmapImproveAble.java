package android.graphics.improve;

import android.content.Context;
import android.graphics.Bitmap;

public interface BitmapImproveAble {
    Bitmap improveBitmap(Bitmap bitmap);

    boolean init(Context context, int i);

    void release();
}
