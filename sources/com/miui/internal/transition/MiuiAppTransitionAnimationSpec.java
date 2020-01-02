package com.miui.internal.transition;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MiuiAppTransitionAnimationSpec implements Parcelable {
    public static final Creator<MiuiAppTransitionAnimationSpec> CREATOR = new Creator<MiuiAppTransitionAnimationSpec>() {
        public MiuiAppTransitionAnimationSpec createFromParcel(Parcel in) {
            return new MiuiAppTransitionAnimationSpec(in);
        }

        public MiuiAppTransitionAnimationSpec[] newArray(int size) {
            return new MiuiAppTransitionAnimationSpec[size];
        }
    };
    public final Bitmap mBitmap;
    public final Rect mRect;

    public MiuiAppTransitionAnimationSpec(Bitmap bitmap, Rect rect) {
        this.mBitmap = bitmap;
        this.mRect = rect;
    }

    public MiuiAppTransitionAnimationSpec(Parcel in) {
        this.mBitmap = (Bitmap) in.readParcelable(null);
        this.mRect = (Rect) in.readParcelable(null);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mBitmap, flags);
        dest.writeParcelable(this.mRect, flags);
    }

    public int describeContents() {
        return 0;
    }
}
