package android.text.style;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;

public class BulletSpan implements LeadingMarginSpan, ParcelableSpan {
    private static final int STANDARD_BULLET_RADIUS = 4;
    private static final int STANDARD_COLOR = 0;
    public static final int STANDARD_GAP_WIDTH = 2;
    private final int mBulletRadius;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final int mColor;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final int mGapWidth;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final boolean mWantColor;

    public BulletSpan() {
        this(2, 0, false, 4);
    }

    public BulletSpan(int gapWidth) {
        this(gapWidth, 0, false, 4);
    }

    public BulletSpan(int gapWidth, int color) {
        this(gapWidth, color, true, 4);
    }

    public BulletSpan(int gapWidth, int color, int bulletRadius) {
        this(gapWidth, color, true, bulletRadius);
    }

    private BulletSpan(int gapWidth, int color, boolean wantColor, int bulletRadius) {
        this.mGapWidth = gapWidth;
        this.mBulletRadius = bulletRadius;
        this.mColor = color;
        this.mWantColor = wantColor;
    }

    public BulletSpan(Parcel src) {
        this.mGapWidth = src.readInt();
        this.mWantColor = src.readInt() != 0;
        this.mColor = src.readInt();
        this.mBulletRadius = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return 8;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(this.mGapWidth);
        dest.writeInt(this.mWantColor);
        dest.writeInt(this.mColor);
        dest.writeInt(this.mBulletRadius);
    }

    public int getLeadingMargin(boolean first) {
        return (this.mBulletRadius * 2) + this.mGapWidth;
    }

    public int getGapWidth() {
        return this.mGapWidth;
    }

    public int getBulletRadius() {
        return this.mBulletRadius;
    }

    public int getColor() {
        return this.mColor;
    }

    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint paint2 = paint;
        int i = start;
        Layout layout2 = layout;
        int bottom2;
        Canvas canvas2;
        if (((Spanned) text).getSpanStart(this) == i) {
            Style style = paint.getStyle();
            int oldcolor = 0;
            if (this.mWantColor) {
                oldcolor = paint.getColor();
                paint.setColor(this.mColor);
            }
            paint.setStyle(Style.FILL);
            if (layout2 != null) {
                bottom2 = bottom - layout2.getLineExtra(layout2.getLineForOffset(i));
            } else {
                bottom2 = bottom;
            }
            float yPosition = ((float) (top + bottom2)) / 2.0f;
            int i2 = this.mBulletRadius;
            canvas2 = canvas;
            canvas.drawCircle((float) ((dir * i2) + x), yPosition, (float) i2, paint);
            if (this.mWantColor) {
                paint.setColor(oldcolor);
            }
            paint.setStyle(style);
            return;
        }
        canvas2 = canvas;
        bottom2 = bottom;
    }
}
