package android.text.style;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class SuggestionRangeSpan extends CharacterStyle implements ParcelableSpan {
    private int mBackgroundColor;

    @UnsupportedAppUsage
    public SuggestionRangeSpan() {
        this.mBackgroundColor = 0;
    }

    @UnsupportedAppUsage
    public SuggestionRangeSpan(Parcel src) {
        this.mBackgroundColor = src.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(this.mBackgroundColor);
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return 21;
    }

    @UnsupportedAppUsage
    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public void updateDrawState(TextPaint tp) {
        tp.bgColor = this.mBackgroundColor;
    }
}
