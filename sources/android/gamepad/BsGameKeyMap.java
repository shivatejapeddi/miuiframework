package android.gamepad;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BsGameKeyMap implements Parcelable {
    public static final Creator<BsGameKeyMap> CREATOR = new Creator<BsGameKeyMap>() {
        public BsGameKeyMap createFromParcel(Parcel in) {
            return new BsGameKeyMap(in);
        }

        public BsGameKeyMap[] newArray(int size) {
            return new BsGameKeyMap[size];
        }
    };
    private float centerX;
    private float centerY;
    private float radius;

    protected BsGameKeyMap(Parcel in) {
        this.centerX = in.readFloat();
        this.centerY = in.readFloat();
        this.radius = in.readFloat();
    }

    public BsGameKeyMap(float x, float y, float radius) {
        this.centerX = x;
        this.centerY = y;
        this.radius = radius;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public float getRadius() {
        return this.radius;
    }

    public String toString() {
        return String.format("Pos {x = %f, y = %f, r = %f}", new Object[]{Float.valueOf(this.centerX), Float.valueOf(this.centerY), Float.valueOf(this.radius)});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.centerX);
        dest.writeFloat(this.centerY);
        dest.writeFloat(this.radius);
    }
}
