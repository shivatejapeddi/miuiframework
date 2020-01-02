package android.graphics.improve;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.improve.SuperResolution.ImproveInfo;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.lang.reflect.InvocationTargetException;

public class MMImproveHook implements ImproveHookAble {
    private static final String ACTIVITY_BROWSEUI = "SnsBrowseUI";
    private static final String IMAGE_VIEW_MULTI_TOUCH = "MultiTouchImageView";

    class MMDrawable extends BitmapDrawable {
        private final Matrix mNewMatrix = new Matrix();
        private RectF mNewRect;
        private RectF mOldRect;
        private final RectF mTemp = new RectF();

        public MMDrawable(ImproveInfo info, Resources resources) {
            super(resources, info.newBitmap);
            this.mOldRect = new RectF(0.0f, 0.0f, (float) info.oldBitmap.getWidth(), (float) info.oldBitmap.getHeight());
            this.mNewRect = new RectF(0.0f, 0.0f, (float) info.newBitmap.getWidth(), (float) info.newBitmap.getHeight());
        }

        public void draw(Canvas canvas) {
            this.mNewMatrix.reset();
            canvas.getMatrix().mapRect(this.mTemp, this.mOldRect);
            this.mNewMatrix.setRectToRect(this.mNewRect, this.mTemp, ScaleToFit.FILL);
            canvas.setMatrix(this.mNewMatrix);
            super.draw(canvas);
        }
    }

    public MMImproveHook(Context context) {
    }

    public Bitmap getBitmap(String className, Drawable drawable, String imgName) {
        if (!ACTIVITY_BROWSEUI.equals(className) || !IMAGE_VIEW_MULTI_TOUCH.equals(imgName)) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        throw new RuntimeException("bitmap not bitmap drawable");
    }

    public void setBitmap(ImproveInfo info, ImageView imageView) throws IllegalAccessException, InvocationTargetException {
        if (ACTIVITY_BROWSEUI.equals(info.clsName) && (info.drawable instanceof BitmapDrawable)) {
            if (imageView.getScaleType() != ScaleType.MATRIX) {
                SRReporter.reportFailure(imageView.getContext(), "set bitmap error");
                return;
            }
            imageView.setImageDrawable(new MMDrawable(info, imageView.getResources()));
        }
    }
}
