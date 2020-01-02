package android.text.style;

import android.graphics.Paint.FontMetricsInt;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import com.android.internal.util.Preconditions;

public interface LineHeightSpan extends ParagraphStyle, WrapTogetherSpan {

    public static class Standard implements LineHeightSpan, ParcelableSpan {
        private final int mHeight;

        public Standard(int height) {
            boolean z = height > 0;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Height:");
            stringBuilder.append(height);
            stringBuilder.append("must be positive");
            Preconditions.checkArgument(z, stringBuilder.toString());
            this.mHeight = height;
        }

        public Standard(Parcel src) {
            this.mHeight = src.readInt();
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getSpanTypeId() {
            return getSpanTypeIdInternal();
        }

        public int getSpanTypeIdInternal() {
            return 28;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            writeToParcelInternal(dest, flags);
        }

        public void writeToParcelInternal(Parcel dest, int flags) {
            dest.writeInt(this.mHeight);
        }

        public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int lineHeight, FontMetricsInt fm) {
            int originHeight = fm.descent - fm.ascent;
            if (originHeight > 0) {
                fm.descent = Math.round(((float) fm.descent) * ((((float) this.mHeight) * 1.0f) / ((float) originHeight)));
                fm.ascent = fm.descent - this.mHeight;
            }
        }
    }

    public interface WithDensity extends LineHeightSpan {
        void chooseHeight(CharSequence charSequence, int i, int i2, int i3, int i4, FontMetricsInt fontMetricsInt, TextPaint textPaint);
    }

    void chooseHeight(CharSequence charSequence, int i, int i2, int i3, int i4, FontMetricsInt fontMetricsInt);
}
