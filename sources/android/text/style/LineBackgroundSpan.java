package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.ParcelableSpan;

public interface LineBackgroundSpan extends ParagraphStyle {

    public static class Standard implements LineBackgroundSpan, ParcelableSpan {
        private final int mColor;

        public Standard(int color) {
            this.mColor = color;
        }

        public Standard(Parcel src) {
            this.mColor = src.readInt();
        }

        public int getSpanTypeId() {
            return getSpanTypeIdInternal();
        }

        public int getSpanTypeIdInternal() {
            return 27;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            writeToParcelInternal(dest, flags);
        }

        public void writeToParcelInternal(Parcel dest, int flags) {
            dest.writeInt(this.mColor);
        }

        public final int getColor() {
            return this.mColor;
        }

        public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lineNumber) {
            Paint paint2 = paint;
            int originColor = paint.getColor();
            paint.setColor(this.mColor);
            canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, paint);
            paint.setColor(originColor);
        }
    }

    void drawBackground(Canvas canvas, Paint paint, int i, int i2, int i3, int i4, int i5, CharSequence charSequence, int i6, int i7, int i8);
}
