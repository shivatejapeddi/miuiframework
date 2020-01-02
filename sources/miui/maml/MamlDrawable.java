package miui.maml;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.LayerDrawable;
import java.lang.ref.WeakReference;

public class MamlDrawable extends Drawable {
    private static WeakReference<Drawable> sLayerBadgeDrawableBmpRef;
    protected Drawable mBadgeDrawable;
    protected Rect mBadgeLocation;
    protected int mHeight;
    protected int mIntrinsicHeight;
    protected int mIntrinsicWidth;
    protected Runnable mInvalidateSelf = new Runnable() {
        public void run() {
            MamlDrawable.this.invalidateSelf();
        }
    };
    protected MamlDrawableState mState;
    protected int mWidth;

    public static class MamlDrawableState extends ConstantState {
        protected Drawable mStateBadgeDrawable;
        protected Rect mStateBadgeLocation;

        public Drawable newDrawable() {
            MamlDrawable ret = createDrawable();
            if (ret == null) {
                return null;
            }
            Drawable badgeDrawable = null;
            Rect badgeLocation = null;
            Drawable drawable = this.mStateBadgeDrawable;
            if (drawable != null) {
                badgeDrawable = drawable.mutate();
            }
            Rect rect = this.mStateBadgeLocation;
            if (rect != null) {
                badgeLocation = new Rect(rect.left, this.mStateBadgeLocation.top, this.mStateBadgeLocation.right, this.mStateBadgeLocation.bottom);
            }
            ret.setBadgeInfo(badgeDrawable, badgeLocation);
            return ret;
        }

        /* Access modifiers changed, original: protected */
        public MamlDrawable createDrawable() {
            return null;
        }

        public int getChangingConfigurations() {
            return 0;
        }
    }

    public void setBadgeInfo(Drawable badgeDrawable, Rect badgeLocation) {
        if (badgeLocation == null || (badgeLocation.left >= 0 && badgeLocation.top >= 0 && badgeLocation.width() <= this.mIntrinsicWidth && badgeLocation.height() <= this.mIntrinsicHeight)) {
            if (badgeDrawable instanceof LayerDrawable) {
                Drawable d = null;
                WeakReference weakReference = sLayerBadgeDrawableBmpRef;
                if (weakReference != null) {
                    d = (Drawable) weakReference.get();
                }
                if (d != null) {
                    badgeDrawable = d.mutate();
                } else {
                    Bitmap bitmap = Bitmap.createBitmap(badgeDrawable.getIntrinsicWidth(), badgeDrawable.getIntrinsicHeight(), Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    badgeDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    badgeDrawable.draw(canvas);
                    badgeDrawable = new BitmapDrawable(bitmap);
                    sLayerBadgeDrawableBmpRef = new WeakReference(badgeDrawable);
                }
            }
            this.mBadgeDrawable = badgeDrawable;
            this.mBadgeLocation = badgeLocation;
            MamlDrawableState mamlDrawableState = this.mState;
            mamlDrawableState.mStateBadgeDrawable = badgeDrawable;
            mamlDrawableState.mStateBadgeLocation = badgeLocation;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Badge location ");
        stringBuilder.append(badgeLocation);
        stringBuilder.append(" not in badged drawable bounds ");
        stringBuilder.append(new Rect(0, 0, this.mIntrinsicWidth, this.mIntrinsicHeight));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setIntrinsicSize(int width, int height) {
        this.mIntrinsicWidth = width;
        this.mIntrinsicHeight = height;
    }

    public void cleanUp() {
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.mWidth = right - left;
        this.mHeight = bottom - top;
    }

    public int getIntrinsicWidth() {
        return this.mIntrinsicWidth;
    }

    public int getIntrinsicHeight() {
        return this.mIntrinsicHeight;
    }

    public void draw(Canvas canvas) {
        drawIcon(canvas);
        try {
            if (this.mBadgeDrawable == null) {
                return;
            }
            if (this.mBadgeLocation != null) {
                this.mBadgeDrawable.setBounds(0, 0, this.mBadgeLocation.width(), this.mBadgeLocation.height());
                canvas.save();
                canvas.translate((float) this.mBadgeLocation.left, (float) this.mBadgeLocation.top);
                this.mBadgeDrawable.draw(canvas);
                canvas.restore();
                return;
            }
            this.mBadgeDrawable.setBounds(0, 0, this.mIntrinsicWidth, this.mIntrinsicHeight);
            this.mBadgeDrawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawIcon(Canvas canvas) {
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter cf) {
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        cleanUp();
        super.finalize();
    }

    public ConstantState getConstantState() {
        return this.mState;
    }
}
