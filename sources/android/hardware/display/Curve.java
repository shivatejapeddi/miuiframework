package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Curve implements Parcelable {
    public static final Creator<Curve> CREATOR = new Creator<Curve>() {
        public Curve createFromParcel(Parcel in) {
            return new Curve(in.createFloatArray(), in.createFloatArray());
        }

        public Curve[] newArray(int size) {
            return new Curve[size];
        }
    };
    private final float[] mX;
    private final float[] mY;

    public Curve(float[] x, float[] y) {
        this.mX = x;
        this.mY = y;
    }

    public float[] getX() {
        return this.mX;
    }

    public float[] getY() {
        return this.mY;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeFloatArray(this.mX);
        out.writeFloatArray(this.mY);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int size = this.mX.length;
        for (int i = 0; i < size; i++) {
            String str = ", ";
            if (i != 0) {
                sb.append(str);
            }
            sb.append("(");
            sb.append(this.mX[i]);
            sb.append(str);
            sb.append(this.mY[i]);
            sb.append(")");
        }
        sb.append("]");
        return sb.toString();
    }
}
